package com.android.systemui.tv;

import android.hardware.SensorPrivacyManager;
import com.android.systemui.statusbar.policy.SensorPrivacyController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class TvSystemUIModule_ProvideSensorPrivacyControllerFactory implements Factory<SensorPrivacyController> {
    public final Provider<SensorPrivacyManager> sensorPrivacyManagerProvider;

    public TvSystemUIModule_ProvideSensorPrivacyControllerFactory(Provider<SensorPrivacyManager> provider) {
        this.sensorPrivacyManagerProvider = provider;
    }

    public SensorPrivacyController get() {
        return provideSensorPrivacyController(this.sensorPrivacyManagerProvider.get());
    }

    public static TvSystemUIModule_ProvideSensorPrivacyControllerFactory create(Provider<SensorPrivacyManager> provider) {
        return new TvSystemUIModule_ProvideSensorPrivacyControllerFactory(provider);
    }

    public static SensorPrivacyController provideSensorPrivacyController(SensorPrivacyManager sensorPrivacyManager) {
        return (SensorPrivacyController) Preconditions.checkNotNullFromProvides(TvSystemUIModule.provideSensorPrivacyController(sensorPrivacyManager));
    }
}
