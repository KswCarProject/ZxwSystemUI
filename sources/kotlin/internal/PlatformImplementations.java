package kotlin.internal;

import java.lang.reflect.Method;
import kotlin.collections.ArraysKt___ArraysKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.random.FallbackThreadLocalRandom;
import kotlin.random.Random;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: PlatformImplementations.kt */
public class PlatformImplementations {

    /* compiled from: PlatformImplementations.kt */
    public static final class ReflectThrowable {
        @NotNull
        public static final ReflectThrowable INSTANCE = new ReflectThrowable();
        @Nullable
        public static final Method addSuppressed;
        @Nullable
        public static final Method getSuppressed;

        static {
            Method method;
            Method method2;
            boolean z;
            Class<Throwable> cls = Throwable.class;
            Method[] methods = cls.getMethods();
            int length = methods.length;
            int i = 0;
            int i2 = 0;
            while (true) {
                method = null;
                if (i2 >= length) {
                    method2 = null;
                    break;
                }
                method2 = methods[i2];
                i2++;
                if (!Intrinsics.areEqual((Object) method2.getName(), (Object) "addSuppressed") || !Intrinsics.areEqual(ArraysKt___ArraysKt.singleOrNull(method2.getParameterTypes()), (Object) cls)) {
                    z = false;
                    continue;
                } else {
                    z = true;
                    continue;
                }
                if (z) {
                    break;
                }
            }
            addSuppressed = method2;
            int length2 = methods.length;
            while (true) {
                if (i >= length2) {
                    break;
                }
                Method method3 = methods[i];
                i++;
                if (Intrinsics.areEqual((Object) method3.getName(), (Object) "getSuppressed")) {
                    method = method3;
                    break;
                }
            }
            getSuppressed = method;
        }
    }

    public void addSuppressed(@NotNull Throwable th, @NotNull Throwable th2) {
        Method method = ReflectThrowable.addSuppressed;
        if (method != null) {
            method.invoke(th, new Object[]{th2});
        }
    }

    @NotNull
    public Random defaultPlatformRandom() {
        return new FallbackThreadLocalRandom();
    }
}
