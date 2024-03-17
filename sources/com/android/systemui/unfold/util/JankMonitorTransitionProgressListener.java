package com.android.systemui.unfold.util;

import android.view.View;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.systemui.unfold.UnfoldTransitionProgressProvider;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;

/* compiled from: JankMonitorTransitionProgressListener.kt */
public final class JankMonitorTransitionProgressListener implements UnfoldTransitionProgressProvider.TransitionProgressListener {
    @NotNull
    public final Supplier<View> attachedViewProvider;
    public final InteractionJankMonitor interactionJankMonitor = InteractionJankMonitor.getInstance();

    public JankMonitorTransitionProgressListener(@NotNull Supplier<View> supplier) {
        this.attachedViewProvider = supplier;
    }

    public void onTransitionProgress(float f) {
        UnfoldTransitionProgressProvider.TransitionProgressListener.DefaultImpls.onTransitionProgress(this, f);
    }

    public void onTransitionStarted() {
        this.interactionJankMonitor.begin(this.attachedViewProvider.get(), 44);
    }

    public void onTransitionFinished() {
        this.interactionJankMonitor.end(44);
    }
}
