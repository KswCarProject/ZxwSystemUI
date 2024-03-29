package com.android.systemui.statusbar.notification.collection;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class TargetSdkResolver_Factory implements Factory<TargetSdkResolver> {
    public final Provider<Context> contextProvider;

    public TargetSdkResolver_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    public TargetSdkResolver get() {
        return newInstance(this.contextProvider.get());
    }

    public static TargetSdkResolver_Factory create(Provider<Context> provider) {
        return new TargetSdkResolver_Factory(provider);
    }

    public static TargetSdkResolver newInstance(Context context) {
        return new TargetSdkResolver(context);
    }
}
