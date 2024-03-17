package com.android.wm.shell.onehanded;

import android.content.ComponentName;
import android.content.Context;
import android.content.om.IOverlayManager;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.graphics.Rect;
import android.os.Handler;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.util.Slog;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.window.WindowContainerTransaction;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.internal.logging.UiEventLogger;
import com.android.wm.shell.R;
import com.android.wm.shell.common.DisplayChangeController;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.DisplayLayout;
import com.android.wm.shell.common.ExecutorUtils;
import com.android.wm.shell.common.RemoteCallable;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.TaskStackListenerCallback;
import com.android.wm.shell.common.TaskStackListenerImpl;
import com.android.wm.shell.onehanded.IOneHanded;
import java.io.PrintWriter;

public class OneHandedController implements RemoteCallable<OneHandedController>, DisplayChangeController.OnDisplayChangingListener {
    public final AccessibilityManager mAccessibilityManager;
    public AccessibilityManager.AccessibilityStateChangeListener mAccessibilityStateChangeListener;
    public final ContentObserver mActivatedObserver;
    public Context mContext;
    public OneHandedDisplayAreaOrganizer mDisplayAreaOrganizer;
    public final DisplayController mDisplayController;
    public final DisplayController.OnDisplaysChangedListener mDisplaysChangedListener;
    public final ContentObserver mEnabledObserver;
    public OneHandedEventCallback mEventCallback;
    public final OneHandedImpl mImpl = new OneHandedImpl();
    public volatile boolean mIsOneHandedEnabled;
    public boolean mIsShortcutEnabled;
    public volatile boolean mIsSwipeToNotificationEnabled;
    public boolean mKeyguardShowing;
    public boolean mLockedDisabled;
    public final ShellExecutor mMainExecutor;
    public final Handler mMainHandler;
    public float mOffSetFraction;
    public final OneHandedAccessibilityUtil mOneHandedAccessibilityUtil;
    public final OneHandedSettingsUtil mOneHandedSettingsUtil;
    public OneHandedUiEventLogger mOneHandedUiEventLogger;
    public final IOverlayManager mOverlayManager;
    public final ContentObserver mShortcutEnabledObserver;
    public final OneHandedState mState;
    public final ContentObserver mSwipeToNotificationEnabledObserver;
    public boolean mTaskChangeToExit;
    public final TaskStackListenerImpl mTaskStackListener;
    public final TaskStackListenerCallback mTaskStackListenerCallback;
    public final OneHandedTimeoutHandler mTimeoutHandler;
    public final OneHandedTouchHandler mTouchHandler;
    public final OneHandedTransitionCallback mTransitionCallBack;
    public final OneHandedTutorialHandler mTutorialHandler;
    public int mUserId;

    public final boolean isInitialized() {
        if (this.mDisplayAreaOrganizer != null && this.mDisplayController != null && this.mOneHandedSettingsUtil != null) {
            return true;
        }
        Slog.w("OneHandedController", "Components may not initialized yet!");
        return false;
    }

    public static OneHandedController create(Context context, WindowManager windowManager, DisplayController displayController, DisplayLayout displayLayout, TaskStackListenerImpl taskStackListenerImpl, InteractionJankMonitor interactionJankMonitor, UiEventLogger uiEventLogger, ShellExecutor shellExecutor, Handler handler) {
        Context context2 = context;
        ShellExecutor shellExecutor2 = shellExecutor;
        OneHandedSettingsUtil oneHandedSettingsUtil = new OneHandedSettingsUtil();
        OneHandedAccessibilityUtil oneHandedAccessibilityUtil = new OneHandedAccessibilityUtil(context2);
        OneHandedTimeoutHandler oneHandedTimeoutHandler = new OneHandedTimeoutHandler(shellExecutor2);
        OneHandedState oneHandedState = new OneHandedState();
        OneHandedTutorialHandler oneHandedTutorialHandler = new OneHandedTutorialHandler(context2, oneHandedSettingsUtil, windowManager, new BackgroundWindowManager(context2));
        OneHandedAnimationController oneHandedAnimationController = new OneHandedAnimationController(context2);
        OneHandedTouchHandler oneHandedTouchHandler = new OneHandedTouchHandler(oneHandedTimeoutHandler, shellExecutor2);
        Context context3 = context;
        OneHandedTutorialHandler oneHandedTutorialHandler2 = oneHandedTutorialHandler;
        return new OneHandedController(context3, displayController, new OneHandedDisplayAreaOrganizer(context3, displayLayout, oneHandedSettingsUtil, oneHandedAnimationController, oneHandedTutorialHandler2, interactionJankMonitor, shellExecutor), oneHandedTouchHandler, oneHandedTutorialHandler2, oneHandedSettingsUtil, oneHandedAccessibilityUtil, oneHandedTimeoutHandler, oneHandedState, interactionJankMonitor, new OneHandedUiEventLogger(uiEventLogger), IOverlayManager.Stub.asInterface(ServiceManager.getService("overlay")), taskStackListenerImpl, shellExecutor2, handler);
    }

    public OneHandedController(Context context, DisplayController displayController, OneHandedDisplayAreaOrganizer oneHandedDisplayAreaOrganizer, OneHandedTouchHandler oneHandedTouchHandler, OneHandedTutorialHandler oneHandedTutorialHandler, OneHandedSettingsUtil oneHandedSettingsUtil, OneHandedAccessibilityUtil oneHandedAccessibilityUtil, OneHandedTimeoutHandler oneHandedTimeoutHandler, OneHandedState oneHandedState, InteractionJankMonitor interactionJankMonitor, OneHandedUiEventLogger oneHandedUiEventLogger, IOverlayManager iOverlayManager, TaskStackListenerImpl taskStackListenerImpl, ShellExecutor shellExecutor, Handler handler) {
        OneHandedState oneHandedState2 = oneHandedState;
        AnonymousClass1 r5 = new DisplayController.OnDisplaysChangedListener() {
            public void onDisplayConfigurationChanged(int i, Configuration configuration) {
                if (i == 0 && OneHandedController.this.isInitialized()) {
                    OneHandedController.this.updateDisplayLayout(i);
                }
            }

            public void onDisplayAdded(int i) {
                if (i == 0 && OneHandedController.this.isInitialized()) {
                    OneHandedController.this.updateDisplayLayout(i);
                }
            }
        };
        this.mDisplaysChangedListener = r5;
        this.mAccessibilityStateChangeListener = new AccessibilityManager.AccessibilityStateChangeListener() {
            public void onAccessibilityStateChanged(boolean z) {
                if (OneHandedController.this.isInitialized()) {
                    if (z) {
                        OneHandedController.this.mTimeoutHandler.setTimeout(OneHandedController.this.mAccessibilityManager.getRecommendedTimeoutMillis(OneHandedController.this.mOneHandedSettingsUtil.getSettingsOneHandedModeTimeout(OneHandedController.this.mContext.getContentResolver(), OneHandedController.this.mUserId) * 1000, 4) / 1000);
                        return;
                    }
                    OneHandedController.this.mTimeoutHandler.setTimeout(OneHandedController.this.mOneHandedSettingsUtil.getSettingsOneHandedModeTimeout(OneHandedController.this.mContext.getContentResolver(), OneHandedController.this.mUserId));
                }
            }
        };
        this.mTransitionCallBack = new OneHandedTransitionCallback() {
            public void onStartFinished(Rect rect) {
                OneHandedController.this.mState.setState(2);
                OneHandedController.this.notifyShortcutStateChanged(2);
            }

            public void onStopFinished(Rect rect) {
                OneHandedController.this.mState.setState(0);
                OneHandedController.this.notifyShortcutStateChanged(0);
            }
        };
        this.mTaskStackListenerCallback = new TaskStackListenerCallback() {
            public void onTaskCreated(int i, ComponentName componentName) {
                OneHandedController.this.stopOneHanded(5);
            }

            public void onTaskMovedToFront(int i) {
                OneHandedController.this.stopOneHanded(5);
            }
        };
        this.mContext = context;
        this.mOneHandedSettingsUtil = oneHandedSettingsUtil;
        this.mOneHandedAccessibilityUtil = oneHandedAccessibilityUtil;
        this.mDisplayAreaOrganizer = oneHandedDisplayAreaOrganizer;
        this.mDisplayController = displayController;
        this.mTouchHandler = oneHandedTouchHandler;
        this.mState = oneHandedState2;
        this.mTutorialHandler = oneHandedTutorialHandler;
        this.mOverlayManager = iOverlayManager;
        this.mMainExecutor = shellExecutor;
        this.mMainHandler = handler;
        this.mOneHandedUiEventLogger = oneHandedUiEventLogger;
        this.mTaskStackListener = taskStackListenerImpl;
        displayController.addDisplayWindowListener(r5);
        int i = SystemProperties.getInt("persist.debug.one_handed_offset_percentage", Math.round(context.getResources().getFraction(R.fraction.config_one_handed_offset, 1, 1) * 100.0f));
        this.mUserId = UserHandle.myUserId();
        this.mOffSetFraction = ((float) i) / 100.0f;
        this.mIsOneHandedEnabled = oneHandedSettingsUtil.getSettingsOneHandedModeEnabled(context.getContentResolver(), this.mUserId);
        this.mIsSwipeToNotificationEnabled = oneHandedSettingsUtil.getSettingsSwipeToNotificationEnabled(context.getContentResolver(), this.mUserId);
        this.mTimeoutHandler = oneHandedTimeoutHandler;
        this.mActivatedObserver = getObserver(new OneHandedController$$ExternalSyntheticLambda0(this));
        this.mEnabledObserver = getObserver(new OneHandedController$$ExternalSyntheticLambda1(this));
        this.mSwipeToNotificationEnabledObserver = getObserver(new OneHandedController$$ExternalSyntheticLambda2(this));
        this.mShortcutEnabledObserver = getObserver(new OneHandedController$$ExternalSyntheticLambda3(this));
        displayController.addDisplayChangingController(this);
        setupCallback();
        registerSettingObservers(this.mUserId);
        setupTimeoutListener();
        updateSettings();
        updateDisplayLayout(this.mContext.getDisplayId());
        AccessibilityManager instance = AccessibilityManager.getInstance(context);
        this.mAccessibilityManager = instance;
        instance.addAccessibilityStateChangeListener(this.mAccessibilityStateChangeListener);
        oneHandedState2.addSListeners(oneHandedTutorialHandler);
    }

    public OneHanded asOneHanded() {
        return this.mImpl;
    }

    public Context getContext() {
        return this.mContext;
    }

    public ShellExecutor getRemoteCallExecutor() {
        return this.mMainExecutor;
    }

    public void setOneHandedEnabled(boolean z) {
        this.mIsOneHandedEnabled = z;
        updateOneHandedEnabled();
    }

    public void setTaskChangeToExit(boolean z) {
        if (z) {
            this.mTaskStackListener.addListener(this.mTaskStackListenerCallback);
        } else {
            this.mTaskStackListener.removeListener(this.mTaskStackListenerCallback);
        }
        this.mTaskChangeToExit = z;
    }

    public void setSwipeToNotificationEnabled(boolean z) {
        this.mIsSwipeToNotificationEnabled = z;
    }

    public void notifyShortcutStateChanged(int i) {
        if (isShortcutEnabled()) {
            this.mOneHandedSettingsUtil.setOneHandedModeActivated(this.mContext.getContentResolver(), i == 2 ? 1 : 0, this.mUserId);
        }
    }

    public void startOneHanded() {
        if (isLockedDisabled() || this.mKeyguardShowing) {
            Slog.d("OneHandedController", "Temporary lock disabled");
        } else if (!this.mDisplayAreaOrganizer.isReady()) {
            this.mMainExecutor.executeDelayed(new OneHandedController$$ExternalSyntheticLambda4(this), 10);
        } else if (!this.mState.isTransitioning() && !this.mState.isInOneHanded()) {
            if (this.mDisplayAreaOrganizer.getDisplayLayout().isLandscape()) {
                Slog.w("OneHandedController", "One handed mode only support portrait mode");
                return;
            }
            this.mState.setState(1);
            int round = Math.round(((float) this.mDisplayAreaOrganizer.getDisplayLayout().height()) * this.mOffSetFraction);
            OneHandedAccessibilityUtil oneHandedAccessibilityUtil = this.mOneHandedAccessibilityUtil;
            oneHandedAccessibilityUtil.announcementForScreenReader(oneHandedAccessibilityUtil.getOneHandedStartDescription());
            this.mDisplayAreaOrganizer.scheduleOffset(0, round);
            this.mTimeoutHandler.resetTimer();
            this.mOneHandedUiEventLogger.writeEvent(0);
        }
    }

    /* renamed from: stopOneHanded */
    public void lambda$updateOneHandedEnabled$3() {
        stopOneHanded(1);
    }

    public final void stopOneHanded(int i) {
        if (!this.mState.isTransitioning() && this.mState.getState() != 0) {
            this.mState.setState(3);
            OneHandedAccessibilityUtil oneHandedAccessibilityUtil = this.mOneHandedAccessibilityUtil;
            oneHandedAccessibilityUtil.announcementForScreenReader(oneHandedAccessibilityUtil.getOneHandedStopDescription());
            this.mDisplayAreaOrganizer.scheduleOffset(0, 0);
            this.mTimeoutHandler.removeTimer();
            this.mOneHandedUiEventLogger.writeEvent(i);
        }
    }

    public void registerEventCallback(OneHandedEventCallback oneHandedEventCallback) {
        this.mEventCallback = oneHandedEventCallback;
    }

    public void registerTransitionCallback(OneHandedTransitionCallback oneHandedTransitionCallback) {
        this.mDisplayAreaOrganizer.registerTransitionCallback(oneHandedTransitionCallback);
    }

    public final void setupCallback() {
        this.mTouchHandler.registerTouchEventListener(new OneHandedController$$ExternalSyntheticLambda8(this));
        this.mDisplayAreaOrganizer.registerTransitionCallback(this.mTouchHandler);
        this.mDisplayAreaOrganizer.registerTransitionCallback(this.mTutorialHandler);
        this.mDisplayAreaOrganizer.registerTransitionCallback(this.mTransitionCallBack);
        if (this.mTaskChangeToExit) {
            this.mTaskStackListener.addListener(this.mTaskStackListenerCallback);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setupCallback$0() {
        stopOneHanded(2);
    }

    public final void registerSettingObservers(int i) {
        this.mOneHandedSettingsUtil.registerSettingsKeyObserver("one_handed_mode_activated", this.mContext.getContentResolver(), this.mActivatedObserver, i);
        this.mOneHandedSettingsUtil.registerSettingsKeyObserver("one_handed_mode_enabled", this.mContext.getContentResolver(), this.mEnabledObserver, i);
        this.mOneHandedSettingsUtil.registerSettingsKeyObserver("swipe_bottom_to_notification_enabled", this.mContext.getContentResolver(), this.mSwipeToNotificationEnabledObserver, i);
        this.mOneHandedSettingsUtil.registerSettingsKeyObserver("accessibility_button_targets", this.mContext.getContentResolver(), this.mShortcutEnabledObserver, i);
        this.mOneHandedSettingsUtil.registerSettingsKeyObserver("accessibility_shortcut_target_service", this.mContext.getContentResolver(), this.mShortcutEnabledObserver, i);
    }

    public final void unregisterSettingObservers() {
        this.mOneHandedSettingsUtil.unregisterSettingsKeyObserver(this.mContext.getContentResolver(), this.mEnabledObserver);
        this.mOneHandedSettingsUtil.unregisterSettingsKeyObserver(this.mContext.getContentResolver(), this.mSwipeToNotificationEnabledObserver);
        this.mOneHandedSettingsUtil.unregisterSettingsKeyObserver(this.mContext.getContentResolver(), this.mShortcutEnabledObserver);
    }

    public final void updateSettings() {
        setOneHandedEnabled(this.mOneHandedSettingsUtil.getSettingsOneHandedModeEnabled(this.mContext.getContentResolver(), this.mUserId));
        this.mTimeoutHandler.setTimeout(this.mOneHandedSettingsUtil.getSettingsOneHandedModeTimeout(this.mContext.getContentResolver(), this.mUserId));
        setTaskChangeToExit(this.mOneHandedSettingsUtil.getSettingsTapsAppToExit(this.mContext.getContentResolver(), this.mUserId));
        setSwipeToNotificationEnabled(this.mOneHandedSettingsUtil.getSettingsSwipeToNotificationEnabled(this.mContext.getContentResolver(), this.mUserId));
        onShortcutEnabledChanged();
    }

    public void updateDisplayLayout(int i) {
        DisplayLayout displayLayout = this.mDisplayController.getDisplayLayout(i);
        if (displayLayout == null) {
            Slog.w("OneHandedController", "Failed to get new DisplayLayout.");
            return;
        }
        this.mDisplayAreaOrganizer.setDisplayLayout(displayLayout);
        this.mTutorialHandler.onDisplayChanged(displayLayout);
    }

    public final ContentObserver getObserver(final Runnable runnable) {
        return new ContentObserver(this.mMainHandler) {
            public void onChange(boolean z) {
                runnable.run();
            }
        };
    }

    public void notifyExpandNotification() {
        if (this.mEventCallback != null) {
            this.mMainExecutor.execute(new OneHandedController$$ExternalSyntheticLambda5(this));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyExpandNotification$1() {
        this.mEventCallback.notifyExpandNotification();
    }

    public void onActivatedActionChanged() {
        if (!isShortcutEnabled()) {
            Slog.w("OneHandedController", "Shortcut not enabled, skip onActivatedActionChanged()");
            return;
        }
        boolean z = true;
        if (!isOneHandedEnabled()) {
            boolean oneHandedModeEnabled = this.mOneHandedSettingsUtil.setOneHandedModeEnabled(this.mContext.getContentResolver(), 1, this.mUserId);
            Slog.d("OneHandedController", "Auto enabled One-handed mode by shortcut trigger, success=" + oneHandedModeEnabled);
        }
        if (isSwipeToNotificationEnabled()) {
            notifyExpandNotification();
            return;
        }
        if (this.mState.getState() != 2) {
            z = false;
        }
        boolean oneHandedModeActivated = this.mOneHandedSettingsUtil.getOneHandedModeActivated(this.mContext.getContentResolver(), this.mUserId);
        if (!(z ^ oneHandedModeActivated)) {
            return;
        }
        if (oneHandedModeActivated) {
            startOneHanded();
        } else {
            lambda$updateOneHandedEnabled$3();
        }
    }

    public void onEnabledSettingChanged() {
        boolean settingsOneHandedModeEnabled = this.mOneHandedSettingsUtil.getSettingsOneHandedModeEnabled(this.mContext.getContentResolver(), this.mUserId);
        this.mOneHandedUiEventLogger.writeEvent(settingsOneHandedModeEnabled ? 8 : 9);
        setOneHandedEnabled(settingsOneHandedModeEnabled);
    }

    public void onSwipeToNotificationEnabledChanged() {
        boolean settingsSwipeToNotificationEnabled = this.mOneHandedSettingsUtil.getSettingsSwipeToNotificationEnabled(this.mContext.getContentResolver(), this.mUserId);
        setSwipeToNotificationEnabled(settingsSwipeToNotificationEnabled);
        notifyShortcutStateChanged(this.mState.getState());
        this.mOneHandedUiEventLogger.writeEvent(settingsSwipeToNotificationEnabled ? 18 : 19);
    }

    public void onShortcutEnabledChanged() {
        boolean shortcutEnabled = this.mOneHandedSettingsUtil.getShortcutEnabled(this.mContext.getContentResolver(), this.mUserId);
        this.mIsShortcutEnabled = shortcutEnabled;
        this.mOneHandedUiEventLogger.writeEvent(shortcutEnabled ? 20 : 21);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setupTimeoutListener$2(int i) {
        stopOneHanded(6);
    }

    public final void setupTimeoutListener() {
        this.mTimeoutHandler.registerTimeoutListener(new OneHandedController$$ExternalSyntheticLambda7(this));
    }

    public boolean isLockedDisabled() {
        return this.mLockedDisabled;
    }

    public boolean isOneHandedEnabled() {
        return this.mIsOneHandedEnabled;
    }

    public boolean isShortcutEnabled() {
        return this.mIsShortcutEnabled;
    }

    public boolean isSwipeToNotificationEnabled() {
        return this.mIsSwipeToNotificationEnabled;
    }

    public final void updateOneHandedEnabled() {
        if (this.mState.getState() == 1 || this.mState.getState() == 2) {
            this.mMainExecutor.execute(new OneHandedController$$ExternalSyntheticLambda6(this));
        }
        if (isOneHandedEnabled() && !isSwipeToNotificationEnabled()) {
            notifyShortcutStateChanged(this.mState.getState());
        }
        this.mTouchHandler.onOneHandedEnabled(this.mIsOneHandedEnabled);
        if (!this.mIsOneHandedEnabled) {
            this.mDisplayAreaOrganizer.unregisterOrganizer();
        } else if (this.mDisplayAreaOrganizer.getDisplayAreaTokenMap().isEmpty()) {
            this.mDisplayAreaOrganizer.registerOrganizer(3);
        }
    }

    public void setLockedDisabled(boolean z, boolean z2) {
        boolean z3 = false;
        if (z2 != (this.mIsOneHandedEnabled || this.mIsSwipeToNotificationEnabled)) {
            if (z && !z2) {
                z3 = true;
            }
            this.mLockedDisabled = z3;
        }
    }

    public final void onConfigChanged(Configuration configuration) {
        if (this.mTutorialHandler != null && this.mIsOneHandedEnabled && configuration.orientation != 2) {
            this.mTutorialHandler.onConfigurationChanged();
        }
    }

    public final void onKeyguardVisibilityChanged(boolean z) {
        this.mKeyguardShowing = z;
    }

    public final void onUserSwitch(int i) {
        unregisterSettingObservers();
        this.mUserId = i;
        registerSettingObservers(i);
        updateSettings();
        updateOneHandedEnabled();
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println();
        printWriter.println("OneHandedController");
        printWriter.print("  mOffSetFraction=");
        printWriter.println(this.mOffSetFraction);
        printWriter.print("  mLockedDisabled=");
        printWriter.println(this.mLockedDisabled);
        printWriter.print("  mUserId=");
        printWriter.println(this.mUserId);
        printWriter.print("  isShortcutEnabled=");
        printWriter.println(isShortcutEnabled());
        printWriter.print("  mIsSwipeToNotificationEnabled=");
        printWriter.println(this.mIsSwipeToNotificationEnabled);
        OneHandedDisplayAreaOrganizer oneHandedDisplayAreaOrganizer = this.mDisplayAreaOrganizer;
        if (oneHandedDisplayAreaOrganizer != null) {
            oneHandedDisplayAreaOrganizer.dump(printWriter);
        }
        OneHandedTouchHandler oneHandedTouchHandler = this.mTouchHandler;
        if (oneHandedTouchHandler != null) {
            oneHandedTouchHandler.dump(printWriter);
        }
        OneHandedTimeoutHandler oneHandedTimeoutHandler = this.mTimeoutHandler;
        if (oneHandedTimeoutHandler != null) {
            oneHandedTimeoutHandler.dump(printWriter);
        }
        OneHandedState oneHandedState = this.mState;
        if (oneHandedState != null) {
            oneHandedState.dump(printWriter);
        }
        OneHandedTutorialHandler oneHandedTutorialHandler = this.mTutorialHandler;
        if (oneHandedTutorialHandler != null) {
            oneHandedTutorialHandler.dump(printWriter);
        }
        OneHandedAccessibilityUtil oneHandedAccessibilityUtil = this.mOneHandedAccessibilityUtil;
        if (oneHandedAccessibilityUtil != null) {
            oneHandedAccessibilityUtil.dump(printWriter);
        }
        this.mOneHandedSettingsUtil.dump(printWriter, "  ", this.mContext.getContentResolver(), this.mUserId);
    }

    public void onRotateDisplay(int i, int i2, int i3, WindowContainerTransaction windowContainerTransaction) {
        if (isInitialized() && this.mOneHandedSettingsUtil.getSettingsOneHandedModeEnabled(this.mContext.getContentResolver(), this.mUserId) && !this.mOneHandedSettingsUtil.getSettingsSwipeToNotificationEnabled(this.mContext.getContentResolver(), this.mUserId)) {
            this.mDisplayAreaOrganizer.onRotateDisplay(this.mContext, i3, windowContainerTransaction);
            this.mOneHandedUiEventLogger.writeEvent(4);
        }
    }

    public class OneHandedImpl implements OneHanded {
        public IOneHandedImpl mIOneHanded;

        public OneHandedImpl() {
        }

        public IOneHanded createExternalInterface() {
            IOneHandedImpl iOneHandedImpl = this.mIOneHanded;
            if (iOneHandedImpl != null) {
                iOneHandedImpl.invalidate();
            }
            IOneHandedImpl iOneHandedImpl2 = new IOneHandedImpl(OneHandedController.this);
            this.mIOneHanded = iOneHandedImpl2;
            return iOneHandedImpl2;
        }

        public void stopOneHanded() {
            OneHandedController.this.mMainExecutor.execute(new OneHandedController$OneHandedImpl$$ExternalSyntheticLambda4(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$stopOneHanded$1() {
            OneHandedController.this.lambda$updateOneHandedEnabled$3();
        }

        public void stopOneHanded(int i) {
            OneHandedController.this.mMainExecutor.execute(new OneHandedController$OneHandedImpl$$ExternalSyntheticLambda7(this, i));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$stopOneHanded$2(int i) {
            OneHandedController.this.stopOneHanded(i);
        }

        public void setLockedDisabled(boolean z, boolean z2) {
            OneHandedController.this.mMainExecutor.execute(new OneHandedController$OneHandedImpl$$ExternalSyntheticLambda6(this, z, z2));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$setLockedDisabled$3(boolean z, boolean z2) {
            OneHandedController.this.setLockedDisabled(z, z2);
        }

        public void registerEventCallback(OneHandedEventCallback oneHandedEventCallback) {
            OneHandedController.this.mMainExecutor.execute(new OneHandedController$OneHandedImpl$$ExternalSyntheticLambda2(this, oneHandedEventCallback));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$registerEventCallback$4(OneHandedEventCallback oneHandedEventCallback) {
            OneHandedController.this.registerEventCallback(oneHandedEventCallback);
        }

        public void registerTransitionCallback(OneHandedTransitionCallback oneHandedTransitionCallback) {
            OneHandedController.this.mMainExecutor.execute(new OneHandedController$OneHandedImpl$$ExternalSyntheticLambda3(this, oneHandedTransitionCallback));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$registerTransitionCallback$5(OneHandedTransitionCallback oneHandedTransitionCallback) {
            OneHandedController.this.registerTransitionCallback(oneHandedTransitionCallback);
        }

        public void onConfigChanged(Configuration configuration) {
            OneHandedController.this.mMainExecutor.execute(new OneHandedController$OneHandedImpl$$ExternalSyntheticLambda5(this, configuration));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onConfigChanged$6(Configuration configuration) {
            OneHandedController.this.onConfigChanged(configuration);
        }

        public void onUserSwitch(int i) {
            OneHandedController.this.mMainExecutor.execute(new OneHandedController$OneHandedImpl$$ExternalSyntheticLambda1(this, i));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onUserSwitch$7(int i) {
            OneHandedController.this.onUserSwitch(i);
        }

        public void onKeyguardVisibilityChanged(boolean z) {
            OneHandedController.this.mMainExecutor.execute(new OneHandedController$OneHandedImpl$$ExternalSyntheticLambda0(this, z));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onKeyguardVisibilityChanged$8(boolean z) {
            OneHandedController.this.onKeyguardVisibilityChanged(z);
        }
    }

    public static class IOneHandedImpl extends IOneHanded.Stub {
        public OneHandedController mController;

        public IOneHandedImpl(OneHandedController oneHandedController) {
            this.mController = oneHandedController;
        }

        public void invalidate() {
            this.mController = null;
        }

        public void startOneHanded() {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "startOneHanded", new OneHandedController$IOneHandedImpl$$ExternalSyntheticLambda0());
        }

        public void stopOneHanded() {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "stopOneHanded", new OneHandedController$IOneHandedImpl$$ExternalSyntheticLambda1());
        }
    }
}
