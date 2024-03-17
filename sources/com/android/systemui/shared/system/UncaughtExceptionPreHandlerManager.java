package com.android.systemui.shared.system;

import android.util.Log;
import java.lang.Thread;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: UncaughtExceptionPreHandlerManager.kt */
public final class UncaughtExceptionPreHandlerManager {
    @NotNull
    public final GlobalUncaughtExceptionHandler globalUncaughtExceptionPreHandler = new GlobalUncaughtExceptionHandler();
    @NotNull
    public final List<Thread.UncaughtExceptionHandler> handlers = new CopyOnWriteArrayList();

    public final void registerHandler(@NotNull Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
        checkGlobalHandlerSetup();
        addHandler(uncaughtExceptionHandler);
    }

    public final void checkGlobalHandlerSetup() {
        Thread.UncaughtExceptionHandler uncaughtExceptionPreHandler = Thread.getUncaughtExceptionPreHandler();
        if (Intrinsics.areEqual((Object) uncaughtExceptionPreHandler, (Object) this.globalUncaughtExceptionPreHandler)) {
            return;
        }
        if (!(uncaughtExceptionPreHandler instanceof GlobalUncaughtExceptionHandler)) {
            if (uncaughtExceptionPreHandler != null) {
                addHandler(uncaughtExceptionPreHandler);
            }
            Thread.setUncaughtExceptionPreHandler(this.globalUncaughtExceptionPreHandler);
            return;
        }
        throw new IllegalStateException("Two UncaughtExceptionPreHandlerManagers created");
    }

    public final void addHandler(Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
        if (!this.handlers.contains(uncaughtExceptionHandler)) {
            this.handlers.add(uncaughtExceptionHandler);
        }
    }

    public final void handleUncaughtException(@Nullable Thread thread, @Nullable Throwable th) {
        for (Thread.UncaughtExceptionHandler uncaughtException : this.handlers) {
            try {
                uncaughtException.uncaughtException(thread, th);
            } catch (Exception e) {
                Log.wtf("Uncaught exception pre-handler error", e);
            }
        }
    }

    /* compiled from: UncaughtExceptionPreHandlerManager.kt */
    public final class GlobalUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
        public GlobalUncaughtExceptionHandler() {
        }

        public void uncaughtException(@Nullable Thread thread, @Nullable Throwable th) {
            UncaughtExceptionPreHandlerManager.this.handleUncaughtException(thread, th);
        }
    }
}
