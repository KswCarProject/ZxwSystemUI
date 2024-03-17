package com.android.systemui.statusbar.notification.collection;

import java.util.List;
import org.jetbrains.annotations.NotNull;

/* compiled from: NotifLiveDataStore.kt */
public interface NotifLiveDataStore {
    @NotNull
    NotifLiveData<Integer> getActiveNotifCount();

    @NotNull
    NotifLiveData<List<NotificationEntry>> getActiveNotifList();

    @NotNull
    NotifLiveData<Boolean> getHasActiveNotifs();
}
