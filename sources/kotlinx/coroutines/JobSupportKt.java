package kotlinx.coroutines;

import kotlinx.coroutines.internal.Symbol;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: JobSupport.kt */
public final class JobSupportKt {
    @NotNull
    public static final Symbol COMPLETING_ALREADY = new Symbol("COMPLETING_ALREADY");
    @NotNull
    public static final Symbol COMPLETING_RETRY = new Symbol("COMPLETING_RETRY");
    @NotNull
    public static final Symbol COMPLETING_WAITING_CHILDREN = new Symbol("COMPLETING_WAITING_CHILDREN");
    @NotNull
    public static final Empty EMPTY_ACTIVE = new Empty(true);
    @NotNull
    public static final Empty EMPTY_NEW = new Empty(false);
    @NotNull
    public static final Symbol SEALED = new Symbol("SEALED");
    @NotNull
    public static final Symbol TOO_LATE_TO_CANCEL = new Symbol("TOO_LATE_TO_CANCEL");

    @Nullable
    public static final Object boxIncomplete(@Nullable Object obj) {
        return obj instanceof Incomplete ? new IncompleteStateBox((Incomplete) obj) : obj;
    }
}
