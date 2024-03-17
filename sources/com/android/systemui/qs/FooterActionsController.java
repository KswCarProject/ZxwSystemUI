package com.android.systemui.qs;

import android.content.Intent;
import android.os.Handler;
import android.os.UserManager;
import android.view.View;
import android.view.ViewGroup;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.globalactions.GlobalActionsDialogLite;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qs.TouchAnimator;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.phone.MultiUserSwitchController;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.statusbar.policy.UserInfoController;
import com.android.systemui.util.ViewController;
import com.android.systemui.util.settings.GlobalSettings;
import javax.inject.Provider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: FooterActionsController.kt */
public final class FooterActionsController extends ViewController<FooterActionsView> {
    @NotNull
    public final ActivityStarter activityStarter;
    public final TouchAnimator alphaAnimator = new TouchAnimator.Builder().addFloat(this.mView, "alpha", 0.0f, 1.0f).setStartDelay(0.9f).build();
    @NotNull
    public final DeviceProvisionedController deviceProvisionedController;
    @NotNull
    public final FalsingManager falsingManager;
    @NotNull
    public final QSFgsManagerFooter fgsManagerFooterController;
    @Nullable
    public GlobalActionsDialogLite globalActionsDialog;
    @NotNull
    public final Provider<GlobalActionsDialogLite> globalActionsDialogProvider;
    @NotNull
    public final GlobalSettings globalSetting;
    @NotNull
    public final Handler handler;
    public float lastExpansion = -1.0f;
    public boolean listening;
    @NotNull
    public final MetricsLogger metricsLogger;
    @NotNull
    public final FooterActionsController$multiUserSetting$1 multiUserSetting;
    public final MultiUserSwitchController multiUserSwitchController;
    @NotNull
    public final View.OnClickListener onClickListener;
    @NotNull
    public final UserInfoController.OnUserInfoChangedListener onUserInfoChangedListener;
    @NotNull
    public final View powerMenuLite;
    @NotNull
    public final QSSecurityFooter securityFooterController;
    @Nullable
    public final ViewGroup securityFootersContainer;
    @NotNull
    public final View securityFootersSeparator;
    @NotNull
    public final View settingsButtonContainer;
    public final boolean showPMLiteButton;
    @NotNull
    public final UiEventLogger uiEventLogger;
    @NotNull
    public final UserInfoController userInfoController;
    @NotNull
    public final UserManager userManager;
    @NotNull
    public final UserTracker userTracker;
    public boolean visible = true;

    public static /* synthetic */ void getSecurityFootersSeparator$frameworks__base__packages__SystemUI__android_common__SystemUI_core$annotations() {
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public FooterActionsController(@NotNull FooterActionsView footerActionsView, @NotNull MultiUserSwitchController.Factory factory, @NotNull ActivityStarter activityStarter2, @NotNull UserManager userManager2, @NotNull UserTracker userTracker2, @NotNull UserInfoController userInfoController2, @NotNull DeviceProvisionedController deviceProvisionedController2, @NotNull QSSecurityFooter qSSecurityFooter, @NotNull QSFgsManagerFooter qSFgsManagerFooter, @NotNull FalsingManager falsingManager2, @NotNull MetricsLogger metricsLogger2, @NotNull Provider<GlobalActionsDialogLite> provider, @NotNull UiEventLogger uiEventLogger2, boolean z, @NotNull GlobalSettings globalSettings, @NotNull Handler handler2) {
        super(footerActionsView);
        FooterActionsView footerActionsView2 = footerActionsView;
        GlobalSettings globalSettings2 = globalSettings;
        Handler handler3 = handler2;
        this.activityStarter = activityStarter2;
        this.userManager = userManager2;
        this.userTracker = userTracker2;
        this.userInfoController = userInfoController2;
        this.deviceProvisionedController = deviceProvisionedController2;
        this.securityFooterController = qSSecurityFooter;
        this.fgsManagerFooterController = qSFgsManagerFooter;
        this.falsingManager = falsingManager2;
        this.metricsLogger = metricsLogger2;
        this.globalActionsDialogProvider = provider;
        this.uiEventLogger = uiEventLogger2;
        this.showPMLiteButton = z;
        this.globalSetting = globalSettings2;
        this.handler = handler3;
        this.settingsButtonContainer = footerActionsView.findViewById(R$id.settings_button_container);
        this.securityFootersContainer = (ViewGroup) footerActionsView.findViewById(R$id.security_footers_container);
        this.powerMenuLite = footerActionsView.findViewById(R$id.pm_lite);
        MultiUserSwitchController.Factory factory2 = factory;
        this.multiUserSwitchController = factory.create(footerActionsView);
        View view = new View(getContext());
        view.setVisibility(8);
        this.securityFootersSeparator = view;
        this.onUserInfoChangedListener = new FooterActionsController$onUserInfoChangedListener$1(this);
        this.multiUserSetting = new FooterActionsController$multiUserSetting$1(this, globalSettings2, handler3, userTracker2.getUserId());
        this.onClickListener = new FooterActionsController$onClickListener$1(this);
    }

    public final boolean getVisible() {
        return this.visible;
    }

    public final void setVisible(boolean z) {
        this.visible = z;
        updateVisibility();
    }

    @NotNull
    public final View getSecurityFootersSeparator$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
        return this.securityFootersSeparator;
    }

    public void onInit() {
        this.multiUserSwitchController.init();
        this.securityFooterController.init();
        this.fgsManagerFooterController.init();
    }

    public final void updateVisibility() {
        int visibility = ((FooterActionsView) this.mView).getVisibility();
        ((FooterActionsView) this.mView).setVisibility(this.visible ? 0 : 4);
        if (visibility != ((FooterActionsView) this.mView).getVisibility()) {
            updateView();
        }
    }

    public final void startSettingsActivity() {
        ActivityLaunchAnimator.Controller controller;
        View view = this.settingsButtonContainer;
        if (view == null) {
            controller = null;
        } else {
            controller = ActivityLaunchAnimator.Controller.Companion.fromView(view, 33);
        }
        this.activityStarter.startActivity(new Intent("android.settings.SETTINGS"), true, controller);
    }

    public void onViewAttached() {
        this.globalActionsDialog = this.globalActionsDialogProvider.get();
        if (this.showPMLiteButton) {
            this.powerMenuLite.setVisibility(0);
            this.powerMenuLite.setOnClickListener(this.onClickListener);
        } else {
            this.powerMenuLite.setVisibility(8);
        }
        this.settingsButtonContainer.setOnClickListener(this.onClickListener);
        this.multiUserSetting.setListening(true);
        View view = this.securityFooterController.getView();
        ViewGroup viewGroup = this.securityFootersContainer;
        if (viewGroup != null) {
            viewGroup.addView(view);
        }
        int dimensionPixelSize = getResources().getDimensionPixelSize(R$dimen.qs_footer_action_inset);
        ViewGroup viewGroup2 = this.securityFootersContainer;
        if (viewGroup2 != null) {
            viewGroup2.addView(this.securityFootersSeparator, dimensionPixelSize, 1);
        }
        View view2 = this.fgsManagerFooterController.getView();
        ViewGroup viewGroup3 = this.securityFootersContainer;
        if (viewGroup3 != null) {
            viewGroup3.addView(view2);
        }
        FooterActionsController$onViewAttached$visibilityListener$1 footerActionsController$onViewAttached$visibilityListener$1 = new FooterActionsController$onViewAttached$visibilityListener$1(view, view2, this);
        this.securityFooterController.setOnVisibilityChangedListener(footerActionsController$onViewAttached$visibilityListener$1);
        this.fgsManagerFooterController.setOnVisibilityChangedListener(footerActionsController$onViewAttached$visibilityListener$1);
        updateView();
    }

    public final void updateView() {
        ((FooterActionsView) this.mView).updateEverything(this.multiUserSwitchController.isMultiUserEnabled());
    }

    public void onViewDetached() {
        GlobalActionsDialogLite globalActionsDialogLite = this.globalActionsDialog;
        if (globalActionsDialogLite != null) {
            globalActionsDialogLite.destroy();
        }
        this.globalActionsDialog = null;
        setListening(false);
        this.multiUserSetting.setListening(false);
    }

    public final void setListening(boolean z) {
        if (this.listening != z) {
            this.listening = z;
            if (z) {
                this.userInfoController.addCallback(this.onUserInfoChangedListener);
                updateView();
            } else {
                this.userInfoController.removeCallback(this.onUserInfoChangedListener);
            }
            this.fgsManagerFooterController.setListening(z);
            this.securityFooterController.setListening(z);
        }
    }

    public final void disable(int i) {
        ((FooterActionsView) this.mView).disable(i, this.multiUserSwitchController.isMultiUserEnabled());
    }

    public final void setExpansion(float f) {
        this.alphaAnimator.setPosition(f);
    }

    public final void setKeyguardShowing(boolean z) {
        setExpansion(this.lastExpansion);
    }
}
