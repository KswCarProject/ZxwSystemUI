package com.android.systemui.statusbar.notification.collection.coordinator.dagger;

import com.android.systemui.statusbar.notification.collection.coordinator.NotifCoordinators;
import com.android.systemui.statusbar.notification.collection.coordinator.dagger.CoordinatorsSubcomponent;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class CoordinatorsModule_NotifCoordinatorsFactory implements Factory<NotifCoordinators> {
    public final Provider<CoordinatorsSubcomponent.Factory> factoryProvider;

    public CoordinatorsModule_NotifCoordinatorsFactory(Provider<CoordinatorsSubcomponent.Factory> provider) {
        this.factoryProvider = provider;
    }

    public NotifCoordinators get() {
        return notifCoordinators(this.factoryProvider.get());
    }

    public static CoordinatorsModule_NotifCoordinatorsFactory create(Provider<CoordinatorsSubcomponent.Factory> provider) {
        return new CoordinatorsModule_NotifCoordinatorsFactory(provider);
    }

    public static NotifCoordinators notifCoordinators(CoordinatorsSubcomponent.Factory factory) {
        return (NotifCoordinators) Preconditions.checkNotNullFromProvides(CoordinatorsModule.notifCoordinators(factory));
    }
}
