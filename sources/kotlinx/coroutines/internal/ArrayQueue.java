package kotlinx.coroutines.internal;

import kotlin.collections.ArraysKt___ArraysJvmKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: ArrayQueue.kt */
public class ArrayQueue<T> {
    @NotNull
    public Object[] elements = new Object[16];
    public int head;
    public int tail;

    public final boolean isEmpty() {
        return this.head == this.tail;
    }

    public final void addLast(@NotNull T t) {
        Object[] objArr = this.elements;
        int i = this.tail;
        objArr[i] = t;
        int length = (objArr.length - 1) & (i + 1);
        this.tail = length;
        if (length == this.head) {
            ensureCapacity();
        }
    }

    @Nullable
    public final T removeFirstOrNull() {
        int i = this.head;
        if (i == this.tail) {
            return null;
        }
        T[] tArr = this.elements;
        T t = tArr[i];
        tArr[i] = null;
        this.head = (i + 1) & (tArr.length - 1);
        if (t != null) {
            return t;
        }
        throw new NullPointerException("null cannot be cast to non-null type T of kotlinx.coroutines.internal.ArrayQueue");
    }

    public final void ensureCapacity() {
        Object[] objArr = this.elements;
        int length = objArr.length;
        Object[] objArr2 = new Object[(length << 1)];
        Object[] objArr3 = objArr2;
        ArraysKt___ArraysJvmKt.copyInto$default(objArr, objArr3, 0, this.head, 0, 10, (Object) null);
        Object[] objArr4 = this.elements;
        int length2 = objArr4.length;
        int i = this.head;
        ArraysKt___ArraysJvmKt.copyInto$default(objArr4, objArr2, length2 - i, 0, i, 4, (Object) null);
        this.elements = objArr3;
        this.head = 0;
        this.tail = length;
    }
}
