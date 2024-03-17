package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.content.res.Resources;
import android.hardware.display.AmbientDisplayConfiguration;
import android.os.Handler;
import android.os.PowerManager;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.doze.AlwaysOnDisplayPolicy;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.unfold.SysUIUnfoldComponent;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;

public final class DozeParameters_Factory implements Factory<DozeParameters> {
    public final Provider<AlwaysOnDisplayPolicy> alwaysOnDisplayPolicyProvider;
    public final Provider<AmbientDisplayConfiguration> ambientDisplayConfigurationProvider;
    public final Provider<BatteryController> batteryControllerProvider;
    public final Provider<ConfigurationController> configurationControllerProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<FeatureFlags> featureFlagsProvider;
    public final Provider<Handler> handlerProvider;
    public final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    public final Provider<PowerManager> powerManagerProvider;
    public final Provider<Resources> resourcesProvider;
    public final Provider<ScreenOffAnimationController> screenOffAnimationControllerProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;
    public final Provider<Optional<SysUIUnfoldComponent>> sysUiUnfoldComponentProvider;
    public final Provider<TunerService> tunerServiceProvider;
    public final Provider<UnlockedScreenOffAnimationController> unlockedScreenOffAnimationControllerProvider;

    public DozeParameters_Factory(Provider<Context> provider, Provider<Handler> provider2, Provider<Resources> provider3, Provider<AmbientDisplayConfiguration> provider4, Provider<AlwaysOnDisplayPolicy> provider5, Provider<PowerManager> provider6, Provider<BatteryController> provider7, Provider<TunerService> provider8, Provider<DumpManager> provider9, Provider<FeatureFlags> provider10, Provider<ScreenOffAnimationController> provider11, Provider<Optional<SysUIUnfoldComponent>> provider12, Provider<UnlockedScreenOffAnimationController> provider13, Provider<KeyguardUpdateMonitor> provider14, Provider<ConfigurationController> provider15, Provider<StatusBarStateController> provider16) {
        this.contextProvider = provider;
        this.handlerProvider = provider2;
        this.resourcesProvider = provider3;
        this.ambientDisplayConfigurationProvider = provider4;
        this.alwaysOnDisplayPolicyProvider = provider5;
        this.powerManagerProvider = provider6;
        this.batteryControllerProvider = provider7;
        this.tunerServiceProvider = provider8;
        this.dumpManagerProvider = provider9;
        this.featureFlagsProvider = provider10;
        this.screenOffAnimationControllerProvider = provider11;
        this.sysUiUnfoldComponentProvider = provider12;
        this.unlockedScreenOffAnimationControllerProvider = provider13;
        this.keyguardUpdateMonitorProvider = provider14;
        this.configurationControllerProvider = provider15;
        this.statusBarStateControllerProvider = provider16;
    }

    public DozeParameters get() {
        return newInstance(this.contextProvider.get(), this.handlerProvider.get(), this.resourcesProvider.get(), this.ambientDisplayConfigurationProvider.get(), this.alwaysOnDisplayPolicyProvider.get(), this.powerManagerProvider.get(), this.batteryControllerProvider.get(), this.tunerServiceProvider.get(), this.dumpManagerProvider.get(), this.featureFlagsProvider.get(), this.screenOffAnimationControllerProvider.get(), this.sysUiUnfoldComponentProvider.get(), this.unlockedScreenOffAnimationControllerProvider.get(), this.keyguardUpdateMonitorProvider.get(), this.configurationControllerProvider.get(), this.statusBarStateControllerProvider.get());
    }

    public static DozeParameters_Factory create(Provider<Context> provider, Provider<Handler> provider2, Provider<Resources> provider3, Provider<AmbientDisplayConfiguration> provider4, Provider<AlwaysOnDisplayPolicy> provider5, Provider<PowerManager> provider6, Provider<BatteryController> provider7, Provider<TunerService> provider8, Provider<DumpManager> provider9, Provider<FeatureFlags> provider10, Provider<ScreenOffAnimationController> provider11, Provider<Optional<SysUIUnfoldComponent>> provider12, Provider<UnlockedScreenOffAnimationController> provider13, Provider<KeyguardUpdateMonitor> provider14, Provider<ConfigurationController> provider15, Provider<StatusBarStateController> provider16) {
        return new DozeParameters_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16);
    }

    public static DozeParameters newInstance(Context context, Handler handler, Resources resources, AmbientDisplayConfiguration ambientDisplayConfiguration, AlwaysOnDisplayPolicy alwaysOnDisplayPolicy, PowerManager powerManager, BatteryController batteryController, TunerService tunerService, DumpManager dumpManager, FeatureFlags featureFlags, ScreenOffAnimationController screenOffAnimationController, Optional<SysUIUnfoldComponent> optional, UnlockedScreenOffAnimationController unlockedScreenOffAnimationController, KeyguardUpdateMonitor keyguardUpdateMonitor, ConfigurationController configurationController, StatusBarStateController statusBarStateController) {
        return new DozeParameters(context, handler, resources, ambientDisplayConfiguration, alwaysOnDisplayPolicy, powerManager, batteryController, tunerService, dumpManager, featureFlags, screenOffAnimationController, optional, unlockedScreenOffAnimationController, keyguardUpdateMonitor, configurationController, statusBarStateController);
    }
}
