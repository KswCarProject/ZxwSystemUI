package com.android.systemui.broadcast;

import com.android.systemui.util.wakelock.WakeLock;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;

/* compiled from: BroadcastSender.kt */
public final class BroadcastSender$sendInBackground$1 implements Runnable {
    public final /* synthetic */ WakeLock $broadcastWakelock;
    public final /* synthetic */ Function0<Unit> $callable;
    public final /* synthetic */ BroadcastSender this$0;

    public BroadcastSender$sendInBackground$1(Function0<Unit> function0, WakeLock wakeLock, BroadcastSender broadcastSender) {
        this.$callable = function0;
        this.$broadcastWakelock = wakeLock;
        this.this$0 = broadcastSender;
    }

    public final void run() {
        try {
            this.$callable.invoke();
        } finally {
            this.$broadcastWakelock.release(this.this$0.WAKE_LOCK_SEND_REASON);
        }
    }
}
