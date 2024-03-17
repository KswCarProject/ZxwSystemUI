package com.android.systemui.media;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class MediaControllerFactory_Factory implements Factory<MediaControllerFactory> {
    public final Provider<Context> contextProvider;

    public MediaControllerFactory_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    public MediaControllerFactory get() {
        return newInstance(this.contextProvider.get());
    }

    public static MediaControllerFactory_Factory create(Provider<Context> provider) {
        return new MediaControllerFactory_Factory(provider);
    }

    public static MediaControllerFactory newInstance(Context context) {
        return new MediaControllerFactory(context);
    }
}
