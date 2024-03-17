package com.android.systemui.qs.dagger;

import com.android.systemui.qs.QuickQSPanel;
import com.android.systemui.qs.QuickStatusBarHeader;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class QSFragmentModule_ProvidesQuickQSPanelFactory implements Factory<QuickQSPanel> {
    public final Provider<QuickStatusBarHeader> quickStatusBarHeaderProvider;

    public QSFragmentModule_ProvidesQuickQSPanelFactory(Provider<QuickStatusBarHeader> provider) {
        this.quickStatusBarHeaderProvider = provider;
    }

    public QuickQSPanel get() {
        return providesQuickQSPanel(this.quickStatusBarHeaderProvider.get());
    }

    public static QSFragmentModule_ProvidesQuickQSPanelFactory create(Provider<QuickStatusBarHeader> provider) {
        return new QSFragmentModule_ProvidesQuickQSPanelFactory(provider);
    }

    public static QuickQSPanel providesQuickQSPanel(QuickStatusBarHeader quickStatusBarHeader) {
        return (QuickQSPanel) Preconditions.checkNotNullFromProvides(QSFragmentModule.providesQuickQSPanel(quickStatusBarHeader));
    }
}
