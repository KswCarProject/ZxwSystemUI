package com.android.systemui.statusbar.notification.row;

import android.view.View;

/* compiled from: ChannelEditorListView.kt */
public final class AppControlView$onFinishInflate$1 implements View.OnClickListener {
    public final /* synthetic */ AppControlView this$0;

    public AppControlView$onFinishInflate$1(AppControlView appControlView) {
        this.this$0 = appControlView;
    }

    public final void onClick(View view) {
        this.this$0.getSwitch().toggle();
    }
}
