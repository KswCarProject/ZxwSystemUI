package kotlinx.coroutines.internal;

import org.jetbrains.annotations.Nullable;

/* compiled from: ThreadSafeHeap.kt */
public interface ThreadSafeHeapNode {
    @Nullable
    ThreadSafeHeap<?> getHeap();

    int getIndex();

    void setHeap(@Nullable ThreadSafeHeap<?> threadSafeHeap);

    void setIndex(int i);
}
