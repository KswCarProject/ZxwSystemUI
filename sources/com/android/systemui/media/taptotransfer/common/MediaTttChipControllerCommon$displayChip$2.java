package com.android.systemui.media.taptotransfer.common;

/* compiled from: MediaTttChipControllerCommon.kt */
public final class MediaTttChipControllerCommon$displayChip$2 implements Runnable {
    public final /* synthetic */ MediaTttChipControllerCommon<T> this$0;

    public MediaTttChipControllerCommon$displayChip$2(MediaTttChipControllerCommon<T> mediaTttChipControllerCommon) {
        this.this$0 = mediaTttChipControllerCommon;
    }

    public final void run() {
        this.this$0.removeChip("TIMEOUT");
    }
}
