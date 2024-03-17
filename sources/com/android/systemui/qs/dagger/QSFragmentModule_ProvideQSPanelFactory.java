package com.android.systemui.qs.dagger;

import android.view.View;
import com.android.systemui.qs.QSPanel;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class QSFragmentModule_ProvideQSPanelFactory implements Factory<QSPanel> {
    public final Provider<View> viewProvider;

    public QSFragmentModule_ProvideQSPanelFactory(Provider<View> provider) {
        this.viewProvider = provider;
    }

    public QSPanel get() {
        return provideQSPanel(this.viewProvider.get());
    }

    public static QSFragmentModule_ProvideQSPanelFactory create(Provider<View> provider) {
        return new QSFragmentModule_ProvideQSPanelFactory(provider);
    }

    public static QSPanel provideQSPanel(View view) {
        return (QSPanel) Preconditions.checkNotNullFromProvides(QSFragmentModule.provideQSPanel(view));
    }
}
