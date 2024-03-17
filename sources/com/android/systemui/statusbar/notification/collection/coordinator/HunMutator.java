package com.android.systemui.statusbar.notification.collection.coordinator;

import org.jetbrains.annotations.NotNull;

/* compiled from: HeadsUpCoordinator.kt */
public interface HunMutator {
    void removeNotification(@NotNull String str, boolean z);

    void updateNotification(@NotNull String str, boolean z);
}
