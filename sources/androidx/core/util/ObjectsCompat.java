package androidx.core.util;

import java.util.Objects;

public class ObjectsCompat {
    public static boolean equals(Object obj, Object obj2) {
        return Api19Impl.equals(obj, obj2);
    }

    public static int hash(Object... objArr) {
        return Api19Impl.hash(objArr);
    }

    public static <T> T requireNonNull(T t) {
        t.getClass();
        return t;
    }

    public static class Api19Impl {
        public static boolean equals(Object obj, Object obj2) {
            return Objects.equals(obj, obj2);
        }

        public static int hash(Object... objArr) {
            return Objects.hash(objArr);
        }
    }
}
