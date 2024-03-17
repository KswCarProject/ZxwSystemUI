package com.android.systemui.controls.controller;

import android.content.ComponentName;
import android.service.controls.Control;
import com.android.systemui.controls.controller.ControlsBindingController;
import com.android.systemui.controls.controller.ControlsController;
import java.util.List;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;

/* compiled from: ControlsControllerImpl.kt */
public final class ControlsControllerImpl$loadForComponent$2 implements ControlsBindingController.LoadCallback {
    public final /* synthetic */ ComponentName $componentName;
    public final /* synthetic */ Consumer<ControlsController.LoadData> $dataCallback;
    public final /* synthetic */ ControlsControllerImpl this$0;

    public ControlsControllerImpl$loadForComponent$2(ControlsControllerImpl controlsControllerImpl, ComponentName componentName, Consumer<ControlsController.LoadData> consumer) {
        this.this$0 = controlsControllerImpl;
        this.$componentName = componentName;
        this.$dataCallback = consumer;
    }

    public void accept(@NotNull List<Control> list) {
        this.this$0.executor.execute(new ControlsControllerImpl$loadForComponent$2$accept$1(this.$componentName, list, this.this$0, this.$dataCallback));
    }

    public void error(@NotNull String str) {
        this.this$0.executor.execute(new ControlsControllerImpl$loadForComponent$2$error$1(this.$componentName, this.$dataCallback, this.this$0));
    }
}
