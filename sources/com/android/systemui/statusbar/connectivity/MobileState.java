package com.android.systemui.statusbar.connectivity;

import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyDisplayInfo;
import com.android.settingslib.SignalIcon$IconGroup;
import com.android.settingslib.Utils;
import com.android.settingslib.mobile.MobileStatusTracker;
import com.android.settingslib.mobile.TelephonyIcons;
import java.util.ArrayList;
import java.util.List;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt__IterablesKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: MobileState.kt */
public final class MobileState extends ConnectivityState {
    public boolean airplaneMode;
    public boolean carrierNetworkChangeMode;
    public boolean dataConnected;
    public boolean dataSim;
    public int dataState;
    public boolean defaultDataOff;
    public boolean imsRegistered;
    public int imsRegistrationTech;
    public boolean isDefault;
    public boolean isEmergency;
    public boolean mobileDataEnabled;
    @Nullable
    public String networkName;
    @Nullable
    public String networkNameData;
    public boolean roaming;
    public boolean roamingDataEnabled;
    @Nullable
    public ServiceState serviceState;
    @Nullable
    public SignalStrength signalStrength;
    @NotNull
    public TelephonyDisplayInfo telephonyDisplayInfo;
    public boolean userSetup;
    public boolean videoCapable;
    public boolean voiceCapable;

    public MobileState() {
        this((String) null, (String) null, false, false, false, false, false, false, false, false, 0, false, false, false, false, false, false, 0, 262143, (DefaultConstructorMarker) null);
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public /* synthetic */ MobileState(java.lang.String r20, java.lang.String r21, boolean r22, boolean r23, boolean r24, boolean r25, boolean r26, boolean r27, boolean r28, boolean r29, int r30, boolean r31, boolean r32, boolean r33, boolean r34, boolean r35, boolean r36, int r37, int r38, kotlin.jvm.internal.DefaultConstructorMarker r39) {
        /*
            r19 = this;
            r0 = r38
            r1 = r0 & 1
            r2 = 0
            if (r1 == 0) goto L_0x0009
            r1 = r2
            goto L_0x000b
        L_0x0009:
            r1 = r20
        L_0x000b:
            r3 = r0 & 2
            if (r3 == 0) goto L_0x0010
            goto L_0x0012
        L_0x0010:
            r2 = r21
        L_0x0012:
            r3 = r0 & 4
            if (r3 == 0) goto L_0x0018
            r3 = 0
            goto L_0x001a
        L_0x0018:
            r3 = r22
        L_0x001a:
            r5 = r0 & 8
            if (r5 == 0) goto L_0x0020
            r5 = 0
            goto L_0x0022
        L_0x0020:
            r5 = r23
        L_0x0022:
            r6 = r0 & 16
            if (r6 == 0) goto L_0x0028
            r6 = 0
            goto L_0x002a
        L_0x0028:
            r6 = r24
        L_0x002a:
            r7 = r0 & 32
            if (r7 == 0) goto L_0x0030
            r7 = 0
            goto L_0x0032
        L_0x0030:
            r7 = r25
        L_0x0032:
            r8 = r0 & 64
            if (r8 == 0) goto L_0x0038
            r8 = 0
            goto L_0x003a
        L_0x0038:
            r8 = r26
        L_0x003a:
            r9 = r0 & 128(0x80, float:1.794E-43)
            if (r9 == 0) goto L_0x0040
            r9 = 0
            goto L_0x0042
        L_0x0040:
            r9 = r27
        L_0x0042:
            r10 = r0 & 256(0x100, float:3.59E-43)
            if (r10 == 0) goto L_0x0048
            r10 = 0
            goto L_0x004a
        L_0x0048:
            r10 = r28
        L_0x004a:
            r11 = r0 & 512(0x200, float:7.175E-43)
            if (r11 == 0) goto L_0x0050
            r11 = 0
            goto L_0x0052
        L_0x0050:
            r11 = r29
        L_0x0052:
            r12 = r0 & 1024(0x400, float:1.435E-42)
            if (r12 == 0) goto L_0x0058
            r12 = 0
            goto L_0x005a
        L_0x0058:
            r12 = r30
        L_0x005a:
            r13 = r0 & 2048(0x800, float:2.87E-42)
            if (r13 == 0) goto L_0x0060
            r13 = 0
            goto L_0x0062
        L_0x0060:
            r13 = r31
        L_0x0062:
            r14 = r0 & 4096(0x1000, float:5.74E-42)
            if (r14 == 0) goto L_0x0068
            r14 = 0
            goto L_0x006a
        L_0x0068:
            r14 = r32
        L_0x006a:
            r15 = r0 & 8192(0x2000, float:1.14794E-41)
            if (r15 == 0) goto L_0x0070
            r15 = 0
            goto L_0x0072
        L_0x0070:
            r15 = r33
        L_0x0072:
            r4 = r0 & 16384(0x4000, float:2.2959E-41)
            if (r4 == 0) goto L_0x0078
            r4 = 0
            goto L_0x007a
        L_0x0078:
            r4 = r34
        L_0x007a:
            r16 = 32768(0x8000, float:4.5918E-41)
            r16 = r0 & r16
            if (r16 == 0) goto L_0x0084
            r16 = 0
            goto L_0x0086
        L_0x0084:
            r16 = r35
        L_0x0086:
            r17 = 65536(0x10000, float:9.18355E-41)
            r17 = r0 & r17
            if (r17 == 0) goto L_0x008f
            r17 = 0
            goto L_0x0091
        L_0x008f:
            r17 = r36
        L_0x0091:
            r18 = 131072(0x20000, float:1.83671E-40)
            r0 = r0 & r18
            if (r0 == 0) goto L_0x0099
            r0 = -1
            goto L_0x009b
        L_0x0099:
            r0 = r37
        L_0x009b:
            r20 = r1
            r21 = r2
            r22 = r3
            r23 = r5
            r24 = r6
            r25 = r7
            r26 = r8
            r27 = r9
            r28 = r10
            r29 = r11
            r30 = r12
            r31 = r13
            r32 = r14
            r33 = r15
            r34 = r4
            r35 = r16
            r36 = r17
            r37 = r0
            r19.<init>(r20, r21, r22, r23, r24, r25, r26, r27, r28, r29, r30, r31, r32, r33, r34, r35, r36, r37)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.connectivity.MobileState.<init>(java.lang.String, java.lang.String, boolean, boolean, boolean, boolean, boolean, boolean, boolean, boolean, int, boolean, boolean, boolean, boolean, boolean, boolean, int, int, kotlin.jvm.internal.DefaultConstructorMarker):void");
    }

    public MobileState(@Nullable String str, @Nullable String str2, boolean z, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6, boolean z7, boolean z8, int i, boolean z9, boolean z10, boolean z11, boolean z12, boolean z13, boolean z14, int i2) {
        this.networkName = str;
        this.networkNameData = str2;
        this.dataSim = z;
        this.dataConnected = z2;
        this.isEmergency = z3;
        this.airplaneMode = z4;
        this.carrierNetworkChangeMode = z5;
        this.isDefault = z6;
        this.userSetup = z7;
        this.roaming = z8;
        this.dataState = i;
        this.defaultDataOff = z9;
        this.imsRegistered = z10;
        this.voiceCapable = z11;
        this.videoCapable = z12;
        this.mobileDataEnabled = z13;
        this.roamingDataEnabled = z14;
        this.imsRegistrationTech = i2;
        this.telephonyDisplayInfo = new TelephonyDisplayInfo(0, 0);
    }

    public final boolean isDataDisabledOrNotDefault() {
        SignalIcon$IconGroup signalIcon$IconGroup = this.iconGroup;
        return (signalIcon$IconGroup == TelephonyIcons.DATA_DISABLED || signalIcon$IconGroup == TelephonyIcons.NOT_DEFAULT_DATA) && this.userSetup;
    }

    public final boolean hasActivityIn() {
        return this.dataConnected && !this.carrierNetworkChangeMode && this.activityIn;
    }

    public final boolean hasActivityOut() {
        return this.dataConnected && !this.carrierNetworkChangeMode && this.activityOut;
    }

    public final boolean showQuickSettingsRatIcon() {
        return this.dataConnected || isDataDisabledOrNotDefault();
    }

    public void copyFrom(@NotNull ConnectivityState connectivityState) {
        MobileState mobileState = connectivityState instanceof MobileState ? (MobileState) connectivityState : null;
        if (mobileState != null) {
            super.copyFrom(mobileState);
            this.networkName = mobileState.networkName;
            this.networkNameData = mobileState.networkNameData;
            this.dataSim = mobileState.dataSim;
            this.dataConnected = mobileState.dataConnected;
            this.isEmergency = mobileState.isEmergency;
            this.airplaneMode = mobileState.airplaneMode;
            this.carrierNetworkChangeMode = mobileState.carrierNetworkChangeMode;
            this.isDefault = mobileState.isDefault;
            this.userSetup = mobileState.userSetup;
            this.roaming = mobileState.roaming;
            this.dataState = mobileState.dataState;
            this.defaultDataOff = mobileState.defaultDataOff;
            this.imsRegistered = mobileState.imsRegistered;
            this.imsRegistrationTech = mobileState.imsRegistrationTech;
            this.voiceCapable = mobileState.voiceCapable;
            this.videoCapable = mobileState.videoCapable;
            this.mobileDataEnabled = mobileState.mobileDataEnabled;
            this.roamingDataEnabled = mobileState.roamingDataEnabled;
            this.telephonyDisplayInfo = mobileState.telephonyDisplayInfo;
            this.serviceState = mobileState.serviceState;
            this.signalStrength = mobileState.signalStrength;
            return;
        }
        throw new IllegalArgumentException("MobileState can only update from another MobileState");
    }

    public final boolean isDataConnected() {
        return this.connected && this.dataState == 2;
    }

    public final int getVoiceServiceState() {
        ServiceState serviceState2 = this.serviceState;
        if (serviceState2 == null) {
            return -1;
        }
        return serviceState2.getState();
    }

    public final boolean isNoCalling() {
        ServiceState serviceState2 = this.serviceState;
        boolean z = false;
        if (serviceState2 != null && serviceState2.getState() == 0) {
            z = true;
        }
        return !z;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0007, code lost:
        r1 = r1.getOperatorAlphaShort();
     */
    @org.jetbrains.annotations.NotNull
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.lang.String getOperatorAlphaShort() {
        /*
            r1 = this;
            android.telephony.ServiceState r1 = r1.serviceState
            java.lang.String r0 = ""
            if (r1 != 0) goto L_0x0007
            goto L_0x000f
        L_0x0007:
            java.lang.String r1 = r1.getOperatorAlphaShort()
            if (r1 != 0) goto L_0x000e
            goto L_0x000f
        L_0x000e:
            r0 = r1
        L_0x000f:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.connectivity.MobileState.getOperatorAlphaShort():java.lang.String");
    }

    public final boolean isCdma() {
        SignalStrength signalStrength2 = this.signalStrength;
        if (signalStrength2 != null) {
            Intrinsics.checkNotNull(signalStrength2);
            if (!signalStrength2.isGsm()) {
                return true;
            }
        }
        return false;
    }

    public final boolean isEmergencyOnly() {
        ServiceState serviceState2 = this.serviceState;
        if (serviceState2 != null) {
            Intrinsics.checkNotNull(serviceState2);
            if (serviceState2.isEmergencyOnly()) {
                return true;
            }
        }
        return false;
    }

    public final boolean isInService() {
        return Utils.isInService(this.serviceState);
    }

    public final boolean isRoaming() {
        ServiceState serviceState2 = this.serviceState;
        if (serviceState2 != null) {
            Intrinsics.checkNotNull(serviceState2);
            if (serviceState2.getRoaming()) {
                return true;
            }
        }
        return false;
    }

    public final int getVoiceNetworkType() {
        ServiceState serviceState2 = this.serviceState;
        if (serviceState2 == null) {
            return 0;
        }
        return serviceState2.getVoiceNetworkType();
    }

    public final int getDataNetworkType() {
        ServiceState serviceState2 = this.serviceState;
        if (serviceState2 == null) {
            return 0;
        }
        return serviceState2.getDataNetworkType();
    }

    public final void setFromMobileStatus(@NotNull MobileStatusTracker.MobileStatus mobileStatus) {
        this.activityIn = mobileStatus.activityIn;
        this.activityOut = mobileStatus.activityOut;
        this.dataSim = mobileStatus.dataSim;
        this.carrierNetworkChangeMode = mobileStatus.carrierNetworkChangeMode;
        this.dataState = mobileStatus.dataState;
        this.signalStrength = mobileStatus.signalStrength;
        this.telephonyDisplayInfo = mobileStatus.telephonyDisplayInfo;
        this.serviceState = mobileStatus.serviceState;
    }

    public void toString(@NotNull StringBuilder sb) {
        String str;
        String access$minLog;
        super.toString(sb);
        sb.append(',');
        sb.append("dataSim=" + this.dataSim + ',');
        sb.append("networkName=" + this.networkName + ',');
        sb.append("networkNameData=" + this.networkNameData + ',');
        sb.append("dataConnected=" + this.dataConnected + ',');
        sb.append("roaming=" + this.roaming + ',');
        sb.append("isDefault=" + this.isDefault + ',');
        sb.append("isEmergency=" + this.isEmergency + ',');
        sb.append("airplaneMode=" + this.airplaneMode + ',');
        sb.append("carrierNetworkChangeMode=" + this.carrierNetworkChangeMode + ',');
        sb.append("userSetup=" + this.userSetup + ',');
        sb.append("dataState=" + this.dataState + ',');
        sb.append("defaultDataOff=" + this.defaultDataOff + ',');
        sb.append("imsRegistered=" + this.imsRegistered + ',');
        sb.append("imsRegistrationTech=" + this.imsRegistrationTech + ',');
        sb.append("voiceCapable=" + this.voiceCapable + ',');
        sb.append("videoCapable=" + this.videoCapable + ',');
        sb.append("mobileDataEnabled=" + this.mobileDataEnabled + ',');
        sb.append("roamingDataEnabled=" + this.roamingDataEnabled + ',');
        sb.append("showQuickSettingsRatIcon=" + showQuickSettingsRatIcon() + ',');
        sb.append("voiceServiceState=" + getVoiceServiceState() + ',');
        sb.append("isInService=" + isInService() + ',');
        StringBuilder sb2 = new StringBuilder();
        sb2.append("serviceState=");
        ServiceState serviceState2 = this.serviceState;
        String str2 = "(null)";
        if (serviceState2 == null || (str = MobileStateKt.minLog(serviceState2)) == null) {
            str = str2;
        }
        sb2.append(str);
        sb2.append(',');
        sb.append(sb2.toString());
        StringBuilder sb3 = new StringBuilder();
        sb3.append("signalStrength=");
        SignalStrength signalStrength2 = this.signalStrength;
        if (!(signalStrength2 == null || (access$minLog = MobileStateKt.minLog(signalStrength2)) == null)) {
            str2 = access$minLog;
        }
        sb3.append(str2);
        sb3.append(',');
        sb.append(sb3.toString());
        sb.append(Intrinsics.stringPlus("displayInfo=", this.telephonyDisplayInfo));
    }

    @NotNull
    public List<String> tableColumns() {
        return CollectionsKt___CollectionsKt.plus(super.tableColumns(), CollectionsKt__CollectionsKt.listOf("dataSim", "networkName", "networkNameData", "dataConnected", "roaming", "isDefault", "isEmergency", "airplaneMode", "carrierNetworkChangeMode", "userSetup", "dataState", "defaultDataOff", "showQuickSettingsRatIcon", "voiceServiceState", "isInService", "serviceState", "signalStrength", "displayInfo"));
    }

    @NotNull
    public List<String> tableData() {
        String str;
        String access$minLog;
        Object[] objArr = new Object[18];
        objArr[0] = Boolean.valueOf(this.dataSim);
        objArr[1] = this.networkName;
        objArr[2] = this.networkNameData;
        objArr[3] = Boolean.valueOf(this.dataConnected);
        objArr[4] = Boolean.valueOf(this.roaming);
        objArr[5] = Boolean.valueOf(this.isDefault);
        objArr[6] = Boolean.valueOf(this.isEmergency);
        objArr[7] = Boolean.valueOf(this.airplaneMode);
        objArr[8] = Boolean.valueOf(this.carrierNetworkChangeMode);
        objArr[9] = Boolean.valueOf(this.userSetup);
        objArr[10] = Integer.valueOf(this.dataState);
        objArr[11] = Boolean.valueOf(this.defaultDataOff);
        objArr[12] = Boolean.valueOf(showQuickSettingsRatIcon());
        objArr[13] = Integer.valueOf(getVoiceServiceState());
        objArr[14] = Boolean.valueOf(isInService());
        ServiceState serviceState2 = this.serviceState;
        String str2 = "(null)";
        if (serviceState2 == null || (str = MobileStateKt.minLog(serviceState2)) == null) {
            str = str2;
        }
        objArr[15] = str;
        SignalStrength signalStrength2 = this.signalStrength;
        if (!(signalStrength2 == null || (access$minLog = MobileStateKt.minLog(signalStrength2)) == null)) {
            str2 = access$minLog;
        }
        objArr[16] = str2;
        objArr[17] = this.telephonyDisplayInfo;
        Iterable<Object> listOf = CollectionsKt__CollectionsKt.listOf(objArr);
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
        if (!Intrinsics.areEqual((Object) MobileState.class, (Object) obj == null ? null : obj.getClass()) || !super.equals(obj)) {
            return false;
        }
        if (obj != null) {
            MobileState mobileState = (MobileState) obj;
            return Intrinsics.areEqual((Object) this.networkName, (Object) mobileState.networkName) && Intrinsics.areEqual((Object) this.networkNameData, (Object) mobileState.networkNameData) && this.dataSim == mobileState.dataSim && this.dataConnected == mobileState.dataConnected && this.isEmergency == mobileState.isEmergency && this.airplaneMode == mobileState.airplaneMode && this.carrierNetworkChangeMode == mobileState.carrierNetworkChangeMode && this.isDefault == mobileState.isDefault && this.userSetup == mobileState.userSetup && this.roaming == mobileState.roaming && this.dataState == mobileState.dataState && this.defaultDataOff == mobileState.defaultDataOff && this.imsRegistered == mobileState.imsRegistered && this.imsRegistrationTech == mobileState.imsRegistrationTech && this.voiceCapable == mobileState.voiceCapable && this.videoCapable == mobileState.videoCapable && this.mobileDataEnabled == mobileState.mobileDataEnabled && this.roamingDataEnabled == mobileState.roamingDataEnabled && Intrinsics.areEqual((Object) this.telephonyDisplayInfo, (Object) mobileState.telephonyDisplayInfo) && Intrinsics.areEqual((Object) this.serviceState, (Object) mobileState.serviceState) && Intrinsics.areEqual((Object) this.signalStrength, (Object) mobileState.signalStrength);
        }
        throw new NullPointerException("null cannot be cast to non-null type com.android.systemui.statusbar.connectivity.MobileState");
    }

    public int hashCode() {
        int hashCode = super.hashCode() * 31;
        String str = this.networkName;
        int i = 0;
        int hashCode2 = (hashCode + (str == null ? 0 : str.hashCode())) * 31;
        String str2 = this.networkNameData;
        int hashCode3 = (((((((((((((((((((((((((((((((((((hashCode2 + (str2 == null ? 0 : str2.hashCode())) * 31) + Boolean.hashCode(this.dataSim)) * 31) + Boolean.hashCode(this.dataConnected)) * 31) + Boolean.hashCode(this.isEmergency)) * 31) + Boolean.hashCode(this.airplaneMode)) * 31) + Boolean.hashCode(this.carrierNetworkChangeMode)) * 31) + Boolean.hashCode(this.isDefault)) * 31) + Boolean.hashCode(this.userSetup)) * 31) + Boolean.hashCode(this.roaming)) * 31) + this.dataState) * 31) + Boolean.hashCode(this.defaultDataOff)) * 31) + Boolean.hashCode(this.imsRegistered)) * 31) + Integer.hashCode(this.imsRegistrationTech)) * 31) + Boolean.hashCode(this.voiceCapable)) * 31) + Boolean.hashCode(this.videoCapable)) * 31) + Boolean.hashCode(this.mobileDataEnabled)) * 31) + Boolean.hashCode(this.roamingDataEnabled)) * 31) + this.telephonyDisplayInfo.hashCode()) * 31;
        ServiceState serviceState2 = this.serviceState;
        int hashCode4 = (hashCode3 + (serviceState2 == null ? 0 : serviceState2.hashCode())) * 31;
        SignalStrength signalStrength2 = this.signalStrength;
        if (signalStrength2 != null) {
            i = signalStrength2.hashCode();
        }
        return hashCode4 + i;
    }
}
