package com.android.systemui.tv;

import android.graphics.Rect;
import android.view.View;
import java.util.Collections;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class TvBottomSheetActivity$$ExternalSyntheticLambda0 implements View.OnLayoutChangeListener {
    public final /* synthetic */ View f$0;

    public /* synthetic */ TvBottomSheetActivity$$ExternalSyntheticLambda0(View view) {
        this.f$0 = view;
    }

    public final void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        this.f$0.setUnrestrictedPreferKeepClearRects(Collections.singletonList(new Rect(0, 0, i3 - i, i4 - i2)));
    }
}
