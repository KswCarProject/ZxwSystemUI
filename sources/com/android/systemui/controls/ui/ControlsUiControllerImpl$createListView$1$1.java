package com.android.systemui.controls.ui;

import android.view.View;
import org.jetbrains.annotations.NotNull;

/* compiled from: ControlsUiControllerImpl.kt */
public final class ControlsUiControllerImpl$createListView$1$1 implements View.OnClickListener {
    public final /* synthetic */ ControlsUiControllerImpl this$0;

    public ControlsUiControllerImpl$createListView$1$1(ControlsUiControllerImpl controlsUiControllerImpl) {
        this.this$0 = controlsUiControllerImpl;
    }

    public final void onClick(@NotNull View view) {
        Runnable access$getOnDismiss$p = this.this$0.onDismiss;
        if (access$getOnDismiss$p == null) {
            access$getOnDismiss$p = null;
        }
        access$getOnDismiss$p.run();
    }
}
