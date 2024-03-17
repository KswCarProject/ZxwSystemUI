package com.android.systemui.statusbar.notification.collection.legacy;

import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.people.PeopleNotificationIdentifier;
import com.android.wm.shell.bubbles.Bubbles;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;

public final class NotificationGroupManagerLegacy_Factory implements Factory<NotificationGroupManagerLegacy> {
    public final Provider<Optional<Bubbles>> bubblesOptionalProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<PeopleNotificationIdentifier> peopleNotificationIdentifierProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;

    public NotificationGroupManagerLegacy_Factory(Provider<StatusBarStateController> provider, Provider<PeopleNotificationIdentifier> provider2, Provider<Optional<Bubbles>> provider3, Provider<DumpManager> provider4) {
        this.statusBarStateControllerProvider = provider;
        this.peopleNotificationIdentifierProvider = provider2;
        this.bubblesOptionalProvider = provider3;
        this.dumpManagerProvider = provider4;
    }

    public NotificationGroupManagerLegacy get() {
        return newInstance(this.statusBarStateControllerProvider.get(), DoubleCheck.lazy(this.peopleNotificationIdentifierProvider), this.bubblesOptionalProvider.get(), this.dumpManagerProvider.get());
    }

    public static NotificationGroupManagerLegacy_Factory create(Provider<StatusBarStateController> provider, Provider<PeopleNotificationIdentifier> provider2, Provider<Optional<Bubbles>> provider3, Provider<DumpManager> provider4) {
        return new NotificationGroupManagerLegacy_Factory(provider, provider2, provider3, provider4);
    }

    public static NotificationGroupManagerLegacy newInstance(StatusBarStateController statusBarStateController, Lazy<PeopleNotificationIdentifier> lazy, Optional<Bubbles> optional, DumpManager dumpManager) {
        return new NotificationGroupManagerLegacy(statusBarStateController, lazy, optional, dumpManager);
    }
}
