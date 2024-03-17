package com.android.systemui.doze;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class AlwaysOnDisplayPolicy_Factory implements Factory<AlwaysOnDisplayPolicy> {
    public final Provider<Context> contextProvider;

    public AlwaysOnDisplayPolicy_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    public AlwaysOnDisplayPolicy get() {
        return newInstance(this.contextProvider.get());
    }

    public static AlwaysOnDisplayPolicy_Factory create(Provider<Context> provider) {
        return new AlwaysOnDisplayPolicy_Factory(provider);
    }

    public static AlwaysOnDisplayPolicy newInstance(Context context) {
        return new AlwaysOnDisplayPolicy(context);
    }
}
