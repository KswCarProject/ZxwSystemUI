package com.android.systemui.media;

import com.android.systemui.media.MediaDataManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: MediaHost.kt */
public final class MediaHost$listener$1 implements MediaDataManager.Listener {
    public final /* synthetic */ MediaHost this$0;

    public MediaHost$listener$1(MediaHost mediaHost) {
        this.this$0 = mediaHost;
    }

    public void onMediaDataLoaded(@NotNull String str, @Nullable String str2, @NotNull MediaData mediaData, boolean z, int i, boolean z2) {
        if (z) {
            this.this$0.updateViewVisibility();
        }
    }

    public void onSmartspaceMediaDataLoaded(@NotNull String str, @NotNull SmartspaceMediaData smartspaceMediaData, boolean z) {
        this.this$0.updateViewVisibility();
    }

    public void onMediaDataRemoved(@NotNull String str) {
        this.this$0.updateViewVisibility();
    }

    public void onSmartspaceMediaDataRemoved(@NotNull String str, boolean z) {
        if (z) {
            this.this$0.updateViewVisibility();
        }
    }
}
