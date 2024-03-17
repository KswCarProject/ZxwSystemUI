package kotlinx.coroutines;

import org.jetbrains.annotations.NotNull;

/* compiled from: EventLoop.kt */
public final class BlockingEventLoop extends EventLoopImplBase {
    @NotNull
    public final Thread thread;

    @NotNull
    public Thread getThread() {
        return this.thread;
    }

    public BlockingEventLoop(@NotNull Thread thread2) {
        this.thread = thread2;
    }
}
