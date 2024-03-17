package com.android.systemui.media;

import com.android.settingslib.media.MediaDevice;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: MediaDeviceManager.kt */
public final class AboutToConnectDevice {
    @Nullable
    public final MediaDeviceData backupMediaDeviceData;
    @Nullable
    public final MediaDevice fullMediaDevice;

    public AboutToConnectDevice() {
        this((MediaDevice) null, (MediaDeviceData) null, 3, (DefaultConstructorMarker) null);
    }

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AboutToConnectDevice)) {
            return false;
        }
        AboutToConnectDevice aboutToConnectDevice = (AboutToConnectDevice) obj;
        return Intrinsics.areEqual((Object) this.fullMediaDevice, (Object) aboutToConnectDevice.fullMediaDevice) && Intrinsics.areEqual((Object) this.backupMediaDeviceData, (Object) aboutToConnectDevice.backupMediaDeviceData);
    }

    public int hashCode() {
        MediaDevice mediaDevice = this.fullMediaDevice;
        int i = 0;
        int hashCode = (mediaDevice == null ? 0 : mediaDevice.hashCode()) * 31;
        MediaDeviceData mediaDeviceData = this.backupMediaDeviceData;
        if (mediaDeviceData != null) {
            i = mediaDeviceData.hashCode();
        }
        return hashCode + i;
    }

    @NotNull
    public String toString() {
        return "AboutToConnectDevice(fullMediaDevice=" + this.fullMediaDevice + ", backupMediaDeviceData=" + this.backupMediaDeviceData + ')';
    }

    public AboutToConnectDevice(@Nullable MediaDevice mediaDevice, @Nullable MediaDeviceData mediaDeviceData) {
        this.fullMediaDevice = mediaDevice;
        this.backupMediaDeviceData = mediaDeviceData;
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ AboutToConnectDevice(MediaDevice mediaDevice, MediaDeviceData mediaDeviceData, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? null : mediaDevice, (i & 2) != 0 ? null : mediaDeviceData);
    }

    @Nullable
    public final MediaDevice getFullMediaDevice() {
        return this.fullMediaDevice;
    }

    @Nullable
    public final MediaDeviceData getBackupMediaDeviceData() {
        return this.backupMediaDeviceData;
    }
}
