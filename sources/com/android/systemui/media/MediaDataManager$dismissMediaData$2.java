package com.android.systemui.media;

/* compiled from: MediaDataManager.kt */
public final class MediaDataManager$dismissMediaData$2 implements Runnable {
    public final /* synthetic */ String $key;
    public final /* synthetic */ MediaDataManager this$0;

    public MediaDataManager$dismissMediaData$2(MediaDataManager mediaDataManager, String str) {
        this.this$0 = mediaDataManager;
        this.$key = str;
    }

    public final void run() {
        this.this$0.removeEntry(this.$key);
    }
}
