package com.android.systemui.qs.dagger;

import android.view.View;
import com.android.systemui.qs.QSContainerImpl;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class QSFragmentModule_ProvidesQSContainerImplFactory implements Factory<QSContainerImpl> {
    public final Provider<View> viewProvider;

    public QSFragmentModule_ProvidesQSContainerImplFactory(Provider<View> provider) {
        this.viewProvider = provider;
    }

    public QSContainerImpl get() {
        return providesQSContainerImpl(this.viewProvider.get());
    }

    public static QSFragmentModule_ProvidesQSContainerImplFactory create(Provider<View> provider) {
        return new QSFragmentModule_ProvidesQSContainerImplFactory(provider);
    }

    public static QSContainerImpl providesQSContainerImpl(View view) {
        return (QSContainerImpl) Preconditions.checkNotNullFromProvides(QSFragmentModule.providesQSContainerImpl(view));
    }
}
