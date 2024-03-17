package com.android.systemui.volume;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.VolumePolicy;
import android.os.Bundle;
import com.android.settingslib.applications.InterestingConfigChanges;
import com.android.systemui.demomode.DemoMode;
import com.android.systemui.demomode.DemoModeController;
import com.android.systemui.keyguard.KeyguardViewMediator;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.PluginDependencyProvider;
import com.android.systemui.plugins.VolumeDialog;
import com.android.systemui.plugins.VolumeDialogController;
import com.android.systemui.qs.tiles.DndTile;
import com.android.systemui.statusbar.policy.ExtensionController;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.volume.VolumeDialogControllerImpl;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class VolumeDialogComponent implements VolumeComponent, TunerService.Tunable, VolumeDialogControllerImpl.UserActivityListener {
    public static final Intent ZEN_PRIORITY_SETTINGS = new Intent("android.settings.ZEN_MODE_PRIORITY_SETTINGS");
    public static final Intent ZEN_SETTINGS = new Intent("android.settings.ZEN_MODE_SETTINGS");
    public final ActivityStarter mActivityStarter;
    public final InterestingConfigChanges mConfigChanges = new InterestingConfigChanges(-1073741308);
    public final Context mContext;
    public final VolumeDialogControllerImpl mController;
    public VolumeDialog mDialog;
    public final KeyguardViewMediator mKeyguardViewMediator;
    public final VolumeDialog.Callback mVolumeDialogCallback = new VolumeDialog.Callback() {
        public void onZenSettingsClicked() {
            VolumeDialogComponent.this.startSettings(VolumeDialogComponent.ZEN_SETTINGS);
        }

        public void onZenPrioritySettingsClicked() {
            VolumeDialogComponent.this.startSettings(VolumeDialogComponent.ZEN_PRIORITY_SETTINGS);
        }
    };
    public VolumePolicy mVolumePolicy = new VolumePolicy(false, false, false, 400);

    public static /* synthetic */ VolumeDialog lambda$new$0(VolumeDialog volumeDialog) {
        return volumeDialog;
    }

    public void dispatchDemoCommand(String str, Bundle bundle) {
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
    }

    public VolumeDialogComponent(Context context, KeyguardViewMediator keyguardViewMediator, ActivityStarter activityStarter, VolumeDialogControllerImpl volumeDialogControllerImpl, DemoModeController demoModeController, PluginDependencyProvider pluginDependencyProvider, ExtensionController extensionController, TunerService tunerService, VolumeDialog volumeDialog) {
        Class<VolumeDialog> cls = VolumeDialog.class;
        this.mContext = context;
        this.mKeyguardViewMediator = keyguardViewMediator;
        this.mActivityStarter = activityStarter;
        this.mController = volumeDialogControllerImpl;
        volumeDialogControllerImpl.setUserActivityListener(this);
        pluginDependencyProvider.allowPluginDependency(VolumeDialogController.class);
        extensionController.newExtension(cls).withPlugin(cls).withDefault(new VolumeDialogComponent$$ExternalSyntheticLambda0(volumeDialog)).withCallback(new VolumeDialogComponent$$ExternalSyntheticLambda1(this)).build();
        applyConfiguration();
        tunerService.addTunable(this, "sysui_volume_down_silent", "sysui_volume_up_silent", "sysui_do_not_disturb");
        demoModeController.addCallback((DemoMode) this);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(VolumeDialog volumeDialog) {
        VolumeDialog volumeDialog2 = this.mDialog;
        if (volumeDialog2 != null) {
            volumeDialog2.destroy();
        }
        this.mDialog = volumeDialog;
        volumeDialog.init(2020, this.mVolumeDialogCallback);
    }

    public void onTuningChanged(String str, String str2) {
        VolumePolicy volumePolicy = this.mVolumePolicy;
        boolean z = volumePolicy.volumeDownToEnterSilent;
        boolean z2 = volumePolicy.volumeUpToExitSilent;
        boolean z3 = volumePolicy.doNotDisturbWhenSilent;
        if ("sysui_volume_down_silent".equals(str)) {
            z = TunerService.parseIntegerSwitch(str2, false);
        } else if ("sysui_volume_up_silent".equals(str)) {
            z2 = TunerService.parseIntegerSwitch(str2, false);
        } else if ("sysui_do_not_disturb".equals(str)) {
            z3 = TunerService.parseIntegerSwitch(str2, false);
        }
        setVolumePolicy(z, z2, z3, this.mVolumePolicy.vibrateToSilentDebounce);
    }

    public final void setVolumePolicy(boolean z, boolean z2, boolean z3, int i) {
        VolumePolicy volumePolicy = new VolumePolicy(z, z2, z3, i);
        this.mVolumePolicy = volumePolicy;
        this.mController.setVolumePolicy(volumePolicy);
    }

    public void setEnableDialogs(boolean z, boolean z2) {
        this.mController.setEnableDialogs(z, z2);
    }

    public void onUserActivity() {
        this.mKeyguardViewMediator.userActivity();
    }

    public final void applyConfiguration() {
        this.mController.setVolumePolicy(this.mVolumePolicy);
        this.mController.showDndTile();
    }

    public void onConfigurationChanged(Configuration configuration) {
        if (this.mConfigChanges.applyNewConfig(this.mContext.getResources())) {
            this.mController.mCallbacks.onConfigurationChanged();
        }
    }

    public void dismissNow() {
        this.mController.dismiss();
    }

    public List<String> demoCommands() {
        ArrayList arrayList = new ArrayList();
        arrayList.add("volume");
        return arrayList;
    }

    public void register() {
        this.mController.register();
        DndTile.setCombinedIcon(this.mContext, true);
    }

    public final void startSettings(Intent intent) {
        this.mActivityStarter.startActivity(intent, true, true);
    }
}
