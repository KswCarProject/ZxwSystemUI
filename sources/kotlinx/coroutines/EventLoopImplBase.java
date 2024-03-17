package kotlinx.coroutines;

import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt___RangesKt;
import kotlinx.atomicfu.AtomicBoolean;
import kotlinx.atomicfu.AtomicFU;
import kotlinx.atomicfu.AtomicRef;
import kotlinx.coroutines.internal.LockFreeTaskQueueCore;
import kotlinx.coroutines.internal.ThreadSafeHeap;
import kotlinx.coroutines.internal.ThreadSafeHeapNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: EventLoop.common.kt */
public abstract class EventLoopImplBase extends EventLoopImplPlatform {
    @NotNull
    public final AtomicRef<DelayedTaskQueue> _delayed = AtomicFU.atomic(null);
    @NotNull
    public final AtomicBoolean _isCompleted = AtomicFU.atomic(false);
    @NotNull
    public final AtomicRef<Object> _queue = AtomicFU.atomic(null);

    /* access modifiers changed from: private */
    public final boolean isCompleted() {
        return this._isCompleted.getValue();
    }

    public final void setCompleted(boolean z) {
        this._isCompleted.setValue(z);
    }

    public boolean isEmpty() {
        if (!isUnconfinedQueueEmpty()) {
            return false;
        }
        DelayedTaskQueue value = this._delayed.getValue();
        if (value != null && !value.isEmpty()) {
            return false;
        }
        Object value2 = this._queue.getValue();
        if (value2 != null) {
            if (value2 instanceof LockFreeTaskQueueCore) {
                return ((LockFreeTaskQueueCore) value2).isEmpty();
            }
            if (value2 != EventLoop_commonKt.CLOSED_EMPTY) {
                return false;
            }
        }
        return true;
    }

    public long getNextTime() {
        if (super.getNextTime() == 0) {
            return 0;
        }
        Object value = this._queue.getValue();
        if (value != null) {
            if (value instanceof LockFreeTaskQueueCore) {
                if (!((LockFreeTaskQueueCore) value).isEmpty()) {
                    return 0;
                }
            } else if (value == EventLoop_commonKt.CLOSED_EMPTY) {
                return Long.MAX_VALUE;
            } else {
                return 0;
            }
        }
        DelayedTaskQueue value2 = this._delayed.getValue();
        DelayedTask delayedTask = value2 == null ? null : (DelayedTask) value2.peek();
        if (delayedTask == null) {
            return Long.MAX_VALUE;
        }
        long j = delayedTask.nanoTime;
        AbstractTimeSourceKt.getTimeSource();
        return RangesKt___RangesKt.coerceAtLeast(j - System.nanoTime(), 0);
    }

    public void shutdown() {
        ThreadLocalEventLoop.INSTANCE.resetEventLoop$external__kotlinx_coroutines__android_common__kotlinx_coroutines();
        setCompleted(true);
        closeQueue();
        do {
        } while (processNextEvent() <= 0);
        rescheduleAllDelayed();
    }

    /* JADX WARNING: Removed duplicated region for block: B:30:0x004f  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x0053  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public long processNextEvent() {
        /*
            r9 = this;
            boolean r0 = r9.processUnconfinedEvent()
            r1 = 0
            if (r0 == 0) goto L_0x0009
            return r1
        L_0x0009:
            kotlinx.atomicfu.AtomicRef<kotlinx.coroutines.EventLoopImplBase$DelayedTaskQueue> r0 = r9._delayed
            java.lang.Object r0 = r0.getValue()
            kotlinx.coroutines.EventLoopImplBase$DelayedTaskQueue r0 = (kotlinx.coroutines.EventLoopImplBase.DelayedTaskQueue) r0
            if (r0 == 0) goto L_0x0049
            boolean r3 = r0.isEmpty()
            if (r3 != 0) goto L_0x0049
            kotlinx.coroutines.AbstractTimeSourceKt.getTimeSource()
            long r3 = java.lang.System.nanoTime()
        L_0x0020:
            monitor-enter(r0)
            kotlinx.coroutines.internal.ThreadSafeHeapNode r5 = r0.firstImpl()     // Catch:{ all -> 0x0046 }
            r6 = 0
            if (r5 != 0) goto L_0x002a
            monitor-exit(r0)
            goto L_0x0041
        L_0x002a:
            kotlinx.coroutines.EventLoopImplBase$DelayedTask r5 = (kotlinx.coroutines.EventLoopImplBase.DelayedTask) r5     // Catch:{ all -> 0x0046 }
            boolean r7 = r5.timeToExecute(r3)     // Catch:{ all -> 0x0046 }
            r8 = 0
            if (r7 == 0) goto L_0x0038
            boolean r5 = r9.enqueueImpl(r5)     // Catch:{ all -> 0x0046 }
            goto L_0x0039
        L_0x0038:
            r5 = r8
        L_0x0039:
            if (r5 == 0) goto L_0x0040
            kotlinx.coroutines.internal.ThreadSafeHeapNode r5 = r0.removeAtImpl(r8)     // Catch:{ all -> 0x0046 }
            r6 = r5
        L_0x0040:
            monitor-exit(r0)
        L_0x0041:
            kotlinx.coroutines.EventLoopImplBase$DelayedTask r6 = (kotlinx.coroutines.EventLoopImplBase.DelayedTask) r6
            if (r6 != 0) goto L_0x0020
            goto L_0x0049
        L_0x0046:
            r9 = move-exception
            monitor-exit(r0)
            throw r9
        L_0x0049:
            java.lang.Runnable r0 = r9.dequeue()
            if (r0 == 0) goto L_0x0053
            r0.run()
            return r1
        L_0x0053:
            long r0 = r9.getNextTime()
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.EventLoopImplBase.processNextEvent():long");
    }

    public final void dispatch(@NotNull CoroutineContext coroutineContext, @NotNull Runnable runnable) {
        enqueue(runnable);
    }

    public final void enqueue(@NotNull Runnable runnable) {
        if (enqueueImpl(runnable)) {
            unpark();
        } else {
            DefaultExecutor.INSTANCE.enqueue(runnable);
        }
    }

    public final boolean enqueueImpl(Runnable runnable) {
        AtomicRef<Object> atomicRef = this._queue;
        while (true) {
            Object value = atomicRef.getValue();
            if (isCompleted()) {
                return false;
            }
            if (value == null) {
                if (this._queue.compareAndSet(null, runnable)) {
                    return true;
                }
            } else if (value instanceof LockFreeTaskQueueCore) {
                LockFreeTaskQueueCore lockFreeTaskQueueCore = (LockFreeTaskQueueCore) value;
                int addLast = lockFreeTaskQueueCore.addLast(runnable);
                if (addLast == 0) {
                    return true;
                }
                if (addLast == 1) {
                    this._queue.compareAndSet(value, lockFreeTaskQueueCore.next());
                } else if (addLast == 2) {
                    return false;
                }
            } else if (value == EventLoop_commonKt.CLOSED_EMPTY) {
                return false;
            } else {
                LockFreeTaskQueueCore lockFreeTaskQueueCore2 = new LockFreeTaskQueueCore(8, true);
                lockFreeTaskQueueCore2.addLast((Runnable) value);
                lockFreeTaskQueueCore2.addLast(runnable);
                if (this._queue.compareAndSet(value, lockFreeTaskQueueCore2)) {
                    return true;
                }
            }
        }
    }

    public final Runnable dequeue() {
        AtomicRef<Object> atomicRef = this._queue;
        while (true) {
            Object value = atomicRef.getValue();
            if (value == null) {
                return null;
            }
            if (value instanceof LockFreeTaskQueueCore) {
                LockFreeTaskQueueCore lockFreeTaskQueueCore = (LockFreeTaskQueueCore) value;
                Object removeFirstOrNull = lockFreeTaskQueueCore.removeFirstOrNull();
                if (removeFirstOrNull != LockFreeTaskQueueCore.REMOVE_FROZEN) {
                    return (Runnable) removeFirstOrNull;
                }
                this._queue.compareAndSet(value, lockFreeTaskQueueCore.next());
            } else if (value == EventLoop_commonKt.CLOSED_EMPTY) {
                return null;
            } else {
                if (this._queue.compareAndSet(value, null)) {
                    return (Runnable) value;
                }
            }
        }
    }

    public final void closeQueue() {
        if (!DebugKt.getASSERTIONS_ENABLED() || isCompleted()) {
            AtomicRef<Object> atomicRef = this._queue;
            while (true) {
                Object value = atomicRef.getValue();
                if (value == null) {
                    if (this._queue.compareAndSet(null, EventLoop_commonKt.CLOSED_EMPTY)) {
                        return;
                    }
                } else if (value instanceof LockFreeTaskQueueCore) {
                    ((LockFreeTaskQueueCore) value).close();
                    return;
                } else if (value != EventLoop_commonKt.CLOSED_EMPTY) {
                    LockFreeTaskQueueCore lockFreeTaskQueueCore = new LockFreeTaskQueueCore(8, true);
                    lockFreeTaskQueueCore.addLast((Runnable) value);
                    if (this._queue.compareAndSet(value, lockFreeTaskQueueCore)) {
                        return;
                    }
                } else {
                    return;
                }
            }
        } else {
            throw new AssertionError();
        }
    }

    public final void schedule(long j, @NotNull DelayedTask delayedTask) {
        int scheduleImpl = scheduleImpl(j, delayedTask);
        if (scheduleImpl != 0) {
            if (scheduleImpl == 1) {
                reschedule(j, delayedTask);
            } else if (scheduleImpl != 2) {
                throw new IllegalStateException("unexpected result".toString());
            }
        } else if (shouldUnpark(delayedTask)) {
            unpark();
        }
    }

    public final boolean shouldUnpark(DelayedTask delayedTask) {
        DelayedTaskQueue value = this._delayed.getValue();
        return (value == null ? null : (DelayedTask) value.peek()) == delayedTask;
    }

    public final int scheduleImpl(long j, DelayedTask delayedTask) {
        if (isCompleted()) {
            return 1;
        }
        DelayedTaskQueue value = this._delayed.getValue();
        if (value == null) {
            this._delayed.compareAndSet(null, new DelayedTaskQueue(j));
            DelayedTaskQueue value2 = this._delayed.getValue();
            Intrinsics.checkNotNull(value2);
            value = value2;
        }
        return delayedTask.scheduleTask(j, value, this);
    }

    public final void resetAll() {
        this._queue.setValue(null);
        this._delayed.setValue(null);
    }

    public final void rescheduleAllDelayed() {
        AbstractTimeSourceKt.getTimeSource();
        long nanoTime = System.nanoTime();
        while (true) {
            DelayedTaskQueue value = this._delayed.getValue();
            DelayedTask delayedTask = value == null ? null : (DelayedTask) value.removeFirstOrNull();
            if (delayedTask != null) {
                reschedule(nanoTime, delayedTask);
            } else {
                return;
            }
        }
    }

    /* compiled from: EventLoop.common.kt */
    public static abstract class DelayedTask implements Runnable, Comparable<DelayedTask>, DisposableHandle, ThreadSafeHeapNode {
        @Nullable
        public Object _heap;
        public int index;
        public long nanoTime;

        @Nullable
        public ThreadSafeHeap<?> getHeap() {
            Object obj = this._heap;
            if (obj instanceof ThreadSafeHeap) {
                return (ThreadSafeHeap) obj;
            }
            return null;
        }

        public void setHeap(@Nullable ThreadSafeHeap<?> threadSafeHeap) {
            if (this._heap != EventLoop_commonKt.DISPOSED_TASK) {
                this._heap = threadSafeHeap;
                return;
            }
            throw new IllegalArgumentException("Failed requirement.".toString());
        }

        public int getIndex() {
            return this.index;
        }

        public void setIndex(int i) {
            this.index = i;
        }

        public int compareTo(@NotNull DelayedTask delayedTask) {
            int i = ((this.nanoTime - delayedTask.nanoTime) > 0 ? 1 : ((this.nanoTime - delayedTask.nanoTime) == 0 ? 0 : -1));
            if (i > 0) {
                return 1;
            }
            return i < 0 ? -1 : 0;
        }

        public final boolean timeToExecute(long j) {
            return j - this.nanoTime >= 0;
        }

        public final synchronized int scheduleTask(long j, @NotNull DelayedTaskQueue delayedTaskQueue, @NotNull EventLoopImplBase eventLoopImplBase) {
            if (this._heap == EventLoop_commonKt.DISPOSED_TASK) {
                return 2;
            }
            synchronized (delayedTaskQueue) {
                DelayedTask delayedTask = (DelayedTask) delayedTaskQueue.firstImpl();
                if (eventLoopImplBase.isCompleted()) {
                    return 1;
                }
                if (delayedTask == null) {
                    delayedTaskQueue.timeNow = j;
                } else {
                    long j2 = delayedTask.nanoTime;
                    if (j2 - j < 0) {
                        j = j2;
                    }
                    if (j - delayedTaskQueue.timeNow > 0) {
                        delayedTaskQueue.timeNow = j;
                    }
                }
                long j3 = this.nanoTime;
                long j4 = delayedTaskQueue.timeNow;
                if (j3 - j4 < 0) {
                    this.nanoTime = j4;
                }
                delayedTaskQueue.addImpl(this);
                return 0;
            }
        }

        public final synchronized void dispose() {
            Object obj = this._heap;
            if (obj != EventLoop_commonKt.DISPOSED_TASK) {
                DelayedTaskQueue delayedTaskQueue = obj instanceof DelayedTaskQueue ? (DelayedTaskQueue) obj : null;
                if (delayedTaskQueue != null) {
                    delayedTaskQueue.remove(this);
                }
                this._heap = EventLoop_commonKt.DISPOSED_TASK;
            }
        }

        @NotNull
        public String toString() {
            return "Delayed[nanos=" + this.nanoTime + ']';
        }
    }

    /* compiled from: EventLoop.common.kt */
    public static final class DelayedTaskQueue extends ThreadSafeHeap<DelayedTask> {
        public long timeNow;

        public DelayedTaskQueue(long j) {
            this.timeNow = j;
        }
    }
}
