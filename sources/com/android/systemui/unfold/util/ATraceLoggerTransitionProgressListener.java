package com.android.systemui.unfold.util;

import android.os.Trace;
import com.android.systemui.unfold.UnfoldTransitionProgressProvider;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: ATraceLoggerTransitionProgressListener.kt */
public final class ATraceLoggerTransitionProgressListener implements UnfoldTransitionProgressProvider.TransitionProgressListener {
    @NotNull
    public final String traceName;

    public ATraceLoggerTransitionProgressListener(@NotNull String str) {
        this.traceName = Intrinsics.stringPlus(str, "#FoldUnfoldTransitionInProgress");
    }

    public void onTransitionStarted() {
        Trace.beginAsyncSection(this.traceName, 0);
    }

    public void onTransitionFinished() {
        Trace.endAsyncSection(this.traceName, 0);
    }

    public void onTransitionProgress(float f) {
        Trace.setCounter(this.traceName, (long) (f * ((float) 100)));
    }
}
