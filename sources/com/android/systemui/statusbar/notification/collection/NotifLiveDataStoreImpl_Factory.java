package com.android.systemui.statusbar.notification.collection;

import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class NotifLiveDataStoreImpl_Factory implements Factory<NotifLiveDataStoreImpl> {
    public final Provider<Executor> mainExecutorProvider;

    public NotifLiveDataStoreImpl_Factory(Provider<Executor> provider) {
        this.mainExecutorProvider = provider;
    }

    public NotifLiveDataStoreImpl get() {
        return newInstance(this.mainExecutorProvider.get());
    }

    public static NotifLiveDataStoreImpl_Factory create(Provider<Executor> provider) {
        return new NotifLiveDataStoreImpl_Factory(provider);
    }

    public static NotifLiveDataStoreImpl newInstance(Executor executor) {
        return new NotifLiveDataStoreImpl(executor);
    }
}
