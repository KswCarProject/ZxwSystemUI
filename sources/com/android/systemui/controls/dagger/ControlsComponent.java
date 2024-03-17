package com.android.systemui.controls.dagger;

import android.content.Context;
import android.database.ContentObserver;
import android.provider.Settings;
import com.android.internal.widget.LockPatternUtils;
import com.android.systemui.controls.controller.ControlsController;
import com.android.systemui.controls.controller.ControlsTileResourceConfiguration;
import com.android.systemui.controls.controller.ControlsTileResourceConfigurationImpl;
import com.android.systemui.controls.management.ControlsListingController;
import com.android.systemui.controls.ui.ControlsUiController;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.settings.SecureSettings;
import dagger.Lazy;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

/* compiled from: ControlsComponent.kt */
public final class ControlsComponent {
    public boolean canShowWhileLockedSetting;
    @NotNull
    public final Context context;
    @NotNull
    public final ControlsTileResourceConfiguration controlsTileResourceConfiguration;
    public final boolean featureEnabled;
    @NotNull
    public final KeyguardStateController keyguardStateController;
    @NotNull
    public final Lazy<ControlsController> lazyControlsController;
    @NotNull
    public final Lazy<ControlsListingController> lazyControlsListingController;
    @NotNull
    public final Lazy<ControlsUiController> lazyControlsUiController;
    @NotNull
    public final LockPatternUtils lockPatternUtils;
    @NotNull
    public final Optional<ControlsTileResourceConfiguration> optionalControlsTileResourceConfiguration;
    @NotNull
    public final SecureSettings secureSettings;
    @NotNull
    public final ContentObserver showWhileLockedObserver;
    @NotNull
    public final UserTracker userTracker;

    /* compiled from: ControlsComponent.kt */
    public enum Visibility {
        AVAILABLE,
        AVAILABLE_AFTER_UNLOCK,
        UNAVAILABLE
    }

    public ControlsComponent(boolean z, @NotNull Context context2, @NotNull Lazy<ControlsController> lazy, @NotNull Lazy<ControlsUiController> lazy2, @NotNull Lazy<ControlsListingController> lazy3, @NotNull LockPatternUtils lockPatternUtils2, @NotNull KeyguardStateController keyguardStateController2, @NotNull UserTracker userTracker2, @NotNull SecureSettings secureSettings2, @NotNull Optional<ControlsTileResourceConfiguration> optional) {
        this.featureEnabled = z;
        this.context = context2;
        this.lazyControlsController = lazy;
        this.lazyControlsUiController = lazy2;
        this.lazyControlsListingController = lazy3;
        this.lockPatternUtils = lockPatternUtils2;
        this.keyguardStateController = keyguardStateController2;
        this.userTracker = userTracker2;
        this.secureSettings = secureSettings2;
        this.optionalControlsTileResourceConfiguration = optional;
        this.controlsTileResourceConfiguration = optional.orElse(new ControlsTileResourceConfigurationImpl());
        ControlsComponent$showWhileLockedObserver$1 controlsComponent$showWhileLockedObserver$1 = new ControlsComponent$showWhileLockedObserver$1(this);
        this.showWhileLockedObserver = controlsComponent$showWhileLockedObserver$1;
        if (z) {
            secureSettings2.registerContentObserverForUser(Settings.Secure.getUriFor("lockscreen_show_controls"), false, (ContentObserver) controlsComponent$showWhileLockedObserver$1, -1);
            updateShowWhileLocked();
        }
    }

    @NotNull
    public final Optional<ControlsController> getControlsController() {
        return this.featureEnabled ? Optional.of(this.lazyControlsController.get()) : Optional.empty();
    }

    @NotNull
    public final Optional<ControlsListingController> getControlsListingController() {
        if (this.featureEnabled) {
            return Optional.of(this.lazyControlsListingController.get());
        }
        return Optional.empty();
    }

    public final boolean isEnabled() {
        return this.featureEnabled;
    }

    @NotNull
    public final Visibility getVisibility() {
        if (!isEnabled()) {
            return Visibility.UNAVAILABLE;
        }
        if (this.lockPatternUtils.getStrongAuthForUser(this.userTracker.getUserHandle().getIdentifier()) == 1) {
            return Visibility.AVAILABLE_AFTER_UNLOCK;
        }
        if (this.canShowWhileLockedSetting || this.keyguardStateController.isUnlocked()) {
            return Visibility.AVAILABLE;
        }
        return Visibility.AVAILABLE_AFTER_UNLOCK;
    }

    public final void updateShowWhileLocked() {
        boolean z = false;
        if (this.secureSettings.getIntForUser("lockscreen_show_controls", 0, -2) != 0) {
            z = true;
        }
        this.canShowWhileLockedSetting = z;
    }

    public final int getTileTitleId() {
        return this.controlsTileResourceConfiguration.getTileTitleId();
    }

    public final int getTileImageId() {
        return this.controlsTileResourceConfiguration.getTileImageId();
    }
}
