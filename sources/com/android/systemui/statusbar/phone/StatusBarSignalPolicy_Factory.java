package com.android.systemui.statusbar.phone;

import android.content.Context;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.statusbar.connectivity.NetworkController;
import com.android.systemui.statusbar.policy.SecurityController;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.CarrierConfigTracker;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class StatusBarSignalPolicy_Factory implements Factory<StatusBarSignalPolicy> {
    public final Provider<CarrierConfigTracker> carrierConfigTrackerProvider;
    public final Provider<Context> contextProvider;
    public final Provider<FeatureFlags> featureFlagsProvider;
    public final Provider<StatusBarIconController> iconControllerProvider;
    public final Provider<NetworkController> networkControllerProvider;
    public final Provider<SecurityController> securityControllerProvider;
    public final Provider<TunerService> tunerServiceProvider;

    public StatusBarSignalPolicy_Factory(Provider<Context> provider, Provider<StatusBarIconController> provider2, Provider<CarrierConfigTracker> provider3, Provider<NetworkController> provider4, Provider<SecurityController> provider5, Provider<TunerService> provider6, Provider<FeatureFlags> provider7) {
        this.contextProvider = provider;
        this.iconControllerProvider = provider2;
        this.carrierConfigTrackerProvider = provider3;
        this.networkControllerProvider = provider4;
        this.securityControllerProvider = provider5;
        this.tunerServiceProvider = provider6;
        this.featureFlagsProvider = provider7;
    }

    public StatusBarSignalPolicy get() {
        return newInstance(this.contextProvider.get(), this.iconControllerProvider.get(), this.carrierConfigTrackerProvider.get(), this.networkControllerProvider.get(), this.securityControllerProvider.get(), this.tunerServiceProvider.get(), this.featureFlagsProvider.get());
    }

    public static StatusBarSignalPolicy_Factory create(Provider<Context> provider, Provider<StatusBarIconController> provider2, Provider<CarrierConfigTracker> provider3, Provider<NetworkController> provider4, Provider<SecurityController> provider5, Provider<TunerService> provider6, Provider<FeatureFlags> provider7) {
        return new StatusBarSignalPolicy_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7);
    }

    public static StatusBarSignalPolicy newInstance(Context context, StatusBarIconController statusBarIconController, CarrierConfigTracker carrierConfigTracker, NetworkController networkController, SecurityController securityController, TunerService tunerService, FeatureFlags featureFlags) {
        return new StatusBarSignalPolicy(context, statusBarIconController, carrierConfigTracker, networkController, securityController, tunerService, featureFlags);
    }
}
