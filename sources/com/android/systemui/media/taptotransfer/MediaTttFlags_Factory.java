package com.android.systemui.media.taptotransfer;

import com.android.systemui.flags.FeatureFlags;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class MediaTttFlags_Factory implements Factory<MediaTttFlags> {
    public final Provider<FeatureFlags> featureFlagsProvider;

    public MediaTttFlags_Factory(Provider<FeatureFlags> provider) {
        this.featureFlagsProvider = provider;
    }

    public MediaTttFlags get() {
        return newInstance(this.featureFlagsProvider.get());
    }

    public static MediaTttFlags_Factory create(Provider<FeatureFlags> provider) {
        return new MediaTttFlags_Factory(provider);
    }

    public static MediaTttFlags newInstance(FeatureFlags featureFlags) {
        return new MediaTttFlags(featureFlags);
    }
}
