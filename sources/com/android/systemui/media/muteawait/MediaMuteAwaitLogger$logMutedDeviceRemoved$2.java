package com.android.systemui.media.muteawait;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: MediaMuteAwaitLogger.kt */
public final class MediaMuteAwaitLogger$logMutedDeviceRemoved$2 extends Lambda implements Function1<LogMessage, String> {
    public static final MediaMuteAwaitLogger$logMutedDeviceRemoved$2 INSTANCE = new MediaMuteAwaitLogger$logMutedDeviceRemoved$2();

    public MediaMuteAwaitLogger$logMutedDeviceRemoved$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "Muted device removed: address=" + logMessage.getStr1() + " name=" + logMessage.getStr2() + " hasMediaUsage=" + logMessage.getBool1() + " isMostRecentDevice=" + logMessage.getBool2();
    }
}
