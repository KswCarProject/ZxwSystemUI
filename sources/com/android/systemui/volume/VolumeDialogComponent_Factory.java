package com.android.systemui.volume;

import android.content.Context;
import com.android.systemui.demomode.DemoModeController;
import com.android.systemui.keyguard.KeyguardViewMediator;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.PluginDependencyProvider;
import com.android.systemui.plugins.VolumeDialog;
import com.android.systemui.statusbar.policy.ExtensionController;
import com.android.systemui.tuner.TunerService;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class VolumeDialogComponent_Factory implements Factory<VolumeDialogComponent> {
    public final Provider<ActivityStarter> activityStarterProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DemoModeController> demoModeControllerProvider;
    public final Provider<ExtensionController> extensionControllerProvider;
    public final Provider<KeyguardViewMediator> keyguardViewMediatorProvider;
    public final Provider<PluginDependencyProvider> pluginDependencyProvider;
    public final Provider<TunerService> tunerServiceProvider;
    public final Provider<VolumeDialogControllerImpl> volumeDialogControllerProvider;
    public final Provider<VolumeDialog> volumeDialogProvider;

    public VolumeDialogComponent_Factory(Provider<Context> provider, Provider<KeyguardViewMediator> provider2, Provider<ActivityStarter> provider3, Provider<VolumeDialogControllerImpl> provider4, Provider<DemoModeController> provider5, Provider<PluginDependencyProvider> provider6, Provider<ExtensionController> provider7, Provider<TunerService> provider8, Provider<VolumeDialog> provider9) {
        this.contextProvider = provider;
        this.keyguardViewMediatorProvider = provider2;
        this.activityStarterProvider = provider3;
        this.volumeDialogControllerProvider = provider4;
        this.demoModeControllerProvider = provider5;
        this.pluginDependencyProvider = provider6;
        this.extensionControllerProvider = provider7;
        this.tunerServiceProvider = provider8;
        this.volumeDialogProvider = provider9;
    }

    public VolumeDialogComponent get() {
        return newInstance(this.contextProvider.get(), this.keyguardViewMediatorProvider.get(), this.activityStarterProvider.get(), this.volumeDialogControllerProvider.get(), this.demoModeControllerProvider.get(), this.pluginDependencyProvider.get(), this.extensionControllerProvider.get(), this.tunerServiceProvider.get(), this.volumeDialogProvider.get());
    }

    public static VolumeDialogComponent_Factory create(Provider<Context> provider, Provider<KeyguardViewMediator> provider2, Provider<ActivityStarter> provider3, Provider<VolumeDialogControllerImpl> provider4, Provider<DemoModeController> provider5, Provider<PluginDependencyProvider> provider6, Provider<ExtensionController> provider7, Provider<TunerService> provider8, Provider<VolumeDialog> provider9) {
        return new VolumeDialogComponent_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9);
    }

    public static VolumeDialogComponent newInstance(Context context, KeyguardViewMediator keyguardViewMediator, ActivityStarter activityStarter, VolumeDialogControllerImpl volumeDialogControllerImpl, DemoModeController demoModeController, PluginDependencyProvider pluginDependencyProvider2, ExtensionController extensionController, TunerService tunerService, VolumeDialog volumeDialog) {
        return new VolumeDialogComponent(context, keyguardViewMediator, activityStarter, volumeDialogControllerImpl, demoModeController, pluginDependencyProvider2, extensionController, tunerService, volumeDialog);
    }
}
