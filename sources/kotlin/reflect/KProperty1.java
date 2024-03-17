package kotlin.reflect;

import kotlin.jvm.functions.Function1;
import org.jetbrains.annotations.NotNull;

/* compiled from: KProperty.kt */
public interface KProperty1<T, V> extends KProperty<V>, Function1<T, V> {

    /* compiled from: KProperty.kt */
    public interface Getter<T, V> extends KFunction, Function1<T, V> {
    }

    V get(T t);

    @NotNull
    Getter<T, V> getGetter();
}
