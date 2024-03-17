package com.android.systemui.media;

import android.app.smartspace.SmartspaceAction;
import android.content.Intent;
import com.android.internal.logging.InstanceId;
import java.util.ArrayList;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: SmartspaceMediaData.kt */
public final class SmartspaceMediaData {
    @Nullable
    public final SmartspaceAction cardAction;
    @Nullable
    public final Intent dismissIntent;
    public final long headphoneConnectionTimeMillis;
    @NotNull
    public final InstanceId instanceId;
    public final boolean isActive;
    @NotNull
    public final String packageName;
    @NotNull
    public final List<SmartspaceAction> recommendations;
    @NotNull
    public final String targetId;

    public static /* synthetic */ SmartspaceMediaData copy$default(SmartspaceMediaData smartspaceMediaData, String str, boolean z, String str2, SmartspaceAction smartspaceAction, List list, Intent intent, long j, InstanceId instanceId2, int i, Object obj) {
        SmartspaceMediaData smartspaceMediaData2 = smartspaceMediaData;
        int i2 = i;
        return smartspaceMediaData.copy((i2 & 1) != 0 ? smartspaceMediaData2.targetId : str, (i2 & 2) != 0 ? smartspaceMediaData2.isActive : z, (i2 & 4) != 0 ? smartspaceMediaData2.packageName : str2, (i2 & 8) != 0 ? smartspaceMediaData2.cardAction : smartspaceAction, (i2 & 16) != 0 ? smartspaceMediaData2.recommendations : list, (i2 & 32) != 0 ? smartspaceMediaData2.dismissIntent : intent, (i2 & 64) != 0 ? smartspaceMediaData2.headphoneConnectionTimeMillis : j, (i2 & 128) != 0 ? smartspaceMediaData2.instanceId : instanceId2);
    }

    @NotNull
    public final SmartspaceMediaData copy(@NotNull String str, boolean z, @NotNull String str2, @Nullable SmartspaceAction smartspaceAction, @NotNull List<SmartspaceAction> list, @Nullable Intent intent, long j, @NotNull InstanceId instanceId2) {
        return new SmartspaceMediaData(str, z, str2, smartspaceAction, list, intent, j, instanceId2);
    }

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SmartspaceMediaData)) {
            return false;
        }
        SmartspaceMediaData smartspaceMediaData = (SmartspaceMediaData) obj;
        return Intrinsics.areEqual((Object) this.targetId, (Object) smartspaceMediaData.targetId) && this.isActive == smartspaceMediaData.isActive && Intrinsics.areEqual((Object) this.packageName, (Object) smartspaceMediaData.packageName) && Intrinsics.areEqual((Object) this.cardAction, (Object) smartspaceMediaData.cardAction) && Intrinsics.areEqual((Object) this.recommendations, (Object) smartspaceMediaData.recommendations) && Intrinsics.areEqual((Object) this.dismissIntent, (Object) smartspaceMediaData.dismissIntent) && this.headphoneConnectionTimeMillis == smartspaceMediaData.headphoneConnectionTimeMillis && Intrinsics.areEqual((Object) this.instanceId, (Object) smartspaceMediaData.instanceId);
    }

    public int hashCode() {
        int hashCode = this.targetId.hashCode() * 31;
        boolean z = this.isActive;
        if (z) {
            z = true;
        }
        int hashCode2 = (((hashCode + (z ? 1 : 0)) * 31) + this.packageName.hashCode()) * 31;
        SmartspaceAction smartspaceAction = this.cardAction;
        int i = 0;
        int hashCode3 = (((hashCode2 + (smartspaceAction == null ? 0 : smartspaceAction.hashCode())) * 31) + this.recommendations.hashCode()) * 31;
        Intent intent = this.dismissIntent;
        if (intent != null) {
            i = intent.hashCode();
        }
        return ((((hashCode3 + i) * 31) + Long.hashCode(this.headphoneConnectionTimeMillis)) * 31) + this.instanceId.hashCode();
    }

    @NotNull
    public String toString() {
        return "SmartspaceMediaData(targetId=" + this.targetId + ", isActive=" + this.isActive + ", packageName=" + this.packageName + ", cardAction=" + this.cardAction + ", recommendations=" + this.recommendations + ", dismissIntent=" + this.dismissIntent + ", headphoneConnectionTimeMillis=" + this.headphoneConnectionTimeMillis + ", instanceId=" + this.instanceId + ')';
    }

    public SmartspaceMediaData(@NotNull String str, boolean z, @NotNull String str2, @Nullable SmartspaceAction smartspaceAction, @NotNull List<SmartspaceAction> list, @Nullable Intent intent, long j, @NotNull InstanceId instanceId2) {
        this.targetId = str;
        this.isActive = z;
        this.packageName = str2;
        this.cardAction = smartspaceAction;
        this.recommendations = list;
        this.dismissIntent = intent;
        this.headphoneConnectionTimeMillis = j;
        this.instanceId = instanceId2;
    }

    @NotNull
    public final String getTargetId() {
        return this.targetId;
    }

    public final boolean isActive() {
        return this.isActive;
    }

    @NotNull
    public final String getPackageName() {
        return this.packageName;
    }

    @Nullable
    public final SmartspaceAction getCardAction() {
        return this.cardAction;
    }

    @Nullable
    public final Intent getDismissIntent() {
        return this.dismissIntent;
    }

    public final long getHeadphoneConnectionTimeMillis() {
        return this.headphoneConnectionTimeMillis;
    }

    @NotNull
    public final InstanceId getInstanceId() {
        return this.instanceId;
    }

    public final boolean isValid() {
        return getValidRecommendations().size() >= 3;
    }

    @NotNull
    public final List<SmartspaceAction> getValidRecommendations() {
        ArrayList arrayList = new ArrayList();
        for (Object next : this.recommendations) {
            if (((SmartspaceAction) next).getIcon() != null) {
                arrayList.add(next);
            }
        }
        return arrayList;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:5:0x000e, code lost:
        r0 = (r0 = r0.getIntent()).getExtras();
     */
    @org.jetbrains.annotations.Nullable
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.lang.CharSequence getAppName(@org.jetbrains.annotations.NotNull android.content.Context r6) {
        /*
            r5 = this;
            android.app.smartspace.SmartspaceAction r0 = r5.cardAction
            r1 = 0
            if (r0 != 0) goto L_0x0007
        L_0x0005:
            r0 = r1
            goto L_0x001b
        L_0x0007:
            android.content.Intent r0 = r0.getIntent()
            if (r0 != 0) goto L_0x000e
            goto L_0x0005
        L_0x000e:
            android.os.Bundle r0 = r0.getExtras()
            if (r0 != 0) goto L_0x0015
            goto L_0x0005
        L_0x0015:
            java.lang.String r2 = "KEY_SMARTSPACE_APP_NAME"
            java.lang.String r0 = r0.getString(r2)
        L_0x001b:
            boolean r2 = android.text.TextUtils.isEmpty(r0)
            if (r2 != 0) goto L_0x0022
            return r0
        L_0x0022:
            android.content.pm.PackageManager r6 = r6.getPackageManager()
            java.lang.String r0 = r5.packageName
            android.content.Intent r0 = r6.getLaunchIntentForPackage(r0)
            r2 = 0
            if (r0 != 0) goto L_0x0059
            java.lang.String r0 = com.android.systemui.media.SmartspaceMediaDataKt.TAG
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "Package "
            r3.append(r4)
            java.lang.String r4 = r5.packageName
            r3.append(r4)
            java.lang.String r4 = " does not have a main launcher activity. Fallback to full app name"
            r3.append(r4)
            java.lang.String r3 = r3.toString()
            android.util.Log.w(r0, r3)
            java.lang.String r5 = r5.packageName     // Catch:{ NameNotFoundException -> 0x0058 }
            android.content.pm.ApplicationInfo r5 = r6.getApplicationInfo(r5, r2)     // Catch:{ NameNotFoundException -> 0x0058 }
            java.lang.CharSequence r1 = r6.getApplicationLabel(r5)     // Catch:{ NameNotFoundException -> 0x0058 }
        L_0x0058:
            return r1
        L_0x0059:
            android.content.pm.ActivityInfo r5 = r0.resolveActivityInfo(r6, r2)
            java.lang.CharSequence r5 = r5.loadLabel(r6)
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.SmartspaceMediaData.getAppName(android.content.Context):java.lang.CharSequence");
    }
}
