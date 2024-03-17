package com.android.systemui.media.taptotransfer;

import android.util.Log;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: MediaTttCommandLineHelper.kt */
public final class MediaTttCommandLineHelper$SenderCommand$getUndoCallback$1 implements Runnable {
    public final /* synthetic */ int $displayState;

    public MediaTttCommandLineHelper$SenderCommand$getUndoCallback$1(int i) {
        this.$displayState = i;
    }

    public final void run() {
        Log.i("MediaTransferCli", Intrinsics.stringPlus("Undo triggered for ", Integer.valueOf(this.$displayState)));
    }
}
