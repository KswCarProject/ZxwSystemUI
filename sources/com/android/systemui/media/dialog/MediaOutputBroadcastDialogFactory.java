package com.android.systemui.media.dialog;

import android.content.Context;
import android.media.AudioManager;
import android.media.session.MediaSessionManager;
import android.os.PowerExemptionManager;
import android.view.View;
import com.android.internal.logging.UiEventLogger;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.systemui.animation.DialogLaunchAnimator;
import com.android.systemui.broadcast.BroadcastSender;
import com.android.systemui.media.nearby.NearbyMediaDevicesManager;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: MediaOutputBroadcastDialogFactory.kt */
public final class MediaOutputBroadcastDialogFactory {
    @NotNull
    public final AudioManager audioManager;
    @NotNull
    public final BroadcastSender broadcastSender;
    @NotNull
    public final Context context;
    @NotNull
    public final DialogLaunchAnimator dialogLaunchAnimator;
    @Nullable
    public final LocalBluetoothManager lbm;
    @Nullable
    public MediaOutputBroadcastDialog mediaOutputBroadcastDialog;
    @NotNull
    public final MediaSessionManager mediaSessionManager;
    @NotNull
    public final Optional<NearbyMediaDevicesManager> nearbyMediaDevicesManagerOptional;
    @NotNull
    public final CommonNotifCollection notifCollection;
    @NotNull
    public final PowerExemptionManager powerExemptionManager;
    @NotNull
    public final ActivityStarter starter;
    @NotNull
    public final UiEventLogger uiEventLogger;

    public MediaOutputBroadcastDialogFactory(@NotNull Context context2, @NotNull MediaSessionManager mediaSessionManager2, @Nullable LocalBluetoothManager localBluetoothManager, @NotNull ActivityStarter activityStarter, @NotNull BroadcastSender broadcastSender2, @NotNull CommonNotifCollection commonNotifCollection, @NotNull UiEventLogger uiEventLogger2, @NotNull DialogLaunchAnimator dialogLaunchAnimator2, @NotNull Optional<NearbyMediaDevicesManager> optional, @NotNull AudioManager audioManager2, @NotNull PowerExemptionManager powerExemptionManager2) {
        this.context = context2;
        this.mediaSessionManager = mediaSessionManager2;
        this.lbm = localBluetoothManager;
        this.starter = activityStarter;
        this.broadcastSender = broadcastSender2;
        this.notifCollection = commonNotifCollection;
        this.uiEventLogger = uiEventLogger2;
        this.dialogLaunchAnimator = dialogLaunchAnimator2;
        this.nearbyMediaDevicesManagerOptional = optional;
        this.audioManager = audioManager2;
        this.powerExemptionManager = powerExemptionManager2;
    }

    public static /* synthetic */ void create$default(MediaOutputBroadcastDialogFactory mediaOutputBroadcastDialogFactory, String str, boolean z, View view, int i, Object obj) {
        if ((i & 4) != 0) {
            view = null;
        }
        mediaOutputBroadcastDialogFactory.create(str, z, view);
    }

    public final void create(@NotNull String str, boolean z, @Nullable View view) {
        MediaOutputBroadcastDialog mediaOutputBroadcastDialog2 = this.mediaOutputBroadcastDialog;
        if (mediaOutputBroadcastDialog2 != null) {
            mediaOutputBroadcastDialog2.dismiss();
        }
        MediaOutputController mediaOutputController = new MediaOutputController(this.context, str, this.mediaSessionManager, this.lbm, this.starter, this.notifCollection, this.dialogLaunchAnimator, this.nearbyMediaDevicesManagerOptional, this.audioManager, this.powerExemptionManager);
        MediaOutputBroadcastDialog mediaOutputBroadcastDialog3 = new MediaOutputBroadcastDialog(this.context, z, this.broadcastSender, mediaOutputController);
        this.mediaOutputBroadcastDialog = mediaOutputBroadcastDialog3;
        if (view != null) {
            DialogLaunchAnimator.showFromView$default(this.dialogLaunchAnimator, mediaOutputBroadcastDialog3, view, false, 4, (Object) null);
        } else {
            mediaOutputBroadcastDialog3.show();
        }
    }
}
