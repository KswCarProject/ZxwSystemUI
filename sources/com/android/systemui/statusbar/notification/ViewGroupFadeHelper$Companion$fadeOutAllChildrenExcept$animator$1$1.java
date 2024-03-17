package com.android.systemui.statusbar.notification;

import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;
import com.android.systemui.R$id;
import java.util.Set;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: ViewGroupFadeHelper.kt */
public final class ViewGroupFadeHelper$Companion$fadeOutAllChildrenExcept$animator$1$1 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ ViewGroup $root;
    public final /* synthetic */ Set<View> $viewsToFadeOut;

    public ViewGroupFadeHelper$Companion$fadeOutAllChildrenExcept$animator$1$1(ViewGroup viewGroup, Set<View> set) {
        this.$root = viewGroup;
        this.$viewsToFadeOut = set;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        Float f = (Float) this.$root.getTag(R$id.view_group_fade_helper_previous_value_tag);
        Object animatedValue = valueAnimator.getAnimatedValue();
        if (animatedValue != null) {
            float floatValue = ((Float) animatedValue).floatValue();
            for (View next : this.$viewsToFadeOut) {
                if (!Intrinsics.areEqual(next.getAlpha(), f)) {
                    next.setTag(R$id.view_group_fade_helper_restore_tag, Float.valueOf(next.getAlpha()));
                }
                next.setAlpha(floatValue);
            }
            this.$root.setTag(R$id.view_group_fade_helper_previous_value_tag, Float.valueOf(floatValue));
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Float");
    }
}
