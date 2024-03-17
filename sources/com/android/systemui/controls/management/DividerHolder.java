package com.android.systemui.controls.management;

import android.view.View;
import com.android.systemui.R$id;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;

/* compiled from: ControlAdapter.kt */
public final class DividerHolder extends Holder {
    @NotNull
    public final View divider = this.itemView.requireViewById(R$id.divider);
    @NotNull
    public final View frame = this.itemView.requireViewById(R$id.frame);

    public DividerHolder(@NotNull View view) {
        super(view, (DefaultConstructorMarker) null);
    }

    public void bindData(@NotNull ElementWrapper elementWrapper) {
        DividerWrapper dividerWrapper = (DividerWrapper) elementWrapper;
        int i = 0;
        this.frame.setVisibility(dividerWrapper.getShowNone() ? 0 : 8);
        View view = this.divider;
        if (!dividerWrapper.getShowDivider()) {
            i = 8;
        }
        view.setVisibility(i);
    }
}
