package com.android.systemui.controls.ui;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.service.controls.Control;
import android.service.controls.templates.ControlTemplate;
import android.service.controls.templates.TemperatureControlTemplate;
import com.android.systemui.R$id;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: TemperatureControlBehavior.kt */
public final class TemperatureControlBehavior implements Behavior {
    public Drawable clipLayer;
    public Control control;
    public ControlViewHolder cvh;
    @Nullable
    public Behavior subBehavior;

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
    }

    public void bind(@NotNull ControlWithState controlWithState, int i) {
        Control control2 = controlWithState.getControl();
        Intrinsics.checkNotNull(control2);
        setControl(control2);
        int i2 = 0;
        ControlViewHolder.setStatusText$default(getCvh(), getControl().getStatusText(), false, 2, (Object) null);
        Drawable background = getCvh().getLayout().getBackground();
        if (background != null) {
            setClipLayer(((LayerDrawable) background).findDrawableByLayerId(R$id.clip_layer));
            TemperatureControlTemplate controlTemplate = getControl().getControlTemplate();
            if (controlTemplate != null) {
                TemperatureControlTemplate temperatureControlTemplate = controlTemplate;
                int currentActiveMode = temperatureControlTemplate.getCurrentActiveMode();
                ControlTemplate template = temperatureControlTemplate.getTemplate();
                if (Intrinsics.areEqual((Object) template, (Object) ControlTemplate.getNoTemplateObject()) || Intrinsics.areEqual((Object) template, (Object) ControlTemplate.getErrorTemplate())) {
                    boolean z = (currentActiveMode == 0 || currentActiveMode == 1) ? false : true;
                    Drawable clipLayer2 = getClipLayer();
                    if (z) {
                        i2 = 10000;
                    }
                    clipLayer2.setLevel(i2);
                    ControlViewHolder.applyRenderInfo$frameworks__base__packages__SystemUI__android_common__SystemUI_core$default(getCvh(), z, currentActiveMode, false, 4, (Object) null);
                    getCvh().getLayout().setOnClickListener(new TemperatureControlBehavior$bind$1(this, temperatureControlTemplate));
                    return;
                }
                this.subBehavior = getCvh().bindBehavior(this.subBehavior, ControlViewHolder.Companion.findBehaviorClass(getControl().getStatus(), template, getControl().getDeviceType()), currentActiveMode);
                return;
            }
            throw new NullPointerException("null cannot be cast to non-null type android.service.controls.templates.TemperatureControlTemplate");
        }
        throw new NullPointerException("null cannot be cast to non-null type android.graphics.drawable.LayerDrawable");
    }
}
