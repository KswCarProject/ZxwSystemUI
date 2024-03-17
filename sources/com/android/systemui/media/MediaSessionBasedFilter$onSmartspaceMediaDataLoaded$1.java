package com.android.systemui.media;

/* compiled from: MediaSessionBasedFilter.kt */
public final class MediaSessionBasedFilter$onSmartspaceMediaDataLoaded$1 implements Runnable {
    public final /* synthetic */ SmartspaceMediaData $data;
    public final /* synthetic */ String $key;
    public final /* synthetic */ MediaSessionBasedFilter this$0;

    public MediaSessionBasedFilter$onSmartspaceMediaDataLoaded$1(MediaSessionBasedFilter mediaSessionBasedFilter, String str, SmartspaceMediaData smartspaceMediaData) {
        this.this$0 = mediaSessionBasedFilter;
        this.$key = str;
        this.$data = smartspaceMediaData;
    }

    public final void run() {
        this.this$0.dispatchSmartspaceMediaDataLoaded(this.$key, this.$data);
    }
}
