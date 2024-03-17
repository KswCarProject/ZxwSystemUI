package kotlinx.coroutines.internal;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.jvm.JvmClassMappingKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: ExceptionsConstuctor.kt */
public final class ExceptionsConstuctorKt {
    @NotNull
    public static final ReentrantReadWriteLock cacheLock = new ReentrantReadWriteLock();
    @NotNull
    public static final WeakHashMap<Class<? extends Throwable>, Function1<Throwable, Throwable>> exceptionCtors = new WeakHashMap<>();
    public static final int throwableFields = fieldsCountOrDefault(Throwable.class, -1);

    /*  JADX ERROR: StackOverflow in pass: MarkFinallyVisitor
        jadx.core.utils.exceptions.JadxOverflowException: 
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:47)
        	at jadx.core.utils.ErrorsCounter.methodError(ErrorsCounter.java:81)
        */
    @org.jetbrains.annotations.Nullable
    public static final <E extends java.lang.Throwable> E tryCopyException(@org.jetbrains.annotations.NotNull E r9) {
        /*
            boolean r0 = r9 instanceof kotlinx.coroutines.CopyableThrowable
            r1 = 0
            if (r0 == 0) goto L_0x0028
            kotlin.Result$Companion r0 = kotlin.Result.Companion     // Catch:{ all -> 0x0012 }
            kotlinx.coroutines.CopyableThrowable r9 = (kotlinx.coroutines.CopyableThrowable) r9     // Catch:{ all -> 0x0012 }
            java.lang.Throwable r9 = r9.createCopy()     // Catch:{ all -> 0x0012 }
            java.lang.Object r9 = kotlin.Result.m4641constructorimpl(r9)     // Catch:{ all -> 0x0012 }
            goto L_0x001d
        L_0x0012:
            r9 = move-exception
            kotlin.Result$Companion r0 = kotlin.Result.Companion
            java.lang.Object r9 = kotlin.ResultKt.createFailure(r9)
            java.lang.Object r9 = kotlin.Result.m4641constructorimpl(r9)
        L_0x001d:
            boolean r0 = kotlin.Result.m4645isFailureimpl(r9)
            if (r0 == 0) goto L_0x0024
            goto L_0x0025
        L_0x0024:
            r1 = r9
        L_0x0025:
            java.lang.Throwable r1 = (java.lang.Throwable) r1
            return r1
        L_0x0028:
            java.util.concurrent.locks.ReentrantReadWriteLock r0 = cacheLock
            java.util.concurrent.locks.ReentrantReadWriteLock$ReadLock r2 = r0.readLock()
            r2.lock()
            java.util.WeakHashMap<java.lang.Class<? extends java.lang.Throwable>, kotlin.jvm.functions.Function1<java.lang.Throwable, java.lang.Throwable>> r3 = exceptionCtors     // Catch:{ all -> 0x011a }
            java.lang.Class r4 = r9.getClass()     // Catch:{ all -> 0x011a }
            java.lang.Object r3 = r3.get(r4)     // Catch:{ all -> 0x011a }
            kotlin.jvm.functions.Function1 r3 = (kotlin.jvm.functions.Function1) r3     // Catch:{ all -> 0x011a }
            r2.unlock()
            if (r3 != 0) goto L_0x0113
            int r2 = throwableFields
            java.lang.Class r3 = r9.getClass()
            r4 = 0
            int r3 = fieldsCountOrDefault(r3, r4)
            if (r2 == r3) goto L_0x0095
            java.util.concurrent.locks.ReentrantReadWriteLock$ReadLock r2 = r0.readLock()
            int r3 = r0.getWriteHoldCount()
            if (r3 != 0) goto L_0x005e
            int r3 = r0.getReadHoldCount()
            goto L_0x005f
        L_0x005e:
            r3 = r4
        L_0x005f:
            r5 = r4
        L_0x0060:
            if (r5 >= r3) goto L_0x0068
            int r5 = r5 + 1
            r2.unlock()
            goto L_0x0060
        L_0x0068:
            java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock r0 = r0.writeLock()
            r0.lock()
            java.util.WeakHashMap<java.lang.Class<? extends java.lang.Throwable>, kotlin.jvm.functions.Function1<java.lang.Throwable, java.lang.Throwable>> r5 = exceptionCtors     // Catch:{ all -> 0x0088 }
            java.lang.Class r9 = r9.getClass()     // Catch:{ all -> 0x0088 }
            kotlinx.coroutines.internal.ExceptionsConstuctorKt$tryCopyException$4$1 r6 = kotlinx.coroutines.internal.ExceptionsConstuctorKt$tryCopyException$4$1.INSTANCE     // Catch:{ all -> 0x0088 }
            r5.put(r9, r6)     // Catch:{ all -> 0x0088 }
            kotlin.Unit r9 = kotlin.Unit.INSTANCE     // Catch:{ all -> 0x0088 }
        L_0x007c:
            if (r4 >= r3) goto L_0x0084
            int r4 = r4 + 1
            r2.lock()
            goto L_0x007c
        L_0x0084:
            r0.unlock()
            return r1
        L_0x0088:
            r9 = move-exception
        L_0x0089:
            if (r4 >= r3) goto L_0x0091
            int r4 = r4 + 1
            r2.lock()
            goto L_0x0089
        L_0x0091:
            r0.unlock()
            throw r9
        L_0x0095:
            java.lang.Class r0 = r9.getClass()
            java.lang.reflect.Constructor[] r0 = r0.getConstructors()
            kotlinx.coroutines.internal.ExceptionsConstuctorKt$tryCopyException$$inlined$sortedByDescending$1 r2 = new kotlinx.coroutines.internal.ExceptionsConstuctorKt$tryCopyException$$inlined$sortedByDescending$1
            r2.<init>()
            java.util.List r0 = kotlin.collections.ArraysKt___ArraysKt.sortedWith(r0, r2)
            java.util.Iterator r0 = r0.iterator()
            r2 = r1
        L_0x00ab:
            boolean r3 = r0.hasNext()
            if (r3 == 0) goto L_0x00bd
            java.lang.Object r2 = r0.next()
            java.lang.reflect.Constructor r2 = (java.lang.reflect.Constructor) r2
            kotlin.jvm.functions.Function1 r2 = createConstructor(r2)
            if (r2 == 0) goto L_0x00ab
        L_0x00bd:
            java.util.concurrent.locks.ReentrantReadWriteLock r0 = cacheLock
            java.util.concurrent.locks.ReentrantReadWriteLock$ReadLock r3 = r0.readLock()
            int r5 = r0.getWriteHoldCount()
            if (r5 != 0) goto L_0x00ce
            int r5 = r0.getReadHoldCount()
            goto L_0x00cf
        L_0x00ce:
            r5 = r4
        L_0x00cf:
            r6 = r4
        L_0x00d0:
            if (r6 >= r5) goto L_0x00d8
            int r6 = r6 + 1
            r3.unlock()
            goto L_0x00d0
        L_0x00d8:
            java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock r0 = r0.writeLock()
            r0.lock()
            java.util.WeakHashMap<java.lang.Class<? extends java.lang.Throwable>, kotlin.jvm.functions.Function1<java.lang.Throwable, java.lang.Throwable>> r6 = exceptionCtors     // Catch:{ all -> 0x0106 }
            java.lang.Class r7 = r9.getClass()     // Catch:{ all -> 0x0106 }
            if (r2 != 0) goto L_0x00ea
            kotlinx.coroutines.internal.ExceptionsConstuctorKt$tryCopyException$5$1 r8 = kotlinx.coroutines.internal.ExceptionsConstuctorKt$tryCopyException$5$1.INSTANCE     // Catch:{ all -> 0x0106 }
            goto L_0x00eb
        L_0x00ea:
            r8 = r2
        L_0x00eb:
            r6.put(r7, r8)     // Catch:{ all -> 0x0106 }
            kotlin.Unit r6 = kotlin.Unit.INSTANCE     // Catch:{ all -> 0x0106 }
        L_0x00f0:
            if (r4 >= r5) goto L_0x00f8
            int r4 = r4 + 1
            r3.lock()
            goto L_0x00f0
        L_0x00f8:
            r0.unlock()
            if (r2 != 0) goto L_0x00fe
            goto L_0x0105
        L_0x00fe:
            java.lang.Object r9 = r2.invoke(r9)
            r1 = r9
            java.lang.Throwable r1 = (java.lang.Throwable) r1
        L_0x0105:
            return r1
        L_0x0106:
            r9 = move-exception
        L_0x0107:
            if (r4 >= r5) goto L_0x010f
            int r4 = r4 + 1
            r3.lock()
            goto L_0x0107
        L_0x010f:
            r0.unlock()
            throw r9
        L_0x0113:
            java.lang.Object r9 = r3.invoke(r9)
            java.lang.Throwable r9 = (java.lang.Throwable) r9
            return r9
        L_0x011a:
            r9 = move-exception
            r2.unlock()
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.internal.ExceptionsConstuctorKt.tryCopyException(java.lang.Throwable):java.lang.Throwable");
    }

    public static final Function1<Throwable, Throwable> createConstructor(Constructor<?> constructor) {
        Class<String> cls = String.class;
        Class[] parameterTypes = constructor.getParameterTypes();
        int length = parameterTypes.length;
        if (length == 0) {
            return new ExceptionsConstuctorKt$createConstructor$$inlined$safeCtor$4(constructor);
        }
        if (length == 1) {
            Class cls2 = parameterTypes[0];
            if (Intrinsics.areEqual((Object) cls2, (Object) Throwable.class)) {
                return new ExceptionsConstuctorKt$createConstructor$$inlined$safeCtor$2(constructor);
            }
            if (Intrinsics.areEqual((Object) cls2, (Object) cls)) {
                return new ExceptionsConstuctorKt$createConstructor$$inlined$safeCtor$3(constructor);
            }
            return null;
        } else if (length == 2 && Intrinsics.areEqual((Object) parameterTypes[0], (Object) cls) && Intrinsics.areEqual((Object) parameterTypes[1], (Object) Throwable.class)) {
            return new ExceptionsConstuctorKt$createConstructor$$inlined$safeCtor$1(constructor);
        } else {
            return null;
        }
    }

    public static final int fieldsCountOrDefault(Class<?> cls, int i) {
        Integer num;
        JvmClassMappingKt.getKotlinClass(cls);
        try {
            Result.Companion companion = Result.Companion;
            num = Result.m4641constructorimpl(Integer.valueOf(fieldsCount$default(cls, 0, 1, (Object) null)));
        } catch (Throwable th) {
            Result.Companion companion2 = Result.Companion;
            num = Result.m4641constructorimpl(ResultKt.createFailure(th));
        }
        Integer valueOf = Integer.valueOf(i);
        if (Result.m4645isFailureimpl(num)) {
            num = valueOf;
        }
        return ((Number) num).intValue();
    }

    public static /* synthetic */ int fieldsCount$default(Class cls, int i, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            i = 0;
        }
        return fieldsCount(cls, i);
    }

    public static final int fieldsCount(Class<?> cls, int i) {
        Class<? super Object> superclass;
        do {
            Field[] declaredFields = r5.getDeclaredFields();
            int length = declaredFields.length;
            int i2 = 0;
            int i3 = 0;
            Class<? super Object> cls2 = cls;
            while (i2 < length) {
                Field field = declaredFields[i2];
                i2++;
                if (!Modifier.isStatic(field.getModifiers())) {
                    i3++;
                }
            }
            i += i3;
            superclass = cls2.getSuperclass();
            cls2 = superclass;
        } while (superclass != null);
        return i;
    }
}
