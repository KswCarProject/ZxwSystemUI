package com.android.systemui.broadcast;

import android.content.Intent;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

/* compiled from: BroadcastSender.kt */
public final class BroadcastSender$closeSystemDialogs$1 extends Lambda implements Function0<Unit> {
    public final /* synthetic */ BroadcastSender this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public BroadcastSender$closeSystemDialogs$1(BroadcastSender broadcastSender) {
        super(0);
        this.this$0 = broadcastSender;
    }

    public final void invoke() {
        this.this$0.context.sendBroadcast(new Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
    }
}
