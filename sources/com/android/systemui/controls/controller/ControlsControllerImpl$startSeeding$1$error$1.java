package com.android.systemui.controls.controller;

import android.content.ComponentName;
import java.util.List;
import java.util.function.Consumer;

/* compiled from: ControlsControllerImpl.kt */
public final class ControlsControllerImpl$startSeeding$1$error$1 implements Runnable {
    public final /* synthetic */ Consumer<SeedResponse> $callback;
    public final /* synthetic */ ComponentName $componentName;
    public final /* synthetic */ List<ComponentName> $remaining;
    public final /* synthetic */ ControlsControllerImpl this$0;

    public ControlsControllerImpl$startSeeding$1$error$1(Consumer<SeedResponse> consumer, ComponentName componentName, ControlsControllerImpl controlsControllerImpl, List<ComponentName> list) {
        this.$callback = consumer;
        this.$componentName = componentName;
        this.this$0 = controlsControllerImpl;
        this.$remaining = list;
    }

    public final void run() {
        this.$callback.accept(new SeedResponse(this.$componentName.getPackageName(), false));
        this.this$0.startSeeding(this.$remaining, this.$callback, true);
    }
}
