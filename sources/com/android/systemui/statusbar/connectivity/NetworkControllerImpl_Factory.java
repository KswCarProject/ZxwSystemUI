package com.android.systemui.statusbar.connectivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.demomode.DemoModeController;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.qs.tiles.dialog.InternetDialogFactory;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.telephony.TelephonyListenerManager;
import com.android.systemui.util.CarrierConfigTracker;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class NetworkControllerImpl_Factory implements Factory<NetworkControllerImpl> {
    public final Provider<AccessPointControllerImpl> accessPointControllerProvider;
    public final Provider<Executor> bgExecutorProvider;
    public final Provider<Looper> bgLooperProvider;
    public final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    public final Provider<CallbackHandler> callbackHandlerProvider;
    public final Provider<CarrierConfigTracker> carrierConfigTrackerProvider;
    public final Provider<ConnectivityManager> connectivityManagerProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DemoModeController> demoModeControllerProvider;
    public final Provider<DeviceProvisionedController> deviceProvisionedControllerProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<FeatureFlags> featureFlagsProvider;
    public final Provider<Handler> handlerProvider;
    public final Provider<InternetDialogFactory> internetDialogFactoryProvider;
    public final Provider<SubscriptionManager> subscriptionManagerProvider;
    public final Provider<TelephonyListenerManager> telephonyListenerManagerProvider;
    public final Provider<TelephonyManager> telephonyManagerProvider;
    public final Provider<WifiStatusTrackerFactory> trackerFactoryProvider;
    public final Provider<WifiManager> wifiManagerProvider;

    public NetworkControllerImpl_Factory(Provider<Context> provider, Provider<Looper> provider2, Provider<Executor> provider3, Provider<SubscriptionManager> provider4, Provider<CallbackHandler> provider5, Provider<DeviceProvisionedController> provider6, Provider<BroadcastDispatcher> provider7, Provider<ConnectivityManager> provider8, Provider<TelephonyManager> provider9, Provider<TelephonyListenerManager> provider10, Provider<WifiManager> provider11, Provider<AccessPointControllerImpl> provider12, Provider<DemoModeController> provider13, Provider<CarrierConfigTracker> provider14, Provider<WifiStatusTrackerFactory> provider15, Provider<Handler> provider16, Provider<InternetDialogFactory> provider17, Provider<FeatureFlags> provider18, Provider<DumpManager> provider19) {
        this.contextProvider = provider;
        this.bgLooperProvider = provider2;
        this.bgExecutorProvider = provider3;
        this.subscriptionManagerProvider = provider4;
        this.callbackHandlerProvider = provider5;
        this.deviceProvisionedControllerProvider = provider6;
        this.broadcastDispatcherProvider = provider7;
        this.connectivityManagerProvider = provider8;
        this.telephonyManagerProvider = provider9;
        this.telephonyListenerManagerProvider = provider10;
        this.wifiManagerProvider = provider11;
        this.accessPointControllerProvider = provider12;
        this.demoModeControllerProvider = provider13;
        this.carrierConfigTrackerProvider = provider14;
        this.trackerFactoryProvider = provider15;
        this.handlerProvider = provider16;
        this.internetDialogFactoryProvider = provider17;
        this.featureFlagsProvider = provider18;
        this.dumpManagerProvider = provider19;
    }

    public NetworkControllerImpl get() {
        return newInstance(this.contextProvider.get(), this.bgLooperProvider.get(), this.bgExecutorProvider.get(), this.subscriptionManagerProvider.get(), this.callbackHandlerProvider.get(), this.deviceProvisionedControllerProvider.get(), this.broadcastDispatcherProvider.get(), this.connectivityManagerProvider.get(), this.telephonyManagerProvider.get(), this.telephonyListenerManagerProvider.get(), this.wifiManagerProvider.get(), this.accessPointControllerProvider.get(), this.demoModeControllerProvider.get(), this.carrierConfigTrackerProvider.get(), this.trackerFactoryProvider.get(), this.handlerProvider.get(), this.internetDialogFactoryProvider.get(), this.featureFlagsProvider.get(), this.dumpManagerProvider.get());
    }

    public static NetworkControllerImpl_Factory create(Provider<Context> provider, Provider<Looper> provider2, Provider<Executor> provider3, Provider<SubscriptionManager> provider4, Provider<CallbackHandler> provider5, Provider<DeviceProvisionedController> provider6, Provider<BroadcastDispatcher> provider7, Provider<ConnectivityManager> provider8, Provider<TelephonyManager> provider9, Provider<TelephonyListenerManager> provider10, Provider<WifiManager> provider11, Provider<AccessPointControllerImpl> provider12, Provider<DemoModeController> provider13, Provider<CarrierConfigTracker> provider14, Provider<WifiStatusTrackerFactory> provider15, Provider<Handler> provider16, Provider<InternetDialogFactory> provider17, Provider<FeatureFlags> provider18, Provider<DumpManager> provider19) {
        return new NetworkControllerImpl_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, provider18, provider19);
    }

    public static NetworkControllerImpl newInstance(Context context, Looper looper, Executor executor, SubscriptionManager subscriptionManager, CallbackHandler callbackHandler, DeviceProvisionedController deviceProvisionedController, BroadcastDispatcher broadcastDispatcher, ConnectivityManager connectivityManager, TelephonyManager telephonyManager, TelephonyListenerManager telephonyListenerManager, WifiManager wifiManager, AccessPointControllerImpl accessPointControllerImpl, DemoModeController demoModeController, CarrierConfigTracker carrierConfigTracker, WifiStatusTrackerFactory wifiStatusTrackerFactory, Handler handler, InternetDialogFactory internetDialogFactory, FeatureFlags featureFlags, DumpManager dumpManager) {
        return new NetworkControllerImpl(context, looper, executor, subscriptionManager, callbackHandler, deviceProvisionedController, broadcastDispatcher, connectivityManager, telephonyManager, telephonyListenerManager, wifiManager, accessPointControllerImpl, demoModeController, carrierConfigTracker, wifiStatusTrackerFactory, handler, internetDialogFactory, featureFlags, dumpManager);
    }
}
