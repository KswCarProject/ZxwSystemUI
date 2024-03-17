package kotlin.properties;

import org.jetbrains.annotations.NotNull;

/* compiled from: Delegates.kt */
public final class Delegates {
    @NotNull
    public static final Delegates INSTANCE = new Delegates();

    @NotNull
    public final <T> ReadWriteProperty<Object, T> notNull() {
        return new NotNullVar();
    }
}
