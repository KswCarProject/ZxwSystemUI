package kotlin.sequences;

import java.util.Iterator;
import kotlin.collections.EmptyIterator;
import org.jetbrains.annotations.NotNull;

/* compiled from: Sequences.kt */
public final class EmptySequence implements Sequence, DropTakeSequence {
    @NotNull
    public static final EmptySequence INSTANCE = new EmptySequence();

    @NotNull
    public Iterator iterator() {
        return EmptyIterator.INSTANCE;
    }

    @NotNull
    public EmptySequence take(int i) {
        return INSTANCE;
    }
}
