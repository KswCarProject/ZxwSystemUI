package com.android.systemui.media.nearby;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: NearbyMediaDevicesLogger.kt */
public final class NearbyMediaDevicesLogger$logProviderUnregistered$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NearbyMediaDevicesLogger$logProviderUnregistered$2 INSTANCE = new NearbyMediaDevicesLogger$logProviderUnregistered$2();

    public NearbyMediaDevicesLogger$logProviderUnregistered$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return Intrinsics.stringPlus("Provider unregistered; total providers = ", Integer.valueOf(logMessage.getInt1()));
    }
}
