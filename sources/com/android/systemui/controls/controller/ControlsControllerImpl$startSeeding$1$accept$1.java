package com.android.systemui.controls.controller;

import android.content.ComponentName;
import android.service.controls.Control;
import android.util.ArrayMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/* compiled from: ControlsControllerImpl.kt */
public final class ControlsControllerImpl$startSeeding$1$accept$1 implements Runnable {
    public final /* synthetic */ Consumer<SeedResponse> $callback;
    public final /* synthetic */ ComponentName $componentName;
    public final /* synthetic */ List<Control> $controls;
    public final /* synthetic */ boolean $didAnyFail;
    public final /* synthetic */ List<ComponentName> $remaining;
    public final /* synthetic */ ControlsControllerImpl this$0;

    public ControlsControllerImpl$startSeeding$1$accept$1(List<Control> list, ControlsControllerImpl controlsControllerImpl, Consumer<SeedResponse> consumer, ComponentName componentName, List<ComponentName> list2, boolean z) {
        this.$controls = list;
        this.this$0 = controlsControllerImpl;
        this.$callback = consumer;
        this.$componentName = componentName;
        this.$remaining = list2;
        this.$didAnyFail = z;
    }

    public final void run() {
        ArrayMap arrayMap = new ArrayMap();
        for (Control control : this.$controls) {
            Object structure = control.getStructure();
            if (structure == null) {
                structure = "";
            }
            List list = (List) arrayMap.get(structure);
            if (list == null) {
                list = new ArrayList();
            }
            if (list.size() < 6) {
                list.add(new ControlInfo(control.getControlId(), control.getTitle(), control.getSubtitle(), control.getDeviceType()));
                arrayMap.put(structure, list);
            }
        }
        ComponentName componentName = this.$componentName;
        for (Map.Entry entry : arrayMap.entrySet()) {
            Favorites.INSTANCE.replaceControls(new StructureInfo(componentName, (CharSequence) entry.getKey(), (List) entry.getValue()));
        }
        this.this$0.persistenceWrapper.storeFavorites(Favorites.INSTANCE.getAllStructures());
        this.$callback.accept(new SeedResponse(this.$componentName.getPackageName(), true));
        this.this$0.startSeeding(this.$remaining, this.$callback, this.$didAnyFail);
    }
}
