package kotlinx.coroutines;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: CommonPool.kt */
public final class CommonPool$createPlainPool$1 implements ThreadFactory {
    public final /* synthetic */ AtomicInteger $threadId;

    public CommonPool$createPlainPool$1(AtomicInteger atomicInteger) {
        this.$threadId = atomicInteger;
    }

    public final Thread newThread(Runnable runnable) {
        Thread thread = new Thread(runnable, Intrinsics.stringPlus("CommonPool-worker-", Integer.valueOf(this.$threadId.incrementAndGet())));
        thread.setDaemon(true);
        return thread;
    }
}
