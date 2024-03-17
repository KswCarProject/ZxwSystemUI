package com.android.systemui.broadcast;

import android.content.Context;
import com.android.systemui.CoreStartable;
import org.jetbrains.annotations.NotNull;

/* compiled from: BroadcastDispatcherStartable.kt */
public final class BroadcastDispatcherStartable extends CoreStartable {
    @NotNull
    public final BroadcastDispatcher broadcastDispatcher;

    public BroadcastDispatcherStartable(@NotNull Context context, @NotNull BroadcastDispatcher broadcastDispatcher2) {
        super(context);
        this.broadcastDispatcher = broadcastDispatcher2;
    }

    public void start() {
        this.broadcastDispatcher.initialize();
    }
}
