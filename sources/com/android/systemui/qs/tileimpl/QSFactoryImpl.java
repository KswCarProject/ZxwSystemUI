package com.android.systemui.qs.tileimpl;

import android.content.Context;
import android.util.Log;
import com.android.systemui.plugins.qs.QSFactory;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.qs.QSTileView;
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
import javax.inject.Provider;

public class QSFactoryImpl implements QSFactory {
    public final Provider<AirplaneModeTile> mAirplaneModeTileProvider;
    public final Provider<AlarmTile> mAlarmTileProvider;
    public final Provider<BatterySaverTile> mBatterySaverTileProvider;
    public final Provider<BlackScreenTile> mBlackScreenTileProvider;
    public final Provider<BluetoothTile> mBluetoothTileProvider;
    public final Provider<CameraToggleTile> mCameraToggleTileProvider;
    public final Provider<CastTile> mCastTileProvider;
    public final Provider<CellularTile> mCellularTileProvider;
    public final Provider<ColorCorrectionTile> mColorCorrectionTileProvider;
    public final Provider<ColorInversionTile> mColorInversionTileProvider;
    public final Provider<CustomTile.Builder> mCustomTileBuilderProvider;
    public final Provider<DataSaverTile> mDataSaverTileProvider;
    public final Provider<DeviceControlsTile> mDeviceControlsTileProvider;
    public final Provider<DndTile> mDndTileProvider;
    public final Provider<FlashlightTile> mFlashlightTileProvider;
    public final Provider<HotspotTile> mHotspotTileProvider;
    public final Provider<InternetTile> mInternetTileProvider;
    public final Provider<LocationTile> mLocationTileProvider;
    public final Provider<GarbageMonitor.MemoryTile> mMemoryTileProvider;
    public final Provider<MicrophoneToggleTile> mMicrophoneToggleTileProvider;
    public final Provider<NfcTile> mNfcTileProvider;
    public final Provider<NightDisplayTile> mNightDisplayTileProvider;
    public final Provider<OneHandedModeTile> mOneHandedModeTileProvider;
    public final Provider<QRCodeScannerTile> mQRCodeScannerTileProvider;
    public final Lazy<QSHost> mQsHostLazy;
    public final Provider<QuickAccessWalletTile> mQuickAccessWalletTileProvider;
    public final Provider<RebootTile> mRebootTileProvider;
    public final Provider<ReduceBrightColorsTile> mReduceBrightColorsTileProvider;
    public final Provider<RotationLockTile> mRotationLockTileProvider;
    public final Provider<ScreenRecordTile> mScreenRecordTileProvider;
    public final Provider<ScreenShotTile> mScreenShotTileProvider;
    public final Provider<SettingTile> mSettingTileProvider;
    public final Provider<SoundTile> mSoundTileProvider;
    public final Provider<UiModeNightTile> mUiModeNightTileProvider;
    public final Provider<WifiTile> mWifiTileProvider;
    public final Provider<WorkModeTile> mWorkModeTileProvider;

    public QSFactoryImpl(Lazy<QSHost> lazy, Provider<CustomTile.Builder> provider, Provider<WifiTile> provider2, Provider<InternetTile> provider3, Provider<BluetoothTile> provider4, Provider<CellularTile> provider5, Provider<DndTile> provider6, Provider<ColorInversionTile> provider7, Provider<AirplaneModeTile> provider8, Provider<WorkModeTile> provider9, Provider<RotationLockTile> provider10, Provider<FlashlightTile> provider11, Provider<LocationTile> provider12, Provider<CastTile> provider13, Provider<HotspotTile> provider14, Provider<BatterySaverTile> provider15, Provider<DataSaverTile> provider16, Provider<NightDisplayTile> provider17, Provider<NfcTile> provider18, Provider<GarbageMonitor.MemoryTile> provider19, Provider<UiModeNightTile> provider20, Provider<ScreenRecordTile> provider21, Provider<ReduceBrightColorsTile> provider22, Provider<CameraToggleTile> provider23, Provider<MicrophoneToggleTile> provider24, Provider<DeviceControlsTile> provider25, Provider<AlarmTile> provider26, Provider<QuickAccessWalletTile> provider27, Provider<QRCodeScannerTile> provider28, Provider<OneHandedModeTile> provider29, Provider<ColorCorrectionTile> provider30, Provider<ScreenShotTile> provider31, Provider<BlackScreenTile> provider32, Provider<SettingTile> provider33, Provider<SoundTile> provider34, Provider<RebootTile> provider35) {
        this.mQsHostLazy = lazy;
        this.mCustomTileBuilderProvider = provider;
        this.mWifiTileProvider = provider2;
        this.mInternetTileProvider = provider3;
        this.mBluetoothTileProvider = provider4;
        this.mCellularTileProvider = provider5;
        this.mDndTileProvider = provider6;
        this.mColorInversionTileProvider = provider7;
        this.mAirplaneModeTileProvider = provider8;
        this.mWorkModeTileProvider = provider9;
        this.mRotationLockTileProvider = provider10;
        this.mFlashlightTileProvider = provider11;
        this.mLocationTileProvider = provider12;
        this.mCastTileProvider = provider13;
        this.mHotspotTileProvider = provider14;
        this.mBatterySaverTileProvider = provider15;
        this.mDataSaverTileProvider = provider16;
        this.mNightDisplayTileProvider = provider17;
        this.mNfcTileProvider = provider18;
        this.mMemoryTileProvider = provider19;
        this.mUiModeNightTileProvider = provider20;
        this.mScreenRecordTileProvider = provider21;
        this.mReduceBrightColorsTileProvider = provider22;
        this.mCameraToggleTileProvider = provider23;
        this.mMicrophoneToggleTileProvider = provider24;
        this.mDeviceControlsTileProvider = provider25;
        this.mAlarmTileProvider = provider26;
        this.mQuickAccessWalletTileProvider = provider27;
        this.mQRCodeScannerTileProvider = provider28;
        this.mOneHandedModeTileProvider = provider29;
        this.mColorCorrectionTileProvider = provider30;
        this.mScreenShotTileProvider = provider31;
        this.mBlackScreenTileProvider = provider32;
        this.mSettingTileProvider = provider33;
        this.mSoundTileProvider = provider34;
        this.mRebootTileProvider = provider35;
    }

    public final QSTile createTile(String str) {
        QSTileImpl createTileInternal = createTileInternal(str);
        if (createTileInternal != null) {
            createTileInternal.initialize();
            createTileInternal.postStale();
        }
        return createTileInternal;
    }

    public QSTileImpl createTileInternal(String str) {
        Log.w("QSFactory", "createTileInternal: " + str);
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case -2109393100:
                if (str.equals("onehanded")) {
                    c = 0;
                    break;
                }
                break;
            case -2016941037:
                if (str.equals("inversion")) {
                    c = 1;
                    break;
                }
                break;
            case -1183073498:
                if (str.equals("flashlight")) {
                    c = 2;
                    break;
                }
                break;
            case -934938715:
                if (str.equals("reboot")) {
                    c = 3;
                    break;
                }
                break;
            case -805491779:
                if (str.equals("screenrecord")) {
                    c = 4;
                    break;
                }
                break;
            case -795192327:
                if (str.equals("wallet")) {
                    c = 5;
                    break;
                }
                break;
            case -677011630:
                if (str.equals("airplane")) {
                    c = 6;
                    break;
                }
                break;
            case -657925702:
                if (str.equals("color_correction")) {
                    c = 7;
                    break;
                }
                break;
            case -566933834:
                if (str.equals("controls")) {
                    c = 8;
                    break;
                }
                break;
            case -416447130:
                if (str.equals("screenshot")) {
                    c = 9;
                    break;
                }
                break;
            case -343519030:
                if (str.equals("reduce_brightness")) {
                    c = 10;
                    break;
                }
                break;
            case -331239923:
                if (str.equals("battery")) {
                    c = 11;
                    break;
                }
                break;
            case -40300674:
                if (str.equals("rotation")) {
                    c = 12;
                    break;
                }
                break;
            case -37334949:
                if (str.equals("mictoggle")) {
                    c = 13;
                    break;
                }
                break;
            case 3154:
                if (str.equals("bt")) {
                    c = 14;
                    break;
                }
                break;
            case 99610:
                if (str.equals("dnd")) {
                    c = 15;
                    break;
                }
                break;
            case 108971:
                if (str.equals("nfc")) {
                    c = 16;
                    break;
                }
                break;
            case 3046207:
                if (str.equals("cast")) {
                    c = 17;
                    break;
                }
                break;
            case 3049826:
                if (str.equals("cell")) {
                    c = 18;
                    break;
                }
                break;
            case 3075958:
                if (str.equals("dark")) {
                    c = 19;
                    break;
                }
                break;
            case 3649301:
                if (str.equals("wifi")) {
                    c = 20;
                    break;
                }
                break;
            case 3655441:
                if (str.equals("work")) {
                    c = 21;
                    break;
                }
                break;
            case 6344377:
                if (str.equals("cameratoggle")) {
                    c = 22;
                    break;
                }
                break;
            case 92895825:
                if (str.equals("alarm")) {
                    c = 23;
                    break;
                }
                break;
            case 104817688:
                if (str.equals("night")) {
                    c = 24;
                    break;
                }
                break;
            case 109211285:
                if (str.equals("saver")) {
                    c = 25;
                    break;
                }
                break;
            case 109627663:
                if (str.equals("sound")) {
                    c = 26;
                    break;
                }
                break;
            case 570410817:
                if (str.equals("internet")) {
                    c = 27;
                    break;
                }
                break;
            case 876619530:
                if (str.equals("qr_code_scanner")) {
                    c = 28;
                    break;
                }
                break;
            case 1099603663:
                if (str.equals("hotspot")) {
                    c = 29;
                    break;
                }
                break;
            case 1319834923:
                if (str.equals("blackscreen")) {
                    c = 30;
                    break;
                }
                break;
            case 1901043637:
                if (str.equals("location")) {
                    c = 31;
                    break;
                }
                break;
            case 1985941072:
                if (str.equals("setting")) {
                    c = ' ';
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return this.mOneHandedModeTileProvider.get();
            case 1:
                return this.mColorInversionTileProvider.get();
            case 2:
                return this.mFlashlightTileProvider.get();
            case 3:
                return this.mRebootTileProvider.get();
            case 4:
                return this.mScreenRecordTileProvider.get();
            case 5:
                return this.mQuickAccessWalletTileProvider.get();
            case 6:
                return this.mAirplaneModeTileProvider.get();
            case 7:
                return this.mColorCorrectionTileProvider.get();
            case 8:
                return this.mDeviceControlsTileProvider.get();
            case 9:
                return this.mScreenShotTileProvider.get();
            case 10:
                return this.mReduceBrightColorsTileProvider.get();
            case 11:
                return this.mBatterySaverTileProvider.get();
            case 12:
                return this.mRotationLockTileProvider.get();
            case 13:
                return this.mMicrophoneToggleTileProvider.get();
            case 14:
                return this.mBluetoothTileProvider.get();
            case 15:
                return this.mDndTileProvider.get();
            case 16:
                return this.mNfcTileProvider.get();
            case 17:
                return this.mCastTileProvider.get();
            case 18:
                return this.mCellularTileProvider.get();
            case 19:
                return this.mUiModeNightTileProvider.get();
            case 20:
                return this.mWifiTileProvider.get();
            case 21:
                return this.mWorkModeTileProvider.get();
            case 22:
                return this.mCameraToggleTileProvider.get();
            case 23:
                return this.mAlarmTileProvider.get();
            case 24:
                return this.mNightDisplayTileProvider.get();
            case 25:
                return this.mDataSaverTileProvider.get();
            case 26:
                return this.mSoundTileProvider.get();
            case 27:
                return this.mInternetTileProvider.get();
            case 28:
                return this.mQRCodeScannerTileProvider.get();
            case 29:
                return this.mHotspotTileProvider.get();
            case 30:
                return this.mBlackScreenTileProvider.get();
            case 31:
                return this.mLocationTileProvider.get();
            case ' ':
                return this.mSettingTileProvider.get();
            default:
                if (str.startsWith("custom(")) {
                    return CustomTile.create(this.mCustomTileBuilderProvider.get(), str, this.mQsHostLazy.get().getUserContext());
                }
                Log.w("QSFactory", "No stock tile spec: " + str);
                return null;
        }
    }

    public QSTileView createTileView(Context context, QSTile qSTile, boolean z) {
        return new QSTileViewImpl(context, qSTile.createTileView(context), z);
    }
}
