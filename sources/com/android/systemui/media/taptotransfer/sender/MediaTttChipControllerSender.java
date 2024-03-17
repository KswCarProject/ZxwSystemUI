package com.android.systemui.media.taptotransfer.sender;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaRoute2Info;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import com.android.internal.statusbar.IUndoMediaTransferCallback;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.media.taptotransfer.common.MediaTttChipControllerCommon;
import com.android.systemui.media.taptotransfer.common.MediaTttLogger;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.gesture.TapGestureDetector;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.view.ViewUtil;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: MediaTttChipControllerSender.kt */
public final class MediaTttChipControllerSender extends MediaTttChipControllerCommon<ChipSenderInfo> {
    @NotNull
    public final MediaTttChipControllerSender$commandQueueCallbacks$1 commandQueueCallbacks;
    @Nullable
    public ChipStateSender currentlyDisplayedChipState;
    @NotNull
    public final MediaTttSenderUiEventLogger uiEventLogger;

    public final int visibleIfTrue(boolean z) {
        return z ? 0 : 8;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public MediaTttChipControllerSender(@NotNull CommandQueue commandQueue, @NotNull Context context, @NotNull MediaTttLogger mediaTttLogger, @NotNull WindowManager windowManager, @NotNull ViewUtil viewUtil, @NotNull DelayableExecutor delayableExecutor, @NotNull TapGestureDetector tapGestureDetector, @NotNull PowerManager powerManager, @NotNull MediaTttSenderUiEventLogger mediaTttSenderUiEventLogger) {
        super(context, mediaTttLogger, windowManager, viewUtil, delayableExecutor, tapGestureDetector, powerManager, R$layout.media_ttt_chip);
        this.uiEventLogger = mediaTttSenderUiEventLogger;
        MediaTttChipControllerSender$commandQueueCallbacks$1 mediaTttChipControllerSender$commandQueueCallbacks$1 = new MediaTttChipControllerSender$commandQueueCallbacks$1(this);
        this.commandQueueCallbacks = mediaTttChipControllerSender$commandQueueCallbacks$1;
        CommandQueue commandQueue2 = commandQueue;
        commandQueue.addCallback((CommandQueue.Callbacks) mediaTttChipControllerSender$commandQueueCallbacks$1);
    }

    public final void updateMediaTapToTransferSenderDisplay(int i, MediaRoute2Info mediaRoute2Info, IUndoMediaTransferCallback iUndoMediaTransferCallback) {
        String name;
        ChipStateSender senderStateFromId = ChipStateSender.Companion.getSenderStateFromId(i);
        String str = "Invalid";
        if (!(senderStateFromId == null || (name = senderStateFromId.name()) == null)) {
            str = name;
        }
        getLogger$frameworks__base__packages__SystemUI__android_common__SystemUI_core().logStateChange(str, mediaRoute2Info.getId());
        if (senderStateFromId == null) {
            Log.e("MediaTapToTransferSender", Intrinsics.stringPlus("Unhandled MediaTransferSenderState ", Integer.valueOf(i)));
            return;
        }
        this.uiEventLogger.logSenderStateChange(senderStateFromId);
        ChipStateSender chipStateSender = ChipStateSender.FAR_FROM_RECEIVER;
        if (senderStateFromId == chipStateSender) {
            String simpleName = Reflection.getOrCreateKotlinClass(chipStateSender.getClass()).getSimpleName();
            Intrinsics.checkNotNull(simpleName);
            removeChip(simpleName);
            return;
        }
        displayChip(new ChipSenderInfo(senderStateFromId, mediaRoute2Info, iUndoMediaTransferCallback));
    }

    public void updateChipView(@NotNull ChipSenderInfo chipSenderInfo, @NotNull ViewGroup viewGroup) {
        ChipStateSender state = chipSenderInfo.getState();
        this.currentlyDisplayedChipState = state;
        MediaTttChipControllerCommon.setIcon$frameworks__base__packages__SystemUI__android_common__SystemUI_core$default(this, viewGroup, chipSenderInfo.getRouteInfo().getPackageName(), (Drawable) null, (CharSequence) null, 12, (Object) null);
        String obj = chipSenderInfo.getRouteInfo().getName().toString();
        TextView textView = (TextView) viewGroup.requireViewById(R$id.text);
        textView.setText(state.getChipTextString(textView.getContext(), obj));
        viewGroup.requireViewById(R$id.loading).setVisibility(visibleIfTrue(state.isMidTransfer()));
        View requireViewById = viewGroup.requireViewById(R$id.undo);
        View.OnClickListener undoClickListener = state.undoClickListener(this, chipSenderInfo.getRouteInfo(), chipSenderInfo.getUndoCallback(), this.uiEventLogger);
        requireViewById.setOnClickListener(undoClickListener);
        requireViewById.setVisibility(visibleIfTrue(undoClickListener != null));
        viewGroup.requireViewById(R$id.failure_icon).setVisibility(visibleIfTrue(state.isTransferFailure()));
    }

    public void removeChip(@NotNull String str) {
        ChipStateSender chipStateSender = this.currentlyDisplayedChipState;
        boolean z = false;
        if (chipStateSender != null && chipStateSender.isMidTransfer()) {
            z = true;
        }
        if (!z || Intrinsics.areEqual((Object) str, (Object) "TIMEOUT")) {
            super.removeChip(str);
            this.currentlyDisplayedChipState = null;
        }
    }
}
