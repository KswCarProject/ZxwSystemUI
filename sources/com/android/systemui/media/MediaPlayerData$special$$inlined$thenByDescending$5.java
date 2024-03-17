package com.android.systemui.media;

import com.android.systemui.media.MediaPlayerData;
import java.util.Comparator;
import kotlin.comparisons.ComparisonsKt__ComparisonsKt;

/* compiled from: Comparisons.kt */
public final class MediaPlayerData$special$$inlined$thenByDescending$5<T> implements Comparator {
    public final /* synthetic */ Comparator $this_thenByDescending;

    public MediaPlayerData$special$$inlined$thenByDescending$5(Comparator comparator) {
        this.$this_thenByDescending = comparator;
    }

    public final int compare(T t, T t2) {
        int compare = this.$this_thenByDescending.compare(t, t2);
        if (compare != 0) {
            return compare;
        }
        int playbackLocation = ((MediaPlayerData.MediaSortKey) t2).getData().getPlaybackLocation();
        boolean z = true;
        Boolean valueOf = Boolean.valueOf(playbackLocation != 2);
        if (((MediaPlayerData.MediaSortKey) t).getData().getPlaybackLocation() == 2) {
            z = false;
        }
        return ComparisonsKt__ComparisonsKt.compareValues(valueOf, Boolean.valueOf(z));
    }
}
