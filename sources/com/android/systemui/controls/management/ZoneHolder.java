package com.android.systemui.controls.management;

import android.view.View;
import android.widget.TextView;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;

/* compiled from: ControlAdapter.kt */
public final class ZoneHolder extends Holder {
    @NotNull
    public final TextView zone = ((TextView) this.itemView);

    public ZoneHolder(@NotNull View view) {
        super(view, (DefaultConstructorMarker) null);
    }

    public void bindData(@NotNull ElementWrapper elementWrapper) {
        this.zone.setText(((ZoneNameWrapper) elementWrapper).getZoneName());
    }
}
