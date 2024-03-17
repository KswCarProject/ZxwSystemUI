package com.android.wm.shell.dagger;

import android.content.Context;
import com.android.wm.shell.WindowManagerShellWrapper;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.TaskStackListenerImpl;
import com.android.wm.shell.pip.Pip;
import com.android.wm.shell.pip.PipAppOpsListener;
import com.android.wm.shell.pip.PipMediaController;
import com.android.wm.shell.pip.PipParamsChangedForwarder;
import com.android.wm.shell.pip.PipTaskOrganizer;
import com.android.wm.shell.pip.PipTransitionController;
import com.android.wm.shell.pip.tv.TvPipBoundsAlgorithm;
import com.android.wm.shell.pip.tv.TvPipBoundsController;
import com.android.wm.shell.pip.tv.TvPipBoundsState;
import com.android.wm.shell.pip.tv.TvPipMenuController;
import com.android.wm.shell.pip.tv.TvPipNotificationController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;

public final class TvPipModule_ProvidePipFactory implements Factory<Optional<Pip>> {
    public final Provider<Context> contextProvider;
    public final Provider<DisplayController> displayControllerProvider;
    public final Provider<ShellExecutor> mainExecutorProvider;
    public final Provider<PipAppOpsListener> pipAppOpsListenerProvider;
    public final Provider<PipMediaController> pipMediaControllerProvider;
    public final Provider<PipParamsChangedForwarder> pipParamsChangedForwarderProvider;
    public final Provider<PipTaskOrganizer> pipTaskOrganizerProvider;
    public final Provider<PipTransitionController> pipTransitionControllerProvider;
    public final Provider<TaskStackListenerImpl> taskStackListenerProvider;
    public final Provider<TvPipBoundsAlgorithm> tvPipBoundsAlgorithmProvider;
    public final Provider<TvPipBoundsController> tvPipBoundsControllerProvider;
    public final Provider<TvPipBoundsState> tvPipBoundsStateProvider;
    public final Provider<TvPipMenuController> tvPipMenuControllerProvider;
    public final Provider<TvPipNotificationController> tvPipNotificationControllerProvider;
    public final Provider<WindowManagerShellWrapper> windowManagerShellWrapperProvider;

    public TvPipModule_ProvidePipFactory(Provider<Context> provider, Provider<TvPipBoundsState> provider2, Provider<TvPipBoundsAlgorithm> provider3, Provider<TvPipBoundsController> provider4, Provider<PipAppOpsListener> provider5, Provider<PipTaskOrganizer> provider6, Provider<TvPipMenuController> provider7, Provider<PipMediaController> provider8, Provider<PipTransitionController> provider9, Provider<TvPipNotificationController> provider10, Provider<TaskStackListenerImpl> provider11, Provider<PipParamsChangedForwarder> provider12, Provider<DisplayController> provider13, Provider<WindowManagerShellWrapper> provider14, Provider<ShellExecutor> provider15) {
        this.contextProvider = provider;
        this.tvPipBoundsStateProvider = provider2;
        this.tvPipBoundsAlgorithmProvider = provider3;
        this.tvPipBoundsControllerProvider = provider4;
        this.pipAppOpsListenerProvider = provider5;
        this.pipTaskOrganizerProvider = provider6;
        this.tvPipMenuControllerProvider = provider7;
        this.pipMediaControllerProvider = provider8;
        this.pipTransitionControllerProvider = provider9;
        this.tvPipNotificationControllerProvider = provider10;
        this.taskStackListenerProvider = provider11;
        this.pipParamsChangedForwarderProvider = provider12;
        this.displayControllerProvider = provider13;
        this.windowManagerShellWrapperProvider = provider14;
        this.mainExecutorProvider = provider15;
    }

    public Optional<Pip> get() {
        return providePip(this.contextProvider.get(), this.tvPipBoundsStateProvider.get(), this.tvPipBoundsAlgorithmProvider.get(), this.tvPipBoundsControllerProvider.get(), this.pipAppOpsListenerProvider.get(), this.pipTaskOrganizerProvider.get(), this.tvPipMenuControllerProvider.get(), this.pipMediaControllerProvider.get(), this.pipTransitionControllerProvider.get(), this.tvPipNotificationControllerProvider.get(), this.taskStackListenerProvider.get(), this.pipParamsChangedForwarderProvider.get(), this.displayControllerProvider.get(), this.windowManagerShellWrapperProvider.get(), this.mainExecutorProvider.get());
    }

    public static TvPipModule_ProvidePipFactory create(Provider<Context> provider, Provider<TvPipBoundsState> provider2, Provider<TvPipBoundsAlgorithm> provider3, Provider<TvPipBoundsController> provider4, Provider<PipAppOpsListener> provider5, Provider<PipTaskOrganizer> provider6, Provider<TvPipMenuController> provider7, Provider<PipMediaController> provider8, Provider<PipTransitionController> provider9, Provider<TvPipNotificationController> provider10, Provider<TaskStackListenerImpl> provider11, Provider<PipParamsChangedForwarder> provider12, Provider<DisplayController> provider13, Provider<WindowManagerShellWrapper> provider14, Provider<ShellExecutor> provider15) {
        return new TvPipModule_ProvidePipFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15);
    }

    public static Optional<Pip> providePip(Context context, TvPipBoundsState tvPipBoundsState, TvPipBoundsAlgorithm tvPipBoundsAlgorithm, TvPipBoundsController tvPipBoundsController, PipAppOpsListener pipAppOpsListener, PipTaskOrganizer pipTaskOrganizer, TvPipMenuController tvPipMenuController, PipMediaController pipMediaController, PipTransitionController pipTransitionController, TvPipNotificationController tvPipNotificationController, TaskStackListenerImpl taskStackListenerImpl, PipParamsChangedForwarder pipParamsChangedForwarder, DisplayController displayController, WindowManagerShellWrapper windowManagerShellWrapper, ShellExecutor shellExecutor) {
        return (Optional) Preconditions.checkNotNullFromProvides(TvPipModule.providePip(context, tvPipBoundsState, tvPipBoundsAlgorithm, tvPipBoundsController, pipAppOpsListener, pipTaskOrganizer, tvPipMenuController, pipMediaController, pipTransitionController, tvPipNotificationController, taskStackListenerImpl, pipParamsChangedForwarder, displayController, windowManagerShellWrapper, shellExecutor));
    }
}
