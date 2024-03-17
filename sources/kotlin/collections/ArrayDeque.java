package kotlin.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt___RangesKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: ArrayDeque.kt */
public final class ArrayDeque<E> extends AbstractMutableList<E> {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    public static final Object[] emptyElementData = new Object[0];
    @NotNull
    public Object[] elementData = emptyElementData;
    public int head;
    public int size;

    public int getSize() {
        return this.size;
    }

    public final void ensureCapacity(int i) {
        if (i >= 0) {
            Object[] objArr = this.elementData;
            if (i > objArr.length) {
                if (objArr == emptyElementData) {
                    this.elementData = new Object[RangesKt___RangesKt.coerceAtLeast(i, 10)];
                } else {
                    copyElements(Companion.newCapacity$kotlin_stdlib(objArr.length, i));
                }
            }
        } else {
            throw new IllegalStateException("Deque is too big.");
        }
    }

    public final void copyElements(int i) {
        Object[] objArr = new Object[i];
        Object[] objArr2 = this.elementData;
        ArraysKt___ArraysJvmKt.copyInto((T[]) objArr2, (T[]) objArr, 0, this.head, objArr2.length);
        Object[] objArr3 = this.elementData;
        int length = objArr3.length;
        int i2 = this.head;
        ArraysKt___ArraysJvmKt.copyInto((T[]) objArr3, (T[]) objArr, length - i2, 0, i2);
        this.head = 0;
        this.elementData = objArr;
    }

    public final int positiveMod(int i) {
        Object[] objArr = this.elementData;
        return i >= objArr.length ? i - objArr.length : i;
    }

    public final int negativeMod(int i) {
        return i < 0 ? i + this.elementData.length : i;
    }

    public final int incremented(int i) {
        if (i == ArraysKt___ArraysKt.getLastIndex(this.elementData)) {
            return 0;
        }
        return i + 1;
    }

    public final int decremented(int i) {
        return i == 0 ? ArraysKt___ArraysKt.getLastIndex(this.elementData) : i - 1;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public final void addFirst(E e) {
        ensureCapacity(size() + 1);
        int decremented = decremented(this.head);
        this.head = decremented;
        this.elementData[decremented] = e;
        this.size = size() + 1;
    }

    public final void addLast(E e) {
        ensureCapacity(size() + 1);
        this.elementData[positiveMod(this.head + size())] = e;
        this.size = size() + 1;
    }

    public final E removeFirst() {
        if (!isEmpty()) {
            E e = this.elementData[this.head];
            Object[] objArr = this.elementData;
            int i = this.head;
            objArr[i] = null;
            this.head = incremented(i);
            this.size = size() - 1;
            return e;
        }
        throw new NoSuchElementException("ArrayDeque is empty.");
    }

    @Nullable
    public final E removeFirstOrNull() {
        if (isEmpty()) {
            return null;
        }
        return removeFirst();
    }

    public final E removeLast() {
        if (!isEmpty()) {
            int access$positiveMod = positiveMod(this.head + CollectionsKt__CollectionsKt.getLastIndex(this));
            E e = this.elementData[access$positiveMod];
            this.elementData[access$positiveMod] = null;
            this.size = size() - 1;
            return e;
        }
        throw new NoSuchElementException("ArrayDeque is empty.");
    }

    public boolean add(E e) {
        addLast(e);
        return true;
    }

    public void add(int i, E e) {
        AbstractList.Companion.checkPositionIndex$kotlin_stdlib(i, size());
        if (i == size()) {
            addLast(e);
        } else if (i == 0) {
            addFirst(e);
        } else {
            ensureCapacity(size() + 1);
            int access$positiveMod = positiveMod(this.head + i);
            if (i < ((size() + 1) >> 1)) {
                int decremented = decremented(access$positiveMod);
                int decremented2 = decremented(this.head);
                int i2 = this.head;
                if (decremented >= i2) {
                    Object[] objArr = this.elementData;
                    objArr[decremented2] = objArr[i2];
                    ArraysKt___ArraysJvmKt.copyInto((T[]) objArr, (T[]) objArr, i2, i2 + 1, decremented + 1);
                } else {
                    Object[] objArr2 = this.elementData;
                    ArraysKt___ArraysJvmKt.copyInto((T[]) objArr2, (T[]) objArr2, i2 - 1, i2, objArr2.length);
                    Object[] objArr3 = this.elementData;
                    objArr3[objArr3.length - 1] = objArr3[0];
                    ArraysKt___ArraysJvmKt.copyInto((T[]) objArr3, (T[]) objArr3, 0, 1, decremented + 1);
                }
                this.elementData[decremented] = e;
                this.head = decremented2;
            } else {
                int access$positiveMod2 = positiveMod(this.head + size());
                if (access$positiveMod < access$positiveMod2) {
                    Object[] objArr4 = this.elementData;
                    ArraysKt___ArraysJvmKt.copyInto((T[]) objArr4, (T[]) objArr4, access$positiveMod + 1, access$positiveMod, access$positiveMod2);
                } else {
                    Object[] objArr5 = this.elementData;
                    ArraysKt___ArraysJvmKt.copyInto((T[]) objArr5, (T[]) objArr5, 1, 0, access$positiveMod2);
                    Object[] objArr6 = this.elementData;
                    objArr6[0] = objArr6[objArr6.length - 1];
                    ArraysKt___ArraysJvmKt.copyInto((T[]) objArr6, (T[]) objArr6, access$positiveMod + 1, access$positiveMod, objArr6.length - 1);
                }
                this.elementData[access$positiveMod] = e;
            }
            this.size = size() + 1;
        }
    }

    public final void copyCollectionElements(int i, Collection<? extends E> collection) {
        Iterator<? extends E> it = collection.iterator();
        int length = this.elementData.length;
        while (i < length) {
            int i2 = i + 1;
            if (!it.hasNext()) {
                break;
            }
            this.elementData[i] = it.next();
            i = i2;
        }
        int i3 = 0;
        int i4 = this.head;
        while (i3 < i4) {
            int i5 = i3 + 1;
            if (!it.hasNext()) {
                break;
            }
            this.elementData[i3] = it.next();
            i3 = i5;
        }
        this.size = size() + collection.size();
    }

    public boolean addAll(@NotNull Collection<? extends E> collection) {
        if (collection.isEmpty()) {
            return false;
        }
        ensureCapacity(size() + collection.size());
        copyCollectionElements(positiveMod(this.head + size()), collection);
        return true;
    }

    public boolean addAll(int i, @NotNull Collection<? extends E> collection) {
        AbstractList.Companion.checkPositionIndex$kotlin_stdlib(i, size());
        if (collection.isEmpty()) {
            return false;
        }
        if (i == size()) {
            return addAll(collection);
        }
        ensureCapacity(size() + collection.size());
        int access$positiveMod = positiveMod(this.head + size());
        int access$positiveMod2 = positiveMod(this.head + i);
        int size2 = collection.size();
        if (i < ((size() + 1) >> 1)) {
            int i2 = this.head;
            int i3 = i2 - size2;
            if (access$positiveMod2 < i2) {
                Object[] objArr = this.elementData;
                ArraysKt___ArraysJvmKt.copyInto((T[]) objArr, (T[]) objArr, i3, i2, objArr.length);
                if (size2 >= access$positiveMod2) {
                    Object[] objArr2 = this.elementData;
                    ArraysKt___ArraysJvmKt.copyInto((T[]) objArr2, (T[]) objArr2, objArr2.length - size2, 0, access$positiveMod2);
                } else {
                    Object[] objArr3 = this.elementData;
                    ArraysKt___ArraysJvmKt.copyInto((T[]) objArr3, (T[]) objArr3, objArr3.length - size2, 0, size2);
                    Object[] objArr4 = this.elementData;
                    ArraysKt___ArraysJvmKt.copyInto((T[]) objArr4, (T[]) objArr4, 0, size2, access$positiveMod2);
                }
            } else if (i3 >= 0) {
                Object[] objArr5 = this.elementData;
                ArraysKt___ArraysJvmKt.copyInto((T[]) objArr5, (T[]) objArr5, i3, i2, access$positiveMod2);
            } else {
                Object[] objArr6 = this.elementData;
                i3 += objArr6.length;
                int i4 = access$positiveMod2 - i2;
                int length = objArr6.length - i3;
                if (length >= i4) {
                    ArraysKt___ArraysJvmKt.copyInto((T[]) objArr6, (T[]) objArr6, i3, i2, access$positiveMod2);
                } else {
                    ArraysKt___ArraysJvmKt.copyInto((T[]) objArr6, (T[]) objArr6, i3, i2, i2 + length);
                    Object[] objArr7 = this.elementData;
                    ArraysKt___ArraysJvmKt.copyInto((T[]) objArr7, (T[]) objArr7, 0, this.head + length, access$positiveMod2);
                }
            }
            this.head = i3;
            copyCollectionElements(negativeMod(access$positiveMod2 - size2), collection);
        } else {
            int i5 = access$positiveMod2 + size2;
            if (access$positiveMod2 < access$positiveMod) {
                int i6 = size2 + access$positiveMod;
                Object[] objArr8 = this.elementData;
                if (i6 <= objArr8.length) {
                    ArraysKt___ArraysJvmKt.copyInto((T[]) objArr8, (T[]) objArr8, i5, access$positiveMod2, access$positiveMod);
                } else if (i5 >= objArr8.length) {
                    ArraysKt___ArraysJvmKt.copyInto((T[]) objArr8, (T[]) objArr8, i5 - objArr8.length, access$positiveMod2, access$positiveMod);
                } else {
                    int length2 = access$positiveMod - (i6 - objArr8.length);
                    ArraysKt___ArraysJvmKt.copyInto((T[]) objArr8, (T[]) objArr8, 0, length2, access$positiveMod);
                    Object[] objArr9 = this.elementData;
                    ArraysKt___ArraysJvmKt.copyInto((T[]) objArr9, (T[]) objArr9, i5, access$positiveMod2, length2);
                }
            } else {
                Object[] objArr10 = this.elementData;
                ArraysKt___ArraysJvmKt.copyInto((T[]) objArr10, (T[]) objArr10, size2, 0, access$positiveMod);
                Object[] objArr11 = this.elementData;
                if (i5 >= objArr11.length) {
                    ArraysKt___ArraysJvmKt.copyInto((T[]) objArr11, (T[]) objArr11, i5 - objArr11.length, access$positiveMod2, objArr11.length);
                } else {
                    ArraysKt___ArraysJvmKt.copyInto((T[]) objArr11, (T[]) objArr11, 0, objArr11.length - size2, objArr11.length);
                    Object[] objArr12 = this.elementData;
                    ArraysKt___ArraysJvmKt.copyInto((T[]) objArr12, (T[]) objArr12, i5, access$positiveMod2, objArr12.length - size2);
                }
            }
            copyCollectionElements(access$positiveMod2, collection);
        }
        return true;
    }

    public E get(int i) {
        AbstractList.Companion.checkElementIndex$kotlin_stdlib(i, size());
        return this.elementData[positiveMod(this.head + i)];
    }

    public E set(int i, E e) {
        AbstractList.Companion.checkElementIndex$kotlin_stdlib(i, size());
        int access$positiveMod = positiveMod(this.head + i);
        E e2 = this.elementData[access$positiveMod];
        this.elementData[access$positiveMod] = e;
        return e2;
    }

    public boolean contains(Object obj) {
        return indexOf(obj) != -1;
    }

    public int indexOf(Object obj) {
        int i;
        int access$positiveMod = positiveMod(this.head + size());
        int i2 = this.head;
        if (i2 < access$positiveMod) {
            while (i2 < access$positiveMod) {
                int i3 = i2 + 1;
                if (Intrinsics.areEqual(obj, this.elementData[i2])) {
                    i = this.head;
                } else {
                    i2 = i3;
                }
            }
            return -1;
        } else if (i2 < access$positiveMod) {
            return -1;
        } else {
            int length = this.elementData.length;
            while (true) {
                if (i2 < length) {
                    int i4 = i2 + 1;
                    if (Intrinsics.areEqual(obj, this.elementData[i2])) {
                        i = this.head;
                        break;
                    }
                    i2 = i4;
                } else {
                    int i5 = 0;
                    while (i5 < access$positiveMod) {
                        int i6 = i5 + 1;
                        if (Intrinsics.areEqual(obj, this.elementData[i5])) {
                            i2 = i5 + this.elementData.length;
                            i = this.head;
                        } else {
                            i5 = i6;
                        }
                    }
                    return -1;
                }
            }
        }
        return i2 - i;
    }

    public int lastIndexOf(Object obj) {
        int i;
        int i2;
        int access$positiveMod = positiveMod(this.head + size());
        int i3 = this.head;
        if (i3 < access$positiveMod) {
            i = access$positiveMod - 1;
            if (i3 > i) {
                return -1;
            }
            while (true) {
                int i4 = i - 1;
                if (Intrinsics.areEqual(obj, this.elementData[i])) {
                    i2 = this.head;
                    break;
                } else if (i == i3) {
                    return -1;
                } else {
                    i = i4;
                }
            }
        } else if (i3 <= access$positiveMod) {
            return -1;
        } else {
            int i5 = access$positiveMod - 1;
            if (i5 >= 0) {
                while (true) {
                    int i6 = i5 - 1;
                    if (Intrinsics.areEqual(obj, this.elementData[i5])) {
                        i = i5 + this.elementData.length;
                        i2 = this.head;
                        break;
                    } else if (i6 < 0) {
                        break;
                    } else {
                        i5 = i6;
                    }
                }
            }
            int lastIndex = ArraysKt___ArraysKt.getLastIndex(this.elementData);
            int i7 = this.head;
            if (i7 > lastIndex) {
                return -1;
            }
            while (true) {
                int i8 = i - 1;
                if (Intrinsics.areEqual(obj, this.elementData[i])) {
                    i2 = this.head;
                    break;
                } else if (i == i7) {
                    return -1;
                } else {
                    lastIndex = i8;
                }
            }
        }
        return i - i2;
    }

    public boolean remove(Object obj) {
        int indexOf = indexOf(obj);
        if (indexOf == -1) {
            return false;
        }
        remove(indexOf);
        return true;
    }

    public E removeAt(int i) {
        AbstractList.Companion.checkElementIndex$kotlin_stdlib(i, size());
        if (i == CollectionsKt__CollectionsKt.getLastIndex(this)) {
            return removeLast();
        }
        if (i == 0) {
            return removeFirst();
        }
        int access$positiveMod = positiveMod(this.head + i);
        E e = this.elementData[access$positiveMod];
        if (i < (size() >> 1)) {
            int i2 = this.head;
            if (access$positiveMod >= i2) {
                Object[] objArr = this.elementData;
                ArraysKt___ArraysJvmKt.copyInto((T[]) objArr, (T[]) objArr, i2 + 1, i2, access$positiveMod);
            } else {
                Object[] objArr2 = this.elementData;
                ArraysKt___ArraysJvmKt.copyInto((T[]) objArr2, (T[]) objArr2, 1, 0, access$positiveMod);
                Object[] objArr3 = this.elementData;
                objArr3[0] = objArr3[objArr3.length - 1];
                int i3 = this.head;
                ArraysKt___ArraysJvmKt.copyInto((T[]) objArr3, (T[]) objArr3, i3 + 1, i3, objArr3.length - 1);
            }
            Object[] objArr4 = this.elementData;
            int i4 = this.head;
            objArr4[i4] = null;
            this.head = incremented(i4);
        } else {
            int access$positiveMod2 = positiveMod(this.head + CollectionsKt__CollectionsKt.getLastIndex(this));
            if (access$positiveMod <= access$positiveMod2) {
                Object[] objArr5 = this.elementData;
                ArraysKt___ArraysJvmKt.copyInto((T[]) objArr5, (T[]) objArr5, access$positiveMod, access$positiveMod + 1, access$positiveMod2 + 1);
            } else {
                Object[] objArr6 = this.elementData;
                ArraysKt___ArraysJvmKt.copyInto((T[]) objArr6, (T[]) objArr6, access$positiveMod, access$positiveMod + 1, objArr6.length);
                Object[] objArr7 = this.elementData;
                objArr7[objArr7.length - 1] = objArr7[0];
                ArraysKt___ArraysJvmKt.copyInto((T[]) objArr7, (T[]) objArr7, 0, 1, access$positiveMod2 + 1);
            }
            this.elementData[access$positiveMod2] = null;
        }
        this.size = size() - 1;
        return e;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v1, resolved type: boolean} */
    /* JADX WARNING: type inference failed for: r1v0 */
    /* JADX WARNING: type inference failed for: r1v2 */
    /* JADX WARNING: type inference failed for: r1v3, types: [int] */
    /* JADX WARNING: type inference failed for: r1v4 */
    /* JADX WARNING: type inference failed for: r1v7 */
    /* JADX WARNING: type inference failed for: r1v9 */
    /* JADX WARNING: type inference failed for: r1v12 */
    /* JADX WARNING: type inference failed for: r1v14 */
    /* JADX WARNING: Failed to insert additional move for type inference */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean removeAll(@org.jetbrains.annotations.NotNull java.util.Collection<? extends java.lang.Object> r12) {
        /*
            r11 = this;
            boolean r0 = r11.isEmpty()
            r1 = 0
            if (r0 != 0) goto L_0x00c0
            java.lang.Object[] r0 = r11.elementData
            int r0 = r0.length
            r2 = 1
            if (r0 != 0) goto L_0x0011
            r0 = r2
            goto L_0x0012
        L_0x0011:
            r0 = r1
        L_0x0012:
            if (r0 == 0) goto L_0x0016
            goto L_0x00c0
        L_0x0016:
            int r0 = r11.size()
            int r3 = r11.head
            int r3 = r3 + r0
            int r0 = r11.positiveMod(r3)
            int r3 = r11.head
            int r4 = r11.head
            r5 = 0
            if (r4 >= r0) goto L_0x0059
            int r4 = r11.head
        L_0x0032:
            if (r4 >= r0) goto L_0x0051
            int r6 = r4 + 1
            java.lang.Object[] r7 = r11.elementData
            r4 = r7[r4]
            boolean r7 = r12.contains(r4)
            r7 = r7 ^ r2
            if (r7 == 0) goto L_0x004e
            java.lang.Object[] r7 = r11.elementData
            int r8 = r3 + 1
            r7[r3] = r4
            r4 = r6
            r3 = r8
            goto L_0x0032
        L_0x004e:
            r1 = r2
            r4 = r6
            goto L_0x0032
        L_0x0051:
            java.lang.Object[] r12 = r11.elementData
            kotlin.collections.ArraysKt___ArraysJvmKt.fill(r12, r5, r3, r0)
            goto L_0x00b2
        L_0x0059:
            int r4 = r11.head
            java.lang.Object[] r6 = r11.elementData
            int r6 = r6.length
            r7 = r1
        L_0x0063:
            if (r4 >= r6) goto L_0x0088
            int r8 = r4 + 1
            java.lang.Object[] r9 = r11.elementData
            r9 = r9[r4]
            java.lang.Object[] r10 = r11.elementData
            r10[r4] = r5
            boolean r4 = r12.contains(r9)
            r4 = r4 ^ r2
            if (r4 == 0) goto L_0x0085
            java.lang.Object[] r4 = r11.elementData
            int r10 = r3 + 1
            r4[r3] = r9
            r4 = r8
            r3 = r10
            goto L_0x0063
        L_0x0085:
            r7 = r2
            r4 = r8
            goto L_0x0063
        L_0x0088:
            int r3 = r11.positiveMod(r3)
        L_0x008c:
            if (r1 >= r0) goto L_0x00b1
            int r4 = r1 + 1
            java.lang.Object[] r6 = r11.elementData
            r6 = r6[r1]
            java.lang.Object[] r8 = r11.elementData
            r8[r1] = r5
            boolean r1 = r12.contains(r6)
            r1 = r1 ^ r2
            if (r1 == 0) goto L_0x00ae
            java.lang.Object[] r1 = r11.elementData
            r1[r3] = r6
            int r3 = r11.incremented(r3)
            goto L_0x00af
        L_0x00ae:
            r7 = r2
        L_0x00af:
            r1 = r4
            goto L_0x008c
        L_0x00b1:
            r1 = r7
        L_0x00b2:
            if (r1 == 0) goto L_0x00c0
            int r12 = r11.head
            int r3 = r3 - r12
            int r12 = r11.negativeMod(r3)
            r11.size = r12
        L_0x00c0:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.collections.ArrayDeque.removeAll(java.util.Collection):boolean");
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v1, resolved type: boolean} */
    /* JADX WARNING: type inference failed for: r1v0 */
    /* JADX WARNING: type inference failed for: r1v2 */
    /* JADX WARNING: type inference failed for: r1v3, types: [int] */
    /* JADX WARNING: type inference failed for: r1v4 */
    /* JADX WARNING: type inference failed for: r1v6 */
    /* JADX WARNING: type inference failed for: r1v8 */
    /* JADX WARNING: type inference failed for: r1v11 */
    /* JADX WARNING: type inference failed for: r1v13 */
    /* JADX WARNING: Failed to insert additional move for type inference */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean retainAll(@org.jetbrains.annotations.NotNull java.util.Collection<? extends java.lang.Object> r12) {
        /*
            r11 = this;
            boolean r0 = r11.isEmpty()
            r1 = 0
            if (r0 != 0) goto L_0x00bd
            java.lang.Object[] r0 = r11.elementData
            int r0 = r0.length
            r2 = 1
            if (r0 != 0) goto L_0x0011
            r0 = r2
            goto L_0x0012
        L_0x0011:
            r0 = r1
        L_0x0012:
            if (r0 == 0) goto L_0x0016
            goto L_0x00bd
        L_0x0016:
            int r0 = r11.size()
            int r3 = r11.head
            int r3 = r3 + r0
            int r0 = r11.positiveMod(r3)
            int r3 = r11.head
            int r4 = r11.head
            r5 = 0
            if (r4 >= r0) goto L_0x0058
            int r4 = r11.head
        L_0x0032:
            if (r4 >= r0) goto L_0x0050
            int r6 = r4 + 1
            java.lang.Object[] r7 = r11.elementData
            r4 = r7[r4]
            boolean r7 = r12.contains(r4)
            if (r7 == 0) goto L_0x004d
            java.lang.Object[] r7 = r11.elementData
            int r8 = r3 + 1
            r7[r3] = r4
            r4 = r6
            r3 = r8
            goto L_0x0032
        L_0x004d:
            r1 = r2
            r4 = r6
            goto L_0x0032
        L_0x0050:
            java.lang.Object[] r12 = r11.elementData
            kotlin.collections.ArraysKt___ArraysJvmKt.fill(r12, r5, r3, r0)
            goto L_0x00af
        L_0x0058:
            int r4 = r11.head
            java.lang.Object[] r6 = r11.elementData
            int r6 = r6.length
            r7 = r1
        L_0x0062:
            if (r4 >= r6) goto L_0x0086
            int r8 = r4 + 1
            java.lang.Object[] r9 = r11.elementData
            r9 = r9[r4]
            java.lang.Object[] r10 = r11.elementData
            r10[r4] = r5
            boolean r4 = r12.contains(r9)
            if (r4 == 0) goto L_0x0083
            java.lang.Object[] r4 = r11.elementData
            int r10 = r3 + 1
            r4[r3] = r9
            r4 = r8
            r3 = r10
            goto L_0x0062
        L_0x0083:
            r7 = r2
            r4 = r8
            goto L_0x0062
        L_0x0086:
            int r3 = r11.positiveMod(r3)
        L_0x008a:
            if (r1 >= r0) goto L_0x00ae
            int r4 = r1 + 1
            java.lang.Object[] r6 = r11.elementData
            r6 = r6[r1]
            java.lang.Object[] r8 = r11.elementData
            r8[r1] = r5
            boolean r1 = r12.contains(r6)
            if (r1 == 0) goto L_0x00ab
            java.lang.Object[] r1 = r11.elementData
            r1[r3] = r6
            int r3 = r11.incremented(r3)
            goto L_0x00ac
        L_0x00ab:
            r7 = r2
        L_0x00ac:
            r1 = r4
            goto L_0x008a
        L_0x00ae:
            r1 = r7
        L_0x00af:
            if (r1 == 0) goto L_0x00bd
            int r12 = r11.head
            int r3 = r3 - r12
            int r12 = r11.negativeMod(r3)
            r11.size = r12
        L_0x00bd:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.collections.ArrayDeque.retainAll(java.util.Collection):boolean");
    }

    public void clear() {
        int access$positiveMod = positiveMod(this.head + size());
        int i = this.head;
        if (i < access$positiveMod) {
            ArraysKt___ArraysJvmKt.fill(this.elementData, null, i, access$positiveMod);
        } else if (!isEmpty()) {
            Object[] objArr = this.elementData;
            ArraysKt___ArraysJvmKt.fill(objArr, null, this.head, objArr.length);
            ArraysKt___ArraysJvmKt.fill(this.elementData, null, 0, access$positiveMod);
        }
        this.head = 0;
        this.size = 0;
    }

    @NotNull
    public <T> T[] toArray(@NotNull T[] tArr) {
        if (tArr.length < size()) {
            tArr = ArraysKt__ArraysJVMKt.arrayOfNulls(tArr, size());
        }
        int access$positiveMod = positiveMod(this.head + size());
        int i = this.head;
        if (i < access$positiveMod) {
            ArraysKt___ArraysJvmKt.copyInto$default(this.elementData, (Object[]) tArr, 0, i, access$positiveMod, 2, (Object) null);
        } else if (!isEmpty()) {
            Object[] objArr = this.elementData;
            ArraysKt___ArraysJvmKt.copyInto((T[]) objArr, tArr, 0, this.head, objArr.length);
            Object[] objArr2 = this.elementData;
            ArraysKt___ArraysJvmKt.copyInto((T[]) objArr2, tArr, objArr2.length - this.head, 0, access$positiveMod);
        }
        if (tArr.length > size()) {
            tArr[size()] = null;
        }
        return tArr;
    }

    @NotNull
    public Object[] toArray() {
        return toArray(new Object[size()]);
    }

    /* compiled from: ArrayDeque.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final int newCapacity$kotlin_stdlib(int i, int i2) {
            int i3 = i + (i >> 1);
            if (i3 - i2 < 0) {
                i3 = i2;
            }
            int i4 = 2147483639;
            if (i3 - 2147483639 <= 0) {
                return i3;
            }
            if (i2 > 2147483639) {
                i4 = Integer.MAX_VALUE;
            }
            return i4;
        }

        public Companion() {
        }
    }
}
