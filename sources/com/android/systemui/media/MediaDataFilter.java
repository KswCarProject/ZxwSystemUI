package com.android.systemui.media;

import android.app.PendingIntent;
import android.app.smartspace.SmartspaceAction;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.media.session.MediaSession;
import android.util.Log;
import androidx.appcompat.R$styleable;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.InstanceId;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.broadcast.BroadcastSender;
import com.android.systemui.media.MediaDataManager;
import com.android.systemui.settings.CurrentUserTracker;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.util.time.SystemClock;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.collections.MapsKt__MapsJVMKt;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: MediaDataFilter.kt */
public final class MediaDataFilter implements MediaDataManager.Listener {
    @NotNull
    public final Set<MediaDataManager.Listener> _listeners = new LinkedHashSet();
    @NotNull
    public final LinkedHashMap<String, MediaData> allEntries = new LinkedHashMap<>();
    @NotNull
    public final BroadcastDispatcher broadcastDispatcher;
    @NotNull
    public final BroadcastSender broadcastSender;
    @NotNull
    public final Context context;
    @NotNull
    public final Executor executor;
    @NotNull
    public final NotificationLockscreenUserManager lockscreenUserManager;
    @NotNull
    public final MediaUiEventLogger logger;
    public MediaDataManager mediaDataManager;
    @Nullable
    public String reactivatedKey;
    @NotNull
    public SmartspaceMediaData smartspaceMediaData = MediaDataManagerKt.getEMPTY_SMARTSPACE_MEDIA_DATA();
    @NotNull
    public final SystemClock systemClock;
    @NotNull
    public final LinkedHashMap<String, MediaData> userEntries = new LinkedHashMap<>();
    @NotNull
    public final CurrentUserTracker userTracker;

    public MediaDataFilter(@NotNull Context context2, @NotNull BroadcastDispatcher broadcastDispatcher2, @NotNull BroadcastSender broadcastSender2, @NotNull NotificationLockscreenUserManager notificationLockscreenUserManager, @NotNull Executor executor2, @NotNull SystemClock systemClock2, @NotNull MediaUiEventLogger mediaUiEventLogger) {
        this.context = context2;
        this.broadcastDispatcher = broadcastDispatcher2;
        this.broadcastSender = broadcastSender2;
        this.lockscreenUserManager = notificationLockscreenUserManager;
        this.executor = executor2;
        this.systemClock = systemClock2;
        this.logger = mediaUiEventLogger;
        AnonymousClass1 r1 = new CurrentUserTracker(this, broadcastDispatcher2) {
            public final /* synthetic */ MediaDataFilter this$0;

            {
                this.this$0 = r1;
            }

            public void onUserSwitched(int i) {
                this.this$0.executor.execute(new MediaDataFilter$1$onUserSwitched$1(this.this$0, i));
            }
        };
        this.userTracker = r1;
        r1.startTracking();
    }

    @NotNull
    public final Set<MediaDataManager.Listener> getListeners$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
        return CollectionsKt___CollectionsKt.toSet(this._listeners);
    }

    @NotNull
    public final MediaDataManager getMediaDataManager$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
        MediaDataManager mediaDataManager2 = this.mediaDataManager;
        if (mediaDataManager2 != null) {
            return mediaDataManager2;
        }
        return null;
    }

    public final void setMediaDataManager$frameworks__base__packages__SystemUI__android_common__SystemUI_core(@NotNull MediaDataManager mediaDataManager2) {
        this.mediaDataManager = mediaDataManager2;
    }

    public void onMediaDataLoaded(@NotNull String str, @Nullable String str2, @NotNull MediaData mediaData, boolean z, int i, boolean z2) {
        if (str2 != null && !Intrinsics.areEqual((Object) str2, (Object) str)) {
            this.allEntries.remove(str2);
        }
        this.allEntries.put(str, mediaData);
        if (this.lockscreenUserManager.isCurrentProfile(mediaData.getUserId())) {
            if (str2 != null && !Intrinsics.areEqual((Object) str2, (Object) str)) {
                this.userEntries.remove(str2);
            }
            this.userEntries.put(str, mediaData);
            for (MediaDataManager.Listener onMediaDataLoaded$default : getListeners$frameworks__base__packages__SystemUI__android_common__SystemUI_core()) {
                MediaDataManager.Listener.DefaultImpls.onMediaDataLoaded$default(onMediaDataLoaded$default, str, str2, mediaData, false, 0, false, 56, (Object) null);
            }
        }
    }

    public void onSmartspaceMediaDataLoaded(@NotNull String str, @NotNull SmartspaceMediaData smartspaceMediaData2, boolean z) {
        SmartspaceMediaData smartspaceMediaData3 = smartspaceMediaData2;
        if (!smartspaceMediaData2.isActive()) {
            Log.d("MediaDataFilter", "Inactive recommendation data. Skip triggering.");
            return;
        }
        this.smartspaceMediaData = smartspaceMediaData3;
        SortedMap<String, MediaData> sortedMap = MapsKt__MapsJVMKt.toSortedMap(this.userEntries, new MediaDataFilter$onSmartspaceMediaDataLoaded$$inlined$compareBy$1(this));
        long timeSinceActiveForMostRecentMedia = timeSinceActiveForMostRecentMedia(sortedMap);
        long smartspace_max_age = MediaDataFilterKt.getSMARTSPACE_MAX_AGE();
        SmartspaceAction cardAction = smartspaceMediaData2.getCardAction();
        if (cardAction != null) {
            long j = cardAction.getExtras().getLong("resumable_media_max_age_seconds", 0);
            if (j > 0) {
                smartspace_max_age = TimeUnit.SECONDS.toMillis(j);
            }
        }
        boolean z2 = true;
        boolean z3 = !hasActiveMedia() && hasAnyMedia();
        if (timeSinceActiveForMostRecentMedia < smartspace_max_age) {
            if (z3) {
                String lastKey = sortedMap.lastKey();
                Log.d("MediaDataFilter", "reactivating " + lastKey + " instead of smartspace");
                this.reactivatedKey = lastKey;
                Object obj = sortedMap.get(lastKey);
                Intrinsics.checkNotNull(obj);
                MediaData copy$default = MediaData.copy$default((MediaData) obj, 0, false, (String) null, (Icon) null, (CharSequence) null, (CharSequence) null, (Icon) null, (List) null, (List) null, (MediaButton) null, (String) null, (MediaSession.Token) null, (PendingIntent) null, (MediaDeviceData) null, true, (Runnable) null, 0, false, (String) null, false, (Boolean) null, false, 0, (InstanceId) null, 0, 33538047, (Object) null);
                this.logger.logRecommendationActivated(copy$default.getAppUid(), copy$default.getPackageName(), copy$default.getInstanceId());
                for (MediaDataManager.Listener onMediaDataLoaded$default : getListeners$frameworks__base__packages__SystemUI__android_common__SystemUI_core()) {
                    MediaDataManager.Listener.DefaultImpls.onMediaDataLoaded$default(onMediaDataLoaded$default, lastKey, lastKey, copy$default, false, (int) (this.systemClock.currentTimeMillis() - smartspaceMediaData2.getHeadphoneConnectionTimeMillis()), true, 8, (Object) null);
                }
            }
            z2 = false;
        }
        if (!smartspaceMediaData2.isValid()) {
            Log.d("MediaDataFilter", "Invalid recommendation data. Skip showing the rec card");
            return;
        }
        this.logger.logRecommendationAdded(this.smartspaceMediaData.getPackageName(), this.smartspaceMediaData.getInstanceId());
        for (MediaDataManager.Listener onSmartspaceMediaDataLoaded : getListeners$frameworks__base__packages__SystemUI__android_common__SystemUI_core()) {
            onSmartspaceMediaDataLoaded.onSmartspaceMediaDataLoaded(str, smartspaceMediaData3, z2);
        }
    }

    public void onMediaDataRemoved(@NotNull String str) {
        this.allEntries.remove(str);
        if (((MediaData) this.userEntries.remove(str)) != null) {
            for (MediaDataManager.Listener onMediaDataRemoved : getListeners$frameworks__base__packages__SystemUI__android_common__SystemUI_core()) {
                onMediaDataRemoved.onMediaDataRemoved(str);
            }
        }
    }

    public void onSmartspaceMediaDataRemoved(@NotNull String str, boolean z) {
        String str2 = this.reactivatedKey;
        if (str2 != null) {
            this.reactivatedKey = null;
            Log.d("MediaDataFilter", Intrinsics.stringPlus("expiring reactivated key ", str2));
            MediaData mediaData = this.userEntries.get(str2);
            if (mediaData != null) {
                for (MediaDataManager.Listener onMediaDataLoaded$default : getListeners$frameworks__base__packages__SystemUI__android_common__SystemUI_core()) {
                    MediaDataManager.Listener.DefaultImpls.onMediaDataLoaded$default(onMediaDataLoaded$default, str2, str2, mediaData, z, 0, false, 48, (Object) null);
                }
            }
        }
        if (this.smartspaceMediaData.isActive()) {
            this.smartspaceMediaData = SmartspaceMediaData.copy$default(MediaDataManagerKt.getEMPTY_SMARTSPACE_MEDIA_DATA(), this.smartspaceMediaData.getTargetId(), false, (String) null, (SmartspaceAction) null, (List) null, (Intent) null, 0, this.smartspaceMediaData.getInstanceId(), R$styleable.AppCompatTheme_windowNoTitle, (Object) null);
        }
        for (MediaDataManager.Listener onSmartspaceMediaDataRemoved : getListeners$frameworks__base__packages__SystemUI__android_common__SystemUI_core()) {
            onSmartspaceMediaDataRemoved.onSmartspaceMediaDataRemoved(str, z);
        }
    }

    @VisibleForTesting
    public final void handleUserSwitched$frameworks__base__packages__SystemUI__android_common__SystemUI_core(int i) {
        Set<MediaDataManager.Listener> listeners$frameworks__base__packages__SystemUI__android_common__SystemUI_core = getListeners$frameworks__base__packages__SystemUI__android_common__SystemUI_core();
        List<String> mutableList = CollectionsKt___CollectionsKt.toMutableList(this.userEntries.keySet());
        this.userEntries.clear();
        for (String str : mutableList) {
            Log.d("MediaDataFilter", "Removing " + str + " after user change");
            for (MediaDataManager.Listener onMediaDataRemoved : listeners$frameworks__base__packages__SystemUI__android_common__SystemUI_core) {
                onMediaDataRemoved.onMediaDataRemoved(str);
            }
        }
        for (Map.Entry next : this.allEntries.entrySet()) {
            String str2 = (String) next.getKey();
            MediaData mediaData = (MediaData) next.getValue();
            if (this.lockscreenUserManager.isCurrentProfile(mediaData.getUserId())) {
                Log.d("MediaDataFilter", "Re-adding " + str2 + " after user change");
                this.userEntries.put(str2, mediaData);
                for (MediaDataManager.Listener onMediaDataLoaded$default : listeners$frameworks__base__packages__SystemUI__android_common__SystemUI_core) {
                    MediaDataManager.Listener.DefaultImpls.onMediaDataLoaded$default(onMediaDataLoaded$default, str2, (String) null, mediaData, false, 0, false, 56, (Object) null);
                }
            }
        }
    }

    public final void onSwipeToDismiss() {
        Log.d("MediaDataFilter", "Media carousel swiped away");
        for (String timedOut$frameworks__base__packages__SystemUI__android_common__SystemUI_core : CollectionsKt___CollectionsKt.toSet(this.userEntries.keySet())) {
            getMediaDataManager$frameworks__base__packages__SystemUI__android_common__SystemUI_core().setTimedOut$frameworks__base__packages__SystemUI__android_common__SystemUI_core(timedOut$frameworks__base__packages__SystemUI__android_common__SystemUI_core, true, true);
        }
        if (this.smartspaceMediaData.isActive()) {
            Intent dismissIntent = this.smartspaceMediaData.getDismissIntent();
            if (dismissIntent == null) {
                Log.w("MediaDataFilter", "Cannot create dismiss action click action: extras missing dismiss_intent.");
            } else if (dismissIntent.getComponent() == null || !Intrinsics.areEqual((Object) dismissIntent.getComponent().getClassName(), (Object) "com.google.android.apps.gsa.staticplugins.opa.smartspace.ExportedSmartspaceTrampolineActivity")) {
                this.broadcastSender.sendBroadcast(dismissIntent);
            } else {
                this.context.startActivity(dismissIntent);
            }
            this.smartspaceMediaData = SmartspaceMediaData.copy$default(MediaDataManagerKt.getEMPTY_SMARTSPACE_MEDIA_DATA(), this.smartspaceMediaData.getTargetId(), false, (String) null, (SmartspaceAction) null, (List) null, (Intent) null, 0, this.smartspaceMediaData.getInstanceId(), R$styleable.AppCompatTheme_windowNoTitle, (Object) null);
            getMediaDataManager$frameworks__base__packages__SystemUI__android_common__SystemUI_core().dismissSmartspaceRecommendation(this.smartspaceMediaData.getTargetId(), 0);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x002f  */
    /* JADX WARNING: Removed duplicated region for block: B:18:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final boolean hasActiveMediaOrRecommendation() {
        /*
            r4 = this;
            java.util.LinkedHashMap<java.lang.String, com.android.systemui.media.MediaData> r0 = r4.userEntries
            boolean r1 = r0.isEmpty()
            r2 = 1
            r3 = 0
            if (r1 == 0) goto L_0x000c
        L_0x000a:
            r0 = r3
            goto L_0x002d
        L_0x000c:
            java.util.Set r0 = r0.entrySet()
            java.util.Iterator r0 = r0.iterator()
        L_0x0014:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L_0x000a
            java.lang.Object r1 = r0.next()
            java.util.Map$Entry r1 = (java.util.Map.Entry) r1
            java.lang.Object r1 = r1.getValue()
            com.android.systemui.media.MediaData r1 = (com.android.systemui.media.MediaData) r1
            boolean r1 = r1.getActive()
            if (r1 == 0) goto L_0x0014
            r0 = r2
        L_0x002d:
            if (r0 != 0) goto L_0x0041
            com.android.systemui.media.SmartspaceMediaData r0 = r4.smartspaceMediaData
            boolean r0 = r0.isActive()
            if (r0 == 0) goto L_0x0040
            com.android.systemui.media.SmartspaceMediaData r4 = r4.smartspaceMediaData
            boolean r4 = r4.isValid()
            if (r4 == 0) goto L_0x0040
            goto L_0x0041
        L_0x0040:
            r2 = r3
        L_0x0041:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.MediaDataFilter.hasActiveMediaOrRecommendation():boolean");
    }

    public final boolean hasAnyMediaOrRecommendation() {
        if (!this.userEntries.isEmpty()) {
            return true;
        }
        if (!this.smartspaceMediaData.isActive() || !this.smartspaceMediaData.isValid()) {
            return false;
        }
        return true;
    }

    public final boolean hasActiveMedia() {
        LinkedHashMap<String, MediaData> linkedHashMap = this.userEntries;
        if (linkedHashMap.isEmpty()) {
            return false;
        }
        for (Map.Entry<String, MediaData> value : linkedHashMap.entrySet()) {
            if (((MediaData) value.getValue()).getActive()) {
                return true;
            }
        }
        return false;
    }

    public final boolean hasAnyMedia() {
        return !this.userEntries.isEmpty();
    }

    public final boolean addListener(@NotNull MediaDataManager.Listener listener) {
        return this._listeners.add(listener);
    }

    public final boolean removeListener(@NotNull MediaDataManager.Listener listener) {
        return this._listeners.remove(listener);
    }

    public final long timeSinceActiveForMostRecentMedia(SortedMap<String, MediaData> sortedMap) {
        if (sortedMap.isEmpty()) {
            return Long.MAX_VALUE;
        }
        long elapsedRealtime = this.systemClock.elapsedRealtime();
        MediaData mediaData = (MediaData) sortedMap.get(sortedMap.lastKey());
        if (mediaData == null) {
            return Long.MAX_VALUE;
        }
        return elapsedRealtime - mediaData.getLastActive();
    }
}
