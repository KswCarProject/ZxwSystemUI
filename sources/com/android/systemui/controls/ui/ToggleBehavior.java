package com.android.systemui.controls.ui;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.service.controls.Control;
import android.service.controls.templates.ControlTemplate;
import android.service.controls.templates.TemperatureControlTemplate;
import android.service.controls.templates.ToggleTemplate;
import android.util.Log;
import com.android.systemui.R$id;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: ToggleBehavior.kt */
public final class ToggleBehavior implements Behavior {
    public Drawable clipLayer;
    public Control control;
    public ControlViewHolder cvh;
    public ToggleTemplate template;

    @NotNull
    public final Drawable getClipLayer() {
        Drawable drawable = this.clipLayer;
        if (drawable != null) {
            return drawable;
        }
        return null;
    }

    public final void setClipLayer(@NotNull Drawable drawable) {
        this.clipLayer = drawable;
    }

    @NotNull
    public final ToggleTemplate getTemplate() {
        ToggleTemplate toggleTemplate = this.template;
        if (toggleTemplate != null) {
            return toggleTemplate;
        }
        return null;
    }

    public final void setTemplate(@NotNull ToggleTemplate toggleTemplate) {
        this.template = toggleTemplate;
    }

    @NotNull
    public final Control getControl() {
        Control control2 = this.control;
        if (control2 != null) {
            return control2;
        }
        return null;
    }

    public final void setControl(@NotNull Control control2) {
        this.control = control2;
    }

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
        controlViewHolder.getLayout().setOnClickListener(new ToggleBehavior$initialize$1(controlViewHolder, this));
    }

    public void bind(@NotNull ControlWithState controlWithState, int i) {
        ToggleTemplate toggleTemplate;
        Control control2 = controlWithState.getControl();
        Intrinsics.checkNotNull(control2);
        setControl(control2);
        ControlViewHolder.setStatusText$default(getCvh(), getControl().getStatusText(), false, 2, (Object) null);
        TemperatureControlTemplate controlTemplate = getControl().getControlTemplate();
        if (controlTemplate instanceof ToggleTemplate) {
            toggleTemplate = (ToggleTemplate) controlTemplate;
        } else if (controlTemplate instanceof TemperatureControlTemplate) {
            ControlTemplate template2 = controlTemplate.getTemplate();
            if (template2 != null) {
                toggleTemplate = (ToggleTemplate) template2;
            } else {
                throw new NullPointerException("null cannot be cast to non-null type android.service.controls.templates.ToggleTemplate");
            }
        } else {
            Log.e("ControlsUiController", Intrinsics.stringPlus("Unsupported template type: ", controlTemplate));
            return;
        }
        setTemplate(toggleTemplate);
        Drawable background = getCvh().getLayout().getBackground();
        if (background != null) {
            setClipLayer(((LayerDrawable) background).findDrawableByLayerId(R$id.clip_layer));
            getClipLayer().setLevel(10000);
            ControlViewHolder.applyRenderInfo$frameworks__base__packages__SystemUI__android_common__SystemUI_core$default(getCvh(), getTemplate().isChecked(), i, false, 4, (Object) null);
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type android.graphics.drawable.LayerDrawable");
    }
}
