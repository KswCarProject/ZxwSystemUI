package com.android.systemui.media;

import android.media.session.MediaController;
import com.android.systemui.media.MediaDeviceManager;
import com.android.systemui.media.muteawait.MediaMuteAwaitConnectionManager;

/* compiled from: MediaDeviceManager.kt */
public final class MediaDeviceManager$Entry$stop$1 implements Runnable {
    public final /* synthetic */ MediaDeviceManager.Entry this$0;
    public final /* synthetic */ MediaDeviceManager this$1;

    public MediaDeviceManager$Entry$stop$1(MediaDeviceManager.Entry entry, MediaDeviceManager mediaDeviceManager) {
        this.this$0 = entry;
        this.this$1 = mediaDeviceManager;
    }

    public final void run() {
        this.this$0.started = false;
        MediaController controller = this.this$0.getController();
        if (controller != null) {
            controller.unregisterCallback(this.this$0);
        }
        this.this$0.getLocalMediaManager().stopScan();
        this.this$0.getLocalMediaManager().unregisterCallback(this.this$0);
        MediaMuteAwaitConnectionManager muteAwaitConnectionManager = this.this$0.getMuteAwaitConnectionManager();
        if (muteAwaitConnectionManager != null) {
            muteAwaitConnectionManager.stopListening();
        }
        this.this$1.configurationController.removeCallback(this.this$0.configListener);
    }
}
