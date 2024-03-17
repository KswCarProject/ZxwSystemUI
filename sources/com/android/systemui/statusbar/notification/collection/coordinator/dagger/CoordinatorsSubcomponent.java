package com.android.systemui.statusbar.notification.collection.coordinator.dagger;

import com.android.systemui.statusbar.notification.collection.coordinator.NotifCoordinators;
import org.jetbrains.annotations.NotNull;

/* compiled from: CoordinatorsModule.kt */
public interface CoordinatorsSubcomponent {

    /* compiled from: CoordinatorsModule.kt */
    public interface Factory {
        @NotNull
        CoordinatorsSubcomponent create();
    }

    @NotNull
    NotifCoordinators getNotifCoordinators();
}
