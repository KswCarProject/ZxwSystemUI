package com.android.systemui.media;

import android.app.PendingIntent;
import android.graphics.drawable.Icon;
import android.media.session.MediaSession;
import com.android.internal.logging.InstanceId;
import com.android.systemui.util.time.SystemClock;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import kotlin.Triple;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt__IterablesKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: MediaCarouselController.kt */
public final class MediaPlayerData {
    @NotNull
    public static final MediaData EMPTY = new MediaData(-1, false, (String) null, (Icon) null, (CharSequence) null, (CharSequence) null, (Icon) null, CollectionsKt__CollectionsKt.emptyList(), CollectionsKt__CollectionsKt.emptyList(), (MediaButton) null, "INVALID", (MediaSession.Token) null, (PendingIntent) null, (MediaDeviceData) null, true, (Runnable) null, 0, false, (String) null, false, (Boolean) null, false, 0, InstanceId.fakeInstanceId(-1), -1, 8323584, (DefaultConstructorMarker) null);
    @NotNull
    public static final MediaPlayerData INSTANCE = new MediaPlayerData();
    @NotNull
    public static final Comparator<MediaSortKey> comparator;
    @NotNull
    public static final Map<String, MediaSortKey> mediaData = new LinkedHashMap();
    @NotNull
    public static final TreeMap<MediaSortKey, MediaControlPanel> mediaPlayers;
    public static boolean shouldPrioritizeSs;
    @Nullable
    public static SmartspaceMediaData smartspaceMediaData;

    private MediaPlayerData() {
    }

    static {
        MediaPlayerData$special$$inlined$thenByDescending$8 mediaPlayerData$special$$inlined$thenByDescending$8 = new MediaPlayerData$special$$inlined$thenByDescending$8(new MediaPlayerData$special$$inlined$thenByDescending$7(new MediaPlayerData$special$$inlined$thenByDescending$6(new MediaPlayerData$special$$inlined$thenByDescending$5(new MediaPlayerData$special$$inlined$thenByDescending$4(new MediaPlayerData$special$$inlined$thenByDescending$3(new MediaPlayerData$special$$inlined$thenByDescending$2(new MediaPlayerData$special$$inlined$thenByDescending$1(new MediaPlayerData$special$$inlined$compareByDescending$1()))))))));
        comparator = mediaPlayerData$special$$inlined$thenByDescending$8;
        mediaPlayers = new TreeMap<>(mediaPlayerData$special$$inlined$thenByDescending$8);
    }

    public final boolean getShouldPrioritizeSs$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
        return shouldPrioritizeSs;
    }

    @Nullable
    public final SmartspaceMediaData getSmartspaceMediaData$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
        return smartspaceMediaData;
    }

    /* compiled from: MediaCarouselController.kt */
    public static final class MediaSortKey {
        @NotNull
        public final MediaData data;
        public final boolean isSsMediaRec;
        public final boolean isSsReactivated;
        public final long updateTime;

        public boolean equals(@Nullable Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof MediaSortKey)) {
                return false;
            }
            MediaSortKey mediaSortKey = (MediaSortKey) obj;
            return this.isSsMediaRec == mediaSortKey.isSsMediaRec && Intrinsics.areEqual((Object) this.data, (Object) mediaSortKey.data) && this.updateTime == mediaSortKey.updateTime && this.isSsReactivated == mediaSortKey.isSsReactivated;
        }

        public int hashCode() {
            boolean z = this.isSsMediaRec;
            boolean z2 = true;
            if (z) {
                z = true;
            }
            int hashCode = (((((z ? 1 : 0) * true) + this.data.hashCode()) * 31) + Long.hashCode(this.updateTime)) * 31;
            boolean z3 = this.isSsReactivated;
            if (!z3) {
                z2 = z3;
            }
            return hashCode + (z2 ? 1 : 0);
        }

        @NotNull
        public String toString() {
            return "MediaSortKey(isSsMediaRec=" + this.isSsMediaRec + ", data=" + this.data + ", updateTime=" + this.updateTime + ", isSsReactivated=" + this.isSsReactivated + ')';
        }

        public MediaSortKey(boolean z, @NotNull MediaData mediaData, long j, boolean z2) {
            this.isSsMediaRec = z;
            this.data = mediaData;
            this.updateTime = j;
            this.isSsReactivated = z2;
        }

        public final boolean isSsMediaRec() {
            return this.isSsMediaRec;
        }

        @NotNull
        public final MediaData getData() {
            return this.data;
        }

        public final long getUpdateTime() {
            return this.updateTime;
        }

        public final boolean isSsReactivated() {
            return this.isSsReactivated;
        }
    }

    public final void addMediaPlayer(@NotNull String str, @NotNull MediaData mediaData2, @NotNull MediaControlPanel mediaControlPanel, @NotNull SystemClock systemClock, boolean z, @Nullable MediaCarouselControllerLogger mediaCarouselControllerLogger) {
        MediaControlPanel removeMediaPlayer = removeMediaPlayer(str);
        if (!(removeMediaPlayer == null || Intrinsics.areEqual((Object) removeMediaPlayer, (Object) mediaControlPanel) || mediaCarouselControllerLogger == null)) {
            mediaCarouselControllerLogger.logPotentialMemoryLeak(str);
        }
        MediaSortKey mediaSortKey = new MediaSortKey(false, mediaData2, systemClock.currentTimeMillis(), z);
        mediaData.put(str, mediaSortKey);
        mediaPlayers.put(mediaSortKey, mediaControlPanel);
    }

    public final void addMediaRecommendation(@NotNull String str, @NotNull SmartspaceMediaData smartspaceMediaData2, @NotNull MediaControlPanel mediaControlPanel, boolean z, @NotNull SystemClock systemClock, @Nullable MediaCarouselControllerLogger mediaCarouselControllerLogger) {
        String str2 = str;
        MediaControlPanel mediaControlPanel2 = mediaControlPanel;
        MediaCarouselControllerLogger mediaCarouselControllerLogger2 = mediaCarouselControllerLogger;
        shouldPrioritizeSs = z;
        MediaControlPanel removeMediaPlayer = removeMediaPlayer(str);
        if (!(removeMediaPlayer == null || Intrinsics.areEqual((Object) removeMediaPlayer, (Object) mediaControlPanel2) || mediaCarouselControllerLogger2 == null)) {
            mediaCarouselControllerLogger2.logPotentialMemoryLeak(str2);
        }
        MediaSortKey mediaSortKey = new MediaSortKey(true, MediaData.copy$default(EMPTY, 0, false, (String) null, (Icon) null, (CharSequence) null, (CharSequence) null, (Icon) null, (List) null, (List) null, (MediaButton) null, (String) null, (MediaSession.Token) null, (PendingIntent) null, (MediaDeviceData) null, false, (Runnable) null, 0, false, (String) null, false, Boolean.FALSE, false, 0, (InstanceId) null, 0, 32505855, (Object) null), systemClock.currentTimeMillis(), true);
        mediaData.put(str2, mediaSortKey);
        mediaPlayers.put(mediaSortKey, mediaControlPanel2);
        smartspaceMediaData = smartspaceMediaData2;
    }

    public static /* synthetic */ void moveIfExists$default(MediaPlayerData mediaPlayerData, String str, String str2, MediaCarouselControllerLogger mediaCarouselControllerLogger, int i, Object obj) {
        if ((i & 4) != 0) {
            mediaCarouselControllerLogger = null;
        }
        mediaPlayerData.moveIfExists(str, str2, mediaCarouselControllerLogger);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:3:0x0009, code lost:
        r1 = mediaData;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void moveIfExists(@org.jetbrains.annotations.Nullable java.lang.String r2, @org.jetbrains.annotations.NotNull java.lang.String r3, @org.jetbrains.annotations.Nullable com.android.systemui.media.MediaCarouselControllerLogger r4) {
        /*
            r1 = this;
            if (r2 == 0) goto L_0x0029
            boolean r1 = kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r2, (java.lang.Object) r3)
            if (r1 == 0) goto L_0x0009
            goto L_0x0029
        L_0x0009:
            java.util.Map<java.lang.String, com.android.systemui.media.MediaPlayerData$MediaSortKey> r1 = mediaData
            java.lang.Object r2 = r1.remove(r2)
            com.android.systemui.media.MediaPlayerData$MediaSortKey r2 = (com.android.systemui.media.MediaPlayerData.MediaSortKey) r2
            if (r2 != 0) goto L_0x0014
            goto L_0x0029
        L_0x0014:
            com.android.systemui.media.MediaPlayerData r0 = INSTANCE
            com.android.systemui.media.MediaControlPanel r0 = r0.removeMediaPlayer(r3)
            if (r0 != 0) goto L_0x001d
            goto L_0x0023
        L_0x001d:
            if (r4 != 0) goto L_0x0020
            goto L_0x0023
        L_0x0020:
            r4.logPotentialMemoryLeak(r3)
        L_0x0023:
            java.lang.Object r1 = r1.put(r3, r2)
            com.android.systemui.media.MediaPlayerData$MediaSortKey r1 = (com.android.systemui.media.MediaPlayerData.MediaSortKey) r1
        L_0x0029:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.MediaPlayerData.moveIfExists(java.lang.String, java.lang.String, com.android.systemui.media.MediaCarouselControllerLogger):void");
    }

    @Nullable
    public final MediaControlPanel getMediaPlayer(@NotNull String str) {
        MediaSortKey mediaSortKey = mediaData.get(str);
        if (mediaSortKey == null) {
            return null;
        }
        return mediaPlayers.get(mediaSortKey);
    }

    public final int getMediaPlayerIndex(@NotNull String str) {
        MediaSortKey mediaSortKey = mediaData.get(str);
        int i = 0;
        for (Object next : mediaPlayers.entrySet()) {
            int i2 = i + 1;
            if (i < 0) {
                CollectionsKt__CollectionsKt.throwIndexOverflow();
            }
            if (Intrinsics.areEqual(((Map.Entry) next).getKey(), (Object) mediaSortKey)) {
                return i;
            }
            i = i2;
        }
        return -1;
    }

    @Nullable
    public final MediaControlPanel removeMediaPlayer(@NotNull String str) {
        MediaSortKey remove = mediaData.remove(str);
        if (remove == null) {
            return null;
        }
        if (remove.isSsMediaRec()) {
            smartspaceMediaData = null;
        }
        return mediaPlayers.remove(remove);
    }

    @NotNull
    public final List<Triple<String, MediaData, Boolean>> mediaData() {
        Iterable<Map.Entry> entrySet = mediaData.entrySet();
        ArrayList arrayList = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(entrySet, 10));
        for (Map.Entry entry : entrySet) {
            arrayList.add(new Triple(entry.getKey(), ((MediaSortKey) entry.getValue()).getData(), Boolean.valueOf(((MediaSortKey) entry.getValue()).isSsMediaRec())));
        }
        return arrayList;
    }

    @NotNull
    public final Set<String> dataKeys() {
        return mediaData.keySet();
    }

    @NotNull
    public final Collection<MediaControlPanel> players() {
        return mediaPlayers.values();
    }

    @NotNull
    public final Set<MediaSortKey> playerKeys() {
        return mediaPlayers.keySet();
    }

    public final int firstActiveMediaIndex() {
        int i = 0;
        for (Object next : mediaPlayers.entrySet()) {
            int i2 = i + 1;
            if (i < 0) {
                CollectionsKt__CollectionsKt.throwIndexOverflow();
            }
            Map.Entry entry = (Map.Entry) next;
            if (!((MediaSortKey) entry.getKey()).isSsMediaRec() && ((MediaSortKey) entry.getKey()).getData().getActive()) {
                return i;
            }
            i = i2;
        }
        return -1;
    }

    @Nullable
    public final String smartspaceMediaKey() {
        for (Map.Entry entry : mediaData.entrySet()) {
            if (((MediaSortKey) entry.getValue()).isSsMediaRec()) {
                return (String) entry.getKey();
            }
        }
        return null;
    }

    public final void clear() {
        mediaData.clear();
        mediaPlayers.clear();
    }

    public final boolean hasActiveMediaOrRecommendationCard() {
        SmartspaceMediaData smartspaceMediaData2 = smartspaceMediaData;
        if (smartspaceMediaData2 != null) {
            Boolean valueOf = smartspaceMediaData2 == null ? null : Boolean.valueOf(smartspaceMediaData2.isActive());
            Intrinsics.checkNotNull(valueOf);
            if (valueOf.booleanValue()) {
                return true;
            }
        }
        if (firstActiveMediaIndex() != -1) {
            return true;
        }
        return false;
    }

    public final boolean isSsReactivated(@NotNull String str) {
        MediaSortKey mediaSortKey = mediaData.get(str);
        if (mediaSortKey == null) {
            return false;
        }
        return mediaSortKey.isSsReactivated();
    }
}
