package com.android.systemui.qs.dagger;

import android.view.View;
import com.android.systemui.qs.QSFooterView;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class QSFragmentModule_ProvidesQSFooterViewFactory implements Factory<QSFooterView> {
    public final Provider<View> viewProvider;

    public QSFragmentModule_ProvidesQSFooterViewFactory(Provider<View> provider) {
        this.viewProvider = provider;
    }

    public QSFooterView get() {
        return providesQSFooterView(this.viewProvider.get());
    }

    public static QSFragmentModule_ProvidesQSFooterViewFactory create(Provider<View> provider) {
        return new QSFragmentModule_ProvidesQSFooterViewFactory(provider);
    }

    public static QSFooterView providesQSFooterView(View view) {
        return (QSFooterView) Preconditions.checkNotNullFromProvides(QSFragmentModule.providesQSFooterView(view));
    }
}
