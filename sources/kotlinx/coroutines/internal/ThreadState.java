package kotlinx.coroutines.internal;

import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.ThreadContextElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: ThreadContext.kt */
public final class ThreadState {
    @NotNull
    public final CoroutineContext context;
    @NotNull
    public final ThreadContextElement<Object>[] elements;
    public int i;
    @NotNull
    public final Object[] values;

    public ThreadState(@NotNull CoroutineContext coroutineContext, int i2) {
        this.context = coroutineContext;
        this.values = new Object[i2];
        this.elements = new ThreadContextElement[i2];
    }

    public final void append(@NotNull ThreadContextElement<?> threadContextElement, @Nullable Object obj) {
        Object[] objArr = this.values;
        int i2 = this.i;
        objArr[i2] = obj;
        ThreadContextElement<Object>[] threadContextElementArr = this.elements;
        this.i = i2 + 1;
        threadContextElementArr[i2] = threadContextElement;
    }

    public final void restore(@NotNull CoroutineContext coroutineContext) {
        int length = this.elements.length - 1;
        if (length >= 0) {
            while (true) {
                int i2 = length - 1;
                ThreadContextElement<Object> threadContextElement = this.elements[length];
                Intrinsics.checkNotNull(threadContextElement);
                threadContextElement.restoreThreadContext(coroutineContext, this.values[length]);
                if (i2 >= 0) {
                    length = i2;
                } else {
                    return;
                }
            }
        }
    }
}
