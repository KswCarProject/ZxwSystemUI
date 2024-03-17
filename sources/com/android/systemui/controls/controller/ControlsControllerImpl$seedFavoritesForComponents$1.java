package com.android.systemui.controls.controller;

import android.content.ComponentName;
import java.util.List;
import java.util.function.Consumer;

/* compiled from: ControlsControllerImpl.kt */
public final class ControlsControllerImpl$seedFavoritesForComponents$1 implements Runnable {
    public final /* synthetic */ Consumer<SeedResponse> $callback;
    public final /* synthetic */ List<ComponentName> $componentNames;
    public final /* synthetic */ ControlsControllerImpl this$0;

    public ControlsControllerImpl$seedFavoritesForComponents$1(ControlsControllerImpl controlsControllerImpl, List<ComponentName> list, Consumer<SeedResponse> consumer) {
        this.this$0 = controlsControllerImpl;
        this.$componentNames = list;
        this.$callback = consumer;
    }

    public final void run() {
        this.this$0.seedFavoritesForComponents(this.$componentNames, this.$callback);
    }
}
