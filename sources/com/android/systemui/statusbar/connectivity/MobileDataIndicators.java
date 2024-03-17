package com.android.systemui.statusbar.connectivity;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: SignalCallback.kt */
public final class MobileDataIndicators {
    public final boolean activityIn;
    public final boolean activityOut;
    @Nullable
    public final CharSequence qsDescription;
    @Nullable
    public final IconState qsIcon;
    public final int qsType;
    public final boolean roaming;
    public final boolean showTriangle;
    @Nullable
    public final IconState statusIcon;
    public final int statusType;
    public final int subId;
    @Nullable
    public final CharSequence typeContentDescription;
    @Nullable
    public final CharSequence typeContentDescriptionHtml;
    public final int volteIcon;

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MobileDataIndicators)) {
            return false;
        }
        MobileDataIndicators mobileDataIndicators = (MobileDataIndicators) obj;
        return Intrinsics.areEqual((Object) this.statusIcon, (Object) mobileDataIndicators.statusIcon) && Intrinsics.areEqual((Object) this.qsIcon, (Object) mobileDataIndicators.qsIcon) && this.statusType == mobileDataIndicators.statusType && this.qsType == mobileDataIndicators.qsType && this.activityIn == mobileDataIndicators.activityIn && this.activityOut == mobileDataIndicators.activityOut && this.volteIcon == mobileDataIndicators.volteIcon && Intrinsics.areEqual((Object) this.typeContentDescription, (Object) mobileDataIndicators.typeContentDescription) && Intrinsics.areEqual((Object) this.typeContentDescriptionHtml, (Object) mobileDataIndicators.typeContentDescriptionHtml) && Intrinsics.areEqual((Object) this.qsDescription, (Object) mobileDataIndicators.qsDescription) && this.subId == mobileDataIndicators.subId && this.roaming == mobileDataIndicators.roaming && this.showTriangle == mobileDataIndicators.showTriangle;
    }

    public int hashCode() {
        IconState iconState = this.statusIcon;
        int i = 0;
        int hashCode = (iconState == null ? 0 : iconState.hashCode()) * 31;
        IconState iconState2 = this.qsIcon;
        int hashCode2 = (((((hashCode + (iconState2 == null ? 0 : iconState2.hashCode())) * 31) + Integer.hashCode(this.statusType)) * 31) + Integer.hashCode(this.qsType)) * 31;
        boolean z = this.activityIn;
        boolean z2 = true;
        if (z) {
            z = true;
        }
        int i2 = (hashCode2 + (z ? 1 : 0)) * 31;
        boolean z3 = this.activityOut;
        if (z3) {
            z3 = true;
        }
        int hashCode3 = (((i2 + (z3 ? 1 : 0)) * 31) + Integer.hashCode(this.volteIcon)) * 31;
        CharSequence charSequence = this.typeContentDescription;
        int hashCode4 = (hashCode3 + (charSequence == null ? 0 : charSequence.hashCode())) * 31;
        CharSequence charSequence2 = this.typeContentDescriptionHtml;
        int hashCode5 = (hashCode4 + (charSequence2 == null ? 0 : charSequence2.hashCode())) * 31;
        CharSequence charSequence3 = this.qsDescription;
        if (charSequence3 != null) {
            i = charSequence3.hashCode();
        }
        int hashCode6 = (((hashCode5 + i) * 31) + Integer.hashCode(this.subId)) * 31;
        boolean z4 = this.roaming;
        if (z4) {
            z4 = true;
        }
        int i3 = (hashCode6 + (z4 ? 1 : 0)) * 31;
        boolean z5 = this.showTriangle;
        if (!z5) {
            z2 = z5;
        }
        return i3 + (z2 ? 1 : 0);
    }

    public MobileDataIndicators(@Nullable IconState iconState, @Nullable IconState iconState2, int i, int i2, boolean z, boolean z2, int i3, @Nullable CharSequence charSequence, @Nullable CharSequence charSequence2, @Nullable CharSequence charSequence3, int i4, boolean z3, boolean z4) {
        this.statusIcon = iconState;
        this.qsIcon = iconState2;
        this.statusType = i;
        this.qsType = i2;
        this.activityIn = z;
        this.activityOut = z2;
        this.volteIcon = i3;
        this.typeContentDescription = charSequence;
        this.typeContentDescriptionHtml = charSequence2;
        this.qsDescription = charSequence3;
        this.subId = i4;
        this.roaming = z3;
        this.showTriangle = z4;
    }

    @NotNull
    public String toString() {
        String str;
        String iconState;
        StringBuilder sb = new StringBuilder("MobileDataIndicators[");
        sb.append("statusIcon=");
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
        sb.append(",statusType=");
        sb.append(this.statusType);
        sb.append(",qsType=");
        sb.append(this.qsType);
        sb.append(",activityIn=");
        sb.append(this.activityIn);
        sb.append(",activityOut=");
        sb.append(this.activityOut);
        sb.append(",volteIcon=");
        sb.append(this.volteIcon);
        sb.append(",typeContentDescription=");
        sb.append(this.typeContentDescription);
        sb.append(",typeContentDescriptionHtml=");
        sb.append(this.typeContentDescriptionHtml);
        sb.append(",description=");
        sb.append(this.qsDescription);
        sb.append(",subId=");
        sb.append(this.subId);
        sb.append(",roaming=");
        sb.append(this.roaming);
        sb.append(",showTriangle=");
        sb.append(this.showTriangle);
        sb.append(']');
        return sb.toString();
    }
}
