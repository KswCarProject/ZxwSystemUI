package com.android.systemui.media.taptotransfer.sender;

import android.media.MediaRoute2Info;
import android.view.View;
import com.android.internal.statusbar.IUndoMediaTransferCallback;

/* compiled from: ChipStateSender.kt */
public final class ChipStateSender$TRANSFER_TO_RECEIVER_SUCCEEDED$undoClickListener$1 implements View.OnClickListener {
    public final /* synthetic */ MediaTttChipControllerSender $controllerSender;
    public final /* synthetic */ MediaRoute2Info $routeInfo;
    public final /* synthetic */ MediaTttSenderUiEventLogger $uiEventLogger;
    public final /* synthetic */ IUndoMediaTransferCallback $undoCallback;

    public ChipStateSender$TRANSFER_TO_RECEIVER_SUCCEEDED$undoClickListener$1(MediaTttSenderUiEventLogger mediaTttSenderUiEventLogger, IUndoMediaTransferCallback iUndoMediaTransferCallback, MediaTttChipControllerSender mediaTttChipControllerSender, MediaRoute2Info mediaRoute2Info) {
        this.$uiEventLogger = mediaTttSenderUiEventLogger;
        this.$undoCallback = iUndoMediaTransferCallback;
        this.$controllerSender = mediaTttChipControllerSender;
        this.$routeInfo = mediaRoute2Info;
    }

    public final void onClick(View view) {
        this.$uiEventLogger.logUndoClicked(MediaTttSenderUiEvents.MEDIA_TTT_SENDER_UNDO_TRANSFER_TO_RECEIVER_CLICKED);
        this.$undoCallback.onUndoTriggered();
        this.$controllerSender.displayChip(new ChipSenderInfo(ChipStateSender.TRANSFER_TO_THIS_DEVICE_TRIGGERED, this.$routeInfo, this.$undoCallback));
    }
}
