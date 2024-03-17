package com.android.systemui.dump;

import java.lang.Thread;

/* compiled from: DumpHandler.kt */
public final class DumpHandler$init$1 implements Thread.UncaughtExceptionHandler {
    public final /* synthetic */ DumpHandler this$0;

    public DumpHandler$init$1(DumpHandler dumpHandler) {
        this.this$0 = dumpHandler;
    }

    public final void uncaughtException(Thread thread, Throwable th) {
        if (th instanceof Exception) {
            this.this$0.logBufferEulogizer.record((Exception) th);
        }
    }
}
