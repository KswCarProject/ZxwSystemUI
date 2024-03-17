package com.android.systemui.tv;

import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class TvBottomSheetActivity$$ExternalSyntheticLambda1 implements Consumer {
    public final /* synthetic */ TvBottomSheetActivity f$0;

    public /* synthetic */ TvBottomSheetActivity$$ExternalSyntheticLambda1(TvBottomSheetActivity tvBottomSheetActivity) {
        this.f$0 = tvBottomSheetActivity;
    }

    public final void accept(Object obj) {
        this.f$0.onBlurChanged(((Boolean) obj).booleanValue());
    }
}
