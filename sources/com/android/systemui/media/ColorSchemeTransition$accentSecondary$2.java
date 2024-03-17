package com.android.systemui.media;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;

/* compiled from: ColorSchemeTransition.kt */
public final class ColorSchemeTransition$accentSecondary$2 extends Lambda implements Function1<Integer, Unit> {
    public final /* synthetic */ ColorSchemeTransition this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public ColorSchemeTransition$accentSecondary$2(ColorSchemeTransition colorSchemeTransition) {
        super(1);
        this.this$0 = colorSchemeTransition;
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        invoke(((Number) obj).intValue());
        return Unit.INSTANCE;
    }

    public final void invoke(int i) {
        ColorStateList valueOf = ColorStateList.valueOf(i);
        Drawable background = this.this$0.mediaViewHolder.getSeamlessButton().getBackground();
        RippleDrawable rippleDrawable = background instanceof RippleDrawable ? (RippleDrawable) background : null;
        if (rippleDrawable != null) {
            rippleDrawable.setColor(valueOf);
            rippleDrawable.setEffectColor(valueOf);
        }
    }
}
