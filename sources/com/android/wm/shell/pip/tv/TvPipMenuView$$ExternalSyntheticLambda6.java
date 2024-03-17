package com.android.wm.shell.pip.tv;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class TvPipMenuView$$ExternalSyntheticLambda6 implements Icon.OnDrawableLoadedListener {
    public final /* synthetic */ TvPipMenuActionButton f$0;

    public /* synthetic */ TvPipMenuView$$ExternalSyntheticLambda6(TvPipMenuActionButton tvPipMenuActionButton) {
        this.f$0 = tvPipMenuActionButton;
    }

    public final void onDrawableLoaded(Drawable drawable) {
        this.f$0.setImageDrawable(drawable);
    }
}
