package com.android.systemui.media;

import android.animation.ValueAnimator;
import com.android.internal.graphics.ColorUtils;
import java.util.ArrayList;

/* compiled from: IlluminationDrawable.kt */
public final class IlluminationDrawable$animateBackground$1$1 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ int $finalHighlight;
    public final /* synthetic */ int $initialBackground;
    public final /* synthetic */ int $initialHighlight;
    public final /* synthetic */ IlluminationDrawable this$0;

    public IlluminationDrawable$animateBackground$1$1(IlluminationDrawable illuminationDrawable, int i, int i2, int i3) {
        this.this$0 = illuminationDrawable;
        this.$initialBackground = i;
        this.$initialHighlight = i2;
        this.$finalHighlight = i3;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        Object animatedValue = valueAnimator.getAnimatedValue();
        if (animatedValue != null) {
            float floatValue = ((Float) animatedValue).floatValue();
            this.this$0.paint.setColor(ColorUtils.blendARGB(this.$initialBackground, this.this$0.backgroundColor, floatValue));
            this.this$0.highlightColor = ColorUtils.blendARGB(this.$initialHighlight, this.$finalHighlight, floatValue);
            ArrayList<LightSourceDrawable> access$getLightSources$p = this.this$0.lightSources;
            IlluminationDrawable illuminationDrawable = this.this$0;
            for (LightSourceDrawable highlightColor : access$getLightSources$p) {
                highlightColor.setHighlightColor(illuminationDrawable.highlightColor);
            }
            this.this$0.invalidateSelf();
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Float");
    }
}
