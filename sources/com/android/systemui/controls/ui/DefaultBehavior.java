package com.android.systemui.controls.ui;

import android.service.controls.Control;
import org.jetbrains.annotations.NotNull;

/* compiled from: DefaultBehavior.kt */
public final class DefaultBehavior implements Behavior {
    public ControlViewHolder cvh;

    @NotNull
    public final ControlViewHolder getCvh() {
        ControlViewHolder controlViewHolder = this.cvh;
        if (controlViewHolder != null) {
            return controlViewHolder;
        }
        return null;
    }

    public final void setCvh(@NotNull ControlViewHolder controlViewHolder) {
        this.cvh = controlViewHolder;
    }

    public void initialize(@NotNull ControlViewHolder controlViewHolder) {
        setCvh(controlViewHolder);
    }

    public void bind(@NotNull ControlWithState controlWithState, int i) {
        CharSequence statusText;
        ControlViewHolder cvh2 = getCvh();
        Control control = controlWithState.getControl();
        CharSequence charSequence = "";
        if (!(control == null || (statusText = control.getStatusText()) == null)) {
            charSequence = statusText;
        }
        ControlViewHolder.setStatusText$default(cvh2, charSequence, false, 2, (Object) null);
        ControlViewHolder.applyRenderInfo$frameworks__base__packages__SystemUI__android_common__SystemUI_core$default(getCvh(), false, i, false, 4, (Object) null);
    }
}
