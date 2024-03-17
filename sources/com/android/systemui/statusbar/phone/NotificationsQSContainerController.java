package com.android.systemui.statusbar.phone;

import android.content.res.Configuration;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.plugins.qs.QSContainerController;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.shared.system.QuickStepContract;
import com.android.systemui.util.LargeScreenUtils;
import com.android.systemui.util.ViewController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;

/* compiled from: NotificationsQSContainerController.kt */
public final class NotificationsQSContainerController extends ViewController<NotificationsQuickSettingsContainer> implements QSContainerController {
    public int bottomCutoutInsets;
    public int bottomStableInsets;
    @NotNull
    public final DelayableExecutor delayableExecutor;
    @NotNull
    public final NotificationsQSContainerController$delayedInsetSetter$1 delayedInsetSetter = new NotificationsQSContainerController$delayedInsetSetter$1(this);
    @NotNull
    public final FeatureFlags featureFlags;
    public int footerActionsOffset;
    public boolean isGestureNavigation = true;
    public boolean isQSCustomizerAnimating;
    public boolean isQSCustomizing;
    public boolean isQSDetailShowing;
    public boolean largeScreenShadeHeaderActive;
    public int largeScreenShadeHeaderHeight;
    @NotNull
    public final NavigationModeController navigationModeController;
    public int notificationsBottomMargin;
    @NotNull
    public final OverviewProxyService overviewProxyService;
    public int panelMarginHorizontal;
    public boolean qsExpanded;
    public int scrimShadeBottomMargin;
    public boolean splitShadeEnabled;
    @NotNull
    public final OverviewProxyService.OverviewProxyListener taskbarVisibilityListener = new NotificationsQSContainerController$taskbarVisibilityListener$1(this);
    public boolean taskbarVisible;
    public int topMargin;
    public final boolean useCombinedQSHeaders;

    public NotificationsQSContainerController(@NotNull NotificationsQuickSettingsContainer notificationsQuickSettingsContainer, @NotNull NavigationModeController navigationModeController2, @NotNull OverviewProxyService overviewProxyService2, @NotNull FeatureFlags featureFlags2, @NotNull DelayableExecutor delayableExecutor2) {
        super(notificationsQuickSettingsContainer);
        this.navigationModeController = navigationModeController2;
        this.overviewProxyService = overviewProxyService2;
        this.featureFlags = featureFlags2;
        this.delayableExecutor = delayableExecutor2;
        this.useCombinedQSHeaders = featureFlags2.isEnabled(Flags.COMBINED_QS_HEADERS);
    }

    public final void setQsExpanded(boolean z) {
        if (this.qsExpanded != z) {
            this.qsExpanded = z;
            ((NotificationsQuickSettingsContainer) this.mView).invalidate();
        }
    }

    public void onInit() {
        this.isGestureNavigation = QuickStepContract.isGesturalMode(this.navigationModeController.addListener(new NotificationsQSContainerController$onInit$currentMode$1(this)));
    }

    public void onViewAttached() {
        updateResources();
        this.overviewProxyService.addCallback(this.taskbarVisibilityListener);
        ((NotificationsQuickSettingsContainer) this.mView).setInsetsChangedListener(this.delayedInsetSetter);
        ((NotificationsQuickSettingsContainer) this.mView).setQSFragmentAttachedListener(new NotificationsQSContainerController$onViewAttached$1(this));
        ((NotificationsQuickSettingsContainer) this.mView).setConfigurationChangedListener(new NotificationsQSContainerController$onViewAttached$2(this));
    }

    public void onViewDetached() {
        this.overviewProxyService.removeCallback(this.taskbarVisibilityListener);
        ((NotificationsQuickSettingsContainer) this.mView).removeOnInsetsChangedListener();
        ((NotificationsQuickSettingsContainer) this.mView).removeQSFragmentAttachedListener();
        ((NotificationsQuickSettingsContainer) this.mView).setConfigurationChangedListener((Consumer<Configuration>) null);
    }

    public final void updateResources() {
        int i;
        boolean shouldUseSplitNotificationShade = LargeScreenUtils.shouldUseSplitNotificationShade(getResources());
        boolean z = true;
        boolean z2 = shouldUseSplitNotificationShade != this.splitShadeEnabled;
        this.splitShadeEnabled = shouldUseSplitNotificationShade;
        this.largeScreenShadeHeaderActive = LargeScreenUtils.shouldUseLargeScreenShadeHeader(getResources());
        this.notificationsBottomMargin = getResources().getDimensionPixelSize(R$dimen.notification_panel_margin_bottom);
        this.largeScreenShadeHeaderHeight = getResources().getDimensionPixelSize(R$dimen.large_screen_shade_header_height);
        this.panelMarginHorizontal = getResources().getDimensionPixelSize(R$dimen.notification_panel_margin_horizontal);
        if (this.largeScreenShadeHeaderActive) {
            i = this.largeScreenShadeHeaderHeight;
        } else {
            i = getResources().getDimensionPixelSize(R$dimen.notification_panel_margin_top);
        }
        this.topMargin = i;
        updateConstraints();
        boolean access$setAndReportChange = NotificationsQSContainerControllerKt.setAndReportChange(new NotificationsQSContainerController$updateResources$scrimMarginChanged$1(this), getResources().getDimensionPixelSize(R$dimen.split_shade_notifications_scrim_margin_bottom));
        boolean access$setAndReportChange2 = NotificationsQSContainerControllerKt.setAndReportChange(new NotificationsQSContainerController$updateResources$footerOffsetChanged$1(this), getResources().getDimensionPixelSize(R$dimen.qs_footer_action_inset) + getResources().getDimensionPixelSize(R$dimen.qs_footer_actions_bottom_padding));
        if (!access$setAndReportChange && !access$setAndReportChange2) {
            z = false;
        }
        if (z2 || z) {
            updateBottomSpacing();
        }
    }

    public void setCustomizerAnimating(boolean z) {
        if (this.isQSCustomizerAnimating != z) {
            this.isQSCustomizerAnimating = z;
            ((NotificationsQuickSettingsContainer) this.mView).invalidate();
        }
    }

    public void setCustomizerShowing(boolean z) {
        this.isQSCustomizing = z;
        updateBottomSpacing();
    }

    public void setDetailShowing(boolean z) {
        this.isQSDetailShowing = z;
        updateBottomSpacing();
    }

    public final void updateBottomSpacing() {
        Paddings calculateBottomSpacing = calculateBottomSpacing();
        int component1 = calculateBottomSpacing.component1();
        int component2 = calculateBottomSpacing.component2();
        int component3 = calculateBottomSpacing.component3();
        ((NotificationsQuickSettingsContainer) this.mView).setPadding(0, 0, 0, component1);
        ((NotificationsQuickSettingsContainer) this.mView).setNotificationsMarginBottom(component2);
        ((NotificationsQuickSettingsContainer) this.mView).setQSContainerPaddingBottom(component3);
    }

    /* JADX WARNING: Removed duplicated region for block: B:27:0x0040  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0049  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final com.android.systemui.statusbar.phone.Paddings calculateBottomSpacing() {
        /*
            r5 = this;
            int r0 = r5.notificationsBottomMargin
            boolean r1 = r5.splitShadeEnabled
            r2 = 0
            if (r1 == 0) goto L_0x001a
            boolean r3 = r5.isGestureNavigation
            if (r3 == 0) goto L_0x000e
            int r3 = r5.bottomCutoutInsets
            goto L_0x0036
        L_0x000e:
            boolean r3 = r5.taskbarVisible
            if (r3 == 0) goto L_0x0015
            int r3 = r5.bottomStableInsets
            goto L_0x0036
        L_0x0015:
            int r3 = r5.bottomStableInsets
        L_0x0017:
            int r0 = r0 + r3
            r3 = r2
            goto L_0x0036
        L_0x001a:
            boolean r3 = r5.isQSCustomizing
            if (r3 != 0) goto L_0x0034
            boolean r3 = r5.isQSDetailShowing
            if (r3 == 0) goto L_0x0023
            goto L_0x0034
        L_0x0023:
            boolean r3 = r5.isGestureNavigation
            if (r3 == 0) goto L_0x002a
            int r3 = r5.bottomCutoutInsets
            goto L_0x0036
        L_0x002a:
            boolean r3 = r5.taskbarVisible
            if (r3 == 0) goto L_0x0031
            int r3 = r5.bottomStableInsets
            goto L_0x0036
        L_0x0031:
            int r3 = r5.bottomStableInsets
            goto L_0x0017
        L_0x0034:
            r0 = r2
            r3 = r0
        L_0x0036:
            boolean r4 = r5.isQSCustomizing
            if (r4 != 0) goto L_0x004b
            boolean r4 = r5.isQSDetailShowing
            if (r4 != 0) goto L_0x004b
            if (r1 == 0) goto L_0x0049
            int r1 = r5.scrimShadeBottomMargin
            int r1 = r0 - r1
            int r5 = r5.footerActionsOffset
            int r2 = r1 - r5
            goto L_0x004b
        L_0x0049:
            int r2 = r5.bottomStableInsets
        L_0x004b:
            com.android.systemui.statusbar.phone.Paddings r5 = new com.android.systemui.statusbar.phone.Paddings
            r5.<init>(r3, r0, r2)
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.NotificationsQSContainerController.calculateBottomSpacing():com.android.systemui.statusbar.phone.Paddings");
    }

    public final void updateConstraints() {
        ensureAllViewsHaveIds((ViewGroup) this.mView);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone((ConstraintLayout) this.mView);
        setKeyguardStatusViewConstraints(constraintSet);
        setQsConstraints(constraintSet);
        setNotificationsConstraints(constraintSet);
        setLargeScreenShadeHeaderConstraints(constraintSet);
        ((NotificationsQuickSettingsContainer) this.mView).applyConstraints(constraintSet);
    }

    public final void setLargeScreenShadeHeaderConstraints(ConstraintSet constraintSet) {
        if (this.largeScreenShadeHeaderActive) {
            constraintSet.constrainHeight(R$id.split_shade_status_bar, this.largeScreenShadeHeaderHeight);
        } else if (this.useCombinedQSHeaders) {
            constraintSet.constrainHeight(R$id.split_shade_status_bar, -2);
        }
    }

    public final void setNotificationsConstraints(ConstraintSet constraintSet) {
        int i = 0;
        int i2 = this.splitShadeEnabled ? R$id.qs_edge_guideline : 0;
        int i3 = R$id.notification_stack_scroller;
        constraintSet.connect(i3, 6, i2, 6);
        if (!this.splitShadeEnabled) {
            i = this.panelMarginHorizontal;
        }
        constraintSet.setMargin(i3, 6, i);
        constraintSet.setMargin(i3, 7, this.panelMarginHorizontal);
        constraintSet.setMargin(i3, 3, this.topMargin);
        constraintSet.setMargin(i3, 4, this.notificationsBottomMargin);
    }

    public final void setQsConstraints(ConstraintSet constraintSet) {
        int i = 0;
        int i2 = this.splitShadeEnabled ? R$id.qs_edge_guideline : 0;
        int i3 = R$id.qs_frame;
        constraintSet.connect(i3, 7, i2, 7);
        constraintSet.setMargin(i3, 6, this.splitShadeEnabled ? 0 : this.panelMarginHorizontal);
        if (!this.splitShadeEnabled) {
            i = this.panelMarginHorizontal;
        }
        constraintSet.setMargin(i3, 7, i);
        constraintSet.setMargin(i3, 3, this.topMargin);
    }

    public final void setKeyguardStatusViewConstraints(ConstraintSet constraintSet) {
        int dimensionPixelSize = getResources().getDimensionPixelSize(R$dimen.status_view_margin_horizontal);
        int i = R$id.keyguard_status_view;
        constraintSet.setMargin(i, 6, dimensionPixelSize);
        constraintSet.setMargin(i, 7, dimensionPixelSize);
    }

    public final void ensureAllViewsHaveIds(ViewGroup viewGroup) {
        int childCount = viewGroup.getChildCount();
        int i = 0;
        while (i < childCount) {
            int i2 = i + 1;
            View childAt = viewGroup.getChildAt(i);
            if (childAt.getId() == -1) {
                childAt.setId(View.generateViewId());
            }
            i = i2;
        }
    }
}
