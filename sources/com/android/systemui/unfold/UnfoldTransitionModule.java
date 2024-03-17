package com.android.systemui.unfold;

import android.content.Context;
import android.view.IWindowManager;
import com.android.systemui.keyguard.LifecycleScreenStatusProvider;
import com.android.systemui.unfold.config.UnfoldTransitionConfig;
import com.android.systemui.unfold.updates.FoldStateProvider;
import com.android.systemui.unfold.updates.screen.ScreenStatusProvider;
import com.android.systemui.unfold.util.NaturalRotationUnfoldProgressProvider;
import com.android.systemui.unfold.util.ScopedUnfoldTransitionProgressProvider;
import com.android.systemui.util.time.SystemClockImpl;
import com.android.wm.shell.unfold.ShellUnfoldProgressProvider;
import dagger.Lazy;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

/* compiled from: UnfoldTransitionModule.kt */
public final class UnfoldTransitionModule {
    @NotNull
    public final ScreenStatusProvider screenStatusProvider(@NotNull LifecycleScreenStatusProvider lifecycleScreenStatusProvider) {
        return lifecycleScreenStatusProvider;
    }

    @NotNull
    public final String tracingTagPrefix() {
        return "systemui";
    }

    @NotNull
    public final Optional<FoldStateLoggingProvider> providesFoldStateLoggingProvider(@NotNull UnfoldTransitionConfig unfoldTransitionConfig, @NotNull Lazy<FoldStateProvider> lazy) {
        if (unfoldTransitionConfig.isHingeAngleEnabled()) {
            return Optional.of(new FoldStateLoggingProviderImpl(lazy.get(), new SystemClockImpl()));
        }
        return Optional.empty();
    }

    @NotNull
    public final Optional<FoldStateLogger> providesFoldStateLogger(@NotNull Optional<FoldStateLoggingProvider> optional) {
        return optional.map(UnfoldTransitionModule$providesFoldStateLogger$1.INSTANCE);
    }

    @NotNull
    public final UnfoldTransitionConfig provideUnfoldTransitionConfig(@NotNull Context context) {
        return UnfoldTransitionFactory.createConfig(context);
    }

    @NotNull
    public final Optional<NaturalRotationUnfoldProgressProvider> provideNaturalRotationProgressProvider(@NotNull Context context, @NotNull IWindowManager iWindowManager, @NotNull Optional<UnfoldTransitionProgressProvider> optional) {
        return optional.map(new UnfoldTransitionModule$provideNaturalRotationProgressProvider$1(context, iWindowManager));
    }

    @NotNull
    public final Optional<ScopedUnfoldTransitionProgressProvider> provideStatusBarScopedTransitionProvider(@NotNull Optional<NaturalRotationUnfoldProgressProvider> optional) {
        return optional.map(UnfoldTransitionModule$provideStatusBarScopedTransitionProvider$1.INSTANCE);
    }

    @NotNull
    public final ShellUnfoldProgressProvider provideShellProgressProvider(@NotNull UnfoldTransitionConfig unfoldTransitionConfig, @NotNull Optional<UnfoldTransitionProgressProvider> optional) {
        if (!unfoldTransitionConfig.isEnabled() || !optional.isPresent()) {
            return ShellUnfoldProgressProvider.NO_PROVIDER;
        }
        return new UnfoldProgressProvider(optional.get());
    }
}
