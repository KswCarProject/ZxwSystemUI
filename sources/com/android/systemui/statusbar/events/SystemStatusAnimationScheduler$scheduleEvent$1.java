package com.android.systemui.statusbar.events;

/* compiled from: SystemStatusAnimationScheduler.kt */
public final class SystemStatusAnimationScheduler$scheduleEvent$1 implements Runnable {
    public final /* synthetic */ SystemStatusAnimationScheduler this$0;

    public SystemStatusAnimationScheduler$scheduleEvent$1(SystemStatusAnimationScheduler systemStatusAnimationScheduler) {
        this.this$0 = systemStatusAnimationScheduler;
    }

    public final void run() {
        this.this$0.runChipAnimation();
    }
}
