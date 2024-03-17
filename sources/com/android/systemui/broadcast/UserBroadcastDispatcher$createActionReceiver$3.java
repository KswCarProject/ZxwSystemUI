package com.android.systemui.broadcast;

import android.content.BroadcastReceiver;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.FunctionReferenceImpl;
import org.jetbrains.annotations.NotNull;

/* compiled from: UserBroadcastDispatcher.kt */
public /* synthetic */ class UserBroadcastDispatcher$createActionReceiver$3 extends FunctionReferenceImpl implements Function2<BroadcastReceiver, Integer, Boolean> {
    public UserBroadcastDispatcher$createActionReceiver$3(Object obj) {
        super(2, obj, PendingRemovalStore.class, "isPendingRemoval", "isPendingRemoval(Landroid/content/BroadcastReceiver;I)Z", 0);
    }

    @NotNull
    public final Boolean invoke(@NotNull BroadcastReceiver broadcastReceiver, int i) {
        return Boolean.valueOf(((PendingRemovalStore) this.receiver).isPendingRemoval(broadcastReceiver, i));
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj, Object obj2) {
        return invoke((BroadcastReceiver) obj, ((Number) obj2).intValue());
    }
}
