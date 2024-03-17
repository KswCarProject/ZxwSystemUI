package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.view.ViewGroup;
import com.android.systemui.R$dimen;
import com.android.systemui.shared.animation.UnfoldConstantTranslateAnimator;
import com.android.systemui.unfold.util.NaturalRotationUnfoldProgressProvider;
import kotlin.Lazy;
import kotlin.LazyKt__LazyJVMKt;
import org.jetbrains.annotations.NotNull;

/* compiled from: NotificationPanelUnfoldAnimationController.kt */
public final class NotificationPanelUnfoldAnimationController {
    @NotNull
    public final Context context;
    @NotNull
    public final Lazy translateAnimator$delegate;

    public NotificationPanelUnfoldAnimationController(@NotNull Context context2, @NotNull NaturalRotationUnfoldProgressProvider naturalRotationUnfoldProgressProvider) {
        this.context = context2;
        this.translateAnimator$delegate = LazyKt__LazyJVMKt.lazy(new NotificationPanelUnfoldAnimationController$translateAnimator$2(naturalRotationUnfoldProgressProvider));
    }

    public final UnfoldConstantTranslateAnimator getTranslateAnimator() {
        return (UnfoldConstantTranslateAnimator) this.translateAnimator$delegate.getValue();
    }

    public final void setup(@NotNull ViewGroup viewGroup) {
        getTranslateAnimator().init(viewGroup, (float) this.context.getResources().getDimensionPixelSize(R$dimen.notification_side_paddings));
    }
}
