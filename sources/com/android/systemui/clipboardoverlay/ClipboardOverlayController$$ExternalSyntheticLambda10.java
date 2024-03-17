package com.android.systemui.clipboardoverlay;

import android.view.View;
import android.widget.TextView;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ClipboardOverlayController$$ExternalSyntheticLambda10 implements View.OnLayoutChangeListener {
    public final /* synthetic */ ClipboardOverlayController f$0;
    public final /* synthetic */ CharSequence f$1;
    public final /* synthetic */ TextView f$2;

    public /* synthetic */ ClipboardOverlayController$$ExternalSyntheticLambda10(ClipboardOverlayController clipboardOverlayController, CharSequence charSequence, TextView textView) {
        this.f$0 = clipboardOverlayController;
        this.f$1 = charSequence;
        this.f$2 = textView;
    }

    public final void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        this.f$0.lambda$showTextPreview$10(this.f$1, this.f$2, view, i, i2, i3, i4, i5, i6, i7, i8);
    }
}
