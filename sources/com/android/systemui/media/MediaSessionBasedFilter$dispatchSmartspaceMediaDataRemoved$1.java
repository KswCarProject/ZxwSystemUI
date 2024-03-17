package com.android.systemui.media;

import com.android.systemui.media.MediaDataManager;
import kotlin.collections.CollectionsKt___CollectionsKt;

/* compiled from: MediaSessionBasedFilter.kt */
public final class MediaSessionBasedFilter$dispatchSmartspaceMediaDataRemoved$1 implements Runnable {
    public final /* synthetic */ boolean $immediately;
    public final /* synthetic */ String $key;
    public final /* synthetic */ MediaSessionBasedFilter this$0;

    public MediaSessionBasedFilter$dispatchSmartspaceMediaDataRemoved$1(MediaSessionBasedFilter mediaSessionBasedFilter, String str, boolean z) {
        this.this$0 = mediaSessionBasedFilter;
        this.$key = str;
        this.$immediately = z;
    }

    public final void run() {
        String str = this.$key;
        boolean z = this.$immediately;
        for (MediaDataManager.Listener onSmartspaceMediaDataRemoved : CollectionsKt___CollectionsKt.toSet(this.this$0.listeners)) {
            onSmartspaceMediaDataRemoved.onSmartspaceMediaDataRemoved(str, z);
        }
    }
}
