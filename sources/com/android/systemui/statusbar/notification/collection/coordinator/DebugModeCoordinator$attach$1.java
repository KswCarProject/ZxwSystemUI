package com.android.systemui.statusbar.notification.collection.coordinator;

/* compiled from: DebugModeCoordinator.kt */
public /* synthetic */ class DebugModeCoordinator$attach$1 implements Runnable {
    public final /* synthetic */ DebugModeCoordinator$preGroupFilter$1 $tmp0;

    public DebugModeCoordinator$attach$1(DebugModeCoordinator$preGroupFilter$1 debugModeCoordinator$preGroupFilter$1) {
        this.$tmp0 = debugModeCoordinator$preGroupFilter$1;
    }

    public final void run() {
        this.$tmp0.invalidateList();
    }
}
