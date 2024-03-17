package com.android.wm.shell.back;

import android.app.ActivityTaskManager;
import android.app.IActivityTaskManager;
import android.app.WindowConfiguration;
import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.graphics.Point;
import android.graphics.PointF;
import android.hardware.HardwareBuffer;
import android.net.Uri;
import android.os.Handler;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.provider.Settings;
import android.util.Log;
import android.view.RemoteAnimationTarget;
import android.view.SurfaceControl;
import android.window.BackEvent;
import android.window.BackNavigationInfo;
import android.window.IOnBackInvokedCallback;
import com.android.internal.annotations.VisibleForTesting;
import com.android.wm.shell.back.IBackAnimation;
import com.android.wm.shell.common.ExecutorUtils;
import com.android.wm.shell.common.RemoteCallable;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.protolog.ShellProtoLogCache;
import com.android.wm.shell.protolog.ShellProtoLogGroup;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import java.util.concurrent.atomic.AtomicBoolean;

public class BackAnimationController implements RemoteCallable<BackAnimationController> {
    public static final boolean IS_ENABLED;
    public static final int PROGRESS_THRESHOLD = SystemProperties.getInt("persist.wm.debug.predictive_back_progress_threshold", -1);
    public final IActivityTaskManager mActivityTaskManager;
    public final BackAnimation mBackAnimation;
    public boolean mBackGestureStarted;
    public BackNavigationInfo mBackNavigationInfo;
    public IOnBackInvokedCallback mBackToLauncherCallback;
    public final Context mContext;
    public final AtomicBoolean mEnableAnimations;
    public final PointF mInitTouchLocation;
    public float mProgressThreshold;
    public final Runnable mResetTransitionRunnable;
    public final ShellExecutor mShellExecutor;
    public final Point mTouchEventDelta;
    public final SurfaceControl.Transaction mTransaction;
    public boolean mTransitionInProgress;
    public boolean mTriggerBack;
    public float mTriggerThreshold;

    static {
        boolean z = true;
        if (SystemProperties.getInt("persist.wm.debug.predictive_back", 1) == 0) {
            z = false;
        }
        IS_ENABLED = z;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        finishAnimation();
        this.mTransitionInProgress = false;
    }

    public BackAnimationController(ShellExecutor shellExecutor, Handler handler, Context context) {
        this(shellExecutor, handler, new SurfaceControl.Transaction(), ActivityTaskManager.getService(), context, context.getContentResolver());
    }

    @VisibleForTesting
    public BackAnimationController(ShellExecutor shellExecutor, Handler handler, SurfaceControl.Transaction transaction, IActivityTaskManager iActivityTaskManager, Context context, ContentResolver contentResolver) {
        this.mEnableAnimations = new AtomicBoolean(false);
        this.mInitTouchLocation = new PointF();
        this.mTouchEventDelta = new Point();
        this.mBackGestureStarted = false;
        this.mTransitionInProgress = false;
        this.mResetTransitionRunnable = new BackAnimationController$$ExternalSyntheticLambda0(this);
        this.mBackAnimation = new BackAnimationImpl();
        this.mShellExecutor = shellExecutor;
        this.mTransaction = transaction;
        this.mActivityTaskManager = iActivityTaskManager;
        this.mContext = context;
        setupAnimationDeveloperSettingsObserver(contentResolver, handler);
    }

    public final void setupAnimationDeveloperSettingsObserver(ContentResolver contentResolver, Handler handler) {
        contentResolver.registerContentObserver(Settings.Global.getUriFor("enable_back_animation"), false, new ContentObserver(handler) {
            public void onChange(boolean z, Uri uri) {
                BackAnimationController.this.updateEnableAnimationFromSetting();
            }
        }, 0);
        updateEnableAnimationFromSetting();
    }

    public final void updateEnableAnimationFromSetting() {
        boolean z = Settings.Global.getInt(this.mContext.getContentResolver(), "enable_back_animation", 0) == 1;
        this.mEnableAnimations.set(z);
        if (ShellProtoLogCache.WM_SHELL_BACK_PREVIEW_enabled) {
            ShellProtoLogImpl.d(ShellProtoLogGroup.WM_SHELL_BACK_PREVIEW, 2142828447, 0, "Back animation enabled=%s", String.valueOf(z));
        }
    }

    public BackAnimation getBackAnimationImpl() {
        return this.mBackAnimation;
    }

    public Context getContext() {
        return this.mContext;
    }

    public ShellExecutor getRemoteCallExecutor() {
        return this.mShellExecutor;
    }

    public class BackAnimationImpl implements BackAnimation {
        public IBackAnimationImpl mBackAnimation;

        public BackAnimationImpl() {
        }

        public IBackAnimation createExternalInterface() {
            IBackAnimationImpl iBackAnimationImpl = this.mBackAnimation;
            if (iBackAnimationImpl != null) {
                iBackAnimationImpl.invalidate();
            }
            IBackAnimationImpl iBackAnimationImpl2 = new IBackAnimationImpl(BackAnimationController.this);
            this.mBackAnimation = iBackAnimationImpl2;
            return iBackAnimationImpl2;
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onBackMotion$0(float f, float f2, int i, int i2) {
            BackAnimationController.this.onMotionEvent(f, f2, i, i2);
        }

        public void onBackMotion(float f, float f2, int i, int i2) {
            BackAnimationController.this.mShellExecutor.execute(new BackAnimationController$BackAnimationImpl$$ExternalSyntheticLambda1(this, f, f2, i, i2));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$setTriggerBack$1(boolean z) {
            BackAnimationController.this.setTriggerBack(z);
        }

        public void setTriggerBack(boolean z) {
            BackAnimationController.this.mShellExecutor.execute(new BackAnimationController$BackAnimationImpl$$ExternalSyntheticLambda2(this, z));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$setSwipeThresholds$2(float f, float f2) {
            BackAnimationController.this.setSwipeThresholds(f, f2);
        }

        public void setSwipeThresholds(float f, float f2) {
            BackAnimationController.this.mShellExecutor.execute(new BackAnimationController$BackAnimationImpl$$ExternalSyntheticLambda0(this, f, f2));
        }
    }

    public static class IBackAnimationImpl extends IBackAnimation.Stub {
        public BackAnimationController mController;

        public IBackAnimationImpl(BackAnimationController backAnimationController) {
            this.mController = backAnimationController;
        }

        public void setBackToLauncherCallback(IOnBackInvokedCallback iOnBackInvokedCallback) {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "setBackToLauncherCallback", new BackAnimationController$IBackAnimationImpl$$ExternalSyntheticLambda0(iOnBackInvokedCallback));
        }

        public void clearBackToLauncherCallback() {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "clearBackToLauncherCallback", new BackAnimationController$IBackAnimationImpl$$ExternalSyntheticLambda2());
        }

        public void onBackToLauncherAnimationFinished() {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "onBackToLauncherAnimationFinished", new BackAnimationController$IBackAnimationImpl$$ExternalSyntheticLambda1());
        }

        public void invalidate() {
            this.mController = null;
        }
    }

    @VisibleForTesting
    public void setBackToLauncherCallback(IOnBackInvokedCallback iOnBackInvokedCallback) {
        this.mBackToLauncherCallback = iOnBackInvokedCallback;
    }

    public final void clearBackToLauncherCallback() {
        this.mBackToLauncherCallback = null;
    }

    @VisibleForTesting
    public void onBackToLauncherAnimationFinished() {
        BackNavigationInfo backNavigationInfo = this.mBackNavigationInfo;
        if (backNavigationInfo != null) {
            IOnBackInvokedCallback onBackInvokedCallback = backNavigationInfo.getOnBackInvokedCallback();
            if (this.mTriggerBack) {
                dispatchOnBackInvoked(onBackInvokedCallback);
            } else {
                dispatchOnBackCancelled(onBackInvokedCallback);
            }
        }
        finishAnimation();
    }

    public void onMotionEvent(float f, float f2, int i, int i2) {
        if (!this.mTransitionInProgress) {
            if (i == 2) {
                if (!this.mBackGestureStarted) {
                    initAnimation(f, f2);
                }
                onMove(f, f2, i2);
            } else if (i == 1 || i == 3) {
                if (ShellProtoLogCache.WM_SHELL_BACK_PREVIEW_enabled) {
                    ShellProtoLogImpl.d(ShellProtoLogGroup.WM_SHELL_BACK_PREVIEW, -593738189, 1, "Finishing gesture with event action: %d", Long.valueOf((long) i));
                }
                onGestureFinished();
            }
        }
    }

    public final void initAnimation(float f, float f2) {
        if (ShellProtoLogCache.WM_SHELL_BACK_PREVIEW_enabled) {
            boolean z = this.mBackGestureStarted;
            ShellProtoLogImpl.d(ShellProtoLogGroup.WM_SHELL_BACK_PREVIEW, 1188911440, 3, "initAnimation mMotionStarted=%b", Boolean.valueOf(z));
        }
        if (this.mBackGestureStarted || this.mBackNavigationInfo != null) {
            Log.e("BackAnimationController", "Animation is being initialized but is already started.");
            finishAnimation();
        }
        this.mInitTouchLocation.set(f, f2);
        this.mBackGestureStarted = true;
        try {
            BackNavigationInfo startBackNavigation = this.mActivityTaskManager.startBackNavigation(this.mEnableAnimations.get());
            this.mBackNavigationInfo = startBackNavigation;
            onBackNavigationInfoReceived(startBackNavigation);
        } catch (RemoteException e) {
            Log.e("BackAnimationController", "Failed to initAnimation", e);
            finishAnimation();
        }
    }

    public final void onBackNavigationInfoReceived(BackNavigationInfo backNavigationInfo) {
        if (ShellProtoLogCache.WM_SHELL_BACK_PREVIEW_enabled) {
            String valueOf = String.valueOf(backNavigationInfo);
            ShellProtoLogImpl.d(ShellProtoLogGroup.WM_SHELL_BACK_PREVIEW, -2134376374, 0, "Received backNavigationInfo:%s", valueOf);
        }
        if (backNavigationInfo == null) {
            Log.e("BackAnimationController", "Received BackNavigationInfo is null.");
            finishAnimation();
            return;
        }
        int type = backNavigationInfo.getType();
        IOnBackInvokedCallback iOnBackInvokedCallback = null;
        if (type == 2) {
            HardwareBuffer screenshotHardwareBuffer = backNavigationInfo.getScreenshotHardwareBuffer();
            if (screenshotHardwareBuffer != null) {
                displayTargetScreenshot(screenshotHardwareBuffer, backNavigationInfo.getTaskWindowConfiguration());
            }
            this.mTransaction.apply();
        } else if (shouldDispatchToLauncher(type)) {
            iOnBackInvokedCallback = this.mBackToLauncherCallback;
        } else if (type == 4) {
            iOnBackInvokedCallback = this.mBackNavigationInfo.getOnBackInvokedCallback();
        }
        dispatchOnBackStarted(iOnBackInvokedCallback);
    }

    public final void displayTargetScreenshot(HardwareBuffer hardwareBuffer, WindowConfiguration windowConfiguration) {
        BackNavigationInfo backNavigationInfo = this.mBackNavigationInfo;
        SurfaceControl screenshotSurface = backNavigationInfo == null ? null : backNavigationInfo.getScreenshotSurface();
        if (screenshotSurface == null) {
            Log.e("BackAnimationController", "BackNavigationInfo doesn't contain a surface for the screenshot. ");
            return;
        }
        float width = (float) windowConfiguration.getBounds().width();
        float height = (float) windowConfiguration.getBounds().height();
        float f = 1.0f;
        float width2 = width != ((float) hardwareBuffer.getWidth()) ? width / ((float) hardwareBuffer.getWidth()) : 1.0f;
        if (height != ((float) hardwareBuffer.getHeight())) {
            f = height / ((float) hardwareBuffer.getHeight());
        }
        this.mTransaction.setScale(screenshotSurface, width2, f);
        this.mTransaction.setBuffer(screenshotSurface, hardwareBuffer);
        this.mTransaction.setVisibility(screenshotSurface, true);
    }

    public final void onMove(float f, float f2, int i) {
        if (this.mBackGestureStarted && this.mBackNavigationInfo != null) {
            int round = Math.round(f - this.mInitTouchLocation.x);
            int i2 = PROGRESS_THRESHOLD;
            float min = Math.min(Math.max(((float) Math.abs(round)) / (i2 >= 0 ? (float) i2 : this.mProgressThreshold), 0.0f), 1.0f);
            int type = this.mBackNavigationInfo.getType();
            BackEvent backEvent = new BackEvent(f, f2, min, i, this.mBackNavigationInfo.getDepartingAnimationTarget());
            IOnBackInvokedCallback iOnBackInvokedCallback = null;
            if (shouldDispatchToLauncher(type)) {
                iOnBackInvokedCallback = this.mBackToLauncherCallback;
            } else if (!(type == 3 || type == 2 || type != 4)) {
                iOnBackInvokedCallback = this.mBackNavigationInfo.getOnBackInvokedCallback();
            }
            dispatchOnBackProgressed(iOnBackInvokedCallback, backEvent);
        }
    }

    public final void onGestureFinished() {
        BackNavigationInfo backNavigationInfo;
        IOnBackInvokedCallback iOnBackInvokedCallback;
        if (ShellProtoLogCache.WM_SHELL_BACK_PREVIEW_enabled) {
            String valueOf = String.valueOf(this.mTriggerBack);
            ShellProtoLogImpl.d(ShellProtoLogGroup.WM_SHELL_BACK_PREVIEW, -14660627, 0, "onGestureFinished() mTriggerBack == %s", valueOf);
        }
        if (this.mBackGestureStarted && (backNavigationInfo = this.mBackNavigationInfo) != null) {
            int type = backNavigationInfo.getType();
            boolean shouldDispatchToLauncher = shouldDispatchToLauncher(type);
            if (shouldDispatchToLauncher) {
                iOnBackInvokedCallback = this.mBackToLauncherCallback;
            } else {
                iOnBackInvokedCallback = this.mBackNavigationInfo.getOnBackInvokedCallback();
            }
            if (shouldDispatchToLauncher) {
                startTransition();
            }
            if (this.mTriggerBack) {
                dispatchOnBackInvoked(iOnBackInvokedCallback);
            } else {
                dispatchOnBackCancelled(iOnBackInvokedCallback);
            }
            if (type != 1 || !shouldDispatchToLauncher) {
                finishAnimation();
            }
        }
    }

    public final boolean shouldDispatchToLauncher(int i) {
        if (i != 1 || this.mBackToLauncherCallback == null || !this.mEnableAnimations.get()) {
            return false;
        }
        return true;
    }

    public static void dispatchOnBackStarted(IOnBackInvokedCallback iOnBackInvokedCallback) {
        if (iOnBackInvokedCallback != null) {
            try {
                iOnBackInvokedCallback.onBackStarted();
            } catch (RemoteException e) {
                Log.e("BackAnimationController", "dispatchOnBackStarted error: ", e);
            }
        }
    }

    public static void dispatchOnBackInvoked(IOnBackInvokedCallback iOnBackInvokedCallback) {
        if (iOnBackInvokedCallback != null) {
            try {
                iOnBackInvokedCallback.onBackInvoked();
            } catch (RemoteException e) {
                Log.e("BackAnimationController", "dispatchOnBackInvoked error: ", e);
            }
        }
    }

    public static void dispatchOnBackCancelled(IOnBackInvokedCallback iOnBackInvokedCallback) {
        if (iOnBackInvokedCallback != null) {
            try {
                iOnBackInvokedCallback.onBackCancelled();
            } catch (RemoteException e) {
                Log.e("BackAnimationController", "dispatchOnBackCancelled error: ", e);
            }
        }
    }

    public static void dispatchOnBackProgressed(IOnBackInvokedCallback iOnBackInvokedCallback, BackEvent backEvent) {
        if (iOnBackInvokedCallback != null) {
            try {
                iOnBackInvokedCallback.onBackProgressed(backEvent);
            } catch (RemoteException e) {
                Log.e("BackAnimationController", "dispatchOnBackProgressed error: ", e);
            }
        }
    }

    public void setTriggerBack(boolean z) {
        if (!this.mTransitionInProgress) {
            this.mTriggerBack = z;
        }
    }

    public final void setSwipeThresholds(float f, float f2) {
        this.mProgressThreshold = f2;
        this.mTriggerThreshold = f;
    }

    public final void finishAnimation() {
        SurfaceControl surfaceControl;
        if (ShellProtoLogCache.WM_SHELL_BACK_PREVIEW_enabled) {
            ShellProtoLogImpl.d(ShellProtoLogGroup.WM_SHELL_BACK_PREVIEW, -143863875, 0, "BackAnimationController: finishAnimation()", (Object[]) null);
        }
        this.mBackGestureStarted = false;
        this.mTouchEventDelta.set(0, 0);
        this.mInitTouchLocation.set(0.0f, 0.0f);
        BackNavigationInfo backNavigationInfo = this.mBackNavigationInfo;
        boolean z = this.mTriggerBack;
        this.mBackNavigationInfo = null;
        this.mTriggerBack = false;
        if (backNavigationInfo != null) {
            RemoteAnimationTarget departingAnimationTarget = backNavigationInfo.getDepartingAnimationTarget();
            if (!(departingAnimationTarget == null || (surfaceControl = departingAnimationTarget.leash) == null || !surfaceControl.isValid())) {
                this.mTransaction.remove(departingAnimationTarget.leash);
            }
            SurfaceControl screenshotSurface = backNavigationInfo.getScreenshotSurface();
            if (screenshotSurface != null && screenshotSurface.isValid()) {
                this.mTransaction.remove(screenshotSurface);
            }
            this.mTransaction.apply();
            stopTransition();
            backNavigationInfo.onBackNavigationFinished(z);
        }
    }

    public final void startTransition() {
        if (!this.mTransitionInProgress) {
            this.mTransitionInProgress = true;
            this.mShellExecutor.executeDelayed(this.mResetTransitionRunnable, 2000);
        }
    }

    public final void stopTransition() {
        if (this.mTransitionInProgress) {
            this.mShellExecutor.removeCallbacks(this.mResetTransitionRunnable);
            this.mTransitionInProgress = false;
        }
    }
}
