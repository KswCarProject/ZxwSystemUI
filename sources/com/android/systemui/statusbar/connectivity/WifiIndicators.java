package com.android.systemui.statusbar.connectivity;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: SignalCallback.kt */
public final class WifiIndicators {
    public final boolean activityIn;
    public final boolean activityOut;
    @Nullable
    public final String description;
    public final boolean enabled;
    public final boolean isTransient;
    @Nullable
    public final IconState qsIcon;
    @Nullable
    public final IconState statusIcon;
    @Nullable
    public final String statusLabel;

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof WifiIndicators)) {
            return false;
        }
        WifiIndicators wifiIndicators = (WifiIndicators) obj;
        return this.enabled == wifiIndicators.enabled && Intrinsics.areEqual((Object) this.statusIcon, (Object) wifiIndicators.statusIcon) && Intrinsics.areEqual((Object) this.qsIcon, (Object) wifiIndicators.qsIcon) && this.activityIn == wifiIndicators.activityIn && this.activityOut == wifiIndicators.activityOut && Intrinsics.areEqual((Object) this.description, (Object) wifiIndicators.description) && this.isTransient == wifiIndicators.isTransient && Intrinsics.areEqual((Object) this.statusLabel, (Object) wifiIndicators.statusLabel);
    }

    public int hashCode() {
        boolean z = this.enabled;
        boolean z2 = true;
        if (z) {
            z = true;
        }
        int i = (z ? 1 : 0) * true;
        IconState iconState = this.statusIcon;
        int i2 = 0;
        int hashCode = (i + (iconState == null ? 0 : iconState.hashCode())) * 31;
        IconState iconState2 = this.qsIcon;
        int hashCode2 = (hashCode + (iconState2 == null ? 0 : iconState2.hashCode())) * 31;
        boolean z3 = this.activityIn;
        if (z3) {
            z3 = true;
        }
        int i3 = (hashCode2 + (z3 ? 1 : 0)) * 31;
        boolean z4 = this.activityOut;
        if (z4) {
            z4 = true;
        }
        int i4 = (i3 + (z4 ? 1 : 0)) * 31;
        String str = this.description;
        int hashCode3 = (i4 + (str == null ? 0 : str.hashCode())) * 31;
        boolean z5 = this.isTransient;
        if (!z5) {
            z2 = z5;
        }
        int i5 = (hashCode3 + (z2 ? 1 : 0)) * 31;
        String str2 = this.statusLabel;
        if (str2 != null) {
            i2 = str2.hashCode();
        }
        return i5 + i2;
    }

    public WifiIndicators(boolean z, @Nullable IconState iconState, @Nullable IconState iconState2, boolean z2, boolean z3, @Nullable String str, boolean z4, @Nullable String str2) {
        this.enabled = z;
        this.statusIcon = iconState;
        this.qsIcon = iconState2;
        this.activityIn = z2;
        this.activityOut = z3;
        this.description = str;
        this.isTransient = z4;
        this.statusLabel = str2;
    }

    @NotNull
    public String toString() {
        String str;
        String iconState;
        StringBuilder sb = new StringBuilder("WifiIndicators[");
        sb.append("enabled=");
        sb.append(this.enabled);
        sb.append(",statusIcon=");
        IconState iconState2 = this.statusIcon;
        String str2 = "";
        if (iconState2 == null || (str = iconState2.toString()) == null) {
            str = str2;
        }
        sb.append(str);
        sb.append(",qsIcon=");
        IconState iconState3 = this.qsIcon;
        if (!(iconState3 == null || (iconState = iconState3.toString()) == null)) {
            str2 = iconState;
        }
        sb.append(str2);
        sb.append(",activityIn=");
        sb.append(this.activityIn);
        sb.append(",activityOut=");
        sb.append(this.activityOut);
        sb.append(",qsDescription=");
        sb.append(this.description);
        sb.append(",isTransient=");
        sb.append(this.isTransient);
        sb.append(",statusLabel=");
        sb.append(this.statusLabel);
        sb.append(']');
        return sb.toString();
    }
}
