package com.android.systemui.media.muteawait;

import android.media.AudioDeviceAttributes;
import android.media.AudioManager;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: MediaMuteAwaitConnectionManager.kt */
public final class MediaMuteAwaitConnectionManager$muteAwaitConnectionChangeListener$1 extends AudioManager.MuteAwaitConnectionCallback {
    public final /* synthetic */ MediaMuteAwaitConnectionManager this$0;

    public MediaMuteAwaitConnectionManager$muteAwaitConnectionChangeListener$1(MediaMuteAwaitConnectionManager mediaMuteAwaitConnectionManager) {
        this.this$0 = mediaMuteAwaitConnectionManager;
    }

    public void onMutedUntilConnection(@NotNull AudioDeviceAttributes audioDeviceAttributes, @NotNull int[] iArr) {
        this.this$0.logger.logMutedDeviceAdded(audioDeviceAttributes.getAddress(), audioDeviceAttributes.getName(), this.this$0.hasMedia(iArr));
        if (this.this$0.hasMedia(iArr)) {
            this.this$0.setCurrentMutedDevice(audioDeviceAttributes);
            this.this$0.localMediaManager.dispatchAboutToConnectDeviceAdded(audioDeviceAttributes.getAddress(), audioDeviceAttributes.getName(), this.this$0.getIcon(audioDeviceAttributes));
        }
    }

    public void onUnmutedEvent(int i, @NotNull AudioDeviceAttributes audioDeviceAttributes, @NotNull int[] iArr) {
        boolean areEqual = Intrinsics.areEqual((Object) this.this$0.getCurrentMutedDevice(), (Object) audioDeviceAttributes);
        this.this$0.logger.logMutedDeviceRemoved(audioDeviceAttributes.getAddress(), audioDeviceAttributes.getName(), this.this$0.hasMedia(iArr), areEqual);
        if (areEqual && this.this$0.hasMedia(iArr)) {
            this.this$0.setCurrentMutedDevice((AudioDeviceAttributes) null);
            this.this$0.localMediaManager.dispatchAboutToConnectDeviceRemoved();
        }
    }
}
