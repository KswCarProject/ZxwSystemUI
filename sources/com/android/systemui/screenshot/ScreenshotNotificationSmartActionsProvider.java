package com.android.systemui.screenshot;

import android.app.Notification;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.UserHandle;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ScreenshotNotificationSmartActionsProvider {
    public static final String TAG = LogConfig.logTag(ScreenshotNotificationSmartActionsProvider.class);

    public enum ScreenshotOp {
        OP_UNKNOWN,
        RETRIEVE_SMART_ACTIONS,
        REQUEST_SMART_ACTIONS,
        WAIT_FOR_SMART_ACTIONS
    }

    public enum ScreenshotOpStatus {
        OP_STATUS_UNKNOWN,
        SUCCESS,
        ERROR,
        TIMEOUT
    }

    public enum ScreenshotSmartActionType {
        REGULAR_SMART_ACTIONS,
        QUICK_SHARE_ACTION
    }

    public void notifyAction(String str, String str2, boolean z, Intent intent) {
    }

    public void notifyOp(String str, ScreenshotOp screenshotOp, ScreenshotOpStatus screenshotOpStatus, long j) {
    }

    public CompletableFuture<List<Notification.Action>> getActions(String str, Uri uri, Bitmap bitmap, ComponentName componentName, ScreenshotSmartActionType screenshotSmartActionType, UserHandle userHandle) {
        return CompletableFuture.completedFuture(Collections.emptyList());
    }
}
