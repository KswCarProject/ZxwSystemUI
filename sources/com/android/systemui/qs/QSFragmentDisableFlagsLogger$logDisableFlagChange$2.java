package com.android.systemui.qs;

import com.android.systemui.log.LogMessage;
import com.android.systemui.statusbar.DisableFlagsLogger;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: QSFragmentDisableFlagsLogger.kt */
public final class QSFragmentDisableFlagsLogger$logDisableFlagChange$2 extends Lambda implements Function1<LogMessage, String> {
    public final /* synthetic */ QSFragmentDisableFlagsLogger this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public QSFragmentDisableFlagsLogger$logDisableFlagChange$2(QSFragmentDisableFlagsLogger qSFragmentDisableFlagsLogger) {
        super(1);
        this.this$0 = qSFragmentDisableFlagsLogger;
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return this.this$0.disableFlagsLogger.getDisableFlagsString((DisableFlagsLogger.DisableState) null, new DisableFlagsLogger.DisableState(logMessage.getInt1(), logMessage.getInt2()), new DisableFlagsLogger.DisableState((int) logMessage.getLong1(), (int) logMessage.getLong2()));
    }
}
