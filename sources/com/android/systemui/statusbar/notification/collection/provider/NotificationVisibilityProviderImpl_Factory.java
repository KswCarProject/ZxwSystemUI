package com.android.systemui.statusbar.notification.collection.provider;

import com.android.systemui.statusbar.notification.collection.NotifLiveDataStore;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class NotificationVisibilityProviderImpl_Factory implements Factory<NotificationVisibilityProviderImpl> {
    public final Provider<CommonNotifCollection> notifCollectionProvider;
    public final Provider<NotifLiveDataStore> notifDataStoreProvider;

    public NotificationVisibilityProviderImpl_Factory(Provider<NotifLiveDataStore> provider, Provider<CommonNotifCollection> provider2) {
        this.notifDataStoreProvider = provider;
        this.notifCollectionProvider = provider2;
    }

    public NotificationVisibilityProviderImpl get() {
        return newInstance(this.notifDataStoreProvider.get(), this.notifCollectionProvider.get());
    }

    public static NotificationVisibilityProviderImpl_Factory create(Provider<NotifLiveDataStore> provider, Provider<CommonNotifCollection> provider2) {
        return new NotificationVisibilityProviderImpl_Factory(provider, provider2);
    }

    public static NotificationVisibilityProviderImpl newInstance(NotifLiveDataStore notifLiveDataStore, CommonNotifCollection commonNotifCollection) {
        return new NotificationVisibilityProviderImpl(notifLiveDataStore, commonNotifCollection);
    }
}
