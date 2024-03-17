package com.android.systemui.media;

/* compiled from: MediaSessionBasedFilter.kt */
public final class MediaSessionBasedFilter$onSmartspaceMediaDataRemoved$1 implements Runnable {
    public final /* synthetic */ boolean $immediately;
    public final /* synthetic */ String $key;
    public final /* synthetic */ MediaSessionBasedFilter this$0;

    public MediaSessionBasedFilter$onSmartspaceMediaDataRemoved$1(MediaSessionBasedFilter mediaSessionBasedFilter, String str, boolean z) {
        this.this$0 = mediaSessionBasedFilter;
        this.$key = str;
        this.$immediately = z;
    }

    public final void run() {
        this.this$0.dispatchSmartspaceMediaDataRemoved(this.$key, this.$immediately);
    }
}
