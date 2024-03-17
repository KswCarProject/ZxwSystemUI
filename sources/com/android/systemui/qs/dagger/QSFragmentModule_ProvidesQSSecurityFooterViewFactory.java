package com.android.systemui.qs.dagger;

import android.view.LayoutInflater;
import android.view.View;
import com.android.systemui.qs.FooterActionsView;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class QSFragmentModule_ProvidesQSSecurityFooterViewFactory implements Factory<View> {
    public final Provider<FooterActionsView> footerActionsViewProvider;
    public final Provider<LayoutInflater> layoutInflaterProvider;

    public QSFragmentModule_ProvidesQSSecurityFooterViewFactory(Provider<LayoutInflater> provider, Provider<FooterActionsView> provider2) {
        this.layoutInflaterProvider = provider;
        this.footerActionsViewProvider = provider2;
    }

    public View get() {
        return providesQSSecurityFooterView(this.layoutInflaterProvider.get(), this.footerActionsViewProvider.get());
    }

    public static QSFragmentModule_ProvidesQSSecurityFooterViewFactory create(Provider<LayoutInflater> provider, Provider<FooterActionsView> provider2) {
        return new QSFragmentModule_ProvidesQSSecurityFooterViewFactory(provider, provider2);
    }

    public static View providesQSSecurityFooterView(LayoutInflater layoutInflater, FooterActionsView footerActionsView) {
        return (View) Preconditions.checkNotNullFromProvides(QSFragmentModule.providesQSSecurityFooterView(layoutInflater, footerActionsView));
    }
}
