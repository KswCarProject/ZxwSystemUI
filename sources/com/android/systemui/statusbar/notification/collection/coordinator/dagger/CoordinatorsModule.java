package com.android.systemui.statusbar.notification.collection.coordinator.dagger;

import com.android.systemui.statusbar.notification.collection.coordinator.NotifCoordinators;
import com.android.systemui.statusbar.notification.collection.coordinator.dagger.CoordinatorsSubcomponent;
import org.jetbrains.annotations.NotNull;

/* compiled from: CoordinatorsModule.kt */
public final class CoordinatorsModule {
    @NotNull
    public static final CoordinatorsModule INSTANCE = new CoordinatorsModule();

    @NotNull
    public static final NotifCoordinators notifCoordinators(@NotNull CoordinatorsSubcomponent.Factory factory) {
        return factory.create().getNotifCoordinators();
    }
}
