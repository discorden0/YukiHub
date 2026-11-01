package com.yuki.yukihub.music;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.yuki.yukihub.R;
import com.yuki.yukihub.data.MusicRepository;
import com.yuki.yukihub.data.MusicSafHelper;
import com.yuki.yukihub.model.MusicAlbum;
import com.yuki.yukihub.model.MusicTrack;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 音乐库管理页（音乐厅数据录入的唯一入口）。
 *
 * <p>「录入在 App，鉴赏在展厅」：这里只做**管理**（添加 / 编辑 / 绑 PV / 删除），
 * 播放与鉴赏全在 3D 展厅里完成。
 *
 * <p><b>引用模式</b>：添加文件时只做 **SAF 持久化授权 + 记 URI**，
 * 绝不复制文件；删除时只删数据库记录，**永不碰用户的原文件**。
 *
 * <p><b>UI 范式</b>：纯 Java 构建 View（与 HomeActivity / 各大对话框一致），
 * 不新增 layout XML —— 项目里除主界面外基本都是这个风格。
 */
public class MusicLibraryActivity extends Activity {

    private static final int REQ_FOLDER = 1001;   // 添加专辑（选文件夹）
    private static final int REQ_AUDIO = 1002;   // 添加单曲（选音频）
    private static final int REQ_PV = 1003;   // 绑定 PV（选视频）
    private static final int REQ_COVER = 1004;   // 换封面（选图片）

    /** 当前等待结果的上下文（哪个专辑 / 哪条曲目） */
    private long pendingTrackId = 0;      // 绑 PV 用
    private long pendingAlbumId = 0;      // 换封面用

    private MusicRepository repo;
    private final Handler main = new Handler(Looper.getMainLooper());

    private LinearLayout listContainer;
    private TextView emptyHint;
    private ProgressBar progress;
    private boolean busy = false;

    /** 展开状态（专辑 id → 是否展开曲目列表） */
    private final java.util.Set<Long> expanded = new java.util.HashSet<>();

    /* ==================== 生命周期 ==================== */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repo = new MusicRepository(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(buildRoot());
        reload();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }

    /* ==================== 界面骨架 ==================== */

    private View buildRoot() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(getColorCompat(R.color.yh_bg));

        // —— 顶栏：返回 + 标题 + 添加 ——
        LinearLayout top = new LinearLayout(this);
        top.setOrientation(LinearLayout.HORIZONTAL);
        top.setGravity(Gravity.CENTER_VERTICAL);
        top.setPadding(dp(12), dp(14), dp(12), dp(12));
        top.setBackgroundColor(getColorCompat(R.color.yh_bg_2));

        TextView back = new TextView(this);
        back.setText("‹");
        back.setTextSize(28);
        back.setTextColor(getColorCompat(R.color.yh_text));
        back.setGravity(Gravity.CENTER);
        back.setPadding(dp(10), 0, dp(10), dp(2));
        back.setOnClickListener(v -> finish());
        top.addView(back, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        TextView title = new TextView(this);
        title.setText("Music library");
        title.setTextSize(19);
        title.setTextColor(getColorCompat(R.color.yh_text));
        title.setTypeface(null, android.graphics.Typeface.BOLD);
        LinearLayout.LayoutParams tp = new LinearLayout.LayoutParams(0,
                ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        tp.leftMargin = dp(6);
        top.addView(title, tp);

        TextView addBtn = new TextView(this);
        addBtn.setText("+ Add");
        addBtn.setTextSize(14);
        addBtn.setTextColor(getColorCompat(R.color.yh_primary));
        addBtn.setPadding(dp(12), dp(8), dp(12), dp(8));
        addBtn.setOnClickListener(v -> showAddMenu());
        top.addView(addBtn);
        root.addView(top, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        // —— 进度条（扫描时显示）——
        progress = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        progress.setIndeterminate(true);
        progress.setVisibility(View.GONE);
        root.addView(progress, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dp(3)));

        // —— 列表 ——
        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        listContainer = new LinearLayout(this);
        listContainer.setOrientation(LinearLayout.VERTICAL);
        listContainer.setPadding(dp(12), dp(10), dp(12), dp(24));
        scroll.addView(listContainer);
        root.addView(scroll, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f));

        emptyHint = new TextView(this);
        emptyHint.setText("No music yet\n\nClick \"+ Add\" in the upper right corner:\n   📁 Add album — select a folder and automatically scan the audio inside\n   🎵 Add singles — pick a few individually\n\nAfter adding it, you can hear it in the concert hall of the 3D exhibition hall ♪");
        emptyHint.setTextSize(13);
        emptyHint.setTextColor(getColorCompat(R.color.yh_text_muted));
        emptyHint.setLineSpacing(dp(3), 1f);
        emptyHint.setGravity(Gravity.CENTER);
        emptyHint.setVisibility(View.GONE);
        listContainer.addView(emptyHint);

        return root;
    }

    /* ==================== 列表渲染 ==================== */

    private void reload() {
        if (listContainer == null) return;
        listContainer.removeAllViews();
        listContainer.addView(emptyHint);

        List<MusicAlbum> albums = repo.getAllAlbums();
        // 把"散装单曲"虚拟专辑放在最后
        MusicAlbum singles = repo.virtualSinglesAlbum();

        List<MusicAlbum> show = new ArrayList<>();
        for (MusicAlbum a : albums) {
            if (a.trackCount > 0) show.add(a);
        }

        boolean any = !show.isEmpty() || singles != null;
        emptyHint.setVisibility(any ? View.GONE : View.VISIBLE);

        for (MusicAlbum a : show) {
            listContainer.addView(buildAlbumCard(a, false));
        }
        if (singles != null) {
            listContainer.addView(buildAlbumCard(singles, true));
        }
    }

    /** 专辑卡片（含可展开的曲目列表） */
    private View buildAlbumCard(MusicAlbum album, boolean isSingles) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setBackgroundResource(R.drawable.bg_music_album);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.bottomMargin = dp(10);
        card.setLayoutParams(lp);

        // —— 头行：封面 + 标题/副标题 + 展开箭头 ——
        LinearLayout head = new LinearLayout(this);
        head.setOrientation(LinearLayout.HORIZONTAL);
        head.setGravity(Gravity.CENTER_VERTICAL);
        head.setPadding(dp(10), dp(10), dp(10), dp(10));

        ImageView cover = new ImageView(this);
        cover.setScaleType(ImageView.ScaleType.CENTER_CROP);
        cover.setBackgroundResource(R.drawable.bg_music_cover_empty);
        LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(dp(56), dp(56));
        head.addView(cover, cp);
        loadCover(cover, album.coverUri);

        LinearLayout texts = new LinearLayout(this);
        texts.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams tp = new LinearLayout.LayoutParams(0,
                ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        tp.leftMargin = dp(12);
        head.addView(texts, tp);

        TextView t1 = new TextView(this);
        t1.setText(isSingles ? "♪ Bulk singles" : nz(album.title));
        t1.setTextSize(15);
        t1.setTextColor(getColorCompat(R.color.yh_text));
        t1.setTypeface(null, android.graphics.Typeface.BOLD);
        t1.setMaxLines(2);
        t1.setEllipsize(TextUtils.TruncateAt.END);
        texts.addView(t1);

        TextView t2 = new TextView(this);
        t2.setText(album.trackCount + "Head");
        t2.setTextSize(12);
        t2.setTextColor(getColorCompat(R.color.yh_text_muted));
        t2.setPadding(0, dp(4), 0, 0);
        texts.addView(t2);

        TextView arrow = new TextView(this);
        boolean isExp = expanded.contains(album.id);
        arrow.setText(isExp ? "▾" : "▸");
        arrow.setTextSize(16);
        arrow.setTextColor(getColorCompat(R.color.yh_text_muted));
        arrow.setPadding(dp(8), dp(4), dp(4), dp(4));
        head.addView(arrow);

        head.setOnClickListener(v -> {
            if (expanded.contains(album.id)) expanded.remove(album.id);
            else expanded.add(album.id);
            reload();
        });
        head.setOnLongClickListener(v -> {
            if (!isSingles) showAlbumMenu(album);
            return true;
        });
        card.addView(head);

        // —— 展开：曲目列表 ——
        if (isExp) {
            List<MusicTrack> tracks = repo.getTracks(album.id);
            LinearLayout box = new LinearLayout(this);
            box.setOrientation(LinearLayout.VERTICAL);
            box.setPadding(dp(10), 0, dp(10), dp(10));
            for (int i = 0; i < tracks.size(); i++) {
                box.addView(buildTrackRow(tracks.get(i), i + 1, album));
            }
            card.addView(box);
        }
        return card;
    }

    /** 单条曲目行 */
    private View buildTrackRow(MusicTrack t, int index, MusicAlbum album) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setBackgroundResource(R.drawable.bg_music_track);
        row.setPadding(dp(10), dp(9), dp(8), dp(9));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.topMargin = dp(6);
        row.setLayoutParams(lp);

        TextView no = new TextView(this);
        no.setText((t.trackNo > 0 ? t.trackNo : index) + "");
        no.setTextSize(11);
        no.setTextColor(getColorCompat(R.color.yh_text_muted));
        no.setGravity(Gravity.CENTER);
        no.setMinWidth(dp(22));
        row.addView(no);

        LinearLayout mid = new LinearLayout(this);
        mid.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams mp = new LinearLayout.LayoutParams(0,
                ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        mp.leftMargin = dp(8);
        row.addView(mid, mp);

        TextView name = new TextView(this);
        name.setText(nz(t.title));
        name.setTextSize(13.5f);
        name.setTextColor(getColorCompat(R.color.yh_text));
        name.setMaxLines(1);
        name.setEllipsize(TextUtils.TruncateAt.END);
        mid.addView(name);

        TextView meta = new TextView(this);
        StringBuilder sb = new StringBuilder();
        sb.append(t.durationText());
        if (!TextUtils.isEmpty(t.artist)) sb.append(" · ").append(t.artist);
        meta.setText(sb.toString());
        meta.setTextSize(11);
        meta.setTextColor(getColorCompat(R.color.yh_text_muted));
        meta.setPadding(0, dp(2), 0, 0);
        mid.addView(meta);

        // PV 标记
        TextView pv = new TextView(this);
        if (t.hasPv()) {
            pv.setText("PV");
            pv.setTextSize(10);
            pv.setTextColor(getColorCompat(R.color.yh_primary));
            pv.setBackgroundResource(R.drawable.bg_chip);
            pv.setPadding(dp(7), dp(2), dp(7), dp(2));
            LinearLayout.LayoutParams pp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            pp.rightMargin = dp(8);
            row.addView(pv, pp);
        }

        TextView more = new TextView(this);
        more.setText("⋮");
        more.setTextSize(18);
        more.setTextColor(getColorCompat(R.color.yh_text_muted));
        more.setPadding(dp(10), dp(2), dp(10), dp(2));
        more.setOnClickListener(v -> showTrackMenu(t, album));
        row.addView(more);

        return row;
    }

    /* ==================== 菜单 ==================== */

    private void showAddMenu() {
        LinearLayout root = dialogRoot();
        final AlertDialog[] holder = new AlertDialog[1];

        root.addView(dialogOption("📁 Add album",
                "Select a folder and automatically scan the audio inside (recommended)",
                () -> {
                    if (holder[0] != null) holder[0].dismiss();
                    startActivityForResult(MusicSafHelper.folderPickerIntent(null), REQ_FOLDER);
                }));
        root.addView(dialogGap());
        root.addView(dialogOption("🎵 Add single",
                "Select a few audio tracks individually and put them into \"Bulk Singles\"",
                () -> {
                    if (holder[0] != null) holder[0].dismiss();
                    startActivityForResult(MusicSafHelper.audioPickerIntent(true), REQ_AUDIO);
                }));

        AlertDialog d = new AlertDialog.Builder(this)
                .setTitle("Add music").setView(root).setNegativeButton("Cancel", null).create();
        holder[0] = d;
        styleDialog(d);
        d.show();
    }

    private void showAlbumMenu(MusicAlbum album) {
        LinearLayout root = dialogRoot();
        final AlertDialog[] holder = new AlertDialog[1];

        root.addView(dialogOption("✏️ Rename",
                "Change album title",
                () -> {
                    if (holder[0] != null) holder[0].dismiss();
                    promptRenameAlbum(album);
                }));
        root.addView(dialogGap());
        root.addView(dialogOption("🖼 Change cover",
                "Choose a picture as cover",
                () -> {
                    if (holder[0] != null) holder[0].dismiss();
                    pendingAlbumId = album.id;
                    startActivityForResult(MusicSafHelper.imagePickerIntent(), REQ_COVER);
                }));
        root.addView(dialogGap());
        root.addView(dialogOption("🗑 Delete album",
                "Only delete the record and leave your original files untouched",
                () -> {
                    if (holder[0] != null) holder[0].dismiss();
                    confirmDeleteAlbum(album);
                }));

        AlertDialog d = new AlertDialog.Builder(this)
                .setTitle(nz(album.title)).setView(root).setNegativeButton("Cancel", null).create();
        holder[0] = d;
        styleDialog(d);
        d.show();
    }

    private void showTrackMenu(MusicTrack t, MusicAlbum album) {
        LinearLayout root = dialogRoot();
        final AlertDialog[] holder = new AlertDialog[1];

        if (t.hasPv()) {
            root.addView(dialogOption("🎬 Unbind PV",
                    "Keep audio, only remove PV association",
                    () -> {
                        if (holder[0] != null) holder[0].dismiss();
                        repo.setTrackPv(t.id, null);
                        toast("Unbound PV");
                        reload();
                    }));
        } else {
            root.addView(dialogOption("🎬 Bind PV",
                    "Choose a video as the PV for this song",
                    () -> {
                        if (holder[0] != null) holder[0].dismiss();
                        pendingTrackId = t.id;
                        startActivityForResult(MusicSafHelper.videoPickerIntent(), REQ_PV);
                    }));
        }
        root.addView(dialogGap());
        root.addView(dialogOption("✏️ Edit title",
                "Change the displayed name of this song",
                () -> {
                    if (holder[0] != null) holder[0].dismiss();
                    promptRenameTrack(t);
                }));
        root.addView(dialogGap());
        root.addView(dialogOption("🗑 Delete this song",
                "Only delete the record and leave your original files untouched",
                () -> {
                    if (holder[0] != null) holder[0].dismiss();
                    repo.deleteTrack(t.id);
                    if (album.id != MusicAlbum.SINGLES_ID) repo.refreshTrackCount(album.id);
                    toast("Removed");
                    reload();
                }));

        AlertDialog d = new AlertDialog.Builder(this)
                .setTitle(nz(t.title)).setView(root).setNegativeButton("Cancel", null).create();
        holder[0] = d;
        styleDialog(d);
        d.show();
    }

    private void promptRenameAlbum(MusicAlbum album) {
        EditText input = new EditText(this);
        input.setText(nz(album.title));
        input.setTextColor(getColorCompat(R.color.yh_text));
        input.setSelection(input.getText().length());
        FrameLayout wrap = new FrameLayout(this);
        wrap.setPadding(dp(18), dp(4), dp(18), dp(4));
        wrap.addView(input);
        AlertDialog d = new AlertDialog.Builder(this)
                .setTitle("Rename album")
                .setView(wrap)
                .setPositiveButton("OK", (dl, w) -> {
                    album.title = input.getText().toString().trim();
                    if (album.title.isEmpty()) album.title = "Untitled album";
                    repo.updateAlbum(album);
                    reload();
                })
                .setNegativeButton("Cancel", null)
                .create();
        styleDialog(d);
        d.show();
    }

    private void promptRenameTrack(MusicTrack t) {
        EditText input = new EditText(this);
        input.setText(nz(t.title));
        input.setTextColor(getColorCompat(R.color.yh_text));
        input.setSelection(input.getText().length());
        FrameLayout wrap = new FrameLayout(this);
        wrap.setPadding(dp(18), dp(4), dp(18), dp(4));
        wrap.addView(input);
        AlertDialog d = new AlertDialog.Builder(this)
                .setTitle("Edit title")
                .setView(wrap)
                .setPositiveButton("OK", (dl, w) -> {
                    String s = input.getText().toString().trim();
                    t.title = s.isEmpty() ? t.title : s;
                    repo.updateTrack(t);
                    reload();
                })
                .setNegativeButton("Cancel", null)
                .create();
        styleDialog(d);
        d.show();
    }

    private void confirmDeleteAlbum(MusicAlbum album) {
        AlertDialog d = new AlertDialog.Builder(this)
                .setTitle("Delete album")
                .setMessage("Will remove \"" + nz(album.title) + "” and its" + album.trackCount + "The record of the song."
                        + "⚠️ The original files on your phone will not be deleted.")
                .setPositiveButton("Delete", (dl, w) -> {
                    repo.deleteAlbum(album.id);
                    toast("Removed");
                    reload();
                })
                .setNegativeButton("Cancel", null)
                .create();
        styleDialog(d);
        d.show();
    }

    /* ==================== 选择结果 ==================== */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null) return;

        switch (requestCode) {
            case REQ_FOLDER:
                handleFolderPicked(data.getData());
                break;
            case REQ_AUDIO:
                handleAudioPicked(data);
                break;
            case REQ_PV:
                handlePvPicked(data.getData());
                break;
            case REQ_COVER:
                handleCoverPicked(data.getData());
                break;
            default:
                break;
        }
    }

    /** 添加专辑：持久化授权 → 后台扫描 → 写库 */
    private void handleFolderPicked(Uri treeUri) {
        if (treeUri == null) return;
        if (!MusicSafHelper.persistRead(this, treeUri)) {
            toast("Unable to save access permissions for this folder, please select again from the system file selector");
            return;
        }
        // 同一个文件夹不重复添加
        MusicAlbum exist = repo.findAlbumByRootUri(treeUri.toString());
        if (exist != null) {
            toast("This folder has already been added");
            return;
        }

        setBusy(true, "Scanning for audio…");
        new Thread(() -> {
            final MusicSafHelper.ScanResult r = MusicSafHelper.scanAudioFolder(this, treeUri);
            main.post(() -> {
                setBusy(false, null);
                if (r.error != null) {
                    toast(r.error);
                    return;
                }
                if (r.tracks.isEmpty()) {
                    toast("No audio files found in this folder");
                    return;
                }
                MusicAlbum a = new MusicAlbum();
                a.title = r.folderName;
                a.rootUri = r.folderUri;
                a.coverUri = r.coverUri;
                a.trackCount = r.tracks.size();
                long albumId = repo.insertAlbum(a);
                if (albumId <= 0) {
                    toast("Write failed (album)");
                    return;
                }
                List<MusicTrack> list = new ArrayList<>();
                for (MusicSafHelper.ScannedTrack st : r.tracks) {
                    MusicTrack t = new MusicTrack();
                    t.albumId = albumId;
                    t.trackNo = st.trackNo;
                    t.title = st.title;
                    t.artist = st.artist;
                    t.audioUri = st.audioUri;
                    t.durationMs = st.durationMs;
                    t.sizeBytes = st.sizeBytes;
                    list.add(t);
                }
                int n = repo.insertTracks(list);
                repo.refreshTrackCount(albumId);
                expanded.add(albumId);
                toast("Added \"" + r.folderName + "\",common" + n + "Head");
                reload();
            });
        }, "music-scan").start();
    }

    /** 添加单曲（支持多选） */
    private void handleAudioPicked(Intent data) {
        final List<Uri> uris = new ArrayList<>();
        if (data.getClipData() != null) {
            int n = data.getClipData().getItemCount();
            for (int i = 0; i < n; i++) uris.add(data.getClipData().getItemAt(i).getUri());
        } else if (data.getData() != null) {
            uris.add(data.getData());
        }
        if (uris.isEmpty()) return;

        setBusy(true, "Reading audio information...");
        new Thread(() -> {
            int ok = 0, denied = 0;
            for (Uri u : uris) {
                if (!MusicSafHelper.persistRead(this, u)) {
                    denied++;
                    continue;
                }
                MusicSafHelper.ScannedTrack st = MusicSafHelper.scanSingleAudio(this, u);
                if (st == null) continue;
                MusicTrack t = new MusicTrack();
                t.albumId = MusicAlbum.SINGLES_ID;   // 散装单曲
                t.trackNo = st.trackNo;
                t.title = st.title;
                t.artist = st.artist;
                t.audioUri = st.audioUri;
                t.durationMs = st.durationMs;
                t.sizeBytes = st.sizeBytes;
                if (repo.insertTrack(t) > 0) ok++;
            }
            final int fok = ok, fdenied = denied;
            main.post(() -> {
                setBusy(false, null);
                if (fok > 0) toast("Added" + fok + "Head");
                if (fdenied > 0) toast(fdenied + "Unable to save access rights, skipped");
                if (fok == 0 && fdenied == 0) toast("No audio to add");
                expanded.add(MusicAlbum.SINGLES_ID);
                reload();
            });
        }, "music-single").start();
    }

    /** 绑定 PV */
    private void handlePvPicked(Uri uri) {
        if (uri == null || pendingTrackId <= 0) return;
        if (!MusicSafHelper.persistRead(this, uri)) {
            toast("Unable to save access to this video, please select again from the system file selector");
            return;
        }
        repo.setTrackPv(pendingTrackId, uri.toString());
        toast("Bound PV");
        reload();
    }

    /** 换封面 */
    private void handleCoverPicked(Uri uri) {
        if (uri == null || pendingAlbumId <= 0) return;
        if (!MusicSafHelper.persistRead(this, uri)) {
            toast("Unable to save access to this image, please select again from the system file selector");
            return;
        }
        repo.updateAlbumCover(pendingAlbumId, uri.toString());
        toast("Cover has been updated");
        reload();
    }

    /* ==================== 封面加载 ==================== */

    /** 异步小图加载（只读缩略，避免大图 OOM）。 */
    private void loadCover(final ImageView iv, final String uriStr) {
        if (iv == null) return;
        if (uriStr == null || uriStr.trim().isEmpty()) {
            iv.setImageDrawable(null);
            return;
        }
        new Thread(() -> {
            Bitmap bmp = null;
            InputStream in = null;
            try {
                in = getContentResolver().openInputStream(Uri.parse(uriStr));
                if (in != null) {
                    BitmapFactory.Options o = new BitmapFactory.Options();
                    o.inJustDecodeBounds = true;
                    BitmapFactory.decodeStream(in, null, o);
                    try { in.close(); } catch (Throwable ignored) { }
                    in = null;

                    int scale = 1;
                    int target = dp(112);
                    while (o.outWidth / scale > target * 2 || o.outHeight / scale > target * 2) scale *= 2;

                    BitmapFactory.Options o2 = new BitmapFactory.Options();
                    o2.inSampleSize = Math.max(1, scale);
                    in = getContentResolver().openInputStream(Uri.parse(uriStr));
                    if (in != null) bmp = BitmapFactory.decodeStream(in, null, o2);
                }
            } catch (Throwable ignored) {
            } finally {
                if (in != null) { try { in.close(); } catch (Throwable ignored) { } }
            }
            final Bitmap fb = bmp;
            main.post(() -> { if (fb != null && !fb.isRecycled()) iv.setImageBitmap(fb); });
        }, "music-cover").start();
    }

    /* ==================== 小工具 ==================== */

    private void setBusy(boolean b, String msg) {
        busy = b;
        if (progress != null) progress.setVisibility(b ? View.VISIBLE : View.GONE);
        if (b && msg != null) toast(msg);
    }

    private void toast(String s) {
        if (!TextUtils.isEmpty(s)) Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    private int dp(float v) {
        return Math.round(v * getResources().getDisplayMetrics().density);
    }

    private int getColorCompat(int resId) {
        return getResources().getColor(resId);
    }

    private static String nz(String s) {
        return s == null ? "" : s;
    }

    /* ==================== 对话框样式（对齐 HomeActivity） ==================== */

    private LinearLayout dialogRoot() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        int pad = dp(16);
        root.setPadding(pad, dp(4), pad, dp(4));
        return root;
    }

    private View dialogGap() {
        View v = new View(this);
        v.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dp(8)));
        return v;
    }

    private View dialogOption(String title, String desc, Runnable onClick) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setBackgroundResource(R.drawable.bg_home_glass);
        card.setPadding(dp(14), dp(12), dp(14), dp(12));
        card.setClickable(true);
        card.setFocusable(true);

        TextView t = new TextView(this);
        t.setText(title);
        t.setTextColor(getColorCompat(R.color.yh_text));
        t.setTextSize(15);
        t.setTypeface(null, android.graphics.Typeface.BOLD);
        card.addView(t);

        TextView d = new TextView(this);
        d.setText(desc);
        d.setTextColor(getColorCompat(R.color.yh_text_muted));
        d.setTextSize(11);
        d.setPadding(0, dp(3), 0, 0);
        card.addView(d);

        card.setOnClickListener(v -> onClick.run());
        return card;
    }

    private void styleDialog(AlertDialog dialog) {
        if (dialog == null) return;
        try {
            Window w = dialog.getWindow();
            if (w != null) {
                w.setBackgroundDrawableResource(R.drawable.bg_dialog);
                w.setDimAmount(0.5f);
                w.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }
            int titleId = getResources().getIdentifier("alertTitle", "id", "android");
            TextView title = titleId != 0 ? dialog.findViewById(titleId) : null;
            if (title != null) title.setTextColor(getColorCompat(R.color.yh_text));
            TextView msg = dialog.findViewById(android.R.id.message);
            if (msg != null) msg.setTextColor(getColorCompat(R.color.yh_text_muted));
            android.widget.Button p = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            android.widget.Button n = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            if (p != null) p.setTextColor(getColorCompat(R.color.yh_primary));
            if (n != null) n.setTextColor(getColorCompat(R.color.yh_secondary));
        } catch (Throwable ignored) {
        }
    }
}