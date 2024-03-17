package com.android.systemui.media.taptotransfer.common;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: MediaTttLogger.kt */
public final class MediaTttLogger$logChipRemoval$2 extends Lambda implements Function1<LogMessage, String> {
    public static final MediaTttLogger$logChipRemoval$2 INSTANCE = new MediaTttLogger$logChipRemoval$2();

    public MediaTttLogger$logChipRemoval$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return Intrinsics.stringPlus("Chip removed due to ", logMessage.getStr1());
    }
}
