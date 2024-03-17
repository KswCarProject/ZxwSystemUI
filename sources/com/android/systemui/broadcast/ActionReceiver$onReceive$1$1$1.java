package com.android.systemui.broadcast;

import android.content.Context;
import android.content.Intent;

/* compiled from: ActionReceiver.kt */
public final class ActionReceiver$onReceive$1$1$1 implements Runnable {
    public final /* synthetic */ Context $context;
    public final /* synthetic */ int $id;
    public final /* synthetic */ Intent $intent;
    public final /* synthetic */ ReceiverData $it;
    public final /* synthetic */ ActionReceiver this$0;

    public ActionReceiver$onReceive$1$1$1(ReceiverData receiverData, ActionReceiver actionReceiver, Context context, Intent intent, int i) {
        this.$it = receiverData;
        this.this$0 = actionReceiver;
        this.$context = context;
        this.$intent = intent;
        this.$id = i;
    }

    public final void run() {
        this.$it.getReceiver().setPendingResult(this.this$0.getPendingResult());
        this.$it.getReceiver().onReceive(this.$context, this.$intent);
        this.this$0.logger.logBroadcastDispatched(this.$id, this.this$0.action, this.$it.getReceiver());
    }
}
