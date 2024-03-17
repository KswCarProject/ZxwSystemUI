package com.android.systemui.statusbar.notification;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class SectionHeaderVisibilityProvider_Factory implements Factory<SectionHeaderVisibilityProvider> {
    public final Provider<Context> contextProvider;

    public SectionHeaderVisibilityProvider_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    public SectionHeaderVisibilityProvider get() {
        return newInstance(this.contextProvider.get());
    }

    public static SectionHeaderVisibilityProvider_Factory create(Provider<Context> provider) {
        return new SectionHeaderVisibilityProvider_Factory(provider);
    }

    public static SectionHeaderVisibilityProvider newInstance(Context context) {
        return new SectionHeaderVisibilityProvider(context);
    }
}
