package com.android.systemui.recents;

import android.app.ActivityTaskManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Insets;
import android.graphics.Rect;
import android.graphics.Region;
import android.hardware.input.InputManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.UserHandle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.inputmethod.InputMethodManager;
import com.android.internal.accessibility.dialog.AccessibilityButtonChooserActivity;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.app.AssistUtils;
import com.android.internal.app.IVoiceInteractionSessionListener;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.policy.ScreenDecorationsUtils;
import com.android.internal.util.ScreenshotHelper;
import com.android.systemui.Dumpable;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.keyguard.KeyguardUnlockAnimationController;
import com.android.systemui.keyguard.ScreenLifecycle;
import com.android.systemui.model.SysUiState;
import com.android.systemui.navigationbar.NavigationBar;
import com.android.systemui.navigationbar.NavigationBarController;
import com.android.systemui.navigationbar.NavigationBarView;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.navigationbar.buttons.KeyButtonView;
import com.android.systemui.settings.CurrentUserTracker;
import com.android.systemui.shared.recents.IOverviewProxy;
import com.android.systemui.shared.recents.ISystemUiProxy;
import com.android.systemui.shared.recents.model.Task$TaskKey;
import com.android.systemui.shared.system.ActivityManagerWrapper;
import com.android.systemui.shared.system.QuickStepContract;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.phone.StatusBarWindowCallback;
import com.android.systemui.statusbar.policy.CallbackController;
import com.android.systemui.theme.ThemeOverlayApplier;
import com.android.wm.shell.back.BackAnimation;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreenController;
import com.android.wm.shell.onehanded.OneHanded;
import com.android.wm.shell.pip.Pip;
import com.android.wm.shell.recents.RecentTasks;
import com.android.wm.shell.splitscreen.SplitScreen;
import com.android.wm.shell.splitscreen.SplitScreenController;
import com.android.wm.shell.startingsurface.StartingSurface;
import com.android.wm.shell.transition.ShellTransitions;
import dagger.Lazy;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class OverviewProxyService extends CurrentUserTracker implements CallbackController<OverviewProxyListener>, NavigationModeController.ModeChangedListener, Dumpable {
    public Region mActiveNavBarRegion;
    public final Optional<BackAnimation> mBackAnimation;
    public boolean mBound;
    public final Lazy<Optional<CentralSurfaces>> mCentralSurfacesOptionalLazy;
    public final CommandQueue mCommandQueue;
    public int mConnectionBackoffAttempts;
    public final List<OverviewProxyListener> mConnectionCallbacks = new ArrayList();
    public final Runnable mConnectionRunnable = new OverviewProxyService$$ExternalSyntheticLambda0(this);
    public final Context mContext;
    public int mCurrentBoundedUserId = -1;
    public final Runnable mDeferredConnectionCallback = new OverviewProxyService$$ExternalSyntheticLambda1(this);
    public final Handler mHandler;
    public long mInputFocusTransferStartMillis;
    public float mInputFocusTransferStartY;
    public boolean mInputFocusTransferStarted;
    public boolean mIsEnabled;
    public final BroadcastReceiver mLauncherStateChangedReceiver;
    public final Optional<LegacySplitScreenController> mLegacySplitescreenCtlOptional;
    public float mNavBarButtonAlpha;
    public final Lazy<NavigationBarController> mNavBarControllerLazy;
    public int mNavBarMode = 0;
    public final Optional<OneHanded> mOneHandedOptional;
    public IOverviewProxy mOverviewProxy;
    public final ServiceConnection mOverviewServiceConnection;
    public final IBinder.DeathRecipient mOverviewServiceDeathRcpt;
    public final Optional<Pip> mPipOptional;
    public final Intent mQuickStepIntent;
    public final Optional<RecentTasks> mRecentTasks;
    public final ComponentName mRecentsComponentName;
    public final ScreenshotHelper mScreenshotHelper;
    public final ShellTransitions mShellTransitions;
    public final BiConsumer<Rect, Rect> mSplitScreenBoundsChangeListener;
    public final Optional<SplitScreen> mSplitScreenOptional;
    public final Optional<SplitScreenController> mSplitescreenCtlOptional;
    public final Optional<StartingSurface> mStartingSurface;
    public final NotificationShadeWindowController mStatusBarWinController;
    public final StatusBarWindowCallback mStatusBarWindowCallback;
    public boolean mSupportsRoundedCornersOnWindows;
    @VisibleForTesting
    public ISystemUiProxy mSysUiProxy = new ISystemUiProxy.Stub() {
        public Rect getNonMinimizedSplitScreenSecondaryBounds() {
            return null;
        }

        public void handleImageAsScreenshot(Bitmap bitmap, Rect rect, Insets insets, int i) {
        }

        public void setSplitScreenMinimized(boolean z) {
        }

        public void startScreenPinning(int i) {
            verifyCallerAndClearCallingIdentityPostMain("startScreenPinning", new OverviewProxyService$1$$ExternalSyntheticLambda5(this, i));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$startScreenPinning$1(int i) {
            ((Optional) OverviewProxyService.this.mCentralSurfacesOptionalLazy.get()).ifPresent(new OverviewProxyService$1$$ExternalSyntheticLambda19(i));
        }

        public void stopScreenPinning() {
            verifyCallerAndClearCallingIdentityPostMain("stopScreenPinning", new OverviewProxyService$1$$ExternalSyntheticLambda8());
        }

        public static /* synthetic */ void lambda$stopScreenPinning$2() {
            try {
                ActivityTaskManager.getService().stopSystemLockTaskMode();
            } catch (RemoteException unused) {
                Log.e("OverviewProxyService", "Failed to stop screen pinning");
            }
        }

        public void onStatusBarMotionEvent(MotionEvent motionEvent) {
            verifyCallerAndClearCallingIdentity("onStatusBarMotionEvent", (Runnable) new OverviewProxyService$1$$ExternalSyntheticLambda7(this, motionEvent));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onStatusBarMotionEvent$5(MotionEvent motionEvent) {
            ((Optional) OverviewProxyService.this.mCentralSurfacesOptionalLazy.get()).ifPresent(new OverviewProxyService$1$$ExternalSyntheticLambda23(this, motionEvent));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onStatusBarMotionEvent$4(MotionEvent motionEvent, CentralSurfaces centralSurfaces) {
            if (motionEvent.getActionMasked() == 0) {
                centralSurfaces.getPanelController().startExpandLatencyTracking();
            }
            OverviewProxyService.this.mHandler.post(new OverviewProxyService$1$$ExternalSyntheticLambda26(this, motionEvent, centralSurfaces));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onStatusBarMotionEvent$3(MotionEvent motionEvent, CentralSurfaces centralSurfaces) {
            int actionMasked = motionEvent.getActionMasked();
            boolean z = false;
            if (actionMasked == 0) {
                OverviewProxyService.this.mInputFocusTransferStarted = true;
                OverviewProxyService.this.mInputFocusTransferStartY = motionEvent.getY();
                OverviewProxyService.this.mInputFocusTransferStartMillis = motionEvent.getEventTime();
                centralSurfaces.onInputFocusTransfer(OverviewProxyService.this.mInputFocusTransferStarted, false, 0.0f);
            }
            if (actionMasked == 1 || actionMasked == 3) {
                OverviewProxyService.this.mInputFocusTransferStarted = false;
                float y = (motionEvent.getY() - OverviewProxyService.this.mInputFocusTransferStartY) / ((float) (motionEvent.getEventTime() - OverviewProxyService.this.mInputFocusTransferStartMillis));
                boolean r9 = OverviewProxyService.this.mInputFocusTransferStarted;
                if (actionMasked == 3) {
                    z = true;
                }
                centralSurfaces.onInputFocusTransfer(r9, z, y);
            }
            motionEvent.recycle();
        }

        public void onBackPressed() throws RemoteException {
            verifyCallerAndClearCallingIdentityPostMain("onBackPressed", new OverviewProxyService$1$$ExternalSyntheticLambda10(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onBackPressed$6() {
            sendEvent(0, 4);
            sendEvent(1, 4);
            OverviewProxyService.this.notifyBackAction(true, -1, -1, true, false);
        }

        public void onImeSwitcherPressed() throws RemoteException {
            ((InputMethodManager) OverviewProxyService.this.mContext.getSystemService(InputMethodManager.class)).showInputMethodPickerFromSystem(true, 0);
            OverviewProxyService.this.mUiEventLogger.log(KeyButtonView.NavBarButtonEvent.NAVBAR_IME_SWITCHER_BUTTON_TAP);
        }

        public void setHomeRotationEnabled(boolean z) {
            verifyCallerAndClearCallingIdentityPostMain("setHomeRotationEnabled", new OverviewProxyService$1$$ExternalSyntheticLambda1(this, z));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$setHomeRotationEnabled$7(boolean z) {
            OverviewProxyService.this.notifyHomeRotationEnabled(z);
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$setHomeRotationEnabled$8(boolean z) {
            OverviewProxyService.this.mHandler.post(new OverviewProxyService$1$$ExternalSyntheticLambda20(this, z));
        }

        public void notifyTaskbarStatus(boolean z, boolean z2) {
            verifyCallerAndClearCallingIdentityPostMain("notifyTaskbarStatus", new OverviewProxyService$1$$ExternalSyntheticLambda0(this, z, z2));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$notifyTaskbarStatus$9(boolean z, boolean z2) {
            OverviewProxyService.this.onTaskbarStatusUpdated(z, z2);
        }

        public void notifyTaskbarAutohideSuspend(boolean z) {
            verifyCallerAndClearCallingIdentityPostMain("notifyTaskbarAutohideSuspend", new OverviewProxyService$1$$ExternalSyntheticLambda4(this, z));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$notifyTaskbarAutohideSuspend$10(boolean z) {
            OverviewProxyService.this.onTaskbarAutohideSuspend(z);
        }

        public final boolean sendEvent(int i, int i2) {
            long uptimeMillis = SystemClock.uptimeMillis();
            KeyEvent keyEvent = new KeyEvent(uptimeMillis, uptimeMillis, i, i2, 0, 0, -1, 0, 72, 257);
            keyEvent.setDisplayId(OverviewProxyService.this.mContext.getDisplay().getDisplayId());
            return InputManager.getInstance().injectInputEvent(keyEvent, 0);
        }

        public void onOverviewShown(boolean z) {
            verifyCallerAndClearCallingIdentityPostMain("onOverviewShown", new OverviewProxyService$1$$ExternalSyntheticLambda16(this, z));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onOverviewShown$11(boolean z) {
            for (int size = OverviewProxyService.this.mConnectionCallbacks.size() - 1; size >= 0; size--) {
                ((OverviewProxyListener) OverviewProxyService.this.mConnectionCallbacks.get(size)).onOverviewShown(z);
            }
        }

        public void setNavBarButtonAlpha(float f, boolean z) {
            verifyCallerAndClearCallingIdentityPostMain("setNavBarButtonAlpha", new OverviewProxyService$1$$ExternalSyntheticLambda6(this, f, z));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$setNavBarButtonAlpha$12(float f, boolean z) {
            OverviewProxyService.this.notifyNavBarButtonAlphaChanged(f, z);
        }

        public void onAssistantProgress(float f) {
            verifyCallerAndClearCallingIdentityPostMain("onAssistantProgress", new OverviewProxyService$1$$ExternalSyntheticLambda12(this, f));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onAssistantProgress$13(float f) {
            OverviewProxyService.this.notifyAssistantProgress(f);
        }

        public void onAssistantGestureCompletion(float f) {
            verifyCallerAndClearCallingIdentityPostMain("onAssistantGestureCompletion", new OverviewProxyService$1$$ExternalSyntheticLambda3(this, f));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onAssistantGestureCompletion$14(float f) {
            OverviewProxyService.this.notifyAssistantGestureCompletion(f);
        }

        public void startAssistant(Bundle bundle) {
            verifyCallerAndClearCallingIdentityPostMain("startAssistant", new OverviewProxyService$1$$ExternalSyntheticLambda11(this, bundle));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$startAssistant$15(Bundle bundle) {
            OverviewProxyService.this.notifyStartAssistant(bundle);
        }

        public void notifyAccessibilityButtonClicked(int i) {
            verifyCallerAndClearCallingIdentity("notifyAccessibilityButtonClicked", (Runnable) new OverviewProxyService$1$$ExternalSyntheticLambda15(this, i));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$notifyAccessibilityButtonClicked$16(int i) {
            AccessibilityManager.getInstance(OverviewProxyService.this.mContext).notifyAccessibilityButtonClicked(i);
        }

        public void notifyAccessibilityButtonLongClicked() {
            verifyCallerAndClearCallingIdentity("notifyAccessibilityButtonLongClicked", (Runnable) new OverviewProxyService$1$$ExternalSyntheticLambda17(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$notifyAccessibilityButtonLongClicked$17() {
            Intent intent = new Intent("com.android.internal.intent.action.CHOOSE_ACCESSIBILITY_BUTTON");
            intent.setClassName(ThemeOverlayApplier.ANDROID_PACKAGE, AccessibilityButtonChooserActivity.class.getName());
            intent.addFlags(268468224);
            OverviewProxyService.this.mContext.startActivityAsUser(intent, UserHandle.CURRENT);
        }

        public void notifySwipeToHomeFinished() {
            verifyCallerAndClearCallingIdentity("notifySwipeToHomeFinished", (Runnable) new OverviewProxyService$1$$ExternalSyntheticLambda13(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$notifySwipeToHomeFinished$19() {
            OverviewProxyService.this.mPipOptional.ifPresent(new OverviewProxyService$1$$ExternalSyntheticLambda22());
        }

        public void notifySwipeUpGestureStarted() {
            verifyCallerAndClearCallingIdentityPostMain("notifySwipeUpGestureStarted", new OverviewProxyService$1$$ExternalSyntheticLambda9(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$notifySwipeUpGestureStarted$20() {
            OverviewProxyService.this.notifySwipeUpGestureStartedInternal();
        }

        public void notifyPrioritizedRotation(int i) {
            verifyCallerAndClearCallingIdentityPostMain("notifyPrioritizedRotation", new OverviewProxyService$1$$ExternalSyntheticLambda18(this, i));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$notifyPrioritizedRotation$21(int i) {
            OverviewProxyService.this.notifyPrioritizedRotationInternal(i);
        }

        public void handleImageBundleAsScreenshot(Bundle bundle, Rect rect, Insets insets, Task$TaskKey task$TaskKey) {
            OverviewProxyService.this.mScreenshotHelper.provideScreenshot(bundle, rect, insets, task$TaskKey.id, task$TaskKey.userId, task$TaskKey.sourceComponent, 3, OverviewProxyService.this.mHandler, (Consumer) null);
        }

        public void expandNotificationPanel() {
            verifyCallerAndClearCallingIdentity("expandNotificationPanel", (Runnable) new OverviewProxyService$1$$ExternalSyntheticLambda14(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$expandNotificationPanel$22() {
            OverviewProxyService.this.mCommandQueue.handleSystemKey(281);
        }

        public void toggleNotificationPanel() {
            verifyCallerAndClearCallingIdentityPostMain("toggleNotificationPanel", new OverviewProxyService$1$$ExternalSyntheticLambda2(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$toggleNotificationPanel$23() {
            ((Optional) OverviewProxyService.this.mCentralSurfacesOptionalLazy.get()).ifPresent(new OverviewProxyService$1$$ExternalSyntheticLambda25());
        }

        public final boolean verifyCaller(String str) {
            int identifier = Binder.getCallingUserHandle().getIdentifier();
            if (identifier == OverviewProxyService.this.mCurrentBoundedUserId) {
                return true;
            }
            Log.w("OverviewProxyService", "Launcher called sysui with invalid user: " + identifier + ", reason: " + str);
            return false;
        }

        public final <T> T verifyCallerAndClearCallingIdentity(String str, Supplier<T> supplier) {
            if (!verifyCaller(str)) {
                return null;
            }
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return supplier.get();
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public final void verifyCallerAndClearCallingIdentity(String str, Runnable runnable) {
            verifyCallerAndClearCallingIdentity(str, new OverviewProxyService$1$$ExternalSyntheticLambda21(runnable));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ Boolean lambda$verifyCallerAndClearCallingIdentityPostMain$25(Runnable runnable) {
            return Boolean.valueOf(OverviewProxyService.this.mHandler.post(runnable));
        }

        public final void verifyCallerAndClearCallingIdentityPostMain(String str, Runnable runnable) {
            verifyCallerAndClearCallingIdentity(str, new OverviewProxyService$1$$ExternalSyntheticLambda24(this, runnable));
        }
    };
    public SysUiState mSysUiState;
    public final KeyguardUnlockAnimationController mSysuiUnlockAnimationController;
    public final UiEventLogger mUiEventLogger;
    public final IVoiceInteractionSessionListener mVoiceInteractionSessionListener;
    public float mWindowCornerRadius;

    public interface OverviewProxyListener {
        void onAssistantGestureCompletion(float f) {
        }

        void onAssistantProgress(float f) {
        }

        void onConnectionChanged(boolean z) {
        }

        void onHomeRotationEnabled(boolean z) {
        }

        void onNavBarButtonAlphaChanged(float f, boolean z) {
        }

        void onOverviewShown(boolean z) {
        }

        void onPrioritizedRotation(int i) {
        }

        void onSwipeUpGestureStarted() {
        }

        void onTaskbarAutohideSuspend(boolean z) {
        }

        void onTaskbarStatusUpdated(boolean z, boolean z2) {
        }

        void onToggleRecentApps() {
        }

        void startAssistant(Bundle bundle) {
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        Log.w("OverviewProxyService", "Binder supposed established connection but actual connection to service timed out, trying again");
        retryConnectionWithBackoff();
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public OverviewProxyService(Context context, CommandQueue commandQueue, Lazy<NavigationBarController> lazy, Lazy<Optional<CentralSurfaces>> lazy2, NavigationModeController navigationModeController, NotificationShadeWindowController notificationShadeWindowController, SysUiState sysUiState, Optional<Pip> optional, Optional<LegacySplitScreenController> optional2, Optional<SplitScreen> optional3, Optional<SplitScreenController> optional4, Optional<OneHanded> optional5, Optional<RecentTasks> optional6, Optional<BackAnimation> optional7, Optional<StartingSurface> optional8, BroadcastDispatcher broadcastDispatcher, ShellTransitions shellTransitions, ScreenLifecycle screenLifecycle, UiEventLogger uiEventLogger, KeyguardUnlockAnimationController keyguardUnlockAnimationController, AssistUtils assistUtils, DumpManager dumpManager) {
        super(broadcastDispatcher);
        NotificationShadeWindowController notificationShadeWindowController2 = notificationShadeWindowController;
        SysUiState sysUiState2 = sysUiState;
        AnonymousClass2 r6 = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                OverviewProxyService.this.updateEnabledState();
                OverviewProxyService.this.startConnectionToCurrentUser();
            }
        };
        this.mLauncherStateChangedReceiver = r6;
        this.mOverviewServiceConnection = new ServiceConnection() {
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                OverviewProxyService.this.mConnectionBackoffAttempts = 0;
                OverviewProxyService.this.mHandler.removeCallbacks(OverviewProxyService.this.mDeferredConnectionCallback);
                try {
                    iBinder.linkToDeath(OverviewProxyService.this.mOverviewServiceDeathRcpt, 0);
                    OverviewProxyService overviewProxyService = OverviewProxyService.this;
                    overviewProxyService.mCurrentBoundedUserId = overviewProxyService.getCurrentUserId();
                    OverviewProxyService.this.mOverviewProxy = IOverviewProxy.Stub.asInterface(iBinder);
                    Bundle bundle = new Bundle();
                    bundle.putBinder("extra_sysui_proxy", OverviewProxyService.this.mSysUiProxy.asBinder());
                    bundle.putFloat("extra_window_corner_radius", OverviewProxyService.this.mWindowCornerRadius);
                    bundle.putBoolean("extra_supports_window_corners", OverviewProxyService.this.mSupportsRoundedCornersOnWindows);
                    OverviewProxyService.this.mPipOptional.ifPresent(new OverviewProxyService$3$$ExternalSyntheticLambda0(bundle));
                    OverviewProxyService.this.mSplitScreenOptional.ifPresent(new OverviewProxyService$3$$ExternalSyntheticLambda1(bundle));
                    OverviewProxyService.this.mOneHandedOptional.ifPresent(new OverviewProxyService$3$$ExternalSyntheticLambda2(bundle));
                    bundle.putBinder("extra_shell_shell_transitions", OverviewProxyService.this.mShellTransitions.createExternalInterface().asBinder());
                    OverviewProxyService.this.mStartingSurface.ifPresent(new OverviewProxyService$3$$ExternalSyntheticLambda3(bundle));
                    bundle.putBinder("unlock_animation", OverviewProxyService.this.mSysuiUnlockAnimationController.asBinder());
                    OverviewProxyService.this.mRecentTasks.ifPresent(new OverviewProxyService$3$$ExternalSyntheticLambda4(bundle));
                    OverviewProxyService.this.mBackAnimation.ifPresent(new OverviewProxyService$3$$ExternalSyntheticLambda5(bundle));
                    try {
                        Log.d("OverviewProxyService", "OverviewProxyService connected, initializing overview proxy");
                        OverviewProxyService.this.mOverviewProxy.onInitialize(bundle);
                    } catch (RemoteException e) {
                        OverviewProxyService.this.mCurrentBoundedUserId = -1;
                        Log.e("OverviewProxyService", "Failed to call onInitialize()", e);
                    }
                    OverviewProxyService.this.dispatchNavButtonBounds();
                    OverviewProxyService.this.updateSystemUiStateFlags();
                    OverviewProxyService overviewProxyService2 = OverviewProxyService.this;
                    overviewProxyService2.notifySystemUiStateFlags(overviewProxyService2.mSysUiState.getFlags());
                    OverviewProxyService.this.notifyConnectionChanged();
                } catch (RemoteException e2) {
                    Log.e("OverviewProxyService", "Lost connection to launcher service", e2);
                    OverviewProxyService.this.disconnectFromLauncherService();
                    OverviewProxyService.this.retryConnectionWithBackoff();
                }
            }

            public void onNullBinding(ComponentName componentName) {
                Log.w("OverviewProxyService", "Null binding of '" + componentName + "', try reconnecting");
                OverviewProxyService.this.mCurrentBoundedUserId = -1;
                OverviewProxyService.this.retryConnectionWithBackoff();
            }

            public void onBindingDied(ComponentName componentName) {
                Log.w("OverviewProxyService", "Binding died of '" + componentName + "', try reconnecting");
                OverviewProxyService.this.mCurrentBoundedUserId = -1;
                OverviewProxyService.this.retryConnectionWithBackoff();
            }

            public void onServiceDisconnected(ComponentName componentName) {
                Log.w("OverviewProxyService", "Service disconnected");
                OverviewProxyService.this.mCurrentBoundedUserId = -1;
            }
        };
        OverviewProxyService$$ExternalSyntheticLambda2 overviewProxyService$$ExternalSyntheticLambda2 = new OverviewProxyService$$ExternalSyntheticLambda2(this);
        this.mStatusBarWindowCallback = overviewProxyService$$ExternalSyntheticLambda2;
        this.mSplitScreenBoundsChangeListener = new OverviewProxyService$$ExternalSyntheticLambda3(this);
        this.mOverviewServiceDeathRcpt = new OverviewProxyService$$ExternalSyntheticLambda4(this);
        AnonymousClass4 r8 = new IVoiceInteractionSessionListener.Stub() {
            public void onSetUiHints(Bundle bundle) {
            }

            public void onVoiceSessionHidden() {
            }

            public void onVoiceSessionShown() {
            }

            public void onVoiceSessionWindowVisibilityChanged(boolean z) {
                OverviewProxyService.this.mContext.getMainExecutor().execute(new OverviewProxyService$4$$ExternalSyntheticLambda0(this, z));
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$onVoiceSessionWindowVisibilityChanged$0(boolean z) {
                OverviewProxyService.this.onVoiceSessionWindowVisibilityChanged(z);
            }
        };
        this.mVoiceInteractionSessionListener = r8;
        this.mContext = context;
        this.mPipOptional = optional;
        this.mCentralSurfacesOptionalLazy = lazy2;
        this.mHandler = new Handler();
        this.mNavBarControllerLazy = lazy;
        this.mStatusBarWinController = notificationShadeWindowController2;
        this.mConnectionBackoffAttempts = 0;
        ComponentName unflattenFromString = ComponentName.unflattenFromString(context.getString(17040029));
        this.mRecentsComponentName = unflattenFromString;
        this.mQuickStepIntent = new Intent("android.intent.action.QUICKSTEP_SERVICE").setPackage(unflattenFromString.getPackageName());
        this.mWindowCornerRadius = ScreenDecorationsUtils.getWindowCornerRadius(context);
        this.mSupportsRoundedCornersOnWindows = ScreenDecorationsUtils.supportsRoundedCornersOnWindows(context.getResources());
        this.mSysUiState = sysUiState2;
        sysUiState2.addCallback(new OverviewProxyService$$ExternalSyntheticLambda5(this));
        this.mOneHandedOptional = optional5;
        this.mShellTransitions = shellTransitions;
        this.mRecentTasks = optional6;
        this.mBackAnimation = optional7;
        this.mUiEventLogger = uiEventLogger;
        this.mNavBarButtonAlpha = 1.0f;
        dumpManager.registerDumpable(getClass().getSimpleName(), this);
        this.mNavBarMode = navigationModeController.addListener(this);
        IntentFilter intentFilter = new IntentFilter("android.intent.action.PACKAGE_ADDED");
        intentFilter.addDataScheme("package");
        intentFilter.addDataSchemeSpecificPart(unflattenFromString.getPackageName(), 0);
        intentFilter.addAction("android.intent.action.PACKAGE_CHANGED");
        context.registerReceiver(r6, intentFilter);
        notificationShadeWindowController2.registerCallback(overviewProxyService$$ExternalSyntheticLambda2);
        this.mScreenshotHelper = new ScreenshotHelper(context);
        commandQueue.addCallback((CommandQueue.Callbacks) new CommandQueue.Callbacks() {
            public void onTracingStateChanged(boolean z) {
                OverviewProxyService.this.mSysUiState.setFlag(4096, z).commitUpdate(OverviewProxyService.this.mContext.getDisplayId());
            }
        });
        this.mCommandQueue = commandQueue;
        this.mSplitScreenOptional = optional3;
        this.mSplitescreenCtlOptional = optional4;
        this.mLegacySplitescreenCtlOptional = optional2;
        startTracking();
        screenLifecycle.addObserver(new ScreenLifecycle.Observer() {
            public void onScreenTurnedOn() {
                OverviewProxyService.this.notifyScreenTurnedOn();
            }
        });
        updateEnabledState();
        startConnectionToCurrentUser();
        this.mStartingSurface = optional8;
        this.mSysuiUnlockAnimationController = keyguardUnlockAnimationController;
        assistUtils.registerVoiceInteractionSessionListener(r8);
    }

    public void onUserSwitched(int i) {
        this.mConnectionBackoffAttempts = 0;
        internalConnectToCurrentUser();
    }

    public void onVoiceSessionWindowVisibilityChanged(boolean z) {
        this.mSysUiState.setFlag(33554432, z).commitUpdate(this.mContext.getDisplayId());
    }

    public void notifyBackAction(boolean z, int i, int i2, boolean z2, boolean z3) {
        try {
            IOverviewProxy iOverviewProxy = this.mOverviewProxy;
            if (iOverviewProxy != null) {
                iOverviewProxy.onBackAction(z, i, i2, z2, z3);
            }
        } catch (RemoteException e) {
            Log.e("OverviewProxyService", "Failed to notify back action", e);
        }
    }

    public final void updateSystemUiStateFlags() {
        NavigationBar defaultNavigationBar = this.mNavBarControllerLazy.get().getDefaultNavigationBar();
        NavigationBarView navigationBarView = this.mNavBarControllerLazy.get().getNavigationBarView(this.mContext.getDisplayId());
        NotificationPanelViewController panelController = ((CentralSurfaces) this.mCentralSurfacesOptionalLazy.get().get()).getPanelController();
        if (defaultNavigationBar != null) {
            defaultNavigationBar.updateSystemUiStateFlags();
        }
        if (navigationBarView != null) {
            navigationBarView.updateDisabledSystemUiStateFlags(this.mSysUiState);
        }
        if (panelController != null) {
            panelController.updateSystemUiStateFlags();
        }
        NotificationShadeWindowController notificationShadeWindowController = this.mStatusBarWinController;
        if (notificationShadeWindowController != null) {
            notificationShadeWindowController.notifyStateChangedCallbacks();
        }
    }

    public final void notifySystemUiStateFlags(int i) {
        try {
            IOverviewProxy iOverviewProxy = this.mOverviewProxy;
            if (iOverviewProxy != null) {
                iOverviewProxy.onSystemUiStateChanged(i);
            }
        } catch (RemoteException e) {
            Log.e("OverviewProxyService", "Failed to notify sysui state change", e);
        }
    }

    public final void onStatusBarStateChanged(boolean z, boolean z2, boolean z3, boolean z4) {
        boolean z5 = true;
        SysUiState flag = this.mSysUiState.setFlag(64, z && !z2);
        if (!z || !z2) {
            z5 = false;
        }
        flag.setFlag(512, z5).setFlag(8, z3).setFlag(2097152, z4).commitUpdate(this.mContext.getDisplayId());
    }

    public void onActiveNavBarRegionChanges(Region region) {
        this.mActiveNavBarRegion = region;
        dispatchNavButtonBounds();
    }

    public final void dispatchNavButtonBounds() {
        Region region;
        IOverviewProxy iOverviewProxy = this.mOverviewProxy;
        if (iOverviewProxy != null && (region = this.mActiveNavBarRegion) != null) {
            try {
                iOverviewProxy.onActiveNavBarRegionChanges(region);
            } catch (RemoteException e) {
                Log.e("OverviewProxyService", "Failed to call onActiveNavBarRegionChanges()", e);
            }
        }
    }

    public void cleanupAfterDeath() {
        if (this.mInputFocusTransferStarted) {
            this.mHandler.post(new OverviewProxyService$$ExternalSyntheticLambda6(this));
        }
        startConnectionToCurrentUser();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$cleanupAfterDeath$4() {
        this.mCentralSurfacesOptionalLazy.get().ifPresent(new OverviewProxyService$$ExternalSyntheticLambda7(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$cleanupAfterDeath$3(CentralSurfaces centralSurfaces) {
        this.mInputFocusTransferStarted = false;
        centralSurfaces.onInputFocusTransfer(false, true, 0.0f);
    }

    public void startConnectionToCurrentUser() {
        if (this.mHandler.getLooper() != Looper.myLooper()) {
            this.mHandler.post(this.mConnectionRunnable);
        } else {
            internalConnectToCurrentUser();
        }
    }

    public final void internalConnectToCurrentUser() {
        disconnectFromLauncherService();
        if (!isEnabled()) {
            Log.v("OverviewProxyService", "Cannot attempt connection, is enabled " + isEnabled());
            return;
        }
        this.mHandler.removeCallbacks(this.mConnectionRunnable);
        try {
            this.mBound = this.mContext.bindServiceAsUser(new Intent("android.intent.action.QUICKSTEP_SERVICE").setPackage(this.mRecentsComponentName.getPackageName()), this.mOverviewServiceConnection, 33554433, UserHandle.of(getCurrentUserId()));
        } catch (SecurityException e) {
            Log.e("OverviewProxyService", "Unable to bind because of security error", e);
        }
        if (this.mBound) {
            this.mHandler.postDelayed(this.mDeferredConnectionCallback, 5000);
        } else {
            retryConnectionWithBackoff();
        }
    }

    public final void retryConnectionWithBackoff() {
        if (!this.mHandler.hasCallbacks(this.mConnectionRunnable)) {
            long min = (long) Math.min(Math.scalb(1000.0f, this.mConnectionBackoffAttempts), 600000.0f);
            this.mHandler.postDelayed(this.mConnectionRunnable, min);
            this.mConnectionBackoffAttempts++;
            Log.w("OverviewProxyService", "Failed to connect on attempt " + this.mConnectionBackoffAttempts + " will try again in " + min + "ms");
        }
    }

    public void addCallback(OverviewProxyListener overviewProxyListener) {
        if (!this.mConnectionCallbacks.contains(overviewProxyListener)) {
            this.mConnectionCallbacks.add(overviewProxyListener);
        }
        overviewProxyListener.onConnectionChanged(this.mOverviewProxy != null);
        overviewProxyListener.onNavBarButtonAlphaChanged(this.mNavBarButtonAlpha, false);
    }

    public void removeCallback(OverviewProxyListener overviewProxyListener) {
        this.mConnectionCallbacks.remove(overviewProxyListener);
    }

    public boolean shouldShowSwipeUpUI() {
        return isEnabled() && !QuickStepContract.isLegacyMode(this.mNavBarMode);
    }

    public boolean isEnabled() {
        return this.mIsEnabled;
    }

    public IOverviewProxy getProxy() {
        return this.mOverviewProxy;
    }

    public final void disconnectFromLauncherService() {
        if (this.mBound) {
            this.mContext.unbindService(this.mOverviewServiceConnection);
            this.mBound = false;
        }
        IOverviewProxy iOverviewProxy = this.mOverviewProxy;
        if (iOverviewProxy != null) {
            iOverviewProxy.asBinder().unlinkToDeath(this.mOverviewServiceDeathRcpt, 0);
            this.mOverviewProxy = null;
            notifyNavBarButtonAlphaChanged(1.0f, false);
            notifyConnectionChanged();
        }
    }

    public final void notifyNavBarButtonAlphaChanged(float f, boolean z) {
        for (int size = this.mConnectionCallbacks.size() - 1; size >= 0; size--) {
            this.mConnectionCallbacks.get(size).onNavBarButtonAlphaChanged(f, z);
        }
    }

    public final void notifyHomeRotationEnabled(boolean z) {
        for (int size = this.mConnectionCallbacks.size() - 1; size >= 0; size--) {
            this.mConnectionCallbacks.get(size).onHomeRotationEnabled(z);
        }
    }

    public final void onTaskbarStatusUpdated(boolean z, boolean z2) {
        for (int size = this.mConnectionCallbacks.size() - 1; size >= 0; size--) {
            this.mConnectionCallbacks.get(size).onTaskbarStatusUpdated(z, z2);
        }
    }

    public final void onTaskbarAutohideSuspend(boolean z) {
        for (int size = this.mConnectionCallbacks.size() - 1; size >= 0; size--) {
            this.mConnectionCallbacks.get(size).onTaskbarAutohideSuspend(z);
        }
    }

    public final void notifyConnectionChanged() {
        for (int size = this.mConnectionCallbacks.size() - 1; size >= 0; size--) {
            this.mConnectionCallbacks.get(size).onConnectionChanged(this.mOverviewProxy != null);
        }
    }

    public final void notifyPrioritizedRotationInternal(int i) {
        for (int size = this.mConnectionCallbacks.size() - 1; size >= 0; size--) {
            this.mConnectionCallbacks.get(size).onPrioritizedRotation(i);
        }
    }

    public final void notifyAssistantProgress(float f) {
        for (int size = this.mConnectionCallbacks.size() - 1; size >= 0; size--) {
            this.mConnectionCallbacks.get(size).onAssistantProgress(f);
        }
    }

    public final void notifyAssistantGestureCompletion(float f) {
        for (int size = this.mConnectionCallbacks.size() - 1; size >= 0; size--) {
            this.mConnectionCallbacks.get(size).onAssistantGestureCompletion(f);
        }
    }

    public final void notifyStartAssistant(Bundle bundle) {
        for (int size = this.mConnectionCallbacks.size() - 1; size >= 0; size--) {
            this.mConnectionCallbacks.get(size).startAssistant(bundle);
        }
    }

    public final void notifySwipeUpGestureStartedInternal() {
        for (int size = this.mConnectionCallbacks.size() - 1; size >= 0; size--) {
            this.mConnectionCallbacks.get(size).onSwipeUpGestureStarted();
        }
    }

    public void notifySplitScreenBoundsChanged(Rect rect, Rect rect2) {
        try {
            IOverviewProxy iOverviewProxy = this.mOverviewProxy;
            if (iOverviewProxy != null) {
                iOverviewProxy.onSplitScreenSecondaryBoundsChanged(rect, rect2);
            } else {
                Log.e("OverviewProxyService", "Failed to get overview proxy for split screen bounds.");
            }
        } catch (RemoteException e) {
            Log.e("OverviewProxyService", "Failed to call onSplitScreenSecondaryBoundsChanged()", e);
        }
    }

    public void notifyScreenTurnedOn() {
        try {
            IOverviewProxy iOverviewProxy = this.mOverviewProxy;
            if (iOverviewProxy != null) {
                iOverviewProxy.onScreenTurnedOn();
            } else {
                Log.e("OverviewProxyService", "Failed to get overview proxy for screen turned on event.");
            }
        } catch (RemoteException e) {
            Log.e("OverviewProxyService", "Failed to call notifyScreenTurnedOn()", e);
        }
    }

    public void notifyToggleRecentApps() {
        for (int size = this.mConnectionCallbacks.size() - 1; size >= 0; size--) {
            this.mConnectionCallbacks.get(size).onToggleRecentApps();
        }
    }

    public void disable(int i, int i2, int i3, boolean z) {
        try {
            IOverviewProxy iOverviewProxy = this.mOverviewProxy;
            if (iOverviewProxy != null) {
                iOverviewProxy.disable(i, i2, i3, z);
            } else {
                Log.e("OverviewProxyService", "Failed to get overview proxy for disable flags.");
            }
        } catch (RemoteException e) {
            Log.e("OverviewProxyService", "Failed to call disable()", e);
        }
    }

    public void onRotationProposal(int i, boolean z) {
        try {
            IOverviewProxy iOverviewProxy = this.mOverviewProxy;
            if (iOverviewProxy != null) {
                iOverviewProxy.onRotationProposal(i, z);
            } else {
                Log.e("OverviewProxyService", "Failed to get overview proxy for proposing rotation.");
            }
        } catch (RemoteException e) {
            Log.e("OverviewProxyService", "Failed to call onRotationProposal()", e);
        }
    }

    public void onSystemBarAttributesChanged(int i, int i2) {
        try {
            IOverviewProxy iOverviewProxy = this.mOverviewProxy;
            if (iOverviewProxy != null) {
                iOverviewProxy.onSystemBarAttributesChanged(i, i2);
            } else {
                Log.e("OverviewProxyService", "Failed to get overview proxy for system bar attr change.");
            }
        } catch (RemoteException e) {
            Log.e("OverviewProxyService", "Failed to call onSystemBarAttributesChanged()", e);
        }
    }

    public void onNavButtonsDarkIntensityChanged(float f) {
        try {
            IOverviewProxy iOverviewProxy = this.mOverviewProxy;
            if (iOverviewProxy != null) {
                iOverviewProxy.onNavButtonsDarkIntensityChanged(f);
            } else {
                Log.e("OverviewProxyService", "Failed to get overview proxy to update nav buttons dark intensity");
            }
        } catch (RemoteException e) {
            Log.e("OverviewProxyService", "Failed to call onNavButtonsDarkIntensityChanged()", e);
        }
    }

    public final void updateEnabledState() {
        this.mIsEnabled = this.mContext.getPackageManager().resolveServiceAsUser(this.mQuickStepIntent, 1048576, ActivityManagerWrapper.getInstance().getCurrentUserId()) != null;
    }

    public void onNavigationModeChanged(int i) {
        this.mNavBarMode = i;
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("OverviewProxyService state:");
        printWriter.print("  isConnected=");
        printWriter.println(this.mOverviewProxy != null);
        printWriter.print("  mIsEnabled=");
        printWriter.println(isEnabled());
        printWriter.print("  mRecentsComponentName=");
        printWriter.println(this.mRecentsComponentName);
        printWriter.print("  mQuickStepIntent=");
        printWriter.println(this.mQuickStepIntent);
        printWriter.print("  mBound=");
        printWriter.println(this.mBound);
        printWriter.print("  mCurrentBoundedUserId=");
        printWriter.println(this.mCurrentBoundedUserId);
        printWriter.print("  mConnectionBackoffAttempts=");
        printWriter.println(this.mConnectionBackoffAttempts);
        printWriter.print("  mInputFocusTransferStarted=");
        printWriter.println(this.mInputFocusTransferStarted);
        printWriter.print("  mInputFocusTransferStartY=");
        printWriter.println(this.mInputFocusTransferStartY);
        printWriter.print("  mInputFocusTransferStartMillis=");
        printWriter.println(this.mInputFocusTransferStartMillis);
        printWriter.print("  mWindowCornerRadius=");
        printWriter.println(this.mWindowCornerRadius);
        printWriter.print("  mSupportsRoundedCornersOnWindows=");
        printWriter.println(this.mSupportsRoundedCornersOnWindows);
        printWriter.print("  mNavBarButtonAlpha=");
        printWriter.println(this.mNavBarButtonAlpha);
        printWriter.print("  mActiveNavBarRegion=");
        printWriter.println(this.mActiveNavBarRegion);
        printWriter.print("  mNavBarMode=");
        printWriter.println(this.mNavBarMode);
        this.mSysUiState.dump(printWriter, strArr);
    }
}
