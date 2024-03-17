package kotlin.concurrent;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: Thread.kt */
public final class ThreadsKt {
    public static /* synthetic */ Thread thread$default(boolean z, boolean z2, ClassLoader classLoader, String str, int i, Function0 function0, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            z = true;
        }
        boolean z3 = z;
        if ((i2 & 2) != 0) {
            z2 = false;
        }
        boolean z4 = z2;
        ClassLoader classLoader2 = (i2 & 4) != 0 ? null : classLoader;
        String str2 = (i2 & 8) != 0 ? null : str;
        if ((i2 & 16) != 0) {
            i = -1;
        }
        return thread(z3, z4, classLoader2, str2, i, function0);
    }

    @NotNull
    public static final Thread thread(boolean z, boolean z2, @Nullable ClassLoader classLoader, @Nullable String str, int i, @NotNull Function0<Unit> function0) {
        ThreadsKt$thread$thread$1 threadsKt$thread$thread$1 = new ThreadsKt$thread$thread$1(function0);
        if (z2) {
            threadsKt$thread$thread$1.setDaemon(true);
        }
        if (i > 0) {
            threadsKt$thread$thread$1.setPriority(i);
        }
        if (str != null) {
            threadsKt$thread$thread$1.setName(str);
        }
        if (classLoader != null) {
            threadsKt$thread$thread$1.setContextClassLoader(classLoader);
        }
        if (z) {
            threadsKt$thread$thread$1.start();
        }
        return threadsKt$thread$thread$1;
    }
}
