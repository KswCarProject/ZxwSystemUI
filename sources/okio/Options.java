package okio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.RandomAccess;
import kotlin.collections.AbstractList;
import kotlin.collections.ArraysKt___ArraysKt;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt__MutableCollectionsJVMKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: Options.kt */
public final class Options extends AbstractList<ByteString> implements RandomAccess {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    public final ByteString[] byteStrings;
    @NotNull
    public final int[] trie;

    public /* synthetic */ Options(ByteString[] byteStringArr, int[] iArr, DefaultConstructorMarker defaultConstructorMarker) {
        this(byteStringArr, iArr);
    }

    @NotNull
    public static final Options of(@NotNull ByteString... byteStringArr) {
        return Companion.of(byteStringArr);
    }

    public final /* bridge */ boolean contains(Object obj) {
        if (!(obj instanceof ByteString)) {
            return false;
        }
        return contains((ByteString) obj);
    }

    public /* bridge */ boolean contains(ByteString byteString) {
        return super.contains(byteString);
    }

    public final /* bridge */ int indexOf(Object obj) {
        if (!(obj instanceof ByteString)) {
            return -1;
        }
        return indexOf((ByteString) obj);
    }

    public /* bridge */ int indexOf(ByteString byteString) {
        return super.indexOf(byteString);
    }

    public final /* bridge */ int lastIndexOf(Object obj) {
        if (!(obj instanceof ByteString)) {
            return -1;
        }
        return lastIndexOf((ByteString) obj);
    }

    public /* bridge */ int lastIndexOf(ByteString byteString) {
        return super.lastIndexOf(byteString);
    }

    @NotNull
    public final ByteString[] getByteStrings$external__okio__android_common__okio_lib() {
        return this.byteStrings;
    }

    @NotNull
    public final int[] getTrie$external__okio__android_common__okio_lib() {
        return this.trie;
    }

    public Options(ByteString[] byteStringArr, int[] iArr) {
        this.byteStrings = byteStringArr;
        this.trie = iArr;
    }

    public int getSize() {
        return this.byteStrings.length;
    }

    @NotNull
    public ByteString get(int i) {
        return this.byteStrings[i];
    }

    /* compiled from: Options.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }

        @NotNull
        public final Options of(@NotNull ByteString... byteStringArr) {
            ByteString[] byteStringArr2 = byteStringArr;
            int i = 0;
            if (byteStringArr2.length == 0) {
                return new Options(new ByteString[0], new int[]{0, -1}, (DefaultConstructorMarker) null);
            }
            List mutableList = ArraysKt___ArraysKt.toMutableList((T[]) byteStringArr);
            CollectionsKt__MutableCollectionsJVMKt.sort(mutableList);
            ArrayList arrayList = new ArrayList(byteStringArr2.length);
            int length = byteStringArr2.length;
            int i2 = 0;
            while (i2 < length) {
                ByteString byteString = byteStringArr2[i2];
                i2++;
                arrayList.add(-1);
            }
            Object[] array = arrayList.toArray(new Integer[0]);
            if (array != null) {
                Integer[] numArr = (Integer[]) array;
                List mutableListOf = CollectionsKt__CollectionsKt.mutableListOf(Arrays.copyOf(numArr, numArr.length));
                int length2 = byteStringArr2.length;
                int i3 = 0;
                int i4 = 0;
                while (i3 < length2) {
                    mutableListOf.set(CollectionsKt__CollectionsKt.binarySearch$default(mutableList, byteStringArr2[i3], 0, 0, 6, (Object) null), Integer.valueOf(i4));
                    i3++;
                    i4++;
                }
                if (((ByteString) mutableList.get(0)).size() > 0) {
                    int i5 = 0;
                    while (i5 < mutableList.size()) {
                        ByteString byteString2 = (ByteString) mutableList.get(i5);
                        int i6 = i5 + 1;
                        int i7 = i6;
                        while (i7 < mutableList.size()) {
                            ByteString byteString3 = (ByteString) mutableList.get(i7);
                            if (!byteString3.startsWith(byteString2)) {
                                continue;
                                break;
                            }
                            if (!(byteString3.size() != byteString2.size())) {
                                throw new IllegalArgumentException(Intrinsics.stringPlus("duplicate option: ", byteString3).toString());
                            } else if (((Number) mutableListOf.get(i7)).intValue() > ((Number) mutableListOf.get(i5)).intValue()) {
                                mutableList.remove(i7);
                                mutableListOf.remove(i7);
                            } else {
                                i7++;
                            }
                        }
                        i5 = i6;
                    }
                    Buffer buffer = new Buffer();
                    buildTrieRecursive$default(this, 0, buffer, 0, mutableList, 0, 0, mutableListOf, 53, (Object) null);
                    int[] iArr = new int[((int) getIntCount(buffer))];
                    while (!buffer.exhausted()) {
                        iArr[i] = buffer.readInt();
                        i++;
                    }
                    return new Options((ByteString[]) Arrays.copyOf(byteStringArr2, byteStringArr2.length), iArr, (DefaultConstructorMarker) null);
                }
                throw new IllegalArgumentException("the empty byte string is not a supported option".toString());
            }
            throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>");
        }

        public static /* synthetic */ void buildTrieRecursive$default(Companion companion, long j, Buffer buffer, int i, List list, int i2, int i3, List list2, int i4, Object obj) {
            int i5;
            long j2 = (i4 & 1) != 0 ? 0 : j;
            int i6 = (i4 & 4) != 0 ? 0 : i;
            int i7 = (i4 & 16) != 0 ? 0 : i2;
            if ((i4 & 32) != 0) {
                i5 = list.size();
            } else {
                i5 = i3;
            }
            companion.buildTrieRecursive(j2, buffer, i6, list, i7, i5, list2);
        }

        public final void buildTrieRecursive(long j, Buffer buffer, int i, List<? extends ByteString> list, int i2, int i3, List<Integer> list2) {
            int i4;
            int i5;
            int i6;
            int i7;
            Buffer buffer2;
            Buffer buffer3 = buffer;
            int i8 = i;
            List<? extends ByteString> list3 = list;
            int i9 = i2;
            int i10 = i3;
            List<Integer> list4 = list2;
            int i11 = 1;
            if (i9 < i10) {
                int i12 = i9;
                while (i12 < i10) {
                    int i13 = i12 + 1;
                    if (((ByteString) list3.get(i12)).size() >= i8) {
                        i12 = i13;
                    } else {
                        throw new IllegalArgumentException("Failed requirement.".toString());
                    }
                }
                ByteString byteString = (ByteString) list.get(i2);
                ByteString byteString2 = (ByteString) list3.get(i10 - 1);
                int i14 = -1;
                if (i8 == byteString.size()) {
                    int intValue = list4.get(i9).intValue();
                    int i15 = i9 + 1;
                    i4 = i15;
                    i5 = intValue;
                    byteString = (ByteString) list3.get(i15);
                } else {
                    i4 = i9;
                    i5 = -1;
                }
                if (byteString.getByte(i8) != byteString2.getByte(i8)) {
                    int i16 = i4 + 1;
                    while (i16 < i10) {
                        int i17 = i16 + 1;
                        if (((ByteString) list3.get(i16 - 1)).getByte(i8) != ((ByteString) list3.get(i16)).getByte(i8)) {
                            i11++;
                        }
                        i16 = i17;
                    }
                    long intCount = j + getIntCount(buffer3) + ((long) 2) + ((long) (i11 * 2));
                    buffer3.writeInt(i11);
                    buffer3.writeInt(i5);
                    int i18 = i4;
                    while (i18 < i10) {
                        int i19 = i18 + 1;
                        byte b = ((ByteString) list3.get(i18)).getByte(i8);
                        if (i18 == i4 || b != ((ByteString) list3.get(i18 - 1)).getByte(i8)) {
                            buffer3.writeInt(b & 255);
                        }
                        i18 = i19;
                    }
                    Buffer buffer4 = new Buffer();
                    while (i4 < i10) {
                        byte b2 = ((ByteString) list3.get(i4)).getByte(i8);
                        int i20 = i4 + 1;
                        int i21 = i20;
                        while (true) {
                            if (i21 >= i10) {
                                i6 = i10;
                                break;
                            }
                            int i22 = i21 + 1;
                            if (b2 != ((ByteString) list3.get(i21)).getByte(i8)) {
                                i6 = i21;
                                break;
                            }
                            i21 = i22;
                        }
                        if (i20 == i6 && i8 + 1 == ((ByteString) list3.get(i4)).size()) {
                            buffer3.writeInt(list4.get(i4).intValue());
                            i7 = i6;
                            buffer2 = buffer4;
                        } else {
                            buffer3.writeInt(((int) (intCount + getIntCount(buffer4))) * i14);
                            i7 = i6;
                            buffer2 = buffer4;
                            buildTrieRecursive(intCount, buffer4, i8 + 1, list, i4, i6, list2);
                        }
                        buffer4 = buffer2;
                        i4 = i7;
                        i14 = -1;
                    }
                    buffer3.writeAll(buffer4);
                    return;
                }
                int min = Math.min(byteString.size(), byteString2.size());
                int i23 = i8;
                int i24 = 0;
                while (i23 < min) {
                    int i25 = i23 + 1;
                    if (byteString.getByte(i23) != byteString2.getByte(i23)) {
                        break;
                    }
                    i24++;
                    i23 = i25;
                }
                long intCount2 = j + getIntCount(buffer3) + ((long) 2) + ((long) i24) + 1;
                buffer3.writeInt(-i24);
                buffer3.writeInt(i5);
                int i26 = i8 + i24;
                while (i8 < i26) {
                    buffer3.writeInt(byteString.getByte(i8) & 255);
                    i8++;
                }
                if (i4 + 1 == i10) {
                    if (i26 == ((ByteString) list3.get(i4)).size()) {
                        buffer3.writeInt(list4.get(i4).intValue());
                        return;
                    }
                    throw new IllegalStateException("Check failed.".toString());
                }
                Buffer buffer5 = new Buffer();
                buffer3.writeInt(((int) (getIntCount(buffer5) + intCount2)) * -1);
                buildTrieRecursive(intCount2, buffer5, i26, list, i4, i3, list2);
                buffer3.writeAll(buffer5);
                return;
            }
            throw new IllegalArgumentException("Failed requirement.".toString());
        }

        public final long getIntCount(Buffer buffer) {
            return buffer.size() / ((long) 4);
        }
    }
}
