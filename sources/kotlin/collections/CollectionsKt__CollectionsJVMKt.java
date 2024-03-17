package kotlin.collections;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: CollectionsJVM.kt */
public class CollectionsKt__CollectionsJVMKt {
    @NotNull
    public static final <T> List<T> listOf(T t) {
        return Collections.singletonList(t);
    }

    @NotNull
    public static final <T> Object[] copyToArrayOfAny(@NotNull T[] tArr, boolean z) {
        Class<Object[]> cls = Object[].class;
        return (!z || !Intrinsics.areEqual((Object) tArr.getClass(), (Object) cls)) ? Arrays.copyOf(tArr, tArr.length, cls) : tArr;
    }
}
