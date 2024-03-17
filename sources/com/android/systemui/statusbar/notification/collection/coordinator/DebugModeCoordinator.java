package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.provider.DebugModeFilterProvider;
import org.jetbrains.annotations.NotNull;

/* compiled from: DebugModeCoordinator.kt */
public final class DebugModeCoordinator implements Coordinator {
    @NotNull
    public final DebugModeFilterProvider debugModeFilterProvider;
    @NotNull
    public final DebugModeCoordinator$preGroupFilter$1 preGroupFilter = new DebugModeCoordinator$preGroupFilter$1(this);

    public DebugModeCoordinator(@NotNull DebugModeFilterProvider debugModeFilterProvider2) {
        this.debugModeFilterProvider = debugModeFilterProvider2;
    }

    public void attach(@NotNull NotifPipeline notifPipeline) {
        notifPipeline.addPreGroupFilter(this.preGroupFilter);
        this.debugModeFilterProvider.registerInvalidationListener(new DebugModeCoordinator$attach$1(this.preGroupFilter));
    }
}
