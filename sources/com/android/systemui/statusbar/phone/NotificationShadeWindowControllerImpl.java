package com.android.systemui.statusbar.phone;

import android.app.IActivityManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Binder;
import android.os.Build;
import android.os.RemoteException;
import android.os.Trace;
import android.util.Log;
import android.view.Display;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.DejankUtils;
import com.android.systemui.Dumpable;
import com.android.systemui.R$integer;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.colorextraction.SysuiColorExtractor;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.keyguard.KeyguardViewMediator;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class NotificationShadeWindowControllerImpl implements NotificationShadeWindowController, Dumpable, ConfigurationController.ConfigurationListener {
    public final IActivityManager mActivityManager;
    public final AuthController mAuthController;
    public final ArrayList<WeakReference<StatusBarWindowCallback>> mCallbacks = new ArrayList<>();
    public final SysuiColorExtractor mColorExtractor;
    public final Context mContext;
    public final State mCurrentState = new State();
    public int mDeferWindowLayoutParams;
    public final DozeParameters mDozeParameters;
    public final Set<Object> mForceOpenTokens = new HashSet();
    public NotificationShadeWindowController.ForcePluginOpenListener mForcePluginOpenListener;
    public boolean mHasTopUi;
    public boolean mHasTopUiChanged;
    public final KeyguardBypassController mKeyguardBypassController;
    public final float mKeyguardMaxRefreshRate;
    public final float mKeyguardPreferredRefreshRate;
    public final KeyguardStateController mKeyguardStateController;
    public final KeyguardViewMediator mKeyguardViewMediator;
    public boolean mLastKeyguardRotationAllowed;
    public NotificationShadeWindowController.OtherwisedCollapsedListener mListener;
    public final long mLockScreenDisplayTimeout;
    public WindowManager.LayoutParams mLp;
    public final WindowManager.LayoutParams mLpChanged;
    public ViewGroup mNotificationShadeView;
    public float mScreenBrightnessDoze;
    public final ScreenOffAnimationController mScreenOffAnimationController;
    public Consumer<Integer> mScrimsVisibilityListener;
    public final StatusBarStateController.StateListener mStateListener;
    public final WindowManager mWindowManager;

    public NotificationShadeWindowControllerImpl(Context context, WindowManager windowManager, IActivityManager iActivityManager, DozeParameters dozeParameters, StatusBarStateController statusBarStateController, ConfigurationController configurationController, KeyguardViewMediator keyguardViewMediator, KeyguardBypassController keyguardBypassController, SysuiColorExtractor sysuiColorExtractor, DumpManager dumpManager, KeyguardStateController keyguardStateController, ScreenOffAnimationController screenOffAnimationController, AuthController authController) {
        AnonymousClass1 r0 = new StatusBarStateController.StateListener() {
            public void onStateChanged(int i) {
                NotificationShadeWindowControllerImpl.this.setStatusBarState(i);
            }

            public void onDozingChanged(boolean z) {
                NotificationShadeWindowControllerImpl.this.setDozing(z);
            }
        };
        this.mStateListener = r0;
        this.mContext = context;
        this.mWindowManager = windowManager;
        this.mActivityManager = iActivityManager;
        this.mDozeParameters = dozeParameters;
        this.mKeyguardStateController = keyguardStateController;
        this.mScreenBrightnessDoze = dozeParameters.getScreenBrightnessDoze();
        this.mLpChanged = new WindowManager.LayoutParams();
        this.mKeyguardViewMediator = keyguardViewMediator;
        this.mKeyguardBypassController = keyguardBypassController;
        this.mColorExtractor = sysuiColorExtractor;
        this.mScreenOffAnimationController = screenOffAnimationController;
        dumpManager.registerDumpable(getClass().getName(), this);
        this.mAuthController = authController;
        this.mLastKeyguardRotationAllowed = keyguardStateController.isKeyguardScreenRotationAllowed();
        this.mLockScreenDisplayTimeout = (long) context.getResources().getInteger(R$integer.config_lockScreenDisplayTimeout);
        ((SysuiStatusBarStateController) statusBarStateController).addCallback(r0, 1);
        configurationController.addCallback(this);
        float integer = (float) context.getResources().getInteger(R$integer.config_keyguardRefreshRate);
        float f = -1.0f;
        if (integer > -1.0f) {
            Display.Mode[] supportedModes = context.getDisplay().getSupportedModes();
            int length = supportedModes.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                Display.Mode mode = supportedModes[i];
                if (((double) Math.abs(mode.getRefreshRate() - integer)) <= 0.1d) {
                    f = mode.getRefreshRate();
                    break;
                }
                i++;
            }
        }
        this.mKeyguardPreferredRefreshRate = f;
        this.mKeyguardMaxRefreshRate = (float) context.getResources().getInteger(R$integer.config_keyguardMaxRefreshRate);
    }

    public void registerCallback(StatusBarWindowCallback statusBarWindowCallback) {
        int i = 0;
        while (i < this.mCallbacks.size()) {
            if (this.mCallbacks.get(i).get() != statusBarWindowCallback) {
                i++;
            } else {
                return;
            }
        }
        this.mCallbacks.add(new WeakReference(statusBarWindowCallback));
    }

    public void setScrimsVisibilityListener(Consumer<Integer> consumer) {
        if (consumer != null && this.mScrimsVisibilityListener != consumer) {
            this.mScrimsVisibilityListener = consumer;
        }
    }

    public void attach() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-1, -1, 2040, -2138832824, -3);
        this.mLp = layoutParams;
        layoutParams.token = new Binder();
        WindowManager.LayoutParams layoutParams2 = this.mLp;
        layoutParams2.gravity = 48;
        layoutParams2.setFitInsetsTypes(0);
        WindowManager.LayoutParams layoutParams3 = this.mLp;
        layoutParams3.softInputMode = 16;
        layoutParams3.setTitle("NotificationShade");
        this.mLp.packageName = this.mContext.getPackageName();
        WindowManager.LayoutParams layoutParams4 = this.mLp;
        layoutParams4.layoutInDisplayCutoutMode = 3;
        layoutParams4.privateFlags |= 134217728;
        layoutParams4.insetsFlags.behavior = 2;
        this.mWindowManager.addView(this.mNotificationShadeView, layoutParams4);
        this.mLpChanged.copyFrom(this.mLp);
        onThemeChanged();
        if (this.mKeyguardViewMediator.isShowingAndNotOccluded()) {
            setKeyguardShowing(true);
        }
    }

    public void setNotificationShadeView(ViewGroup viewGroup) {
        this.mNotificationShadeView = viewGroup;
    }

    public ViewGroup getNotificationShadeView() {
        return this.mNotificationShadeView;
    }

    public void setDozeScreenBrightness(int i) {
        this.mScreenBrightnessDoze = ((float) i) / 255.0f;
    }

    public final void setKeyguardDark(boolean z) {
        int systemUiVisibility = this.mNotificationShadeView.getSystemUiVisibility();
        this.mNotificationShadeView.setSystemUiVisibility(z ? systemUiVisibility | 16 | 8192 : systemUiVisibility & -17 & -8193);
    }

    public final void applyKeyguardFlags(State state) {
        boolean z = false;
        if ((!(state.mKeyguardShowing || (state.mDozing && this.mDozeParameters.getAlwaysOn())) || state.mBackdropShowing || state.mLightRevealScrimOpaque) && !this.mKeyguardViewMediator.isAnimatingBetweenKeyguardAndSurfaceBehind()) {
            this.mLpChanged.flags &= -1048577;
        } else {
            this.mLpChanged.flags |= 1048576;
        }
        if (state.mDozing) {
            this.mLpChanged.privateFlags |= 524288;
        } else {
            this.mLpChanged.privateFlags &= -524289;
        }
        if (this.mKeyguardPreferredRefreshRate > 0.0f) {
            if (state.mStatusBarState == 1 && !state.mKeyguardFadingAway && !state.mKeyguardGoingAway) {
                z = true;
            }
            if (!z || !this.mAuthController.isUdfpsEnrolled(KeyguardUpdateMonitor.getCurrentUser())) {
                this.mLpChanged.preferredMaxDisplayRefreshRate = 0.0f;
            } else {
                this.mLpChanged.preferredMaxDisplayRefreshRate = this.mKeyguardPreferredRefreshRate;
            }
            Trace.setCounter("display_set_preferred_refresh_rate", (long) this.mKeyguardPreferredRefreshRate);
        } else if (this.mKeyguardMaxRefreshRate > 0.0f) {
            if (this.mKeyguardBypassController.getBypassEnabled() && state.mStatusBarState == 1 && !state.mKeyguardFadingAway && !state.mKeyguardGoingAway) {
                z = true;
            }
            if (state.mDozing || z) {
                this.mLpChanged.preferredMaxDisplayRefreshRate = this.mKeyguardMaxRefreshRate;
            } else {
                this.mLpChanged.preferredMaxDisplayRefreshRate = 0.0f;
            }
            Trace.setCounter("display_max_refresh_rate", (long) this.mLpChanged.preferredMaxDisplayRefreshRate);
        }
        if (!state.mBouncerShowing || isDebuggable()) {
            this.mLpChanged.flags &= -8193;
            return;
        }
        this.mLpChanged.flags |= 8192;
    }

    public boolean isDebuggable() {
        return Build.IS_DEBUGGABLE;
    }

    public final void adjustScreenOrientation(State state) {
        if (!state.mBouncerShowing && !state.isKeyguardShowingAndNotOccluded() && !state.mDozing) {
            this.mLpChanged.screenOrientation = -1;
        } else if (this.mKeyguardStateController.isKeyguardScreenRotationAllowed()) {
            this.mLpChanged.screenOrientation = 2;
        } else {
            this.mLpChanged.screenOrientation = 5;
        }
    }

    public final void applyFocusableFlag(State state) {
        boolean z = state.mNotificationShadeFocusable && state.mPanelExpanded;
        if ((state.mBouncerShowing && (state.mKeyguardOccluded || state.mKeyguardNeedsInput)) || ((NotificationRemoteInputManager.ENABLE_REMOTE_INPUT && state.mRemoteInputActive) || this.mScreenOffAnimationController.shouldIgnoreKeyguardTouches())) {
            WindowManager.LayoutParams layoutParams = this.mLpChanged;
            layoutParams.flags = layoutParams.flags & -9 & -131073;
        } else if (state.isKeyguardShowingAndNotOccluded() || z) {
            this.mLpChanged.flags &= -9;
            if (!state.mKeyguardNeedsInput || !state.isKeyguardShowingAndNotOccluded()) {
                this.mLpChanged.flags |= 131072;
            } else {
                this.mLpChanged.flags &= -131073;
            }
        } else {
            WindowManager.LayoutParams layoutParams2 = this.mLpChanged;
            layoutParams2.flags = (layoutParams2.flags | 8) & -131073;
        }
        this.mLpChanged.softInputMode = 16;
    }

    public final void applyForceShowNavigationFlag(State state) {
        if (state.mPanelExpanded || state.mBouncerShowing || (NotificationRemoteInputManager.ENABLE_REMOTE_INPUT && state.mRemoteInputActive)) {
            this.mLpChanged.privateFlags |= 8388608;
            return;
        }
        this.mLpChanged.privateFlags &= -8388609;
    }

    public final void applyVisibility(State state) {
        boolean isExpanded = isExpanded(state);
        if (state.mForcePluginOpen) {
            NotificationShadeWindowController.OtherwisedCollapsedListener otherwisedCollapsedListener = this.mListener;
            if (otherwisedCollapsedListener != null) {
                otherwisedCollapsedListener.setWouldOtherwiseCollapse(isExpanded);
            }
            isExpanded = true;
        }
        ViewGroup viewGroup = this.mNotificationShadeView;
        if (viewGroup == null) {
            return;
        }
        if (isExpanded) {
            viewGroup.setVisibility(0);
        } else {
            viewGroup.setVisibility(4);
        }
    }

    public final boolean isExpanded(State state) {
        return (!state.mForceCollapsed && (state.isKeyguardShowingAndNotOccluded() || state.mPanelVisible || state.mKeyguardFadingAway || state.mBouncerShowing || state.mHeadsUpShowing || state.mScrimsVisibility != 0)) || state.mBackgroundBlurRadius > 0 || state.mLaunchingActivity;
    }

    public final void applyFitsSystemWindows(State state) {
        boolean z = !state.isKeyguardShowingAndNotOccluded();
        ViewGroup viewGroup = this.mNotificationShadeView;
        if (viewGroup != null && viewGroup.getFitsSystemWindows() != z) {
            this.mNotificationShadeView.setFitsSystemWindows(z);
            this.mNotificationShadeView.requestApplyInsets();
        }
    }

    public final void applyUserActivityTimeout(State state) {
        long j;
        if (!state.isKeyguardShowingAndNotOccluded() || state.mStatusBarState != 1 || state.mQsExpanded) {
            this.mLpChanged.userActivityTimeout = -1;
            return;
        }
        WindowManager.LayoutParams layoutParams = this.mLpChanged;
        if (state.mBouncerShowing) {
            j = 10000;
        } else {
            j = this.mLockScreenDisplayTimeout;
        }
        layoutParams.userActivityTimeout = j;
    }

    public final void applyInputFeatures(State state) {
        if (!state.isKeyguardShowingAndNotOccluded() || state.mStatusBarState != 1 || state.mQsExpanded || state.mForceUserActivity) {
            this.mLpChanged.inputFeatures &= -3;
            return;
        }
        this.mLpChanged.inputFeatures |= 2;
    }

    public final void applyStatusBarColorSpaceAgnosticFlag(State state) {
        if (!isExpanded(state)) {
            this.mLpChanged.privateFlags |= 16777216;
            return;
        }
        this.mLpChanged.privateFlags &= -16777217;
    }

    public final void applyWindowLayoutParams() {
        WindowManager.LayoutParams layoutParams;
        if (this.mDeferWindowLayoutParams == 0 && (layoutParams = this.mLp) != null && layoutParams.copyFrom(this.mLpChanged) != 0) {
            Trace.beginSection("updateViewLayout");
            this.mWindowManager.updateViewLayout(this.mNotificationShadeView, this.mLp);
            Trace.endSection();
        }
    }

    public void batchApplyWindowLayoutParams(Runnable runnable) {
        this.mDeferWindowLayoutParams++;
        runnable.run();
        this.mDeferWindowLayoutParams--;
        applyWindowLayoutParams();
    }

    public final void apply(State state) {
        applyKeyguardFlags(state);
        applyFocusableFlag(state);
        applyForceShowNavigationFlag(state);
        adjustScreenOrientation(state);
        applyVisibility(state);
        applyUserActivityTimeout(state);
        applyInputFeatures(state);
        applyFitsSystemWindows(state);
        applyModalFlag(state);
        applyBrightness(state);
        applyHasTopUi(state);
        applyNotTouchable(state);
        applyStatusBarColorSpaceAgnosticFlag(state);
        applyWindowLayoutParams();
        if (this.mHasTopUi != this.mHasTopUiChanged) {
            DejankUtils.whitelistIpcs((Runnable) new NotificationShadeWindowControllerImpl$$ExternalSyntheticLambda0(this));
        }
        notifyStateChangedCallbacks();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$apply$0() {
        try {
            this.mActivityManager.setHasTopUi(this.mHasTopUiChanged);
        } catch (RemoteException e) {
            Log.e("NotificationShadeWindowController", "Failed to call setHasTopUi", e);
        }
        this.mHasTopUi = this.mHasTopUiChanged;
    }

    public void notifyStateChangedCallbacks() {
        for (StatusBarWindowCallback onStateChanged : (List) this.mCallbacks.stream().map(new NotificationShadeWindowControllerImpl$$ExternalSyntheticLambda1()).filter(new NotificationShadeWindowControllerImpl$$ExternalSyntheticLambda2()).collect(Collectors.toList())) {
            State state = this.mCurrentState;
            onStateChanged.onStateChanged(state.mKeyguardShowing, state.mKeyguardOccluded, state.mBouncerShowing, state.mDozing);
        }
    }

    public final void applyModalFlag(State state) {
        if (state.mHeadsUpShowing) {
            this.mLpChanged.flags |= 32;
            return;
        }
        this.mLpChanged.flags &= -33;
    }

    public final void applyBrightness(State state) {
        if (state.mForceDozeBrightness) {
            this.mLpChanged.screenBrightness = this.mScreenBrightnessDoze;
            return;
        }
        this.mLpChanged.screenBrightness = -1.0f;
    }

    public final void applyHasTopUi(State state) {
        this.mHasTopUiChanged = !state.mComponentsForcingTopUi.isEmpty() || isExpanded(state);
    }

    public final void applyNotTouchable(State state) {
        if (state.mNotTouchable) {
            this.mLpChanged.flags |= 16;
            return;
        }
        this.mLpChanged.flags &= -17;
    }

    public void setKeyguardShowing(boolean z) {
        State state = this.mCurrentState;
        state.mKeyguardShowing = z;
        apply(state);
    }

    public void setKeyguardOccluded(boolean z) {
        State state = this.mCurrentState;
        state.mKeyguardOccluded = z;
        apply(state);
    }

    public void setKeyguardNeedsInput(boolean z) {
        State state = this.mCurrentState;
        state.mKeyguardNeedsInput = z;
        apply(state);
    }

    public void setPanelVisible(boolean z) {
        State state = this.mCurrentState;
        if (state.mPanelVisible != z || state.mNotificationShadeFocusable != z) {
            state.mPanelVisible = z;
            state.mNotificationShadeFocusable = z;
            apply(state);
        }
    }

    public void setNotificationShadeFocusable(boolean z) {
        State state = this.mCurrentState;
        state.mNotificationShadeFocusable = z;
        apply(state);
    }

    public void setBouncerShowing(boolean z) {
        State state = this.mCurrentState;
        state.mBouncerShowing = z;
        apply(state);
    }

    public void setBackdropShowing(boolean z) {
        State state = this.mCurrentState;
        state.mBackdropShowing = z;
        apply(state);
    }

    public void setKeyguardFadingAway(boolean z) {
        State state = this.mCurrentState;
        state.mKeyguardFadingAway = z;
        apply(state);
    }

    public void setQsExpanded(boolean z) {
        State state = this.mCurrentState;
        state.mQsExpanded = z;
        apply(state);
    }

    public void setLaunchingActivity(boolean z) {
        State state = this.mCurrentState;
        state.mLaunchingActivity = z;
        apply(state);
    }

    public boolean isLaunchingActivity() {
        return this.mCurrentState.mLaunchingActivity;
    }

    public void setScrimsVisibility(int i) {
        State state = this.mCurrentState;
        if (i != state.mScrimsVisibility) {
            boolean isExpanded = isExpanded(state);
            State state2 = this.mCurrentState;
            state2.mScrimsVisibility = i;
            if (isExpanded != isExpanded(state2)) {
                apply(this.mCurrentState);
            }
            this.mScrimsVisibilityListener.accept(Integer.valueOf(i));
        }
    }

    public void setBackgroundBlurRadius(int i) {
        State state = this.mCurrentState;
        if (state.mBackgroundBlurRadius != i) {
            state.mBackgroundBlurRadius = i;
            apply(state);
        }
    }

    public void setHeadsUpShowing(boolean z) {
        State state = this.mCurrentState;
        state.mHeadsUpShowing = z;
        apply(state);
    }

    public void setLightRevealScrimOpaque(boolean z) {
        State state = this.mCurrentState;
        if (state.mLightRevealScrimOpaque != z) {
            state.mLightRevealScrimOpaque = z;
            apply(state);
        }
    }

    public void setWallpaperSupportsAmbientMode(boolean z) {
        State state = this.mCurrentState;
        state.mWallpaperSupportsAmbientMode = z;
        apply(state);
    }

    public final void setStatusBarState(int i) {
        State state = this.mCurrentState;
        state.mStatusBarState = i;
        apply(state);
    }

    public void setForceWindowCollapsed(boolean z) {
        State state = this.mCurrentState;
        state.mForceCollapsed = z;
        apply(state);
    }

    public void setPanelExpanded(boolean z) {
        State state = this.mCurrentState;
        if (state.mPanelExpanded != z) {
            state.mPanelExpanded = z;
            apply(state);
        }
    }

    public void onRemoteInputActive(boolean z) {
        State state = this.mCurrentState;
        state.mRemoteInputActive = z;
        apply(state);
    }

    public void setForceDozeBrightness(boolean z) {
        State state = this.mCurrentState;
        if (state.mForceDozeBrightness != z) {
            state.mForceDozeBrightness = z;
            apply(state);
        }
    }

    public void setDozing(boolean z) {
        State state = this.mCurrentState;
        state.mDozing = z;
        apply(state);
    }

    public void setForcePluginOpen(boolean z, Object obj) {
        if (z) {
            this.mForceOpenTokens.add(obj);
        } else {
            this.mForceOpenTokens.remove(obj);
        }
        State state = this.mCurrentState;
        boolean z2 = state.mForcePluginOpen;
        state.mForcePluginOpen = !this.mForceOpenTokens.isEmpty();
        State state2 = this.mCurrentState;
        if (z2 != state2.mForcePluginOpen) {
            apply(state2);
            NotificationShadeWindowController.ForcePluginOpenListener forcePluginOpenListener = this.mForcePluginOpenListener;
            if (forcePluginOpenListener != null) {
                forcePluginOpenListener.onChange(this.mCurrentState.mForcePluginOpen);
            }
        }
    }

    public boolean getForcePluginOpen() {
        return this.mCurrentState.mForcePluginOpen;
    }

    public void setNotTouchable(boolean z) {
        State state = this.mCurrentState;
        state.mNotTouchable = z;
        apply(state);
    }

    public void setStateListener(NotificationShadeWindowController.OtherwisedCollapsedListener otherwisedCollapsedListener) {
        this.mListener = otherwisedCollapsedListener;
    }

    public void setForcePluginOpenListener(NotificationShadeWindowController.ForcePluginOpenListener forcePluginOpenListener) {
        this.mForcePluginOpenListener = forcePluginOpenListener;
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("NotificationShadeWindowController:");
        printWriter.println("  mKeyguardMaxRefreshRate=" + this.mKeyguardMaxRefreshRate);
        printWriter.println("  mKeyguardPreferredRefreshRate=" + this.mKeyguardPreferredRefreshRate);
        printWriter.println("  mDeferWindowLayoutParams=" + this.mDeferWindowLayoutParams);
        printWriter.println(this.mCurrentState);
        ViewGroup viewGroup = this.mNotificationShadeView;
        if (viewGroup != null && viewGroup.getViewRootImpl() != null) {
            this.mNotificationShadeView.getViewRootImpl().dump("  ", printWriter);
        }
    }

    public boolean isShowingWallpaper() {
        return !this.mCurrentState.mBackdropShowing;
    }

    public void onThemeChanged() {
        if (this.mNotificationShadeView != null) {
            setKeyguardDark(this.mColorExtractor.getNeutralColors().supportsDarkText());
        }
    }

    public void onConfigChanged(Configuration configuration) {
        boolean isKeyguardScreenRotationAllowed = this.mKeyguardStateController.isKeyguardScreenRotationAllowed();
        if (this.mLastKeyguardRotationAllowed != isKeyguardScreenRotationAllowed) {
            apply(this.mCurrentState);
            this.mLastKeyguardRotationAllowed = isKeyguardScreenRotationAllowed;
        }
    }

    public void setKeyguardGoingAway(boolean z) {
        State state = this.mCurrentState;
        state.mKeyguardGoingAway = z;
        apply(state);
    }

    public void setRequestTopUi(boolean z, String str) {
        if (z) {
            this.mCurrentState.mComponentsForcingTopUi.add(str);
        } else {
            this.mCurrentState.mComponentsForcingTopUi.remove(str);
        }
        apply(this.mCurrentState);
    }

    public static class State {
        public boolean mBackdropShowing;
        public int mBackgroundBlurRadius;
        public boolean mBouncerShowing;
        public Set<String> mComponentsForcingTopUi;
        public boolean mDozing;
        public boolean mForceCollapsed;
        public boolean mForceDozeBrightness;
        public boolean mForcePluginOpen;
        public boolean mForceUserActivity;
        public boolean mHeadsUpShowing;
        public boolean mKeyguardFadingAway;
        public boolean mKeyguardGoingAway;
        public boolean mKeyguardNeedsInput;
        public boolean mKeyguardOccluded;
        public boolean mKeyguardShowing;
        public boolean mLaunchingActivity;
        public boolean mLightRevealScrimOpaque;
        public boolean mNotTouchable;
        public boolean mNotificationShadeFocusable;
        public boolean mPanelExpanded;
        public boolean mPanelVisible;
        public boolean mQsExpanded;
        public boolean mRemoteInputActive;
        public int mScrimsVisibility;
        public int mStatusBarState;
        public boolean mWallpaperSupportsAmbientMode;

        public State() {
            this.mComponentsForcingTopUi = new HashSet();
        }

        public final boolean isKeyguardShowingAndNotOccluded() {
            return this.mKeyguardShowing && !this.mKeyguardOccluded;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Window State {");
            sb.append("\n");
            for (Field field : getClass().getDeclaredFields()) {
                sb.append("  ");
                try {
                    sb.append(field.getName());
                    sb.append(": ");
                    sb.append(field.get(this));
                } catch (IllegalAccessException unused) {
                }
                sb.append("\n");
            }
            sb.append("}");
            return sb.toString();
        }
    }
}
