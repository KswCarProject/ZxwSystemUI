package com.android.systemui.controls.ui;

import com.android.systemui.controls.ControlsServiceInfo;
import com.android.systemui.controls.management.ControlsListingController;
import java.util.ArrayList;
import java.util.List;
import kotlin.Unit;
import kotlin.collections.CollectionsKt__IterablesKt;
import kotlin.jvm.functions.Function1;
import org.jetbrains.annotations.NotNull;

/* compiled from: ControlsUiControllerImpl.kt */
public final class ControlsUiControllerImpl$createCallback$1 implements ControlsListingController.ControlsListingCallback {
    public final /* synthetic */ Function1<List<SelectionItem>, Unit> $onResult;
    public final /* synthetic */ ControlsUiControllerImpl this$0;

    public ControlsUiControllerImpl$createCallback$1(ControlsUiControllerImpl controlsUiControllerImpl, Function1<? super List<SelectionItem>, Unit> function1) {
        this.this$0 = controlsUiControllerImpl;
        this.$onResult = function1;
    }

    public void onServicesUpdated(@NotNull List<ControlsServiceInfo> list) {
        Iterable<ControlsServiceInfo> iterable = list;
        ArrayList arrayList = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(iterable, 10));
        for (ControlsServiceInfo controlsServiceInfo : iterable) {
            arrayList.add(new SelectionItem(controlsServiceInfo.loadLabel(), "", controlsServiceInfo.loadIcon(), controlsServiceInfo.componentName, controlsServiceInfo.getServiceInfo().applicationInfo.uid));
        }
        this.this$0.getUiExecutor().execute(new ControlsUiControllerImpl$createCallback$1$onServicesUpdated$1(this.this$0, arrayList, this.$onResult));
    }
}
