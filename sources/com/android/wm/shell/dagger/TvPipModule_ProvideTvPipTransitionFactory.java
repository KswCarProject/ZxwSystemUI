package com.android.wm.shell.dagger;

import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.pip.PipAnimationController;
import com.android.wm.shell.pip.PipTransitionController;
import com.android.wm.shell.pip.tv.TvPipBoundsAlgorithm;
import com.android.wm.shell.pip.tv.TvPipBoundsState;
import com.android.wm.shell.pip.tv.TvPipMenuController;
import com.android.wm.shell.transition.Transitions;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class TvPipModule_ProvideTvPipTransitionFactory implements Factory<PipTransitionController> {
    public final Provider<PipAnimationController> pipAnimationControllerProvider;
    public final Provider<TvPipMenuController> pipMenuControllerProvider;
    public final Provider<ShellTaskOrganizer> shellTaskOrganizerProvider;
    public final Provider<Transitions> transitionsProvider;
    public final Provider<TvPipBoundsAlgorithm> tvPipBoundsAlgorithmProvider;
    public final Provider<TvPipBoundsState> tvPipBoundsStateProvider;

    public TvPipModule_ProvideTvPipTransitionFactory(Provider<Transitions> provider, Provider<ShellTaskOrganizer> provider2, Provider<PipAnimationController> provider3, Provider<TvPipBoundsAlgorithm> provider4, Provider<TvPipBoundsState> provider5, Provider<TvPipMenuController> provider6) {
        this.transitionsProvider = provider;
        this.shellTaskOrganizerProvider = provider2;
        this.pipAnimationControllerProvider = provider3;
        this.tvPipBoundsAlgorithmProvider = provider4;
        this.tvPipBoundsStateProvider = provider5;
        this.pipMenuControllerProvider = provider6;
    }

    public PipTransitionController get() {
        return provideTvPipTransition(this.transitionsProvider.get(), this.shellTaskOrganizerProvider.get(), this.pipAnimationControllerProvider.get(), this.tvPipBoundsAlgorithmProvider.get(), this.tvPipBoundsStateProvider.get(), this.pipMenuControllerProvider.get());
    }

    public static TvPipModule_ProvideTvPipTransitionFactory create(Provider<Transitions> provider, Provider<ShellTaskOrganizer> provider2, Provider<PipAnimationController> provider3, Provider<TvPipBoundsAlgorithm> provider4, Provider<TvPipBoundsState> provider5, Provider<TvPipMenuController> provider6) {
        return new TvPipModule_ProvideTvPipTransitionFactory(provider, provider2, provider3, provider4, provider5, provider6);
    }

    public static PipTransitionController provideTvPipTransition(Transitions transitions, ShellTaskOrganizer shellTaskOrganizer, PipAnimationController pipAnimationController, TvPipBoundsAlgorithm tvPipBoundsAlgorithm, TvPipBoundsState tvPipBoundsState, TvPipMenuController tvPipMenuController) {
        return (PipTransitionController) Preconditions.checkNotNullFromProvides(TvPipModule.provideTvPipTransition(transitions, shellTaskOrganizer, pipAnimationController, tvPipBoundsAlgorithm, tvPipBoundsState, tvPipMenuController));
    }
}
