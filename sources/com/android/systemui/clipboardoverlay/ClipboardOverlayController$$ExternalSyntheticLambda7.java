package com.android.systemui.clipboardoverlay;

import android.net.Uri;
import android.view.View;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ClipboardOverlayController$$ExternalSyntheticLambda7 implements View.OnClickListener {
    public final /* synthetic */ ClipboardOverlayController f$0;
    public final /* synthetic */ Uri f$1;

    public /* synthetic */ ClipboardOverlayController$$ExternalSyntheticLambda7(ClipboardOverlayController clipboardOverlayController, Uri uri) {
        this.f$0 = clipboardOverlayController;
        this.f$1 = uri;
    }

    public final void onClick(View view) {
        this.f$0.lambda$tryShowEditableImage$12(this.f$1, view);
    }
}
