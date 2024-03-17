package com.android.systemui.broadcast;

import android.content.BroadcastReceiver;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import org.jetbrains.annotations.NotNull;

/* compiled from: BroadcastDispatcher.kt */
public final class BroadcastDispatcher$handler$1 extends Handler {
    public final /* synthetic */ BroadcastDispatcher this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public BroadcastDispatcher$handler$1(BroadcastDispatcher broadcastDispatcher, Looper looper) {
        super(looper);
        this.this$0 = broadcastDispatcher;
    }

    public void handleMessage(@NotNull Message message) {
        int i;
        int i2 = message.what;
        if (i2 == 0) {
            Object obj = message.obj;
            if (obj != null) {
                ReceiverData receiverData = (ReceiverData) obj;
                int i3 = message.arg1;
                if (receiverData.getUser().getIdentifier() == -2) {
                    i = this.this$0.userTracker.getUserId();
                } else {
                    i = receiverData.getUser().getIdentifier();
                }
                if (i >= -1) {
                    UserBroadcastDispatcher userBroadcastDispatcher = (UserBroadcastDispatcher) this.this$0.receiversByUser.get(i, this.this$0.createUBRForUser(i));
                    this.this$0.receiversByUser.put(i, userBroadcastDispatcher);
                    userBroadcastDispatcher.registerReceiver(receiverData, i3);
                    return;
                }
                throw new IllegalStateException("Attempting to register receiver for invalid user {" + i + '}');
            }
            throw new NullPointerException("null cannot be cast to non-null type com.android.systemui.broadcast.ReceiverData");
        } else if (i2 == 1) {
            int i4 = 0;
            int size = this.this$0.receiversByUser.size();
            while (i4 < size) {
                int i5 = i4 + 1;
                UserBroadcastDispatcher userBroadcastDispatcher2 = (UserBroadcastDispatcher) this.this$0.receiversByUser.valueAt(i4);
                Object obj2 = message.obj;
                if (obj2 != null) {
                    userBroadcastDispatcher2.unregisterReceiver((BroadcastReceiver) obj2);
                    i4 = i5;
                } else {
                    throw new NullPointerException("null cannot be cast to non-null type android.content.BroadcastReceiver");
                }
            }
            PendingRemovalStore access$getRemovalPendingStore$p = this.this$0.removalPendingStore;
            Object obj3 = message.obj;
            if (obj3 != null) {
                access$getRemovalPendingStore$p.clearPendingRemoval((BroadcastReceiver) obj3, -1);
                return;
            }
            throw new NullPointerException("null cannot be cast to non-null type android.content.BroadcastReceiver");
        } else if (i2 != 2) {
            super.handleMessage(message);
        } else {
            int i6 = message.arg1;
            if (i6 == -2) {
                i6 = this.this$0.userTracker.getUserId();
            }
            UserBroadcastDispatcher userBroadcastDispatcher3 = (UserBroadcastDispatcher) this.this$0.receiversByUser.get(i6);
            if (userBroadcastDispatcher3 != null) {
                Object obj4 = message.obj;
                if (obj4 != null) {
                    userBroadcastDispatcher3.unregisterReceiver((BroadcastReceiver) obj4);
                } else {
                    throw new NullPointerException("null cannot be cast to non-null type android.content.BroadcastReceiver");
                }
            }
            PendingRemovalStore access$getRemovalPendingStore$p2 = this.this$0.removalPendingStore;
            Object obj5 = message.obj;
            if (obj5 != null) {
                access$getRemovalPendingStore$p2.clearPendingRemoval((BroadcastReceiver) obj5, i6);
                return;
            }
            throw new NullPointerException("null cannot be cast to non-null type android.content.BroadcastReceiver");
        }
    }
}
