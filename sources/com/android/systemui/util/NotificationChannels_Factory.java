package com.android.systemui.util;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class NotificationChannels_Factory implements Factory<NotificationChannels> {
    public final Provider<Context> contextProvider;

    public NotificationChannels_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    public NotificationChannels get() {
        return newInstance(this.contextProvider.get());
    }

    public static NotificationChannels_Factory create(Provider<Context> provider) {
        return new NotificationChannels_Factory(provider);
    }

    public static NotificationChannels newInstance(Context context) {
        return new NotificationChannels(context);
    }
}
