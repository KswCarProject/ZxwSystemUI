package com.android.systemui.statusbar.notification.row;

import android.app.NotificationChannel;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: ChannelEditorDialogController.kt */
public final class ChannelEditorDialogController$padToFourChannels$1 extends Lambda implements Function1<NotificationChannel, Boolean> {
    public final /* synthetic */ ChannelEditorDialogController this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public ChannelEditorDialogController$padToFourChannels$1(ChannelEditorDialogController channelEditorDialogController) {
        super(1);
        this.this$0 = channelEditorDialogController;
    }

    @NotNull
    public final Boolean invoke(@NotNull NotificationChannel notificationChannel) {
        return Boolean.valueOf(this.this$0.getPaddedChannels$frameworks__base__packages__SystemUI__android_common__SystemUI_core().contains(notificationChannel));
    }
}
