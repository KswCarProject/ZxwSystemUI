package com.android.systemui.unfold.util;

import android.animation.ValueAnimator;
import android.content.ContentResolver;
import android.provider.Settings;
import com.android.systemui.unfold.UnfoldTransitionProgressProvider;
import org.jetbrains.annotations.NotNull;

/* compiled from: ScaleAwareTransitionProgressProvider.kt */
public final class ScaleAwareTransitionProgressProvider implements UnfoldTransitionProgressProvider {
    @NotNull
    public final ScaleAwareTransitionProgressProvider$animatorDurationScaleObserver$1 animatorDurationScaleObserver;
    @NotNull
    public final ContentResolver contentResolver;
    @NotNull
    public final ScopedUnfoldTransitionProgressProvider scopedUnfoldTransitionProgressProvider;

    /* compiled from: ScaleAwareTransitionProgressProvider.kt */
    public interface Factory {
        @NotNull
        ScaleAwareTransitionProgressProvider wrap(@NotNull UnfoldTransitionProgressProvider unfoldTransitionProgressProvider);
    }

    public ScaleAwareTransitionProgressProvider(@NotNull UnfoldTransitionProgressProvider unfoldTransitionProgressProvider, @NotNull ContentResolver contentResolver2) {
        this.contentResolver = contentResolver2;
        this.scopedUnfoldTransitionProgressProvider = new ScopedUnfoldTransitionProgressProvider(unfoldTransitionProgressProvider);
        ScaleAwareTransitionProgressProvider$animatorDurationScaleObserver$1 scaleAwareTransitionProgressProvider$animatorDurationScaleObserver$1 = new ScaleAwareTransitionProgressProvider$animatorDurationScaleObserver$1(this);
        this.animatorDurationScaleObserver = scaleAwareTransitionProgressProvider$animatorDurationScaleObserver$1;
        contentResolver2.registerContentObserver(Settings.Global.getUriFor("animator_duration_scale"), false, scaleAwareTransitionProgressProvider$animatorDurationScaleObserver$1);
        onAnimatorScaleChanged();
    }

    public final void onAnimatorScaleChanged() {
        this.scopedUnfoldTransitionProgressProvider.setReadyToHandleTransition(ValueAnimator.areAnimatorsEnabled());
    }

    public void addCallback(@NotNull UnfoldTransitionProgressProvider.TransitionProgressListener transitionProgressListener) {
        this.scopedUnfoldTransitionProgressProvider.addCallback(transitionProgressListener);
    }

    public void removeCallback(@NotNull UnfoldTransitionProgressProvider.TransitionProgressListener transitionProgressListener) {
        this.scopedUnfoldTransitionProgressProvider.removeCallback(transitionProgressListener);
    }
}
