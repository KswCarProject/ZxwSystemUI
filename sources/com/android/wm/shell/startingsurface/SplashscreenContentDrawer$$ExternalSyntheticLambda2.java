package com.android.wm.shell.startingsurface;

import android.content.res.TypedArray;
import java.util.function.UnaryOperator;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SplashscreenContentDrawer$$ExternalSyntheticLambda2 implements UnaryOperator {
    public final /* synthetic */ TypedArray f$0;

    public /* synthetic */ SplashscreenContentDrawer$$ExternalSyntheticLambda2(TypedArray typedArray) {
        this.f$0 = typedArray;
    }

    public final Object apply(Object obj) {
        return Integer.valueOf(this.f$0.getColor(56, ((Integer) obj).intValue()));
    }
}
