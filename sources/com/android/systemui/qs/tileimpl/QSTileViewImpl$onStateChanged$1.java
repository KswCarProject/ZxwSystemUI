package com.android.systemui.qs.tileimpl;

import com.android.systemui.plugins.qs.QSTile;

/* compiled from: QSTileViewImpl.kt */
public final class QSTileViewImpl$onStateChanged$1 implements Runnable {
    public final /* synthetic */ QSTile.State $state;
    public final /* synthetic */ QSTileViewImpl this$0;

    public QSTileViewImpl$onStateChanged$1(QSTileViewImpl qSTileViewImpl, QSTile.State state) {
        this.this$0 = qSTileViewImpl;
        this.$state = state;
    }

    public final void run() {
        this.this$0.handleStateChanged(this.$state);
    }
}
