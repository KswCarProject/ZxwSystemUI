package com.android.systemui.statusbar;

import kotlin.jvm.functions.Function0;

/* compiled from: LockscreenShadeTransitionController.kt */
public final class LockscreenShadeTransitionController$onDraggedDown$cancelRunnable$1 implements Runnable {
    public final /* synthetic */ LockscreenShadeTransitionController this$0;

    public LockscreenShadeTransitionController$onDraggedDown$cancelRunnable$1(LockscreenShadeTransitionController lockscreenShadeTransitionController) {
        this.this$0 = lockscreenShadeTransitionController;
    }

    public final void run() {
        this.this$0.logger.logGoingToLockedShadeAborted();
        LockscreenShadeTransitionController.setDragDownAmountAnimated$default(this.this$0, 0.0f, 0, (Function0) null, 6, (Object) null);
    }
}
