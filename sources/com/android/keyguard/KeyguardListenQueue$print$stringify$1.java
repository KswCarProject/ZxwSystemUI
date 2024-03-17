package com.android.keyguard;

import java.text.DateFormat;
import java.util.Date;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: KeyguardListenQueue.kt */
public final class KeyguardListenQueue$print$stringify$1 extends Lambda implements Function1<KeyguardListenModel, String> {
    public final /* synthetic */ DateFormat $dateFormat;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public KeyguardListenQueue$print$stringify$1(DateFormat dateFormat) {
        super(1);
        this.$dateFormat = dateFormat;
    }

    @NotNull
    public final String invoke(@NotNull KeyguardListenModel keyguardListenModel) {
        return "    " + this.$dateFormat.format(new Date(keyguardListenModel.getTimeMillis())) + ' ' + keyguardListenModel;
    }
}
