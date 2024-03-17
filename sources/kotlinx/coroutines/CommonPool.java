package kotlinx.coroutines;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt___RangesKt;
import kotlin.text.StringsKt__StringNumberConversionsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: CommonPool.kt */
public final class CommonPool extends ExecutorCoroutineDispatcher {
    @NotNull
    public static final CommonPool INSTANCE = new CommonPool();
    @Nullable
    public static volatile Executor pool;
    public static final int requestedParallelism;
    public static boolean usePrivatePool;

    @NotNull
    public String toString() {
        return "CommonPool";
    }

    static {
        String str;
        int i;
        try {
            str = System.getProperty("kotlinx.coroutines.default.parallelism");
        } catch (Throwable unused) {
            str = null;
        }
        if (str == null) {
            i = -1;
        } else {
            Integer intOrNull = StringsKt__StringNumberConversionsKt.toIntOrNull(str);
            if (intOrNull == null || intOrNull.intValue() < 1) {
                throw new IllegalStateException(Intrinsics.stringPlus("Expected positive number in kotlinx.coroutines.default.parallelism, but has ", str).toString());
            }
            i = intOrNull.intValue();
        }
        requestedParallelism = i;
    }

    public final int getParallelism() {
        Integer valueOf = Integer.valueOf(requestedParallelism);
        if (!(valueOf.intValue() > 0)) {
            valueOf = null;
        }
        if (valueOf == null) {
            return RangesKt___RangesKt.coerceAtLeast(Runtime.getRuntime().availableProcessors() - 1, 1);
        }
        return valueOf.intValue();
    }

    /* JADX WARNING: Removed duplicated region for block: B:24:0x003d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.util.concurrent.ExecutorService createPool() {
        /*
            r6 = this;
            java.lang.SecurityManager r0 = java.lang.System.getSecurityManager()
            if (r0 == 0) goto L_0x000b
            java.util.concurrent.ExecutorService r6 = r6.createPlainPool()
            return r6
        L_0x000b:
            r0 = 0
            java.lang.String r1 = "java.util.concurrent.ForkJoinPool"
            java.lang.Class r1 = java.lang.Class.forName(r1)     // Catch:{ all -> 0x0013 }
            goto L_0x0014
        L_0x0013:
            r1 = r0
        L_0x0014:
            if (r1 != 0) goto L_0x001b
            java.util.concurrent.ExecutorService r6 = r6.createPlainPool()
            return r6
        L_0x001b:
            boolean r2 = usePrivatePool
            r3 = 0
            if (r2 != 0) goto L_0x004b
            int r2 = requestedParallelism
            if (r2 >= 0) goto L_0x004b
            java.lang.String r2 = "commonPool"
            java.lang.Class[] r4 = new java.lang.Class[r3]     // Catch:{ all -> 0x0039 }
            java.lang.reflect.Method r2 = r1.getMethod(r2, r4)     // Catch:{ all -> 0x0039 }
            java.lang.Object[] r4 = new java.lang.Object[r3]     // Catch:{ all -> 0x0039 }
            java.lang.Object r2 = r2.invoke(r0, r4)     // Catch:{ all -> 0x0039 }
            boolean r4 = r2 instanceof java.util.concurrent.ExecutorService     // Catch:{ all -> 0x0039 }
            if (r4 == 0) goto L_0x0039
            java.util.concurrent.ExecutorService r2 = (java.util.concurrent.ExecutorService) r2     // Catch:{ all -> 0x0039 }
            goto L_0x003a
        L_0x0039:
            r2 = r0
        L_0x003a:
            if (r2 != 0) goto L_0x003d
            goto L_0x004b
        L_0x003d:
            kotlinx.coroutines.CommonPool r4 = INSTANCE
            boolean r4 = r4.isGoodCommonPool$external__kotlinx_coroutines__android_common__kotlinx_coroutines(r1, r2)
            if (r4 == 0) goto L_0x0046
            goto L_0x0047
        L_0x0046:
            r2 = r0
        L_0x0047:
            if (r2 != 0) goto L_0x004a
            goto L_0x004b
        L_0x004a:
            return r2
        L_0x004b:
            r2 = 1
            java.lang.Class[] r4 = new java.lang.Class[r2]     // Catch:{ all -> 0x006f }
            java.lang.Class r5 = java.lang.Integer.TYPE     // Catch:{ all -> 0x006f }
            r4[r3] = r5     // Catch:{ all -> 0x006f }
            java.lang.reflect.Constructor r1 = r1.getConstructor(r4)     // Catch:{ all -> 0x006f }
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ all -> 0x006f }
            kotlinx.coroutines.CommonPool r4 = INSTANCE     // Catch:{ all -> 0x006f }
            int r4 = r4.getParallelism()     // Catch:{ all -> 0x006f }
            java.lang.Integer r4 = java.lang.Integer.valueOf(r4)     // Catch:{ all -> 0x006f }
            r2[r3] = r4     // Catch:{ all -> 0x006f }
            java.lang.Object r1 = r1.newInstance(r2)     // Catch:{ all -> 0x006f }
            boolean r2 = r1 instanceof java.util.concurrent.ExecutorService     // Catch:{ all -> 0x006f }
            if (r2 == 0) goto L_0x006f
            java.util.concurrent.ExecutorService r1 = (java.util.concurrent.ExecutorService) r1     // Catch:{ all -> 0x006f }
            r0 = r1
        L_0x006f:
            if (r0 != 0) goto L_0x0076
            java.util.concurrent.ExecutorService r6 = r6.createPlainPool()
            return r6
        L_0x0076:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.CommonPool.createPool():java.util.concurrent.ExecutorService");
    }

    public final boolean isGoodCommonPool$external__kotlinx_coroutines__android_common__kotlinx_coroutines(@NotNull Class<?> cls, @NotNull ExecutorService executorService) {
        executorService.submit(CommonPool$isGoodCommonPool$1.INSTANCE);
        Integer num = null;
        try {
            Object invoke = cls.getMethod("getPoolSize", new Class[0]).invoke(executorService, new Object[0]);
            if (invoke instanceof Integer) {
                num = (Integer) invoke;
            }
        } catch (Throwable unused) {
        }
        if (num != null && num.intValue() >= 1) {
            return true;
        }
        return false;
    }

    public final ExecutorService createPlainPool() {
        return Executors.newFixedThreadPool(getParallelism(), new CommonPool$createPlainPool$1(new AtomicInteger()));
    }

    public final synchronized Executor getOrCreatePoolSync() {
        Executor executor;
        executor = pool;
        if (executor == null) {
            ExecutorService createPool = createPool();
            pool = createPool;
            executor = createPool;
        }
        return executor;
    }

    public void dispatch(@NotNull CoroutineContext coroutineContext, @NotNull Runnable runnable) {
        try {
            Executor executor = pool;
            if (executor == null) {
                executor = getOrCreatePoolSync();
            }
            AbstractTimeSourceKt.getTimeSource();
            executor.execute(runnable);
        } catch (RejectedExecutionException unused) {
            AbstractTimeSourceKt.getTimeSource();
            DefaultExecutor.INSTANCE.enqueue(runnable);
        }
    }

    public void close() {
        throw new IllegalStateException("Close cannot be invoked on CommonPool".toString());
    }
}
