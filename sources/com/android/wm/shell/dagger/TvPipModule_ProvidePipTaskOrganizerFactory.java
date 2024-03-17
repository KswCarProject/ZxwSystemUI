package com.android.wm.shell.dagger;

import android.content.Context;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.pip.PipAnimationController;
import com.android.wm.shell.pip.PipParamsChangedForwarder;
import com.android.wm.shell.pip.PipSurfaceTransactionHelper;
import com.android.wm.shell.pip.PipTaskOrganizer;
import com.android.wm.shell.pip.PipTransitionController;
import com.android.wm.shell.pip.PipTransitionState;
import com.android.wm.shell.pip.PipUiEventLogger;
import com.android.wm.shell.pip.tv.TvPipBoundsAlgorithm;
import com.android.wm.shell.pip.tv.TvPipBoundsState;
import com.android.wm.shell.pip.tv.TvPipMenuController;
import com.android.wm.shell.splitscreen.SplitScreenController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;

public final class TvPipModule_ProvidePipTaskOrganizerFactory implements Factory<PipTaskOrganizer> {
    public final Provider<Context> contextProvider;
    public final Provider<DisplayController> displayControllerProvider;
    public final Provider<ShellExecutor> mainExecutorProvider;
    public final Provider<PipAnimationController> pipAnimationControllerProvider;
    public final Provider<PipParamsChangedForwarder> pipParamsChangedForwarderProvider;
    public final Provider<PipSurfaceTransactionHelper> pipSurfaceTransactionHelperProvider;
    public final Provider<PipTransitionController> pipTransitionControllerProvider;
    public final Provider<PipTransitionState> pipTransitionStateProvider;
    public final Provider<PipUiEventLogger> pipUiEventLoggerProvider;
    public final Provider<ShellTaskOrganizer> shellTaskOrganizerProvider;
    public final Provider<Optional<SplitScreenController>> splitScreenControllerOptionalProvider;
    public final Provider<SyncTransactionQueue> syncTransactionQueueProvider;
    public final Provider<TvPipBoundsAlgorithm> tvPipBoundsAlgorithmProvider;
    public final Provider<TvPipBoundsState> tvPipBoundsStateProvider;
    public final Provider<TvPipMenuController> tvPipMenuControllerProvider;

    public TvPipModule_ProvidePipTaskOrganizerFactory(Provider<Context> provider, Provider<TvPipMenuController> provider2, Provider<SyncTransactionQueue> provider3, Provider<TvPipBoundsState> provider4, Provider<PipTransitionState> provider5, Provider<TvPipBoundsAlgorithm> provider6, Provider<PipAnimationController> provider7, Provider<PipTransitionController> provider8, Provider<PipParamsChangedForwarder> provider9, Provider<PipSurfaceTransactionHelper> provider10, Provider<Optional<SplitScreenController>> provider11, Provider<DisplayController> provider12, Provider<PipUiEventLogger> provider13, Provider<ShellTaskOrganizer> provider14, Provider<ShellExecutor> provider15) {
        this.contextProvider = provider;
        this.tvPipMenuControllerProvider = provider2;
        this.syncTransactionQueueProvider = provider3;
        this.tvPipBoundsStateProvider = provider4;
        this.pipTransitionStateProvider = provider5;
        this.tvPipBoundsAlgorithmProvider = provider6;
        this.pipAnimationControllerProvider = provider7;
        this.pipTransitionControllerProvider = provider8;
        this.pipParamsChangedForwarderProvider = provider9;
        this.pipSurfaceTransactionHelperProvider = provider10;
        this.splitScreenControllerOptionalProvider = provider11;
        this.displayControllerProvider = provider12;
        this.pipUiEventLoggerProvider = provider13;
        this.shellTaskOrganizerProvider = provider14;
        this.mainExecutorProvider = provider15;
    }

    public PipTaskOrganizer get() {
        return providePipTaskOrganizer(this.contextProvider.get(), this.tvPipMenuControllerProvider.get(), this.syncTransactionQueueProvider.get(), this.tvPipBoundsStateProvider.get(), this.pipTransitionStateProvider.get(), this.tvPipBoundsAlgorithmProvider.get(), this.pipAnimationControllerProvider.get(), this.pipTransitionControllerProvider.get(), this.pipParamsChangedForwarderProvider.get(), this.pipSurfaceTransactionHelperProvider.get(), this.splitScreenControllerOptionalProvider.get(), this.displayControllerProvider.get(), this.pipUiEventLoggerProvider.get(), this.shellTaskOrganizerProvider.get(), this.mainExecutorProvider.get());
    }

    public static TvPipModule_ProvidePipTaskOrganizerFactory create(Provider<Context> provider, Provider<TvPipMenuController> provider2, Provider<SyncTransactionQueue> provider3, Provider<TvPipBoundsState> provider4, Provider<PipTransitionState> provider5, Provider<TvPipBoundsAlgorithm> provider6, Provider<PipAnimationController> provider7, Provider<PipTransitionController> provider8, Provider<PipParamsChangedForwarder> provider9, Provider<PipSurfaceTransactionHelper> provider10, Provider<Optional<SplitScreenController>> provider11, Provider<DisplayController> provider12, Provider<PipUiEventLogger> provider13, Provider<ShellTaskOrganizer> provider14, Provider<ShellExecutor> provider15) {
        return new TvPipModule_ProvidePipTaskOrganizerFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15);
    }

    public static PipTaskOrganizer providePipTaskOrganizer(Context context, TvPipMenuController tvPipMenuController, SyncTransactionQueue syncTransactionQueue, TvPipBoundsState tvPipBoundsState, PipTransitionState pipTransitionState, TvPipBoundsAlgorithm tvPipBoundsAlgorithm, PipAnimationController pipAnimationController, PipTransitionController pipTransitionController, PipParamsChangedForwarder pipParamsChangedForwarder, PipSurfaceTransactionHelper pipSurfaceTransactionHelper, Optional<SplitScreenController> optional, DisplayController displayController, PipUiEventLogger pipUiEventLogger, ShellTaskOrganizer shellTaskOrganizer, ShellExecutor shellExecutor) {
        return (PipTaskOrganizer) Preconditions.checkNotNullFromProvides(TvPipModule.providePipTaskOrganizer(context, tvPipMenuController, syncTransactionQueue, tvPipBoundsState, pipTransitionState, tvPipBoundsAlgorithm, pipAnimationController, pipTransitionController, pipParamsChangedForwarder, pipSurfaceTransactionHelper, optional, displayController, pipUiEventLogger, shellTaskOrganizer, shellExecutor));
    }
}
