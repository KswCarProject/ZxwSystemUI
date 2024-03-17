package com.android.systemui.statusbar.notification.collection.render;

import android.view.LayoutInflater;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class MediaContainerController_Factory implements Factory<MediaContainerController> {
    public final Provider<LayoutInflater> layoutInflaterProvider;

    public MediaContainerController_Factory(Provider<LayoutInflater> provider) {
        this.layoutInflaterProvider = provider;
    }

    public MediaContainerController get() {
        return newInstance(this.layoutInflaterProvider.get());
    }

    public static MediaContainerController_Factory create(Provider<LayoutInflater> provider) {
        return new MediaContainerController_Factory(provider);
    }

    public static MediaContainerController newInstance(LayoutInflater layoutInflater) {
        return new MediaContainerController(layoutInflater);
    }
}
