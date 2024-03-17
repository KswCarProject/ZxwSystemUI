package com.android.wm.shell.dagger;

import android.content.Context;
import android.os.Handler;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.WindowManagerShellWrapper;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.common.SystemWindows;
import com.android.wm.shell.common.TaskStackListenerImpl;
import com.android.wm.shell.pip.Pip;
import com.android.wm.shell.pip.PipAnimationController;
import com.android.wm.shell.pip.PipAppOpsListener;
import com.android.wm.shell.pip.PipMediaController;
import com.android.wm.shell.pip.PipParamsChangedForwarder;
import com.android.wm.shell.pip.PipSnapAlgorithm;
import com.android.wm.shell.pip.PipSurfaceTransactionHelper;
import com.android.wm.shell.pip.PipTaskOrganizer;
import com.android.wm.shell.pip.PipTransitionController;
import com.android.wm.shell.pip.PipTransitionState;
import com.android.wm.shell.pip.PipUiEventLogger;
import com.android.wm.shell.pip.tv.TvPipBoundsAlgorithm;
import com.android.wm.shell.pip.tv.TvPipBoundsController;
import com.android.wm.shell.pip.tv.TvPipBoundsState;
import com.android.wm.shell.pip.tv.TvPipController;
import com.android.wm.shell.pip.tv.TvPipMenuController;
import com.android.wm.shell.pip.tv.TvPipNotificationController;
import com.android.wm.shell.pip.tv.TvPipTaskOrganizer;
import com.android.wm.shell.pip.tv.TvPipTransition;
import com.android.wm.shell.splitscreen.SplitScreenController;
import com.android.wm.shell.transition.Transitions;
import java.util.Objects;
import java.util.Optional;

public abstract class TvPipModule {
    public static Optional<Pip> providePip(Context context, TvPipBoundsState tvPipBoundsState, TvPipBoundsAlgorithm tvPipBoundsAlgorithm, TvPipBoundsController tvPipBoundsController, PipAppOpsListener pipAppOpsListener, PipTaskOrganizer pipTaskOrganizer, TvPipMenuController tvPipMenuController, PipMediaController pipMediaController, PipTransitionController pipTransitionController, TvPipNotificationController tvPipNotificationController, TaskStackListenerImpl taskStackListenerImpl, PipParamsChangedForwarder pipParamsChangedForwarder, DisplayController displayController, WindowManagerShellWrapper windowManagerShellWrapper, ShellExecutor shellExecutor) {
        return Optional.of(TvPipController.create(context, tvPipBoundsState, tvPipBoundsAlgorithm, tvPipBoundsController, pipAppOpsListener, pipTaskOrganizer, pipTransitionController, tvPipMenuController, pipMediaController, tvPipNotificationController, taskStackListenerImpl, pipParamsChangedForwarder, displayController, windowManagerShellWrapper, shellExecutor));
    }

    public static TvPipBoundsController provideTvPipBoundsController(Context context, Handler handler, TvPipBoundsState tvPipBoundsState, TvPipBoundsAlgorithm tvPipBoundsAlgorithm) {
        return new TvPipBoundsController(context, new TvPipModule$$ExternalSyntheticLambda0(), handler, tvPipBoundsState, tvPipBoundsAlgorithm);
    }

    public static PipSnapAlgorithm providePipSnapAlgorithm() {
        return new PipSnapAlgorithm();
    }

    public static TvPipBoundsAlgorithm provideTvPipBoundsAlgorithm(Context context, TvPipBoundsState tvPipBoundsState, PipSnapAlgorithm pipSnapAlgorithm) {
        return new TvPipBoundsAlgorithm(context, tvPipBoundsState, pipSnapAlgorithm);
    }

    public static TvPipBoundsState provideTvPipBoundsState(Context context) {
        return new TvPipBoundsState(context);
    }

    public static PipTransitionController provideTvPipTransition(Transitions transitions, ShellTaskOrganizer shellTaskOrganizer, PipAnimationController pipAnimationController, TvPipBoundsAlgorithm tvPipBoundsAlgorithm, TvPipBoundsState tvPipBoundsState, TvPipMenuController tvPipMenuController) {
        return new TvPipTransition(tvPipBoundsState, tvPipMenuController, tvPipBoundsAlgorithm, pipAnimationController, transitions, shellTaskOrganizer);
    }

    public static TvPipMenuController providesTvPipMenuController(Context context, TvPipBoundsState tvPipBoundsState, SystemWindows systemWindows, PipMediaController pipMediaController, Handler handler) {
        return new TvPipMenuController(context, tvPipBoundsState, systemWindows, pipMediaController, handler);
    }

    public static TvPipNotificationController provideTvPipNotificationController(Context context, PipMediaController pipMediaController, PipParamsChangedForwarder pipParamsChangedForwarder, TvPipBoundsState tvPipBoundsState, Handler handler) {
        return new TvPipNotificationController(context, pipMediaController, pipParamsChangedForwarder, tvPipBoundsState, handler);
    }

    public static PipAnimationController providePipAnimationController(PipSurfaceTransactionHelper pipSurfaceTransactionHelper) {
        return new PipAnimationController(pipSurfaceTransactionHelper);
    }

    public static PipTransitionState providePipTransitionState() {
        return new PipTransitionState();
    }

    public static PipTaskOrganizer providePipTaskOrganizer(Context context, TvPipMenuController tvPipMenuController, SyncTransactionQueue syncTransactionQueue, TvPipBoundsState tvPipBoundsState, PipTransitionState pipTransitionState, TvPipBoundsAlgorithm tvPipBoundsAlgorithm, PipAnimationController pipAnimationController, PipTransitionController pipTransitionController, PipParamsChangedForwarder pipParamsChangedForwarder, PipSurfaceTransactionHelper pipSurfaceTransactionHelper, Optional<SplitScreenController> optional, DisplayController displayController, PipUiEventLogger pipUiEventLogger, ShellTaskOrganizer shellTaskOrganizer, ShellExecutor shellExecutor) {
        return new TvPipTaskOrganizer(context, syncTransactionQueue, pipTransitionState, tvPipBoundsState, tvPipBoundsAlgorithm, tvPipMenuController, pipAnimationController, pipSurfaceTransactionHelper, pipTransitionController, pipParamsChangedForwarder, optional, displayController, pipUiEventLogger, shellTaskOrganizer, shellExecutor);
    }

    public static PipParamsChangedForwarder providePipParamsChangedForwarder() {
        return new PipParamsChangedForwarder();
    }

    public static PipAppOpsListener providePipAppOpsListener(Context context, PipTaskOrganizer pipTaskOrganizer, ShellExecutor shellExecutor) {
        Objects.requireNonNull(pipTaskOrganizer);
        return new PipAppOpsListener(context, new TvPipModule$$ExternalSyntheticLambda1(pipTaskOrganizer), shellExecutor);
    }
}
