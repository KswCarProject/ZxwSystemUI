package com.android.systemui.media;

import android.content.ComponentName;
import android.content.Context;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.MediaSessionManager;
import com.android.systemui.media.MediaDataManager;
import com.android.systemui.statusbar.phone.NotificationListenerWithPlugins;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt__IterablesKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: MediaSessionBasedFilter.kt */
public final class MediaSessionBasedFilter implements MediaDataManager.Listener {
    @NotNull
    public final Executor backgroundExecutor;
    @NotNull
    public final Executor foregroundExecutor;
    @NotNull
    public final Map<String, Set<MediaSession.Token>> keyedTokens = new LinkedHashMap();
    @NotNull
    public final Set<MediaDataManager.Listener> listeners = new LinkedHashSet();
    @NotNull
    public final LinkedHashMap<String, List<MediaController>> packageControllers = new LinkedHashMap<>();
    @NotNull
    public final MediaSessionBasedFilter$sessionListener$1 sessionListener = new MediaSessionBasedFilter$sessionListener$1(this);
    @NotNull
    public final MediaSessionManager sessionManager;
    @NotNull
    public final Set<MediaSession.Token> tokensWithNotifications = new LinkedHashSet();

    public MediaSessionBasedFilter(@NotNull final Context context, @NotNull MediaSessionManager mediaSessionManager, @NotNull Executor executor, @NotNull Executor executor2) {
        this.sessionManager = mediaSessionManager;
        this.foregroundExecutor = executor;
        this.backgroundExecutor = executor2;
        executor2.execute(new Runnable() {
            public final void run() {
                ComponentName componentName = new ComponentName(context, NotificationListenerWithPlugins.class);
                this.sessionManager.addOnActiveSessionsChangedListener(this.sessionListener, componentName);
                MediaSessionBasedFilter mediaSessionBasedFilter = this;
                mediaSessionBasedFilter.handleControllersChanged(mediaSessionBasedFilter.sessionManager.getActiveSessions(componentName));
            }
        });
    }

    public final boolean addListener(@NotNull MediaDataManager.Listener listener) {
        return this.listeners.add(listener);
    }

    public void onMediaDataLoaded(@NotNull String str, @Nullable String str2, @NotNull MediaData mediaData, boolean z, int i, boolean z2) {
        this.backgroundExecutor.execute(new MediaSessionBasedFilter$onMediaDataLoaded$1(mediaData, str2, str, this, z));
    }

    public void onSmartspaceMediaDataLoaded(@NotNull String str, @NotNull SmartspaceMediaData smartspaceMediaData, boolean z) {
        this.backgroundExecutor.execute(new MediaSessionBasedFilter$onSmartspaceMediaDataLoaded$1(this, str, smartspaceMediaData));
    }

    public void onMediaDataRemoved(@NotNull String str) {
        this.backgroundExecutor.execute(new MediaSessionBasedFilter$onMediaDataRemoved$1(this, str));
    }

    public void onSmartspaceMediaDataRemoved(@NotNull String str, boolean z) {
        this.backgroundExecutor.execute(new MediaSessionBasedFilter$onSmartspaceMediaDataRemoved$1(this, str, z));
    }

    public final void dispatchMediaDataLoaded(String str, String str2, MediaData mediaData, boolean z) {
        this.foregroundExecutor.execute(new MediaSessionBasedFilter$dispatchMediaDataLoaded$1(this, str, str2, mediaData, z));
    }

    public final void dispatchMediaDataRemoved(String str) {
        this.foregroundExecutor.execute(new MediaSessionBasedFilter$dispatchMediaDataRemoved$1(this, str));
    }

    public final void dispatchSmartspaceMediaDataLoaded(String str, SmartspaceMediaData smartspaceMediaData) {
        this.foregroundExecutor.execute(new MediaSessionBasedFilter$dispatchSmartspaceMediaDataLoaded$1(this, str, smartspaceMediaData));
    }

    public final void dispatchSmartspaceMediaDataRemoved(String str, boolean z) {
        this.foregroundExecutor.execute(new MediaSessionBasedFilter$dispatchSmartspaceMediaDataRemoved$1(this, str, z));
    }

    public final void handleControllersChanged(List<MediaController> list) {
        Boolean bool;
        this.packageControllers.clear();
        Iterable<MediaController> iterable = list;
        for (MediaController mediaController : iterable) {
            List list2 = this.packageControllers.get(mediaController.getPackageName());
            if (list2 == null) {
                bool = null;
            } else {
                bool = Boolean.valueOf(list2.add(mediaController));
            }
            if (bool == null) {
                List list3 = (List) this.packageControllers.put(mediaController.getPackageName(), CollectionsKt__CollectionsKt.mutableListOf(mediaController));
            }
        }
        Set<MediaSession.Token> set = this.tokensWithNotifications;
        ArrayList arrayList = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(iterable, 10));
        for (MediaController sessionToken : iterable) {
            arrayList.add(sessionToken.getSessionToken());
        }
        set.retainAll(arrayList);
    }
}
