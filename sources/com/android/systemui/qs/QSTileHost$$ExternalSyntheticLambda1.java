package com.android.systemui.qs;

import com.android.systemui.Dumpable;
import com.android.systemui.plugins.qs.QSTile;
import java.io.PrintWriter;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class QSTileHost$$ExternalSyntheticLambda1 implements Consumer {
    public final /* synthetic */ PrintWriter f$0;
    public final /* synthetic */ String[] f$1;

    public /* synthetic */ QSTileHost$$ExternalSyntheticLambda1(PrintWriter printWriter, String[] strArr) {
        this.f$0 = printWriter;
        this.f$1 = strArr;
    }

    public final void accept(Object obj) {
        ((Dumpable) ((QSTile) obj)).dump(this.f$0, this.f$1);
    }
}
