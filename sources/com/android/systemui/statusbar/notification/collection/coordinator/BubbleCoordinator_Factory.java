package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.NotifCollection;
import com.android.systemui.wmshell.BubblesManager;
import com.android.wm.shell.bubbles.Bubbles;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;

public final class BubbleCoordinator_Factory implements Factory<BubbleCoordinator> {
    public final Provider<Optional<BubblesManager>> bubblesManagerOptionalProvider;
    public final Provider<Optional<Bubbles>> bubblesOptionalProvider;
    public final Provider<NotifCollection> notifCollectionProvider;

    public BubbleCoordinator_Factory(Provider<Optional<BubblesManager>> provider, Provider<Optional<Bubbles>> provider2, Provider<NotifCollection> provider3) {
        this.bubblesManagerOptionalProvider = provider;
        this.bubblesOptionalProvider = provider2;
        this.notifCollectionProvider = provider3;
    }

    public BubbleCoordinator get() {
        return newInstance(this.bubblesManagerOptionalProvider.get(), this.bubblesOptionalProvider.get(), this.notifCollectionProvider.get());
    }

    public static BubbleCoordinator_Factory create(Provider<Optional<BubblesManager>> provider, Provider<Optional<Bubbles>> provider2, Provider<NotifCollection> provider3) {
        return new BubbleCoordinator_Factory(provider, provider2, provider3);
    }

    public static BubbleCoordinator newInstance(Optional<BubblesManager> optional, Optional<Bubbles> optional2, NotifCollection notifCollection) {
        return new BubbleCoordinator(optional, optional2, notifCollection);
    }
}
