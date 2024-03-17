package kotlinx.coroutines;

import org.jetbrains.annotations.NotNull;

/* compiled from: EventLoop.kt */
public final class EventLoopKt {
    @NotNull
    public static final EventLoop createEventLoop() {
        return new BlockingEventLoop(Thread.currentThread());
    }
}
