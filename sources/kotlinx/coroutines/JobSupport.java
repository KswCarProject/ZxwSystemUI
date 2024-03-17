package kotlinx.coroutines;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CancellationException;
import kotlin.ExceptionsKt__ExceptionsKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsJvmKt;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugProbesKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.atomicfu.AtomicBoolean;
import kotlinx.atomicfu.AtomicFU;
import kotlinx.atomicfu.AtomicRef;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.internal.LockFreeLinkedListNode;
import kotlinx.coroutines.internal.OpDescriptor;
import kotlinx.coroutines.internal.StackTraceRecoveryKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: JobSupport.kt */
public class JobSupport implements Job, ChildJob, ParentJob {
    @NotNull
    public final AtomicRef<ChildHandle> _parentHandle;
    @NotNull
    public final AtomicRef<Object> _state;

    public void afterCompletion(@Nullable Object obj) {
    }

    @NotNull
    public String cancellationExceptionMessage() {
        return "Job was cancelled";
    }

    public boolean getHandlesException$external__kotlinx_coroutines__android_common__kotlinx_coroutines() {
        return true;
    }

    public boolean getOnCancelComplete$external__kotlinx_coroutines__android_common__kotlinx_coroutines() {
        return false;
    }

    public boolean handleJobException(@NotNull Throwable th) {
        return false;
    }

    public boolean isScopedCoroutine() {
        return false;
    }

    public void onCancelling(@Nullable Throwable th) {
    }

    public void onCompletionInternal(@Nullable Object obj) {
    }

    public void onStart() {
    }

    public JobSupport(boolean z) {
        this._state = AtomicFU.atomic(z ? JobSupportKt.EMPTY_ACTIVE : JobSupportKt.EMPTY_NEW);
        this._parentHandle = AtomicFU.atomic(null);
    }

    public <R> R fold(R r, @NotNull Function2<? super R, ? super CoroutineContext.Element, ? extends R> function2) {
        return Job.DefaultImpls.fold(this, r, function2);
    }

    @Nullable
    public <E extends CoroutineContext.Element> E get(@NotNull CoroutineContext.Key<E> key) {
        return Job.DefaultImpls.get(this, key);
    }

    @NotNull
    public CoroutineContext minusKey(@NotNull CoroutineContext.Key<?> key) {
        return Job.DefaultImpls.minusKey(this, key);
    }

    @NotNull
    public CoroutineContext plus(@NotNull CoroutineContext coroutineContext) {
        return Job.DefaultImpls.plus(this, coroutineContext);
    }

    @NotNull
    public final CoroutineContext.Key<?> getKey() {
        return Job.Key;
    }

    public final boolean addLastAtomic(Object obj, NodeList nodeList, JobNode jobNode) {
        int tryCondAddNext;
        JobSupport$addLastAtomic$$inlined$addLastIf$1 jobSupport$addLastAtomic$$inlined$addLastIf$1 = new JobSupport$addLastAtomic$$inlined$addLastIf$1(jobNode, this, obj);
        do {
            tryCondAddNext = nodeList.getPrevNode().tryCondAddNext(jobNode, nodeList, jobSupport$addLastAtomic$$inlined$addLastIf$1);
            if (tryCondAddNext == 1) {
                return true;
            }
        } while (tryCondAddNext != 2);
        return false;
    }

    @Nullable
    public final ChildHandle getParentHandle$external__kotlinx_coroutines__android_common__kotlinx_coroutines() {
        return this._parentHandle.getValue();
    }

    public final void setParentHandle$external__kotlinx_coroutines__android_common__kotlinx_coroutines(@Nullable ChildHandle childHandle) {
        this._parentHandle.setValue(childHandle);
    }

    public final void initParentJob(@Nullable Job job) {
        if (DebugKt.getASSERTIONS_ENABLED()) {
            if (!(getParentHandle$external__kotlinx_coroutines__android_common__kotlinx_coroutines() == null)) {
                throw new AssertionError();
            }
        }
        if (job == null) {
            setParentHandle$external__kotlinx_coroutines__android_common__kotlinx_coroutines(NonDisposableHandle.INSTANCE);
            return;
        }
        job.start();
        ChildHandle attachChild = job.attachChild(this);
        setParentHandle$external__kotlinx_coroutines__android_common__kotlinx_coroutines(attachChild);
        if (isCompleted()) {
            attachChild.dispose();
            setParentHandle$external__kotlinx_coroutines__android_common__kotlinx_coroutines(NonDisposableHandle.INSTANCE);
        }
    }

    @Nullable
    public final Object getState$external__kotlinx_coroutines__android_common__kotlinx_coroutines() {
        AtomicRef<Object> atomicRef = this._state;
        while (true) {
            Object value = atomicRef.getValue();
            if (!(value instanceof OpDescriptor)) {
                return value;
            }
            ((OpDescriptor) value).perform(this);
        }
    }

    public final Object cancelMakeCompleting(Object obj) {
        Object tryMakeCompleting;
        do {
            Object state$external__kotlinx_coroutines__android_common__kotlinx_coroutines = getState$external__kotlinx_coroutines__android_common__kotlinx_coroutines();
            if (!(state$external__kotlinx_coroutines__android_common__kotlinx_coroutines instanceof Incomplete) || ((state$external__kotlinx_coroutines__android_common__kotlinx_coroutines instanceof Finishing) && ((Finishing) state$external__kotlinx_coroutines__android_common__kotlinx_coroutines).isCompleting())) {
                return JobSupportKt.COMPLETING_ALREADY;
            }
            tryMakeCompleting = tryMakeCompleting(state$external__kotlinx_coroutines__android_common__kotlinx_coroutines, new CompletedExceptionally(createCauseException(obj), false, 2, (DefaultConstructorMarker) null));
        } while (tryMakeCompleting == JobSupportKt.COMPLETING_RETRY);
        return tryMakeCompleting;
    }

    public final boolean joinInternal() {
        Object state$external__kotlinx_coroutines__android_common__kotlinx_coroutines;
        do {
            state$external__kotlinx_coroutines__android_common__kotlinx_coroutines = getState$external__kotlinx_coroutines__android_common__kotlinx_coroutines();
            if (!(state$external__kotlinx_coroutines__android_common__kotlinx_coroutines instanceof Incomplete)) {
                return false;
            }
        } while (startInternal(state$external__kotlinx_coroutines__android_common__kotlinx_coroutines) < 0);
        return true;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:24:0x003e, code lost:
        if (r0 != null) goto L_0x0041;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0041, code lost:
        notifyCancelling(((kotlinx.coroutines.JobSupport.Finishing) r2).getList(), r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x004e, code lost:
        return kotlinx.coroutines.JobSupportKt.access$getCOMPLETING_ALREADY$p();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.lang.Object makeCancelling(java.lang.Object r7) {
        /*
            r6 = this;
            r0 = 0
            r1 = r0
        L_0x0002:
            java.lang.Object r2 = r6.getState$external__kotlinx_coroutines__android_common__kotlinx_coroutines()
            boolean r3 = r2 instanceof kotlinx.coroutines.JobSupport.Finishing
            if (r3 == 0) goto L_0x0052
            monitor-enter(r2)
            r3 = r2
            kotlinx.coroutines.JobSupport$Finishing r3 = (kotlinx.coroutines.JobSupport.Finishing) r3     // Catch:{ all -> 0x004f }
            boolean r3 = r3.isSealed()     // Catch:{ all -> 0x004f }
            if (r3 == 0) goto L_0x001a
            kotlinx.coroutines.internal.Symbol r6 = kotlinx.coroutines.JobSupportKt.TOO_LATE_TO_CANCEL     // Catch:{ all -> 0x004f }
            monitor-exit(r2)
            return r6
        L_0x001a:
            r3 = r2
            kotlinx.coroutines.JobSupport$Finishing r3 = (kotlinx.coroutines.JobSupport.Finishing) r3     // Catch:{ all -> 0x004f }
            boolean r3 = r3.isCancelling()     // Catch:{ all -> 0x004f }
            if (r7 != 0) goto L_0x0025
            if (r3 != 0) goto L_0x0031
        L_0x0025:
            if (r1 != 0) goto L_0x002b
            java.lang.Throwable r1 = r6.createCauseException(r7)     // Catch:{ all -> 0x004f }
        L_0x002b:
            r7 = r2
            kotlinx.coroutines.JobSupport$Finishing r7 = (kotlinx.coroutines.JobSupport.Finishing) r7     // Catch:{ all -> 0x004f }
            r7.addExceptionLocked(r1)     // Catch:{ all -> 0x004f }
        L_0x0031:
            r7 = r2
            kotlinx.coroutines.JobSupport$Finishing r7 = (kotlinx.coroutines.JobSupport.Finishing) r7     // Catch:{ all -> 0x004f }
            java.lang.Throwable r7 = r7.getRootCause()     // Catch:{ all -> 0x004f }
            r1 = r3 ^ 1
            if (r1 == 0) goto L_0x003d
            r0 = r7
        L_0x003d:
            monitor-exit(r2)
            if (r0 != 0) goto L_0x0041
            goto L_0x004a
        L_0x0041:
            kotlinx.coroutines.JobSupport$Finishing r2 = (kotlinx.coroutines.JobSupport.Finishing) r2
            kotlinx.coroutines.NodeList r7 = r2.getList()
            r6.notifyCancelling(r7, r0)
        L_0x004a:
            kotlinx.coroutines.internal.Symbol r6 = kotlinx.coroutines.JobSupportKt.COMPLETING_ALREADY
            return r6
        L_0x004f:
            r6 = move-exception
            monitor-exit(r2)
            throw r6
        L_0x0052:
            boolean r3 = r2 instanceof kotlinx.coroutines.Incomplete
            if (r3 == 0) goto L_0x009a
            if (r1 != 0) goto L_0x005c
            java.lang.Throwable r1 = r6.createCauseException(r7)
        L_0x005c:
            r3 = r2
            kotlinx.coroutines.Incomplete r3 = (kotlinx.coroutines.Incomplete) r3
            boolean r4 = r3.isActive()
            if (r4 == 0) goto L_0x0070
            boolean r2 = r6.tryMakeCancelling(r3, r1)
            if (r2 == 0) goto L_0x0002
            kotlinx.coroutines.internal.Symbol r6 = kotlinx.coroutines.JobSupportKt.COMPLETING_ALREADY
            return r6
        L_0x0070:
            kotlinx.coroutines.CompletedExceptionally r3 = new kotlinx.coroutines.CompletedExceptionally
            r4 = 0
            r5 = 2
            r3.<init>(r1, r4, r5, r0)
            java.lang.Object r3 = r6.tryMakeCompleting(r2, r3)
            kotlinx.coroutines.internal.Symbol r4 = kotlinx.coroutines.JobSupportKt.COMPLETING_ALREADY
            if (r3 == r4) goto L_0x008a
            kotlinx.coroutines.internal.Symbol r2 = kotlinx.coroutines.JobSupportKt.COMPLETING_RETRY
            if (r3 != r2) goto L_0x0089
            goto L_0x0002
        L_0x0089:
            return r3
        L_0x008a:
            java.lang.IllegalStateException r6 = new java.lang.IllegalStateException
            java.lang.String r7 = "Cannot happen in "
            java.lang.String r7 = kotlin.jvm.internal.Intrinsics.stringPlus(r7, r2)
            java.lang.String r7 = r7.toString()
            r6.<init>(r7)
            throw r6
        L_0x009a:
            kotlinx.coroutines.internal.Symbol r6 = kotlinx.coroutines.JobSupportKt.TOO_LATE_TO_CANCEL
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.JobSupport.makeCancelling(java.lang.Object):java.lang.Object");
    }

    @Nullable
    public final Object makeCompletingOnce$external__kotlinx_coroutines__android_common__kotlinx_coroutines(@Nullable Object obj) {
        Object tryMakeCompleting;
        do {
            tryMakeCompleting = tryMakeCompleting(getState$external__kotlinx_coroutines__android_common__kotlinx_coroutines(), obj);
            if (tryMakeCompleting == JobSupportKt.COMPLETING_ALREADY) {
                throw new IllegalStateException("Job " + this + " is already complete or completing, but is being completed with " + obj, getExceptionOrNull(obj));
            }
        } while (tryMakeCompleting == JobSupportKt.COMPLETING_RETRY);
        return tryMakeCompleting;
    }

    /* JADX WARNING: Removed duplicated region for block: B:0:0x0000 A[LOOP_START, MTH_ENTER_BLOCK] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void removeNode$external__kotlinx_coroutines__android_common__kotlinx_coroutines(@org.jetbrains.annotations.NotNull kotlinx.coroutines.JobNode r4) {
        /*
            r3 = this;
        L_0x0000:
            java.lang.Object r0 = r3.getState$external__kotlinx_coroutines__android_common__kotlinx_coroutines()
            boolean r1 = r0 instanceof kotlinx.coroutines.JobNode
            if (r1 == 0) goto L_0x0018
            if (r0 == r4) goto L_0x000b
            return
        L_0x000b:
            kotlinx.atomicfu.AtomicRef<java.lang.Object> r1 = r3._state
            kotlinx.coroutines.Empty r2 = kotlinx.coroutines.JobSupportKt.EMPTY_ACTIVE
            boolean r0 = r1.compareAndSet(r0, r2)
            if (r0 == 0) goto L_0x0000
            return
        L_0x0018:
            boolean r3 = r0 instanceof kotlinx.coroutines.Incomplete
            if (r3 == 0) goto L_0x0027
            kotlinx.coroutines.Incomplete r0 = (kotlinx.coroutines.Incomplete) r0
            kotlinx.coroutines.NodeList r3 = r0.getList()
            if (r3 == 0) goto L_0x0027
            r4.remove()
        L_0x0027:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.JobSupport.removeNode$external__kotlinx_coroutines__android_common__kotlinx_coroutines(kotlinx.coroutines.JobNode):void");
    }

    public final boolean start() {
        int startInternal;
        do {
            startInternal = startInternal(getState$external__kotlinx_coroutines__android_common__kotlinx_coroutines());
            if (startInternal == 0) {
                return false;
            }
        } while (startInternal != 1);
        return true;
    }

    public boolean isActive() {
        Object state$external__kotlinx_coroutines__android_common__kotlinx_coroutines = getState$external__kotlinx_coroutines__android_common__kotlinx_coroutines();
        return (state$external__kotlinx_coroutines__android_common__kotlinx_coroutines instanceof Incomplete) && ((Incomplete) state$external__kotlinx_coroutines__android_common__kotlinx_coroutines).isActive();
    }

    public final boolean isCompleted() {
        return !(getState$external__kotlinx_coroutines__android_common__kotlinx_coroutines() instanceof Incomplete);
    }

    public final Object finalizeFinishingState(Finishing finishing, Object obj) {
        boolean isCancelling;
        Throwable finalRootCause;
        boolean z = true;
        if (DebugKt.getASSERTIONS_ENABLED()) {
            if (!(getState$external__kotlinx_coroutines__android_common__kotlinx_coroutines() == finishing)) {
                throw new AssertionError();
            }
        }
        if (DebugKt.getASSERTIONS_ENABLED() && !(!finishing.isSealed())) {
            throw new AssertionError();
        } else if (!DebugKt.getASSERTIONS_ENABLED() || finishing.isCompleting()) {
            CompletedExceptionally completedExceptionally = obj instanceof CompletedExceptionally ? (CompletedExceptionally) obj : null;
            Throwable th = completedExceptionally == null ? null : completedExceptionally.cause;
            synchronized (finishing) {
                isCancelling = finishing.isCancelling();
                List<Throwable> sealLocked = finishing.sealLocked(th);
                finalRootCause = getFinalRootCause(finishing, sealLocked);
                if (finalRootCause != null) {
                    addSuppressedExceptions(finalRootCause, sealLocked);
                }
            }
            if (!(finalRootCause == null || finalRootCause == th)) {
                obj = new CompletedExceptionally(finalRootCause, false, 2, (DefaultConstructorMarker) null);
            }
            if (finalRootCause != null) {
                if (!cancelParent(finalRootCause) && !handleJobException(finalRootCause)) {
                    z = false;
                }
                if (z) {
                    if (obj != null) {
                        ((CompletedExceptionally) obj).makeHandled();
                    } else {
                        throw new NullPointerException("null cannot be cast to non-null type kotlinx.coroutines.CompletedExceptionally");
                    }
                }
            }
            if (!isCancelling) {
                onCancelling(finalRootCause);
            }
            onCompletionInternal(obj);
            boolean compareAndSet = this._state.compareAndSet(finishing, JobSupportKt.boxIncomplete(obj));
            if (!DebugKt.getASSERTIONS_ENABLED() || compareAndSet) {
                completeStateFinalization(finishing, obj);
                return obj;
            }
            throw new AssertionError();
        } else {
            throw new AssertionError();
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v0, resolved type: java.lang.Throwable} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v1, resolved type: java.lang.Throwable} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v7, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v0, resolved type: java.lang.Throwable} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v3, resolved type: java.lang.Throwable} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.lang.Throwable getFinalRootCause(kotlinx.coroutines.JobSupport.Finishing r5, java.util.List<? extends java.lang.Throwable> r6) {
        /*
            r4 = this;
            boolean r0 = r6.isEmpty()
            r1 = 0
            if (r0 == 0) goto L_0x0018
            boolean r5 = r5.isCancelling()
            if (r5 == 0) goto L_0x0017
            kotlinx.coroutines.JobCancellationException r5 = new kotlinx.coroutines.JobCancellationException
            java.lang.String r6 = r4.cancellationExceptionMessage()
            r5.<init>(r6, r1, r4)
            return r5
        L_0x0017:
            return r1
        L_0x0018:
            r4 = r6
            java.lang.Iterable r4 = (java.lang.Iterable) r4
            java.util.Iterator r5 = r4.iterator()
        L_0x001f:
            boolean r0 = r5.hasNext()
            r2 = 1
            if (r0 == 0) goto L_0x0033
            java.lang.Object r0 = r5.next()
            r3 = r0
            java.lang.Throwable r3 = (java.lang.Throwable) r3
            boolean r3 = r3 instanceof java.util.concurrent.CancellationException
            r3 = r3 ^ r2
            if (r3 == 0) goto L_0x001f
            goto L_0x0034
        L_0x0033:
            r0 = r1
        L_0x0034:
            java.lang.Throwable r0 = (java.lang.Throwable) r0
            if (r0 == 0) goto L_0x0039
            return r0
        L_0x0039:
            r5 = 0
            java.lang.Object r6 = r6.get(r5)
            java.lang.Throwable r6 = (java.lang.Throwable) r6
            boolean r0 = r6 instanceof kotlinx.coroutines.TimeoutCancellationException
            if (r0 == 0) goto L_0x0066
            java.util.Iterator r4 = r4.iterator()
        L_0x0048:
            boolean r0 = r4.hasNext()
            if (r0 == 0) goto L_0x0061
            java.lang.Object r0 = r4.next()
            r3 = r0
            java.lang.Throwable r3 = (java.lang.Throwable) r3
            if (r3 == r6) goto L_0x005d
            boolean r3 = r3 instanceof kotlinx.coroutines.TimeoutCancellationException
            if (r3 == 0) goto L_0x005d
            r3 = r2
            goto L_0x005e
        L_0x005d:
            r3 = r5
        L_0x005e:
            if (r3 == 0) goto L_0x0048
            r1 = r0
        L_0x0061:
            java.lang.Throwable r1 = (java.lang.Throwable) r1
            if (r1 == 0) goto L_0x0066
            return r1
        L_0x0066:
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.JobSupport.getFinalRootCause(kotlinx.coroutines.JobSupport$Finishing, java.util.List):java.lang.Throwable");
    }

    public final void addSuppressedExceptions(Throwable th, List<? extends Throwable> list) {
        if (list.size() > 1) {
            Set newSetFromMap = Collections.newSetFromMap(new IdentityHashMap(list.size()));
            Throwable unwrapImpl = !DebugKt.getRECOVER_STACK_TRACES() ? th : StackTraceRecoveryKt.unwrapImpl(th);
            for (Throwable th2 : list) {
                if (DebugKt.getRECOVER_STACK_TRACES()) {
                    th2 = StackTraceRecoveryKt.unwrapImpl(th2);
                }
                if (th2 != th && th2 != unwrapImpl && !(th2 instanceof CancellationException) && newSetFromMap.add(th2)) {
                    ExceptionsKt__ExceptionsKt.addSuppressed(th, th2);
                }
            }
        }
    }

    public final boolean tryFinalizeSimpleState(Incomplete incomplete, Object obj) {
        if (DebugKt.getASSERTIONS_ENABLED()) {
            if (!((incomplete instanceof Empty) || (incomplete instanceof JobNode))) {
                throw new AssertionError();
            }
        }
        if (DebugKt.getASSERTIONS_ENABLED() && !(!(obj instanceof CompletedExceptionally))) {
            throw new AssertionError();
        } else if (!this._state.compareAndSet(incomplete, JobSupportKt.boxIncomplete(obj))) {
            return false;
        } else {
            onCancelling((Throwable) null);
            onCompletionInternal(obj);
            completeStateFinalization(incomplete, obj);
            return true;
        }
    }

    public final void completeStateFinalization(Incomplete incomplete, Object obj) {
        ChildHandle parentHandle$external__kotlinx_coroutines__android_common__kotlinx_coroutines = getParentHandle$external__kotlinx_coroutines__android_common__kotlinx_coroutines();
        if (parentHandle$external__kotlinx_coroutines__android_common__kotlinx_coroutines != null) {
            parentHandle$external__kotlinx_coroutines__android_common__kotlinx_coroutines.dispose();
            setParentHandle$external__kotlinx_coroutines__android_common__kotlinx_coroutines(NonDisposableHandle.INSTANCE);
        }
        Throwable th = null;
        CompletedExceptionally completedExceptionally = obj instanceof CompletedExceptionally ? (CompletedExceptionally) obj : null;
        if (completedExceptionally != null) {
            th = completedExceptionally.cause;
        }
        if (incomplete instanceof JobNode) {
            try {
                ((JobNode) incomplete).invoke(th);
            } catch (Throwable th2) {
                handleOnCompletionException$external__kotlinx_coroutines__android_common__kotlinx_coroutines(new CompletionHandlerException("Exception in completion handler " + incomplete + " for " + this, th2));
            }
        } else {
            NodeList list = incomplete.getList();
            if (list != null) {
                notifyCompletion(list, th);
            }
        }
    }

    public final Object joinSuspend(Continuation<? super Unit> continuation) {
        CancellableContinuationImpl cancellableContinuationImpl = new CancellableContinuationImpl(IntrinsicsKt__IntrinsicsJvmKt.intercepted(continuation), 1);
        cancellableContinuationImpl.initCancellability();
        CancellableContinuationKt.disposeOnCancellation(cancellableContinuationImpl, invokeOnCompletion(new ResumeOnCompletion(cancellableContinuationImpl)));
        Object result = cancellableContinuationImpl.getResult();
        if (result == IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        return result == IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED() ? result : Unit.INSTANCE;
    }

    public final void notifyCancelling(NodeList nodeList, Throwable th) {
        CompletionHandlerException completionHandlerException;
        onCancelling(th);
        CompletionHandlerException completionHandlerException2 = null;
        for (LockFreeLinkedListNode lockFreeLinkedListNode = (LockFreeLinkedListNode) nodeList.getNext(); !Intrinsics.areEqual((Object) lockFreeLinkedListNode, (Object) nodeList); lockFreeLinkedListNode = lockFreeLinkedListNode.getNextNode()) {
            if (lockFreeLinkedListNode instanceof JobCancellingNode) {
                JobNode jobNode = (JobNode) lockFreeLinkedListNode;
                try {
                    jobNode.invoke(th);
                } catch (Throwable th2) {
                    if (completionHandlerException2 == null) {
                        completionHandlerException = null;
                    } else {
                        ExceptionsKt__ExceptionsKt.addSuppressed(completionHandlerException2, th2);
                        completionHandlerException = completionHandlerException2;
                    }
                    if (completionHandlerException == null) {
                        completionHandlerException2 = new CompletionHandlerException("Exception in completion handler " + jobNode + " for " + this, th2);
                    }
                }
            }
        }
        if (completionHandlerException2 != null) {
            handleOnCompletionException$external__kotlinx_coroutines__android_common__kotlinx_coroutines(completionHandlerException2);
        }
        cancelParent(th);
    }

    public final boolean cancelParent(Throwable th) {
        if (isScopedCoroutine()) {
            return true;
        }
        boolean z = th instanceof CancellationException;
        ChildHandle parentHandle$external__kotlinx_coroutines__android_common__kotlinx_coroutines = getParentHandle$external__kotlinx_coroutines__android_common__kotlinx_coroutines();
        if (parentHandle$external__kotlinx_coroutines__android_common__kotlinx_coroutines == null || parentHandle$external__kotlinx_coroutines__android_common__kotlinx_coroutines == NonDisposableHandle.INSTANCE) {
            return z;
        }
        if (parentHandle$external__kotlinx_coroutines__android_common__kotlinx_coroutines.childCancelled(th) || z) {
            return true;
        }
        return false;
    }

    public final int startInternal(Object obj) {
        if (obj instanceof Empty) {
            if (((Empty) obj).isActive()) {
                return 0;
            }
            if (!this._state.compareAndSet(obj, JobSupportKt.EMPTY_ACTIVE)) {
                return -1;
            }
            onStart();
            return 1;
        } else if (!(obj instanceof InactiveNodeList)) {
            return 0;
        } else {
            if (!this._state.compareAndSet(obj, ((InactiveNodeList) obj).getList())) {
                return -1;
            }
            onStart();
            return 1;
        }
    }

    @NotNull
    public final CancellationException getCancellationException() {
        Object state$external__kotlinx_coroutines__android_common__kotlinx_coroutines = getState$external__kotlinx_coroutines__android_common__kotlinx_coroutines();
        CancellationException cancellationException = null;
        if (state$external__kotlinx_coroutines__android_common__kotlinx_coroutines instanceof Finishing) {
            Throwable rootCause = ((Finishing) state$external__kotlinx_coroutines__android_common__kotlinx_coroutines).getRootCause();
            if (rootCause != null) {
                cancellationException = toCancellationException(rootCause, Intrinsics.stringPlus(DebugStringsKt.getClassSimpleName(this), " is cancelling"));
            }
            if (cancellationException != null) {
                return cancellationException;
            }
            throw new IllegalStateException(Intrinsics.stringPlus("Job is still new or active: ", this).toString());
        } else if (state$external__kotlinx_coroutines__android_common__kotlinx_coroutines instanceof Incomplete) {
            throw new IllegalStateException(Intrinsics.stringPlus("Job is still new or active: ", this).toString());
        } else if (state$external__kotlinx_coroutines__android_common__kotlinx_coroutines instanceof CompletedExceptionally) {
            return toCancellationException$default(this, ((CompletedExceptionally) state$external__kotlinx_coroutines__android_common__kotlinx_coroutines).cause, (String) null, 1, (Object) null);
        } else {
            return new JobCancellationException(Intrinsics.stringPlus(DebugStringsKt.getClassSimpleName(this), " has completed normally"), (Throwable) null, this);
        }
    }

    public static /* synthetic */ CancellationException toCancellationException$default(JobSupport jobSupport, Throwable th, String str, int i, Object obj) {
        if (obj == null) {
            if ((i & 1) != 0) {
                str = null;
            }
            return jobSupport.toCancellationException(th, str);
        }
        throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: toCancellationException");
    }

    @NotNull
    public final CancellationException toCancellationException(@NotNull Throwable th, @Nullable String str) {
        CancellationException cancellationException = th instanceof CancellationException ? (CancellationException) th : null;
        if (cancellationException == null) {
            if (str == null) {
                str = cancellationExceptionMessage();
            }
            cancellationException = new JobCancellationException(str, th, this);
        }
        return cancellationException;
    }

    @NotNull
    public final DisposableHandle invokeOnCompletion(@NotNull Function1<? super Throwable, Unit> function1) {
        return invokeOnCompletion(false, true, function1);
    }

    @NotNull
    public final DisposableHandle invokeOnCompletion(boolean z, boolean z2, @NotNull Function1<? super Throwable, Unit> function1) {
        JobNode makeNode = makeNode(function1, z);
        while (true) {
            Object state$external__kotlinx_coroutines__android_common__kotlinx_coroutines = getState$external__kotlinx_coroutines__android_common__kotlinx_coroutines();
            if (state$external__kotlinx_coroutines__android_common__kotlinx_coroutines instanceof Empty) {
                Empty empty = (Empty) state$external__kotlinx_coroutines__android_common__kotlinx_coroutines;
                if (!empty.isActive()) {
                    promoteEmptyToNodeList(empty);
                } else if (this._state.compareAndSet(state$external__kotlinx_coroutines__android_common__kotlinx_coroutines, makeNode)) {
                    return makeNode;
                }
            } else {
                Throwable th = null;
                if (state$external__kotlinx_coroutines__android_common__kotlinx_coroutines instanceof Incomplete) {
                    NodeList list = ((Incomplete) state$external__kotlinx_coroutines__android_common__kotlinx_coroutines).getList();
                    if (list != null) {
                        DisposableHandle disposableHandle = NonDisposableHandle.INSTANCE;
                        if (z && (state$external__kotlinx_coroutines__android_common__kotlinx_coroutines instanceof Finishing)) {
                            synchronized (state$external__kotlinx_coroutines__android_common__kotlinx_coroutines) {
                                th = ((Finishing) state$external__kotlinx_coroutines__android_common__kotlinx_coroutines).getRootCause();
                                if (th == null || ((function1 instanceof ChildHandleNode) && !((Finishing) state$external__kotlinx_coroutines__android_common__kotlinx_coroutines).isCompleting())) {
                                    if (addLastAtomic(state$external__kotlinx_coroutines__android_common__kotlinx_coroutines, list, makeNode)) {
                                        if (th == null) {
                                            return makeNode;
                                        }
                                        disposableHandle = makeNode;
                                    }
                                }
                                Unit unit = Unit.INSTANCE;
                            }
                        }
                        if (th != null) {
                            if (z2) {
                                function1.invoke(th);
                            }
                            return disposableHandle;
                        } else if (addLastAtomic(state$external__kotlinx_coroutines__android_common__kotlinx_coroutines, list, makeNode)) {
                            return makeNode;
                        }
                    } else if (state$external__kotlinx_coroutines__android_common__kotlinx_coroutines != null) {
                        promoteSingleToNodeList((JobNode) state$external__kotlinx_coroutines__android_common__kotlinx_coroutines);
                    } else {
                        throw new NullPointerException("null cannot be cast to non-null type kotlinx.coroutines.JobNode");
                    }
                } else {
                    if (z2) {
                        CompletedExceptionally completedExceptionally = state$external__kotlinx_coroutines__android_common__kotlinx_coroutines instanceof CompletedExceptionally ? (CompletedExceptionally) state$external__kotlinx_coroutines__android_common__kotlinx_coroutines : null;
                        if (completedExceptionally != null) {
                            th = completedExceptionally.cause;
                        }
                        function1.invoke(th);
                    }
                    return NonDisposableHandle.INSTANCE;
                }
            }
        }
    }

    public final JobNode makeNode(Function1<? super Throwable, Unit> function1, boolean z) {
        JobNode jobNode = null;
        if (z) {
            if (function1 instanceof JobCancellingNode) {
                jobNode = (JobCancellingNode) function1;
            }
            if (jobNode == null) {
                jobNode = new InvokeOnCancelling(function1);
            }
        } else {
            JobNode jobNode2 = function1 instanceof JobNode ? (JobNode) function1 : null;
            if (jobNode2 != null) {
                if (!DebugKt.getASSERTIONS_ENABLED() || (!(jobNode2 instanceof JobCancellingNode))) {
                    jobNode = jobNode2;
                } else {
                    throw new AssertionError();
                }
            }
            if (jobNode == null) {
                jobNode = new InvokeOnCompletion(function1);
            }
        }
        jobNode.setJob(this);
        return jobNode;
    }

    /* JADX WARNING: type inference failed for: r1v1, types: [kotlinx.coroutines.InactiveNodeList] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void promoteEmptyToNodeList(kotlinx.coroutines.Empty r3) {
        /*
            r2 = this;
            kotlinx.coroutines.NodeList r0 = new kotlinx.coroutines.NodeList
            r0.<init>()
            boolean r1 = r3.isActive()
            if (r1 == 0) goto L_0x000c
            goto L_0x0012
        L_0x000c:
            kotlinx.coroutines.InactiveNodeList r1 = new kotlinx.coroutines.InactiveNodeList
            r1.<init>(r0)
            r0 = r1
        L_0x0012:
            kotlinx.atomicfu.AtomicRef<java.lang.Object> r2 = r2._state
            r2.compareAndSet(r3, r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.JobSupport.promoteEmptyToNodeList(kotlinx.coroutines.Empty):void");
    }

    public final void promoteSingleToNodeList(JobNode jobNode) {
        jobNode.addOneIfEmpty(new NodeList());
        this._state.compareAndSet(jobNode, jobNode.getNextNode());
    }

    @Nullable
    public final Object join(@NotNull Continuation<? super Unit> continuation) {
        if (!joinInternal()) {
            JobKt.ensureActive(continuation.getContext());
            return Unit.INSTANCE;
        }
        Object joinSuspend = joinSuspend(continuation);
        return joinSuspend == IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED() ? joinSuspend : Unit.INSTANCE;
    }

    public void cancelInternal(@NotNull Throwable th) {
        cancelImpl$external__kotlinx_coroutines__android_common__kotlinx_coroutines(th);
    }

    public final void parentCancelled(@NotNull ParentJob parentJob) {
        cancelImpl$external__kotlinx_coroutines__android_common__kotlinx_coroutines(parentJob);
    }

    public final void notifyCompletion(NodeList nodeList, Throwable th) {
        CompletionHandlerException completionHandlerException;
        CompletionHandlerException completionHandlerException2 = null;
        for (LockFreeLinkedListNode lockFreeLinkedListNode = (LockFreeLinkedListNode) nodeList.getNext(); !Intrinsics.areEqual((Object) lockFreeLinkedListNode, (Object) nodeList); lockFreeLinkedListNode = lockFreeLinkedListNode.getNextNode()) {
            if (lockFreeLinkedListNode instanceof JobNode) {
                JobNode jobNode = (JobNode) lockFreeLinkedListNode;
                try {
                    jobNode.invoke(th);
                } catch (Throwable th2) {
                    if (completionHandlerException2 == null) {
                        completionHandlerException = null;
                    } else {
                        ExceptionsKt__ExceptionsKt.addSuppressed(completionHandlerException2, th2);
                        completionHandlerException = completionHandlerException2;
                    }
                    if (completionHandlerException == null) {
                        completionHandlerException2 = new CompletionHandlerException("Exception in completion handler " + jobNode + " for " + this, th2);
                    }
                }
            }
        }
        if (completionHandlerException2 != null) {
            handleOnCompletionException$external__kotlinx_coroutines__android_common__kotlinx_coroutines(completionHandlerException2);
        }
    }

    public boolean childCancelled(@NotNull Throwable th) {
        if (th instanceof CancellationException) {
            return true;
        }
        if (!cancelImpl$external__kotlinx_coroutines__android_common__kotlinx_coroutines(th) || !getHandlesException$external__kotlinx_coroutines__android_common__kotlinx_coroutines()) {
            return false;
        }
        return true;
    }

    public final boolean cancelImpl$external__kotlinx_coroutines__android_common__kotlinx_coroutines(@Nullable Object obj) {
        Object access$getCOMPLETING_ALREADY$p = JobSupportKt.COMPLETING_ALREADY;
        if (getOnCancelComplete$external__kotlinx_coroutines__android_common__kotlinx_coroutines() && (access$getCOMPLETING_ALREADY$p = cancelMakeCompleting(obj)) == JobSupportKt.COMPLETING_WAITING_CHILDREN) {
            return true;
        }
        if (access$getCOMPLETING_ALREADY$p == JobSupportKt.COMPLETING_ALREADY) {
            access$getCOMPLETING_ALREADY$p = makeCancelling(obj);
        }
        if (access$getCOMPLETING_ALREADY$p == JobSupportKt.COMPLETING_ALREADY || access$getCOMPLETING_ALREADY$p == JobSupportKt.COMPLETING_WAITING_CHILDREN) {
            return true;
        }
        if (access$getCOMPLETING_ALREADY$p == JobSupportKt.TOO_LATE_TO_CANCEL) {
            return false;
        }
        afterCompletion(access$getCOMPLETING_ALREADY$p);
        return true;
    }

    public void cancel(@Nullable CancellationException cancellationException) {
        if (cancellationException == null) {
            cancellationException = new JobCancellationException(cancellationExceptionMessage(), (Throwable) null, this);
        }
        cancelInternal(cancellationException);
    }

    @NotNull
    public CancellationException getChildJobCancellationCause() {
        Throwable th;
        Object state$external__kotlinx_coroutines__android_common__kotlinx_coroutines = getState$external__kotlinx_coroutines__android_common__kotlinx_coroutines();
        CancellationException cancellationException = null;
        if (state$external__kotlinx_coroutines__android_common__kotlinx_coroutines instanceof Finishing) {
            th = ((Finishing) state$external__kotlinx_coroutines__android_common__kotlinx_coroutines).getRootCause();
        } else if (state$external__kotlinx_coroutines__android_common__kotlinx_coroutines instanceof CompletedExceptionally) {
            th = ((CompletedExceptionally) state$external__kotlinx_coroutines__android_common__kotlinx_coroutines).cause;
        } else if (!(state$external__kotlinx_coroutines__android_common__kotlinx_coroutines instanceof Incomplete)) {
            th = null;
        } else {
            throw new IllegalStateException(Intrinsics.stringPlus("Cannot be cancelling child in this state: ", state$external__kotlinx_coroutines__android_common__kotlinx_coroutines).toString());
        }
        if (th instanceof CancellationException) {
            cancellationException = (CancellationException) th;
        }
        return cancellationException == null ? new JobCancellationException(Intrinsics.stringPlus("Parent job is ", stateString(state$external__kotlinx_coroutines__android_common__kotlinx_coroutines)), th, this) : cancellationException;
    }

    public final Throwable createCauseException(Object obj) {
        if (obj == null ? true : obj instanceof Throwable) {
            Throwable th = (Throwable) obj;
            return th == null ? new JobCancellationException(cancellationExceptionMessage(), (Throwable) null, this) : th;
        } else if (obj != null) {
            return ((ParentJob) obj).getChildJobCancellationCause();
        } else {
            throw new NullPointerException("null cannot be cast to non-null type kotlinx.coroutines.ParentJob");
        }
    }

    public final NodeList getOrPromoteCancellingList(Incomplete incomplete) {
        NodeList list = incomplete.getList();
        if (list != null) {
            return list;
        }
        if (incomplete instanceof Empty) {
            return new NodeList();
        }
        if (incomplete instanceof JobNode) {
            promoteSingleToNodeList((JobNode) incomplete);
            return null;
        }
        throw new IllegalStateException(Intrinsics.stringPlus("State should have list: ", incomplete).toString());
    }

    public final boolean tryMakeCancelling(Incomplete incomplete, Throwable th) {
        if (DebugKt.getASSERTIONS_ENABLED() && !(!(incomplete instanceof Finishing))) {
            throw new AssertionError();
        } else if (!DebugKt.getASSERTIONS_ENABLED() || incomplete.isActive()) {
            NodeList orPromoteCancellingList = getOrPromoteCancellingList(incomplete);
            if (orPromoteCancellingList == null) {
                return false;
            }
            if (!this._state.compareAndSet(incomplete, new Finishing(orPromoteCancellingList, false, th))) {
                return false;
            }
            notifyCancelling(orPromoteCancellingList, th);
            return true;
        } else {
            throw new AssertionError();
        }
    }

    public final Object tryMakeCompleting(Object obj, Object obj2) {
        if (!(obj instanceof Incomplete)) {
            return JobSupportKt.COMPLETING_ALREADY;
        }
        if ((!(obj instanceof Empty) && !(obj instanceof JobNode)) || (obj instanceof ChildHandleNode) || (obj2 instanceof CompletedExceptionally)) {
            return tryMakeCompletingSlowPath((Incomplete) obj, obj2);
        }
        if (tryFinalizeSimpleState((Incomplete) obj, obj2)) {
            return obj2;
        }
        return JobSupportKt.COMPLETING_RETRY;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:46:0x0072, code lost:
        if (r2 != null) goto L_0x0075;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x0075, code lost:
        notifyCancelling(r0, r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x0078, code lost:
        r7 = firstChild(r7);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x007c, code lost:
        if (r7 == null) goto L_0x0087;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x0082, code lost:
        if (tryWaitForChild(r1, r7, r8) == false) goto L_0x0087;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x0086, code lost:
        return kotlinx.coroutines.JobSupportKt.COMPLETING_WAITING_CHILDREN;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x008b, code lost:
        return finalizeFinishingState(r1, r8);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.lang.Object tryMakeCompletingSlowPath(kotlinx.coroutines.Incomplete r7, java.lang.Object r8) {
        /*
            r6 = this;
            kotlinx.coroutines.NodeList r0 = r6.getOrPromoteCancellingList(r7)
            if (r0 != 0) goto L_0x000b
            kotlinx.coroutines.internal.Symbol r6 = kotlinx.coroutines.JobSupportKt.COMPLETING_RETRY
            return r6
        L_0x000b:
            boolean r1 = r7 instanceof kotlinx.coroutines.JobSupport.Finishing
            r2 = 0
            if (r1 == 0) goto L_0x0014
            r1 = r7
            kotlinx.coroutines.JobSupport$Finishing r1 = (kotlinx.coroutines.JobSupport.Finishing) r1
            goto L_0x0015
        L_0x0014:
            r1 = r2
        L_0x0015:
            if (r1 != 0) goto L_0x001d
            kotlinx.coroutines.JobSupport$Finishing r1 = new kotlinx.coroutines.JobSupport$Finishing
            r3 = 0
            r1.<init>(r0, r3, r2)
        L_0x001d:
            monitor-enter(r1)
            boolean r3 = r1.isCompleting()     // Catch:{ all -> 0x008c }
            if (r3 == 0) goto L_0x002a
            kotlinx.coroutines.internal.Symbol r6 = kotlinx.coroutines.JobSupportKt.COMPLETING_ALREADY     // Catch:{ all -> 0x008c }
            monitor-exit(r1)
            return r6
        L_0x002a:
            r3 = 1
            r1.setCompleting(r3)     // Catch:{ all -> 0x008c }
            if (r1 == r7) goto L_0x003e
            kotlinx.atomicfu.AtomicRef<java.lang.Object> r4 = r6._state     // Catch:{ all -> 0x008c }
            boolean r4 = r4.compareAndSet(r7, r1)     // Catch:{ all -> 0x008c }
            if (r4 != 0) goto L_0x003e
            kotlinx.coroutines.internal.Symbol r6 = kotlinx.coroutines.JobSupportKt.COMPLETING_RETRY     // Catch:{ all -> 0x008c }
            monitor-exit(r1)
            return r6
        L_0x003e:
            boolean r4 = kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()     // Catch:{ all -> 0x008c }
            if (r4 == 0) goto L_0x0052
            boolean r4 = r1.isSealed()     // Catch:{ all -> 0x008c }
            r4 = r4 ^ r3
            if (r4 == 0) goto L_0x004c
            goto L_0x0052
        L_0x004c:
            java.lang.AssertionError r6 = new java.lang.AssertionError     // Catch:{ all -> 0x008c }
            r6.<init>()     // Catch:{ all -> 0x008c }
            throw r6     // Catch:{ all -> 0x008c }
        L_0x0052:
            boolean r4 = r1.isCancelling()     // Catch:{ all -> 0x008c }
            boolean r5 = r8 instanceof kotlinx.coroutines.CompletedExceptionally     // Catch:{ all -> 0x008c }
            if (r5 == 0) goto L_0x005e
            r5 = r8
            kotlinx.coroutines.CompletedExceptionally r5 = (kotlinx.coroutines.CompletedExceptionally) r5     // Catch:{ all -> 0x008c }
            goto L_0x005f
        L_0x005e:
            r5 = r2
        L_0x005f:
            if (r5 != 0) goto L_0x0062
            goto L_0x0067
        L_0x0062:
            java.lang.Throwable r5 = r5.cause     // Catch:{ all -> 0x008c }
            r1.addExceptionLocked(r5)     // Catch:{ all -> 0x008c }
        L_0x0067:
            java.lang.Throwable r5 = r1.getRootCause()     // Catch:{ all -> 0x008c }
            r3 = r3 ^ r4
            if (r3 == 0) goto L_0x006f
            r2 = r5
        L_0x006f:
            kotlin.Unit r3 = kotlin.Unit.INSTANCE     // Catch:{ all -> 0x008c }
            monitor-exit(r1)
            if (r2 != 0) goto L_0x0075
            goto L_0x0078
        L_0x0075:
            r6.notifyCancelling(r0, r2)
        L_0x0078:
            kotlinx.coroutines.ChildHandleNode r7 = r6.firstChild(r7)
            if (r7 == 0) goto L_0x0087
            boolean r7 = r6.tryWaitForChild(r1, r7, r8)
            if (r7 == 0) goto L_0x0087
            kotlinx.coroutines.internal.Symbol r6 = kotlinx.coroutines.JobSupportKt.COMPLETING_WAITING_CHILDREN
            return r6
        L_0x0087:
            java.lang.Object r6 = r6.finalizeFinishingState(r1, r8)
            return r6
        L_0x008c:
            r6 = move-exception
            monitor-exit(r1)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.JobSupport.tryMakeCompletingSlowPath(kotlinx.coroutines.Incomplete, java.lang.Object):java.lang.Object");
    }

    public final Throwable getExceptionOrNull(Object obj) {
        CompletedExceptionally completedExceptionally = obj instanceof CompletedExceptionally ? (CompletedExceptionally) obj : null;
        if (completedExceptionally == null) {
            return null;
        }
        return completedExceptionally.cause;
    }

    public final ChildHandleNode firstChild(Incomplete incomplete) {
        ChildHandleNode childHandleNode = incomplete instanceof ChildHandleNode ? (ChildHandleNode) incomplete : null;
        if (childHandleNode != null) {
            return childHandleNode;
        }
        NodeList list = incomplete.getList();
        if (list == null) {
            return null;
        }
        return nextChild(list);
    }

    public final boolean tryWaitForChild(Finishing finishing, ChildHandleNode childHandleNode, Object obj) {
        while (Job.DefaultImpls.invokeOnCompletion$default(childHandleNode.childJob, false, false, new ChildCompletion(this, finishing, childHandleNode, obj), 1, (Object) null) == NonDisposableHandle.INSTANCE) {
            childHandleNode = nextChild(childHandleNode);
            if (childHandleNode == null) {
                return false;
            }
        }
        return true;
    }

    public final void continueCompleting(Finishing finishing, ChildHandleNode childHandleNode, Object obj) {
        if (DebugKt.getASSERTIONS_ENABLED()) {
            if (!(getState$external__kotlinx_coroutines__android_common__kotlinx_coroutines() == finishing)) {
                throw new AssertionError();
            }
        }
        ChildHandleNode nextChild = nextChild(childHandleNode);
        if (nextChild == null || !tryWaitForChild(finishing, nextChild, obj)) {
            afterCompletion(finalizeFinishingState(finishing, obj));
        }
    }

    public final ChildHandleNode nextChild(LockFreeLinkedListNode lockFreeLinkedListNode) {
        while (lockFreeLinkedListNode.isRemoved()) {
            lockFreeLinkedListNode = lockFreeLinkedListNode.getPrevNode();
        }
        while (true) {
            lockFreeLinkedListNode = lockFreeLinkedListNode.getNextNode();
            if (!lockFreeLinkedListNode.isRemoved()) {
                if (lockFreeLinkedListNode instanceof ChildHandleNode) {
                    return (ChildHandleNode) lockFreeLinkedListNode;
                }
                if (lockFreeLinkedListNode instanceof NodeList) {
                    return null;
                }
            }
        }
    }

    @NotNull
    public final ChildHandle attachChild(@NotNull ChildJob childJob) {
        return (ChildHandle) Job.DefaultImpls.invokeOnCompletion$default(this, true, false, new ChildHandleNode(childJob), 2, (Object) null);
    }

    public void handleOnCompletionException$external__kotlinx_coroutines__android_common__kotlinx_coroutines(@NotNull Throwable th) {
        throw th;
    }

    @NotNull
    public String toString() {
        return toDebugString() + '@' + DebugStringsKt.getHexAddress(this);
    }

    @NotNull
    public final String toDebugString() {
        return nameString$external__kotlinx_coroutines__android_common__kotlinx_coroutines() + '{' + stateString(getState$external__kotlinx_coroutines__android_common__kotlinx_coroutines()) + '}';
    }

    @NotNull
    public String nameString$external__kotlinx_coroutines__android_common__kotlinx_coroutines() {
        return DebugStringsKt.getClassSimpleName(this);
    }

    public final String stateString(Object obj) {
        if (obj instanceof Finishing) {
            Finishing finishing = (Finishing) obj;
            if (finishing.isCancelling()) {
                return "Cancelling";
            }
            if (finishing.isCompleting()) {
                return "Completing";
            }
            return "Active";
        } else if (!(obj instanceof Incomplete)) {
            return obj instanceof CompletedExceptionally ? "Cancelled" : "Completed";
        } else {
            if (((Incomplete) obj).isActive()) {
                return "Active";
            }
            return "New";
        }
    }

    /* compiled from: JobSupport.kt */
    public static final class Finishing implements Incomplete {
        @NotNull
        public final AtomicRef<Object> _exceptionsHolder = AtomicFU.atomic(null);
        @NotNull
        public final AtomicBoolean _isCompleting;
        @NotNull
        public final AtomicRef<Throwable> _rootCause;
        @NotNull
        public final NodeList list;

        @NotNull
        public NodeList getList() {
            return this.list;
        }

        public Finishing(@NotNull NodeList nodeList, boolean z, @Nullable Throwable th) {
            this.list = nodeList;
            this._isCompleting = AtomicFU.atomic(z);
            this._rootCause = AtomicFU.atomic(th);
        }

        public final boolean isCompleting() {
            return this._isCompleting.getValue();
        }

        public final void setCompleting(boolean z) {
            this._isCompleting.setValue(z);
        }

        @Nullable
        public final Throwable getRootCause() {
            return this._rootCause.getValue();
        }

        public final void setRootCause(@Nullable Throwable th) {
            this._rootCause.setValue(th);
        }

        public final Object getExceptionsHolder() {
            return this._exceptionsHolder.getValue();
        }

        public final void setExceptionsHolder(Object obj) {
            this._exceptionsHolder.setValue(obj);
        }

        public final boolean isSealed() {
            return getExceptionsHolder() == JobSupportKt.SEALED;
        }

        public final boolean isCancelling() {
            return getRootCause() != null;
        }

        public boolean isActive() {
            return getRootCause() == null;
        }

        @NotNull
        public final List<Throwable> sealLocked(@Nullable Throwable th) {
            ArrayList<Throwable> arrayList;
            Object exceptionsHolder = getExceptionsHolder();
            if (exceptionsHolder == null) {
                arrayList = allocateList();
            } else if (exceptionsHolder instanceof Throwable) {
                ArrayList<Throwable> allocateList = allocateList();
                allocateList.add(exceptionsHolder);
                arrayList = allocateList;
            } else if (exceptionsHolder instanceof ArrayList) {
                arrayList = (ArrayList) exceptionsHolder;
            } else {
                throw new IllegalStateException(Intrinsics.stringPlus("State is ", exceptionsHolder).toString());
            }
            Throwable rootCause = getRootCause();
            if (rootCause != null) {
                arrayList.add(0, rootCause);
            }
            if (th != null && !Intrinsics.areEqual((Object) th, (Object) rootCause)) {
                arrayList.add(th);
            }
            setExceptionsHolder(JobSupportKt.SEALED);
            return arrayList;
        }

        public final void addExceptionLocked(@NotNull Throwable th) {
            Throwable rootCause = getRootCause();
            if (rootCause == null) {
                setRootCause(th);
            } else if (th != rootCause) {
                Object exceptionsHolder = getExceptionsHolder();
                if (exceptionsHolder == null) {
                    setExceptionsHolder(th);
                } else if (exceptionsHolder instanceof Throwable) {
                    if (th != exceptionsHolder) {
                        ArrayList<Throwable> allocateList = allocateList();
                        allocateList.add(exceptionsHolder);
                        allocateList.add(th);
                        setExceptionsHolder(allocateList);
                    }
                } else if (exceptionsHolder instanceof ArrayList) {
                    ((ArrayList) exceptionsHolder).add(th);
                } else {
                    throw new IllegalStateException(Intrinsics.stringPlus("State is ", exceptionsHolder).toString());
                }
            }
        }

        public final ArrayList<Throwable> allocateList() {
            return new ArrayList<>(4);
        }

        @NotNull
        public String toString() {
            return "Finishing[cancelling=" + isCancelling() + ", completing=" + isCompleting() + ", rootCause=" + getRootCause() + ", exceptions=" + getExceptionsHolder() + ", list=" + getList() + ']';
        }
    }

    /* compiled from: JobSupport.kt */
    public static final class ChildCompletion extends JobNode {
        @NotNull
        public final ChildHandleNode child;
        @NotNull
        public final JobSupport parent;
        @Nullable
        public final Object proposedUpdate;
        @NotNull
        public final Finishing state;

        public /* bridge */ /* synthetic */ Object invoke(Object obj) {
            invoke((Throwable) obj);
            return Unit.INSTANCE;
        }

        public ChildCompletion(@NotNull JobSupport jobSupport, @NotNull Finishing finishing, @NotNull ChildHandleNode childHandleNode, @Nullable Object obj) {
            this.parent = jobSupport;
            this.state = finishing;
            this.child = childHandleNode;
            this.proposedUpdate = obj;
        }

        public void invoke(@Nullable Throwable th) {
            this.parent.continueCompleting(this.state, this.child, this.proposedUpdate);
        }
    }
}
