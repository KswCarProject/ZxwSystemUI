package com.android.systemui.media;

import android.media.session.MediaController;
import com.android.systemui.media.MediaDeviceManager;
import com.android.systemui.media.muteawait.MediaMuteAwaitConnectionManager;

/* compiled from: MediaDeviceManager.kt */
public final class MediaDeviceManager$Entry$start$1 implements Runnable {
    public final /* synthetic */ MediaDeviceManager.Entry this$0;
    public final /* synthetic */ MediaDeviceManager this$1;

    public MediaDeviceManager$Entry$start$1(MediaDeviceManager.Entry entry, MediaDeviceManager mediaDeviceManager) {
        this.this$0 = entry;
        this.this$1 = mediaDeviceManager;
    }

    public final void run() {
        MediaController.PlaybackInfo playbackInfo;
        this.this$0.getLocalMediaManager().registerCallback(this.this$0);
        this.this$0.getLocalMediaManager().startScan();
        MediaMuteAwaitConnectionManager muteAwaitConnectionManager = this.this$0.getMuteAwaitConnectionManager();
        if (muteAwaitConnectionManager != null) {
            muteAwaitConnectionManager.startListening();
        }
        MediaDeviceManager.Entry entry = this.this$0;
        MediaController controller = entry.getController();
        int i = 0;
        if (!(controller == null || (playbackInfo = controller.getPlaybackInfo()) == null)) {
            i = playbackInfo.getPlaybackType();
        }
        entry.playbackType = i;
        MediaController controller2 = this.this$0.getController();
        if (controller2 != null) {
            controller2.registerCallback(this.this$0);
        }
        this.this$0.updateCurrent();
        this.this$0.started = true;
        this.this$1.configurationController.addCallback(this.this$0.configListener);
    }
}
