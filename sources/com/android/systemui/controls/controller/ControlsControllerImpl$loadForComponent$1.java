package com.android.systemui.controls.controller;

import android.content.ComponentName;
import com.android.systemui.controls.controller.ControlsController;
import java.util.function.Consumer;

/* compiled from: ControlsControllerImpl.kt */
public final class ControlsControllerImpl$loadForComponent$1 implements Runnable {
    public final /* synthetic */ Consumer<Runnable> $cancelWrapper;
    public final /* synthetic */ ComponentName $componentName;
    public final /* synthetic */ Consumer<ControlsController.LoadData> $dataCallback;
    public final /* synthetic */ ControlsControllerImpl this$0;

    public ControlsControllerImpl$loadForComponent$1(ControlsControllerImpl controlsControllerImpl, ComponentName componentName, Consumer<ControlsController.LoadData> consumer, Consumer<Runnable> consumer2) {
        this.this$0 = controlsControllerImpl;
        this.$componentName = componentName;
        this.$dataCallback = consumer;
        this.$cancelWrapper = consumer2;
    }

    public final void run() {
        this.this$0.loadForComponent(this.$componentName, this.$dataCallback, this.$cancelWrapper);
    }
}
