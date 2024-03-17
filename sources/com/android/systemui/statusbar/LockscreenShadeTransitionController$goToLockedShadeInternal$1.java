package com.android.systemui.statusbar;

import com.android.systemui.plugins.ActivityStarter;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

/* compiled from: LockscreenShadeTransitionController.kt */
public final class LockscreenShadeTransitionController$goToLockedShadeInternal$1 implements ActivityStarter.OnDismissAction {
    public final /* synthetic */ Function1<Long, Unit> $animationHandler;
    public final /* synthetic */ LockscreenShadeTransitionController this$0;

    public LockscreenShadeTransitionController$goToLockedShadeInternal$1(LockscreenShadeTransitionController lockscreenShadeTransitionController, Function1<? super Long, Unit> function1) {
        this.this$0 = lockscreenShadeTransitionController;
        this.$animationHandler = function1;
    }

    public final boolean onDismiss() {
        this.this$0.animationHandlerOnKeyguardDismiss = this.$animationHandler;
        return false;
    }
}
