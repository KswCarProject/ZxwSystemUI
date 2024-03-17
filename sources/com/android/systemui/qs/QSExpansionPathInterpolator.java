package com.android.systemui.qs;

import android.view.animation.Interpolator;
import org.jetbrains.annotations.NotNull;

/* compiled from: QSExpansionPathInterpolator.kt */
public final class QSExpansionPathInterpolator {
    @NotNull
    public PathInterpolatorBuilder pathInterpolatorBuilder = new PathInterpolatorBuilder(0.0f, 0.0f, 0.0f, 1.0f);

    @NotNull
    public final Interpolator getXInterpolator() {
        return this.pathInterpolatorBuilder.getXInterpolator();
    }

    @NotNull
    public final Interpolator getYInterpolator() {
        return this.pathInterpolatorBuilder.getYInterpolator();
    }
}
