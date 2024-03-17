package com.android.systemui.privacy;

import android.media.projection.MediaProjectionInfo;
import android.media.projection.MediaProjectionManager;
import kotlin.Unit;
import org.jetbrains.annotations.NotNull;

/* compiled from: MediaProjectionPrivacyItemMonitor.kt */
public final class MediaProjectionPrivacyItemMonitor$mediaProjectionCallback$1 extends MediaProjectionManager.Callback {
    public final /* synthetic */ MediaProjectionPrivacyItemMonitor this$0;

    public MediaProjectionPrivacyItemMonitor$mediaProjectionCallback$1(MediaProjectionPrivacyItemMonitor mediaProjectionPrivacyItemMonitor) {
        this.this$0 = mediaProjectionPrivacyItemMonitor;
    }

    public void onStart(@NotNull MediaProjectionInfo mediaProjectionInfo) {
        Object access$getLock$p = this.this$0.lock;
        MediaProjectionPrivacyItemMonitor mediaProjectionPrivacyItemMonitor = this.this$0;
        synchronized (access$getLock$p) {
            mediaProjectionPrivacyItemMonitor.onMediaProjectionStartedLocked(mediaProjectionInfo);
            Unit unit = Unit.INSTANCE;
        }
        this.this$0.dispatchOnPrivacyItemsChanged();
    }

    public void onStop(@NotNull MediaProjectionInfo mediaProjectionInfo) {
        Object access$getLock$p = this.this$0.lock;
        MediaProjectionPrivacyItemMonitor mediaProjectionPrivacyItemMonitor = this.this$0;
        synchronized (access$getLock$p) {
            mediaProjectionPrivacyItemMonitor.onMediaProjectionStoppedLocked(mediaProjectionInfo);
            Unit unit = Unit.INSTANCE;
        }
        this.this$0.dispatchOnPrivacyItemsChanged();
    }
}
