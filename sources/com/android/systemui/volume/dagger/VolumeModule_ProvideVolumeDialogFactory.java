package com.android.systemui.volume.dagger;

import android.content.Context;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.systemui.media.dialog.MediaOutputDialogFactory;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.VolumeDialog;
import com.android.systemui.plugins.VolumeDialogController;
import com.android.systemui.statusbar.policy.AccessibilityManagerWrapper;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class VolumeModule_ProvideVolumeDialogFactory implements Factory<VolumeDialog> {
    public final Provider<AccessibilityManagerWrapper> accessibilityManagerWrapperProvider;
    public final Provider<ActivityStarter> activityStarterProvider;
    public final Provider<ConfigurationController> configurationControllerProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DeviceProvisionedController> deviceProvisionedControllerProvider;
    public final Provider<InteractionJankMonitor> interactionJankMonitorProvider;
    public final Provider<MediaOutputDialogFactory> mediaOutputDialogFactoryProvider;
    public final Provider<VolumeDialogController> volumeDialogControllerProvider;

    public VolumeModule_ProvideVolumeDialogFactory(Provider<Context> provider, Provider<VolumeDialogController> provider2, Provider<AccessibilityManagerWrapper> provider3, Provider<DeviceProvisionedController> provider4, Provider<ConfigurationController> provider5, Provider<MediaOutputDialogFactory> provider6, Provider<ActivityStarter> provider7, Provider<InteractionJankMonitor> provider8) {
        this.contextProvider = provider;
        this.volumeDialogControllerProvider = provider2;
        this.accessibilityManagerWrapperProvider = provider3;
        this.deviceProvisionedControllerProvider = provider4;
        this.configurationControllerProvider = provider5;
        this.mediaOutputDialogFactoryProvider = provider6;
        this.activityStarterProvider = provider7;
        this.interactionJankMonitorProvider = provider8;
    }

    public VolumeDialog get() {
        return provideVolumeDialog(this.contextProvider.get(), this.volumeDialogControllerProvider.get(), this.accessibilityManagerWrapperProvider.get(), this.deviceProvisionedControllerProvider.get(), this.configurationControllerProvider.get(), this.mediaOutputDialogFactoryProvider.get(), this.activityStarterProvider.get(), this.interactionJankMonitorProvider.get());
    }

    public static VolumeModule_ProvideVolumeDialogFactory create(Provider<Context> provider, Provider<VolumeDialogController> provider2, Provider<AccessibilityManagerWrapper> provider3, Provider<DeviceProvisionedController> provider4, Provider<ConfigurationController> provider5, Provider<MediaOutputDialogFactory> provider6, Provider<ActivityStarter> provider7, Provider<InteractionJankMonitor> provider8) {
        return new VolumeModule_ProvideVolumeDialogFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8);
    }

    public static VolumeDialog provideVolumeDialog(Context context, VolumeDialogController volumeDialogController, AccessibilityManagerWrapper accessibilityManagerWrapper, DeviceProvisionedController deviceProvisionedController, ConfigurationController configurationController, MediaOutputDialogFactory mediaOutputDialogFactory, ActivityStarter activityStarter, InteractionJankMonitor interactionJankMonitor) {
        return (VolumeDialog) Preconditions.checkNotNullFromProvides(VolumeModule.provideVolumeDialog(context, volumeDialogController, accessibilityManagerWrapper, deviceProvisionedController, configurationController, mediaOutputDialogFactory, activityStarter, interactionJankMonitor));
    }
}
