package com.yuki.yukihub.translate;

import android.accessibilityservice.AccessibilityService;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.HardwareBuffer;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

/**
 * Android 11+ 无障碍截图服务。
 *
 * 仅负责将系统截图转换为可变 ARGB_8888 Bitmap，并按可选区域裁剪；
 * OCR、图片缓存和翻译由后续模块负责。
 *
 * 适配说明：参考 MoeTranslate 的 ScreenShotAccessibilityService，
 * 已将协程/SharedFlow 改为 Java 回调，并保留原 LGPL 适配要求。
 */
public class YukiScreenshotAccessibilityService extends AccessibilityService {
    private static final String TAG = "YukiScreenshotService";

    public interface Callback {
        void onSuccess(Bitmap bitmap);
        void onFailure(String message);
    }

    public void takeScreenshot(RectF cropRect, Point offset, Callback callback) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            fail(callback, "Screen translation requires Android 11 or higher");
            return;
        }
        try {
            takeScreenshot(
                    Display.DEFAULT_DISPLAY,
                    getMainExecutor(),
                    new TakeScreenshotCallback() {
                        @Override
                        public void onSuccess(ScreenshotResult screenshot) {
                            processScreenshot(screenshot, cropRect, offset, callback);
                        }

                        @Override
                        public void onFailure(int errorCode) {
                            fail(callback, screenshotError(errorCode));
                        }
                    });
        } catch (Throwable t) {
            Log.e(TAG, "takeScreenshot failed", t);
            fail(callback, "System screenshot failed:" + safeMessage(t));
        }
    }

    private void processScreenshot(ScreenshotResult result, RectF cropRect,
                                   Point offset, Callback callback) {
        HardwareBuffer hardwareBuffer = result == null ? null : result.getHardwareBuffer();
        Bitmap bitmap = null;
        try {
            if (hardwareBuffer == null) {
                fail(callback, "The system did not return the screenshot buffer");
                return;
            }
            Bitmap hardwareBitmap = Bitmap.wrapHardwareBuffer(
                    hardwareBuffer, result.getColorSpace());
            if (hardwareBitmap == null) {
                fail(callback, "Unable to read system screenshot");
                return;
            }
            bitmap = hardwareBitmap.copy(Bitmap.Config.ARGB_8888, true);
            if (bitmap == null) {
                fail(callback, "Unable to convert screenshot format");
                return;
            }
            if (cropRect != null) {
                Point safeOffset = offset == null ? new Point(0, 0) : offset;
                Bitmap cropped = cropBitmap(bitmap, cropRect, safeOffset);
                if (cropped == null) {
                    fail(callback, "The screenshot area is invalid or exceeds the screen range");
                    return;
                }
                if (cropped != bitmap) {
                    bitmap.recycle();
                    bitmap = cropped;
                }
            }
            Bitmap deliver = bitmap;
            bitmap = null;
            if (callback != null) callback.onSuccess(deliver);
        } catch (Throwable t) {
            Log.e(TAG, "process screenshot failed", t);
            fail(callback, "Screenshot processing failed:" + safeMessage(t));
        } finally {
            if (bitmap != null && !bitmap.isRecycled()) bitmap.recycle();
            if (hardwareBuffer != null) hardwareBuffer.close();
        }
    }

    private Bitmap cropBitmap(Bitmap source, RectF cropRect, Point offset) {
        int left = Math.round(cropRect.left) + offset.x;
        int top = Math.round(cropRect.top) + offset.y;
        int right = Math.round(cropRect.right) + offset.x;
        int bottom = Math.round(cropRect.bottom) + offset.y;
        Rect bounds = new Rect(0, 0, source.getWidth(), source.getHeight());
        Rect requested = new Rect(left, top, right, bottom);
        if (!requested.intersect(bounds) || requested.width() <= 0 || requested.height() <= 0) {
            return null;
        }
        return Bitmap.createBitmap(source, requested.left, requested.top,
                requested.width(), requested.height());
    }

    private void fail(Callback callback, String message) {
        String text = message == null || message.trim().isEmpty() ? "Screenshot failed" : message;
        if (callback != null) callback.onFailure(text);
        try {
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
        } catch (Throwable ignored) {
        }
    }

    private String screenshotError(int errorCode) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) return "Screen translation requires Android 11 or higher";
        switch (errorCode) {
            case AccessibilityService.ERROR_TAKE_SCREENSHOT_INTERNAL_ERROR:
                return "Screenshot failed: internal system error";
            case AccessibilityService.ERROR_TAKE_SCREENSHOT_NO_ACCESSIBILITY_ACCESS:
                return "Screenshot failed: Accessibility permission not available";
            case AccessibilityService.ERROR_TAKE_SCREENSHOT_INTERVAL_TIME_SHORT:
                return "Screenshots taken too frequently, please try again later";
            case AccessibilityService.ERROR_TAKE_SCREENSHOT_INVALID_DISPLAY:
                return "Screenshot failed: Invalid screen";
            default:
                return "Screenshot failed, error code:" + errorCode;
        }
    }

    private String safeMessage(Throwable throwable) {
        String message = throwable.getMessage();
        return message == null || message.isEmpty() ? throwable.getClass().getSimpleName() : message;
    }

    @Override
    public void onServiceConnected() {
        super.onServiceConnected();
        ScreenshotServiceManager.setService(this);
        Log.d(TAG, "Accessibility screenshot service connected");
        // 自检手势能力：canPerformGestures 没配置的话 dispatchGesture 会被系统
        // 直接拒绝（返回 false），且不留任何系统日志，极难排查。这里显式报告。
        try {
            android.accessibilityservice.AccessibilityServiceInfo info = getServiceInfo();
            boolean canGesture = info != null
                    && (info.getCapabilities()
                    & android.accessibilityservice.AccessibilityServiceInfo
                    .CAPABILITY_CAN_PERFORM_GESTURES) != 0;
            Log.i(TAG, "gesture capability=" + canGesture);
        } catch (Throwable t) {
            Log.w(TAG, "capability check failed", t);
        }
    }

    /**
     * 无障碍手势点击（游戏内虚拟鼠标的 Artemis 注入通道）。
     *
     * 手势构造有两个坑，都踩过：
     * 1. 零位移路径（moveTo(x,y) 后 lineTo(x,y)）会被部分引擎的命中判定忽略，
     *    NativeActivity 的 native 输入队列尤其挑。这里改成 1px 位移，
     *    既产生真实的 MOVE 事件，视觉与逻辑上仍是原地点击。
     * 2. dispatchGesture 的返回值必须检查：false = 手势被系统直接拒绝
     *    （常见原因是上一个手势还没结束、或服务缺 CAPABILITY_CAN_PERFORM_GESTURES）。
     *    之前传 null 回调又忽略返回值，等于把失败原因全丢了。
     */
    public static void tap(int x, int y, long durationMs) {
        YukiScreenshotAccessibilityService service = ScreenshotServiceManager.getService();
        if (service == null) {
            Log.w(TAG, "tap ignored: accessibility service not connected");
            return;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) return;
        try {
            android.graphics.Path path = new android.graphics.Path();
            path.moveTo(x, y);
            // 1px 位移：零位移手势部分引擎不认（收不到 MOVE 就不判命中）
            path.lineTo(x + 1, y + 1);
            long dur = Math.max(50, durationMs);
            android.accessibilityservice.GestureDescription.StrokeDescription stroke =
                    new android.accessibilityservice.GestureDescription.StrokeDescription(path, 0, dur);
            boolean ok = service.dispatchGesture(
                    new android.accessibilityservice.GestureDescription.Builder()
                            .addStroke(stroke)
                            .build(),
                    new android.accessibilityservice.AccessibilityService.GestureResultCallback() {
                        @Override
                        public void onCompleted(android.accessibilityservice.GestureDescription d) {
                            Log.d(TAG, "gesture completed (" + x + "," + y + ")");
                        }

                        @Override
                        public void onCancelled(android.accessibilityservice.GestureDescription d) {
                            Log.w(TAG, "gesture CANCELLED (" + x + "," + y + ")");
                        }
                    }, null);
            if (!ok) {
                Log.w(TAG, "dispatchGesture REJECTED (" + x + "," + y + ")");
            }
        } catch (Throwable t) {
            Log.w(TAG, "dispatchGesture failed", t);
        }
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // 当前阶段不处理无障碍事件，仅使用截图 API。
    }

    @Override
    public void onInterrupt() {
        // 系统中断时由 onDestroy 清除服务引用。
    }

    @Override
    public void onDestroy() {
        ScreenshotServiceManager.setService(null);
        super.onDestroy();
    }
}