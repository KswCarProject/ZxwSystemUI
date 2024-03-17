package kotlin.text;

import java.util.Iterator;
import kotlin.Pair;
import kotlin.jvm.functions.Function2;
import kotlin.ranges.IntRange;
import kotlin.sequences.Sequence;
import org.jetbrains.annotations.NotNull;

/* compiled from: Strings.kt */
public final class DelimitedRangesSequence implements Sequence<IntRange> {
    @NotNull
    public final Function2<CharSequence, Integer, Pair<Integer, Integer>> getNextMatch;
    @NotNull
    public final CharSequence input;
    public final int limit;
    public final int startIndex;

    public DelimitedRangesSequence(@NotNull CharSequence charSequence, int i, int i2, @NotNull Function2<? super CharSequence, ? super Integer, Pair<Integer, Integer>> function2) {
        this.input = charSequence;
        this.startIndex = i;
        this.limit = i2;
        this.getNextMatch = function2;
    }

    @NotNull
    public Iterator<IntRange> iterator() {
        return new DelimitedRangesSequence$iterator$1(this);
    }
}
