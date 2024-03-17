package com.android.systemui.biometrics;

import android.content.Context;
import android.util.AttributeSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: UdfpsBpView.kt */
public final class UdfpsBpView extends UdfpsAnimationView {
    @NotNull
    public final UdfpsFpDrawable fingerprintDrawable;

    public UdfpsBpView(@NotNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        this.fingerprintDrawable = new UdfpsFpDrawable(context);
    }

    @NotNull
    public UdfpsDrawable getDrawable() {
        return this.fingerprintDrawable;
    }
}
