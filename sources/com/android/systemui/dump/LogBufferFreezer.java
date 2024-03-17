package com.android.systemui.dump;

import android.content.IntentFilter;
import android.os.UserHandle;
import android.util.Log;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.util.concurrency.DelayableExecutor;
import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: LogBufferFreezer.kt */
public final class LogBufferFreezer {
    @NotNull
    public final DumpManager dumpManager;
    @NotNull
    public final DelayableExecutor executor;
    public final long freezeDuration;
    @Nullable
    public Runnable pendingToken;

    public LogBufferFreezer(@NotNull DumpManager dumpManager2, @NotNull DelayableExecutor delayableExecutor, long j) {
        this.dumpManager = dumpManager2;
        this.executor = delayableExecutor;
        this.freezeDuration = j;
    }

    public LogBufferFreezer(@NotNull DumpManager dumpManager2, @NotNull DelayableExecutor delayableExecutor) {
        this(dumpManager2, delayableExecutor, TimeUnit.MINUTES.toMillis(5));
    }

    public final void attach(@NotNull BroadcastDispatcher broadcastDispatcher) {
        BroadcastDispatcher.registerReceiver$default(broadcastDispatcher, new LogBufferFreezer$attach$1(this), new IntentFilter("com.android.internal.intent.action.BUGREPORT_STARTED"), this.executor, UserHandle.ALL, 0, (String) null, 48, (Object) null);
    }

    public final void onBugreportStarted() {
        Runnable runnable = this.pendingToken;
        if (runnable != null) {
            runnable.run();
        }
        Log.i("LogBufferFreezer", "Freezing log buffers");
        this.dumpManager.freezeBuffers();
        this.pendingToken = this.executor.executeDelayed(new LogBufferFreezer$onBugreportStarted$1(this), this.freezeDuration);
    }
}
