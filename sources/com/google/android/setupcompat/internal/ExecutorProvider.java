package com.google.android.setupcompat.internal;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class ExecutorProvider<T extends Executor> {
    public static final ExecutorProvider<ExecutorService> setupCompatServiceInvoker = new ExecutorProvider<>(createSizeBoundedExecutor("SetupCompatServiceInvoker", 50));
    public final T executor;
    public T injectedExecutor;

    public ExecutorProvider(T t) {
        this.executor = t;
    }

    public T get() {
        T t = this.injectedExecutor;
        if (t != null) {
            return t;
        }
        return this.executor;
    }

    public void injectExecutor(T t) {
        this.injectedExecutor = t;
    }

    public static void resetExecutors() {
        setupCompatServiceInvoker.injectedExecutor = null;
    }

    public static ExecutorService createSizeBoundedExecutor(String str, int i) {
        return new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS, new ArrayBlockingQueue(i), new ExecutorProvider$$ExternalSyntheticLambda0(str));
    }

    public static /* synthetic */ Thread lambda$createSizeBoundedExecutor$0(String str, Runnable runnable) {
        return new Thread(runnable, str);
    }
}
