package com.android.systemui.util.concurrency;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

public class ExecutorImpl implements DelayableExecutor {
    public final Handler mHandler;

    public ExecutorImpl(Looper looper) {
        this.mHandler = new Handler(looper, new ExecutorImpl$$ExternalSyntheticLambda0(this));
    }

    public void execute(Runnable runnable) {
        if (!this.mHandler.post(runnable)) {
            throw new RejectedExecutionException(this.mHandler + " is shutting down");
        }
    }

    public Runnable executeDelayed(Runnable runnable, long j, TimeUnit timeUnit) {
        ExecutionToken executionToken = new ExecutionToken(runnable);
        this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(0, executionToken), timeUnit.toMillis(j));
        return executionToken;
    }

    public Runnable executeAtTime(Runnable runnable, long j, TimeUnit timeUnit) {
        ExecutionToken executionToken = new ExecutionToken(runnable);
        this.mHandler.sendMessageAtTime(this.mHandler.obtainMessage(0, executionToken), timeUnit.toMillis(j));
        return executionToken;
    }

    public final boolean onHandleMessage(Message message) {
        if (message.what == 0) {
            ((ExecutionToken) message.obj).runnable.run();
            return true;
        }
        throw new IllegalStateException("Unrecognized message: " + message.what);
    }

    public class ExecutionToken implements Runnable {
        public final Runnable runnable;

        public ExecutionToken(Runnable runnable2) {
            this.runnable = runnable2;
        }

        public void run() {
            ExecutorImpl.this.mHandler.removeCallbacksAndMessages(this);
        }
    }
}
