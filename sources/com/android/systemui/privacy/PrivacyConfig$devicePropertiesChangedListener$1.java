package com.android.systemui.privacy;

import android.provider.DeviceConfig;
import com.android.systemui.privacy.PrivacyConfig;
import java.lang.ref.WeakReference;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: PrivacyConfig.kt */
public final class PrivacyConfig$devicePropertiesChangedListener$1 implements DeviceConfig.OnPropertiesChangedListener {
    public final /* synthetic */ PrivacyConfig this$0;

    public PrivacyConfig$devicePropertiesChangedListener$1(PrivacyConfig privacyConfig) {
        this.this$0 = privacyConfig;
    }

    public final void onPropertiesChanged(DeviceConfig.Properties properties) {
        if (Intrinsics.areEqual((Object) "privacy", (Object) properties.getNamespace())) {
            if (properties.getKeyset().contains("camera_mic_icons_enabled")) {
                this.this$0.micCameraAvailable = false;
                PrivacyConfig privacyConfig = this.this$0;
                for (WeakReference weakReference : this.this$0.callbacks) {
                    PrivacyConfig.Callback callback = (PrivacyConfig.Callback) weakReference.get();
                    if (callback != null) {
                        callback.onFlagMicCameraChanged(privacyConfig.getMicCameraAvailable());
                    }
                }
            }
            if (properties.getKeyset().contains("location_indicators_enabled")) {
                this.this$0.locationAvailable = properties.getBoolean("location_indicators_enabled", false);
                PrivacyConfig privacyConfig2 = this.this$0;
                for (WeakReference weakReference2 : this.this$0.callbacks) {
                    PrivacyConfig.Callback callback2 = (PrivacyConfig.Callback) weakReference2.get();
                    if (callback2 != null) {
                        callback2.onFlagLocationChanged(privacyConfig2.getLocationAvailable());
                    }
                }
            }
            if (properties.getKeyset().contains("media_projection_indicators_enabled")) {
                this.this$0.mediaProjectionAvailable = properties.getBoolean("media_projection_indicators_enabled", true);
                PrivacyConfig privacyConfig3 = this.this$0;
                for (WeakReference weakReference3 : this.this$0.callbacks) {
                    PrivacyConfig.Callback callback3 = (PrivacyConfig.Callback) weakReference3.get();
                    if (callback3 != null) {
                        callback3.onFlagMediaProjectionChanged(privacyConfig3.getMediaProjectionAvailable());
                    }
                }
            }
        }
    }
}
