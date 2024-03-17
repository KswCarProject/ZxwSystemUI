package com.android.systemui.media;

import android.media.session.MediaController;

/* compiled from: SeekBarViewModel.kt */
public final class SeekBarViewModel$onSeek$1 implements Runnable {
    public final /* synthetic */ long $position;
    public final /* synthetic */ SeekBarViewModel this$0;

    public SeekBarViewModel$onSeek$1(SeekBarViewModel seekBarViewModel, long j) {
        this.this$0 = seekBarViewModel;
        this.$position = j;
    }

    public final void run() {
        MediaController.TransportControls transportControls;
        if (this.this$0.isFalseSeek) {
            this.this$0.setScrubbing(false);
            this.this$0.checkPlaybackPosition();
            return;
        }
        this.this$0.getLogSeek().invoke();
        MediaController access$getController$p = this.this$0.controller;
        if (!(access$getController$p == null || (transportControls = access$getController$p.getTransportControls()) == null)) {
            transportControls.seekTo(this.$position);
        }
        this.this$0.playbackState = null;
        this.this$0.setScrubbing(false);
    }
}
