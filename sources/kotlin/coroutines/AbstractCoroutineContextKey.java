package kotlin.coroutines;

import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.CoroutineContext.Element;
import kotlin.jvm.functions.Function1;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: CoroutineContextImpl.kt */
public abstract class AbstractCoroutineContextKey<B extends CoroutineContext.Element, E extends B> implements CoroutineContext.Key<E> {
    @NotNull
    public final Function1<CoroutineContext.Element, E> safeCast;
    @NotNull
    public final CoroutineContext.Key<?> topmostKey;

    public AbstractCoroutineContextKey(@NotNull CoroutineContext.Key<B> key, @NotNull Function1<? super CoroutineContext.Element, ? extends E> function1) {
        this.safeCast = function1;
        this.topmostKey = key instanceof AbstractCoroutineContextKey ? ((AbstractCoroutineContextKey) key).topmostKey : key;
    }

    @Nullable
    public final E tryCast$kotlin_stdlib(@NotNull CoroutineContext.Element element) {
        return (CoroutineContext.Element) this.safeCast.invoke(element);
    }

    public final boolean isSubKey$kotlin_stdlib(@NotNull CoroutineContext.Key<?> key) {
        return key == this || this.topmostKey == key;
    }
}
