package com.android.systemui.media.muteawait;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.AudioDeviceAttributes;
import android.media.AudioManager;
import com.android.settingslib.media.DeviceIconUtil;
import com.android.settingslib.media.LocalMediaManager;
import java.util.concurrent.Executor;
import kotlin.collections.ArraysKt___ArraysKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: MediaMuteAwaitConnectionManager.kt */
public final class MediaMuteAwaitConnectionManager {
    @NotNull
    public final AudioManager audioManager;
    @NotNull
    public final Context context;
    @Nullable
    public AudioDeviceAttributes currentMutedDevice;
    @NotNull
    public final DeviceIconUtil deviceIconUtil;
    @NotNull
    public final LocalMediaManager localMediaManager;
    @NotNull
    public final MediaMuteAwaitLogger logger;
    @NotNull
    public final Executor mainExecutor;
    @NotNull
    public final AudioManager.MuteAwaitConnectionCallback muteAwaitConnectionChangeListener;

    public MediaMuteAwaitConnectionManager(@NotNull Executor executor, @NotNull LocalMediaManager localMediaManager2, @NotNull Context context2, @NotNull DeviceIconUtil deviceIconUtil2, @NotNull MediaMuteAwaitLogger mediaMuteAwaitLogger) {
        this.mainExecutor = executor;
        this.localMediaManager = localMediaManager2;
        this.context = context2;
        this.deviceIconUtil = deviceIconUtil2;
        this.logger = mediaMuteAwaitLogger;
        Object systemService = context2.getSystemService("audio");
        if (systemService != null) {
            this.audioManager = (AudioManager) systemService;
            this.muteAwaitConnectionChangeListener = new MediaMuteAwaitConnectionManager$muteAwaitConnectionChangeListener$1(this);
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type android.media.AudioManager");
    }

    @Nullable
    public final AudioDeviceAttributes getCurrentMutedDevice() {
        return this.currentMutedDevice;
    }

    public final void setCurrentMutedDevice(@Nullable AudioDeviceAttributes audioDeviceAttributes) {
        this.currentMutedDevice = audioDeviceAttributes;
    }

    public final void startListening() {
        this.audioManager.registerMuteAwaitConnectionCallback(this.mainExecutor, this.muteAwaitConnectionChangeListener);
        AudioDeviceAttributes mutingExpectedDevice = this.audioManager.getMutingExpectedDevice();
        if (mutingExpectedDevice != null) {
            this.currentMutedDevice = mutingExpectedDevice;
            this.localMediaManager.dispatchAboutToConnectDeviceAdded(mutingExpectedDevice.getAddress(), mutingExpectedDevice.getName(), getIcon(mutingExpectedDevice));
        }
    }

    public final void stopListening() {
        this.audioManager.unregisterMuteAwaitConnectionCallback(this.muteAwaitConnectionChangeListener);
    }

    public final Drawable getIcon(AudioDeviceAttributes audioDeviceAttributes) {
        return this.deviceIconUtil.getIconFromAudioDeviceType(audioDeviceAttributes.getType(), this.context);
    }

    public final boolean hasMedia(int[] iArr) {
        return ArraysKt___ArraysKt.contains(iArr, 1);
    }
}
