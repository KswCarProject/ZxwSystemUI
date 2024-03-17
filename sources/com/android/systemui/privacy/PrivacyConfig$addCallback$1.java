package com.android.systemui.privacy;

import com.android.systemui.privacy.PrivacyConfig;
import java.lang.ref.WeakReference;

/* compiled from: PrivacyConfig.kt */
public final class PrivacyConfig$addCallback$1 implements Runnable {
    public final /* synthetic */ WeakReference<PrivacyConfig.Callback> $callback;
    public final /* synthetic */ PrivacyConfig this$0;

    public PrivacyConfig$addCallback$1(PrivacyConfig privacyConfig, WeakReference<PrivacyConfig.Callback> weakReference) {
        this.this$0 = privacyConfig;
        this.$callback = weakReference;
    }

    public final void run() {
        this.this$0.callbacks.add(this.$callback);
    }
}
