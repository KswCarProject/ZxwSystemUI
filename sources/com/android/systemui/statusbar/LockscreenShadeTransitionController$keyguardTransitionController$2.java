package com.android.systemui.statusbar;

import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: LockscreenShadeTransitionController.kt */
public final class LockscreenShadeTransitionController$keyguardTransitionController$2 extends Lambda implements Function0<LockscreenShadeKeyguardTransitionController> {
    public final /* synthetic */ LockscreenShadeTransitionController this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public LockscreenShadeTransitionController$keyguardTransitionController$2(LockscreenShadeTransitionController lockscreenShadeTransitionController) {
        super(0);
        this.this$0 = lockscreenShadeTransitionController;
    }

    @NotNull
    public final LockscreenShadeKeyguardTransitionController invoke() {
        return this.this$0.keyguardTransitionControllerFactory.create(this.this$0.getNotificationPanelController());
    }
}
