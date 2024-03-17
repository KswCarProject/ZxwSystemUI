package com.android.systemui.statusbar.connectivity;

import java.util.ArrayList;
import java.util.List;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt__IterablesKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: WifiState.kt */
public final class WifiState extends ConnectivityState {
    public boolean isCarrierMerged;
    public boolean isDefault;
    public boolean isReady;
    public boolean isTransient;
    @Nullable
    public String ssid;
    @Nullable
    public String statusLabel;
    public int subId;
    public int wifiStandard;

    public WifiState() {
        this((String) null, false, false, (String) null, false, 0, 0, false, 255, (DefaultConstructorMarker) null);
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ WifiState(String str, boolean z, boolean z2, String str2, boolean z3, int i, int i2, boolean z4, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this((i3 & 1) != 0 ? null : str, (i3 & 2) != 0 ? false : z, (i3 & 4) != 0 ? false : z2, (i3 & 8) != 0 ? null : str2, (i3 & 16) != 0 ? false : z3, (i3 & 32) != 0 ? 0 : i, (i3 & 64) != 0 ? 0 : i2, (i3 & 128) != 0 ? false : z4);
    }

    public WifiState(@Nullable String str, boolean z, boolean z2, @Nullable String str2, boolean z3, int i, int i2, boolean z4) {
        this.ssid = str;
        this.isTransient = z;
        this.isDefault = z2;
        this.statusLabel = str2;
        this.isCarrierMerged = z3;
        this.subId = i;
        this.wifiStandard = i2;
        this.isReady = z4;
    }

    public void copyFrom(@NotNull ConnectivityState connectivityState) {
        super.copyFrom(connectivityState);
        WifiState wifiState = (WifiState) connectivityState;
        this.ssid = wifiState.ssid;
        this.isTransient = wifiState.isTransient;
        this.isDefault = wifiState.isDefault;
        this.statusLabel = wifiState.statusLabel;
        this.isCarrierMerged = wifiState.isCarrierMerged;
        this.subId = wifiState.subId;
        this.wifiStandard = wifiState.wifiStandard;
        this.isReady = wifiState.isReady;
    }

    public void toString(@NotNull StringBuilder sb) {
        super.toString(sb);
        sb.append(",ssid=");
        sb.append(this.ssid);
        sb.append(",wifiStandard=");
        sb.append(this.wifiStandard);
        sb.append(",isReady=");
        sb.append(this.isReady);
        sb.append(",isTransient=");
        sb.append(this.isTransient);
        sb.append(",isDefault=");
        sb.append(this.isDefault);
        sb.append(",statusLabel=");
        sb.append(this.statusLabel);
        sb.append(",isCarrierMerged=");
        sb.append(this.isCarrierMerged);
        sb.append(",subId=");
        sb.append(this.subId);
    }

    @NotNull
    public List<String> tableColumns() {
        return CollectionsKt___CollectionsKt.plus(super.tableColumns(), CollectionsKt__CollectionsKt.listOf("ssid", "isTransient", "isDefault", "statusLabel", "isCarrierMerged", "subId"));
    }

    @NotNull
    public List<String> tableData() {
        Iterable<Object> listOf = CollectionsKt__CollectionsKt.listOf(this.ssid, Boolean.valueOf(this.isTransient), Boolean.valueOf(this.isDefault), this.statusLabel, Boolean.valueOf(this.isCarrierMerged), Integer.valueOf(this.subId));
        ArrayList arrayList = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(listOf, 10));
        for (Object valueOf : listOf) {
            arrayList.add(String.valueOf(valueOf));
        }
        return CollectionsKt___CollectionsKt.plus(super.tableData(), arrayList);
    }

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!Intrinsics.areEqual((Object) WifiState.class, (Object) obj == null ? null : obj.getClass()) || !super.equals(obj)) {
            return false;
        }
        if (obj != null) {
            WifiState wifiState = (WifiState) obj;
            return Intrinsics.areEqual((Object) this.ssid, (Object) wifiState.ssid) && this.wifiStandard == wifiState.wifiStandard && this.isReady == wifiState.isReady && this.isTransient == wifiState.isTransient && this.isDefault == wifiState.isDefault && Intrinsics.areEqual((Object) this.statusLabel, (Object) wifiState.statusLabel) && this.isCarrierMerged == wifiState.isCarrierMerged && this.subId == wifiState.subId;
        }
        throw new NullPointerException("null cannot be cast to non-null type com.android.systemui.statusbar.connectivity.WifiState");
    }

    public int hashCode() {
        int hashCode = super.hashCode() * 31;
        String str = this.ssid;
        int i = 0;
        int hashCode2 = (((((hashCode + (str == null ? 0 : str.hashCode())) * 31) + Boolean.hashCode(this.isTransient)) * 31) + Boolean.hashCode(this.isDefault)) * 31;
        String str2 = this.statusLabel;
        if (str2 != null) {
            i = str2.hashCode();
        }
        return ((((hashCode2 + i) * 31) + Boolean.hashCode(this.isCarrierMerged)) * 31) + this.subId;
    }
}
