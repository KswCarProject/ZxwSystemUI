package com.android.systemui.usb;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class StorageNotification_Factory implements Factory<StorageNotification> {
    public final Provider<Context> contextProvider;

    public StorageNotification_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    public StorageNotification get() {
        return newInstance(this.contextProvider.get());
    }

    public static StorageNotification_Factory create(Provider<Context> provider) {
        return new StorageNotification_Factory(provider);
    }

    public static StorageNotification newInstance(Context context) {
        return new StorageNotification(context);
    }
}
