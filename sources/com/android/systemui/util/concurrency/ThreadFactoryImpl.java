package com.android.systemui.util.concurrency;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import java.util.concurrent.Executor;

public class ThreadFactoryImpl implements ThreadFactory {
    public Looper buildLooperOnNewThread(String str) {
        HandlerThread handlerThread = new HandlerThread(str);
        handlerThread.start();
        return handlerThread.getLooper();
    }

    public Handler buildHandlerOnNewThread(String str) {
        return new Handler(buildLooperOnNewThread(str));
    }

    public Executor buildExecutorOnNewThread(String str) {
        return buildDelayableExecutorOnNewThread(str);
    }

    public DelayableExecutor buildDelayableExecutorOnNewThread(String str) {
        HandlerThread handlerThread = new HandlerThread(str);
        handlerThread.start();
        return buildDelayableExecutorOnLooper(handlerThread.getLooper());
    }

    public DelayableExecutor buildDelayableExecutorOnHandler(Handler handler) {
        return buildDelayableExecutorOnLooper(handler.getLooper());
    }

    public DelayableExecutor buildDelayableExecutorOnLooper(Looper looper) {
        return new ExecutorImpl(looper);
    }
}
