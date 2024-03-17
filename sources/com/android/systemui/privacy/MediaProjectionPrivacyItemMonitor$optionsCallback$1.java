package com.android.systemui.privacy;

import com.android.systemui.privacy.PrivacyConfig;
import kotlin.Unit;

/* compiled from: MediaProjectionPrivacyItemMonitor.kt */
public final class MediaProjectionPrivacyItemMonitor$optionsCallback$1 implements PrivacyConfig.Callback {
    public final /* synthetic */ MediaProjectionPrivacyItemMonitor this$0;

    public MediaProjectionPrivacyItemMonitor$optionsCallback$1(MediaProjectionPrivacyItemMonitor mediaProjectionPrivacyItemMonitor) {
        this.this$0 = mediaProjectionPrivacyItemMonitor;
    }

    public void onFlagMediaProjectionChanged(boolean z) {
        Object access$getLock$p = this.this$0.lock;
        MediaProjectionPrivacyItemMonitor mediaProjectionPrivacyItemMonitor = this.this$0;
        synchronized (access$getLock$p) {
            mediaProjectionPrivacyItemMonitor.mediaProjectionAvailable = mediaProjectionPrivacyItemMonitor.privacyConfig.getMediaProjectionAvailable();
            mediaProjectionPrivacyItemMonitor.setListeningStateLocked();
            Unit unit = Unit.INSTANCE;
        }
        this.this$0.dispatchOnPrivacyItemsChanged();
    }
}
