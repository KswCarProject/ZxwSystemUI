package com.android.systemui.media;

import com.android.systemui.media.MediaTimeoutListener;

/* compiled from: MediaTimeoutListener.kt */
public final class MediaTimeoutListener$onMediaDataLoaded$2$1 implements Runnable {
    public final /* synthetic */ String $key;
    public final /* synthetic */ MediaTimeoutListener this$0;

    public MediaTimeoutListener$onMediaDataLoaded$2$1(MediaTimeoutListener mediaTimeoutListener, String str) {
        this.this$0 = mediaTimeoutListener;
        this.$key = str;
    }

    public final void run() {
        MediaTimeoutListener.PlaybackStateListener playbackStateListener = (MediaTimeoutListener.PlaybackStateListener) this.this$0.mediaListeners.get(this.$key);
        boolean z = false;
        if (playbackStateListener != null && playbackStateListener.isPlaying()) {
            z = true;
        }
        if (z) {
            this.this$0.logger.logDelayedUpdate(this.$key);
            this.this$0.getTimeoutCallback().invoke(this.$key, Boolean.FALSE);
        }
    }
}
