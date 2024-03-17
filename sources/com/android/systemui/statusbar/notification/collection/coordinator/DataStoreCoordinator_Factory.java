package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.NotifLiveDataStoreImpl;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class DataStoreCoordinator_Factory implements Factory<DataStoreCoordinator> {
    public final Provider<NotifLiveDataStoreImpl> notifLiveDataStoreImplProvider;

    public DataStoreCoordinator_Factory(Provider<NotifLiveDataStoreImpl> provider) {
        this.notifLiveDataStoreImplProvider = provider;
    }

    public DataStoreCoordinator get() {
        return newInstance(this.notifLiveDataStoreImplProvider.get());
    }

    public static DataStoreCoordinator_Factory create(Provider<NotifLiveDataStoreImpl> provider) {
        return new DataStoreCoordinator_Factory(provider);
    }

    public static DataStoreCoordinator newInstance(NotifLiveDataStoreImpl notifLiveDataStoreImpl) {
        return new DataStoreCoordinator(notifLiveDataStoreImpl);
    }
}
