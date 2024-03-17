package com.android.systemui.media.muteawait;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: MediaMuteAwaitLogger.kt */
public final class MediaMuteAwaitLogger$logMutedDeviceAdded$2 extends Lambda implements Function1<LogMessage, String> {
    public static final MediaMuteAwaitLogger$logMutedDeviceAdded$2 INSTANCE = new MediaMuteAwaitLogger$logMutedDeviceAdded$2();

    public MediaMuteAwaitLogger$logMutedDeviceAdded$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "Muted device added: address=" + logMessage.getStr1() + " name=" + logMessage.getStr2() + " hasMediaUsage=" + logMessage.getBool1();
    }
}
