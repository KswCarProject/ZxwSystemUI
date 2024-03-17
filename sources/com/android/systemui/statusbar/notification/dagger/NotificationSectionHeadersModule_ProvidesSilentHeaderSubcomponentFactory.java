package com.android.systemui.statusbar.notification.dagger;

import com.android.systemui.statusbar.notification.dagger.SectionHeaderControllerSubcomponent;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class NotificationSectionHeadersModule_ProvidesSilentHeaderSubcomponentFactory implements Factory<SectionHeaderControllerSubcomponent> {
    public final Provider<SectionHeaderControllerSubcomponent.Builder> builderProvider;

    public NotificationSectionHeadersModule_ProvidesSilentHeaderSubcomponentFactory(Provider<SectionHeaderControllerSubcomponent.Builder> provider) {
        this.builderProvider = provider;
    }

    public SectionHeaderControllerSubcomponent get() {
        return providesSilentHeaderSubcomponent(this.builderProvider);
    }

    public static NotificationSectionHeadersModule_ProvidesSilentHeaderSubcomponentFactory create(Provider<SectionHeaderControllerSubcomponent.Builder> provider) {
        return new NotificationSectionHeadersModule_ProvidesSilentHeaderSubcomponentFactory(provider);
    }

    public static SectionHeaderControllerSubcomponent providesSilentHeaderSubcomponent(Provider<SectionHeaderControllerSubcomponent.Builder> provider) {
        return (SectionHeaderControllerSubcomponent) Preconditions.checkNotNullFromProvides(NotificationSectionHeadersModule.providesSilentHeaderSubcomponent(provider));
    }
}
