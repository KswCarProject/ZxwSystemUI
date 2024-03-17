package com.android.systemui.battery;

import android.content.ContentResolver;
import android.os.Handler;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.tuner.TunerService;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class BatteryMeterViewController_Factory implements Factory<BatteryMeterViewController> {
    public final Provider<BatteryController> batteryControllerProvider;
    public final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    public final Provider<ConfigurationController> configurationControllerProvider;
    public final Provider<ContentResolver> contentResolverProvider;
    public final Provider<Handler> mainHandlerProvider;
    public final Provider<TunerService> tunerServiceProvider;
    public final Provider<BatteryMeterView> viewProvider;

    public BatteryMeterViewController_Factory(Provider<BatteryMeterView> provider, Provider<ConfigurationController> provider2, Provider<TunerService> provider3, Provider<BroadcastDispatcher> provider4, Provider<Handler> provider5, Provider<ContentResolver> provider6, Provider<BatteryController> provider7) {
        this.viewProvider = provider;
        this.configurationControllerProvider = provider2;
        this.tunerServiceProvider = provider3;
        this.broadcastDispatcherProvider = provider4;
        this.mainHandlerProvider = provider5;
        this.contentResolverProvider = provider6;
        this.batteryControllerProvider = provider7;
    }

    public BatteryMeterViewController get() {
        return newInstance(this.viewProvider.get(), this.configurationControllerProvider.get(), this.tunerServiceProvider.get(), this.broadcastDispatcherProvider.get(), this.mainHandlerProvider.get(), this.contentResolverProvider.get(), this.batteryControllerProvider.get());
    }

    public static BatteryMeterViewController_Factory create(Provider<BatteryMeterView> provider, Provider<ConfigurationController> provider2, Provider<TunerService> provider3, Provider<BroadcastDispatcher> provider4, Provider<Handler> provider5, Provider<ContentResolver> provider6, Provider<BatteryController> provider7) {
        return new BatteryMeterViewController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7);
    }

    public static BatteryMeterViewController newInstance(BatteryMeterView batteryMeterView, ConfigurationController configurationController, TunerService tunerService, BroadcastDispatcher broadcastDispatcher, Handler handler, ContentResolver contentResolver, BatteryController batteryController) {
        return new BatteryMeterViewController(batteryMeterView, configurationController, tunerService, broadcastDispatcher, handler, contentResolver, batteryController);
    }
}
