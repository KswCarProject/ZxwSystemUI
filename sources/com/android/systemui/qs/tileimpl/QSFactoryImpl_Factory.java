package com.android.systemui.qs.tileimpl;

import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.external.CustomTile;
import com.android.systemui.qs.tiles.AirplaneModeTile;
import com.android.systemui.qs.tiles.AlarmTile;
import com.android.systemui.qs.tiles.BatterySaverTile;
import com.android.systemui.qs.tiles.BlackScreenTile;
import com.android.systemui.qs.tiles.BluetoothTile;
import com.android.systemui.qs.tiles.CameraToggleTile;
import com.android.systemui.qs.tiles.CastTile;
import com.android.systemui.qs.tiles.CellularTile;
import com.android.systemui.qs.tiles.ColorCorrectionTile;
import com.android.systemui.qs.tiles.ColorInversionTile;
import com.android.systemui.qs.tiles.DataSaverTile;
import com.android.systemui.qs.tiles.DeviceControlsTile;
import com.android.systemui.qs.tiles.DndTile;
import com.android.systemui.qs.tiles.FlashlightTile;
import com.android.systemui.qs.tiles.HotspotTile;
import com.android.systemui.qs.tiles.InternetTile;
import com.android.systemui.qs.tiles.LocationTile;
import com.android.systemui.qs.tiles.MicrophoneToggleTile;
import com.android.systemui.qs.tiles.NfcTile;
import com.android.systemui.qs.tiles.NightDisplayTile;
import com.android.systemui.qs.tiles.OneHandedModeTile;
import com.android.systemui.qs.tiles.QRCodeScannerTile;
import com.android.systemui.qs.tiles.QuickAccessWalletTile;
import com.android.systemui.qs.tiles.RebootTile;
import com.android.systemui.qs.tiles.ReduceBrightColorsTile;
import com.android.systemui.qs.tiles.RotationLockTile;
import com.android.systemui.qs.tiles.ScreenRecordTile;
import com.android.systemui.qs.tiles.ScreenShotTile;
import com.android.systemui.qs.tiles.SettingTile;
import com.android.systemui.qs.tiles.SoundTile;
import com.android.systemui.qs.tiles.UiModeNightTile;
import com.android.systemui.qs.tiles.WifiTile;
import com.android.systemui.qs.tiles.WorkModeTile;
import com.android.systemui.util.leak.GarbageMonitor;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class QSFactoryImpl_Factory implements Factory<QSFactoryImpl> {
    public final Provider<AirplaneModeTile> airplaneModeTileProvider;
    public final Provider<AlarmTile> alarmTileProvider;
    public final Provider<BatterySaverTile> batterySaverTileProvider;
    public final Provider<BlackScreenTile> blackScreenTileProvider;
    public final Provider<BluetoothTile> bluetoothTileProvider;
    public final Provider<CameraToggleTile> cameraToggleTileProvider;
    public final Provider<CastTile> castTileProvider;
    public final Provider<CellularTile> cellularTileProvider;
    public final Provider<ColorCorrectionTile> colorCorrectionTileProvider;
    public final Provider<ColorInversionTile> colorInversionTileProvider;
    public final Provider<CustomTile.Builder> customTileBuilderProvider;
    public final Provider<DataSaverTile> dataSaverTileProvider;
    public final Provider<DeviceControlsTile> deviceControlsTileProvider;
    public final Provider<DndTile> dndTileProvider;
    public final Provider<FlashlightTile> flashlightTileProvider;
    public final Provider<HotspotTile> hotspotTileProvider;
    public final Provider<InternetTile> internetTileProvider;
    public final Provider<LocationTile> locationTileProvider;
    public final Provider<GarbageMonitor.MemoryTile> memoryTileProvider;
    public final Provider<MicrophoneToggleTile> microphoneToggleTileProvider;
    public final Provider<NfcTile> nfcTileProvider;
    public final Provider<NightDisplayTile> nightDisplayTileProvider;
    public final Provider<OneHandedModeTile> oneHandedModeTileProvider;
    public final Provider<QRCodeScannerTile> qrCodeScannerTileProvider;
    public final Provider<QSHost> qsHostLazyProvider;
    public final Provider<QuickAccessWalletTile> quickAccessWalletTileProvider;
    public final Provider<RebootTile> rebootTileProvider;
    public final Provider<ReduceBrightColorsTile> reduceBrightColorsTileProvider;
    public final Provider<RotationLockTile> rotationLockTileProvider;
    public final Provider<ScreenRecordTile> screenRecordTileProvider;
    public final Provider<ScreenShotTile> screenShotTileProvider;
    public final Provider<SettingTile> settingTileProvider;
    public final Provider<SoundTile> soundTileProvider;
    public final Provider<UiModeNightTile> uiModeNightTileProvider;
    public final Provider<WifiTile> wifiTileProvider;
    public final Provider<WorkModeTile> workModeTileProvider;

    public QSFactoryImpl_Factory(Provider<QSHost> provider, Provider<CustomTile.Builder> provider2, Provider<WifiTile> provider3, Provider<InternetTile> provider4, Provider<BluetoothTile> provider5, Provider<CellularTile> provider6, Provider<DndTile> provider7, Provider<ColorInversionTile> provider8, Provider<AirplaneModeTile> provider9, Provider<WorkModeTile> provider10, Provider<RotationLockTile> provider11, Provider<FlashlightTile> provider12, Provider<LocationTile> provider13, Provider<CastTile> provider14, Provider<HotspotTile> provider15, Provider<BatterySaverTile> provider16, Provider<DataSaverTile> provider17, Provider<NightDisplayTile> provider18, Provider<NfcTile> provider19, Provider<GarbageMonitor.MemoryTile> provider20, Provider<UiModeNightTile> provider21, Provider<ScreenRecordTile> provider22, Provider<ReduceBrightColorsTile> provider23, Provider<CameraToggleTile> provider24, Provider<MicrophoneToggleTile> provider25, Provider<DeviceControlsTile> provider26, Provider<AlarmTile> provider27, Provider<QuickAccessWalletTile> provider28, Provider<QRCodeScannerTile> provider29, Provider<OneHandedModeTile> provider30, Provider<ColorCorrectionTile> provider31, Provider<ScreenShotTile> provider32, Provider<BlackScreenTile> provider33, Provider<SettingTile> provider34, Provider<SoundTile> provider35, Provider<RebootTile> provider36) {
        this.qsHostLazyProvider = provider;
        this.customTileBuilderProvider = provider2;
        this.wifiTileProvider = provider3;
        this.internetTileProvider = provider4;
        this.bluetoothTileProvider = provider5;
        this.cellularTileProvider = provider6;
        this.dndTileProvider = provider7;
        this.colorInversionTileProvider = provider8;
        this.airplaneModeTileProvider = provider9;
        this.workModeTileProvider = provider10;
        this.rotationLockTileProvider = provider11;
        this.flashlightTileProvider = provider12;
        this.locationTileProvider = provider13;
        this.castTileProvider = provider14;
        this.hotspotTileProvider = provider15;
        this.batterySaverTileProvider = provider16;
        this.dataSaverTileProvider = provider17;
        this.nightDisplayTileProvider = provider18;
        this.nfcTileProvider = provider19;
        this.memoryTileProvider = provider20;
        this.uiModeNightTileProvider = provider21;
        this.screenRecordTileProvider = provider22;
        this.reduceBrightColorsTileProvider = provider23;
        this.cameraToggleTileProvider = provider24;
        this.microphoneToggleTileProvider = provider25;
        this.deviceControlsTileProvider = provider26;
        this.alarmTileProvider = provider27;
        this.quickAccessWalletTileProvider = provider28;
        this.qrCodeScannerTileProvider = provider29;
        this.oneHandedModeTileProvider = provider30;
        this.colorCorrectionTileProvider = provider31;
        this.screenShotTileProvider = provider32;
        this.blackScreenTileProvider = provider33;
        this.settingTileProvider = provider34;
        this.soundTileProvider = provider35;
        this.rebootTileProvider = provider36;
    }

    public QSFactoryImpl get() {
        return newInstance(DoubleCheck.lazy(this.qsHostLazyProvider), this.customTileBuilderProvider, this.wifiTileProvider, this.internetTileProvider, this.bluetoothTileProvider, this.cellularTileProvider, this.dndTileProvider, this.colorInversionTileProvider, this.airplaneModeTileProvider, this.workModeTileProvider, this.rotationLockTileProvider, this.flashlightTileProvider, this.locationTileProvider, this.castTileProvider, this.hotspotTileProvider, this.batterySaverTileProvider, this.dataSaverTileProvider, this.nightDisplayTileProvider, this.nfcTileProvider, this.memoryTileProvider, this.uiModeNightTileProvider, this.screenRecordTileProvider, this.reduceBrightColorsTileProvider, this.cameraToggleTileProvider, this.microphoneToggleTileProvider, this.deviceControlsTileProvider, this.alarmTileProvider, this.quickAccessWalletTileProvider, this.qrCodeScannerTileProvider, this.oneHandedModeTileProvider, this.colorCorrectionTileProvider, this.screenShotTileProvider, this.blackScreenTileProvider, this.settingTileProvider, this.soundTileProvider, this.rebootTileProvider);
    }

    public static QSFactoryImpl_Factory create(Provider<QSHost> provider, Provider<CustomTile.Builder> provider2, Provider<WifiTile> provider3, Provider<InternetTile> provider4, Provider<BluetoothTile> provider5, Provider<CellularTile> provider6, Provider<DndTile> provider7, Provider<ColorInversionTile> provider8, Provider<AirplaneModeTile> provider9, Provider<WorkModeTile> provider10, Provider<RotationLockTile> provider11, Provider<FlashlightTile> provider12, Provider<LocationTile> provider13, Provider<CastTile> provider14, Provider<HotspotTile> provider15, Provider<BatterySaverTile> provider16, Provider<DataSaverTile> provider17, Provider<NightDisplayTile> provider18, Provider<NfcTile> provider19, Provider<GarbageMonitor.MemoryTile> provider20, Provider<UiModeNightTile> provider21, Provider<ScreenRecordTile> provider22, Provider<ReduceBrightColorsTile> provider23, Provider<CameraToggleTile> provider24, Provider<MicrophoneToggleTile> provider25, Provider<DeviceControlsTile> provider26, Provider<AlarmTile> provider27, Provider<QuickAccessWalletTile> provider28, Provider<QRCodeScannerTile> provider29, Provider<OneHandedModeTile> provider30, Provider<ColorCorrectionTile> provider31, Provider<ScreenShotTile> provider32, Provider<BlackScreenTile> provider33, Provider<SettingTile> provider34, Provider<SoundTile> provider35, Provider<RebootTile> provider36) {
        return new QSFactoryImpl_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, provider18, provider19, provider20, provider21, provider22, provider23, provider24, provider25, provider26, provider27, provider28, provider29, provider30, provider31, provider32, provider33, provider34, provider35, provider36);
    }

    public static QSFactoryImpl newInstance(Lazy<QSHost> lazy, Provider<CustomTile.Builder> provider, Provider<WifiTile> provider2, Provider<InternetTile> provider3, Provider<BluetoothTile> provider4, Provider<CellularTile> provider5, Provider<DndTile> provider6, Provider<ColorInversionTile> provider7, Provider<AirplaneModeTile> provider8, Provider<WorkModeTile> provider9, Provider<RotationLockTile> provider10, Provider<FlashlightTile> provider11, Provider<LocationTile> provider12, Provider<CastTile> provider13, Provider<HotspotTile> provider14, Provider<BatterySaverTile> provider15, Provider<DataSaverTile> provider16, Provider<NightDisplayTile> provider17, Provider<NfcTile> provider18, Provider<GarbageMonitor.MemoryTile> provider19, Provider<UiModeNightTile> provider20, Provider<ScreenRecordTile> provider21, Provider<ReduceBrightColorsTile> provider22, Provider<CameraToggleTile> provider23, Provider<MicrophoneToggleTile> provider24, Provider<DeviceControlsTile> provider25, Provider<AlarmTile> provider26, Provider<QuickAccessWalletTile> provider27, Provider<QRCodeScannerTile> provider28, Provider<OneHandedModeTile> provider29, Provider<ColorCorrectionTile> provider30, Provider<ScreenShotTile> provider31, Provider<BlackScreenTile> provider32, Provider<SettingTile> provider33, Provider<SoundTile> provider34, Provider<RebootTile> provider35) {
        return new QSFactoryImpl(lazy, provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, provider18, provider19, provider20, provider21, provider22, provider23, provider24, provider25, provider26, provider27, provider28, provider29, provider30, provider31, provider32, provider33, provider34, provider35);
    }
}
