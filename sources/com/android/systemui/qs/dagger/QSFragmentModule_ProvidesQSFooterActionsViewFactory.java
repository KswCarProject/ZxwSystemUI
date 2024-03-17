package com.android.systemui.qs.dagger;

import android.view.View;
import com.android.systemui.qs.FooterActionsView;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class QSFragmentModule_ProvidesQSFooterActionsViewFactory implements Factory<FooterActionsView> {
    public final Provider<View> viewProvider;

    public QSFragmentModule_ProvidesQSFooterActionsViewFactory(Provider<View> provider) {
        this.viewProvider = provider;
    }

    public FooterActionsView get() {
        return providesQSFooterActionsView(this.viewProvider.get());
    }

    public static QSFragmentModule_ProvidesQSFooterActionsViewFactory create(Provider<View> provider) {
        return new QSFragmentModule_ProvidesQSFooterActionsViewFactory(provider);
    }

    public static FooterActionsView providesQSFooterActionsView(View view) {
        return (FooterActionsView) Preconditions.checkNotNullFromProvides(QSFragmentModule.providesQSFooterActionsView(view));
    }
}
