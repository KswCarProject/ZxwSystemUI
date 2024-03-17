package com.android.systemui.media;

import com.android.systemui.media.MediaPlayerData;
import java.util.Comparator;
import kotlin.comparisons.ComparisonsKt__ComparisonsKt;

/* compiled from: Comparisons.kt */
public final class MediaPlayerData$special$$inlined$thenByDescending$8<T> implements Comparator {
    public final /* synthetic */ Comparator $this_thenByDescending;

    public MediaPlayerData$special$$inlined$thenByDescending$8(Comparator comparator) {
        this.$this_thenByDescending = comparator;
    }

    public final int compare(T t, T t2) {
        int compare = this.$this_thenByDescending.compare(t, t2);
        return compare != 0 ? compare : ComparisonsKt__ComparisonsKt.compareValues(((MediaPlayerData.MediaSortKey) t2).getData().getNotificationKey(), ((MediaPlayerData.MediaSortKey) t).getData().getNotificationKey());
    }
}
