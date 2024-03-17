package com.android.systemui.media;

import android.media.session.MediaController;
import android.media.session.MediaSessionManager;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/* compiled from: MediaSessionBasedFilter.kt */
public final class MediaSessionBasedFilter$sessionListener$1 implements MediaSessionManager.OnActiveSessionsChangedListener {
    public final /* synthetic */ MediaSessionBasedFilter this$0;

    public MediaSessionBasedFilter$sessionListener$1(MediaSessionBasedFilter mediaSessionBasedFilter) {
        this.this$0 = mediaSessionBasedFilter;
    }

    public void onActiveSessionsChanged(@NotNull List<MediaController> list) {
        this.this$0.handleControllersChanged(list);
    }
}
