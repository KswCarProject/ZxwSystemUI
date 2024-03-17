package kotlinx.coroutines.internal;

import java.util.ArrayDeque;
import java.util.Iterator;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.coroutines.jvm.internal.CoroutineStackFrame;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt__StringsJVMKt;
import kotlinx.coroutines.CopyableThrowable;
import org.jetbrains.annotations.NotNull;

/* compiled from: StackTraceRecovery.kt */
public final class StackTraceRecoveryKt {
    public static final String baseContinuationImplClassName;
    public static final String stackTraceRecoveryClassName;

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v0, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v5, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v9, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v10, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v11, resolved type: java.lang.String} */
    /* JADX WARNING: Multi-variable type inference failed */
    static {
        /*
            java.lang.String r0 = "kotlin.coroutines.jvm.internal.BaseContinuationImpl"
            kotlin.Result$Companion r1 = kotlin.Result.Companion     // Catch:{ all -> 0x0011 }
            java.lang.Class r1 = java.lang.Class.forName(r0)     // Catch:{ all -> 0x0011 }
            java.lang.String r1 = r1.getCanonicalName()     // Catch:{ all -> 0x0011 }
            java.lang.Object r1 = kotlin.Result.m4641constructorimpl(r1)     // Catch:{ all -> 0x0011 }
            goto L_0x001c
        L_0x0011:
            r1 = move-exception
            kotlin.Result$Companion r2 = kotlin.Result.Companion
            java.lang.Object r1 = kotlin.ResultKt.createFailure(r1)
            java.lang.Object r1 = kotlin.Result.m4641constructorimpl(r1)
        L_0x001c:
            java.lang.Throwable r2 = kotlin.Result.m4643exceptionOrNullimpl(r1)
            if (r2 != 0) goto L_0x0023
            r0 = r1
        L_0x0023:
            java.lang.String r0 = (java.lang.String) r0
            baseContinuationImplClassName = r0
            kotlin.Result$Companion r0 = kotlin.Result.Companion     // Catch:{ all -> 0x0034 }
            java.lang.Class<kotlinx.coroutines.internal.StackTraceRecoveryKt> r0 = kotlinx.coroutines.internal.StackTraceRecoveryKt.class
            java.lang.String r0 = r0.getCanonicalName()     // Catch:{ all -> 0x0034 }
            java.lang.Object r0 = kotlin.Result.m4641constructorimpl(r0)     // Catch:{ all -> 0x0034 }
            goto L_0x003f
        L_0x0034:
            r0 = move-exception
            kotlin.Result$Companion r1 = kotlin.Result.Companion
            java.lang.Object r0 = kotlin.ResultKt.createFailure(r0)
            java.lang.Object r0 = kotlin.Result.m4641constructorimpl(r0)
        L_0x003f:
            java.lang.Throwable r1 = kotlin.Result.m4643exceptionOrNullimpl(r0)
            if (r1 != 0) goto L_0x0046
            goto L_0x0048
        L_0x0046:
            java.lang.String r0 = "kotlinx.coroutines.internal.StackTraceRecoveryKt"
        L_0x0048:
            java.lang.String r0 = (java.lang.String) r0
            stackTraceRecoveryClassName = r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.internal.StackTraceRecoveryKt.<clinit>():void");
    }

    public static final <E extends Throwable> E recoverFromStackFrame(E e, CoroutineStackFrame coroutineStackFrame) {
        Pair causeAndStacktrace = causeAndStacktrace(e);
        E e2 = (Throwable) causeAndStacktrace.component1();
        StackTraceElement[] stackTraceElementArr = (StackTraceElement[]) causeAndStacktrace.component2();
        Throwable tryCopyAndVerify = tryCopyAndVerify(e2);
        if (tryCopyAndVerify == null) {
            return e;
        }
        ArrayDeque createStackTrace = createStackTrace(coroutineStackFrame);
        if (createStackTrace.isEmpty()) {
            return e;
        }
        if (e2 != e) {
            mergeRecoveredTraces(stackTraceElementArr, createStackTrace);
        }
        return createFinalException(e2, tryCopyAndVerify, createStackTrace);
    }

    public static final <E extends Throwable> E tryCopyAndVerify(E e) {
        E tryCopyException = ExceptionsConstuctorKt.tryCopyException(e);
        if (tryCopyException == null) {
            return null;
        }
        if ((e instanceof CopyableThrowable) || Intrinsics.areEqual((Object) tryCopyException.getMessage(), (Object) e.getMessage())) {
            return tryCopyException;
        }
        return null;
    }

    public static final <E extends Throwable> E createFinalException(E e, E e2, ArrayDeque<StackTraceElement> arrayDeque) {
        arrayDeque.addFirst(artificialFrame("Coroutine boundary"));
        StackTraceElement[] stackTrace = e.getStackTrace();
        int frameIndex = frameIndex(stackTrace, baseContinuationImplClassName);
        int i = 0;
        if (frameIndex == -1) {
            Object[] array = arrayDeque.toArray(new StackTraceElement[0]);
            if (array != null) {
                e2.setStackTrace((StackTraceElement[]) array);
                return e2;
            }
            throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>");
        }
        StackTraceElement[] stackTraceElementArr = new StackTraceElement[(arrayDeque.size() + frameIndex)];
        for (int i2 = 0; i2 < frameIndex; i2++) {
            stackTraceElementArr[i2] = stackTrace[i2];
        }
        Iterator<StackTraceElement> it = arrayDeque.iterator();
        while (it.hasNext()) {
            stackTraceElementArr[i + frameIndex] = it.next();
            i++;
        }
        e2.setStackTrace(stackTraceElementArr);
        return e2;
    }

    public static final <E extends Throwable> Pair<E, StackTraceElement[]> causeAndStacktrace(E e) {
        boolean z;
        Throwable cause = e.getCause();
        if (cause == null || !Intrinsics.areEqual((Object) cause.getClass(), (Object) e.getClass())) {
            return TuplesKt.to(e, new StackTraceElement[0]);
        }
        StackTraceElement[] stackTrace = e.getStackTrace();
        int length = stackTrace.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                z = false;
                break;
            }
            StackTraceElement stackTraceElement = stackTrace[i];
            i++;
            if (isArtificial(stackTraceElement)) {
                z = true;
                break;
            }
        }
        if (z) {
            return TuplesKt.to(cause, stackTrace);
        }
        return TuplesKt.to(e, new StackTraceElement[0]);
    }

    @NotNull
    public static final <E extends Throwable> E unwrapImpl(@NotNull E e) {
        E cause = e.getCause();
        if (cause != null && Intrinsics.areEqual((Object) cause.getClass(), (Object) e.getClass())) {
            StackTraceElement[] stackTrace = e.getStackTrace();
            int length = stackTrace.length;
            boolean z = false;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                StackTraceElement stackTraceElement = stackTrace[i];
                i++;
                if (isArtificial(stackTraceElement)) {
                    z = true;
                    break;
                }
            }
            if (z) {
                return cause;
            }
        }
        return e;
    }

    public static final ArrayDeque<StackTraceElement> createStackTrace(CoroutineStackFrame coroutineStackFrame) {
        ArrayDeque<StackTraceElement> arrayDeque = new ArrayDeque<>();
        StackTraceElement stackTraceElement = coroutineStackFrame.getStackTraceElement();
        if (stackTraceElement != null) {
            arrayDeque.add(stackTraceElement);
        }
        while (true) {
            if (!(coroutineStackFrame instanceof CoroutineStackFrame)) {
                coroutineStackFrame = null;
            }
            coroutineStackFrame = coroutineStackFrame == null ? null : coroutineStackFrame.getCallerFrame();
            if (coroutineStackFrame == null) {
                return arrayDeque;
            }
            StackTraceElement stackTraceElement2 = coroutineStackFrame.getStackTraceElement();
            if (stackTraceElement2 != null) {
                arrayDeque.add(stackTraceElement2);
            }
        }
    }

    @NotNull
    public static final StackTraceElement artificialFrame(@NotNull String str) {
        return new StackTraceElement(Intrinsics.stringPlus("\b\b\b(", str), "\b", "\b", -1);
    }

    public static final boolean isArtificial(@NotNull StackTraceElement stackTraceElement) {
        return StringsKt__StringsJVMKt.startsWith$default(stackTraceElement.getClassName(), "\b\b\b", false, 2, (Object) null);
    }

    public static final boolean elementWiseEquals(StackTraceElement stackTraceElement, StackTraceElement stackTraceElement2) {
        return stackTraceElement.getLineNumber() == stackTraceElement2.getLineNumber() && Intrinsics.areEqual((Object) stackTraceElement.getMethodName(), (Object) stackTraceElement2.getMethodName()) && Intrinsics.areEqual((Object) stackTraceElement.getFileName(), (Object) stackTraceElement2.getFileName()) && Intrinsics.areEqual((Object) stackTraceElement.getClassName(), (Object) stackTraceElement2.getClassName());
    }

    public static final int frameIndex(StackTraceElement[] stackTraceElementArr, String str) {
        int length = stackTraceElementArr.length;
        int i = 0;
        while (i < length) {
            int i2 = i + 1;
            if (Intrinsics.areEqual((Object) str, (Object) stackTraceElementArr[i].getClassName())) {
                return i;
            }
            i = i2;
        }
        return -1;
    }

    public static final void mergeRecoveredTraces(StackTraceElement[] stackTraceElementArr, ArrayDeque<StackTraceElement> arrayDeque) {
        int length = stackTraceElementArr.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                i = -1;
                break;
            }
            int i2 = i + 1;
            if (isArtificial(stackTraceElementArr[i])) {
                break;
            }
            i = i2;
        }
        int i3 = i + 1;
        int length2 = stackTraceElementArr.length - 1;
        if (i3 <= length2) {
            while (true) {
                int i4 = length2 - 1;
                if (elementWiseEquals(stackTraceElementArr[length2], arrayDeque.getLast())) {
                    arrayDeque.removeLast();
                }
                arrayDeque.addFirst(stackTraceElementArr[length2]);
                if (length2 != i3) {
                    length2 = i4;
                } else {
                    return;
                }
            }
        }
    }
}
