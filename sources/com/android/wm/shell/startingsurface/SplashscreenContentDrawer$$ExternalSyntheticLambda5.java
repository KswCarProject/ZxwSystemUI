package com.android.wm.shell.startingsurface;

import android.content.res.TypedArray;
import java.util.function.UnaryOperator;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SplashscreenContentDrawer$$ExternalSyntheticLambda5 implements UnaryOperator {
    public final /* synthetic */ TypedArray f$0;

    public /* synthetic */ SplashscreenContentDrawer$$ExternalSyntheticLambda5(TypedArray typedArray) {
        this.f$0 = typedArray;
    }

    public final Object apply(Object obj) {
        return Integer.valueOf(this.f$0.getColor(60, ((Integer) obj).intValue()));
    }
}
