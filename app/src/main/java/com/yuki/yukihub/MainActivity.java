package com.yuki.yukihub;

import com.yuki.yukihub.util.ActionButtonStyle;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.UriPermission;
import android.content.pm.ApplicationInfo;
import android.content.pm.LauncherApps;
import android.content.pm.ShortcutInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.Nullable;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SurfaceTexture;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.UserHandle;
import android.os.UserManager;
import android.os.PersistableBundle;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.media.SoundPool;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import rikka.shizuku.Shizuku;
   
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
 import org.w3c.dom.Element;
 import org.w3c.dom.NodeList;
 
 import java.io.File;
 import java.io.FileInputStream;
 import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.File;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
   import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.lang.reflect.Method;

 import javax.xml.parsers.DocumentBuilderFactory;
 import javax.xml.transform.OutputKeys;
 import javax.xml.transform.Transformer;
 import javax.xml.transform.TransformerFactory;
 import javax.xml.transform.dom.DOMSource;
 import javax.xml.transform.stream.StreamResult;
 
 import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.documentfile.provider.DocumentFile;

import com.yuki.yukihub.ai.AiReviewClient;
import com.yuki.yukihub.ai.AiReviewController;
import com.yuki.yukihub.ai.AiReviewHistoryStore;
import com.yuki.yukihub.ai.AiReviewResult;
import com.yuki.yukihub.ai.AiReviewSettings;
import com.yuki.yukihub.ai.WeeklyPlayStats;
import com.yuki.yukihub.data.GameRepository;
import com.yuki.yukihub.data.GameRepository.PlayActivity;
import com.yuki.yukihub.data.MetadataRepository;
import com.yuki.yukihub.gamecursor.GameCursorConfig;
import com.yuki.yukihub.fvp.FvpLaunchPrefs;
import com.yuki.yukihub.gamecursor.GameCursorIconRenderer;
import com.yuki.yukihub.launcher.EmulatorLauncher;
import com.yuki.yukihub.launcher.PcLaunchPrefs;
import com.yuki.yukihub.launcher.WinlatorPcLauncher;
import com.yuki.yukihub.metadata.BangumiClient;
import com.yuki.yukihub.metadata.MetadataController;
import com.yuki.yukihub.metadata.VnMetadata;
import com.yuki.yukihub.metadata.YmgalClient;
import com.yuki.yukihub.model.EngineType;
import com.yuki.yukihub.model.Game;
import com.yuki.yukihub.ons.OnsLibLoader;
import com.yuki.yukihub.ons.OnsSettings;
import com.yuki.yukihub.scanner.FastGameScanner;
import com.yuki.yukihub.scanner.GameScanner;
import com.yuki.yukihub.scanner.ScanResult;
import com.yuki.yukihub.ui.GameAdapter;
import com.yuki.yukihub.ui.TapSafeSpinner;
import com.yuki.yukihub.ui.CardGlowView;
import com.yuki.yukihub.ui.DynamicSnowBackgroundView;
import com.yuki.yukihub.ui.DynamicTheme;
import com.yuki.yukihub.ui.ThemeColorExtractor;
import com.yuki.yukihub.ui.ScanResultAdapter;
import com.yuki.yukihub.ui.colorpicker.ColorPickerDialog;
import com.yuki.yukihub.util.AppExecutors;
import com.yuki.yukihub.util.NoMediaHelper;
import com.yuki.yukihub.util.DevLogger;
import com.yuki.yukihub.util.TimeFormatUtil;
import com.yuki.yukihub.util.UiScaleUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.text.Collator;
import java.util.Map;
import java.util.Calendar;
import java.util.Set;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(UiScaleUtil.wrap(newBase));
    }

    private GameRepository repository;
    private MetadataRepository metadataRepository;
    private AiReviewController aiReviewController;
    private MetadataController metadataController;
    private GameAdapter adapter;
    private final List<Game> allGames = new ArrayList<>();
    private String filter = "ALL";
    /**
     * 隐藏游戏列表：与 {@link #allGames} 分开维护。
     * <p>{@code repository.getAll()} 在 SQL 层就排除了 {@code hidden=1}，所以隐藏项不会混进
     * 普通分类；仅当 {@code filter == "HIDDEN"} 时把它作为列表数据源。
     */
    private final List<Game> hiddenGames = new ArrayList<>();
    /** 上次加载时的隐藏条数；onResume 用它判断其它界面（大屏）是否改过隐藏状态。 */
    private int lastKnownHiddenCount = -1;
    /** 本地游戏分类的安装检测缓存（key=rootUri，进入分类时清空重查，分类内搜索/排序复用）。 */
    private final java.util.Map<String, Boolean> localInstalledCache = new java.util.HashMap<>();
private String query = "";
private String developerFilter = "";
    // 多选批量操作栏
    private LinearLayout multiSelectBar;
    private TextView multiSelectCount;
    /** 多选栏里的「隐藏 / 恢复」按钮：文案随当前分类变化，需要每次刷新。 */
    private TextView multiSelectHideBtn;
    private TextView tvEmpty, tvStatsGames, tvStatsTime, tvProfileName, tvProfileInitial;
private ImageView ivProfileAvatar;
private View profileStatusDot;
private LinearLayout detailPanel, detailMetaPanel;
private ImageView sideDetailCover;
    /**
     * M21-2：当前已经设在 sideDetailCover 上的封面 uri。
     * <p>{@code setImageURI} 是**同步解码**（主线程），每点一次卡片就重解一遍封面，
     * 既让 UI 发木，又把"第二次点击"推到双击窗口之外。同一张图不再重解。
     */
    private String sideDetailCoverUri;
private ProgressBar sideDetailCoverLoading;
    private TextView sideDetailPlaceholder, sideDetailTitle, sideMetadataSourceBadge, sideDetailOriginalTitle, sideDetailHint, sideDetailPath, sideDetailDeveloper, sideDetailDate, sideDetailRating, sideDetailLength, sideDetailTags, sideDescToggle, sideTranslateToggle;
private LinearLayout sideTagContainer;
private ImageView sideScreenshot1, sideScreenshot2;
private TextView sideBtnLaunch, sideBtnOptions;
private boolean sideDescExpanded = false;
private boolean sideShowingTranslatedDescription = false;
private String sideFullDescription = "";
private VnMetadata currentSideMetadata;
    private Game selectedGame;
    private Dialog pendingEditDialog;
    private String pendingDirUri, pendingCoverUri;
    /** 当前编辑对话框对应的游戏 id（-1 = 新增模式）。Activity 重建恢复编辑状态用。 */
    private long editingGameId = -1L;
    /** 最近一次保存的编辑对话框状态（onSaveInstanceState 快照），用于重建后恢复及选封面兜底。 */
    private Bundle lastEditDialogState;
    private long runningGameId = -1;
private long runningSessionId = -1;
private long sessionStart = 0;
private boolean launchedExternal = false;
private StorageProbeResult lastStorageProbeResult;
private long lastStorageProbeAt;
private static final long MIN_PLAY_SESSION_MS = 0L;
private static final long MAX_PLAY_SESSION_MS = 12L * 60L * 60L * 1000L;
private static final long STORAGE_PROBE_TIMEOUT_MS = 1000L;
    private boolean coverScanRunning = false;
    private boolean coverMetadataRepairRunning = false;
    private boolean coverMaintenanceDone = false;
    private boolean autoLibraryScanRunning = false;
    private boolean webDavAutoSyncRunning = false;
    private boolean scanLoadingAnimated = false;
    private ObjectAnimator scanAnimator;
    private ImageView ivScanLoading;
    private SharedPreferences prefs;
    private static final String PREFS_NAME = "yukihub_prefs";
    private static final String KEY_LAST_SCAN_ROOT_URI = "last_scan_root_uri";
    private static final String KEY_SCAN_ROOT_URIS = "scan_root_uris";
    private static final String KEY_USE_BUILTIN_FILE_CHOOSER = "use_builtin_file_chooser"; // true=内置, false=原生SAF
    private static final String KEY_SCAN_ROOT_ENABLED = "scan_root_enabled"; // 保存每个目录的开关状态
    private static final int MAX_SCAN_ROOTS = 3;
    // .nomedia 询问记录：按扫描根 uri 存（不按下标，避免删除中间项时错位）
    private static final String KEY_NOMEDIA_ASKED = "nomedia_asked_uris";
    private static final String KEY_STARTUP_SCAN_DEPTH = "startup_scan_depth";
    private static final String KEY_AUTO_SCAN_ON_STARTUP = "auto_scan_on_startup";
    private static final String KEY_SCAN_MODE = "scan_mode"; // fast / legacy
    private static final String SCAN_MODE_FAST = "fast";
    private static final String SCAN_MODE_LEGACY = "legacy";
    private static final String KEY_CHECK_UPDATE_ON_STARTUP = "check_update_on_startup";
    private static final String KEY_LAST_UPDATE_CHECK_AT = "last_update_check_at";
    private static final String KEY_UPDATE_SOURCE = "update_source";
    private static final String UPDATE_SOURCE_GITCODE = "gitcode";
    private static final String UPDATE_SOURCE_GITHUB = "github";
    private static final long UPDATE_AUTO_CHECK_INTERVAL_MS = 12L * 60L * 60L * 1000L;
    private static final String UPDATE_GITHUB_API_URL = "https://api.github.com/repos/xm486/YukiHub/releases/latest";
    private static final String UPDATE_GITHUB_REPO_URL = "https://github.com/xm486/YukiHub";
    private static final String UPDATE_GITCODE_API_URL = "https://gitcode.com/api/v5/repos/xm486/YukiHub/releases/latest";
    private static final String UPDATE_GITCODE_REPO_URL = "https://gitcode.com/xm486/YukiHub";
    private static final String KEY_ENGINE_LABEL_POSITION = "engine_label_position";
    private static final int DEFAULT_STARTUP_SCAN_DEPTH = 2;
    private static final int MAX_STARTUP_SCAN_DEPTH = 4;
    private static final String KEY_KR_COMPAT_MODE = "kr_compat_mode";
private static final String KEY_KR_ENGINE_VERSION = "kr_engine_version";
private static final String KEY_KR_SCOPED_SAVE_DIR = "kr_scoped_save_dir";
private static final String KEY_KR_INTERNAL_RUN = "kr_internal_run";
private static final String KEY_ARTEMIS_SCOPED_SAVE_DIR = "artemis_scoped_save_dir";
/** 编辑对话框重建恢复：标记/状态/表单字段 key（onSaveInstanceState 保存）。 */
private static final String KEY_EDIT_DIALOG_OPEN = "edit_dialog_open";
private static final String KEY_EDIT_STATE = "edit_dialog_state";
private static final String KEY_EDIT_GAME_ID = "edit_game_id";
private static final String KEY_EDIT_TITLE = "edit_title";
private static final String KEY_EDIT_PKG = "edit_pkg";
private static final String KEY_EDIT_DESC = "edit_desc";
private static final String KEY_EDIT_GAMEHUB_ID = "edit_gamehub_id";
private static final String KEY_EDIT_ENGINE = "edit_engine";
private static final String KEY_EDIT_LAUNCH_TARGET = "edit_launch_target";
private static final String KEY_EDIT_WINLATOR_MODE = "edit_winlator_mode";
private static final String KEY_EDIT_GAMEHUB_MODE = "edit_gamehub_mode";
private static final String KEY_EDIT_DIR_URI = "edit_dir_uri";
private static final String KEY_EDIT_COVER_URI = "edit_cover_uri";
/** KRKR 引擎允许绘制刘海/挖孔区域（默认开启）。 */
public static final String KEY_KR_DRAW_CUTOUT = "kr_draw_cutout";
/** Artemis 引擎允许绘制刘海/挖孔区域（默认开启）。 */
public static final String KEY_ARTEMIS_DRAW_CUTOUT = "artemis_draw_cutout";
/** 主界面（首页/游戏库/登录页）允许绘制刘海/挖孔区域（默认关闭，可在首页设置开启）。 */
public static final String KEY_MAIN_DRAW_CUTOUT = "main_draw_cutout";
private static final String KEY_SORT_MODE = "sort_mode";
private static final String SORT_MODE_RECENT = "recent";
private static final String SORT_MODE_NAME = "name";
private static final String SORT_MODE_NEWEST = "newest";
private static final String KEY_PROFILE_NAME = "profile_name";
    private static final String KEY_AUTH_ACCESS_TOKEN = "auth_access_token";
private static final String KEY_AUTH_REFRESH_TOKEN = "auth_refresh_token";
private static final String KEY_AUTH_USER_ID = "auth_user_id";
private static final String KEY_AUTH_UID = "auth_uid";
private static final String KEY_AUTH_NICKNAME = "auth_nickname";
private static final String KEY_AUTH_AVATAR = "auth_avatar";
private static final String KEY_AUTH_EMAIL = "auth_email";
private static final String KEY_AUTH_STATUS = "auth_status";
private static final String KEY_KUN_BOUND = "kungal_bound";
private static final String KEY_KUN_OAUTH_STATE = "kun_oauth_state";
    private static final String KEY_KUN_OAUTH_CODE_VERIFIER = "kun_oauth_code_verifier";
    private static final String KEY_KUN_OAUTH_STARTED_AT = "kun_oauth_started_at";
    private static final String KEY_KUN_OAUTH_MODE = "kun_oauth_mode";
    private static final String KUN_OAUTH_MODE_BIND = "bind";
    private static final String KUN_ANDROID_CLIENT_ID = "16cc006913d6b666c6b1a1a115f644de";
    // M19-1：账号中心迁移 —— oauth.kungal.com → account.nextmoe.com（同 AuthActivity）
    // M19-2：用户可见品牌更名「NextMoe·未萌」（原「鲲 Galgame」）；KUN_*/kungal 为历史命名，保留勿改。
    private static final String KUN_OAUTH_AUTHORIZE_URL = "https://account.nextmoe.com/api/v1/oauth/authorize";
    private static final String KUN_OAUTH_REDIRECT_URI = "yukihub://oauth/callback";
    private static final String KUN_OAUTH_SCOPE = "openid profile email";
    private static final String KEY_HIKARINAGI_BOUND = "hikarinagi_bound";
    private static final String KEY_HIKARINAGI_OAUTH_STATE = "hikarinagi_oauth_state";
    private static final String KEY_HIKARINAGI_OAUTH_CODE_VERIFIER = "hikarinagi_oauth_code_verifier";
    private static final String KEY_HIKARINAGI_OAUTH_NONCE = "hikarinagi_oauth_nonce";
    private static final String KEY_HIKARINAGI_OAUTH_STARTED_AT = "hikarinagi_oauth_started_at";
    private static final String KEY_HIKARINAGI_OAUTH_MODE = "hikarinagi_oauth_mode";
    private static final String HIKARINAGI_OAUTH_MODE_BIND = "bind";
    private static final String HIKARINAGI_ANDROID_CLIENT_ID = "hkn_qtmXMJfBoxcNLA-a";
    private static final String HIKARINAGI_OAUTH_AUTHORIZE_URL = "https://id.hikarinagi.org/oidc/auth";
    private static final String HIKARINAGI_OAUTH_REDIRECT_URI = "yukihub://hikarinagi/callback";
    private static final String HIKARINAGI_OAUTH_SCOPE = "openid user:read";
private static final String AUTH_BASE_URL = "https://yukihub.zh.kg/api";
private static final String KEY_CLOUD_SYNC_ENABLED = "cloud_sync_enabled";
private static final String KEY_SHARE_PLAYING = "share_playing_status";
private static final String KEY_FRIEND_PLAY_NOTIFY = "friend_play_notify";
private static final String KEY_LAST_SYNC_AT = "last_sync_at";
private static final String AUTH_STATUS_ONLINE = "online";
    private static final String BROWSER_UA = "Mozilla/5.0 (Linux; Android 15; Mobile) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.6723.58 Mobile Safari/537.36";
private static final String AUTH_STATUS_OFFLINE = "offline";
private static final String AUTH_STATUS_EXPIRED = "expired";
private static final String AUTH_STATUS_SYNCING = "syncing";
private static final String KEY_PROFILE_SIGNATURE = "profile_signature";
private static final String KEY_PROFILE_AVATAR = "profile_avatar";
private static final String EXTRA_HOME_TARGET = "home_target";
private static final String EXTRA_HOME_GAME_ID = "home_game_id";
private static final String HOME_TARGET_PROFILE = "profile";
private static final String HOME_TARGET_SETTINGS = "settings";
private static final String HOME_TARGET_LAUNCH_GAME = "launch_game";
private static final String HOME_TARGET_FRIENDS = "friends";
private static final String KEY_CUSTOM_BACKGROUND = "custom_background";
private static final String KEY_CUSTOM_BACKGROUND_TYPE = "custom_background_type";
private static final String KEY_BACKGROUND_DIM_ENABLED = "background_dim_enabled";
private static final String KEY_BACKGROUND_VIDEO_SOUND = "background_video_sound";
private static final String KEY_BG_THEME_ENABLED = "bg_theme_enabled";
private static final String KEY_UI_CLICK_SOUND = "ui_click_sound";
    public static final String KEY_NSFW_BLUR = "nsfw_blur_enabled";
    private static final int UI_SOUND_CLICK = 0;
private static final int UI_SOUND_CONFIRM = 1;
private static final int UI_SOUND_SWITCH = 2;
    private static final String KEY_DISCLAIMER_ACCEPTED = "disclaimer_accepted";
    private static final String KEY_DISCLAIMER_ACCEPTED_AT = "disclaimer_accepted_at";
    private static final String KEY_DISCLAIMER_ACCEPTED_VERSION = "disclaimer_accepted_version";
    private static final int DISCLAIMER_VERSION = 2;
    private static final String KEY_GAME_COLUMNS = "game_columns";
    private static final int DEFAULT_GAME_COLUMNS = 5;
private int pendingScanRootReplaceIndex = -2;
private LinearLayout activeScanRootList;
private TextView activeScanRootInfo;

    private ActivityResultLauncher<Uri> scanDirLauncher;
    private ActivityResultLauncher<Uri> editDirLauncher;
private ActivityResultLauncher<String> coverLauncher;
private ActivityResultLauncher<String> profileAvatarLauncher;
private ActivityResultLauncher<String> backgroundPickerLauncher;
private ActivityResultLauncher<String> videoBackgroundPickerLauncher;
private ActivityResultLauncher<String> cursorIconPickerLauncher;
    /** Windows 光标包（文件夹）导入。 */
    private ActivityResultLauncher<Uri> cursorSchemePickerLauncher;
    /** FVP 引擎：自定义字体文件选择。 */
    private ActivityResultLauncher<String> fvpFontLauncher;
    /**
     * FVP 设置对话框正在编辑的偏好对象与对应游戏 id。
     *
     * 字体选择是异步回调，回来时必须改**对话框正在持有的那个** prefs，
     * 否则点「保存」时会用旧快照把这次选择覆盖掉（与虚拟鼠标图标同一套路）。
     */
    private FvpLaunchPrefs fvpEditingPrefs;
    private long fvpEditingGameId = -1L;
    /** FVP 设置对话框里的「当前字体」文案控件；对话框关闭时置 null。 */
    private TextView fvpFontPathView;
    /** 当前打开的虚拟鼠标设置对话框里的预览控件；关闭时置 null。 */
    private CursorPreviewView gameCursorPreview;
    /**
     * 当前正在编辑的虚拟鼠标配置对象；对话框关闭时置 null。
     *
     * 必须存在：导入光标包/选图是异步回调，回来时得改**对话框正在持有的那个**
     * cfg 对象。否则另 load 一份副本改完保存，用户点"保存"时对话框里的旧快照
     * 又会把结果覆盖回去。
     */
    private GameCursorConfig gameCursorEditing;
    /** 设置对话框里显示"当前外观来源"的那行文字；随导入结果更新。 */
    private TextView gameCursorSourceStatus;
private MediaPlayer backgroundMediaPlayer;
private SoundPool uiSoundPool;
    private int uiClickSoundId;
    private int uiConfirmSoundId;
    private int uiSwitchSoundId;
    private AlertDialog pendingProfileDialog;
    private com.yuki.yukihub.social.PresenceManager presenceManager;
private boolean activityHeartbeatHeld = false;
private long lastUiSoundAt;
private Uri pendingBackgroundVideoUri;
private ActivityResultLauncher<String> backupCreateLauncher;
    private ActivityResultLauncher<String[]> backupOpenLauncher;
    private ActivityResultLauncher<String[]> playniteImportLauncher;
    private ActivityResultLauncher<String[]> potatovnImportLauncher;
    private ActivityResultLauncher<Uri> vniteImportLauncher;
    private ActivityResultLauncher<String[]> lunaboxImportLauncher;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // M14：全 app 手柄适配 —— 给整个界面装上焦点高亮（之前只有系统默认的"一点点变灰"）
        com.yuki.yukihub.ui.GamepadFocus.attach(this);
        // 聊天选图 launcher 必须在 STARTED 之前注册（供 FriendsChatDialog 借用）
        com.yuki.yukihub.social.ChatImagePicker.register(this);
        enterImmersiveMode();
        repository = new GameRepository(this);
metadataRepository = new MetadataRepository(this);
prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        aiReviewController = new AiReviewController(this, new AiReviewController.Delegate() {
            @Override public GameRepository gameRepository() { return repository; }
            @Override public MetadataRepository metadataRepository() { return MainActivity.this.metadataRepository; }
            @Override public List<Game> allGames() { return allGames; }
            @Override public SharedPreferences prefs() { return prefs; }
            @Override public String visibleMetadataSource(long gameId) { return MainActivity.this.visibleMetadataSource(gameId); }
            @Override public VnMetadata metadataForSource(long gameId, String source) { return MainActivity.this.metadataForSource(gameId, source); }
            @Override public VnMetadata anyCachedMetadata(long gameId) { return MainActivity.this.anyCachedMetadata(gameId); }
            @Override public boolean usingYmgal() { return MainActivity.this.usingYmgal(); }
            @Override public boolean usingHikarinagi() { return MainActivity.this.usingHikarinagi(); }
            @Override public boolean usingNextMoe() { return metadataController.usingNextMoe(); }
            @Override public boolean usingBangumi() { return MainActivity.this.usingBangumi(); }
            @Override public String bangumiToken() { return MainActivity.this.bangumiToken(); }
            @Override public String buildMetadataSearchKeyword(String title) { return MainActivity.this.buildMetadataSearchKeyword(title); }
            @Override public boolean isConfidentMatch(String localTitle, VnMetadata meta) { return MainActivity.this.isConfidentMatch(localTitle, meta); }
            @Override public int dp(int value) { return MainActivity.this.dp(value); }
            @Override public int getColorCompat(int id) { return MainActivity.this.getColorCompat(id); }
            @Override public Button krButton(String text) { return MainActivity.this.krButton(text); }
            @Override public Spinner krSpinner(String[] values, String selected) { return MainActivity.this.krSpinner(values, selected); }
            @Override public CheckBox krCheckBox(String text, boolean checked) { return MainActivity.this.krCheckBox(text, checked); }
            @Override public void styleAlertDialogDark(AlertDialog dialog) { MainActivity.this.styleAlertDialogDark(dialog); }
            @Override public void applyImmersiveToWindow(Window window) { MainActivity.this.applyImmersiveToWindow(window); }
            @Override public void playUiSound(int type) { MainActivity.this.playUiSound(type); }
            @Override public String emptyText(String s, String fallback) { return MainActivity.this.emptyText(s, fallback); }
            @Override public String normalizePlayStatus(String status) { return MainActivity.this.normalizePlayStatus(status); }
            @Override public String safeCoverUri(Game g) { return MainActivity.this.safeCoverUri(g); }
            @Override public String initials(String title) { return MainActivity.this.initials(title); }
        });
        metadataController = new MetadataController(new MetadataController.Delegate() {
            @Override public SharedPreferences prefs() { return prefs; }
            @Override public MetadataRepository metadataRepository() { return MainActivity.this.metadataRepository; }
            @Override public GameRepository gameRepository() { return repository; }
            @Override public List<Game> allGames() { return allGames; }
            @Override public void runOnUiThread(Runnable r) { MainActivity.this.runOnUiThread(r); }
            @Override public android.app.Activity activity() { return MainActivity.this; }
            @Override public Game selectedGame() { return selectedGame; }
            @Override public void setSelectedGame(Game game) { selectedGame = game; }
            @Override public VnMetadata currentSideMetadata() { return MainActivity.this.currentSideMetadata; }
            @Override public void setCurrentSideMetadata(VnMetadata meta) { MainActivity.this.currentSideMetadata = meta; }
            @Override public boolean sideShowingTranslatedDescription() { return sideShowingTranslatedDescription; }
            @Override public void setSideShowingTranslatedDescription(boolean value) { sideShowingTranslatedDescription = value; }
            @Override public String sideFullDescription() { return sideFullDescription; }
            @Override public void setSideFullDescription(String text) { sideFullDescription = text; }
            @Override public TextView sideDetailTitle() { return sideDetailTitle; }
            @Override public TextView sideDetailOriginalTitle() { return sideDetailOriginalTitle; }
            @Override public TextView sideDetailHint() { return sideDetailHint; }
            @Override public TextView sideDetailPath() { return sideDetailPath; }
            @Override public TextView sideDetailDeveloper() { return sideDetailDeveloper; }
            @Override public TextView sideDetailDate() { return sideDetailDate; }
            @Override public TextView sideDetailRating() { return sideDetailRating; }
            @Override public TextView sideDetailLength() { return sideDetailLength; }
            @Override public TextView sideDetailTags() { return sideDetailTags; }
            @Override public TextView sideDescToggle() { return sideDescToggle; }
            @Override public TextView sideTranslateToggle() { return sideTranslateToggle; }
            @Override public TextView sideMetadataSourceBadge() { return sideMetadataSourceBadge; }
            @Override public ImageView sideDetailCover() { return sideDetailCover; }
            @Override public View sideDetailPlaceholder() { return sideDetailPlaceholder; }
            @Override public ImageView sideScreenshot1() { return sideScreenshot1; }
            @Override public ImageView sideScreenshot2() { return sideScreenshot2; }
            @Override public LinearLayout sideTagContainer() { return sideTagContainer; }
            @Override public TextView sideBtnLaunch() { return sideBtnLaunch; }
            @Override public TextView sideBtnOptions() { return sideBtnOptions; }
            @Override public GameAdapter adapter() { return adapter; }
            @Override public int dp(int value) { return MainActivity.this.dp(value); }
            @Override public int getColorCompat(int id) { return MainActivity.this.getColorCompat(id); }
            @Override public void styleAlertDialogDark(AlertDialog dialog) { MainActivity.this.styleAlertDialogDark(dialog); }
            @Override public void playUiSound(int type) { MainActivity.this.playUiSound(type); }
            @Override public void applyImmersiveToWindow(Window window) { MainActivity.this.applyImmersiveToWindow(window); }
            @Override public void loadRemoteImage(String url, ImageView target, String prefix) { MainActivity.this.loadRemoteImage(url, target, prefix); }
            @Override public void setSideDescription(String text) { MainActivity.this.setSideDescription(text); }
            @Override public void renderSideDescription() { MainActivity.this.renderSideDescription(); }
            @Override public void renderTagChips(String tagsText) { MainActivity.this.renderTagChips(tagsText); }
            @Override public void updateTranslateButtonState() { MainActivity.this.updateTranslateButtonState(); }
            @Override public String emptyText(String s, String fallback) { return MainActivity.this.emptyText(s, fallback); }
            @Override public String safeCoverUri(Game g) { return MainActivity.this.safeCoverUri(g); }
            @Override public String initials(String title) { return MainActivity.this.initials(title); }
            @Override public String displayPath(String value) { return MainActivity.this.displayPath(value); }
            @Override public File persistentRemoteCoverDir() { return MainActivity.this.persistentRemoteCoverDir(); }
            @Override public File cacheDir() { return getCacheDir(); }
            @Override public boolean isMissingFileUri(String uriText) { return MainActivity.this.isMissingFileUri(uriText); }
            @Override public void loadGames() { MainActivity.this.loadGames(); }
            @Override public void updateSideDetail(Game game) { MainActivity.this.updateSideDetail(game); }
            @Override public void showEditDialog(Game game) { MainActivity.this.showEditDialog(game); }
            @Override public void showPlayStatusDialog(Game game, Dialog parentDialog) { MainActivity.this.showPlayStatusDialog(game, parentDialog); }
            @Override public void showEditPlayTimeDialog(Game game) { MainActivity.this.showEditPlayTimeDialog(game); }
            @Override public void showKrSettingsDialog(Game game) { MainActivity.this.showKrSettingsDialog(game); }
            @Override public void showOnsSettingsDialog(Game game) { MainActivity.this.showOnsSettingsDialog(game); }
            @Override public void showDetailDialog(Game game) { MainActivity.this.showDetailDialog(game); }
            @Override public void confirmDeleteGame(Game game) { MainActivity.this.confirmDeleteGame(game); }
            @Override public void showToast(String text, int duration) { Toast.makeText(MainActivity.this, text, duration).show(); }
            @Override public void updateProfilePanel() { MainActivity.this.updateProfilePanel(); }
            @Override public String translateTextToChinese(String text) throws Exception { return MainActivity.this.translateTextToChinese(text); }
        });
        try { DevLogger.init(this); } catch (Throwable ignored) { }
        try { setVolumeControlStream(AudioManager.STREAM_MUSIC); } catch (Throwable ignored) { }
        try { ensureUiSoundPool(); } catch (Throwable ignored) { }
if (!ensureDisclaimerAccepted()) {
            return;
        }
        applyCustomBackground();
        repository.deleteSampleGames();
        finishStalePlaySessionsIfAny();
        setupLaunchers();
        setupUi();
        loadGames();
        if (ivScanLoading != null) ivScanLoading.setVisibility(View.GONE);
        if (prefs != null && prefs.getBoolean(KEY_AUTO_SCAN_ON_STARTUP, false)) {
            autoScanLastRootIfAvailable();
        }
        checkUpdateOnStartupIfEnabled();
        ensureStoragePermissionForInternalKrkr();
        handleHomeTargetIntent(getIntent());
        // 编辑对话框可能在选封面/选目录期间因 Activity 重建而丢失：
        // 检测到保存的编辑状态时，延迟重新弹出对话框并回填表单。
        if (savedInstanceState != null && savedInstanceState.getBoolean(KEY_EDIT_DIALOG_OPEN, false)) {
            final Bundle editState = savedInstanceState.getBundle(KEY_EDIT_STATE);
            if (editState != null) {
                lastEditDialogState = editState;
                getWindow().getDecorView().post(() -> restoreEditDialog(editState));
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // 编辑对话框打开时保存快照，Activity 重建（旋转/内存回收）后恢复编辑现场。
        if (pendingEditDialog != null && pendingEditDialog.isShowing()) {
            Bundle state = captureEditDialogState();
            if (state != null) {
                lastEditDialogState = state;
                outState.putBoolean(KEY_EDIT_DIALOG_OPEN, true);
                outState.putBundle(KEY_EDIT_STATE, state);
            }
        }
    }

    /** 读取编辑对话框当前表单，生成状态快照。 */
    private Bundle captureEditDialogState() {
        if (pendingEditDialog == null) return null;
        Bundle b = new Bundle();
        b.putLong(KEY_EDIT_GAME_ID, editingGameId);
        b.putString(KEY_EDIT_DIR_URI, pendingDirUri);
        b.putString(KEY_EDIT_COVER_URI, pendingCoverUri);
        EditText title = pendingEditDialog.findViewById(R.id.etGameTitle);
        if (title != null) b.putString(KEY_EDIT_TITLE, title.getText() == null ? "" : title.getText().toString());
        EditText pkg = pendingEditDialog.findViewById(R.id.etEmulatorPackage);
        if (pkg != null) b.putString(KEY_EDIT_PKG, pkg.getText() == null ? "" : pkg.getText().toString());
        EditText desc = pendingEditDialog.findViewById(R.id.etDescription);
        if (desc != null) b.putString(KEY_EDIT_DESC, desc.getText() == null ? "" : desc.getText().toString());
        EditText ghId = pendingEditDialog.findViewById(R.id.etGameHubLocalGameId);
        if (ghId != null) b.putString(KEY_EDIT_GAMEHUB_ID, ghId.getText() == null ? "" : ghId.getText().toString());
        Spinner engineSp = pendingEditDialog.findViewById(R.id.spEngine);
        if (engineSp != null && engineSp.getSelectedItem() != null) b.putString(KEY_EDIT_ENGINE, engineSp.getSelectedItem().toString());
        Spinner launchSp = pendingEditDialog.findViewById(R.id.spLaunchTarget);
        if (launchSp != null && launchSp.getSelectedItem() != null) b.putString(KEY_EDIT_LAUNCH_TARGET, launchSp.getSelectedItem().toString());
        Spinner winSp = pendingEditDialog.findViewById(R.id.spWinlatorLaunchMode);
        if (winSp != null && winSp.getSelectedItem() != null) b.putString(KEY_EDIT_WINLATOR_MODE, winSp.getSelectedItem().toString());
        Spinner ghSp = pendingEditDialog.findViewById(R.id.spGameHubLaunchMode);
        if (ghSp != null && ghSp.getSelectedItem() != null) b.putString(KEY_EDIT_GAMEHUB_MODE, ghSp.getSelectedItem().toString());
        return b;
    }

    private void refreshRuntimeAccountState() {
        if (prefs == null) return;
        updateProfilePanel();
        try { if (adapter != null) adapter.notifyDataSetChanged(); } catch (Throwable ignored) { }
    }

private void handleHomeTargetIntent(Intent intent) {
        if (intent == null) return;
        refreshRuntimeAccountState();
        String target = intent.getStringExtra(EXTRA_HOME_TARGET);
        if (target == null || target.trim().isEmpty()) return;
        long targetGameId = intent.getLongExtra(EXTRA_HOME_GAME_ID, -1L);
        // 通知点击直达会话（与 HomeActivity 同一套 extra）
        final String chatFriendId = intent.getStringExtra("chat_friend_id");
        final int chatGroupId = intent.getIntExtra("chat_group_id", 0);
        intent.removeExtra(EXTRA_HOME_TARGET);
        intent.removeExtra(EXTRA_HOME_GAME_ID);
        intent.removeExtra("chat_friend_id");
        intent.removeExtra("chat_group_id");
        getWindow().getDecorView().post(() -> {
            if (isFinishing() || (android.os.Build.VERSION.SDK_INT >= 17 && isDestroyed())) return;
            if (HOME_TARGET_PROFILE.equals(target)) {
                if (pendingProfileDialog != null && pendingProfileDialog.isShowing()) pendingProfileDialog.dismiss();
                showProfileDialog();
            } else if (HOME_TARGET_SETTINGS.equals(target)) {
                showSettingsDialog();
            } else if (HOME_TARGET_LAUNCH_GAME.equals(target)) {
                // 未完成记录弹窗是模态的，此时启动会让游戏跑在弹窗背后。
                // 挂起请求，等弹窗关闭后由 consumePendingShortcutLaunch 接手。
                if (staleSessionDialogShowing) {
                    pendingShortcutGameId = targetGameId;
                } else {
                    launchGameFromHome(targetGameId);
                }
            } else if (HOME_TARGET_FRIENDS.equals(target)) {
                if (chatFriendId != null && !chatFriendId.isEmpty()) {
                    new com.yuki.yukihub.social.FriendsChatDialog(this)
                            .showAndOpenFriendChat(chatFriendId);
                } else if (chatGroupId > 0) {
                    new com.yuki.yukihub.social.FriendsChatDialog(this)
                            .showAndOpenGroupChat(chatGroupId);
                } else {
                    showFriendsChatPlaceholder();
                }
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleHomeTargetIntent(intent);
    }

    private void launchGameFromHome(long gameId) {
        if (gameId <= 0L) {
            Toast.makeText(this, "The game to launch cannot be recognized", Toast.LENGTH_SHORT).show();
            return;
        }
        Game target = null;
        for (Game game : allGames) {
            if (game != null && game.id == gameId) {
                target = game;
                break;
            }
        }
        if (target == null && repository != null) {
            List<Game> latest = repository.getAll();
            if (latest != null) {
                for (Game game : latest) {
                    if (game != null && game.id == gameId) {
                        target = game;
                        break;
                    }
                }
            }
        }
        if (target == null) {
            Toast.makeText(this, "The game does not exist or has been deleted", Toast.LENGTH_SHORT).show();
            return;
        }
        selectedGame = target;
        updateSideDetail(target);
        launchGame(target);
    }

    private boolean ensureDisclaimerAccepted() {
        if (prefs == null) return false;
        long acceptedAt = prefs.getLong(KEY_DISCLAIMER_ACCEPTED_AT, 0L);
        boolean accepted = prefs.getBoolean(KEY_DISCLAIMER_ACCEPTED, false);
        int acceptedVersion = prefs.getInt(KEY_DISCLAIMER_ACCEPTED_VERSION, 0);
        // 版本号变化时重新展示新声明（老用户升级后也能看到）
        if (accepted && acceptedAt > 0 && acceptedVersion == DISCLAIMER_VERSION) return true;
        View content = LayoutInflater.from(this).inflate(R.layout.dialog_disclaimer_first_launch, null, false);
        tintDialogRoot(content);
        CheckBox agree = content.findViewById(R.id.cbDisclaimerAgree);
        TextView btnExit = content.findViewById(R.id.btnDisclaimerExit);
        TextView btnContinue = content.findViewById(R.id.btnDisclaimerContinue);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(content)
                .setCancelable(false)
                .create();
        dialog.show();
        styleAlertDialogDark(dialog);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.72f), (int) (getResources().getDisplayMetrics().heightPixels * 0.78f));
        }
        Runnable refreshContinueState = () -> {
            boolean enabled = agree.isChecked();
            btnContinue.setEnabled(enabled);
            btnContinue.setAlpha(enabled ? 1f : 0.45f);
        };
        refreshContinueState.run();
        agree.setOnCheckedChangeListener((buttonView, isChecked) -> refreshContinueState.run());
        btnExit.setOnClickListener(v -> finish());
        btnContinue.setOnClickListener(v -> {
            if (!agree.isChecked()) return;
            prefs.edit().putBoolean(KEY_DISCLAIMER_ACCEPTED, true)
                                .putLong(KEY_DISCLAIMER_ACCEPTED_AT, System.currentTimeMillis())
                                .putInt(KEY_DISCLAIMER_ACCEPTED_VERSION, DISCLAIMER_VERSION)
                                .apply();
            dialog.dismiss();
            recreate();
        });
        return false;
    }

    private void ensureStoragePermissionForInternalKrkr() {
        try {
            if (Build.VERSION.SDK_INT >= 30) {
                if (!Environment.isExternalStorageManager()) {
                    new AlertDialog.Builder(this)
                            .setTitle("File access required")
                            .setMessage("内置引擎（KRKR / FVP）需要访问外部存储来读取游戏文件。请在系统页面允许“管理所有文件”。")
                            .setPositiveButton("To authorize", (d, w) -> {
                                try {
                                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                                    intent.setData(Uri.parse("package:" + getPackageName()));
                                    startActivity(intent);
                                } catch (Throwable t) {
                                    try { startActivity(new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)); } catch (Throwable ignored) { }
                                }
                            })
                            .setNegativeButton("Later", null)
                            .show();
                }
            } else if (Build.VERSION.SDK_INT >= 23) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1001);
            }
        } catch (Throwable ignored) { }
    }

    private void enterImmersiveMode() {
Window window = getWindow();
applyImmersiveToWindow(window);
}

private void applyImmersiveToWindow(Window window) {
 if (window == null) return;
 window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
 // 主界面可选：是否绘制到刘海/挖孔区域（首页设置里开关，默认开启）
 com.yuki.yukihub.util.CutoutCompat.setCutoutMode(window, prefs != null && prefs.getBoolean(KEY_MAIN_DRAW_CUTOUT, true));
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
| View.SYSTEM_UI_FLAG_LAYOUT_STABLE
);
}

    private void setupLaunchers() {
        scanDirLauncher = registerForActivityResult(new ActivityResultContracts.OpenDocumentTree(), uri -> {
if (uri != null) {
takeFlags(uri);
boolean changed = addOrReplaceScanRoot(uri.toString(), pendingScanRootReplaceIndex);
pendingScanRootReplaceIndex = -2;
if (changed) {
refreshActiveScanRootListUi();
Toast.makeText(MainActivity.this, "Scan catalog updated", Toast.LENGTH_SHORT).show();
maybePromptNoMedia(uri.toString());
}
} else {
pendingScanRootReplaceIndex = -2;
}
});
        editDirLauncher = registerForActivityResult(new ActivityResultContracts.OpenDocumentTree(), uri -> {
            if (uri != null) {
                takeFlags(uri);
                pendingDirUri = uri.toString();
                if (pendingCoverUri == null || pendingCoverUri.isEmpty()) {
                    Uri autoCover = findFirstLevelImage(pendingDirUri);
                    if (autoCover != null) pendingCoverUri = copyCoverToInternalStorage(autoCover);
                }
                if (pendingEditDialog != null) {
                    ((TextView) pendingEditDialog.findViewById(R.id.tvSelectedDir)).setText(pendingDirUri);
                    Spinner launchSp = pendingEditDialog.findViewById(R.id.spLaunchTarget);
                    List<String> options = buildLaunchOptions(pendingDirUri);
                    ArrayAdapter<String> adapter = krSpinnerAdapter(options.toArray(new String[0]));
                    launchSp.setAdapter(adapter);
                    ((TextView) pendingEditDialog.findViewById(R.id.tvSelectedCover)).setText(emptyText(pendingCoverUri, "No cover selected"));
                    updateClearDirButton(pendingEditDialog.findViewById(R.id.btnClearDir), pendingDirUri);
                }
            }
        });
        coverLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                pendingCoverUri = copyCoverToInternalStorage(uri);
                if (pendingEditDialog != null) {
                    ((TextView) pendingEditDialog.findViewById(R.id.tvSelectedCover)).setText(pendingCoverUri == null ? "Cover copy failed" : pendingCoverUri);
                } else {
                    // 对话框已随 Activity 重建丢失：不再静默，提示并尝试用快照恢复
                    Toast.makeText(MainActivity.this,
                            pendingCoverUri == null ? "Cover copy failed" : "Cover selected, restoring editing window...",
                            Toast.LENGTH_SHORT).show();
                    if (lastEditDialogState != null) {
                        final Bundle s = lastEditDialogState;
                        if (pendingCoverUri != null) s.putString(KEY_EDIT_COVER_URI, pendingCoverUri);
                        getWindow().getDecorView().post(() -> restoreEditDialog(s));
                    }
                }
            }
        });
profileAvatarLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                String avatar = copyImageToInternalStorage(uri, "avatars", "avatar_", 320, 90);
                if (avatar == null || avatar.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Avatar failed to save", Toast.LENGTH_SHORT).show();
                    return;
                }
                prefs.edit().putString(KEY_PROFILE_AVATAR, avatar).remove(KEY_AUTH_AVATAR).apply();
                updateProfilePanel();
                showProfileDialog();
                // 立即上传新头像到服务器
                AppExecutors.runOnIo(() -> {
                    try {
                        com.yuki.yukihub.sync.SyncManager syncMgr = new com.yuki.yukihub.sync.SyncManager(MainActivity.this);
                        syncMgr.uploadAvatarNow();
                    } catch (Throwable ignored) {}
                });
            }
        });
        backgroundPickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
String bg = copyImageToInternalStorage(uri, "backgrounds", "bg_", 1920, 88);
                if (bg == null || bg.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Background saving failed", Toast.LENGTH_SHORT).show();
                    return;
                }
                replaceCustomBackground(bg, "image");
                applyCustomBackground();
                Toast.makeText(MainActivity.this, "Image background set", Toast.LENGTH_SHORT).show();
            }
        });
        videoBackgroundPickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                String bg = copyVideoToInternalStorage(uri);
                if (bg == null || bg.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Video background saving failed", Toast.LENGTH_SHORT).show();
                    return;
                }
                replaceCustomBackground(bg, "video");
                applyCustomBackground();
                Toast.makeText(MainActivity.this, "Video background set", Toast.LENGTH_SHORT).show();
            }
        });
        // 游戏内虚拟鼠标：光标图标选择（复制到内部存储，避免 content Uri 授权失效）
        cursorIconPickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri == null) return;
            String saved = copyImageToInternalStorage(uri, "cursors", "cursor_", 512, 100);
            if (saved == null || saved.isEmpty()) {
                Toast.makeText(MainActivity.this, "Cursor icon failed to save", Toast.LENGTH_SHORT).show();
                return;
            }
            // 必须改设置对话框正在持有的那个 cfg，不能另 load 一份 ——
            // 否则对话框点"保存"时会用旧快照把这次选择覆盖掉。
            GameCursorConfig cfg = gameCursorEditing != null
                    ? gameCursorEditing : GameCursorConfig.load(this);
            cfg.iconUri = saved;
            // PNG 与光标包互斥：光标包优先级更高，选了 PNG 就得把包卸掉，
            // 否则设置了 PNG 却看不到变化。
            if (cfg.hasWinCursor()) {
                cfg.winCursorName = null;
                com.yuki.yukihub.gamecursor.wincursor.WinCursorSupport.uninstall(this);
            }
            cfg.save(this);
            if (gameCursorPreview != null) gameCursorPreview.refresh(cfg);
            if (gameCursorSourceStatus != null) gameCursorSourceStatus.setText("Current: Custom image");
            Toast.makeText(MainActivity.this, "The cursor icon has been updated and takes effect in real time in the game", Toast.LENGTH_SHORT).show();
        });
        // 游戏内虚拟鼠标：Windows 光标包（.ani/.cur）整包导入
        cursorSchemePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.OpenDocumentTree(), uri -> {
                    if (uri == null) return;
                    onCursorSchemeFolderPicked(uri);
                });
        // FVP 引擎：自定义字体文件（复制到应用私有目录，避免 content Uri 授权失效）
        fvpFontLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri == null) return;
            String saved = copyFvpFontToInternalStorage(uri);
            if (saved == null || saved.isEmpty()) {
                Toast.makeText(MainActivity.this, "Failed to save font file", Toast.LENGTH_SHORT).show();
                return;
            }
            if (fvpEditingPrefs != null) {
                fvpEditingPrefs.fontPath = saved;
            } else if (fvpEditingGameId > 0) {
                // 对话框因 Activity 重建而丢失：直接把字体写进该游戏的设置，避免用户白选一次。
                FvpLaunchPrefs p = FvpLaunchPrefs.load(this, fvpEditingGameId);
                p.fontPath = saved;
                p.save(this, fvpEditingGameId);
            }
            if (fvpFontPathView != null) {
                fvpFontPathView.setText("Current: Custom font (" + new File(saved).getName() + "）");
            }
            Toast.makeText(MainActivity.this, "The font has been selected. Click \"Save\" to take effect next time you start it.", Toast.LENGTH_SHORT).show();
        });

        backupCreateLauncher = registerForActivityResult(new ActivityResultContracts.CreateDocument("application/octet-stream"), uri -> {
            if (uri != null) exportLocalBackup(uri);
        });
        backupOpenLauncher = registerForActivityResult(new ActivityResultContracts.OpenDocument(), uri -> {
            if (uri != null) importLocalBackup(uri);
        });

        // ===== 三方平台导入 =====
        playniteImportLauncher = registerForActivityResult(new ActivityResultContracts.OpenDocument(), uri -> {
            if (uri != null) doImportFromPlaynite(uri);
        });
        potatovnImportLauncher = registerForActivityResult(new ActivityResultContracts.OpenDocument(), uri -> {
            if (uri != null) doImportFromPotatoVN(uri);
        });
        vniteImportLauncher = registerForActivityResult(new ActivityResultContracts.OpenDocumentTree(), uri -> {
            if (uri != null) doImportFromVnite(uri);
        });
        lunaboxImportLauncher = registerForActivityResult(new ActivityResultContracts.OpenDocument(), uri -> {
            if (uri != null) doImportFromLunaBox(uri);
        });
    }

private File persistentRemoteCoverDir() {
        File dir = new File(getFilesDir(), "covers_remote");
        if (!dir.exists()) dir.mkdirs();
        return dir;
    }

    /**
     * FVP 自定义字体：把用户选中的字体文件复制到应用私有目录。
     *
     * 必须落盘：content Uri 的授权随时可能失效（重启、清理），而引擎启动时需要一个
     * 长期有效的真实文件路径。返回 null 表示失败。
     */
    private String copyFvpFontToInternalStorage(Uri uri) {
        if (uri == null) return null;
        try {
            File dir = new File(getFilesDir(), "fvp_fonts");
            if (!dir.exists() && !dir.mkdirs()) return null;
            File out = new File(dir, "font_" + System.currentTimeMillis() + ".ttf");
            try (java.io.InputStream in = getContentResolver().openInputStream(uri);
                 java.io.OutputStream os = new java.io.FileOutputStream(out)) {
                if (in == null) return null;
                byte[] buf = new byte[64 * 1024];
                long total = 0L;
                int n;
                while ((n = in.read(buf)) > 0) {
                    os.write(buf, 0, n);
                    total += n;
                    // 字体文件不该超过 64MB：超了说明选错了文件，及时止损。
                    if (total > 64L * 1024 * 1024) return null;
                }
            }
            return out.getAbsolutePath();
        } catch (Throwable t) {
            Log.w("YukiHub", "copyFvpFont failed", t);
            return null;
        }
    }

private boolean isMissingFileUri(String uriText) {
    if (uriText == null || uriText.trim().isEmpty()) return false;
    try {
        Uri uri = Uri.parse(uriText);
        if (!"file".equalsIgnoreCase(uri.getScheme())) return false;
        String path = uri.getPath();
        return path == null || !(new File(path).exists());
    } catch (Throwable ignored) {
        return false;
    }
}

private void repairMissingMetadataCoversIfNeeded() {
    if (allGames.isEmpty() || metadataRepository == null) return;
    if (coverMetadataRepairRunning) return;
    List<Game> targets = new ArrayList<>();
    for (Game g : allGames) {
        if (g == null || g.id <= 0) continue;
        // Target games that have no cover at all, OR have a cover URI pointing
        // to a file that no longer exists (e.g. restored from backup on a new
        // device where the cached cover file path is invalid).
        boolean noCover = !hasCover(g);
        boolean missingFile = isMissingFileUri(g.coverPersistUri) || isMissingFileUri(g.coverUri);
        if (noCover || missingFile) targets.add(g);
    }
    if (targets.isEmpty()) return;
    coverMetadataRepairRunning = true;
    AppExecutors.runOnIo(() -> {
        int changed = 0;
        for (Game g : targets) {
            try {
                VnMetadata meta = currentSourceMetadata(g.id);
                if (meta == null) meta = anyCachedMetadata(g.id);
                if (meta == null || meta.coverUrl == null || meta.coverUrl.trim().isEmpty()) continue;
                String cover = cacheRemoteImageSync(meta.coverUrl, "repair_cover_" + emptyText(meta.id, String.valueOf(g.id)));
                if (cover == null || cover.isEmpty()) continue;
                g.coverUri = cover;
                g.coverPersistUri = cover;
                g.coverSourceType = 1;
                repository.update(g);
                changed++;
            } catch (Throwable t) {
                Log.w("YukiHub", "repair cover failed: " + (g == null ? "null" : g.title), t);
            }
        }
        int finalChanged = changed;
        runOnUiThread(() -> {
            coverMetadataRepairRunning = false;
            if (finalChanged > 0) {
                allGames.clear();
                allGames.addAll(repository.getAll());
                applyFilter();
                if (selectedGame != null && containsGameId(allGames, selectedGame.id)) {
                    // Refresh side panel cover if it was the selected game
                    for (Game g : allGames) {
                        if (g.id == selectedGame.id) { selectedGame = g; break; }
                    }
                }
                Toast.makeText(MainActivity.this, "Restored" + finalChanged + "Synchronized covers", Toast.LENGTH_SHORT).show();
            }
        });
    });
}

private void deleteInternalFileUri(String uriText) {
    if (uriText == null || uriText.trim().isEmpty()) return;
    try {
        Uri uri = Uri.parse(uriText);
        if (!"file".equalsIgnoreCase(uri.getScheme())) return;
        String path = uri.getPath();
        if (path == null) return;
        File file = new File(path);
        File filesRoot = getFilesDir();
        String fp = file.getCanonicalPath();
        String rp = filesRoot.getCanonicalPath();
        if (fp.startsWith(rp) && file.exists()) file.delete();
    } catch (Throwable ignored) { }
}

private void replaceCustomBackground(String bg, String type) {
    String old = prefs == null ? null : prefs.getString(KEY_CUSTOM_BACKGROUND, "");
    if (prefs != null) prefs.edit().putString(KEY_CUSTOM_BACKGROUND, bg).putString(KEY_CUSTOM_BACKGROUND_TYPE, type).apply();
    if (old != null && !old.equals(bg)) deleteInternalFileUri(old);
}

private String copyCoverToInternalStorage(Uri uri) {
return copyImageToInternalStorage(uri, "covers", "cover_", 720, 88);
}

private void applyCustomBackground() {
    if (prefs == null) return;
    ImageView bgImage = findViewById(R.id.customBackgroundImage);
    TextureView bgVideo = findViewById(R.id.customBackgroundVideo);
    View bgDim = findViewById(R.id.customBackgroundDim);
    View dynamicBg = findViewById(R.id.dynamicBackground);
    if (bgImage == null || bgVideo == null || bgDim == null || dynamicBg == null) return;
    String bg = prefs.getString(KEY_CUSTOM_BACKGROUND, "");
    String type = prefs.getString(KEY_CUSTOM_BACKGROUND_TYPE, "image");
    boolean dimEnabled = prefs.getBoolean(KEY_BACKGROUND_DIM_ENABLED, true);
    boolean customColorEnabled = prefs.getBoolean(DynamicTheme.KEY_CUSTOM_COLOR_ENABLED, false);
    DynamicTheme theme = DynamicTheme.getInstance();
    theme.loadCustomColorSettings(prefs);
    if (bg == null || bg.isEmpty()) {
        stopBackgroundVideo();
        bgImage.setImageDrawable(null);
        bgImage.setVisibility(View.GONE);
        bgVideo.setVisibility(View.GONE);
        bgDim.setVisibility(View.GONE);
        dynamicBg.setVisibility(View.VISIBLE);
        if (customColorEnabled) {
            theme.setEnabled(true);
            theme.setCustomColorEnabled(true);
            applyDynamicTheme(theme.getColors());
        } else {
            applyDynamicTheme(null);
        }
        return;
    }
    try {
        if ("video".equals(type)) {
            bgImage.setImageDrawable(null);
            bgImage.setVisibility(View.GONE);
            dynamicBg.setVisibility(View.GONE);
            bgVideo.setVisibility(View.VISIBLE);
            bgDim.setVisibility(dimEnabled ? View.VISIBLE : View.GONE);
            playBackgroundVideo(bgVideo, Uri.parse(bg), true);
        } else {
            stopBackgroundVideo();
            bgVideo.setVisibility(View.GONE);
            bgImage.setImageURI(Uri.parse(bg));
            bgImage.setVisibility(View.VISIBLE);
            bgDim.setVisibility(dimEnabled ? View.VISIBLE : View.GONE);
            dynamicBg.setVisibility(View.GONE);
        }
        // 主题优先级：自定义颜色 > 背景取色 > 默认
        if (customColorEnabled) {
            theme.setEnabled(true);
            theme.setCustomColorEnabled(true);
            applyDynamicTheme(theme.getColors());
        } else if (prefs.getBoolean(KEY_BG_THEME_ENABLED, false)) {
            extractAndApplyTheme(bg, type);
        } else {
            applyDynamicTheme(null);
        }
    } catch (Throwable t) {
        prefs.edit().remove(KEY_CUSTOM_BACKGROUND).remove(KEY_CUSTOM_BACKGROUND_TYPE).apply();
        stopBackgroundVideo();
        bgImage.setImageDrawable(null);
        bgImage.setVisibility(View.GONE);
        bgVideo.setVisibility(View.GONE);
        bgDim.setVisibility(View.GONE);
        dynamicBg.setVisibility(View.VISIBLE);
        if (customColorEnabled) {
            theme.setEnabled(true);
            theme.setCustomColorEnabled(true);
            applyDynamicTheme(theme.getColors());
        } else {
            applyDynamicTheme(null);
        }
    }
}

private void extractAndApplyTheme(String bgUri, String type) {
    DynamicTheme dt = DynamicTheme.getInstance();
    dt.setEnabled(true);
    if ("image".equals(type)) {
        AppExecutors.io().execute(() -> {
            ThemeColorExtractor.ThemeColors colors = dt.extractAndCache(this, bgUri);
            runOnUiThread(() -> applyDynamicTheme(colors));
        });
    } else {
        // For video, extract from first frame using MediaMetadataRetriever
        AppExecutors.io().execute(() -> {
            ThemeColorExtractor.ThemeColors colors = extractVideoFirstFrame(bgUri);
            if (colors != null) {
                runOnUiThread(() -> applyDynamicTheme(colors));
            } else {
                // Fallback: try cache
                if (!dt.loadCache(prefs)) {
                    // Last resort: try TextureView after delay
                    runOnUiThread(this::scheduleVideoThemeExtraction);
                } else {
                    runOnUiThread(() -> applyDynamicTheme(dt.getColors()));
                }
            }
        });
    }
}

@Nullable
private ThemeColorExtractor.ThemeColors extractVideoFirstFrame(String videoUri) {
    android.media.MediaMetadataRetriever retriever = null;
    try {
        retriever = new android.media.MediaMetadataRetriever();
        retriever.setDataSource(this, Uri.parse(videoUri));
        android.graphics.Bitmap frame = retriever.getFrameAtTime(0, android.media.MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
        if (frame != null) {
            // Scale down for faster Palette extraction
            android.graphics.Bitmap scaled = frame;
            if (frame.getWidth() > 200 || frame.getHeight() > 200) {
                float scale = 200f / Math.max(frame.getWidth(), frame.getHeight());
                scaled = Bitmap.createScaledBitmap(frame, 
                        (int)(frame.getWidth() * scale), (int)(frame.getHeight() * scale), true);
                if (scaled != frame) frame.recycle();
            }
            DynamicTheme dt = DynamicTheme.getInstance();
            ThemeColorExtractor.ThemeColors colors = dt.extractFromBitmapAndCache(this, scaled);
            scaled.recycle();
            return colors;
        }
    } catch (Throwable ignored) {
    } finally {
        if (retriever != null) {
            try { retriever.release(); } catch (Throwable ignored) {}
        }
    }
    return null;
}

private void scheduleVideoThemeExtraction() {
    TextureView bgVideo = findViewById(R.id.customBackgroundVideo);
    if (bgVideo == null) return;
    bgVideo.postDelayed(() -> {
        try {
            if (bgVideo.isAvailable()) {
                android.graphics.Bitmap frame = bgVideo.getBitmap(100, 100);
                if (frame != null) {
                    DynamicTheme dt = DynamicTheme.getInstance();
                    ThemeColorExtractor.ThemeColors colors = dt.extractFromBitmapAndCache(this, frame);
                    frame.recycle();
                    applyDynamicTheme(colors);
                }
            }
        } catch (Throwable ignored) {}
    }, 1500);
}

/** Apply dynamic theme colors to all UI components. Pass null to reset to defaults. */
    private void applyDynamicTheme(ThemeColorExtractor.ThemeColors colors) {
        // 主题应用绝不允许把异常抛出去：0.3 的自定义主题色闪退就是
        // 「半透明色喂给 calculateContrast → 炸 → 颜色已存盘 → 每次开屏必崩」的闪退循环。
        // 这里兜底：任何异常都降级为默认主题并停用自定义颜色，下次启动自愈。
        try {
            applyDynamicThemeInner(colors);
        } catch (Throwable t) {
            android.util.Log.e("YukiTheme", "applyDynamicTheme failed, fallback to default theme", t);
            try {
                DynamicTheme dt = DynamicTheme.getInstance();
                dt.setCustomColorEnabled(false);
                dt.setEnabled(false);
                dt.saveCustomColorSettings(this);
                prefs.edit().putBoolean(DynamicTheme.KEY_BG_THEME_ENABLED, false).apply();
            } catch (Throwable ignored) { }
            try {
                applyDynamicThemeInner(null);
            } catch (Throwable ignored) { }
            try {
                Toast.makeText(this, "The theme color is abnormal and the default theme has been restored", Toast.LENGTH_SHORT).show();
            } catch (Throwable ignored) { }
        }
    }

    private void applyDynamicThemeInner(ThemeColorExtractor.ThemeColors colors) {
        DynamicTheme dt = DynamicTheme.getInstance();
        boolean resetting = (colors == null);
        if (resetting) {
            dt.setEnabled(false);
            dt.setCustomColorEnabled(false);
            colors = ThemeColorExtractor.DEFAULT;
        } else {
            // 如果传入了颜色，检查是否是自定义颜色
            if (dt.isCustomColorEnabled()) {
                // 应用自定义颜色
                dt.setEnabled(true);
            } else {
                // 应用背景提取的颜色
                dt.setEnabled(true);
            }
        }
    // Update DynamicSnowBackgroundView
    DynamicSnowBackgroundView dynamicBg = findViewById(R.id.dynamicBackground);
    if (dynamicBg != null) {
        dynamicBg.setThemeColors(colors.bg, colors.bg2, colors.auroraColor1, colors.auroraColor2, colors.auroraColor3);
    }
    // Update GameAdapter (card backgrounds only, not text)
    if (adapter != null) {
        adapter.setFullThemeColors(resetting ? null : colors);
        adapter.notifyDataSetChanged();
    }
    // Update CardGlowView instances (via RecyclerView)
    RecyclerView recycler = findViewById(R.id.recyclerGames);
    if (recycler != null) {
        for (int i = 0; i < recycler.getChildCount(); i++) {
            View child = recycler.getChildAt(i);
            if (child == null) continue;
            CardGlowView glow = child.findViewById(R.id.cardGlow);
            if (glow != null) glow.setThemeColors(colors.glowColor1, colors.glowColor2);
        }
    }

    // --- Apply extracted colors to UI component backgrounds/borders only ---

    //Sidebar filter items (background only)
    int[] sidebarIds = {R.id.filterAll, R.id.filterFavorite, R.id.filterRecent, R.id.filterStatus, R.id.filterDeveloper};
    for (int id : sidebarIds) {
        TextView tv = findViewById(id);
        if (tv != null) {
            tv.setBackground(resetting ? ContextCompat.getDrawable(this, R.drawable.bg_sidebar_item) : tintSidebarItem(colors));
        }
    }

    // Profile panel (background only)
    View profilePanel = findViewById(R.id.profilePanel);
    if (profilePanel != null) {
        profilePanel.setBackground(resetting ? ContextCompat.getDrawable(this, R.drawable.bg_sidebar) : tintSidebar(colors));
    }

    // Search input (background only)
    EditText etSearch = findViewById(R.id.etSearch);
    if (etSearch != null) {
        etSearch.setBackground(resetting ? ContextCompat.getDrawable(this, R.drawable.bg_input) : tintInput(colors));
    }

    // Detail panel (right sidebar) — outer FrameLayout has bg_game_card
    View detailPanel = findViewById(R.id.detailPanel);
    if (detailPanel != null) {
        // The outer FrameLayout is the detailPanel's parent (ScrollView's parent)
        View detailOuterFrame = (View) detailPanel.getParent().getParent();
        if (detailOuterFrame != null) {
            detailOuterFrame.setBackground(resetting ? ContextCompat.getDrawable(this, R.drawable.bg_game_card) : tintCardSelected(colors));
        }
        // The ScrollView parent
        View detailScrollParent = (View) detailPanel.getParent();
        if (detailScrollParent != null) {
            detailScrollParent.setBackgroundColor(0x00000000);
        }
    }

    // Detail panel interactive element backgrounds
    TextView sideMetadataSourceBadge = findViewById(R.id.sideMetadataSourceBadge);
    if (sideMetadataSourceBadge != null) {
        sideMetadataSourceBadge.setBackground(resetting ? ContextCompat.getDrawable(this, R.drawable.bg_chip) : tintChip(colors));
    }
    TextView sideTranslateToggle = findViewById(R.id.sideTranslateToggle);
    if (sideTranslateToggle != null) {
        sideTranslateToggle.setBackground(resetting ? ContextCompat.getDrawable(this, R.drawable.bg_input) : tintInput(colors));
    }

    // Detail panel cover placeholder
    TextView sideDetailPlaceholder = findViewById(R.id.sideDetailPlaceholder);
    if (sideDetailPlaceholder != null) {
        View coverFrame = (View) sideDetailPlaceholder.getParent();
        if (coverFrame != null) {
            coverFrame.setBackground(resetting ? ContextCompat.getDrawable(this, R.drawable.bg_cover_placeholder) : tintCoverPlaceholder(colors));
        }
    }

    // Detail panel screenshot placeholders
    ImageView sideScreenshot1 = findViewById(R.id.sideScreenshot1);
    if (sideScreenshot1 != null) {
        sideScreenshot1.setBackground(resetting ? ContextCompat.getDrawable(this, R.drawable.bg_cover_placeholder) : tintCoverPlaceholder(colors));
    }
    ImageView sideScreenshot2 = findViewById(R.id.sideScreenshot2);
    if (sideScreenshot2 != null) {
        sideScreenshot2.setBackground(resetting ? ContextCompat.getDrawable(this, R.drawable.bg_cover_placeholder) : tintCoverPlaceholder(colors));
    }

    // 次级选项仍沿用输入框风格；主/工具按钮在通用主题处理后统一刷新。
    TextView sideBtnOptions = findViewById(R.id.sideBtnOptions);
    if (sideBtnOptions != null) {
        sideBtnOptions.setBackground(resetting ? ContextCompat.getDrawable(this, R.drawable.bg_input) : tintInput(colors));
    }

    // Nav buttons (background only)
    int[] navIds = {R.id.navHome, R.id.navLibrary, R.id.navBigScreen, R.id.navChat};
    for (int id : navIds) {
        View nv = findViewById(id);
        if (nv != null) {
            if (id == R.id.navLibrary) {
                // navLibrary is the active/selected nav — use bg_home_nav_active on reset, tintSidebar when themed
                nv.setBackground(resetting ? ContextCompat.getDrawable(this, R.drawable.bg_home_nav_active) : tintSidebar(colors));
            } else {
                nv.setBackground(resetting ? ContextCompat.getDrawable(this, R.drawable.bg_sidebar_item) : tintSidebarItem(colors));
            }
        }
    }

    // Sidebar container backgrounds (profile, nav, stats)
    int[] sidebarContainerIds = {R.id.profileContainer, R.id.navContainer, R.id.statsContainer};
    for (int id : sidebarContainerIds) {
        View sc = findViewById(id);
        if (sc != null) {
            sc.setBackground(resetting ? ContextCompat.getDrawable(this, R.drawable.bg_sidebar) : tintSidebar(colors));
        }
    }

    // Filter bar items + sort button (use bg_input style)
    int[] filterIds = {R.id.filterAll, R.id.filterFavorite, R.id.filterRecent, R.id.filterStatus, R.id.filterDeveloper, R.id.btnSort};
    for (int id : filterIds) {
        View fv = findViewById(id);
        if (fv != null) {
            fv.setBackground(resetting ? ContextCompat.getDrawable(this, R.drawable.bg_input) : tintInput(colors));
        }
    }

    // Status bar items (clock, battery — same bg_input style)
    int[] statusIds = {R.id.tvClock, R.id.tvBatteryLevel};
    for (int id : statusIds) {
        View sv = findViewById(id);
        if (sv != null) {
            sv.setBackground(resetting ? ContextCompat.getDrawable(this, R.drawable.bg_input) : tintInput(colors));
        }
    }

    // Notice button (uses bg_status_item on reset, tintInput when themed — no padding to avoid icon squeeze)
    View btnNotice = findViewById(R.id.btnNotice);
    if (btnNotice != null) {
        btnNotice.setBackground(resetting ? ContextCompat.getDrawable(this, R.drawable.bg_status_item) : tintInput(colors));
    }

    // Background dim overlay color
    View bgDim = findViewById(R.id.customBackgroundDim);
    if (bgDim != null) {
        bgDim.setBackgroundColor((0x66 << 24) | (colors.bg & 0x00FFFFFF));
    }

    // Profile avatar placeholder
    // Cover placeholders (profile avatar, detail panel cover, screenshots)
    int[] placeholderIds = {R.id.tvCoverPlaceholder};
    for (int id : placeholderIds) {
        View v = findViewById(id);
        if (v != null && v.getVisibility() == View.VISIBLE) {
            v.setBackground(resetting ? ContextCompat.getDrawable(this, R.drawable.bg_cover_placeholder) : tintCoverPlaceholder(colors));
        }
    }

    // Detail panel cover placeholder
    View detailCoverPlaceholder = null;
    View detailPanel2 = findViewById(R.id.detailPanel);
    if (detailPanel2 != null && detailPanel2 instanceof ViewGroup) {
        detailCoverPlaceholder = findViewByIdRecursive((ViewGroup) detailPanel2, R.id.tvCoverPlaceholder);
    }
    if (detailCoverPlaceholder != null) {
        detailCoverPlaceholder.setBackground(resetting ? ContextCompat.getDrawable(this, R.drawable.bg_cover_placeholder) : tintCoverPlaceholder(colors));
    }

    // Profile avatar placeholder
    View profileAvatar = findViewById(R.id.profilePanel);
    if (profileAvatar != null && profileAvatar instanceof ViewGroup) {
        View avatarFrame = findViewByIndex((ViewGroup) profileAvatar, 0);
        if (avatarFrame != null) {
            avatarFrame.setBackground(resetting ? ContextCompat.getDrawable(this, R.drawable.bg_cover_placeholder) : tintCoverPlaceholder(colors));
        }
    }

    // Navigation bar color
    getWindow().setNavigationBarColor(colors.bg);

    // Tint all text colors in main layout
    View mainRoot = findViewById(android.R.id.content);
    if (mainRoot instanceof ViewGroup) {
        tintMainTextColors((ViewGroup) mainRoot, resetting ? null : colors);
    }
    // 通用主题递归之后恢复按钮层级，同时重建 ImageSpan 的 tint。
    refreshLibraryActionStyles(resetting ? null : colors);
    updateFilterSelection();
}

/** Recursively tint text colors in the main layout for dynamic theme. */
private void tintMainTextColors(ViewGroup root, ThemeColorExtractor.ThemeColors colors) {
    boolean enabled = (colors != null);
    for (int i = 0; i < root.getChildCount(); i++) {
        View child = root.getChildAt(i);
        if (child instanceof ViewGroup) {
            tintMainTextColors((ViewGroup) child, colors);
        }
        if (child instanceof TextView) {
            TextView tv = (TextView) child;
            if (enabled) {
                int currentColor = tv.getCurrentTextColor();
                boolean isDarkText = (currentColor & 0x00FFFFFF) < 0x555555;
                // Buttons or dark-text TextViews (on bright buttons) → pure white
                if (child instanceof Button || child instanceof CheckBox || isDarkText) {
                    tv.setTextColor(0xFFFFFFFF);
                }
                // EditText → near-white text, gray hint
                else if (child instanceof EditText) {
                    tv.setTextColor(0xFFF0F4FA);
                    tv.setHintTextColor(0xFF8090A8);
                }
                // Other TextViews → gray-white
                else {
                    tv.setTextColor(0xFFE8EDF5);
                }
            } else {
                // Reset to original colors
                int currentColor = tv.getCurrentTextColor();
                boolean isDarkText = (currentColor & 0x00FFFFFF) < 0x555555;
                if (child instanceof Button || child instanceof CheckBox || isDarkText) {
                    tv.setTextColor(0xFF000000);
                } else if (child instanceof EditText) {
                    tv.setTextColor(getColorCompat(R.color.yh_text));
                    tv.setHintTextColor(getColorCompat(R.color.yh_text_muted));
                } else {
                    tv.setTextColor(getColorCompat(R.color.yh_text));
                }
            }
        }
    }
}

/** Create a sidebar-style background with dynamic colors.
     * 渐变模式时使用 c1→c2 真实渐变。 */
    private android.graphics.drawable.GradientDrawable tintSidebar(ThemeColorExtractor.ThemeColors c) {
        android.graphics.drawable.GradientDrawable d;
        if (c.isGradient) {
            d = new android.graphics.drawable.GradientDrawable(
                    android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM,
                    new int[]{
                            (0xC8 << 24) | (c.bg & 0x00FFFFFF),
                            (0xC8 << 24) | (c.bg2 & 0x00FFFFFF)
                    });
        } else {
            d = new android.graphics.drawable.GradientDrawable();
            d.setColor((0xC8 << 24) | (c.bg & 0x00FFFFFF));
        }
        d.setCornerRadius(dp(10));
        d.setStroke(dp(1), (0x42 << 24) | (c.primary & 0x00FFFFFF));
        return d;
    }

/** Create a sidebar item background with dynamic colors.
     * 渐变模式时 selected 使用 secondary 色来体现双色渐变感。 */
    private android.graphics.drawable.StateListDrawable tintSidebarItem(ThemeColorExtractor.ThemeColors c) {
        android.graphics.drawable.StateListDrawable sld = new android.graphics.drawable.StateListDrawable();
        // Selected state：渐变模式使用 secondary，单色模式使用 primary
        int selColor = (c.isGradient) ? c.secondary : c.primary;
        android.graphics.drawable.GradientDrawable selected = new android.graphics.drawable.GradientDrawable();
        selected.setColor((0x27 << 24) | (selColor & 0x00FFFFFF));
        selected.setCornerRadius(dp(9));
        selected.setStroke(dp(1), (0x7E << 24) | (selColor & 0x00FFFFFF));
        sld.addState(new int[]{android.R.attr.state_selected}, selected);
        // Pressed state
        android.graphics.drawable.GradientDrawable pressed = new android.graphics.drawable.GradientDrawable();
        pressed.setColor((0x1A << 24) | (c.primary & 0x00FFFFFF));
        pressed.setCornerRadius(dp(9));
        pressed.setStroke(dp(1), (0x55 << 24) | (c.primary & 0x00FFFFFF));
        sld.addState(new int[]{android.R.attr.state_pressed}, pressed);
        // Default state
        android.graphics.drawable.GradientDrawable normal = new android.graphics.drawable.GradientDrawable();
        normal.setColor(0x00000000);
        normal.setCornerRadius(dp(9));
        sld.addState(new int[]{}, normal);
        return sld;
    }

/** Create an input field background with dynamic colors.
     * 渐变模式时边框使用 c1→c2 渐变。 */
    private android.graphics.drawable.GradientDrawable tintInput(ThemeColorExtractor.ThemeColors c) {
        android.graphics.drawable.GradientDrawable d = new android.graphics.drawable.GradientDrawable();
        d.setColor((0xA8 << 24) | (c.bg & 0x00FFFFFF));
        d.setCornerRadius(dp(8));
        if (c.isGradient) {
            d.setStroke(dp(1), (0x5E << 24) | (c.secondary & 0x00FFFFFF));
        } else {
            d.setStroke(dp(1), (0x5E << 24) | (c.primary & 0x00FFFFFF));
        }
        return d;
    }

/** 工具与主按钮共享刷新入口，避免背景更新后 ImageSpan 仍保持旧颜色。 */
private void refreshLibraryActionStyles(ThemeColorExtractor.ThemeColors colors) {
    View scan = findViewById(R.id.btnScan);
    ActionButtonStyle.apply(scan, ActionButtonStyle.TOOL, colors);
    styleIconAction(findViewById(R.id.tvScanLabel), ActionButtonStyle.TOOL,
            R.drawable.ic_st_search, "Scanning", colors);
    styleIconAction(findViewById(R.id.btnAdd), ActionButtonStyle.TOOL,
            R.drawable.ic_st_plus, "Add to", colors);
    styleIconAction(findViewById(R.id.btnSettings), ActionButtonStyle.TOOL,
            R.drawable.ic_btn_settings, " 设置", colors);
    styleIconAction(findViewById(R.id.sideBtnLaunch), ActionButtonStyle.PRIMARY,
            R.drawable.ic_btn_play, " 开始", colors);
}

private void styleIconAction(TextView view, int role, int icon, String label,
                             ThemeColorExtractor.ThemeColors colors) {
    if (view == null) return;
    // 扫描标签属于 FrameLayout，不给它再画一层按钮背景。
    if (view.getId() != R.id.tvScanLabel) ActionButtonStyle.apply(view, role, colors);
    int fg = ActionButtonStyle.foreground(this, role, colors);
    view.setTextColor(fg);
    com.yuki.yukihub.util.IconedText.set(view, icon, label, 9f, fg);
}

private boolean applyTaggedActionStyle(View view, ThemeColorExtractor.ThemeColors colors) {
    Object tag = view.getTag();
    int role;
    if (ActionButtonStyle.TAG_TOOL.equals(tag)) role = ActionButtonStyle.TOOL;
    else if (ActionButtonStyle.TAG_PRIMARY.equals(tag)) role = ActionButtonStyle.PRIMARY;
    else if (ActionButtonStyle.TAG_FILTER.equals(tag)) {
        role = view.isSelected() ? ActionButtonStyle.FILTER : ActionButtonStyle.TOOL;
    } else return false;
    ActionButtonStyle.apply(view, role, colors);
    return true;
}

private void styleGameEditActions(ViewGroup root, ThemeColorExtractor.ThemeColors colors) {
    if (root == null) return;
    for (int i = 0; i < root.getChildCount(); i++) {
        View child = root.getChildAt(i);
        applyTaggedActionStyle(child, colors);
        if (child instanceof ViewGroup) styleGameEditActions((ViewGroup) child, colors);
    }
}

/** Create a primary action button background with dynamic colors.
     * 渐变模式时使用真正的渐变色 c1→c2。 */
    private android.graphics.drawable.GradientDrawable tintButton(ThemeColorExtractor.ThemeColors c) {
        android.graphics.drawable.GradientDrawable d;
        if (c.isGradient) {
            // 渐变模式：从 primary 到 secondary 的真实渐变
            d = new android.graphics.drawable.GradientDrawable(
                    android.graphics.drawable.GradientDrawable.Orientation.LEFT_RIGHT,
                    new int[]{
                            (0xB4 << 24) | (c.primary & 0x00FFFFFF),
                            (0x86 << 24) | (c.secondary & 0x00FFFFFF)
                    });
        } else {
            // 单色模式：保持原样
            d = new android.graphics.drawable.GradientDrawable(
                    android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM,
                    new int[]{
                            (0xB4 << 24) | (c.primary & 0x00FFFFFF),
                            (0x86 << 24) | (c.primary & 0x00FFFFFF),
                            (0x6B << 24) | (c.primary & 0x00FFFFFF)
                    });
        }
        d.setCornerRadius(dp(8));
        d.setStroke(dp(1), (0xBF << 24) | (c.primary & 0x00FFFFFF));
        return d;
    }

/** Create a chip/tag background with dynamic colors. */
private android.graphics.drawable.GradientDrawable tintChip(ThemeColorExtractor.ThemeColors c) {
    android.graphics.drawable.GradientDrawable d = new android.graphics.drawable.GradientDrawable();
    d.setColor((0xAA << 24) | (c.card2 & 0x00FFFFFF));
    d.setCornerRadius(dp(999));
    d.setStroke(dp(1), (0x7A << 24) | (c.primary & 0x00FFFFFF));
    return d;
}

/** Create a selected card background with dynamic colors.
     * 渐变模式时使用 c1→c2 真实渐变。 */
    private android.graphics.drawable.GradientDrawable tintCardSelected(ThemeColorExtractor.ThemeColors c) {
        android.graphics.drawable.GradientDrawable d;
        if (c.isGradient) {
            d = new android.graphics.drawable.GradientDrawable(
                    android.graphics.drawable.GradientDrawable.Orientation.LEFT_RIGHT,
                    new int[]{
                            (0xE4 << 24) | (c.card & 0x00FFFFFF),
                            (0xD8 << 24) | (c.card2 & 0x00FFFFFF)
                    });
        } else {
            d = new android.graphics.drawable.GradientDrawable(
                    android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM,
                    new int[]{
                            (0xE4 << 24) | (c.card & 0x00FFFFFF),
                            (0xD9 << 24) | (c.card & 0x00FFFFFF),
                            (0xD8 << 24) | (c.bg2 & 0x00FFFFFF)
                    });
        }
        d.setCornerRadius(dp(10));
        d.setStroke(dp(2), (0x9C << 24) | (c.secondary & 0x00FFFFFF));
        return d;
    }

/** Create a cover placeholder gradient with dynamic colors. */
private GradientDrawable tintCoverPlaceholder(ThemeColorExtractor.ThemeColors c) {
    GradientDrawable d = new GradientDrawable(
            GradientDrawable.Orientation.BL_TR,
            new int[]{
                    (0xFF << 24) | (c.card & 0x00FFFFFF),
                    (0xFF << 24) | (c.card2 & 0x00FFFFFF),
                    (0xFF << 24) | (c.secondary & 0x00FFFFFF)
            });
    d.setCornerRadius(dp(8));
    d.setStroke(dp(1), 0x22FFFFFF);
    return d;
}

/** Recursively find a view by ID within a ViewGroup. */
private View findViewByIdRecursive(ViewGroup root, int id) {
    if (root == null) return null;
    for (int i = 0; i < root.getChildCount(); i++) {
        View child = root.getChildAt(i);
        if (child.getId() == id) return child;
        if (child instanceof ViewGroup) {
            View found = findViewByIdRecursive((ViewGroup) child, id);
            if (found != null) return found;
        }
    }
    return null;
}

/** Get child view at a specific index from a ViewGroup. */
private View findViewByIndex(ViewGroup parent, int index) {
    if (parent == null || index < 0 || index >= parent.getChildCount()) return null;
    return parent.getChildAt(index);
}

/** Apply dynamic theme to a dialog's root view. Call after setting up the dialog content. */
public void tintDialogRoot(View rootView) {
    DynamicTheme dt = DynamicTheme.getInstance();
    if (!dt.isEnabled()) return;
    ThemeColorExtractor.ThemeColors colors = dt.getColors();
    if (colors == null) return;
    rootView.setBackground(tintDialog(colors));
    // Postpone child tinting so all views are added first
    rootView.post(() -> tintDialogChildren((ViewGroup) rootView, colors));
}

/** Create a dialog background with dynamic colors. */
private GradientDrawable tintDialog(ThemeColorExtractor.ThemeColors c) {
    GradientDrawable d = new GradientDrawable();
    d.setColor((0xF0 << 24) | (c.card & 0x00FFFFFF));
    d.setStroke(dp(1), (0x5E << 24) | (c.primary & 0x00FFFFFF));
    d.setCornerRadius(dp(10));
    return d;
}

/** Recursively tint known background patterns inside dialogs. */
private void tintDialogChildren(ViewGroup root, ThemeColorExtractor.ThemeColors colors) {
    for (int i = 0; i < root.getChildCount(); i++) {
        View child = root.getChildAt(i);
        // Recurse into child ViewGroups first
        if (child instanceof ViewGroup) {
            tintDialogChildren((ViewGroup) child, colors);
        }

        // 编辑弹窗按钮按明确的语义处理，不再靠文字深浅猜按钮类型。
        if (applyTaggedActionStyle(child, colors)) continue;

        // ViewGroup with bg_dialog background → replace with tintDialog
        if (child instanceof ViewGroup && !(child instanceof RecyclerView) && !(child instanceof FrameLayout)
                && !(child instanceof Spinner) && !(child instanceof EditText)
                && child.getBackground() != null) {
            child.setBackground(tintDialog(colors));
        }

        // CheckBox → dynamic tint + white text
        if (child instanceof CheckBox) {
            ((CheckBox) child).setButtonTintList(android.content.res.ColorStateList.valueOf(colors.primary));
            ((CheckBox) child).setTextColor(0xFFE8EDF5);
            continue;
        }

        // SeekBar → dynamic progress tint
        if (child instanceof SeekBar) {
            ((SeekBar) child).setProgressTintList(android.content.res.ColorStateList.valueOf(colors.primary));
            ((SeekBar) child).setThumbTintList(android.content.res.ColorStateList.valueOf(colors.primary));
            continue;
        }

        // Button → tintButton + white text
        if (child instanceof Button) {
            child.setBackground(tintButton(colors));
            ((Button) child).setTextColor(0xFFFFFFFF);
            continue;
        }
        // EditText → tintInput + white text + gray hint
        if (child instanceof EditText) {
            child.setBackground(tintInput(colors));
            ((EditText) child).setTextColor(0xFFF0F4FA);
            ((EditText) child).setHintTextColor(0xFF8090A8);
            continue;
        }
        // Spinner → tintInput + white text
        if (child instanceof Spinner) {
            child.setBackground(tintInput(colors));
            continue;
        }

        Drawable bg = child.getBackground();
        // TextView with background (labels, badges, action buttons like btnReset/btnPick)
        if (child instanceof TextView && !(child instanceof EditText) && !(child instanceof Button)) {
            TextView tv = (TextView) child;
            int currentTextColor = tv.getCurrentTextColor();
            // If text was dark (like #071221 on bright buttons), treat as action button
            boolean isDarkText = (currentTextColor & 0x00FFFFFF) < 0x555555;
            if (isDarkText) {
                child.setBackground(tintButton(colors));
                tv.setTextColor(0xFFFFFFFF);
            } else if (bg != null) {
                child.setBackground(tintLabel(colors));
                tv.setTextColor(0xFFE8EDF5);
            } else {
                // TextView without background — just tint text
                tv.setTextColor(0xFFE8EDF5);
            }
            continue;
        }
        // FrameLayout with background → cover placeholder
        if (child instanceof FrameLayout && bg != null) {
            child.setBackground(tintCoverPlaceholder(colors));
        }
    }
}

/** Create a label background with dynamic colors (replaces bg_label). */
private GradientDrawable tintLabel(ThemeColorExtractor.ThemeColors c) {
    GradientDrawable d = new GradientDrawable();
    d.setColor((0x33 << 24) | (c.primary & 0x00FFFFFF));
    d.setStroke(dp(1), (0x7A << 24) | (c.primary & 0x00FFFFFF));
    d.setCornerRadius(dp(6));
    d.setPadding(dp(10), dp(5), dp(10), dp(5));
    return d;
}

private void playBackgroundVideo(TextureView textureView, Uri uri, boolean forceRestart) {
    pendingBackgroundVideoUri = uri;
    if (forceRestart) releaseBackgroundMediaPlayer();
    textureView.setSurfaceTextureListener(null);
    if (textureView.isAvailable()) {
        textureView.post(() -> startBackgroundMediaPlayer(textureView, uri));
    } else {
        textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                startBackgroundMediaPlayer(textureView, uri);
            }
            @Override public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
                applyVideoCenterCrop(textureView, backgroundMediaPlayer);
            }
            @Override public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                releaseBackgroundMediaPlayer();
                return true;
            }
            @Override public void onSurfaceTextureUpdated(SurfaceTexture surface) { }
        });
    }
}

private void startBackgroundMediaPlayer(TextureView textureView, Uri uri) {
    try {
        releaseBackgroundMediaPlayer();
        MediaPlayer mp = new MediaPlayer();
        backgroundMediaPlayer = mp;
        mp.setDataSource(this, uri);
        Surface surface = new Surface(textureView.getSurfaceTexture());
        mp.setSurface(surface);
        surface.release();
        mp.setLooping(true);
        boolean soundOn = prefs != null && prefs.getBoolean(KEY_BACKGROUND_VIDEO_SOUND, false);
        mp.setVolume(soundOn ? 1f : 0f, soundOn ? 1f : 0f);
        mp.setOnPreparedListener(player -> {
            applyVideoCenterCrop(textureView, player);
            player.start();
        });
        mp.setOnErrorListener((player, what, extra) -> {
            Toast.makeText(MainActivity.this, "Video background playback failed, please try changing the video format", Toast.LENGTH_SHORT).show();
            releaseBackgroundMediaPlayer();
            return true;
        });
        mp.prepareAsync();
    } catch (Throwable t) {
        if (prefs != null) prefs.edit().remove(KEY_CUSTOM_BACKGROUND).remove(KEY_CUSTOM_BACKGROUND_TYPE).apply();
        applyCustomBackground();
    }
}

private void applyVideoCenterCrop(TextureView textureView, MediaPlayer player) {
    if (textureView == null || player == null) return;
    int viewW = textureView.getWidth();
    int viewH = textureView.getHeight();
    int videoW = player.getVideoWidth();
    int videoH = player.getVideoHeight();
    if (viewW <= 0 || viewH <= 0 || videoW <= 0 || videoH <= 0) return;
    float scale = Math.max((float) viewW / videoW, (float) viewH / videoH);
    float scaledW = videoW * scale;
    float scaledH = videoH * scale;
    Matrix matrix = new Matrix();
    matrix.setScale(scaledW / viewW, scaledH / viewH, viewW / 2f, viewH / 2f);
    textureView.setTransform(matrix);
}

private void releaseBackgroundMediaPlayer() {
    if (backgroundMediaPlayer == null) return;
    try { backgroundMediaPlayer.stop(); } catch (Throwable ignored) { }
    try { backgroundMediaPlayer.release(); } catch (Throwable ignored) { }
    backgroundMediaPlayer = null;
}

private void stopBackgroundVideo() {
    pendingBackgroundVideoUri = null;
    releaseBackgroundMediaPlayer();
}

private String copyVideoToInternalStorage(Uri uri) {
    try {
        java.io.File dir = new java.io.File(getFilesDir(), "backgrounds");
        if (!dir.exists()) dir.mkdirs();
        java.io.File file = new java.io.File(dir, "bg_video_" + System.currentTimeMillis() + ".mp4");
        try (InputStream in = getContentResolver().openInputStream(uri); java.io.FileOutputStream out = new java.io.FileOutputStream(file)) {
            if (in == null) return null;
            byte[] buf = new byte[8192];
            int len;
            while ((len = in.read(buf)) != -1) out.write(buf, 0, len);
            out.flush();
        }
        return Uri.fromFile(file).toString();
    } catch (Throwable t) {
        return null;
    }
}

private String copyImageToInternalStorage(Uri uri, String folder, String prefix, int max, int quality) {
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
            if (bitmap == null) return null;
            int w = bitmap.getWidth();
            int h = bitmap.getHeight();
            if (w > max || h > max) {
                float scale = Math.min(max / (float) w, max / (float) h);
                Bitmap scaled = Bitmap.createScaledBitmap(bitmap, Math.max(1, (int) (w * scale)), Math.max(1, (int) (h * scale)), true);
                bitmap.recycle();
                bitmap = scaled;
            }
            java.io.File dir = new java.io.File(getFilesDir(), folder == null ? "images" : folder);
            if (!dir.exists()) dir.mkdirs();
            java.io.File file = new java.io.File(dir, (prefix == null ? "image_" : prefix) + System.currentTimeMillis() + ".jpg");
            java.io.FileOutputStream out = new java.io.FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);
            out.flush();
            out.close();
            bitmap.recycle();
            return Uri.fromFile(file).toString();
        } catch (Exception e) {
            return null;
        }
    }

    private void takeFlags(Uri uri) {
        if (uri == null) return;
        int flags = Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
        try {
            getContentResolver().takePersistableUriPermission(uri, flags);
            Log.i("YukiHub", "persisted tree permission: " + uri);
        } catch (SecurityException writeDenied) {
            try {
                getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Log.i("YukiHub", "persisted read-only tree permission: " + uri);
            } catch (Exception readDenied) {
                Log.w("YukiHub", "persist tree permission failed: " + uri, readDenied);
                Toast.makeText(this, "Directory authorization saving failed, please select the TF card directory again.", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.w("YukiHub", "persist tree permission failed: " + uri, e);
            Toast.makeText(this, "Directory authorization saving failed, please select the TF card directory again.", Toast.LENGTH_LONG).show();
        }
    }

    private void scanMissingCoversIfNeeded() {
        if (coverScanRunning || allGames.isEmpty()) return;
        List<Game> targets = new ArrayList<>();
        for (Game g : allGames) {
            if (g == null || g.rootUri == null || g.rootUri.isEmpty()) continue;
            if (hasCover(g)) continue;
            targets.add(g);
        }
        if (targets.isEmpty()) return;
        coverScanRunning = true;
        AppExecutors.runOnIo(() -> {
            int changed = 0;
            for (Game g : targets) {
                try {
                    Uri image = findFirstLevelImage(g.rootUri);
                    if (image == null) continue;
                    String cover = copyCoverToInternalStorage(image);
                    if (cover == null || cover.isEmpty()) continue;
                    g.coverUri = cover;
                    g.coverPersistUri = cover;
                    g.coverSourceType = 1;
                    repository.update(g);
                    changed++;
                } catch (Throwable ignored) { }
            }
            int finalChanged = changed;
            runOnUiThread(() -> {
                coverScanRunning = false;
                if (finalChanged > 0) {
                    allGames.clear();
                    allGames.addAll(repository.getAll());
                    applyFilter();
                }
            });
        });
    }

    private boolean hasCover(Game g) {
        return (g.coverPersistUri != null && !g.coverPersistUri.trim().isEmpty())
                || (g.coverUri != null && !g.coverUri.trim().isEmpty());
    }

    private Uri findFirstLevelImage(String rootUri) {
try {
if (rootUri == null || rootUri.trim().isEmpty()) return null;
DocumentFile dir = gameDir(rootUri);
if (dir == null || !dir.isDirectory()) return null;
            DocumentFile[] files = dir.listFiles();
            if (files == null) return null;
            DocumentFile best = null;
            int bestScore = Integer.MIN_VALUE;
            for (DocumentFile f : files) {
                if (f == null || !f.isFile()) continue;
                String name = f.getName();
                if (!isImageFile(name)) continue;
                int score = coverNameScore(name);
                if (best == null || score > bestScore) {
                    best = f;
                    bestScore = score;
                }
            }
            return best == null ? null : best.getUri();
        } catch (Throwable ignored) { return null; }
    }

    private boolean isImageFile(String name) {
        if (name == null) return false;
        String lower = name.toLowerCase(Locale.ROOT);
        return lower.endsWith(".jpg") || lower.endsWith(".jpeg") || lower.endsWith(".png") || lower.endsWith(".webp") || lower.endsWith(".bmp");
    }

    private int coverNameScore(String name) {
        if (name == null) return 0;
        String lower = name.toLowerCase(Locale.ROOT);
        if (lower.equals("cover.jpg") || lower.equals("cover.png") || lower.equals("cover.webp")) return 100;
        if (lower.equals("folder.jpg") || lower.equals("folder.png") || lower.equals("folder.webp")) return 95;
        if (lower.contains("cover") || lower.contains("folder") || lower.contains("Cover")) return 80;
        if (lower.contains("poster") || lower.contains("package") || lower.contains("main")) return 60;
        return 10;
    }
 
    private TextView tvGreeting;

    private void setupUi() {
        RecyclerView recycler = findViewById(R.id.recyclerGames);
        tvEmpty = findViewById(R.id.tvEmpty);
tvStatsGames = findViewById(R.id.tvStatsGames);
tvStatsTime = findViewById(R.id.tvStatsTime);
tvProfileName = findViewById(R.id.tvProfileName);
tvProfileInitial = findViewById(R.id.tvProfileInitial);
profileStatusDot = findViewById(R.id.profileStatusDot);
ivProfileAvatar = findViewById(R.id.ivProfileAvatar);
tvGreeting = findViewById(R.id.tvGreeting);
detailPanel = findViewById(R.id.detailPanel);
        detailMetaPanel = findViewById(R.id.detailMetaPanel);
        sideDetailCover = findViewById(R.id.sideDetailCover);
        sideDetailCoverLoading = findViewById(R.id.sideDetailCoverLoading);
        sideDetailPlaceholder = findViewById(R.id.sideDetailPlaceholder);
        sideDetailTitle = findViewById(R.id.sideDetailTitle);
sideMetadataSourceBadge = findViewById(R.id.sideMetadataSourceBadge);
sideDetailOriginalTitle = findViewById(R.id.sideDetailOriginalTitle);
sideDetailHint = findViewById(R.id.sideDetailHint);
sideDetailPath = findViewById(R.id.sideDetailPath);
sideDescToggle = findViewById(R.id.sideDescToggle);
sideTranslateToggle = findViewById(R.id.sideTranslateToggle);
        sideDetailDeveloper = findViewById(R.id.sideDetailDeveloper);
        sideDetailDate = findViewById(R.id.sideDetailDate);
        sideDetailRating = findViewById(R.id.sideDetailRating);
sideDetailLength = findViewById(R.id.sideDetailLength);
sideDetailTags = findViewById(R.id.sideDetailTags);
sideTagContainer = findViewById(R.id.sideTagContainer);
sideScreenshot1 = findViewById(R.id.sideScreenshot1);
sideScreenshot2 = findViewById(R.id.sideScreenshot2);
sideBtnLaunch = findViewById(R.id.sideBtnLaunch);
        sideBtnOptions = findViewById(R.id.sideBtnOptions);
        prepareManualClickFeedback(sideBtnLaunch);
        prepareManualClickFeedback(sideBtnOptions);
        prepareManualClickFeedback(sideDescToggle);
        prepareManualClickFeedback(sideTranslateToggle);
        // 用 ImageSpan 把开始/选项图标紧贴文字并垂直居中（不走 drawableStart，避免图标与文字分离）
        setIconedText(sideBtnLaunch, R.drawable.ic_btn_play, " 开始", getColorCompat(R.color.yh_action_primary_text));
        setIconedText(sideBtnOptions, R.drawable.ic_btn_settings, "Options", -1);
        sideBtnLaunch.setOnClickListener(v -> { clickFeedback(v); if (selectedGame != null) launchGame(selectedGame); });
        sideBtnOptions.setOnClickListener(v -> { clickFeedback(v); if (selectedGame != null) showSideOptions(selectedGame); });
        sideDescToggle.setOnClickListener(v -> { clickFeedback(v); sideDescExpanded = !sideDescExpanded; renderSideDescription(); });
if (sideTranslateToggle != null) sideTranslateToggle.setOnClickListener(v -> { clickFeedback(v); toggleOrTranslateDescription(); });
        updateSideDetail(null);
        adapter = new GameAdapter();
adapter.setOnUiFeedbackListener(type -> playUiSound(type == GameAdapter.FEEDBACK_CONFIRM ? UI_SOUND_CONFIRM : (type == GameAdapter.FEEDBACK_SWITCH ? UI_SOUND_SWITCH : UI_SOUND_CLICK)));
adapter.setOnGameClickListener(new GameAdapter.OnGameClickListener() {
            @Override public void onGameClick(Game game) { updateSideDetail(game); }
            @Override public void onGameDoubleClick(Game game) { if (game != null) launchGame(game); }
            @Override public void onGameLongClick(Game game) { showEditDialog(game); }
            @Override public void onStatusClick(Game game) { updateSideDetail(game); showPlayStatusDialog(game, null); }
        });
        adapter.setOnSelectionChangedListener(count -> updateMultiSelectBar(count));
        adapter.setNsfwBlurEnabled(nsfwBlurEnabled());
        int columns = prefs == null ? DEFAULT_GAME_COLUMNS : prefs.getInt(KEY_GAME_COLUMNS, DEFAULT_GAME_COLUMNS);
        columns = Math.max(2, Math.min(10, columns));
        recycler.setLayoutManager(new GridLayoutManager(this, columns));
        recycler.setAdapter(adapter);
        // M21-3：**关掉"单格刷新"的交叉淡入动画**（"不能连点、要狂按"的真凶）
        //
        // 现象：点一下卡片后要等一会儿才能点第二下，双击启动游戏得狂按。
        // 真因：15987f5 把点击后的刷新从 notifyDataSetChanged() 改成了 notifyItemChanged(position)，
        //       而本 RecyclerView 用的是默认的 DefaultItemAnimator：
        //         · notifyDataSetChanged() → 全量刷新，**不跑 item 动画**（改之前：点一下立刻响应）
        //         · notifyItemChanged(pos) → 走 animateChange：**新建一个 ViewHolder**、
        //           把旧的当成"消失中"的视图叠在上面淡出，约 250ms 后 removeView
        //       于是：① 每次点击都多出一个约 250ms 的动画（用户看到的就是它）；
        //             ② 手指紧跟着的第二次点击，DOWN 落在那张"正在消失"的 View 上，
        //                UP 还没到它就被 removeView → 手势被取消 → 点击整个丢掉。
        //
        // 关掉 change 动画后，SimpleItemAnimator.canReuseUpdatedViewHolder() 恒为 true，
        // RecyclerView 复用同一个 ViewHolder：不换视图、不播动画、点击不再被吃。
        // 只关"change"，增删/移动动画不受影响。
        if (recycler.getItemAnimator() instanceof androidx.recyclerview.widget.SimpleItemAnimator) {
            ((androidx.recyclerview.widget.SimpleItemAnimator) recycler.getItemAnimator())
                    .setSupportsChangeAnimations(false);
        }
        ensureMultiSelectBar();
View addButton = findViewById(R.id.btnAdd);
        View scanButton = findViewById(R.id.btnScan);
        // 顶栏深色工具按钮：浅色文字，图标同色
        if (addButton instanceof TextView) {
            com.yuki.yukihub.util.IconedText.set((TextView) addButton,
                    R.drawable.ic_st_plus, "Add to", 9f, getColorCompat(R.color.yh_action_tool_text));
        }
        TextView scanLabel = findViewById(R.id.tvScanLabel);
        if (scanLabel != null) {
            com.yuki.yukihub.util.IconedText.set(scanLabel,
                    R.drawable.ic_st_search, "Scanning", 9f, getColorCompat(R.color.yh_action_tool_text));
        }
        ivScanLoading = findViewById(R.id.ivScanLoading);
        applyTopActionFeedback(addButton);
applyTopActionFeedback(scanButton);
prepareManualClickFeedback(addButton);
prepareManualClickFeedback(scanButton);
addButton.setOnClickListener(v -> { clickFeedback(v); showEditDialog(null); });
scanButton.setOnClickListener(v -> { clickFeedback(v); scanLastRootOrChoose(); });
scanButton.setOnLongClickListener(v -> { clickFeedback(v); launchScanRootPicker(-1); return true; });
// 设置按钮（右上角）
View settingsButton = findViewById(R.id.btnSettings);
if (settingsButton != null) {
    applyTopActionFeedback(settingsButton);
    prepareManualClickFeedback(settingsButton);
    // 用 ImageSpan 将小图标紧贴文字并垂直居中（不走 drawableStart，避免图标与文字分离）
    if (settingsButton instanceof android.widget.TextView) {
        setIconedText((android.widget.TextView) settingsButton, R.drawable.ic_btn_settings, " 设置", getColorCompat(R.color.yh_action_tool_text));
    }
    settingsButton.setOnClickListener(v -> { clickFeedback(v); showSettingsDialog(); });
}
refreshLibraryActionStyles(ActionButtonStyle.activeColors());
// 通知按钮
View btnNotice = findViewById(R.id.btnNotice);
if (btnNotice != null) {
    prepareManualClickFeedback(btnNotice);
    btnNotice.setOnClickListener(v -> { clickFeedback(v); showNoticeDialog(); });
}
// 导航按钮
View navHome = findViewById(R.id.navHome);
 View navTranslate = findViewById(R.id.navTranslate);
 View navBigScreen = findViewById(R.id.navBigScreen);
View navChat = findViewById(R.id.navChat);
if (navHome != null) { prepareManualClickFeedback(navHome); navHome.setOnClickListener(v -> { clickFeedback(v); startActivity(new Intent(this, HomeActivity.class).putExtra("force_home", true)); finish(); }); }
if (navTranslate != null) { prepareManualClickFeedback(navTranslate); navTranslate.setOnClickListener(v -> { clickFeedback(v); startActivity(new Intent(this, com.yuki.yukihub.translate.TranslateControlActivity.class)); }); }
if (navBigScreen != null) { prepareManualClickFeedback(navBigScreen); navBigScreen.setOnClickListener(v -> { clickFeedback(v); startActivity(new Intent(this, com.yuki.yukihub.bigscreen.BigScreenActivity.class)); }); }
if (navChat != null) { prepareManualClickFeedback(navChat); navChat.setOnClickListener(v -> { clickFeedback(v); showFriendsChatPlaceholder(); }); }
// 排序按钮
View btnSort = findViewById(R.id.btnSort);
if (btnSort != null) { prepareManualClickFeedback(btnSort); btnSort.setOnClickListener(v -> { clickFeedback(v); showSortDialog(); }); }
// 头像点击
View profilePanel = findViewById(R.id.profilePanel);
if (profilePanel != null) {
    prepareManualClickFeedback(profilePanel);
    profilePanel.setOnClickListener(v -> { clickFeedback(v); showProfileDialog(); });
}
        setupDeveloperToggle();
        setupStatusToggle();
bindFilter(R.id.filterAll, "ALL"); bindFilter(R.id.filterFavorite, "FAVORITE"); bindFilter(R.id.filterRecent, "LOCAL"); bindFilter(R.id.filterHidden, "HIDDEN");
        updateFilterSelection();
        EditText searchInput = findViewById(R.id.etSearch);
        searchInput.setFocusableInTouchMode(true); // 搜索输入必须允许触摸获取焦点
        searchInput.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
            public void onTextChanged(CharSequence s, int st, int b, int c) { query = s.toString(); applyFilter(); }
            public void afterTextChanged(Editable e) {}
        });
    }

    private void applyTopActionFeedback(View view) {
    if (view == null) return;
    view.setOnTouchListener((v, event) -> {
        if (event == null) return false;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            v.animate().cancel();
            v.animate().scaleX(0.92f).scaleY(0.92f).alpha(0.78f).setDuration(70L).start();
        } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
            v.animate().cancel();
            v.animate().scaleX(1.0f).scaleY(1.0f).alpha(1.0f).setDuration(120L).start();
        }
        return false;
    });
}

private void prepareManualClickFeedback(View v) {
    if (v == null) return;
    try { v.setSoundEffectsEnabled(false); } catch (Throwable ignored) { }
}

private void attachUiTouchSound(View v, int type) {
    if (v == null) return;
    prepareManualClickFeedback(v);
    v.setOnTouchListener((view, event) -> {
        if (event != null && event.getAction() == MotionEvent.ACTION_DOWN) playUiSound(type);
        return false;
    });
}

private boolean uiClickSoundEnabled() {
    return prefs == null || prefs.getBoolean(KEY_UI_CLICK_SOUND, true);
}

private void clickFeedback(View v) {
    if (v == null) return;
    try { v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY); } catch (Throwable ignored) { }
    playUiSound(UI_SOUND_CLICK);
}

private void playUiSound(int type) {
    if (!uiClickSoundEnabled()) return;
    long now = System.currentTimeMillis();
    if (now - lastUiSoundAt < 35L) return;
    lastUiSoundAt = now;
    try {
        ensureUiSoundPool();
        int soundId = type == UI_SOUND_CONFIRM ? uiConfirmSoundId : (type == UI_SOUND_SWITCH ? uiSwitchSoundId : uiClickSoundId);
        if (soundId != 0 && uiSoundPool != null) uiSoundPool.play(soundId, 0.65f, 0.65f, 1, 0, 1.0f);
    } catch (Throwable ignored) { }
}

private void ensureUiSoundPool() {
    if (uiSoundPool != null) return;
    uiSoundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
    uiClickSoundId = uiSoundPool.load(this, R.raw.ui_click, 1);
    uiConfirmSoundId = uiSoundPool.load(this, R.raw.ui_confirm, 1);
    uiSwitchSoundId = uiSoundPool.load(this, R.raw.ui_switch, 1);
}

private void releaseUiSoundPool() {
    if (uiSoundPool == null) return;
    try { uiSoundPool.release(); } catch (Throwable ignored) { }
    uiSoundPool = null;
    uiClickSoundId = 0;
    uiConfirmSoundId = 0;
    uiSwitchSoundId = 0;
}

private void setScanLoading(boolean loading) {
    if (ivScanLoading == null) return;
    if (loading) {
        ivScanLoading.setVisibility(View.VISIBLE);
        ivScanLoading.setRotation(0f);
        if (scanAnimator == null) {
            scanAnimator = ObjectAnimator.ofFloat(ivScanLoading, View.ROTATION, 0f, 360f);
            scanAnimator.setDuration(900L);
            scanAnimator.setRepeatCount(ObjectAnimator.INFINITE);
            scanAnimator.setInterpolator(new LinearInterpolator());
        }
        if (!scanAnimator.isStarted()) scanAnimator.start();
        scanLoadingAnimated = true;
    } else {
        if (scanAnimator != null) {
            try { scanAnimator.cancel(); } catch (Throwable ignored) { }
        }
        ivScanLoading.setRotation(0f);
        ivScanLoading.setVisibility(View.GONE);
        scanLoadingAnimated = false;
    }
}

private void showProfileDialog() {
    final String currentName = displayProfileName();
    final String localName = profileName();
    final String currentSignature = profileSignature();
    long total = totalPlayTime();

    LinearLayout root = new LinearLayout(this);
    root.setOrientation(LinearLayout.VERTICAL);
    root.setBackgroundResource(R.drawable.bg_dialog);
    tintDialogRoot(root);
    int pad = dp(16);
    root.setPadding(pad, dp(14), pad, dp(10));

    LinearLayout header = new LinearLayout(this);
    header.setOrientation(LinearLayout.HORIZONTAL);
    header.setGravity(android.view.Gravity.CENTER_VERTICAL);

    FrameLayout avatarBox = new FrameLayout(this);
    avatarBox.setBackgroundResource(R.drawable.bg_cover_placeholder);
    ImageView avatar = new ImageView(this);
    avatar.setScaleType(ImageView.ScaleType.CENTER_CROP);
    TextView avatarInitial = new TextView(this);
    avatarInitial.setGravity(android.view.Gravity.CENTER);
    avatarInitial.setText(initials(currentName));
    avatarInitial.setTextColor(getColorCompat(R.color.yh_text));
    avatarInitial.setTextSize(24);
    avatarInitial.setTypeface(null, android.graphics.Typeface.BOLD);
    avatarBox.addView(avatar, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
    avatarBox.addView(avatarInitial, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
    loadProfileAvatarInto(avatar, avatarInitial);
    avatarBox.setOnClickListener(v -> profileAvatarLauncher.launch("image/*"));
    header.addView(avatarBox, new LinearLayout.LayoutParams(dp(72), dp(72)));

    LinearLayout info = new LinearLayout(this);
    info.setOrientation(LinearLayout.VERTICAL);
    info.setPadding(dp(12), 0, 0, 0);
    // 昵称行：昵称 + 铅笔按钮（仅登录后可改云端昵称）
    LinearLayout nameRow = new LinearLayout(this);
    nameRow.setOrientation(LinearLayout.HORIZONTAL);
    nameRow.setGravity(android.view.Gravity.CENTER_VERTICAL);
    TextView nameView = new TextView(this);
    nameView.setText(currentName);
    nameView.setTextColor(getColorCompat(R.color.yh_text));
    nameView.setTextSize(20);
    nameView.setTypeface(null, android.graphics.Typeface.BOLD);
    nameView.setSingleLine(true);
    nameView.setEllipsize(android.text.TextUtils.TruncateAt.END);
    // 昵称按内容宽度排布，铅笔才能紧跟其后（用 weight=1 会把铅笔推到行尾）。
    // 超长昵称用 maxWidth 兜底，省略号截断而不是把铅笔挤出屏幕。
    nameView.setMaxWidth((int) (getResources().getDisplayMetrics().widthPixels * 0.45f));
    nameRow.addView(nameView, new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
    if (isLoggedIn()) {
        ImageView editNameBtn = new ImageView(this);
        editNameBtn.setImageResource(R.drawable.ic_edit_pencil);
        editNameBtn.setColorFilter(getColorCompat(R.color.yh_text_muted));
        editNameBtn.setScaleType(ImageView.ScaleType.FIT_CENTER);
        editNameBtn.setContentDescription("Modify nickname");
        int ep = dp(4);
        editNameBtn.setPadding(ep, ep, ep, ep);
        editNameBtn.setOnClickListener(v -> showCloudNicknameDialog(nameView));
        LinearLayout.LayoutParams editLp = new LinearLayout.LayoutParams(dp(26), dp(26));
        editLp.setMargins(dp(6), 0, 0, 0);
        nameRow.addView(editNameBtn, editLp);
    }
    TextView statsView = new TextView(this);
    String uid = prefs == null ? "" : prefs.getString(KEY_AUTH_UID, "");
    String uidStr = uid.isEmpty() ? "" : "UID: " + uid + "  ·  ";
    statsView.setText(uidStr + allGames.size() + " Games · " + TimeFormatUtil.playTime(total) + "\n" + emptyText(currentSignature, "This person has not written a signature yet"));
    statsView.setTextColor(getColorCompat(R.color.yh_text_muted));
    statsView.setTextSize(12);
    statsView.setPadding(0, dp(5), 0, 0);
    info.addView(nameRow);
    TextView accountBadge = new TextView(this);
    accountBadge.setText(accountStatusLabelForDialog());
    accountBadge.setTextSize(11);
    accountBadge.setTextColor(accountStatusTextColor());
    accountBadge.setGravity(android.view.Gravity.CENTER);
    accountBadge.setPadding(dp(8), dp(2), dp(8), dp(2));
    accountBadge.setBackgroundResource(accountStatusBackground());
    LinearLayout.LayoutParams badgeLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, dp(22));
    badgeLp.setMargins(0, dp(5), 0, 0);
    info.addView(accountBadge, badgeLp);
    // 等级 + 经验（仅登录用户，异步拉取服务器数据）。
    // 展示口径为「B站式继承条」：总经验 / 升到下一级的累计阈值（与网页端一致）
    if (isLoggedIn()) {
        final TextView levelView = new TextView(this);
        levelView.setText("Lv.1 · 0 EXP");
        levelView.setTextSize(11);
        levelView.setTextColor(getColorCompat(R.color.yh_text_muted));
        levelView.setPadding(0, dp(4), 0, 0);
        info.addView(levelView);
        AppExecutors.runOnIo(() -> {
            try {
                com.yuki.yukihub.social.SocialApiClient client = new com.yuki.yukihub.social.SocialApiClient(MainActivity.this);
                org.json.JSONObject lv = client.getMyLevel();
                final int level = lv.optInt("level", 1);
                final int totalExp = lv.optInt("exp", 0);
                final int nextTotal = lv.optInt("nextLevelTotalExp", totalExp + 100);
                final boolean maxed = lv.optBoolean("isMaxLevel", false) || level >= 30;
                final boolean checked = lv.optBoolean("todayCheckedIn", false);
                runOnUiThread(() -> {
                    levelView.setText(maxed
                            ? ("Lv." + level + " · " + totalExp + "EXP·Maximum level")
                            : ("Lv." + level + " · " + totalExp + "/" + nextTotal + " EXP") + (checked ? "· Signed in" : ""));
                    levelView.setTextColor(levelColorForLevel(level));
                });
            } catch (Throwable t) {
                // 等级信息拿不到就保留原显示，不打扰用户；但留痕便于排查
                Log.w("YukiHub", "level info fetch failed: " + t.getMessage(), t);
            }
        });
    }
    info.addView(statsView);
    header.addView(info, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
    root.addView(header);

    TextView avatarHint = new TextView(this);
    avatarHint.setText(isLoggedIn() ? "It is currently a cloud account. After logging out, local data display will be restored." : "Currently a local account: nickname, avatar and game data are only saved locally. After logging in, you can enable cloud synchronization and friends/chat. Click on the avatar to change the avatar.");
    avatarHint.setTextColor(primaryTextColor());
    avatarHint.setTextSize(11);
    avatarHint.setPadding(0, dp(6), 0, dp(8));
    root.addView(avatarHint);

    LinearLayout accountRow = new LinearLayout(this);
    accountRow.setOrientation(LinearLayout.HORIZONTAL);
    Button loginBtn = krButton(isLoggedIn() ? "Account settings" : "登录 / 注册");
    Button syncBtn = krButton("Cloud sync");
    loginBtn.setTextColor(primaryTextColor());
    syncBtn.setTextColor(isLoggedIn() ? primaryTextColor() : getColorCompat(R.color.yh_text_muted));
    syncBtn.setEnabled(isLoggedIn());
    loginBtn.setOnClickListener(v -> showAuthPlaceholderDialog());
    syncBtn.setOnClickListener(v -> {
        if (!isLoggedIn()) {
            Toast.makeText(this, "Log in to use cloud sync", Toast.LENGTH_SHORT).show();
            return;
        }
        doServerSync(false);
    });
    accountRow.addView(loginBtn, new LinearLayout.LayoutParams(0, dp(40), 1));
    LinearLayout.LayoutParams syncLp = new LinearLayout.LayoutParams(0, dp(40), 1);
    syncLp.setMargins(dp(8), 0, 0, 0);
    accountRow.addView(syncBtn, syncLp);
    root.addView(accountRow);

    LinearLayout kunRow = new LinearLayout(this);
    kunRow.setOrientation(LinearLayout.HORIZONTAL);
    kunRow.setGravity(android.view.Gravity.CENTER_VERTICAL);
    kunRow.setPadding(0, dp(8), 0, dp(4));
    ImageView kunIcon = new ImageView(this);
    kunIcon.setImageResource(R.drawable.ic_kungal_logo);
    kunIcon.setScaleType(ImageView.ScaleType.FIT_CENTER);
    kunIcon.setAdjustViewBounds(true);
    LinearLayout.LayoutParams iconLp = new LinearLayout.LayoutParams(dp(22), dp(22));
    iconLp.setMargins(0, 0, dp(6), 0);
    kunRow.addView(kunIcon, iconLp);
    TextView kunStatus = new TextView(this);
    kunStatus.setText(kungalBound() ? "NextMoe · Bound" : "NextMoe · Unbound");
    kunStatus.setTextColor(kungalBound() ? 0xFFE8FFE9 : getColorCompat(R.color.yh_text_muted));
    kunStatus.setTextSize(12);
    kunStatus.setGravity(android.view.Gravity.CENTER_VERTICAL);
    kunRow.addView(kunStatus, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
    Button kunBindBtn = krButton(kungalBound() ? "Bound" : "Bind NextMoe");
    kunBindBtn.setTextColor(kungalBound() ? getColorCompat(R.color.yh_text_muted) : primaryTextColor());
    kunBindBtn.setEnabled(isLoggedIn() && !kungalBound());
    kunBindBtn.setOnClickListener(v -> startKungalBindOAuth());
    LinearLayout.LayoutParams kunBtnLp = new LinearLayout.LayoutParams(dp(118), dp(40));
    kunBtnLp.setMargins(dp(8), 0, 0, 0);
    kunRow.addView(kunBindBtn, kunBtnLp);
    root.addView(kunRow);

    LinearLayout hikariRow = new LinearLayout(this);
    hikariRow.setOrientation(LinearLayout.HORIZONTAL);
    hikariRow.setGravity(android.view.Gravity.CENTER_VERTICAL);
    hikariRow.setPadding(0, dp(8), 0, dp(4));
    ImageView hikariIcon = new ImageView(this);
    hikariIcon.setImageResource(R.drawable.ic_hikarinagi_logo);
    hikariIcon.setScaleType(ImageView.ScaleType.FIT_CENTER);
    hikariIcon.setAdjustViewBounds(true);
    LinearLayout.LayoutParams hikariIconLp = new LinearLayout.LayoutParams(dp(22), dp(22));
    hikariIconLp.setMargins(0, 0, dp(6), 0);
    hikariRow.addView(hikariIcon, hikariIconLp);
    TextView hikariStatus = new TextView(this);
    hikariStatus.setText(hikarinagiBound() ? "Hikarinagi · Bound" : "Hikarinagi · Unbound");
    hikariStatus.setTextColor(hikarinagiBound() ? 0xFFE8FFE9 : getColorCompat(R.color.yh_text_muted));
    hikariStatus.setTextSize(12);
    hikariStatus.setGravity(android.view.Gravity.CENTER_VERTICAL);
    hikariRow.addView(hikariStatus, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
    Button hikariBindBtn = krButton(hikarinagiBound() ? "Bound" : "Binding Hikarinagi");
    hikariBindBtn.setTextColor(hikarinagiBound() ? getColorCompat(R.color.yh_text_muted) : primaryTextColor());
    hikariBindBtn.setEnabled(isLoggedIn() && !hikarinagiBound());
    hikariBindBtn.setOnClickListener(v -> startHikarinagiBindOAuth());
    LinearLayout.LayoutParams hikariBtnLp = new LinearLayout.LayoutParams(dp(118), dp(40));
    hikariBtnLp.setMargins(dp(8), 0, 0, 0);
    hikariRow.addView(hikariBindBtn, hikariBtnLp);
    root.addView(hikariRow);

    LinearLayout statCards = new LinearLayout(this);
    statCards.setOrientation(LinearLayout.HORIZONTAL);
    statCards.setPadding(0, dp(2), 0, dp(10));
    statCards.addView(profileStatCard("Games", String.valueOf(allGames.size())), new LinearLayout.LayoutParams(0, dp(48), 1));
    LinearLayout.LayoutParams statMid = new LinearLayout.LayoutParams(0, dp(48), 1);
    statMid.setMargins(dp(6), 0, dp(6), 0);
    statCards.addView(profileStatCard("Total duration", TimeFormatUtil.playTime(total)), statMid);
    statCards.addView(profileStatCard("Today", TimeFormatUtil.playTime(todayTotalPlayTime())), new LinearLayout.LayoutParams(0, dp(48), 1));
    root.addView(statCards);

    Button aiReviewBtn = krButton("AI Week Review");
    aiReviewBtn.setTextColor(primaryTextColor());
    aiReviewBtn.setOnClickListener(v -> aiReviewController.showAiReviewDialog());
    root.addView(aiReviewBtn, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp(42)));
    TextView aiReviewHint = new TextView(this);
    aiReviewHint.setText("Generate a little devil's critical review based on the play records of the last 7 days; only the game name and statistics will be sent, but the path, archive or account information will not be sent.");
    aiReviewHint.setTextColor(getColorCompat(R.color.yh_text_muted));
    aiReviewHint.setTextSize(10);
    aiReviewHint.setPadding(0, dp(5), 0, dp(8));
    root.addView(aiReviewHint);

    TextView nameLabel = profileLabel("Nickname");
    root.addView(nameLabel);
    EditText nameInput = profileEdit(localName, "Enter nickname");
    root.addView(nameInput, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp(42)));

    TextView signLabel = profileLabel("Personal signature");
    signLabel.setPadding(0, dp(10), 0, dp(4));
    root.addView(signLabel);
    EditText signatureInput = profileEdit(currentSignature, "Write something, for example: I have to finish a work seriously today.");
    signatureInput.setSingleLine(false);
    signatureInput.setMinLines(2);
    signatureInput.setGravity(android.view.Gravity.TOP | android.view.Gravity.START);
    root.addView(signatureInput, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp(62)));

    TextView activityTitle = profileLabel("Today's news");
    activityTitle.setPadding(0, dp(12), 0, dp(4));
    root.addView(activityTitle);
    TextView activity = new TextView(this);
    activity.setText(buildTodayActivityText());
    activity.setTextColor(getColorCompat(R.color.yh_text_muted));
    activity.setTextSize(12);
    activity.setLineSpacing(dp(1), 1.0f);
    activity.setBackgroundResource(R.drawable.bg_input);
    activity.setPadding(dp(10), dp(8), dp(10), dp(8));
    root.addView(activity, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

    TextView recentTitle = profileLabel("Latest news");
    recentTitle.setPadding(0, dp(12), 0, dp(4));
    root.addView(recentTitle);
    LinearLayout feedList = new LinearLayout(this);
    feedList.setOrientation(LinearLayout.VERTICAL);
    buildRecentActivityViews(feedList);
    root.addView(feedList, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

    TextView backupTitle = profileLabel("Sync center");
    backupTitle.setPadding(0, dp(12), 0, dp(4));
    root.addView(backupTitle);
    Button syncCenterBtn = krButton("Open sync center");
    syncCenterBtn.setTextColor(primaryTextColor());
    syncCenterBtn.setOnClickListener(v -> showWebDavSettingsDialog());
    root.addView(syncCenterBtn, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp(42)));
    TextView backupHint = new TextView(this);
    backupHint.setText("The Sync Center includes WebDAV cloud synchronization, test connection, automatic synchronization and local backup/import.\nLocal backup and cloud synchronization use the same data structure to avoid inconsistent import and export logic.");
    backupHint.setTextColor(getColorCompat(R.color.yh_text_muted));
    backupHint.setTextSize(10);
    backupHint.setPadding(0, dp(6), 0, 0);
    root.addView(backupHint);

    ScrollView scroll = new ScrollView(this);
    scroll.setFillViewport(false);
    scroll.setBackgroundResource(R.drawable.bg_dialog);
    tintDialogRoot(scroll);
    scroll.addView(root, new ScrollView.LayoutParams(ScrollView.LayoutParams.MATCH_PARENT, ScrollView.LayoutParams.WRAP_CONTENT));

    AlertDialog dialog = new AlertDialog.Builder(this)
            .setTitle("Profile")
            .setView(scroll)
            .setPositiveButton("Save", null)
            .setNeutralButton("Change avatar", null)
            .setNegativeButton("Close", null)
            .show();
    styleAlertDialogDark(dialog);
    pendingProfileDialog = dialog;
    dialog.setOnDismissListener(d -> { if (pendingProfileDialog == dialog) pendingProfileDialog = null; });
    if (dialog.getWindow() != null) {
        dialog.getWindow().setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.62f), (int) (getResources().getDisplayMetrics().heightPixels * 0.82f));
    }
    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
        String name = nameInput.getText() == null ? "" : nameInput.getText().toString().trim();
        String sign = signatureInput.getText() == null ? "" : signatureInput.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(MainActivity.this, "Nickname cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        prefs.edit().putString(KEY_PROFILE_NAME, name).putString(KEY_PROFILE_SIGNATURE, sign).apply();
        updateProfilePanel();
        Toast.makeText(MainActivity.this, "Profile saved", Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    });
    dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(v -> profileAvatarLauncher.launch("image/*"));
}

/** 等级颜色（与社区 Web 端每5级一档一致） */
private int levelColorForLevel(int level) {
    String hex = "#B9BCC7";
    if (level >= 30) hex = "#FFD27A";
    else if (level >= 25) hex = "#FF9090";
    else if (level >= 20) hex = "#FFB37A";
    else if (level >= 15) hex = "#C9A0FF";
    else if (level >= 10) hex = "#7DB8FF";
    else if (level >= 5) hex = "#7EE2A0";
    try {
        return android.graphics.Color.parseColor(hex);
    } catch (Throwable t) {
        return 0xFFB9BCC7;
    }
}

/**
 * 云端昵称修改弹窗（个人资料页头像右侧铅笔按钮触发）。
 * 调用服务器 /user/update_nickname 更新云端昵称主数据源，
 * 成功后同步更新本地 auth_nickname 缓存并刷新界面。
 */
private void showCloudNicknameDialog(final TextView nameViewToUpdate) {
    if (!isLoggedIn()) {
        Toast.makeText(this, "You must log in to modify your cloud nickname.", Toast.LENGTH_SHORT).show();
        return;
    }
    final String current = displayProfileName();

    final EditText input = new EditText(this);
    input.setText(current);
    input.setHint("2-20 characters");
    input.setSingleLine(true);
    input.setTextColor(getColorCompat(R.color.yh_text));
    input.setHintTextColor(getColorCompat(R.color.yh_text_muted));
    input.setBackgroundResource(R.drawable.bg_input);
    input.setPadding(dp(10), dp(8), dp(10), dp(8));
    if (input.getText() != null) input.setSelection(input.getText().length());

    LinearLayout wrap = new LinearLayout(this);
    wrap.setOrientation(LinearLayout.VERTICAL);
    wrap.setBackgroundResource(R.drawable.bg_dialog);
    tintDialogRoot(wrap);
    wrap.setPadding(dp(18), dp(14), dp(18), dp(10));
    wrap.addView(input, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp(44)));
    TextView tip = new TextView(this);
    tip.setText("The nickname will be synced to the cloud and can be seen by friends and the community. It cannot contain special characters such as < > { } [ ] / \\ etc.");
    tip.setTextColor(getColorCompat(R.color.yh_text_muted));
    tip.setTextSize(11);
    tip.setPadding(dp(2), dp(8), dp(2), 0);
    wrap.addView(tip);

    AlertDialog dialog = new AlertDialog.Builder(this)
            .setTitle("Modify nickname")
            .setView(wrap)
            .setPositiveButton("Save", null)
            .setNegativeButton("Cancel", null)
            .show();
    styleAlertDialogDark(dialog);

    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
        final String name = input.getText() == null ? "" : input.getText().toString().trim();
        if (name.length() < 2 || name.length() > 20) {
            Toast.makeText(MainActivity.this, "Nickname requires 2-20 characters", Toast.LENGTH_SHORT).show();
            return;
        }
        if (name.matches(".*[<>{}\\[\\]/\\\\].*")) {
            Toast.makeText(MainActivity.this, "Nickname contains disallowed characters", Toast.LENGTH_SHORT).show();
            return;
        }
        v.setEnabled(false);
        AppExecutors.runOnIo(() -> {
            try {
                com.yuki.yukihub.social.SocialApiClient client = new com.yuki.yukihub.social.SocialApiClient(MainActivity.this);
                final String newName = client.updateNickname(name);
                runOnUiThread(() -> {
                    // 更新本地云端昵称缓存 + 本地昵称，避免下次同步被本地旧值覆盖
                    if (prefs != null) {
                        prefs.edit()
                                .putString(KEY_AUTH_NICKNAME, newName)
                                .putString(KEY_PROFILE_NAME, newName)
                                .apply();
                    }
                    if (nameViewToUpdate != null) nameViewToUpdate.setText(newName);
                    updateProfilePanel();
                    Toast.makeText(MainActivity.this, "Nickname has been modified", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                });
            } catch (Throwable t) {
                runOnUiThread(() -> {
                    v.setEnabled(true);
                    Toast.makeText(MainActivity.this, "Modification failed:" + t.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        });
    });
}

private String profileName() {
    return prefs == null ? "Yuki" : prefs.getString(KEY_PROFILE_NAME, "Yuki");
}

private String profileSignature() {
    return prefs == null ? "" : prefs.getString(KEY_PROFILE_SIGNATURE, "");
}

private long totalPlayTime() {
    long total = 0;
    for (Game g : allGames) if (g != null) total += g.totalPlayTime;
    return total;
}



private String accountStatusLabelForDialog() {
    String s = accountStatus();
    if ("local".equals(s)) return "Local account";
    if (AUTH_STATUS_ONLINE.equals(s)) return "Cloud Account · Online";
    if (AUTH_STATUS_SYNCING.equals(s)) return "Cloud account · Synchronizing";
    if (AUTH_STATUS_EXPIRED.equals(s)) return "Cloud account · Login expired";
    return "Cloud Account · Offline";
}

private int accountStatusBackground() {
    String s = accountStatus();
    if ("local".equals(s)) return R.drawable.bg_account_status_local;
    if (AUTH_STATUS_ONLINE.equals(s)) return R.drawable.bg_account_status_online;
    if (AUTH_STATUS_SYNCING.equals(s)) return R.drawable.bg_status_playing;
    if (AUTH_STATUS_EXPIRED.equals(s)) return R.drawable.bg_account_status_expired;
    return R.drawable.bg_account_status_offline;
}

private int accountStatusTextColor() {
    String s = accountStatus();
    if (AUTH_STATUS_ONLINE.equals(s)) return 0xFFE8FFE9;
    if (AUTH_STATUS_EXPIRED.equals(s)) return 0xFFFFF0D6;
    if (AUTH_STATUS_SYNCING.equals(s)) return 0xFFEAF7FF;
    if ("local".equals(s)) return 0xFFDCEBFF;
    return 0xFFE3E8F2;
}

private boolean kungalBound() {
    return prefs != null && prefs.getBoolean(KEY_KUN_BOUND, false);
}

private void startKungalBindOAuth() {
    if (!isLoggedIn()) {
        Toast.makeText(this, "Please log in to your YukiHub account first", Toast.LENGTH_SHORT).show();
        return;
    }
    try {
        String state = randomUrlSafe(32);
        String verifier = randomUrlSafe(64);
        String challenge = pkceS256(verifier);
        prefs.edit()
                .putString(KEY_KUN_OAUTH_STATE, state)
                .putString(KEY_KUN_OAUTH_CODE_VERIFIER, verifier)
                .putLong(KEY_KUN_OAUTH_STARTED_AT, System.currentTimeMillis())
                .putString(KEY_KUN_OAUTH_MODE, KUN_OAUTH_MODE_BIND)
                .apply();
        Uri uri = Uri.parse(KUN_OAUTH_AUTHORIZE_URL).buildUpon()
                .appendQueryParameter("client_id", KUN_ANDROID_CLIENT_ID)
                .appendQueryParameter("redirect_uri", KUN_OAUTH_REDIRECT_URI)
                .appendQueryParameter("response_type", "code")
                .appendQueryParameter("state", state)
                .appendQueryParameter("scope", KUN_OAUTH_SCOPE)
                .appendQueryParameter("code_challenge", challenge)
                .appendQueryParameter("code_challenge_method", "S256")
                .build();
        startActivity(new Intent(Intent.ACTION_VIEW, uri).addCategory(Intent.CATEGORY_BROWSABLE));
    } catch (Throwable t) {
        Toast.makeText(this, "Unable to open NextMoe·Weimeng binding page:" + emptyText(t.getMessage(), "Please check your browser"), Toast.LENGTH_LONG).show();
    }
}

private boolean hikarinagiBound() {
    return prefs != null && prefs.getBoolean(KEY_HIKARINAGI_BOUND, false);
}

private void startHikarinagiBindOAuth() {
    if (!isLoggedIn()) {
        Toast.makeText(this, "Please log in to your YukiHub account first", Toast.LENGTH_SHORT).show();
        return;
    }
    try {
        String state = randomUrlSafe(32);
        String verifier = randomUrlSafe(64);
        String challenge = pkceS256(verifier);
        String nonce = randomUrlSafe(16);
        prefs.edit()
                .putString(KEY_HIKARINAGI_OAUTH_STATE, state)
                .putString(KEY_HIKARINAGI_OAUTH_CODE_VERIFIER, verifier)
                .putString(KEY_HIKARINAGI_OAUTH_NONCE, nonce)
                .putLong(KEY_HIKARINAGI_OAUTH_STARTED_AT, System.currentTimeMillis())
                .putString(KEY_HIKARINAGI_OAUTH_MODE, HIKARINAGI_OAUTH_MODE_BIND)
                .apply();
        Uri uri = Uri.parse(HIKARINAGI_OAUTH_AUTHORIZE_URL).buildUpon()
                .appendQueryParameter("response_type", "code")
                .appendQueryParameter("client_id", HIKARINAGI_ANDROID_CLIENT_ID)
                .appendQueryParameter("redirect_uri", HIKARINAGI_OAUTH_REDIRECT_URI)
                .appendQueryParameter("scope", HIKARINAGI_OAUTH_SCOPE)
                .appendQueryParameter("state", state)
                .appendQueryParameter("nonce", nonce)
                .appendQueryParameter("code_challenge", challenge)
                .appendQueryParameter("code_challenge_method", "S256")
                .build();
        startActivity(new Intent(Intent.ACTION_VIEW, uri).addCategory(Intent.CATEGORY_BROWSABLE));
    } catch (Throwable t) {
        Toast.makeText(this, "Unable to open Hikarinagi binding page:" + emptyText(t.getMessage(), "Please check your browser"), Toast.LENGTH_LONG).show();
    }
}

private String randomUrlSafe(int bytes) {
    byte[] data = new byte[bytes];
    new SecureRandom().nextBytes(data);
    return Base64.getUrlEncoder().withoutPadding().encodeToString(data);
}

private String pkceS256(String verifier) throws Exception {
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    byte[] digest = md.digest(verifier.getBytes(StandardCharsets.US_ASCII));
    return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
}

private void showAuthPlaceholderDialog() {
    if (isLoggedIn()) {
        showAccountSettingsDialog();
        return;
    }
    // 解除限制，直接跳转登录/注册界面
    startActivity(new Intent(this, AuthActivity.class));
}

private void showFriendsChatPlaceholder() {
    new com.yuki.yukihub.social.FriendsChatDialog(this).show();
}

private void showSortDialog() {
    String[] items = {"Recently visited", "Recently added", "Name sort"};
    String current = prefs == null ? SORT_MODE_RECENT : prefs.getString(KEY_SORT_MODE, SORT_MODE_RECENT);
    int checkedItem = SORT_MODE_NEWEST.equals(current) ? 1 : (SORT_MODE_NAME.equals(current) ? 2 : 0);
    final int[] selected = {checkedItem};
    AlertDialog dialog = new AlertDialog.Builder(this)
            .setTitle("Sort by")
            .setSingleChoiceItems(items, checkedItem, (d, which) -> {
                selected[0] = which;
            })
            .setPositiveButton("OK", (d, w) -> {
                String mode = SORT_MODE_RECENT;
                if (selected[0] == 1) mode = SORT_MODE_NEWEST;
                else if (selected[0] == 2) mode = SORT_MODE_NAME;
                if (prefs != null) prefs.edit().putString(KEY_SORT_MODE, mode).apply();
                applyFilter();
            })
            .setNegativeButton("Cancel", null)
            .show();
    styleAlertDialogDark(dialog);
    // Color the radio list items (needs post to run after ListView measures children)
    try {
        android.widget.ListView list = dialog.getListView();
        if (list != null) {
            list.setBackgroundColor(Color.TRANSPARENT);
            list.post(() -> {
                for (int i = 0; i < list.getChildCount(); i++) {
                    View child = list.getChildAt(i);
                    if (child instanceof android.widget.CheckedTextView) {
                        ((android.widget.CheckedTextView) child).setTextColor(
                                i == selected[0] ? getColorCompat(R.color.yh_primary) : getColorCompat(R.color.yh_text));
                    }
                }
            });
            list.setOnItemClickListener((parent, view, position, lid) -> {
                selected[0] = position;
                list.post(() -> {
                    for (int i = 0; i < parent.getChildCount(); i++) {
                        View c = parent.getChildAt(i);
                        if (c instanceof android.widget.CheckedTextView) {
                            ((android.widget.CheckedTextView) c).setTextColor(
                                    i == position ? getColorCompat(R.color.yh_primary) : getColorCompat(R.color.yh_text));
                        }
                    }
                });
            });
        }
    } catch (Throwable ignored) { }
}

private void showNoticeDialog() {
    // 清除红点
    View dot = findViewById(R.id.noticeRedDot);
    if (dot != null) dot.setVisibility(View.GONE);
    AlertDialog dialog = new AlertDialog.Builder(this)
            .setTitle("Message notification")
            .setMessage("No new news yet.")
            .setPositiveButton("Knew", null)
            .show();
    styleAlertDialogDark(dialog);
}

private android.os.Handler statusBarHandler;
private Runnable statusBarRunnable;
private android.content.BroadcastReceiver batteryReceiver;

private void startStatusBarUpdates() {
    if (statusBarHandler == null) statusBarHandler = new android.os.Handler(android.os.Looper.getMainLooper());
    if (statusBarRunnable == null) statusBarRunnable = new Runnable() {
        @Override
        public void run() {
            updateClock();
            updateChatBadge();
            if (statusBarHandler != null) statusBarHandler.postDelayed(this, 5000L);
        }
    };
    statusBarHandler.post(statusBarRunnable);
    updateBatteryLevel();
    registerBatteryReceiver();
}

private void stopStatusBarUpdates() {
    if (statusBarHandler != null && statusBarRunnable != null) {
        statusBarHandler.removeCallbacks(statusBarRunnable);
    }
    if (batteryReceiver != null) {
        try { unregisterReceiver(batteryReceiver); } catch (Throwable ignored) { }
        batteryReceiver = null;
    }
}

private void updateClock() {
    TextView tvClock = findViewById(R.id.tvClock);
    if (tvClock == null) return;
    java.util.Calendar cal = java.util.Calendar.getInstance();
    int hour = cal.get(java.util.Calendar.HOUR_OF_DAY);
    int minute = cal.get(java.util.Calendar.MINUTE);
    int second = cal.get(java.util.Calendar.SECOND);
    String timeStr = String.format(java.util.Locale.getDefault(), "%02d:%02d:%02d", hour, minute, second);
    tvClock.setText(com.yuki.yukihub.util.IconedText.build(this, R.drawable.ic_nav_clock, timeStr, 9f, 0));
}

private void updateBatteryLevel() {
    try {
        android.os.BatteryManager bm = (android.os.BatteryManager) getSystemService(BATTERY_SERVICE);
        if (bm == null) return;
        int level = bm.getIntProperty(android.os.BatteryManager.BATTERY_PROPERTY_CAPACITY);
        int status = bm.isCharging() ? 1 : 0;
        TextView tvBattery = findViewById(R.id.tvBatteryLevel);
        if (tvBattery == null) return;
        String label = level + "%";
        int color;
        int iconRes;
        if (status == 1) {
            iconRes = R.drawable.ic_st_battery_charging;
            color = getColorCompat(R.color.yh_success);
        } else if (level <= 20) {
            iconRes = R.drawable.ic_st_battery;
            color = getColorCompat(R.color.yh_secondary);
        } else {
            iconRes = R.drawable.ic_st_battery;
            color = getColorCompat(R.color.yh_text);
        }
        tvBattery.setText(com.yuki.yukihub.util.IconedText.build(this, iconRes, label, 9f, color));
        tvBattery.setTextColor(color);
    } catch (Throwable ignored) { }
}

private void updateChatBadge() {
    if (!isLoggedIn()) return;
    TextView badge = findViewById(R.id.navChatBadge);
    if (badge == null) return;
    com.yuki.yukihub.util.AppExecutors.runOnIo(() -> {
        try {
            com.yuki.yukihub.social.SocialApiClient client = new com.yuki.yukihub.social.SocialApiClient(this);
            int unread = client.getTotalUnread();
            // 调用 getFriendsList() 刷新 pendingRequests 缓存，再取好友申请数
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
            Log.w("YukiHub", "profile badge fetch failed: " + t.getMessage(), t);
        }
    });
}

private void registerBatteryReceiver() {
    if (batteryReceiver != null) return;
    batteryReceiver = new android.content.BroadcastReceiver() {
        @Override
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            updateBatteryLevel();
        }
    };
    android.content.IntentFilter filter = new android.content.IntentFilter();
    filter.addAction(android.content.Intent.ACTION_BATTERY_CHANGED);
    filter.addAction(android.content.Intent.ACTION_BATTERY_LOW);
    try { registerReceiver(batteryReceiver, filter); } catch (Throwable ignored) { }
}

private void showAuthDialog() {
    if (isLoggedIn()) {
        showAccountSettingsDialog();
        return;
    }
    startActivity(new Intent(this, AuthActivity.class));
}

private void showAccountSettingsDialog() {
    LinearLayout root = new LinearLayout(this);
    root.setOrientation(LinearLayout.VERTICAL);
    int pad = dp(16);
    root.setPadding(pad, dp(12), pad, dp(4));
    TextView info = new TextView(this);
    String email = prefs == null ? "" : prefs.getString(KEY_AUTH_EMAIL, "");
    String name = displayProfileName();
    String uid = prefs == null ? "" : prefs.getString(KEY_AUTH_UID, "");
    boolean syncEnabled = prefs != null && prefs.getBoolean(KEY_CLOUD_SYNC_ENABLED, false);
    boolean sharePlaying = prefs == null || prefs.getBoolean(KEY_SHARE_PLAYING, true);
    boolean friendNotify = prefs == null || prefs.getBoolean(KEY_FRIEND_PLAY_NOTIFY, true);
    long lastSync = prefs == null ? 0 : prefs.getLong(KEY_LAST_SYNC_AT, 0);
    info.setText("Account:" + name + "\nUID：" + (uid.isEmpty() ? "-" : uid) + "Mail:" + emptyText(email, "-") + "State:" + accountStatusLabelForDialog() + "Automatic synchronization:" + (syncEnabled ? "Turn on" : "Close") + "Last sync:" + (lastSync > 0 ? TimeFormatUtil.date(lastSync) : "Not synced yet"));
    info.setTextColor(getColorCompat(R.color.yh_text_muted));
    info.setTextSize(13);
    info.setLineSpacing(dp(2), 1.0f);
    root.addView(info);
    CheckBox syncCheck = krCheckBox("Turn on automatic synchronization (automatic cloud synchronization at startup)", syncEnabled);
    syncCheck.setPadding(0, dp(10), 0, 0);
    root.addView(syncCheck);
    CheckBox sharePlayingCheck = krCheckBox("Show your friends what you're playing", sharePlaying);
    sharePlayingCheck.setPadding(0, dp(8), 0, 0);
    root.addView(sharePlayingCheck);
    CheckBox friendNotifyCheck = krCheckBox("Notify when friends start playing games (background keep-alive)", friendNotify);
    friendNotifyCheck.setPadding(0, dp(8), 0, 0);
    root.addView(friendNotifyCheck);
    TextView notifyHint = new TextView(this);
    notifyHint.setText("When turned on, a low-key resident notification will be displayed, used to stay online and monitor friends' play.");
    notifyHint.setTextColor(getColorCompat(R.color.yh_text_muted));
    notifyHint.setTextSize(11);
    notifyHint.setPadding(0, dp(4), 0, 0);
    root.addView(notifyHint);
    // 通知通道设置入口：允许用户手动调整横幅/铃声/锁屏
    TextView channelSettingsBtn = new TextView(this);
    channelSettingsBtn.setText("⚙ Notification style settings (banner · ringtone · lock screen)");
    channelSettingsBtn.setTextColor(getColorCompat(R.color.yh_primary));
    channelSettingsBtn.setTextSize(12);
    channelSettingsBtn.setPadding(0, dp(8), 0, 0);
    channelSettingsBtn.setOnClickListener(v -> {
        ensureNotificationPermission();
        // 如果权限已授予，直接打开通道设置；否则等权限回调自动引导
        if (Build.VERSION.SDK_INT < 33 ||
                checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            openChannelSettings();
        }
    });
    root.addView(channelSettingsBtn);

    // 包一层 ScrollView，防止小屏设备内容被遮挡
    ScrollView scrollView = new ScrollView(this);
    scrollView.addView(root, new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

    AlertDialog dialog = new AlertDialog.Builder(this)
            .setTitle("Account settings")
            .setView(scrollView)
            .setPositiveButton("Save", null)
            .setNeutralButton("Log out", null)
            .setNegativeButton("Close", null)
            .show();
    styleAlertDialogDark(dialog);
    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
        boolean enableNotify = friendNotifyCheck.isChecked();
        prefs.edit()
                .putBoolean(KEY_CLOUD_SYNC_ENABLED, syncCheck.isChecked())
                .putBoolean(KEY_SHARE_PLAYING, sharePlayingCheck.isChecked())
                .putBoolean(KEY_FRIEND_PLAY_NOTIFY, enableNotify)
                .apply();
        if (enableNotify) ensureNotificationPermission();
        com.yuki.yukihub.social.PresenceService.sync(MainActivity.this);
        updateProfilePanel();
        Toast.makeText(MainActivity.this, "Account settings saved", Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    });
    dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(v -> confirmLogout(dialog));
}

private void ensureNotificationPermission() {
        if (android.os.Build.VERSION.SDK_INT < 33) return;
        try {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                    == android.content.pm.PackageManager.PERMISSION_GRANTED) return;
            // 先弹解释弹窗，再请求权限（QQ 做法）
            new AlertDialog.Builder(this)
                    .setTitle("Notification permission required")
                    .setMessage("YukiHub requires notification permission to remind you \"what games your friends started playing\" when you play games.\n\nAfter clicking \"Allow\", the system will pop up a permission request.")
                    .setPositiveButton("Continue", (d, w) -> {
                        d.dismiss();
                        requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 9910);
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        } catch (Throwable ignored) {}
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 9910) {
            boolean granted = grantResults.length > 0
                    && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED;
            if (granted) {
                // 权限已授予，引导用户打开通道详情页（横幅/铃声/锁屏可能默认开启）
                showNotificationChannelGuide();
            } else {
                // 用户拒绝：引导去系统设置手动开启
                showNotificationDeniedGuide();
            }
        }
    }

    /** 通知权限被拒绝后，引导用户去系统设置手动开启 */
    private void showNotificationDeniedGuide() {
        try {
            new AlertDialog.Builder(this)
                    .setTitle("Notification permission is not enabled")
                    .setMessage("If you deny notification permission, you will not be able to receive reminders when your friends start playing.\n\nYou can go to system settings to turn it on manually at any time.")
                    .setPositiveButton("Go to settings", (d, w) -> {
                        d.dismiss();
                        openAppNotificationSettings();
                    })
                    .setNegativeButton("Later", null)
                    .show();
        } catch (Throwable ignored) {}
    }

    /** 通知权限已授予后，引导用户确认通知通道设置（横幅/铃声/振动/锁屏） */
    private void showNotificationChannelGuide() {
        try {
            new AlertDialog.Builder(this)
                    .setTitle("Notifications are on")
                    .setMessage("You can further adjust the notification style: whether to pop up a banner, whether to ring, and whether to display on the lock screen.\n\nClick the button below to enter notification channel settings.")
                    .setPositiveButton("Go to settings", (d, w) -> {
                        d.dismiss();
                        openChannelSettings();
                    })
                    .setNegativeButton("Keep default", null)
                    .show();
        } catch (Throwable ignored) {}
    }

    /** 打开 App 通知设置页 */
    private void openAppNotificationSettings() {
        try {
            Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
            startActivity(intent);
        } catch (Throwable e) {
            // 降级：打开全部通知设置
            try {
                startActivity(new Intent("android.settings.NOTIFICATION_SETTINGS"));
            } catch (Throwable ignored) {}
        }
    }

    /** 直接打开 friend_play_v2 通知通道详情页（横幅/铃声/锁屏开关） */
    private void openChannelSettings() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                intent.putExtra(Settings.EXTRA_CHANNEL_ID,
                        com.yuki.yukihub.social.FriendNotifier.CHANNEL_FRIEND_PLAY);
                startActivity(intent);
            }
        } catch (Throwable e) {
            openAppNotificationSettings();
        }
    }

private void confirmLogout(AlertDialog parent) {
    AlertDialog d = new AlertDialog.Builder(this)
            .setTitle("Log out")
            .setMessage("退出登录不会删除本地游戏库和本地个人资料。云同步、好友/聊天将暂停。")
            .setPositiveButton("Log out", (x, w) -> {
                logoutLocalOnly();
                if (parent != null) parent.dismiss();
                refreshRuntimeAccountState();
                if (pendingProfileDialog != null && pendingProfileDialog.isShowing()) pendingProfileDialog.dismiss();
                getWindow().getDecorView().postDelayed(() -> {
                    if (!isFinishing() && !(android.os.Build.VERSION.SDK_INT >= 17 && isDestroyed())) showProfileDialog();
                }, 150L);
            })
            .setNegativeButton("Cancel", null)
            .show();
    styleAlertDialogDark(d);
}

private void showWebDavSettingsDialog() {
    com.yuki.yukihub.sync.WebDavSettingsDialog dialog = com.yuki.yukihub.sync.WebDavSettingsDialog.newInstance();
    dialog.show(getSupportFragmentManager(), "webdav_settings");
}

private void maybeAutoWebDavSync() {
    if (webDavAutoSyncRunning) return;
    com.yuki.yukihub.sync.SyncManager sm = new com.yuki.yukihub.sync.SyncManager(this);
    if (!sm.isConfigured() || !sm.isAutoSyncEnabled()) return;
    long last = sm.getLastSyncTime();
    if (last > 0 && System.currentTimeMillis() - last < 10L * 60L * 1000L) return;
    webDavAutoSyncRunning = true;
    sm.sync(new com.yuki.yukihub.sync.SyncManager.SyncListener() {
        @Override public void onSyncStart() { }
        @Override public void onProgress(String item, boolean changed) { }
        @Override public int onConflict(com.yuki.yukihub.sync.SyncManager.Conflict conflict) { return com.yuki.yukihub.sync.SyncManager.RESOLVE_MERGE; }
        @Override public void onSyncComplete(com.yuki.yukihub.sync.SyncManager.SyncResult result) {
            runOnUiThread(() -> {
                webDavAutoSyncRunning = false;
                if (result != null && result.hasChanges()) {
                    coverMaintenanceDone = false;
                    loadGames();
                    updateProfilePanel();
                    Toast.makeText(MainActivity.this, "WebDAV automatic synchronization completed", Toast.LENGTH_SHORT).show();
                }
            });
        }
        @Override public void onError(String error) {
            runOnUiThread(() -> webDavAutoSyncRunning = false);
        }
    });
}

// ========== 服务器云同步 ==========

private boolean serverSyncRunning = false;

/**
 * 手动触发服务器云同步。
 * @param silent true=自动同步（不弹Toast），false=手动点击（有UI反馈）
 */
private void doServerSync(boolean silent) {
    if (serverSyncRunning) {
        if (!silent) Toast.makeText(this, "Syncing...", Toast.LENGTH_SHORT).show();
        return;
    }
    serverSyncRunning = true;
    com.yuki.yukihub.sync.SyncManager sm = new com.yuki.yukihub.sync.SyncManager(this);
    if (!silent) Toast.makeText(this, "Start cloud sync...", Toast.LENGTH_SHORT).show();
    sm.syncToServer(new com.yuki.yukihub.sync.SyncManager.SyncListener() {
        @Override public void onSyncStart() { }
        @Override public void onProgress(String item, boolean changed) { }
        @Override public int onConflict(com.yuki.yukihub.sync.SyncManager.Conflict conflict) {
            return com.yuki.yukihub.sync.SyncManager.RESOLVE_MERGE;
        }
        @Override public void onSyncComplete(com.yuki.yukihub.sync.SyncManager.SyncResult result) {
            runOnUiThread(() -> {
                serverSyncRunning = false;
                if (result != null) {
                    coverMaintenanceDone = false;
                    loadGames();
                    updateProfilePanel();
                    if (!silent) {
                        int showBytes = result.compressedBytes > 0 ? result.compressedBytes : result.localBytes;
                        String msg;
                        if (result.merged) {
                            msg = "Cloud synchronization completed·Smart merge·" + (showBytes / 1024) + "KB";
                        } else if (result.downloaded) {
                            msg = "Cloud synchronization completed · Cloud data downloaded ·" + (showBytes / 1024) + "KB";
                        } else if (result.uploaded) {
                            msg = "Cloud synchronization completed · Local data has been uploaded ·" + (showBytes / 1024) + "KB";
                        } else if (result.noChanges) {
                            msg = "Cloud synchronization completed · Data is up to date ·" + (showBytes / 1024) + "KB";
                        } else if (result.cancelled) {
                            msg = "Cloud sync has been canceled";
                        } else {
                            msg = "Cloud synchronization completed ·" + (showBytes / 1024) + "KB";
                        }
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        @Override public void onError(String error) {
            runOnUiThread(() -> {
                serverSyncRunning = false;
                if (!silent) Toast.makeText(MainActivity.this, "Cloud sync failed:" + error, Toast.LENGTH_LONG).show();
            });
        }
    });
}

/**
 * App 启动时自动服务器同步（静默）。
 * 1. 如果是登录后首次（needs_initial_sync=true），无视开关状态直接同步一次
 * 2. 否则检查自动同步开关，按常规逻辑走
 */
private void maybeAutoServerSync() {
    if (serverSyncRunning) return;
    if (!isLoggedIn()) return;

    // ===== 登录后首次同步（独立于自动同步开关）=====
    if (prefs != null && prefs.getBoolean("needs_initial_sync", false)) {
        prefs.edit().putBoolean("needs_initial_sync", false).apply();
        doServerSync(true);  // 静默同步
        return;
    }

    serverSyncRunning = true;
    com.yuki.yukihub.sync.SyncManager sm = new com.yuki.yukihub.sync.SyncManager(this);
    boolean triggered = sm.maybeAutoSyncToServer(new com.yuki.yukihub.sync.SyncManager.SyncListener() {
        @Override public void onSyncStart() { }
        @Override public void onProgress(String item, boolean changed) { }
        @Override public int onConflict(com.yuki.yukihub.sync.SyncManager.Conflict conflict) {
            return com.yuki.yukihub.sync.SyncManager.RESOLVE_MERGE;
        }
        @Override public void onSyncComplete(com.yuki.yukihub.sync.SyncManager.SyncResult result) {
            runOnUiThread(() -> {
                serverSyncRunning = false;
                if (result != null) {
                    if (result.hasChanges()) {
                        coverMaintenanceDone = false;
                        loadGames();
                        updateProfilePanel();
                    }
                    // 自动同步也弹窗，让用户知道发生了什么
                    int showBytes = result.compressedBytes > 0 ? result.compressedBytes : result.localBytes;
                    String msg;
                    if (result.merged) {
                        msg = "Automatic cloud synchronization · Smart merge ·" + (showBytes / 1024) + "KB";
                    } else if (result.downloaded) {
                        msg = "Automatic cloud synchronization · Downloaded cloud data ·" + (showBytes / 1024) + "KB";
                    } else if (result.uploaded) {
                        msg = "Automatic cloud synchronization · Local data uploaded ·" + (showBytes / 1024) + "KB";
                    } else if (result.noChanges) {
                        msg = "Automatic cloud synchronization · Data is up to date";
                    } else if (result.cancelled) {
                        msg = "Automatic cloud sync canceled";
                    } else {
                        msg = "Automatic cloud synchronization completed ·" + (showBytes / 1024) + "KB";
                    }
                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            });
        }
        @Override public void onError(String error) {
            runOnUiThread(() -> {
                serverSyncRunning = false;
                Toast.makeText(MainActivity.this, "Automatic cloud sync failed:" + error, Toast.LENGTH_LONG).show();
            });
        }
    });
    if (!triggered) serverSyncRunning = false;
}

private void logoutLocalOnly() {
    // 停止心跳与后台服务，尽力标记离线
    try { com.yuki.yukihub.social.PresenceService.stop(this); } catch (Throwable ignored) {}
    if (presenceManager != null) presenceManager.markOffline();
    if (prefs != null) prefs.edit()
            .remove(KEY_AUTH_ACCESS_TOKEN)
            .remove(KEY_AUTH_REFRESH_TOKEN)
            .remove(KEY_AUTH_USER_ID)
            .remove(KEY_AUTH_UID)
            .remove(KEY_AUTH_NICKNAME)
            .remove(KEY_AUTH_AVATAR)
            .remove(KEY_AUTH_STATUS)
            .remove(KEY_KUN_BOUND)
            .remove(KEY_HIKARINAGI_BOUND)
            .remove("server_last_sync_hash")
            .remove(KEY_LAST_SYNC_AT)
            .remove("needs_initial_sync")
            .remove("current_playing_activity")
            .putBoolean(KEY_CLOUD_SYNC_ENABLED, false)
            .apply();
    updateProfilePanel();
    Toast.makeText(MainActivity.this, "You have logged out, but your local account can still be used.", Toast.LENGTH_SHORT).show();
}

private String normalizeBaseUrl(String base) {
    if (base == null) return "";
    String s = base.trim();
    while (s.endsWith("/")) s = s.substring(0, s.length() - 1);
    return s;
}

private void performAuthRequest(boolean register, String email, String password, String nickname, Runnable onSuccess, Runnable onFailureUi) {
    final String base = normalizeBaseUrl(AUTH_BASE_URL);
    AppExecutors.runOnIo(() -> {
        try {
            String endpoint = register ? "/auth/register" : "/auth/login";
            // 登录/注册改用 POST + JSON body，密码不再进入 URL 与访问日志（见 AuthActivity 同步修改）。
            JSONObject body = new JSONObject();
            body.put("email", email);
            body.put("password", password);
            if (register) body.put("nickname", nickname);
            JSONObject resp = postJson(base + endpoint, body, null);

            saveAuthSession(resp, email, nickname);
            runOnUiThread(() -> {
                updateProfilePanel();
                Toast.makeText(this, register ? "Registered and logged in successfully" : "Login successful", Toast.LENGTH_SHORT).show();
                if (onSuccess != null) onSuccess.run();
                // 登录成功后自动触发一次云同步（有弹窗反馈）
                doServerSync(false);
            });
        } catch (Throwable t) {
            Log.w("YukiHub", "auth failed", t);
            runOnUiThread(() -> {
                if (prefs != null && isLoggedIn()) prefs.edit().putString(KEY_AUTH_STATUS, AUTH_STATUS_OFFLINE).apply();
                updateProfilePanel();
                Toast.makeText(this, "Login/registration failed:" + emptyText(t.getMessage(), "Please check the network or try again later"), Toast.LENGTH_LONG).show();
                if (onFailureUi != null) onFailureUi.run();
            });
        }
    });
}

private JSONObject getJson(String urlStr) throws Exception {
    HttpURLConnection c = (HttpURLConnection) new URL(urlStr).openConnection();
    c.setRequestMethod("GET");
    c.setInstanceFollowRedirects(true);
    c.setConnectTimeout(15000);
    c.setReadTimeout(20000);
    c.setRequestProperty("Accept", "application/json,text/plain,*/*");
    c.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
    c.setRequestProperty("User-Agent", BROWSER_UA);
    c.setRequestProperty("Referer", "https://yukihub.kesug.com/");
    int code = c.getResponseCode();
    String text = readSmallText(code >= 200 && code < 300 ? c.getInputStream() : c.getErrorStream());
    if (text != null && text.trim().startsWith("<")) {
        throw new RuntimeException("The server returned an HTML page, which may be a free host protection page/cache page. Please try again later.");
    }
    if (code < 200 || code >= 300) throw new RuntimeException("HTTP " + code + ": " + text);
    return text == null || text.trim().isEmpty() ? new JSONObject() : new JSONObject(text);
}

private JSONObject postJson(String url, JSONObject body, String bearerToken) throws Exception {
    HttpURLConnection c = (HttpURLConnection) new URL(url).openConnection();
    c.setRequestMethod("POST");
    c.setInstanceFollowRedirects(true);
    c.setConnectTimeout(15000);
    c.setReadTimeout(20000);
    c.setDoOutput(true);
    c.setRequestProperty("Accept", "application/json");
    c.setRequestProperty("Content-Type", "application/json; charset=utf-8");
    c.setRequestProperty("User-Agent", "YukiHub/1.0 (Android)");
    if (bearerToken != null && !bearerToken.trim().isEmpty()) c.setRequestProperty("Authorization", "Bearer " + bearerToken.trim());
    byte[] data = body == null ? new byte[0] : body.toString().getBytes(StandardCharsets.UTF_8);
    c.setFixedLengthStreamingMode(data.length);
    try (OutputStream os = new BufferedOutputStream(c.getOutputStream())) { os.write(data); }
    int code = c.getResponseCode();
    String text = readSmallText(code >= 200 && code < 300 ? c.getInputStream() : c.getErrorStream());
    if (code < 200 || code >= 300) throw new RuntimeException("HTTP " + code + ": " + text);
    return text == null || text.trim().isEmpty() ? new JSONObject() : new JSONObject(text);
}

/**
 * 用 Refresh Token 获取新的 Access Token
 * @return true 刷新成功，false 刷新失败需要重新登录
 */
private boolean refreshAccessToken() {
    if (prefs == null) return false;
    String refreshToken = prefs.getString(KEY_AUTH_REFRESH_TOKEN, "");
    if (refreshToken == null || refreshToken.isEmpty()) return false;
    
    try {
        String base = normalizeBaseUrl(AUTH_BASE_URL);
        JSONObject req = new JSONObject();
        req.put("refreshToken", refreshToken);
        JSONObject resp = postJson(base + "/auth/refresh", req, null);
        
        String newAccess = firstJsonString(resp, "accessToken", "access_token", "token");
        String newRefresh = firstJsonString(resp, "refreshToken", "refresh_token");
        JSONObject user = resp.optJSONObject("user");
        if (user == null) user = resp.optJSONObject("data") == null ? null : resp.optJSONObject("data").optJSONObject("user");
        
        if (newAccess == null || newAccess.isEmpty()) return false;
        
        // 更新 Token
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_AUTH_ACCESS_TOKEN, newAccess);
        if (newRefresh != null && !newRefresh.isEmpty()) {
            editor.putString(KEY_AUTH_REFRESH_TOKEN, newRefresh);
        }
        // 更新用户信息（如果有）
        if (user != null) {
            String nickname = firstJsonString(user, "nickname", "name", "username");
            String avatar = firstJsonString(user, "avatarUrl", "avatar_url", "avatar");
            String uid = user.optString("uid", "");
            if (nickname != null && !nickname.isEmpty()) editor.putString(KEY_AUTH_NICKNAME, nickname);
            if (avatar != null && !avatar.isEmpty()) editor.putString(KEY_AUTH_AVATAR, avatar);
            if (!uid.isEmpty()) editor.putString(KEY_AUTH_UID, uid);
            editor.putBoolean(KEY_KUN_BOUND, user.optBoolean("kungalBound", false));
            editor.putBoolean(KEY_HIKARINAGI_BOUND, user.optBoolean("hikarinagiBound", false));
        }
        editor.putString(KEY_AUTH_STATUS, AUTH_STATUS_ONLINE);
        editor.apply();
        
        Log.d("YukiHub", "Token refreshed successfully");
        return true;
    } catch (Throwable t) {
        Log.w("YukiHub", "Token refresh failed", t);
        return false;
    }
}

/**
 * 带自动刷新的 API 请求
 * 如果请求返回 401，自动尝试刷新 Token 后重试
 */
private JSONObject postJsonWithAuth(String url, JSONObject body) throws Exception {
    String token = prefs == null ? "" : prefs.getString(KEY_AUTH_ACCESS_TOKEN, "");
    
    try {
        return postJson(url, body, token);
    } catch (RuntimeException e) {
        // 检查是否是 403 禁用
        if (e.getMessage() != null && e.getMessage().contains("HTTP 403") && e.getMessage().contains("Disable")) {
            if (prefs != null) {
                prefs.edit()
                        .remove(KEY_AUTH_ACCESS_TOKEN)
                        .remove(KEY_AUTH_REFRESH_TOKEN)
                        .putString(KEY_AUTH_STATUS, AUTH_STATUS_EXPIRED)
                        .apply();
            }
            runOnUiThread(() -> {
                updateProfilePanel();
                Toast.makeText(this, "Your account has been disabled by the administrator", Toast.LENGTH_LONG).show();
            });
            throw new RuntimeException("Account has been disabled");
        }
        // 检查是否是 401 错误
        if (e.getMessage() != null && e.getMessage().contains("HTTP 401")) {
            Log.d("YukiHub", "Got 401, attempting token refresh...");
            if (refreshAccessToken()) {
                // 刷新成功，用新 Token 重试
                String newToken = prefs.getString(KEY_AUTH_ACCESS_TOKEN, "");
                return postJson(url, body, newToken);
            } else {
                // 刷新失败，标记为登录过期
                if (prefs != null) {
                    prefs.edit().putString(KEY_AUTH_STATUS, AUTH_STATUS_EXPIRED).apply();
                }
                throw new RuntimeException("Login has expired, please log in again");
            }
        }
        throw e;
    }
}

private void saveAuthSession(JSONObject resp, String emailFallback, String nicknameFallback) throws Exception {
    if (resp == null) throw new RuntimeException("empty response");
    String access = firstJsonString(resp, "accessToken", "access_token", "token");
    String refresh = firstJsonString(resp, "refreshToken", "refresh_token");
    JSONObject user = resp.optJSONObject("user");
    if (user == null) user = resp.optJSONObject("data") == null ? null : resp.optJSONObject("data").optJSONObject("user");
    String userId = user == null ? firstJsonString(resp, "userId", "user_id", "id") : firstJsonString(user, "id", "userId", "user_id");
    String nickname = user == null ? firstJsonString(resp, "nickname", "name", "username") : firstJsonString(user, "nickname", "name", "username");
    String email = user == null ? firstJsonString(resp, "email") : firstJsonString(user, "email");
    String avatar = user == null ? firstJsonString(resp, "avatarUrl", "avatar_url", "avatar") : firstJsonString(user, "avatarUrl", "avatar_url", "avatar");
    if (access == null || access.trim().isEmpty()) throw new RuntimeException("Login failed, please try again later");
    if (nickname == null || nickname.trim().isEmpty()) nickname = nicknameFallback;
    if (email == null || email.trim().isEmpty()) email = emailFallback;
    prefs.edit()
            .putString(KEY_AUTH_ACCESS_TOKEN, access)
            .putString(KEY_AUTH_REFRESH_TOKEN, refresh == null ? "" : refresh)
            .putString(KEY_AUTH_USER_ID, userId == null ? "" : userId)
            .putString(KEY_AUTH_UID, (user != null ? user.optString("uid", "") : ""))
            .putString(KEY_AUTH_NICKNAME, nickname == null ? "" : nickname)
            .putString(KEY_AUTH_EMAIL, email == null ? "" : email)
            .putString(KEY_AUTH_AVATAR, avatar == null ? "" : avatar)
            .putString(KEY_AUTH_STATUS, AUTH_STATUS_ONLINE)
            .putBoolean(KEY_KUN_BOUND, user != null && user.optBoolean("kungalBound", false))
            .putBoolean(KEY_HIKARINAGI_BOUND, user != null && user.optBoolean("hikarinagiBound", false))
            .putBoolean(KEY_CLOUD_SYNC_ENABLED, false)
            .apply();
}

private String firstJsonString(JSONObject o, String... keys) {
    if (o == null || keys == null) return "";
    for (String k : keys) {
        String v = o.optString(k, "");
        if (v != null && !v.trim().isEmpty() && !"null".equalsIgnoreCase(v.trim())) return v.trim();
    }
    return "";
}

private TextView profileStatCard(String label, String value) {
    TextView v = new TextView(this);
    v.setText(label + "\n" + value);
    v.setGravity(android.view.Gravity.CENTER);
    v.setTextColor(getColorCompat(R.color.yh_text));
    v.setTextSize(11);
    v.setTypeface(null, android.graphics.Typeface.BOLD);
    v.setLineSpacing(dp(1), 1.0f);
    v.setBackgroundResource(R.drawable.bg_input);
    return v;
}

private TextView profileLabel(String text) {
    TextView v = new TextView(this);
    v.setText(text);
    v.setTextColor(getColorCompat(R.color.yh_text));
    v.setTextSize(13);
    v.setTypeface(null, android.graphics.Typeface.BOLD);
    v.setPadding(0, 0, 0, dp(4));
    return v;
}

private EditText profileEdit(String value, String hint) {
    EditText v = new EditText(this);
    v.setText(value == null ? "" : value);
    v.setHint(hint);
    v.setTextColor(getColorCompat(R.color.yh_text));
    v.setHintTextColor(getColorCompat(R.color.yh_text_muted));
    v.setTextSize(13);
    v.setSingleLine(true);
    v.setBackgroundResource(R.drawable.bg_input);
    v.setPadding(dp(10), 0, dp(10), 0);
    return v;
}

public void openLocalBackupExportFromSyncCenter() {
    backupCreateLauncher.launch("yukihub_backup_" + System.currentTimeMillis() + ".ykbak");
}

public void openLocalBackupImportFromSyncCenter() {
    backupOpenLauncher.launch(new String[]{"application/octet-stream", "application/json", "text/*", "*/*"});
}

private void exportLocalBackup(Uri uri) {
    try {
        JSONObject root = new com.yuki.yukihub.sync.SyncManager(this).exportSnapshotForLocalBackup();
        root.put("created_at", System.currentTimeMillis());
        root.put("backup_type", "local_full");
        root.put("note", "Local backup keeps the latest 30 play sessions. Uses gzip compression.");
        // toString() 而非 toString(2)：备份是机读文件，缩进只会白白撑大待压缩的字节数
        byte[] jsonBytes = root.toString().getBytes(StandardCharsets.UTF_8);
        // gzip 压缩后写入文件
        ByteArrayOutputStream gzipBos = new ByteArrayOutputStream(jsonBytes.length / 4);
        try (GZIPOutputStream gzip = new GZIPOutputStream(gzipBos)) {
            gzip.write(jsonBytes);
            gzip.finish();
        }
        byte[] compressed = gzipBos.toByteArray();
        try (OutputStream out = getContentResolver().openOutputStream(uri)) {
            if (out == null) throw new Exception("openOutputStream failed");
            out.write(compressed);
            out.flush();
        }
        Toast.makeText(this, "Backup completed:" + (compressed.length / 1024) + "KB (compressed, original" + (jsonBytes.length / 1024) + "KB）", Toast.LENGTH_LONG).show();
} catch (Throwable t) {
            Toast.makeText(this, "Backup failed:" + t.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("YukiHub", "export backup failed", t);
        }
}

private void importLocalBackup(Uri uri) {
     try {
         // 读取原始字节，自动检测 gzip 或纯 JSON
         byte[] rawBytes;
         try (InputStream in = getContentResolver().openInputStream(uri); ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
             if (in == null) throw new Exception("openInputStream failed");
             byte[] buf = new byte[8192];
             int len;
             while ((len = in.read(buf)) != -1) bos.write(buf, 0, len);
             rawBytes = bos.toByteArray();
         }
         // gzip 文件头: 0x1f 0x8b
         String text;
         if (rawBytes.length >= 2 && (rawBytes[0] & 0xff) == 0x1f && (rawBytes[1] & 0xff) == 0x8b) {
             // gzip 压缩格式，解压
             try (GZIPInputStream gzip = new GZIPInputStream(new ByteArrayInputStream(rawBytes)); ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                 byte[] buf = new byte[8192];
                 int len;
                 while ((len = gzip.read(buf)) != -1) bos.write(buf, 0, len);
                 text = bos.toString("UTF-8");
             }
         } else {
             // 纯 JSON 文本，兼容老备份
             text = new String(rawBytes, StandardCharsets.UTF_8);
         }
         JSONObject root = new JSONObject(text);
         if (!"YukiHub".equals(root.optString("app", ""))) {
             Toast.makeText(this, "Not a valid YukiHub backup", Toast.LENGTH_LONG).show();
             return;
         }
         // 显示导入预览
         showYukiHubBackupPreview(root);
     } catch (Throwable t) {
         Toast.makeText(this, "Failed to read backup:" + t.getMessage(), Toast.LENGTH_LONG).show();
         Log.e("YukiHub", "read backup failed", t);
     }
 }

 /**
  * YukiHub 备份导入预览：展示备份中的游戏/记录/元数据统计，用户确认后导入。
  */
 private void showYukiHubBackupPreview(JSONObject root) {
     int gameCount = root.optJSONArray("games") == null ? 0 : root.optJSONArray("games").length();
     int sessionCount = root.optJSONArray("play_sessions") == null ? 0 : root.optJSONArray("play_sessions").length();
     int metaCount = root.optJSONArray("metadata_cache") == null ? 0 : root.optJSONArray("metadata_cache").length();
     String backupType = root.optString("backup_type", "local_full");
     String note = root.optString("note", "");
     // 备份 JSON 写的字段名是 "schema"（整数），不是 "schema_version"
     int schemaInt = root.optInt("schema", -1);
     String schemaVersion = schemaInt > 0 ? String.valueOf(schemaInt) : root.optString("schema_version", "Unknown");
     long createdAt = root.optLong("created_at", 0);

     // 统计新游戏 vs 已存在
     int newGames = 0, existingGames = 0;
     JSONArray gamesArr = root.optJSONArray("games");
     java.util.Set<String> existingNames = new java.util.HashSet<>();
     if (repository != null && repository.getAll() != null) {
         for (Game g : repository.getAll()) {
             if (g.title != null) existingNames.add(g.title.trim().toLowerCase());
         }
     }
     if (gamesArr != null) {
         for (int i = 0; i < gamesArr.length(); i++) {
             JSONObject o = gamesArr.optJSONObject(i);
             if (o == null) continue;
             String title = o.optString("title", "").trim().toLowerCase();
             if (title.isEmpty()) continue;
             if (existingNames.contains(title)) existingGames++;
             else newGames++;
         }
     }

     // 时间格式化
     String dateStr = "";
     if (createdAt > 0) {
         java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault());
         dateStr = sdf.format(new java.util.Date(createdAt));
     }

     int pad = dp(16);
     int itemSpacing = dp(10);

     LinearLayout listRoot = new LinearLayout(this);
     listRoot.setOrientation(LinearLayout.VERTICAL);
     listRoot.setPadding(pad, dp(14), pad, dp(14));
     // ── 备份信息卡片 ──
     LinearLayout infoCard = new LinearLayout(this);
     infoCard.setOrientation(LinearLayout.VERTICAL);
     infoCard.setPadding(pad, dp(12), pad, dp(12));
     infoCard.setBackgroundResource(R.drawable.bg_card);


     TextView infoHeader = new TextView(this);
     infoHeader.setText("📁 Backup information");
     infoHeader.setTextColor(getColorCompat(R.color.yh_text));
     infoHeader.setTextSize(14);
     infoHeader.setTypeface(null, android.graphics.Typeface.BOLD);
     infoHeader.setPadding(0, 0, 0, dp(8));
     infoCard.addView(infoHeader);

     // 用表格风格的两列展示信息
     infoCard.addView(makeInfoRow("Version", schemaVersion));
     infoCard.addView(makeInfoRow("Type", backupType));
     if (!dateStr.isEmpty()) infoCard.addView(makeInfoRow("Backup time", dateStr));

     listRoot.addView(infoCard);

     // 间距
     addVerticalSpace(listRoot, itemSpacing);

     // ── 数据统计卡片 ──
     LinearLayout statCard = new LinearLayout(this);
     statCard.setOrientation(LinearLayout.VERTICAL);
     statCard.setPadding(pad, dp(12), pad, dp(12));
     statCard.setBackgroundResource(R.drawable.bg_card);

     TextView statHeader = new TextView(this);
     statHeader.setText("📊 Data Overview");
     statHeader.setTextColor(getColorCompat(R.color.yh_text));
     statHeader.setTextSize(14);
     statHeader.setTypeface(null, android.graphics.Typeface.BOLD);
     statHeader.setPadding(0, 0, 0, dp(8));
     statCard.addView(statHeader);

     // 三列统计
     LinearLayout statRow = new LinearLayout(this);
     statRow.setOrientation(LinearLayout.HORIZONTAL);
     statRow.setGravity(android.view.Gravity.CENTER_VERTICAL);

     statRow.addView(makeStatBlock(gameCount + "", "Games",
             newGames > 0 ? getColorCompat(R.color.yh_primary) : getColorCompat(R.color.yh_text)));
     statRow.addView(makeStatBlock(sessionCount + "", "Play records", getColorCompat(R.color.yh_text)));
     statRow.addView(makeStatBlock(metaCount + "", "Metadata", getColorCompat(R.color.yh_text)));

     statCard.addView(statRow);

     // 新增/已存在标签
     TextView existInfo = new TextView(this);
     existInfo.setText("┃ New" + newGames + "· already exists" + existingGames + "(will be merged and updated)");
     existInfo.setTextColor(newGames > 0 ? getColorCompat(R.color.yh_primary) : getColorCompat(R.color.yh_text_muted));
     existInfo.setTextSize(11);
     existInfo.setTypeface(null, android.graphics.Typeface.BOLD);
     existInfo.setPadding(0, dp(10), 0, 0);
     statCard.addView(existInfo);

     listRoot.addView(statCard);

     addVerticalSpace(listRoot, itemSpacing);

     // ── 导入策略卡片 ──
     LinearLayout strategyCard = new LinearLayout(this);
     strategyCard.setOrientation(LinearLayout.VERTICAL);
     strategyCard.setPadding(pad, dp(12), pad, dp(12));
     strategyCard.setBackgroundResource(R.drawable.bg_card);

     TextView strategyHeader = new TextView(this);
     strategyHeader.setText("ℹ️ Import strategy");
     strategyHeader.setTextColor(getColorCompat(R.color.yh_text));
     strategyHeader.setTextSize(14);
     strategyHeader.setTypeface(null, android.graphics.Typeface.BOLD);
     strategyHeader.setPadding(0, 0, 0, dp(8));
     strategyCard.addView(strategyHeader);

     String[] strategies = {
         "游戏按 rootUri/标题去重合并（字段更新，非跳过）",
         "Game records are deduplicated by session_uuid",
         "Metadata cache matches by source + source_id",
         "图片只恢复 URI/URL，不复制图片文件"
     };
     for (String s : strategies) {
         TextView item = new TextView(this);
         item.setText("• " + s);
         item.setTextColor(getColorCompat(R.color.yh_text_muted));
         item.setTextSize(10);
         item.setLineSpacing(dp(2), 1.0f);
         item.setPadding(0, dp(2), 0, dp(2));
         strategyCard.addView(item);
     }

     listRoot.addView(strategyCard);

     // 备注
     if (!note.isEmpty()) {
         addVerticalSpace(listRoot, itemSpacing);
         TextView noteText = new TextView(this);
         noteText.setText("💬 " + note);
         noteText.setTextColor(getColorCompat(R.color.yh_text_muted));
         noteText.setTextSize(10);
         noteText.setPadding(pad, dp(8), pad, dp(8));
         noteText.setBackgroundColor(0x00000000);
         listRoot.addView(noteText);
     }

     ScrollView scroll = new ScrollView(this);
     scroll.setFillViewport(false);
     scroll.setBackgroundResource(R.drawable.bg_dialog);
     tintDialogRoot(scroll);
     scroll.addView(listRoot, new ScrollView.LayoutParams(ScrollView.LayoutParams.MATCH_PARENT, ScrollView.LayoutParams.WRAP_CONTENT));

     AlertDialog dialog = new AlertDialog.Builder(this)
             .setTitle("YukiHub backup import preview")
             .setView(scroll)
             .setPositiveButton("Confirm import", null)
             .setNegativeButton("Cancel", null)
             .show();
     styleAlertDialogDark(dialog);
     if (dialog.getWindow() != null) {
         dialog.getWindow().setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.54f), (int) (getResources().getDisplayMetrics().heightPixels * 0.72f));
     }
     dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
         dialog.dismiss();
         executeYukiHubBackupImport(root);
     });
 }

 /** 创建信息行：标签 + 值 */
 private LinearLayout makeInfoRow(String label, String value) {
     LinearLayout row = new LinearLayout(this);
     row.setOrientation(LinearLayout.HORIZONTAL);
     row.setGravity(android.view.Gravity.CENTER_VERTICAL);
     row.setPadding(0, dp(3), 0, dp(3));

     TextView labelView = new TextView(this);
     labelView.setText(label);
     labelView.setTextColor(getColorCompat(R.color.yh_text_muted));
     labelView.setTextSize(11);
     labelView.setTypeface(null, android.graphics.Typeface.BOLD);
     labelView.setMinWidth(dp(72));
     row.addView(labelView);

     TextView sepView = new TextView(this);
     sepView.setText("  :  ");
     sepView.setTextColor(getColorCompat(R.color.yh_text_muted));
     sepView.setTextSize(11);
     row.addView(sepView);

     TextView valueView = new TextView(this);
     valueView.setText(value);
     valueView.setTextColor(getColorCompat(R.color.yh_text));
     valueView.setTextSize(12);
     row.addView(valueView, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

     return row;
 }

 /** 创建统计块：大数字 + 小标签 */
 private LinearLayout makeStatBlock(String number, String label, int color) {
     LinearLayout block = new LinearLayout(this);
     block.setOrientation(LinearLayout.VERTICAL);
     block.setGravity(android.view.Gravity.CENTER);

     TextView numView = new TextView(this);
     numView.setText(number);
     numView.setTextColor(color);
     numView.setTextSize(22);
     numView.setTypeface(null, android.graphics.Typeface.BOLD);
     numView.setGravity(android.view.Gravity.CENTER);
     block.addView(numView);

     TextView labelView = new TextView(this);
     labelView.setText(label);
     labelView.setTextColor(getColorCompat(R.color.yh_text_muted));
     labelView.setTextSize(10);
     labelView.setGravity(android.view.Gravity.CENTER);
     labelView.setPadding(0, dp(2), 0, 0);
     block.addView(labelView);

     LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
     block.setLayoutParams(lp);
     return block;
 }

 /** 添加垂直间距 */
 private void addVerticalSpace(LinearLayout parent, int heightDp) {
     View space = new View(this);
     space.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, heightDp));
     parent.addView(space);
 }

 private void executeYukiHubBackupImport(JSONObject root) {
     showImportLoading("Importing YukiHub backup...");
     new Thread(() -> {
         try {
             new com.yuki.yukihub.sync.SyncManager(this).importSnapshotFromLocalBackup(root);
             runOnUiThread(() -> {
                 hideImportLoading();
                 coverMaintenanceDone = false;
                 loadGames();
                 applyCustomBackground();
                 updateProfilePanel();
                 int gameCount = root.optJSONArray("games") == null ? 0 : root.optJSONArray("games").length();
                 int sessionCount = root.optJSONArray("play_sessions") == null ? 0 : root.optJSONArray("play_sessions").length();
                 int metaCount = root.optJSONArray("metadata_cache") == null ? 0 : root.optJSONArray("metadata_cache").length();
                 Toast.makeText(this, "Import completed: game" + gameCount + ",Record" + sessionCount + ", metadata" + metaCount, Toast.LENGTH_LONG).show();
             });
         } catch (Exception e) {
             runOnUiThread(() -> {
                 hideImportLoading();
                 Toast.makeText(this, "Import failed:" + e.getMessage(), Toast.LENGTH_LONG).show();
                 Log.e("YukiHub", "backup import failed", e);
             });
         }
     }).start();
 }

 private String readTextFromUri(Uri uri) throws Exception {
     try (InputStream in = getContentResolver().openInputStream(uri); ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
         if (in == null) throw new Exception("openInputStream failed");
         byte[] buf = new byte[8192];
         int len;
         while ((len = in.read(buf)) != -1) bos.write(buf, 0, len);
         return bos.toString("UTF-8");
     }
 }

 // ==================== 三方平台数据导入 ====================

/**
   * 弹出三方平台导入选择对话框。
   * 四个平台：Playnite / PotatoVN / Vnite / LunaBox
   */
  private void showExternalImportDialog() {
      int pad = dp(16);

      LinearLayout root = new LinearLayout(this);
      root.setOrientation(LinearLayout.VERTICAL);
      root.setPadding(pad, dp(14), pad, dp(14));

      // 说明文字
      TextView intro = new TextView(this);
      intro.setText("Select the platform to which you want to import data. All games that can be imported will be previewed before importing.");
      intro.setTextColor(getColorCompat(R.color.yh_text_muted));
      intro.setTextSize(11);
      intro.setLineSpacing(dp(2), 1.0f);
      intro.setPadding(0, 0, 0, dp(12));
      root.addView(intro);

      // 四个平台卡片
      String[][] platforms = {
          {"🎮  Playnite", "JSON file", "Export game library to JSON in Playnite",
           "application/json", "text/*", "*/*"},
          {"🥔  PotatoVN", "ZIP file", "Contains game list, cover and play history",
           "application/zip", "application/octet-stream", "*/*"},
          {"📁  Vnite", "Export directory", "Select the export folder for Vnite",
           null, null, null},
          {"🌙  LunaBox", "ZIP backup", "LunaBox full backup (game + cover + play history)",
           "application/zip", "application/octet-stream", "*/*"}
      };

      for (int i = 0; i < platforms.length; i++) {
          final int idx = i;
          LinearLayout card = new LinearLayout(this);
          card.setOrientation(LinearLayout.HORIZONTAL);
          card.setGravity(android.view.Gravity.CENTER_VERTICAL);
          card.setPadding(pad, dp(12), pad, dp(12));
          card.setBackgroundResource(R.drawable.bg_card);

          // 左侧：图标 + 名称 + 格式
          LinearLayout textCol = new LinearLayout(this);
          textCol.setOrientation(LinearLayout.VERTICAL);
          textCol.setPadding(0, 0, dp(8), 0);

          TextView title = new TextView(this);
          title.setText(platforms[i][0]);
          title.setTextColor(getColorCompat(R.color.yh_text));
          title.setTextSize(14);
          title.setTypeface(null, android.graphics.Typeface.BOLD);
          textCol.addView(title);

          TextView subtitle = new TextView(this);
          subtitle.setText(platforms[i][1] + "  ·  " + platforms[i][2]);
          subtitle.setTextColor(getColorCompat(R.color.yh_text_muted));
          subtitle.setTextSize(10);
          subtitle.setPadding(0, dp(2), 0, 0);
          textCol.addView(subtitle);

          card.addView(textCol, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

          // 右侧：箭头
          TextView arrow = new TextView(this);
          arrow.setText("→");
          arrow.setTextColor(getColorCompat(R.color.yh_primary));
          arrow.setTextSize(18);
          arrow.setTypeface(null, android.graphics.Typeface.BOLD);
          card.addView(arrow);

          // 点击事件
          card.setOnClickListener(v -> {
              switch (idx) {
                  case 0: // Playnite
                      playniteImportLauncher.launch(new String[]{"application/json", "text/*", "*/*"});
                      break;
                  case 1: // PotatoVN
                      potatovnImportLauncher.launch(new String[]{"application/zip", "application/octet-stream", "*/*"});
                      break;
                  case 2: // Vnite
                      vniteImportLauncher.launch(null);
                      break;
                  case 3: // LunaBox
                      lunaboxImportLauncher.launch(new String[]{"application/zip", "application/octet-stream", "*/*"});
                      break;
              }
          });

          LinearLayout.LayoutParams cardLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
          if (i > 0) cardLp.setMargins(0, dp(8), 0, 0);
          root.addView(card, cardLp);
      }

      // 底部注意事项
      addVerticalSpace(root, dp(12));
      TextView notice = new TextView(this);
      notice.setText("⚠️ Note: The game path is the PC path, and there is no game directory after import. Existing games with the same name will be automatically skipped. Play records will be imported automatically.");
      notice.setTextColor(getColorCompat(R.color.yh_text_muted));
      notice.setTextSize(9);
      notice.setLineSpacing(dp(2), 1.0f);
      root.addView(notice);

      ScrollView scroll = new ScrollView(this);
      scroll.setFillViewport(false);
      scroll.setBackgroundResource(R.drawable.bg_dialog);
      tintDialogRoot(scroll);
      scroll.addView(root, new ScrollView.LayoutParams(ScrollView.LayoutParams.MATCH_PARENT, ScrollView.LayoutParams.WRAP_CONTENT));

      AlertDialog dialog = new AlertDialog.Builder(this)
              .setTitle("Import from other platforms")
              .setView(scroll)
              .setNegativeButton("Close", null)
              .show();
      styleAlertDialogDark(dialog);
      if (dialog.getWindow() != null) {
          dialog.getWindow().setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.56f), (int) (getResources().getDisplayMetrics().heightPixels * 0.72f));
      }
  }

 /** 存储待确认的导入候选项（预览阶段） */
 private java.util.List<com.yuki.yukihub.importer.ImportGameData> pendingImportGames;
 /** 存储待确认导入的数据源标签 */
 private String pendingImportSourceLabel;

 /**
  * 执行 Playnite JSON 导入（解析 → 预览）
  */
 private void doImportFromPlaynite(Uri uri) {
     showImportLoading("Parsing Playnite data…");
     new Thread(() -> {
         try {
             java.util.List<com.yuki.yukihub.importer.ImportGameData> games =
                     com.yuki.yukihub.importer.PlayniteImporter.parse(this, uri);
             com.yuki.yukihub.importer.ImporterService service =
                     new com.yuki.yukihub.importer.ImporterService(this);
             service.markExisting(games);
             runOnUiThread(() -> {
                 hideImportLoading();
                 pendingImportGames = games;
                 pendingImportSourceLabel = "Playnite";
                 showImportPreviewDialog(games, "Playnite");
             });
         } catch (Exception e) {
             runOnUiThread(() -> {
                 hideImportLoading();
                 Toast.makeText(this, "Playnite parsing failed:" + e.getMessage(), Toast.LENGTH_LONG).show();
                 Log.e("YukiHub", "Playnite parse failed", e);
             });
         }
     }).start();
 }

 /**
  * 执行 PotatoVN ZIP 导入（解析 → 预览）
  */
 private void doImportFromPotatoVN(Uri uri) {
     showImportLoading("Parsing PotatoVN data…");
     new Thread(() -> {
         try {
             java.util.List<com.yuki.yukihub.importer.ImportGameData> games =
                     com.yuki.yukihub.importer.PotatoVnImporter.parse(this, uri);
             com.yuki.yukihub.importer.ImporterService service =
                     new com.yuki.yukihub.importer.ImporterService(this);
             service.markExisting(games);
             runOnUiThread(() -> {
                 hideImportLoading();
                 pendingImportGames = games;
                 pendingImportSourceLabel = "PotatoVN";
                 showImportPreviewDialog(games, "PotatoVN");
             });
         } catch (Exception e) {
             runOnUiThread(() -> {
                 hideImportLoading();
                 Toast.makeText(this, "PotatoVN parsing failed:" + e.getMessage(), Toast.LENGTH_LONG).show();
                 Log.e("YukiHub", "PotatoVN parse failed", e);
             });
         }
     }).start();
 }

/**
     * 执行 Vnite 目录导入（解析 → 预览）
     */
    private void doImportFromVnite(Uri uri) {
        showImportLoading("Parsing Vnite data…");
        new Thread(() -> {
            try {
                java.util.List<com.yuki.yukihub.importer.ImportGameData> games =
                        com.yuki.yukihub.importer.VniteImporter.parse(this, uri);
                com.yuki.yukihub.importer.ImporterService service =
                        new com.yuki.yukihub.importer.ImporterService(this);
                service.markExisting(games);
                runOnUiThread(() -> {
                    hideImportLoading();
                    pendingImportGames = games;
                    pendingImportSourceLabel = "Vnite";
                    showImportPreviewDialog(games, "Vnite");
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    hideImportLoading();
                    Toast.makeText(this, "Vnite parsing failed:" + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("YukiHub", "Vnite parse failed", e);
                });
            }
        }).start();
    }

    /**
     * 执行 LunaBox ZIP 备份导入（解析 → 预览）
     */
    private void doImportFromLunaBox(Uri uri) {
        showImportLoading("Parsing LunaBox backup...");
        new Thread(() -> {
            try {
                java.util.List<com.yuki.yukihub.importer.ImportGameData> games =
                        com.yuki.yukihub.importer.LunaBoxImporter.parse(this, uri);
                com.yuki.yukihub.importer.ImporterService service =
                        new com.yuki.yukihub.importer.ImporterService(this);
                service.markExisting(games);
                runOnUiThread(() -> {
                    hideImportLoading();
                    pendingImportGames = games;
                    pendingImportSourceLabel = "LunaBox";
                    showImportPreviewDialog(games, "LunaBox");
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    hideImportLoading();
                    Toast.makeText(this, "LunaBox parsing failed:" + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("YukiHub", "LunaBox parse failed", e);
                });
            }
        }).start();
    }

 // ==================== 导入预览对话框（通用） ====================

 /**
  * 通用导入预览对话框：展示候选项列表 + 勾选，用户确认后才实际导入。
  *
  * 列表内容：游戏名、开发商、数据源、状态标签（新增/已存在）
  * 底部按钮：全选/全不选、确认导入、取消
  */
 private void showImportPreviewDialog(java.util.List<com.yuki.yukihub.importer.ImportGameData> games, String sourceLabel) {
     if (games == null || games.isEmpty()) {
         Toast.makeText(this, "No game data found to import", Toast.LENGTH_SHORT).show();
         return;
     }

     int newCount = 0, existCount = 0, sessionCount = 0;
     for (com.yuki.yukihub.importer.ImportGameData g : games) {
         if (g.exists) existCount++;
         else newCount++;
if (g.playedTimeMap != null) sessionCount += g.playedTimeMap.size();
            else if (g.vniteTimers != null) sessionCount += g.vniteTimers.size();
            else if (g.lunaBoxSessions != null) sessionCount += g.lunaBoxSessions.size();
     }

     // 列表容器
     LinearLayout listRoot = new LinearLayout(this);
     listRoot.setOrientation(LinearLayout.VERTICAL);
     listRoot.setPadding(dp(8), dp(8), dp(8), dp(8));

     // 摘要
     TextView summary = new TextView(this);
     summary.setText("Source:" + sourceLabel + "·  common" + games.size() + "Games"
             + "· New" + newCount + "· Already exists" + existCount
             + (sessionCount > 0 ? "· Play records" + sessionCount : ""));
     summary.setTextColor(getColorCompat(R.color.yh_text_muted));
     summary.setTextSize(11);
     summary.setPadding(dp(4), dp(4), dp(4), dp(8));
     listRoot.addView(summary);

     // 全选/全不选按钮行
     LinearLayout actionRow = new LinearLayout(this);
     actionRow.setOrientation(LinearLayout.HORIZONTAL);
     actionRow.setGravity(android.view.Gravity.CENTER_VERTICAL);
     Button selectAllBtn = krButton("Select all new games");
     Button deselectAllBtn = krButton("Deselect");
     selectAllBtn.setTextColor(primaryTextColor());
     deselectAllBtn.setTextColor(getColorCompat(R.color.yh_text));
     actionRow.addView(selectAllBtn, new LinearLayout.LayoutParams(0, dp(36), 1));
     LinearLayout.LayoutParams deselectLp = new LinearLayout.LayoutParams(0, dp(36), 1);
     deselectLp.setMargins(dp(6), 0, 0, 0);
     actionRow.addView(deselectAllBtn, deselectLp);

     LinearLayout.LayoutParams actionRowLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
     actionRowLp.setMargins(0, 0, 0, dp(4));
     listRoot.addView(actionRow, actionRowLp);

     // 候选项列表（带勾选框）
     LinearLayout itemsContainer = new LinearLayout(this);
     itemsContainer.setOrientation(LinearLayout.VERTICAL);
     listRoot.addView(itemsContainer);

     // 渲染每个候选项
     java.util.List<CheckBox> checkBoxes = new java.util.ArrayList<>();
     for (int i = 0; i < games.size(); i++) {
         com.yuki.yukihub.importer.ImportGameData g = games.get(i);
         LinearLayout row = new LinearLayout(this);
         row.setOrientation(LinearLayout.HORIZONTAL);
         row.setGravity(android.view.Gravity.CENTER_VERTICAL);
         row.setPadding(dp(4), dp(6), dp(4), dp(6));

         CheckBox cb = new CheckBox(this);
         cb.setChecked(g.selected);
         cb.setEnabled(!g.exists);
         if (g.exists) {
             cb.setAlpha(0.4f);
         }
         checkBoxes.add(cb);
         row.addView(cb);

         // 文字部分：游戏名 + 附加信息
         LinearLayout textCol = new LinearLayout(this);
         textCol.setOrientation(LinearLayout.VERTICAL);
         textCol.setPadding(dp(8), 0, 0, 0);

         TextView nameText = new TextView(this);
         String nameDisplay = g.name;
         if (g.developer != null && !g.developer.isEmpty()) nameDisplay += "  · " + g.developer;
         if (g.sourceType != null && !g.sourceType.isEmpty() && !"local".equals(g.sourceType)) {
             nameDisplay += "  [" + g.sourceType.toUpperCase() + "]";
         }
         nameText.setText(nameDisplay);
         nameText.setTextColor(g.exists ? getColorCompat(R.color.yh_text_muted) : getColorCompat(R.color.yh_text));
         nameText.setTextSize(13);
         nameText.setTypeface(null, android.graphics.Typeface.BOLD);
         textCol.addView(nameText);

         // 第二行：状态信息
         TextView infoText = new TextView(this);
         StringBuilder info = new StringBuilder();
         if (g.exists) {
             info.append("Already exists and will be skipped");
         } else {
             info.append("New");
         }
         // 游玩状态标签（此处是拼接进 StringBuilder 的长句，用纯文字，不挂图标）
         if (g.playStatus != null && !g.playStatus.isEmpty() && !"unplayed".equals(g.playStatus)) {
             info.append("  · ").append(com.yuki.yukihub.util.IconedText.labelForStatus(g.playStatus));
         }
if (g.nsfw) info.append("  · NSFW");
if (g.playedTimeMap != null && !g.playedTimeMap.isEmpty()) info.append("· Play records").append(g.playedTimeMap.size()).append("Sky");
          if (g.vniteTimers != null && !g.vniteTimers.isEmpty()) info.append("· Play records").append(g.vniteTimers.size()).append(" 条");
          if (g.lunaBoxSessions != null && !g.lunaBoxSessions.isEmpty()) info.append("· Play records").append(g.lunaBoxSessions.size()).append(" 条");
         if (g.coverUrl != null && !g.coverUrl.isEmpty()) info.append("· With cover");
         if (g.coverLocalPath != null && !g.coverLocalPath.isEmpty()) info.append("· With cover");
         if (g.description != null && !g.description.isEmpty()) info.append("· With introduction");
         infoText.setText(info.toString());
         infoText.setTextColor(getColorCompat(R.color.yh_text_muted));
         infoText.setTextSize(10);
         textCol.addView(infoText);

         row.addView(textCol, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

         itemsContainer.addView(row, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

         // 勾选变化监听
         final int idx = i;
         cb.setOnCheckedChangeListener((button, isChecked) -> {
             games.get(idx).selected = isChecked;
         });
     }

     // 全选/取消全选逻辑
     selectAllBtn.setOnClickListener(v -> {
         for (int i = 0; i < games.size(); i++) {
             if (!games.get(i).exists) {
                 games.get(i).selected = true;
                 checkBoxes.get(i).setChecked(true);
             }
         }
     });
     deselectAllBtn.setOnClickListener(v -> {
         for (int i = 0; i < games.size(); i++) {
             if (!games.get(i).exists) {
                 games.get(i).selected = false;
                 checkBoxes.get(i).setChecked(false);
             }
         }
     });

     // 滚动容器
     ScrollView scroll = new ScrollView(this);
     scroll.setFillViewport(false);
     scroll.setBackgroundResource(R.drawable.bg_dialog);
     tintDialogRoot(scroll);
     scroll.addView(listRoot, new ScrollView.LayoutParams(ScrollView.LayoutParams.MATCH_PARENT, ScrollView.LayoutParams.WRAP_CONTENT));

     AlertDialog dialog = new AlertDialog.Builder(this)
             .setTitle("Import preview —" + sourceLabel)
             .setView(scroll)
             .setPositiveButton("Confirm import", null)
             .setNegativeButton("Cancel", null)
             .show();
     styleAlertDialogDark(dialog);
     if (dialog.getWindow() != null) {
         dialog.getWindow().setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.56f), (int) (getResources().getDisplayMetrics().heightPixels * 0.78f));
     }
     // 用PositiveButton的独立ClickListener防止对话框自动关闭
     dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
         dialog.dismiss();
         executeExternalImport(games, sourceLabel);
     });
 }

 /**
  * 执行确认后的导入（后台线程）
  */
private void executeExternalImport(java.util.List<com.yuki.yukihub.importer.ImportGameData> games, String sourceLabel) {
      int selectedCount = 0;
      if (games != null) {
          for (com.yuki.yukihub.importer.ImportGameData g : games) {
              if (g != null && g.selected && !g.exists) selectedCount++;
          }
      }
      showImportLoading("Importing" + sourceLabel + "Data…\nready to process" + selectedCount + "Games", true, Math.max(1, selectedCount));
      new Thread(() -> {
          try {
              com.yuki.yukihub.importer.ImporterService service =
                      new com.yuki.yukihub.importer.ImporterService(this);
              final com.yuki.yukihub.importer.ImportResult result = service.importSelected(games, (current, total, itemName) -> {
                  String name = itemName == null || itemName.trim().isEmpty() ? "Unnamed game" : itemName.trim();
                  runOnUiThread(() -> updateImportLoading("Importing" + sourceLabel + "Data…" + current + "/" + total + "  《" + name + "》", current, total));
              });
              runOnUiThread(() -> {
                  hideImportLoading();
                  afterExternalImport(result);
              });
          } catch (Exception e) {
              runOnUiThread(() -> {
                  hideImportLoading();
                  Toast.makeText(this, sourceLabel + "Import failed:" + e.getMessage(), Toast.LENGTH_LONG).show();
                  Log.e("YukiHub", sourceLabel + " import failed", e);
              });
          }
      }).start();
  }

 /**
  * 外部平台导入完成后的统一处理
  */
 private void afterExternalImport(com.yuki.yukihub.importer.ImportResult result) {
     coverMaintenanceDone = false;
     loadGames();
     updateProfilePanel();
     String msg = "Import completed:" + result.toString();
     if (!result.skippedNames.isEmpty()) {
         msg += "Jump over:" + String.join("、", result.skippedNames);
     }
     if (!result.failedNames.isEmpty()) {
         msg += "Fail:" + String.join("、", result.failedNames);
     }
     Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
 }

// 导入进度对话框
  private AlertDialog importLoadingDialog;
  private TextView importLoadingText;
  private ProgressBar importProgressBar;
  private void showImportLoading(String message) {
      showImportLoading(message, false, 0);
  }

  private void showImportLoading(String message, boolean determinate, int max) {
      if (importLoadingDialog != null) importLoadingDialog.dismiss();
      LinearLayout root = new LinearLayout(this);
      root.setOrientation(LinearLayout.VERTICAL);
      root.setPadding(dp(18), dp(12), dp(18), dp(8));
      root.setBackgroundResource(R.drawable.bg_dialog);

      importLoadingText = new TextView(this);
      importLoadingText.setText(message);
      importLoadingText.setTextColor(getColorCompat(R.color.yh_text));
      importLoadingText.setTextSize(12);
      importLoadingText.setLineSpacing(dp(2), 1.0f);
      root.addView(importLoadingText, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

      importProgressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
      importProgressBar.setIndeterminate(!determinate);
      importProgressBar.setMax(Math.max(1, max));
      importProgressBar.setProgress(0);
      LinearLayout.LayoutParams barLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp(8));
      barLp.setMargins(0, dp(12), 0, 0);
      root.addView(importProgressBar, barLp);

      importLoadingDialog = new AlertDialog.Builder(this)
              .setTitle("Data import")
              .setView(root)
              .setCancelable(false)
              .show();
      styleAlertDialogDark(importLoadingDialog);
  }

  private void updateImportLoading(String message, int progress, int max) {
      if (importLoadingText != null) importLoadingText.setText(message);
      if (importProgressBar != null) {
          importProgressBar.setIndeterminate(false);
          importProgressBar.setMax(Math.max(1, max));
          importProgressBar.setProgress(Math.max(0, Math.min(progress, Math.max(1, max))));
      }
  }

  private void hideImportLoading() {
      if (importLoadingDialog != null) {
          importLoadingDialog.dismiss();
          importLoadingDialog = null;
      }
      importLoadingText = null;
      importProgressBar = null;
  }

private void buildRecentActivityViews(LinearLayout container) {
    if (container == null) return;
    List<PlayActivity> activities = repository == null ? new ArrayList<>() : repository.getRecentPlayActivities(8);
    if (activities.isEmpty()) {
        TextView empty = new TextView(this);
        empty.setText("No updates yet. After you start playing, your footprints will be recorded here.");
        empty.setTextColor(getColorCompat(R.color.yh_text_muted));
        empty.setTextSize(12);
        empty.setBackgroundResource(R.drawable.bg_input);
        empty.setPadding(dp(10), dp(8), dp(10), dp(8));
        container.addView(empty, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        return;
    }
    for (PlayActivity a : activities) {
        TextView item = new TextView(this);
        item.setText("Played \"" + a.gameTitle + "》 " + TimeFormatUtil.playTime(a.duration) + "\n" + TimeFormatUtil.date(a.endTime) + " · " + launchTypeLabel(a.launchType));
        item.setTextColor(getColorCompat(R.color.yh_text));
        item.setTextSize(12);
        item.setLineSpacing(dp(1), 1.0f);
        item.setBackgroundResource(R.drawable.bg_input);
        item.setPadding(dp(10), dp(8), dp(10), dp(8));
        item.setOnClickListener(v -> { playUiSound(UI_SOUND_CLICK); showPlayActivityDetail(a); });
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, dp(6));
        container.addView(item, lp);
    }
}

private void showPlayActivityDetail(PlayActivity a) {
    if (a == null) return;
    String text = "Game:" + a.gameTitle + "\n"
            + "Start:" + TimeFormatUtil.date(a.startTime) + "\n"
            + "Finish:" + TimeFormatUtil.date(a.endTime) + "\n"
            + "Duration:" + TimeFormatUtil.playTime(a.duration) + "\n"
            + "Startup type:" + launchTypeLabel(a.launchType) + "\n"
            + "Session ID:" + emptyText(a.sessionUuid, String.valueOf(a.sessionId));
    AlertDialog dialog = new AlertDialog.Builder(this)
            .setTitle("Dynamic details")
            .setMessage(text)
            .setPositiveButton("Close", null)
            .show();
    styleAlertDialogDark(dialog);
}

private String launchTypeLabel(String launchType) {
    String t = launchType == null ? "" : launchType;
    if (t.startsWith("internal.krkr")) return "Built-in KRKR";
    if (t.startsWith("internal.ons")) return "Built-in ONS";
    if (t.startsWith("internal.tyrano")) return "Built-in Tyrano";
    if (t.startsWith("internal.artemis")) return "Built-in Artemis";
    if (t.startsWith("internal.fvp")) return "Built-in FVP";
    return "External emulator";
}

    private void updateProfilePanel() {
        String name = displayProfileName();
        long total = totalPlayTime();
        if (tvProfileName != null) tvProfileName.setText(name);
        if (tvProfileInitial != null) tvProfileInitial.setText(initials(name));
        updateProfileStatusDot();
        if (tvStatsGames != null) tvStatsGames.setText(String.valueOf(allGames.size()));
        if (tvStatsTime != null) tvStatsTime.setText(TimeFormatUtil.playTime(total));
        if (tvGreeting != null) {
            int hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY);
            String period = hour < 5 ? "It's late at night" : hour < 11 ? "Good morning" : hour < 14 ? "Good afternoon" : hour < 18 ? "Good afternoon" : "Good evening";
            tvGreeting.setText(period + "，" + name);
        }
        loadProfileAvatarInto(ivProfileAvatar, tvProfileInitial);
    }

private void updateProfileStatusDot() {
    if (profileStatusDot == null) return;
    String status = accountStatus();
    if ("local".equals(status)) {
        profileStatusDot.setBackgroundResource(R.drawable.bg_profile_dot_local);
    } else if (AUTH_STATUS_ONLINE.equals(status)) {
        profileStatusDot.setBackgroundResource(R.drawable.bg_profile_dot_online);
    } else if (AUTH_STATUS_SYNCING.equals(status)) {
        profileStatusDot.setBackgroundResource(R.drawable.bg_profile_dot_syncing);
    } else if (AUTH_STATUS_EXPIRED.equals(status)) {
        profileStatusDot.setBackgroundResource(R.drawable.bg_profile_dot_expired);
    } else {
        profileStatusDot.setBackgroundResource(R.drawable.bg_profile_dot_local);
    }
}

private boolean isLoggedIn() {
    if (prefs == null) return false;
    String token = prefs.getString(KEY_AUTH_ACCESS_TOKEN, "");
    return token != null && !token.trim().isEmpty();
}

private String displayProfileName() {
    if (prefs != null && isLoggedIn()) {
        String cloudName = prefs.getString(KEY_AUTH_NICKNAME, "");
        if (cloudName != null && !cloudName.trim().isEmpty()) return cloudName.trim();
    }
    return profileName();
}

private String accountStatus() {
    if (!isLoggedIn()) return "local";
    String status = prefs == null ? AUTH_STATUS_OFFLINE : prefs.getString(KEY_AUTH_STATUS, AUTH_STATUS_OFFLINE);
    if (AUTH_STATUS_ONLINE.equals(status) || AUTH_STATUS_SYNCING.equals(status) || AUTH_STATUS_EXPIRED.equals(status)) return status;
    return AUTH_STATUS_OFFLINE;
}

private void loadProfileAvatarInto(ImageView avatar, TextView initial) {
    if (avatar == null) return;
    // 始终使用 profile_avatar 显示（本地 file:// 路径），
    // auth_avatar 仅用于标记是否已上传到服务器，不用于显示
    String uri = prefs == null ? "" : prefs.getString(KEY_PROFILE_AVATAR, "");
    if (uri == null || uri.isEmpty()) {
        avatar.setVisibility(View.GONE);
        if (initial != null) initial.setVisibility(View.VISIBLE);
        return;
    }
    try {
        avatar.setImageURI(Uri.parse(uri));
        avatar.setVisibility(View.VISIBLE);
        if (initial != null) initial.setVisibility(View.GONE);
    } catch (Throwable t) {
        avatar.setVisibility(View.GONE);
        if (initial != null) initial.setVisibility(View.VISIBLE);
    }
}

private long todayTotalPlayTime() {
    if (repository == null) return 0L;
    Calendar start = Calendar.getInstance();
    start.set(Calendar.HOUR_OF_DAY, 0);
    start.set(Calendar.MINUTE, 0);
    start.set(Calendar.SECOND, 0);
    start.set(Calendar.MILLISECOND, 0);
    Calendar end = (Calendar) start.clone();
    end.add(Calendar.DAY_OF_MONTH, 1);
    long total = 0L;
    Map<String, Long> today = repository.getPlayDurationsBetween(start.getTimeInMillis(), end.getTimeInMillis());
    for (Long v : today.values()) total += v == null ? 0L : v;
    return total;
}

private String buildTodayActivityText() {
    if (repository == null) return "There are no play records today.";
    Calendar start = Calendar.getInstance();
    start.set(Calendar.HOUR_OF_DAY, 0);
    start.set(Calendar.MINUTE, 0);
    start.set(Calendar.SECOND, 0);
    start.set(Calendar.MILLISECOND, 0);
    Calendar end = (Calendar) start.clone();
    end.add(Calendar.DAY_OF_MONTH, 1);
    Map<String, Long> today = repository.getPlayDurationsBetween(start.getTimeInMillis(), end.getTimeInMillis());
    if (today.isEmpty()) return "There are no play records today.\nAfter launching the game, returning to YukiHub will generate the feed.";
    StringBuilder sb = new StringBuilder();
    int count = 0;
    long total = 0;
    for (Map.Entry<String, Long> e : today.entrySet()) {
        if (count >= 5) break;
        long duration = e.getValue() == null ? 0L : e.getValue();
        total += duration;
        if (count > 0) sb.append('\n');
        sb.append("Today I played \"").append(e.getKey()).append("》 ").append(TimeFormatUtil.playTime(duration));
        count++;
    }
    if (today.size() > count) sb.append('\n').append("Besides").append(today.size() - count).append("Game records...");
    sb.append("Today’s total:").append(TimeFormatUtil.playTime(total));
    return sb.toString();
}

private void setupDeveloperToggle() {
    TextView title = findViewById(R.id.filterDeveloper);
    View scroll = findViewById(R.id.developerScroll);
    if (title == null || scroll == null) return;
    title.setOnClickListener(v -> {
        playUiSound(UI_SOUND_SWITCH);
        boolean show = scroll.getVisibility() != View.VISIBLE;
        // 两个二级面板互斥：展开一个就收起另一个，避免占两行高度
        if (show) collapseStatusPanel();
        scroll.setVisibility(show ? View.VISIBLE : View.GONE);
        title.setText(show ? "▾ Developer" : "▸ Developer");
    });
}

// ==================== 游玩状态二级分类 ====================

/** 游玩状态二级分类：{筛选值, 显示文案}，顺序即展示顺序。 */
private static final String[][] STATUS_FILTERS = {
        {"", "All"},
        {"PLAYING", "Playing"},
        {"COMPLETED", "Played"},
        {"UNPLAYED", "Not played"},
        {"ONHOLD", "Put on hold"},
        {"DROPPED", "Abandon"},
};

/** 当前选中的游玩状态筛选值，空串表示不按状态过滤。 */
private String statusFilter = "";

private void setupStatusToggle() {
    TextView title = findViewById(R.id.filterStatus);
    View scroll = findViewById(R.id.statusScroll);
    if (title == null || scroll == null) return;
    title.setOnClickListener(v -> {
        playUiSound(UI_SOUND_SWITCH);
        boolean show = scroll.getVisibility() != View.VISIBLE;
        if (show) collapseDeveloperPanel();
        scroll.setVisibility(show ? View.VISIBLE : View.GONE);
        updateStatusTitle();
    });
    rebuildStatusFilters();
}

private void collapseStatusPanel() {
    View scroll = findViewById(R.id.statusScroll);
    if (scroll != null) scroll.setVisibility(View.GONE);
    updateStatusTitle();
}

private void collapseDeveloperPanel() {
    View scroll = findViewById(R.id.developerScroll);
    TextView title = findViewById(R.id.filterDeveloper);
    if (scroll != null) scroll.setVisibility(View.GONE);
    if (title != null) title.setText("▸ Developer");
}

/**
 * 一级标题文案：收起时若已选了具体状态就直接显示该状态，
 * 这样面板收起后依然能看出当前在筛什么。
 */
private void updateStatusTitle() {
    TextView title = findViewById(R.id.filterStatus);
    View scroll = findViewById(R.id.statusScroll);
    if (title == null) return;
    boolean expanded = scroll != null && scroll.getVisibility() == View.VISIBLE;
    String label = "Game status";
    if (!expanded && statusFilter != null && !statusFilter.isEmpty()) {
        for (String[] item : STATUS_FILTERS) {
            if (item[0].equals(statusFilter)) { label = item[1]; break; }
        }
    }
    title.setText((expanded ? "▾ " : "▸ ") + label);
}

private void rebuildStatusFilters() {
    LinearLayout list = findViewById(R.id.statusList);
    if (list == null) return;
    list.removeAllViews();
    for (String[] item : STATUS_FILTERS) {
        list.addView(statusFilterItem(item[1], item[0]));
    }
    updateStatusFilterSelection();
}

private TextView statusFilterItem(String text, String value) {
    TextView v = new TextView(this);
    // 图标 + 文字；「全部」（value 空）无图标，退化为纯文字
    v.setText(com.yuki.yukihub.util.IconedText.build(this,
            com.yuki.yukihub.util.IconedText.drawableForFilterValue(value),
            text, 8f, getColorCompat(R.color.yh_text)));
    v.setTag(value == null ? "" : value);
    v.setGravity(android.view.Gravity.CENTER);
    v.setTextSize(8);
    v.setSingleLine(true);
    v.setBackgroundResource(R.drawable.bg_input);
    v.setTextColor(getColorCompat(R.color.yh_text));
    v.setPadding(dp(10), 0, dp(10), 0);
    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, dp(26));
    lp.setMarginStart(dp(4));
    v.setLayoutParams(lp);
    v.setMinWidth(dp(48));
    v.setClickable(true);
    v.setFocusable(true);
    v.setOnClickListener(view -> {
        playUiSound(UI_SOUND_CLICK);
        statusFilter = value == null ? "" : value;
        // 选了具体状态就退出「收藏 / 本地游戏」这类互斥的一级分类，
        // 否则两个条件叠加会让用户以为筛选没生效。
        if (!statusFilter.isEmpty() && !"ALL".equals(filter)
                && !isEngineFilter(filter)) {
            filter = "ALL";
        }
        updateStatusFilterSelection();
        updateFilterSelection();
        applyFilter();
    });
    return v;
}

/**
 * 状态筛选值（大写）→ 数据库里的状态值（小写）。
 */
private String statusFilterValue(String filterValue) {
    if (filterValue == null) return "";
    switch (filterValue) {
        case "PLAYING": return "playing";
        case "COMPLETED": return "completed";
        case "ONHOLD": return "onhold";
        case "DROPPED": return "dropped";
        case "UNPLAYED": return "unplayed";
        default: return "";
    }
}

/** 引擎类一级分类可以和状态并存（例如「KRKR + 在玩」）。 */
private boolean isEngineFilter(String value) {
    if (value == null) return false;
    switch (value) {
        case "KIRIKIRI": case "ONS": case "TYRANO": case "ARTEMIS":
        case "FVP":
        case "WINLATOR": case "PC": case "GAMEHUB": case "PSP": case "ANDROID":
        case "UNKNOWN":
            return true;
        default:
            return false;
    }
}

private void updateStatusFilterSelection() {
    LinearLayout list = findViewById(R.id.statusList);
    updateStatusTitle();
    if (list == null) return;
    for (int i = 0; i < list.getChildCount(); i++) {
        View child = list.getChildAt(i);
        if (!(child instanceof TextView)) continue;
        String value = child.getTag() instanceof String ? (String) child.getTag() : "";
        boolean selected = (statusFilter == null ? "" : statusFilter).equals(value);
        TextView tv = (TextView) child;
        child.setAlpha(selected ? 1f : 0.82f);
        // 文字色变化时图标 tint 必须一起重建：ImageSpan 持有的是已着色的 Drawable，
        // 只改 setTextColor 不会传导到图标，选中态下图标必须与文字一起着色。
        int fg = selected ? ActionButtonStyle.foreground(this, ActionButtonStyle.FILTER) : getColorCompat(R.color.yh_text);
        String label = value.isEmpty() ? "All"
                : com.yuki.yukihub.util.IconedText.labelForStatus(
                        value.toLowerCase(java.util.Locale.ROOT));
        tv.setText(com.yuki.yukihub.util.IconedText.build(this,
                com.yuki.yukihub.util.IconedText.drawableForFilterValue(value),
                label, 8f, fg));
        tv.setTextColor(fg);
        if (selected) {
            ActionButtonStyle.apply(child, ActionButtonStyle.FILTER);
        } else {
            child.setBackgroundResource(R.drawable.bg_input);
        }
        tv.setTypeface(null, selected ? android.graphics.Typeface.BOLD : android.graphics.Typeface.NORMAL);
    }
}

private void rebuildDeveloperFilters() {
    LinearLayout list = findViewById(R.id.developerList);
    View scroll = findViewById(R.id.developerScroll);
    TextView title = findViewById(R.id.filterDeveloper);
    if (list == null || title == null) return;
    list.removeAllViews();
    java.util.Map<String, Integer> counts = new java.util.TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    for (Game g : allGames) {
        String dev = developerOf(g);
        if (dev == null || dev.trim().isEmpty() || "-".equals(dev.trim())) continue;
        String[] parts = dev.split("/|、|,|，");
        for (String p : parts) {
            String name = p == null ? "" : p.trim();
            if (name.isEmpty()) continue;
            counts.put(name, counts.containsKey(name) ? counts.get(name) + 1 : 1);
        }
    }
    if (counts.isEmpty()) {
        TextView empty = sidebarDeveloperItem("No developer yet", "");
        empty.setAlpha(0.45f);
        empty.setEnabled(false);
        list.addView(empty);
        title.setAlpha(0.55f);
        if (scroll != null) scroll.setVisibility(View.GONE);
        title.setText("▸ Developer");
        return;
    }
    title.setAlpha(1f);
    list.addView(sidebarDeveloperItem("All", ""));
    // 按游戏数降序，数量相同按名称，常玩的开发商排在前面便于横向查找
    List<java.util.Map.Entry<String, Integer>> entries = new ArrayList<>(counts.entrySet());
    java.util.Collections.sort(entries, (a, b) -> {
        int r = b.getValue().compareTo(a.getValue());
        if (r != 0) return r;
        return a.getKey().compareToIgnoreCase(b.getKey());
    });
    for (java.util.Map.Entry<String, Integer> e : entries) {
        list.addView(sidebarDeveloperItem(e.getKey() + " · " + e.getValue(), e.getKey()));
    }
    updateDeveloperFilterSelection();
}
private TextView sidebarDeveloperItem(String text, String developer) {
    TextView v = new TextView(this);
    v.setText(text);
    v.setTag(developer == null ? "" : developer);
    v.setGravity(android.view.Gravity.CENTER);
    v.setTextSize(8);
    v.setSingleLine(true);
    v.setMaxWidth(dp(150));
    v.setEllipsize(android.text.TextUtils.TruncateAt.END);
    v.setBackgroundResource(R.drawable.bg_input);
    v.setTextColor(getColorCompat(R.color.yh_text));
    v.setPadding(dp(10), 0, dp(10), 0);
    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, dp(26));
    lp.setMarginStart(dp(4));
    v.setLayoutParams(lp);
    v.setMinWidth(dp(48));
    v.setClickable(true);
    v.setFocusable(true);
    v.setOnClickListener(view -> {
        playUiSound(UI_SOUND_CLICK);
        developerFilter = developer == null ? "" : developer;
        updateDeveloperFilterSelection();
        applyFilter();
    });
    return v;
}
private void updateDeveloperFilterSelection() {
    LinearLayout list = findViewById(R.id.developerList);
    if (list == null) return;
    for (int i = 0; i < list.getChildCount(); i++) {
        View child = list.getChildAt(i);
        if (!(child instanceof TextView)) continue;
        String dev = child.getTag() instanceof String ? (String) child.getTag() : "";
        boolean selected = (developerFilter == null ? "" : developerFilter).equals(dev);
        TextView tv = (TextView) child;
        child.setAlpha(selected ? 1f : 0.82f);
        if (selected) {
            ActionButtonStyle.apply(child, ActionButtonStyle.FILTER);
            tv.setTextColor(ActionButtonStyle.foreground(this, ActionButtonStyle.FILTER));
        } else {
            child.setBackgroundResource(R.drawable.bg_input);
            tv.setTextColor(getColorCompat(R.color.yh_text));
        }
        tv.setTypeface(null, selected ? android.graphics.Typeface.BOLD : android.graphics.Typeface.NORMAL);
    }
}

private String developerOf(Game game) {
    if (game == null) return "";
    VnMetadata meta = anyCachedMetadata(game.id);
return meta == null ? "" : emptyText(meta.developer, "");
}

private void bindFilter(int id, String value) {
        View item = findViewById(id);
        item.setOnClickListener(v -> {
            playUiSound(UI_SOUND_CLICK);
            filter = value;
            developerFilter = "";
            // 点「全部 / 收藏 / 本地游戏」视为重置筛选，状态二级分类一并清空
            statusFilter = "";
            if ("LOCAL".equals(value)) localInstalledCache.clear(); // 进入本地游戏分类：重新检测最新文件状态
            updateFilterSelection();
            applyFilter();
        });
    }

    private void updateFilterSelection() {
        updateFilterItem(R.id.filterAll, "ALL");
        updateFilterItem(R.id.filterFavorite, "FAVORITE");
        updateFilterItem(R.id.filterRecent, "LOCAL");
        updateFilterItem(R.id.filterHidden, "HIDDEN");
        updateDeveloperFilterSelection();
        updateStatusFilterSelection();
        // 多选模式下切分类（隐藏 ↔ 其它）时，「隐藏/恢复」按钮语义要跟着变。
        if (adapter != null && adapter.isMultiSelectMode()) updateMultiSelectBar(adapter.getCheckedCount());
    }

    private void updateFilterItem(int id, String value) {
        View view = findViewById(id);
        if (view == null) return;
        boolean selected = value.equals(filter);
        view.setAlpha(selected ? 1f : 0.82f);
        if (view instanceof TextView) {
            TextView tv = (TextView) view;
            int fg = selected ? ActionButtonStyle.foreground(this, ActionButtonStyle.FILTER) : getColorCompat(R.color.yh_text);
            // 收藏项带心形图标；文字色随选中态变化，图标 tint 必须同步重建
            if (id == R.id.filterFavorite) {
                com.yuki.yukihub.util.IconedText.set(tv, R.drawable.ic_st_heart, "Collect", 8f, fg);
            }
            // 隐藏项带眼睛图标；数量大于 0 时后缀计数，让用户在别的分类也能看到「有东西被藏起来了」
            if (id == R.id.filterHidden) {
                String label = hiddenGames.isEmpty() ? "Hide" : "Hide" + hiddenGames.size();
                com.yuki.yukihub.util.IconedText.set(tv, R.drawable.ic_st_hidden, label, 8f, fg);
            }
            tv.setTextColor(fg);
            if (selected) ActionButtonStyle.apply(view, ActionButtonStyle.FILTER);
            else view.setBackgroundResource(R.drawable.bg_input);
            tv.setTypeface(null, selected ? android.graphics.Typeface.BOLD : android.graphics.Typeface.NORMAL);
        }
    }
private void loadGames() {
allGames.clear();
allGames.addAll(repository.getAll());
// 隐藏游戏单独查：数量少、只在「隐藏」分类用到，跟随主列表一起刷新可避免分类里读到过期数据。
hiddenGames.clear();
try {
    hiddenGames.addAll(repository.getHiddenGames());
} catch (Throwable t) {
    Log.w("YukiHub", "load hidden games failed", t);
}
lastKnownHiddenCount = hiddenGames.size();
rebuildDeveloperFilters();
applyFilter();
if (adapter != null) adapter.setNsfwBlurEnabled(nsfwBlurEnabled());
runCoverMaintenanceOnceIfNeeded();
    }

private void runCoverMaintenanceOnceIfNeeded() {
if (coverMaintenanceDone) return;
coverMaintenanceDone = true;
repairMissingMetadataCoversIfNeeded();
scanMissingCoversIfNeeded();
}

    /** 判断游戏是否已安装：rootUri 对应的目录/文件真实存在（兼容 file:// 与 content://）。带缓存，避免分类内重复查询。 */
    private boolean isGameInstalled(Game g) {
        if (g == null) return false;
        if (g.engine == EngineType.ANDROID) {
            String pkg = g.emulatorPackage == null ? "" : g.emulatorPackage.trim();
            if (pkg.isEmpty()) return false;
            String key = "android:" + pkg;
            Boolean cached = localInstalledCache.get(key);
            if (cached != null) return cached;
            boolean installed = false;
            try { installed = getPackageManager().getLaunchIntentForPackage(pkg) != null; } catch (Throwable ignored) { }
            localInstalledCache.put(key, installed);
            return installed;
        }
        String rootUri = g.rootUri == null ? "" : g.rootUri.trim();
        if (rootUri.isEmpty()) return false;
        Boolean cached = localInstalledCache.get(rootUri);
        if (cached != null) return cached;
        boolean installed = checkRootExists(rootUri);
        localInstalledCache.put(rootUri, installed);
        return installed;
    }

    private boolean checkRootExists(String rootUri) {
        try {
            DocumentFile dir = documentDir(rootUri);
            return dir != null && dir.exists();
        } catch (Throwable ignored) {
            return false;
        }
    }

    /**
     * onResume 时对齐隐藏状态。
     * <p>大屏模式是独立 Activity，在里面隐藏/恢复了游戏后回到主界面，本 Activity 的
     * {@code allGames}/{@code hiddenGames} 还是旧的。这里用隐藏条数（一次 COUNT，很轻）
     * 判断「是否有人动过」，只在真的变了时才整表重载，避免每次回前台都白刷一遍列表。
     * <p>用条数而非时间戳是刻意的：它足够覆盖「大屏改了隐藏」这个场景，又不会因为
     * 游玩时长之类与隐藏无关的字段变化而误触发重载（那会打断用户当前的选择和滚动位置）。
     */
    private void syncHiddenGamesIfChanged() {
        if (repository == null || adapter == null) return;
        int count;
        try {
            count = repository.countHiddenGames();
        } catch (Throwable t) {
            Log.w("YukiHub", "countHiddenGames failed", t);
            return;
        }
        if (count == lastKnownHiddenCount) return;
        Game previous = selectedGame;
        loadGames();
        // 与库内恢复行为保持一致：隐藏分类被清空（在大屏里恢复完了）就退回「全部」，
        // 否则用户回到主界面会对着一个空列表，以为自己刚才看的东西不见了。
        if ("HIDDEN".equals(filter) && hiddenGames.isEmpty()) {
            filter = "ALL";
            updateFilterSelection();
            applyFilter();
        }
        // 尽量把侧栏选中的游戏还原回去——但只在它确实属于「当前分类正在展示的那份列表」时。
        // 否则会出现「列表是隐藏分类，侧栏却指着一款已恢复的普通游戏」这种不一致。
        if (previous == null) return;
        List<Game> visibleSource = "HIDDEN".equals(filter) ? hiddenGames : allGames;
        for (Game g : visibleSource) {
            if (g.id == previous.id) { updateSideDetail(g); return; }
        }
    }

    private void applyFilter() {
        List<Game> shown = new ArrayList<>();
        String q = query == null ? "" : query.toLowerCase(Locale.ROOT).trim();
        long total = 0;
        // 统计口径固定为「游戏库总量」，与当前筛选分类无关，避免切到隐藏分类时数字跳动。
        for (Game g : allGames) {
            total += g.totalPlayTime;
        }
        // 「隐藏」分类的数据源是独立查询出来的隐藏列表，其余分类都用可见列表。
        boolean hiddenView = "HIDDEN".equals(filter);
        List<Game> source = hiddenView ? hiddenGames : allGames;
        for (Game g : source) {
            if (!q.isEmpty() && (g.title == null || !g.title.toLowerCase(Locale.ROOT).contains(q))) continue;
            // 隐藏分类是独立的互斥一级分类，不再叠加收藏/本地/引擎等其它一级条件。
            if (hiddenView) { shown.add(g); continue; }
            if ("LOCAL".equals(filter) && !isGameInstalled(g)) continue;
            if ("FAVORITE".equals(filter) && !g.favorite) continue;
            // 游玩状态是独立维度（statusFilter），可与引擎分类、开发商叠加
            if (statusFilter != null && !statusFilter.isEmpty()
                    && !statusFilterValue(statusFilter).equals(normalizePlayStatus(g.playStatus))) continue;
            if ("KIRIKIRI".equals(filter) && g.engine != EngineType.KIRIKIRI) continue;
        if ("ONS".equals(filter) && g.engine != EngineType.ONS) continue;
        if ("TYRANO".equals(filter) && g.engine != EngineType.TYRANO) continue;
        if ("ARTEMIS".equals(filter) && g.engine != EngineType.ARTEMIS) continue;
        if ("FVP".equals(filter) && g.engine != EngineType.FVP) continue;
        if ("WINLATOR".equals(filter) && g.engine != EngineType.WINLATOR) continue;
        if ("PC".equals(filter) && g.engine != EngineType.PC) continue;
        if ("GAMEHUB".equals(filter) && g.engine != EngineType.GAMEHUB) continue;
        if ("PSP".equals(filter) && g.engine != EngineType.PSP) continue;
        if ("ANDROID".equals(filter) && g.engine != EngineType.ANDROID) continue;
        if ("UNKNOWN".equals(filter) && g.engine != EngineType.UNKNOWN) continue;
            if (developerFilter != null && !developerFilter.isEmpty()) {
                String dev = developerOf(g);
                if (dev == null || !dev.toLowerCase(Locale.ROOT).contains(developerFilter.toLowerCase(Locale.ROOT))) continue;
            }
            shown.add(g);
        }
        sortGames(shown);
        adapter.submit(shown);
        tvEmpty.setVisibility(shown.isEmpty() ? View.VISIBLE : View.GONE);
        if (shown.isEmpty() && hiddenView) {
            // 隐藏分类为空时给出可执行的说明，避免用户以为功能坏了。
            tvEmpty.setText("No hidden games\nYou can hide the game in the \"Options\" menu");
        } else if (shown.isEmpty()) {
            tvEmpty.setText("No games yet\nClick \"Add\" to enter manually, or click \"Scan\" to import the directory");
        }
        tvStatsGames.setText(String.valueOf(allGames.size()));
        tvStatsTime.setText(TimeFormatUtil.playTime(total));
        // 隐藏数量可能因隐藏/恢复而变化，筛选栏按钮文案在这里统一刷新（所有刷新路径都会经过 applyFilter）。
        updateFilterItem(R.id.filterHidden, "HIDDEN");
        updateProfilePanel();
        if (shown.isEmpty()) {
            updateSideDetail(null);
        } else if (selectedGame == null || !containsGameId(shown, selectedGame.id)) {
            updateSideDetail(shown.get(0));
        }
    }

    private void sortGames(List<Game> list) {
        if (list == null || list.size() <= 1) return;
        String mode = prefs == null ? SORT_MODE_RECENT : prefs.getString(KEY_SORT_MODE, SORT_MODE_RECENT);
        java.util.Comparator<Game> cmp;
        if (SORT_MODE_NAME.equals(mode)) {
            final Collator collator = Collator.getInstance(Locale.CHINA);
            collator.setStrength(Collator.PRIMARY);
            cmp = (a, b) -> {
                if (a == null && b == null) return 0;
                if (a == null) return 1;
                if (b == null) return -1;
                boolean af = a.favorite;
                boolean bf = b.favorite;
                if (af != bf) return af ? -1 : 1;
                String at = a.title == null ? "" : a.title;
                String bt = b.title == null ? "" : b.title;
                int r = collator.compare(at, bt);
                if (r != 0) return r;
                return Long.compare(b.createdAt, a.createdAt);
            };
        } else if (SORT_MODE_NEWEST.equals(mode)) {
            cmp = (a, b) -> {
                if (a == null && b == null) return 0;
                if (a == null) return 1;
                if (b == null) return -1;
                boolean af = a.favorite;
                boolean bf = b.favorite;
                if (af != bf) return af ? -1 : 1;
                int r = Long.compare(b.createdAt, a.createdAt);
                if (r != 0) return r;
                return Long.compare(b.lastPlayedAt, a.lastPlayedAt);
            };
        } else {
            cmp = (a, b) -> {
                if (a == null && b == null) return 0;
                if (a == null) return 1;
                if (b == null) return -1;
                boolean af = a.favorite;
                boolean bf = b.favorite;
                if (af != bf) return af ? -1 : 1;
                int r = Long.compare(b.lastPlayedAt, a.lastPlayedAt);
                if (r != 0) return r;
                r = Long.compare(b.createdAt, a.createdAt);
                if (r != 0) return r;
                String at = a.title == null ? "" : a.title;
                String bt = b.title == null ? "" : b.title;
                return at.compareToIgnoreCase(bt);
            };
        }
        list.sort(cmp);
    }

    private boolean containsGameId(List<Game> games, long id) {
    if (games == null) return false;
    for (Game g : games) if (g != null && g.id == id) return true;
    return false;
}

private void loadRemoteImage(String url, ImageView target) {
    loadRemoteImage(url, target, "img");
}

private void invalidateRemoteImageRequest(ImageView target) {
    if (target == null) return;
    target.setTag(R.id.tag_remote_image_request, "cancel:" + System.nanoTime());
}

private void loadRemoteImage(String url, ImageView target, String prefix) {
    if (target == null) return;
    final boolean isDetailCover = target == sideDetailCover && prefix != null && prefix.startsWith("cover_");
    // M21-2：走远程加载后，图片内容不再等于 sideDetailCoverUri 所指的那张，标记失效
    if (isDetailCover) { sideDetailCoverUri = null; }
    final String requestTag = "remote:" + (prefix == null ? "img" : prefix) + ":" + (url == null ? "" : url.trim());
    target.setTag(R.id.tag_remote_image_request, requestTag);
    if (isDetailCover && sideDetailCoverLoading != null) {
        sideDetailCoverLoading.setVisibility(View.VISIBLE);
        if (sideDetailPlaceholder != null) {
            sideDetailPlaceholder.setText("Cover loading...");
            sideDetailPlaceholder.setVisibility(View.VISIBLE);
        }
    }
    if (url == null || url.trim().isEmpty()) {
        target.setImageDrawable(null);
        if (isDetailCover && sideDetailCoverLoading != null) sideDetailCoverLoading.setVisibility(View.GONE);
        return;
    }
    final String imageUrl = url.trim();
    AppExecutors.runOnIo(() -> {
        try {
            File cacheDir = prefix != null && prefix.startsWith("cover_") ? persistentRemoteCoverDir() : new File(getCacheDir(), "vndb_images");
            if (!cacheDir.exists()) cacheDir.mkdirs();
            File cacheFile = new File(cacheDir, safeCacheName(prefix + "_" + imageUrl));
            Bitmap bitmap = null;
            if (cacheFile.exists() && cacheFile.length() > 0) {
                bitmap = BitmapFactory.decodeFile(cacheFile.getAbsolutePath());
                if (bitmap == null) cacheFile.delete();
            }
            if (bitmap == null) {
                boolean ok = downloadImageAllowVndbWarningPage(imageUrl, cacheFile, 0);
                if (!ok) {
                    showRemoteImageFailed(target, requestTag, isDetailCover);
                    return;
                }
                bitmap = BitmapFactory.decodeFile(cacheFile.getAbsolutePath());
                if (bitmap == null) {
                    cacheFile.delete();
                    showRemoteImageFailed(target, requestTag, isDetailCover);
                    return;
                }
            }
            Bitmap finalBitmap = bitmap;
            runOnUiThread(() -> {
                if (finalBitmap == null || target.getWindowToken() == null) return;
                Object currentRequest = target.getTag(R.id.tag_remote_image_request);
                if (!(currentRequest instanceof String) || !requestTag.equals(currentRequest)) return;
                target.setImageBitmap(finalBitmap);
                if (isDetailCover) {
                    if (sideDetailCoverLoading != null) sideDetailCoverLoading.setVisibility(View.GONE);
                    if (sideDetailPlaceholder != null) sideDetailPlaceholder.setVisibility(View.GONE);
                    target.setVisibility(View.VISIBLE);
                }
                Object tag = target.getTag();
                if (tag instanceof Game && prefix != null && prefix.startsWith("cover_") && cacheFile.exists()) {
                    Game taggedGame = (Game) tag;
                    String local = Uri.fromFile(cacheFile).toString();
                    if (taggedGame.coverUri == null || taggedGame.coverUri.isEmpty() || isMissingFileUri(taggedGame.coverUri)) {
                        taggedGame.coverUri = local;
                        taggedGame.coverPersistUri = local;
                        taggedGame.coverSourceType = 1;
                        try { repository.update(taggedGame); } catch (Throwable ignored) { }
                        if (adapter != null) adapter.notifyDataSetChanged();
                    }
                }
            });
        } catch (Throwable ignored) {
            showRemoteImageFailed(target, requestTag, isDetailCover);
        }
    });
}

private void showRemoteImageFailed(ImageView target, String requestTag, boolean isDetailCover) {
    if (target == null) return;
    runOnUiThread(() -> {
        Object currentRequest = target.getTag(R.id.tag_remote_image_request);
        if (!(currentRequest instanceof String) || !requestTag.equals(currentRequest)) return;
        target.setImageDrawable(null);
        if (isDetailCover) {
            if (sideDetailCoverLoading != null) sideDetailCoverLoading.setVisibility(View.GONE);
            target.setVisibility(View.GONE);
            if (sideDetailPlaceholder != null) {
                sideDetailPlaceholder.setText("Cover loading failed\nYou can try again later or set it manually");
                sideDetailPlaceholder.setVisibility(View.VISIBLE);
            }
        }
    });
}

private String metadataSource() {
    return metadataController.metadataSource();
}

private String metadataSourceLabel() {
    return metadataController.metadataSourceLabel();
}

private String metadataSourceLabel(String source) {
    return metadataController.metadataSourceLabel(source);
}

private String normalizeMetadataSource(String source) {
    return metadataController.normalizeMetadataSource(source);
}

private boolean isValidMetadataSource(String source) {
    return metadataController.isValidMetadataSource(source);
}

private String visibleMetadataSource(long gameId) {
    return metadataController.visibleMetadataSource(gameId);
}

private void setVisibleMetadataSource(long gameId, String source) {
    metadataController.setVisibleMetadataSource(gameId, source);
}

private VnMetadata metadataForSource(long gameId, String source) {
    return metadataController.metadataForSource(gameId, source);
}

private String metadataSourceForVisibleMetadata(long gameId, VnMetadata meta) {
    return metadataController.metadataSourceForVisibleMetadata(gameId, meta);
}

private String metadataSourceLabelForVisibleMetadata(long gameId, VnMetadata meta) {
    return metadataController.metadataSourceLabelForVisibleMetadata(gameId, meta);
}

private void updateSideMetadataSourceBadge(String label) {
    metadataController.updateSideMetadataSourceBadge(label);
}

private boolean usingBangumi() {
    return metadataController.usingBangumi();
}

private boolean usingYmgal() {
    return metadataController.usingYmgal();
}

private boolean usingHikarinagi() {
    return metadataController.usingHikarinagi();
}

private String bangumiToken() {
    return metadataController.bangumiToken();
}

private void fetchSelectedMetadata(Game game) {
    metadataController.fetchSelectedMetadata(game);
}

private void fetchSelectedMetadata(Game game, boolean forceRefresh) {
    metadataController.fetchSelectedMetadata(game, forceRefresh);
}

private void fetchCurrentSourceMetadata(Game game, boolean forceRefresh) {
    metadataController.fetchCurrentSourceMetadata(game, forceRefresh);
}

private VnMetadata currentSourceCachedMetadata(long gameId) {
    return metadataController.currentSourceCachedMetadata(gameId);
}

private VnMetadata anyCachedMetadata(long gameId) {
    return metadataController.anyCachedMetadata(gameId);
}

private VnMetadata otherSourceCachedMetadata(long gameId) {
    return metadataController.otherSourceCachedMetadata(gameId);
}

private void saveCurrentSourceMetadata(long gameId, VnMetadata meta) {
    metadataController.saveCurrentSourceMetadata(gameId, meta);
}

private void saveVisibleMetadata(long gameId, VnMetadata meta) {
    metadataController.saveVisibleMetadata(gameId, meta);
}

private void saveMetadataForSource(long gameId, String source, VnMetadata meta) {
    metadataController.saveMetadataForSource(gameId, source, meta);
}

private boolean sameMetadataIdentity(VnMetadata a, VnMetadata b) {
    return metadataController.sameMetadataIdentity(a, b);
}

private void clearCurrentSourceMetadata(long gameId) {
    metadataController.clearCurrentSourceMetadata(gameId);
}

private VnMetadata currentSourceMetadata(long gameId) {
    return metadataController.currentSourceMetadata(gameId);
}

private void showCurrentSourceCustomSearchDialog(Game game) {
    metadataController.showCurrentSourceCustomSearchDialog(game);
}

private void searchCurrentSourceWithKeyword(Game game, String keyword) {
    metadataController.searchCurrentSourceWithKeyword(game, keyword);
}

private void fetchVndbMetadata(Game game, boolean forceRefresh) {
    metadataController.fetchVndbMetadata(game, forceRefresh);
}

private void fetchBangumiMetadata(Game game, boolean forceRefresh) {
    metadataController.fetchBangumiMetadata(game, forceRefresh);
}

private void fetchYmgalMetadata(Game game, boolean forceRefresh) {
    metadataController.fetchYmgalMetadata(game, forceRefresh);
}

private void fetchHikarinagiMetadata(Game game, boolean forceRefresh) {
    metadataController.fetchHikarinagiMetadata(game, forceRefresh);
}

private void fetchAndApplyYmgalDetail(Game game, VnMetadata candidate) {
    metadataController.fetchAndApplyYmgalDetail(game, candidate);
}

private void fetchAndApplyHikarinagiDetail(Game game, VnMetadata candidate) {
    metadataController.fetchAndApplyHikarinagiDetail(game, candidate);
}

private boolean downloadImageAllowVndbWarningPage(String imageUrl, File cacheFile, int depth) {
    return metadataController.downloadImageAllowVndbWarningPage(imageUrl, cacheFile, depth);
}

private String readSmallText(InputStream is) throws Exception {
    return metadataController.readSmallText(is);
}

private boolean isTranslatedStateFor(long gameId) {
    return metadataController.isTranslatedStateFor(gameId);
}

private void setTranslatedStateFor(long gameId, boolean translated) {
    metadataController.setTranslatedStateFor(gameId, translated);
}

private void updateTranslateButtonState() {
    if (sideTranslateToggle == null) return;
    boolean hasMeta = currentSideMetadata != null;
    boolean hasDescription = hasMeta && currentSideMetadata.description != null && !currentSideMetadata.description.trim().isEmpty();
    sideTranslateToggle.setVisibility(hasDescription ? View.VISIBLE : View.GONE);
    if (!hasDescription) return;
    sideTranslateToggle.setText(sideShowingTranslatedDescription ? "Original" : "translation");
    sideTranslateToggle.setEnabled(true);
    sideTranslateToggle.setAlpha(1f);
}

private void toggleOrTranslateDescription() {
    if (selectedGame == null || currentSideMetadata == null) return;
    VnMetadata meta = currentSideMetadata;
    if (sideShowingTranslatedDescription) {
        sideShowingTranslatedDescription = false;
        setTranslatedStateFor(selectedGame.id, false);
        setSideDescription(emptyText(meta.description, "None yet" + metadataSourceLabel() + "Introduction."));
        updateTranslateButtonState();
        return;
    }
    if (meta.translatedDescription != null && !meta.translatedDescription.trim().isEmpty()) {
        sideShowingTranslatedDescription = true;
        setTranslatedStateFor(selectedGame.id, true);
        setSideDescription(meta.translatedDescription);
        updateTranslateButtonState();
        return;
    }
    final long gameId = selectedGame.id;
    sideTranslateToggle.setText("...");
    sideTranslateToggle.setEnabled(false);
    sideTranslateToggle.setAlpha(0.65f);
    AppExecutors.runOnIo(() -> {
        try {
            String translated = translateTextToChinese(meta.description);
            runOnUiThread(() -> {
                if (selectedGame == null || selectedGame.id != gameId || currentSideMetadata != meta) return;
                if (translated == null || translated.trim().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Introduction translation failed", Toast.LENGTH_SHORT).show();
                    updateTranslateButtonState();
                    return;
                }
                meta.translatedDescription = translated.trim();
                if (metadataRepository != null) saveVisibleMetadata(gameId, meta);
                sideShowingTranslatedDescription = true;
                setTranslatedStateFor(gameId, true);
                setSideDescription(meta.translatedDescription);
                updateTranslateButtonState();
            });
        } catch (Throwable t) {
            Log.w("YukiHub", "translate description failed", t);
            runOnUiThread(() -> {
                if (selectedGame != null && selectedGame.id == gameId) {
                    Toast.makeText(MainActivity.this, "Introduction translation failed", Toast.LENGTH_SHORT).show();
                    updateTranslateButtonState();
                }
            });
        }
    });
}

private String translateTextToChinese(String text) throws Exception {
    if (text == null || text.trim().isEmpty()) return "";
    List<String> parts = splitTextForTranslation(text.trim(), 480);
    StringBuilder out = new StringBuilder();
    Throwable last = null;
    for (String part : parts) {
        if (part == null || part.trim().isEmpty()) continue;
        String translated = null;
        try {
            translated = translateTextByMyMemory(part);
        } catch (Throwable t) {
            last = t;
            try { translated = translateTextByGoogleapis(part); }
            catch (Throwable t2) { last = t2; }
        }
        if (translated == null || translated.trim().isEmpty()) {
            if (last instanceof Exception) throw (Exception) last;
            throw new RuntimeException("Translate empty result");
        }
        if (out.length() > 0) out.append("\n\n");
        out.append(translated.trim());
        try { Thread.sleep(220); } catch (InterruptedException ignored) { }
    }
    return out.toString().trim();
}

private List<String> splitTextForTranslation(String text, int maxLen) {
    List<String> list = new ArrayList<>();
    if (text == null) return list;
    String s = text.trim();
    while (s.length() > maxLen) {
        int cut = Math.max(s.lastIndexOf("\n", maxLen), Math.max(s.lastIndexOf(". ", maxLen), s.lastIndexOf("。", maxLen)));
        if (cut < maxLen / 2) cut = maxLen;
        list.add(s.substring(0, Math.min(cut + 1, s.length())).trim());
        s = s.substring(Math.min(cut + 1, s.length())).trim();
    }
    if (!s.isEmpty()) list.add(s);
    return list;
}

private String translateTextByMyMemory(String q) throws Exception {
    String url = "https://api.mymemory.translated.net/get?q=" + URLEncoder.encode(q, "UTF-8") + "&langpair=en%7Czh-CN";
    HttpURLConnection c = (HttpURLConnection) new URL(url).openConnection();
    c.setInstanceFollowRedirects(true);
    c.setConnectTimeout(12000);
    c.setReadTimeout(18000);
    c.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 13) AppleWebKit/537.36 Chrome/120 Mobile Safari/537.36 YukiHub/1.0");
    c.setRequestProperty("Accept", "application/json,text/plain,*/*");
    c.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
    int code = c.getResponseCode();
    String body = readSmallText(code >= 200 && code < 300 ? c.getInputStream() : c.getErrorStream());
    if (code < 200 || code >= 300) throw new RuntimeException("MyMemory HTTP " + code + ": " + body);
    JSONObject root = new JSONObject(body);
    if (root.optInt("responseStatus", 200) >= 400) throw new RuntimeException("MyMemory response " + root.optString("responseDetails", "failed"));
    JSONObject data = root.optJSONObject("responseData");
    String translated = data == null ? "" : data.optString("translatedText", "");
    return translated == null ? "" : translated.trim();
}

private String translateTextByEdge(String q) throws Exception {
    String endpoint = "https://api-edge.cognitive.microsofttranslator.com/translate?api-version=3.0&from=en&to=zh-Hans";
    HttpURLConnection c = (HttpURLConnection) new URL(endpoint).openConnection();
    c.setRequestMethod("POST");
    c.setInstanceFollowRedirects(true);
    c.setConnectTimeout(12000);
    c.setReadTimeout(18000);
    c.setDoOutput(true);
    c.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
    c.setRequestProperty("Accept", "application/json");
    c.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
    c.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 Chrome/120 Safari/537.36 Edg/120");
    c.setRequestProperty("Origin", "https://www.bing.com");
    c.setRequestProperty("Referer", "https://www.bing.com/translator");
    JSONArray req = new JSONArray();
    JSONObject obj = new JSONObject();
    obj.put("Text", q);
    req.put(obj);
    byte[] data = req.toString().getBytes("UTF-8");
    c.setFixedLengthStreamingMode(data.length);
    try (OutputStream os = c.getOutputStream()) { os.write(data); }
    int code = c.getResponseCode();
    String body = readSmallText(code >= 200 && code < 300 ? c.getInputStream() : c.getErrorStream());
    if (code < 200 || code >= 300) throw new RuntimeException("Edge Translate HTTP " + code + ": " + body);
    JSONArray root = new JSONArray(body);
    if (root.length() == 0) return "";
    JSONArray translations = root.optJSONObject(0) == null ? null : root.optJSONObject(0).optJSONArray("translations");
    if (translations == null || translations.length() == 0) return "";
    JSONObject first = translations.optJSONObject(0);
    return first == null ? "" : first.optString("text", "").trim();
}

private String translateTextByGoogleapis(String q) throws Exception {
    return translateWithGoogleEndpoint("https://translate.googleapis.com/translate_a/single", q);
}

private String translateWithGoogleEndpoint(String endpoint, String q) throws Exception {
    String url = endpoint + "?client=gtx&sl=auto&tl=zh-CN&dt=t&q=" + URLEncoder.encode(q, "UTF-8");
    HttpURLConnection c = (HttpURLConnection) new URL(url).openConnection();
    c.setInstanceFollowRedirects(true);
    c.setConnectTimeout(12000);
    c.setReadTimeout(18000);
    c.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 13) AppleWebKit/537.36 Chrome/120 Mobile Safari/537.36");
    c.setRequestProperty("Accept", "application/json,text/plain,*/*");
    c.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
    int code = c.getResponseCode();
    String body = readSmallText(code >= 200 && code < 300 ? c.getInputStream() : c.getErrorStream());
    if (code < 200 || code >= 300) throw new RuntimeException("Translate HTTP " + code + " " + endpoint);
    JSONArray root = new JSONArray(body);
    JSONArray sentences = root.optJSONArray(0);
    StringBuilder sb = new StringBuilder();
    if (sentences != null) {
        for (int i = 0; i < sentences.length(); i++) {
            JSONArray part = sentences.optJSONArray(i);
            if (part != null) sb.append(part.optString(0, ""));
        }
    }
    return sb.toString().trim();
}

private String extractImageUrlFromHtml(String html, String baseUrl) {
    return metadataController.extractImageUrlFromHtml(html, baseUrl);
}

private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }

    /** 在 TextView 文字前用 ImageSpan 内联一个小图标（紧贴文字、垂直居中）。
     * 不依赖 drawableStart，避免工具链/布局导致的图标与文字分离问题。
     * textWithLeadingSpace 形如 " 开始"，首字符（空格）用来承载图标。
     * tintColor：>0 时对图标着色（如浅色按钮上需深色图标）；<=0 保持矢量原色。 */
    private void setIconedText(android.widget.TextView tv, int drawableRes, String textWithLeadingSpace, int tintColor) {
        if (tv == null) return;
        Drawable icon = ContextCompat.getDrawable(this, drawableRes);
        if (icon == null) { tv.setText(textWithLeadingSpace == null ? "" : textWithLeadingSpace.trim()); return; }
        int px = dp(9);
        icon.setBounds(0, 0, px, px);
        if (tintColor > 0) {
            icon = icon.mutate();
            if (android.os.Build.VERSION.SDK_INT >= 21) icon.setTint(tintColor);
            else icon.setColorFilter(tintColor, android.graphics.PorterDuff.Mode.SRC_IN);
        }
        String s = (textWithLeadingSpace == null || textWithLeadingSpace.isEmpty()) ? " " : textWithLeadingSpace;
        android.text.SpannableString ss = new android.text.SpannableString(s);
        ss.setSpan(new android.text.style.ImageSpan(icon, android.text.style.ImageSpan.ALIGN_CENTER), 0, 1, android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(ss);
    }

private String safeCacheName(String input) {
    return metadataController.safeCacheName(input);
}

private void setSideDescription(String text) {
    sideFullDescription = emptyText(text, "No introduction yet.");
    sideDescExpanded = false;
    renderSideDescription();
}

private void renderSideDescription() {
    if (sideDetailHint == null || sideDescToggle == null) return;
    sideDetailHint.setText(sideFullDescription == null ? "" : sideFullDescription);
    boolean longEnough = sideFullDescription != null && (sideFullDescription.length() > 110 || sideFullDescription.contains("\n\n") || sideFullDescription.split("\n").length > 5);
    sideDetailHint.setMaxLines(sideDescExpanded ? Integer.MAX_VALUE : 5);
    sideDetailHint.setEllipsize(sideDescExpanded ? null : android.text.TextUtils.TruncateAt.END);
    sideDescToggle.setVisibility(longEnough ? View.VISIBLE : View.GONE);
    sideDescToggle.setText(sideDescExpanded ? "Close" : "Expand");
}

private void renderTagChips(String tagsText) {
    if (sideTagContainer == null || sideDetailTags == null) return;
    sideTagContainer.removeAllViews();
    String source = tagsText == null ? "" : tagsText.trim();
    if (source.isEmpty() || "-".equals(source)) {
        sideTagContainer.addView(sideDetailTags);
        sideDetailTags.setText("-");
        sideDetailTags.setVisibility(View.VISIBLE);
        return;
    }
    sideDetailTags.setVisibility(View.GONE);
    String[] tags = source.split("\\s{2,}|[,，/]");
    LinearLayout row = null;
    int countInRow = 0;
    for (String raw : tags) {
        String tag = raw == null ? "" : raw.trim();
        if (tag.isEmpty()) continue;
        if (row == null || countInRow >= 2) {
            row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            sideTagContainer.addView(row);
            countInRow = 0;
        }
        TextView chip = new TextView(this);
        chip.setText(tag);
        chip.setTextSize(7);
        chip.setTextColor(primaryTextColor());
        chip.setSingleLine(true);
        chip.setEllipsize(android.text.TextUtils.TruncateAt.END);
        chip.setGravity(android.view.Gravity.CENTER);
        chip.setBackgroundResource(R.drawable.bg_chip);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, dp(20), 1);
        lp.setMargins(0, 0, dp(3), dp(3));
        row.addView(chip, lp);
        countInRow++;
    }
    if (sideTagContainer.getChildCount() == 0) {
        sideTagContainer.addView(sideDetailTags);
        sideDetailTags.setText("-");
        sideDetailTags.setVisibility(View.VISIBLE);
    }
}

private String buildMetadataSearchKeyword(String title) {
    return metadataController.buildMetadataSearchKeyword(title);
}

    private boolean isConfidentMatch(String localTitle, VnMetadata meta) {
        return metadataController.isConfidentMatch(localTitle, meta);
    }

private void showVndbCandidateDialog(Game game, List<VnMetadata> list) {
    metadataController.showVndbCandidateDialog(game, list);
}

private void applyVndbMetadata(VnMetadata meta, Game game) {
    metadataController.applyVndbMetadata(meta, game);
}

private void updateSideDetail(Game game) {
        selectedGame = game;
        currentSideMetadata = null;
        sideShowingTranslatedDescription = game != null && isTranslatedStateFor(game.id);
        updateTranslateButtonState();
        if (adapter != null) adapter.setSelectedGameId(game == null ? -1 : game.id);
        if (sideDetailTitle == null) return;
        boolean hasGame = game != null;
        sideBtnLaunch.setEnabled(hasGame);
        sideBtnOptions.setEnabled(hasGame);
        sideBtnLaunch.setAlpha(hasGame ? 1f : 0.45f);
        sideBtnOptions.setAlpha(hasGame ? 1f : 0.45f);
        if (!hasGame) {
sideDetailTitle.setText("Please select a game");
updateSideMetadataSourceBadge("");
sideDetailOriginalTitle.setText("");
            setSideDescription("点击中间的游戏卡片后，这里会显示封面、启动入口和选项。后续可接 VNDB/API 简介与元数据。");
            sideDetailDate.setText("release date:-");
sideDetailDeveloper.setText("Developer:-");
if (sideDetailPath != null) sideDetailPath.setText("path:-");
            sideDetailRating.setText("评分：-/10");
            if (sideDetailLength != null) sideDetailLength.setText("Play time:-");
            renderTagChips("-");
            sideDetailCoverUri = null;
            sideDetailCover.setImageDrawable(null);
            sideDetailCover.setVisibility(View.GONE);
            sideDetailPlaceholder.setVisibility(View.VISIBLE);
            sideDetailPlaceholder.setText("Select game");
            if (sideScreenshot1 != null) sideScreenshot1.setImageDrawable(null);
            if (sideScreenshot2 != null) sideScreenshot2.setImageDrawable(null);
            return;
        }
        sideDetailTitle.setText(emptyText(game.title, "Unnamed game"));
updateSideMetadataSourceBadge("");
sideDetailOriginalTitle.setText(metadataSourceLabel() + "Matching…");
        setSideDescription(emptyText(game.description, "Removing from" + metadataSourceLabel() + "Get an introduction…"));
        sideDetailDate.setText("release date:-");
sideDetailDeveloper.setText("Developer:-");
if (sideDetailPath != null) sideDetailPath.setText("Path:" + displayPath(game.rootUri));
        sideDetailRating.setText("评分：-/10");
        if (sideDetailLength != null) sideDetailLength.setText("Play time:-");
        renderTagChips("-");
        if (sideScreenshot1 != null) sideScreenshot1.setImageDrawable(null);
        if (sideScreenshot2 != null) sideScreenshot2.setImageDrawable(null);
        String coverUri = safeCoverUri(game);
        if (coverUri != null && !coverUri.isEmpty()) {
            // M21-2：**同一张封面不重复解码**。
            // setImageURI 是同步解码（主线程），每次单击都重解一遍 → 几百毫秒的停顿，
            // 既让 UI 发木，也把第二次点击推到双击窗口之外（"不能连点/得狂按"的根因之一）。
            if (coverUri.equals(sideDetailCoverUri) && sideDetailCover.getDrawable() != null) {
                sideDetailCover.setVisibility(View.VISIBLE);
                sideDetailPlaceholder.setVisibility(View.GONE);
            } else {
                try {
                    sideDetailCover.setImageURI(Uri.parse(coverUri));
                    sideDetailCoverUri = coverUri;
                    sideDetailCover.setVisibility(View.VISIBLE);
                    sideDetailPlaceholder.setVisibility(View.GONE);
                } catch (Throwable t) {
                    sideDetailCoverUri = null;
                    sideDetailCover.setImageDrawable(null);
                    sideDetailCover.setVisibility(View.GONE);
                    sideDetailPlaceholder.setVisibility(View.VISIBLE);
                    sideDetailPlaceholder.setText(initials(game.title));
                }
            }
        } else {
            sideDetailCoverUri = null;
            sideDetailCover.setImageDrawable(null);
            sideDetailCover.setVisibility(View.GONE);
            sideDetailPlaceholder.setVisibility(View.VISIBLE);
            sideDetailPlaceholder.setText(initials(game.title));
        }
        fetchSelectedMetadata(game);
    }

    private void showCustomVndbSearchDialog(Game game) {
        metadataController.showCustomVndbSearchDialog(game);
    }

private void showCustomBangumiSearchDialog(Game game) {
    metadataController.showCustomBangumiSearchDialog(game);
}

private void showCustomYmgalSearchDialog(Game game) {
    metadataController.showCustomYmgalSearchDialog(game);
}

private void showCustomHikarinagiSearchDialog(Game game) {
    metadataController.showCustomHikarinagiSearchDialog(game);
}

private void searchBangumiWithKeyword(Game game, String keyword) {
    metadataController.searchBangumiWithKeyword(game, keyword);
}

private void searchYmgalWithKeyword(Game game, String keyword) {
    metadataController.searchYmgalWithKeyword(game, keyword);
}

private void searchHikarinagiWithKeyword(Game game, String keyword) {
    metadataController.searchHikarinagiWithKeyword(game, keyword);
}

private void searchVndbWithKeyword(Game game, String keyword) {
    metadataController.searchVndbWithKeyword(game, keyword);
}

private void syncCurrentMetadataToGameCard(Game game) {
    metadataController.syncCurrentMetadataToGameCard(game);
}

private String cacheRemoteImageSync(String url, String prefix) {
    return metadataController.cacheRemoteImageSync(url, prefix);
}

private void styleAlertDialogDark(AlertDialog dialog) {
    if (dialog == null) return;
    try {
        Window w = dialog.getWindow();
        if (w != null) {
            w.setBackgroundDrawableResource(R.drawable.bg_dialog);
            DynamicTheme dt = DynamicTheme.getInstance();
            if (dt.isEnabled() && dt.getColors() != null) {
                w.setBackgroundDrawable(tintDialog(dt.getColors()));
            }
        }
        int text = getColorCompat(R.color.yh_text);
        int muted = getColorCompat(R.color.yh_text_muted);
        int primary = getColorCompat(R.color.yh_primary);
        int titleId = getResources().getIdentifier("alertTitle", "id", "android");
        TextView title = dialog.findViewById(titleId);
        if (title != null) title.setTextColor(text);
        TextView msg = dialog.findViewById(android.R.id.message);
        if (msg != null) msg.setTextColor(muted);
        Button p = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button n = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        Button neu = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);
        DynamicTheme dt2 = DynamicTheme.getInstance();
        if (dt2.isEnabled() && dt2.getColors() != null) {
            ThemeColorExtractor.ThemeColors dc = dt2.getColors();
            if (p != null) { p.setBackground(tintButton(dc)); p.setTextColor(0xFFFFFFFF); }
            if (n != null) { n.setBackground(tintInput(dc)); n.setTextColor(0xFFFFFFFF); }
            if (neu != null) { neu.setBackground(tintInput(dc)); neu.setTextColor(0xFFFFFFFF); }
        } else {
            if (p != null) p.setTextColor(primary);
            if (n != null) n.setTextColor(primary);
            if (neu != null) neu.setTextColor(primary);
        }
        android.widget.ListView list = dialog.getListView();
        if (list != null) {
            list.setBackgroundColor(Color.TRANSPARENT);
            list.setCacheColorHint(Color.TRANSPARENT);
        }
    } catch (Throwable ignored) { }
}

private void showSideOptions(Game game) {
        if (game == null) return;
        String sourceLabel = metadataSourceLabel();
String rematchItem = "Rematch" + sourceLabel;
        String customSearchItem = "Custom search" + sourceLabel;
        String syncItem = "Sync" + sourceLabel + "To card";
        String playTimeItem = "Modify play time";
        String favoriteItem = game.favorite ? "Cancel favorites" : "Collection games";
        String hideItem = game.hidden ? "Unhide" : "Hidden game";
        String nsfwBlurLabel = "🔞 NSFW cover blurry";
        boolean nsfwBlurGameOn = game.nsfw;
        String nsfwBlurItem = nsfwBlurLabel + "：" + (nsfwBlurGameOn ? "Turn on" : "Close");
        java.util.List<String> itemList = new java.util.ArrayList<>(java.util.Arrays.asList(
                "Edit game", "Set play status", playTimeItem, favoriteItem, hideItem, nsfwBlurItem, rematchItem, customSearchItem, syncItem));
        if (game.engine == EngineType.KIRIKIRI || game.engine == EngineType.ONS || game.engine == EngineType.PC || game.engine == EngineType.FVP) itemList.add("Engine settings");
        if (game.engine == EngineType.KIRIKIRI || game.engine == EngineType.ARTEMIS || game.engine == EngineType.FVP) itemList.add("Virtual mouse");
        // 桌面快捷方式：部分启动器（含部分定制 ROM）不支持固定快捷方式，不支持时不显示该项
        boolean shortcutSupported = com.yuki.yukihub.shortcut.GameShortcutManager.isSupported(this);
        String shortcutItem = "📌 Add to desktop";
        if (shortcutSupported) itemList.add(shortcutItem);
        itemList.add("Details");
        itemList.add("Delete game");
        itemList.add("多选操作（隐藏 / 删除）…");
        String[] items = itemList.toArray(new String[0]);
        LinearLayout listRoot = new LinearLayout(this);
        listRoot.setOrientation(LinearLayout.VERTICAL);
        listRoot.setBackgroundResource(R.drawable.bg_dialog);
        tintDialogRoot(listRoot);
        int hp = dp(18);
        listRoot.setPadding(0, dp(6), 0, dp(6));
        final AlertDialog[] ref = new AlertDialog[1];
        for (String item : items) {
            TextView row = new TextView(this);
            row.setText(item);
            boolean isDangerItem = "Delete game".equals(item) || item.startsWith("Multiple selection operation");
            row.setTextColor(getColorCompat(isDangerItem ? R.color.yh_secondary : R.color.yh_text));
            row.setTextSize(15);
            row.setGravity(android.view.Gravity.CENTER_VERTICAL);
            row.setPadding(hp, 0, hp, 0);
            row.setBackgroundResource(R.drawable.bg_input);
            LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp(46));
            rlp.setMargins(dp(10), dp(4), dp(10), dp(4));
            listRoot.addView(row, rlp);
            row.setOnClickListener(v -> {
                playUiSound(UI_SOUND_CONFIRM);
                if (ref[0] != null) ref[0].dismiss();
                String chosen = ((TextView) v).getText().toString();
                if ("Edit game".equals(chosen)) showEditDialog(game);
                else if ("Set play status".equals(chosen)) showPlayStatusDialog(game, null);
                else if (playTimeItem.equals(chosen)) showEditPlayTimeDialog(game);
                else if (favoriteItem.equals(chosen)) {
                    game.favorite = !game.favorite;
                    repository.update(game);
                    loadGames();
                    Toast.makeText(this, game.favorite ? "Collected" : "Canceled favorites", Toast.LENGTH_SHORT).show();
                }
                else if (hideItem.equals(chosen)) toggleGameHidden(game);
                else if (rematchItem.equals(chosen)) {
    clearCurrentSourceMetadata(game.id);
    fetchSelectedMetadata(game, true);
}
else if (customSearchItem.equals(chosen)) showCurrentSourceCustomSearchDialog(game);
else if (syncItem.equals(chosen)) syncCurrentMetadataToGameCard(game);
                else if (chosen != null && chosen.startsWith(nsfwBlurLabel)) {
                    boolean newVal = !nsfwBlurGameOn;
                    game.nsfw = newVal;
                    repository.update(game);
                    Toast.makeText(this, "NSFW cover blurry:" + (newVal ? "Turn on" : "Close"), Toast.LENGTH_SHORT).show();
                    loadGames();
                }
                else if ("Engine settings".equals(chosen)) { if (game.engine == EngineType.ONS) showOnsSettingsDialog(game); else if (game.engine == EngineType.PC) showPcSettingsDialog(game); else if (game.engine == EngineType.FVP) showFvpSettingsDialog(game); else showKrSettingsDialog(game); }
                else if ("Virtual mouse".equals(chosen)) showGameCursorDialog(game);
                else if (shortcutItem.equals(chosen)) requestGameShortcut(game);
                else if ("Details".equals(chosen)) showDetailDialog(game);
                else if ("Delete game".equals(chosen)) confirmDeleteGame(game);
                else if (chosen != null && chosen.startsWith("Multiple selection operation")) enterMultiSelectMode(game);
            });
        }
        ScrollView optionScroll = new ScrollView(this);
        optionScroll.setFillViewport(false);
        optionScroll.setBackgroundResource(R.drawable.bg_dialog);
        tintDialogRoot(optionScroll);
        optionScroll.addView(listRoot, new ScrollView.LayoutParams(ScrollView.LayoutParams.MATCH_PARENT, ScrollView.LayoutParams.WRAP_CONTENT));
        AlertDialog optionDialog = new AlertDialog.Builder(this)
                .setTitle(emptyText(game.title, "Game options"))
                .setView(optionScroll)
                .show();
        ref[0] = optionDialog;
        styleAlertDialogDark(optionDialog);
        if (optionDialog.getWindow() != null) {
            optionDialog.getWindow().setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.48f), (int) (getResources().getDisplayMetrics().heightPixels * 0.78f));
        }
    }

    // ==================== 游戏库隐藏 / 恢复 ====================

    /**
     * 隐藏或恢复单款游戏。
     * <p>隐藏只改数据库 {@code hidden} 标志：不删除记录、不动封面/PV/游玩时长，
     * 所以「隐藏分类 → 取消隐藏」是无损可逆的。恢复时若当前停在隐藏分类且已清空，
     * 自动切回「全部」并提示，避免用户对着空列表以为游戏丢了。
     */
    private void toggleGameHidden(Game game) {
        if (game == null || game.id <= 0) return;
        boolean hide = !game.hidden;
        if (hide) {
            // 隐藏属于「要让人找不到」的操作，给一次二次确认；恢复不设门槛（用户本来就在找它）。
            String title = emptyText(game.title, "This game");
            new AlertDialog.Builder(this)
                    .setTitle("Hide this game?")
                    .setMessage("“" + title + "” will be removed from the game library list.\n\nGame files will not be deleted, nor will cover, play time, and collections be cleared;"
                            + "You can then unhide it in the \"Hide\" category of the filter bar.")
                    .setPositiveButton("hide", (d, w) -> applyGameHidden(game, true))
                    .setNegativeButton("Cancel", null)
                    .show();
            return;
        }
        applyGameHidden(game, false);
    }

    /** 真正落库 + 刷新列表。写库失败不假报成功。 */
    private void applyGameHidden(Game game, boolean hidden) {
        int rows;
        try {
            rows = repository.setHidden(game.id, hidden);
        } catch (Throwable t) {
            Log.w("YukiHub", "setHidden failed: " + game.id, t);
            rows = 0;
        }
        if (rows != 1) {
            Toast.makeText(this, hidden ? "Failed to hide, please try again" : "Failed to unhide, please try again", Toast.LENGTH_SHORT).show();
            return;
        }
        game.hidden = hidden;
        if (hidden && selectedGame != null && selectedGame.id == game.id) {
            // 被隐藏的游戏马上就要从列表消失，侧栏不能继续指向它。
            updateSideDetail(null);
        }
        // 在隐藏分类里恢复最后一项 → 先把分类切回「全部」，避免先生成一份空列表再跳转。
        boolean leavingHiddenView = !hidden && "HIDDEN".equals(filter);
        loadGames(); // 内部会重建隐藏列表并刷新计数
        if (leavingHiddenView && hiddenGames.isEmpty()) {
            filter = "ALL";
            updateFilterSelection();
            applyFilter();
        }
        Toast.makeText(this, hidden
                ? "Hidden; the \"hidden\" category in the filter bar can be unhidden"
                : "Restored ·" + emptyText(game.title, "") + "(Can be viewed in \"All\")",
                Toast.LENGTH_SHORT).show();
    }

    // ==================== 批量重扫全部游戏资料 ====================

    /** 保存批量重扫间隔（秒），非法输入回退到已存值。 */
    private void saveBulkRefreshInterval(EditText input) {
        if (input == null) return;
        try {
            float sec = Float.parseFloat(input.getText().toString().trim());
            if (sec < 0.5f) sec = 0.5f;
            if (sec > 15f) sec = 15f;
            prefs.edit().putFloat(MetadataController.KEY_BULK_REFRESH_INTERVAL_SEC, sec).apply();
            input.setText(String.valueOf(sec));
        } catch (Throwable t) {
            input.setText(String.valueOf(prefs.getFloat(MetadataController.KEY_BULK_REFRESH_INTERVAL_SEC, 2.5f)));
        }
    }

    /** 设置页"一键重扫全部游戏资料"入口：确认弹窗。 */
    private void showBulkMetadataRefreshDialog() {
        if (metadataController.isBulkRefreshRunning()) {
            Toast.makeText(this, "Batch rescan is already running", Toast.LENGTH_SHORT).show();
            return;
        }
        int count = 0;
        int incomplete = 0;
        for (Game g : allGames) {
            if (g != null && g.id > 0 && g.title != null && !g.title.trim().isEmpty()) {
                count++;
                if (!metadataController.isCurrentSourceMetadataComplete(g.id)) incomplete++;
            }
        }
        if (count == 0) {
            Toast.makeText(this, "There are no games to rescan", Toast.LENGTH_SHORT).show();
            return;
        }
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(dp(20), dp(6), dp(20), 0);
        TextView msg = new TextView(this);
        float intervalSec = prefs.getFloat(MetadataController.KEY_BULK_REFRESH_INTERVAL_SEC, 2.5f);
        msg.setText("Will use the current data source \"" + metadataSourceLabel() + "\"Reacquire" + count + "Game information" + (incomplete > 0 ? "(Of which approx." + incomplete + "Payment information is incomplete)" : "") + ", the cached content will be overwritten and refreshed.\n\nThe third-party interface has limited current flow, serial rescan and the interval between each model is about" + intervalSec + "Seconds, please stay connected to the Internet during this time.");
        msg.setTextColor(getColorCompat(R.color.yh_text));
        msg.setTextSize(12);
        msg.setLineSpacing(dp(2), 1.0f);
        box.addView(msg);
        CheckBox onlyIncomplete = new CheckBox(this);
        onlyIncomplete.setText("Only rescan games with incomplete data (skip games with complete cache, faster and more economical)");
        onlyIncomplete.setTextColor(getColorCompat(R.color.yh_text));
        onlyIncomplete.setTextSize(12);
        onlyIncomplete.setChecked(true);
        box.addView(onlyIncomplete);
        final int totalCount = count;
        AlertDialog confirmDialog = new AlertDialog.Builder(this)
                .setTitle("Re-scan all game data")
                .setView(box)
                .setPositiveButton("Start rescanning", (d, w) -> startBulkMetadataRefresh(onlyIncomplete.isChecked(), totalCount))
                .setNegativeButton("Cancel", null)
                .show();
        styleAlertDialogDark(confirmDialog);
    }

    /** 执行批量重扫：进度对话框 + 回调。 */
    private void startBulkMetadataRefresh(boolean onlyIncomplete, int totalGames) {
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(dp(20), dp(10), dp(20), dp(10));
        ProgressBar pb = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        pb.setMax(Math.max(totalGames, 1));
        pb.setProgress(0);
        box.addView(pb);
        TextView status = new TextView(this);
        status.setText("Preparing…");
        status.setTextColor(getColorCompat(R.color.yh_text));
        status.setTextSize(12);
        status.setLineSpacing(dp(2), 1.0f);
        status.setPadding(0, dp(8), 0, 0);
        box.addView(status);
        AlertDialog progressDialog = new AlertDialog.Builder(this)
                .setTitle("Re-scanning game data")
                .setView(box)
                .setNegativeButton("Stop", (d, w) -> {
                    metadataController.cancelBulkRefresh();
                    status.setText("Stopping...(Exiting after the current game is processed)");
                })
                .setCancelable(false)
                .create();
        progressDialog.show();
        styleAlertDialogDark(progressDialog);

        metadataController.refreshAllMetadata(onlyIncomplete, new MetadataController.BulkRefreshCallback() {
            @Override
            public void onProgress(int done, int total, String gameTitle, String statusText) {
                if (isFinishing() || isDestroyed()) {
                    metadataController.cancelBulkRefresh();
                    return;
                }
                pb.setMax(Math.max(total, 1));
                pb.setProgress(Math.min(done + 1, Math.max(total, 1)));
                status.setText("Processing" + (done + 1) + "/" + total + "：" + gameTitle);
            }

            @Override
            public void onFinished(MetadataController.BulkRefreshResult result) {
                if (isFinishing() || isDestroyed()) {
                    metadataController.cancelBulkRefresh();
                    return;
                }
                try { progressDialog.dismiss(); } catch (Throwable ignored) { }
                showBulkRefreshResult(result);
            }
        });
    }

    /** 批量重扫结果汇总。 */
    private void showBulkRefreshResult(MetadataController.BulkRefreshResult r) {
        StringBuilder sb = new StringBuilder();
        sb.append(r.cancelled ? "Stopped, completed part:" : "Rescan completed:");
        sb.append("· try").append(r.attempted).append("Payment");
        if (r.skipped > 0) sb.append("· Skip (information is complete)").append(r.skipped).append("Payment");
        sb.append("· success").append(r.success).append("Payment");
        sb.append("· fail").append(r.failed).append("Payment");
        if (r.needConfirm > 0) sb.append("· Manual confirmation required").append(r.needConfirm).append("Payment");
        StringBuilder detailSb = new StringBuilder();
        int shown = 0;
        for (String d : r.details) {
            if (shown >= 20) break;
            detailSb.append(d).append("\n");
            shown++;
        }
        if (r.details.size() > shown) detailSb.append("…common").append(r.details.size()).append("Item details");
        AlertDialog resultDialog = new AlertDialog.Builder(this)
                .setTitle(r.cancelled ? "Stopped" : "Rescan completed")
                .setMessage(sb.toString() + (detailSb.length() > 0 ? "\n" + detailSb : ""))
                .setPositiveButton("Knew", null)
                .show();
        styleAlertDialogDark(resultDialog);
        // 当前选中的游戏若刚被重扫，刷新右侧资料卡片
        if (selectedGame != null) updateSideDetail(selectedGame);
    }

    /**
     * 请求把游戏添加到桌面。
     *
     * 图标生成要解码封面 + 取主色，放在 IO 线程做；
     * requestPinShortcut 本身会弹系统确认框，必须回到主线程调用。
     */
    private void requestGameShortcut(Game game) {
        if (game == null) return;
        if (!com.yuki.yukihub.shortcut.GameShortcutManager.isSupported(this)) {
            Toast.makeText(this, "The current desktop does not support adding shortcuts", Toast.LENGTH_LONG).show();
            return;
        }
        if (com.yuki.yukihub.shortcut.GameShortcutManager.isPinned(this, game.id)) {
            Toast.makeText(this, "There is already a shortcut for this game on your desktop", Toast.LENGTH_SHORT).show();
            return;
        }
        final long gameId = game.id;
        final String title = game.title;
        final String cover = safeCoverUri(game);
        AppExecutors.runOnIo(() -> {
            // 封面解码 + 取主色在 IO 线程完成，避免主线程卡顿
            final android.graphics.drawable.Icon icon =
                    com.yuki.yukihub.shortcut.GameShortcutManager.prepareIcon(
                            MainActivity.this, title, cover);
            runOnUiThread(() -> {
                if (isFinishing() || isDestroyed()) return;
                // requestPinShortcut 会弹系统确认框，必须在主线程
                boolean ok = com.yuki.yukihub.shortcut.GameShortcutManager.requestPinWithIcon(
                        MainActivity.this, gameId, title, icon, MainActivity.class);
                if (!ok) {
                    Toast.makeText(MainActivity.this, "Adding failed, the current desktop may not support", Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    private void confirmDeleteGame(Game game) {
        if (game == null) return;
        new AlertDialog.Builder(this)
                .setTitle("Delete game")
                .setMessage("Confirm to delete \"" + game.title + "\"? The ontology file will not be deleted.")
                .setPositiveButton("Delete", (x,w)->{
                    com.yuki.yukihub.shortcut.GameShortcutManager.disableForGame(this, game.id);
                    PcLaunchPrefs.clear(this, game.id);
                    FvpLaunchPrefs.clear(this, game.id);
                    repository.delete(game.id); selectedGame = null; loadGames(); })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // ==================== 多选批量操作（删除 / 隐藏 / 恢复）====================

    /** 进入多选模式，并预勾选当前游戏。 */
    private void enterMultiSelectMode(Game seed) {
        if (adapter == null) return;
        adapter.setMultiSelectMode(true);
        if (seed != null) adapter.toggleChecked(seed.id);
        ensureMultiSelectBar();
        if (multiSelectBar != null) multiSelectBar.setVisibility(View.VISIBLE);
        updateMultiSelectBar(adapter.getCheckedCount());
        Toast.makeText(this, "You have entered multi-selection mode. You can hide or delete games in batches after clicking on them.", Toast.LENGTH_SHORT).show();
    }

    private void exitMultiSelectMode() {
        if (adapter != null) adapter.setMultiSelectMode(false);
        if (multiSelectBar != null) multiSelectBar.setVisibility(View.GONE);
    }

    private void updateMultiSelectBar(int count) {
        if (multiSelectCount != null) {
            multiSelectCount.setText("Selected" + count + " 个");
        }
        // 隐藏分类里按钮是「恢复」，其余分类是「隐藏」；文案与配色一起跟着分类走。
        if (multiSelectHideBtn != null) {
            boolean inHiddenView = "HIDDEN".equals(filter);
            multiSelectHideBtn.setText(inHiddenView ? "Recover" : "hide");
            multiSelectHideBtn.setTextColor(getColorCompat(inHiddenView ? R.color.yh_primary : R.color.yh_text));
        }
        if (multiSelectBar != null) {
            multiSelectBar.setVisibility(adapter != null && adapter.isMultiSelectMode() ? View.VISIBLE : View.GONE);
        }
    }

    /** 在主内容区顶部动态挂载多选操作栏。 */
    private void ensureMultiSelectBar() {
        if (multiSelectBar != null) return;
        ViewGroup contentRoot = findViewById(android.R.id.content);
        if (contentRoot == null) return;
        // 挂到 activity 根 FrameLayout 上，绝对定位在底部
        View root = contentRoot.getChildCount() > 0 ? contentRoot.getChildAt(0) : contentRoot;
        if (!(root instanceof ViewGroup)) return;

        multiSelectBar = new LinearLayout(this);
        multiSelectBar.setOrientation(LinearLayout.HORIZONTAL);
        multiSelectBar.setGravity(android.view.Gravity.CENTER_VERTICAL);
        multiSelectBar.setPadding(dp(12), dp(8), dp(12), dp(8));
        GradientDrawable barBg = new GradientDrawable();
        barBg.setColor(0xF0121A2E);
        barBg.setCornerRadius(dp(12));
        barBg.setStroke(dp(1), 0x5534C759);
        multiSelectBar.setBackground(barBg);
        multiSelectBar.setElevation(dp(8));
        multiSelectBar.setVisibility(View.GONE);

        multiSelectCount = new TextView(this);
        multiSelectCount.setText("0 selected");
        multiSelectCount.setTextColor(getColorCompat(R.color.yh_text));
        multiSelectCount.setTextSize(13);
        multiSelectCount.setTypeface(null, android.graphics.Typeface.BOLD);
        multiSelectBar.addView(multiSelectCount, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        TextView selectAllBtn = makeMultiSelectAction("Select all", getColorCompat(R.color.yh_primary));
        selectAllBtn.setOnClickListener(v -> {
            if (adapter != null) adapter.selectAllVisible();
        });
        multiSelectBar.addView(selectAllBtn, multiSelectBtnLp());

        TextView deleteBtn = makeMultiSelectAction("Delete", getColorCompat(R.color.yh_secondary));
        deleteBtn.setOnClickListener(v -> confirmBatchDelete());
        multiSelectBar.addView(deleteBtn, multiSelectBtnLp());

        // 批量隐藏 / 恢复：按钮语义跟随当前分类（在「隐藏」分类里就是恢复）。
        // 多选栏只创建一次，所以文案不在闭包里定死，由 updateMultiSelectBar 每次刷新。
        multiSelectHideBtn = makeMultiSelectAction("hide", getColorCompat(R.color.yh_text));
        multiSelectHideBtn.setOnClickListener(v -> confirmBatchHide(!"HIDDEN".equals(filter)));
        multiSelectBar.addView(multiSelectHideBtn, multiSelectBtnLp());

        TextView cancelBtn = makeMultiSelectAction("Cancel", getColorCompat(R.color.yh_text_muted));
        cancelBtn.setOnClickListener(v -> exitMultiSelectMode());
        multiSelectBar.addView(cancelBtn, multiSelectBtnLp());

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        lp.gravity = android.view.Gravity.BOTTOM;
        lp.setMargins(dp(12), 0, dp(12), dp(14));
        if (root instanceof FrameLayout) {
            ((FrameLayout) root).addView(multiSelectBar, lp);
        } else {
            // 兜底：包一层
            ((ViewGroup) root).addView(multiSelectBar, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }

    private TextView makeMultiSelectAction(String text, int color) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextColor(color);
        tv.setTextSize(13);
        tv.setTypeface(null, android.graphics.Typeface.BOLD);
        tv.setGravity(android.view.Gravity.CENTER);
        tv.setPadding(dp(10), dp(6), dp(10), dp(6));
        tv.setBackgroundResource(R.drawable.bg_input);
        return tv;
    }

    private LinearLayout.LayoutParams multiSelectBtnLp() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(dp(6), 0, 0, 0);
        return lp;
    }

    /** 批量隐藏 / 恢复（二次确认 + 事务落库）。 */
    private void confirmBatchHide(boolean hide) {
        if (adapter == null) return;
        final java.util.Set<Long> ids = adapter.getCheckedIds();
        if (ids.isEmpty()) {
            Toast.makeText(this, hide ? "Please check the games you want to hide first" : "Please check the game you want to restore first", Toast.LENGTH_SHORT).show();
            return;
        }
        int count = ids.size();
        new AlertDialog.Builder(this)
                .setTitle(hide ? "Hide in batches" : "Batch recovery")
                .setMessage(hide
                        ? "Confirm to hide selected" + count + "A game?\nThe game files will not be deleted, nor will the cover and play time be cleared; they can be restored in the \"Hidden\" category of the filter bar."
                        : "Confirm to restore the selected" + count + "A game?\nAfter recovery, it will return to the \"All\" category.")
                .setPositiveButton(hide ? "Hide" + count + " 个" : "Recover" + count + " 个", (d, w) -> {
                    int updated;
                    try {
                        updated = repository.setHiddenBatch(ids, hide);
                    } catch (Throwable t) {
                        Log.w("YukiHub", "setHiddenBatch failed", t);
                        updated = 0;
                    }
                    if (updated <= 0) {
                        Toast.makeText(MainActivity.this, hide ? "Failed to hide, please try again" : "Restore failed, please try again", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // 选中项里可能含被隐藏的那款，清掉选中；侧栏由 applyFilter 重新指向首项。
                    selectedGame = null;
                    exitMultiSelectMode();
                    loadGames();
                    if (!hide && "HIDDEN".equals(filter) && hiddenGames.isEmpty()) {
                        filter = "ALL";
                        updateFilterSelection();
                        applyFilter();
                    }
                    Toast.makeText(MainActivity.this,
                            (hide ? "Hidden" : "Restored") + updated + "Games", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void confirmBatchDelete() {
        if (adapter == null) return;
        java.util.Set<Long> ids = adapter.getCheckedIds();
        if (ids.isEmpty()) {
            Toast.makeText(this, "Please check the game you want to delete first", Toast.LENGTH_SHORT).show();
            return;
        }
        int count = ids.size();
        new AlertDialog.Builder(this)
                .setTitle("Batch delete")
                .setMessage("Confirm to delete selected" + count + "A game?\nThe base file will not be deleted, only removed from the game library.")
                .setPositiveButton("Delete" + count + " 个", (d, w) -> {
                    com.yuki.yukihub.shortcut.GameShortcutManager.disableForGames(this, ids);
                    int deleted = repository.deleteBatch(ids);
                    selectedGame = null;
                    exitMultiSelectMode();
                    loadGames();
                    Toast.makeText(MainActivity.this, "Deleted" + deleted + "Games", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    /** 一键清空游戏库（二次确认）。 */
    private void confirmClearAllGames() {
        int total = allGames == null ? 0 : allGames.size();
        if (total <= 0) {
            Toast.makeText(this, "The game library is already empty", Toast.LENGTH_SHORT).show();
            return;
        }
        new AlertDialog.Builder(this)
                .setTitle("Clear game library")
                .setMessage("All will be deleted soon" + total + "Game records."
                        + "· Ontology files will not be deleted"
                        + "· Play time records will be cleared together"
                        + "· This operation is irreversible"
                        + "Are you sure to continue?")
                .setPositiveButton("Clear all", (d, w) -> {
                    // 再确认一次，防止误触
                    new AlertDialog.Builder(this)
                            .setTitle("Final confirmation")
                            .setMessage("I really need to clear everything" + total + "A game?")
                            .setPositiveButton("OK to clear", (d2, w2) -> {
                                com.yuki.yukihub.shortcut.GameShortcutManager.disableAll(this);
                                int deleted = repository.deleteAll();
                                selectedGame = null;
                                exitMultiSelectMode();
                                loadGames();
                                Toast.makeText(MainActivity.this, "Cleared" + deleted + "Games", Toast.LENGTH_SHORT).show();
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private String initials(String title) {
        if (title == null || title.trim().isEmpty()) return "YH";
        return title.trim().substring(0, 1).toUpperCase(Locale.ROOT);
    }

    private String safeCoverUri(Game g) {
        if (g == null) return null;
        if (g.coverPersistUri != null && !g.coverPersistUri.isEmpty()) return g.coverPersistUri;
        if (g.coverUri != null && !g.coverUri.isEmpty()) return g.coverUri;
        return null;
    }

    private void showSettingsDialog() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundResource(R.drawable.bg_dialog);
        tintDialogRoot(root);
        int pad = dp(16);
        root.setPadding(pad, dp(12), pad, dp(8));

        TextView scanTitle = new TextView(this);
        scanTitle.setText("Scan directory");
        scanTitle.setTextColor(getColorCompat(R.color.yh_text));
        scanTitle.setTextSize(14);
        scanTitle.setTypeface(null, android.graphics.Typeface.BOLD);
        root.addView(scanTitle);

        TextView scanInfo = new TextView(this);
        scanInfo.setTextColor(getColorCompat(R.color.yh_text_muted));
        scanInfo.setTextSize(11);
        scanInfo.setPadding(0, dp(4), 0, dp(8));
        root.addView(scanInfo, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        LinearLayout scanRootList = new LinearLayout(this);
        scanRootList.setOrientation(LinearLayout.VERTICAL);
        root.addView(scanRootList);
        activeScanRootList = scanRootList;
        activeScanRootInfo = scanInfo;
        refreshScanRootListUi(scanRootList, scanInfo);

        Button addScanRootButton = krButton("+ Add scan directory");
        addScanRootButton.setTextColor(primaryTextColor());
        addScanRootButton.setOnClickListener(v -> {
            if (getScanRootUris().size() >= MAX_SCAN_ROOTS) {
                Toast.makeText(MainActivity.this, "Most bindings" + MAX_SCAN_ROOTS + "Scan directories", Toast.LENGTH_SHORT).show();
                return;
            }
            launchScanRootPicker(-1);
        });
        LinearLayout.LayoutParams addScanRootLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp(40));
        addScanRootLp.setMargins(0, dp(4), 0, dp(8));
        root.addView(addScanRootButton, addScanRootLp);

        TextView scanDepthTitle = new TextView(this);
        scanDepthTitle.setText("Scan maximum depth on startup");
        scanDepthTitle.setTextColor(getColorCompat(R.color.yh_text));
        scanDepthTitle.setTextSize(14);
        scanDepthTitle.setTypeface(null, android.graphics.Typeface.BOLD);
        root.addView(scanDepthTitle);

        TextView scanDepthInfo = new TextView(this);
        int savedDepth = prefs == null ? DEFAULT_STARTUP_SCAN_DEPTH : prefs.getInt(KEY_STARTUP_SCAN_DEPTH, DEFAULT_STARTUP_SCAN_DEPTH);
        savedDepth = Math.max(1, Math.min(MAX_STARTUP_SCAN_DEPTH, savedDepth));
        scanDepthInfo.setText("Current:" + savedDepth + "Layers (up to 4 layers)");
        scanDepthInfo.setTextColor(getColorCompat(R.color.yh_text_muted));
        scanDepthInfo.setTextSize(11);
        scanDepthInfo.setPadding(0, dp(4), 0, dp(6));
        root.addView(scanDepthInfo);

        LinearLayout depthRow = new LinearLayout(this);
        depthRow.setOrientation(LinearLayout.HORIZONTAL);
        depthRow.setGravity(android.view.Gravity.CENTER_VERTICAL);
        SeekBar scanDepthSeek = new SeekBar(this);
        scanDepthSeek.setMax(MAX_STARTUP_SCAN_DEPTH - 1);
        scanDepthSeek.setProgress(savedDepth - 1);
        LinearLayout.LayoutParams seekLp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        depthRow.addView(scanDepthSeek, seekLp);
        TextView depthValue = new TextView(this);
        depthValue.setText(String.valueOf(savedDepth));
        depthValue.setTextColor(getColorCompat(R.color.yh_text));
        depthValue.setTextSize(15);
        depthValue.setTypeface(null, android.graphics.Typeface.BOLD);
        depthValue.setPadding(dp(10), 0, 0, 0);
        depthRow.addView(depthValue);
        root.addView(depthRow);
        scanDepthSeek.setOnSeekBarChangeListener(new android.widget.SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int depth = Math.max(1, Math.min(MAX_STARTUP_SCAN_DEPTH, progress + 1));
                depthValue.setText(String.valueOf(depth));
                scanDepthInfo.setText("Current:" + depth + "Layers (up to 4 layers)");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
        scanDepthSeek.setProgress(savedDepth - 1);

        TextView scanModeTitle = new TextView(this);
        scanModeTitle.setText("Scan mode");
        scanModeTitle.setTextColor(getColorCompat(R.color.yh_text));
        scanModeTitle.setTextSize(14);
        scanModeTitle.setTypeface(null, android.graphics.Typeface.BOLD);
        root.addView(scanModeTitle);

        TextView scanModeInfo = new TextView(this);
        scanModeInfo.setText("Fast mode: reads directories in batches and caches them, scanning faster; Compatibility mode: the old version scans item by item, with the most conservative behavior. If the fast mode scan results are abnormal, you can switch back to compatibility mode.");
        scanModeInfo.setTextColor(getColorCompat(R.color.yh_text_muted));
        scanModeInfo.setTextSize(11);
        scanModeInfo.setPadding(0, dp(4), 0, dp(6));
        root.addView(scanModeInfo);

        Spinner scanModeSpinner = new TapSafeSpinner(this);
        ArrayAdapter<String> scanModeAdapter = krSpinnerAdapter(new String[]{"Quick mode", "Compatibility mode"});
        scanModeSpinner.setAdapter(scanModeAdapter);
        String savedScanMode = prefs == null ? SCAN_MODE_FAST : prefs.getString(KEY_SCAN_MODE, SCAN_MODE_FAST);
        scanModeSpinner.setSelection(SCAN_MODE_LEGACY.equals(savedScanMode) ? 1 : 0);
        root.addView(scanModeSpinner, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp(44)));

        TextView fontTitle = new TextView(this);
        fontTitle.setText("Overall font size");
        fontTitle.setTextColor(getColorCompat(R.color.yh_text));
        fontTitle.setTextSize(14);
        fontTitle.setTypeface(null, android.graphics.Typeface.BOLD);
        root.addView(fontTitle);

        float savedFontScale = prefs == null ? UiScaleUtil.DEFAULT_FONT_SCALE : prefs.getFloat(UiScaleUtil.KEY_UI_FONT_SCALE, UiScaleUtil.DEFAULT_FONT_SCALE);
        TextView fontInfo = new TextView(this);
        fontInfo.setText("Current:" + UiScaleUtil.percent(savedFontScale) + "% (default 100%)");
        fontInfo.setTextColor(getColorCompat(R.color.yh_text_muted));
        fontInfo.setTextSize(11);
        fontInfo.setPadding(0, dp(4), 0, dp(6));
        root.addView(fontInfo);

        LinearLayout fontRow = new LinearLayout(this);
        fontRow.setOrientation(LinearLayout.HORIZONTAL);
        fontRow.setGravity(android.view.Gravity.CENTER_VERTICAL);
        SeekBar fontSeek = new SeekBar(this);
        fontSeek.setMax((int) ((UiScaleUtil.MAX_FONT_SCALE - UiScaleUtil.MIN_FONT_SCALE) * 100f));
        fontSeek.setProgress(Math.round((savedFontScale - UiScaleUtil.MIN_FONT_SCALE) * 100f));
        LinearLayout.LayoutParams fontSeekLp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        fontRow.addView(fontSeek, fontSeekLp);
        TextView fontValue = new TextView(this);
        fontValue.setText(UiScaleUtil.percent(savedFontScale) + "%");
        fontValue.setTextColor(getColorCompat(R.color.yh_text));
        fontValue.setTextSize(15);
        fontValue.setTypeface(null, android.graphics.Typeface.BOLD);
        fontValue.setPadding(dp(10), 0, 0, 0);
        fontRow.addView(fontValue);
        root.addView(fontRow);
        fontSeek.setOnSeekBarChangeListener(new android.widget.SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float scale = UiScaleUtil.clamp(UiScaleUtil.MIN_FONT_SCALE + progress / 100f);
                fontValue.setText(UiScaleUtil.percent(scale) + "%");
                fontInfo.setText("Current:" + UiScaleUtil.percent(scale) + "% (default 100%)");
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override public void onStopTrackingTouch(SeekBar seekBar) { }
        });
        Button fontReset = krButton("Restore default font");
        fontReset.setTextColor(getColorCompat(R.color.yh_text));
        fontReset.setOnClickListener(v -> {
            fontSeek.setProgress(Math.round((UiScaleUtil.DEFAULT_FONT_SCALE - UiScaleUtil.MIN_FONT_SCALE) * 100f));
            fontValue.setText("100%");
            fontInfo.setText("Current: 100% (default 100%)");
        });
        LinearLayout.LayoutParams fontResetLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp(40));
        fontResetLp.topMargin = dp(6);
        root.addView(fontReset, fontResetLp);

        TextView uiScaleTitle = new TextView(this);
        uiScaleTitle.setText("Overall interface scaling");
        uiScaleTitle.setTextColor(getColorCompat(R.color.yh_text));
        uiScaleTitle.setTextSize(14);
        uiScaleTitle.setTypeface(null, android.graphics.Typeface.BOLD);
        root.addView(uiScaleTitle);

        float savedUiScale = prefs == null ? UiScaleUtil.DEFAULT_UI_SCALE : prefs.getFloat(UiScaleUtil.KEY_UI_SCALE, UiScaleUtil.DEFAULT_UI_SCALE);
        TextView uiScaleInfo = new TextView(this);
        uiScaleInfo.setText("Current:" + UiScaleUtil.uiScalePercent(savedUiScale) + "% (default 100%) · Tablet recommended 120-150%");
        uiScaleInfo.setTextColor(getColorCompat(R.color.yh_text_muted));
        uiScaleInfo.setTextSize(11);
        uiScaleInfo.setPadding(0, dp(4), 0, dp(6));
        root.addView(uiScaleInfo);

        LinearLayout uiScaleRow = new LinearLayout(this);
        uiScaleRow.setOrientation(LinearLayout.HORIZONTAL);
        uiScaleRow.setGravity(android.view.Gravity.CENTER_VERTICAL);
        SeekBar uiScaleSeek = new SeekBar(this);
        uiScaleSeek.setMax((int) ((UiScaleUtil.MAX_UI_SCALE - UiScaleUtil.MIN_UI_SCALE) * 100f));
        uiScaleSeek.setProgress(Math.round((savedUiScale - UiScaleUtil.MIN_UI_SCALE) * 100f));
        LinearLayout.LayoutParams uiScaleSeekLp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        uiScaleRow.addView(uiScaleSeek, uiScaleSeekLp);
        TextView uiScaleValue = new TextView(this);
        uiScaleValue.setText(UiScaleUtil.uiScalePercent(savedUiScale) + "%");
        uiScaleValue.setTextColor(getColorCompat(R.color.yh_text));
        uiScaleValue.setTextSize(15);
        uiScaleValue.setTypeface(null, android.graphics.Typeface.BOLD);
        uiScaleValue.setPadding(dp(10), 0, 0, 0);
        uiScaleRow.addView(uiScaleValue);
        root.addView(uiScaleRow);
        uiScaleSeek.setOnSeekBarChangeListener(new android.widget.SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float scale = UiScaleUtil.clampUiScale(UiScaleUtil.MIN_UI_SCALE + progress / 100f);
                uiScaleValue.setText(UiScaleUtil.uiScalePercent(scale) + "%");
                uiScaleInfo.setText("Current:" + UiScaleUtil.uiScalePercent(scale) + "% (default 100%) · Tablet recommended 120-150%");
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override public void onStopTrackingTouch(SeekBar seekBar) { }
        });
        Button uiScaleReset = krButton("Restore default zoom");
        uiScaleReset.setTextColor(getColorCompat(R.color.yh_text));
        uiScaleReset.setOnClickListener(v -> {
            uiScaleSeek.setProgress(Math.round((UiScaleUtil.DEFAULT_UI_SCALE - UiScaleUtil.MIN_UI_SCALE) * 100f));
            uiScaleValue.setText("100%");
            uiScaleInfo.setText("Current: 100% (default 100%) · Tablet recommended 120-150%");
        });
        LinearLayout.LayoutParams uiScaleResetLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp(40));
        uiScaleResetLp.topMargin = dp(6);
        root.addView(uiScaleReset, uiScaleResetLp);

        CheckBox autoScanCheck = krCheckBox("Automatically scan the last directory when entering the application", prefs == null || prefs.getBoolean(KEY_AUTO_SCAN_ON_STARTUP, true));
        root.addView(autoScanCheck);

        TextView sortTitle = new TextView(this);
        sortTitle.setText("Game library sorting");
        sortTitle.setTextColor(getColorCompat(R.color.yh_text));
        sortTitle.setTextSize(14);
        sortTitle.setTypeface(null, android.graphics.Typeface.BOLD);
        root.addView(sortTitle);

        TextView sortInfo = new TextView(this);
        sortInfo.setText("Sort by recent play by default, and favorites will always be on top.");
        sortInfo.setTextColor(getColorCompat(R.color.yh_text_muted));
        sortInfo.setTextSize(11);
        sortInfo.setPadding(0, dp(4), 0, dp(6));
        root.addView(sortInfo);

        Spinner sortSpinner = new TapSafeSpinner(this);
        ArrayAdapter<String> sortAdapter = krSpinnerAdapter(new String[]{"Recently visited", "Recently added", "Name sort"});
        sortSpinner.setAdapter(sortAdapter);
        String savedSortMode = prefs == null ? SORT_MODE_RECENT : prefs.getString(KEY_SORT_MODE, SORT_MODE_RECENT);
        if (SORT_MODE_NEWEST.equals(savedSortMode)) sortSpinner.setSelection(1);
        else if (SORT_MODE_NAME.equals(savedSortMode)) sortSpinner.setSelection(2);
        else sortSpinner.setSelection(0);
        root.addView(sortSpinner, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp(44)));

        TextView personalizationTitle = new TextView(this);
        personalizationTitle.setText("Personalization features");
        personalizationTitle.setTextColor(getColorCompat(R.color.yh_text));
        personalizationTitle.setTextSize(14);
        personalizationTitle.setTypeface(null, android.graphics.Typeface.BOLD);
        root.addView(personalizationTitle);

        TextView columnsTitle = new TextView(this);
        columnsTitle.setText("Number of games per row");
        columnsTitle.setTextColor(getColorCompat(R.color.yh_text_muted));
        columnsTitle.setTextSize(11);
        columnsTitle.setPadding(0, dp(4), 0, dp(6));
        root.addView(columnsTitle);

        int savedColumns = prefs == null ? DEFAULT_GAME_COLUMNS : prefs.getInt(KEY_GAME_COLUMNS, DEFAULT_GAME_COLUMNS);
        savedColumns = Math.max(2, Math.min(10, savedColumns));
        TextView columnsInfo = new TextView(this);
        columnsInfo.setText("Current:" + savedColumns + "(default 5)");
        columnsInfo.setTextColor(getColorCompat(R.color.yh_text_muted));
        columnsInfo.setTextSize(11);
        columnsInfo.setPadding(0, dp(4), 0, dp(6));
        root.addView(columnsInfo);

        LinearLayout columnsRow = new LinearLayout(this);
        columnsRow.setOrientation(LinearLayout.HORIZONTAL);
        columnsRow.setGravity(android.view.Gravity.CENTER_VERTICAL);
        SeekBar columnsSeek = new SeekBar(this);
        columnsSeek.setMax(8); // 2-10: 0-8
        columnsSeek.setProgress(savedColumns - 2);
        LinearLayout.LayoutParams columnsSeekLp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        columnsRow.addView(columnsSeek, columnsSeekLp);
        TextView columnsValue = new TextView(this);
        columnsValue.setText(String.valueOf(savedColumns));
        columnsValue.setTextColor(getColorCompat(R.color.yh_text));
        columnsValue.setTextSize(15);
        columnsValue.setTypeface(null, android.graphics.Typeface.BOLD);
        columnsValue.setPadding(dp(10), 0, 0, 0);
        columnsRow.addView(columnsValue);
        root.addView(columnsRow);
        columnsSeek.setOnSeekBarChangeListener(new android.widget.SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int cols = Math.max(2, Math.min(10, progress + 2));
                columnsValue.setText(String.valueOf(cols));
                columnsInfo.setText("Current:" + cols + "(default 5)");
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        TextView engineLabelTitle = new TextView(this);
        engineLabelTitle.setText("Game engine tag location");
        engineLabelTitle.setTextColor(getColorCompat(R.color.yh_text_muted));
        engineLabelTitle.setTextSize(11);
        engineLabelTitle.setPadding(0, dp(4), 0, dp(6));
        root.addView(engineLabelTitle);

        Spinner engineLabelSpinner = new TapSafeSpinner(this);
        ArrayAdapter<String> engineLabelAdapter = krSpinnerAdapter(new String[]{"Below the game title", "Cover lower left corner"});
        engineLabelSpinner.setAdapter(engineLabelAdapter);
        String engineLabelPos = prefs == null ? "title" : prefs.getString(KEY_ENGINE_LABEL_POSITION, "title");
        engineLabelSpinner.setSelection("cover".equals(engineLabelPos) ? 1 : 0);
        root.addView(engineLabelSpinner, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp(44)));

        CheckBox uiClickSoundCheck = krCheckBox("Interface click sound effect", prefs == null || prefs.getBoolean(KEY_UI_CLICK_SOUND, true));
        root.addView(uiClickSoundCheck);

        CheckBox nsfwBlurCheck = krCheckBox("🔞 NSFW cover blurry", prefs == null || prefs.getBoolean(KEY_NSFW_BLUR, true));
        root.addView(nsfwBlurCheck);

        TextView accountTitle = new TextView(this);
accountTitle.setText("Accounts and sync");
accountTitle.setTextColor(getColorCompat(R.color.yh_text));
accountTitle.setTextSize(14);
accountTitle.setTypeface(null, android.graphics.Typeface.BOLD);
root.addView(accountTitle);
TextView accountInfo = new TextView(this);
accountInfo.setText(isLoggedIn()
        ? ("Current:" + displayProfileName() + " · " + accountStatusLabelForDialog())
        : "Current: Local account, not logged in to the cloud account");
accountInfo.setTextColor(getColorCompat(R.color.yh_text_muted));
accountInfo.setTextSize(11);
accountInfo.setPadding(0, dp(4), 0, dp(6));
root.addView(accountInfo);
LinearLayout accountActions = new LinearLayout(this);
        accountActions.setOrientation(LinearLayout.HORIZONTAL);
        Button accountButton = krButton(isLoggedIn() ? "Account settings" : "Login/Register");
        Button webdavButton = krButton("Sync center");
        accountButton.setTextColor(primaryTextColor());
        webdavButton.setTextColor(primaryTextColor());
        accountButton.setOnClickListener(v -> showAuthPlaceholderDialog());
        webdavButton.setOnClickListener(v -> showWebDavSettingsDialog());
        accountActions.addView(accountButton, new LinearLayout.LayoutParams(0, dp(40), 1));
        LinearLayout.LayoutParams webdavLp = new LinearLayout.LayoutParams(0, dp(40), 1);
        webdavLp.setMargins(dp(8), 0, 0, 0);
        accountActions.addView(webdavButton, webdavLp);
        root.addView(accountActions);

        // ===== 从其他平台导入 =====
        TextView importTitle = new TextView(this);
        importTitle.setText("Data migration");
        importTitle.setTextColor(getColorCompat(R.color.yh_text));
        importTitle.setTextSize(14);
        importTitle.setTypeface(null, android.graphics.Typeface.BOLD);
        root.addView(importTitle);

        TextView importInfo = new TextView(this);
        importInfo.setText("Import game lists, covers and play history from Playnite, PotatoVN, Vnite, LunaBox.");
        importInfo.setTextColor(getColorCompat(R.color.yh_text_muted));
        importInfo.setTextSize(11);
        importInfo.setPadding(0, dp(4), 0, dp(6));
        root.addView(importInfo);

        Button externalImportBtn = krButton("Import from other platforms");
        externalImportBtn.setTextColor(primaryTextColor());
        externalImportBtn.setOnClickListener(v -> showExternalImportDialog());
        root.addView(externalImportBtn, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp(40)));

        // ===== 危险操作 =====
        TextView dangerTitle = new TextView(this);
        dangerTitle.setText("Dangerous operation");
        dangerTitle.setTextColor(getColorCompat(R.color.yh_secondary));
        dangerTitle.setTextSize(14);
        dangerTitle.setTypeface(null, android.graphics.Typeface.BOLD);
        root.addView(dangerTitle);

        TextView dangerInfo = new TextView(this);
        dangerInfo.setText("Clearing the game library will only delete game records and play time, but not the main body files. Operations cannot be undone.");
        dangerInfo.setTextColor(getColorCompat(R.color.yh_text_muted));
        dangerInfo.setTextSize(11);
        dangerInfo.setPadding(0, dp(4), 0, dp(6));
        root.addView(dangerInfo);

        Button clearAllBtn = krButton("Clear game library");
        clearAllBtn.setTextColor(getColorCompat(R.color.yh_secondary));
        clearAllBtn.setOnClickListener(v -> confirmClearAllGames());
        root.addView(clearAllBtn, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp(40)));

        TextView disclaimerTitle = new TextView(this);
        disclaimerTitle.setText("Instructions for use and disclaimer");
        disclaimerTitle.setTextColor(getColorCompat(R.color.yh_text));
        disclaimerTitle.setTextSize(14);
        disclaimerTitle.setTypeface(null, android.graphics.Typeface.BOLD);
        root.addView(disclaimerTitle);
        TextView disclaimerInfo = new TextView(this);
        disclaimerInfo.setText("This application is an open source project designed to help users manage and launch game/application resources that they have permissions for." +
                "Users need to confirm the legality and availability of added content, accounts, third-party logins (NextMoe·Wei Moe / Hikarinagi), synchronization services and third-party components." +
                "The program does not provide any game resources, cracked resources or the ability to bypass authorization; third-party services/applications such as Shizuku, GameHub, WebDAV, VNDB, Bangumi, Moon Screen Gal, Hikarinagi, KIRIKIRI2, Tyranno, Winlator, etc. are determined by their respective rules and availability." +
                "If you do not agree with the above content, please do not continue to use related functions." );
        disclaimerInfo.setTextColor(getColorCompat(R.color.yh_text_muted));
        disclaimerInfo.setTextSize(11);
        disclaimerInfo.setLineSpacing(dp(2), 1.0f);
        disclaimerInfo.setPadding(0, dp(4), 0, dp(6));
        root.addView(disclaimerInfo);
        Button disclaimerButton = krButton("View full disclaimer");
        disclaimerButton.setTextColor(primaryTextColor());
        disclaimerButton.setOnClickListener(v -> showDisclaimerDialog());
        root.addView(disclaimerButton, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp(40)));

        TextView aboutTitle = new TextView(this);
        aboutTitle.setText("About Us");
        aboutTitle.setTextColor(getColorCompat(R.color.yh_text));
        aboutTitle.setTextSize(14);
        aboutTitle.setTypeface(null, android.graphics.Typeface.BOLD);
        root.addView(aboutTitle);

        TextView aboutInfo = new TextView(this);
        aboutInfo.setText("Here you can find the project homepage, official website and communication group, where you can easily check updates, report problems and get help.");
        aboutInfo.setTextColor(getColorCompat(R.color.yh_text_muted));
        aboutInfo.setTextSize(11);
        aboutInfo.setLineSpacing(dp(2), 1.0f);
        aboutInfo.setPadding(0, dp(4), 0, dp(6));
        root.addView(aboutInfo);

        Button updateButton = krButton("Check for updates");
        updateButton.setTextColor(primaryTextColor());
        updateButton.setOnClickListener(v -> checkUpdateManually());
        CheckBox updateOnStartupCheck = krCheckBox("Automatically check for updates on startup", prefs == null || prefs.getBoolean(KEY_CHECK_UPDATE_ON_STARTUP, true));
        root.addView(updateButton, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp(40)));
        root.addView(updateOnStartupCheck);

        TextView updateSourceTitle = new TextView(this);
        updateSourceTitle.setText("Update source (single choice, switch takes effect immediately)");
        updateSourceTitle.setTextColor(getColorCompat(R.color.yh_text));
        updateSourceTitle.setTextSize(12);
        updateSourceTitle.setPadding(0, dp(8), 0, dp(4));
        root.addView(updateSourceTitle);
        android.widget.RadioGroup updateSourceGroup = new android.widget.RadioGroup(this);
        updateSourceGroup.setOrientation(LinearLayout.HORIZONTAL);
        android.widget.RadioButton gitcodeSource = new android.widget.RadioButton(this);
        gitcodeSource.setId(View.generateViewId());
        gitcodeSource.setText("GitCode (default)");
        gitcodeSource.setTextColor(getColorCompat(R.color.yh_text));
        gitcodeSource.setTextSize(12);
        android.widget.RadioButton githubSource = new android.widget.RadioButton(this);
        githubSource.setId(View.generateViewId());
        githubSource.setText("GitHub");
        githubSource.setTextColor(getColorCompat(R.color.yh_text));
        githubSource.setTextSize(12);
        DynamicTheme updateTheme = DynamicTheme.getInstance();
        int updateAccent = updateTheme.isEnabled() && updateTheme.getColors() != null
                ? updateTheme.getColors().primary : getColorCompat(R.color.yh_primary);
        android.content.res.ColorStateList updateTint = new android.content.res.ColorStateList(
                new int[][]{new int[]{android.R.attr.state_checked}, new int[]{}},
                new int[]{updateAccent, getColorCompat(R.color.yh_text_muted)});
        gitcodeSource.setButtonTintList(updateTint);
        githubSource.setButtonTintList(updateTint);
        updateSourceGroup.addView(gitcodeSource, new android.widget.RadioGroup.LayoutParams(0, dp(44), 1f));
        updateSourceGroup.addView(githubSource, new android.widget.RadioGroup.LayoutParams(0, dp(44), 1f));
        updateSourceGroup.check(UPDATE_SOURCE_GITHUB.equals(updateSource()) ? githubSource.getId() : gitcodeSource.getId());
        updateSourceGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == -1 || prefs == null) { return; }
            String selected = checkedId == githubSource.getId() ? UPDATE_SOURCE_GITHUB : UPDATE_SOURCE_GITCODE;
            if (!selected.equals(updateSource())) {
                // 换源后不沿用另一源的 12 小时自动检查限流时间。
                prefs.edit().putString(KEY_UPDATE_SOURCE, selected).remove(KEY_LAST_UPDATE_CHECK_AT).apply();
            }
        });
        root.addView(updateSourceGroup, new LinearLayout.LayoutParams(-1, -2));

        LinearLayout githubButton = linkCardButton("GitHub repository", R.drawable.ic_github);
        LinearLayout gitcodeButton = linkCardButton("GitCode repository", R.drawable.ic_gitcode);
        LinearLayout websiteButton = linkCardButton("Official website", android.R.drawable.ic_menu_view);
        LinearLayout groupButton = linkCardButton("QQ communication group", android.R.drawable.ic_dialog_email);
        githubButton.setOnClickListener(v -> openExternalUrl("https://github.com/xm486/YukiHub"));
        gitcodeButton.setOnClickListener(v -> openExternalUrl("https://gitcode.com/xm486/YukiHub"));
        websiteButton.setOnClickListener(v -> openExternalUrl("https://yukihub.kesug.com/"));
        groupButton.setOnClickListener(v -> openExternalUrl("https://qun.qq.com/universal-share/share?ac=1&authKey=nZMa0s3mxxG1A0f%2BY0nAWmBYpul7FWTEDI6UWrzqb2IgKC4aDkUhvkV2AekAkW%2F1&busi_data=eyJncm91cENvZGUiOiIxNjM2MDM2MzUiLCJ0b2tlbiI6Im93eFRyY0tqNDdxK3FGQXlVZ0lhMEZGbWZWemphZnpYYW1kWWpPN1ViL3A0SkRUd1dEclMwZkM1bWI0UEYxME4iLCJ1aW4iOiIzMDg2Njc4NzU1In0%3D&data=bwoLG7XAPzqsvtfneNCQUUlu-HpX1yCn-6dkgd8ubDeBJKEPgd7wKYa6ym-EbW07Vapc3xm_o-iy0GbFHhZk5Q&svctype=4&tempid=h5_group_info"));
        root.addView(githubButton, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp(48)));
        LinearLayout.LayoutParams gitcodeLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp(48));
        gitcodeLp.topMargin = dp(8);
        root.addView(gitcodeButton, gitcodeLp);
        LinearLayout.LayoutParams websiteLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp(48));
        websiteLp.topMargin = dp(8);
        root.addView(websiteButton, websiteLp);
        LinearLayout.LayoutParams groupLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp(48));
        groupLp.topMargin = dp(8);
        root.addView(groupButton, groupLp);

        TextView sourceTitle = new TextView(this);
        sourceTitle.setText("Data source on the right");
        sourceTitle.setTextColor(getColorCompat(R.color.yh_text));
        sourceTitle.setTextSize(14);
        sourceTitle.setTypeface(null, android.graphics.Typeface.BOLD);
        root.addView(sourceTitle);

        Spinner sourceSpinner = new TapSafeSpinner(this);
        // 下拉项：NextMoe（默认）/ VNDB / Bangumi / 月幕Gal / Hikarinagi
        // （原「Bangumi 镜像」项已移除：镜像站长期不稳定，且已有 NextMoe 源替代）
        // NextMoe 作默认源：目录只读面匿名可调（不必登录），且是六源对齐数据。
        ArrayAdapter<String> sourceAdapter = krSpinnerAdapter(new String[]{"NextMoe (recommended · default)", "VNDB", "Bangumi (requires Token)", "Moon Curtain Gal (Public API)", "Hikarinagi (public API)"});
        sourceSpinner.setAdapter(sourceAdapter);
        String currentSource = metadataSource();
        if (MetadataController.SOURCE_VNDB.equals(currentSource)) sourceSpinner.setSelection(1);
        else if (MetadataController.SOURCE_BANGUMI.equals(currentSource)) sourceSpinner.setSelection(2);
        else if (MetadataController.SOURCE_YMGAL.equals(currentSource)) sourceSpinner.setSelection(3);
        else if (MetadataController.SOURCE_HIKARINAGI.equals(currentSource)) sourceSpinner.setSelection(4);
        else sourceSpinner.setSelection(0); // NextMoe（默认）
        root.addView(sourceSpinner, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp(44)));

        // NextMoe 登录入口（可选）：环回回调 + PKCE，令牌走 Android Keystore 加密存储。
        // 未登录也能正常用 NextMoe 源（匿名档，按 IP 计配额），登录只是为了提升刮削配额。
        TextView nextmoeStatus = new TextView(this);
        boolean nextmoeConnected = com.yuki.yukihub.nextmoe.NextMoeAuthStore.isConnected();
        nextmoeStatus.setText("NextMoe account:" + (nextmoeConnected
                ? "Connected" + (com.yuki.yukihub.nextmoe.NextMoeAuthStore.getAccountLabel().isEmpty() ? "" : "（" + com.yuki.yukihub.nextmoe.NextMoeAuthStore.getAccountLabel() + "）") + "· Scraping quota has been increased"
                : "Not logged in (optional) · Log in to increase scraping quota"));
        nextmoeStatus.setTextColor(getColorCompat(R.color.yh_text_muted));
        nextmoeStatus.setTextSize(11);
        nextmoeStatus.setPadding(0, dp(8), 0, dp(2));
        root.addView(nextmoeStatus);

        Button nextmoeButton = krButton(nextmoeConnected ? "Disconnect NextMoe account" : "Log in to NextMoe (optional · increase quota)");
        nextmoeButton.setTextColor(primaryTextColor());
        nextmoeButton.setOnClickListener(v -> {
            if (com.yuki.yukihub.nextmoe.NextMoeAuthStore.isConnected()) {
                com.yuki.yukihub.nextmoe.NextMoeAuthStore.clear();
                nextmoeStatus.setText("NextMoe account: Not logged in (optional) · Log in to increase scraping quota");
                nextmoeButton.setText("Log in to NextMoe (optional · increase quota)");
                Toast.makeText(this, "NextMoe account has been disconnected", Toast.LENGTH_SHORT).show();
                return;
            }
            nextmoeButton.setEnabled(false);
            nextmoeButton.setText("Waiting for authorization…");
            new Thread(() -> {
                String err = null;
                try {
                    err = com.yuki.yukihub.nextmoe.NextMoeConnectFlow.connectAndWait();
                } catch (Throwable t) {
                    err = t.getMessage() == null ? "Abnormal authorization process" : t.getMessage();
                }
                final String error = err;
                runOnUiThread(() -> {
                    nextmoeButton.setEnabled(true);
                    boolean ok = error == null && com.yuki.yukihub.nextmoe.NextMoeAuthStore.isConnected();
                    nextmoeButton.setText(ok ? "Disconnect NextMoe account" : "Log in to NextMoe (optional · increase quota)");
                    nextmoeStatus.setText(ok
                            ? "NextMoe account: Connected" + (com.yuki.yukihub.nextmoe.NextMoeAuthStore.getAccountLabel().isEmpty() ? "" : "（" + com.yuki.yukihub.nextmoe.NextMoeAuthStore.getAccountLabel() + "）") + "· Scraping quota has been increased"
                            : "NextMoe Account: Not logged in (optional) · Anonymous quota available" + (error == null ? "" : "（" + error + "）"));
                    if (ok) Toast.makeText(this, "NextMoe logged in successfully, scraping quota has been increased", Toast.LENGTH_SHORT).show();
                });
            }, "yukihub-nextmoe-auth").start();
        });
        root.addView(nextmoeButton, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp(44)));

        TextView nextmoeHint = new TextView(this);
        nextmoeHint.setText("Login is optional: You can use NextMoe feed normally without logging in (anonymous quota, calculated according to your network egress IP)."
                + "After logging in, the scraping quota can be increased (according to the account, not occupied by others with the same IP), and used for subsequent account-related functions."
                + "Authorization will open the system browser and jump to the end, and automatically return to this page after completion; the token is encrypted and stored in the device, and will be cleared when disconnected.");
        nextmoeHint.setTextColor(getColorCompat(R.color.yh_text_muted));
        nextmoeHint.setTextSize(10);
        nextmoeHint.setPadding(0, dp(4), 0, dp(4));
        root.addView(nextmoeHint);

        TextView tokenLabel = new TextView(this);
        tokenLabel.setText("Bangumi Access Token");
        tokenLabel.setTextColor(getColorCompat(R.color.yh_text));
        tokenLabel.setTextSize(13);
        tokenLabel.setPadding(0, dp(10), 0, dp(4));
        root.addView(tokenLabel);

        EditText tokenInput = new EditText(this);
        tokenInput.setSingleLine(true);
        tokenInput.setText(bangumiToken());
        tokenInput.setHint("Required when selecting Bangumi");
        tokenInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        tokenInput.setTextColor(getColorCompat(R.color.yh_text));
        tokenInput.setHintTextColor(getColorCompat(R.color.yh_text_muted));
        tokenInput.setBackgroundResource(R.drawable.bg_input);
        tokenInput.setPadding(dp(10), 0, dp(10), 0);
        root.addView(tokenInput, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp(44)));

        // 一键重扫全部游戏资料（使用当前选择的资料源，串行限速防第三方限流）
        Button bulkRefreshButton = krButton("↻ Re-scan all game data with one click");
        LinearLayout.LayoutParams bulkRefreshLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp(44));
        bulkRefreshLp.topMargin = dp(8);
        root.addView(bulkRefreshButton, bulkRefreshLp);

        // 重扫间隔设置行（秒/款，0.5~15s）
        LinearLayout intervalRow = new LinearLayout(this);
        intervalRow.setOrientation(LinearLayout.HORIZONTAL);
        intervalRow.setGravity(android.view.Gravity.CENTER_VERTICAL);
        intervalRow.setPadding(dp(4), dp(6), dp(4), 0);
        TextView intervalLabel = new TextView(this);
        intervalLabel.setText("Rescan interval");
        intervalLabel.setTextColor(getColorCompat(R.color.yh_text));
        intervalLabel.setTextSize(13);
        intervalRow.addView(intervalLabel, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        EditText intervalInput = new EditText(this);
        intervalInput.setSingleLine(true);
        intervalInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        intervalInput.setText(String.valueOf(prefs.getFloat(MetadataController.KEY_BULK_REFRESH_INTERVAL_SEC, 2.5f)));
        intervalInput.setTextColor(getColorCompat(R.color.yh_text));
        intervalInput.setHintTextColor(getColorCompat(R.color.yh_text_muted));
        intervalInput.setBackgroundResource(R.drawable.bg_input);
        intervalInput.setGravity(android.view.Gravity.CENTER);
        intervalInput.setPadding(dp(8), 0, dp(8), 0);
        LinearLayout.LayoutParams intervalInputLp = new LinearLayout.LayoutParams(dp(72), dp(36));
        intervalRow.addView(intervalInput, intervalInputLp);
        TextView intervalUnit = new TextView(this);
        intervalUnit.setText("Second/style (0.5~15)");
        intervalUnit.setTextColor(getColorCompat(R.color.yh_text_muted));
        intervalUnit.setTextSize(12);
        intervalUnit.setPadding(dp(6), 0, 0, 0);
        intervalRow.addView(intervalUnit);
        root.addView(intervalRow);

        bulkRefreshButton.setOnClickListener(v -> {
            saveBulkRefreshInterval(intervalInput);
            showBulkMetadataRefreshDialog();
        });

        TextView warn = new TextView(this);
        warn.setText("Reminder: Bangumi API Token is recommended to be applied with an account registered for more than three months. Tsukimu Gal and Hikarinagi use public APIs and do not require a token. After switching data sources, caches of other existing sources will continue to be displayed; for the current game, you can click \"Rematch\" to refresh, or click \"One-click to rescan all game data\" above to refresh the current source data in batches.");
        warn.setTextColor(getColorCompat(R.color.yh_warning));
        warn.setTextSize(11);
        warn.setPadding(0, dp(8), 0, 0);
        root.addView(warn);

        TextView tokenLink = new TextView(this);
        tokenLink.setText("No token?");
        tokenLink.setTextColor(primaryTextColor());
        tokenLink.setTextSize(12);
        tokenLink.setTypeface(null, android.graphics.Typeface.BOLD);
        tokenLink.setPadding(0, dp(8), 0, dp(4));
        tokenLink.setOnClickListener(v -> {
            try { startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://next.bgm.tv/demo/access-token/create"))); }
            catch (Throwable t) { Toast.makeText(MainActivity.this, "Unable to open link", Toast.LENGTH_SHORT).show(); }
        });
        root.addView(tokenLink);

        TextView bgTitle = new TextView(this);
        bgTitle.setText("Interface background");
        bgTitle.setTextColor(getColorCompat(R.color.yh_text));
        bgTitle.setTextSize(14);
        bgTitle.setTypeface(null, android.graphics.Typeface.BOLD);
        root.addView(bgTitle);

        TextView bgInfo = new TextView(this);
        String customBg = prefs.getString(KEY_CUSTOM_BACKGROUND, "");
        String customBgType = prefs.getString(KEY_CUSTOM_BACKGROUND_TYPE, "image");
        bgInfo.setText(customBg == null || customBg.isEmpty() ? "Current: Default dynamic background" : ("video".equals(customBgType) ? "Current: Custom video background" : "Current: Custom image background"));
        bgInfo.setTextColor(getColorCompat(R.color.yh_text_muted));
        bgInfo.setTextSize(11);
        bgInfo.setPadding(0, dp(4), 0, dp(6));
        root.addView(bgInfo);

        LinearLayout bgActions = new LinearLayout(this);
        bgActions.setOrientation(LinearLayout.HORIZONTAL);
        Button chooseBgButton = krButton("Picture background");
        Button chooseVideoBgButton = krButton("Video background");
        Button resetBgButton = krButton("Restore default");
        chooseBgButton.setTextColor(primaryTextColor());
        chooseVideoBgButton.setTextColor(primaryTextColor());
        resetBgButton.setTextColor(getColorCompat(R.color.yh_text));
        bgActions.addView(chooseBgButton, new LinearLayout.LayoutParams(0, dp(40), 1));
        LinearLayout.LayoutParams videoBgLp = new LinearLayout.LayoutParams(0, dp(40), 1);
        videoBgLp.setMargins(dp(6), 0, 0, 0);
        bgActions.addView(chooseVideoBgButton, videoBgLp);
        LinearLayout.LayoutParams resetBgLp = new LinearLayout.LayoutParams(0, dp(40), 1);
        resetBgLp.setMargins(dp(6), 0, 0, 0);
        bgActions.addView(resetBgButton, resetBgLp);
        root.addView(bgActions);

        CheckBox bgDimEnabled = krCheckBox("Background masking (improves text readability)", prefs.getBoolean(KEY_BACKGROUND_DIM_ENABLED, true));
        CheckBox bgVideoSound = krCheckBox("Video background sound", prefs.getBoolean(KEY_BACKGROUND_VIDEO_SOUND, false));
        CheckBox bgThemeEnabled = krCheckBox("Background UI color picking (extracting hue from background)", prefs.getBoolean(KEY_BG_THEME_ENABLED, false));
        root.addView(bgDimEnabled);
        root.addView(bgVideoSound);
        root.addView(bgThemeEnabled);

        // 自定义颜色选项
        CheckBox customColorEnabled = krCheckBox("Custom theme colors", prefs.getBoolean(DynamicTheme.KEY_CUSTOM_COLOR_ENABLED, false));
        root.addView(customColorEnabled);
        
        // 颜色选择按钮和预览
        LinearLayout colorPickerRow = new LinearLayout(this);
        colorPickerRow.setOrientation(LinearLayout.HORIZONTAL);
        colorPickerRow.setGravity(android.view.Gravity.CENTER_VERTICAL);
        colorPickerRow.setPadding(0, dp(8), 0, dp(8));
        
        // 颜色预览
        View colorPreview = new View(this);
        colorPreview.setBackgroundColor(prefs.getInt(DynamicTheme.KEY_CUSTOM_COLOR_PRIMARY, 0xFF8AB4FF));
        LinearLayout.LayoutParams previewLp = new LinearLayout.LayoutParams(dp(32), dp(32));
        previewLp.setMarginEnd(dp(12));
        colorPreview.setLayoutParams(previewLp);
        colorPickerRow.addView(colorPreview);
        
        // 选择颜色按钮
        Button chooseColorBtn = krButton("Choose theme color");
        chooseColorBtn.setOnClickListener(v -> {
            customColorEnabled.setChecked(true);
            showColorPickerDialog(colorPreview, customColorEnabled);
        });
        colorPickerRow.addView(chooseColorBtn, new LinearLayout.LayoutParams(0, dp(40), 1));
        
        // 恢复默认值按钮
        Button resetColorBtn = krButton("Restore default");
        resetColorBtn.setOnClickListener(v -> {
            DynamicTheme dt = DynamicTheme.getInstance();
            dt.setCustomColorEnabled(false);
            dt.setCustomColorPrimary(0xFF8AB4FF);
            dt.setCustomColorSecondary(0xFFFF8AB3);
            dt.setCustomGradientAngle(0f);
            dt.saveCustomColorSettings(this);
            customColorEnabled.setChecked(false);
            colorPreview.setBackgroundColor(0xFF8AB4FF);
            applyDynamicTheme(null);
            Toast.makeText(MainActivity.this, "Default theme colors restored", Toast.LENGTH_SHORT).show();
        });
        LinearLayout.LayoutParams resetLp = new LinearLayout.LayoutParams(dp(80), dp(40));
        resetLp.setMarginStart(dp(8));
        colorPickerRow.addView(resetColorBtn, resetLp);
        
        root.addView(colorPickerRow);

        // 文件选择器选项
        CheckBox useBuiltinFileChooser = krCheckBox("Use the built-in file picker (avoid permission issues)", prefs.getBoolean(KEY_USE_BUILTIN_FILE_CHOOSER, true));
        root.addView(useBuiltinFileChooser);

        TextView krTitle = new TextView(this);
        krTitle.setText("KRKR engine");
        krTitle.setTextColor(getColorCompat(R.color.yh_text));
        krTitle.setTextSize(14);
        krTitle.setTypeface(null, android.graphics.Typeface.BOLD);
        root.addView(krTitle);

        TextView krInfo = new TextView(this);
        krInfo.setText("If the engine crashes or crashes due to storage permissions on some Huawei and other models, you can open an independent archive directory for the corresponding engine.");
        krInfo.setTextColor(getColorCompat(R.color.yh_text_muted));
        krInfo.setTextSize(11);
        krInfo.setPadding(0, dp(4), 0, dp(6));
        root.addView(krInfo);

        root.addView(krLabel("KR engine version"));
        Spinner krEngineVersion = krSpinner(new String[]{"Auto", "1.3.9", "1.3.4"}, krEngineVersionToLabel(prefs.getString(KEY_KR_ENGINE_VERSION, "auto")));
        root.addView(krEngineVersion);

        CheckBox krCompatMode = krCheckBox("KR startup parameter compatibility mode", prefs.getBoolean(KEY_KR_COMPAT_MODE, false));
        CheckBox krScopedSaveDir = krCheckBox("KR independent archive directory (opened when permissions fail abnormally)", prefs.getBoolean(KEY_KR_SCOPED_SAVE_DIR, false));
        CheckBox artemisScopedSaveDir = krCheckBox("Artemis independent archive directory (opened when permissions fail abnormally)", prefs.getBoolean(KEY_ARTEMIS_SCOPED_SAVE_DIR, false));
        CheckBox krDrawCutout = krCheckBox("KRKR allows drawing bangs/cutout areas", prefs.getBoolean(KEY_KR_DRAW_CUTOUT, true));
        CheckBox artemisDrawCutout = krCheckBox("Artemis 允许绘制刘海/挖孔区域", prefs.getBoolean(KEY_ARTEMIS_DRAW_CUTOUT, true));
        root.addView(krCompatMode);
        root.addView(krScopedSaveDir);
        root.addView(artemisScopedSaveDir);
        root.addView(krDrawCutout);
        root.addView(artemisDrawCutout);

        // ===== KRKR 内部存储启动（实验性）=====
        CheckBox krInternalRun = krCheckBox("KRKR internal storage startup (experimental)", prefs.getBoolean(KEY_KR_INTERNAL_RUN, false));
        root.addView(krInternalRun);

        TextView krInternalInfo = new TextView(this);
        krInternalInfo.setText("After turning it on, when starting the KR game, the entire game directory will be copied to the internal storage of the application and then run."
                + "Alleviate the problem of dialogue/fast forward lagging in some new engine games."
                + "· The archive is saved in an internal copy and can be viewed or cleaned at any time using \"Management\""
                + "· Copying is required when starting up for the first time. Please be patient when the game is large."
                + "· Requires additional internal storage space");
        krInternalInfo.setTextColor(getColorCompat(R.color.yh_text_muted));
        krInternalInfo.setTextSize(11);
        krInternalInfo.setPadding(0, dp(4), 0, dp(6));
        root.addView(krInternalInfo);

        TextView krInternalUsage = new TextView(this);
        krInternalUsage.setTextColor(getColorCompat(R.color.yh_text_muted));
        krInternalUsage.setTextSize(11);
        root.addView(krInternalUsage);

        Button krInternalManage = krButton("Manage internal storage");
        krInternalManage.setTextColor(primaryTextColor());
        krInternalManage.setOnClickListener(v -> showKrInternalStoreDialog(krInternalUsage));
        LinearLayout.LayoutParams krInternalLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp(42));
        krInternalLp.setMargins(0, dp(8), 0, dp(4));
        root.addView(krInternalManage, krInternalLp);

        refreshKrInternalUsage(krInternalUsage);

        Button nativeKrkrButton = krButton("Enter native KRKR");
        nativeKrkrButton.setTextColor(primaryTextColor());
        nativeKrkrButton.setOnClickListener(v -> {
            try {
                startActivity(EmulatorLauncher.buildInternalKrkrIntent(this, "", "", true));
            } catch (Throwable t) {
                Toast.makeText(MainActivity.this, "Unable to enter native KRKR", Toast.LENGTH_SHORT).show();
            }
        });
        LinearLayout.LayoutParams nativeKrkrLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp(42));
        nativeKrkrLp.setMargins(0, dp(10), 0, dp(4));
        root.addView(nativeKrkrButton, nativeKrkrLp);

        // === 开发者选项 ===
        TextView devTitle = new TextView(this);
        devTitle.setText("Developer options");
        devTitle.setTextColor(getColorCompat(R.color.yh_text));
        devTitle.setTextSize(14);
        devTitle.setTypeface(null, android.graphics.Typeface.BOLD);
        root.addView(devTitle);

        TextView devInfo = new TextView(this);
        devInfo.setText("After being turned on, the system log (logcat) is automatically captured, including all output such as engine running, WebView, exceptions, etc.\nLog path: Android/data/com.yuki.yukihub/files/logs/");
        devInfo.setTextColor(getColorCompat(R.color.yh_text_muted));
        devInfo.setTextSize(11);
        devInfo.setPadding(0, dp(4), 0, dp(6));
        root.addView(devInfo);

        CheckBox logEnabledCheck = krCheckBox("Enable log collection", DevLogger.isEnabled());
        root.addView(logEnabledCheck);

        LinearLayout logActions = new LinearLayout(this);
        logActions.setOrientation(LinearLayout.HORIZONTAL);
        logActions.setPadding(0, dp(8), 0, 0);

        Button exportLogBtn = krButton("Export log");
        exportLogBtn.setTextColor(primaryTextColor());
        exportLogBtn.setOnClickListener(v -> {
            File logFile = DevLogger.getLogFile();
            if (logFile == null || !logFile.exists()) {
                Toast.makeText(MainActivity.this, "No log file yet", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", logFile));
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "YukiHub Logcat");
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(shareIntent, "Export log"));
            } catch (Throwable t) {
                Toast.makeText(this, "Export failed:" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        logActions.addView(exportLogBtn, new LinearLayout.LayoutParams(0, dp(40), 1));

        Button clearLogBtn = krButton("Clear log");
        clearLogBtn.setTextColor(getColorCompat(R.color.yh_text));
        clearLogBtn.setOnClickListener(v -> {
            if (DevLogger.clearLog()) {
                Toast.makeText(MainActivity.this, "The log has been cleared", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Clearing failed", Toast.LENGTH_SHORT).show();
            }
        });
        LinearLayout.LayoutParams clearLogLp = new LinearLayout.LayoutParams(0, dp(40), 1);
        clearLogLp.setMargins(dp(8), 0, 0, 0);
        logActions.addView(clearLogBtn, clearLogLp);
        root.addView(logActions);

        TextView logPathInfo = new TextView(this);
        String logPath = DevLogger.getLogPath();
        String logSize = DevLogger.formatSize(DevLogger.getLogSize());
        logPathInfo.setText("Current log size:" + logSize + "Path:" + (logPath != null ? logPath : "Not initialized"));
        logPathInfo.setTextColor(getColorCompat(R.color.yh_text_muted));
        logPathInfo.setTextSize(10);
        logPathInfo.setPadding(0, dp(6), 0, dp(4));
        root.addView(logPathInfo);

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(false);
        scroll.setBackgroundResource(R.drawable.bg_dialog);
        tintDialogRoot(scroll);
        scroll.addView(root, new ScrollView.LayoutParams(ScrollView.LayoutParams.MATCH_PARENT, ScrollView.LayoutParams.WRAP_CONTENT));

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Settings")
                .setView(scroll)
                .setPositiveButton("Save", null)
                .setNeutralButton("Add directory", null)
                .setNegativeButton("Close", null)
                .show();
        styleAlertDialogDark(dialog);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.58f), (int) (getResources().getDisplayMetrics().heightPixels * 0.78f));
        }
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            int sourceSelection = sourceSpinner.getSelectedItemPosition();
            // 索引对应下拉项：0=NextMoe(默认) 1=VNDB 2=Bangumi 3=月幕Gal 4=Hikarinagi
            boolean nextmoe = sourceSelection == 0;
            boolean bangumi = sourceSelection == 2;
            boolean ymgal = sourceSelection == 3;
            boolean hikarinagi = sourceSelection == 4;
            String selectedMetadataSource = nextmoe ? MetadataController.SOURCE_NEXTMOE : (hikarinagi ? MetadataController.SOURCE_HIKARINAGI : (ymgal ? MetadataController.SOURCE_YMGAL : (bangumi ? MetadataController.SOURCE_BANGUMI : MetadataController.SOURCE_VNDB)));
            String token = tokenInput.getText() == null ? "" : tokenInput.getText().toString().trim();
            if (bangumi && token.isEmpty()) {
                Toast.makeText(MainActivity.this, "Token is required when selecting Bangumi", Toast.LENGTH_SHORT).show();
                return;
            }
            int depth = Math.max(1, Math.min(MAX_STARTUP_SCAN_DEPTH, scanDepthSeek.getProgress() + 1));
            float fontScale = UiScaleUtil.clamp(UiScaleUtil.MIN_FONT_SCALE + fontSeek.getProgress() / 100f);
            String sortMode = SORT_MODE_RECENT;
            int sortSelection = sortSpinner.getSelectedItemPosition();
            if (sortSelection == 1) sortMode = SORT_MODE_NEWEST;
            else if (sortSelection == 2) sortMode = SORT_MODE_NAME;
            prefs.edit()
                    .putString(MetadataController.KEY_METADATA_SOURCE, selectedMetadataSource)
                    .putString(MetadataController.KEY_BANGUMI_TOKEN, token)
                    .putInt(KEY_STARTUP_SCAN_DEPTH, depth)
                    .putBoolean(KEY_AUTO_SCAN_ON_STARTUP, autoScanCheck.isChecked())
                    .putString(KEY_SCAN_MODE, scanModeSpinner.getSelectedItemPosition() == 1 ? SCAN_MODE_LEGACY : SCAN_MODE_FAST)
                    .putBoolean(KEY_CHECK_UPDATE_ON_STARTUP, updateOnStartupCheck.isChecked())
                    .putString(KEY_ENGINE_LABEL_POSITION, engineLabelSpinner.getSelectedItemPosition() == 1 ? "cover" : "title")
                    .putString(KEY_SORT_MODE, sortMode)
                    .putBoolean(KEY_BACKGROUND_DIM_ENABLED, bgDimEnabled.isChecked())
                    .putBoolean(KEY_BACKGROUND_VIDEO_SOUND, bgVideoSound.isChecked())
                    .putBoolean(KEY_BG_THEME_ENABLED, bgThemeEnabled.isChecked())
                .putBoolean(DynamicTheme.KEY_CUSTOM_COLOR_ENABLED, customColorEnabled.isChecked())
                .putBoolean(KEY_USE_BUILTIN_FILE_CHOOSER, useBuiltinFileChooser.isChecked())
                    .putBoolean(KEY_UI_CLICK_SOUND, uiClickSoundCheck.isChecked())
                    .putBoolean(KEY_NSFW_BLUR, nsfwBlurCheck.isChecked())
                    .putString(KEY_KR_ENGINE_VERSION, krEngineVersionFromLabel(String.valueOf(krEngineVersion.getSelectedItem())))
                    .putBoolean(KEY_KR_COMPAT_MODE, krCompatMode.isChecked())
                    .putBoolean(KEY_KR_SCOPED_SAVE_DIR, krScopedSaveDir.isChecked())
                    .putBoolean(KEY_KR_INTERNAL_RUN, krInternalRun.isChecked())
                    .putBoolean(KEY_ARTEMIS_SCOPED_SAVE_DIR, artemisScopedSaveDir.isChecked())
                    .putBoolean(KEY_KR_DRAW_CUTOUT, krDrawCutout.isChecked())
                    .putBoolean(KEY_ARTEMIS_DRAW_CUTOUT, artemisDrawCutout.isChecked())
                    .putFloat(UiScaleUtil.KEY_UI_FONT_SCALE, fontScale)
                    .putFloat(UiScaleUtil.KEY_UI_SCALE, UiScaleUtil.clampUiScale(UiScaleUtil.MIN_UI_SCALE + uiScaleSeek.getProgress() / 100f))
                    .putInt(KEY_GAME_COLUMNS, Math.max(2, Math.min(10, columnsSeek.getProgress() + 2)))
                    .putBoolean("dev_log_enabled", logEnabledCheck.isChecked())
                    .apply();
                
                // 保存自定义颜色设置
                DynamicTheme dt = DynamicTheme.getInstance();
                dt.setCustomColorEnabled(customColorEnabled.isChecked());
                dt.saveCustomColorSettings(this);
            applyCustomBackground();
            Toast.makeText(MainActivity.this, "Saved source:" + (nextmoe ? "NextMoe" : (hikarinagi ? "Hikarinagi" : (ymgal ? "Moon Curtain Gal" : (bangumi ? "Bangumi" : "VNDB")))) + ", scanning depth:" + depth + "Layer, font:" + UiScaleUtil.percent(fontScale) + "%", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
recreate();
        });
        chooseBgButton.setOnClickListener(v -> {
            dialog.dismiss();
            backgroundPickerLauncher.launch("image/*");
        });
        chooseVideoBgButton.setOnClickListener(v -> {
            dialog.dismiss();
            videoBackgroundPickerLauncher.launch("video/*");
        });
        resetBgButton.setOnClickListener(v -> {
            String oldBg = prefs.getString(KEY_CUSTOM_BACKGROUND, "");
            prefs.edit().remove(KEY_CUSTOM_BACKGROUND).remove(KEY_CUSTOM_BACKGROUND_TYPE).apply();
            DynamicTheme.getInstance().clearCache(prefs);
            deleteInternalFileUri(oldBg);
            applyCustomBackground();
            bgInfo.setText("Current: Default dynamic background");
            Toast.makeText(MainActivity.this, "Default background restored", Toast.LENGTH_SHORT).show();
        });
        dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(v -> {
            if (getScanRootUris().size() >= MAX_SCAN_ROOTS) {
                Toast.makeText(MainActivity.this, "Most bindings" + MAX_SCAN_ROOTS + "Scan directories", Toast.LENGTH_SHORT).show();
                return;
            }
            launchScanRootPicker(-1);
        });
        dialog.setOnDismissListener(d -> {
            activeScanRootList = null;
            activeScanRootInfo = null;
        });
    }
    
    private void showColorPickerDialog(View colorPreview, CheckBox customColorEnabledCheck) {
        DynamicTheme dt = DynamicTheme.getInstance();
        dt.loadCustomColorSettings(prefs);
        final boolean oldCustomEnabled = dt.isCustomColorEnabled();
        final int oldPrimary = dt.getCustomColorPrimary();
        final int oldSecondary = dt.getCustomColorSecondary();
        final float oldAngle = dt.getCustomGradientAngle();
        final boolean[] applied = {false};
        
        ColorPickerDialog colorPickerDialog = new ColorPickerDialog(this);
        colorPickerDialog.setInitialColor(dt.getCustomColorPrimary());
        colorPickerDialog.setInitialGradient(dt.getCustomColorPrimary(), dt.getCustomColorSecondary(), dt.getCustomGradientAngle());
        colorPickerDialog.setOnColorPreviewListener((color1, color2, gradientAngle, gradientMode) -> {
            dt.setCustomColorPrimary(color1);
            dt.setCustomColorSecondary(color2);
            dt.setCustomGradientAngle(gradientAngle);
            dt.setCustomColorMode(gradientMode ? 1 : 0);
            dt.setCustomColorEnabled(true);
            dt.setEnabled(true);
            colorPreview.setBackgroundColor(color1);
            applyDynamicTheme(dt.getColors());
        });
        
        colorPickerDialog.setOnColorSelectedListener((color1, color2, gradientAngle) -> {
            // 更新自定义颜色设置
            dt.setCustomColorPrimary(color1);
            dt.setCustomColorSecondary(color2);
            dt.setCustomGradientAngle(gradientAngle);
            // 使用对话框的实际模式，而不是比较颜色
            dt.setCustomColorMode(colorPickerDialog.getCurrentMode() == ColorPickerDialog.ColorMode.GRADIENT_COLOR ? 1 : 0);
            dt.setCustomColorEnabled(true);
            if (customColorEnabledCheck != null) customColorEnabledCheck.setChecked(true);
            applied[0] = true;
            dt.saveCustomColorSettings(this);
            
            // 更新预览颜色
            colorPreview.setBackgroundColor(color1);
            
            // 应用新的主题颜色（直接应用，不调用 applyCustomBackground 避免重新加载设置）
            applyDynamicTheme(dt.getColors());
            
            Toast.makeText(MainActivity.this, "Custom theme colors applied", Toast.LENGTH_SHORT).show();
        });
        colorPickerDialog.setOnDismissListener(d -> {
            if (!applied[0]) {
                dt.setCustomColorEnabled(oldCustomEnabled);
                if (customColorEnabledCheck != null) customColorEnabledCheck.setChecked(oldCustomEnabled);
                dt.setCustomColorPrimary(oldPrimary);
                dt.setCustomColorSecondary(oldSecondary);
                dt.setCustomGradientAngle(oldAngle);
                if (oldCustomEnabled) {
                    dt.setEnabled(true);
                    applyDynamicTheme(dt.getColors());
                    colorPreview.setBackgroundColor(oldPrimary);
                } else {
                    applyCustomBackground();
                    colorPreview.setBackgroundColor(0xFF8AB4FF);
                }
            }
        });
        
        colorPickerDialog.show();
    }

    private List<String> getScanRootUris() {
List<String> roots = new ArrayList<>();
if (prefs == null) return roots;
String joined = prefs.getString(KEY_SCAN_ROOT_URIS, "");
if (joined != null && !joined.trim().isEmpty()) {
for (String part : joined.split("\\n")) {
String s = part == null ? "" : part.trim();
if (!s.isEmpty() && !roots.contains(s)) roots.add(s);
if (roots.size() >= MAX_SCAN_ROOTS) break;
}
}
String legacy = prefs.getString(KEY_LAST_SCAN_ROOT_URI, "");
if (roots.isEmpty() && legacy != null && !legacy.trim().isEmpty()) roots.add(legacy.trim());
return roots;
}

private void saveScanRootUris(List<String> roots) {
if (prefs == null) return;
List<String> cleaned = new ArrayList<>();
if (roots != null) {
for (String r : roots) {
String s = r == null ? "" : r.trim();
if (!s.isEmpty() && !cleaned.contains(s)) cleaned.add(s);
if (cleaned.size() >= MAX_SCAN_ROOTS) break;
}
}
StringBuilder joined = new StringBuilder();
for (String r : cleaned) {
if (joined.length() > 0) joined.append('\n');
joined.append(r);
}
SharedPreferences.Editor e = prefs.edit().putString(KEY_SCAN_ROOT_URIS, joined.toString());
        if (!cleaned.isEmpty()) e.putString(KEY_LAST_SCAN_ROOT_URI, cleaned.get(0)); else e.remove(KEY_LAST_SCAN_ROOT_URI);
        e.apply();
    }

    /**
     * 获取扫描目录的开关状态
     * @return 返回一个布尔值列表，表示每个目录是否启用
     */
    private List<Boolean> getScanRootEnabledStates() {
        List<Boolean> states = new ArrayList<>();
        if (prefs == null) return states;
        String joined = prefs.getString(KEY_SCAN_ROOT_ENABLED, "");
        if (joined != null && !joined.trim().isEmpty()) {
            for (String part : joined.split(",")) {
                String s = part == null ? "" : part.trim();
                states.add("1".equals(s));
            }
        }
        // 补齐到MAX_SCAN_ROOTS个，默认全部启用
        while (states.size() < MAX_SCAN_ROOTS) {
            states.add(true);
        }
        return states;
    }

    /**
     * 保存扫描目录的开关状态
     */
    private void saveScanRootEnabledStates(List<Boolean> states) {
        if (prefs == null || states == null) return;
        StringBuilder joined = new StringBuilder();
        for (int i = 0; i < Math.min(states.size(), MAX_SCAN_ROOTS); i++) {
            if (joined.length() > 0) joined.append(',');
            joined.append(states.get(i) ? "1" : "0");
        }
        prefs.edit().putString(KEY_SCAN_ROOT_ENABLED, joined.toString()).apply();
    }

    /**
     * 设置指定目录的开关状态
     */
    private void setScanRootEnabled(int index, boolean enabled) {
        List<Boolean> states = getScanRootEnabledStates();
        if (index >= 0 && index < states.size()) {
            states.set(index, enabled);
            saveScanRootEnabledStates(states);
        }
    }

    /**
     * 获取启用的扫描目录列表
     */
    private List<String> getActiveScanRootUris() {
        List<String> roots = getScanRootUris();
        List<Boolean> states = getScanRootEnabledStates();
        List<String> active = new ArrayList<>();
        for (int i = 0; i < roots.size(); i++) {
            if (i < states.size() && states.get(i)) {
                active.add(roots.get(i));
            }
        }
        return active;
    }

// ==================== .nomedia 相册防污染 ====================

    /**
     * 已询问过 .nomedia 的扫描根 uri 集合（按 uri 存，删除中间项不会错位）。
     *
     * <p>读取时顺带清理已经不在扫描根列表里的陈旧条目，
     * 避免用户反复更换目录后这个集合无限增长。
     */
    private Set<String> getNoMediaAskedUris() {
        Set<String> asked = new HashSet<>();
        if (prefs == null) return asked;
        String joined = prefs.getString(KEY_NOMEDIA_ASKED, "");
        if (joined != null && !joined.trim().isEmpty()) {
            for (String part : joined.split("\\n")) {
                String s = part == null ? "" : part.trim();
                if (!s.isEmpty()) asked.add(s);
            }
        }
        return asked;
    }

    /** 丢弃已不在扫描根列表中的「已问过」记录，保持 prefs 不无限增长。 */
    private void pruneNoMediaAsked() {
        if (prefs == null) return;
        try {
            Set<String> asked = getNoMediaAskedUris();
            if (asked.isEmpty()) return;
            Set<String> valid = new HashSet<>();
            for (String r : getScanRootUris()) {
                if (r != null && !r.trim().isEmpty()) valid.add(r.trim());
            }
            if (asked.retainAll(valid)) {
                saveNoMediaAsked(asked);
            }
        } catch (Throwable t) {
            Log.w("YukiHub", "prune nomedia asked failed", t);
        }
    }

    private void saveNoMediaAsked(Set<String> asked) {
        if (prefs == null) return;
        StringBuilder sb = new StringBuilder();
        for (String s : asked) {
            if (s == null || s.trim().isEmpty()) continue;
            if (sb.length() > 0) sb.append('\n');
            sb.append(s.trim());
        }
        prefs.edit().putString(KEY_NOMEDIA_ASKED, sb.toString()).apply();
    }

    private void markNoMediaAsked(String rootUri) {
        if (prefs == null || rootUri == null || rootUri.trim().isEmpty()) return;
        Set<String> asked = getNoMediaAskedUris();
        if (!asked.add(rootUri.trim())) return; // 已经记过，不必重写 prefs
        saveNoMediaAsked(asked);
    }

    /**
     * 绑定扫描目录 / 扫描完成后调用：探测是否需要询问创建 .nomedia。
     *
     * <p>此阶段<b>只做只读探测，不做安全判定，不弹任何警告</b>。
     * 安全判定要等用户明确点了「创建」之后才执行（见 {@link #performNoMediaCreate}），
     * 这样不想用这功能的人从头到尾只会看到一个普通询问框。
     */
    private void maybePromptNoMedia(String rootUri) {
        if (rootUri == null || rootUri.trim().isEmpty()) return;
        final String root = rootUri.trim();
        if (getNoMediaAskedUris().contains(root)) return;
        AppExecutors.runOnIo(() -> {
            boolean exists;
            try {
                exists = NoMediaHelper.exists(MainActivity.this, root);
            } catch (Throwable t) {
                Log.w("YukiHub", "nomedia probe failed root=" + root, t);
                return;
            }
            if (exists) return; // 已经有了，静默
            runOnUiThread(() -> showNoMediaAskDialog(root));
        });
    }

    /** 扫描完成后对所有启用的扫描根做一次检查（覆盖已绑定目录的老用户）。 */
    private void maybePromptNoMediaForActiveRoots() {
        try {
            List<String> roots = getActiveScanRootUris();
            Set<String> asked = getNoMediaAskedUris();
            for (String root : roots) {
                if (root == null || root.trim().isEmpty()) continue;
                if (asked.contains(root.trim())) continue;
                maybePromptNoMedia(root);
                return; // 一次只问一个，避免弹窗叠加
            }
        } catch (Throwable t) {
            Log.w("YukiHub", "nomedia batch prompt failed", t);
        }
    }

    /** 询问弹窗：用户表态前不显示任何警告。 */
    private void showNoMediaAskDialog(final String rootUri) {
        if (isFinishing() || isDestroyed()) return;
        String shown = displayPath(rootUri);
        String msg = "Scan directory:" + shown + "\n\n"
                + "After the game is decompressed, a large number of CG and three-dimensional pictures will be generated, and the system photo album will include them all, causing the photo album to be full."
                + "You can create an empty .nomedia file in this directory to let the system album ignore all pictures in this directory."
                + "This does not affect game play, nor does it affect YukiHub reading covers."
                + "⚠️ The pictures that have been entered into the album will not disappear immediately and need to be rescanned by the system (the device may need to be restarted).";
        try {
            new AlertDialog.Builder(this)
                    .setTitle("Prevent game pictures from filling your photo album")
                    .setMessage(msg)
                    .setPositiveButton("Create", (d, w) -> performNoMediaCreate(rootUri, true))
                    .setNegativeButton("No", (d, w) -> markNoMediaAsked(rootUri))
                    .setNeutralButton("Talk about it later", null) // 不记状态，下次还会问
                    .show();
        } catch (Throwable t) {
            Log.w("YukiHub", "show nomedia dialog failed", t);
        }
    }

    /**
     * 用户点了「创建」之后才执行：先安全判定，通过再写入。
     *
     * @param allowFallback 被安全判定拦截时是否提供「只在游戏目录创建」的降级选项
     */
    private void performNoMediaCreate(final String rootUri, final boolean allowFallback) {
        NoMediaHelper.Safety safety = NoMediaHelper.check(rootUri);
        if (!safety.allowed) {
            if (allowFallback) {
                showNoMediaBlockedDialog(rootUri, safety);
            } else {
                Toast.makeText(this, safety.message(), Toast.LENGTH_LONG).show();
            }
            return;
        }
        AppExecutors.runOnIo(() -> {
            final NoMediaHelper.Result r = NoMediaHelper.create(MainActivity.this, rootUri);
            runOnUiThread(() -> {
                if (isFinishing() || isDestroyed()) return;
                if (r.success) {
                    markNoMediaAsked(rootUri);
                    Toast.makeText(this,
                            r.alreadyExists ? "This directory already has .nomedia, no need to create it again"
                                    : ".nomedia has been created, the album will ignore pictures in this directory",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, r.error, Toast.LENGTH_LONG).show();
                }
                refreshActiveScanRootListUi();
            });
        });
    }

    /** 安全判定拦截时的降级弹窗：改为只在扫描到的游戏目录里逐个创建。 */
    private void showNoMediaBlockedDialog(final String rootUri, NoMediaHelper.Safety safety) {
        if (isFinishing() || isDestroyed()) return;
        String msg = safety.message() + "\n\n"
                + "You can instead create it separately in the scanned game directory, which has the same effect and is safe.";
        try {
            new AlertDialog.Builder(this)
                    .setTitle("⚠️ Blocked: The location cannot be created")
                    .setMessage(msg)
                    .setPositiveButton("Only created in the game directory", (d, w) -> createNoMediaForScannedGames(rootUri))
                    .setNegativeButton("Cancel", null)
                    .show();
        } catch (Throwable t) {
            Log.w("YukiHub", "show nomedia blocked dialog failed", t);
        }
    }

    /**
     * 降级路径：扫描该根下的游戏目录，对每个目录逐个做安全判定后创建。
     * 安全判定不放水，同一套规则。
     */
    private void createNoMediaForScannedGames(final String rootUri) {
        Toast.makeText(this, "Scanning game directory, please wait...", Toast.LENGTH_SHORT).show();
        AppExecutors.runOnSingle(() -> {
            List<ScanResult> results = new ArrayList<>();
            try {
                int depth = prefs == null ? DEFAULT_STARTUP_SCAN_DEPTH
                        : prefs.getInt(KEY_STARTUP_SCAN_DEPTH, DEFAULT_STARTUP_SCAN_DEPTH);
                depth = Math.max(1, Math.min(MAX_STARTUP_SCAN_DEPTH, depth));
                boolean useFast = prefs == null
                        || SCAN_MODE_FAST.equals(prefs.getString(KEY_SCAN_MODE, SCAN_MODE_FAST));
                if (useFast) {
                    results.addAll(FastGameScanner.scan(this, Uri.parse(rootUri), depth));
                } else {
                    results.addAll(GameScanner.scan(this, Uri.parse(rootUri), depth));
                }
            } catch (Throwable t) {
                Log.w("YukiHub", "nomedia fallback scan failed root=" + rootUri, t);
            }

            int created = 0, already = 0, skipped = 0;
            List<String> rescanPaths = new ArrayList<>();
            for (ScanResult sr : results) {
                if (sr == null || sr.uri == null || sr.uri.trim().isEmpty()) continue;
                try {
                    NoMediaHelper.Safety s = NoMediaHelper.check(sr.uri);
                    if (!s.allowed) { skipped++; continue; }
                    // rescan=false：批量期间不逐个触发媒体扫描，结束后统一扫一次
                    NoMediaHelper.Result r = NoMediaHelper.create(MainActivity.this, sr.uri, false);
                    if (r.success) {
                        if (r.alreadyExists) already++; else created++;
                        if (r.realPath != null && !r.realPath.isEmpty()) rescanPaths.add(r.realPath);
                    } else {
                        skipped++;
                    }
                } catch (Throwable t) {
                    Log.w("YukiHub", "nomedia create failed uri=" + sr.uri, t);
                    skipped++;
                }
            }
            if (!rescanPaths.isEmpty()) {
                NoMediaHelper.requestMediaRescan(MainActivity.this,
                        rescanPaths.toArray(new String[0]));
            }

            final int fCreated = created, fAlready = already, fSkipped = skipped;
            final int total = results.size();
            runOnUiThread(() -> {
                if (isFinishing() || isDestroyed()) return;
                // 只有真的扫到了目录才记「已问过」；扫描失败(total=0)不记，
                // 否则用户以后再也不会被提示（多为权限或目录变动导致的临时失败）。
                if (total > 0) markNoMediaAsked(rootUri);
                String msg = total == 0
                        ? "The game directory was not scanned and no changes were made."
                        : "New" + fCreated + ", already exists" + fAlready + " 个"
                                + (fSkipped > 0 ? ",jump over" + fSkipped + " 个" : "");
                Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                refreshActiveScanRootListUi();
            });
        });
    }

    /** 设置界面点击 .nomedia 状态时的管理弹窗（创建 / 移除）。 */
    private void showNoMediaManageDialog(final String rootUri) {
        if (isFinishing() || isDestroyed()) return;
        AppExecutors.runOnIo(() -> {
            final boolean exists = NoMediaHelper.exists(MainActivity.this, rootUri);
            runOnUiThread(() -> {
                if (isFinishing() || isDestroyed()) return;
                String shown = displayPath(rootUri);
                try {
                    if (exists) {
                        new AlertDialog.Builder(this)
                                .setTitle("Album anti-pollution: enabled")
                                .setMessage("Table of contents:" + shown + "\n\n"
                                        + "This directory already has .nomedia, and the system album will ignore the pictures here."
                                        + "After removal, the album will re-include the pictures in this directory.")
                                .setPositiveButton("Remove", (d, w) -> performNoMediaRemove(rootUri))
                                .setNegativeButton("Close", null)
                                .show();
                    } else {
                        new AlertDialog.Builder(this)
                                .setTitle("Album anti-pollution: not turned on")
                                .setMessage("Table of contents:" + shown + "\n\n"
                                        + "After creating .nomedia, the system album will ignore all pictures in this directory."
                                        + "Avoid cluttering your photo album with game CGs. Does not affect game running and cover reading."
                                        + "⚠️ Please do not create it in the root directory of the memory card, otherwise the album will not be able to read any pictures."
                                        + "YukiHub automatically blocks such dangerous locations.")
                                .setPositiveButton("Create", (d, w) -> performNoMediaCreate(rootUri, true))
                                .setNegativeButton("Cancel", null)
                                .show();
                    }
                } catch (Throwable t) {
                    Log.w("YukiHub", "show nomedia manage dialog failed", t);
                }
            });
        });
    }

    private void performNoMediaRemove(final String rootUri) {
        AppExecutors.runOnIo(() -> {
            final NoMediaHelper.Result r = NoMediaHelper.remove(MainActivity.this, rootUri);
            runOnUiThread(() -> {
                if (isFinishing() || isDestroyed()) return;
                Toast.makeText(this,
                        r.success ? ".nomedia has been removed and the album will re-include images from this directory" : r.error,
                        Toast.LENGTH_LONG).show();
                // 不清「已问过」记录：用户是主动移除的，
                // 清掉会导致下次扫描又弹窗询问，属于骚扰。
                // 需要重新创建时可从设置里的「相册」入口手动操作。
                refreshActiveScanRootListUi();
            });
        });
    }

private boolean addOrReplaceScanRoot(String uri, int replaceIndex) {
if (uri == null || uri.trim().isEmpty()) return false;
List<String> roots = getScanRootUris();
String value = uri.trim();
roots.remove(value);
if (replaceIndex >= 0 && replaceIndex < roots.size()) roots.set(replaceIndex, value);
else if (roots.size() < MAX_SCAN_ROOTS) roots.add(value);
else {
Toast.makeText(MainActivity.this, "Most bindings" + MAX_SCAN_ROOTS + "Scan directories", Toast.LENGTH_SHORT).show();
return false;
}
saveScanRootUris(roots);
return true;
}

private void removeScanRootAt(int index) {
List<String> roots = getScanRootUris();
if (index < 0 || index >= roots.size()) return;
roots.remove(index);
saveScanRootUris(roots);
// 目录已移除，顺带丢掉它的 .nomedia 询问记录，避免 prefs 无限增长
pruneNoMediaAsked();
}

private String scanRootsSummary() {
List<String> roots = getScanRootUris();
if (roots.isEmpty()) return "Not bound";
StringBuilder sb = new StringBuilder();
for (int i = 0; i < roots.size(); i++) {
if (i > 0) sb.append('\n');
sb.append(i + 1).append(". ").append(roots.get(i));
}
return sb.toString();
}

private String compactUriLabel(String uri) {
if (uri == null || uri.trim().isEmpty()) return "Not bound";
String s = uri.trim();
try {
Uri u = Uri.parse(s);
String last = u.getLastPathSegment();
if (last != null && !last.isEmpty()) return java.net.URLDecoder.decode(last, "UTF-8").replace("primary:", "/storage/emulated/0/");
} catch (Throwable ignored) { }
return s.length() > 72 ? "..." + s.substring(s.length() - 72) : s;
}

private boolean useBuiltinFileChooser() {
        return prefs == null || prefs.getBoolean(KEY_USE_BUILTIN_FILE_CHOOSER, true);
    }
    private boolean nsfwBlurEnabled() {
        return prefs == null || prefs.getBoolean(KEY_NSFW_BLUR, true);
    }

    private void launchScanRootPicker(int replaceIndex) {
        pendingScanRootReplaceIndex = replaceIndex;
        if (useBuiltinFileChooser()) {
            new com.yuki.yukihub.ui.filechooser.FileChooserDialog(this)
                .setMode(com.yuki.yukihub.ui.filechooser.FileChooserDialog.Mode.DIRECTORY)
                .setTitle("Select scan directory")
                .setOnFileSelectedListener(new com.yuki.yukihub.ui.filechooser.FileChooserDialog.OnFileSelectedListener() {
                    @Override public void onFileSelected(Uri uri, String path, String fileName) {}
                    @Override public void onDirectorySelected(Uri uri, String path) {
String selectedDir = uri != null ? uri.toString() : path;
boolean changed = addOrReplaceScanRoot(selectedDir, pendingScanRootReplaceIndex);
pendingScanRootReplaceIndex = -2;
                        if (changed) {
                            refreshActiveScanRootListUi();
                            Toast.makeText(MainActivity.this, "Scan catalog updated", Toast.LENGTH_SHORT).show();
                            maybePromptNoMedia(selectedDir);
                        }
                    }
                })
                .setOnSafRequestListener(() -> scanDirLauncher.launch(null))
                .show();
        } else {
            scanDirLauncher.launch(null);
        }
    }

private LinearLayout scanRootCard(String uri, int index, Runnable refresh) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.HORIZONTAL);
        card.setGravity(android.view.Gravity.CENTER_VERTICAL);
        card.setBackgroundResource(R.drawable.bg_input);
        card.setPadding(dp(10), dp(8), dp(8), dp(8));
        
        // 添加开关控件
        android.widget.Switch enableSwitch = new android.widget.Switch(this);
        List<Boolean> states = getScanRootEnabledStates();
        boolean isEnabled = index < states.size() ? states.get(index) : true;
        enableSwitch.setChecked(isEnabled);
        enableSwitch.setOnCheckedChangeListener((buttonView, checked) -> {
            setScanRootEnabled(index, checked);
            // 更新提示信息
            if (refresh != null) refresh.run();
        });
        card.addView(enableSwitch, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        
        TextView text = new TextView(this);
        text.setText((index + 1) + ". " + compactUriLabel(uri));
        text.setTextColor(getColorCompat(R.color.yh_text));
        text.setTextSize(11);
        text.setSingleLine(false);
        card.addView(text, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        // 相册防污染（.nomedia）状态入口：异步探测后回填，避免主线程 IO
        TextView noMedia = new TextView(this);
        noMedia.setText("Photo album…");
        noMedia.setTextColor(getColorCompat(R.color.yh_text_muted));
        noMedia.setTextSize(12);
        noMedia.setTypeface(null, android.graphics.Typeface.BOLD);
        noMedia.setPadding(dp(8), 0, dp(4), 0);
        noMedia.setOnClickListener(v -> showNoMediaManageDialog(uri));
        card.addView(noMedia);
        AppExecutors.runOnIo(() -> {
            boolean has;
            try {
                has = NoMediaHelper.exists(MainActivity.this, uri);
            } catch (Throwable t) {
                return;
            }
            final boolean fHas = has;
            runOnUiThread(() -> {
                // 卡片可能已被刷新替换掉，旧 View 脱离视图树后不必再更新
                if (isFinishing() || isDestroyed() || noMedia.getParent() == null) return;
                noMedia.setText(fHas ? "Album✓" : "Album✕");
                noMedia.setTextColor(fHas ? primaryTextColor() : getColorCompat(R.color.yh_text_muted));
            });
        });
        TextView change = new TextView(this);
        change.setText("Replace");
        change.setTextColor(primaryTextColor());
        change.setTextSize(12);
        change.setTypeface(null, android.graphics.Typeface.BOLD);
        change.setPadding(dp(10), 0, dp(8), 0);
        change.setOnClickListener(v -> launchScanRootPicker(index));
        card.addView(change);
        TextView remove = new TextView(this);
        remove.setText("Remove");
        remove.setTextColor(getColorCompat(R.color.yh_warning));
        remove.setTextSize(12);
        remove.setTypeface(null, android.graphics.Typeface.BOLD);
        remove.setPadding(dp(8), 0, 0, 0);
        remove.setOnClickListener(v -> {
            removeScanRootAt(index);
            if (refresh != null) refresh.run();
        });
        card.addView(remove);
        return card;
    }

private void refreshActiveScanRootListUi() {
if (activeScanRootList != null) refreshScanRootListUi(activeScanRootList, activeScanRootInfo);
}

private void refreshScanRootListUi(LinearLayout container, TextView info) {
        if (container == null) return;
        container.removeAllViews();
        List<String> roots = getScanRootUris();
        List<Boolean> states = getScanRootEnabledStates();
        long enabledCount = 0;
        for (int i = 0; i < roots.size(); i++) {
            if (i < states.size() && states.get(i)) enabledCount++;
        }
        if (info != null) info.setText("Bound" + roots.size() + "/" + MAX_SCAN_ROOTS + "Directories, enabled" + enabledCount + "Indivual." + (roots.isEmpty() ? "Please add the scan directory first." : ""));
if (roots.isEmpty()) {
TextView empty = new TextView(this);
empty.setText("No scan directory yet");
empty.setTextColor(getColorCompat(R.color.yh_text_muted));
empty.setTextSize(12);
empty.setGravity(android.view.Gravity.CENTER);
empty.setBackgroundResource(R.drawable.bg_input);
container.addView(empty, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp(44)));
return;
}
for (int i = 0; i < roots.size(); i++) {
final int index = i;
LinearLayout card = scanRootCard(roots.get(i), index, () -> refreshScanRootListUi(container, info));
LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
lp.setMargins(0, 0, 0, dp(6));
container.addView(card, lp);
}
}

private void checkUpdateOnStartupIfEnabled() {
        try {
            if (prefs == null || !prefs.getBoolean(KEY_CHECK_UPDATE_ON_STARTUP, true)) return;
            long last = prefs.getLong(KEY_LAST_UPDATE_CHECK_AT, 0L);
            if (last > 0 && System.currentTimeMillis() - last < UPDATE_AUTO_CHECK_INTERVAL_MS) return;
            checkUpdate(false);
        } catch (Throwable t) {
            Log.w("YukiHub", "startup update check skipped", t);
        }
    }

    private void checkUpdateManually() {
        Toast.makeText(MainActivity.this, "Passing" + (UPDATE_SOURCE_GITHUB.equals(updateSource()) ? "GitHub" : "GitCode") + "Check for updates...", Toast.LENGTH_SHORT).show();
        checkUpdate(true);
    }

    private void checkUpdate(boolean manual) {
        final String selectedSource = updateSource(); // 本次请求固定来源，设置切换不影响在途请求
        AppExecutors.runOnIo(() -> {
            try {
                UpdateInfo info = fetchLatestRelease(selectedSource);
                if (prefs != null && selectedSource.equals(updateSource())) prefs.edit().putLong(KEY_LAST_UPDATE_CHECK_AT, System.currentTimeMillis()).apply();
                String current = getCurrentVersionName();
                boolean newer = info != null && isNewerVersion(info.version, current);
                runOnUiThread(() -> {
                    if (newer) {
                        showUpdateDialog(info, current);
                    } else if (manual) {
                        Toast.makeText(this, "Already the latest version:" + emptyText(current, "Unknown"), Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Throwable t) {
                Log.w("YukiHub", "check update failed", t);
                if (manual) {
                    runOnUiThread(() -> Toast.makeText(this, "Checking for updates failed:" + emptyText(t.getMessage(), "Please try again later"), Toast.LENGTH_LONG).show());
                }
            }
        });
    }

    private String updateSource() {
        String saved = prefs == null ? UPDATE_SOURCE_GITCODE : prefs.getString(KEY_UPDATE_SOURCE, UPDATE_SOURCE_GITCODE);
        return UPDATE_SOURCE_GITHUB.equals(saved) ? UPDATE_SOURCE_GITHUB : UPDATE_SOURCE_GITCODE;
    }

    private UpdateInfo fetchLatestRelease(String selectedSource) throws Exception {
        UpdateSource source = UPDATE_SOURCE_GITHUB.equals(selectedSource)
                ? new UpdateSource("GitHub", UPDATE_GITHUB_API_URL, UPDATE_GITHUB_REPO_URL, "application/vnd.github+json")
                : new UpdateSource("GitCode", UPDATE_GITCODE_API_URL, UPDATE_GITCODE_REPO_URL, "application/json");
        // 尊重单选来源；失败时提示用户换源，不静默回退到另一站点。
        try { return fetchLatestReleaseFrom(source); }
        catch (Exception e) { throw new RuntimeException(source.name + "The update source is unavailable. You can switch the update source in settings.", e); }
    }

    private UpdateInfo fetchLatestReleaseFrom(UpdateSource source) throws Exception {
        HttpURLConnection c = (HttpURLConnection) new URL(source.apiUrl).openConnection();
        try {
            c.setRequestMethod("GET");
            c.setInstanceFollowRedirects(true);
            c.setConnectTimeout(12000);
            c.setReadTimeout(15000);
            c.setRequestProperty("Accept", source.accept);
            c.setRequestProperty("User-Agent", "YukiHub-Android/" + getCurrentVersionName());
            int code = c.getResponseCode();
            String text;
            try (InputStream stream = code >= 200 && code < 300 ? c.getInputStream() : c.getErrorStream()) {
                text = readSmallText(stream);
            }
            if (code < 200 || code >= 300) throw new RuntimeException(source.name + " HTTP " + code + ": " + trimForDialog(text, 160));
            JSONObject o = new JSONObject(text == null ? "{}" : text);
            UpdateInfo info = parseReleaseInfo(o, source);
            if (info.version == null || info.version.isEmpty()) throw new RuntimeException(source.name + "No valid version number returned");
            return info;
        } finally { c.disconnect(); }
    }

    private UpdateInfo parseReleaseInfo(JSONObject o, UpdateSource source) {
        UpdateInfo info = new UpdateInfo();
        info.sourceName = source.name;
        info.repoUrl = source.repoUrl;
        info.tagName = o.optString("tag_name", "");
        info.version = normalizeVersion(info.tagName);
        info.name = o.optString("name", info.tagName);
        info.body = o.optString("body", "");
        info.releaseUrl = o.optString("html_url", "");
        if (info.releaseUrl.trim().isEmpty()) {
            info.releaseUrl = source.repoUrl + "/releases" + (info.tagName.isEmpty() ? "" : "/tag/" + Uri.encode(info.tagName));
        }
        JSONArray assets = o.optJSONArray("assets");
        if (assets != null) {
            for (int i = 0; i < assets.length(); i++) {
                JSONObject a = assets.optJSONObject(i);
                if (a == null) continue;
                String assetName = a.optString("name", "");
                String url = a.optString("browser_download_url", "");
                if (url == null || url.trim().isEmpty()) continue;
                if (info.downloadUrl == null || info.downloadUrl.isEmpty()) info.downloadUrl = url;
                String lowerName = assetName.toLowerCase(Locale.ROOT);
                String lowerUrl = url.toLowerCase(Locale.ROOT);
                if (lowerName.endsWith(".apk") || lowerUrl.contains(".apk")) {
                    info.apkUrl = url;
                    break;
                }
            }
        }
        if (info.version == null || info.version.isEmpty()) info.version = normalizeVersion(info.name);
        if (info.downloadUrl == null || info.downloadUrl.isEmpty()) info.downloadUrl = info.releaseUrl;
        if (info.apkUrl == null || info.apkUrl.isEmpty()) info.apkUrl = info.releaseUrl;
        return info;
    }

    private String getCurrentVersionName() {
        try {
            return getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (Throwable ignored) {
            return "";
        }
    }

    private boolean isNewerVersion(String latest, String current) {
        String l = normalizeVersion(latest);
        String c = normalizeVersion(current);
        if (l.isEmpty() || c.isEmpty()) return !l.equals(c);
        String[] la = l.split("\\.");
        String[] ca = c.split("\\.");
        int n = Math.max(la.length, ca.length);
        for (int i = 0; i < n; i++) {
            long lv = i < la.length ? parseVersionPart(la[i]) : 0L;
            long cv = i < ca.length ? parseVersionPart(ca[i]) : 0L;
            if (lv > cv) return true;
            if (lv < cv) return false;
        }
        return false;
    }

    private long parseVersionPart(String part) {
        try {
            if (part == null) return 0L;
            String digits = part.replaceAll("[^0-9]", "");
            return digits.isEmpty() ? 0L : Long.parseLong(digits);
        } catch (Throwable ignored) {
            return 0L;
        }
    }

    private String normalizeVersion(String value) {
        if (value == null) return "";
        String v = value.trim();
        Matcher m = Pattern.compile("(\\d+(?:\\.\\d+){1,5})").matcher(v);
        if (m.find()) return m.group(1);
        v = v.replaceFirst("^[vV]", "").replaceAll("[^0-9.]", "");
        while (v.startsWith(".")) v = v.substring(1);
        while (v.endsWith(".")) v = v.substring(0, v.length() - 1);
        return v;
    }

    private void showUpdateDialog(UpdateInfo info, String currentVersion) {
        if (info == null || isFinishing()) return;
        String latestLabel = emptyText(info.tagName, info.version);
        StringBuilder msg = new StringBuilder();
        msg.append("Current version:").append(emptyText(currentVersion, "Unknown")).append("\n");
        msg.append("Latest version:").append(emptyText(latestLabel, "Unknown")).append("\n\n");
        String body = trimForDialog(info.body, 1600);
        if (body != null && !body.trim().isEmpty()) {
            msg.append("Updates:").append(body.trim());
        } else {
            msg.append("Discover new").append(emptyText(info.sourceName, "Publish source")).append("Release, you can go to the release page to view details.");
        }
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("New version found" + emptyText(latestLabel, ""))
                .setMessage(msg.toString())
                .setPositiveButton("Go to download", (d, w) -> openExternalUrl(emptyText(info.apkUrl, info.releaseUrl)))
                .setNeutralButton("Release page", (d, w) -> openExternalUrl(emptyText(info.releaseUrl, emptyText(info.repoUrl, UPDATE_GITCODE_REPO_URL) + "/releases")))
                .setNegativeButton("Later", null)
                .show();
        styleAlertDialogDark(dialog);
    }

    private String trimForDialog(String text, int max) {
        if (text == null) return "";
        String t = text.trim();
        if (max <= 0 || t.length() <= max) return t;
        return t.substring(0, max) + "\n...";
    }

    private static class UpdateSource {
        final String name;
        final String apiUrl;
        final String repoUrl;
        final String accept;

        UpdateSource(String name, String apiUrl, String repoUrl, String accept) {
            this.name = name;
            this.apiUrl = apiUrl;
            this.repoUrl = repoUrl;
            this.accept = accept;
        }
    }

    private static class UpdateInfo {
        String sourceName;
        String repoUrl;
        String tagName;
        String version;
        String name;
        String body;
        String releaseUrl;
        String downloadUrl;
        String apkUrl;
    }

    private void openExternalUrl(String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } catch (Throwable t) {
            Toast.makeText(MainActivity.this, "Unable to open link", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDisclaimerDialog() {
        String text = "Disclaimer" +
                "1. This application is an open source project. It is only used to manage, organize and launch games and applications that the user has the right to use. It does not provide any game ontology, crack resources, bypass authorization or the ability to circumvent copyright/platform rules." +
                "2. Users should ensure the legality, integrity and availability of added resources, accounts, synchronized content, community content and third-party services; the user shall bear all responsibilities arising from the use of these contents." +
                "3. Third-party login (NextMoe, Hikarinagi and other account systems) is only used for identity association and cloud synchronization. Account data, avatars and information are determined by the rules of the corresponding platform; please keep your account and authorization properly and avoid logging in on untrusted devices." +
                "4. Cloud synchronization (WebDAV/server), friend chat, online status and community functions rely on external services and network environment, and may be unavailable or have data differences due to service changes, network conditions or account status." +
                "5. The game startup relies on third-party engines and applications such as KIRIKIRI2, Tyranno, Artemis, ONScripter, Winlator, and Gasgoo. The automatic selection of startup files, fast/compatible scan mode, etc. only provide best-effort adaptation and do not guarantee that all resources can be started correctly." +
                "6. The covers, introductions and tags obtained by game data scraping (VNDB, Bangumi, Moon Screen Gal, Hikarinagi, etc.) are from third-party metadata services, which may be inaccurate or incomplete and are for collation and reference only." +
                "7. System storage permissions, Shizuku, GameHub and other capabilities depend on the system environment and third-party applications, and may be unavailable due to device, system version, permission status or service changes." +
                "8. The developer does not bear additional responsibility for data loss, synchronization anomalies, startup failures, compatibility issues or other losses caused by third-party services, system limitations, user misoperation or problems with the resources themselves." +
                "9. If you do not agree with the above instructions, please stop using relevant functions.";
        TextView tv = new TextView(this);
        int pad = dp(18);
        tv.setPadding(pad, pad, pad, pad);
        tv.setTextColor(getColorCompat(R.color.yh_text_muted));
        tv.setTextSize(13);
        tv.setLineSpacing(dp(3), 1.08f);
        tv.setText(text);
        ScrollView scroll = new ScrollView(this);
        scroll.addView(tv);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Disclaimer")
                .setView(scroll)
                .setPositiveButton("Knew", null)
                .show();
        styleAlertDialogDark(dialog);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.62f), (int) (getResources().getDisplayMetrics().heightPixels * 0.72f));
        }
    }

    private String normalizePlayStatus(String status) {
    if (status == null) return "unplayed";
    String s = status.trim().toLowerCase(Locale.ROOT);
    if ("completed".equals(s) || "played".equals(s) || "done".equals(s)) return "completed";
    if ("playing".equals(s) || "current".equals(s)) return "playing";
    // 搁置：开了但暂时不打算继续，将来还想回来
    if ("onhold".equals(s) || "on_hold".equals(s) || "on-hold".equals(s)
            || "shelved".equals(s) || "paused".equals(s) || "hold".equals(s)) return "onhold";
    // 抛弃：明确不再继续
    if ("dropped".equals(s) || "drop".equals(s) || "abandoned".equals(s)
            || "abandon".equals(s) || "give_up".equals(s)) return "dropped";
    return "unplayed";
}
private String playStatusLabel(String status) {
    return com.yuki.yukihub.util.IconedText.labelForStatus(normalizePlayStatus(status));
}
private int playStatusIndex(String status) {
    String s = normalizePlayStatus(status);
    if ("playing".equals(s)) return 1;
    if ("completed".equals(s)) return 2;
    if ("onhold".equals(s)) return 3;
    if ("dropped".equals(s)) return 4;
    return 0;
}
private String playStatusFromIndex(int index) {
    if (index == 1) return "playing";
    if (index == 2) return "completed";
    if (index == 3) return "onhold";
    if (index == 4) return "dropped";
    return "unplayed";
}
private void showPlayStatusDialog(Game game, Dialog parentDialog) {
    if (game == null) return;
    String[] labels = new String[]{"Not played", "Playing", "Played", "Put on hold", "Abandon"};
    String[] statusKeys = new String[]{"unplayed", "playing", "completed", "onhold", "dropped"};
    LinearLayout root = new LinearLayout(this);
    root.setOrientation(LinearLayout.VERTICAL);
    root.setBackgroundResource(R.drawable.bg_dialog);
    tintDialogRoot(root);
    root.setPadding(dp(14), dp(8), dp(14), dp(8));
    final AlertDialog[] ref = new AlertDialog[1];
    int selected = playStatusIndex(game.playStatus);
    for (int i = 0; i < labels.length; i++) {
        final int index = i;
        TextView row = new TextView(this);
        int rowColor = index == selected ? primaryTextColor() : getColorCompat(R.color.yh_text);
        row.setText(com.yuki.yukihub.util.IconedText.buildWithPrefix(this,
                index == selected ? "●  " : "○  ",
                com.yuki.yukihub.util.IconedText.drawableForStatus(statusKeys[index]),
                " " + labels[index], 14f, rowColor));
        row.setTextColor(rowColor);
        row.setTextSize(18);
        row.setGravity(android.view.Gravity.CENTER_VERTICAL);
        row.setBackgroundResource(R.drawable.bg_input);
        row.setPadding(dp(16), 0, dp(16), 0);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp(48));
        lp.setMargins(0, dp(4), 0, dp(4));
        root.addView(row, lp);
        row.setOnClickListener(v -> {
            game.playStatus = playStatusFromIndex(index);
            repository.update(game);
            Toast.makeText(this, "Tagged:" + playStatusLabel(game.playStatus), Toast.LENGTH_SHORT).show();
            if (ref[0] != null) ref[0].dismiss();
            if (parentDialog != null) parentDialog.dismiss();
            loadGames();
            updateSideDetail(game);
        });
    }
    AlertDialog dialog = new AlertDialog.Builder(this)
            .setTitle("Set play status")
            .setView(wrapDialogScroll(root))
            .setNegativeButton("Cancel", null)
            .show();
    ref[0] = dialog;
    styleAlertDialogDark(dialog);
    if (dialog.getWindow() != null) {
        dialog.getWindow().setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.42f), android.view.WindowManager.LayoutParams.WRAP_CONTENT);
    }
}

/**
 * 把弹窗内容包进 ScrollView，避免内容高度超过屏幕时选项被截断且无法滑动。
 *
 * 弹窗高度按内容自适应；只有内容真的超出可用高度时才会出现滚动，
 * 所以选项少的时候观感与原来完全一致。
 */
private android.view.View wrapDialogScroll(android.view.View content) {
    android.widget.ScrollView sv = new android.widget.ScrollView(this);
    sv.setLayoutParams(new android.view.ViewGroup.LayoutParams(
            android.view.ViewGroup.LayoutParams.MATCH_PARENT,
            android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
    sv.setFillViewport(false);
    sv.setVerticalScrollBarEnabled(false);
    sv.setOverScrollMode(android.view.View.OVER_SCROLL_NEVER);
    sv.addView(content, new android.widget.FrameLayout.LayoutParams(
            android.view.ViewGroup.LayoutParams.MATCH_PARENT,
            android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
    return sv;
}

private void showDetailDialog(Game game) {
        Dialog d = new Dialog(this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.getWindow();
        d.setOnShowListener(dialog -> {
            applyImmersiveToWindow(d.getWindow());
            enterImmersiveMode();
        });
        d.setOnDismissListener(dialog -> enterImmersiveMode());
        d.setContentView(R.layout.dialog_game_detail);
        tintDialogRoot(d.findViewById(android.R.id.content));
        if (d.getWindow() != null) {
            d.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            d.getWindow().setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.82f), android.view.WindowManager.LayoutParams.WRAP_CONTENT);
            d.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
            applyImmersiveToWindow(d.getWindow());
        }
        ((TextView)d.findViewById(R.id.detailTitle)).setText(game.title);
        ((TextView)d.findViewById(R.id.detailInfo)).setText("State:" + playStatusLabel(game.playStatus) + "Engine:" + game.engine.getDisplayName() + "Total duration:" + TimeFormatUtil.playTime(game.totalPlayTime) + "Recently played:" + TimeFormatUtil.date(game.lastPlayedAt) + "Simulator:" + emptyText(game.emulatorPackage, "Not configured"));
        ((TextView)d.findViewById(R.id.detailPath)).setText("Path:" + displayPath(game.rootUri));
        ImageView cover = d.findViewById(R.id.detailCover);
        TextView ph = d.findViewById(R.id.detailCoverPlaceholder);
        String safeCover = safeCoverUri(game);
        if (safeCover != null && !safeCover.isEmpty()) {
            try {
                Uri u = Uri.parse(safeCover);
                cover.setImageURI(u);
                cover.setVisibility(View.VISIBLE);
                ph.setVisibility(View.GONE);
            } catch (Throwable e) {
                cover.setImageDrawable(null);
                cover.setVisibility(View.GONE);
                ph.setVisibility(View.VISIBLE);
            }
        }
        d.findViewById(R.id.btnStatus).setOnClickListener(v -> showPlayStatusDialog(game, d));
        d.findViewById(R.id.btnEdit).setOnClickListener(v -> { d.dismiss(); showEditDialog(game); });
        boolean hasEngineSettings = game.engine == EngineType.KIRIKIRI || game.engine == EngineType.ONS || game.engine == EngineType.PC || game.engine == EngineType.FVP;
        d.findViewById(R.id.btnKrSettings).setVisibility(hasEngineSettings ? View.VISIBLE : View.GONE);
        d.findViewById(R.id.btnKrSettings).setOnClickListener(v -> {
            if (game.engine == EngineType.ONS) showOnsSettingsDialog(game);
            else if (game.engine == EngineType.PC) showPcSettingsDialog(game);
            else if (game.engine == EngineType.FVP) showFvpSettingsDialog(game);
            else showKrSettingsDialog(game);
        });
        d.findViewById(R.id.btnDelete).setOnClickListener(v -> new AlertDialog.Builder(this).setTitle("Delete game").setMessage("Confirm to delete \"" + game.title + "\"? The ontology file will not be deleted.").setPositiveButton("Delete", (x,w)->{ com.yuki.yukihub.shortcut.GameShortcutManager.disableForGame(this, game.id); PcLaunchPrefs.clear(this, game.id); FvpLaunchPrefs.clear(this, game.id); repository.delete(game.id); d.dismiss(); loadGames(); }).setNegativeButton("Cancel", null).show());
        d.findViewById(R.id.btnLaunch).setOnClickListener(v -> launchGame(game));
        d.show();
        applyImmersiveToWindow(d.getWindow());
        enterImmersiveMode();
        if (d.getWindow() != null) {
            d.getWindow().setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.82f), android.view.WindowManager.LayoutParams.WRAP_CONTENT);
            applyImmersiveToWindow(d.getWindow());
            d.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
            applyImmersiveToWindow(d.getWindow());
        }
    }

    private void showGameHubShortcutPicker(EditText titleTarget, EditText pkgTarget, EditText gamehubIdTarget) {
        if (requestShizukuPermissionIfNeeded()) return;
        AppExecutors.runOnSingle(() -> {
            try {
                List<GameHubShortcutItem> items = loadGameHubShortcuts();
                runOnUiThread(() -> {
                    if (items.isEmpty()) {
                        new AlertDialog.Builder(this)
                                .setTitle("Import shortcuts")
                                .setMessage("No available GameHub shortcuts were read.\n\nPlease confirm: 1) Shizuku is running and authorized; 2) GameHub has created a desktop shortcut; 3) The patch package is named com.xiaoji.egggamz or the original package com.xiaoji.egggame.\n\nIf it is a device such as iQOO/OriginOS, you can click \"Copy Diagnosis\" to send the Shizuku/Shortcut output to the developer for troubleshooting. You can also paste the shortcut dump parameter import.")
                                .setPositiveButton("Paste parameters", (x, w) -> showGameHubShortcutTextImport(titleTarget, pkgTarget, gamehubIdTarget))
                                .setNeutralButton("Copy diagnostics", (x, w) -> copyGameHubShortcutDiagnostics())
                                .setNegativeButton("Knew", null)
                                .show();
                        return;
                    }
                    showGameHubShortcutListDialog(items, titleTarget, pkgTarget, gamehubIdTarget);
                });
            } catch (Throwable t) {
                runOnUiThread(() -> new AlertDialog.Builder(this)
                        .setTitle("Import failed")
                        .setMessage("Failed to read shortcut:" + t.getClass().getSimpleName() + "If the system does not grant permission to read desktop shortcuts, this is a system limitation.")
                        .setPositiveButton("Knew", null)
                        .show());
            }
        });
    }

    private void showGameHubShortcutListDialog(List<GameHubShortcutItem> source, EditText titleTarget, EditText pkgTarget, EditText gamehubIdTarget) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_gamehub_shortcut_picker);
        tintDialogRoot(dialog.findViewById(android.R.id.content));
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.getWindow().setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.74f), (int) (getResources().getDisplayMetrics().heightPixels * 0.82f));
        }
        RecyclerView rv = dialog.findViewById(R.id.recyclerGameHubShortcuts);
        EditText search = dialog.findViewById(R.id.etGameHubShortcutSearch);
        TextView hint = dialog.findViewById(R.id.tvGameHubShortcutHint);
        rv.setLayoutManager(new LinearLayoutManager(this));
        Drawable icon = getGameHubIcon();
        for (GameHubShortcutItem item : source) {
            if (item != null && item.icon == null) item.icon = icon;
        }
        final GameHubShortcutAdapter[] adapterRef = new GameHubShortcutAdapter[1];
        adapterRef[0] = new GameHubShortcutAdapter(source, item -> {
            if (item == null) return;
            if (gamehubIdTarget != null) gamehubIdTarget.setText(item.localGameId);
            if (titleTarget != null && (titleTarget.getText() == null || titleTarget.getText().toString().trim().isEmpty())) titleTarget.setText(item.localAppName);
            if (pkgTarget != null && (pkgTarget.getText() == null || pkgTarget.getText().toString().trim().isEmpty())) pkgTarget.setText(guessInstalledGameHubPackage());
            Toast.makeText(MainActivity.this, "Imported GameHub shortcuts", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
        rv.setAdapter(adapterRef[0]);
        hint.setText("Common" + adapterRef[0].getItemCount() + "Shortcut to search for game name or ID");
        search.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
            public void onTextChanged(CharSequence s, int st, int b, int c) {
                if (adapterRef[0] == null) return;
                adapterRef[0].filter(s == null ? "" : s.toString());
                hint.setText("Common" + source.size() + "Shortcuts, currently displayed" + adapterRef[0].getItemCount() + " 个");
            }
            public void afterTextChanged(Editable e) {}
        });
        dialog.findViewById(R.id.btnCloseGameHubShortcutPicker).setOnClickListener(v -> dialog.dismiss());
        dialog.show();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.74f), (int) (getResources().getDisplayMetrics().heightPixels * 0.82f));
        }
    }

    private Drawable getGameHubIcon() {
        try { return getPackageManager().getApplicationIcon(guessInstalledGameHubPackage()); } catch (Throwable ignored) { }
        try { return getPackageManager().getApplicationIcon("com.xiaoji.egggame"); } catch (Throwable ignored) { }
        return null;
    }

    private interface GameHubShortcutCallback { void onPick(GameHubShortcutItem item); }

    private class GameHubShortcutAdapter extends RecyclerView.Adapter<GameHubShortcutAdapter.Holder> {
        private final List<GameHubShortcutItem> allItems;
        private final List<GameHubShortcutItem> items = new ArrayList<>();
        private final GameHubShortcutCallback callback;
        GameHubShortcutAdapter(List<GameHubShortcutItem> source, GameHubShortcutCallback callback) {
            this.allItems = source == null ? new ArrayList<>() : new ArrayList<>(source);
            this.items.addAll(this.allItems);
            this.callback = callback;
        }
        void filter(String query) {
            String q = query == null ? "" : query.trim().toLowerCase(Locale.ROOT);
            items.clear();
            if (q.isEmpty()) {
                items.addAll(allItems);
            } else {
                for (GameHubShortcutItem item : allItems) {
                    if (item == null) continue;
                    String label = item.displayLabel == null ? "" : item.displayLabel.toLowerCase(Locale.ROOT);
                    String name = item.localAppName == null ? "" : item.localAppName.toLowerCase(Locale.ROOT);
                    String id = item.localGameId == null ? "" : item.localGameId.toLowerCase(Locale.ROOT);
                    if (label.contains(q) || name.contains(q) || id.contains(q)) items.add(item);
                }
            }
            notifyDataSetChanged();
        }
        @Override public Holder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app_picker, parent, false);
            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, dp(76));
            lp.setMargins(0, 0, 0, dp(8));
            v.setLayoutParams(lp);
            return new Holder(v);
        }
        @Override public void onBindViewHolder(Holder h, int position) {
            GameHubShortcutItem item = items.get(position);
            h.label.setText(emptyText(item.displayLabel, item.localAppName));
            h.id.setText(item.localGameId);
            if (item.icon != null) h.icon.setImageDrawable(item.icon); else h.icon.setImageResource(android.R.mipmap.sym_def_app_icon);
            h.itemView.setOnClickListener(v -> { if (callback != null) callback.onPick(item); });
            // Dynamic theme text colors
            DynamicTheme dt = DynamicTheme.getInstance();
            if (dt.isEnabled() && dt.getColors() != null) {
                h.label.setTextColor(0xFFF0F4FA);
                h.id.setTextColor(0xFFB0B8C8);
            }
        }
        @Override public int getItemCount() { return items.size(); }
        class Holder extends RecyclerView.ViewHolder {
            ImageView icon;
            TextView label, id;
            Holder(View itemView) {
                super(itemView);
                icon = itemView.findViewById(R.id.ivAppIcon);
                label = itemView.findViewById(R.id.tvAppLabel);
                id = itemView.findViewById(R.id.tvAppPackage);
            }
        }
    }

    private void showGameHubShortcutTextImport(EditText titleTarget, EditText pkgTarget, EditText gamehubIdTarget) {
        final EditText input = new EditText(this);
        input.setMinLines(5);
        input.setMaxLines(10);
        input.setGravity(android.view.Gravity.TOP);
        input.setHint("Paste the shortcut parameters containing localGameId=local_xxx or steamAppId=123456");
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        int pad = (int) (16 * getResources().getDisplayMetrics().density);
        input.setPadding(pad, pad / 2, pad, pad / 2);
        new AlertDialog.Builder(this)
                .setTitle("Paste GameHub shortcut parameters")
                .setView(input)
                .setPositiveButton("Import", (d, w) -> {
                    GameHubShortcutItem item = parseGameHubShortcutText(input.getText() == null ? "" : input.getText().toString());
                    if (item == null || item.localGameId.isEmpty()) {
                        Toast.makeText(this, "LocalGameId or steamAppId not recognized", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (gamehubIdTarget != null) gamehubIdTarget.setText(item.localGameId);
                    if (titleTarget != null && (titleTarget.getText() == null || titleTarget.getText().toString().trim().isEmpty())) titleTarget.setText(item.localAppName);
                    if (pkgTarget != null && (pkgTarget.getText() == null || pkgTarget.getText().toString().trim().isEmpty())) pkgTarget.setText(guessInstalledGameHubPackage());
                    Toast.makeText(MainActivity.this, "GameHub shortcut parameters imported", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private GameHubShortcutItem parseGameHubShortcutText(String text) {
        if (text == null) return null;
        text = text.replace('\0', ' ');
        String localGameId = matchFirst(text, "(?i)\\blocalGameId\\b\\s*[:=]\\s*([^,}\\]\\s]+)");
        localGameId = cleanGameHubValue(localGameId);
        if (localGameId == null || localGameId.trim().isEmpty()) localGameId = matchFirst(text, "\\blocal_[0-9a-fA-F\\-]{8,}\\b");
        localGameId = cleanGameHubValue(localGameId);
        String steamAppId = matchFirst(text, "(?i)\\bsteamAppI[dD]\\b\\s*[:=]\\s*[^0-9]*([0-9]+)");
        steamAppId = cleanGameHubValue(steamAppId);
        String storedId = localGameId == null || localGameId.trim().isEmpty() ? null : localGameId.trim();
        if ((storedId == null || storedId.isEmpty()) && steamAppId != null && !steamAppId.trim().isEmpty() && !"0".equals(steamAppId.trim())) storedId = "steam:" + steamAppId.trim();
        if (storedId == null || storedId.trim().isEmpty()) return null;
        String localAppName = extractGameHubNameFromText(text);
        if (localAppName == null || localAppName.trim().isEmpty()) localAppName = storedId;
        return new GameHubShortcutItem(localAppName.trim(), localAppName.trim(), storedId.trim());
    }

    private String extractGameHubNameFromText(String text) {
        String name = matchFirst(text, "(?i)\\blocalAppName\\b\\s*[:=]\\s*([^,}\\]\\r\\n]+)");
        if (name == null || name.trim().isEmpty()) name = matchFirst(text, "(?i)\\bgameName\\b\\s*[:=]\\s*([^,}\\]\\r\\n]+)");
        if (name == null || name.trim().isEmpty()) name = matchFirst(text, "(?i)\\bshortLabel\\b\\s*[:=]\\s*([^,}\\]\\r\\n]+)");
        if (name == null || name.trim().isEmpty()) name = matchFirst(text, "(?i)\\blabel\\b\\s*[:=]\\s*([^,}\\]\\r\\n]+)");
        return cleanGameHubValue(name);
    }

    private String cleanGameHubValue(String value) {
        if (value == null) return null;
        String v = value.replace('\0', ' ').trim();
        while (v.startsWith("\"") || v.startsWith("'") || v.startsWith("[")) v = v.substring(1).trim();
        while (v.endsWith("\"") || v.endsWith("'") || v.endsWith(",") || v.endsWith("}") || v.endsWith("]")) v = v.substring(0, v.length() - 1).trim();
        if (v.startsWith("String:")) v = v.substring("String:".length()).trim();
        if ("null".equalsIgnoreCase(v)) return null;
        return v;
    }

    private String matchFirst(String text, String regex) {
        try {
            Matcher m = Pattern.compile(regex).matcher(text);
            return m.find() ? m.group(1) : null;
        } catch (Throwable ignored) { return null; }
    }

    private List<GameHubShortcutItem> loadGameHubShortcuts() {
        List<GameHubShortcutItem> items = new ArrayList<>();
        items.addAll(loadGameHubShortcutsFromShizuku());
        if (!items.isEmpty()) return items;
        try {
            LauncherApps launcherApps = (LauncherApps) getSystemService(Context.LAUNCHER_APPS_SERVICE);
            if (launcherApps == null) return items;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                try {
                    if (!launcherApps.hasShortcutHostPermission()) {
                        Log.w("YukiHub", "LauncherApps shortcut permission missing");
                    }
                } catch (Throwable ignored) { }
            }
            List<ShortcutInfo> shortcuts = new ArrayList<>();
            for (String ghPkg : new String[]{"com.xiaoji.egggamz", "com.xiaoji.egggame"}) {
                try {
                    LauncherApps.ShortcutQuery query = new LauncherApps.ShortcutQuery();
                    query.setPackage(ghPkg);
                    query.setQueryFlags(LauncherApps.ShortcutQuery.FLAG_MATCH_DYNAMIC | LauncherApps.ShortcutQuery.FLAG_MATCH_PINNED | LauncherApps.ShortcutQuery.FLAG_MATCH_MANIFEST);
                    List<ShortcutInfo> part = launcherApps.getShortcuts(query, android.os.Process.myUserHandle());
                    if (part != null) shortcuts.addAll(part);
                } catch (Throwable ignored) { }
            }
            if (shortcuts.isEmpty()) return items;
            for (ShortcutInfo si : shortcuts) {
                if (si == null) continue;
                String localGameId = extractGameHubLocalGameId(si);
                if (localGameId == null || localGameId.trim().isEmpty()) continue;
                String localAppName = extractGameHubLocalAppName(si);
                String label = String.valueOf(si.getShortLabel());
                if (label == null || label.trim().isEmpty() || "null".equalsIgnoreCase(label.trim())) label = localAppName;
                if (label == null || label.trim().isEmpty()) label = localGameId;
                Drawable shortcutIcon = null;
                try { shortcutIcon = launcherApps.getShortcutIconDrawable(si, getResources().getDisplayMetrics().densityDpi); } catch (Throwable ignored) { }
                items.add(new GameHubShortcutItem(label, localAppName, localGameId, shortcutIcon));
            }
            items.sort((a, b) -> a.displayLabel.compareToIgnoreCase(b.displayLabel));
        } catch (Throwable t) {
            Log.w("YukiHub", "loadGameHubShortcuts failed", t);
        }
        if (items.isEmpty()) items.addAll(loadGameHubShortcutsFromExternalLogs());
        return items;
    }

    private boolean requestShizukuPermissionIfNeeded() {
        try {
            if (!Shizuku.pingBinder()) return false;
            if (Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED) return false;
            Shizuku.requestPermission(62001);
            Toast.makeText(this, "Please authorize in the Shizuku pop-up window, and click again to import the shortcut after authorization.", Toast.LENGTH_LONG).show();
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }

    private List<GameHubShortcutItem> loadGameHubShortcutsFromShizuku() {
        List<GameHubShortcutItem> items = new ArrayList<>();
        try {
            if (!Shizuku.pingBinder() || Shizuku.checkSelfPermission() != PackageManager.PERMISSION_GRANTED) return items;
            String out = runGameHubShizukuCommand(buildGameHubShortcutDumpCommand(false));
            java.util.HashSet<String> seen = new java.util.HashSet<>();
            addGameHubShortcutItemsFromText(out, items, seen);
            items.sort((a, b) -> a.displayLabel.compareToIgnoreCase(b.displayLabel));
        } catch (Throwable t) {
            Log.w("YukiHub", "loadGameHubShortcutsFromShizuku failed", t);
        }
        return items;
    }

    private void addGameHubShortcutItemsFromText(String text, List<GameHubShortcutItem> out, java.util.HashSet<String> seen) {
        if (text == null || out == null || seen == null) return;
        String normalized = text.replace('\0', ' ');
        String[] lines = normalized.split("\\r?\\n");
        for (String line : lines) {
            addGameHubShortcutItemIfValid(parseGameHubShortcutText(line), out, seen);
        }
        try {
            Matcher m = Pattern.compile("(?i)(localGameId\\s*[:=]\\s*([^,}\\]\\s]+)|\\blocal_[0-9a-fA-F\\-]{8,}\\b|steamAppI[dD]\\s*[:=]\\s*[^0-9]*([0-9]+))").matcher(normalized);
            while (m.find()) {
                int start = Math.max(0, m.start() - 700);
                int end = Math.min(normalized.length(), m.end() + 1600);
                addGameHubShortcutItemIfValid(parseGameHubShortcutText(normalized.substring(start, end)), out, seen);
            }
        } catch (Throwable ignored) { }
    }

    private void addGameHubShortcutItemIfValid(GameHubShortcutItem item, List<GameHubShortcutItem> out, java.util.HashSet<String> seen) {
        if (item == null || item.localGameId == null || item.localGameId.trim().isEmpty()) return;
        String key = item.localGameId.trim().toLowerCase(Locale.ROOT);
        if (seen.contains(key)) return;
        seen.add(key);
        out.add(item);
    }

    private String buildGameHubShortcutDumpCommand(boolean diagnostic) {
        StringBuilder cmd = new StringBuilder();
        cmd.append("uid=$(am get-current-user 2>/dev/null | tr -d '\\r' | head -n 1); ");
        cmd.append("case \"$uid\" in ''|*[!0-9]*) uid=0;; esac; ");
        cmd.append("echo '--- YukiHub GameHub shortcut dump ---'; ");
        cmd.append("echo user=$uid; ");
        cmd.append("echo sdk=$(getprop ro.build.version.sdk 2>/dev/null) release=$(getprop ro.build.version.release 2>/dev/null); ");
        cmd.append("echo brand=$(getprop ro.product.brand 2>/dev/null) manufacturer=$(getprop ro.product.manufacturer 2>/dev/null) model=$(getprop ro.product.model 2>/dev/null); ");
        cmd.append("echo '--- packages ---'; pm path com.xiaoji.egggamz 2>&1; pm path com.xiaoji.egggame 2>&1; ");
        cmd.append("for u in $uid 0; do ");
        cmd.append("echo --- cmd shortcut user=$u package=com.xiaoji.egggamz ---; cmd shortcut get-shortcuts --user $u --flags 31 com.xiaoji.egggamz 2>&1; ");
        cmd.append("echo --- cmd shortcut user=$u package=com.xiaoji.egggame ---; cmd shortcut get-shortcuts --user $u --flags 31 com.xiaoji.egggame 2>&1; ");
        cmd.append("done; ");
        cmd.append("echo '--- dumpsys shortcut filtered ---'; ");
        cmd.append("dumpsys shortcut 2>&1 | grep -i -A 40 -B 12 'com.xiaoji.egggamz\\|com.xiaoji.egggame\\|localGameId\\|local_\\|steamAppId' 2>&1; ");
        if (diagnostic) {
            cmd.append("echo '--- launcher packages ---'; pm list packages | grep -i 'launcher\\|bbk\\|vivo\\|origin' 2>&1; ");
        }
        return cmd.toString();
    }

    private String runGameHubShizukuCommand(String cmd) throws Exception {
        Process p;
        try {
            Method m = Shizuku.class.getDeclaredMethod("newProcess", String[].class, String[].class, String.class);
            m.setAccessible(true);
            p = (Process) m.invoke(null, new Object[]{new String[]{"/system/bin/sh", "-c", cmd}, null, null});
        } catch (Throwable reflectError) {
            throw new RuntimeException("Shizuku newProcess unavailable", reflectError);
        }
        String out = readProcessStream(p.getInputStream()) + "\n" + readProcessStream(p.getErrorStream());
        try { p.waitFor(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        return out;
    }

    private void copyGameHubShortcutDiagnostics() {
        if (requestShizukuPermissionIfNeeded()) return;
        Toast.makeText(MainActivity.this, "Generating GameHub shortcut diagnostics...", Toast.LENGTH_SHORT).show();
        AppExecutors.runOnSingle(() -> {
            String result;
            try {
                if (!Shizuku.pingBinder() || Shizuku.checkSelfPermission() != PackageManager.PERMISSION_GRANTED) {
                    result = "Shizuku is not running or authorized.";
                } else {
                    result = runGameHubShizukuCommand(buildGameHubShortcutDumpCommand(true));
                }
            } catch (Throwable t) {
                result = "Generating diagnostic failed:" + t.getClass().getName() + "\n" + String.valueOf(t.getMessage());
            }
            final String text = result == null ? "" : (result.length() > 180000 ? result.substring(0, 180000) + "\n--- truncated by YukiHub ---" : result);
            runOnUiThread(() -> {
                try {
                    Object service = getSystemService(Context.CLIPBOARD_SERVICE);
                    if (service instanceof android.content.ClipboardManager) {
                        ((android.content.ClipboardManager) service).setPrimaryClip(android.content.ClipData.newPlainText("YukiHub GameHub shortcut diagnostics", text));
                        Toast.makeText(this, "The diagnostic information has been copied and can be sent to developers for troubleshooting.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "Copy failed: Unable to obtain clipboard service", Toast.LENGTH_LONG).show();
                    }
                } catch (Throwable t) {
                    Toast.makeText(this, "Replication diagnostics failed:" + t.getClass().getSimpleName(), Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    private String readProcessStream(InputStream in) {
        if (in == null) return "";
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[4096];
            int n;
            while ((n = in.read(buf)) >= 0) bos.write(buf, 0, n);
            return bos.toString("UTF-8");
        } catch (Throwable ignored) {
            return "";
        }
    }

    private List<GameHubShortcutItem> loadGameHubShortcutsFromExternalLogs() {
        List<GameHubShortcutItem> items = new ArrayList<>();
        String[] roots = new String[]{
                "/sdcard/Android/data/com.xiaoji.egggamz/files/log",
                "/sdcard/Android/data/com.xiaoji.egggamz/files/logs",
                "/sdcard/Android/data/com.xiaoji.egggamz/files/Documents/XiaoKunLogcat",
                "/sdcard/Android/data/com.xiaoji.egggame/files/log",
                "/sdcard/Android/data/com.xiaoji.egggame/files/logs",
                "/sdcard/Android/data/com.xiaoji.egggame/files/Documents/XiaoKunLogcat"
        };
        java.util.HashSet<String> seen = new java.util.HashSet<>();
        for (String root : roots) {
            collectGameHubShortcutItemsFromDir(new File(root), items, seen, 2);
        }
        items.sort((a, b) -> a.displayLabel.compareToIgnoreCase(b.displayLabel));
        return items;
    }

    private void collectGameHubShortcutItemsFromDir(File dir, List<GameHubShortcutItem> out, java.util.HashSet<String> seen, int depth) {
        if (dir == null || out == null || seen == null || depth < 0 || !dir.exists()) return;
        File[] files = dir.listFiles();
        if (files == null) return;
        for (File f : files) {
            if (f == null) continue;
            if (f.isDirectory()) {
                collectGameHubShortcutItemsFromDir(f, out, seen, depth - 1);
                continue;
            }
            String name = f.getName() == null ? "" : f.getName().toLowerCase(Locale.ROOT);
            if (!(name.endsWith(".txt") || name.endsWith(".log") || name.endsWith(".json") || name.endsWith(".xml"))) continue;
            if (f.length() > 1024L * 1024L * 4L) continue;
            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (!line.contains("localGameId") && !line.contains("local_") && !line.contains("steamAppId") && !line.contains("steamAppid")) continue;
                    GameHubShortcutItem item = parseGameHubShortcutText(line);
                    if (item == null || item.localGameId.isEmpty() || seen.contains(item.localGameId)) continue;
                    seen.add(item.localGameId);
                    out.add(item);
                }
            } catch (Throwable ignored) { }
        }
    }

    private String extractGameHubLocalGameId(ShortcutInfo si) {
        if (si == null) return null;
        try {
            Intent[] intents = si.getIntents();
            if (intents != null && intents.length > 0) {
                for (int i = intents.length - 1; i >= 0; i--) {
                    Intent intent = intents[i];
                    if (intent == null) continue;
                    Bundle extras = intent.getExtras();
                    if (extras != null) {
                        String localGameId = extras.getString("localGameId");
                        if (localGameId != null && !localGameId.trim().isEmpty()) return localGameId.trim();
                        GameHubShortcutItem fromExtras = parseGameHubShortcutText(extras.toString());
                        if (fromExtras != null && fromExtras.localGameId != null && !fromExtras.localGameId.trim().isEmpty()) return fromExtras.localGameId.trim();
                    }
                    GameHubShortcutItem fromIntent = parseGameHubShortcutText(intent.toUri(0));
                    if (fromIntent != null && fromIntent.localGameId != null && !fromIntent.localGameId.trim().isEmpty()) return fromIntent.localGameId.trim();
                }
            }
        } catch (Throwable ignored) { }
        try {
            PersistableBundle extras = si.getExtras();
            if (extras != null) {
                String localGameId = extras.getString("localGameId");
                if (localGameId != null && !localGameId.trim().isEmpty()) return localGameId.trim();
                GameHubShortcutItem fromExtras = parseGameHubShortcutText(extras.toString());
                if (fromExtras != null && fromExtras.localGameId != null && !fromExtras.localGameId.trim().isEmpty()) return fromExtras.localGameId.trim();
            }
        } catch (Throwable ignored) { }
        try {
            GameHubShortcutItem fromShortcut = parseGameHubShortcutText(si.toString());
            if (fromShortcut != null && fromShortcut.localGameId != null && !fromShortcut.localGameId.trim().isEmpty()) return fromShortcut.localGameId.trim();
        } catch (Throwable ignored) { }
        return null;
    }

    private String extractGameHubLocalAppName(ShortcutInfo si) {
        if (si == null) return "";
        try {
            Intent[] intents = si.getIntents();
            if (intents != null && intents.length > 0) {
                for (int i = intents.length - 1; i >= 0; i--) {
                    Intent intent = intents[i];
                    if (intent == null) continue;
                    Bundle extras = intent.getExtras();
                    if (extras != null) {
                        String name = extras.getString("localAppName");
                        if (name != null && !name.trim().isEmpty()) return name.trim();
                        name = extractGameHubNameFromText(extras.toString());
                        if (name != null && !name.trim().isEmpty()) return name.trim();
                    }
                    String name = extractGameHubNameFromText(intent.toUri(0));
                    if (name != null && !name.trim().isEmpty()) return name.trim();
                }
            }
        } catch (Throwable ignored) { }
        try {
            PersistableBundle extras = si.getExtras();
            if (extras != null) {
                String name = extras.getString("localAppName");
                if (name != null && !name.trim().isEmpty()) return name.trim();
                name = extractGameHubNameFromText(extras.toString());
                if (name != null && !name.trim().isEmpty()) return name.trim();
            }
        } catch (Throwable ignored) { }
        try {
            String name = extractGameHubNameFromText(si.toString());
            if (name != null && !name.trim().isEmpty()) return name.trim();
        } catch (Throwable ignored) { }
        CharSequence shortLabel = null;
        try { shortLabel = si.getShortLabel(); } catch (Throwable ignored) { }
        return shortLabel == null ? "" : shortLabel.toString();
    }

    private static class GameHubShortcutItem {
        final String displayLabel;
        final String localAppName;
        final String localGameId;
        Drawable icon;
        GameHubShortcutItem(String displayLabel, String localAppName, String localGameId) {
            this(displayLabel, localAppName, localGameId, null);
        }
        GameHubShortcutItem(String displayLabel, String localAppName, String localGameId, Drawable icon) {
            this.displayLabel = displayLabel == null ? "" : displayLabel;
            this.localAppName = localAppName == null ? "" : localAppName;
            this.localGameId = localGameId == null ? "" : localGameId;
            this.icon = icon;
        }
    }

    private void showInstalledAppPicker(EditText target, EditText titleTarget) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_app_picker);
        tintDialogRoot(dialog.findViewById(android.R.id.content));
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.getWindow().setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.74f), (int) (getResources().getDisplayMetrics().heightPixels * 0.82f));
        }
        RecyclerView rv = dialog.findViewById(R.id.recyclerAppPicker);
        View loading = dialog.findViewById(R.id.layoutAppLoading);
        TextView hint = dialog.findViewById(R.id.tvAppPickerHint);
        EditText search = dialog.findViewById(R.id.etAppSearch);
        rv.setLayoutManager(new LinearLayoutManager(this));
        dialog.findViewById(R.id.btnCloseAppPicker).setOnClickListener(v -> dialog.dismiss());
        dialog.show();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.74f), (int) (getResources().getDisplayMetrics().heightPixels * 0.82f));
        }

        AppExecutors.runOnIo(() -> {
            List<AppPickItem> items = loadLaunchableAppsForPicker();
            runOnUiThread(() -> {
                if (!dialog.isShowing()) return;
                loading.setVisibility(View.GONE);
                rv.setVisibility(View.VISIBLE);
                if (items.isEmpty()) {
                    hint.setText("No launchable application found");
                    return;
                }
                hint.setText("Common" + items.size() + "A launchable application can be searched for application name or package name");
                final AppPickerAdapter[] adapterRef = new AppPickerAdapter[1];
                adapterRef[0] = new AppPickerAdapter(items, item -> {
                    target.setText(item.packageName);
                    if (titleTarget != null && (titleTarget.getText() == null || titleTarget.getText().toString().trim().isEmpty())) {
                        titleTarget.setText(item.label);
                    }
                    dialog.dismiss();
                });
                rv.setAdapter(adapterRef[0]);
                search.addTextChangedListener(new TextWatcher() {
                    public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
                    public void onTextChanged(CharSequence s, int st, int b, int c) {
                        if (adapterRef[0] == null) return;
                        adapterRef[0].filter(s == null ? "" : s.toString());
                        hint.setText("Common" + items.size() + "Apps, currently showing" + adapterRef[0].getItemCount() + " 个");
                    }
                    public void afterTextChanged(Editable e) {}
                });
            });
        });
    }

    private interface AppPickCallback { void onPick(AppPickItem item); }

    private List<AppPickItem> loadLaunchableAppsForPicker() {
        LinkedHashMap<String, AppPickItem> map = new LinkedHashMap<>();
        try {
            PackageManager pm = getPackageManager();
            Intent launcher = new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER);
            List<ResolveInfo> launchers = pm.queryIntentActivities(launcher, 0);
            if (launchers != null) {
                for (ResolveInfo ri : launchers) {
                    if (ri == null || ri.activityInfo == null || ri.activityInfo.packageName == null) continue;
                    addAppPickItem(map, pm, ri.activityInfo.applicationInfo);
                }
            }
            List<ApplicationInfo> apps = pm.getInstalledApplications(PackageManager.GET_META_DATA);
            if (apps != null) {
                for (ApplicationInfo app : apps) {
                    if (app == null || app.packageName == null) continue;
                    if (pm.getLaunchIntentForPackage(app.packageName) != null) addAppPickItem(map, pm, app);
                }
            }
        } catch (Throwable t) {
            Log.w("YukiHub", "load launchable apps failed", t);
        }
        List<AppPickItem> items = new ArrayList<>(map.values());
        items.sort((a, b) -> a.label.compareToIgnoreCase(b.label));
        return items;
    }

    private void addAppPickItem(Map<String, AppPickItem> map, PackageManager pm, ApplicationInfo app) {
        if (map == null || pm == null || app == null || app.packageName == null) return;
        String key = app.packageName;
        if (map.containsKey(key)) return;
        String label;
        try { label = String.valueOf(pm.getApplicationLabel(app)); }
        catch (Throwable ignored) { label = app.packageName; }
        Drawable icon = null;
        try { icon = pm.getApplicationIcon(app); } catch (Throwable ignored) { }
        map.put(key, new AppPickItem(label, app.packageName, icon));
    }

    private static class AppPickItem {
        final String label;
        final String packageName;
        final Drawable icon;
        AppPickItem(String label, String packageName, Drawable icon) {
            this.label = label == null ? "" : label;
            this.packageName = packageName == null ? "" : packageName;
            this.icon = icon;
        }
    }

    private class AppPickerAdapter extends RecyclerView.Adapter<AppPickerAdapter.Holder> {
        private final List<AppPickItem> allItems;
        private final List<AppPickItem> items = new ArrayList<>();
        private final AppPickCallback callback;
        AppPickerAdapter(List<AppPickItem> items, AppPickCallback callback) {
            this.allItems = items == null ? new ArrayList<>() : new ArrayList<>(items);
            this.items.addAll(this.allItems);
            this.callback = callback;
        }
        void filter(String query) {
            String q = query == null ? "" : query.trim().toLowerCase(Locale.ROOT);
            items.clear();
            if (q.isEmpty()) {
                items.addAll(allItems);
            } else {
                for (AppPickItem item : allItems) {
                    String label = item.label == null ? "" : item.label.toLowerCase(Locale.ROOT);
                    String pkg = item.packageName == null ? "" : item.packageName.toLowerCase(Locale.ROOT);
                    if (label.contains(q) || pkg.contains(q)) items.add(item);
                }
            }
            notifyDataSetChanged();
        }
        @Override public Holder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app_picker, parent, false);
            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, dp(76));
            lp.setMargins(0, 0, 0, dp(8));
            v.setLayoutParams(lp);
            return new Holder(v);
        }
        @Override public void onBindViewHolder(Holder h, int position) {
            AppPickItem item = items.get(position);
            h.label.setText(emptyText(item.label, item.packageName));
            h.pkg.setText(item.packageName);
            if (item.icon != null) h.icon.setImageDrawable(item.icon); else h.icon.setImageResource(android.R.mipmap.sym_def_app_icon);
            h.itemView.setOnClickListener(v -> { if (callback != null) callback.onPick(item); });
            // Dynamic theme text colors
            DynamicTheme dt = DynamicTheme.getInstance();
            if (dt.isEnabled() && dt.getColors() != null) {
                h.label.setTextColor(0xFFF0F4FA);
                h.pkg.setTextColor(0xFFB0B8C8);
            }
        }
        @Override public int getItemCount() { return items.size(); }
        class Holder extends RecyclerView.ViewHolder {
            ImageView icon;
            TextView label, pkg;
            Holder(View itemView) {
                super(itemView);
                icon = itemView.findViewById(R.id.ivAppIcon);
                label = itemView.findViewById(R.id.tvAppLabel);
                pkg = itemView.findViewById(R.id.tvAppPackage);
            }
        }
    }

private String displayPath(String value) {
        if (value == null || value.trim().isEmpty()) return "No game directory selected";
        String s = value.trim();
        if (s.startsWith("file://")) {
            try {
                String path = Uri.parse(s).getPath();
                return path == null || path.isEmpty() ? s.substring("file://".length()) : path;
            } catch (Throwable ignored) {
                return s.substring("file://".length());
            }
        }
        if (s.startsWith("content://")) {
            String path = documentUriToPath(s);
            if (path != null && !path.isEmpty()) return path;
        }
        return s;
    }

    private String documentUriToPath(String value) {
        try {
            Uri uri = Uri.parse(value);
            String docId = null;
            // DocumentFile.fromTreeUri(...).listFiles() 得到的子目录 URI 通常是：
            // content://.../tree/primary%3AGames/document/primary%3AGames%2FExample
            // 详情页要显示到真正的游戏子目录，所以优先取 documentId，而不是 treeId。
            try {
                docId = DocumentsContract.getDocumentId(uri);
            } catch (Throwable ignored) { }
            if (docId == null || docId.isEmpty()) {
                try {
                    docId = DocumentsContract.getTreeDocumentId(uri);
                } catch (Throwable ignored) { }
            }
            if (docId == null || docId.isEmpty()) {
                docId = uri.getLastPathSegment();
            }
            return documentIdToPath(docId);
        } catch (Throwable ignored) {
            return null;
        }
    }

    private String documentIdToPath(String docId) {
        if (docId == null || docId.trim().isEmpty()) return null;
        String id = Uri.decode(docId.trim());
        // 有些 fallback 可能拿到带前缀的片段，先剥掉 URI 结构前缀；
        // 但不能按最后一个 / 截断，因为 primary:Games/Example 里的 / 是真实路径层级。
        int docPrefix = id.indexOf("/document/");
        if (docPrefix >= 0) id = id.substring(docPrefix + "/document/".length());
        if (id.startsWith("document/")) id = id.substring("document/".length());
        int treePrefix = id.indexOf("/tree/");
        if (treePrefix >= 0) id = id.substring(treePrefix + "/tree/".length());
        if (id.startsWith("tree/")) id = id.substring("tree/".length());
        int colon = id.indexOf(':');
        if (colon < 0) return null;
        String volume = id.substring(0, colon);
        String rel = id.substring(colon + 1);
        if (rel.startsWith("/")) rel = rel.substring(1);
        if ("primary".equalsIgnoreCase(volume)) {
            return rel.isEmpty() ? "/storage/emulated/0" : "/storage/emulated/0/" + rel;
        }
        return rel.isEmpty() ? "/storage/" + volume : "/storage/" + volume + "/" + rel;
    }

    private void showEditDialog(Game game) {
        editingGameId = game == null ? -1L : game.id;
        pendingDirUri = game == null ? null : game.rootUri;
        pendingCoverUri = game == null ? null : game.coverUri;
        Dialog d = new Dialog(this); pendingEditDialog = d;
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.dialog_game_edit);
        styleGameEditActions((ViewGroup) d.findViewById(android.R.id.content), ActionButtonStyle.activeColors());
        // Apply dynamic theme to the dialog's root ScrollView
        View dialogRoot = d.findViewById(R.id.editDialogTitle);
        if (dialogRoot != null) {
            View scrollView = (View) dialogRoot.getParent();
            while (scrollView != null && !(scrollView instanceof ScrollView)) {
                ViewParent p = scrollView.getParent();
                scrollView = p instanceof View ? (View) p : null;
            }
            if (scrollView != null) tintDialogRoot(scrollView);
        }
        if (d.getWindow() != null) {
            d.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            d.getWindow().setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.82f), android.view.WindowManager.LayoutParams.WRAP_CONTENT);
        }
        ((TextView)d.findViewById(R.id.editDialogTitle)).setText(game == null ? "add game" : "Edit game");
        EditText title = d.findViewById(R.id.etGameTitle), pkg = d.findViewById(R.id.etEmulatorPackage), desc = d.findViewById(R.id.etDescription);
        EditText gamehubLocalGameId = d.findViewById(R.id.etGameHubLocalGameId);
        Spinner sp = d.findViewById(R.id.spEngine);
        Spinner launchSp = d.findViewById(R.id.spLaunchTarget);
        Spinner winlatorModeSp = d.findViewById(R.id.spWinlatorLaunchMode);
        Spinner gamehubModeSp = d.findViewById(R.id.spGameHubLaunchMode);
        View winlatorAdvancedLayout = d.findViewById(R.id.layoutWinlatorLaunchMode);
        View gamehubLaunchLayout = d.findViewById(R.id.layoutGameHubLaunch);
        View artemisVersionLayout = d.findViewById(R.id.layoutArtemisVersion);
        Button btnArtemisAuto = d.findViewById(R.id.btnArtemisAuto);
        Button btnArtemisStd = d.findViewById(R.id.btnArtemisStd);
        Button btnArtemisCompat = d.findViewById(R.id.btnArtemisCompat);
        Button btnArtemisCompatV2 = d.findViewById(R.id.btnArtemisCompatV2);
        TextView tvPlayTimeInfo = d.findViewById(R.id.tvPlayTimeInfo);
        View btnClearDir = d.findViewById(R.id.btnClearDir);
        btnClearDir.setOnClickListener(v -> {
            pendingDirUri = null;
            if (pendingEditDialog != null) {
                ((TextView) pendingEditDialog.findViewById(R.id.tvSelectedDir)).setText("No game directory selected");
                List<String> options = buildLaunchOptions(null);
                launchSp.setAdapter(krSpinnerAdapter(options.toArray(new String[0])));
                updateClearDirButton(btnClearDir, pendingDirUri);
            }
            Toast.makeText(MainActivity.this, "Game directory cleared", Toast.LENGTH_SHORT).show();
        });
        View btnPickEmulatorApp = d.findViewById(R.id.btnPickEmulatorApp);
        View btnResetEmulatorPackage = d.findViewById(R.id.btnResetEmulatorPackage);
        View btnPickGameHubShortcut = d.findViewById(R.id.btnPickGameHubShortcut);
        btnPickGameHubShortcut.setOnClickListener(v -> showGameHubShortcutPicker(title, pkg, gamehubLocalGameId));
        btnPickEmulatorApp.setOnClickListener(v -> showInstalledAppPicker(pkg, title));
        pkg.setOnClickListener(v -> showInstalledAppPicker(pkg, title));
        Runnable updateWinlatorAdvanced = () -> {
            String engine = sp.getSelectedItem() == null ? "" : sp.getSelectedItem().toString();
            // PC 引擎走 winlator-cn 外置启动协议，不消费 winlatorLaunchMode，隐藏旧引擎的高级选项
            boolean isWinlator = !"PC".equals(engine) && ("WINLATOR".equals(engine) || isWinlatorPackageName(pkg.getText() == null ? "" : pkg.getText().toString()));
            winlatorAdvancedLayout.setVisibility(isWinlator ? View.VISIBLE : View.GONE);
            gamehubLaunchLayout.setVisibility("GAMEHUB".equals(engine) ? View.VISIBLE : View.GONE);
        };
        Runnable updateAndroidVisibility = () -> {
            boolean isAndroid = "ANDROID".equals(sp.getSelectedItem() == null ? "" : sp.getSelectedItem().toString());
            View dirRow = d.findViewById(R.id.layoutDirRow);
            View btnPickDirView = d.findViewById(R.id.btnPickDir);
            View launchLabel = d.findViewById(R.id.tvLaunchTargetLabel);
            if (dirRow != null) dirRow.setVisibility(isAndroid ? View.GONE : View.VISIBLE);
            if (btnPickDirView != null) btnPickDirView.setVisibility(isAndroid ? View.GONE : View.VISIBLE);
            if (launchLabel != null) launchLabel.setVisibility(isAndroid ? View.GONE : View.VISIBLE);
            if (launchSp != null) launchSp.setVisibility(isAndroid ? View.GONE : View.VISIBLE);
        };
        btnResetEmulatorPackage.setOnClickListener(v -> {
            String engine = sp.getSelectedItem() == null ? "" : sp.getSelectedItem().toString();
            String defaultPkg = defaultEmulatorPackageForEngine(engine);
            pkg.setText(defaultPkg);
            if ("GAMEHUB".equals(engine)) gamehubLocalGameId.setText("");
            if ("ARTEMIS".equals(engine)) updateArtemisVersionButtons(defaultPkg, btnArtemisAuto, btnArtemisStd, btnArtemisCompat, btnArtemisCompatV2);
            updateWinlatorAdvanced.run();
            Toast.makeText(this, defaultPkg.isEmpty() ? "The default package name has been cleared" : "The default package name has been restored:" + defaultPkg, Toast.LENGTH_SHORT).show();
        });

        pkg.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
            public void onTextChanged(CharSequence s, int st, int b, int c) { updateWinlatorAdvanced.run(); }
            public void afterTextChanged(Editable e) {}
        });
        ArrayAdapter<String> spAdapter = krSpinnerAdapter(new String[]{"AUTO", "KIRIKIRI", "ONS", "TYRANO", "ARTEMIS", "FVP", "WINLATOR", "PC", "GAMEHUB", "PSP", "ANDROID", "UNKNOWN"});
        sp.setAdapter(spAdapter);
        ArrayAdapter<String> winlatorModeAdapter = krSpinnerAdapter(new String[]{"Boot into game", "Boot into program"});
        winlatorModeSp.setAdapter(winlatorModeAdapter);
        ArrayAdapter<String> gamehubModeAdapter = krSpinnerAdapter(new String[]{"Boot into game", "Boot into program"});
        gamehubModeSp.setAdapter(gamehubModeAdapter);
        List<String> launchOptions = buildLaunchOptions(pendingDirUri);
        ArrayAdapter<String> launchAdapter = krSpinnerAdapter(launchOptions.toArray(new String[0]));
        launchSp.setAdapter(launchAdapter);
        if (game != null) {
            tvPlayTimeInfo.setVisibility(View.VISIBLE);
            tvPlayTimeInfo.setText("Total duration:" + TimeFormatUtil.playTime(game.totalPlayTime) + "/ Recently played:" + TimeFormatUtil.date(game.lastPlayedAt));
        }
        btnArtemisAuto.setOnClickListener(v -> { pkg.setText(resolveArtemisPackageFromMarkers(pendingDirUri)); updateArtemisVersionButtons(pkg.getText().toString(), btnArtemisAuto, btnArtemisStd, btnArtemisCompat, btnArtemisCompatV2); });
        btnArtemisStd.setOnClickListener(v -> { pkg.setText("internal.artemis"); updateArtemisVersionButtons(pkg.getText().toString(), btnArtemisAuto, btnArtemisStd, btnArtemisCompat, btnArtemisCompatV2); });
        btnArtemisCompat.setOnClickListener(v -> { pkg.setText("internal.artemis.compat"); updateArtemisVersionButtons(pkg.getText().toString(), btnArtemisAuto, btnArtemisStd, btnArtemisCompat, btnArtemisCompatV2); });
        btnArtemisCompatV2.setOnClickListener(v -> { pkg.setText("internal.artemis.compat.v2"); updateArtemisVersionButtons(pkg.getText().toString(), btnArtemisAuto, btnArtemisStd, btnArtemisCompat, btnArtemisCompatV2); });
        if (game != null) {
            title.setText(game.title); pkg.setText(game.emulatorPackage); gamehubLocalGameId.setText(game.gamehubLocalGameId); updateWinlatorAdvanced.run(); desc.setText(game.description);
            winlatorModeSp.setSelection(winlatorModeIndex(game.winlatorLaunchMode));
            gamehubModeSp.setSelection(gamehubModeIndex(game.gamehubLaunchMode));
            sp.setSelection(engineIndex(game.engine));
            launchSp.setSelection(findLaunchSelection(launchOptions, game.launchTarget));
            ((TextView)d.findViewById(R.id.tvSelectedDir)).setText(emptyText(game.rootUri, "No game directory selected"));
            ((TextView)d.findViewById(R.id.tvSelectedCover)).setText(emptyText(game.coverUri, "No cover selected"));
            if (game.engine == EngineType.ARTEMIS) updateArtemisVersionButtons(pkg.getText().toString(), btnArtemisAuto, btnArtemisStd, btnArtemisCompat, btnArtemisCompatV2);
        } else if (pendingDirUri != null) {
            ((TextView)d.findViewById(R.id.tvSelectedDir)).setText(pendingDirUri);
        }
        updateClearDirButton(btnClearDir, pendingDirUri);
        updateAndroidVisibility.run();
        sp.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                String engine = (String) sp.getSelectedItem();
                boolean isArtemis = "ARTEMIS".equals(engine);
                boolean isGameHub = "GAMEHUB".equals(engine);
                artemisVersionLayout.setVisibility(isArtemis ? View.VISIBLE : View.GONE);
                pkg.setVisibility(isArtemis ? View.GONE : View.VISIBLE);
                if ((pkg.getText() == null || pkg.getText().toString().trim().isEmpty()) && "KIRIKIRI".equals(engine)) {
                    pkg.setText("internal.krkr");
                } else if ((pkg.getText() == null || pkg.getText().toString().trim().isEmpty()) && "TYRANO".equals(engine)) {
                    pkg.setText("internal.tyrano");
                } else if ((pkg.getText() == null || pkg.getText().toString().trim().isEmpty()) && "ONS".equals(engine)) {
                    pkg.setText("internal.ons");
                } else if ((pkg.getText() == null || pkg.getText().toString().trim().isEmpty()) && "ARTEMIS".equals(engine)) {
                    pkg.setText("internal.artemis");
                } else if ((pkg.getText() == null || pkg.getText().toString().trim().isEmpty()) && "FVP".equals(engine)) {
                    pkg.setText("internal.fvp");
                } else if ((pkg.getText() == null || pkg.getText().toString().trim().isEmpty()) && "WINLATOR".equals(engine)) {
                    pkg.setText(guessInstalledWinlatorPackage());
                } else if ((pkg.getText() == null || pkg.getText().toString().trim().isEmpty()) && "PC".equals(engine)) {
                    pkg.setText(WinlatorPcLauncher.PACKAGE_NAME);
                } else if ((pkg.getText() == null || pkg.getText().toString().trim().isEmpty()) && isGameHub) {
                    pkg.setText(guessInstalledGameHubPackage());
                } else if ((pkg.getText() == null || pkg.getText().toString().trim().isEmpty()) && "PSP".equals(engine)) {
                    pkg.setText("org.ppsspp.ppsspp");
                    // 检查PPSSPP是否安装，如果未安装给出提示
                    if (!EmulatorLauncher.isPPSSPPInstalled(MainActivity.this)) {
                        Toast.makeText(MainActivity.this, "Tip: PSP games need to install the PPSSPP emulator", Toast.LENGTH_LONG).show();
                    }
                }
                updateWinlatorAdvanced.run();
                updateAndroidVisibility.run();
                if (isArtemis) updateArtemisVersionButtons(pkg.getText().toString(), btnArtemisAuto, btnArtemisStd, btnArtemisCompat, btnArtemisCompatV2);
            }
            @Override public void onNothingSelected(android.widget.AdapterView<?> parent) { }
        });
        d.findViewById(R.id.btnPickDir).setOnClickListener(v -> {
            if (useBuiltinFileChooser()) {
                new com.yuki.yukihub.ui.filechooser.FileChooserDialog(this)
                    .setMode(com.yuki.yukihub.ui.filechooser.FileChooserDialog.Mode.DIRECTORY)
                    .setTitle("Select game directory")
                    .setOnFileSelectedListener(new com.yuki.yukihub.ui.filechooser.FileChooserDialog.OnFileSelectedListener() {
                        @Override public void onFileSelected(Uri uri, String path, String fileName) {}
                        @Override public void onDirectorySelected(Uri uri, String path) {
pendingDirUri = uri != null ? uri.toString() : path;
if (pendingCoverUri == null || pendingCoverUri.isEmpty()) {
                                Uri autoCover = findFirstLevelImage(pendingDirUri);
                                if (autoCover != null) pendingCoverUri = copyCoverToInternalStorage(autoCover);
                            }
                            if (pendingEditDialog != null) {
                                ((TextView) pendingEditDialog.findViewById(R.id.tvSelectedDir)).setText(pendingDirUri);
                                Spinner launchSp = pendingEditDialog.findViewById(R.id.spLaunchTarget);
                                List<String> options = buildLaunchOptions(pendingDirUri);
                                ArrayAdapter<String> adapter = krSpinnerAdapter(options.toArray(new String[0]));
                                launchSp.setAdapter(adapter);
                                ((TextView) pendingEditDialog.findViewById(R.id.tvSelectedCover)).setText(emptyText(pendingCoverUri, "No cover selected"));
                                updateClearDirButton(pendingEditDialog.findViewById(R.id.btnClearDir), pendingDirUri);
                            }
                        }
                    })
                    .setOnSafRequestListener(() -> editDirLauncher.launch(null))
                    .show();
            } else {
                editDirLauncher.launch(null);
            }
        });
        d.findViewById(R.id.btnPickCover).setOnClickListener(v -> coverLauncher.launch("image/*"));
        d.findViewById(R.id.btnCancel).setOnClickListener(v -> d.dismiss());
        d.findViewById(R.id.btnSave).setOnClickListener(v -> {
            if (title.getText().toString().trim().isEmpty()) { Toast.makeText(MainActivity.this, "Please fill in the title", Toast.LENGTH_SHORT).show(); return; }
            Game g = game == null ? new Game() : game;
            if ((pendingCoverUri == null || pendingCoverUri.isEmpty()) && pendingDirUri != null && !pendingDirUri.isEmpty()) {
                Uri autoCover = findFirstLevelImage(pendingDirUri);
                if (autoCover != null) pendingCoverUri = copyCoverToInternalStorage(autoCover);
            }
            g.title = title.getText().toString().trim(); g.rootUri = pendingDirUri == null ? "" : pendingDirUri; g.coverUri = pendingCoverUri; g.coverPersistUri = pendingCoverUri; g.coverSourceType = pendingCoverUri == null ? 0 : 1;
            g.engine = EngineType.fromString((String) sp.getSelectedItem()); if (g.engine == EngineType.AUTO) g.engine = EngineType.UNKNOWN;
            g.emulatorPackage = pkg.getText().toString().trim();
            g.gamehubLocalGameId = gamehubLocalGameId.getText().toString().trim();
            if (g.engine == EngineType.ARTEMIS) {
                g.emulatorPackage = normalizeArtemisPackage(g.emulatorPackage);
                if (!saveArtemisVersionMarker(g.rootUri, g.emulatorPackage)) {
                    Toast.makeText(this, "Failed to save Artemis compatible tag", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            if (g.engine == EngineType.ONS && (g.emulatorPackage == null || g.emulatorPackage.trim().isEmpty())) g.emulatorPackage = "internal.ons";
            if (g.engine == EngineType.WINLATOR && (g.emulatorPackage == null || g.emulatorPackage.trim().isEmpty())) g.emulatorPackage = guessInstalledWinlatorPackage();
            if (g.engine == EngineType.PC && (g.emulatorPackage == null || g.emulatorPackage.trim().isEmpty())) g.emulatorPackage = WinlatorPcLauncher.PACKAGE_NAME;
            if (g.engine == EngineType.GAMEHUB && (g.emulatorPackage == null || g.emulatorPackage.trim().isEmpty())) g.emulatorPackage = guessInstalledGameHubPackage();
            if (g.engine == EngineType.PSP && (g.emulatorPackage == null || g.emulatorPackage.trim().isEmpty())) g.emulatorPackage = "org.ppsspp.ppsspp";
            if (g.engine != EngineType.GAMEHUB) g.gamehubLocalGameId = "";
            if (g.engine == EngineType.ANDROID) {
                String androidPkg = pkg.getText() == null ? "" : pkg.getText().toString().trim();
                if (androidPkg.isEmpty()) { Toast.makeText(MainActivity.this, "Please select the Android application to start first", Toast.LENGTH_SHORT).show(); return; }
                g.rootUri = "";
                g.winlatorLaunchMode = "game";
                g.gamehubLaunchMode = "game";
            }
            g.winlatorLaunchMode = (g.engine == EngineType.WINLATOR || (g.engine != EngineType.PC && isWinlatorPackageName(g.emulatorPackage))) ? winlatorModeValue(winlatorModeSp.getSelectedItemPosition()) : "game";
            g.gamehubLaunchMode = g.engine == EngineType.GAMEHUB ? gamehubModeValue(gamehubModeSp.getSelectedItemPosition()) : "game";
            String selectedLaunchTarget = (String) launchSp.getSelectedItem();
            if (g.engine == EngineType.ANDROID) selectedLaunchTarget = "";
            if (g.engine == EngineType.ARTEMIS || g.engine == EngineType.TYRANO || g.engine == EngineType.FVP) selectedLaunchTarget = "[Game Catalog]";
            if (g.engine == EngineType.GAMEHUB) selectedLaunchTarget = "[GameHub]";
            g.launchTarget = selectedLaunchTarget;
            g.description = desc.getText().toString();
            if (game == null) repository.insert(g); else repository.update(g);
            // 标题/封面可能变了，刷新已固定到桌面的快捷方式（没有则静默跳过）
            if (game != null) {
                final long shortcutGameId = g.id;
                final String shortcutTitle = g.title;
                final String shortcutCover = safeCoverUri(g);
                AppExecutors.runOnIo(() -> com.yuki.yukihub.shortcut.GameShortcutManager
                        .updateIfExists(MainActivity.this, shortcutGameId, shortcutTitle,
                                shortcutCover, MainActivity.class));
            }
            d.dismiss(); loadGames();
        });
        d.setOnDismissListener(x -> {
            pendingEditDialog = null;
            editingGameId = -1L;
        });
        d.show();
        if (d.getWindow() != null) {
            d.getWindow().setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.82f), android.view.WindowManager.LayoutParams.WRAP_CONTENT);
        }
    }

    /**
     * Activity 重建后恢复编辑对话框（选封面/选目录期间进程被系统回收的场景）。
     * 已保存的目录/封面/表单字段会回填，用户无需重新输入。
     */
    private void restoreEditDialog(Bundle state) {
        if (state == null || isFinishing() || isDestroyed()) return;
        // 对话框可能已在重建流程中被重新打开（比如 coverLauncher 回调兜底），
        // 此时只需用快照刷新封面/目录显示，避免重复弹窗。
        if (pendingEditDialog != null && pendingEditDialog.isShowing()) {
            String cover = state.getString(KEY_EDIT_COVER_URI);
            if (cover != null) {
                pendingCoverUri = cover;
                TextView coverTv = pendingEditDialog.findViewById(R.id.tvSelectedCover);
                if (coverTv != null) coverTv.setText(cover);
            }
            return;
        }
        long savedGameId = state.getLong(KEY_EDIT_GAME_ID, -1L);
        Game game = null;
        if (savedGameId > 0) {
            for (Game g : allGames) {
                if (g != null && g.id == savedGameId) { game = g; break; }
            }
            // allGames 未加载完时兜底：直接查库
            if (game == null) {
                try {
                    java.util.List<Game> all = repository.getAll();
                    if (all != null) {
                        for (Game g : all) {
                            if (g != null && g.id == savedGameId) { game = g; break; }
                        }
                    }
                } catch (Throwable ignored) { }
            }
        }
        final String savedDir = state.getString(KEY_EDIT_DIR_URI);
        final String savedCover = state.getString(KEY_EDIT_COVER_URI);
        showEditDialog(game);
        if (pendingEditDialog == null) return;
        // showEditDialog 开头会用 game 的 rootUri/coverUri 重置 pending 字段，这里恢复为快照值
        pendingDirUri = savedDir;
        pendingCoverUri = savedCover;
        // 引擎选择会触发 listener（联动 Artemis 版本按钮、包名默认值等），必须先设置引擎再回填表单文本
        Spinner engineSp = pendingEditDialog.findViewById(R.id.spEngine);
        String engine = state.getString(KEY_EDIT_ENGINE, "");
        if (engineSp != null && !engine.isEmpty()) {
            ArrayAdapter<String> ad = (ArrayAdapter<String>) engineSp.getAdapter();
            for (int i = 0; ad != null && i < ad.getCount(); i++) {
                if (engine.equals(ad.getItem(i))) { engineSp.setSelection(i); break; }
            }
        }
        // 回填表单（showEditDialog 已按 game 填过默认值，这里用快照覆盖为用户编辑中的值）
        EditText title = pendingEditDialog.findViewById(R.id.etGameTitle);
        if (title != null) title.setText(state.getString(KEY_EDIT_TITLE, ""));
        EditText pkg = pendingEditDialog.findViewById(R.id.etEmulatorPackage);
        if (pkg != null) pkg.setText(state.getString(KEY_EDIT_PKG, ""));
        EditText desc = pendingEditDialog.findViewById(R.id.etDescription);
        if (desc != null) desc.setText(state.getString(KEY_EDIT_DESC, ""));
        EditText ghId = pendingEditDialog.findViewById(R.id.etGameHubLocalGameId);
        if (ghId != null) ghId.setText(state.getString(KEY_EDIT_GAMEHUB_ID, ""));
        ((TextView) pendingEditDialog.findViewById(R.id.tvSelectedDir)).setText(emptyText(pendingDirUri, "No game directory selected"));
        ((TextView) pendingEditDialog.findViewById(R.id.tvSelectedCover)).setText(emptyText(pendingCoverUri, "No cover selected"));
        updateClearDirButton(pendingEditDialog.findViewById(R.id.btnClearDir), pendingDirUri);
        Spinner launchSp = pendingEditDialog.findViewById(R.id.spLaunchTarget);
        // 恢复的目录可能与 game.rootUri 不同（用户改过目录但未保存），按快照目录重建启动项列表
        String gameRoot = game == null ? null : game.rootUri;
        boolean dirChanged = savedDir != null && !savedDir.equals(gameRoot);
        if (launchSp != null && dirChanged) {
            List<String> options = buildLaunchOptions(pendingDirUri);
            launchSp.setAdapter(krSpinnerAdapter(options.toArray(new String[0])));
        }
        String launchTarget = state.getString(KEY_EDIT_LAUNCH_TARGET, "");
        if (launchSp != null && !launchTarget.isEmpty()) {
            ArrayAdapter<String> ad = (ArrayAdapter<String>) launchSp.getAdapter();
            for (int i = 0; ad != null && i < ad.getCount(); i++) {
                if (launchTarget.equals(ad.getItem(i))) { launchSp.setSelection(i); break; }
            }
        }
        Spinner winSp = pendingEditDialog.findViewById(R.id.spWinlatorLaunchMode);
        String winMode = state.getString(KEY_EDIT_WINLATOR_MODE, "");
        if (winSp != null && !winMode.isEmpty()) {
            ArrayAdapter<String> ad = (ArrayAdapter<String>) winSp.getAdapter();
            for (int i = 0; ad != null && i < ad.getCount(); i++) {
                if (winMode.equals(ad.getItem(i))) { winSp.setSelection(i); break; }
            }
        }
        Spinner ghSp = pendingEditDialog.findViewById(R.id.spGameHubLaunchMode);
        String ghMode = state.getString(KEY_EDIT_GAMEHUB_MODE, "");
        if (ghSp != null && !ghMode.isEmpty()) {
            ArrayAdapter<String> ad = (ArrayAdapter<String>) ghSp.getAdapter();
            for (int i = 0; ad != null && i < ad.getCount(); i++) {
                if (ghMode.equals(ad.getItem(i))) { ghSp.setSelection(i); break; }
            }
        }
        Toast.makeText(MainActivity.this, "The editing window has been restored, please check and save", Toast.LENGTH_SHORT).show();
    }

    private void updateClearDirButton(View btn, String dirUri) {
        boolean hasDir = dirUri != null && !dirUri.trim().isEmpty();
        btn.setEnabled(hasDir);
        btn.setAlpha(hasDir ? 1f : 0.45f);
    }

    private String defaultEmulatorPackageForEngine(String engine) {
        String e = engine == null ? "" : engine.trim().toUpperCase(Locale.ROOT);
        if ("KIRIKIRI".equals(e)) return "internal.krkr";
        if ("TYRANO".equals(e)) return "internal.tyrano";
        if ("ONS".equals(e)) return "internal.ons";
        if ("ARTEMIS".equals(e)) return "internal.artemis";
        if ("FVP".equals(e)) return "internal.fvp";
        if ("WINLATOR".equals(e)) return guessInstalledWinlatorPackage();
        if ("PC".equals(e)) return WinlatorPcLauncher.PACKAGE_NAME;
        if ("GAMEHUB".equals(e)) return guessInstalledGameHubPackage();
        return "";
    }

    private void updateArtemisVersionButtons(String value, Button auto, Button std, Button compat, Button compatV2) {
String pkg = normalizeArtemisPackage(value);
boolean isCompat = "internal.artemis.compat".equalsIgnoreCase(pkg);
boolean isV2 = "internal.artemis.compat.v2".equalsIgnoreCase(pkg);
boolean isStd = "internal.artemis".equalsIgnoreCase(pkg);
boolean autoMode = false;
auto.setSelected(autoMode);
std.setSelected(isStd);
compat.setSelected(isCompat);
compatV2.setSelected(isV2);
        auto.setAlpha(auto.isSelected() ? 1f : 0.55f);
        std.setAlpha(std.isSelected() ? 1f : 0.55f);
        compat.setAlpha(compat.isSelected() ? 1f : 0.55f);
        compatV2.setAlpha(compatV2.isSelected() ? 1f : 0.55f);
        for (Button button : new Button[]{auto, std, compat, compatV2}) {
            ActionButtonStyle.apply(button, button.isSelected() ? ActionButtonStyle.FILTER : ActionButtonStyle.TOOL);
        }
    }

    private String normalizeArtemisPackage(String value) {
String pkg = value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
if (pkg.contains("compat.v2") || pkg.contains("compatible_v2") || pkg.endsWith(".2")) return "internal.artemis.compat.v2";
if (pkg.contains("compat")) return "internal.artemis.compat";
return "internal.artemis";
}

private String resolveArtemisPackageFromMarkers(String rootUri) {
try {
DocumentFile dir = gameDir(rootUri);
if (dir != null) {
if (dir.findFile(".compatible_v2") != null || dir.findFile("compatible_v2.ini") != null) return "internal.artemis.compat.v2";
if (dir.findFile(".compatible") != null || dir.findFile("compatible.ini") != null) return "internal.artemis.compat";
}
} catch (Throwable ignored) { }
try {
String path = displayPath(rootUri);
if (path != null && path.startsWith("/")) {
if (new File(path, ".compatible_v2").exists() || new File(path, "compatible_v2.ini").exists()) return "internal.artemis.compat.v2";
if (new File(path, ".compatible").exists() || new File(path, "compatible.ini").exists()) return "internal.artemis.compat";
}
} catch (Throwable ignored) { }
return "internal.artemis";
}

private boolean saveArtemisVersionMarker(String rootUri, String artemisPackage) {
String mode = normalizeArtemisPackage(artemisPackage);
try {
DocumentFile dir = gameDir(rootUri);
if (dir != null) {
DocumentFile c1 = dir.findFile(".compatible");
DocumentFile c2 = dir.findFile(".compatible_v2");
DocumentFile i1 = dir.findFile("compatible.ini");
DocumentFile i2 = dir.findFile("compatible_v2.ini");
if ("internal.artemis".equals(mode)) return true;
if (c1 != null) c1.delete();
if (c2 != null) c2.delete();
if (i1 != null) i1.delete();
if (i2 != null) i2.delete();
if ("internal.artemis.compat".equals(mode)) return dir.createFile("application/octet-stream", ".compatible") != null;
if ("internal.artemis.compat.v2".equals(mode)) return dir.createFile("application/octet-stream", ".compatible_v2") != null;
return true;
}
} catch (Throwable ignored) { }
try {
String path = displayPath(rootUri);
if (path == null || !path.startsWith("/")) return false;
File c1 = new File(path, ".compatible");
File c2 = new File(path, ".compatible_v2");
File i1 = new File(path, "compatible.ini");
File i2 = new File(path, "compatible_v2.ini");
if ("internal.artemis".equals(mode)) return true;
deleteFileQuietly(c1);
deleteFileQuietly(c2);
deleteFileQuietly(i1);
deleteFileQuietly(i2);
if ("internal.artemis.compat".equals(mode)) return c1.exists() || c1.createNewFile();
if ("internal.artemis.compat.v2".equals(mode)) return c2.exists() || c2.createNewFile();
return true;
} catch (Throwable ignored) {
return false;
}
}

private DocumentFile gameDir(String rootUri) {
return documentDir(rootUri);
}

private DocumentFile documentDir(String value) {
if (value == null || value.trim().isEmpty()) return null;
String s = value.trim();
if (s.startsWith("/") || s.startsWith("file://")) {
File file = fileFromRootUri(s);
return file == null ? null : DocumentFile.fromFile(file);
}
try {
Uri uri = Uri.parse(s);
if ("content".equalsIgnoreCase(uri.getScheme())) {
DocumentFile documentTree = documentBackedTreeDir(uri);
if (documentTree != null) return documentTree;
}
return DocumentFile.fromTreeUri(this, uri);
} catch (Throwable ignored) {
return null;
}
}

private DocumentFile documentBackedTreeDir(Uri uri) {
try {
if (uri == null || uri.getAuthority() == null) return null;
String path = uri.getPath();
if (path == null || !path.contains("/document/")) return null;
String docId = DocumentsContract.getDocumentId(uri);
if (docId == null || docId.trim().isEmpty()) return null;
Uri treeUri = DocumentsContract.buildTreeDocumentUri(uri.getAuthority(), docId);
DocumentFile dir = DocumentFile.fromTreeUri(this, treeUri);
return dir != null && dir.isDirectory() ? dir : null;
} catch (Throwable ignored) {
return null;
}
}

private File fileFromRootUri(String value) {
if (value == null || value.trim().isEmpty()) return null;
String s = value.trim();
if (s.startsWith("file://")) {
String path = Uri.parse(s).getPath();
return path == null || path.isEmpty() ? null : new File(path);
}
return new File(s);
}

private void deleteFileQuietly(File file) {
try {
if (file != null && file.exists()) file.delete();
} catch (Throwable ignored) { }
}

private void showEditPlayTimeDialog(Game game) {
        if (game == null || game.id <= 0) return;
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundResource(R.drawable.bg_dialog);
        tintDialogRoot(root);
        int pad = dp(16);
        root.setPadding(pad, dp(12), pad, dp(10));

        TextView info = new TextView(this);
        info.setText("Current total duration:" + TimeFormatUtil.playTime(game.totalPlayTime) + "Recently played:" + TimeFormatUtil.date(game.lastPlayedAt));
        info.setTextColor(getColorCompat(R.color.yh_text_muted));
        info.setTextSize(12);
        info.setLineSpacing(dp(2), 1.0f);
        root.addView(info);

        TextView totalLabel = new TextView(this);
        totalLabel.setText("Reset the total duration (leave blank to not change)");
        totalLabel.setTextColor(getColorCompat(R.color.yh_text));
        totalLabel.setTextSize(14);
        totalLabel.setTypeface(null, android.graphics.Typeface.BOLD);
        root.addView(totalLabel);

        // 这里不再预填当前时长：预填会让「只想追加」的用户也带上一个总时长值，
        // 从而误触重设路径（重设会清空历史游玩记录）。留空 = 不改，语义才清晰。
        EditText totalInput = krEdit("Leave blank to not change, currently" + parseDurationForEdit(game.totalPlayTime), "");
        root.addView(totalInput, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp(42)));

        TextView addLabel = new TextView(this);
        addLabel.setText("Add play time");
        addLabel.setTextColor(getColorCompat(R.color.yh_text));
        addLabel.setTextSize(14);
        addLabel.setTypeface(null, android.graphics.Typeface.BOLD);
        root.addView(addLabel);

        EditText addInput = krEdit("For example 30m / 1h30m / 0.5h", "");
        root.addView(addInput, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp(42)));

        TextView hint = new TextView(this);
        hint.setText("Note: \"Additional play time\" only increases based on the current time, and a new play record will be added, and the historical record will not be affected. \"Reset total time\" will directly change the accumulated time to the filled-in value, and clear the existing play records of the game. After filling in, it will be confirmed again.");
        hint.setTextColor(getColorCompat(R.color.yh_text_muted));
        hint.setTextSize(11);
        hint.setLineSpacing(dp(2), 1.0f);
        hint.setPadding(0, dp(8), 0, 0);
        root.addView(hint);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Modify play time")
                .setView(root)
                .setPositiveButton("Save", null)
                .setNegativeButton("Cancel", null)
                .show();
        styleAlertDialogDark(dialog);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.52f), android.view.WindowManager.LayoutParams.WRAP_CONTENT);
        }
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            Long totalMs = parseDurationToMillis(totalInput.getText() == null ? "" : totalInput.getText().toString().trim());
            Long addMs = parseDurationToMillis(addInput.getText() == null ? "" : addInput.getText().toString().trim());
            if ((totalMs == null || totalMs < 0) && (addMs == null || addMs <= 0)) {
                Toast.makeText(MainActivity.this, "Please fill in the valid duration", Toast.LENGTH_SHORT).show();
                return;
            }
            // 重设与追加是两种语义，必须走不同的数据层方法：
            // - 重设：setManualPlayTimeForGame（清空该游戏的游玩记录 + 推进 playtime_reset_at），不可逆，需确认
            // - 追加：addManualPlayTime（只新增一条记录并累加总时长），历史记录与 reset_at 都不动
            final long addDelta = addMs != null && addMs > 0 ? addMs : 0L;
            if (totalMs != null && totalMs >= 0) {
                final long resetTo = Math.max(0L, totalMs + addDelta);
                AlertDialog confirm = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Confirm total reset time")
                        .setMessage("Will put \"" + game.title + "》The cumulative duration is reset to" + TimeFormatUtil.playTime(resetTo) + "。\n\n"
                                + "· Existing play records of this game will be cleared"
                                + "· The history entry of this game in the dynamic list will disappear"
                                + "· This operation is irreversible"
                                + "If you just want to increase the play time, please cancel and just fill in \"Add play time\".")
                        .setPositiveButton("Confirm reset", (d, w) -> {
                            repository.setManualPlayTimeForGame(game.id, resetTo);
                            Toast.makeText(MainActivity.this, "Play time has been reset", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            refreshAfterPlayTimeEdit(game);
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
                styleAlertDialogDark(confirm);
                return;
            }
            repository.addManualPlayTime(game.id, addDelta);
            Toast.makeText(MainActivity.this, "Added" + TimeFormatUtil.playTime(addDelta), Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            refreshAfterPlayTimeEdit(game);
        });
    }

    /** 时长编辑后统一刷新：游戏列表、侧栏详情、个人面板。 */
    private void refreshAfterPlayTimeEdit(Game game) {
        loadGames();
        if (game != null) {
            // loadGames 会重建 allGames，这里取回新实例，避免侧栏用到已失效的旧对象
            Game latest = null;
            for (Game g : allGames) {
                if (g != null && g.id == game.id) { latest = g; break; }
            }
            updateSideDetail(latest != null ? latest : game);
        }
        updateProfilePanel();
    }

    /**
     * 解析时长文本为毫秒。
     *
     * 之前返回分钟，导致 30s 这类输入被整除抹成 0（提示语却写着支持 7200s）。
     * 统一返回毫秒后秒级输入才能保真。
     */
    private Long parseDurationToMillis(String input) {
        if (input == null) return null;
        String s = input.trim().toLowerCase(Locale.ROOT);
        if (s.isEmpty()) return null;
        try {
            // 纯数字按分钟解释，保持与旧版输入习惯一致
            if (s.matches("^\\d+$")) return Long.parseLong(s) * 60_000L;
            long totalMs = 0L;
            java.util.regex.Matcher m = java.util.regex.Pattern.compile("(\\d+(?:\\.\\d+)?)\\s*([dhms])").matcher(s);
            boolean matched = false;
            while (m.find()) {
                matched = true;
                double value = Double.parseDouble(m.group(1));
                String unit = m.group(2);
                if ("d".equals(unit)) totalMs += (long) (value * 24d * 60d * 60d * 1000d);
                else if ("h".equals(unit)) totalMs += (long) (value * 60d * 60d * 1000d);
                else if ("m".equals(unit)) totalMs += (long) (value * 60d * 1000d);
                else if ("s".equals(unit)) totalMs += (long) (value * 1000d);
            }
            if (!matched) return null;
            return Math.max(0L, totalMs);
        } catch (Throwable ignored) {
            return null;
        }
    }

    private String parseDurationForEdit(long durationMs) {
        if (durationMs <= 0) return "0m";
        long minutes = durationMs / 60_000L;
        long hours = minutes / 60L;
        long remain = minutes % 60L;
        if (hours <= 0) return remain + "m";
        if (remain <= 0) return hours + "h";
        return hours + "h" + remain + "m";
    }

    /** 引擎下拉索引映射（必须与 spAdapter 的数组顺序一致：AUTO=0 起）。 */
    private int engineIndex(EngineType e) { if (e == EngineType.KIRIKIRI) return 1; if (e == EngineType.ONS) return 2; if (e == EngineType.TYRANO) return 3; if (e == EngineType.ARTEMIS) return 4; if (e == EngineType.FVP) return 5; if (e == EngineType.WINLATOR) return 6; if (e == EngineType.PC) return 7; if (e == EngineType.GAMEHUB) return 8; if (e == EngineType.PSP) return 9; if (e == EngineType.ANDROID) return 10; if (e == EngineType.UNKNOWN) return 11; return 0; }

    private boolean isWinlatorPackageName(String pkg) {
        if (pkg == null) return false;
        String p = pkg.trim().toLowerCase(Locale.ROOT);
        return p.equals("com.winlator")
                || p.startsWith("com.winlator.")
                || p.contains("winlator")
                || p.contains("glibc")
                || p.contains("proot")
                || p.contains("mobox")
                || p.contains("winalator");
    }

    private int winlatorModeIndex(String mode) {
        String m = mode == null ? "game" : mode.trim().toLowerCase(Locale.ROOT);
        if ("program".equals(m) || "normal".equals(m)) return 1;
        return 0;
    }

    private String winlatorModeValue(int index) {
        if (index == 1) return "program";
        return "game";
    }

    private int gamehubModeIndex(String mode) {
        String m = mode == null ? "game" : mode.trim().toLowerCase(Locale.ROOT);
        if ("program".equals(m) || "normal".equals(m)) return 1;
        return 0;
    }

    private String gamehubModeValue(int index) {
        if (index == 1) return "program";
        return "game";
    }

    /**
     * 游戏内虚拟鼠标设置（KRKR / Artemis 共用）。
     * 含内置箭头实时预览（与游戏内共用 GameCursorIconRenderer）。
     */
    private void showGameCursorDialog(Game game) {
        if (game == null) return;
        boolean krkr = game.engine == EngineType.KIRIKIRI;
        boolean artemis = game.engine == EngineType.ARTEMIS;
        boolean fvp = game.engine == EngineType.FVP;
        if (!krkr && !artemis && !fvp) {
            Toast.makeText(this, "This engine does not currently support virtual mouse", Toast.LENGTH_SHORT).show();
            return;
        }
        GameCursorConfig cfg = GameCursorConfig.load(this);
        gameCursorEditing = cfg;   // 供异步导入回调直接改这一份

        Dialog dialog = new Dialog(this);
        LinearLayout panel = new LinearLayout(this);
        panel.setOrientation(LinearLayout.VERTICAL);
        panel.setBackgroundColor(getColorCompat(com.yuki.yukihub.R.color.yh_card));
        int pad = (int) (18 * getResources().getDisplayMetrics().density);
        panel.setPadding(pad, pad, pad, pad);

        TextView title = new TextView(this);
        title.setText(krkr ? "KRKR virtual mouse" : (artemis ? "Artemis Virtual Mouse" : "FVP virtual mouse"));
        title.setTextColor(getColorCompat(com.yuki.yukihub.R.color.yh_text));
        title.setTextSize(22);
        title.setPadding(0, 0, 0, pad / 2);
        panel.addView(title);

        ScrollView scroll = new ScrollView(this);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        scroll.addView(root);
        panel.addView(scroll, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f));

        CheckBox enable = krCheckBox(krkr
                        ? "Enable KRKR virtual mouse (hover cursor, drag to position, raise hand to click)"
                        : (artemis
                        ? "Enable Artemis virtual mouse (hover cursor, drag to position, raise your hand to click)"
                        : "Enable FVP virtual mouse (hover cursor, drag to position, raise your hand to click)"),
                krkr ? cfg.krkrEnabled : (artemis ? cfg.artemisEnabled : cfg.fvpEnabled));
        root.addView(enable);

        // ===== 实时预览（与游戏内同一渲染代码）=====
        TextView previewLabel = krLabel("Cursor preview (consistent with in-game, red cross = mouse tip position)");
        root.addView(previewLabel);
        final CursorPreviewView preview = new CursorPreviewView(this, cfg);
        gameCursorPreview = preview;   // 供导入回调刷新
        root.addView(preview, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp(80)));

        TextView scaleLabel = krLabel("Cursor size");
        root.addView(scaleLabel);
        android.widget.SeekBar scaleSeek = new android.widget.SeekBar(this);
        scaleSeek.setMax(100);
        final float scaleSpan = GameCursorConfig.MAX_SCALE - GameCursorConfig.MIN_SCALE;
        scaleSeek.setProgress(Math.round(
                (gcClampScale(cfg.scale) - GameCursorConfig.MIN_SCALE) / scaleSpan * 100f));
        scaleSeek.setOnSeekBarChangeListener(new android.widget.SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(android.widget.SeekBar sb, int progress, boolean fromUser) {
                cfg.scale = GameCursorConfig.MIN_SCALE + progress / 100f * scaleSpan;
                preview.refresh(cfg);
            }
            @Override public void onStartTrackingTouch(android.widget.SeekBar sb) { }
            @Override public void onStopTrackingTouch(android.widget.SeekBar sb) { }
        });
        root.addView(scaleSeek);

        TextView alphaLabel = krLabel("Cursor opacity");
        root.addView(alphaLabel);
        android.widget.SeekBar alphaSeek = new android.widget.SeekBar(this);
        alphaSeek.setMax(80);
        alphaSeek.setProgress(Math.round((gcClampAlpha(cfg.alpha) - 0.2f) / 0.8f * 80f));
        alphaSeek.setOnSeekBarChangeListener(new android.widget.SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(android.widget.SeekBar sb, int progress, boolean fromUser) {
                cfg.alpha = 0.2f + progress / 80f * 0.8f;
                preview.refresh(cfg);
            }
            @Override public void onStartTrackingTouch(android.widget.SeekBar sb) { }
            @Override public void onStopTrackingTouch(android.widget.SeekBar sb) { }
        });
        root.addView(alphaSeek);

        TextView sensLabel = krLabel("Mouse mode movement sensitivity");
        root.addView(sensLabel);
        android.widget.SeekBar sensSeek = new android.widget.SeekBar(this);
        sensSeek.setMax(100);
        sensSeek.setProgress(Math.round((gcClampSens(cfg.sensitivity) - 0.5f) / 2.5f * 100f));
        sensSeek.setOnSeekBarChangeListener(new android.widget.SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(android.widget.SeekBar sb, int progress, boolean fromUser) {
                cfg.sensitivity = 0.5f + progress / 100f * 2.5f;
            }
            @Override public void onStartTrackingTouch(android.widget.SeekBar sb) { }
            @Override public void onStopTrackingTouch(android.widget.SeekBar sb) { }
        });
        root.addView(sensSeek);

        // ===== 光标外观来源 =====
        TextView srcLabel = krLabel("Cursor appearance");
        root.addView(srcLabel);

        TextView srcStatus = new TextView(this);
        gameCursorSourceStatus = srcStatus;   // 导入回调要更新它
        srcStatus.setTextColor(getColorCompat(com.yuki.yukihub.R.color.yh_text_muted));
        srcStatus.setTextSize(11);
        srcStatus.setPadding(0, 0, 0, dp(6));
        srcStatus.setText(cfg.hasWinCursor()
                ? "Current:" + cfg.winCursorName
                : (cfg.iconUri != null && !cfg.iconUri.isEmpty()
                ? "Current: Custom image" : "Current: Built-in arrows"));
        root.addView(srcStatus);

        // Windows 光标包：整包导入，能读 inf 就自动标出哪个是正常状态
        Button importScheme = krButton("Import Windows cursor package (folder)…");
        importScheme.setOnClickListener(v -> {
            try {
                cursorSchemePickerLauncher.launch(null);
            } catch (Throwable t) {
                Toast.makeText(this, "Unable to open file chooser", Toast.LENGTH_SHORT).show();
            }
        });
        root.addView(importScheme, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dp(44)));

        TextView schemeHint = new TextView(this);
        schemeHint.setText("Supports .ani (animation) and .cur (static). After selecting the folder containing the cursor file,"
                + "The cursors inside will be listed for you to select one. The package with AutoSetup.inf can automatically mark the \"normal state\" one."
                + "The animated cursor plays at original speed, with the position of the mouse tip taken directly from the file itself.");
        schemeHint.setTextColor(getColorCompat(com.yuki.yukihub.R.color.yh_text_muted));
        schemeHint.setTextSize(10);
        schemeHint.setPadding(0, dp(4), 0, dp(8));
        root.addView(schemeHint);

        LinearLayout iconRow = new LinearLayout(this);
        iconRow.setOrientation(LinearLayout.HORIZONTAL);
        Button pickIcon = krButton("Select image…");
        pickIcon.setOnClickListener(v -> cursorIconPickerLauncher.launch("image/*"));
        Button resetIcon = krButton("Restore default arrow");
        resetIcon.setOnClickListener(v -> {
            cfg.iconUri = null;
            // 同时卸掉光标包，否则"恢复默认"只清了 PNG 却仍显示光标包
            if (cfg.hasWinCursor()) {
                cfg.winCursorName = null;
                com.yuki.yukihub.gamecursor.wincursor.WinCursorSupport.uninstall(this);
            }
            cfg.save(this);
            preview.refresh(cfg);
            srcStatus.setText("Current: Built-in arrows");
            Toast.makeText(this, "Built-in arrows restored", Toast.LENGTH_SHORT).show();
        });
        iconRow.addView(pickIcon, new LinearLayout.LayoutParams(0, dp(44), 1f));
        iconRow.addView(resetIcon, new LinearLayout.LayoutParams(0, dp(44), 1f));
        root.addView(iconRow);

        // Artemis 专属：显示 native 注入通道状态 + 无障碍（现在只是备用通道）
        if (artemis) {
            TextView accStatus = new TextView(this);
            int probe = com.yuki.yukihub.gamecursor.ArtemisNativeInput.probe();
            boolean nativeOk = probe >= 1; // 库已加载且符号取到（2 才是引擎也就绪）
            boolean ready = isAccessibilityTapReady();
            accStatus.setText(nativeOk
                    ? "✓ Engine direct injection is available, no additional permissions required"
                    : (ready
                    ? "Engine direct connection is unavailable and will fall back to accessibility gestures"
                    : "⚠ Engine direct connection is not available and accessibility is not turned on: clicking will not take effect"));
            accStatus.setTextColor(getColorCompat(nativeOk || ready
                    ? com.yuki.yukihub.R.color.yh_text : com.yuki.yukihub.R.color.yh_warning));
            accStatus.setTextSize(12);
            accStatus.setPadding(0, dp(10), 0, dp(4));
            root.addView(accStatus);

            // 只在直连不可用时才提供无障碍入口，避免让人以为必须开
            if (!nativeOk) {
                Button accBtn = krButton(ready ? "Accessibility service settings (enabled)" : "Go to enable accessibility services (alternative channel)");
                accBtn.setOnClickListener(v -> {
                    try {
                        startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
                    } catch (Throwable t) {
                        Toast.makeText(this, "Unable to open system settings, please go to: Settings → Accessibility manually", Toast.LENGTH_LONG).show();
                    }
                });
                root.addView(accBtn, new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, dp(44)));
            }
        }

        TextView tip = new TextView(this);
        tip.setText("Usage: After entering the game, there is a mode float in the lower right corner (can be dragged to change position)."
                + "· Switch the point float to mouse mode (the float turns blue): slide the cursor anywhere on the full screen and tap in place = inject a click at the tip of the cursor."
                + "· Click again to switch back to direct touch (the float turns gray): the touch is given to the game as is, and the cursor is hidden."
                + (krkr
                ? "KRKR's click-and-go engine uses its own touch pipeline, and settings such as the original engine's \"virtual cursor scaling\" are not affected."
                : (artemis
                ? "Artemis' clicks and hovers are written directly to the engine input layer and are not dispatched through system input."
                + "Therefore no accessibility permissions are required. Cursor display still requires the \"Show on top of other apps\" permission."
                : "FVP's clicks and hovers are directly sent to the rfvp engine (rfvp_android_touch),"
                + "Hanging on the game window View tree, no floating window permission is required.")));
        tip.setTextColor(getColorCompat(com.yuki.yukihub.R.color.yh_text));
        tip.setTextSize(11);
        tip.setPadding(0, dp(10), 0, 0);
        root.addView(tip);

        LinearLayout actions = new LinearLayout(this);
        actions.setOrientation(LinearLayout.HORIZONTAL);
        Button cancel = krButton("Cancel");
        cancel.setOnClickListener(v -> dialog.dismiss());
        Button save = krButton("Save");
        save.setOnClickListener(v -> {
            // 只改当前引擎自己的开关，另一个引擎的设置必须原样保留。
            // 之前写成 cfg.krkrEnabled = krkr && checked，在 artemis 对话框里
            // krkr=false 会把 krkr 的开关直接清掉，导致"开一个关另一个"。
            if (krkr) {
                cfg.krkrEnabled = enable.isChecked();
            } else if (artemis) {
                cfg.artemisEnabled = enable.isChecked();
            } else {
                cfg.fvpEnabled = enable.isChecked();
                // 与「FVP 引擎设置」里的同名开关保持一致（那边是按游戏存的）
                try {
                    FvpLaunchPrefs fp = FvpLaunchPrefs.load(this, game.id, game.rootUri);
                    fp.virtualMouse = enable.isChecked();
                    fp.save(this, game.id);
                } catch (Throwable ignored) { }
            }
            cfg.save(this);
            // 只在 native 直连不可用时才提示无障碍：
            // 直连走引擎内部输入层，根本不需要无障碍权限。
            if (artemis && enable.isChecked()
                    && com.yuki.yukihub.gamecursor.ArtemisNativeInput.probe() < 1
                    && !isAccessibilityTapReady()) {
                promptAccessibilityForCursor(dialog);
                return;
            }
            Toast.makeText(this, "The virtual mouse settings have been saved and will take effect next time you enter the game.", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
        actions.addView(cancel, new LinearLayout.LayoutParams(0, dp(46), 1f));
        actions.addView(save, new LinearLayout.LayoutParams(0, dp(46), 1f));
        panel.addView(actions);

        // 对话框关掉后别再持有这些引用（异步回调会判空）
        dialog.setOnDismissListener(d -> {
            if (gameCursorPreview == preview) gameCursorPreview = null;
            if (gameCursorEditing == cfg) gameCursorEditing = null;
            if (gameCursorSourceStatus == srcStatus) gameCursorSourceStatus = null;
        });
        dialog.setContentView(panel);
        Window w = dialog.getWindow();
        if (w != null) {
            w.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            w.setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.72f),
                    (int) (getResources().getDisplayMetrics().heightPixels * 0.8f));
        }
        dialog.show();
    }

    /**
     * 光标包文件夹选中后的处理：扫描 → 弹角色选择列表。
     *
     * 有 inf 时能准确标出哪个是「正常状态」（inf 的 [Strings] 段 pointer= 那一项），
     * 没有 inf 就按文件名猜，猜不到就不标注让用户自己认 —— 有些包确实分不清。
     */
    private void onCursorSchemeFolderPicked(Uri treeUri) {
        try {
            // 持久化授权，否则下次读不到（虽然我们会立刻解码落盘，但扫描期间要用）
            getContentResolver().takePersistableUriPermission(treeUri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } catch (Throwable ignored) { }

        Toast.makeText(this, "Scanning cursor packages...", Toast.LENGTH_SHORT).show();
        new Thread(() -> {
            final com.yuki.yukihub.gamecursor.wincursor.CursorSchemeScanner.Scheme scheme =
                    com.yuki.yukihub.gamecursor.wincursor.CursorSchemeScanner.scan(this, treeUri);
            runOnUiThread(() -> {
                if (scheme.entries.isEmpty()) {
                    new AlertDialog.Builder(this)
                            .setTitle("Cursor file not found")
                            .setMessage("There are no .ani or .cur files in this folder."
                                    + "Please select the folder containing the cursor file (usually also an AutoSetup.inf).")
                            .setPositiveButton("Knew", null)
                            .show();
                    return;
                }
                showCursorSchemePickDialog(scheme);
            });
        }, "cursor-scan").start();
    }

    /** 光标包角色选择对话框：每项带动图预览，可直接看到长什么样。 */
    private void showCursorSchemePickDialog(
            com.yuki.yukihub.gamecursor.wincursor.CursorSchemeScanner.Scheme scheme) {
        Dialog dialog = new Dialog(this);
        LinearLayout panel = new LinearLayout(this);
        panel.setOrientation(LinearLayout.VERTICAL);
        panel.setBackgroundResource(com.yuki.yukihub.R.drawable.bg_dialog);
        panel.setPadding(dp(18), dp(18), dp(18), dp(18));

        TextView title = new TextView(this);
        title.setText(scheme.name);
        title.setTextColor(getColorCompat(com.yuki.yukihub.R.color.yh_text));
        title.setTextSize(17);
        title.setTypeface(null, android.graphics.Typeface.BOLD);
        panel.addView(title);

        TextView hint = new TextView(this);
        hint.setText(scheme.hasRoleInfo
                ? "The purpose of each cursor has been identified from the installation information. Choose one as the in-game cursor:"
                + "It is recommended to select \"Normal Selection\" - that is the normal pointer on the desktop."
                : "This package has no readable installation information to determine the purpose of each file."
                + "Listed below by file name, click on the preview to pick one yourself.");
        hint.setTextColor(getColorCompat(com.yuki.yukihub.R.color.yh_text_muted));
        hint.setTextSize(11);
        hint.setPadding(0, dp(8), 0, dp(10));
        panel.addView(hint);

        ScrollView scroll = new ScrollView(this);
        LinearLayout list = new LinearLayout(this);
        list.setOrientation(LinearLayout.VERTICAL);

        // 每项：动图预览 + 标签 + 帧数。解码放后台，避免一次点开卡住 UI。
        final List<CursorPreviewItemView> items = new ArrayList<>();
        for (com.yuki.yukihub.gamecursor.wincursor.CursorSchemeScanner.Entry e : scheme.entries) {
            CursorPreviewItemView item = new CursorPreviewItemView(this, e);
            item.setOnClickListener(v -> {
                dialog.dismiss();
                installCursorScheme(scheme.name, e);
            });
            list.addView(item, new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, dp(64)));
            items.add(item);
        }
        scroll.addView(list);
        panel.addView(scroll, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f));

        Button cancel = krButton("Cancel");
        cancel.setOnClickListener(v -> dialog.dismiss());
        panel.addView(cancel, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dp(46)));

        dialog.setOnDismissListener(d -> {
            for (CursorPreviewItemView it : items) it.release();
        });
        dialog.setContentView(panel);
        Window w = dialog.getWindow();
        if (w != null) {
            w.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            w.setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.8f),
                    (int) (getResources().getDisplayMetrics().heightPixels * 0.75f));
        }
        dialog.show();

        // 后台逐个解码并刷新预览
        new Thread(() -> {
            for (CursorPreviewItemView it : items) {
                it.decodeInBackground();
            }
        }, "cursor-preview").start();
    }

    /** 安装选中的光标为常态光标。 */
    private void installCursorScheme(
            String schemeName,
            com.yuki.yukihub.gamecursor.wincursor.CursorSchemeScanner.Entry entry) {
        Toast.makeText(this, "Importing…", Toast.LENGTH_SHORT).show();
        new Thread(() -> {
            final int frames = com.yuki.yukihub.gamecursor.wincursor.WinCursorSupport
                    .install(this, entry.uri, entry.fileName);
            runOnUiThread(() -> {
                if (frames <= 0) {
                    Toast.makeText(this, "This cursor file cannot be decoded, try another one.", Toast.LENGTH_LONG).show();
                    return;
                }
                final String name = schemeName + " · " + entry.label();
                // 关键：必须改设置对话框正在持有的那个 cfg 对象，不能另 load 一份。
                // 之前在新 load 的副本上改并保存，对话框里的 cfg 仍是旧快照，
                // 点"保存"时又用 winCursorName=null 把导入结果覆盖掉了 ——
                // 表现就是导入能预览、一动滑块就变回普通箭头、进游戏也没生效。
                if (gameCursorEditing != null) {
                    gameCursorEditing.winCursorName = name;
                    gameCursorEditing.iconUri = null;   // 光标包优先级高于 PNG
                    gameCursorEditing.save(this);
                } else {
                    GameCursorConfig cfg = GameCursorConfig.load(this);
                    cfg.winCursorName = name;
                    cfg.iconUri = null;
                    cfg.save(this);
                }
                if (gameCursorPreview != null) gameCursorPreview.refresh(gameCursorEditing);
                if (gameCursorSourceStatus != null) gameCursorSourceStatus.setText("Current:" + name);
                Toast.makeText(this,
                        frames > 1 ? "Imported (" + frames + "Frame animation)" : "Imported (static cursor)",
                        Toast.LENGTH_SHORT).show();
            });
        }, "cursor-install").start();
    }

    /**
     * 光标包选择列表里的一项：左边动图预览，右边名称与帧数。
     *
     * 预览直接播动画，这样「哪个是我想要的」一眼就能看出来，
     * 不用靠文件名猜。
     */
    private final class CursorPreviewItemView extends LinearLayout {
        private final com.yuki.yukihub.gamecursor.wincursor.CursorSchemeScanner.Entry entry;
        private final AnimatedCursorView icon;
        private final TextView label;

        CursorPreviewItemView(Context c,
                              com.yuki.yukihub.gamecursor.wincursor.CursorSchemeScanner.Entry e) {
            super(c);
            this.entry = e;
            setOrientation(HORIZONTAL);
            setGravity(android.view.Gravity.CENTER_VERTICAL);
            setPadding(dp(6), dp(6), dp(6), dp(6));
            setClickable(true);
            setBackgroundResource(com.yuki.yukihub.R.drawable.bg_game_card);

            icon = new AnimatedCursorView(c);
            addView(icon, new LinearLayout.LayoutParams(dp(48), dp(48)));

            label = new TextView(c);
            label.setText(e.label());
            label.setTextColor(getColorCompat(e.isNormal
                    ? com.yuki.yukihub.R.color.yh_primary : com.yuki.yukihub.R.color.yh_text));
            label.setTextSize(13);
            label.setPadding(dp(12), 0, 0, 0);
            LinearLayout.LayoutParams lp =
                    new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            addView(label, lp);
        }

        void decodeInBackground() {
            final com.yuki.yukihub.gamecursor.wincursor.CursorPack pack =
                    com.yuki.yukihub.gamecursor.wincursor.WinCursorSupport
                            .decode(MainActivity.this, entry.uri, entry.fileName);
            runOnUiThread(() -> {
                if (pack == null) {
                    label.setText(entry.label() + "(cannot decode)");
                    return;
                }
                int n = pack.sequence != null ? pack.sequence.length : 1;
                label.setText(entry.label() + (n > 1 ? "  " + n + " 帧" : "Static"));
                icon.setPack(pack);
            });
        }

        void release() {
            icon.release();
        }
    }

    /** 播放 CursorPack 动画的小控件，只用于设置界面的预览列表。 */
    private static final class AnimatedCursorView extends View {
        private com.yuki.yukihub.gamecursor.wincursor.CursorPack pack;
        private int step;
        private Runnable ticker;
        private final RectF dst = new RectF();

        AnimatedCursorView(Context c) {
            super(c);
        }

        void setPack(com.yuki.yukihub.gamecursor.wincursor.CursorPack p) {
            release();
            pack = p;
            step = 0;
            invalidate();
            if (p != null && p.isAnimated()) startTicker();
        }

        private void startTicker() {
            stopTicker();
            ticker = new Runnable() {
                @Override
                public void run() {
                    if (ticker != this || pack == null) return;
                    step = (step + 1) % pack.sequence.length;
                    invalidate();
                    int d = step < pack.stepDurations.length ? pack.stepDurations[step] : 67;
                    postDelayed(this, Math.max(16, d));
                }
            };
            int d = pack.stepDurations.length > 0 ? pack.stepDurations[0] : 67;
            postDelayed(ticker, Math.max(16, d));
        }

        private void stopTicker() {
            if (ticker != null) {
                removeCallbacks(ticker);
                ticker = null;
            }
        }

        void release() {
            stopTicker();
            if (pack != null) {
                pack.recycle();
                pack = null;
            }
        }

        @Override
        protected void onDetachedFromWindow() {
            release();
            super.onDetachedFromWindow();
        }

        @Override
        protected void onDraw(android.graphics.Canvas canvas) {
            if (pack == null || pack.frames == null || pack.frames.length == 0) return;
            int idx = pack.sequence != null && pack.sequence.length > 0
                    ? pack.sequence[step % pack.sequence.length] : 0;
            if (idx < 0 || idx >= pack.frames.length) idx = 0;
            android.graphics.Bitmap bmp = pack.frames[idx].bitmap;
            if (bmp == null || bmp.isRecycled()) return;
            // 等比铺满，保持光标原始比例
            float vw = getWidth(), vh = getHeight();
            float bw = bmp.getWidth(), bh = bmp.getHeight();
            float k = Math.min(vw / bw, vh / bh);
            float w = bw * k, h = bh * k;
            dst.set((vw - w) / 2f, (vh - h) / 2f, (vw + w) / 2f, (vh + h) / 2f);
            canvas.drawBitmap(bmp, null, dst, null);
        }
    }

    /** 光标缩放/透明度/灵敏度的取值夹取，与 GameCursorConfig 的上下限保持一致。 */
    private static float gcClampScale(float v) {
        return v < GameCursorConfig.MIN_SCALE ? GameCursorConfig.MIN_SCALE
                : (v > GameCursorConfig.MAX_SCALE ? GameCursorConfig.MAX_SCALE : v);
    }
    private static float gcClampAlpha(float v) { return v < 0.2f ? 0.2f : (v > 1f ? 1f : v); }
    private static float gcClampSens(float v) { return v < 0.5f ? 0.5f : (v > 3f ? 3f : v); }

    /**
     * Artemis 虚拟鼠标的点击注入依赖无障碍手势，这里查系统的已启用服务列表。
     * 不能只看 ScreenshotServiceManager.getService()：那是进程内实例，
     * 主进程没跑过翻译时即使系统已开启也拿不到。
     */
    private boolean isAccessibilityTapReady() {
        try {
            String enabled = Settings.Secure.getString(getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (enabled == null || enabled.isEmpty()) return false;
            String target = getPackageName() + "/"
                    + com.yuki.yukihub.translate.YukiScreenshotAccessibilityService.class.getName();
            String shortTarget = getPackageName() + "/."
                    + com.yuki.yukihub.translate.YukiScreenshotAccessibilityService.class.getSimpleName();
            for (String s : enabled.split(":")) {
                String t = s.trim();
                if (t.equalsIgnoreCase(target) || t.equalsIgnoreCase(shortTarget)) return true;
                // 兜底：厂商 ROM 有时只写包名段，做包含匹配
                if (t.startsWith(getPackageName() + "/")
                        && t.contains("YukiScreenshotAccessibilityService")) return true;
            }
            return false;
        } catch (Throwable t) {
            return false;
        }
    }

    /** 引导用户开启无障碍服务（Artemis 点击注入的唯一通道）。 */
    private void promptAccessibilityForCursor(Dialog parent) {
        new AlertDialog.Builder(this)
                .setTitle("Accessibility services need to be turned on")
                .setMessage("The Artemis engine is NativeActivity, and touch events are consumed by the engine's own native layer."
                        + "The application layer cannot directly inject clicks."
                        + "Therefore, Artemis's virtual mouse click must use system accessibility gestures:"
                        + "Cursor movement is not affected, but \"clicking\" requires turning on YukiHub's accessibility service to take effect."
                        + "Enable path: System Settings → Accessibility → Downloaded Services → YukiHub → Enable."
                        + "(This service is shared with screen translation, it only takes screenshots and gestures, and does not read the window content)")
                .setPositiveButton("To open", (d, w) -> {
                    try {
                        startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
                    } catch (Throwable t) {
                        Toast.makeText(this, "Unable to open system settings, please go to: Settings → Accessibility manually", Toast.LENGTH_LONG).show();
                    }
                    if (parent != null) parent.dismiss();
                })
                .setNegativeButton("Talk to you later", (d, w) -> {
                    Toast.makeText(this, "Saved. Before accessibility is turned on, the Artemis cursor can be moved but clicks are invalid.", Toast.LENGTH_LONG).show();
                    if (parent != null) parent.dismiss();
                })
                .show();
    }

    /**
     * 设置界面里的光标实时预览。
     *
     * 用与游戏内 CursorView 完全相同的 CursorAppearance，所以看到的就是实际效果：
     * 光标包会播动画，PNG 显示图片，都没有则内置箭头。
     * 之前这里只画内置箭头，换了图在预览里看不出区别、进游戏才发现不一样。
     *
     * 顺便画一个十字标出热点（鼠标尖的实际位置），这在光标包场景下很有用 ——
     * 不同角色的热点差异极大（文本选择在垂直中间，对角箭头在右上）。
     */
    private final class CursorPreviewView extends View {
        private GameCursorConfig cfg;
        private final com.yuki.yukihub.gamecursor.CursorAppearance appearance =
                new com.yuki.yukihub.gamecursor.CursorAppearance();
        private Runnable ticker;
        private Paint hotspotPaint;
        /**
         * 上次实际加载的外观来源。
         *
         * 不能拿 cfg 前后对比：refresh 传进来的常常就是同一个对象引用，
         * 字段已被就地改掉，比较结果永远"没变"。必须自己留一份快照。
         */
        private String loadedWinName;
        private String loadedIconUri;

        CursorPreviewView(Context c, GameCursorConfig initial) {
            super(c);
            cfg = initial;
            reloadAppearance();
        }

        void refresh(GameCursorConfig c) {
            if (c == null) return;
            cfg = c;
            // 只有外观来源真的变了才重新解码。滑块拖动时 refresh 每帧都来，
            // 每次重解几十帧 PNG 会卡住 UI。
            if (!eq(loadedWinName, cfg.winCursorName) || !eq(loadedIconUri, cfg.iconUri)) {
                reloadAppearance();
            }
            invalidate();
        }

        private void reloadAppearance() {
            appearance.load(MainActivity.this, cfg);
            loadedWinName = cfg != null ? cfg.winCursorName : null;
            loadedIconUri = cfg != null ? cfg.iconUri : null;
            restartTicker();
        }

        private boolean eq(String x, String y) {
            return x == null ? y == null : x.equals(y);
        }

        private void restartTicker() {
            stopTicker();
            if (!appearance.isAnimated()) return;
            appearance.resetAnimation();
            ticker = new Runnable() {
                @Override
                public void run() {
                    if (ticker != this) return;
                    appearance.advance();
                    invalidate();
                    postDelayed(this, Math.max(16, appearance.currentDurationMs()));
                }
            };
            postDelayed(ticker, Math.max(16, appearance.currentDurationMs()));
        }

        private void stopTicker() {
            if (ticker != null) {
                removeCallbacks(ticker);
                ticker = null;
            }
        }

        @Override
        protected void onDetachedFromWindow() {
            stopTicker();
            appearance.release();
            super.onDetachedFromWindow();
        }

        @Override
        protected void onDraw(android.graphics.Canvas canvas) {
            // 与游戏内一致的尺寸算法：基准高度 × 缩放，按内容宽高比定宽
            float aspect = appearance.aspect();
            float h = dp(44) * gcClampScale(cfg.scale) / GameCursorConfig.DEFAULT_SCALE;
            float w = h * aspect;
            // 预览框放不下时等比缩小，保证小缩放值也能看清
            float maxW = getWidth() * 0.9f, maxH = getHeight() * 0.9f;
            if (w > maxW || h > maxH) {
                float k = Math.min(maxW / w, maxH / h);
                w *= k;
                h *= k;
            }
            float left = (getWidth() - w) / 2f;
            float top = (getHeight() - h) / 2f;

            int saveCount = canvas.save();
            canvas.translate(left, top);
            // 应用不透明度：游戏内是 View.setAlpha()，预览这里没有独立 View，
            // 所以用 saveLayerAlpha 包一层。只包光标本身 ——
            // 下面的热点十字是辅助标记，不该跟着变淡。
            int alpha = Math.round(gcClampAlpha(cfg.alpha) * 255f);
            int layer = -1;
            if (alpha < 255) {
                layer = canvas.saveLayerAlpha(0, 0, w, h, alpha);
            }
            appearance.draw(canvas, w, h, false);
            if (layer >= 0) canvas.restoreToCount(layer);
            canvas.restoreToCount(saveCount);

            // 热点十字：标出"鼠标尖"的实际位置
            if (hotspotPaint == null) {
                hotspotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                hotspotPaint.setStyle(Paint.Style.STROKE);
                hotspotPaint.setStrokeWidth(dp(1));
                hotspotPaint.setColor(0x99FF3B30);
            }
            float hx = left + w * appearance.hotspotX();
            float hy = top + h * appearance.hotspotY();
            float r = dp(4);
            canvas.drawLine(hx - r, hy, hx + r, hy, hotspotPaint);
            canvas.drawLine(hx, hy - r, hx, hy + r, hotspotPaint);
        }
    }

    private void showKrSettingsDialog(Game game) {
        if (game == null || game.rootUri == null || game.rootUri.isEmpty()) {
            Toast.makeText(MainActivity.this, "Please select the game directory first", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, String> prefs = loadKrPrefs(game.rootUri);
        Dialog dialog = new Dialog(this);
        LinearLayout panel = new LinearLayout(this);
        panel.setOrientation(LinearLayout.VERTICAL);
        panel.setBackgroundColor(getColorCompat(com.yuki.yukihub.R.color.yh_card));
        int pad = (int) (18 * getResources().getDisplayMetrics().density);
        panel.setPadding(pad, pad, pad, pad);

        TextView title = new TextView(this);
        title.setText("KR Game Settings");
        title.setTextColor(getColorCompat(com.yuki.yukihub.R.color.yh_text));
        title.setTextSize(22);
        title.setPadding(0, 0, 0, pad / 2);

        ScrollView scroll = new ScrollView(this);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(0, 0, 0, 0);

        CheckBox outputLog = krCheckBox("Print log", "1".equals(pref(prefs, "outputlog", "1")));
        CheckBox showFps = krCheckBox("Show FPS", "1".equals(pref(prefs, "showfps", "0")));
        CheckBox keepScreen = krCheckBox("Keep the screen on", "1".equals(pref(prefs, "keep_screen_alive", "1")));
        CheckBox forceFont = krCheckBox("Force default font", "1".equals(pref(prefs, "force_default_font", "0")));
        CheckBox textureCompress = krCheckBox("Texture compression", "1".equals(pref(prefs, "texture_compress", "0")));
        Spinner renderer = krSpinner(new String[]{"Software renderer", "OpenGL (experimental)"}, rendererToLabel(pref(prefs, "renderer", "software")));
        Spinner memusage = krSpinner(new String[]{"unlimited", "low", "medium", "high"}, pref(prefs, "memusage", "unlimited"));
        Spinner renderThread = krSpinner(new String[]{"auto", "1", "2", "3", "4", "6", "8"}, pref(prefs, "render_thread", "auto"));
        EditText fpsLimit = krEdit("FPS limit, e.g. 60", pref(prefs, "fps_limit", "60"));
        fpsLimit.setInputType(InputType.TYPE_CLASS_NUMBER);
        EditText menuOpa = krEdit("Handle/menu transparency, e.g. 0.15", pref(prefs, "menu_handler_opa", "0.15"));
        menuOpa.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        EditText cursorScale = krEdit("Virtual cursor scaling, e.g. 0.5", pref(prefs, "vcursor_scale", "0.5"));
        cursorScale.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        EditText defaultFont = krEdit("Default font path, leave blank to use built-in fonts", pref(prefs, "default_font", ""));

        root.addView(krLabel("Graphics renderer")); root.addView(renderer);
        root.addView(krLabel("Memory usage")); root.addView(memusage);
        root.addView(krLabel("Number of rendering threads")); root.addView(renderThread);
        root.addView(krLabel("Limit FPS")); root.addView(fpsLimit);
        root.addView(krLabel("Handle/menu transparency")); root.addView(menuOpa);
        root.addView(krLabel("Virtual cursor zoom")); root.addView(cursorScale);
        root.addView(outputLog);
        root.addView(showFps);
        root.addView(keepScreen);
        root.addView(textureCompress);
        root.addView(forceFont);
        root.addView(krLabel("Default font path")); root.addView(defaultFont);

        LinearLayout actions = new LinearLayout(this);
        actions.setOrientation(LinearLayout.HORIZONTAL);
        actions.setPadding(0, pad / 2, 0, 0);
        Button cancel = krButton("Cancel");
        Button save = krButton("Save");
        actions.addView(cancel, new LinearLayout.LayoutParams(0, (int) (44 * getResources().getDisplayMetrics().density), 1));
        LinearLayout.LayoutParams saveLp = new LinearLayout.LayoutParams(0, (int) (44 * getResources().getDisplayMetrics().density), 1);
        saveLp.leftMargin = pad / 2;
        actions.addView(save, saveLp);

        scroll.addView(root);
        panel.addView(title);
        panel.addView(scroll, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1));
        panel.addView(actions);
        dialog.setContentView(panel);
        Window w = dialog.getWindow();
        if (w != null) {
            w.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        cancel.setOnClickListener(v -> dialog.dismiss());
        save.setOnClickListener(v -> {
            prefs.put("menu_handler_opa", menuOpa.getText().toString().trim().isEmpty() ? "0.15" : menuOpa.getText().toString().trim());
            prefs.put("vcursor_scale", cursorScale.getText().toString().trim().isEmpty() ? "0.5" : cursorScale.getText().toString().trim());
            prefs.put("renderer", rendererFromLabel(String.valueOf(renderer.getSelectedItem())));
            prefs.put("memusage", String.valueOf(memusage.getSelectedItem()));
            prefs.put("render_thread", String.valueOf(renderThread.getSelectedItem()));
            prefs.put("fps_limit", fpsLimit.getText().toString().trim().isEmpty() ? "60" : fpsLimit.getText().toString().trim());
            prefs.put("outputlog", outputLog.isChecked() ? "1" : "0");
            prefs.put("showfps", showFps.isChecked() ? "1" : "0");
            prefs.put("keep_screen_alive", keepScreen.isChecked() ? "1" : "0");
            prefs.put("texture_compress", textureCompress.isChecked() ? "1" : "0");
            prefs.put("force_default_font", forceFont.isChecked() ? "1" : "0");
            prefs.put("default_font", defaultFont.getText().toString().trim());
            if (saveKrPrefs(game.rootUri, prefs)) {
                Toast.makeText(MainActivity.this, "KR settings saved", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Failed to save KR settings", Toast.LENGTH_LONG).show();
            }
        });
        dialog.show();
        Window shownWindow = dialog.getWindow();
        if (shownWindow != null) {
            shownWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            shownWindow.setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.72f), (int) (getResources().getDisplayMetrics().heightPixels * 0.82f));
        }
    }

    private void showOnsSettingsDialog(Game game) {
        OnsSettings settings = OnsSettings.load(this);
        Dialog dialog = new Dialog(this);
        LinearLayout panel = new LinearLayout(this);
        panel.setOrientation(LinearLayout.VERTICAL);
        panel.setBackgroundColor(getColorCompat(com.yuki.yukihub.R.color.yh_card));
        int pad = (int) (18 * getResources().getDisplayMetrics().density);
        panel.setPadding(pad, pad, pad, pad);

        TextView title = new TextView(this);
        title.setText("ONScripter settings");
        title.setTextColor(getColorCompat(com.yuki.yukihub.R.color.yh_text));
        title.setTextSize(22);
        title.setPadding(0, 0, 0, pad / 2);

        ScrollView scroll = new ScrollView(this);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);

        CheckBox stretchFull = krCheckBox("Stretch full screen (--fullscreen2)", settings.stretchFull);
        CheckBox ignoreCutout = krCheckBox("忽略刘海/挖孔区域", settings.ignoreCutout);
        CheckBox disableVideo = krCheckBox("Disable video playback (--no-video)", settings.disableVideo);
        CheckBox scopedSave = krCheckBox("Use YukiHub independent archive directory", settings.scopedSaveDir);
        CheckBox allowEditArgs = krCheckBox("Allow editing startup parameters in details", settings.allowEditArgs);
        CheckBox sharpness = krCheckBox("Enable sharpening (--sharpness)", settings.sharpness);
        EditText sharpnessValue = krEdit("Sharpening value, for example 2", settings.sharpnessValue);
        sharpnessValue.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        Spinner encoding = krSpinner(new String[]{"gbk", "sjis", "utf8"}, settings.encoding);
        // 引擎版本可切换：0.7.7 为默认，遇到兼容问题可退回 0.7.6。
        Spinner engineVersion = krSpinner(OnsLibLoader.AVAILABLE_VERSIONS, OnsLibLoader.getSelectedVersion(this));

        CheckBox noVsync = krCheckBox("Turn off vertical synchronization (--no-vsync)", settings.noVsync);
        CheckBox fontCache = krCheckBox("Cache fonts (--fontcache)", settings.fontCache);
        CheckBox renderFontOutline = krCheckBox("Text stroke instead of drop shadow (--render-font-outline)", settings.renderFontOutline);
        CheckBox disableRescale = krCheckBox("Do not scale images in files (--disable-rescale)", settings.disableRescale);
        CheckBox forceButtonShortcut = krCheckBox("Force button shortcuts to be enabled (--force-button-shortcut)", settings.forceButtonShortcut);
        CheckBox wheelDownAdvance = krCheckBox("Scroll down to advance text", settings.wheelDownAdvance);
        CheckBox debugLog = krCheckBox("Output engine debug log (--debug:1)", settings.debugLog);
        EditText forceWidth = krEdit("Width, 0 is automatic", settings.forceWidth > 0 ? String.valueOf(settings.forceWidth) : "");
        forceWidth.setInputType(InputType.TYPE_CLASS_NUMBER);
        EditText forceHeight = krEdit("Height, 0 is automatic", settings.forceHeight > 0 ? String.valueOf(settings.forceHeight) : "");
        forceHeight.setInputType(InputType.TYPE_CLASS_NUMBER);

        root.addView(krLabel("Engine version")); root.addView(engineVersion);
        root.addView(krLabel("Text encoding")); root.addView(encoding);

        // 虚拟按键布局编辑器入口：进全屏界面直接拖，所见即所得
        Button btnLayout = krButton("Edit virtual key layout…");
        final TextView layoutSummary = krLabel(onsLayoutSummary());
        layoutSummary.setTextColor(getColorCompat(com.yuki.yukihub.R.color.yh_text_muted));
        btnLayout.setOnClickListener(v -> {
            try {
                startActivity(new android.content.Intent(MainActivity.this,
                        com.yuki.yukihub.ons.OnsButtonLayoutActivity.class));
            } catch (Throwable t) {
                android.widget.Toast.makeText(MainActivity.this,
                        "Failed to open layout editor:" + t, android.widget.Toast.LENGTH_LONG).show();
            }
        });
        root.addView(krLabel("Virtual keys"));
        root.addView(btnLayout);
        root.addView(layoutSummary);

        root.addView(stretchFull);
        root.addView(ignoreCutout);
        root.addView(disableVideo);
        root.addView(scopedSave);
        root.addView(allowEditArgs);
        root.addView(sharpness);
        root.addView(krLabel("Sharpening value")); root.addView(sharpnessValue);
        root.addView(krLabel("Advanced options"));
        root.addView(noVsync);
        root.addView(fontCache);
        root.addView(renderFontOutline);
        root.addView(disableRescale);
        root.addView(forceButtonShortcut);
        root.addView(wheelDownAdvance);
        root.addView(debugLog);
        root.addView(krLabel("Forced resolution (width and height must be filled in at the same time, leave blank for automatic)"));
        root.addView(forceWidth);
        root.addView(forceHeight);

        TextView tip = krLabel("说明：设置会生成 OnsYuri 原版参数：--root、--font、--fullscreen/--fullscreen2、--enc、--save-dir 等。修改后下次启动 ONS 游戏生效。引擎版本切换需要完全退出游戏进程后才会重新加载。");
        tip.setTextColor(getColorCompat(com.yuki.yukihub.R.color.yh_text_muted));
        root.addView(tip);

        LinearLayout actions = new LinearLayout(this);
        actions.setOrientation(LinearLayout.HORIZONTAL);
        actions.setPadding(0, pad / 2, 0, 0);
        Button cancel = krButton("Cancel");
        Button save = krButton("Save");
        actions.addView(cancel, new LinearLayout.LayoutParams(0, (int) (44 * getResources().getDisplayMetrics().density), 1));
        LinearLayout.LayoutParams saveLp = new LinearLayout.LayoutParams(0, (int) (44 * getResources().getDisplayMetrics().density), 1);
        saveLp.leftMargin = pad / 2;
        actions.addView(save, saveLp);

        scroll.addView(root);
        panel.addView(title);
        panel.addView(scroll, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1));
        panel.addView(actions);
        dialog.setContentView(panel);
        cancel.setOnClickListener(v -> dialog.dismiss());
        save.setOnClickListener(v -> {
            settings.stretchFull = stretchFull.isChecked();
            settings.ignoreCutout = ignoreCutout.isChecked();
            settings.disableVideo = disableVideo.isChecked();
            settings.scopedSaveDir = scopedSave.isChecked();
            settings.allowEditArgs = allowEditArgs.isChecked();
            settings.sharpness = sharpness.isChecked();
            settings.sharpnessValue = sharpnessValue.getText().toString().trim().isEmpty() ? "2" : sharpnessValue.getText().toString().trim();
            settings.encoding = OnsSettings.normalizeEncoding(String.valueOf(encoding.getSelectedItem()));
            settings.noVsync = noVsync.isChecked();
            settings.fontCache = fontCache.isChecked();
            settings.renderFontOutline = renderFontOutline.isChecked();
            settings.disableRescale = disableRescale.isChecked();
            settings.forceButtonShortcut = forceButtonShortcut.isChecked();
            settings.wheelDownAdvance = wheelDownAdvance.isChecked();
            settings.debugLog = debugLog.isChecked();
            settings.forceWidth = parsePositiveInt(forceWidth.getText().toString());
            settings.forceHeight = parsePositiveInt(forceHeight.getText().toString());
            settings.save(this);
            OnsLibLoader.setSelectedVersion(this, String.valueOf(engineVersion.getSelectedItem()));
            if (game != null && (game.emulatorPackage == null || game.emulatorPackage.trim().isEmpty())) {
                game.emulatorPackage = "internal.ons";
                repository.update(game);
                loadGames();
            }
            Toast.makeText(MainActivity.this, "ONS settings saved", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
        dialog.show();
        Window shownWindow = dialog.getWindow();
        if (shownWindow != null) {
            shownWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            shownWindow.setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.72f), (int) (getResources().getDisplayMetrics().heightPixels * 0.82f));
        }
    }

    /**
     * PC 引擎设置：winlator-cn 外置启动的单游戏覆盖参数。
     *
     * <p>每项默认「跟随容器配置」（不下发该参数，由 Winlator 按容器自身配置处理）。
     * 只做单游戏覆盖，不做全局设置——全局调优的正确入口是 Winlator 容器设置本身。
     */
    private void showPcSettingsDialog(Game game) {
        if (game == null || game.id <= 0) return;
        PcLaunchPrefs prefs = PcLaunchPrefs.load(this, game.id);

        Dialog dialog = new Dialog(this);
        LinearLayout panel = new LinearLayout(this);
        panel.setOrientation(LinearLayout.VERTICAL);
        panel.setBackgroundColor(getColorCompat(com.yuki.yukihub.R.color.yh_card));
        int pad = (int) (18 * getResources().getDisplayMetrics().density);
        panel.setPadding(pad, pad, pad, pad);

        TextView title = new TextView(this);
        title.setText("PC Engine Settings (Winlator)");
        title.setTextColor(getColorCompat(com.yuki.yukihub.R.color.yh_text));
        title.setTextSize(22);
        title.setPadding(0, 0, 0, pad / 2);

        ScrollView scroll = new ScrollView(this);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);

        EditText containerId = krEdit("Leave blank = follow container configuration (e.g. 1)", prefs.containerId);
        containerId.setInputType(InputType.TYPE_CLASS_NUMBER);
        EditText containerName = krEdit("Leave blank = follow container configuration (e.g. container-1)", prefs.containerName);
        // 下拉：展示中文标签，保存时映射回协议值；回显用「值 → 标签」反查
        android.widget.Spinner graphicsDriver = krSpinner(
                PcLaunchPrefs.GRAPHICS_DRIVER_LABELS,
                PcLaunchPrefs.labelOfValue(PcLaunchPrefs.GRAPHICS_DRIVER_LABELS, PcLaunchPrefs.GRAPHICS_DRIVER_VALUES, prefs.graphicsDriver));
        android.widget.Spinner dxwrapper = krSpinner(
                PcLaunchPrefs.DXWRAPPER_LABELS,
                PcLaunchPrefs.labelOfValue(PcLaunchPrefs.DXWRAPPER_LABELS, PcLaunchPrefs.DXWRAPPER_VALUES, prefs.dxwrapper));
        android.widget.Spinner screenSize = krSpinner(
                PcLaunchPrefs.SCREEN_SIZE_LABELS,
                PcLaunchPrefs.labelOfValue(PcLaunchPrefs.SCREEN_SIZE_LABELS, PcLaunchPrefs.SCREEN_SIZE_VALUES, prefs.screenSize));
        android.widget.Spinner lcAll = krSpinner(
                PcLaunchPrefs.LC_ALL_LABELS,
                PcLaunchPrefs.labelOfValue(PcLaunchPrefs.LC_ALL_LABELS, PcLaunchPrefs.LC_ALL_VALUES, prefs.lcAll));
        android.widget.Spinner box64 = krSpinner(
                PcLaunchPrefs.BOX64_LABELS,
                PcLaunchPrefs.labelOfValue(PcLaunchPrefs.BOX64_LABELS, PcLaunchPrefs.BOX64_VALUES, prefs.box64Preset));
        android.widget.Spinner screenOrientation = krSpinner(
                PcLaunchPrefs.ORIENTATION_LABELS,
                PcLaunchPrefs.labelOfValue(PcLaunchPrefs.ORIENTATION_LABELS, PcLaunchPrefs.ORIENTATION_VALUES, prefs.screenOrientation));
        android.widget.Spinner swapResolution = krSpinner(
                PcLaunchPrefs.SWAP_LABELS,
                PcLaunchPrefs.labelOfValue(PcLaunchPrefs.SWAP_LABELS, PcLaunchPrefs.SWAP_VALUES, prefs.swapResolution));
        android.widget.Spinner forceFullscreen = krSpinner(
                PcLaunchPrefs.BOOL10_LABELS,
                PcLaunchPrefs.labelOfValue(PcLaunchPrefs.BOOL10_LABELS, PcLaunchPrefs.BOOL10_VALUES, prefs.forceFullscreen));
        android.widget.Spinner toggleFullscreen = krSpinner(
                PcLaunchPrefs.BOOL10_LABELS,
                PcLaunchPrefs.labelOfValue(PcLaunchPrefs.BOOL10_LABELS, PcLaunchPrefs.BOOL10_VALUES, prefs.toggleFullscreen));
        android.widget.Spinner audioDriver = krSpinner(
                PcLaunchPrefs.AUDIO_LABELS,
                PcLaunchPrefs.labelOfValue(PcLaunchPrefs.AUDIO_LABELS, PcLaunchPrefs.AUDIO_VALUES, prefs.audioDriver));
        EditText envVars = krEdit("For example, WINEDLLOVERRIDES=d3d9=n,b (separated by spaces, leave blank to follow)", prefs.envVars);
        EditText execArgs = krEdit("Such as -windowed (append startup parameters, leave blank to follow)", prefs.execArgs);
        CheckBox saveToContainer = krCheckBox("Write this override into the container configuration (persist to the container)", prefs.save);

        root.addView(krLabel("Container ID (number, takes precedence over container name; leave blank to follow container configuration)"));
        root.addView(containerId);
        root.addView(krLabel("Container name (supports Chinese, case insensitive)"));
        root.addView(containerName);
        root.addView(krLabel("Graphics driver"));
        root.addView(graphicsDriver);
        root.addView(krLabel("Graphics acceleration"));
        root.addView(dxwrapper);
        root.addView(krLabel("Resolution"));
        root.addView(screenSize);
        root.addView(krLabel("Locale (LC_ALL)"));
        root.addView(lcAll);
        root.addView(krLabel("Box64 default"));
        root.addView(box64);
        root.addView(krLabel("Screen orientation"));
        root.addView(screenOrientation);
        root.addView(krLabel("Swap width and height"));
        root.addView(swapResolution);
        root.addView(krLabel("Force full screen"));
        root.addView(forceFullscreen);
        root.addView(krLabel("Switchable to full screen"));
        root.addView(toggleFullscreen);
        root.addView(krLabel("Audio driver"));
        root.addView(audioDriver);
        root.addView(krLabel("Environment variables (advanced)"));
        root.addView(envVars);
        root.addView(krLabel("Append startup parameters (session level, not written to the container)"));
        root.addView(execArgs);
        root.addView(saveToContainer);

        TextView tip = krLabel("Note: Leave it blank or \"Follow container configuration\" means that this parameter will not be issued and will be handled by Winlator according to the container's own configuration (if you want to make global adjustments, please change the container settings directly). If the format of the environment variable is illegal, it will be automatically ignored (not issued); if you need to change the time zone, you can fill in TZ=Asia/Tokyo in the environment variable. The modification will take effect the next time you start it.");
        tip.setTextColor(getColorCompat(com.yuki.yukihub.R.color.yh_text_muted));
        root.addView(tip);

        LinearLayout actions = new LinearLayout(this);
        actions.setOrientation(LinearLayout.HORIZONTAL);
        actions.setPadding(0, pad / 2, 0, 0);
        Button cancel = krButton("Cancel");
        Button save = krButton("Save");
        actions.addView(cancel, new LinearLayout.LayoutParams(0, (int) (44 * getResources().getDisplayMetrics().density), 1));
        LinearLayout.LayoutParams saveLp = new LinearLayout.LayoutParams(0, (int) (44 * getResources().getDisplayMetrics().density), 1);
        saveLp.leftMargin = pad / 2;
        actions.addView(save, saveLp);

        scroll.addView(root);
        panel.addView(title);
        panel.addView(scroll, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1));
        panel.addView(actions);
        dialog.setContentView(panel);
        cancel.setOnClickListener(v -> dialog.dismiss());
        save.setOnClickListener(v -> {
            prefs.containerId = containerId.getText().toString();
            prefs.containerName = containerName.getText().toString();
            prefs.graphicsDriver = PcLaunchPrefs.valueOfLabel(PcLaunchPrefs.GRAPHICS_DRIVER_LABELS, PcLaunchPrefs.GRAPHICS_DRIVER_VALUES, String.valueOf(graphicsDriver.getSelectedItem()));
            prefs.dxwrapper = PcLaunchPrefs.valueOfLabel(PcLaunchPrefs.DXWRAPPER_LABELS, PcLaunchPrefs.DXWRAPPER_VALUES, String.valueOf(dxwrapper.getSelectedItem()));
            prefs.screenSize = PcLaunchPrefs.valueOfLabel(PcLaunchPrefs.SCREEN_SIZE_LABELS, PcLaunchPrefs.SCREEN_SIZE_VALUES, String.valueOf(screenSize.getSelectedItem()));
            prefs.lcAll = PcLaunchPrefs.valueOfLabel(PcLaunchPrefs.LC_ALL_LABELS, PcLaunchPrefs.LC_ALL_VALUES, String.valueOf(lcAll.getSelectedItem()));
            prefs.box64Preset = PcLaunchPrefs.valueOfLabel(PcLaunchPrefs.BOX64_LABELS, PcLaunchPrefs.BOX64_VALUES, String.valueOf(box64.getSelectedItem()));
            prefs.screenOrientation = PcLaunchPrefs.valueOfLabel(PcLaunchPrefs.ORIENTATION_LABELS, PcLaunchPrefs.ORIENTATION_VALUES, String.valueOf(screenOrientation.getSelectedItem()));
            prefs.swapResolution = PcLaunchPrefs.valueOfLabel(PcLaunchPrefs.SWAP_LABELS, PcLaunchPrefs.SWAP_VALUES, String.valueOf(swapResolution.getSelectedItem()));
            prefs.forceFullscreen = PcLaunchPrefs.valueOfLabel(PcLaunchPrefs.BOOL10_LABELS, PcLaunchPrefs.BOOL10_VALUES, String.valueOf(forceFullscreen.getSelectedItem()));
            prefs.toggleFullscreen = PcLaunchPrefs.valueOfLabel(PcLaunchPrefs.BOOL10_LABELS, PcLaunchPrefs.BOOL10_VALUES, String.valueOf(toggleFullscreen.getSelectedItem()));
            prefs.audioDriver = PcLaunchPrefs.valueOfLabel(PcLaunchPrefs.AUDIO_LABELS, PcLaunchPrefs.AUDIO_VALUES, String.valueOf(audioDriver.getSelectedItem()));
            prefs.envVars = envVars.getText().toString();
            prefs.execArgs = execArgs.getText().toString();
            prefs.save = saveToContainer.isChecked();
            prefs.save(this, game.id);
            Toast.makeText(MainActivity.this, "PC Engine settings saved", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
        dialog.show();
        Window shownWindow = dialog.getWindow();
        if (shownWindow != null) {
            shownWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            shownWindow.setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.72f), (int) (getResources().getDisplayMetrics().heightPixels * 0.82f));
        }
    }

    /**
     * FVP 引擎（rfvp）设置：单游戏覆盖「文本编码 / 文本 HiDPI / 系统字体回退 / 自定义字体」。
     *
     * <p>这四项正是「同一个游戏别人机器上正常显示、我这里乱码或方框」的常见原因：
     * <ul>
     *   <li>编码：原版日文游戏 sjis，简中汉化版 gbk，部分移植/重打包是 utf8；</li>
     *   <li>文本 HiDPI：小字号下中文更清晰（默认开）；</li>
     *   <li>系统字体回退：游戏自带字体缺字时用系统字体补齐（默认开）；</li>
     *   <li>自定义字体：强制用指定字体渲染（汉化游戏常需要）。</li>
     * </ul>
     *
     * <p>引擎侧没有其它可调项——存档位置固定为「游戏根目录/save」，与容器/驱动无关，
     * 所以这里不做多余开关（要调显示以外的行为只能改游戏本体或等上游引擎支持）。
     */
    private void showFvpSettingsDialog(Game game) {
        if (game == null || game.id <= 0) return;
        FvpLaunchPrefs prefs = FvpLaunchPrefs.load(this, game.id, game.rootUri);
        // 字体选择回调需要改这一份，见字段注释。
        fvpEditingPrefs = prefs;
        fvpEditingGameId = game.id;

        Dialog dialog = new Dialog(this);
        LinearLayout panel = new LinearLayout(this);
        panel.setOrientation(LinearLayout.VERTICAL);
        panel.setBackgroundColor(getColorCompat(com.yuki.yukihub.R.color.yh_card));
        int pad = (int) (18 * getResources().getDisplayMetrics().density);
        panel.setPadding(pad, pad, pad, pad);

        TextView title = new TextView(this);
        title.setText("FVP engine settings (rfvp)");
        title.setTextColor(getColorCompat(com.yuki.yukihub.R.color.yh_text));
        title.setTextSize(22);
        title.setPadding(0, 0, 0, pad / 2);

        ScrollView scroll = new ScrollView(this);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);

        android.widget.Spinner nlsSpinner = krSpinner(FvpLaunchPrefs.NLS_LABELS,
                FvpLaunchPrefs.labelOfNls(prefs.nls));
        CheckBox textHidpi = krCheckBox("High-resolution rendering of text (small text is clearer)", prefs.textHidpi);
        CheckBox systemFont = krCheckBox("Enable system font fallback (use system fonts to fill in missing characters)", prefs.systemFont);
        CheckBox stretchFill = krCheckBox("The picture fills the entire screen (stretching the picture to eliminate black edges, the picture ratio will be slightly distorted)", prefs.stretchFill);
        CheckBox virtualKeys = krCheckBox("虚拟按键（左侧竖排：ESC 菜单 / 历史 / Ctrl 快进）", prefs.virtualKeys);
        CheckBox virtualMouse = krCheckBox("虚拟鼠标（可拖动光标；配合触摸板模式，与 KRKR/Artemis 同一套）", prefs.virtualMouse);
        android.widget.Spinner screenScale = krSpinner(FvpLaunchPrefs.SCALE_LABELS,
                FvpLaunchPrefs.labelOfScale(prefs.screenScale));
        android.widget.Spinner textScale = krSpinner(FvpLaunchPrefs.TEXT_SCALE_LABELS,
                FvpLaunchPrefs.labelOfTextScale(prefs.textScale));
        android.widget.Spinner lineScale = krSpinner(FvpLaunchPrefs.LINE_SCALE_LABELS,
                FvpLaunchPrefs.labelOfLineScale(prefs.lineScale));

        fvpFontPathView = krLabel(fvpFontSummary(prefs.fontPath));
        fvpFontPathView.setTextColor(getColorCompat(com.yuki.yukihub.R.color.yh_text_muted));
        Button pickFont = krButton("Select font file…");
        Button clearFont = krButton("Clear custom fonts");
        pickFont.setOnClickListener(v -> {
            try {
                fvpFontLauncher.launch("*/*");
            } catch (Throwable t) {
                Toast.makeText(MainActivity.this, "Failed to open file picker:" + t, Toast.LENGTH_SHORT).show();
            }
        });
        clearFont.setOnClickListener(v -> {
            if (fvpEditingPrefs != null) fvpEditingPrefs.fontPath = "";
            if (fvpFontPathView != null) fvpFontPathView.setText("Current: The game comes with fonts");
            Toast.makeText(MainActivity.this, "Custom font has been cleared (click \"Save\" to take effect)", Toast.LENGTH_SHORT).show();
        });

        root.addView(krLabel("Text encoding (automatically recognized, if there are still garbled characters, you can change it manually)"));
        root.addView(nlsSpinner);
        root.addView(krLabel("Magnify the screen (enlarge proportionally and crop the edges, the text will become larger; the rendering burden will increase)"));
        root.addView(screenScale);
        root.addView(krLabel("Text size (only the text is enlarged, the screen layout remains unchanged; if it is too large, the dialog box will be exceeded)"));
        root.addView(textScale);
        root.addView(krLabel("Line spacing (how much distance follows between lines when the font size is enlarged)"));
        root.addView(lineScale);
        root.addView(textHidpi);
        root.addView(systemFont);
        root.addView(stretchFill);
        root.addView(krLabel("Virtual controls (used when there is no keyboard or mouse on the phone; turned off by default)"));
        root.addView(virtualKeys);
        root.addView(virtualMouse);
        root.addView(krLabel("虚拟鼠标的光标外观/大小/灵敏度请在「虚拟鼠标设置」里调整"));
        root.addView(krLabel("Custom font (forced to use the specified font for rendering; leave it blank to use the game's own font)"));
        root.addView(pickFont);
        root.addView(clearFont);
        root.addView(fvpFontPathView);

        TextView tip = krLabel("Note: The settings are saved individually by game and will take effect the next time you start them after modification."
                + "The archive is located in the save folder (rfvp_s###.bin) of the game directory and can be backed up together with the game directory.");
        tip.setTextColor(getColorCompat(com.yuki.yukihub.R.color.yh_text_muted));
        root.addView(tip);

        LinearLayout actions = new LinearLayout(this);
        actions.setOrientation(LinearLayout.HORIZONTAL);
        actions.setPadding(0, pad / 2, 0, 0);
        Button cancel = krButton("Cancel");
        Button save = krButton("Save");
        actions.addView(cancel, new LinearLayout.LayoutParams(0, (int) (44 * getResources().getDisplayMetrics().density), 1));
        LinearLayout.LayoutParams saveLp = new LinearLayout.LayoutParams(0, (int) (44 * getResources().getDisplayMetrics().density), 1);
        saveLp.leftMargin = pad / 2;
        actions.addView(save, saveLp);

        scroll.addView(root);
        panel.addView(title);
        panel.addView(scroll, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1));
        panel.addView(actions);
        dialog.setContentView(panel);
        cancel.setOnClickListener(v -> dialog.dismiss());
        save.setOnClickListener(v -> {
            prefs.nls = FvpLaunchPrefs.valueOfNlsLabel(String.valueOf(nlsSpinner.getSelectedItem()));
            prefs.screenScale = FvpLaunchPrefs.valueOfScaleLabel(String.valueOf(screenScale.getSelectedItem()));
            prefs.textScale = FvpLaunchPrefs.valueOfTextScaleLabel(String.valueOf(textScale.getSelectedItem()));
            prefs.lineScale = FvpLaunchPrefs.valueOfLineScaleLabel(String.valueOf(lineScale.getSelectedItem()));
            prefs.textHidpi = textHidpi.isChecked();
            prefs.systemFont = systemFont.isChecked();
            prefs.stretchFill = stretchFill.isChecked();
            prefs.virtualKeys = virtualKeys.isChecked();
            prefs.virtualMouse = virtualMouse.isChecked();
            // fontPath 由「选择/清除」按钮直接改 prefs，这里不能覆盖（否则会把刚选的字体抹掉）。
            prefs.save(this, game.id);
            Toast.makeText(MainActivity.this, "FVP engine settings saved", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
        dialog.setOnDismissListener(d -> {
            fvpEditingPrefs = null;
            fvpEditingGameId = -1L;
            fvpFontPathView = null;
        });
        dialog.show();
        Window shownWindow = dialog.getWindow();
        if (shownWindow != null) {
            shownWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            shownWindow.setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.72f), (int) (getResources().getDisplayMetrics().heightPixels * 0.82f));
        }
    }

    /** FVP 设置里的「当前字体」一句话描述。 */
    private String fvpFontSummary(String fontPath) {
        String p = FvpLaunchPrefs.normalizePath(fontPath);
        if (p.isEmpty()) return "Current: The game comes with fonts";
        File f = new File(p);
        if (!f.isFile()) return "Current: Custom font (file is missing and will be automatically ignored)";
        return "Current: Custom font (" + f.getName() + "）";
    }

    /**
     * 虚拟按键当前配置的一句话摘要，显示在设置项下方。
     */
    private String onsLayoutSummary() {
        try {
            com.yuki.yukihub.ons.OnsButtonConfig c =
                    com.yuki.yukihub.ons.OnsButtonConfig.load(this);
            int left = 0, right = 0, custom = 0;
            for (com.yuki.yukihub.ons.OnsButtonConfig.Item it : c.items) {
                if (it.x < 50f) left++; else right++;
                if (it.isCustom()) custom++;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Common").append(c.items.size()).append("(left").append(left)
              .append("/ right").append(right).append("）");
            if (custom > 0) sb.append(", including").append(custom).append("Custom");
            sb.append("Diameter").append(c.size).append("Dp opacity")
              .append(c.opacity).append("%");
            return sb.toString();
        } catch (Throwable t) {
            return "Click the button above to configure";
        }
    }

    /**
     * 解析用户输入的正整数，非法或空值返回 0（表示「自动 / 不指定」）。
     * 用于强制分辨率这类可留空的数值输入。
     */
    private int parsePositiveInt(String text) {
        if (text == null) return 0;
        String t = text.trim();
        if (t.isEmpty()) return 0;
        try {
            int v = Integer.parseInt(t);
            return v > 0 ? v : 0;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private TextView krLabel(String text) {
        TextView v = new TextView(this);
        v.setText(text);
        v.setTextSize(13);
        v.setTextColor(getColorCompat(com.yuki.yukihub.R.color.yh_text));
        v.setPadding(0, 10, 0, 4);
        return v;
    }

    private CheckBox krCheckBox(String text, boolean checked) {
CheckBox v = new CheckBox(this);
v.setText(text);
v.setChecked(checked);
DynamicTheme dt = DynamicTheme.getInstance();
if (dt.isEnabled() && dt.getColors() != null) {
    v.setButtonTintList(android.content.res.ColorStateList.valueOf(dt.getColors().primary));
} else {
    v.setTextColor(getColorCompat(com.yuki.yukihub.R.color.yh_text));
    v.setButtonTintList(android.content.res.ColorStateList.valueOf(getColorCompat(com.yuki.yukihub.R.color.yh_primary)));
}
attachUiTouchSound(v, UI_SOUND_SWITCH);
return v;
}

private Button krButton(String text) {
Button b = new Button(this);
b.setText(text);
b.setAllCaps(false);
DynamicTheme dt = DynamicTheme.getInstance();
if (dt.isEnabled() && dt.getColors() != null) {
    b.setBackground(tintButton(dt.getColors()));
    b.setTextColor(0xFFFFFFFF);
} else {
    b.setTextColor(getColorCompat(com.yuki.yukihub.R.color.yh_text));
    b.setBackgroundColor(getColorCompat(com.yuki.yukihub.R.color.yh_card_2));
}
attachUiTouchSound(b, UI_SOUND_CONFIRM);
return b;
}

    private LinearLayout linkCardButton(String text, int iconResId) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(android.view.Gravity.CENTER_VERTICAL);
        row.setPadding(dp(16), 0, dp(16), 0);
        DynamicTheme dt = DynamicTheme.getInstance();
        if (dt.isEnabled() && dt.getColors() != null) {
            row.setBackground(tintInput(dt.getColors()));
        } else {
            row.setBackgroundResource(R.drawable.bg_auth_tab_inactive);
        }
        row.setMinimumHeight(dp(48));
        ImageView icon = new ImageView(this);
        try {
            Drawable d = getDrawable(iconResId);
            if (d != null) {
                d = d.mutate();
                d.setTint(getColorCompat(com.yuki.yukihub.R.color.yh_primary));
                icon.setImageDrawable(d);
            }
        } catch (Throwable ignored) { }
        LinearLayout.LayoutParams iconLp = new LinearLayout.LayoutParams(dp(24), dp(24));
        iconLp.rightMargin = dp(10);
        row.addView(icon, iconLp);
        TextView label = new TextView(this);
        label.setText(text);
        label.setTextColor(getColorCompat(com.yuki.yukihub.R.color.yh_text));
        label.setTextSize(14);
        label.setTypeface(null, android.graphics.Typeface.BOLD);
        label.setGravity(android.view.Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams labelLp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        row.addView(label, labelLp);
        return row;
    }

    private EditText krEdit(String hint, String value) {
        EditText v = new EditText(this);
        v.setHint(hint);
        v.setSingleLine(true);
        v.setText(value);
        v.setTextColor(getColorCompat(com.yuki.yukihub.R.color.yh_text));
        v.setHintTextColor(getColorCompat(com.yuki.yukihub.R.color.yh_text_muted));
        v.setBackgroundColor(getColorCompat(com.yuki.yukihub.R.color.yh_card_2));
        v.setPadding(12, 0, 12, 0);
        return v;
    }

    private Spinner krSpinner(String[] values, String selected) {
        Spinner sp = new TapSafeSpinner(this);
        ArrayAdapter<String> adapter = krSpinnerAdapter(values);
        sp.setAdapter(adapter);
        DynamicTheme dt = DynamicTheme.getInstance();
        if (dt.isEnabled() && dt.getColors() != null) {
            sp.setBackground(tintInput(dt.getColors()));
        }
        for (int i = 0; i < values.length; i++) if (values[i].equalsIgnoreCase(selected)) { sp.setSelection(i); break; }
        return sp;
    }

    private int getColorCompat(int id) {
        if (Build.VERSION.SDK_INT >= 23) return getColor(id);
        return getResources().getColor(id);
    }

    /** In dynamic theme mode, returns white for button text; otherwise returns yh_primary. */
    private int primaryTextColor() {
        DynamicTheme dt = DynamicTheme.getInstance();
        return (dt.isEnabled() && dt.getColors() != null) ? 0xFFFFFFFF : getColorCompat(R.color.yh_primary);
    }

    /** Create an ArrayAdapter with dynamic theme text colors for Spinner. */
    private ArrayAdapter<String> krSpinnerAdapter(String[] items) {
        DynamicTheme dt = DynamicTheme.getInstance();
        boolean themed = dt.isEnabled() && dt.getColors() != null;
        int itemLayout = themed ? R.layout.spinner_item_themed : R.layout.spinner_item_dark;
        int dropLayout = themed ? R.layout.spinner_dropdown_themed : R.layout.spinner_dropdown_dark;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, itemLayout, items);
        adapter.setDropDownViewResource(dropLayout);
        return adapter;
    }

    private String rendererToLabel(String value) {
        if (value == null) return "Software renderer";
        String v = value.trim().toLowerCase(Locale.ROOT);
        if ("opengl".equals(v) || "open_gl".equals(v) || "gl".equals(v) || "hardware".equals(v)) return "OpenGL (experimental)";
        return "Software renderer";
    }

    private String rendererFromLabel(String label) {
if (label != null && label.toLowerCase(Locale.ROOT).contains("opengl")) return "opengl";
return "software";
}

private String krEngineVersionToLabel(String value) {
String mode = normalizeKrEngineVersion(value);
if ("1.3.4".equals(mode)) return "1.3.4";
if ("1.3.9".equals(mode)) return "1.3.9";
return "Auto";
}

private String krEngineVersionFromLabel(String label) {
if (label == null) return "auto";
String v = label.trim();
if (v.contains("1.3.4")) return "1.3.4";
if (v.contains("1.3.9")) return "1.3.9";
return "auto";
}

private String normalizeKrEngineVersion(String value) {
String v = value == null ? "auto" : value.trim().toLowerCase(Locale.ROOT);
if ("134".equals(v) || "1.3.4".equals(v) || "kr134".equals(v)) return "1.3.4";
if ("139".equals(v) || "1.3.9".equals(v) || "kr139".equals(v)) return "1.3.9";
return "auto";
}

private String pref(Map<String, String> prefs, String key, String def) {
        String v = prefs.get(key);
        return v == null ? def : v;
    }

    private List<String> buildLaunchOptions(String rootUri) {
        List<String> options = new ArrayList<>();
        if (rootUri != null && !rootUri.isEmpty()) {
            String directPath = displayPath(rootUri);
            if (directPath != null && directPath.toLowerCase(Locale.ROOT).endsWith(".desktop")) {
                String name = directPath.substring(Math.max(directPath.lastIndexOf('/'), directPath.lastIndexOf('\\')) + 1);
                if (!name.isEmpty() && !options.contains(name)) options.add(name);
            }
            try {
                DocumentFile dir = gameDir(rootUri);
                if (dir != null && dir.isDirectory()) {
                    DocumentFile[] files = dir.listFiles();
                    if (files != null) {
                        for (DocumentFile file : files) {
                            String name = file.getName();
                            if (name == null || !file.isFile()) continue;
                            if (isLaunchCandidate(name) && !options.contains(name)) {
                                options.add(name);
                            }
                        }
                    }
                }
            } catch (Exception ignored) { }
        }
        // KR 启动入口排序：中文「启动/游戏」xp3 > data.xp3 > 其它（见 KrkrEntryPriority）
        // 非 KR 候选（.desktop / .iso 等）保持原有相对顺序，排在 KR 候选之后。
        java.util.Collections.sort(options, (a, b) -> {
            int sa = com.yuki.yukihub.scanner.KrkrEntryPriority.scoreEntry(a);
            int sb = com.yuki.yukihub.scanner.KrkrEntryPriority.scoreEntry(b);
            boolean ka = sa != com.yuki.yukihub.scanner.KrkrEntryPriority.SCORE_NONE;
            boolean kb = sb != com.yuki.yukihub.scanner.KrkrEntryPriority.SCORE_NONE;
            if (ka && kb) return sb - sa;
            if (ka) return -1;
            if (kb) return 1;
            return 0;
        });
        if (options.contains("[Game Catalog]")) options.remove("[Game Catalog]");
        options.add("[Game Catalog]");
        if (options.isEmpty()) options.add("No bootable files were scanned, please select a directory first");
        return options;
    }

    private boolean isLaunchCandidate(String name) {
        if (name == null) return false;
        String lower = name.toLowerCase(Locale.ROOT);
        return lower.endsWith(".xp3") || lower.endsWith(".tjs") || lower.endsWith(".ks")
                || lower.endsWith(".html") || lower.endsWith(".htm") || lower.endsWith(".txt")
                || lower.endsWith(".dat") || lower.endsWith(".pfs") || lower.endsWith(".desktop")
                || lower.endsWith(".nsa") || lower.endsWith(".sar")
                || lower.endsWith(".iso") || lower.endsWith(".cso") || lower.endsWith(".chd")
                || lower.endsWith(".elf") || lower.endsWith(".pbp")
                // .exe：部分 KR 整合版把真正入口放在同名 exe 上（例：是谁杀了知更鸟.exe），
                // 内核可以直接吃 exe 路径。EmulatorLauncher 早已支持该后缀，
                // 这里补上候选，否则下拉里选不到。
                || lower.endsWith(".exe");
    }

    private int findLaunchSelection(List<String> options, String target) {
        if (options == null || options.isEmpty()) return 0;
        if (target == null || target.trim().isEmpty()) target = "[Game Catalog]";
        for (int i = 0; i < options.size(); i++) {
            if (target.equals(options.get(i))) return i;
        }
        int dirIndex = options.indexOf("[Game Catalog]");
        return dirIndex >= 0 ? dirIndex : 0;
    }

    private Map<String, String> loadKrPrefs(String rootUri) {
        Map<String, String> prefs = defaultKrPrefs();
        try (InputStream in = openKrPrefsInput(rootUri)) {
            if (in == null) return prefs;
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(in);
            NodeList items = doc.getElementsByTagName("Item");
            for (int i = 0; i < items.getLength(); i++) {
                if (!(items.item(i) instanceof Element)) continue;
                Element item = (Element) items.item(i);
                String key = item.getAttribute("key");
                if (key == null || key.isEmpty()) continue;
                prefs.put(key, item.getAttribute("value"));
            }
        } catch (Throwable ignored) { }
        return prefs;
    }

    private Map<String, String> defaultKrPrefs() {
        Map<String, String> prefs = new LinkedHashMap<>();
        prefs.put("menu_handler_opa", "0.15");
        prefs.put("vcursor_scale", "0.5");
        prefs.put("force_default_font", "0");
        prefs.put("default_font", "");
        prefs.put("renderer", "software");
        prefs.put("memusage", "unlimited");
        prefs.put("render_thread", "auto");
        prefs.put("texture_compress", "0");
        prefs.put("fps_limit", "60");
        prefs.put("keep_screen_alive", "1");
        prefs.put("showfps", "0");
        prefs.put("outputlog", "1");
        return prefs;
    }

    // KR 引擎版本只保留全局设置，不再通过单个游戏目录的 .1.3.4 标记读写，避免与右上角设置冲突。

    private DocumentFile krGameDir(String rootUri) {
return documentDir(rootUri);
}

    private InputStream openKrPrefsInput(String rootUri) {
try {
if (rootUri == null || rootUri.isEmpty()) return null;
if (rootUri.startsWith("/") || rootUri.startsWith("file://")) {
File f = new File(rootUri.startsWith("file://") ? Uri.parse(rootUri).getPath() : rootUri, "Kirikiroid2Preference.xml");
return f.exists() ? new FileInputStream(f) : null;
}
DocumentFile dir = documentDir(rootUri);
DocumentFile file = dir == null ? null : dir.findFile("Kirikiroid2Preference.xml");
return file == null || !file.isFile() ? null : getContentResolver().openInputStream(file.getUri());
} catch (Throwable ignored) { return null; }
}

    private boolean saveKrPrefs(String rootUri, Map<String, String> prefs) {
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element root = doc.createElement("GlobalPreference");
            doc.appendChild(root);
            for (Map.Entry<String, String> e : prefs.entrySet()) {
                Element item = doc.createElement("Item");
                item.setAttribute("key", e.getKey());
                item.setAttribute("value", e.getValue() == null ? "" : e.getValue());
                root.appendChild(item);
            }
            try (OutputStream out = openKrPrefsOutput(rootUri)) {
                if (out == null) return false;
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.transform(new DOMSource(doc), new StreamResult(out));
            }
            return true;
        } catch (Throwable ignored) { return false; }
    }

    private OutputStream openKrPrefsOutput(String rootUri) {
try {
if (rootUri == null || rootUri.isEmpty()) return null;
if (rootUri.startsWith("/") || rootUri.startsWith("file://")) {
File dir = new File(rootUri.startsWith("file://") ? Uri.parse(rootUri).getPath() : rootUri);
if (!dir.exists() && !dir.mkdirs()) return null;
return new FileOutputStream(new File(dir, "Kirikiroid2Preference.xml"));
}
DocumentFile dir = documentDir(rootUri);
if (dir == null || !dir.isDirectory()) return null;
DocumentFile file = dir.findFile("Kirikiroid2Preference.xml");
if (file == null) file = dir.createFile("text/xml", "Kirikiroid2Preference.xml");
return file == null ? null : getContentResolver().openOutputStream(file.getUri(), "wt");
} catch (Throwable ignored) { return null; }
}
private void showScanResults(List<ScanResult> results) {
        if (results.isEmpty()) { Toast.makeText(this, "No subdirectory candidate games found", Toast.LENGTH_LONG).show(); return; }
        Dialog d = new Dialog(this); d.requestWindowFeature(Window.FEATURE_NO_TITLE); d.setContentView(R.layout.dialog_scan_result);
        tintDialogRoot(d.findViewById(android.R.id.content));
        if (d.getWindow() != null) {
            d.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            d.getWindow().setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.88f), android.view.WindowManager.LayoutParams.WRAP_CONTENT);
        }
        RecyclerView rv = d.findViewById(R.id.recyclerScanResults); rv.setLayoutManager(new LinearLayoutManager(this)); rv.setAdapter(new ScanResultAdapter(results));
        ((TextView)d.findViewById(R.id.tvScanTitle)).setText("Scan results:" + results.size() + "Candidate games");
        d.findViewById(R.id.btnCancelScan).setOnClickListener(v -> d.dismiss());
        d.findViewById(R.id.btnImportScan).setOnClickListener(v -> {
            ScanImportStats stats = importScannedGames(results);
            if (stats.added > 0) AppExecutors.runOnIo(() -> autoMatchVndbForImportedGames(stats.importedGames));
            d.dismiss();
            loadGames();
            Toast.makeText(this, "New" + stats.added + ", already exists" + stats.skipped + " 个" + (stats.added > 0 ? ", matching automatically" + metadataSourceLabel() + "Cover" : ""), Toast.LENGTH_SHORT).show();
        });
        d.show();
    }

    private void runLibraryScan(List<String> rootUris, boolean showToast) {
if (rootUris == null || rootUris.isEmpty()) return;
if (autoLibraryScanRunning) return;
autoLibraryScanRunning = true;
runOnUiThread(() -> setScanLoading(true));
if (showToast) Toast.makeText(MainActivity.this, "Scanning" + rootUris.size() + "Directory, please wait...", Toast.LENGTH_SHORT).show();
        int scanDepth = prefs == null ? DEFAULT_STARTUP_SCAN_DEPTH : prefs.getInt(KEY_STARTUP_SCAN_DEPTH, DEFAULT_STARTUP_SCAN_DEPTH);
        scanDepth = Math.max(1, Math.min(MAX_STARTUP_SCAN_DEPTH, scanDepth));
        final int finalScanDepth = scanDepth;
        final boolean useFastScan = prefs == null || SCAN_MODE_FAST.equals(prefs.getString(KEY_SCAN_MODE, SCAN_MODE_FAST));
        final String scanModeLabel = useFastScan ? "Fast" : "compatible";
        final List<String> scanRoots = new ArrayList<>(rootUris);
        AppExecutors.runOnSingle(() -> {
            List<ScanResult> results = new ArrayList<>();
            for (String root : scanRoots) {
                if (root == null || root.trim().isEmpty()) continue;
                try {
                    if (useFastScan) {
                        results.addAll(FastGameScanner.scan(this, Uri.parse(root), finalScanDepth));
                    } else {
                        results.addAll(GameScanner.scan(this, Uri.parse(root), finalScanDepth));
                    }
                } catch (Throwable t) {
                    Log.w("YukiHub", "library scan failed root=" + root + " mode=" + scanModeLabel, t);
                }
            }
            ScanImportStats stats = importScannedGames(results);
            if (stats.added > 0) AppExecutors.runOnIo(() -> autoMatchVndbForImportedGames(stats.importedGames));
            runOnUiThread(() -> {
                autoLibraryScanRunning = false;
                setScanLoading(false);
                loadGames();
                if (showToast) Toast.makeText(this, "Scanning[" + scanModeLabel + "] " + scanRoots.size() + "Catalog: New" + stats.added + ", already exists" + stats.skipped + " 个" + (stats.added > 0 ? ", matching automatically" + metadataSourceLabel() + "Cover" : ""), Toast.LENGTH_SHORT).show();
                // 覆盖已绑定目录的老用户：扫描完成后检查一次是否需要询问 .nomedia。
                // 只在用户主动扫描时触发，开机自动扫描不打扰。
                if (showToast) maybePromptNoMediaForActiveRoots();
            });
        });
    }

    private void runLibraryScan(Uri rootUri, boolean showToast) {
        if (rootUri == null) return;
        List<String> roots = new ArrayList<>();
        roots.add(rootUri.toString());
        runLibraryScan(roots, showToast);
    }

    private void scanLastRootOrChoose() {
        List<String> roots = getActiveScanRootUris();
        if (roots.isEmpty()) {
            launchScanRootPicker(-1);
            return;
        }
        runLibraryScan(roots, true);
    }

    private void autoScanLastRootIfAvailable() {
        List<String> roots = getActiveScanRootUris();
        if (roots.isEmpty()) return;
        runLibraryScan(roots, false);
    }

    private void setScanButtonLoadingState(boolean loading) {
        setScanLoading(loading);
    }

    private String defaultLaunchTargetForEngine(EngineType engine) {
        if (engine == EngineType.TYRANO || engine == EngineType.ARTEMIS || engine == EngineType.KIRIKIRI) return "[Game Catalog]";
        if (engine == EngineType.GAMEHUB) return "[GameHub]";
        if (engine == EngineType.PSP) return "[PSP game files]";
        return "[Game Catalog]";
    }

    private void autoMatchVndbForImportedGames(List<Game> games) {
        if (games == null || games.isEmpty()) return;
        // 按「设置 → 右侧资料源」当前选择的源来匹配，不再硬编码 VNDB。
        // （以前这里固定走 VNDB，导致扫描出来的游戏永远被标成 VNDB 源，
        //   而手动添加走 fetchCurrentSourceMetadata() 所以是正常的。）
        String sourceLabel = metadataSourceLabel();
        int matched = metadataController.autoMatchCurrentSourceForImportedGames(games);
        if (matched > 0) runOnUiThread(() -> {
            loadGames();
            Toast.makeText(MainActivity.this, "Autocompleted" + matched + " 个" + sourceLabel + "Cover", Toast.LENGTH_SHORT).show();
        });
    }

    private boolean isDesktopLaunchTarget(String target) {
        return target != null && target.trim().toLowerCase(Locale.ROOT).endsWith(".desktop");
    }

    private String guessInstalledGameHubPackage() {
        try {
            PackageManager pm = getPackageManager();
            if (pm.getLaunchIntentForPackage("com.xiaoji.egggamz") != null) return "com.xiaoji.egggamz";
            if (pm.getLaunchIntentForPackage("com.xiaoji.egggame") != null) return "com.xiaoji.egggame";
        } catch (Throwable ignored) { }
        return "com.xiaoji.egggamz";
    }

/** guessInstalledWinlatorPackage 的进程内缓存（null=未扫描过）。全量枚举+loadLabel 在主线程代价高，扫描一次后复用。 */
    private static volatile String sCachedWinlatorPackage = null;
    private static volatile long sCachedWinlatorPackageAt = 0L;
    /** 缓存有效期：5 分钟。装了新的 Winlator 改版后最多 5 分钟内猜测结果会刷新（也可手动改包名）。 */
    private static final long WINLATOR_GUESS_CACHE_MS = 5L * 60L * 1000L;

private String guessInstalledWinlatorPackage() {
 try {
            long now = System.currentTimeMillis();
            if (sCachedWinlatorPackage != null && now - sCachedWinlatorPackageAt < WINLATOR_GUESS_CACHE_MS) {
                return sCachedWinlatorPackage;
            }
            PackageManager pm = getPackageManager();
            // GET_META_DATA 无必要（只匹配包名/应用名），去掉可避免解析全部 meta
            List<ApplicationInfo> apps = pm.getInstalledApplications(0);
            String fallback = "";
            // 第一轮：仅按包名匹配（零 APK 资源加载，主流改版包名都含关键词，绝大多数在这里命中）
            for (ApplicationInfo app : apps) {
                if (app == null || app.packageName == null) continue;
                String pkg = app.packageName.toLowerCase(Locale.ROOT);
                boolean pkgHit = pkg.contains("winlator") || pkg.contains("glibc") || pkg.contains("proot");
                if (!pkgHit) continue;
                if (pm.getLaunchIntentForPackage(app.packageName) == null) continue;
                if (pkg.contains("cmod")) { sCachedWinlatorPackage = app.packageName; sCachedWinlatorPackageAt = now; return app.packageName; }
                if (fallback.isEmpty()) fallback = app.packageName;
            }
            if (!fallback.isEmpty()) { sCachedWinlatorPackage = fallback; sCachedWinlatorPackageAt = now; return fallback; }
            // 第二轮兜底：仅当包名全部未命中时，才做应用名匹配（loadLabel 要打开每个 APK 的资源，
            // 几百个应用在主线程代价极高——logcat 里成片的 ApkAssets Deleting 就是它）
            for (ApplicationInfo app : apps) {
                if (app == null || app.packageName == null) continue;
                String label;
                try { label = String.valueOf(pm.getApplicationLabel(app)).toLowerCase(Locale.ROOT); } catch (Throwable ignored) { continue; }
                if (!label.contains("winlator")) continue;
                if (pm.getLaunchIntentForPackage(app.packageName) == null) continue;
                fallback = app.packageName;
                break;
            }
            sCachedWinlatorPackage = fallback;
            sCachedWinlatorPackageAt = now;
            return fallback;
        } catch (Throwable ignored) {
            return "";
        }
    }

    private ScanImportStats importScannedGames(List<ScanResult> results) {
        ScanImportStats stats = new ScanImportStats();
        if (results == null || results.isEmpty()) return stats;
        Set<String> existing = repository.getRootUriKeySet();
        for (ScanResult r : results) {
            if (r == null || r.uri == null || r.uri.trim().isEmpty()) continue;
            String rootKey = GameRepository.normalizeRootUriKey(r.uri);
            if (existing.contains(rootKey)) {
                stats.skipped++;
                continue;
            }
            Game g = new Game();
            g.title = r.title;
            g.rootUri = r.uri;
            g.engine = r.engine;
            g.launchTarget = (r.launchTarget == null || r.launchTarget.trim().isEmpty()) ? defaultLaunchTargetForEngine(r.engine) : r.launchTarget;
            String cover = null;
            if (r.coverUri != null && !r.coverUri.trim().isEmpty()) {
                cover = copyCoverToInternalStorage(Uri.parse(r.coverUri));
            }
            if (cover == null || cover.isEmpty()) {
                Uri autoCover = findFirstLevelImage(r.uri);
                if (autoCover != null) cover = copyCoverToInternalStorage(autoCover);
            }
            if (cover != null) {
                g.coverUri = cover;
                g.coverPersistUri = cover;
                g.coverSourceType = 1;
            }
            if (r.engine == EngineType.KIRIKIRI) g.emulatorPackage = "internal.krkr";
            if (r.engine == EngineType.ONS) g.emulatorPackage = "internal.ons";
            if (r.engine == EngineType.TYRANO) g.emulatorPackage = "internal.tyrano";
            if (r.engine == EngineType.ARTEMIS) g.emulatorPackage = resolveArtemisPackageFromMarkers(g.rootUri);
            if (r.engine == EngineType.FVP) g.emulatorPackage = "internal.fvp";
            if (r.engine == EngineType.PSP) g.emulatorPackage = "org.ppsspp.ppsspp";
            if (isDesktopLaunchTarget(g.launchTarget)) g.emulatorPackage = guessInstalledWinlatorPackage();
            long newId = repository.insertIfNotExists(g);
            if (newId > 0) {
                g.id = newId;
                existing.add(rootKey);
                stats.added++;
                stats.importedGames.add(g);
            } else {
                stats.skipped++;
            }
        }
        return stats;
    }

    private static class ScanImportStats {
        int added;
        int skipped;
        final List<Game> importedGames = new ArrayList<>();
    }

    private void launchGame(Game game) {
        lastStorageProbeResult = null;
        lastStorageProbeAt = 0L;
        if (shouldShowLaunchLoadingOverlay(game)) {
            showLaunchLoadingOverlay();
        }
        // 【KRKR 内部存储启动】开启且是 KR 游戏时，先复制到内部副本再启动。
        if (shouldRunKrkrInternal(game)) {
            launchGameWithInternalCopy(game);
            return;
        }
        if (shouldProbeStorageBeforeLaunch(game)) {
            launchGameWithStorageProbe(game);
            return;
        }
        doLaunchGame(game);
    }

    /** 是否需要走「内部存储启动」流程。 */
    private boolean shouldRunKrkrInternal(Game game) {
        if (game == null || game.engine != EngineType.KIRIKIRI) return false;
        if (prefs == null || !prefs.getBoolean(KEY_KR_INTERNAL_RUN, false)) return false;
        if (game.rootUri == null || game.rootUri.trim().isEmpty()) return false;
        return true;
    }

    /**
     * 复制游戏到内部存储后启动。
     *
     * <p>复制在 IO 线程进行（大游戏可能数十秒），期间用进度对话框提示。
     * 复制失败或空间不足时**不静默降级**：明确提示并中止，避免用户误以为已生效。
     * 复制成功后把 {@code game.rootUri} 临时替换为内部路径再走正常启动链，
     * 这样 {@link EmulatorLauncher} 会识别为内部运行并关掉存档重定向。
     */
    private void launchGameWithInternalCopy(Game game) {
        final Game target = game;
        final String sourcePath = EmulatorLauncher.uriToFilePath(game.rootUri);
        if (sourcePath == null || sourcePath.trim().isEmpty()) {
            doLaunchGame(game);
            return;
        }
        final File sourceRoot = new File(sourcePath);
        if (!sourceRoot.isDirectory()) {
            // 不是本地目录（如 SAF/空目录条目），无法复制 → 按原流程启动
            Log.w("KrkrInternal", "source not a directory, fallback: " + sourcePath);
            doLaunchGame(game);
            return;
        }

        // 自绘卡片式进度窗：不依赖系统 AlertDialog 主题，
        // 避免浅色主题下「白底 + 白字」看不见内容。
        final android.view.View copyView = getLayoutInflater().inflate(R.layout.dialog_kr_internal_copy, null);
        final TextView copyGame = copyView.findViewById(R.id.tvCopyGame);
        final TextView copyPercent = copyView.findViewById(R.id.tvCopyPercent);
        final TextView copySize = copyView.findViewById(R.id.tvCopySize);
        final android.widget.ProgressBar copyBar = copyView.findViewById(R.id.pbCopy);
        copyGame.setText(target.title == null ? "" : target.title);

        final Dialog progressDialog = new Dialog(this);
        progressDialog.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
        progressDialog.setContentView(copyView);
        progressDialog.setCancelable(false);
        if (progressDialog.getWindow() != null) {
            progressDialog.getWindow().setBackgroundDrawable(
                    new android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT));
            progressDialog.getWindow().setLayout(
                    (int) (getResources().getDisplayMetrics().widthPixels * 0.86f),
                    android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        progressDialog.show();

        AppExecutors.io().execute(() -> {
            File copyDir = com.yuki.yukihub.krkr.KrkrInternalStore.ensureCopy(
                    this, target.id, target.title, sourceRoot, (copied, total) -> {
                        if (total <= 0) return;
                        final int pct = (int) Math.min(100, copied * 100 / total);
                        runOnUiThread(() -> {
                            copyPercent.setText(pct + "%");
                            copyBar.setProgress(pct);
                            copySize.setText(com.yuki.yukihub.krkr.KrkrInternalStore.formatSize(copied)
                                    + " / " + com.yuki.yukihub.krkr.KrkrInternalStore.formatSize(total));
                        });
                    });
            runOnUiThread(() -> {
                try { progressDialog.dismiss(); } catch (Throwable ignored) { }
                if (copyDir == null) {
                    clearLaunchLoadingOverlay();
                    Toast.makeText(this,
                            "Copy to internal storage failed (out of space or read/write error), startup aborted."
                                    + "You can turn off \"KRKR Internal Storage Startup\" and play the game in the original way.",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                // 临时把游戏根目录指向内部副本，再走原有启动链
                final String originalRootUri = target.rootUri;
                target.rootUri = copyDir.getAbsolutePath();
                try {
                    doLaunchGame(target);
                } finally {
                    // 还原，避免内存中的 Game 对象长期持有内部路径
                    target.rootUri = originalRootUri;
                }
            });
        });
    }

    private View launchLoadingOverlay = null;
    private int launchLoadingTipIndex = -1;
    /** PC 引擎等启动链写入的具体失败原因；doLaunchGame 弹错时优先显示，弹后清空。其余引擎恒为 null，走原有通用文案。 */
    private String lastLaunchFailReason = null;

    private boolean shouldShowLaunchLoadingOverlay(Game game) {
        if (game == null) return false;
        if (shouldProbeStorageBeforeLaunch(game)) return true;
        String pkg = game.emulatorPackage == null ? "" : game.emulatorPackage.trim();
        if (pkg.isEmpty() && game.engine == EngineType.KIRIKIRI) return true;
        return isInternalKrkrLaunchPackage(pkg);
    }

    private boolean isInternalKrkrLaunchPackage(String emulatorPackage) {
        String pkg = emulatorPackage == null ? "" : emulatorPackage.trim().toLowerCase(Locale.ROOT);
        return pkg.startsWith("internal.krkr") || pkg.equals("org.tvp.kirikiri2.internal");
    }

    private void showLaunchLoadingOverlay() {
        try {
            if (launchLoadingOverlay != null) return;
            T3.GameLoadingView overlay = new T3.GameLoadingView(this);
            overlay.setLayoutParams(new android.widget.FrameLayout.LayoutParams(-1, -1));
            ViewGroup root = findViewById(android.R.id.content);
            if (root != null) {
                root.addView(overlay);
                launchLoadingOverlay = overlay;
                launchLoadingTipIndex = overlay.getTipIndex();
            }
        } catch (Throwable ignored) { }
    }

    private void hideLaunchLoadingOverlay() {
        try {
            if (launchLoadingOverlay == null) return;
            View v = launchLoadingOverlay;
            launchLoadingOverlay = null;
            launchLoadingTipIndex = -1;
            v.animate().alpha(0f).setDuration(200L).setStartDelay(0)
                    .setInterpolator(new android.view.animation.AccelerateDecelerateInterpolator())
                    .withEndAction(() -> {
                        try { ((ViewGroup) v.getParent()).removeView(v); } catch (Throwable ignored) { }
                    }).start();
        } catch (Throwable ignored) {
            launchLoadingOverlay = null;
            launchLoadingTipIndex = -1;
        }
    }

    private void clearLaunchLoadingOverlay() {
        try {
            if (launchLoadingOverlay == null) return;
            View v = launchLoadingOverlay;
            launchLoadingOverlay = null;
            launchLoadingTipIndex = -1;
            try { v.animate().cancel(); } catch (Throwable ignored) { }
            try { ((ViewGroup) v.getParent()).removeView(v); } catch (Throwable ignored) { }
        } catch (Throwable ignored) {
            launchLoadingOverlay = null;
            launchLoadingTipIndex = -1;
        }
    }

    private void doLaunchGame(Game game) {
        String emulatorPackage = game.emulatorPackage == null ? "" : game.emulatorPackage.trim();
        if (emulatorPackage.isEmpty() && game.engine == EngineType.KIRIKIRI) emulatorPackage = "internal.krkr";
        if (emulatorPackage.isEmpty() && game.engine == EngineType.ONS) emulatorPackage = "internal.ons";
        if (emulatorPackage.isEmpty() && game.engine == EngineType.TYRANO) emulatorPackage = "internal.tyrano";
        if (emulatorPackage.isEmpty() && game.engine == EngineType.FVP) emulatorPackage = "internal.fvp";
        if (emulatorPackage.isEmpty() && game.engine == EngineType.WINLATOR) emulatorPackage = guessInstalledWinlatorPackage();
        if (emulatorPackage.isEmpty() && game.engine == EngineType.PC) emulatorPackage = WinlatorPcLauncher.PACKAGE_NAME;
        if (emulatorPackage.isEmpty() && game.engine == EngineType.GAMEHUB) emulatorPackage = guessInstalledGameHubPackage();
        if (emulatorPackage.isEmpty() && game.engine == EngineType.PSP) emulatorPackage = "org.ppsspp.ppsspp";
        if (game.engine == EngineType.ARTEMIS) {
            emulatorPackage = normalizeArtemisPackage(emulatorPackage);
        }
        String launchTarget = game.launchTarget;
        if (game.engine == EngineType.ARTEMIS || game.engine == EngineType.TYRANO || game.engine == EngineType.FVP) launchTarget = "[Game Catalog]";
        if (game.engine == EngineType.GAMEHUB) {
            String ghMode = game.gamehubLaunchMode == null ? "game" : game.gamehubLaunchMode.trim().toLowerCase(Locale.ROOT);
            if (!("program".equals(ghMode) || "normal".equals(ghMode)) && (game.gamehubLocalGameId == null || game.gamehubLocalGameId.trim().isEmpty())) { clearLaunchLoadingOverlay(); Toast.makeText(this, "Please edit the game first and import GameHub localGameId through Shizuku.", Toast.LENGTH_LONG).show(); return; }
            launchTarget = game.title;
        }
        if (emulatorPackage.isEmpty()) { clearLaunchLoadingOverlay(); Toast.makeText(this, "Please edit the game first and fill in the emulator package name.", Toast.LENGTH_LONG).show(); return; }
        runningGameId = game.id;
        sessionStart = System.currentTimeMillis();
        String launchType = resolveLaunchType(emulatorPackage);
        runningSessionId = repository.startPlaySession(game.id, sessionStart, launchType);
        // 复位「本次运行已处理过未完成记录」标记：本次游玩若异常结束，
        // 回到启动器时应当能再次得到提示，而不是被之前的标记压掉。
        GameRepository.resetStaleSessionHandled();
        launchedExternal = true;
        // 标记正在玩（Steam 风格），并确保后台心跳继续
        try {
            if (presenceManager == null) presenceManager = com.yuki.yukihub.social.PresenceManager.get(this);
            presenceManager.setPlayingGame(game.title);
            com.yuki.yukihub.social.PresenceService.sync(this);
            com.yuki.yukihub.social.PresenceService.refresh(this);
        } catch (Throwable ignored) {}
        if (!launchGameInternal(game, emulatorPackage, launchTarget)) {
            clearLaunchLoadingOverlay();
            repository.cancelPlaySession(runningSessionId);
            launchedExternal = false;
            runningGameId = -1;
            runningSessionId = -1;
            sessionStart = 0;
            try {
                if (presenceManager != null) {
                    presenceManager.clearPlayingGame();
                    com.yuki.yukihub.social.PresenceService.refresh(this);
                }
            } catch (Throwable ignored) {}
            Toast.makeText(this, lastLaunchFailReason != null ? lastLaunchFailReason : "Launch failed: The emulator was not found or the emulator does not accept the current launch target", Toast.LENGTH_LONG).show();
            lastLaunchFailReason = null;
        }
    }

    private boolean shouldProbeStorageBeforeLaunch(Game game) {
        if (game == null || game.rootUri == null || game.rootUri.trim().isEmpty()) return false;
        // If the user explicitly enabled YukiHub/app-private save redirection,
        // keep the old scoped-save path and do not enable the SAF file fallback hook.
        if (isScopedSaveEnabledFor(game.engine)) return false;
        if (game.engine == EngineType.KIRIKIRI) return true;
        if (game.engine == EngineType.ARTEMIS) return true;
        return false;
    }

    private void launchGameWithStorageProbe(Game game) {
        final Game target = game;
        final AtomicBoolean launched = new AtomicBoolean(false);
        Future<?> future = AppExecutors.io().submit(() -> {
            StorageProbeResult result = probeGameStorage(target);
            Log.i("YukiStorageProbe", result.toLogLine());
            runOnUiThread(() -> {
                if (!launched.compareAndSet(false, true)) return;
                handleStorageProbeResultBeforeLaunch(target, result);
            });
        });
        AppExecutors.schedule(() -> runOnUiThread(() -> {
            if (!launched.compareAndSet(false, true)) return;
            Log.w("YukiStorageProbe", "probe timeout after " + STORAGE_PROBE_TIMEOUT_MS + "ms root=" + (target == null ? null : target.rootUri));
            try { future.cancel(true); } catch (Throwable ignored) { }
            doLaunchGame(target);
        }), STORAGE_PROBE_TIMEOUT_MS);
    }

    private void handleStorageProbeResultBeforeLaunch(Game game, StorageProbeResult result) {
        lastStorageProbeResult = result;
        lastStorageProbeAt = System.currentTimeMillis();
        if (game == null) return;
        if (result != null && result.rawResolved && !result.rawWriteOk) {
            boolean scopedEnabled = isScopedSaveEnabledFor(game.engine);
            boolean safCanHandle = canUseKrSafFileFallback(game, result);
            String engine = game.engine == null ? "Engine" : game.engine.getDisplayName();
            Log.w("YukiStorageProbe", "raw write unavailable for " + engine + ", scopedSaveEnabled=" + scopedEnabled + ", safCanHandle=" + safCanHandle + ", rawPath=" + result.rawPath + ", err=" + result.writeError + ", safErr=" + result.safError);
            if (!scopedEnabled && !safCanHandle) {
                Toast.makeText(this, "It is detected that the game directory may not be writable. If it crashes or cannot be archived, please enable it in the settings." + engine + "Independent archive directory.", Toast.LENGTH_LONG).show();
            }
        }
        if (result != null && result.rawResolved && !result.rawReadOk) {
            Log.w("YukiStorageProbe", "raw read unavailable rawPath=" + result.rawPath + ", err=" + result.readError + ", safErr=" + result.safError);
        }
        doLaunchGame(game);
    }
private boolean isScopedSaveEnabledFor(EngineType engine) {
        if (prefs == null || engine == null) return false;
        if (engine == EngineType.KIRIKIRI) return prefs.getBoolean(KEY_KR_SCOPED_SAVE_DIR, false);
        if (engine == EngineType.ARTEMIS) return prefs.getBoolean(KEY_ARTEMIS_SCOPED_SAVE_DIR, false);
        return false;
    }

    // ==================== KRKR 内部存储启动 ====================

    /** 设置页的「已用 / 可用」摘要文本。 */
    private void refreshKrInternalUsage(TextView tv) {
        if (tv == null) return;
        tv.setText("Currently used internally:" + com.yuki.yukihub.krkr.KrkrInternalStore.formatSize(
                        com.yuki.yukihub.krkr.KrkrInternalStore.usedBytes(this))
                + "Available:" + com.yuki.yukihub.krkr.KrkrInternalStore.formatSize(
                        com.yuki.yukihub.krkr.KrkrInternalStore.availableBytes(this)));
    }

    /**
     * 「管理内部存储」对话框：列出所有内部副本、占用大小，支持单个/全部清理。
     *
     * <p>统计与删除都放到 IO 线程，避免大目录卡住 UI。
     */
    private void showKrInternalStoreDialog(TextView usageView) {
        final android.view.View view = getLayoutInflater().inflate(R.layout.dialog_kr_internal_store, null);
        final TextView summary = view.findViewById(R.id.tvKrMgrSummary);
        final android.widget.ProgressBar usageBar = view.findViewById(R.id.pbKrMgrUsage);
        final android.widget.LinearLayout listBox = view.findViewById(R.id.llKrMgrList);

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(
                    new android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.getWindow().setLayout(
                    (int) (getResources().getDisplayMetrics().widthPixels * 0.90f),
                    android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        view.findViewById(R.id.tvKrMgrClose).setOnClickListener(v -> dialog.dismiss());
        view.findViewById(R.id.btnKrMgrDone).setOnClickListener(v -> dialog.dismiss());
        view.findViewById(R.id.btnKrMgrClearAll).setOnClickListener(v ->
                new AlertDialog.Builder(this)
                        .setTitle("Clear all internal copies?")
                        .setMessage("All copied games and their internal saves will be deleted and cannot be recovered.\n(The external original directory is not affected)")
                        .setPositiveButton("Confirm clearing", (d2, w) -> AppExecutors.io().execute(() -> {
                            com.yuki.yukihub.krkr.KrkrInternalStore.removeAll(this);
                            runOnUiThread(() -> {
                                loadKrInternalList(listBox, summary, usageBar);
                                refreshKrInternalUsage(usageView);
                                Toast.makeText(this, "Cleared", Toast.LENGTH_SHORT).show();
                            });
                        }))
                        .setNegativeButton("Cancel", null)
                        .show());

        dialog.show();
        loadKrInternalList(listBox, summary, usageBar);
    }

    /** 异步填充副本列表。 */
    private void loadKrInternalList(android.widget.LinearLayout listBox, TextView summary,
                                    android.widget.ProgressBar usageBar) {
        summary.setText("Counting...");
        listBox.removeAllViews();
        AppExecutors.io().execute(() -> {
            final java.util.List<com.yuki.yukihub.krkr.KrkrInternalStore.Entry> entries =
                    com.yuki.yukihub.krkr.KrkrInternalStore.list(this);
            final long used = com.yuki.yukihub.krkr.KrkrInternalStore.usedBytes(this);
            final long avail = com.yuki.yukihub.krkr.KrkrInternalStore.availableBytes(this);
            runOnUiThread(() -> {
                summary.setText("Used" + com.yuki.yukihub.krkr.KrkrInternalStore.formatSize(used)
                        + "·Available" + com.yuki.yukihub.krkr.KrkrInternalStore.formatSize(avail)
                        + "· common" + entries.size() + " 个");
                if (usageBar != null) {
                    // 进度 = 已用 /（已用 + 可用），直观展示内部空间占用
                    long denom = used + avail;
                    usageBar.setProgress(denom <= 0 ? 0 : (int) Math.min(100, used * 100 / denom));
                }
                if (entries.isEmpty()) {
                    TextView empty = new TextView(this);
                    empty.setText("No internal copy yet\n\nAfter turning on \"KRKR Internal Storage Startup\"\nThe KR game will be automatically copied here when you start it for the first time.");
                    empty.setTextColor(getColorCompat(R.color.yh_text_muted));
                    empty.setTextSize(12);
                    empty.setGravity(android.view.Gravity.CENTER);
                    empty.setPadding(0, dp(24), 0, dp(24));
                    listBox.addView(empty);
                    return;
                }
                for (final com.yuki.yukihub.krkr.KrkrInternalStore.Entry e : entries) {
                    listBox.addView(buildKrInternalRow(e, listBox, summary, usageBar));
                }
            });
        });
    }

    /** 构造单个副本行：卡片式，左名称+大小，右删除按钮。 */
    private android.view.View buildKrInternalRow(
            final com.yuki.yukihub.krkr.KrkrInternalStore.Entry e,
            final android.widget.LinearLayout listBox, final TextView summary,
            final android.widget.ProgressBar usageBar) {
        android.widget.LinearLayout row = new android.widget.LinearLayout(this);
        row.setOrientation(android.widget.LinearLayout.HORIZONTAL);
        row.setGravity(android.view.Gravity.CENTER_VERTICAL);
        row.setBackground(getDrawable(R.drawable.bg_auth_input));
        row.setPadding(dp(12), dp(10), dp(10), dp(10));
        android.widget.LinearLayout.LayoutParams rowLp = new android.widget.LinearLayout.LayoutParams(
                android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
        rowLp.bottomMargin = dp(8);
        row.setLayoutParams(rowLp);

        android.widget.LinearLayout textCol = new android.widget.LinearLayout(this);
        textCol.setOrientation(android.widget.LinearLayout.VERTICAL);

        TextView name = new TextView(this);
        name.setText(e.displayName);
        name.setTextColor(getColorCompat(R.color.yh_text));
        name.setTextSize(13);
        name.setMaxLines(1);
        name.setEllipsize(android.text.TextUtils.TruncateAt.MIDDLE);
        textCol.addView(name);

        TextView size = new TextView(this);
        size.setText(com.yuki.yukihub.krkr.KrkrInternalStore.formatSize(e.bytes));
        size.setTextColor(getColorCompat(R.color.yh_text_muted));
        size.setTextSize(11);
        size.setPadding(0, dp(2), 0, 0);
        textCol.addView(size);

        row.addView(textCol, new android.widget.LinearLayout.LayoutParams(0, -2, 1f));

        TextView del = new TextView(this);
        del.setText("Delete");
        del.setTextColor(getColorCompat(R.color.yh_warning));
        del.setTextSize(12);
        del.setPadding(dp(12), dp(6), dp(4), dp(6));
        del.setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle("Delete\"" + e.displayName + "」？")
                .setMessage("The archives in this copy will also be deleted and cannot be restored.\n(The external original directory is not affected)")
                .setPositiveButton("Confirm deletion", (d2, w) -> AppExecutors.io().execute(() -> {
                    com.yuki.yukihub.krkr.KrkrInternalStore.remove(this, e.dirName);
                    runOnUiThread(() -> {
                        loadKrInternalList(listBox, summary, usageBar);
                        Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
                    });
                }))
                .setNegativeButton("Cancel", null)
                .show());
        row.addView(del);
        return row;
    }

    // ==================== /KRKR 内部存储启动 ====================


    private boolean shouldUseKrSafFileFallback(Game game) {
        return canUseKrSafFileFallback(game, lastStorageProbeResult)
                && System.currentTimeMillis() - lastStorageProbeAt <= 5000L;
    }

    private boolean canUseKrSafFileFallback(Game game, StorageProbeResult r) {
        if (game == null || game.engine != EngineType.KIRIKIRI) return false;
        if (isScopedSaveEnabledFor(game.engine)) return false;
        if (r == null || !r.rawResolved || !r.safTreeCoversPath || !r.safWriteOk) return false;
        return r.rawReadOk || r.safReadOk;
    }

    private StorageProbeResult probeGameStorage(Game game) {
        long start = System.currentTimeMillis();
        StorageProbeResult result = new StorageProbeResult();
        result.engine = game == null || game.engine == null ? "unknown" : game.engine.name();
        result.rootUri = game == null ? null : game.rootUri;
        result.rawPath = fastRawPathFromUri(result.rootUri);
        result.rawResolved = result.rawPath != null && result.rawPath.startsWith("/");
        try {
            File appExternal = getExternalFilesDir(null);
            result.appPrivateWriteOk = quickWriteProbe(appExternal, ".yukihub_app_probe");
        } catch (Throwable t) {
            result.appPrivateError = shortError(t);
        }
        if (!result.rawResolved) {
            result.elapsedMs = System.currentTimeMillis() - start;
            result.readError = "raw path unavailable";
            return result;
        }
        File root = new File(result.rawPath);
        try {
            result.rawExists = root.exists();
            result.rawIsDirectory = root.isDirectory();
            if (result.rawIsDirectory) {
                String[] names = root.list();
                result.rawReadOk = names != null;
            } else {
                result.rawReadOk = root.isFile() && root.canRead();
            }
        } catch (Throwable t) {
            result.readError = shortError(t);
        }
        if (!result.rawReadOk && result.readError == null) result.readError = "list/canRead failed";
        try {
            File writeDir = root.isDirectory() ? root : root.getParentFile();
            result.rawWriteOk = quickWriteProbe(writeDir, ".yukihub_write_probe");
        } catch (Throwable t) {
            result.writeError = shortError(t);
        }
        if (!result.rawWriteOk && result.writeError == null) result.writeError = "create/write/delete failed";
        if (game != null && game.engine == EngineType.KIRIKIRI) {
            probeSafWriteFallback(result);
        } else if (!result.rawReadOk || !result.rawWriteOk) {
            probeSafWriteFallback(result);
        }
        result.elapsedMs = System.currentTimeMillis() - start;
        return result;
    }

    private void probeSafWriteFallback(StorageProbeResult result) {
        if (result == null || !result.rawResolved || result.rawPath == null || result.rawPath.trim().isEmpty()) return;
        result.safCandidate = result.rawPath.startsWith("/storage/") || result.rawPath.startsWith("/sdcard");
        if (!result.safCandidate) return;
        try {
            SafPath safPath = toSafPath(result.rawPath);
            if (safPath == null || safPath.volume == null || safPath.rel == null) {
                result.safError = "raw path cannot map to SAF doc id";
                return;
            }
            ContentResolver resolver = getContentResolver();
            if (resolver == null) {
                result.safError = "content resolver unavailable";
                return;
            }
            for (UriPermission perm : resolver.getPersistedUriPermissions()) {
                if (perm == null || perm.getUri() == null) continue;
                String treeId;
                try { treeId = DocumentsContract.getTreeDocumentId(perm.getUri()); } catch (Throwable ignored) { continue; }
                if (treeId == null) continue;
                String decodedTreeId = Uri.decode(treeId);
                if (decodedTreeId == null || !decodedTreeId.startsWith(safPath.volume + ":")) continue;
                String treeRel = decodedTreeId.substring((safPath.volume + ":").length());
                if (!treeRel.isEmpty() && !safPath.rel.equals(treeRel) && !safPath.rel.startsWith(treeRel + "/")) continue;
                result.safTreeCoversPath = true;
                result.safReadOk = perm.isReadPermission();
                boolean safTargetIsDirectory = result.rawIsDirectory || isSafTargetDirectory(perm.getUri(), decodedTreeId, safPath);
                if (!perm.isWritePermission()) {
                    result.safError = "persisted SAF tree is read-only";
                    return;
                }
                Uri probeUri = createSafProbeDocument(resolver, perm.getUri(), decodedTreeId, safPath, safTargetIsDirectory, ".yukihub_saf_probe_" + android.os.Process.myPid() + "_" + System.nanoTime() + ".tmp");
                if (probeUri == null) {
                    result.safError = "create SAF probe failed";
                    return;
                }
                try (OutputStream out = resolver.openOutputStream(probeUri, "wt")) {
                    if (out == null) {
                        result.safError = "open SAF probe output failed";
                        return;
                    }
                    out.write(new byte[]{'Y', 'H'});
                    out.flush();
                } finally {
                    try { DocumentsContract.deleteDocument(resolver, probeUri); } catch (Throwable ignored) { }
                }
                result.safWriteOk = true;
                result.safError = null;
                return;
            }
            result.safError = "no persisted SAF tree covers raw path";
        } catch (Throwable t) {
            result.safError = shortError(t);
        }
    }

    private SafPath toSafPath(String path) {
        if (path == null) return null;
        String p = path.trim();
        if (p.startsWith("file://")) p = p.substring("file://".length());
        while (p.contains("//")) p = p.replace("//", "/");
        String volume;
        String rel;
        if (p.startsWith("/storage/emulated/0/")) {
            volume = "primary";
            rel = p.substring("/storage/emulated/0/".length());
        } else if ("/storage/emulated/0".equals(p)) {
            volume = "primary";
            rel = "";
        } else if (p.startsWith("/sdcard/")) {
            volume = "primary";
            rel = p.substring("/sdcard/".length());
        } else if ("/sdcard".equals(p)) {
            volume = "primary";
            rel = "";
        } else if (p.startsWith("/storage/")) {
            String rest = p.substring("/storage/".length());
            int slash = rest.indexOf('/');
            if (slash <= 0) return null;
            volume = rest.substring(0, slash);
            rel = rest.substring(slash + 1);
        } else {
            return null;
        }
        if (volume == null || volume.isEmpty() || rel == null) return null;
        return new SafPath(volume, rel);
    }

    private boolean isSafTargetDirectory(Uri tree, String decodedTreeId, SafPath safPath) {
        try {
            if (tree == null || safPath == null) return false;
            DocumentFile current = DocumentFile.fromTreeUri(this, tree);
            if (current == null) return false;
            String treePrefix = safPath.volume + ":";
            String localRel = safPath.rel;
            String treeRel = decodedTreeId != null && decodedTreeId.startsWith(treePrefix) ? decodedTreeId.substring(treePrefix.length()) : "";
            if (!treeRel.isEmpty()) {
                if (localRel.equals(treeRel)) localRel = "";
                else if (localRel.startsWith(treeRel + "/")) localRel = localRel.substring(treeRel.length() + 1);
            }
            if (localRel == null || localRel.isEmpty()) return current.isDirectory();
            String[] parts = localRel.split("/");
            for (String part : parts) {
                if (part == null || part.isEmpty() || ".".equals(part)) continue;
                current = current.findFile(part);
                if (current == null) return false;
            }
            return current.isDirectory();
        } catch (Throwable ignored) {
            return false;
        }
    }

    private Uri createSafProbeDocument(ContentResolver resolver, Uri tree, String decodedTreeId, SafPath safPath, boolean rawIsDirectory, String probeName) {
        try {
            if (resolver == null || tree == null || safPath == null || probeName == null || probeName.trim().isEmpty()) return null;
            DocumentFile dir = DocumentFile.fromTreeUri(this, tree);
            if (dir == null) return null;
            String treePrefix = safPath.volume + ":";
            String localRel = safPath.rel;
            String treeRel = decodedTreeId != null && decodedTreeId.startsWith(treePrefix) ? decodedTreeId.substring(treePrefix.length()) : "";
            if (!treeRel.isEmpty()) {
                if (localRel.equals(treeRel)) localRel = "";
                else if (localRel.startsWith(treeRel + "/")) localRel = localRel.substring(treeRel.length() + 1);
            }
            String[] parts = localRel.split("/");
            DocumentFile current = dir;
            int end = rawIsDirectory ? parts.length : Math.max(0, parts.length - 1);
            for (int i = 0; i < end; i++) {
                String part = parts[i];
                if (part == null || part.isEmpty() || ".".equals(part)) continue;
                DocumentFile child = current.findFile(part);
                if (child == null) child = current.createDirectory(part);
                if (child == null || !child.isDirectory()) return null;
                current = child;
            }
            DocumentFile existing = current.findFile(probeName);
            if (existing != null) {
                try { existing.delete(); } catch (Throwable ignored) { }
            }
            DocumentFile probe = current.createFile("application/octet-stream", probeName);
            return probe == null ? null : probe.getUri();
        } catch (Throwable t) {
            Log.w("YukiStorageProbe", "create SAF probe failed", t);
            return null;
        }
    }

    private static class SafPath {
        final String volume;
        final String rel;
        SafPath(String volume, String rel) {
            this.volume = volume;
            this.rel = rel;
        }
    }

    private boolean quickWriteProbe(File dir, String prefix) throws Exception {
        if (dir == null || !dir.isDirectory()) return false;
        File probe = new File(dir, prefix + "_" + android.os.Process.myPid() + "_" + System.nanoTime() + ".tmp");
        boolean ok = false;
        try (FileOutputStream out = new FileOutputStream(probe, false)) {
            out.write(new byte[]{'Y', 'H'});
            out.flush();
            ok = probe.isFile() && probe.length() >= 2;
        } finally {
            if (probe.exists() && !probe.delete()) Log.w("YukiStorageProbe", "probe delete failed " + probe.getAbsolutePath());
        }
        return ok;
    }

    private String fastRawPathFromUri(String value) {
        if (value == null || value.trim().isEmpty()) return value;
        String s = value.trim();
        if (s.startsWith("file://")) {
            try { return Uri.parse(s).getPath(); } catch (Throwable ignored) { return s.substring("file://".length()); }
        }
        if (s.startsWith("/")) return s;
        try {
            Uri uri = Uri.parse(s);
            String docId = null;
            String path = uri.getPath();
            if (path != null && path.contains("/document/")) {
                try { docId = DocumentsContract.getDocumentId(uri); } catch (Throwable ignored) { }
            }
            if (docId == null || docId.isEmpty()) {
                try { docId = DocumentsContract.getTreeDocumentId(uri); } catch (Throwable ignored) { }
            }
            if (docId == null || docId.isEmpty()) {
                try { docId = DocumentsContract.getDocumentId(uri); } catch (Throwable ignored) { }
            }
            if (docId != null && !docId.isEmpty()) {
                int colon = docId.indexOf(':');
                String volume = colon >= 0 ? docId.substring(0, colon) : docId;
                String rel = colon >= 0 ? docId.substring(colon + 1) : "";
                if ("primary".equalsIgnoreCase(volume)) return rel.isEmpty() ? "/storage/emulated/0" : "/storage/emulated/0/" + rel;
                if (volume != null && !volume.isEmpty()) return rel.isEmpty() ? "/storage/" + volume : "/storage/" + volume + "/" + rel;
            }
            String p = uri.getPath();
            return p == null ? s : p;
        } catch (Throwable t) {
            return s;
        }
    }

    private String shortError(Throwable t) {
        if (t == null) return null;
        String msg = t.getMessage();
        String name = t.getClass().getSimpleName();
        return msg == null || msg.trim().isEmpty() ? name : name + ": " + msg;
    }

    private static class StorageProbeResult {
        String engine;
        String rootUri;
        String rawPath;
        boolean rawResolved;
        boolean rawExists;
        boolean rawIsDirectory;
        boolean rawReadOk;
        boolean rawWriteOk;
        boolean safCandidate;
        boolean safTreeCoversPath;
        boolean safReadOk;
        boolean safWriteOk;
        boolean appPrivateWriteOk;
        String readError;
        String writeError;
        String safError;
        String appPrivateError;
        long elapsedMs;

        String toLogLine() {
            return "engine=" + engine
                    + " rawResolved=" + rawResolved
                    + " rawExists=" + rawExists
                    + " rawDir=" + rawIsDirectory
                    + " rawReadOk=" + rawReadOk
                    + " rawWriteOk=" + rawWriteOk
                    + " safCandidate=" + safCandidate
                    + " safCovers=" + safTreeCoversPath
                    + " safReadOk=" + safReadOk
                    + " safWriteOk=" + safWriteOk
                    + " appPrivateWriteOk=" + appPrivateWriteOk
                    + " elapsedMs=" + elapsedMs
                    + " rawPath=" + rawPath
                    + " readErr=" + readError
                    + " writeErr=" + writeError
                    + " safErr=" + safError
                    + " appErr=" + appPrivateError;
        }
    }

    private boolean launchGameInternal(Game game, String emulatorPackage, String launchTarget) {
        if (game == null || emulatorPackage == null || emulatorPackage.trim().isEmpty()) return false;
        String pkg = emulatorPackage.trim();
        if (pkg.startsWith("internal.krkr") || pkg.equals("org.tvp.kirikiri2.internal")) {
boolean compatMode = prefs != null && prefs.getBoolean(KEY_KR_COMPAT_MODE, false);
String krEngineVersion = prefs == null ? "auto" : prefs.getString(KEY_KR_ENGINE_VERSION, "auto");
boolean safFileFallback = shouldUseKrSafFileFallback(game);
if (safFileFallback) Log.i("YukiStorageProbe", "enable KR SAF file fallback for root=" + game.rootUri);
android.content.Intent intent = EmulatorLauncher.buildInternalKrkrIntent(this, game.rootUri, launchTarget, false, compatMode, krEngineVersion, safFileFallback);
if (launchLoadingOverlay != null) {
    intent.putExtra(T3.GameLoadingView.EXTRA_CONTINUE_LOADING, true);
    intent.putExtra(T3.GameLoadingView.EXTRA_INITIAL_TIP_INDEX, launchLoadingTipIndex);
}
return startActivitySafely(intent);
}
        if (pkg.startsWith("internal.tyrano") || pkg.equals("com.yuki.yukihub.tyrano")) {
            return startActivitySafely(EmulatorLauncher.buildInternalTyranoIntent(this, game.rootUri, launchTarget));
        }
        if (pkg.startsWith("internal.ons") || pkg.equals("com.yuki.yukihub.ons")) {
            return startActivitySafely(EmulatorLauncher.buildInternalOnsIntent(this, game.rootUri, launchTarget));
        }
        if (pkg.startsWith("internal.artemis")) {
            return startActivitySafely(EmulatorLauncher.buildInternalArtemisIntent(this, pkg, game.rootUri, launchTarget));
        }
        if (pkg.startsWith("internal.fvp") || pkg.equals("com.yuki.yukihub.fvp")) {
            // FVP 引擎（rfvp）内嵌在本应用；参数全部走单游戏设置（FvpLaunchPrefs）。
            Intent fvpIntent = EmulatorLauncher.buildInternalFvpIntent(
                    this, game.rootUri, launchTarget, FvpLaunchPrefs.load(this, game.id, game.rootUri));
            if (fvpIntent == null) {
                lastLaunchFailReason = "The FVP engine cannot locate the game directory (the SAF directory does not support the built-in engine), please reselect the local real path.";
                return false;
            }
            return startActivitySafely(fvpIntent);
        }
        if (pkg.startsWith("internal.psp") || pkg.equals("org.ppsspp.ppsspp")) {
            // 检查PPSSPP是否安装
            if (!EmulatorLauncher.isPPSSPPInstalled(this)) {
                // 显示提示对话框
                new AlertDialog.Builder(this)
                    .setTitle("Requires PPSSPP emulator")
                    .setMessage("Launching PSP games requires installing the PPSSPP emulator.\n\nDo you want to download it now?")
                    .setPositiveButton("Go to download", (d, w) -> {
                        try {
                            startActivity(EmulatorLauncher.getPPSSPPDownloadIntent());
                        } catch (Exception e) {
                            Toast.makeText(MainActivity.this, "Can't open app store", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
                return false;
            }
            return startActivitySafely(EmulatorLauncher.buildInternalPspIntent(this, game.rootUri, launchTarget));
        }
        if (game.engine == EngineType.ANDROID) {
            return EmulatorLauncher.launch(this, pkg);
        }
        if (game.engine == EngineType.PC) {
            WinlatorPcLauncher.Result result = WinlatorPcLauncher.launch(
                    this, game.rootUri, launchTarget, PcLaunchPrefs.load(this, game.id));
            if (!result.success) {
                // 具体原因写入 lastLaunchFailReason，由外层 doLaunchGame 统一弹一条，避免双 Toast
                if (WinlatorPcLauncher.CODE_PACKAGE_NOT_INSTALLED.equals(result.code)) {
                    lastLaunchFailReason = "PC games need to install Winlator (winlator-cn version) to start";
                } else if (WinlatorPcLauncher.CODE_EXTERNAL_LAUNCH_UNSUPPORTED.equals(result.code)) {
                    lastLaunchFailReason = "Currently Winlator does not support external boot protocol, please install the latest version of winlator-cn";
                } else if (WinlatorPcLauncher.CODE_DIR_UNRESOLVED.equals(result.code)) {
                    lastLaunchFailReason = "PC Engine cannot locate the game directory (the SAF directory does not support external startup), please reselect the local real path.";
                } else if (WinlatorPcLauncher.CODE_EXE_UNRESOLVED.equals(result.code)) {
                    lastLaunchFailReason = "No bootable exe found, please reselect the startup target in editing";
                } else {
                    lastLaunchFailReason = "PC engine failed to start, please check Winlator installation status";
                }
                return false;
            }
            return true;
        }
        return EmulatorLauncher.launchGame(this, emulatorPackage, game.rootUri, launchTarget, game.winlatorLaunchMode, game.gamehubLaunchMode, game.gamehubLocalGameId);
    }

    private boolean startActivitySafely(android.content.Intent intent) {
        if (intent == null) return false;
        try {
            startActivity(intent);
            return true;
        } catch (Throwable t) {
            Log.w("YukiHub", "startActivitySafely failed", t);
            return false;
        }
    }

    private String resolveLaunchType(String emulatorPackage) {
        String pkg = emulatorPackage == null ? "" : emulatorPackage.trim().toLowerCase(Locale.ROOT);
        if (pkg.startsWith("internal.krkr") || pkg.equals("org.tvp.kirikiri2.internal")) return "internal.krkr";
        if (pkg.startsWith("internal.ons") || pkg.equals("com.yuki.yukihub.ons")) return "internal.ons";
        if (pkg.startsWith("internal.tyrano") || pkg.equals("com.yuki.yukihub.tyrano")) return "internal.tyrano";
        if (pkg.startsWith("internal.artemis")) return pkg;
        if (pkg.startsWith("internal.fvp") || pkg.equals("com.yuki.yukihub.fvp")) return "internal.fvp";
        return "external";
    }

    private void finishCurrentPlaySessionIfAny() {
        if (launchedExternal && runningGameId > 0 && runningSessionId > 0 && sessionStart > 0) {
            long end = System.currentTimeMillis();
            repository.finishPlaySession(runningSessionId, end, MIN_PLAY_SESSION_MS, MAX_PLAY_SESSION_MS);
            // 上报游玩时长到服务器（获得游戏经验）
            reportPlaySessionToServer(runningSessionId, sessionStart, end);
            launchedExternal = false;
            runningGameId = -1;
            runningSessionId = -1;
            sessionStart = 0;
            // 结束游玩状态
            try {
                if (presenceManager == null) presenceManager = com.yuki.yukihub.social.PresenceManager.get(this);
                presenceManager.clearPlayingGame();
                com.yuki.yukihub.social.PresenceService.refresh(this);
            } catch (Throwable ignored) {}
            loadGames();
        }
    }

    /**
     * 游戏会话结束 → 上报游玩时长到服务器（获得游戏经验）。
     * 仅登录用户上报；失败静默忽略（本地 play_sessions 仍保留，后续版本可做云同步补报）。
     */
    private void reportPlaySessionToServer(long sessionId, long start, long end) {
        if (!isLoggedIn()) return;
        AppExecutors.runOnIo(() -> {
            try {
                GameRepository.PlayActivity a = repository.findPlaySession(sessionId);
                if (a == null || a.sessionUuid == null || a.sessionUuid.isEmpty()) return;
                com.yuki.yukihub.social.SocialApiClient client = new com.yuki.yukihub.social.SocialApiClient(MainActivity.this);
                org.json.JSONObject res = client.reportPlayTime(a.sessionUuid, "", a.gameTitle == null ? "" : a.gameTitle, start, end);
                int awarded = res.optInt("expAwarded", 0);
                if (awarded > 0) {
                    Log.i("YukiHub", "Play session reported: exp +" + awarded + ", level " + res.optInt("level", 1));
                }
            } catch (Throwable t) {
                // 不阻塞游戏退出流程，但要留痕：用户开日志时能查到上报失败原因
                Log.w("YukiHub", "Play session report failed: " + t.getMessage(), t);
            }
        });
    }

    private boolean staleSessionDialogShowing = false;
    /** 未完成记录弹窗期间到达的快捷方式启动请求，弹窗关闭后再执行。 */
    private long pendingShortcutGameId = -1L;

    /**
     * 秒退阈值：未完成且起始时间距今短于此值的记录直接丢弃，不询问用户。
     * 游戏启动失败会连续产生多条这类垃圾记录，逐条询问会让弹窗看起来没完没了。
     * 正常手动退出的游戏由 finishCurrentPlaySessionIfAny 结算，不受此阈值影响。
     */
    private static final long DISCARD_OPEN_SESSION_MS = 10L * 1000L;

    /** 进程内互斥：MainActivity 与 HomeActivity 都会调用，标记放在 GameRepository 供两处共用。 */
    private void finishStalePlaySessionsIfAny() {
        if (repository == null) return;
        // 同一次运行内只处理一次，避免在两个界面间切换时反复弹窗
        if (GameRepository.isStaleSessionHandled()) return;

        // 先静默丢弃启动失败产生的秒退垃圾记录
        int discarded = repository.discardShortOpenPlaySessions(DISCARD_OPEN_SESSION_MS);
        if (discarded > 0) {
            Log.i("YukiHub", "discarded " + discarded + " short open play sessions");
        }

        PlayActivity open = repository.findLatestOpenPlaySession();
        if (open == null) {
            GameRepository.markStaleSessionHandled();
            return;
        }
        GameRepository.markStaleSessionHandled();
        staleSessionDialogShowing = true;
        // 剩余未完成记录的总数：一次性告知并统一处理，不再逐条弹窗
        int total = repository.countOpenPlaySessions();
        long now = System.currentTimeMillis();
        long rawDuration = Math.max(0L, now - open.startTime);
        long duration = Math.min(rawDuration, MAX_PLAY_SESSION_MS);
        String message = "It was detected that the latest play session did not end normally."
                + "Game:" + emptyText(open.gameTitle, "Unnamed game") + "\n"
                + "Start time:" + TimeFormatUtil.date(open.startTime) + "\n"
                + "The amount of time that can be added to the record:" + TimeFormatUtil.playTime(duration) + "\n\n"
                + (total > 1
                    ? "Also" + (total - 1) + "Earlier unfinished records will be processed together."
                    : "")
                + "If you are really playing during this period, you can choose to make a supplementary note; if it is just a test startup, crash or misoperation, please choose to ignore.";
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Found unfinished game history")
                .setMessage(message)
                .setPositiveButton("Supplementary note", (d, w) -> {
                    // 一次性结算全部未完成记录，避免下次进来又弹
                    repository.finishUnfinishedPlaySessions(
                            System.currentTimeMillis(), MIN_PLAY_SESSION_MS, MAX_PLAY_SESSION_MS);
                    loadGames();
                    updateProfilePanel();
                    Toast.makeText(MainActivity.this, "Last play time has been recorded", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Neglect", (d, w) -> {
                    // 同样一次性清掉全部，而非只删这一条
                    repository.deleteOpenPlaySessions();
                    loadGames();
                    updateProfilePanel();
                    Toast.makeText(MainActivity.this, "Incomplete records ignored", Toast.LENGTH_SHORT).show();
                })
                .setCancelable(false)
                // 这个弹窗是模态的。桌面快捷方式冷启动时它会先弹出来，
                // 若不拦住，游戏会在弹窗背后被启动，用户看不到也无法确认。
                // 因此启动请求先挂起，弹窗关掉后再执行。
                .setOnDismissListener(d -> {
                    staleSessionDialogShowing = false;
                    consumePendingShortcutLaunch();
                })
                .show();
        styleAlertDialogDark(dialog);
    }

    /** 执行被未完成记录弹窗挡住的快捷方式启动请求。 */
    private void consumePendingShortcutLaunch() {
        long gameId = pendingShortcutGameId;
        pendingShortcutGameId = -1L;
        if (gameId <= 0) return;
        getWindow().getDecorView().post(() -> {
            if (isFinishing() || isDestroyed()) return;
            launchGameFromHome(gameId);
        });
    }

@Override protected void onResume() {
     super.onResume();
     clearLaunchLoadingOverlay();
     enterImmersiveMode();
     // 启动在线心跳（引用计数 retain，避免重复 acquire）
     if (presenceManager == null) presenceManager = com.yuki.yukihub.social.PresenceManager.get(this);
     if (!activityHeartbeatHeld) {
         presenceManager.retainHeartbeat();
         activityHeartbeatHeld = true;
     }
     // 按开关同步前台保活服务
     try {
         if (isLoggedIn() && (prefs == null || prefs.getBoolean(KEY_FRIEND_PLAY_NOTIFY, true))) {
             ensureNotificationPermission();
         }
         com.yuki.yukihub.social.PresenceService.sync(this);
     } catch (Throwable ignored) {}
     finishCurrentPlaySessionIfAny();
    resumeBackgroundVideoIfNeeded();
    syncHiddenGamesIfChanged();
    
    // 自动刷新 Token（如果已登录但可能过期）
    if (isLoggedIn()) {
        AppExecutors.runOnIo(() -> {
            if (refreshAccessToken()) {
                runOnUiThread(() -> refreshRuntimeAccountState());
            }
        });
    }
    
    refreshRuntimeAccountState();
    maybeAutoWebDavSync();
    maybeAutoServerSync();
    startStatusBarUpdates();
}

@Override
public void onBackPressed() {
    // 多选模式下返回键先退出多选
    if (adapter != null && adapter.isMultiSelectMode()) {
        exitMultiSelectMode();
        return;
    }
    super.onBackPressed();
}

@Override protected void onPause() {
     pauseBackgroundVideoIfNeeded();
     stopStatusBarUpdates();
     // 游戏运行中不释放心跳：后台仍需保持「正在玩」
     // 前台服务若开启也会继续 retain；这里仅在非游玩时 release
     if (presenceManager != null && !launchedExternal && activityHeartbeatHeld) {
         presenceManager.releaseHeartbeat();
         activityHeartbeatHeld = false;
     }
     super.onPause();
 }

@Override protected void onStop() {
    clearLaunchLoadingOverlay();
    super.onStop();
}

@Override protected void onDestroy() {
 releaseBackgroundMediaPlayer();
 releaseUiSoundPool();
 stopStatusBarUpdates();
 // 释放本 Activity 持有的心跳；真正离线由 logout / 服务端超时判定
 // 若仍在游玩且前台服务活着，服务会继续 retain
 if (presenceManager != null && activityHeartbeatHeld) {
     try { presenceManager.releaseHeartbeat(); } catch (Throwable ignored) {}
     activityHeartbeatHeld = false;
 }
 super.onDestroy();
}

private void resumeBackgroundVideoIfNeeded() {
    if (prefs == null || !"video".equals(prefs.getString(KEY_CUSTOM_BACKGROUND_TYPE, "image"))) return;
    if (backgroundMediaPlayer != null) {
        try { if (!backgroundMediaPlayer.isPlaying()) backgroundMediaPlayer.start(); } catch (Throwable ignored) { }
    } else if (pendingBackgroundVideoUri != null) {
        TextureView textureView = findViewById(R.id.customBackgroundVideo);
        if (textureView != null && textureView.getVisibility() == View.VISIBLE) playBackgroundVideo(textureView, pendingBackgroundVideoUri, false);
    }
}

private void pauseBackgroundVideoIfNeeded() {
    if (prefs == null || !"video".equals(prefs.getString(KEY_CUSTOM_BACKGROUND_TYPE, "image"))) return;
    try { if (backgroundMediaPlayer != null && backgroundMediaPlayer.isPlaying()) backgroundMediaPlayer.pause(); } catch (Throwable ignored) { }
}

    private String emptyText(String s, String fallback) { return s == null || s.trim().isEmpty() ? fallback : s; }
}
