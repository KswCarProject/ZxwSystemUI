package kotlinx.coroutines.scheduling;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.locks.LockSupport;
import kotlin.Unit;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.random.Random;
import kotlin.ranges.RangesKt___RangesKt;
import kotlinx.atomicfu.AtomicBoolean;
import kotlinx.atomicfu.AtomicFU;
import kotlinx.atomicfu.AtomicInt;
import kotlinx.atomicfu.AtomicLong;
import kotlinx.coroutines.AbstractTimeSourceKt;
import kotlinx.coroutines.DebugKt;
import kotlinx.coroutines.DebugStringsKt;
import kotlinx.coroutines.internal.Symbol;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: CoroutineScheduler.kt */
public final class CoroutineScheduler implements Executor, Closeable {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    public static final Symbol NOT_IN_STACK = new Symbol("NOT_IN_STACK");
    @NotNull
    public final AtomicBoolean _isTerminated;
    @NotNull
    public final AtomicLong controlState;
    public final int corePoolSize;
    @NotNull
    public final GlobalQueue globalBlockingQueue;
    @NotNull
    public final GlobalQueue globalCpuQueue;
    public final long idleWorkerKeepAliveNs;
    public final int maxPoolSize;
    @NotNull
    public final AtomicLong parkedWorkersStack;
    @NotNull
    public final String schedulerName;
    @NotNull
    public final AtomicReferenceArray<Worker> workers;

    /* compiled from: CoroutineScheduler.kt */
    public /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[WorkerState.values().length];
            iArr[WorkerState.PARKING.ordinal()] = 1;
            iArr[WorkerState.BLOCKING.ordinal()] = 2;
            iArr[WorkerState.CPU_ACQUIRED.ordinal()] = 3;
            iArr[WorkerState.DORMANT.ordinal()] = 4;
            iArr[WorkerState.TERMINATED.ordinal()] = 5;
            $EnumSwitchMapping$0 = iArr;
        }
    }

    /* compiled from: CoroutineScheduler.kt */
    public enum WorkerState {
        CPU_ACQUIRED,
        BLOCKING,
        PARKING,
        DORMANT,
        TERMINATED
    }

    /* compiled from: CoroutineScheduler.kt */
    public final class Worker extends Thread {
        public volatile int indexInArray;
        @NotNull
        public final WorkQueue localQueue;
        public boolean mayHaveLocalTasks;
        public long minDelayUntilStealableTaskNs;
        @Nullable
        public volatile Object nextParkedWorker;
        public int rngState;
        @NotNull
        public WorkerState state;
        public long terminationDeadline;
        @NotNull
        public final AtomicInt workerCtl;

        public final void executeTask(Task task) {
            int taskMode = task.taskContext.getTaskMode();
            idleReset(taskMode);
            beforeTask(taskMode);
            CoroutineScheduler.this.runSafely(task);
            afterTask(taskMode);
        }

        public Worker() {
            setDaemon(true);
            this.localQueue = new WorkQueue();
            this.state = WorkerState.DORMANT;
            this.workerCtl = AtomicFU.atomic(0);
            this.nextParkedWorker = CoroutineScheduler.NOT_IN_STACK;
            this.rngState = Random.Default.nextInt();
        }

        public final int getIndexInArray() {
            return this.indexInArray;
        }

        public final void setIndexInArray(int i) {
            StringBuilder sb = new StringBuilder();
            sb.append(CoroutineScheduler.this.schedulerName);
            sb.append("-worker-");
            sb.append(i == 0 ? "TERMINATED" : String.valueOf(i));
            setName(sb.toString());
            this.indexInArray = i;
        }

        public Worker(int i) {
            this();
            setIndexInArray(i);
        }

        @NotNull
        public final AtomicInt getWorkerCtl() {
            return this.workerCtl;
        }

        @Nullable
        public final Object getNextParkedWorker() {
            return this.nextParkedWorker;
        }

        public final void setNextParkedWorker(@Nullable Object obj) {
            this.nextParkedWorker = obj;
        }

        public final boolean tryAcquireCpuPermit() {
            boolean z;
            if (this.state != WorkerState.CPU_ACQUIRED) {
                CoroutineScheduler coroutineScheduler = CoroutineScheduler.this;
                AtomicLong access$getControlState$p = coroutineScheduler.controlState;
                while (true) {
                    long value = access$getControlState$p.getValue();
                    if (((int) ((9223367638808264704L & value) >> 42)) != 0) {
                        if (coroutineScheduler.controlState.compareAndSet(value, value - 4398046511104L)) {
                            z = true;
                            break;
                        }
                    } else {
                        z = false;
                        break;
                    }
                }
                if (!z) {
                    return false;
                }
                this.state = WorkerState.CPU_ACQUIRED;
            }
            return true;
        }

        public final boolean tryReleaseCpu(@NotNull WorkerState workerState) {
            WorkerState workerState2 = this.state;
            boolean z = workerState2 == WorkerState.CPU_ACQUIRED;
            if (z) {
                CoroutineScheduler.this.controlState.addAndGet(4398046511104L);
            }
            if (workerState2 != workerState) {
                this.state = workerState;
            }
            return z;
        }

        public void run() {
            runWorker();
        }

        public final void runWorker() {
            loop0:
            while (true) {
                boolean z = false;
                while (!CoroutineScheduler.this.isTerminated() && this.state != WorkerState.TERMINATED) {
                    Task findTask = findTask(this.mayHaveLocalTasks);
                    if (findTask != null) {
                        this.minDelayUntilStealableTaskNs = 0;
                        executeTask(findTask);
                    } else {
                        this.mayHaveLocalTasks = false;
                        if (this.minDelayUntilStealableTaskNs == 0) {
                            tryPark();
                        } else if (!z) {
                            z = true;
                        } else {
                            tryReleaseCpu(WorkerState.PARKING);
                            Thread.interrupted();
                            LockSupport.parkNanos(this.minDelayUntilStealableTaskNs);
                            this.minDelayUntilStealableTaskNs = 0;
                        }
                    }
                }
            }
            tryReleaseCpu(WorkerState.TERMINATED);
        }

        public final void tryPark() {
            if (!inStack()) {
                CoroutineScheduler.this.parkedWorkersStackPush(this);
                return;
            }
            if (DebugKt.getASSERTIONS_ENABLED()) {
                if (!(this.localQueue.getSize$external__kotlinx_coroutines__android_common__kotlinx_coroutines() == 0)) {
                    throw new AssertionError();
                }
            }
            this.workerCtl.setValue(-1);
            while (inStack() && this.workerCtl.getValue() == -1 && !CoroutineScheduler.this.isTerminated() && this.state != WorkerState.TERMINATED) {
                tryReleaseCpu(WorkerState.PARKING);
                Thread.interrupted();
                park();
            }
        }

        public final boolean inStack() {
            return this.nextParkedWorker != CoroutineScheduler.NOT_IN_STACK;
        }

        public final void beforeTask(int i) {
            if (i != 0 && tryReleaseCpu(WorkerState.BLOCKING)) {
                CoroutineScheduler.this.signalCpuWork();
            }
        }

        public final void afterTask(int i) {
            if (i != 0) {
                CoroutineScheduler.this.controlState.addAndGet(-2097152);
                WorkerState workerState = this.state;
                if (workerState != WorkerState.TERMINATED) {
                    if (DebugKt.getASSERTIONS_ENABLED()) {
                        if (!(workerState == WorkerState.BLOCKING)) {
                            throw new AssertionError();
                        }
                    }
                    this.state = WorkerState.DORMANT;
                }
            }
        }

        public final int nextInt(int i) {
            int i2 = this.rngState;
            int i3 = i2 ^ (i2 << 13);
            int i4 = i3 ^ (i3 >> 17);
            int i5 = i4 ^ (i4 << 5);
            this.rngState = i5;
            int i6 = i - 1;
            if ((i6 & i) == 0) {
                return i6 & i5;
            }
            return (Integer.MAX_VALUE & i5) % i;
        }

        public final void park() {
            if (this.terminationDeadline == 0) {
                this.terminationDeadline = System.nanoTime() + CoroutineScheduler.this.idleWorkerKeepAliveNs;
            }
            LockSupport.parkNanos(CoroutineScheduler.this.idleWorkerKeepAliveNs);
            if (System.nanoTime() - this.terminationDeadline >= 0) {
                this.terminationDeadline = 0;
                tryTerminateWorker();
            }
        }

        public final void tryTerminateWorker() {
            CoroutineScheduler coroutineScheduler = CoroutineScheduler.this;
            synchronized (coroutineScheduler.workers) {
                if (!coroutineScheduler.isTerminated()) {
                    if (((int) (coroutineScheduler.controlState.getValue() & 2097151)) > coroutineScheduler.corePoolSize) {
                        if (getWorkerCtl().compareAndSet(-1, 1)) {
                            int indexInArray2 = getIndexInArray();
                            setIndexInArray(0);
                            coroutineScheduler.parkedWorkersStackTopUpdate(this, indexInArray2, 0);
                            int andDecrement = (int) (coroutineScheduler.controlState.getAndDecrement() & 2097151);
                            if (andDecrement != indexInArray2) {
                                Worker worker = coroutineScheduler.workers.get(andDecrement);
                                Intrinsics.checkNotNull(worker);
                                Worker worker2 = worker;
                                coroutineScheduler.workers.set(indexInArray2, worker2);
                                worker2.setIndexInArray(indexInArray2);
                                coroutineScheduler.parkedWorkersStackTopUpdate(worker2, andDecrement, indexInArray2);
                            }
                            coroutineScheduler.workers.set(andDecrement, (Object) null);
                            Unit unit = Unit.INSTANCE;
                            this.state = WorkerState.TERMINATED;
                        }
                    }
                }
            }
        }

        public final void idleReset(int i) {
            this.terminationDeadline = 0;
            if (this.state == WorkerState.PARKING) {
                if (DebugKt.getASSERTIONS_ENABLED()) {
                    boolean z = true;
                    if (i != 1) {
                        z = false;
                    }
                    if (!z) {
                        throw new AssertionError();
                    }
                }
                this.state = WorkerState.BLOCKING;
            }
        }

        @Nullable
        public final Task findTask(boolean z) {
            Task task;
            if (tryAcquireCpuPermit()) {
                return findAnyTask(z);
            }
            if (z) {
                task = this.localQueue.poll();
                if (task == null) {
                    task = (Task) CoroutineScheduler.this.globalBlockingQueue.removeFirstOrNull();
                }
            } else {
                task = (Task) CoroutineScheduler.this.globalBlockingQueue.removeFirstOrNull();
            }
            return task == null ? trySteal(true) : task;
        }

        public final Task findAnyTask(boolean z) {
            Task pollGlobalQueues;
            Task pollGlobalQueues2;
            if (z) {
                boolean z2 = nextInt(CoroutineScheduler.this.corePoolSize * 2) == 0;
                if (z2 && (pollGlobalQueues2 = pollGlobalQueues()) != null) {
                    return pollGlobalQueues2;
                }
                Task poll = this.localQueue.poll();
                if (poll != null) {
                    return poll;
                }
                if (!z2 && (pollGlobalQueues = pollGlobalQueues()) != null) {
                    return pollGlobalQueues;
                }
            } else {
                Task pollGlobalQueues3 = pollGlobalQueues();
                if (pollGlobalQueues3 != null) {
                    return pollGlobalQueues3;
                }
            }
            return trySteal(false);
        }

        public final Task pollGlobalQueues() {
            if (nextInt(2) == 0) {
                Task task = (Task) CoroutineScheduler.this.globalCpuQueue.removeFirstOrNull();
                return task == null ? (Task) CoroutineScheduler.this.globalBlockingQueue.removeFirstOrNull() : task;
            }
            Task task2 = (Task) CoroutineScheduler.this.globalBlockingQueue.removeFirstOrNull();
            return task2 == null ? (Task) CoroutineScheduler.this.globalCpuQueue.removeFirstOrNull() : task2;
        }

        public final Task trySteal(boolean z) {
            long j;
            if (DebugKt.getASSERTIONS_ENABLED()) {
                if (!(this.localQueue.getSize$external__kotlinx_coroutines__android_common__kotlinx_coroutines() == 0)) {
                    throw new AssertionError();
                }
            }
            int value = (int) (CoroutineScheduler.this.controlState.getValue() & 2097151);
            if (value < 2) {
                return null;
            }
            int nextInt = nextInt(value);
            CoroutineScheduler coroutineScheduler = CoroutineScheduler.this;
            int i = 0;
            long j2 = Long.MAX_VALUE;
            while (i < value) {
                i++;
                nextInt++;
                if (nextInt > value) {
                    nextInt = 1;
                }
                Worker worker = coroutineScheduler.workers.get(nextInt);
                if (!(worker == null || worker == this)) {
                    if (DebugKt.getASSERTIONS_ENABLED()) {
                        if (!(this.localQueue.getSize$external__kotlinx_coroutines__android_common__kotlinx_coroutines() == 0)) {
                            throw new AssertionError();
                        }
                    }
                    if (z) {
                        j = this.localQueue.tryStealBlockingFrom(worker.localQueue);
                    } else {
                        j = this.localQueue.tryStealFrom(worker.localQueue);
                    }
                    if (j == -1) {
                        return this.localQueue.poll();
                    }
                    if (j > 0) {
                        j2 = Math.min(j2, j);
                    }
                }
            }
            if (j2 == Long.MAX_VALUE) {
                j2 = 0;
            }
            this.minDelayUntilStealableTaskNs = j2;
            return null;
        }
    }

    public final boolean addToGlobalQueue(Task task) {
        boolean z = true;
        if (task.taskContext.getTaskMode() != 1) {
            z = false;
        }
        if (z) {
            return this.globalBlockingQueue.addLast(task);
        }
        return this.globalCpuQueue.addLast(task);
    }

    public CoroutineScheduler(int i, int i2, long j, @NotNull String str) {
        this.corePoolSize = i;
        this.maxPoolSize = i2;
        this.idleWorkerKeepAliveNs = j;
        this.schedulerName = str;
        if (i >= 1) {
            if (i2 >= i) {
                if (i2 <= 2097150) {
                    if (j > 0) {
                        this.globalCpuQueue = new GlobalQueue();
                        this.globalBlockingQueue = new GlobalQueue();
                        this.parkedWorkersStack = AtomicFU.atomic(0);
                        this.workers = new AtomicReferenceArray<>(i2 + 1);
                        this.controlState = AtomicFU.atomic(((long) i) << 42);
                        this._isTerminated = AtomicFU.atomic(false);
                        return;
                    }
                    throw new IllegalArgumentException(("Idle worker keep alive time " + j + " must be positive").toString());
                }
                throw new IllegalArgumentException(("Max pool size " + i2 + " should not exceed maximal supported number of threads 2097150").toString());
            }
            throw new IllegalArgumentException(("Max pool size " + i2 + " should be greater than or equals to core pool size " + i).toString());
        }
        throw new IllegalArgumentException(("Core pool size " + i + " should be at least 1").toString());
    }

    public final void parkedWorkersStackTopUpdate(@NotNull Worker worker, int i, int i2) {
        AtomicLong atomicLong = this.parkedWorkersStack;
        while (true) {
            long value = atomicLong.getValue();
            int i3 = (int) (2097151 & value);
            long j = (2097152 + value) & -2097152;
            if (i3 == i) {
                i3 = i2 == 0 ? parkedWorkersStackNextIndex(worker) : i2;
            }
            if (i3 >= 0 && this.parkedWorkersStack.compareAndSet(value, j | ((long) i3))) {
                return;
            }
        }
    }

    public final boolean parkedWorkersStackPush(@NotNull Worker worker) {
        long value;
        long j;
        int indexInArray;
        if (worker.getNextParkedWorker() != NOT_IN_STACK) {
            return false;
        }
        AtomicLong atomicLong = this.parkedWorkersStack;
        do {
            value = atomicLong.getValue();
            int i = (int) (2097151 & value);
            j = (2097152 + value) & -2097152;
            indexInArray = worker.getIndexInArray();
            if (DebugKt.getASSERTIONS_ENABLED()) {
                if (!(indexInArray != 0)) {
                    throw new AssertionError();
                }
            }
            worker.setNextParkedWorker(this.workers.get(i));
        } while (!this.parkedWorkersStack.compareAndSet(value, j | ((long) indexInArray)));
        return true;
    }

    public final Worker parkedWorkersStackPop() {
        AtomicLong atomicLong = this.parkedWorkersStack;
        while (true) {
            long value = atomicLong.getValue();
            Worker worker = this.workers.get((int) (2097151 & value));
            if (worker == null) {
                return null;
            }
            long j = (2097152 + value) & -2097152;
            int parkedWorkersStackNextIndex = parkedWorkersStackNextIndex(worker);
            if (parkedWorkersStackNextIndex >= 0 && this.parkedWorkersStack.compareAndSet(value, j | ((long) parkedWorkersStackNextIndex))) {
                worker.setNextParkedWorker(NOT_IN_STACK);
                return worker;
            }
        }
    }

    public final int parkedWorkersStackNextIndex(Worker worker) {
        Object nextParkedWorker = worker.getNextParkedWorker();
        while (nextParkedWorker != NOT_IN_STACK) {
            if (nextParkedWorker == null) {
                return 0;
            }
            Worker worker2 = (Worker) nextParkedWorker;
            int indexInArray = worker2.getIndexInArray();
            if (indexInArray != 0) {
                return indexInArray;
            }
            nextParkedWorker = worker2.getNextParkedWorker();
        }
        return -1;
    }

    public final void signalBlockingWork(boolean z) {
        long addAndGet = this.controlState.addAndGet(2097152);
        if (!z && !tryUnpark() && !tryCreateWorker(addAndGet)) {
            tryUnpark();
        }
    }

    public final boolean isTerminated() {
        return this._isTerminated.getValue();
    }

    /* compiled from: CoroutineScheduler.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }

    public void execute(@NotNull Runnable runnable) {
        dispatch$default(this, runnable, (TaskContext) null, false, 6, (Object) null);
    }

    public void close() {
        shutdown(10000);
    }

    public final void shutdown(long j) {
        int value;
        Task task;
        boolean z = false;
        if (this._isTerminated.compareAndSet(false, true)) {
            Worker currentWorker = currentWorker();
            synchronized (this.workers) {
                value = (int) (this.controlState.getValue() & 2097151);
            }
            if (1 <= value) {
                int i = 1;
                while (true) {
                    int i2 = i + 1;
                    Worker worker = this.workers.get(i);
                    Intrinsics.checkNotNull(worker);
                    Worker worker2 = worker;
                    if (worker2 != currentWorker) {
                        while (worker2.isAlive()) {
                            LockSupport.unpark(worker2);
                            worker2.join(j);
                        }
                        WorkerState workerState = worker2.state;
                        if (DebugKt.getASSERTIONS_ENABLED()) {
                            if (!(workerState == WorkerState.TERMINATED)) {
                                throw new AssertionError();
                            }
                        }
                        worker2.localQueue.offloadAllWorkTo(this.globalBlockingQueue);
                    }
                    if (i == value) {
                        break;
                    }
                    i = i2;
                }
            }
            this.globalBlockingQueue.close();
            this.globalCpuQueue.close();
            while (true) {
                if (currentWorker == null) {
                    task = null;
                } else {
                    task = currentWorker.findTask(true);
                }
                if (task == null && (task = (Task) this.globalCpuQueue.removeFirstOrNull()) == null && (task = (Task) this.globalBlockingQueue.removeFirstOrNull()) == null) {
                    break;
                }
                runSafely(task);
            }
            if (currentWorker != null) {
                currentWorker.tryReleaseCpu(WorkerState.TERMINATED);
            }
            if (DebugKt.getASSERTIONS_ENABLED()) {
                if (((int) ((this.controlState.getValue() & 9223367638808264704L) >> 42)) == this.corePoolSize) {
                    z = true;
                }
                if (!z) {
                    throw new AssertionError();
                }
            }
            this.parkedWorkersStack.setValue(0);
            this.controlState.setValue(0);
        }
    }

    public static /* synthetic */ void dispatch$default(CoroutineScheduler coroutineScheduler, Runnable runnable, TaskContext taskContext, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            taskContext = NonBlockingContext.INSTANCE;
        }
        if ((i & 4) != 0) {
            z = false;
        }
        coroutineScheduler.dispatch(runnable, taskContext, z);
    }

    public final void dispatch(@NotNull Runnable runnable, @NotNull TaskContext taskContext, boolean z) {
        AbstractTimeSourceKt.getTimeSource();
        Task createTask = createTask(runnable, taskContext);
        Worker currentWorker = currentWorker();
        Task submitToLocalQueue = submitToLocalQueue(currentWorker, createTask, z);
        if (submitToLocalQueue == null || addToGlobalQueue(submitToLocalQueue)) {
            boolean z2 = z && currentWorker != null;
            if (createTask.taskContext.getTaskMode() != 0) {
                signalBlockingWork(z2);
            } else if (!z2) {
                signalCpuWork();
            }
        } else {
            throw new RejectedExecutionException(Intrinsics.stringPlus(this.schedulerName, " was terminated"));
        }
    }

    @NotNull
    public final Task createTask(@NotNull Runnable runnable, @NotNull TaskContext taskContext) {
        long nanoTime = TasksKt.schedulerTimeSource.nanoTime();
        if (!(runnable instanceof Task)) {
            return new TaskImpl(runnable, nanoTime, taskContext);
        }
        Task task = (Task) runnable;
        task.submissionTime = nanoTime;
        task.taskContext = taskContext;
        return task;
    }

    public final void signalCpuWork() {
        if (!tryUnpark() && !tryCreateWorker$default(this, 0, 1, (Object) null)) {
            tryUnpark();
        }
    }

    public static /* synthetic */ boolean tryCreateWorker$default(CoroutineScheduler coroutineScheduler, long j, int i, Object obj) {
        if ((i & 1) != 0) {
            j = coroutineScheduler.controlState.getValue();
        }
        return coroutineScheduler.tryCreateWorker(j);
    }

    public final boolean tryCreateWorker(long j) {
        if (RangesKt___RangesKt.coerceAtLeast(((int) (2097151 & j)) - ((int) ((j & 4398044413952L) >> 21)), 0) < this.corePoolSize) {
            int createNewWorker = createNewWorker();
            if (createNewWorker == 1 && this.corePoolSize > 1) {
                createNewWorker();
            }
            if (createNewWorker > 0) {
                return true;
            }
        }
        return false;
    }

    public final boolean tryUnpark() {
        Worker parkedWorkersStackPop;
        do {
            parkedWorkersStackPop = parkedWorkersStackPop();
            if (parkedWorkersStackPop == null) {
                return false;
            }
        } while (!parkedWorkersStackPop.getWorkerCtl().compareAndSet(-1, 0));
        LockSupport.unpark(parkedWorkersStackPop);
        return true;
    }

    public final int createNewWorker() {
        synchronized (this.workers) {
            if (isTerminated()) {
                return -1;
            }
            long value = this.controlState.getValue();
            int i = (int) (value & 2097151);
            boolean z = false;
            int coerceAtLeast = RangesKt___RangesKt.coerceAtLeast(i - ((int) ((value & 4398044413952L) >> 21)), 0);
            if (coerceAtLeast >= this.corePoolSize) {
                return 0;
            }
            if (i >= this.maxPoolSize) {
                return 0;
            }
            int value2 = ((int) (this.controlState.getValue() & 2097151)) + 1;
            if (value2 > 0 && this.workers.get(value2) == null) {
                Worker worker = new Worker(value2);
                this.workers.set(value2, worker);
                if (value2 == ((int) (2097151 & this.controlState.incrementAndGet()))) {
                    z = true;
                }
                if (z) {
                    worker.start();
                    int i2 = coerceAtLeast + 1;
                    return i2;
                }
                throw new IllegalArgumentException("Failed requirement.".toString());
            }
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
    }

    public final Task submitToLocalQueue(Worker worker, Task task, boolean z) {
        if (worker == null || worker.state == WorkerState.TERMINATED) {
            return task;
        }
        if (task.taskContext.getTaskMode() == 0 && worker.state == WorkerState.BLOCKING) {
            return task;
        }
        worker.mayHaveLocalTasks = true;
        return worker.localQueue.add(task, z);
    }

    public final Worker currentWorker() {
        Thread currentThread = Thread.currentThread();
        Worker worker = currentThread instanceof Worker ? (Worker) currentThread : null;
        if (worker != null && Intrinsics.areEqual((Object) CoroutineScheduler.this, (Object) this)) {
            return worker;
        }
        return null;
    }

    @NotNull
    public String toString() {
        ArrayList arrayList = new ArrayList();
        int length = this.workers.length();
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        int i6 = 1;
        while (i6 < length) {
            int i7 = i6 + 1;
            Worker worker = this.workers.get(i6);
            if (worker != null) {
                int size$external__kotlinx_coroutines__android_common__kotlinx_coroutines = worker.localQueue.getSize$external__kotlinx_coroutines__android_common__kotlinx_coroutines();
                int i8 = WhenMappings.$EnumSwitchMapping$0[worker.state.ordinal()];
                if (i8 == 1) {
                    i3++;
                } else if (i8 == 2) {
                    i2++;
                    StringBuilder sb = new StringBuilder();
                    sb.append(size$external__kotlinx_coroutines__android_common__kotlinx_coroutines);
                    sb.append('b');
                    arrayList.add(sb.toString());
                } else if (i8 == 3) {
                    i++;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(size$external__kotlinx_coroutines__android_common__kotlinx_coroutines);
                    sb2.append('c');
                    arrayList.add(sb2.toString());
                } else if (i8 == 4) {
                    i4++;
                    if (size$external__kotlinx_coroutines__android_common__kotlinx_coroutines > 0) {
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append(size$external__kotlinx_coroutines__android_common__kotlinx_coroutines);
                        sb3.append('d');
                        arrayList.add(sb3.toString());
                    }
                } else if (i8 == 5) {
                    i5++;
                }
            }
            i6 = i7;
        }
        long value = this.controlState.getValue();
        return this.schedulerName + '@' + DebugStringsKt.getHexAddress(this) + "[Pool Size {core = " + this.corePoolSize + ", max = " + this.maxPoolSize + "}, Worker States {CPU = " + i + ", blocking = " + i2 + ", parked = " + i3 + ", dormant = " + i4 + ", terminated = " + i5 + "}, running workers queues = " + arrayList + ", global CPU queue size = " + this.globalCpuQueue.getSize() + ", global blocking queue size = " + this.globalBlockingQueue.getSize() + ", Control State {created workers= " + ((int) (2097151 & value)) + ", blocking tasks = " + ((int) ((4398044413952L & value) >> 21)) + ", CPUs acquired = " + (this.corePoolSize - ((int) ((9223367638808264704L & value) >> 42))) + "}]";
    }

    public final void runSafely(@NotNull Task task) {
        try {
            task.run();
        } catch (Throwable th) {
            AbstractTimeSourceKt.getTimeSource();
            throw th;
        }
        AbstractTimeSourceKt.getTimeSource();
    }
}
