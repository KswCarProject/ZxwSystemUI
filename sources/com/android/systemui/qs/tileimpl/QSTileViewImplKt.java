package com.android.systemui.qs.tileimpl;

import android.animation.ArgbEvaluator;
import android.animation.PropertyValuesHolder;
import java.util.Arrays;

/* compiled from: QSTileViewImpl.kt */
public final class QSTileViewImplKt {
    public static final float constrainSquishiness(float f) {
        return (f * 0.9f) + 0.1f;
    }

    public static final PropertyValuesHolder colorValuesHolder(String str, int... iArr) {
        PropertyValuesHolder ofInt = PropertyValuesHolder.ofInt(str, Arrays.copyOf(iArr, iArr.length));
        ofInt.setEvaluator(ArgbEvaluator.getInstance());
        return ofInt;
    }
}
