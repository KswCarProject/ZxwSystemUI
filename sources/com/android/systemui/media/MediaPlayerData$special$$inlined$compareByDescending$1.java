package com.android.systemui.media;

import com.android.systemui.media.MediaPlayerData;
import java.util.Comparator;
import kotlin.comparisons.ComparisonsKt__ComparisonsKt;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: Comparisons.kt */
public final class MediaPlayerData$special$$inlined$compareByDescending$1<T> implements Comparator {
    public final int compare(T t, T t2) {
        MediaPlayerData.MediaSortKey mediaSortKey = (MediaPlayerData.MediaSortKey) t2;
        Boolean isPlaying = mediaSortKey.getData().isPlaying();
        Boolean bool = Boolean.TRUE;
        boolean z = true;
        Boolean valueOf = Boolean.valueOf(Intrinsics.areEqual((Object) isPlaying, (Object) bool) && mediaSortKey.getData().getPlaybackLocation() == 0);
        MediaPlayerData.MediaSortKey mediaSortKey2 = (MediaPlayerData.MediaSortKey) t;
        if (!Intrinsics.areEqual((Object) mediaSortKey2.getData().isPlaying(), (Object) bool) || mediaSortKey2.getData().getPlaybackLocation() != 0) {
            z = false;
        }
        return ComparisonsKt__ComparisonsKt.compareValues(valueOf, Boolean.valueOf(z));
    }
}
