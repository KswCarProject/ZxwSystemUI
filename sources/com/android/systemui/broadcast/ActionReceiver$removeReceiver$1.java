package com.android.systemui.broadcast;

import android.content.BroadcastReceiver;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: ActionReceiver.kt */
public final class ActionReceiver$removeReceiver$1 extends Lambda implements Function1<ReceiverData, Boolean> {
    public final /* synthetic */ BroadcastReceiver $receiver;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public ActionReceiver$removeReceiver$1(BroadcastReceiver broadcastReceiver) {
        super(1);
        this.$receiver = broadcastReceiver;
    }

    @NotNull
    public final Boolean invoke(ReceiverData receiverData) {
        return Boolean.valueOf(Intrinsics.areEqual((Object) receiverData.getReceiver(), (Object) this.$receiver));
    }
}
