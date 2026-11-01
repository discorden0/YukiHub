package com.yuki.yukihub.sync;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.yuki.yukihub.R;
import com.yuki.yukihub.MainActivity;
import com.yuki.yukihub.util.AppExecutors;

/**
 * WebDAV 同步设置对话框
 */
public class WebDavSettingsDialog extends DialogFragment {
    
    private EditText etServer, etUsername, etPassword;
    private Switch swAutoSync;
    private Button btnTest, btnSave, btnSyncNow, btnLocalBackup, btnLocalImport;
    private TextView tvStatus, tvLastSync;
    private ProgressBar progressBar;
    private LinearLayout syncOptions;
    
    private SyncManager syncManager;
    
    public static WebDavSettingsDialog newInstance() {
        return new WebDavSettingsDialog();
    }
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Theme_YukiHub_WebDavDialog);
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_webdav_settings, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Apply dynamic theme tinting
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).tintDialogRoot(view);
        }
        
        syncManager = new SyncManager(requireContext());
        
        initViews(view);
        loadConfig();
        setupListeners();
    }
    
    private void initViews(View view) {
        etServer = view.findViewById(R.id.etWebDavServer);
        etUsername = view.findViewById(R.id.etWebDavUsername);
        etPassword = view.findViewById(R.id.etWebDavPassword);
        
        swAutoSync = view.findViewById(R.id.swAutoSync);
        
        btnTest = view.findViewById(R.id.btnTestConnection);
        btnSave = view.findViewById(R.id.btnSaveWebDav);
        btnSyncNow = view.findViewById(R.id.btnSyncNow);
        btnLocalBackup = view.findViewById(R.id.btnLocalBackup);
        btnLocalImport = view.findViewById(R.id.btnLocalImport);
        
        tvStatus = view.findViewById(R.id.tvWebDavStatus);
        tvLastSync = view.findViewById(R.id.tvLastSync);
        
        progressBar = view.findViewById(R.id.progressBar);
        syncOptions = view.findViewById(R.id.syncOptions);
    }
    
    private void loadConfig() {
        SyncManager.SyncConfig config = syncManager.getConfig();
        
        etServer.setText(config.serverUrl);
        etUsername.setText(config.username);
        etPassword.setText(config.password);
        swAutoSync.setChecked(config.autoSync);
        
        // 显示最后同步时间
        long lastSync = syncManager.getLastSyncTime();
        if (lastSync > 0) {
            tvLastSync.setText("Last sync:" + android.text.format.DateFormat.format("yyyy-MM-dd HH:mm", lastSync));
            tvLastSync.setVisibility(View.VISIBLE);
        }
        
        // 如果已配置，显示同步选项
        if (syncManager.isConfigured()) {
            syncOptions.setVisibility(View.VISIBLE);
            btnSyncNow.setEnabled(true);
        }
    }
    
    private void setupListeners() {
        // 测试连接
        btnTest.setOnClickListener(v -> testConnection());
        
        // 保存配置
        btnSave.setOnClickListener(v -> saveConfig());
        
        // 本地备份/导入
        if (btnLocalBackup != null) {
            btnLocalBackup.setOnClickListener(v -> {
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).openLocalBackupExportFromSyncCenter();
                }
            });
        }
        if (btnLocalImport != null) {
            btnLocalImport.setOnClickListener(v -> {
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).openLocalBackupImportFromSyncCenter();
                }
            });
        }
        
        // 立即同步
        btnSyncNow.setOnClickListener(v -> startSync());
        
        // 服务器地址变化时更新 UI
        etServer.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                validateServerUrl();
            }
        });
    }
    
    private void testConnection() {
        String server = etServer.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        
        if (server.isEmpty() || username.isEmpty() || password.isEmpty()) {
            showStatus("Please complete your WebDAV configuration", 0xFFFF9500);
            return;
        }
        
        btnTest.setEnabled(false);
        btnTest.setText("Testing...");
        showStatus("Testing connection...", 0xFF8E9AB5);
        
        AppExecutors.runOnIo(() -> {
            // 创建临时客户端测试
            WebDavClient client = new WebDavClient(server, username, password);
            final String[] error = new String[]{null};
            boolean success;
            try {
                client.testConnectionOrThrow();
                success = true;
            } catch (Throwable t) {
                success = false;
                error[0] = t.getMessage();
            }
            final boolean finalSuccess = success;
            
            requireActivity().runOnUiThread(() -> {
                btnTest.setEnabled(true);
                btnTest.setText("test connection");
                
                if (finalSuccess) {
                    showStatus("✓ Connection successful!", 0xFF34D158);
                    syncOptions.setVisibility(View.VISIBLE);
                } else {
                    showStatus("✗ Connection failed:" + (error[0] == null ? "Please check configuration" : error[0]), 0xFFFF3B30);
                }
            });
        });
    }
    
    private void saveConfig() {
        String server = etServer.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        boolean autoSync = swAutoSync.isChecked();
        
        if (server.isEmpty()) {
            showStatus("Please enter the WebDAV server address", 0xFFFF9500);
            return;
        }
        
        if (username.isEmpty()) {
            showStatus("Please enter username", 0xFFFF9500);
            return;
        }
        
        if (password.isEmpty()) {
            showStatus("Please enter password/application password", 0xFFFF9500);
            return;
        }
        
        // 确保 URL 格式正确
        if (!server.startsWith("http://") && !server.startsWith("https://")) {
            server = "https://" + server;
            etServer.setText(server);
        }
        
        // 保存配置
        syncManager.saveConfig(server, username, password, autoSync);
        
        showStatus("✓ Configuration saved", 0xFF34D158);
        btnSyncNow.setEnabled(true);
        
        Toast.makeText(requireContext(), "WebDAV configuration saved", Toast.LENGTH_SHORT).show();
    }
    
    private void startSync() {
        if (!syncManager.isConfigured()) {
            showStatus("Please save the WebDAV configuration first", 0xFFFF9500);
            return;
        }
        
        btnSyncNow.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
        showStatus("Syncing...", 0xFF8E9AB5);
        
        syncManager.sync(new SyncManager.SyncListener() {
            @Override
            public void onSyncStart() {
                requireActivity().runOnUiThread(() -> {
                    showStatus("Synchronization starts...", 0xFF8E9AB5);
                });
            }
            
            @Override
            public void onProgress(String item, boolean changed) {
                requireActivity().runOnUiThread(() -> {
                    showStatus("同步 " + item + (changed ? " ✓" : "(no change)"), 0xFF8E9AB5);
                });
            }
            
            @Override
            public int onConflict(SyncManager.Conflict conflict) {
                final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
                final int[] decision = new int[]{SyncManager.RESOLVE_CANCEL};
                requireActivity().runOnUiThread(() -> {
                    String msg = "There are modifications both locally and in the cloud, please choose a processing method."
                            + "Local size:" + (conflict.localBytes / 1024) + "KB\n"
                            + "Cloud size:" + (conflict.remoteBytes / 1024) + "KB\n\n"
                            + "It is recommended to select \"Smart Merge\": the game path and play record UUID will be deduplicated and merged, and the cumulative play time will be the larger of both sides; if a device has been cleared/reset, the updated reset time will prevail.";
                    new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                            .setTitle("Synchronization conflict")
                            .setMessage(msg)
                            .setPositiveButton("Smart merge", (d, w) -> { decision[0] = SyncManager.RESOLVE_MERGE; latch.countDown(); })
                            .setNegativeButton("Use the cloud", (d, w) -> { decision[0] = SyncManager.RESOLVE_USE_REMOTE; latch.countDown(); })
                            .setNeutralButton("Use local", (d, w) -> { decision[0] = SyncManager.RESOLVE_USE_LOCAL; latch.countDown(); })
                            .setOnCancelListener(d -> { decision[0] = SyncManager.RESOLVE_CANCEL; latch.countDown(); })
                            .show();
                });
                try { latch.await(); } catch (InterruptedException ignored) { }
                return decision[0];
            }
            
            @Override
            public void onSyncComplete(SyncManager.SyncResult result) {
                requireActivity().runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    btnSyncNow.setEnabled(true);
                    
                    if (result.hasChanges()) {
                        StringBuilder msg = new StringBuilder("Synchronization completed:");
                        if (result.uploaded) msg.append("Uploaded");
                        if (result.downloaded) msg.append("Downloaded");
                        if (result.merged) msg.append("Merged");
                        int showBytes = result.compressedBytes > 0 ? result.compressedBytes : result.localBytes;
                        msg.append(" · ").append(showBytes / 1024).append("KB");
                        showStatus(msg.toString(), 0xFF34D158);
                    } else if (result.cancelled) {
                        showStatus("Sync canceled", 0xFFFF9500);
                    } else {
                        int showBytes = result.compressedBytes > 0 ? result.compressedBytes : result.localBytes;
                        showStatus("✓ Already the latest ·" + (showBytes / 1024) + "KB", 0xFF34D158);
                    }
                    
                    // 更新最后同步时间
                    tvLastSync.setText("Last sync:" + android.text.format.DateFormat.format("yyyy-MM-dd HH:mm", System.currentTimeMillis()));
                    tvLastSync.setVisibility(View.VISIBLE);

                    // 如果本地数据被云端覆盖或合并，刷新主界面让游戏库/资料立即生效。
                    if (result.downloaded || result.merged) {
                        Toast.makeText(requireContext(), "The synchronized data has been applied and the interface is being refreshed.", Toast.LENGTH_SHORT).show();
                        requireActivity().recreate();
                    }
                });
            }
            
            @Override
            public void onError(String error) {
                requireActivity().runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    btnSyncNow.setEnabled(true);
                    showStatus("✗ Sync failed:" + error, 0xFFFF3B30);
                });
            }
        });
    }
    
    private void validateServerUrl() {
        String server = etServer.getText().toString().trim();
        if (!server.isEmpty() && !server.startsWith("http://") && !server.startsWith("https://")) {
            etServer.setText("https://" + server);
        }
    }
    
    private void showStatus(String msg, int color) {
        tvStatus.setVisibility(View.VISIBLE);
        tvStatus.setText(msg);
        tvStatus.setTextColor(color);
    }
    
    @Override
    public void onStart() {
        super.onStart();
        // 设置对话框大小
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(
                (int) (getResources().getDisplayMetrics().widthPixels * 0.85f),
                ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }
    }
}