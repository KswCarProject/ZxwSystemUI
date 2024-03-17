package com.android.systemui.controls.controller;

import android.os.IBinder;
import android.service.controls.Control;
import android.service.controls.IControlsSubscription;
import com.android.systemui.controls.controller.ControlsBindingControllerImpl;
import java.util.ArrayList;

/* compiled from: ControlsBindingControllerImpl.kt */
public final class ControlsBindingControllerImpl$LoadSubscriber$onNext$1 implements Runnable {
    public final /* synthetic */ Control $c;
    public final /* synthetic */ IBinder $token;
    public final /* synthetic */ ControlsBindingControllerImpl.LoadSubscriber this$0;
    public final /* synthetic */ ControlsBindingControllerImpl this$1;

    public ControlsBindingControllerImpl$LoadSubscriber$onNext$1(ControlsBindingControllerImpl.LoadSubscriber loadSubscriber, Control control, ControlsBindingControllerImpl controlsBindingControllerImpl, IBinder iBinder) {
        this.this$0 = loadSubscriber;
        this.$c = control;
        this.this$1 = controlsBindingControllerImpl;
        this.$token = iBinder;
    }

    public final void run() {
        if (!this.this$0.isTerminated.get()) {
            this.this$0.getLoadedControls().add(this.$c);
            if (((long) this.this$0.getLoadedControls().size()) >= this.this$0.getRequestLimit()) {
                ControlsBindingControllerImpl.LoadSubscriber loadSubscriber = this.this$0;
                ControlsBindingControllerImpl controlsBindingControllerImpl = this.this$1;
                IBinder iBinder = this.$token;
                ArrayList<Control> loadedControls = loadSubscriber.getLoadedControls();
                IControlsSubscription access$getSubscription$p = this.this$0.subscription;
                if (access$getSubscription$p == null) {
                    access$getSubscription$p = null;
                }
                loadSubscriber.maybeTerminateAndRun(new ControlsBindingControllerImpl.OnCancelAndLoadRunnable(iBinder, loadedControls, access$getSubscription$p, this.this$0.getCallback()));
            }
        }
    }
}
