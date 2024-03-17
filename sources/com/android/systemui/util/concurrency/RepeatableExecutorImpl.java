package com.android.systemui.util.concurrency;

import java.util.concurrent.TimeUnit;

public class RepeatableExecutorImpl implements RepeatableExecutor {
    public final DelayableExecutor mExecutor;

    public RepeatableExecutorImpl(DelayableExecutor delayableExecutor) {
        this.mExecutor = delayableExecutor;
    }

    public void execute(Runnable runnable) {
        this.mExecutor.execute(runnable);
    }

    public Runnable executeRepeatedly(Runnable runnable, long j, long j2, TimeUnit timeUnit) {
        ExecutionToken executionToken = new ExecutionToken(runnable, j2, timeUnit);
        executionToken.start(j, timeUnit);
        return new RepeatableExecutorImpl$$ExternalSyntheticLambda0(executionToken);
    }

    public class ExecutionToken implements Runnable {
        public Runnable mCancel;
        public final Runnable mCommand;
        public final long mDelay;
        public final Object mLock = new Object();
        public final TimeUnit mUnit;

        public ExecutionToken(Runnable runnable, long j, TimeUnit timeUnit) {
            this.mCommand = runnable;
            this.mDelay = j;
            this.mUnit = timeUnit;
        }

        public void run() {
            this.mCommand.run();
            synchronized (this.mLock) {
                if (this.mCancel != null) {
                    this.mCancel = RepeatableExecutorImpl.this.mExecutor.executeDelayed(this, this.mDelay, this.mUnit);
                }
            }
        }

        public void start(long j, TimeUnit timeUnit) {
            synchronized (this.mLock) {
                this.mCancel = RepeatableExecutorImpl.this.mExecutor.executeDelayed(this, j, timeUnit);
            }
        }

        public void cancel() {
            synchronized (this.mLock) {
                Runnable runnable = this.mCancel;
                if (runnable != null) {
                    runnable.run();
                    this.mCancel = null;
                }
            }
        }
    }
}
