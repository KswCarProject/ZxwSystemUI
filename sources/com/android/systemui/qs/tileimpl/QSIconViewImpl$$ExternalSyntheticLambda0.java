package com.android.systemui.qs.tileimpl;

import android.widget.ImageView;
import com.android.systemui.plugins.qs.QSTile;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class QSIconViewImpl$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ QSIconViewImpl f$0;
    public final /* synthetic */ ImageView f$1;
    public final /* synthetic */ QSTile.State f$2;
    public final /* synthetic */ boolean f$3;

    public /* synthetic */ QSIconViewImpl$$ExternalSyntheticLambda0(QSIconViewImpl qSIconViewImpl, ImageView imageView, QSTile.State state, boolean z) {
        this.f$0 = qSIconViewImpl;
        this.f$1 = imageView;
        this.f$2 = state;
        this.f$3 = z;
    }

    public final void run() {
        this.f$0.lambda$setIcon$0(this.f$1, this.f$2, this.f$3);
    }
}
