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
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: MediaOutputDialogFactory.kt */
public final class MediaOutputDialogFactory {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @Nullable
    public static MediaOutputDialog mediaOutputDialog;
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

    public MediaOutputDialogFactory(@NotNull Context context2, @NotNull MediaSessionManager mediaSessionManager2, @Nullable LocalBluetoothManager localBluetoothManager, @NotNull ActivityStarter activityStarter, @NotNull BroadcastSender broadcastSender2, @NotNull CommonNotifCollection commonNotifCollection, @NotNull UiEventLogger uiEventLogger2, @NotNull DialogLaunchAnimator dialogLaunchAnimator2, @NotNull Optional<NearbyMediaDevicesManager> optional, @NotNull AudioManager audioManager2, @NotNull PowerExemptionManager powerExemptionManager2) {
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

    /* compiled from: MediaOutputDialogFactory.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }

    public static /* synthetic */ void create$default(MediaOutputDialogFactory mediaOutputDialogFactory, String str, boolean z, View view, int i, Object obj) {
        if ((i & 4) != 0) {
            view = null;
        }
        mediaOutputDialogFactory.create(str, z, view);
    }

    public final void create(@NotNull String str, boolean z, @Nullable View view) {
        MediaOutputDialog mediaOutputDialog2 = mediaOutputDialog;
        if (mediaOutputDialog2 != null) {
            mediaOutputDialog2.dismiss();
        }
        MediaOutputController mediaOutputController = new MediaOutputController(this.context, str, this.mediaSessionManager, this.lbm, this.starter, this.notifCollection, this.dialogLaunchAnimator, this.nearbyMediaDevicesManagerOptional, this.audioManager, this.powerExemptionManager);
        MediaOutputDialog mediaOutputDialog3 = new MediaOutputDialog(this.context, z, this.broadcastSender, mediaOutputController, this.uiEventLogger);
        mediaOutputDialog = mediaOutputDialog3;
        if (view != null) {
            DialogLaunchAnimator.showFromView$default(this.dialogLaunchAnimator, mediaOutputDialog3, view, false, 4, (Object) null);
        } else {
            mediaOutputDialog3.show();
        }
    }

    public final void dismiss() {
        MediaOutputDialog mediaOutputDialog2 = mediaOutputDialog;
        if (mediaOutputDialog2 != null) {
            mediaOutputDialog2.dismiss();
        }
        mediaOutputDialog = null;
    }
}
