package kotlin.sequences;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import kotlin.Pair;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.text.StringsKt__AppendableKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: _Sequences.kt */
public class SequencesKt___SequencesKt extends SequencesKt___SequencesJvmKt {
    public static final <T> T elementAtOrElse(@NotNull Sequence<? extends T> sequence, int i, @NotNull Function1<? super Integer, ? extends T> function1) {
        if (i < 0) {
            return function1.invoke(Integer.valueOf(i));
        }
        int i2 = 0;
        for (T next : sequence) {
            int i3 = i2 + 1;
            if (i == i2) {
                return next;
            }
            i2 = i3;
        }
        return function1.invoke(Integer.valueOf(i));
    }

    @Nullable
    public static final <T> T firstOrNull(@NotNull Sequence<? extends T> sequence) {
        Iterator<? extends T> it = sequence.iterator();
        if (!it.hasNext()) {
            return null;
        }
        return it.next();
    }

    public static final <T> T last(@NotNull Sequence<? extends T> sequence) {
        Iterator<? extends T> it = sequence.iterator();
        if (it.hasNext()) {
            T next = it.next();
            while (it.hasNext()) {
                next = it.next();
            }
            return next;
        }
        throw new NoSuchElementException("Sequence is empty.");
    }

    @NotNull
    public static final <T> Sequence<T> filter(@NotNull Sequence<? extends T> sequence, @NotNull Function1<? super T, Boolean> function1) {
        return new FilteringSequence(sequence, true, function1);
    }

    @NotNull
    public static final <T> Sequence<T> filterNot(@NotNull Sequence<? extends T> sequence, @NotNull Function1<? super T, Boolean> function1) {
        return new FilteringSequence(sequence, false, function1);
    }

    @NotNull
    public static final <T> Sequence<T> filterNotNull(@NotNull Sequence<? extends T> sequence) {
        return filterNot(sequence, SequencesKt___SequencesKt$filterNotNull$1.INSTANCE);
    }

    @NotNull
    public static final <T> Sequence<T> take(@NotNull Sequence<? extends T> sequence, int i) {
        if (!(i >= 0)) {
            throw new IllegalArgumentException(("Requested element count " + i + " is less than zero.").toString());
        } else if (i == 0) {
            return SequencesKt__SequencesKt.emptySequence();
        } else {
            if (sequence instanceof DropTakeSequence) {
                return ((DropTakeSequence) sequence).take(i);
            }
            return new TakeSequence(sequence, i);
        }
    }

    @NotNull
    public static final <T> Sequence<T> takeWhile(@NotNull Sequence<? extends T> sequence, @NotNull Function1<? super T, Boolean> function1) {
        return new TakeWhileSequence(sequence, function1);
    }

    @NotNull
    public static final <T> Sequence<T> sortedWith(@NotNull Sequence<? extends T> sequence, @NotNull Comparator<? super T> comparator) {
        return new SequencesKt___SequencesKt$sortedWith$1(sequence, comparator);
    }

    @NotNull
    public static final <T, C extends Collection<? super T>> C toCollection(@NotNull Sequence<? extends T> sequence, @NotNull C c) {
        for (Object add : sequence) {
            c.add(add);
        }
        return c;
    }

    @NotNull
    public static final <T> List<T> toList(@NotNull Sequence<? extends T> sequence) {
        return CollectionsKt__CollectionsKt.optimizeReadOnlyList(toMutableList(sequence));
    }

    @NotNull
    public static final <T> List<T> toMutableList(@NotNull Sequence<? extends T> sequence) {
        return (List) toCollection(sequence, new ArrayList());
    }

    @NotNull
    public static final <T, R> Sequence<R> flatMap(@NotNull Sequence<? extends T> sequence, @NotNull Function1<? super T, ? extends Sequence<? extends R>> function1) {
        return new FlatteningSequence(sequence, function1, SequencesKt___SequencesKt$flatMap$2.INSTANCE);
    }

    @NotNull
    public static final <T, R> Sequence<R> map(@NotNull Sequence<? extends T> sequence, @NotNull Function1<? super T, ? extends R> function1) {
        return new TransformingSequence(sequence, function1);
    }

    @NotNull
    public static final <T, R> Sequence<R> mapIndexed(@NotNull Sequence<? extends T> sequence, @NotNull Function2<? super Integer, ? super T, ? extends R> function2) {
        return new TransformingIndexedSequence(sequence, function2);
    }

    @NotNull
    public static final <T, R> Sequence<R> mapNotNull(@NotNull Sequence<? extends T> sequence, @NotNull Function1<? super T, ? extends R> function1) {
        return filterNotNull(new TransformingSequence(sequence, function1));
    }

    @NotNull
    public static final <T> Sequence<T> distinct(@NotNull Sequence<? extends T> sequence) {
        return distinctBy(sequence, SequencesKt___SequencesKt$distinct$1.INSTANCE);
    }

    @NotNull
    public static final <T, K> Sequence<T> distinctBy(@NotNull Sequence<? extends T> sequence, @NotNull Function1<? super T, ? extends K> function1) {
        return new DistinctSequence(sequence, function1);
    }

    public static final <T> int count(@NotNull Sequence<? extends T> sequence) {
        Iterator<? extends T> it = sequence.iterator();
        int i = 0;
        while (it.hasNext()) {
            it.next();
            i++;
            if (i < 0) {
                CollectionsKt__CollectionsKt.throwCountOverflow();
            }
        }
        return i;
    }

    @NotNull
    public static final <T> Sequence<T> plus(@NotNull Sequence<? extends T> sequence, @NotNull Sequence<? extends T> sequence2) {
        return SequencesKt__SequencesKt.flatten(SequencesKt__SequencesKt.sequenceOf(sequence, sequence2));
    }

    @NotNull
    public static final <T, R> Sequence<Pair<T, R>> zip(@NotNull Sequence<? extends T> sequence, @NotNull Sequence<? extends R> sequence2) {
        return new MergingSequence(sequence, sequence2, SequencesKt___SequencesKt$zip$1.INSTANCE);
    }

    @NotNull
    public static final <T, A extends Appendable> A joinTo(@NotNull Sequence<? extends T> sequence, @NotNull A a, @NotNull CharSequence charSequence, @NotNull CharSequence charSequence2, @NotNull CharSequence charSequence3, int i, @NotNull CharSequence charSequence4, @Nullable Function1<? super T, ? extends CharSequence> function1) {
        a.append(charSequence2);
        int i2 = 0;
        for (Object next : sequence) {
            i2++;
            if (i2 > 1) {
                a.append(charSequence);
            }
            if (i >= 0 && i2 > i) {
                break;
            }
            StringsKt__AppendableKt.appendElement(a, next, function1);
        }
        if (i >= 0 && i2 > i) {
            a.append(charSequence4);
        }
        a.append(charSequence3);
        return a;
    }

    public static /* synthetic */ String joinToString$default(Sequence sequence, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, int i, CharSequence charSequence4, Function1 function1, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            charSequence = ", ";
        }
        CharSequence charSequence5 = "";
        CharSequence charSequence6 = (i2 & 2) != 0 ? charSequence5 : charSequence2;
        if ((i2 & 4) == 0) {
            charSequence5 = charSequence3;
        }
        if ((i2 & 8) != 0) {
            i = -1;
        }
        int i3 = i;
        if ((i2 & 16) != 0) {
            charSequence4 = "...";
        }
        CharSequence charSequence7 = charSequence4;
        if ((i2 & 32) != 0) {
            function1 = null;
        }
        return joinToString(sequence, charSequence, charSequence6, charSequence5, i3, charSequence7, function1);
    }

    @NotNull
    public static final <T> String joinToString(@NotNull Sequence<? extends T> sequence, @NotNull CharSequence charSequence, @NotNull CharSequence charSequence2, @NotNull CharSequence charSequence3, int i, @NotNull CharSequence charSequence4, @Nullable Function1<? super T, ? extends CharSequence> function1) {
        return ((StringBuilder) joinTo(sequence, new StringBuilder(), charSequence, charSequence2, charSequence3, i, charSequence4, function1)).toString();
    }

    @NotNull
    public static final <T> Iterable<T> asIterable(@NotNull Sequence<? extends T> sequence) {
        return new SequencesKt___SequencesKt$asIterable$$inlined$Iterable$1(sequence);
    }
}
