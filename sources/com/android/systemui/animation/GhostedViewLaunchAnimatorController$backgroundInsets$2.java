package com.android.systemui.animation;

import android.graphics.Insets;
import android.graphics.drawable.Drawable;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

/* compiled from: GhostedViewLaunchAnimatorController.kt */
public final class GhostedViewLaunchAnimatorController$backgroundInsets$2 extends Lambda implements Function0<Insets> {
    public final /* synthetic */ GhostedViewLaunchAnimatorController this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public GhostedViewLaunchAnimatorController$backgroundInsets$2(GhostedViewLaunchAnimatorController ghostedViewLaunchAnimatorController) {
        super(0);
        this.this$0 = ghostedViewLaunchAnimatorController;
    }

    public final Insets invoke() {
        Drawable access$getBackground$p = this.this$0.background;
        Insets opticalInsets = access$getBackground$p == null ? null : access$getBackground$p.getOpticalInsets();
        return opticalInsets == null ? Insets.NONE : opticalInsets;
    }
}
