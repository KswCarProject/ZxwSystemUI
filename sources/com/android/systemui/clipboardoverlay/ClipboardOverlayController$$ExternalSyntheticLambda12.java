package com.android.systemui.clipboardoverlay;

import java.util.ArrayList;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ClipboardOverlayController$$ExternalSyntheticLambda12 implements Runnable {
    public final /* synthetic */ ClipboardOverlayController f$0;
    public final /* synthetic */ ArrayList f$1;
    public final /* synthetic */ String f$2;

    public /* synthetic */ ClipboardOverlayController$$ExternalSyntheticLambda12(ClipboardOverlayController clipboardOverlayController, ArrayList arrayList, String str) {
        this.f$0 = clipboardOverlayController;
        this.f$1 = arrayList;
        this.f$2 = str;
    }

    public final void run() {
        this.f$0.lambda$classifyText$7(this.f$1, this.f$2);
    }
}
