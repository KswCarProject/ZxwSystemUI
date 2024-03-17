package com.android.systemui.unfold;

import com.android.systemui.unfold.UnfoldTransitionProgressProvider;
import com.android.wm.shell.unfold.ShellUnfoldProgressProvider;
import java.util.concurrent.Executor;

/* compiled from: UnfoldProgressProvider.kt */
public final class UnfoldProgressProvider$addListener$1 implements UnfoldTransitionProgressProvider.TransitionProgressListener {
    public final /* synthetic */ Executor $executor;
    public final /* synthetic */ ShellUnfoldProgressProvider.UnfoldListener $listener;

    public UnfoldProgressProvider$addListener$1(Executor executor, ShellUnfoldProgressProvider.UnfoldListener unfoldListener) {
        this.$executor = executor;
        this.$listener = unfoldListener;
    }

    public void onTransitionStarted() {
        this.$executor.execute(new UnfoldProgressProvider$addListener$1$onTransitionStarted$1(this.$listener));
    }

    public void onTransitionProgress(float f) {
        this.$executor.execute(new UnfoldProgressProvider$addListener$1$onTransitionProgress$1(this.$listener, f));
    }

    public void onTransitionFinished() {
        this.$executor.execute(new UnfoldProgressProvider$addListener$1$onTransitionFinished$1(this.$listener));
    }
}
