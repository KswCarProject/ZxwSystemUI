package com.android.systemui.screenshot;

import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Insets;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.UserManager;
import android.util.Log;
import android.widget.Toast;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.util.ScreenshotHelper;
import com.android.systemui.R$string;
import java.util.concurrent.Executor;

public class TakeScreenshotService extends Service {
    public static final String TAG = LogConfig.logTag(TakeScreenshotService.class);
    public final Executor mBgExecutor;
    public final BroadcastReceiver mCloseSystemDialogs = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.CLOSE_SYSTEM_DIALOGS".equals(intent.getAction()) && TakeScreenshotService.this.mScreenshot != null && !TakeScreenshotService.this.mScreenshot.isPendingSharedTransition()) {
                TakeScreenshotService.this.mUiEventLogger.log(ScreenshotEvent.SCREENSHOT_DISMISSED_OTHER);
                TakeScreenshotService.this.mScreenshot.dismissScreenshot(false);
            }
        }
    };
    public final Context mContext;
    public final DevicePolicyManager mDevicePolicyManager;
    public final Handler mHandler = new Handler(Looper.getMainLooper(), new TakeScreenshotService$$ExternalSyntheticLambda0(this));
    public final ScreenshotNotificationsController mNotificationsController;
    public ScreenshotController mScreenshot;
    public final UiEventLogger mUiEventLogger;
    public final UserManager mUserManager;

    public interface RequestCallback {
        void onFinish();

        void reportError();
    }

    public void onCreate() {
    }

    public TakeScreenshotService(ScreenshotController screenshotController, UserManager userManager, DevicePolicyManager devicePolicyManager, UiEventLogger uiEventLogger, ScreenshotNotificationsController screenshotNotificationsController, Context context, Executor executor) {
        this.mScreenshot = screenshotController;
        this.mUserManager = userManager;
        this.mDevicePolicyManager = devicePolicyManager;
        this.mUiEventLogger = uiEventLogger;
        this.mNotificationsController = screenshotNotificationsController;
        this.mContext = context;
        this.mBgExecutor = executor;
    }

    public IBinder onBind(Intent intent) {
        registerReceiver(this.mCloseSystemDialogs, new IntentFilter("android.intent.action.CLOSE_SYSTEM_DIALOGS"), 2);
        return new Messenger(this.mHandler).getBinder();
    }

    public boolean onUnbind(Intent intent) {
        ScreenshotController screenshotController = this.mScreenshot;
        if (screenshotController != null) {
            screenshotController.removeWindow();
            this.mScreenshot = null;
        }
        unregisterReceiver(this.mCloseSystemDialogs);
        return false;
    }

    public void onDestroy() {
        super.onDestroy();
        ScreenshotController screenshotController = this.mScreenshot;
        if (screenshotController != null) {
            screenshotController.onDestroy();
            this.mScreenshot = null;
        }
    }

    public static class RequestCallbackImpl implements RequestCallback {
        public final Messenger mReplyTo;

        public RequestCallbackImpl(Messenger messenger) {
            this.mReplyTo = messenger;
        }

        public void reportError() {
            TakeScreenshotService.reportUri(this.mReplyTo, (Uri) null);
            TakeScreenshotService.sendComplete(this.mReplyTo);
        }

        public void onFinish() {
            TakeScreenshotService.sendComplete(this.mReplyTo);
        }
    }

    public final boolean handleMessage(Message message) {
        String str;
        Messenger messenger = message.replyTo;
        TakeScreenshotService$$ExternalSyntheticLambda1 takeScreenshotService$$ExternalSyntheticLambda1 = new TakeScreenshotService$$ExternalSyntheticLambda1(messenger);
        RequestCallbackImpl requestCallbackImpl = new RequestCallbackImpl(messenger);
        if (!this.mUserManager.isUserUnlocked()) {
            Log.w(TAG, "Skipping screenshot because storage is locked!");
            this.mNotificationsController.notifyScreenshotError(R$string.screenshot_failed_to_save_user_locked_text);
            requestCallbackImpl.reportError();
            return true;
        } else if (this.mDevicePolicyManager.getScreenCaptureDisabled((ComponentName) null, -1)) {
            this.mBgExecutor.execute(new TakeScreenshotService$$ExternalSyntheticLambda2(this, requestCallbackImpl));
            return true;
        } else {
            ScreenshotHelper.ScreenshotRequest screenshotRequest = (ScreenshotHelper.ScreenshotRequest) message.obj;
            ComponentName topComponent = screenshotRequest.getTopComponent();
            UiEventLogger uiEventLogger = this.mUiEventLogger;
            ScreenshotEvent screenshotSource = ScreenshotEvent.getScreenshotSource(screenshotRequest.getSource());
            if (topComponent == null) {
                str = "";
            } else {
                str = topComponent.getPackageName();
            }
            uiEventLogger.log(screenshotSource, 0, str);
            int i = message.what;
            if (i == 1) {
                this.mScreenshot.takeScreenshotFullscreen(topComponent, takeScreenshotService$$ExternalSyntheticLambda1, requestCallbackImpl);
            } else if (i == 2) {
                this.mScreenshot.takeScreenshotPartial(topComponent, takeScreenshotService$$ExternalSyntheticLambda1, requestCallbackImpl);
            } else if (i != 3) {
                String str2 = TAG;
                Log.w(str2, "Invalid screenshot option: " + message.what);
                return false;
            } else {
                Bitmap bundleToHardwareBitmap = ScreenshotHelper.HardwareBitmapBundler.bundleToHardwareBitmap(screenshotRequest.getBitmapBundle());
                Rect boundsInScreen = screenshotRequest.getBoundsInScreen();
                Insets insets = screenshotRequest.getInsets();
                int taskId = screenshotRequest.getTaskId();
                int userId = screenshotRequest.getUserId();
                if (bundleToHardwareBitmap == null) {
                    Log.e(TAG, "Got null bitmap from screenshot message");
                    this.mNotificationsController.notifyScreenshotError(R$string.screenshot_failed_to_capture_text);
                    requestCallbackImpl.reportError();
                } else {
                    this.mScreenshot.handleImageAsScreenshot(bundleToHardwareBitmap, boundsInScreen, insets, taskId, userId, topComponent, takeScreenshotService$$ExternalSyntheticLambda1, requestCallbackImpl);
                }
            }
            return true;
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$handleMessage$3(RequestCallback requestCallback) {
        Log.w(TAG, "Skipping screenshot because an IT admin has disabled screenshots on the device");
        this.mHandler.post(new TakeScreenshotService$$ExternalSyntheticLambda4(this, this.mDevicePolicyManager.getResources().getString("SystemUi.SCREENSHOT_BLOCKED_BY_ADMIN", new TakeScreenshotService$$ExternalSyntheticLambda3(this))));
        requestCallback.reportError();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$handleMessage$1() {
        return this.mContext.getString(R$string.screenshot_blocked_by_admin);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$handleMessage$2(String str) {
        Toast.makeText(this.mContext, str, 0).show();
    }

    public static void sendComplete(Messenger messenger) {
        try {
            messenger.send(Message.obtain((Handler) null, 2));
        } catch (RemoteException e) {
            Log.d(TAG, "ignored remote exception", e);
        }
    }

    public static void reportUri(Messenger messenger, Uri uri) {
        try {
            messenger.send(Message.obtain((Handler) null, 1, uri));
        } catch (RemoteException e) {
            Log.d(TAG, "ignored remote exception", e);
        }
    }
}
