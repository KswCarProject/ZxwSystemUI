package com.android.wm.shell.pip.phone;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class PipMenuView$$ExternalSyntheticLambda6 implements Icon.OnDrawableLoadedListener {
    public final /* synthetic */ PipMenuActionView f$0;

    public /* synthetic */ PipMenuView$$ExternalSyntheticLambda6(PipMenuActionView pipMenuActionView) {
        this.f$0 = pipMenuActionView;
    }

    public final void onDrawableLoaded(Drawable drawable) {
        PipMenuView.lambda$updateActionViews$6(this.f$0, drawable);
    }
}
