package com.android.systemui.media;

import com.android.systemui.flags.FeatureFlags;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class MediaFlags_Factory implements Factory<MediaFlags> {
    public final Provider<FeatureFlags> featureFlagsProvider;

    public MediaFlags_Factory(Provider<FeatureFlags> provider) {
        this.featureFlagsProvider = provider;
    }

    public MediaFlags get() {
        return newInstance(this.featureFlagsProvider.get());
    }

    public static MediaFlags_Factory create(Provider<FeatureFlags> provider) {
        return new MediaFlags_Factory(provider);
    }

    public static MediaFlags newInstance(FeatureFlags featureFlags) {
        return new MediaFlags(featureFlags);
    }
}
