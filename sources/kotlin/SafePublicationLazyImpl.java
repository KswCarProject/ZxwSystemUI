package kotlin;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: LazyJVM.kt */
final class SafePublicationLazyImpl<T> implements Lazy<T>, Serializable {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    public static final AtomicReferenceFieldUpdater<SafePublicationLazyImpl<?>, Object> valueUpdater = AtomicReferenceFieldUpdater.newUpdater(SafePublicationLazyImpl.class, Object.class, "_value");
    @Nullable
    private volatile Object _value;
    @NotNull

    /* renamed from: final  reason: not valid java name */
    private final Object f9final;
    @Nullable
    private volatile Function0<? extends T> initializer;

    public SafePublicationLazyImpl(@NotNull Function0<? extends T> function0) {
        this.initializer = function0;
        UNINITIALIZED_VALUE uninitialized_value = UNINITIALIZED_VALUE.INSTANCE;
        this._value = uninitialized_value;
        this.f9final = uninitialized_value;
    }

    public T getValue() {
        T t = this._value;
        T t2 = UNINITIALIZED_VALUE.INSTANCE;
        if (t != t2) {
            return t;
        }
        Function0<? extends T> function0 = this.initializer;
        if (function0 != null) {
            T invoke = function0.invoke();
            if (valueUpdater.compareAndSet(this, t2, invoke)) {
                this.initializer = null;
                return invoke;
            }
        }
        return this._value;
    }

    public boolean isInitialized() {
        return this._value != UNINITIALIZED_VALUE.INSTANCE;
    }

    @NotNull
    public String toString() {
        return isInitialized() ? String.valueOf(getValue()) : "Lazy value not initialized yet.";
    }

    private final Object writeReplace() {
        return new InitializedLazyImpl(getValue());
    }

    /* compiled from: LazyJVM.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }
}
