package com.android.systemui.statusbar.phone;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ConfigurationControllerImpl_Factory implements Factory<ConfigurationControllerImpl> {
    public final Provider<Context> contextProvider;

    public ConfigurationControllerImpl_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    public ConfigurationControllerImpl get() {
        return newInstance(this.contextProvider.get());
    }

    public static ConfigurationControllerImpl_Factory create(Provider<Context> provider) {
        return new ConfigurationControllerImpl_Factory(provider);
    }

    public static ConfigurationControllerImpl newInstance(Context context) {
        return new ConfigurationControllerImpl(context);
    }
}
