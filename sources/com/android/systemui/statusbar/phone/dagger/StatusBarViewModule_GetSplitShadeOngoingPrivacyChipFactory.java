package com.android.systemui.statusbar.phone.dagger;

import android.view.View;
import com.android.systemui.privacy.OngoingPrivacyChip;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class StatusBarViewModule_GetSplitShadeOngoingPrivacyChipFactory implements Factory<OngoingPrivacyChip> {
    public final Provider<View> headerProvider;

    public StatusBarViewModule_GetSplitShadeOngoingPrivacyChipFactory(Provider<View> provider) {
        this.headerProvider = provider;
    }

    public OngoingPrivacyChip get() {
        return getSplitShadeOngoingPrivacyChip(this.headerProvider.get());
    }

    public static StatusBarViewModule_GetSplitShadeOngoingPrivacyChipFactory create(Provider<View> provider) {
        return new StatusBarViewModule_GetSplitShadeOngoingPrivacyChipFactory(provider);
    }

    public static OngoingPrivacyChip getSplitShadeOngoingPrivacyChip(View view) {
        return (OngoingPrivacyChip) Preconditions.checkNotNullFromProvides(StatusBarViewModule.getSplitShadeOngoingPrivacyChip(view));
    }
}
