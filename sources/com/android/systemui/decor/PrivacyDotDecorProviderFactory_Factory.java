package com.android.systemui.decor;

import android.content.res.Resources;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class PrivacyDotDecorProviderFactory_Factory implements Factory<PrivacyDotDecorProviderFactory> {
    public final Provider<Resources> resProvider;

    public PrivacyDotDecorProviderFactory_Factory(Provider<Resources> provider) {
        this.resProvider = provider;
    }

    public PrivacyDotDecorProviderFactory get() {
        return newInstance(this.resProvider.get());
    }

    public static PrivacyDotDecorProviderFactory_Factory create(Provider<Resources> provider) {
        return new PrivacyDotDecorProviderFactory_Factory(provider);
    }

    public static PrivacyDotDecorProviderFactory newInstance(Resources resources) {
        return new PrivacyDotDecorProviderFactory(resources);
    }
}
