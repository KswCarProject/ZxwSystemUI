package com.android.wm.shell.pip.tv;

import android.app.PictureInPictureParams;
import android.content.Context;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.pip.PipAnimationController;
import com.android.wm.shell.pip.PipBoundsAlgorithm;
import com.android.wm.shell.pip.PipBoundsState;
import com.android.wm.shell.pip.PipMenuController;
import com.android.wm.shell.pip.PipParamsChangedForwarder;
import com.android.wm.shell.pip.PipSurfaceTransactionHelper;
import com.android.wm.shell.pip.PipTaskOrganizer;
import com.android.wm.shell.pip.PipTransitionController;
import com.android.wm.shell.pip.PipTransitionState;
import com.android.wm.shell.pip.PipUiEventLogger;
import com.android.wm.shell.pip.PipUtils;
import com.android.wm.shell.splitscreen.SplitScreenController;
import java.util.Objects;
import java.util.Optional;

public class TvPipTaskOrganizer extends PipTaskOrganizer {
    public TvPipTaskOrganizer(Context context, SyncTransactionQueue syncTransactionQueue, PipTransitionState pipTransitionState, PipBoundsState pipBoundsState, PipBoundsAlgorithm pipBoundsAlgorithm, PipMenuController pipMenuController, PipAnimationController pipAnimationController, PipSurfaceTransactionHelper pipSurfaceTransactionHelper, PipTransitionController pipTransitionController, PipParamsChangedForwarder pipParamsChangedForwarder, Optional<SplitScreenController> optional, DisplayController displayController, PipUiEventLogger pipUiEventLogger, ShellTaskOrganizer shellTaskOrganizer, ShellExecutor shellExecutor) {
        super(context, syncTransactionQueue, pipTransitionState, pipBoundsState, pipBoundsAlgorithm, pipMenuController, pipAnimationController, pipSurfaceTransactionHelper, pipTransitionController, pipParamsChangedForwarder, optional, displayController, pipUiEventLogger, shellTaskOrganizer, shellExecutor);
    }

    public void applyNewPictureInPictureParams(PictureInPictureParams pictureInPictureParams) {
        super.applyNewPictureInPictureParams(pictureInPictureParams);
        if (PipUtils.aspectRatioChanged(pictureInPictureParams.getExpandedAspectRatioFloat(), this.mPictureInPictureParams.getExpandedAspectRatioFloat())) {
            this.mPipParamsChangedForwarder.notifyExpandedAspectRatioChanged(pictureInPictureParams.getExpandedAspectRatioFloat());
        }
        if (!Objects.equals(pictureInPictureParams.getTitle(), this.mPictureInPictureParams.getTitle())) {
            this.mPipParamsChangedForwarder.notifyTitleChanged(pictureInPictureParams.getTitle());
        }
        if (!Objects.equals(pictureInPictureParams.getSubtitle(), this.mPictureInPictureParams.getSubtitle())) {
            this.mPipParamsChangedForwarder.notifySubtitleChanged(pictureInPictureParams.getSubtitle());
        }
    }
}
