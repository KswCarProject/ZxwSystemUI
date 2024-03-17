package com.android.systemui.broadcast;

import android.content.Context;
import android.content.Intent;
import android.os.UserHandle;
import com.android.systemui.util.wakelock.WakeLock;
import java.util.concurrent.Executor;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: BroadcastSender.kt */
public final class BroadcastSender {
    @NotNull
    public final String WAKE_LOCK_SEND_REASON = "sendInBackground";
    @NotNull
    public final String WAKE_LOCK_TAG = "SysUI:BroadcastSender";
    @NotNull
    public final Executor bgExecutor;
    @NotNull
    public final Context context;
    @NotNull
    public final WakeLock.Builder wakeLockBuilder;

    public BroadcastSender(@NotNull Context context2, @NotNull WakeLock.Builder builder, @NotNull Executor executor) {
        this.context = context2;
        this.wakeLockBuilder = builder;
        this.bgExecutor = executor;
    }

    public final void sendBroadcast(@NotNull Intent intent) {
        sendInBackground(new BroadcastSender$sendBroadcast$1(this, intent));
    }

    public final void sendBroadcast(@NotNull Intent intent, @Nullable String str) {
        sendInBackground(new BroadcastSender$sendBroadcast$2(this, intent, str));
    }

    public final void sendBroadcastAsUser(@NotNull Intent intent, @NotNull UserHandle userHandle) {
        sendInBackground(new BroadcastSender$sendBroadcastAsUser$1(this, intent, userHandle));
    }

    public final void closeSystemDialogs() {
        sendInBackground(new BroadcastSender$closeSystemDialogs$1(this));
    }

    public final void sendInBackground(Function0<Unit> function0) {
        WakeLock build = this.wakeLockBuilder.setTag(this.WAKE_LOCK_TAG).setMaxTimeout(5000).build();
        build.acquire(this.WAKE_LOCK_SEND_REASON);
        this.bgExecutor.execute(new BroadcastSender$sendInBackground$1(function0, build, this));
    }
}
