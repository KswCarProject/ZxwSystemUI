package kotlin.collections;

import java.lang.reflect.Array;
import org.jetbrains.annotations.NotNull;

/* compiled from: ArraysJVM.kt */
public class ArraysKt__ArraysJVMKt {
    @NotNull
    public static final <T> T[] arrayOfNulls(@NotNull T[] tArr, int i) {
        Object newInstance = Array.newInstance(tArr.getClass().getComponentType(), i);
        if (newInstance != null) {
            return (Object[]) newInstance;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.arrayOfNulls>");
    }

    public static final void copyOfRangeToIndexCheck(int i, int i2) {
        if (i > i2) {
            throw new IndexOutOfBoundsException("toIndex (" + i + ") is greater than size (" + i2 + ").");
        }
    }
}
