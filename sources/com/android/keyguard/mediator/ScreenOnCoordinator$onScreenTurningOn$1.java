package com.android.keyguard.mediator;

/* compiled from: ScreenOnCoordinator.kt */
public final class ScreenOnCoordinator$onScreenTurningOn$1 implements Runnable {
    public final /* synthetic */ Runnable $onDrawn;

    public ScreenOnCoordinator$onScreenTurningOn$1(Runnable runnable) {
        this.$onDrawn = runnable;
    }

    public final void run() {
        this.$onDrawn.run();
    }
}
