package com.android.systemui.media;

import com.android.systemui.media.MediaPlayerData;
import java.util.Comparator;
import kotlin.comparisons.ComparisonsKt__ComparisonsKt;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: Comparisons.kt */
public final class MediaPlayerData$special$$inlined$thenByDescending$1<T> implements Comparator {
    public final /* synthetic */ Comparator $this_thenByDescending;

    public MediaPlayerData$special$$inlined$thenByDescending$1(Comparator comparator) {
        this.$this_thenByDescending = comparator;
    }

    public final int compare(T t, T t2) {
        int compare = this.$this_thenByDescending.compare(t, t2);
        if (compare != 0) {
            return compare;
        }
        MediaPlayerData.MediaSortKey mediaSortKey = (MediaPlayerData.MediaSortKey) t2;
        Boolean isPlaying = mediaSortKey.getData().isPlaying();
        Boolean bool = Boolean.TRUE;
        boolean z = false;
        Boolean valueOf = Boolean.valueOf(Intrinsics.areEqual((Object) isPlaying, (Object) bool) && mediaSortKey.getData().getPlaybackLocation() == 1);
        MediaPlayerData.MediaSortKey mediaSortKey2 = (MediaPlayerData.MediaSortKey) t;
        if (Intrinsics.areEqual((Object) mediaSortKey2.getData().isPlaying(), (Object) bool) && mediaSortKey2.getData().getPlaybackLocation() == 1) {
            z = true;
        }
        return ComparisonsKt__ComparisonsKt.compareValues(valueOf, Boolean.valueOf(z));
    }
}
