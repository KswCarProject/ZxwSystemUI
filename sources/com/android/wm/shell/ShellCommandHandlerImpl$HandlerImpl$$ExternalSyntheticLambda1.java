package com.android.wm.shell;

import com.android.wm.shell.ShellCommandHandlerImpl;
import java.io.PrintWriter;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ShellCommandHandlerImpl$HandlerImpl$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ ShellCommandHandlerImpl.HandlerImpl f$0;
    public final /* synthetic */ PrintWriter f$1;

    public /* synthetic */ ShellCommandHandlerImpl$HandlerImpl$$ExternalSyntheticLambda1(ShellCommandHandlerImpl.HandlerImpl handlerImpl, PrintWriter printWriter) {
        this.f$0 = handlerImpl;
        this.f$1 = printWriter;
    }

    public final void run() {
        this.f$0.lambda$dump$0(this.f$1);
    }
}
