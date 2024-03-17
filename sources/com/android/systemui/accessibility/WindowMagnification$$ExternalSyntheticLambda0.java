package com.android.systemui.accessibility;

import java.io.PrintWriter;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class WindowMagnification$$ExternalSyntheticLambda0 implements Consumer {
    public final /* synthetic */ PrintWriter f$0;

    public /* synthetic */ WindowMagnification$$ExternalSyntheticLambda0(PrintWriter printWriter) {
        this.f$0 = printWriter;
    }

    public final void accept(Object obj) {
        ((WindowMagnificationController) obj).dump(this.f$0);
    }
}
