package com.android.systemui.qs.dagger;

import android.content.Context;
import android.hardware.display.NightDisplayListener;
import android.os.Handler;
import com.android.systemui.qs.AutoAddTracker;
import com.android.systemui.qs.QSTileHost;
import com.android.systemui.qs.ReduceBrightColorsController;
import com.android.systemui.statusbar.phone.AutoTileManager;
import com.android.systemui.statusbar.phone.ManagedProfileController;
import com.android.systemui.statusbar.policy.CastController;
import com.android.systemui.statusbar.policy.DataSaverController;
import com.android.systemui.statusbar.policy.DeviceControlsController;
import com.android.systemui.statusbar.policy.HotspotController;
import com.android.systemui.statusbar.policy.SafetyController;
import com.android.systemui.statusbar.policy.WalletController;
import com.android.systemui.util.settings.SecureSettings;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class QSModule_ProvideAutoTileManagerFactory implements Factory<AutoTileManager> {
    public final Provider<AutoAddTracker.Builder> autoAddTrackerBuilderProvider;
    public final Provider<CastController> castControllerProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DataSaverController> dataSaverControllerProvider;
    public final Provider<DeviceControlsController> deviceControlsControllerProvider;
    public final Provider<Handler> handlerProvider;
    public final Provider<QSTileHost> hostProvider;
    public final Provider<HotspotController> hotspotControllerProvider;
    public final Provider<Boolean> isReduceBrightColorsAvailableProvider;
    public final Provider<ManagedProfileController> managedProfileControllerProvider;
    public final Provider<NightDisplayListener> nightDisplayListenerProvider;
    public final Provider<ReduceBrightColorsController> reduceBrightColorsControllerProvider;
    public final Provider<SafetyController> safetyControllerProvider;
    public final Provider<SecureSettings> secureSettingsProvider;
    public final Provider<WalletController> walletControllerProvider;

    public QSModule_ProvideAutoTileManagerFactory(Provider<Context> provider, Provider<AutoAddTracker.Builder> provider2, Provider<QSTileHost> provider3, Provider<Handler> provider4, Provider<SecureSettings> provider5, Provider<HotspotController> provider6, Provider<DataSaverController> provider7, Provider<ManagedProfileController> provider8, Provider<NightDisplayListener> provider9, Provider<CastController> provider10, Provider<ReduceBrightColorsController> provider11, Provider<DeviceControlsController> provider12, Provider<WalletController> provider13, Provider<SafetyController> provider14, Provider<Boolean> provider15) {
        this.contextProvider = provider;
        this.autoAddTrackerBuilderProvider = provider2;
        this.hostProvider = provider3;
        this.handlerProvider = provider4;
        this.secureSettingsProvider = provider5;
        this.hotspotControllerProvider = provider6;
        this.dataSaverControllerProvider = provider7;
        this.managedProfileControllerProvider = provider8;
        this.nightDisplayListenerProvider = provider9;
        this.castControllerProvider = provider10;
        this.reduceBrightColorsControllerProvider = provider11;
        this.deviceControlsControllerProvider = provider12;
        this.walletControllerProvider = provider13;
        this.safetyControllerProvider = provider14;
        this.isReduceBrightColorsAvailableProvider = provider15;
    }

    public AutoTileManager get() {
        return provideAutoTileManager(this.contextProvider.get(), this.autoAddTrackerBuilderProvider.get(), this.hostProvider.get(), this.handlerProvider.get(), this.secureSettingsProvider.get(), this.hotspotControllerProvider.get(), this.dataSaverControllerProvider.get(), this.managedProfileControllerProvider.get(), this.nightDisplayListenerProvider.get(), this.castControllerProvider.get(), this.reduceBrightColorsControllerProvider.get(), this.deviceControlsControllerProvider.get(), this.walletControllerProvider.get(), this.safetyControllerProvider.get(), this.isReduceBrightColorsAvailableProvider.get().booleanValue());
    }

    public static QSModule_ProvideAutoTileManagerFactory create(Provider<Context> provider, Provider<AutoAddTracker.Builder> provider2, Provider<QSTileHost> provider3, Provider<Handler> provider4, Provider<SecureSettings> provider5, Provider<HotspotController> provider6, Provider<DataSaverController> provider7, Provider<ManagedProfileController> provider8, Provider<NightDisplayListener> provider9, Provider<CastController> provider10, Provider<ReduceBrightColorsController> provider11, Provider<DeviceControlsController> provider12, Provider<WalletController> provider13, Provider<SafetyController> provider14, Provider<Boolean> provider15) {
        return new QSModule_ProvideAutoTileManagerFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15);
    }

    public static AutoTileManager provideAutoTileManager(Context context, AutoAddTracker.Builder builder, QSTileHost qSTileHost, Handler handler, SecureSettings secureSettings, HotspotController hotspotController, DataSaverController dataSaverController, ManagedProfileController managedProfileController, NightDisplayListener nightDisplayListener, CastController castController, ReduceBrightColorsController reduceBrightColorsController, DeviceControlsController deviceControlsController, WalletController walletController, SafetyController safetyController, boolean z) {
        return (AutoTileManager) Preconditions.checkNotNullFromProvides(QSModule.provideAutoTileManager(context, builder, qSTileHost, handler, secureSettings, hotspotController, dataSaverController, managedProfileController, nightDisplayListener, castController, reduceBrightColorsController, deviceControlsController, walletController, safetyController, z));
    }
}
