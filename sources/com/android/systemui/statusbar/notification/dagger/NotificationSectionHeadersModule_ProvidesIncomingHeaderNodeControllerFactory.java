package com.android.systemui.statusbar.notification.dagger;

import com.android.systemui.statusbar.notification.collection.render.NodeController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class NotificationSectionHeadersModule_ProvidesIncomingHeaderNodeControllerFactory implements Factory<NodeController> {
    public final Provider<SectionHeaderControllerSubcomponent> subcomponentProvider;

    public NotificationSectionHeadersModule_ProvidesIncomingHeaderNodeControllerFactory(Provider<SectionHeaderControllerSubcomponent> provider) {
        this.subcomponentProvider = provider;
    }

    public NodeController get() {
        return providesIncomingHeaderNodeController(this.subcomponentProvider.get());
    }

    public static NotificationSectionHeadersModule_ProvidesIncomingHeaderNodeControllerFactory create(Provider<SectionHeaderControllerSubcomponent> provider) {
        return new NotificationSectionHeadersModule_ProvidesIncomingHeaderNodeControllerFactory(provider);
    }

    public static NodeController providesIncomingHeaderNodeController(SectionHeaderControllerSubcomponent sectionHeaderControllerSubcomponent) {
        return (NodeController) Preconditions.checkNotNullFromProvides(NotificationSectionHeadersModule.providesIncomingHeaderNodeController(sectionHeaderControllerSubcomponent));
    }
}
