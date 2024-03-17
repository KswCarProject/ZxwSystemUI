package kotlinx.coroutines;

import java.util.concurrent.locks.LockSupport;
import kotlinx.coroutines.EventLoopImplBase;
import org.jetbrains.annotations.NotNull;

/* compiled from: EventLoop.kt */
public abstract class EventLoopImplPlatform extends EventLoop {
    @NotNull
    public abstract Thread getThread();

    public final void unpark() {
        Thread thread = getThread();
        if (Thread.currentThread() != thread) {
            AbstractTimeSourceKt.getTimeSource();
            LockSupport.unpark(thread);
        }
    }

    public final void reschedule(long j, @NotNull EventLoopImplBase.DelayedTask delayedTask) {
        if (DebugKt.getASSERTIONS_ENABLED()) {
            if (!(this != DefaultExecutor.INSTANCE)) {
                throw new AssertionError();
            }
        }
        DefaultExecutor.INSTANCE.schedule(j, delayedTask);
    }
}
