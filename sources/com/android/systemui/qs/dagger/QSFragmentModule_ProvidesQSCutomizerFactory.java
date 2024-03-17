package com.android.systemui.qs.dagger;

import android.view.View;
import com.android.systemui.qs.customize.QSCustomizer;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class QSFragmentModule_ProvidesQSCutomizerFactory implements Factory<QSCustomizer> {
    public final Provider<View> viewProvider;

    public QSFragmentModule_ProvidesQSCutomizerFactory(Provider<View> provider) {
        this.viewProvider = provider;
    }

    public QSCustomizer get() {
        return providesQSCutomizer(this.viewProvider.get());
    }

    public static QSFragmentModule_ProvidesQSCutomizerFactory create(Provider<View> provider) {
        return new QSFragmentModule_ProvidesQSCutomizerFactory(provider);
    }

    public static QSCustomizer providesQSCutomizer(View view) {
        return (QSCustomizer) Preconditions.checkNotNullFromProvides(QSFragmentModule.providesQSCutomizer(view));
    }
}
