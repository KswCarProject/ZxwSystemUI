package com.android.systemui.broadcast;

import android.content.Intent;
import android.os.UserHandle;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

/* compiled from: BroadcastSender.kt */
public final class BroadcastSender$sendBroadcastAsUser$1 extends Lambda implements Function0<Unit> {
    public final /* synthetic */ Intent $intent;
    public final /* synthetic */ UserHandle $userHandle;
    public final /* synthetic */ BroadcastSender this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public BroadcastSender$sendBroadcastAsUser$1(BroadcastSender broadcastSender, Intent intent, UserHandle userHandle) {
        super(0);
        this.this$0 = broadcastSender;
        this.$intent = intent;
        this.$userHandle = userHandle;
    }

    public final void invoke() {
        this.this$0.context.sendBroadcastAsUser(this.$intent, this.$userHandle);
    }
}
