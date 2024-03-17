package com.android.systemui.media;

import com.android.systemui.media.MediaDataManager;
import kotlin.collections.CollectionsKt___CollectionsKt;

/* compiled from: MediaSessionBasedFilter.kt */
public final class MediaSessionBasedFilter$dispatchMediaDataLoaded$1 implements Runnable {
    public final /* synthetic */ boolean $immediately;
    public final /* synthetic */ MediaData $info;
    public final /* synthetic */ String $key;
    public final /* synthetic */ String $oldKey;
    public final /* synthetic */ MediaSessionBasedFilter this$0;

    public MediaSessionBasedFilter$dispatchMediaDataLoaded$1(MediaSessionBasedFilter mediaSessionBasedFilter, String str, String str2, MediaData mediaData, boolean z) {
        this.this$0 = mediaSessionBasedFilter;
        this.$key = str;
        this.$oldKey = str2;
        this.$info = mediaData;
        this.$immediately = z;
    }

    public final void run() {
        String str = this.$key;
        String str2 = this.$oldKey;
        MediaData mediaData = this.$info;
        boolean z = this.$immediately;
        for (MediaDataManager.Listener onMediaDataLoaded$default : CollectionsKt___CollectionsKt.toSet(this.this$0.listeners)) {
            MediaDataManager.Listener.DefaultImpls.onMediaDataLoaded$default(onMediaDataLoaded$default, str, str2, mediaData, z, 0, false, 48, (Object) null);
        }
    }
}
