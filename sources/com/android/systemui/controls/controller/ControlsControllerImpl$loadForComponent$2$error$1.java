package com.android.systemui.controls.controller;

import android.content.ComponentName;
import com.android.systemui.controls.ControlStatus;
import com.android.systemui.controls.controller.ControlsController;
import java.util.ArrayList;
import java.util.function.Consumer;
import kotlin.collections.CollectionsKt__IterablesKt;
import kotlin.collections.CollectionsKt__MutableCollectionsKt;

/* compiled from: ControlsControllerImpl.kt */
public final class ControlsControllerImpl$loadForComponent$2$error$1 implements Runnable {
    public final /* synthetic */ ComponentName $componentName;
    public final /* synthetic */ Consumer<ControlsController.LoadData> $dataCallback;
    public final /* synthetic */ ControlsControllerImpl this$0;

    public ControlsControllerImpl$loadForComponent$2$error$1(ComponentName componentName, Consumer<ControlsController.LoadData> consumer, ControlsControllerImpl controlsControllerImpl) {
        this.$componentName = componentName;
        this.$dataCallback = consumer;
        this.this$0 = controlsControllerImpl;
    }

    public final void run() {
        ControlsControllerImpl controlsControllerImpl = this.this$0;
        ComponentName componentName = this.$componentName;
        ArrayList<ControlStatus> arrayList = new ArrayList<>();
        for (StructureInfo structureInfo : Favorites.INSTANCE.getStructuresForComponent(this.$componentName)) {
            Iterable<ControlInfo> controls = structureInfo.getControls();
            ArrayList arrayList2 = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(controls, 10));
            for (ControlInfo access$createRemovedStatus : controls) {
                arrayList2.add(controlsControllerImpl.createRemovedStatus(componentName, access$createRemovedStatus, structureInfo.getStructure(), false));
            }
            CollectionsKt__MutableCollectionsKt.addAll(arrayList, arrayList2);
        }
        ArrayList arrayList3 = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(arrayList, 10));
        for (ControlStatus control : arrayList) {
            arrayList3.add(control.getControl().getControlId());
        }
        this.$dataCallback.accept(ControlsControllerKt.createLoadDataObject(arrayList, arrayList3, true));
    }
}
