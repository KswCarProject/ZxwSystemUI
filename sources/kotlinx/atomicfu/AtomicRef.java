package kotlinx.atomicfu;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlinx.atomicfu.TraceBase;
import org.jetbrains.annotations.NotNull;

/* compiled from: AtomicFU.kt */
public final class AtomicRef<T> {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @Deprecated
    public static final AtomicReferenceFieldUpdater<AtomicRef<?>, Object> FU = AtomicReferenceFieldUpdater.newUpdater(AtomicRef.class, Object.class, "value");
    @NotNull
    public final TraceBase trace;
    public volatile T value;

    public AtomicRef(T t, @NotNull TraceBase traceBase) {
        this.trace = traceBase;
        this.value = t;
    }

    public final T getValue() {
        return this.value;
    }

    public final void setValue(T t) {
        InterceptorKt.getInterceptor().beforeUpdate(this);
        this.value = t;
        TraceBase traceBase = this.trace;
        if (traceBase != TraceBase.None.INSTANCE) {
            traceBase.append("set(" + t + ')');
        }
        InterceptorKt.getInterceptor().afterSet(this, t);
    }

    public final void lazySet(T t) {
        InterceptorKt.getInterceptor().beforeUpdate(this);
        FU.lazySet(this, t);
        TraceBase traceBase = this.trace;
        if (traceBase != TraceBase.None.INSTANCE) {
            traceBase.append("lazySet(" + t + ')');
        }
        InterceptorKt.getInterceptor().afterSet(this, t);
    }

    public final boolean compareAndSet(T t, T t2) {
        InterceptorKt.getInterceptor().beforeUpdate(this);
        boolean compareAndSet = FU.compareAndSet(this, t, t2);
        if (compareAndSet) {
            TraceBase traceBase = this.trace;
            if (traceBase != TraceBase.None.INSTANCE) {
                traceBase.append("CAS(" + t + ", " + t2 + ')');
            }
            InterceptorKt.getInterceptor().afterRMW(this, t, t2);
        }
        return compareAndSet;
    }

    public final T getAndSet(T t) {
        InterceptorKt.getInterceptor().beforeUpdate(this);
        T andSet = FU.getAndSet(this, t);
        TraceBase traceBase = this.trace;
        if (traceBase != TraceBase.None.INSTANCE) {
            traceBase.append("getAndSet(" + t + "):" + andSet);
        }
        InterceptorKt.getInterceptor().afterRMW(this, andSet, t);
        return andSet;
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
