package com.android.systemui.unfold;

import com.android.wm.shell.unfold.ShellUnfoldProgressProvider;

/* compiled from: UnfoldProgressProvider.kt */
public final class UnfoldProgressProvider$addListener$1$onTransitionProgress$1 implements Runnable {
    public final /* synthetic */ ShellUnfoldProgressProvider.UnfoldListener $listener;
    public final /* synthetic */ float $progress;

    public UnfoldProgressProvider$addListener$1$onTransitionProgress$1(ShellUnfoldProgressProvider.UnfoldListener unfoldListener, float f) {
        this.$listener = unfoldListener;
        this.$progress = f;
    }

    public final void run() {
        this.$listener.onStateChangeProgress(this.$progress);
    }
}
