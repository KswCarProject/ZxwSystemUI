package kotlinx.coroutines.scheduling;

import java.util.concurrent.atomic.AtomicReferenceArray;
import kotlinx.atomicfu.AtomicFU;
import kotlinx.atomicfu.AtomicInt;
import kotlinx.atomicfu.AtomicRef;
import kotlinx.coroutines.DebugKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: WorkQueue.kt */
public final class WorkQueue {
    @NotNull
    public final AtomicInt blockingTasksInBuffer = AtomicFU.atomic(0);
    @NotNull
    public final AtomicReferenceArray<Task> buffer = new AtomicReferenceArray<>(128);
    @NotNull
    public final AtomicInt consumerIndex = AtomicFU.atomic(0);
    @NotNull
    public final AtomicRef<Task> lastScheduledTask = AtomicFU.atomic(null);
    @NotNull
    public final AtomicInt producerIndex = AtomicFU.atomic(0);

    public final int getBufferSize$external__kotlinx_coroutines__android_common__kotlinx_coroutines() {
        return this.producerIndex.getValue() - this.consumerIndex.getValue();
    }

    public final int getSize$external__kotlinx_coroutines__android_common__kotlinx_coroutines() {
        Task value = this.lastScheduledTask.getValue();
        int bufferSize$external__kotlinx_coroutines__android_common__kotlinx_coroutines = getBufferSize$external__kotlinx_coroutines__android_common__kotlinx_coroutines();
        return value != null ? bufferSize$external__kotlinx_coroutines__android_common__kotlinx_coroutines + 1 : bufferSize$external__kotlinx_coroutines__android_common__kotlinx_coroutines;
    }

    @Nullable
    public final Task poll() {
        Task andSet = this.lastScheduledTask.getAndSet(null);
        return andSet == null ? pollBuffer() : andSet;
    }

    public static /* synthetic */ Task add$default(WorkQueue workQueue, Task task, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return workQueue.add(task, z);
    }

    @Nullable
    public final Task add(@NotNull Task task, boolean z) {
        if (z) {
            return addLast(task);
        }
        Task andSet = this.lastScheduledTask.getAndSet(task);
        if (andSet == null) {
            return null;
        }
        return addLast(andSet);
    }

    public final Task addLast(Task task) {
        boolean z = true;
        if (task.taskContext.getTaskMode() != 1) {
            z = false;
        }
        if (z) {
            this.blockingTasksInBuffer.incrementAndGet();
        }
        if (getBufferSize$external__kotlinx_coroutines__android_common__kotlinx_coroutines() == 127) {
            return task;
        }
        int value = this.producerIndex.getValue() & 127;
        while (this.buffer.get(value) != null) {
            Thread.yield();
        }
        this.buffer.lazySet(value, task);
        this.producerIndex.incrementAndGet();
        return null;
    }

    public final void decrementIfBlocking(Task task) {
        if (task != null) {
            boolean z = false;
            if (task.taskContext.getTaskMode() == 1) {
                int decrementAndGet = this.blockingTasksInBuffer.decrementAndGet();
                if (DebugKt.getASSERTIONS_ENABLED()) {
                    if (decrementAndGet >= 0) {
                        z = true;
                    }
                    if (!z) {
                        throw new AssertionError();
                    }
                }
            }
        }
    }

    public final long tryStealFrom(@NotNull WorkQueue workQueue) {
        boolean z = true;
        if (DebugKt.getASSERTIONS_ENABLED()) {
            if (!(getBufferSize$external__kotlinx_coroutines__android_common__kotlinx_coroutines() == 0)) {
                throw new AssertionError();
            }
        }
        Task pollBuffer = workQueue.pollBuffer();
        if (pollBuffer == null) {
            return tryStealLastScheduled(workQueue, false);
        }
        Task add$default = add$default(this, pollBuffer, false, 2, (Object) null);
        if (!DebugKt.getASSERTIONS_ENABLED()) {
            return -1;
        }
        if (add$default != null) {
            z = false;
        }
        if (z) {
            return -1;
        }
        throw new AssertionError();
    }

    public final long tryStealBlockingFrom(@NotNull WorkQueue workQueue) {
        if (DebugKt.getASSERTIONS_ENABLED()) {
            if (!(getBufferSize$external__kotlinx_coroutines__android_common__kotlinx_coroutines() == 0)) {
                throw new AssertionError();
            }
        }
        int value = workQueue.producerIndex.getValue();
        AtomicReferenceArray<Task> atomicReferenceArray = workQueue.buffer;
        for (int value2 = workQueue.consumerIndex.getValue(); value2 != value; value2++) {
            int i = value2 & 127;
            if (workQueue.blockingTasksInBuffer.getValue() == 0) {
                break;
            }
            Task task = atomicReferenceArray.get(i);
            if (task != null) {
                if ((task.taskContext.getTaskMode() == 1) && atomicReferenceArray.compareAndSet(i, task, (Object) null)) {
                    workQueue.blockingTasksInBuffer.decrementAndGet();
                    add$default(this, task, false, 2, (Object) null);
                    return -1;
                }
            }
        }
        return tryStealLastScheduled(workQueue, true);
    }

    public final void offloadAllWorkTo(@NotNull GlobalQueue globalQueue) {
        Task andSet = this.lastScheduledTask.getAndSet(null);
        if (andSet != null) {
            globalQueue.addLast(andSet);
        }
        do {
        } while (pollTo(globalQueue));
    }

    public final long tryStealLastScheduled(WorkQueue workQueue, boolean z) {
        Task value;
        do {
            value = workQueue.lastScheduledTask.getValue();
            if (value == null) {
                return -2;
            }
            if (z) {
                boolean z2 = true;
                if (value.taskContext.getTaskMode() != 1) {
                    z2 = false;
                }
                if (!z2) {
                    return -2;
                }
            }
            long nanoTime = TasksKt.schedulerTimeSource.nanoTime() - value.submissionTime;
            long j = TasksKt.WORK_STEALING_TIME_RESOLUTION_NS;
            if (nanoTime < j) {
                return j - nanoTime;
            }
        } while (!workQueue.lastScheduledTask.compareAndSet(value, null));
        add$default(this, value, false, 2, (Object) null);
        return -1;
    }

    public final boolean pollTo(GlobalQueue globalQueue) {
        Task pollBuffer = pollBuffer();
        if (pollBuffer == null) {
            return false;
        }
        globalQueue.addLast(pollBuffer);
        return true;
    }

    public final Task pollBuffer() {
        Task andSet;
        while (true) {
            int value = this.consumerIndex.getValue();
            if (value - this.producerIndex.getValue() == 0) {
                return null;
            }
            int i = value & 127;
            if (this.consumerIndex.compareAndSet(value, value + 1) && (andSet = this.buffer.getAndSet(i, (Object) null)) != null) {
                decrementIfBlocking(andSet);
                return andSet;
            }
        }
    }
}
