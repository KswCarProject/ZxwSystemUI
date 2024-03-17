package com.android.systemui.biometrics;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import com.android.systemui.R$id;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: UdfpsFpmOtherView.kt */
public final class UdfpsFpmOtherView extends UdfpsAnimationView {
    @NotNull
    public final UdfpsFpDrawable fingerprintDrawable;
    public ImageView fingerprintView;

    public UdfpsFpmOtherView(@NotNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        this.fingerprintDrawable = new UdfpsFpDrawable(context);
    }

    public void onFinishInflate() {
        View findViewById = findViewById(R$id.udfps_fpm_other_fp_view);
        Intrinsics.checkNotNull(findViewById);
        ImageView imageView = (ImageView) findViewById;
        this.fingerprintView = imageView;
        if (imageView == null) {
            imageView = null;
        }
        imageView.setImageDrawable(this.fingerprintDrawable);
    }

    @NotNull
    public UdfpsDrawable getDrawable() {
        return this.fingerprintDrawable;
    }
}
