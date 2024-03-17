package com.android.systemui.qs.dagger;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class QSFragmentModule_ProvidesQSUsingMediaPlayerFactory implements Factory<Boolean> {
    public final Provider<Context> contextProvider;

    public QSFragmentModule_ProvidesQSUsingMediaPlayerFactory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    public Boolean get() {
        return Boolean.valueOf(providesQSUsingMediaPlayer(this.contextProvider.get()));
    }

    public static QSFragmentModule_ProvidesQSUsingMediaPlayerFactory create(Provider<Context> provider) {
        return new QSFragmentModule_ProvidesQSUsingMediaPlayerFactory(provider);
    }

    public static boolean providesQSUsingMediaPlayer(Context context) {
        return QSFragmentModule.providesQSUsingMediaPlayer(context);
    }
}
