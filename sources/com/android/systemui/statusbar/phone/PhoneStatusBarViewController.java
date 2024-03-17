package com.android.systemui.statusbar.phone;

import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import com.android.systemui.R$id;
import com.android.systemui.shared.animation.UnfoldMoveFromCenterAnimator;
import com.android.systemui.statusbar.phone.PhoneStatusBarView;
import com.android.systemui.statusbar.phone.userswitcher.StatusBarUserSwitcherController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.unfold.SysUIUnfoldComponent;
import com.android.systemui.unfold.util.ScopedUnfoldTransitionProgressProvider;
import com.android.systemui.util.ViewController;
import com.android.systemui.util.view.ViewUtil;
import java.util.Optional;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: PhoneStatusBarViewController.kt */
public final class PhoneStatusBarViewController extends ViewController<PhoneStatusBarView> {
    @NotNull
    public final ConfigurationController configurationController;
    @NotNull
    public final PhoneStatusBarViewController$configurationListener$1 configurationListener;
    @Nullable
    public final StatusBarMoveFromCenterAnimationController moveFromCenterAnimationController;
    @Nullable
    public final ScopedUnfoldTransitionProgressProvider progressProvider;
    @NotNull
    public final StatusBarUserSwitcherController userSwitcherController;
    @NotNull
    public final ViewUtil viewUtil;

    public /* synthetic */ PhoneStatusBarViewController(PhoneStatusBarView phoneStatusBarView, ScopedUnfoldTransitionProgressProvider scopedUnfoldTransitionProgressProvider, StatusBarMoveFromCenterAnimationController statusBarMoveFromCenterAnimationController, StatusBarUserSwitcherController statusBarUserSwitcherController, ViewUtil viewUtil2, PhoneStatusBarView.TouchEventHandler touchEventHandler, ConfigurationController configurationController2, DefaultConstructorMarker defaultConstructorMarker) {
        this(phoneStatusBarView, scopedUnfoldTransitionProgressProvider, statusBarMoveFromCenterAnimationController, statusBarUserSwitcherController, viewUtil2, touchEventHandler, configurationController2);
    }

    public PhoneStatusBarViewController(PhoneStatusBarView phoneStatusBarView, ScopedUnfoldTransitionProgressProvider scopedUnfoldTransitionProgressProvider, StatusBarMoveFromCenterAnimationController statusBarMoveFromCenterAnimationController, StatusBarUserSwitcherController statusBarUserSwitcherController, ViewUtil viewUtil2, PhoneStatusBarView.TouchEventHandler touchEventHandler, ConfigurationController configurationController2) {
        super(phoneStatusBarView);
        this.progressProvider = scopedUnfoldTransitionProgressProvider;
        this.moveFromCenterAnimationController = statusBarMoveFromCenterAnimationController;
        this.userSwitcherController = statusBarUserSwitcherController;
        this.viewUtil = viewUtil2;
        this.configurationController = configurationController2;
        this.configurationListener = new PhoneStatusBarViewController$configurationListener$1(this);
        ((PhoneStatusBarView) this.mView).setTouchEventHandler(touchEventHandler);
    }

    public void onViewAttached() {
        if (this.moveFromCenterAnimationController != null) {
            ((PhoneStatusBarView) this.mView).getViewTreeObserver().addOnPreDrawListener(new PhoneStatusBarViewController$onViewAttached$1(this, new View[]{((PhoneStatusBarView) this.mView).findViewById(R$id.status_bar_left_side), (ViewGroup) ((PhoneStatusBarView) this.mView).findViewById(R$id.system_icon_area)}));
            ((PhoneStatusBarView) this.mView).addOnLayoutChangeListener(new PhoneStatusBarViewController$onViewAttached$2(this));
            ScopedUnfoldTransitionProgressProvider scopedUnfoldTransitionProgressProvider = this.progressProvider;
            if (scopedUnfoldTransitionProgressProvider != null) {
                scopedUnfoldTransitionProgressProvider.setReadyToHandleTransition(true);
            }
            this.configurationController.addCallback(this.configurationListener);
        }
    }

    public void onViewDetached() {
        ScopedUnfoldTransitionProgressProvider scopedUnfoldTransitionProgressProvider = this.progressProvider;
        if (scopedUnfoldTransitionProgressProvider != null) {
            scopedUnfoldTransitionProgressProvider.setReadyToHandleTransition(false);
        }
        StatusBarMoveFromCenterAnimationController statusBarMoveFromCenterAnimationController = this.moveFromCenterAnimationController;
        if (statusBarMoveFromCenterAnimationController != null) {
            statusBarMoveFromCenterAnimationController.onViewDetached();
        }
        this.configurationController.removeCallback(this.configurationListener);
    }

    public void onInit() {
        this.userSwitcherController.init();
    }

    public final void setImportantForAccessibility(int i) {
        ((PhoneStatusBarView) this.mView).setImportantForAccessibility(i);
    }

    public final boolean sendTouchToView(@NotNull MotionEvent motionEvent) {
        return ((PhoneStatusBarView) this.mView).dispatchTouchEvent(motionEvent);
    }

    public final boolean touchIsWithinView(float f, float f2) {
        return this.viewUtil.touchIsWithinView(this.mView, f, f2);
    }

    /* compiled from: PhoneStatusBarViewController.kt */
    public static final class StatusBarViewsCenterProvider implements UnfoldMoveFromCenterAnimator.ViewCenterProvider {
        public void getViewCenter(@NotNull View view, @NotNull Point point) {
            int id = view.getId();
            if (id == R$id.status_bar_left_side) {
                getViewEdgeCenter(view, point, true);
            } else if (id == R$id.system_icon_area) {
                getViewEdgeCenter(view, point, false);
            } else {
                UnfoldMoveFromCenterAnimator.ViewCenterProvider.DefaultImpls.getViewCenter(this, view, point);
            }
        }

        public final void getViewEdgeCenter(View view, Point point, boolean z) {
            boolean z2 = (view.getResources().getConfiguration().getLayoutDirection() == 1) ^ z;
            int[] iArr = new int[2];
            view.getLocationOnScreen(iArr);
            int i = iArr[0];
            int i2 = iArr[1];
            point.x = i + (z2 ? view.getHeight() / 2 : view.getWidth() - (view.getHeight() / 2));
            point.y = i2 + (view.getHeight() / 2);
        }
    }

    /* compiled from: PhoneStatusBarViewController.kt */
    public static final class Factory {
        @NotNull
        public final ConfigurationController configurationController;
        @NotNull
        public final Optional<ScopedUnfoldTransitionProgressProvider> progressProvider;
        @NotNull
        public final Optional<SysUIUnfoldComponent> unfoldComponent;
        @NotNull
        public final StatusBarUserSwitcherController userSwitcherController;
        @NotNull
        public final ViewUtil viewUtil;

        public Factory(@NotNull Optional<SysUIUnfoldComponent> optional, @NotNull Optional<ScopedUnfoldTransitionProgressProvider> optional2, @NotNull StatusBarUserSwitcherController statusBarUserSwitcherController, @NotNull ViewUtil viewUtil2, @NotNull ConfigurationController configurationController2) {
            this.unfoldComponent = optional;
            this.progressProvider = optional2;
            this.userSwitcherController = statusBarUserSwitcherController;
            this.viewUtil = viewUtil2;
            this.configurationController = configurationController2;
        }

        @NotNull
        public final PhoneStatusBarViewController create(@NotNull PhoneStatusBarView phoneStatusBarView, @NotNull PhoneStatusBarView.TouchEventHandler touchEventHandler) {
            ScopedUnfoldTransitionProgressProvider orElse = this.progressProvider.orElse((Object) null);
            SysUIUnfoldComponent orElse2 = this.unfoldComponent.orElse((Object) null);
            return new PhoneStatusBarViewController(phoneStatusBarView, orElse, orElse2 == null ? null : orElse2.getStatusBarMoveFromCenterAnimationController(), this.userSwitcherController, this.viewUtil, touchEventHandler, this.configurationController, (DefaultConstructorMarker) null);
        }
    }
}
