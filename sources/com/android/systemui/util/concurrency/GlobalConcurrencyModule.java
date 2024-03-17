package com.android.systemui.util.concurrency;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public abstract class GlobalConcurrencyModule {
    public static Looper provideMainLooper() {
        return Looper.getMainLooper();
    }

    public static Handler provideMainHandler(Looper looper) {
        return new Handler(looper);
    }

    @Deprecated
    public static Handler provideHandler() {
        return new Handler();
    }

    public static Executor provideUiBackgroundExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    public static Executor provideMainExecutor(Context context) {
        return context.getMainExecutor();
    }

    public static DelayableExecutor provideMainDelayableExecutor(Looper looper) {
        return new ExecutorImpl(looper);
    }
}
