package com.android.systemui.media.taptotransfer.receiver;

import android.graphics.drawable.Icon;
import android.media.MediaRoute2Info;
import com.android.systemui.statusbar.CommandQueue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: MediaTttChipControllerReceiver.kt */
public final class MediaTttChipControllerReceiver$commandQueueCallbacks$1 implements CommandQueue.Callbacks {
    public final /* synthetic */ MediaTttChipControllerReceiver this$0;

    public MediaTttChipControllerReceiver$commandQueueCallbacks$1(MediaTttChipControllerReceiver mediaTttChipControllerReceiver) {
        this.this$0 = mediaTttChipControllerReceiver;
    }

    public void updateMediaTapToTransferReceiverDisplay(int i, @NotNull MediaRoute2Info mediaRoute2Info, @Nullable Icon icon, @Nullable CharSequence charSequence) {
        this.this$0.updateMediaTapToTransferReceiverDisplay(i, mediaRoute2Info, icon, charSequence);
    }
}
