package com.android.systemui.unfold;

import com.android.wm.shell.unfold.ShellUnfoldProgressProvider;
import java.util.concurrent.Executor;
import org.jetbrains.annotations.NotNull;

/* compiled from: UnfoldProgressProvider.kt */
public final class UnfoldProgressProvider implements ShellUnfoldProgressProvider {
    @NotNull
    public final UnfoldTransitionProgressProvider unfoldProgressProvider;

    public UnfoldProgressProvider(@NotNull UnfoldTransitionProgressProvider unfoldTransitionProgressProvider) {
        this.unfoldProgressProvider = unfoldTransitionProgressProvider;
    }

    public void addListener(@NotNull Executor executor, @NotNull ShellUnfoldProgressProvider.UnfoldListener unfoldListener) {
        this.unfoldProgressProvider.addCallback(new UnfoldProgressProvider$addListener$1(executor, unfoldListener));
    }
}
