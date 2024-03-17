package kotlinx.atomicfu;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlinx.atomicfu.TraceBase;
import org.jetbrains.annotations.NotNull;

/* compiled from: AtomicFU.kt */
public final class AtomicBoolean {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @Deprecated
    public static final AtomicIntegerFieldUpdater<AtomicBoolean> FU = AtomicIntegerFieldUpdater.newUpdater(AtomicBoolean.class, "_value");
    public volatile int _value;
    @NotNull
    public final TraceBase trace;

    public AtomicBoolean(boolean z, @NotNull TraceBase traceBase) {
        this.trace = traceBase;
        this._value = z ? 1 : 0;
    }

    public final boolean getValue() {
        return this._value != 0;
    }

    public final void setValue(boolean z) {
        InterceptorKt.getInterceptor().beforeUpdate(this);
        this._value = z ? 1 : 0;
        TraceBase traceBase = this.trace;
        if (traceBase != TraceBase.None.INSTANCE) {
            traceBase.append("set(" + z + ')');
        }
        InterceptorKt.getInterceptor().afterSet(this, z);
    }

    public final boolean compareAndSet(boolean z, boolean z2) {
        InterceptorKt.getInterceptor().beforeUpdate(this);
        boolean compareAndSet = FU.compareAndSet(this, z ? 1 : 0, z2 ? 1 : 0);
        if (compareAndSet) {
            TraceBase traceBase = this.trace;
            if (traceBase != TraceBase.None.INSTANCE) {
                traceBase.append("CAS(" + z + ", " + z2 + ')');
            }
            InterceptorKt.getInterceptor().afterRMW(this, z, z2);
        }
        return compareAndSet;
    }

    @NotNull
    public String toString() {
        return String.valueOf(getValue());
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
