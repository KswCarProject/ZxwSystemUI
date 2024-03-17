package com.android.systemui.statusbar.notification.row;

import android.view.View;
import android.widget.Switch;

/* compiled from: ChannelEditorListView.kt */
public final class ChannelRow$onFinishInflate$2 implements View.OnClickListener {
    public final /* synthetic */ ChannelRow this$0;

    public ChannelRow$onFinishInflate$2(ChannelRow channelRow) {
        this.this$0 = channelRow;
    }

    public final void onClick(View view) {
        Switch access$getSwitch$p = this.this$0.f8switch;
        if (access$getSwitch$p == null) {
            access$getSwitch$p = null;
        }
        access$getSwitch$p.toggle();
    }
}
