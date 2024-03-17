package com.android.systemui.privacy.television;

import android.content.Context;
import android.view.IWindowManager;
import com.android.systemui.privacy.PrivacyItemController;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class TvOngoingPrivacyChip_Factory implements Factory<TvOngoingPrivacyChip> {
    public final Provider<Context> contextProvider;
    public final Provider<IWindowManager> iWindowManagerProvider;
    public final Provider<PrivacyItemController> privacyItemControllerProvider;

    public TvOngoingPrivacyChip_Factory(Provider<Context> provider, Provider<PrivacyItemController> provider2, Provider<IWindowManager> provider3) {
        this.contextProvider = provider;
        this.privacyItemControllerProvider = provider2;
        this.iWindowManagerProvider = provider3;
    }

    public TvOngoingPrivacyChip get() {
        return newInstance(this.contextProvider.get(), this.privacyItemControllerProvider.get(), this.iWindowManagerProvider.get());
    }

    public static TvOngoingPrivacyChip_Factory create(Provider<Context> provider, Provider<PrivacyItemController> provider2, Provider<IWindowManager> provider3) {
        return new TvOngoingPrivacyChip_Factory(provider, provider2, provider3);
    }

    public static TvOngoingPrivacyChip newInstance(Context context, PrivacyItemController privacyItemController, IWindowManager iWindowManager) {
        return new TvOngoingPrivacyChip(context, privacyItemController, iWindowManager);
    }
}
