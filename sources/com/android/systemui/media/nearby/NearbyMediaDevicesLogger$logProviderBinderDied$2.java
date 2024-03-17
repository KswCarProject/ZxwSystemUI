package com.android.systemui.media.nearby;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: NearbyMediaDevicesLogger.kt */
public final class NearbyMediaDevicesLogger$logProviderBinderDied$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NearbyMediaDevicesLogger$logProviderBinderDied$2 INSTANCE = new NearbyMediaDevicesLogger$logProviderBinderDied$2();

    public NearbyMediaDevicesLogger$logProviderBinderDied$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return Intrinsics.stringPlus("Provider binder died; total providers = ", Integer.valueOf(logMessage.getInt1()));
    }
}
