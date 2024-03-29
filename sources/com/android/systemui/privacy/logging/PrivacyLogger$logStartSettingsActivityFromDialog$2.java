package com.android.systemui.privacy.logging;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: PrivacyLogger.kt */
public final class PrivacyLogger$logStartSettingsActivityFromDialog$2 extends Lambda implements Function1<LogMessage, String> {
    public static final PrivacyLogger$logStartSettingsActivityFromDialog$2 INSTANCE = new PrivacyLogger$logStartSettingsActivityFromDialog$2();

    public PrivacyLogger$logStartSettingsActivityFromDialog$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "Start settings activity from dialog for packageName=" + logMessage.getStr1() + ", userId=" + logMessage.getInt1() + ' ';
    }
}
