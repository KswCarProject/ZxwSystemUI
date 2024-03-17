package kotlinx.coroutines;

import org.jetbrains.annotations.NotNull;

/* compiled from: JobSupport.kt */
public final class IncompleteStateBox {
    @NotNull
    public final Incomplete state;

    public IncompleteStateBox(@NotNull Incomplete incomplete) {
        this.state = incomplete;
    }
}
