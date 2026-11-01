package com.yuki.yukihub;

import android.content.Intent;
import android.content.ComponentName;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

import com.yuki.yukihub.data.GameRepository;
import com.yuki.yukihub.model.Game;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import com.yuki.yukihub.bigscreen.NsfwBlur;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * YukiHub 启动首页。
 * 只负责首页展示与导航；完整游戏库功能继续由 MainActivity 承担。
 */
public class HomeActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "yukihub_prefs";
    private static final String KEY_PROFILE_NAME = "profile_name";
    private static final String KEY_PROFILE_AVATAR = "profile_avatar";
    private static final String KEY_AUTH_ACCESS_TOKEN = "auth_access_token";
    private static final String KEY_AUTH_NICKNAME = "auth_nickname";
    private static final String KEY_AUTH_AVATAR = "auth_avatar";
    private static final String KEY_APP_ICON = "app_icon"; // new=新图标(默认) / classic=经典图标
    private static final String APP_ICON_NEW = "new";
    private static final String APP_ICON_CLASSIC = "classic";
    private static final String ALIAS_ICON_NEW = "com.yuki.yukihub.YukiIcon";
    private static final String ALIAS_ICON_CLASSIC = "com.yuki.yukihub.YukiIconClassic";

    private GameRepository repository;
    private SharedPreferences prefs;
    private com.yuki.yukihub.social.PresenceManager presenceManager;
    private boolean homeHeartbeatHeld = false;
    private LinearLayout quickGames;
    private LinearLayout heroDots;
    private ImageView homeAvatar;
    private TextView homeAvatarInitial;
    private TextView homeGreeting;
    private View homeProfileStatusDot;
    private ImageView heroCover;
    private TextView heroTitle;
    private TextView heroSubtitle;
    private TextView playTime;
    private TextView gameCount;
    private TextView completedCount;
    private TextView playingCount;
    private ImageView homeNewsBanner;
    private TextView homeNewsTitle;
    private TextView homeNewsLoading;
    private LinearLayout homeNewsDots;
    private ImageView homeNewsRefresh;
    private final List<Game> carouselGames = new ArrayList<>();
    /** M18：首页当前这批游戏（NSFW 开关变化后重刷封面用） */
    private List<Game> lastHomeGames;
    private final Handler carouselHandler = new Handler(Looper.getMainLooper());
    private int carouselIndex = 0;
    private String heroImageRequest = "";
    private String avatarImageRequest = "";
    private final Runnable carouselRunnable = new Runnable() {
        @Override public void run() {
            if (carouselGames.size() > 1) {
                carouselIndex = (carouselIndex + 1) % carouselGames.size();
                showCarouselGame(carouselIndex, true);
                carouselHandler.postDelayed(this, 5000L);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyImmersive();
        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        syncAppIconState(); // 校验应用图标 prefs 与系统 alias 状态一致

        // 从 MainActivity 导航栏回来时强制显示首页
        boolean forceHome = getIntent().getBooleanExtra("force_home", false);

        // 启动页选择（非强制回家时生效）
        if (!forceHome) {
            String startupPage = prefs.getString("startup_page", "home");
            if ("library".equals(startupPage)) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return;
            }
            if ("bigscreen".equals(startupPage)) {
                // 开机直接进大屏模式（对应 spec §6.3 的 bigscreen_auto_enter）
                startActivity(new Intent(this, com.yuki.yukihub.bigscreen.BigScreenActivity.class));
                finish();
                return;
            }
        }

        setContentView(R.layout.activity_home);
        // M14：全 app 手柄适配（同 MainActivity）
        com.yuki.yukihub.ui.GamepadFocus.attach(this);
        // 聊天选图 launcher 必须在 STARTED 之前注册（供 FriendsChatDialog 借用）
        com.yuki.yukihub.social.ChatImagePicker.register(this);
        // 窗口背景铺成首页同款渐变，避免内容延伸/挖孔区域露出默认浅色背景（白边）
        try { getWindow().setBackgroundDrawableResource(R.drawable.bg_home_gradient); } catch (Throwable ignored) { }
        repository = new GameRepository(this);
        bindViews();
        bindActions();
        refreshHome();
        handleFriendsTargetIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleFriendsTargetIntent(intent);
    }

    private void handleFriendsTargetIntent(Intent intent) {
        if (intent == null) return;
        String target = intent.getStringExtra("home_target");
        if (!"friends".equals(target)) return;
        // 通知点击可能带上要直达的会话
        final String chatFriendId = intent.getStringExtra("chat_friend_id");
        final int chatGroupId = intent.getIntExtra("chat_group_id", 0);
        intent.removeExtra("home_target");
        intent.removeExtra("chat_friend_id");
        intent.removeExtra("chat_group_id");
        getWindow().getDecorView().post(() -> {
            if (isFinishing()) return;
            try {
                com.yuki.yukihub.social.FriendsChatDialog d =
                        new com.yuki.yukihub.social.FriendsChatDialog(this);
                if (chatFriendId != null && !chatFriendId.isEmpty()) {
                    d.showAndOpenFriendChat(chatFriendId);
                } else if (chatGroupId > 0) {
                    d.showAndOpenGroupChat(chatGroupId);
                } else {
                    d.show();
                }
            } catch (Throwable ignored) {}
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        applyImmersive();
        refreshProfileHeader();
        if (repository != null) {
            finishStalePlaySessionsIfAny();
            refreshHome();
        }
        // 启动在线心跳
        if (presenceManager == null) presenceManager = com.yuki.yukihub.social.PresenceManager.get(this);
        if (!homeHeartbeatHeld) {
            presenceManager.retainHeartbeat();
            homeHeartbeatHeld = true;
        }
        try { com.yuki.yukihub.social.PresenceService.sync(this); } catch (Throwable ignored) {}
    }

    @Override
    protected void onPause() {
        carouselHandler.removeCallbacks(carouselRunnable);
        newsCarouselHandler.removeCallbacks(newsCarouselRunnable);
        // 释放本页心跳持有；前台服务若在跑会继续保活
        if (presenceManager != null && homeHeartbeatHeld) {
            presenceManager.releaseHeartbeat();
            homeHeartbeatHeld = false;
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        carouselHandler.removeCallbacksAndMessages(null);
        newsCarouselHandler.removeCallbacksAndMessages(null);
        if (presenceManager != null && homeHeartbeatHeld) {
            presenceManager.releaseHeartbeat();
            homeHeartbeatHeld = false;
        }
        super.onDestroy();
    }

    private void bindViews() {
        quickGames = findViewById(R.id.homeQuickGames);
        heroDots = findViewById(R.id.homeHeroDots);
        homeAvatar = findViewById(R.id.homeAvatar);
        homeAvatarInitial = findViewById(R.id.homeAvatarInitial);
        homeGreeting = findViewById(R.id.homeGreeting);
        homeProfileStatusDot = findViewById(R.id.homeProfileStatusDot);
        heroCover = findViewById(R.id.homeHeroCover);
        heroTitle = findViewById(R.id.homeHeroTitle);
        heroSubtitle = findViewById(R.id.homeHeroSubtitle);
        playTime = findViewById(R.id.homePlayTime);
        gameCount = findViewById(R.id.homeGameCount);
        completedCount = findViewById(R.id.homeCompletedCount);
        playingCount = findViewById(R.id.homePlayingCount);
        homeNewsBanner = findViewById(R.id.homeNewsBanner);
        homeNewsTitle = findViewById(R.id.homeNewsTitle);
        homeNewsLoading = findViewById(R.id.homeNewsLoading);
        homeNewsDots = findViewById(R.id.homeNewsDots);
        homeNewsRefresh = findViewById(R.id.homeNewsRefresh);
        // 首页专用半透明紫色胶囊：不复用游戏库实色背景，图标与文字统一白色。
        TextView heroActionView = findViewById(R.id.homeHeroAction);
        if (heroActionView != null) {
            heroActionView.setBackgroundResource(R.drawable.bg_home_action_glass);
            heroActionView.setTextColor(Color.WHITE);
            com.yuki.yukihub.util.IconedText.set(heroActionView,
                    R.drawable.ic_btn_play, "Continue game", 10f, Color.WHITE);
        }

        // 题图区四角圆角裁切（画框式）。遮罩用无圆角版 bg_home_news_overlay：
        // 自带圆角的遮罩会在图片裁切角留下一小瓣未遮亮的月牙，直角版罩满裁切区
        android.view.View newsMedia = findViewById(R.id.homeNewsMedia);
        newsMedia.setClipToOutline(true);
        newsMedia.setOutlineProvider(new android.view.ViewOutlineProvider() {
            @Override
            public void getOutline(android.view.View view, android.graphics.Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), dp(12));
            }
        });
        setupNewsSwipe(newsMedia);

        refreshProfileHeader();
    }

    private void bindActions() {
        View.OnClickListener openLibrary = v -> {
            touch(v);
            startActivity(new Intent(this, MainActivity.class));
        };
        findViewById(R.id.homeNavLibrary).setOnClickListener(openLibrary);
        findViewById(R.id.homeHeroAction).setOnClickListener(v -> {
            touch(v);
            launchCurrentCarouselGame();
        });
        setupHeroSwipe(findViewById(R.id.homeHeroCard));

        findViewById(R.id.homeProfileEntry).setOnClickListener(v -> {
            touch(v);
            showProfileDialog();
        });
        findViewById(R.id.homeNavBigScreen).setOnClickListener(v -> {
            touch(v);
            startActivity(new Intent(this, com.yuki.yukihub.bigscreen.BigScreenActivity.class));
        });
        findViewById(R.id.homeNavChat).setOnClickListener(v -> {
            touch(v);
            new com.yuki.yukihub.social.FriendsChatDialog(this).show();
        });
        findViewById(R.id.homeNavSettings).setOnClickListener(v -> {
            touch(v);
            showSettingsDialog();
        });
        findViewById(R.id.homeNavTranslate).setOnClickListener(v -> {
            touch(v);
            startActivity(new Intent(this, com.yuki.yukihub.translate.TranslateControlActivity.class));
        });
        findViewById(R.id.homeGalToolbox).setOnClickListener(v -> {
            touch(v);
            startActivity(new Intent(this, com.yuki.yukihub.galtoolbox.GalToolboxActivity.class));
        });
        // M0：3D 展厅入口（点开后选择 离线个人展厅 / 在线多人展厅）
        findViewById(R.id.homeExhibition).setOnClickListener(v -> {
            touch(v);
            showExhibitionDialog();
        });
        findViewById(R.id.homeCommunity).setOnClickListener(v -> {
            touch(v);
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://yukihub.zh.kg/community")));
            } catch (Throwable t) {
                Toast.makeText(this, "Unable to open the community, please check the network", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /** M0：3D 展厅入口选择（离线个人展厅 / 在线多人展厅） */
    private void showExhibitionDialog() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        int pad = dp(16);
        root.setPadding(pad, dp(4), pad, dp(4));

        // 用自定义卡片而不是 setItems()：setItems 生成的是系统列表项，
        // 不受 styleDialogDark 控制，在深色主题下会变成黑字看不清（实测）。
        final androidx.appcompat.app.AlertDialog[] holder = new androidx.appcompat.app.AlertDialog[1];

        root.addView(buildExhibitionOption("Offline personal exhibition hall",
                "Local inventory · No internet connection required · Can be browsed offline",
                () -> {
                    if (holder[0] != null) holder[0].dismiss();
                    openExhibition(com.yuki.yukihub.exhibition.ExhibitionActivity.MODE_LOCAL);
                }));

        View gap = new View(this);
        gap.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dp(8)));
        root.addView(gap);

        root.addView(buildExhibitionOption("Online multi-person exhibition hall",
                "Site-wide museum · Internet connection required · Player communication square",
                () -> {
                    if (holder[0] != null) holder[0].dismiss();
                    openExhibition(com.yuki.yukihub.exhibition.ExhibitionActivity.MODE_ONLINE);
                }));
        View gap2 = new View(this);
        gap2.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dp(8)));
        root.addView(gap2);
        root.addView(buildExhibitionOption("🎵 Music library",
                "管理展厅音乐与 PV · 加专辑/单曲、绑 PV",
                () -> {
                    if (holder[0] != null) holder[0].dismiss();
                    openMusicLibrary();
                }));

        androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("3D exhibition hall")
                .setView(root)
                .setNegativeButton("Cancel", null)
                .show();
        holder[0] = dialog;
        styleDialogDark(dialog);
        dialog.setOnDismissListener(d -> applyImmersive());
    }

    /** 展厅入口卡片：显式指定颜色，避免深色主题下黑字看不清 */
    private View buildExhibitionOption(String title, String desc, Runnable onClick) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setBackgroundResource(R.drawable.bg_home_glass);
        card.setPadding(dp(14), dp(12), dp(14), dp(12));
        card.setClickable(true);
        card.setFocusable(true);
        card.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        TextView titleView = new TextView(this);
        titleView.setText(title);
        titleView.setTextColor(0xFFFFFFFF);
        titleView.setTextSize(15);
        titleView.setTypeface(null, android.graphics.Typeface.BOLD);
        card.addView(titleView);

        TextView descView = new TextView(this);
        descView.setText(desc);
        descView.setTextColor(0xB3FFFFFF);
        descView.setTextSize(11);
        descView.setPadding(0, dp(3), 0, 0);
        card.addView(descView);

        card.setOnClickListener(v -> {
            touch(v);
            onClick.run();
        });
        return card;
    }

    /** 打开展厅：MODE_LOCAL = 离线个人展厅，MODE_ONLINE = 在线多人展厅 */
    private void openExhibition(int mode) {
        try {
            Intent intent = new Intent(this, com.yuki.yukihub.exhibition.ExhibitionActivity.class);
            intent.putExtra(com.yuki.yukihub.exhibition.ExhibitionActivity.EXTRA_MODE, mode);
            startActivity(intent);
        } catch (Throwable t) {
            Toast.makeText(this, "Unable to open showroom:" + t.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /** 打开音乐库管理页（音乐厅数据录入的唯一入口） */
    private void openMusicLibrary() {
        try {
            startActivity(new Intent(this, com.yuki.yukihub.music.MusicLibraryActivity.class));
            overridePendingTransition(0, 0);
        } catch (Throwable t) {
            Toast.makeText(this, "Unable to open music library:" + t.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showProfileDialog() {
        final String currentName = displayProfileName();
        final String localName = prefs == null ? "Yuki" : prefs.getString(KEY_PROFILE_NAME, "Yuki");
        final String currentSignature = prefs == null ? "" : prefs.getString("profile_signature", "");

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundResource(R.drawable.bg_dialog);
        int pad = dp(16);
        root.setPadding(pad, dp(14), pad, dp(10));

        TextView nameLabel = new TextView(this);
        nameLabel.setText("Nick name");
        nameLabel.setTextColor(0xFFFFFFFF);
        nameLabel.setTextSize(14);
        nameLabel.setTypeface(null, android.graphics.Typeface.BOLD);
        root.addView(nameLabel);

        EditText nameInput = new EditText(this);
        nameInput.setText(localName);
        nameInput.setHint("Enter nickname");
        nameInput.setTextColor(0xFFFFFFFF);
        nameInput.setHintTextColor(0x88FFFFFF);
        nameInput.setBackgroundResource(R.drawable.bg_input);
        nameInput.setPadding(dp(10), 0, dp(10), 0);
        root.addView(nameInput, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp(42)));

        TextView uidLabel = new TextView(this);
        uidLabel.setText("UID");
        uidLabel.setTextColor(0xFF9AA4BF);
        uidLabel.setTextSize(12);
        uidLabel.setPadding(0, dp(6), 0, dp(2));
        root.addView(uidLabel);

        TextView uidValue = new TextView(this);
        String uid = prefs == null ? "" : prefs.getString("auth_uid", "");
        uidValue.setText(uid.isEmpty() ? "-" : uid);
        uidValue.setTextColor(0xFFFFFFFF);
        uidValue.setTextSize(14);
        uidValue.setTypeface(null, android.graphics.Typeface.BOLD);
        root.addView(uidValue, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp(36)));

        TextView signLabel = new TextView(this);
        signLabel.setText("Personal signature");
        signLabel.setTextColor(0xFFFFFFFF);
        signLabel.setTextSize(14);
        signLabel.setTypeface(null, android.graphics.Typeface.BOLD);
        signLabel.setPadding(0, dp(10), 0, dp(4));
        root.addView(signLabel);

        EditText signatureInput = new EditText(this);
        signatureInput.setText(currentSignature);
        signatureInput.setHint("Write something, for example: I have to finish a work seriously today.");
        signatureInput.setSingleLine(false);
        signatureInput.setMinLines(2);
        signatureInput.setTextColor(0xFFFFFFFF);
        signatureInput.setHintTextColor(0x88FFFFFF);
        signatureInput.setBackgroundResource(R.drawable.bg_input);
        signatureInput.setGravity(android.view.Gravity.TOP | android.view.Gravity.START);
        signatureInput.setPadding(dp(10), dp(6), dp(10), dp(6));
        root.addView(signatureInput, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp(62)));

        TextView hint = new TextView(this);
        hint.setText("The avatar and nickname are also displayed on the left side of the homepage. \\n Please enter the game library settings for complete information display, cloud synchronization, account management, etc.");
        hint.setTextColor(0xAAFFFFFF);
        hint.setTextSize(10);
        hint.setPadding(0, dp(8), 0, 0);
        root.addView(hint);

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(false);
        scroll.addView(root, new ScrollView.LayoutParams(ScrollView.LayoutParams.MATCH_PARENT, ScrollView.LayoutParams.WRAP_CONTENT));

        androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Personal data")
                .setView(scroll)
                .setPositiveButton("save", null)
                .setNegativeButton("closure", null)
                .show();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
            dialog.getWindow().setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.72f), (int) (getResources().getDisplayMetrics().heightPixels * 0.72f));
        }
        styleDialogDark(dialog);
        dialog.setOnDismissListener(d -> applyImmersive());
        dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String name = nameInput.getText() == null ? "" : nameInput.getText().toString().trim();
            String sign = signatureInput.getText() == null ? "" : signatureInput.getText().toString().trim();
            if (name.isEmpty()) {
                Toast.makeText(this, "Nickname cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            if (prefs != null) {
                prefs.edit().putString(KEY_PROFILE_NAME, name).putString("profile_signature", sign).apply();
            }
            refreshProfileHeader();
            Toast.makeText(this, "Profile saved", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
    }

    private void showSettingsDialog() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundResource(R.drawable.bg_dialog);
        int pad = dp(16);
        root.setPadding(pad, dp(14), pad, dp(10));

        // 字体大小
        TextView fontTitle = new TextView(this);
        fontTitle.setText("Overall font size");
        fontTitle.setTextColor(0xFFFFFFFF);
        fontTitle.setTextSize(14);
        fontTitle.setTypeface(null, android.graphics.Typeface.BOLD);
        root.addView(fontTitle);

        float savedFontScale = prefs == null ? 1.0f : prefs.getFloat("ui_font_scale", 1.0f);
        TextView fontInfo = new TextView(this);
        fontInfo.setText("Current:" + Math.round(savedFontScale * 100) + "% (default 100%)");
        fontInfo.setTextColor(0xAAFFFFFF);
        fontInfo.setTextSize(11);
        fontInfo.setPadding(0, dp(4), 0, dp(6));
        root.addView(fontInfo);

       SeekBar fontSeek = new SeekBar(this);
        fontSeek.setMax(60); // 70%-130%
        fontSeek.setProgress(Math.round((savedFontScale - 0.7f) * 100f));
        root.addView(fontSeek);
        final float[] fontScaleValue = {savedFontScale};
        fontSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float scale = 0.7f + progress / 100f;
                fontScaleValue[0] = scale;
                fontInfo.setText("Current:" + Math.round(scale * 100) + "% (default 100%)");
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        // 界面缩放
        TextView scaleTitle = new TextView(this);
        scaleTitle.setText("\\n Overall scaling of the interface");
        scaleTitle.setTextColor(0xFFFFFFFF);
        scaleTitle.setTextSize(14);
        scaleTitle.setTypeface(null, android.graphics.Typeface.BOLD);
        root.addView(scaleTitle);

        float savedUiScale = prefs == null ? 1.0f : prefs.getFloat("ui_scale", 1.0f);
        TextView scaleInfo = new TextView(this);
        scaleInfo.setText("Current:" + Math.round(savedUiScale * 100) + "% (default 100%) · Tablet recommended 120-150%");
        scaleInfo.setTextColor(0xAAFFFFFF);
        scaleInfo.setTextSize(11);
        scaleInfo.setPadding(0, dp(4), 0, dp(6));
        root.addView(scaleInfo);

        SeekBar uiScaleSeek = new SeekBar(this);
        uiScaleSeek.setMax(60); // 70%-130%
        uiScaleSeek.setProgress(Math.round((savedUiScale - 0.7f) * 100f));
        root.addView(uiScaleSeek);
        final float[] uiScaleValue = {savedUiScale};
        uiScaleSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float scale = 0.7f + progress / 100f;
                uiScaleValue[0] = scale;
                scaleInfo.setText("Current:" + Math.round(scale * 100) + "% (default 100%) · Tablet recommended 120-150%");
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        // 刘海/挖孔区域（主界面可选，默认开启）
        TextView cutoutTitle = new TextView(this);
        cutoutTitle.setText("Bangs/cutout area");
        cutoutTitle.setTextColor(0xFFFFFFFF);
        cutoutTitle.setTextSize(14);
        cutoutTitle.setTypeface(null, android.graphics.Typeface.BOLD);
        root.addView(cutoutTitle);
        android.widget.CheckBox cutoutCheck = new android.widget.CheckBox(this);
        cutoutCheck.setText("允许绘制到刘海/挖孔区域（游戏库/首页背景铺满）");
        cutoutCheck.setTextColor(0xFFFFFFFF);
        cutoutCheck.setTextSize(13);
        cutoutCheck.setPadding(dp(4), dp(4), 0, dp(4));
        cutoutCheck.setChecked(prefs != null && prefs.getBoolean(MainActivity.KEY_MAIN_DRAW_CUTOUT, true));
        root.addView(cutoutCheck);

        // NSFW 封面模糊
        android.widget.CheckBox nsfwBlurCheck = new android.widget.CheckBox(this);
        nsfwBlurCheck.setText("🔞 NSFW cover blur (game covers identified as R18 are automatically blurred)");
        nsfwBlurCheck.setTextColor(0xFFFFFFFF);
        nsfwBlurCheck.setTextSize(13);
        nsfwBlurCheck.setPadding(dp(4), dp(4), 0, dp(4));
        nsfwBlurCheck.setChecked(prefs != null && prefs.getBoolean(MainActivity.KEY_NSFW_BLUR, true));
        root.addView(nsfwBlurCheck);

        // 启动页选择
        TextView startupTitle = new TextView(this);
        startupTitle.setText("\\nStart page");
        startupTitle.setTextColor(0xFFFFFFFF);
        startupTitle.setTextSize(14);
        startupTitle.setTypeface(null, android.graphics.Typeface.BOLD);
        root.addView(startupTitle);

        String savedStartup = prefs == null ? "home" : prefs.getString("startup_page", "home");
        final String[] startupOptions = {"home", "library", "bigscreen"};
        String[] startupLabels = {"Home page (default)", "game library", "Large screen mode"};
        final int[] startupChoice = {0};
        for (int i = 0; i < startupOptions.length; i++) {
            if (startupOptions[i].equals(savedStartup)) { startupChoice[0] = i; break; }
        }

        android.widget.RadioGroup startupGroup = new android.widget.RadioGroup(this);
        startupGroup.setOrientation(android.widget.RadioGroup.VERTICAL);
        final android.widget.RadioButton[] startupRadios = new android.widget.RadioButton[startupOptions.length];
        for (int i = 0; i < startupOptions.length; i++) {
            startupRadios[i] = new android.widget.RadioButton(this);
            startupRadios[i].setText(startupLabels[i]);
            startupRadios[i].setTextColor(0xFFFFFFFF);
            startupRadios[i].setPadding(dp(4), dp(4), 0, dp(4));
            if (i == startupChoice[0]) startupRadios[i].setChecked(true);
            final int idx = i;
            startupRadios[i].setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    for (int j = 0; j < startupRadios.length; j++) {
                        if (j != idx) startupRadios[j].setChecked(false);
                    }
                    startupChoice[0] = idx;
                }
            });
            startupGroup.addView(startupRadios[i]);
        }
        root.addView(startupGroup);

        // 应用图标选择
        TextView appIconTitle = new TextView(this);
        appIconTitle.setText("\\n application icon");
        appIconTitle.setTextColor(0xFFFFFFFF);
        appIconTitle.setTextSize(14);
        appIconTitle.setTypeface(null, android.graphics.Typeface.BOLD);
        root.addView(appIconTitle);

        LinearLayout appIconRow = new LinearLayout(this);
        appIconRow.setOrientation(LinearLayout.HORIZONTAL);
        appIconRow.setGravity(android.view.Gravity.CENTER_VERTICAL);
        appIconRow.setPadding(0, dp(8), 0, dp(4));

        ImageView appIconPreview = new ImageView(this);
        appIconPreview.setImageResource(currentIconRes());
        appIconPreview.setScaleType(ImageView.ScaleType.FIT_CENTER);
        appIconRow.addView(appIconPreview, new LinearLayout.LayoutParams(dp(40), dp(40)));

        String savedIcon = prefs == null ? APP_ICON_NEW : prefs.getString(KEY_APP_ICON, APP_ICON_NEW);
        TextView appIconLabel = new TextView(this);
        appIconLabel.setText("Current:" + (APP_ICON_NEW.equals(savedIcon) ? "New icon" : "Classic icon"));
        appIconLabel.setTextColor(0xAAFFFFFF);
        appIconLabel.setTextSize(13);
        appIconLabel.setGravity(android.view.Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams labelLp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        labelLp.setMargins(dp(10), 0, 0, 0);
        appIconRow.addView(appIconLabel, labelLp);

        Button appIconBtn = new Button(this);
        appIconBtn.setText("Change");
        appIconBtn.setTextColor(0xFFFFFFFF);
        appIconBtn.setTextSize(12);
        android.graphics.drawable.GradientDrawable iconBtnBg = new android.graphics.drawable.GradientDrawable();
        iconBtnBg.setColor(0x3310183A);
        iconBtnBg.setStroke(dp(1), 0x554A6A9A);
        iconBtnBg.setCornerRadius(dp(8));
        appIconBtn.setBackground(iconBtnBg);
        appIconBtn.setOnClickListener(v -> showAppIconDialog());
        appIconRow.addView(appIconBtn, new LinearLayout.LayoutParams(dp(72), dp(34)));
        root.addView(appIconRow);

        // 高级设置入口
        TextView advancedHint = new TextView(this);
        advancedHint.setText("\\n For advanced settings such as scanning directory, engine configuration, background, WebDAV synchronization, account management, etc., please enter the game library settings page.");
        advancedHint.setTextColor(0xAAFFFFFF);
        advancedHint.setTextSize(10);
        advancedHint.setLineSpacing(dp(2), 1.0f);
        advancedHint.setPadding(0, dp(12), 0, dp(8));
        root.addView(advancedHint);

        Button openFullSettings = new Button(this);
        openFullSettings.setText("Open full settings →");
        openFullSettings.setTextColor(0xFFFFFFFF);
        openFullSettings.setBackgroundResource(R.drawable.bg_home_glass);
        openFullSettings.setOnClickListener(v -> {
            openMainTarget("settings");
        });
        root.addView(openFullSettings, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp(42)));

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(false);
        scroll.addView(root, new ScrollView.LayoutParams(ScrollView.LayoutParams.MATCH_PARENT, ScrollView.LayoutParams.WRAP_CONTENT));

        androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("set up")
                .setView(scroll)
                .setPositiveButton("save", null)
                .setNegativeButton("closure", null)
                .show();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
            dialog.getWindow().setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.72f), (int) (getResources().getDisplayMetrics().heightPixels * 0.72f));
        }
        styleDialogDark(dialog);
        dialog.setOnDismissListener(d -> applyImmersive());
        dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            if (prefs != null) {
                // 大屏模式已可进入（M0 脚手架 + 完整输入地基）
                String selectedStartup = startupOptions[startupChoice[0]];
                prefs.edit()
                        .putFloat("ui_font_scale", fontScaleValue[0])
                        .putFloat("ui_scale", uiScaleValue[0])
                        .putString("startup_page", selectedStartup)
                        .putBoolean(MainActivity.KEY_MAIN_DRAW_CUTOUT, cutoutCheck.isChecked())
                        .putBoolean(MainActivity.KEY_NSFW_BLUR, nsfwBlurCheck.isChecked())
                        .apply();
            }
            Toast.makeText(this, "Settings saved", Toast.LENGTH_SHORT).show();
            // M18：NSFW 模糊开关变了要**立刻重刷**首页封面（不然要回主库再进来才生效）
            refreshNsfwCovers();
            dialog.dismiss();
        });
    }

    // ========== 应用图标切换（activity-alias） ==========

    private int currentIconRes() {
        String id = prefs == null ? APP_ICON_NEW : prefs.getString(KEY_APP_ICON, APP_ICON_NEW);
        return APP_ICON_NEW.equals(id) ? R.mipmap.ic_launcher_new : R.mipmap.ic_launcher;
    }

    private void showAppIconDialog() {
        String current = prefs == null ? APP_ICON_NEW : prefs.getString(KEY_APP_ICON, APP_ICON_NEW);

        LinearLayout list = new LinearLayout(this);
        list.setOrientation(LinearLayout.VERTICAL);
        list.setBackgroundResource(R.drawable.bg_dialog);
        list.setPadding(dp(16), dp(8), dp(16), dp(8));

        androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Select app icon")
                .setView(list)
                .setNegativeButton("Cancel", null)
                .create();

        addAppIconOption(list, "New icon", R.mipmap.ic_launcher_new, APP_ICON_NEW, current, dialog);
        addAppIconOption(list, "Classic icon", R.mipmap.ic_launcher, APP_ICON_CLASSIC, current, dialog);

        dialog.show();
        // 窗口背景统一为深色，避免标题栏/按钮栏露出系统浅色
        if (dialog.getWindow() != null) {
            android.graphics.drawable.GradientDrawable winBg = new android.graphics.drawable.GradientDrawable();
            winBg.setColor(0xFF10172A);
            winBg.setCornerRadius(dp(14));
            dialog.getWindow().setBackgroundDrawable(winBg);
        }
    }

    private void addAppIconOption(LinearLayout list, String name, int iconRes, String id, String current, androidx.appcompat.app.AlertDialog dialog) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(android.view.Gravity.CENTER_VERTICAL);
        row.setPadding(dp(12), dp(10), dp(12), dp(10));
        row.setClickable(true);

        // 选中项高亮，非选中项轻微深色底
        boolean selected = id.equals(current);
        android.graphics.drawable.GradientDrawable rowBg = new android.graphics.drawable.GradientDrawable();
        rowBg.setColor(selected ? 0x334A6A9A : 0x11000000);
        if (selected) rowBg.setStroke(dp(1), 0x664A6A9A);
        rowBg.setCornerRadius(dp(8));
        row.setBackground(rowBg);
        LinearLayout.LayoutParams rowLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        rowLp.setMargins(0, dp(4), 0, dp(4));
        list.addView(row, rowLp);

        ImageView iv = new ImageView(this);
        iv.setImageResource(iconRes);
        iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
        row.addView(iv, new LinearLayout.LayoutParams(dp(48), dp(48)));

        TextView tv = new TextView(this);
        tv.setText(selected ? "✓ " + name + "(current)" : name);
        tv.setTextColor(selected ? 0xFFFFFFFF : 0xBFFFFFFF);
        tv.setTextSize(15);
        LinearLayout.LayoutParams tvLp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        tvLp.setMargins(dp(14), 0, 0, 0);
        row.addView(tv, tvLp);

        row.setOnClickListener(v -> {
            prefs.edit().putString(KEY_APP_ICON, id).apply();
            switchAppIcon(id);
            Toast.makeText(this, "The icon has been switched. Desktop icons may need to be refreshed later.", Toast.LENGTH_SHORT).show();
            if (dialog != null) dialog.dismiss();
        });
    }

    private void switchAppIcon(String id) {
        boolean useNew = APP_ICON_NEW.equals(id);
        try {
            PackageManager pm = getPackageManager();
            pm.setComponentEnabledSetting(
                    new ComponentName(this, ALIAS_ICON_NEW),
                    useNew ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED : PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP);
            pm.setComponentEnabledSetting(
                    new ComponentName(this, ALIAS_ICON_CLASSIC),
                    useNew ? PackageManager.COMPONENT_ENABLED_STATE_DISABLED : PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);
        } catch (Throwable t) {
            Log.w("YukiHub", "switch app icon failed", t);
            Toast.makeText(this, "Icon switching failed:" + (t.getMessage() == null ? "Please try again later" : t.getMessage()), Toast.LENGTH_LONG).show();
        }
    }

    /** 启动时校验 prefs 与系统组件状态一致，防止清除数据/重装后错乱 */
    private void syncAppIconState() {
        if (prefs == null) return;
        try {
            String saved = prefs.getString(KEY_APP_ICON, APP_ICON_NEW);
            PackageManager pm = getPackageManager();
            int st = pm.getComponentEnabledSetting(new ComponentName(this, ALIAS_ICON_NEW));
            boolean actualNew = (st == PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                    || st == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
            boolean wantNew = APP_ICON_NEW.equals(saved);
            if (actualNew != wantNew) {
                switchAppIcon(saved);
            }
        } catch (Throwable t) {
            Log.w("YukiHub", "sync app icon state failed", t);
        }
    }

    private void openMainTarget(String target) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("home_target", target);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    private void launchCurrentCarouselGame() {
        if (carouselIndex < 0 || carouselIndex >= carouselGames.size()) {
            Toast.makeText(this, "There are currently no games to launch", Toast.LENGTH_SHORT).show();
            return;
        }
        launchGameFromHome(carouselGames.get(carouselIndex));
    }

    private void launchGameFromHome(Game game) {
        if (game == null || game.id <= 0L) {
            Toast.makeText(this, "The game to launch cannot be recognized", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("home_target", "launch_game");
        intent.putExtra("home_game_id", game.id);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    private void setupHeroSwipe(View heroCard) {
        if (heroCard == null) return;
        heroCard.setClickable(true);
        heroCard.setOnClickListener(view -> {
            touch(view);
            launchCurrentCarouselGame();
        });
        final float[] downX = {0f};
        final float[] downY = {0f};
        final boolean[] moved = {false};
        heroCard.setOnTouchListener((view, event) -> {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    downX[0] = event.getX();
                    downY[0] = event.getY();
                    moved[0] = false;
                    carouselHandler.removeCallbacks(carouselRunnable);
                    return true;
                case MotionEvent.ACTION_MOVE:
                    float moveX = event.getX() - downX[0];
                    float moveY = event.getY() - downY[0];
                    if (Math.abs(moveX) > dp(8) || Math.abs(moveY) > dp(8)) moved[0] = true;
                    if (Math.abs(moveX) > Math.abs(moveY)) {
                        view.setTranslationX(Math.max(-dp(24), Math.min(dp(24), moveX * 0.12f)));
                    }
                    return true;
                case MotionEvent.ACTION_UP:
                    float deltaX = event.getX() - downX[0];
                    float deltaY = event.getY() - downY[0];
                    view.animate().translationX(0f).setDuration(140).start();
                    boolean horizontalSwipe = carouselGames.size() > 1
                            && Math.abs(deltaX) >= dp(42)
                            && Math.abs(deltaX) > Math.abs(deltaY) * 1.2f;
                    if (horizontalSwipe) {
                        switchCarousel(deltaX < 0 ? 1 : -1);
                        try { view.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK); } catch (Throwable ignored) { }
                    } else if (!moved[0] && Math.abs(deltaX) < dp(12) && Math.abs(deltaY) < dp(12)) {
                        view.performClick();
                    }
                    restartCarouselTimer();
                    return true;
                case MotionEvent.ACTION_CANCEL:
                    view.animate().translationX(0f).setDuration(140).start();
                    restartCarouselTimer();
                    return true;
                default:
                    return true;
            }
        });
    }

    private void switchCarousel(int direction) {
        if (carouselGames.size() <= 1) return;
        carouselIndex = (carouselIndex + direction + carouselGames.size()) % carouselGames.size();
        showCarouselGame(carouselIndex, true);
    }

    /** 资讯轮播手势：与 setupHeroSwipe 同一套手感（视差/慢滑/触觉反馈/按下暂停），单击改为打开详情。 */
    private void setupNewsSwipe(View newsCard) {
        if (newsCard == null) return;
        newsCard.setClickable(true);
        newsCard.setOnClickListener(view -> {
            touch(view);
            if (newsIndex >= 0 && newsIndex < newsItems.size()) openNewsItem(newsItems.get(newsIndex));
        });
        final float[] downX = {0f};
        final float[] downY = {0f};
        final boolean[] moved = {false};
        newsCard.setOnTouchListener((view, event) -> {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    downX[0] = event.getX();
                    downY[0] = event.getY();
                    moved[0] = false;
                    newsCarouselHandler.removeCallbacks(newsCarouselRunnable);
                    return true;
                case MotionEvent.ACTION_MOVE:
                    float moveX = event.getX() - downX[0];
                    float moveY = event.getY() - downY[0];
                    if (Math.abs(moveX) > dp(8) || Math.abs(moveY) > dp(8)) moved[0] = true;
                    if (Math.abs(moveX) > Math.abs(moveY)) {
                        view.setTranslationX(Math.max(-dp(24), Math.min(dp(24), moveX * 0.12f)));
                    }
                    return true;
                case MotionEvent.ACTION_UP:
                    float deltaX = event.getX() - downX[0];
                    float deltaY = event.getY() - downY[0];
                    view.animate().translationX(0f).setDuration(140).start();
                    boolean horizontalSwipe = newsItems.size() > 1
                            && Math.abs(deltaX) >= dp(42)
                            && Math.abs(deltaX) > Math.abs(deltaY) * 1.2f;
                    if (horizontalSwipe) {
                        int direction = deltaX < 0 ? 1 : -1;
                        showNewsItem((newsIndex + direction + newsItems.size()) % newsItems.size(), true);
                        try { view.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK); } catch (Throwable ignored) { }
                    } else if (!moved[0] && Math.abs(deltaX) < dp(12) && Math.abs(deltaY) < dp(12)) {
                        view.performClick();
                    }
                    startNewsCarousel();
                    return true;
                case MotionEvent.ACTION_CANCEL:
                    view.animate().translationX(0f).setDuration(140).start();
                    startNewsCarousel();
                    return true;
                default:
                    return true;
            }
        });
    }

    private void restartCarouselTimer() {
        carouselHandler.removeCallbacks(carouselRunnable);
        if (carouselGames.size() > 1) carouselHandler.postDelayed(carouselRunnable, 5000L);
    }

    private void refreshProfileHeader() {
        String name = displayProfileName();
        int hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY);
        String period = hour < 5 ? "It's late at night" : hour < 11 ? "Good morning" : hour < 14 ? "Good afternoon" : hour < 18 ? "Good afternoon" : "Good evening";
        String tip = greetingTip(hour);
        if (homeGreeting != null) homeGreeting.setText(period + "，" + name + "  ›  " + tip);

        if (homeAvatarInitial != null) {
            homeAvatarInitial.setText(profileInitial(name));
            homeAvatarInitial.setVisibility(View.VISIBLE);
        }
        if (homeProfileStatusDot != null) {
            homeProfileStatusDot.setBackgroundResource(isLoggedIn()
                    ? R.drawable.bg_profile_dot_online
                    : R.drawable.bg_profile_dot_local);
        }
        loadProfileAvatar();
        updateChatBadge();
    }

    private void updateChatBadge() {
        if (!isLoggedIn()) return;
        TextView badge = findViewById(R.id.homeNavChatBadge);
        if (badge == null) return;
        com.yuki.yukihub.util.AppExecutors.runOnIo(() -> {
            try {
                com.yuki.yukihub.social.SocialApiClient client = new com.yuki.yukihub.social.SocialApiClient(this);
                int unread = client.getTotalUnread();
                client.getFriendsList();
                int pending = client.getPendingRequestsCount();
                int total = unread + pending;
                runOnUiThread(() -> {
                    if (total > 0) {
                        badge.setText(String.valueOf(total));
                        badge.setVisibility(View.VISIBLE);
                    } else {
                        badge.setVisibility(View.GONE);
                    }
                });
            } catch (Throwable t) {
                // 角标拿不到就保持原样，不打扰用户；但留痕便于排查
                Log.w("YukiHub", "home badge fetch failed: " + t.getMessage(), t);
            }
        });
    }
    private boolean isLoggedIn() {
        if (prefs == null) return false;
        String token = prefs.getString(KEY_AUTH_ACCESS_TOKEN, "");
        return token != null && !token.trim().isEmpty();
    }

    private String displayProfileName() {
        if (prefs == null) return "Yuki";
        if (isLoggedIn()) {
            String cloudName = prefs.getString(KEY_AUTH_NICKNAME, "");
            if (cloudName != null && !cloudName.trim().isEmpty()) return cloudName.trim();
        }
        String localName = prefs.getString(KEY_PROFILE_NAME, "Yuki");
        return localName == null || localName.trim().isEmpty() ? "Yuki" : localName.trim();
    }

    private String profileInitial(String name) {
        String value = name == null ? "" : name.trim();
        if (value.isEmpty()) return "Y";
        try {
            int end = value.offsetByCodePoints(0, 1);
            return value.substring(0, end).toUpperCase(Locale.getDefault());
        } catch (Throwable ignored) {
            return "Y";
        }
    }

    private String greetingTip(int hour) {
        String[] tips;
        if (hour < 5) {
            tips = new String[]{"Don't forget to save your progress", "It's late at night, so you should also pay attention to rest", "Let's rest after this chapter is over"};
        } else if (hour < 11) {
            tips = new String[]{"A new day starts with your favorite story", "Let’s sort out the game library today too.", "Hope to meet a good story today"};
        } else if (hour < 18) {
            tips = new String[]{"Do you want to continue the story from last time?", "Game library is ready", "Leave yourself some time to play"};
        } else {
            tips = new String[]{"Which story do you want to continue tonight?", "Welcome back, the archive is still waiting for you", "Pick a game you like and relax"};
        }
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        int stableIndex = Math.abs(calendar.get(java.util.Calendar.DAY_OF_YEAR) + hour / 5) % tips.length;
        return tips[stableIndex];
    }

    private void loadProfileAvatar() {
        if (homeAvatar == null) return;
        // 始终使用 profile_avatar（本地 file:// 路径），与 MainActivity 保持一致
        String value = prefs == null ? "" : prefs.getString(KEY_PROFILE_AVATAR, "");
        value = value == null ? "" : value.trim();
        avatarImageRequest = value;
        homeAvatar.setVisibility(View.GONE);
        if (value.isEmpty()) return;
        try {
            Uri uri = value.contains("://") ? Uri.parse(value) : Uri.fromFile(new File(value));
            homeAvatar.setImageURI(uri);
            if (homeAvatar.getDrawable() != null) {
                homeAvatar.setVisibility(View.VISIBLE);
                if (homeAvatarInitial != null) homeAvatarInitial.setVisibility(View.GONE);
            }
        } catch (Throwable ignored) {
            homeAvatar.setVisibility(View.GONE);
        }
    }

    private void loadRemoteProfileAvatar(String url) {
        final String request = url;
        new Thread(() -> {
            Bitmap bitmap = null;
            try {
                File dir = new File(getCacheDir(), "home_profile");
                if (!dir.exists()) dir.mkdirs();
                File file = new File(dir, "avatar_" + Integer.toHexString(url.hashCode()));
                if (file.exists() && file.length() > 0) bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                if (bitmap == null) {
                    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                    connection.setConnectTimeout(6000);
                    connection.setReadTimeout(9000);
                    connection.setInstanceFollowRedirects(true);
                    connection.setRequestProperty("User-Agent", "YukiHub/1.0");
                    try (InputStream input = connection.getInputStream(); FileOutputStream output = new FileOutputStream(file)) {
                        byte[] buffer = new byte[8192];
                        int count;
                        while ((count = input.read(buffer)) != -1) output.write(buffer, 0, count);
                    } finally {
                        connection.disconnect();
                    }
                    bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                }
            } catch (Throwable ignored) { }
            final Bitmap result = bitmap;
            runOnUiThread(() -> {
                if (!request.equals(avatarImageRequest) || result == null || homeAvatar == null) return;
                homeAvatar.setImageBitmap(result);
                homeAvatar.setVisibility(View.VISIBLE);
                if (homeAvatarInitial != null) homeAvatarInitial.setVisibility(View.GONE);
            });
        }).start();
    }

    private void refreshHome() {
        List<Game> games;
        try {
            games = repository.getAll();
        } catch (Throwable t) {
            Toast.makeText(this, "Failed to read homepage data", Toast.LENGTH_SHORT).show();
            return;
        }

        int completed = 0;
        int playing = 0;
        for (Game game : games) {
            if (game == null) continue;
            if ("completed".equals(game.playStatus)) completed++;
            else if ("playing".equals(game.playStatus)) playing++;
        }
        bindTodayPlayTime();
        com.yuki.yukihub.util.IconedText.set(gameCount, R.drawable.ic_nav_library, "Game Library\\n" + games.size() + "Payment", 10f, 0xFFFFFFFF);
        com.yuki.yukihub.util.IconedText.set(completedCount, R.drawable.ic_st_trophy, "Played \\n" + completed + " 个", 10f, 0xFFFFFFFF);
        com.yuki.yukihub.util.IconedText.set(playingCount, R.drawable.ic_st_heart, "Playing\\n" + playing + " 个", 10f, 0xFFFFFFFF);

        setupCarousel(games);
        bindQuickGames(games);
        bindRecentActivity();
        loadGalgameNews();
    }

    private static final long MIN_PLAY_SESSION_MS = 0L;
    private static final long MAX_PLAY_SESSION_MS = 12L * 60L * 60L * 1000L;
    /**
     * 秒退阈值：与 MainActivity 保持一致。
     * 未完成且起始时间距今短于此值的记录直接丢弃，不询问用户。
     * 正常手动退出的游戏由 MainActivity.finishCurrentPlaySessionIfAny 结算，不受影响。
     */
    private static final long DISCARD_OPEN_SESSION_MS = 10L * 1000L;
    private boolean staleSessionDialogShowing = false;

    private void finishStalePlaySessionsIfAny() {
        if (repository == null || staleSessionDialogShowing) return;
        // 与 MainActivity 共用进程级标记，避免在两个界面间切换时重复弹同一个模态窗
        if (GameRepository.isStaleSessionHandled()) return;

        // 先静默丢弃启动失败产生的秒退垃圾记录（游戏启动失败会连续产生多条）
        int discarded = repository.discardShortOpenPlaySessions(DISCARD_OPEN_SESSION_MS);
        if (discarded > 0) {
            android.util.Log.i("YukiHub", "discarded " + discarded + " short open play sessions");
        }

        GameRepository.PlayActivity open = repository.findLatestOpenPlaySession();
        if (open == null) {
            GameRepository.markStaleSessionHandled();
            return;
        }
        GameRepository.markStaleSessionHandled();
        staleSessionDialogShowing = true;
        int total = repository.countOpenPlaySessions();
        long now = System.currentTimeMillis();
        long rawDuration = Math.max(0L, now - open.startTime);
        long duration = Math.min(rawDuration, MAX_PLAY_SESSION_MS);
        String message = "It was detected that the latest play session did not end normally. \\n\\n"
                + "Game:" + empty(open.gameTitle, "Unnamed game") + "\n"
                + "Start time:" + new SimpleDateFormat("MM month dd day HH:mm", Locale.getDefault()).format(new Date(open.startTime)) + "\n"
                + "The amount of time that can be added to the record:" + formatPlayDuration(duration) + "\n\n"
                + (total > 1
                    ? "Also" + (total - 1) + "Earlier unfinished records will be processed together. \\n\\n"
                    : "")
                + "If you are really playing during this period, you can choose to make a supplementary note; if it is just a test startup, crash or misoperation, please choose to ignore.";
        androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Found unfinished game history")
                .setMessage(message)
                .setPositiveButton("Supplementary note", (d, w) -> {
                    // 一次性结算全部未完成记录，避免下次进来又弹
                    repository.finishUnfinishedPlaySessions(
                            System.currentTimeMillis(), MIN_PLAY_SESSION_MS, MAX_PLAY_SESSION_MS);
                    refreshHome();
                    Toast.makeText(this, "Last play time has been recorded", Toast.LENGTH_SHORT).show();
                    staleSessionDialogShowing = false;
                })
                .setNegativeButton("Neglect", (d, w) -> {
                    // 同样一次性清掉全部，而非只删这一条
                    repository.deleteOpenPlaySessions();
                    refreshHome();
                    Toast.makeText(this, "Incomplete records ignored", Toast.LENGTH_SHORT).show();
                    staleSessionDialogShowing = false;
                })
                .setCancelable(false)
                .setOnDismissListener(d -> { staleSessionDialogShowing = false; applyImmersive(); })
                .show();
        styleDialogDark(dialog);
    }

    private void styleDialogDark(androidx.appcompat.app.AlertDialog dialog) {
        if (dialog == null) return;
        try {
            android.view.Window w = dialog.getWindow();
            if (w != null) w.setBackgroundDrawableResource(R.drawable.bg_dialog);
            int text = getColorCompat(R.color.yh_text);
            int muted = getColorCompat(R.color.yh_text_muted);
            int primary = getColorCompat(R.color.yh_primary);
            int secondary = getColorCompat(R.color.yh_secondary);
            int titleId = getResources().getIdentifier("alertTitle", "id", "android");
            // androidx.appcompat 的 alertTitle ID 可能不同，两个都找
            if (titleId == 0) {
                titleId = getResources().getIdentifier("alertTitle", "id", getPackageName());
            }
            TextView title = titleId != 0 ? dialog.findViewById(titleId) : null;
            // 如果找不到 alertTitle，遍历 dialog 的 decor view 找带标题的 TextView
            if (title == null && w != null) {
                title = findTitleTextView(w.getDecorView());
            }
            if (title != null) title.setTextColor(text);
            TextView msg = dialog.findViewById(android.R.id.message);
            if (msg != null) msg.setTextColor(muted);
            android.widget.Button p = dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE);
            android.widget.Button n = dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE);
            if (p != null) p.setTextColor(primary);
            if (n != null) n.setTextColor(secondary);
            applyImmersiveToWindow(w);
            applyImmersive();
        } catch (Throwable ignored) { }
    }

    @SuppressWarnings("deprecation")
    private int getColorCompat(int id) {
        if (android.os.Build.VERSION.SDK_INT >= 23) return getColor(id);
        return getResources().getColor(id);
    }

    /** 遍历 dialog 的 decor view，找标题样式的 TextView */
    private TextView findTitleTextView(View root) {
        if (root instanceof TextView) {
            TextView tv = (TextView) root;
            // 标题通常 textSize >= 18sp 且不是按钮
            if (tv.getText() != null && tv.getText().length() > 0
                    && tv.getTextSize() / getResources().getDisplayMetrics().scaledDensity >= 16f
                    && !(root instanceof android.widget.Button)
                    && tv.getId() != android.R.id.message
                    && tv.getId() != android.R.id.button1
                    && tv.getId() != android.R.id.button2
                    && tv.getId() != android.R.id.button3) {
                return tv;
            }
        }
        if (root instanceof android.view.ViewGroup) {
            android.view.ViewGroup vg = (android.view.ViewGroup) root;
            for (int i = 0; i < vg.getChildCount(); i++) {
                TextView found = findTitleTextView(vg.getChildAt(i));
                if (found != null) return found;
            }
        }
        return null;
    }

    private void applyImmersiveToWindow(android.view.Window window) {
        if (window == null) return;
        window.addFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 主界面可选：是否绘制到刘海/挖孔区域（首页设置里开关，默认开启）
        com.yuki.yukihub.util.CutoutCompat.setCutoutMode(window,
                prefs != null && prefs.getBoolean(MainActivity.KEY_MAIN_DRAW_CUTOUT, true));
        View decor = window.getDecorView();
        if (decor == null) return;
        if (android.os.Build.VERSION.SDK_INT >= 30) {
            WindowInsetsController controller = decor.getWindowInsetsController();
            if (controller != null) {
                controller.hide(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
                controller.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
            }
        }
        decor.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }

    /** 弹窗用的正式时长格式：<1分钟显示秒，>=1分钟显示X小时Y分钟 */
    private String formatPlayDuration(long millis) {
        long totalSeconds = Math.max(0L, millis) / 1000L;
        if (totalSeconds < 60L) return totalSeconds + "Second";
        long totalMinutes = totalSeconds / 60L;
        long hours = totalMinutes / 60L;
        long minutes = totalMinutes % 60L;
        StringBuilder sb = new StringBuilder();
        if (hours > 0L) sb.append(hours).append("Hour");
        sb.append(minutes).append("Minute");
        return sb.toString();
    }

    private void bindTodayPlayTime() {
        if (playTime == null || repository == null) return;
        try {
            java.util.Calendar now = java.util.Calendar.getInstance();
            java.util.Calendar todayStart = (java.util.Calendar) now.clone();
            todayStart.set(java.util.Calendar.HOUR_OF_DAY, 0);
            todayStart.set(java.util.Calendar.MINUTE, 0);
            todayStart.set(java.util.Calendar.SECOND, 0);
            todayStart.set(java.util.Calendar.MILLISECOND, 0);

            java.util.Calendar yesterdayStart = (java.util.Calendar) todayStart.clone();
            yesterdayStart.add(java.util.Calendar.DAY_OF_MONTH, -1);

            // 今天 0:00 ~ 现在
            long today = sumDurations(repository.getPlayDurationsBetween(
                    todayStart.getTimeInMillis(), now.getTimeInMillis() + 1L));
            // 昨天 0:00 ~ 昨天 24:00（整天）
            long yesterday = sumDurations(repository.getPlayDurationsBetween(
                    yesterdayStart.getTimeInMillis(), todayStart.getTimeInMillis() + 1L));
long difference = today - yesterday;
            // 图标 + 文字用 SpannableStringBuilder 拼，保留趋势着色
            CharSequence head = com.yuki.yukihub.util.IconedText.build(
                    this, R.drawable.ic_nav_clock, "Today’s play\\n", 10f, 0xFFFFFFFF);
            android.text.SpannableStringBuilder sb = new android.text.SpannableStringBuilder(head);
            if (today <= 0L && yesterday <= 0L) {
                sb.append("0m\\n I haven’t started playing today yet");
            } else if (yesterday <= 0L) {
                sb.append(formatDuration(today)).append("\\n Yesterday 0m · Start accumulating");
            } else {
                String icon = difference > 0 ? "▲" : difference < 0 ? "▼" : "—";
                String trend = icon + " " + formatCompactDuration(Math.abs(difference)) + "Than yesterday";
                sb.append(formatDuration(today)).append("\n");
                int start = sb.length();
                sb.append(trend);
                int color = difference > 0 ? 0xFF44FF66 : difference < 0 ? 0xFFFF4444 : 0xFFAAAAAA;
                sb.setSpan(new ForegroundColorSpan(color), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            playTime.setText(sb);
        } catch (Throwable ignored) {
            com.yuki.yukihub.util.IconedText.set(playTime, R.drawable.ic_nav_clock,
                    "Today’s play\\n0m\\n— No comparison yet", 10f, 0xFFFFFFFF);
        }
}
    private long sumDurations(java.util.Map<String, Long> durations) {
        long total = 0L;
        if (durations == null) return total;
        for (Long value : durations.values()) total += value == null ? 0L : Math.max(0L, value);
        return total;
    }

    private String formatCompactDuration(long millis) {
        long minutes = Math.max(0L, millis) / 60000L;
        if (minutes < 1L && millis > 0L) return "<1m";
        long hours = minutes / 60L;
        long remain = minutes % 60L;
        if (hours <= 0L) return remain + "m";
        if (remain <= 0L) return hours + "h";
        return hours + "h " + remain + "m";
    }

    private void setupCarousel(List<Game> games) {
        carouselHandler.removeCallbacks(carouselRunnable);
        // M18：存一份，NSFW 开关变化时可以直接重刷（不用回主库再进来）
        lastHomeGames = games == null ? null : new ArrayList<>(games);
        carouselGames.clear();
        if (games != null) {
            // repository.getAll() 已按最近游玩、创建时间排序。
            for (Game game : games) {
                if (game == null) continue;
                carouselGames.add(game);
                if (carouselGames.size() >= 5) break;
            }
        }
        carouselIndex = 0;
        buildCarouselDots();
        if (carouselGames.isEmpty()) {
            heroImageRequest = "";
            heroCover.setImageResource(R.drawable.ic_launcher_foreground);
            heroTitle.setText("Start your gaming journey");
            heroSubtitle.setText("Add or select a game from the game library");
            View action = findViewById(R.id.homeHeroAction);
            if (action != null) action.setEnabled(false);
            return;
        }
        View action = findViewById(R.id.homeHeroAction);
        if (action != null) action.setEnabled(true);
        showCarouselGame(0, false);
        if (carouselGames.size() > 1) carouselHandler.postDelayed(carouselRunnable, 5000L);
    }

    private void buildCarouselDots() {
        if (heroDots == null) return;
        heroDots.removeAllViews();
        for (int i = 0; i < carouselGames.size(); i++) {
            final int index = i;
            View dot = new View(this);
            dot.setBackground(dotDrawable(i == carouselIndex));
            dot.setOnClickListener(v -> {
                carouselIndex = index;
                showCarouselGame(index, true);
                carouselHandler.removeCallbacks(carouselRunnable);
                if (carouselGames.size() > 1) carouselHandler.postDelayed(carouselRunnable, 5000L);
            });
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dp(i == carouselIndex ? 14 : 6), dp(5));
            lp.setMargins(dp(2), 0, dp(2), 0);
            heroDots.addView(dot, lp);
        }
    }

    private GradientDrawable dotDrawable(boolean active) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(dp(3));
        drawable.setColor(active ? 0xFFFFFFFF : 0x55FFFFFF);
        return drawable;
    }

    private void showCarouselGame(int index, boolean animate) {
        if (index < 0 || index >= carouselGames.size()) return;
        Game game = carouselGames.get(index);
        carouselIndex = index;
        if (animate) {
            heroCover.animate().alpha(0.25f).setDuration(120).withEndAction(() -> {
                bindCarouselContent(game);
                heroCover.animate().alpha(1f).setDuration(220).start();
            }).start();
        } else {
            bindCarouselContent(game);
            heroCover.setAlpha(1f);
        }
        buildCarouselDots();
    }

    private void bindCarouselContent(Game game) {
        heroTitle.setText(empty(game.title, "Unnamed game"));
        heroSubtitle.setText(game.lastPlayedAt > 0
                ? "Last time played:" + new SimpleDateFormat("MM month dd day HH:mm", Locale.getDefault()).format(new Date(game.lastPlayedAt))
                : "Already included in the game library");
        loadHeroCover(game);
    }

    private void loadHeroCover(Game game) {
        String value = firstNonEmpty(game == null ? null : game.coverPersistUri, game == null ? null : game.coverUri);
        final String request = String.valueOf(game == null ? -1L : game.id) + ":" + value;
        heroImageRequest = request;
        heroCover.setImageResource(R.drawable.ic_launcher_foreground);
        if (value.isEmpty()) return;
        // M18：首页也要遵守 NSFW 模糊（与主库/大屏同一开关，默认开）
        final boolean blur = needNsfwBlur(game);
        final String blurKey = NsfwBlur.cacheKey("home_hero", game == null ? -1L : game.id, value);
        if (value.startsWith("http://") || value.startsWith("https://")) {
            loadRemoteHeroCover(value, request, blur, blurKey);
            return;
        }
        if (blur) {
            // 安全底线：模糊算不出来就保持占位图，绝不露原图
            NsfwBlur.load(heroCover, value, blurKey, 2, 24f, bitmap -> {
                if (!request.equals(heroImageRequest)) return;
                if (bitmap != null) heroCover.setImageBitmap(bitmap);
                else heroCover.setImageResource(R.drawable.ic_launcher_foreground);
            });
            return;
        }
        try {
            Uri uri = value.contains("://") ? Uri.parse(value) : Uri.fromFile(new File(value));
            heroCover.setImageURI(uri);
            if (heroCover.getDrawable() == null) heroCover.setImageResource(R.drawable.ic_launcher_foreground);
        } catch (Throwable ignored) {
            heroCover.setImageResource(R.drawable.ic_launcher_foreground);
        }
    }

    /** M18：这张卡的封面要不要模糊（NSFW 判定 + 设置开关，与主库一致） */
    private boolean needNsfwBlur(Game game) {
        return game != null && game.nsfw && NsfwBlur.enabled(this);
    }

    /**
     * M18：NSFW 模糊开关变化后重刷首页封面。
     *
     * <p>只重刷"当前英雄位 + 快捷卡片"这两处会显示封面的地方：
     * 英雄位回到当前下标重新渲染，卡片列表按存下来的 {@link #lastHomeGames} 重建
     * （重建时就会走新的模糊判定）。
     */
    private void refreshNsfwCovers() {
        try {
            if (!carouselGames.isEmpty()) {
                int index = Math.max(0, Math.min(carouselIndex, carouselGames.size() - 1));
                showCarouselGame(index, false);
            }
            if (lastHomeGames != null) { bindQuickGames(lastHomeGames); }
        } catch (Throwable ignored) { }
    }

    private void loadRemoteHeroCover(String url, String request, boolean blur, String blurKey) {
        new Thread(() -> {
            Bitmap bitmap = null;
            try {
                File dir = new File(getCacheDir(), "home_carousel");
                if (!dir.exists()) dir.mkdirs();
                File file = new File(dir, "cover_" + Integer.toHexString(url.hashCode()));
                if (file.exists() && file.length() > 0) bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                if (bitmap == null) {
                    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                    connection.setConnectTimeout(6000);
                    connection.setReadTimeout(9000);
                    connection.setInstanceFollowRedirects(true);
                    connection.setRequestProperty("User-Agent", "YukiHub/1.0");
                    try (InputStream input = connection.getInputStream(); FileOutputStream output = new FileOutputStream(file)) {
                        byte[] buffer = new byte[8192];
                        int count;
                        while ((count = input.read(buffer)) != -1) output.write(buffer, 0, count);
                    } finally {
                        connection.disconnect();
                    }
                    bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                }
            } catch (Throwable ignored) { }
            final Bitmap result = bitmap;
            runOnUiThread(() -> {
                if (!request.equals(heroImageRequest)) return;
                if (result == null) return;
                if (blur) {
                    // M18：远程封面也是 NSFW 的话，同样只显示模糊图（算不出来就留占位）
                    NsfwBlur.blurAsync(heroCover, result, blurKey, 24f, blurred -> {
                        if (blurred != null) heroCover.setImageBitmap(blurred);
                        else heroCover.setImageResource(R.drawable.ic_launcher_foreground);
                    });
                } else {
                    heroCover.setImageBitmap(result);
                }
            });
        }).start();
    }

    private void bindQuickGames(List<Game> games) {
        quickGames.removeAllViews();
        int shown = 0;
        if (games != null) {
            for (Game game : games) {
                if (game == null || shown >= 4) break;
                quickGames.addView(createQuickGameCard(game, shown));
                shown++;
            }
        }
        quickGames.addView(createAddGameCard(shown));
    }

    private View createQuickGameCard(Game game, int index) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(4), dp(4), dp(4), dp(4));
        card.setBackgroundResource(R.drawable.bg_home_glass);
        card.setClipToOutline(true);
        card.setOutlineProvider(new android.view.ViewOutlineProvider() {
            @Override
            public void getOutline(View view, android.graphics.Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), dp(16));
            }
        });
        card.setClickable(true);
        card.setFocusable(true);
        card.setContentDescription("Enter the game library to view" + empty(game.title, "Game"));

        ImageView cover = new ImageView(this);
        cover.setScaleType(ImageView.ScaleType.CENTER_CROP);
        cover.setBackgroundColor(0x33273A75);
        cover.setClipToOutline(true);
        cover.setOutlineProvider(new android.view.ViewOutlineProvider() {
            @Override
            public void getOutline(View view, android.graphics.Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), dp(12));
            }
        });
        // M18：快捷卡片的封面也要遵守 NSFW 模糊（之前首页完全没判 NSFW）
        final String cardCover = firstNonEmpty(game.coverPersistUri, game.coverUri);
        final boolean cardBlur = needNsfwBlur(game) && !cardCover.isEmpty()
                && !cardCover.startsWith("http://") && !cardCover.startsWith("https://");
        if (cardBlur) {
            cover.setImageResource(R.drawable.ic_launcher_foreground);   // 先占位，模糊算好再换
            NsfwBlur.load(cover, cardCover, NsfwBlur.cacheKey("home_card", game.id, cardCover),
                    4, 22f, blurred -> {
                        if (blurred != null) cover.setImageBitmap(blurred);
                    });
        } else if (!loadLocalCover(cover, game)) {
            cover.setImageResource(R.drawable.ic_launcher_foreground);
        }
        card.addView(cover, new LinearLayout.LayoutParams(dp(62), dp(42)));

        TextView title = new TextView(this);
        title.setText(empty(game.title, "Unnamed game"));
        title.setTextColor(Color.WHITE);
        title.setTextSize(8);
        title.setSingleLine(true);
        title.setEllipsize(android.text.TextUtils.TruncateAt.END);
        title.setPadding(1, dp(1), 1, 0);
        card.addView(title, new LinearLayout.LayoutParams(dp(62), dp(14)));

        TextView button = new TextView(this);
        button.setGravity(android.view.Gravity.CENTER);
        button.setTextSize(7);
        button.setTypeface(null, android.graphics.Typeface.BOLD);
        // 首页快捷启动：约 18% 白底 + 淡白边，整张卡片仍可启动游戏。
        button.setBackgroundResource(R.drawable.bg_home_quick_action_glass);
        button.setTextColor(Color.WHITE);
        com.yuki.yukihub.util.IconedText.set(button, R.drawable.ic_btn_play, "Start up", 7f, Color.WHITE);
        button.setDuplicateParentStateEnabled(true);
        card.addView(button, new LinearLayout.LayoutParams(dp(62), dp(16)));

        card.setOnClickListener(v -> {
            touch(v);
            launchGameFromHome(game);
        });
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dp(70), dp(80));
        lp.setMargins(index == 0 ? 0 : dp(5), 0, 0, 0);
        card.setLayoutParams(lp);
        return card;
    }

    private View createAddGameCard(int index) {
        TextView add = new TextView(this);
        add.setText("+\\nAdd game");
        add.setGravity(android.view.Gravity.CENTER);
        add.setTextColor(Color.WHITE);
        add.setTextSize(9);
        add.setTypeface(null, android.graphics.Typeface.BOLD);
        add.setBackgroundResource(R.drawable.bg_home_glass);
        add.setClickable(true);
        add.setFocusable(true);
        add.setContentDescription("Enter the game library to add games");
        add.setOnClickListener(v -> {
            touch(v);
            startActivity(new Intent(this, MainActivity.class));
        });
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dp(62), dp(80));
        lp.setMargins(index == 0 ? 0 : dp(5), 0, 0, 0);
        add.setLayoutParams(lp);
        return add;
    }

    // ======================== Galgame 资讯（NextMoe /v2/news，免密钥，客户端直连） ========================

    private static final String NEWS_API_URL = "https://api.nextmoe.dev/v2/news";
    private static final String NEWS_CACHE_FILE = "home_news_cache.json";
    private static final long NEWS_CACHE_TTL_MS = 2L * 60L * 60L * 1000L; // 2 小时
    private static final int NEWS_ITEM_COUNT = 6;
    private static final long NEWS_CAROUSEL_INTERVAL_MS = 5000L;
    private boolean newsLoadInFlight = false;
    private final List<NewsItem> newsItems = new ArrayList<>();
    private int newsIndex = 0;
    private String newsImageRequest = "";
    private final Handler newsCarouselHandler = new Handler(Looper.getMainLooper());
    private final Runnable newsCarouselRunnable = new Runnable() {
        @Override public void run() {
            if (newsItems.size() > 1) {
                showNewsItem((newsIndex + 1) % newsItems.size(), true);
                newsCarouselHandler.postDelayed(this, NEWS_CAROUSEL_INTERVAL_MS);
            }
        }
    };

    /** 一条首页资讯。banner 为题图（当前源覆盖率 100%，仍做空值兜底）。 */
    private static class NewsItem {
        String title = "";
        String summary = "";
        String bannerUrl = "";
        String sourceUrl = "";
        String attribution = "";
        String publishedAt = "";
    }

    private void bindRecentActivity() {
        homeNewsRefresh.setOnClickListener(v -> {
            touch(v);
            loadGalgameNews();
        });
        // 图/标题的点击统一走容器 GestureDetector（见 bindViews），此处不再单独绑定
        // 冷启动优先用新鲜缓存（2h TTL）：不发请求，省匿名配额；过期/缺失才联网刷新
        List<NewsItem> cached = readNewsCache(true);
        if (cached != null && !cached.isEmpty()) {
            renderNews(cached);
            return;
        }
        homeNewsLoading.setVisibility(View.VISIBLE);
        loadGalgameNews();
    }

    private void startNewsCarousel() {
        newsCarouselHandler.removeCallbacks(newsCarouselRunnable);
        if (newsItems.size() > 1) newsCarouselHandler.postDelayed(newsCarouselRunnable, NEWS_CAROUSEL_INTERVAL_MS);
    }

    private void loadGalgameNews() {
        if (newsLoadInFlight) return;
        newsLoadInFlight = true;
        // 刷新中：图标不变，用透明度表示进行中（避免 setText 抖动）
        homeNewsRefresh.setAlpha(0.45f);
        homeNewsRefresh.setEnabled(false);
        new Thread(() -> {
            List<NewsItem> items = null;
            String error = null;
            try {
                items = fetchNewsFromApi();
            } catch (Throwable t) {
                error = t.getMessage() == null ? "Network abnormality" : t.getMessage();
            }
            final List<NewsItem> fetched = items;
            final boolean timedOut = "TIMED_OUT".equals(error);
            runOnUiThread(() -> {
                newsLoadInFlight = false;
                homeNewsRefresh.setAlpha(1f);
                homeNewsRefresh.setEnabled(true);
                if (fetched != null && !fetched.isEmpty()) {
                    writeNewsCache(fetched);
                    renderNews(fetched);
                } else {
                    List<NewsItem> stale = readNewsCache(false);
                    if (stale != null && !stale.isEmpty()) {
                        renderNews(stale);
                    } else {
                        homeNewsLoading.setVisibility(View.VISIBLE);
                        homeNewsLoading.setText(timedOut ? "Information loading is slow, please try again later" : "Failed to obtain information, click refresh in the upper right corner to try again");
                        homeNewsBanner.setVisibility(View.GONE);
                        homeNewsTitle.setText("");
                        buildNewsDots();
                    }
                }
            });
        }, "yukihub-news").start();
    }

    /** 匿名直连 NextMoe。任何错误（429/限流/断网）都不该打爆首页，超时单独识别。 */
    private List<NewsItem> fetchNewsFromApi() throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(
                NEWS_API_URL + "?limit=" + NEWS_ITEM_COUNT + "&nsfw=true").openConnection();
        try {
            conn.setConnectTimeout(6000);
            conn.setReadTimeout(9000);
            conn.setRequestProperty("Accept", "application/json");
            int code = conn.getResponseCode();
            if (code != 200) throw new RuntimeException("HTTP " + code);
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
            try (InputStream in = conn.getInputStream()) {
                byte[] buf = new byte[8192];
                int n;
                while ((n = in.read(buf)) > 0) bos.write(buf, 0, n);
            }
            return parseNewsJson(bos.toString("UTF-8"));
        } catch (java.net.SocketTimeoutException e) {
            throw new RuntimeException("TIMED_OUT");
        } finally {
            try { conn.disconnect(); } catch (Throwable ignored) { }
        }
    }

    /** 无信封集合：{ object:"list", items:[{ id, title, summary, source:{attribution}, source_url, published_at }] }。 */
    private List<NewsItem> parseNewsJson(String json) throws Exception {
        List<NewsItem> out = new ArrayList<>();
        org.json.JSONObject root = new org.json.JSONObject(json);
        org.json.JSONArray items = root.optJSONArray("items");
        if (items == null) return out;
        for (int i = 0; i < items.length() && out.size() < NEWS_ITEM_COUNT; i++) {
            org.json.JSONObject o = items.optJSONObject(i);
            if (o == null) continue;
            NewsItem item = new NewsItem();
            item.title = o.optString("title", "").trim();
            if (item.title.isEmpty()) continue; // 没标题的没法点，跳过
            item.summary = o.optString("summary", "").trim();
            org.json.JSONObject banner = o.optJSONObject("banner");
            if (banner != null) item.bannerUrl = banner.optString("url", "").trim();
            item.sourceUrl = o.optString("source_url", "").trim();
            org.json.JSONObject source = o.optJSONObject("source");
            if (source != null) item.attribution = source.optString("attribution", "").trim();
            item.publishedAt = o.optString("published_at", "").trim();
            out.add(item);
        }
        return out;
    }

    private void renderNews(List<NewsItem> items) {
        newsItems.clear();
        for (int i = 0; i < items.size() && i < NEWS_ITEM_COUNT; i++) newsItems.add(items.get(i));
        homeNewsLoading.setVisibility(View.GONE);
        newsIndex = 0;
        showNewsItem(0, false);
        startNewsCarousel();
    }

    private void showNewsItem(int index, boolean animate) {
        if (index < 0 || index >= newsItems.size()) return;
        NewsItem item = newsItems.get(index);
        newsIndex = index;
        buildNewsDots();
        if (animate) {
            homeNewsBanner.animate().alpha(0.25f).setDuration(120).withEndAction(() -> {
                bindNewsContent(item);
                homeNewsBanner.animate().alpha(1f).setDuration(220).start();
            }).start();
        } else {
            bindNewsContent(item);
            homeNewsBanner.setAlpha(1f);
        }
    }

    private void bindNewsContent(NewsItem item) {
        homeNewsTitle.setText(item.title);
        if (item.bannerUrl == null || item.bannerUrl.isEmpty()) {
            // 无题图兜底：隐藏图片层，遮罩下的深色底仍在，标题照常可读
            newsImageRequest = "";
            homeNewsBanner.setVisibility(View.GONE);
            return;
        }
        loadNewsBanner(item.bannerUrl);
    }

    private void buildNewsDots() {
        homeNewsDots.removeAllViews();
        for (int i = 0; i < newsItems.size(); i++) {
            final int index = i;
            View dot = new View(this);
            dot.setBackground(dotDrawable(i == newsIndex));
            dot.setOnClickListener(v -> {
                touch(v);
                showNewsItem(index, true);
                startNewsCarousel();
            });
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dp(i == newsIndex ? 12 : 5), dp(4));
            lp.setMargins(dp(2), 0, dp(2), 0);
            homeNewsDots.addView(dot, lp);
        }
    }

    /** 复用 hero 轮播的磁盘缓存模式（同目录，news_ 前缀区分）。 */
    private void loadNewsBanner(String url) {
        final String request = url;
        newsImageRequest = request;
        homeNewsBanner.setVisibility(View.VISIBLE);
        new Thread(() -> {
            Bitmap bitmap = null;
            try {
                File dir = new File(getCacheDir(), "home_carousel");
                if (!dir.exists()) dir.mkdirs();
                File file = new File(dir, "news_" + Integer.toHexString(url.hashCode()));
                if (file.exists() && file.length() > 0) bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                if (bitmap == null) {
                    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                    connection.setConnectTimeout(6000);
                    connection.setReadTimeout(9000);
                    connection.setInstanceFollowRedirects(true);
                    connection.setRequestProperty("User-Agent", "YukiHub/1.0");
                    try (InputStream input = connection.getInputStream(); FileOutputStream output = new FileOutputStream(file)) {
                        byte[] buffer = new byte[8192];
                        int count;
                        while ((count = input.read(buffer)) != -1) output.write(buffer, 0, count);
                    } finally {
                        connection.disconnect();
                    }
                    bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                }
            } catch (Throwable ignored) { }
            final Bitmap result = bitmap;
            runOnUiThread(() -> {
                if (!request.equals(newsImageRequest)) return;
                if (result != null) {
                    homeNewsBanner.setImageBitmap(result);
                    homeNewsBanner.setVisibility(View.VISIBLE);
                    // 揭示动画：106% 缩放 + 全透明起步，减速曲线落定（缓存命中同样播放，节奏一致）
                    homeNewsBanner.setAlpha(0f);
                    homeNewsBanner.setScaleX(1.06f);
                    homeNewsBanner.setScaleY(1.06f);
                    homeNewsBanner.animate().alpha(1f).scaleX(1f).scaleY(1f)
                            .setDuration(320L)
                            .setInterpolator(new android.view.animation.DecelerateInterpolator(1.6f))
                            .start();
                } else {
                    homeNewsBanner.setVisibility(View.GONE);
                }
            });
        }, "yukihub-news-banner").start();
    }

    /** 详情弹窗：标题 + 摘要 + 日期/署名（NextMoe 要求引用资讯须标注来源）+ 阅读原文。 */
    private void openNewsItem(NewsItem item) {
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        int pad = dp(18);
        box.setPadding(pad, dp(4), pad, dp(2));

        TextView summary = new TextView(this);
        summary.setText(item.summary.isEmpty() ? "No summary yet." : item.summary);
        summary.setTextSize(13f);
        summary.setTextColor(0xE8FFFFFF);
        summary.setLineSpacing(dp(2), 1.0f);
        box.addView(summary);

        StringBuilder metaText = new StringBuilder();
        if (item.publishedAt != null && item.publishedAt.length() >= 10) {
            metaText.append("Posted in").append(item.publishedAt.substring(0, 10));
        }
        if (item.attribution != null && !item.attribution.isEmpty()) {
            if (metaText.length() > 0) metaText.append('\n');
            metaText.append(item.attribution);
        }
        if (metaText.length() == 0) metaText.append("Via NextMoe·Weimeng");
        TextView meta = new TextView(this);
        meta.setText(metaText.toString());
        meta.setTextSize(10f);
        meta.setTextColor(0x8CFFFFFF);
        meta.setPadding(0, dp(12), 0, 0);
        box.addView(meta, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle(item.title)
                .setView(box)
                .setPositiveButton("closure", null);
        if (item.sourceUrl != null && !item.sourceUrl.isEmpty()) {
            builder.setNegativeButton("Read the original article", (d, w) -> {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(item.sourceUrl)));
                } catch (Throwable t) {
                    Toast.makeText(this, "Unable to open the original message, please check the network", Toast.LENGTH_SHORT).show();
                }
            });
        }
        styleDialogDark(builder.show());
    }

    private File newsCacheFile() {
        return new File(getCacheDir(), NEWS_CACHE_FILE);
    }

    private void writeNewsCache(List<NewsItem> items) {
        try {
            org.json.JSONObject root = new org.json.JSONObject();
            root.put("fetched_at", System.currentTimeMillis());
            org.json.JSONArray arr = new org.json.JSONArray();
            for (NewsItem item : items) {
                org.json.JSONObject o = new org.json.JSONObject();
                o.put("title", item.title);
                o.put("summary", item.summary);
                o.put("bannerUrl", item.bannerUrl);
                o.put("source_url", item.sourceUrl);
                o.put("attribution", item.attribution);
                o.put("published_at", item.publishedAt);
                arr.put(o);
            }
            root.put("items", arr);
            try (java.io.FileOutputStream fos = new java.io.FileOutputStream(newsCacheFile())) {
                fos.write(root.toString().getBytes("UTF-8"));
            }
        } catch (Throwable ignored) { }
    }

    /** freshOnly=true 时过期返回 null；false 时过期缓存也回（离线兜底）。 */
    private List<NewsItem> readNewsCache(boolean freshOnly) {
        try {
            File file = newsCacheFile();
            if (!file.exists()) return null;
            byte[] data = new byte[(int) file.length()];
            try (java.io.FileInputStream fis = new java.io.FileInputStream(file)) {
                int off = 0;
                while (off < data.length) {
                    int n = fis.read(data, off, data.length - off);
                    if (n < 0) break;
                    off += n;
                }
            }
            org.json.JSONObject root = new org.json.JSONObject(new String(data, "UTF-8"));
            if (freshOnly && System.currentTimeMillis() - root.optLong("fetched_at", 0) > NEWS_CACHE_TTL_MS) return null;
            org.json.JSONArray arr = root.optJSONArray("items");
            if (arr == null) return null;
            List<NewsItem> out = new ArrayList<>();
            for (int i = 0; i < arr.length() && out.size() < NEWS_ITEM_COUNT; i++) {
                org.json.JSONObject o = arr.optJSONObject(i);
                if (o == null) continue;
                NewsItem item = new NewsItem();
                item.title = o.optString("title", "");
                item.summary = o.optString("summary", "");
                item.bannerUrl = o.optString("bannerUrl", "");
                item.sourceUrl = o.optString("source_url", "");
                item.attribution = o.optString("attribution", "");
                item.publishedAt = o.optString("published_at", "");
                if (!item.title.isEmpty()) out.add(item);
            }
            return out;
        } catch (Throwable ignored) {
            return null;
        }
    }

    private boolean loadLocalCover(ImageView target, Game game) {
        if (target == null || game == null) return false;
        String value = firstNonEmpty(game.coverPersistUri, game.coverUri);
        if (value.isEmpty() || value.startsWith("http://") || value.startsWith("https://")) return false;
        try {
            Uri uri = Uri.parse(value);
            if ("file".equalsIgnoreCase(uri.getScheme())) {
                File file = new File(uri.getPath() == null ? "" : uri.getPath());
                if (!file.exists()) return false;
            }
            target.setImageURI(uri);
            return target.getDrawable() != null;
        } catch (Throwable ignored) {
            return false;
        }
    }

    private String formatDuration(long millis) {
        long minutes = Math.max(0L, millis) / 60000L;
        if (minutes < 1L && millis > 0L) return "<1m";
        long hours = minutes / 60L;
        long remain = minutes % 60L;
        if (hours <= 0L) return remain + "m";
        if (remain <= 0L) return hours + "h";
        return hours + "h " + remain + "m";
    }

    private String empty(String value, String fallback) {
        return value == null || value.trim().isEmpty() ? fallback : value.trim();
    }

    private String firstNonEmpty(String a, String b) {
        if (a != null && !a.trim().isEmpty()) return a.trim();
        if (b != null && !b.trim().isEmpty()) return b.trim();
        return "";
    }

    private void touch(View view) {
        try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP); } catch (Throwable ignored) { }
        view.animate().scaleX(0.96f).scaleY(0.96f).setDuration(80).withEndAction(() -> view.animate().scaleX(1f).scaleY(1f).setDuration(120).start()).start();
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }

    private void applyImmersive() {
        try {
            // 主界面可选：是否绘制到刘海/挖孔区域（首页设置里开关，默认开启）
            com.yuki.yukihub.util.CutoutCompat.setCutoutMode(getWindow(),
                    prefs != null && prefs.getBoolean(MainActivity.KEY_MAIN_DRAW_CUTOUT, true));
            if (android.os.Build.VERSION.SDK_INT >= 30) {
                // 内容延伸到状态栏/刘海区域，避免安全区外露出窗口背景（白边）
                getWindow().setDecorFitsSystemWindows(false);
                WindowInsetsController controller = getWindow().getInsetsController();
                if (controller != null) {
                    controller.hide(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
                    controller.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
                }
            } else {
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }
        } catch (Throwable ignored) { }
    }
}