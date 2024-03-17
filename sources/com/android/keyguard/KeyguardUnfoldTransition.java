package com.android.keyguard;

import android.content.Context;
import android.view.ViewGroup;
import com.android.systemui.R$dimen;
import com.android.systemui.shared.animation.UnfoldConstantTranslateAnimator;
import com.android.systemui.unfold.util.NaturalRotationUnfoldProgressProvider;
import kotlin.Lazy;
import kotlin.LazyKt__LazyJVMKt;
import kotlin.jvm.functions.Function0;
import org.jetbrains.annotations.NotNull;

/* compiled from: KeyguardUnfoldTransition.kt */
public final class KeyguardUnfoldTransition {
    @NotNull
    public final Context context;
    @NotNull
    public final Function0<Boolean> filterNever = KeyguardUnfoldTransition$filterNever$1.INSTANCE;
    @NotNull
    public final Function0<Boolean> filterSplitShadeOnly = new KeyguardUnfoldTransition$filterSplitShadeOnly$1(this);
    public boolean statusViewCentered;
    @NotNull
    public final Lazy translateAnimator$delegate;

    public KeyguardUnfoldTransition(@NotNull Context context2, @NotNull NaturalRotationUnfoldProgressProvider naturalRotationUnfoldProgressProvider) {
        this.context = context2;
        this.translateAnimator$delegate = LazyKt__LazyJVMKt.lazy(new KeyguardUnfoldTransition$translateAnimator$2(this, naturalRotationUnfoldProgressProvider));
    }

    public final boolean getStatusViewCentered() {
        return this.statusViewCentered;
    }

    public final void setStatusViewCentered(boolean z) {
        this.statusViewCentered = z;
    }

    public final UnfoldConstantTranslateAnimator getTranslateAnimator() {
        return (UnfoldConstantTranslateAnimator) this.translateAnimator$delegate.getValue();
    }

    public final void setup(@NotNull ViewGroup viewGroup) {
        getTranslateAnimator().init(viewGroup, (float) this.context.getResources().getDimensionPixelSize(R$dimen.keyguard_unfold_translation_x));
    }
}
