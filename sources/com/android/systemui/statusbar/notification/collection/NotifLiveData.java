package com.android.systemui.statusbar.notification.collection;

import androidx.lifecycle.Observer;
import org.jetbrains.annotations.NotNull;

/* compiled from: NotifLiveDataStore.kt */
public interface NotifLiveData<T> {
    void addSyncObserver(@NotNull Observer<T> observer);

    T getValue();

    void removeObserver(@NotNull Observer<T> observer);
}
