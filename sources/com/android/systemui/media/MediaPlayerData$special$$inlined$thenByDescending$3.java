package com.android.systemui.media;

import com.android.systemui.media.MediaPlayerData;
import java.util.Comparator;
import kotlin.comparisons.ComparisonsKt__ComparisonsKt;

/* compiled from: Comparisons.kt */
public final class MediaPlayerData$special$$inlined$thenByDescending$3<T> implements Comparator {
    public final /* synthetic */ Comparator $this_thenByDescending;

    public MediaPlayerData$special$$inlined$thenByDescending$3(Comparator comparator) {
        this.$this_thenByDescending = comparator;
    }

    public final int compare(T t, T t2) {
        int compare = this.$this_thenByDescending.compare(t, t2);
        if (compare != 0) {
            return compare;
        }
        MediaPlayerData mediaPlayerData = MediaPlayerData.INSTANCE;
        boolean z = true;
        Boolean valueOf = Boolean.valueOf(mediaPlayerData.getShouldPrioritizeSs$frameworks__base__packages__SystemUI__android_common__SystemUI_core() == ((MediaPlayerData.MediaSortKey) t2).isSsMediaRec());
        if (mediaPlayerData.getShouldPrioritizeSs$frameworks__base__packages__SystemUI__android_common__SystemUI_core() != ((MediaPlayerData.MediaSortKey) t).isSsMediaRec()) {
            z = false;
        }
        return ComparisonsKt__ComparisonsKt.compareValues(valueOf, Boolean.valueOf(z));
    }
}
