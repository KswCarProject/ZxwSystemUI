package com.android.systemui.media;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: MediaTimeoutLogger.kt */
public final class MediaTimeoutLogger$logStateCallback$2 extends Lambda implements Function1<LogMessage, String> {
    public final /* synthetic */ String $key;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public MediaTimeoutLogger$logStateCallback$2(String str) {
        super(1);
        this.$key = str;
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return Intrinsics.stringPlus("dispatching state update for ", this.$key);
    }
}
