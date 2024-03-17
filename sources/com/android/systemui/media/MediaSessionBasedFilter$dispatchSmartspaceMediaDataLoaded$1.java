package com.android.systemui.media;

import com.android.systemui.media.MediaDataManager;
import kotlin.collections.CollectionsKt___CollectionsKt;

/* compiled from: MediaSessionBasedFilter.kt */
public final class MediaSessionBasedFilter$dispatchSmartspaceMediaDataLoaded$1 implements Runnable {
    public final /* synthetic */ SmartspaceMediaData $info;
    public final /* synthetic */ String $key;
    public final /* synthetic */ MediaSessionBasedFilter this$0;

    public MediaSessionBasedFilter$dispatchSmartspaceMediaDataLoaded$1(MediaSessionBasedFilter mediaSessionBasedFilter, String str, SmartspaceMediaData smartspaceMediaData) {
        this.this$0 = mediaSessionBasedFilter;
        this.$key = str;
        this.$info = smartspaceMediaData;
    }

    public final void run() {
        String str = this.$key;
        SmartspaceMediaData smartspaceMediaData = this.$info;
        for (MediaDataManager.Listener onSmartspaceMediaDataLoaded$default : CollectionsKt___CollectionsKt.toSet(this.this$0.listeners)) {
            MediaDataManager.Listener.DefaultImpls.onSmartspaceMediaDataLoaded$default(onSmartspaceMediaDataLoaded$default, str, smartspaceMediaData, false, 4, (Object) null);
        }
    }
}
