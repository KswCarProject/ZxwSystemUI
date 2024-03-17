package com.android.systemui.media;

/* compiled from: MediaSessionBasedFilter.kt */
public final class MediaSessionBasedFilter$onMediaDataRemoved$1 implements Runnable {
    public final /* synthetic */ String $key;
    public final /* synthetic */ MediaSessionBasedFilter this$0;

    public MediaSessionBasedFilter$onMediaDataRemoved$1(MediaSessionBasedFilter mediaSessionBasedFilter, String str) {
        this.this$0 = mediaSessionBasedFilter;
        this.$key = str;
    }

    public final void run() {
        this.this$0.keyedTokens.remove(this.$key);
        this.this$0.dispatchMediaDataRemoved(this.$key);
    }
}
