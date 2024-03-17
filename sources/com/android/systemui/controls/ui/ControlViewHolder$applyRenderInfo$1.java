package com.android.systemui.controls.ui;

import android.content.res.ColorStateList;
import android.service.controls.Control;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

/* compiled from: ControlViewHolder.kt */
public final class ControlViewHolder$applyRenderInfo$1 extends Lambda implements Function0<Unit> {
    public final /* synthetic */ Control $control;
    public final /* synthetic */ boolean $enabled;
    public final /* synthetic */ ColorStateList $fg;
    public final /* synthetic */ CharSequence $newText;
    public final /* synthetic */ RenderInfo $ri;
    public final /* synthetic */ ControlViewHolder this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public ControlViewHolder$applyRenderInfo$1(ControlViewHolder controlViewHolder, boolean z, CharSequence charSequence, RenderInfo renderInfo, ColorStateList colorStateList, Control control) {
        super(0);
        this.this$0 = controlViewHolder;
        this.$enabled = z;
        this.$newText = charSequence;
        this.$ri = renderInfo;
        this.$fg = colorStateList;
        this.$control = control;
    }

    public final void invoke() {
        this.this$0.updateStatusRow$frameworks__base__packages__SystemUI__android_common__SystemUI_core(this.$enabled, this.$newText, this.$ri.getIcon(), this.$fg, this.$control);
    }
}
