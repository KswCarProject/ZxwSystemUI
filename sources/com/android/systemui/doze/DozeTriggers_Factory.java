package com.android.systemui.doze;

import android.content.Context;
import android.hardware.display.AmbientDisplayConfiguration;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dock.DockManager;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.policy.DevicePostureController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.sensors.AsyncSensorManager;
import com.android.systemui.util.sensors.ProximityCheck;
import com.android.systemui.util.sensors.ProximitySensor;
import com.android.systemui.util.settings.SecureSettings;
import com.android.systemui.util.wakelock.WakeLock;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class DozeTriggers_Factory implements Factory<DozeTriggers> {
    public final Provider<AuthController> authControllerProvider;
    public final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    public final Provider<AmbientDisplayConfiguration> configProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DevicePostureController> devicePostureControllerProvider;
    public final Provider<DockManager> dockManagerProvider;
    public final Provider<DozeHost> dozeHostProvider;
    public final Provider<DozeLog> dozeLogProvider;
    public final Provider<DozeParameters> dozeParametersProvider;
    public final Provider<KeyguardStateController> keyguardStateControllerProvider;
    public final Provider<DelayableExecutor> mainExecutorProvider;
    public final Provider<ProximityCheck> proxCheckProvider;
    public final Provider<ProximitySensor> proximitySensorProvider;
    public final Provider<SecureSettings> secureSettingsProvider;
    public final Provider<AsyncSensorManager> sensorManagerProvider;
    public final Provider<UiEventLogger> uiEventLoggerProvider;
    public final Provider<WakeLock> wakeLockProvider;

    public DozeTriggers_Factory(Provider<Context> provider, Provider<DozeHost> provider2, Provider<AmbientDisplayConfiguration> provider3, Provider<DozeParameters> provider4, Provider<AsyncSensorManager> provider5, Provider<WakeLock> provider6, Provider<DockManager> provider7, Provider<ProximitySensor> provider8, Provider<ProximityCheck> provider9, Provider<DozeLog> provider10, Provider<BroadcastDispatcher> provider11, Provider<SecureSettings> provider12, Provider<AuthController> provider13, Provider<DelayableExecutor> provider14, Provider<UiEventLogger> provider15, Provider<KeyguardStateController> provider16, Provider<DevicePostureController> provider17) {
        this.contextProvider = provider;
        this.dozeHostProvider = provider2;
        this.configProvider = provider3;
        this.dozeParametersProvider = provider4;
        this.sensorManagerProvider = provider5;
        this.wakeLockProvider = provider6;
        this.dockManagerProvider = provider7;
        this.proximitySensorProvider = provider8;
        this.proxCheckProvider = provider9;
        this.dozeLogProvider = provider10;
        this.broadcastDispatcherProvider = provider11;
        this.secureSettingsProvider = provider12;
        this.authControllerProvider = provider13;
        this.mainExecutorProvider = provider14;
        this.uiEventLoggerProvider = provider15;
        this.keyguardStateControllerProvider = provider16;
        this.devicePostureControllerProvider = provider17;
    }

    public DozeTriggers get() {
        return newInstance(this.contextProvider.get(), this.dozeHostProvider.get(), this.configProvider.get(), this.dozeParametersProvider.get(), this.sensorManagerProvider.get(), this.wakeLockProvider.get(), this.dockManagerProvider.get(), this.proximitySensorProvider.get(), this.proxCheckProvider.get(), this.dozeLogProvider.get(), this.broadcastDispatcherProvider.get(), this.secureSettingsProvider.get(), this.authControllerProvider.get(), this.mainExecutorProvider.get(), this.uiEventLoggerProvider.get(), this.keyguardStateControllerProvider.get(), this.devicePostureControllerProvider.get());
    }

    public static DozeTriggers_Factory create(Provider<Context> provider, Provider<DozeHost> provider2, Provider<AmbientDisplayConfiguration> provider3, Provider<DozeParameters> provider4, Provider<AsyncSensorManager> provider5, Provider<WakeLock> provider6, Provider<DockManager> provider7, Provider<ProximitySensor> provider8, Provider<ProximityCheck> provider9, Provider<DozeLog> provider10, Provider<BroadcastDispatcher> provider11, Provider<SecureSettings> provider12, Provider<AuthController> provider13, Provider<DelayableExecutor> provider14, Provider<UiEventLogger> provider15, Provider<KeyguardStateController> provider16, Provider<DevicePostureController> provider17) {
        return new DozeTriggers_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17);
    }

    public static DozeTriggers newInstance(Context context, DozeHost dozeHost, AmbientDisplayConfiguration ambientDisplayConfiguration, DozeParameters dozeParameters, AsyncSensorManager asyncSensorManager, WakeLock wakeLock, DockManager dockManager, ProximitySensor proximitySensor, ProximityCheck proximityCheck, DozeLog dozeLog, BroadcastDispatcher broadcastDispatcher, SecureSettings secureSettings, AuthController authController, DelayableExecutor delayableExecutor, UiEventLogger uiEventLogger, KeyguardStateController keyguardStateController, DevicePostureController devicePostureController) {
        return new DozeTriggers(context, dozeHost, ambientDisplayConfiguration, dozeParameters, asyncSensorManager, wakeLock, dockManager, proximitySensor, proximityCheck, dozeLog, broadcastDispatcher, secureSettings, authController, delayableExecutor, uiEventLogger, keyguardStateController, devicePostureController);
    }
}
