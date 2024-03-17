package com.android.systemui.statusbar;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

/* compiled from: LockscreenShadeTransitionController.kt */
public final class LockscreenShadeTransitionController$animateAppear$1 extends Lambda implements Function0<Unit> {
    public final /* synthetic */ LockscreenShadeTransitionController this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public LockscreenShadeTransitionController$animateAppear$1(LockscreenShadeTransitionController lockscreenShadeTransitionController) {
        super(0);
        this.this$0 = lockscreenShadeTransitionController;
    }

    public final void invoke() {
        this.this$0.logger.logDragDownAmountReset();
        this.this$0.setDragDownAmount$frameworks__base__packages__SystemUI__android_common__SystemUI_core(0.0f);
        this.this$0.forceApplyAmount = false;
    }
}
