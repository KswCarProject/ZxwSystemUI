package com.android.systemui.privacy;

import com.android.systemui.privacy.PrivacyConfig;
import com.android.systemui.privacy.PrivacyItemController;
import java.lang.ref.WeakReference;

/* compiled from: PrivacyItemController.kt */
public final class PrivacyItemController$optionsCallback$1 implements PrivacyConfig.Callback {
    public final /* synthetic */ PrivacyItemController this$0;

    public PrivacyItemController$optionsCallback$1(PrivacyItemController privacyItemController) {
        this.this$0 = privacyItemController;
    }

    public void onFlagLocationChanged(boolean z) {
        for (WeakReference weakReference : this.this$0.callbacks) {
            PrivacyItemController.Callback callback = (PrivacyItemController.Callback) weakReference.get();
            if (callback != null) {
                callback.onFlagLocationChanged(z);
            }
        }
    }

    public void onFlagMicCameraChanged(boolean z) {
        for (WeakReference weakReference : this.this$0.callbacks) {
            PrivacyItemController.Callback callback = (PrivacyItemController.Callback) weakReference.get();
            if (callback != null) {
                callback.onFlagMicCameraChanged(z);
            }
        }
    }

    public void onFlagMediaProjectionChanged(boolean z) {
        for (WeakReference weakReference : this.this$0.callbacks) {
            PrivacyItemController.Callback callback = (PrivacyItemController.Callback) weakReference.get();
            if (callback != null) {
                callback.onFlagMediaProjectionChanged(z);
            }
        }
    }
}
