package com.android.systemui.controls.ui;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.service.controls.Control;
import android.service.controls.templates.ControlTemplate;
import com.android.systemui.R$id;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: TouchBehavior.kt */
public final class TouchBehavior implements Behavior {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    public Drawable clipLayer;
    public Control control;
    public ControlViewHolder cvh;
    public int lastColorOffset;
    public boolean statelessTouch;
    public ControlTemplate template;

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
    public final ControlTemplate getTemplate() {
        ControlTemplate controlTemplate = this.template;
        if (controlTemplate != null) {
            return controlTemplate;
        }
        return null;
    }

    public final void setTemplate(@NotNull ControlTemplate controlTemplate) {
        this.template = controlTemplate;
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

    public final boolean getEnabled() {
        return this.lastColorOffset > 0 || this.statelessTouch;
    }

    /* compiled from: TouchBehavior.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }

    public void initialize(@NotNull ControlViewHolder controlViewHolder) {
        setCvh(controlViewHolder);
        controlViewHolder.getLayout().setOnClickListener(new TouchBehavior$initialize$1(controlViewHolder, this));
    }

    public void bind(@NotNull ControlWithState controlWithState, int i) {
        Control control2 = controlWithState.getControl();
        Intrinsics.checkNotNull(control2);
        setControl(control2);
        this.lastColorOffset = i;
        int i2 = 0;
        ControlViewHolder.setStatusText$default(getCvh(), getControl().getStatusText(), false, 2, (Object) null);
        setTemplate(getControl().getControlTemplate());
        Drawable background = getCvh().getLayout().getBackground();
        if (background != null) {
            setClipLayer(((LayerDrawable) background).findDrawableByLayerId(R$id.clip_layer));
            Drawable clipLayer2 = getClipLayer();
            if (getEnabled()) {
                i2 = 10000;
            }
            clipLayer2.setLevel(i2);
            ControlViewHolder.applyRenderInfo$frameworks__base__packages__SystemUI__android_common__SystemUI_core$default(getCvh(), getEnabled(), i, false, 4, (Object) null);
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type android.graphics.drawable.LayerDrawable");
    }
}
