package kotlin;

import java.io.Serializable;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: Result.kt */
public final class Result<T> implements Serializable {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @Nullable
    private final Object value;

    @NotNull
    /* renamed from: constructor-impl  reason: not valid java name */
    public static <T> Object m4641constructorimpl(@Nullable Object obj) {
        return obj;
    }

    /* renamed from: equals-impl  reason: not valid java name */
    public static boolean m4642equalsimpl(Object obj, Object obj2) {
        return (obj2 instanceof Result) && Intrinsics.areEqual(obj, ((Result) obj2).m4647unboximpl());
    }

    /* renamed from: hashCode-impl  reason: not valid java name */
    public static int m4644hashCodeimpl(Object obj) {
        if (obj == null) {
            return 0;
        }
        return obj.hashCode();
    }

    public boolean equals(Object obj) {
        return m4642equalsimpl(this.value, obj);
    }

    public int hashCode() {
        return m4644hashCodeimpl(this.value);
    }

    /* renamed from: unbox-impl  reason: not valid java name */
    public final /* synthetic */ Object m4647unboximpl() {
        return this.value;
    }

    /* renamed from: isFailure-impl  reason: not valid java name */
    public static final boolean m4645isFailureimpl(Object obj) {
        return obj instanceof Failure;
    }

    @Nullable
    /* renamed from: exceptionOrNull-impl  reason: not valid java name */
    public static final Throwable m4643exceptionOrNullimpl(Object obj) {
        if (obj instanceof Failure) {
            return ((Failure) obj).exception;
        }
        return null;
    }

    @NotNull
    public String toString() {
        return m4646toStringimpl(this.value);
    }

    @NotNull
    /* renamed from: toString-impl  reason: not valid java name */
    public static String m4646toStringimpl(Object obj) {
        if (obj instanceof Failure) {
            return ((Failure) obj).toString();
        }
        return "Success(" + obj + ')';
    }

    /* compiled from: Result.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }

    /* compiled from: Result.kt */
    public static final class Failure implements Serializable {
        @NotNull
        public final Throwable exception;

        public Failure(@NotNull Throwable th) {
            this.exception = th;
        }

        public boolean equals(@Nullable Object obj) {
            return (obj instanceof Failure) && Intrinsics.areEqual((Object) this.exception, (Object) ((Failure) obj).exception);
        }

        public int hashCode() {
            return this.exception.hashCode();
        }

        @NotNull
        public String toString() {
            return "Failure(" + this.exception + ')';
        }
    }
}
