package com.android.systemui.broadcast;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.UserHandle;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: UserBroadcastDispatcher.kt */
public final class UserBroadcastDispatcher$createActionReceiver$1 extends Lambda implements Function2<BroadcastReceiver, IntentFilter, Unit> {
    public final /* synthetic */ int $flags;
    public final /* synthetic */ String $permission;
    public final /* synthetic */ UserBroadcastDispatcher this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public UserBroadcastDispatcher$createActionReceiver$1(UserBroadcastDispatcher userBroadcastDispatcher, String str, int i) {
        super(2);
        this.this$0 = userBroadcastDispatcher;
        this.$permission = str;
        this.$flags = i;
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj, Object obj2) {
        invoke((BroadcastReceiver) obj, (IntentFilter) obj2);
        return Unit.INSTANCE;
    }

    public final void invoke(@NotNull BroadcastReceiver broadcastReceiver, @NotNull IntentFilter intentFilter) {
        this.this$0.context.registerReceiverAsUser(broadcastReceiver, UserHandle.of(this.this$0.userId), intentFilter, this.$permission, this.this$0.bgHandler, this.$flags);
        this.this$0.logger.logContextReceiverRegistered(this.this$0.userId, this.$flags, intentFilter);
    }
}
