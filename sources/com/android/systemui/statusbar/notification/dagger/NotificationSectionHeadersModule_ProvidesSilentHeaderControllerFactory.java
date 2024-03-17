package com.android.systemui.statusbar.notification.dagger;

import com.android.systemui.statusbar.notification.collection.render.SectionHeaderController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class NotificationSectionHeadersModule_ProvidesSilentHeaderControllerFactory implements Factory<SectionHeaderController> {
    public final Provider<SectionHeaderControllerSubcomponent> subcomponentProvider;

    public NotificationSectionHeadersModule_ProvidesSilentHeaderControllerFactory(Provider<SectionHeaderControllerSubcomponent> provider) {
        this.subcomponentProvider = provider;
    }

    public SectionHeaderController get() {
        return providesSilentHeaderController(this.subcomponentProvider.get());
    }

    public static NotificationSectionHeadersModule_ProvidesSilentHeaderControllerFactory create(Provider<SectionHeaderControllerSubcomponent> provider) {
        return new NotificationSectionHeadersModule_ProvidesSilentHeaderControllerFactory(provider);
    }

    public static SectionHeaderController providesSilentHeaderController(SectionHeaderControllerSubcomponent sectionHeaderControllerSubcomponent) {
        return (SectionHeaderController) Preconditions.checkNotNullFromProvides(NotificationSectionHeadersModule.providesSilentHeaderController(sectionHeaderControllerSubcomponent));
    }
}
