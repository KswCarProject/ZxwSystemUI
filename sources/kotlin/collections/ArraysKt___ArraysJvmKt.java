package kotlin.collections;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/* compiled from: _ArraysJvm.kt */
public class ArraysKt___ArraysJvmKt extends ArraysKt__ArraysKt {
    @NotNull
    public static final <T> List<T> asList(@NotNull T[] tArr) {
        return ArraysUtilJVM.asList(tArr);
    }

    public static /* synthetic */ Object[] copyInto$default(Object[] objArr, Object[] objArr2, int i, int i2, int i3, int i4, Object obj) {
        if ((i4 & 2) != 0) {
            i = 0;
        }
        if ((i4 & 4) != 0) {
            i2 = 0;
        }
        if ((i4 & 8) != 0) {
            i3 = objArr.length;
        }
        return copyInto((T[]) objArr, (T[]) objArr2, i, i2, i3);
    }

    @NotNull
    public static final <T> T[] copyInto(@NotNull T[] tArr, @NotNull T[] tArr2, int i, int i2, int i3) {
        System.arraycopy(tArr, i2, tArr2, i, i3 - i2);
        return tArr2;
    }

    public static /* synthetic */ byte[] copyInto$default(byte[] bArr, byte[] bArr2, int i, int i2, int i3, int i4, Object obj) {
        if ((i4 & 2) != 0) {
            i = 0;
        }
        if ((i4 & 4) != 0) {
            i2 = 0;
        }
        if ((i4 & 8) != 0) {
            i3 = bArr.length;
        }
        return copyInto(bArr, bArr2, i, i2, i3);
    }

    @NotNull
    public static final byte[] copyInto(@NotNull byte[] bArr, @NotNull byte[] bArr2, int i, int i2, int i3) {
        System.arraycopy(bArr, i2, bArr2, i, i3 - i2);
        return bArr2;
    }

    public static /* synthetic */ float[] copyInto$default(float[] fArr, float[] fArr2, int i, int i2, int i3, int i4, Object obj) {
        if ((i4 & 2) != 0) {
            i = 0;
        }
        if ((i4 & 4) != 0) {
            i2 = 0;
        }
        if ((i4 & 8) != 0) {
            i3 = fArr.length;
        }
        return copyInto(fArr, fArr2, i, i2, i3);
    }

    @NotNull
    public static final float[] copyInto(@NotNull float[] fArr, @NotNull float[] fArr2, int i, int i2, int i3) {
        System.arraycopy(fArr, i2, fArr2, i, i3 - i2);
        return fArr2;
    }

    @NotNull
    public static final byte[] copyOfRange(@NotNull byte[] bArr, int i, int i2) {
        ArraysKt__ArraysJVMKt.copyOfRangeToIndexCheck(i2, bArr.length);
        return Arrays.copyOfRange(bArr, i, i2);
    }

    public static final <T> void fill(@NotNull T[] tArr, T t, int i, int i2) {
        Arrays.fill(tArr, i, i2, t);
    }

    @NotNull
    public static final int[] plus(@NotNull int[] iArr, @NotNull int[] iArr2) {
        int length = iArr.length;
        int length2 = iArr2.length;
        int[] copyOf = Arrays.copyOf(iArr, length + length2);
        System.arraycopy(iArr2, 0, copyOf, length, length2);
        return copyOf;
    }

    public static final <T> void sort(@NotNull T[] tArr) {
        if (tArr.length > 1) {
            Arrays.sort(tArr);
        }
    }

    public static final <T> void sortWith(@NotNull T[] tArr, @NotNull Comparator<? super T> comparator) {
        if (tArr.length > 1) {
            Arrays.sort(tArr, comparator);
        }
    }
}
