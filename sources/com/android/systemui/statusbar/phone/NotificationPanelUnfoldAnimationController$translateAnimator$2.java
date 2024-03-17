package com.android.systemui.statusbar.phone;

import com.android.systemui.R$id;
import com.android.systemui.shared.animation.UnfoldConstantTranslateAnimator;
import com.android.systemui.unfold.util.NaturalRotationUnfoldProgressProvider;
import kotlin.collections.SetsKt__SetsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: NotificationPanelUnfoldAnimationController.kt */
public final class NotificationPanelUnfoldAnimationController$translateAnimator$2 extends Lambda implements Function0<UnfoldConstantTranslateAnimator> {
    public final /* synthetic */ NaturalRotationUnfoldProgressProvider $progressProvider;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public NotificationPanelUnfoldAnimationController$translateAnimator$2(NaturalRotationUnfoldProgressProvider naturalRotationUnfoldProgressProvider) {
        super(0);
        this.$progressProvider = naturalRotationUnfoldProgressProvider;
    }

    @NotNull
    public final UnfoldConstantTranslateAnimator invoke() {
        int i = R$id.quick_settings_panel;
        UnfoldConstantTranslateAnimator.Direction direction = UnfoldConstantTranslateAnimator.Direction.LEFT;
        int i2 = R$id.notification_stack_scroller;
        UnfoldConstantTranslateAnimator.Direction direction2 = UnfoldConstantTranslateAnimator.Direction.RIGHT;
        UnfoldConstantTranslateAnimator.Direction direction3 = direction;
        return new UnfoldConstantTranslateAnimator(SetsKt__SetsKt.setOf(new UnfoldConstantTranslateAnimator.ViewIdToTranslate(i, direction, (Function0) null, 4, (DefaultConstructorMarker) null), new UnfoldConstantTranslateAnimator.ViewIdToTranslate(i2, direction2, (Function0) null, 4, (DefaultConstructorMarker) null), new UnfoldConstantTranslateAnimator.ViewIdToTranslate(R$id.rightLayout, direction2, (Function0) null, 4, (DefaultConstructorMarker) null), new UnfoldConstantTranslateAnimator.ViewIdToTranslate(R$id.clock, direction3, (Function0) null, 4, (DefaultConstructorMarker) null), new UnfoldConstantTranslateAnimator.ViewIdToTranslate(R$id.date, direction3, (Function0) null, 4, (DefaultConstructorMarker) null)), this.$progressProvider);
    }
}
