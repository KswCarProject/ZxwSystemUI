package com.android.systemui.broadcast;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.UserHandle;
import java.util.concurrent.Executor;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: BroadcastDispatcher.kt */
public final class ReceiverData {
    @NotNull
    public final Executor executor;
    @NotNull
    public final IntentFilter filter;
    @Nullable
    public final String permission;
    @NotNull
    public final BroadcastReceiver receiver;
    @NotNull
    public final UserHandle user;

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ReceiverData)) {
            return false;
        }
        ReceiverData receiverData = (ReceiverData) obj;
        return Intrinsics.areEqual((Object) this.receiver, (Object) receiverData.receiver) && Intrinsics.areEqual((Object) this.filter, (Object) receiverData.filter) && Intrinsics.areEqual((Object) this.executor, (Object) receiverData.executor) && Intrinsics.areEqual((Object) this.user, (Object) receiverData.user) && Intrinsics.areEqual((Object) this.permission, (Object) receiverData.permission);
    }

    public int hashCode() {
        int hashCode = ((((((this.receiver.hashCode() * 31) + this.filter.hashCode()) * 31) + this.executor.hashCode()) * 31) + this.user.hashCode()) * 31;
        String str = this.permission;
        return hashCode + (str == null ? 0 : str.hashCode());
    }

    @NotNull
    public String toString() {
        return "ReceiverData(receiver=" + this.receiver + ", filter=" + this.filter + ", executor=" + this.executor + ", user=" + this.user + ", permission=" + this.permission + ')';
    }

    public ReceiverData(@NotNull BroadcastReceiver broadcastReceiver, @NotNull IntentFilter intentFilter, @NotNull Executor executor2, @NotNull UserHandle userHandle, @Nullable String str) {
        this.receiver = broadcastReceiver;
        this.filter = intentFilter;
        this.executor = executor2;
        this.user = userHandle;
        this.permission = str;
    }

    @NotNull
    public final BroadcastReceiver getReceiver() {
        return this.receiver;
    }

    @NotNull
    public final IntentFilter getFilter() {
        return this.filter;
    }

    @NotNull
    public final Executor getExecutor() {
        return this.executor;
    }

    @NotNull
    public final UserHandle getUser() {
        return this.user;
    }

    @Nullable
    public final String getPermission() {
        return this.permission;
    }
}
