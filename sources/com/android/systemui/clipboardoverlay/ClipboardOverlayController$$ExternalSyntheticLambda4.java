package com.android.systemui.clipboardoverlay;

import android.content.ClipData;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ClipboardOverlayController$$ExternalSyntheticLambda4 implements Runnable {
    public final /* synthetic */ ClipboardOverlayController f$0;
    public final /* synthetic */ ClipData f$1;
    public final /* synthetic */ String f$2;

    public /* synthetic */ ClipboardOverlayController$$ExternalSyntheticLambda4(ClipboardOverlayController clipboardOverlayController, ClipData clipData, String str) {
        this.f$0 = clipboardOverlayController;
        this.f$1 = clipData;
        this.f$2 = str;
    }

    public final void run() {
        this.f$0.lambda$setClipData$4(this.f$1, this.f$2);
    }
}
