package kotlin.properties;

import kotlin.reflect.KProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: Delegates.kt */
public final class NotNullVar<T> implements ReadWriteProperty<Object, T> {
    @Nullable
    public T value;

    @NotNull
    public T getValue(@Nullable Object obj, @NotNull KProperty<?> kProperty) {
        T t = this.value;
        if (t != null) {
            return t;
        }
        throw new IllegalStateException("Property " + kProperty.getName() + " should be initialized before get.");
    }

    public void setValue(@Nullable Object obj, @NotNull KProperty<?> kProperty, @NotNull T t) {
        this.value = t;
    }
}
