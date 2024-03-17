package kotlinx.atomicfu;

import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.atomicfu.TraceBase;
import org.jetbrains.annotations.NotNull;

/* compiled from: AtomicFU.kt */
public final class AtomicLong {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @Deprecated
    public static final AtomicLongFieldUpdater<AtomicLong> FU = AtomicLongFieldUpdater.newUpdater(AtomicLong.class, "value");
    @NotNull
    public final TraceBase trace;
    public volatile long value;

    public AtomicLong(long j, @NotNull TraceBase traceBase) {
        this.trace = traceBase;
        this.value = j;
    }

    public final long getValue() {
        return this.value;
    }

    public final void setValue(long j) {
        InterceptorKt.getInterceptor().beforeUpdate(this);
        this.value = j;
        TraceBase traceBase = this.trace;
        if (traceBase != TraceBase.None.INSTANCE) {
            traceBase.append("set(" + j + ')');
        }
        InterceptorKt.getInterceptor().afterSet(this, j);
    }

    public final boolean compareAndSet(long j, long j2) {
        InterceptorKt.getInterceptor().beforeUpdate(this);
        boolean compareAndSet = FU.compareAndSet(this, j, j2);
        if (compareAndSet) {
            TraceBase traceBase = this.trace;
            if (traceBase != TraceBase.None.INSTANCE) {
                traceBase.append("CAS(" + j + ", " + j2 + ')');
            }
            InterceptorKt.getInterceptor().afterRMW(this, j, j2);
        }
        return compareAndSet;
    }

    public final long getAndDecrement() {
        InterceptorKt.getInterceptor().beforeUpdate(this);
        long andDecrement = FU.getAndDecrement(this);
        TraceBase traceBase = this.trace;
        if (traceBase != TraceBase.None.INSTANCE) {
            traceBase.append(Intrinsics.stringPlus("getAndDec():", Long.valueOf(andDecrement)));
        }
        InterceptorKt.getInterceptor().afterRMW(this, andDecrement, andDecrement - 1);
        return andDecrement;
    }

    public final long addAndGet(long j) {
        InterceptorKt.getInterceptor().beforeUpdate(this);
        long addAndGet = FU.addAndGet(this, j);
        TraceBase traceBase = this.trace;
        if (traceBase != TraceBase.None.INSTANCE) {
            traceBase.append("addAndGet(" + j + "):" + addAndGet);
        }
        InterceptorKt.getInterceptor().afterRMW(this, addAndGet - j, addAndGet);
        return addAndGet;
    }

    public final long incrementAndGet() {
        InterceptorKt.getInterceptor().beforeUpdate(this);
        long incrementAndGet = FU.incrementAndGet(this);
        TraceBase traceBase = this.trace;
        if (traceBase != TraceBase.None.INSTANCE) {
            traceBase.append(Intrinsics.stringPlus("incAndGet():", Long.valueOf(incrementAndGet)));
        }
        InterceptorKt.getInterceptor().afterRMW(this, incrementAndGet - 1, incrementAndGet);
        return incrementAndGet;
    }

    @NotNull
    public String toString() {
        return String.valueOf(this.value);
    }

    /* compiled from: AtomicFU.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }
}
