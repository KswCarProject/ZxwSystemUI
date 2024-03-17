package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.content.res.Resources;
import android.widget.FrameLayout;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qs.user.UserSwitchDialogController;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.phone.ScreenOffAnimationController;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class KeyguardQsUserSwitchController_Factory implements Factory<KeyguardQsUserSwitchController> {
    public final Provider<ConfigurationController> configurationControllerProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DozeParameters> dozeParametersProvider;
    public final Provider<FalsingManager> falsingManagerProvider;
    public final Provider<KeyguardStateController> keyguardStateControllerProvider;
    public final Provider<Resources> resourcesProvider;
    public final Provider<ScreenOffAnimationController> screenOffAnimationControllerProvider;
    public final Provider<SysuiStatusBarStateController> statusBarStateControllerProvider;
    public final Provider<UiEventLogger> uiEventLoggerProvider;
    public final Provider<UserSwitchDialogController> userSwitchDialogControllerProvider;
    public final Provider<UserSwitcherController> userSwitcherControllerProvider;
    public final Provider<FrameLayout> viewProvider;

    public KeyguardQsUserSwitchController_Factory(Provider<FrameLayout> provider, Provider<Context> provider2, Provider<Resources> provider3, Provider<UserSwitcherController> provider4, Provider<KeyguardStateController> provider5, Provider<FalsingManager> provider6, Provider<ConfigurationController> provider7, Provider<SysuiStatusBarStateController> provider8, Provider<DozeParameters> provider9, Provider<ScreenOffAnimationController> provider10, Provider<UserSwitchDialogController> provider11, Provider<UiEventLogger> provider12) {
        this.viewProvider = provider;
        this.contextProvider = provider2;
        this.resourcesProvider = provider3;
        this.userSwitcherControllerProvider = provider4;
        this.keyguardStateControllerProvider = provider5;
        this.falsingManagerProvider = provider6;
        this.configurationControllerProvider = provider7;
        this.statusBarStateControllerProvider = provider8;
        this.dozeParametersProvider = provider9;
        this.screenOffAnimationControllerProvider = provider10;
        this.userSwitchDialogControllerProvider = provider11;
        this.uiEventLoggerProvider = provider12;
    }

    public KeyguardQsUserSwitchController get() {
        return newInstance(this.viewProvider.get(), this.contextProvider.get(), this.resourcesProvider.get(), this.userSwitcherControllerProvider.get(), this.keyguardStateControllerProvider.get(), this.falsingManagerProvider.get(), this.configurationControllerProvider.get(), this.statusBarStateControllerProvider.get(), this.dozeParametersProvider.get(), this.screenOffAnimationControllerProvider.get(), this.userSwitchDialogControllerProvider.get(), this.uiEventLoggerProvider.get());
    }

    public static KeyguardQsUserSwitchController_Factory create(Provider<FrameLayout> provider, Provider<Context> provider2, Provider<Resources> provider3, Provider<UserSwitcherController> provider4, Provider<KeyguardStateController> provider5, Provider<FalsingManager> provider6, Provider<ConfigurationController> provider7, Provider<SysuiStatusBarStateController> provider8, Provider<DozeParameters> provider9, Provider<ScreenOffAnimationController> provider10, Provider<UserSwitchDialogController> provider11, Provider<UiEventLogger> provider12) {
        return new KeyguardQsUserSwitchController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12);
    }

    public static KeyguardQsUserSwitchController newInstance(FrameLayout frameLayout, Context context, Resources resources, UserSwitcherController userSwitcherController, KeyguardStateController keyguardStateController, FalsingManager falsingManager, ConfigurationController configurationController, SysuiStatusBarStateController sysuiStatusBarStateController, DozeParameters dozeParameters, ScreenOffAnimationController screenOffAnimationController, UserSwitchDialogController userSwitchDialogController, UiEventLogger uiEventLogger) {
        return new KeyguardQsUserSwitchController(frameLayout, context, resources, userSwitcherController, keyguardStateController, falsingManager, configurationController, sysuiStatusBarStateController, dozeParameters, screenOffAnimationController, userSwitchDialogController, uiEventLogger);
    }
}
