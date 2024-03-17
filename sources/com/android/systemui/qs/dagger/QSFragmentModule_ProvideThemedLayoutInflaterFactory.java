package com.android.systemui.qs.dagger;

import android.content.Context;
import android.view.LayoutInflater;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class QSFragmentModule_ProvideThemedLayoutInflaterFactory implements Factory<LayoutInflater> {
    public final Provider<Context> contextProvider;

    public QSFragmentModule_ProvideThemedLayoutInflaterFactory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    public LayoutInflater get() {
        return provideThemedLayoutInflater(this.contextProvider.get());
    }

    public static QSFragmentModule_ProvideThemedLayoutInflaterFactory create(Provider<Context> provider) {
        return new QSFragmentModule_ProvideThemedLayoutInflaterFactory(provider);
    }

    public static LayoutInflater provideThemedLayoutInflater(Context context) {
        return (LayoutInflater) Preconditions.checkNotNullFromProvides(QSFragmentModule.provideThemedLayoutInflater(context));
    }
}
