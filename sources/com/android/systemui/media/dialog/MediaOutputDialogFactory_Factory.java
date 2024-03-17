package com.android.systemui.media.dialog;

import android.content.Context;
import android.media.AudioManager;
import android.media.session.MediaSessionManager;
import android.os.PowerExemptionManager;
import com.android.internal.logging.UiEventLogger;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.systemui.animation.DialogLaunchAnimator;
import com.android.systemui.broadcast.BroadcastSender;
import com.android.systemui.media.nearby.NearbyMediaDevicesManager;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;

public final class MediaOutputDialogFactory_Factory implements Factory<MediaOutputDialogFactory> {
    public final Provider<AudioManager> audioManagerProvider;
    public final Provider<BroadcastSender> broadcastSenderProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DialogLaunchAnimator> dialogLaunchAnimatorProvider;
    public final Provider<LocalBluetoothManager> lbmProvider;
    public final Provider<MediaSessionManager> mediaSessionManagerProvider;
    public final Provider<Optional<NearbyMediaDevicesManager>> nearbyMediaDevicesManagerOptionalProvider;
    public final Provider<CommonNotifCollection> notifCollectionProvider;
    public final Provider<PowerExemptionManager> powerExemptionManagerProvider;
    public final Provider<ActivityStarter> starterProvider;
    public final Provider<UiEventLogger> uiEventLoggerProvider;

    public MediaOutputDialogFactory_Factory(Provider<Context> provider, Provider<MediaSessionManager> provider2, Provider<LocalBluetoothManager> provider3, Provider<ActivityStarter> provider4, Provider<BroadcastSender> provider5, Provider<CommonNotifCollection> provider6, Provider<UiEventLogger> provider7, Provider<DialogLaunchAnimator> provider8, Provider<Optional<NearbyMediaDevicesManager>> provider9, Provider<AudioManager> provider10, Provider<PowerExemptionManager> provider11) {
        this.contextProvider = provider;
        this.mediaSessionManagerProvider = provider2;
        this.lbmProvider = provider3;
        this.starterProvider = provider4;
        this.broadcastSenderProvider = provider5;
        this.notifCollectionProvider = provider6;
        this.uiEventLoggerProvider = provider7;
        this.dialogLaunchAnimatorProvider = provider8;
        this.nearbyMediaDevicesManagerOptionalProvider = provider9;
        this.audioManagerProvider = provider10;
        this.powerExemptionManagerProvider = provider11;
    }

    public MediaOutputDialogFactory get() {
        return newInstance(this.contextProvider.get(), this.mediaSessionManagerProvider.get(), this.lbmProvider.get(), this.starterProvider.get(), this.broadcastSenderProvider.get(), this.notifCollectionProvider.get(), this.uiEventLoggerProvider.get(), this.dialogLaunchAnimatorProvider.get(), this.nearbyMediaDevicesManagerOptionalProvider.get(), this.audioManagerProvider.get(), this.powerExemptionManagerProvider.get());
    }

    public static MediaOutputDialogFactory_Factory create(Provider<Context> provider, Provider<MediaSessionManager> provider2, Provider<LocalBluetoothManager> provider3, Provider<ActivityStarter> provider4, Provider<BroadcastSender> provider5, Provider<CommonNotifCollection> provider6, Provider<UiEventLogger> provider7, Provider<DialogLaunchAnimator> provider8, Provider<Optional<NearbyMediaDevicesManager>> provider9, Provider<AudioManager> provider10, Provider<PowerExemptionManager> provider11) {
        return new MediaOutputDialogFactory_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11);
    }

    public static MediaOutputDialogFactory newInstance(Context context, MediaSessionManager mediaSessionManager, LocalBluetoothManager localBluetoothManager, ActivityStarter activityStarter, BroadcastSender broadcastSender, CommonNotifCollection commonNotifCollection, UiEventLogger uiEventLogger, DialogLaunchAnimator dialogLaunchAnimator, Optional<NearbyMediaDevicesManager> optional, AudioManager audioManager, PowerExemptionManager powerExemptionManager) {
        return new MediaOutputDialogFactory(context, mediaSessionManager, localBluetoothManager, activityStarter, broadcastSender, commonNotifCollection, uiEventLogger, dialogLaunchAnimator, optional, audioManager, powerExemptionManager);
    }
}
