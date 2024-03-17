package com.android.systemui.statusbar.phone.shade.transition;

import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: ShadeTransitionController.kt */
public final class ShadeTransitionController$splitShadeOverScroller$2 extends Lambda implements Function0<SplitShadeOverScroller> {
    public final /* synthetic */ ShadeTransitionController this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public ShadeTransitionController$splitShadeOverScroller$2(ShadeTransitionController shadeTransitionController) {
        super(0);
        this.this$0 = shadeTransitionController;
    }

    @NotNull
    public final SplitShadeOverScroller invoke() {
        return this.this$0.splitShadeOverScrollerFactory.create(this.this$0.getQs(), this.this$0.getNotificationStackScrollLayoutController());
    }
}
