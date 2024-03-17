package com.android.systemui.clipboardoverlay;

import android.content.Intent;
import android.view.View;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ClipboardOverlayController$$ExternalSyntheticLambda5 implements View.OnClickListener {
    public final /* synthetic */ ClipboardOverlayController f$0;
    public final /* synthetic */ Intent f$1;

    public /* synthetic */ ClipboardOverlayController$$ExternalSyntheticLambda5(ClipboardOverlayController clipboardOverlayController, Intent intent) {
        this.f$0 = clipboardOverlayController;
        this.f$1 = intent;
    }

    public final void onClick(View view) {
        this.f$0.lambda$setClipData$5(this.f$1, view);
    }
}
