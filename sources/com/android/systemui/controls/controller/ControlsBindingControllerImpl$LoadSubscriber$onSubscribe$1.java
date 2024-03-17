package com.android.systemui.controls.controller;

import android.service.controls.IControlsSubscription;
import com.android.systemui.controls.controller.ControlsBindingControllerImpl;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

/* compiled from: ControlsBindingControllerImpl.kt */
public final class ControlsBindingControllerImpl$LoadSubscriber$onSubscribe$1 extends Lambda implements Function0<Unit> {
    public final /* synthetic */ ControlsBindingControllerImpl this$0;
    public final /* synthetic */ ControlsBindingControllerImpl.LoadSubscriber this$1;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public ControlsBindingControllerImpl$LoadSubscriber$onSubscribe$1(ControlsBindingControllerImpl controlsBindingControllerImpl, ControlsBindingControllerImpl.LoadSubscriber loadSubscriber) {
        super(0);
        this.this$0 = controlsBindingControllerImpl;
        this.this$1 = loadSubscriber;
    }

    public final void invoke() {
        ControlsProviderLifecycleManager access$getCurrentProvider$p = this.this$0.currentProvider;
        if (access$getCurrentProvider$p != null) {
            IControlsSubscription access$getSubscription$p = this.this$1.subscription;
            if (access$getSubscription$p == null) {
                access$getSubscription$p = null;
            }
            access$getCurrentProvider$p.cancelSubscription(access$getSubscription$p);
        }
    }
}
