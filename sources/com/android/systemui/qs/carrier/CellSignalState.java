package com.android.systemui.qs.carrier;

import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: CellSignalState.kt */
public final class CellSignalState {
    @Nullable
    public final String contentDescription;
    public final int mobileSignalIconId;
    public final boolean providerModelBehavior;
    public final boolean roaming;
    @Nullable
    public final String typeContentDescription;
    public final boolean visible;

    public CellSignalState() {
        this(false, 0, (String) null, (String) null, false, false, 63, (DefaultConstructorMarker) null);
    }

    public static /* synthetic */ CellSignalState copy$default(CellSignalState cellSignalState, boolean z, int i, String str, String str2, boolean z2, boolean z3, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            z = cellSignalState.visible;
        }
        if ((i2 & 2) != 0) {
            i = cellSignalState.mobileSignalIconId;
        }
        int i3 = i;
        if ((i2 & 4) != 0) {
            str = cellSignalState.contentDescription;
        }
        String str3 = str;
        if ((i2 & 8) != 0) {
            str2 = cellSignalState.typeContentDescription;
        }
        String str4 = str2;
        if ((i2 & 16) != 0) {
            z2 = cellSignalState.roaming;
        }
        boolean z4 = z2;
        if ((i2 & 32) != 0) {
            z3 = cellSignalState.providerModelBehavior;
        }
        return cellSignalState.copy(z, i3, str3, str4, z4, z3);
    }

    @NotNull
    public final CellSignalState copy(boolean z, int i, @Nullable String str, @Nullable String str2, boolean z2, boolean z3) {
        return new CellSignalState(z, i, str, str2, z2, z3);
    }

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CellSignalState)) {
            return false;
        }
        CellSignalState cellSignalState = (CellSignalState) obj;
        return this.visible == cellSignalState.visible && this.mobileSignalIconId == cellSignalState.mobileSignalIconId && Intrinsics.areEqual((Object) this.contentDescription, (Object) cellSignalState.contentDescription) && Intrinsics.areEqual((Object) this.typeContentDescription, (Object) cellSignalState.typeContentDescription) && this.roaming == cellSignalState.roaming && this.providerModelBehavior == cellSignalState.providerModelBehavior;
    }

    public int hashCode() {
        boolean z = this.visible;
        boolean z2 = true;
        if (z) {
            z = true;
        }
        int hashCode = (((z ? 1 : 0) * true) + Integer.hashCode(this.mobileSignalIconId)) * 31;
        String str = this.contentDescription;
        int i = 0;
        int hashCode2 = (hashCode + (str == null ? 0 : str.hashCode())) * 31;
        String str2 = this.typeContentDescription;
        if (str2 != null) {
            i = str2.hashCode();
        }
        int i2 = (hashCode2 + i) * 31;
        boolean z3 = this.roaming;
        if (z3) {
            z3 = true;
        }
        int i3 = (i2 + (z3 ? 1 : 0)) * 31;
        boolean z4 = this.providerModelBehavior;
        if (!z4) {
            z2 = z4;
        }
        return i3 + (z2 ? 1 : 0);
    }

    @NotNull
    public String toString() {
        return "CellSignalState(visible=" + this.visible + ", mobileSignalIconId=" + this.mobileSignalIconId + ", contentDescription=" + this.contentDescription + ", typeContentDescription=" + this.typeContentDescription + ", roaming=" + this.roaming + ", providerModelBehavior=" + this.providerModelBehavior + ')';
    }

    public CellSignalState(boolean z, int i, @Nullable String str, @Nullable String str2, boolean z2, boolean z3) {
        this.visible = z;
        this.mobileSignalIconId = i;
        this.contentDescription = str;
        this.typeContentDescription = str2;
        this.roaming = z2;
        this.providerModelBehavior = z3;
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ CellSignalState(boolean z, int i, String str, String str2, boolean z2, boolean z3, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this((i2 & 1) != 0 ? false : z, (i2 & 2) != 0 ? 0 : i, (i2 & 4) != 0 ? null : str, (i2 & 8) != 0 ? null : str2, (i2 & 16) != 0 ? false : z2, (i2 & 32) != 0 ? false : z3);
    }

    @NotNull
    public final CellSignalState changeVisibility(boolean z) {
        if (this.visible == z) {
            return this;
        }
        return copy$default(this, z, 0, (String) null, (String) null, false, false, 62, (Object) null);
    }
}
