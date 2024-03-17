package com.android.systemui.statusbar.phone;

import android.app.IActivityManager;
import android.content.Context;
import android.view.WindowManager;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.colorextraction.SysuiColorExtractor;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.keyguard.KeyguardViewMediator;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class NotificationShadeWindowControllerImpl_Factory implements Factory<NotificationShadeWindowControllerImpl> {
    public final Provider<IActivityManager> activityManagerProvider;
    public final Provider<AuthController> authControllerProvider;
    public final Provider<SysuiColorExtractor> colorExtractorProvider;
    public final Provider<ConfigurationController> configurationControllerProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DozeParameters> dozeParametersProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<KeyguardBypassController> keyguardBypassControllerProvider;
    public final Provider<KeyguardStateController> keyguardStateControllerProvider;
    public final Provider<KeyguardViewMediator> keyguardViewMediatorProvider;
    public final Provider<ScreenOffAnimationController> screenOffAnimationControllerProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;
    public final Provider<WindowManager> windowManagerProvider;

    public NotificationShadeWindowControllerImpl_Factory(Provider<Context> provider, Provider<WindowManager> provider2, Provider<IActivityManager> provider3, Provider<DozeParameters> provider4, Provider<StatusBarStateController> provider5, Provider<ConfigurationController> provider6, Provider<KeyguardViewMediator> provider7, Provider<KeyguardBypassController> provider8, Provider<SysuiColorExtractor> provider9, Provider<DumpManager> provider10, Provider<KeyguardStateController> provider11, Provider<ScreenOffAnimationController> provider12, Provider<AuthController> provider13) {
        this.contextProvider = provider;
        this.windowManagerProvider = provider2;
        this.activityManagerProvider = provider3;
        this.dozeParametersProvider = provider4;
        this.statusBarStateControllerProvider = provider5;
        this.configurationControllerProvider = provider6;
        this.keyguardViewMediatorProvider = provider7;
        this.keyguardBypassControllerProvider = provider8;
        this.colorExtractorProvider = provider9;
        this.dumpManagerProvider = provider10;
        this.keyguardStateControllerProvider = provider11;
        this.screenOffAnimationControllerProvider = provider12;
        this.authControllerProvider = provider13;
    }

    public NotificationShadeWindowControllerImpl get() {
        return newInstance(this.contextProvider.get(), this.windowManagerProvider.get(), this.activityManagerProvider.get(), this.dozeParametersProvider.get(), this.statusBarStateControllerProvider.get(), this.configurationControllerProvider.get(), this.keyguardViewMediatorProvider.get(), this.keyguardBypassControllerProvider.get(), this.colorExtractorProvider.get(), this.dumpManagerProvider.get(), this.keyguardStateControllerProvider.get(), this.screenOffAnimationControllerProvider.get(), this.authControllerProvider.get());
    }

    public static NotificationShadeWindowControllerImpl_Factory create(Provider<Context> provider, Provider<WindowManager> provider2, Provider<IActivityManager> provider3, Provider<DozeParameters> provider4, Provider<StatusBarStateController> provider5, Provider<ConfigurationController> provider6, Provider<KeyguardViewMediator> provider7, Provider<KeyguardBypassController> provider8, Provider<SysuiColorExtractor> provider9, Provider<DumpManager> provider10, Provider<KeyguardStateController> provider11, Provider<ScreenOffAnimationController> provider12, Provider<AuthController> provider13) {
        return new NotificationShadeWindowControllerImpl_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13);
    }

    public static NotificationShadeWindowControllerImpl newInstance(Context context, WindowManager windowManager, IActivityManager iActivityManager, DozeParameters dozeParameters, StatusBarStateController statusBarStateController, ConfigurationController configurationController, KeyguardViewMediator keyguardViewMediator, KeyguardBypassController keyguardBypassController, SysuiColorExtractor sysuiColorExtractor, DumpManager dumpManager, KeyguardStateController keyguardStateController, ScreenOffAnimationController screenOffAnimationController, AuthController authController) {
        return new NotificationShadeWindowControllerImpl(context, windowManager, iActivityManager, dozeParameters, statusBarStateController, configurationController, keyguardViewMediator, keyguardBypassController, sysuiColorExtractor, dumpManager, keyguardStateController, screenOffAnimationController, authController);
    }
}
