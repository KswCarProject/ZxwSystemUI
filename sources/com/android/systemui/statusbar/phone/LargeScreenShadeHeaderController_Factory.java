package com.android.systemui.statusbar.phone;

import android.view.View;
import com.android.systemui.battery.BatteryMeterViewController;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.qs.HeaderPrivacyIconsController;
import com.android.systemui.qs.carrier.QSCarrierGroupController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class LargeScreenShadeHeaderController_Factory implements Factory<LargeScreenShadeHeaderController> {
    public final Provider<BatteryMeterViewController> batteryMeterViewControllerProvider;
    public final Provider<ConfigurationController> configurationControllerProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<FeatureFlags> featureFlagsProvider;
    public final Provider<View> headerProvider;
    public final Provider<HeaderPrivacyIconsController> privacyIconsControllerProvider;
    public final Provider<QSCarrierGroupController.Builder> qsCarrierGroupControllerBuilderProvider;
    public final Provider<StatusBarIconController> statusBarIconControllerProvider;

    public LargeScreenShadeHeaderController_Factory(Provider<View> provider, Provider<StatusBarIconController> provider2, Provider<HeaderPrivacyIconsController> provider3, Provider<ConfigurationController> provider4, Provider<QSCarrierGroupController.Builder> provider5, Provider<FeatureFlags> provider6, Provider<BatteryMeterViewController> provider7, Provider<DumpManager> provider8) {
        this.headerProvider = provider;
        this.statusBarIconControllerProvider = provider2;
        this.privacyIconsControllerProvider = provider3;
        this.configurationControllerProvider = provider4;
        this.qsCarrierGroupControllerBuilderProvider = provider5;
        this.featureFlagsProvider = provider6;
        this.batteryMeterViewControllerProvider = provider7;
        this.dumpManagerProvider = provider8;
    }

    public LargeScreenShadeHeaderController get() {
        return newInstance(this.headerProvider.get(), this.statusBarIconControllerProvider.get(), this.privacyIconsControllerProvider.get(), this.configurationControllerProvider.get(), this.qsCarrierGroupControllerBuilderProvider.get(), this.featureFlagsProvider.get(), this.batteryMeterViewControllerProvider.get(), this.dumpManagerProvider.get());
    }

    public static LargeScreenShadeHeaderController_Factory create(Provider<View> provider, Provider<StatusBarIconController> provider2, Provider<HeaderPrivacyIconsController> provider3, Provider<ConfigurationController> provider4, Provider<QSCarrierGroupController.Builder> provider5, Provider<FeatureFlags> provider6, Provider<BatteryMeterViewController> provider7, Provider<DumpManager> provider8) {
        return new LargeScreenShadeHeaderController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8);
    }

    public static LargeScreenShadeHeaderController newInstance(View view, StatusBarIconController statusBarIconController, HeaderPrivacyIconsController headerPrivacyIconsController, ConfigurationController configurationController, QSCarrierGroupController.Builder builder, FeatureFlags featureFlags, BatteryMeterViewController batteryMeterViewController, DumpManager dumpManager) {
        return new LargeScreenShadeHeaderController(view, statusBarIconController, headerPrivacyIconsController, configurationController, builder, featureFlags, batteryMeterViewController, dumpManager);
    }
}
