package com.android.systemui.statusbar.events;

import android.view.View;
import org.jetbrains.annotations.NotNull;

/* compiled from: SystemEventChipAnimationController.kt */
public interface BackgroundAnimatableView {
    int getChipWidth();

    @NotNull
    View getView();

    void setBoundsForAnimation(int i, int i2, int i3, int i4);

    /* compiled from: SystemEventChipAnimationController.kt */
    public static final class DefaultImpls {
        @NotNull
        public static View getView(@NotNull BackgroundAnimatableView backgroundAnimatableView) {
            return (View) backgroundAnimatableView;
        }

        public static int getChipWidth(@NotNull BackgroundAnimatableView backgroundAnimatableView) {
            return backgroundAnimatableView.getView().getMeasuredWidth();
        }
    }
}
