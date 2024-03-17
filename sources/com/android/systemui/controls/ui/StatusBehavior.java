package com.android.systemui.controls.ui;

import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.service.controls.Control;
import com.android.systemui.R$string;
import org.jetbrains.annotations.NotNull;

/* compiled from: StatusBehavior.kt */
public final class StatusBehavior implements Behavior {
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
        int i2;
        Control control = controlWithState.getControl();
        int status = control == null ? 0 : control.getStatus();
        if (status == 2) {
            getCvh().getLayout().setOnClickListener(new StatusBehavior$bind$msg$1(this, controlWithState));
            getCvh().getLayout().setOnLongClickListener(new StatusBehavior$bind$msg$2(this, controlWithState));
            i2 = R$string.controls_error_removed;
        } else if (status == 3) {
            i2 = R$string.controls_error_generic;
        } else if (status != 4) {
            getCvh().setLoading(true);
            i2 = 17040591;
        } else {
            i2 = R$string.controls_error_timeout;
        }
        ControlViewHolder.setStatusText$default(getCvh(), getCvh().getContext().getString(i2), false, 2, (Object) null);
        ControlViewHolder.applyRenderInfo$frameworks__base__packages__SystemUI__android_common__SystemUI_core$default(getCvh(), false, i, false, 4, (Object) null);
    }

    public final void showNotFoundDialog(ControlViewHolder controlViewHolder, ControlWithState controlWithState) {
        PackageManager packageManager = controlViewHolder.getContext().getPackageManager();
        CharSequence applicationLabel = packageManager.getApplicationLabel(packageManager.getApplicationInfo(controlWithState.getComponentName().getPackageName(), 128));
        AlertDialog.Builder builder = new AlertDialog.Builder(controlViewHolder.getContext(), 16974545);
        Resources resources = controlViewHolder.getContext().getResources();
        builder.setTitle(resources.getString(R$string.controls_error_removed_title));
        builder.setMessage(resources.getString(R$string.controls_error_removed_message, new Object[]{controlViewHolder.getTitle().getText(), applicationLabel}));
        builder.setPositiveButton(R$string.controls_open_app, new StatusBehavior$showNotFoundDialog$builder$1$1(controlWithState, builder, controlViewHolder));
        builder.setNegativeButton(17039360, StatusBehavior$showNotFoundDialog$builder$1$2.INSTANCE);
        AlertDialog create = builder.create();
        create.getWindow().setType(2020);
        create.show();
        controlViewHolder.setVisibleDialog(create);
    }
}
