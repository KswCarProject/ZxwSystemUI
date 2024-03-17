package com.android.systemui.controls.controller;

import android.content.ComponentName;
import android.service.controls.Control;
import kotlin.collections.CollectionsKt__CollectionsJVMKt;

/* compiled from: ControlsControllerImpl.kt */
public final class ControlsControllerImpl$refreshStatus$1 implements Runnable {
    public final /* synthetic */ ComponentName $componentName;
    public final /* synthetic */ Control $control;
    public final /* synthetic */ ControlsControllerImpl this$0;

    public ControlsControllerImpl$refreshStatus$1(ComponentName componentName, Control control, ControlsControllerImpl controlsControllerImpl) {
        this.$componentName = componentName;
        this.$control = control;
        this.this$0 = controlsControllerImpl;
    }

    public final void run() {
        Favorites favorites = Favorites.INSTANCE;
        if (favorites.updateControls(this.$componentName, CollectionsKt__CollectionsJVMKt.listOf(this.$control))) {
            this.this$0.persistenceWrapper.storeFavorites(favorites.getAllStructures());
        }
    }
}
