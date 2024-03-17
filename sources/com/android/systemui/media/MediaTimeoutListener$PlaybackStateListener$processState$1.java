package com.android.systemui.media;

import com.android.systemui.media.MediaTimeoutListener;

/* compiled from: MediaTimeoutListener.kt */
public final class MediaTimeoutListener$PlaybackStateListener$processState$1 implements Runnable {
    public final /* synthetic */ MediaTimeoutListener.PlaybackStateListener this$0;

    public MediaTimeoutListener$PlaybackStateListener$processState$1(MediaTimeoutListener.PlaybackStateListener playbackStateListener) {
        this.this$0 = playbackStateListener;
    }

    public final void run() {
        this.this$0.doTimeout();
    }
}
