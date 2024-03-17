package com.android.systemui.clipboardoverlay;

import android.content.ClipData;
import android.view.View;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ClipboardOverlayController$$ExternalSyntheticLambda9 implements View.OnClickListener {
    public final /* synthetic */ ClipboardOverlayController f$0;
    public final /* synthetic */ ClipData f$1;

    public /* synthetic */ ClipboardOverlayController$$ExternalSyntheticLambda9(ClipboardOverlayController clipboardOverlayController, ClipData clipData) {
        this.f$0 = clipboardOverlayController;
        this.f$1 = clipData;
    }

    public final void onClick(View view) {
        this.f$0.lambda$showShareChip$8(this.f$1, view);
    }
}
