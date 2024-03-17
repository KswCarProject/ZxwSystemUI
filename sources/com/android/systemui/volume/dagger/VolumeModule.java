package com.android.systemui.volume.dagger;

import android.content.Context;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.systemui.media.dialog.MediaOutputDialogFactory;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.VolumeDialog;
import com.android.systemui.plugins.VolumeDialogController;
import com.android.systemui.statusbar.policy.AccessibilityManagerWrapper;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.volume.VolumeDialogImpl;

public interface VolumeModule {
    static VolumeDialog provideVolumeDialog(Context context, VolumeDialogController volumeDialogController, AccessibilityManagerWrapper accessibilityManagerWrapper, DeviceProvisionedController deviceProvisionedController, ConfigurationController configurationController, MediaOutputDialogFactory mediaOutputDialogFactory, ActivityStarter activityStarter, InteractionJankMonitor interactionJankMonitor) {
        VolumeDialogImpl volumeDialogImpl = new VolumeDialogImpl(context, volumeDialogController, accessibilityManagerWrapper, deviceProvisionedController, configurationController, mediaOutputDialogFactory, activityStarter, interactionJankMonitor);
        volumeDialogImpl.setStreamImportant(1, false);
        volumeDialogImpl.setAutomute(true);
        volumeDialogImpl.setSilentMode(false);
        return volumeDialogImpl;
    }
}
