package kotlin.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt___RangesKt;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt__SequencesKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: _Arrays.kt */
public class ArraysKt___ArraysKt extends ArraysKt___ArraysJvmKt {
    public static final <T> boolean contains(@NotNull T[] tArr, T t) {
        return indexOf(tArr, t) >= 0;
    }

    public static final boolean contains(@NotNull int[] iArr, int i) {
        return indexOf(iArr, i) >= 0;
    }

    public static final <T> int indexOf(@NotNull T[] tArr, T t) {
        int i = 0;
        if (t == null) {
            int length = tArr.length;
            while (i < length) {
                int i2 = i + 1;
                if (tArr[i] == null) {
                    return i;
                }
                i = i2;
            }
            return -1;
        }
        int length2 = tArr.length;
        while (i < length2) {
            int i3 = i + 1;
            if (Intrinsics.areEqual((Object) t, (Object) tArr[i])) {
                return i;
            }
            i = i3;
        }
        return -1;
    }

    public static final int indexOf(@NotNull int[] iArr, int i) {
        int length = iArr.length;
        int i2 = 0;
        while (i2 < length) {
            int i3 = i2 + 1;
            if (i == iArr[i2]) {
                return i2;
            }
            i2 = i3;
        }
        return -1;
    }

    public static final char single(@NotNull char[] cArr) {
        int length = cArr.length;
        if (length == 0) {
            throw new NoSuchElementException("Array is empty.");
        } else if (length == 1) {
            return cArr[0];
        } else {
            throw new IllegalArgumentException("Array has more than one element.");
        }
    }

    @Nullable
    public static final <T> T singleOrNull(@NotNull T[] tArr) {
        if (tArr.length == 1) {
            return tArr[0];
        }
        return null;
    }

    @NotNull
    public static final <T> List<T> drop(@NotNull T[] tArr, int i) {
        if (i >= 0) {
            return takeLast(tArr, RangesKt___RangesKt.coerceAtLeast(tArr.length - i, 0));
        }
        throw new IllegalArgumentException(("Requested element count " + i + " is less than zero.").toString());
    }

    @NotNull
    public static final <T> List<T> filterNotNull(@NotNull T[] tArr) {
        return (List) filterNotNullTo(tArr, new ArrayList());
    }

    @NotNull
    public static final <C extends Collection<? super T>, T> C filterNotNullTo(@NotNull T[] tArr, @NotNull C c) {
        int length = tArr.length;
        int i = 0;
        while (i < length) {
            T t = tArr[i];
            i++;
            if (t != null) {
                c.add(t);
            }
        }
        return c;
    }

    @NotNull
    public static final <T> List<T> takeLast(@NotNull T[] tArr, int i) {
        if (!(i >= 0)) {
            throw new IllegalArgumentException(("Requested element count " + i + " is less than zero.").toString());
        } else if (i == 0) {
            return CollectionsKt__CollectionsKt.emptyList();
        } else {
            int length = tArr.length;
            if (i >= length) {
                return toList(tArr);
            }
            if (i == 1) {
                return CollectionsKt__CollectionsJVMKt.listOf(tArr[length - 1]);
            }
            ArrayList arrayList = new ArrayList(i);
            for (int i2 = length - i; i2 < length; i2++) {
                arrayList.add(tArr[i2]);
            }
            return arrayList;
        }
    }

    @NotNull
    public static final <T> T[] sortedArrayWith(@NotNull T[] tArr, @NotNull Comparator<? super T> comparator) {
        if (tArr.length == 0) {
            return tArr;
        }
        T[] copyOf = Arrays.copyOf(tArr, tArr.length);
        ArraysKt___ArraysJvmKt.sortWith(copyOf, comparator);
        return copyOf;
    }

    @NotNull
    public static final <T> List<T> sortedWith(@NotNull T[] tArr, @NotNull Comparator<? super T> comparator) {
        return ArraysKt___ArraysJvmKt.asList(sortedArrayWith(tArr, comparator));
    }

    public static final <T> int getLastIndex(@NotNull T[] tArr) {
        return tArr.length - 1;
    }

    @NotNull
    public static final <T, C extends Collection<? super T>> C toCollection(@NotNull T[] tArr, @NotNull C c) {
        int length = tArr.length;
        int i = 0;
        while (i < length) {
            T t = tArr[i];
            i++;
            c.add(t);
        }
        return c;
    }

    @NotNull
    public static final <T> List<T> toList(@NotNull T[] tArr) {
        int length = tArr.length;
        if (length == 0) {
            return CollectionsKt__CollectionsKt.emptyList();
        }
        if (length != 1) {
            return toMutableList(tArr);
        }
        return CollectionsKt__CollectionsJVMKt.listOf(tArr[0]);
    }

    @NotNull
    public static final List<Integer> toList(@NotNull int[] iArr) {
        int length = iArr.length;
        if (length == 0) {
            return CollectionsKt__CollectionsKt.emptyList();
        }
        if (length != 1) {
            return toMutableList(iArr);
        }
        return CollectionsKt__CollectionsJVMKt.listOf(Integer.valueOf(iArr[0]));
    }

    @NotNull
    public static final <T> List<T> toMutableList(@NotNull T[] tArr) {
        return new ArrayList(CollectionsKt__CollectionsKt.asCollection(tArr));
    }

    @NotNull
    public static final List<Integer> toMutableList(@NotNull int[] iArr) {
        ArrayList arrayList = new ArrayList(iArr.length);
        int length = iArr.length;
        int i = 0;
        while (i < length) {
            int i2 = iArr[i];
            i++;
            arrayList.add(Integer.valueOf(i2));
        }
        return arrayList;
    }

    @NotNull
    public static final <T> Set<T> toSet(@NotNull T[] tArr) {
        int length = tArr.length;
        if (length == 0) {
            return SetsKt__SetsKt.emptySet();
        }
        if (length != 1) {
            return (Set) toCollection(tArr, new LinkedHashSet(MapsKt__MapsJVMKt.mapCapacity(tArr.length)));
        }
        return SetsKt__SetsJVMKt.setOf(tArr[0]);
    }

    @NotNull
    public static final <T> Sequence<T> asSequence(@NotNull T[] tArr) {
        if (tArr.length == 0) {
            return SequencesKt__SequencesKt.emptySequence();
        }
        return new ArraysKt___ArraysKt$asSequence$$inlined$Sequence$1(tArr);
    }
}
