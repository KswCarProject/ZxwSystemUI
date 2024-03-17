package com.android.systemui.biometrics;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

/* compiled from: SidefpsController.kt */
public final class SidefpsController$orientationListener$1 extends Lambda implements Function0<Unit> {
    public final /* synthetic */ SidefpsController this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public SidefpsController$orientationListener$1(SidefpsController sidefpsController) {
        super(0);
        this.this$0 = sidefpsController;
    }

    public final void invoke() {
        this.this$0.onOrientationChanged();
    }
}
