package com.android.systemui.media.taptotransfer.sender;

import android.media.MediaRoute2Info;
import com.android.internal.statusbar.IUndoMediaTransferCallback;
import com.android.systemui.statusbar.CommandQueue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: MediaTttChipControllerSender.kt */
public final class MediaTttChipControllerSender$commandQueueCallbacks$1 implements CommandQueue.Callbacks {
    public final /* synthetic */ MediaTttChipControllerSender this$0;

    public MediaTttChipControllerSender$commandQueueCallbacks$1(MediaTttChipControllerSender mediaTttChipControllerSender) {
        this.this$0 = mediaTttChipControllerSender;
    }

    public void updateMediaTapToTransferSenderDisplay(int i, @NotNull MediaRoute2Info mediaRoute2Info, @Nullable IUndoMediaTransferCallback iUndoMediaTransferCallback) {
        this.this$0.updateMediaTapToTransferSenderDisplay(i, mediaRoute2Info, iUndoMediaTransferCallback);
    }
}
