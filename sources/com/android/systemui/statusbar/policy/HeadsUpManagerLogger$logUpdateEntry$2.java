package com.android.systemui.statusbar.policy;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: HeadsUpManagerLogger.kt */
public final class HeadsUpManagerLogger$logUpdateEntry$2 extends Lambda implements Function1<LogMessage, String> {
    public final /* synthetic */ String $key;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public HeadsUpManagerLogger$logUpdateEntry$2(String str) {
        super(1);
        this.$key = str;
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "update entry " + this.$key + " updatePostTime: " + logMessage.getBool1();
    }
}
