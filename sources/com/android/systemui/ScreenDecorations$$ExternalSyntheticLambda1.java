package com.android.systemui;

import android.view.View;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ScreenDecorations$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ ScreenDecorations f$0;
    public final /* synthetic */ View f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ ScreenDecorations$$ExternalSyntheticLambda1(ScreenDecorations screenDecorations, View view, int i) {
        this.f$0 = screenDecorations;
        this.f$1 = view;
        this.f$2 = i;
    }

    public final void run() {
        this.f$0.lambda$setOverlayWindowVisibilityIfViewExist$0(this.f$1, this.f$2);
    }
}
