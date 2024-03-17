package kotlinx.coroutines;

import org.jetbrains.annotations.NotNull;

/* compiled from: EventLoop.common.kt */
public final class ThreadLocalEventLoop {
    @NotNull
    public static final ThreadLocalEventLoop INSTANCE = new ThreadLocalEventLoop();
    @NotNull
    public static final ThreadLocal<EventLoop> ref = new ThreadLocal<>();

    @NotNull
    public final EventLoop getEventLoop$external__kotlinx_coroutines__android_common__kotlinx_coroutines() {
        ThreadLocal<EventLoop> threadLocal = ref;
        EventLoop eventLoop = threadLocal.get();
        if (eventLoop != null) {
            return eventLoop;
        }
        EventLoop createEventLoop = EventLoopKt.createEventLoop();
        threadLocal.set(createEventLoop);
        return createEventLoop;
    }

    public final void resetEventLoop$external__kotlinx_coroutines__android_common__kotlinx_coroutines() {
        ref.set((Object) null);
    }

    public final void setEventLoop$external__kotlinx_coroutines__android_common__kotlinx_coroutines(@NotNull EventLoop eventLoop) {
        ref.set(eventLoop);
    }
}
