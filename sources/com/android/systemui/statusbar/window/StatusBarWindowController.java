package com.android.systemui.statusbar.window;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Binder;
import android.os.RemoteException;
import android.os.Trace;
import android.view.DisplayCutout;
import android.view.IWindowManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.android.internal.policy.SystemBarUtils;
import com.android.systemui.R$id;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.animation.DelegateLaunchAnimatorController;
import com.android.systemui.fragments.FragmentHostManager;
import com.android.systemui.statusbar.phone.StatusBarContentInsetsChangedListener;
import com.android.systemui.statusbar.phone.StatusBarContentInsetsProvider;
import com.android.systemui.unfold.UnfoldTransitionProgressProvider;
import com.android.systemui.unfold.util.JankMonitorTransitionProgressListener;
import java.util.Optional;

public class StatusBarWindowController {
    public int mBarHeight = -1;
    public final StatusBarContentInsetsProvider mContentInsetsProvider;
    public final Context mContext;
    public final State mCurrentState = new State();
    public final IWindowManager mIWindowManager;
    public boolean mIsAttached;
    public final ViewGroup mLaunchAnimationContainer;
    public WindowManager.LayoutParams mLp;
    public final WindowManager.LayoutParams mLpChanged;
    public final ViewGroup mStatusBarWindowView;
    public final WindowManager mWindowManager;

    public StatusBarWindowController(Context context, StatusBarWindowView statusBarWindowView, WindowManager windowManager, IWindowManager iWindowManager, StatusBarContentInsetsProvider statusBarContentInsetsProvider, Resources resources, Optional<UnfoldTransitionProgressProvider> optional) {
        this.mContext = context;
        this.mWindowManager = windowManager;
        this.mIWindowManager = iWindowManager;
        this.mContentInsetsProvider = statusBarContentInsetsProvider;
        this.mStatusBarWindowView = statusBarWindowView;
        this.mLaunchAnimationContainer = (ViewGroup) statusBarWindowView.findViewById(R$id.status_bar_launch_animation_container);
        this.mLpChanged = new WindowManager.LayoutParams();
        if (this.mBarHeight < 0) {
            this.mBarHeight = SystemBarUtils.getStatusBarHeight(context);
        }
        optional.ifPresent(new StatusBarWindowController$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(UnfoldTransitionProgressProvider unfoldTransitionProgressProvider) {
        unfoldTransitionProgressProvider.addCallback(new JankMonitorTransitionProgressListener(new StatusBarWindowController$$ExternalSyntheticLambda1(this)));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ View lambda$new$0() {
        return this.mStatusBarWindowView;
    }

    public int getStatusBarHeight() {
        return this.mBarHeight;
    }

    public void refreshStatusBarHeight() {
        int statusBarHeight = SystemBarUtils.getStatusBarHeight(this.mContext);
        if (this.mBarHeight != statusBarHeight) {
            this.mBarHeight = statusBarHeight;
            apply(this.mCurrentState);
        }
    }

    public void attach() {
        Trace.beginSection("StatusBarWindowController.getBarLayoutParams");
        this.mLp = getBarLayoutParams(this.mContext.getDisplay().getRotation());
        Trace.endSection();
        this.mWindowManager.addView(this.mStatusBarWindowView, this.mLp);
        this.mLpChanged.copyFrom(this.mLp);
        this.mContentInsetsProvider.addCallback((StatusBarContentInsetsChangedListener) new StatusBarWindowController$$ExternalSyntheticLambda2(this));
        calculateStatusBarLocationsForAllRotations();
        this.mIsAttached = true;
        apply(this.mCurrentState);
    }

    public void addViewToWindow(View view, ViewGroup.LayoutParams layoutParams) {
        this.mStatusBarWindowView.addView(view, layoutParams);
    }

    public View getBackgroundView() {
        return this.mStatusBarWindowView.findViewById(R$id.status_bar_container);
    }

    public FragmentHostManager getFragmentHostManager() {
        return FragmentHostManager.get(this.mStatusBarWindowView);
    }

    public Optional<ActivityLaunchAnimator.Controller> wrapAnimationControllerIfInStatusBar(View view, ActivityLaunchAnimator.Controller controller) {
        if (view != this.mStatusBarWindowView) {
            return Optional.empty();
        }
        controller.setLaunchContainer(this.mLaunchAnimationContainer);
        return Optional.of(new DelegateLaunchAnimatorController(controller) {
            public void onLaunchAnimationStart(boolean z) {
                getDelegate().onLaunchAnimationStart(z);
                StatusBarWindowController.this.setLaunchAnimationRunning(true);
            }

            public void onLaunchAnimationEnd(boolean z) {
                getDelegate().onLaunchAnimationEnd(z);
                StatusBarWindowController.this.setLaunchAnimationRunning(false);
            }
        });
    }

    public final WindowManager.LayoutParams getBarLayoutParams(int i) {
        WindowManager.LayoutParams barLayoutParamsForRotation = getBarLayoutParamsForRotation(i);
        barLayoutParamsForRotation.paramsForRotation = new WindowManager.LayoutParams[4];
        for (int i2 = 0; i2 <= 3; i2++) {
            barLayoutParamsForRotation.paramsForRotation[i2] = getBarLayoutParamsForRotation(i2);
        }
        return barLayoutParamsForRotation;
    }

    public final WindowManager.LayoutParams getBarLayoutParamsForRotation(int i) {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-1, SystemBarUtils.getStatusBarHeightForRotation(this.mContext, i), 2000, -2139095032, -3);
        layoutParams.privateFlags |= 16777216;
        layoutParams.token = new Binder();
        layoutParams.gravity = 48;
        layoutParams.setFitInsetsTypes(0);
        layoutParams.setTitle("StatusBar");
        layoutParams.packageName = this.mContext.getPackageName();
        layoutParams.layoutInDisplayCutoutMode = 3;
        return layoutParams;
    }

    public final void calculateStatusBarLocationsForAllRotations() {
        DisplayCutout cutout = this.mContext.getDisplay().getCutout();
        try {
            this.mIWindowManager.updateStaticPrivacyIndicatorBounds(this.mContext.getDisplayId(), new Rect[]{this.mContentInsetsProvider.getBoundingRectForPrivacyChipForRotation(0, cutout), this.mContentInsetsProvider.getBoundingRectForPrivacyChipForRotation(1, cutout), this.mContentInsetsProvider.getBoundingRectForPrivacyChipForRotation(2, cutout), this.mContentInsetsProvider.getBoundingRectForPrivacyChipForRotation(3, cutout)});
        } catch (RemoteException unused) {
        }
    }

    public void setForceStatusBarVisible(boolean z) {
        State state = this.mCurrentState;
        state.mForceStatusBarVisible = z;
        apply(state);
    }

    public void setOngoingProcessRequiresStatusBarVisible(boolean z) {
        State state = this.mCurrentState;
        state.mOngoingProcessRequiresStatusBarVisible = z;
        apply(state);
    }

    public final void setLaunchAnimationRunning(boolean z) {
        State state = this.mCurrentState;
        if (z != state.mIsLaunchAnimationRunning) {
            state.mIsLaunchAnimationRunning = z;
            apply(state);
        }
    }

    public final void applyHeight(State state) {
        int i;
        this.mLpChanged.height = state.mIsLaunchAnimationRunning ? -1 : this.mBarHeight;
        for (int i2 = 0; i2 <= 3; i2++) {
            WindowManager.LayoutParams layoutParams = this.mLpChanged.paramsForRotation[i2];
            if (state.mIsLaunchAnimationRunning) {
                i = -1;
            } else {
                i = SystemBarUtils.getStatusBarHeightForRotation(this.mContext, i2);
            }
            layoutParams.height = i;
        }
    }

    public final void apply(State state) {
        if (this.mIsAttached) {
            applyForceStatusBarVisibleFlag(state);
            applyHeight(state);
            WindowManager.LayoutParams layoutParams = this.mLp;
            if (layoutParams != null && layoutParams.copyFrom(this.mLpChanged) != 0) {
                this.mWindowManager.updateViewLayout(this.mStatusBarWindowView, this.mLp);
            }
        }
    }

    public static class State {
        public boolean mForceStatusBarVisible;
        public boolean mIsLaunchAnimationRunning;
        public boolean mOngoingProcessRequiresStatusBarVisible;

        public State() {
        }
    }

    public final void applyForceStatusBarVisibleFlag(State state) {
        if (state.mForceStatusBarVisible || state.mIsLaunchAnimationRunning || state.mOngoingProcessRequiresStatusBarVisible) {
            this.mLpChanged.privateFlags |= 4096;
            return;
        }
        this.mLpChanged.privateFlags &= -4097;
    }
}
