package kotlinx.atomicfu;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.atomicfu.TraceBase;
import org.jetbrains.annotations.NotNull;

/* compiled from: AtomicFU.kt */
public final class AtomicInt {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @Deprecated
    public static final AtomicIntegerFieldUpdater<AtomicInt> FU = AtomicIntegerFieldUpdater.newUpdater(AtomicInt.class, "value");
    @NotNull
    public final TraceBase trace;
    public volatile int value;

    public AtomicInt(int i, @NotNull TraceBase traceBase) {
        this.trace = traceBase;
        this.value = i;
    }

    public final int getValue() {
        return this.value;
    }

    public final void setValue(int i) {
        InterceptorKt.getInterceptor().beforeUpdate(this);
        this.value = i;
        TraceBase traceBase = this.trace;
        if (traceBase != TraceBase.None.INSTANCE) {
            traceBase.append("set(" + i + ')');
        }
        InterceptorKt.getInterceptor().afterSet(this, i);
    }

    public final boolean compareAndSet(int i, int i2) {
        InterceptorKt.getInterceptor().beforeUpdate(this);
        boolean compareAndSet = FU.compareAndSet(this, i, i2);
        if (compareAndSet) {
            TraceBase traceBase = this.trace;
            if (traceBase != TraceBase.None.INSTANCE) {
                traceBase.append("CAS(" + i + ", " + i2 + ')');
            }
            InterceptorKt.getInterceptor().afterRMW(this, i, i2);
        }
        return compareAndSet;
    }

    public final int incrementAndGet() {
        InterceptorKt.getInterceptor().beforeUpdate(this);
        int incrementAndGet = FU.incrementAndGet(this);
        TraceBase traceBase = this.trace;
        if (traceBase != TraceBase.None.INSTANCE) {
            traceBase.append(Intrinsics.stringPlus("incAndGet():", Integer.valueOf(incrementAndGet)));
        }
        InterceptorKt.getInterceptor().afterRMW(this, incrementAndGet - 1, incrementAndGet);
        return incrementAndGet;
    }

    public final int decrementAndGet() {
        InterceptorKt.getInterceptor().beforeUpdate(this);
        int decrementAndGet = FU.decrementAndGet(this);
        TraceBase traceBase = this.trace;
        if (traceBase != TraceBase.None.INSTANCE) {
            traceBase.append(Intrinsics.stringPlus("decAndGet():", Integer.valueOf(decrementAndGet)));
        }
        InterceptorKt.getInterceptor().afterRMW(this, decrementAndGet + 1, decrementAndGet);
        return decrementAndGet;
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
