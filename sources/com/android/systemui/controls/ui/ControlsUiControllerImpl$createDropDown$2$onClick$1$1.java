package com.android.systemui.controls.ui;

import android.view.View;
import android.widget.AdapterView;
import com.android.systemui.globalactions.GlobalActionsPopupMenu;
import org.jetbrains.annotations.NotNull;

/* compiled from: ControlsUiControllerImpl.kt */
public final class ControlsUiControllerImpl$createDropDown$2$onClick$1$1 implements AdapterView.OnItemClickListener {
    public final /* synthetic */ GlobalActionsPopupMenu $this_apply;
    public final /* synthetic */ ControlsUiControllerImpl this$0;

    public ControlsUiControllerImpl$createDropDown$2$onClick$1$1(ControlsUiControllerImpl controlsUiControllerImpl, GlobalActionsPopupMenu globalActionsPopupMenu) {
        this.this$0 = controlsUiControllerImpl;
        this.$this_apply = globalActionsPopupMenu;
    }

    public void onItemClick(@NotNull AdapterView<?> adapterView, @NotNull View view, int i, long j) {
        Object itemAtPosition = adapterView.getItemAtPosition(i);
        if (itemAtPosition != null) {
            this.this$0.switchAppOrStructure((SelectionItem) itemAtPosition);
            this.$this_apply.dismiss();
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type com.android.systemui.controls.ui.SelectionItem");
    }
}
