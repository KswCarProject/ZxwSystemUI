package com.android.systemui.media;

import com.android.systemui.media.MediaDeviceManager;

/* compiled from: MediaDeviceManager.kt */
public final class MediaDeviceManager$Entry$current$1 implements Runnable {
    public final /* synthetic */ MediaDeviceData $value;
    public final /* synthetic */ MediaDeviceManager this$0;
    public final /* synthetic */ MediaDeviceManager.Entry this$1;

    public MediaDeviceManager$Entry$current$1(MediaDeviceManager mediaDeviceManager, MediaDeviceManager.Entry entry, MediaDeviceData mediaDeviceData) {
        this.this$0 = mediaDeviceManager;
        this.this$1 = entry;
        this.$value = mediaDeviceData;
    }

    public final void run() {
        this.this$0.processDevice(this.this$1.getKey(), this.this$1.getOldKey(), this.$value);
    }
}
