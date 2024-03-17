package com.android.systemui.statusbar.phone.userswitcher;

import android.view.View;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qs.user.UserSwitchDialogController;
import com.android.systemui.util.ViewController;
import org.jetbrains.annotations.NotNull;

/* compiled from: StatusBarUserSwitcherController.kt */
public final class StatusBarUserSwitcherControllerImpl extends ViewController<StatusBarUserSwitcherContainer> implements StatusBarUserSwitcherController {
    @NotNull
    public final ActivityStarter activityStarter;
    @NotNull
    public final FalsingManager falsingManager;
    @NotNull
    public final StatusBarUserSwitcherFeatureController featureController;
    @NotNull
    public final StatusBarUserSwitcherControllerImpl$featureFlagListener$1 featureFlagListener = new StatusBarUserSwitcherControllerImpl$featureFlagListener$1(this);
    @NotNull
    public final FeatureFlags featureFlags;
    @NotNull
    public final StatusBarUserSwitcherControllerImpl$listener$1 listener = new StatusBarUserSwitcherControllerImpl$listener$1(this);
    @NotNull
    public final StatusBarUserInfoTracker tracker;
    @NotNull
    public final UserSwitchDialogController userSwitcherDialogController;

    public StatusBarUserSwitcherControllerImpl(@NotNull StatusBarUserSwitcherContainer statusBarUserSwitcherContainer, @NotNull StatusBarUserInfoTracker statusBarUserInfoTracker, @NotNull StatusBarUserSwitcherFeatureController statusBarUserSwitcherFeatureController, @NotNull UserSwitchDialogController userSwitchDialogController, @NotNull FeatureFlags featureFlags2, @NotNull ActivityStarter activityStarter2, @NotNull FalsingManager falsingManager2) {
        super(statusBarUserSwitcherContainer);
        this.tracker = statusBarUserInfoTracker;
        this.featureController = statusBarUserSwitcherFeatureController;
        this.userSwitcherDialogController = userSwitchDialogController;
        this.featureFlags = featureFlags2;
        this.activityStarter = activityStarter2;
        this.falsingManager = falsingManager2;
    }

    public void onViewAttached() {
        this.tracker.addCallback((CurrentUserChipInfoUpdatedListener) this.listener);
        this.featureController.addCallback((OnUserSwitcherPreferenceChangeListener) this.featureFlagListener);
        ((StatusBarUserSwitcherContainer) this.mView).setOnClickListener(new StatusBarUserSwitcherControllerImpl$onViewAttached$1(this));
        updateEnabled();
    }

    public void onViewDetached() {
        this.tracker.removeCallback((CurrentUserChipInfoUpdatedListener) this.listener);
        this.featureController.removeCallback((OnUserSwitcherPreferenceChangeListener) this.featureFlagListener);
        ((StatusBarUserSwitcherContainer) this.mView).setOnClickListener((View.OnClickListener) null);
    }

    public final void updateChip() {
        ((StatusBarUserSwitcherContainer) this.mView).getText().setText(this.tracker.getCurrentUserName());
        ((StatusBarUserSwitcherContainer) this.mView).getAvatar().setImageDrawable(this.tracker.getCurrentUserAvatar());
    }

    public final void updateEnabled() {
        if (!this.featureController.isStatusBarUserSwitcherFeatureEnabled() || !this.tracker.getUserSwitcherEnabled()) {
            ((StatusBarUserSwitcherContainer) this.mView).setVisibility(8);
            return;
        }
        ((StatusBarUserSwitcherContainer) this.mView).setVisibility(0);
        updateChip();
    }
}
