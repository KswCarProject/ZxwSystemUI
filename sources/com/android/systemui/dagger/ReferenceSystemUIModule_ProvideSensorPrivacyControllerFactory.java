package com.android.systemui.dagger;

import android.hardware.SensorPrivacyManager;
import com.android.systemui.statusbar.policy.SensorPrivacyController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class ReferenceSystemUIModule_ProvideSensorPrivacyControllerFactory implements Factory<SensorPrivacyController> {
    public final Provider<SensorPrivacyManager> sensorPrivacyManagerProvider;

    public ReferenceSystemUIModule_ProvideSensorPrivacyControllerFactory(Provider<SensorPrivacyManager> provider) {
        this.sensorPrivacyManagerProvider = provider;
    }

    public SensorPrivacyController get() {
        return provideSensorPrivacyController(this.sensorPrivacyManagerProvider.get());
    }

    public static ReferenceSystemUIModule_ProvideSensorPrivacyControllerFactory create(Provider<SensorPrivacyManager> provider) {
        return new ReferenceSystemUIModule_ProvideSensorPrivacyControllerFactory(provider);
    }

    public static SensorPrivacyController provideSensorPrivacyController(SensorPrivacyManager sensorPrivacyManager) {
        return (SensorPrivacyController) Preconditions.checkNotNullFromProvides(ReferenceSystemUIModule.provideSensorPrivacyController(sensorPrivacyManager));
    }
}
