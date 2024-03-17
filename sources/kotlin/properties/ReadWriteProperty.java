package kotlin.properties;

import kotlin.reflect.KProperty;
import org.jetbrains.annotations.NotNull;

/* compiled from: Interfaces.kt */
public interface ReadWriteProperty<T, V> {
    V getValue(T t, @NotNull KProperty<?> kProperty);

    void setValue(T t, @NotNull KProperty<?> kProperty, V v);
}
