package com.android.systemui.statusbar.phone.panelstate;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: PanelExpansionChangeEvent.kt */
public final class PanelExpansionChangeEvent {
    public final float dragDownPxAmount;
    public final boolean expanded;
    public final float fraction;
    public final boolean tracking;

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PanelExpansionChangeEvent)) {
            return false;
        }
        PanelExpansionChangeEvent panelExpansionChangeEvent = (PanelExpansionChangeEvent) obj;
        return Intrinsics.areEqual((Object) Float.valueOf(this.fraction), (Object) Float.valueOf(panelExpansionChangeEvent.fraction)) && this.expanded == panelExpansionChangeEvent.expanded && this.tracking == panelExpansionChangeEvent.tracking && Intrinsics.areEqual((Object) Float.valueOf(this.dragDownPxAmount), (Object) Float.valueOf(panelExpansionChangeEvent.dragDownPxAmount));
    }

    public int hashCode() {
        int hashCode = Float.hashCode(this.fraction) * 31;
        boolean z = this.expanded;
        boolean z2 = true;
        if (z) {
            z = true;
        }
        int i = (hashCode + (z ? 1 : 0)) * 31;
        boolean z3 = this.tracking;
        if (!z3) {
            z2 = z3;
        }
        return ((i + (z2 ? 1 : 0)) * 31) + Float.hashCode(this.dragDownPxAmount);
    }

    @NotNull
    public String toString() {
        return "PanelExpansionChangeEvent(fraction=" + this.fraction + ", expanded=" + this.expanded + ", tracking=" + this.tracking + ", dragDownPxAmount=" + this.dragDownPxAmount + ')';
    }

    public PanelExpansionChangeEvent(float f, boolean z, boolean z2, float f2) {
        this.fraction = f;
        this.expanded = z;
        this.tracking = z2;
        this.dragDownPxAmount = f2;
    }

    public final float getFraction() {
        return this.fraction;
    }

    public final boolean getExpanded() {
        return this.expanded;
    }

    public final boolean getTracking() {
        return this.tracking;
    }

    public final float getDragDownPxAmount() {
        return this.dragDownPxAmount;
    }
}
