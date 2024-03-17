package com.android.systemui.flags;

import java.io.PrintWriter;
import java.util.function.BiConsumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class FeatureFlagsDebug$$ExternalSyntheticLambda4 implements BiConsumer {
    public final /* synthetic */ PrintWriter f$0;

    public /* synthetic */ FeatureFlagsDebug$$ExternalSyntheticLambda4(PrintWriter printWriter) {
        this.f$0 = printWriter;
    }

    public final void accept(Object obj, Object obj2) {
        this.f$0.println("  sysui_flag_" + ((Integer) obj) + ": [length=" + ((String) obj2).length() + "] \"" + ((String) obj2) + "\"");
    }
}
