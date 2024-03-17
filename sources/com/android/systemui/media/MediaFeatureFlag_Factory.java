package com.android.systemui.media;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class MediaFeatureFlag_Factory implements Factory<MediaFeatureFlag> {
    public final Provider<Context> contextProvider;

    public MediaFeatureFlag_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    public MediaFeatureFlag get() {
        return newInstance(this.contextProvider.get());
    }

    public static MediaFeatureFlag_Factory create(Provider<Context> provider) {
        return new MediaFeatureFlag_Factory(provider);
    }

    public static MediaFeatureFlag newInstance(Context context) {
        return new MediaFeatureFlag(context);
    }
}
