package com.android.systemui.privacy.logging;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: PrivacyLogger.kt */
public final class PrivacyLogger$logUpdatedItemFromMediaProjection$2 extends Lambda implements Function1<LogMessage, String> {
    public static final PrivacyLogger$logUpdatedItemFromMediaProjection$2 INSTANCE = new PrivacyLogger$logUpdatedItemFromMediaProjection$2();

    public PrivacyLogger$logUpdatedItemFromMediaProjection$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "MediaProjection: " + logMessage.getStr1() + '(' + logMessage.getInt1() + "), active=" + logMessage.getBool1();
    }
}
