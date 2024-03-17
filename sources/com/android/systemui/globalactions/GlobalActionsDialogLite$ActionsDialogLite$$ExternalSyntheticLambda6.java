package com.android.systemui.globalactions;

import android.view.View;
import android.widget.AdapterView;
import com.android.systemui.globalactions.GlobalActionsDialogLite;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class GlobalActionsDialogLite$ActionsDialogLite$$ExternalSyntheticLambda6 implements AdapterView.OnItemClickListener {
    public final /* synthetic */ GlobalActionsDialogLite.ActionsDialogLite f$0;

    public /* synthetic */ GlobalActionsDialogLite$ActionsDialogLite$$ExternalSyntheticLambda6(GlobalActionsDialogLite.ActionsDialogLite actionsDialogLite) {
        this.f$0 = actionsDialogLite;
    }

    public final void onItemClick(AdapterView adapterView, View view, int i, long j) {
        this.f$0.lambda$createPowerOverflowPopup$2(adapterView, view, i, j);
    }
}
