package com.android.systemui.media.dialog;

import dagger.internal.Factory;
import javax.inject.Provider;

public final class MediaOutputDialogReceiver_Factory implements Factory<MediaOutputDialogReceiver> {
    public final Provider<MediaOutputBroadcastDialogFactory> mediaOutputBroadcastDialogFactoryProvider;
    public final Provider<MediaOutputDialogFactory> mediaOutputDialogFactoryProvider;

    public MediaOutputDialogReceiver_Factory(Provider<MediaOutputDialogFactory> provider, Provider<MediaOutputBroadcastDialogFactory> provider2) {
        this.mediaOutputDialogFactoryProvider = provider;
        this.mediaOutputBroadcastDialogFactoryProvider = provider2;
    }

    public MediaOutputDialogReceiver get() {
        return newInstance(this.mediaOutputDialogFactoryProvider.get(), this.mediaOutputBroadcastDialogFactoryProvider.get());
    }

    public static MediaOutputDialogReceiver_Factory create(Provider<MediaOutputDialogFactory> provider, Provider<MediaOutputBroadcastDialogFactory> provider2) {
        return new MediaOutputDialogReceiver_Factory(provider, provider2);
    }

    public static MediaOutputDialogReceiver newInstance(MediaOutputDialogFactory mediaOutputDialogFactory, MediaOutputBroadcastDialogFactory mediaOutputBroadcastDialogFactory) {
        return new MediaOutputDialogReceiver(mediaOutputDialogFactory, mediaOutputBroadcastDialogFactory);
    }
}
