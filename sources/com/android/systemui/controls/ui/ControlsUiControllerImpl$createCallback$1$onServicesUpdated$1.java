package com.android.systemui.controls.ui;

import android.view.ViewGroup;
import java.util.List;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

/* compiled from: ControlsUiControllerImpl.kt */
public final class ControlsUiControllerImpl$createCallback$1$onServicesUpdated$1 implements Runnable {
    public final /* synthetic */ List<SelectionItem> $lastItems;
    public final /* synthetic */ Function1<List<SelectionItem>, Unit> $onResult;
    public final /* synthetic */ ControlsUiControllerImpl this$0;

    public ControlsUiControllerImpl$createCallback$1$onServicesUpdated$1(ControlsUiControllerImpl controlsUiControllerImpl, List<SelectionItem> list, Function1<? super List<SelectionItem>, Unit> function1) {
        this.this$0 = controlsUiControllerImpl;
        this.$lastItems = list;
        this.$onResult = function1;
    }

    public final void run() {
        ViewGroup access$getParent$p = this.this$0.parent;
        if (access$getParent$p == null) {
            access$getParent$p = null;
        }
        access$getParent$p.removeAllViews();
        if (this.$lastItems.size() > 0) {
            this.$onResult.invoke(this.$lastItems);
        }
    }
}
