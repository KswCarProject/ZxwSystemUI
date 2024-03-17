package com.android.systemui.media;

import com.android.systemui.media.MediaDeviceManager;
import com.android.systemui.statusbar.policy.ConfigurationController;

/* compiled from: MediaDeviceManager.kt */
public final class MediaDeviceManager$Entry$configListener$1 implements ConfigurationController.ConfigurationListener {
    public final /* synthetic */ MediaDeviceManager.Entry this$0;

    public MediaDeviceManager$Entry$configListener$1(MediaDeviceManager.Entry entry) {
        this.this$0 = entry;
    }

    public void onLocaleListChanged() {
        this.this$0.updateCurrent();
    }
}
