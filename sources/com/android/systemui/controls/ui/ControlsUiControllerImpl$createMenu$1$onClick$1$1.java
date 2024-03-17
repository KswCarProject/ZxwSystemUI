package com.android.systemui.controls.ui;

import android.view.View;
import android.widget.AdapterView;
import com.android.systemui.globalactions.GlobalActionsPopupMenu;
import org.jetbrains.annotations.NotNull;

/* compiled from: ControlsUiControllerImpl.kt */
public final class ControlsUiControllerImpl$createMenu$1$onClick$1$1 implements AdapterView.OnItemClickListener {
    public final /* synthetic */ GlobalActionsPopupMenu $this_apply;
    public final /* synthetic */ ControlsUiControllerImpl this$0;

    public ControlsUiControllerImpl$createMenu$1$onClick$1$1(ControlsUiControllerImpl controlsUiControllerImpl, GlobalActionsPopupMenu globalActionsPopupMenu) {
        this.this$0 = controlsUiControllerImpl;
        this.$this_apply = globalActionsPopupMenu;
    }

    public void onItemClick(@NotNull AdapterView<?> adapterView, @NotNull View view, int i, long j) {
        if (i == 0) {
            ControlsUiControllerImpl controlsUiControllerImpl = this.this$0;
            controlsUiControllerImpl.startFavoritingActivity(controlsUiControllerImpl.selectedStructure);
        } else if (i == 1) {
            ControlsUiControllerImpl controlsUiControllerImpl2 = this.this$0;
            controlsUiControllerImpl2.startEditingActivity(controlsUiControllerImpl2.selectedStructure);
        }
        this.$this_apply.dismiss();
    }
}
