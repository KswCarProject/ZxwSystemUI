package com.android.systemui.globalactions;

import android.view.View;
import android.widget.AdapterView;
import com.android.systemui.globalactions.GlobalActionsDialogLite;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class GlobalActionsDialogLite$ActionsDialogLite$$ExternalSyntheticLambda7 implements AdapterView.OnItemLongClickListener {
    public final /* synthetic */ GlobalActionsDialogLite.ActionsDialogLite f$0;

    public /* synthetic */ GlobalActionsDialogLite$ActionsDialogLite$$ExternalSyntheticLambda7(GlobalActionsDialogLite.ActionsDialogLite actionsDialogLite) {
        this.f$0 = actionsDialogLite;
    }

    public final boolean onItemLongClick(AdapterView adapterView, View view, int i, long j) {
        return this.f$0.lambda$createPowerOverflowPopup$3(adapterView, view, i, j);
    }
}
