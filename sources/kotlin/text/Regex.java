package kotlin.text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kotlin.collections.CollectionsKt__CollectionsJVMKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.ranges.RangesKt___RangesKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: Regex.kt */
public final class Regex implements Serializable {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @Nullable
    private Set<Object> _options;
    @NotNull
    private final Pattern nativePattern;

    public Regex(@NotNull Pattern pattern) {
        this.nativePattern = pattern;
    }

    public Regex(@NotNull String str) {
        this(Pattern.compile(str));
    }

    public final boolean matches(@NotNull CharSequence charSequence) {
        return this.nativePattern.matcher(charSequence).matches();
    }

    @NotNull
    public final String replace(@NotNull CharSequence charSequence, @NotNull String str) {
        return this.nativePattern.matcher(charSequence).replaceAll(str);
    }

    @NotNull
    public final List<String> split(@NotNull CharSequence charSequence, int i) {
        StringsKt__StringsKt.requireNonNegativeLimit(i);
        Matcher matcher = this.nativePattern.matcher(charSequence);
        if (i == 1 || !matcher.find()) {
            return CollectionsKt__CollectionsJVMKt.listOf(charSequence.toString());
        }
        int i2 = 10;
        if (i > 0) {
            i2 = RangesKt___RangesKt.coerceAtMost(i, 10);
        }
        ArrayList arrayList = new ArrayList(i2);
        int i3 = 0;
        int i4 = i - 1;
        do {
            arrayList.add(charSequence.subSequence(i3, matcher.start()).toString());
            i3 = matcher.end();
            if ((i4 >= 0 && arrayList.size() == i4) || !matcher.find()) {
                arrayList.add(charSequence.subSequence(i3, charSequence.length()).toString());
            }
            arrayList.add(charSequence.subSequence(i3, matcher.start()).toString());
            i3 = matcher.end();
            break;
        } while (!matcher.find());
        arrayList.add(charSequence.subSequence(i3, charSequence.length()).toString());
        return arrayList;
    }

    @NotNull
    public String toString() {
        return this.nativePattern.toString();
    }

    private final Object writeReplace() {
        return new Serialized(this.nativePattern.pattern(), this.nativePattern.flags());
    }

    /* compiled from: Regex.kt */
    public static final class Serialized implements Serializable {
        @NotNull
        public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
        private static final long serialVersionUID = 0;
        private final int flags;
        @NotNull
        private final String pattern;

        /* compiled from: Regex.kt */
        public static final class Companion {
            public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }

            public Companion() {
            }
        }

        public Serialized(@NotNull String str, int i) {
            this.pattern = str;
            this.flags = i;
        }

        private final Object readResolve() {
            return new Regex(Pattern.compile(this.pattern, this.flags));
        }
    }

    /* compiled from: Regex.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }
}
