package com.android.systemui.statusbar.notification.row;

import android.view.View;

/* compiled from: ChannelEditorDialogController.kt */
public final class ChannelEditorDialogController$initDialog$1$5 implements View.OnClickListener {
    public final /* synthetic */ ChannelEditorDialogController this$0;

    public ChannelEditorDialogController$initDialog$1$5(ChannelEditorDialogController channelEditorDialogController) {
        this.this$0 = channelEditorDialogController;
    }

    public final void onClick(View view) {
        this.this$0.launchSettings(view);
        this.this$0.done();
    }
}
