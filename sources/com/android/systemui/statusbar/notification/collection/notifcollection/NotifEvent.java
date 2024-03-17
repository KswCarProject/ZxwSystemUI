package com.android.systemui.statusbar.notification.collection.notifcollection;

import java.util.List;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;

/* compiled from: NotifEvent.kt */
public abstract class NotifEvent {
    public /* synthetic */ NotifEvent(DefaultConstructorMarker defaultConstructorMarker) {
        this();
    }

    public abstract void dispatchToListener(@NotNull NotifCollectionListener notifCollectionListener);

    public NotifEvent() {
    }

    public final void dispatchTo(@NotNull List<? extends NotifCollectionListener> list) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            dispatchToListener((NotifCollectionListener) list.get(i));
        }
    }
}
