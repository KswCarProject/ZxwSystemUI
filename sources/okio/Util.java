package okio;

import org.jetbrains.annotations.NotNull;

/* renamed from: okio.-Util  reason: invalid class name */
/* compiled from: -Util.kt */
public final class Util {
    public static final void checkOffsetAndCount(long j, long j2, long j3) {
        if ((j2 | j3) < 0 || j2 > j || j - j2 < j3) {
            throw new ArrayIndexOutOfBoundsException("size=" + j + " offset=" + j2 + " byteCount=" + j3);
        }
    }

    public static final boolean arrayRangeEquals(@NotNull byte[] bArr, int i, @NotNull byte[] bArr2, int i2, int i3) {
        int i4 = 0;
        while (i4 < i3) {
            int i5 = i4 + 1;
            if (bArr[i4 + i] != bArr2[i4 + i2]) {
                return false;
            }
            i4 = i5;
        }
        return true;
    }
}
