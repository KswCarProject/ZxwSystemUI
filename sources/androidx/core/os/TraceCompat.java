package androidx.core.os;

import android.os.Trace;

@Deprecated
public final class TraceCompat {
    public static void beginSection(String str) {
        Api18Impl.beginSection(str);
    }

    public static void endSection() {
        Api18Impl.endSection();
    }

    public static class Api18Impl {
        public static void beginSection(String str) {
            Trace.beginSection(str);
        }

        public static void endSection() {
            Trace.endSection();
        }
    }
}
