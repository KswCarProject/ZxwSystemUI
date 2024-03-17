package com.android.systemui.media.taptotransfer.receiver;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.media.MediaRoute2Info;

/* compiled from: MediaTttChipControllerReceiver.kt */
public final class MediaTttChipControllerReceiver$updateMediaTapToTransferReceiverDisplay$1 implements Icon.OnDrawableLoadedListener {
    public final /* synthetic */ CharSequence $appName;
    public final /* synthetic */ MediaRoute2Info $routeInfo;
    public final /* synthetic */ MediaTttChipControllerReceiver this$0;

    public MediaTttChipControllerReceiver$updateMediaTapToTransferReceiverDisplay$1(MediaTttChipControllerReceiver mediaTttChipControllerReceiver, MediaRoute2Info mediaRoute2Info, CharSequence charSequence) {
        this.this$0 = mediaTttChipControllerReceiver;
        this.$routeInfo = mediaRoute2Info;
        this.$appName = charSequence;
    }

    public final void onDrawableLoaded(Drawable drawable) {
        this.this$0.displayChip(new ChipReceiverInfo(this.$routeInfo, drawable, this.$appName));
    }
}
