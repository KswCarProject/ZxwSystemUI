package com.android.systemui.statusbar;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class RemoteInputNotificationRebuilder_Factory implements Factory<RemoteInputNotificationRebuilder> {
    public final Provider<Context> contextProvider;

    public RemoteInputNotificationRebuilder_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    public RemoteInputNotificationRebuilder get() {
        return newInstance(this.contextProvider.get());
    }

    public static RemoteInputNotificationRebuilder_Factory create(Provider<Context> provider) {
        return new RemoteInputNotificationRebuilder_Factory(provider);
    }

    public static RemoteInputNotificationRebuilder newInstance(Context context) {
        return new RemoteInputNotificationRebuilder(context);
    }
}
