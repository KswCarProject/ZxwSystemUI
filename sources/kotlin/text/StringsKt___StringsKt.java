package kotlin.text;

import kotlin.ranges.RangesKt___RangesKt;
import org.jetbrains.annotations.NotNull;

/* compiled from: _Strings.kt */
public class StringsKt___StringsKt extends StringsKt___StringsJvmKt {
    @NotNull
    public static final String drop(@NotNull String str, int i) {
        if (i >= 0) {
            return str.substring(RangesKt___RangesKt.coerceAtMost(i, str.length()));
        }
        throw new IllegalArgumentException(("Requested character count " + i + " is less than zero.").toString());
    }
}
