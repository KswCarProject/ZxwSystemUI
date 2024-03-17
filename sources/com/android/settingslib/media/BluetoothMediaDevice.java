package com.android.settingslib.media;

import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaRoute2Info;
import android.media.MediaRouter2Manager;
import com.android.settingslib.R$drawable;
import com.android.settingslib.bluetooth.BluetoothUtils;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;

public class BluetoothMediaDevice extends MediaDevice {
    public final AudioManager mAudioManager;
    public CachedBluetoothDevice mCachedDevice;

    public BluetoothMediaDevice(Context context, CachedBluetoothDevice cachedBluetoothDevice, MediaRouter2Manager mediaRouter2Manager, MediaRoute2Info mediaRoute2Info, String str) {
        super(context, mediaRouter2Manager, mediaRoute2Info, str);
        this.mCachedDevice = cachedBluetoothDevice;
        this.mAudioManager = (AudioManager) context.getSystemService(AudioManager.class);
        initDeviceRecord();
    }

    public String getName() {
        return this.mCachedDevice.getName();
    }

    public Drawable getIcon() {
        if (BluetoothUtils.isAdvancedDetailsHeader(this.mCachedDevice.getDevice())) {
            return this.mContext.getDrawable(R$drawable.ic_earbuds_advanced);
        }
        return (Drawable) BluetoothUtils.getBtClassDrawableWithDescription(this.mContext, this.mCachedDevice).first;
    }

    public Drawable getIconWithoutBackground() {
        if (BluetoothUtils.isAdvancedDetailsHeader(this.mCachedDevice.getDevice())) {
            return this.mContext.getDrawable(R$drawable.ic_earbuds_advanced);
        }
        return (Drawable) BluetoothUtils.getBtClassDrawableWithDescription(this.mContext, this.mCachedDevice).first;
    }

    public String getId() {
        return MediaDeviceUtils.getId(this.mCachedDevice);
    }

    public CachedBluetoothDevice getCachedDevice() {
        return this.mCachedDevice;
    }

    public boolean isCarKitDevice() {
        BluetoothClass bluetoothClass = this.mCachedDevice.getDevice().getBluetoothClass();
        if (bluetoothClass == null) {
            return false;
        }
        int deviceClass = bluetoothClass.getDeviceClass();
        return deviceClass == 1032 || deviceClass == 1056;
    }

    public boolean isFastPairDevice() {
        CachedBluetoothDevice cachedBluetoothDevice = this.mCachedDevice;
        return cachedBluetoothDevice != null && BluetoothUtils.getBooleanMetaData(cachedBluetoothDevice.getDevice(), 6);
    }

    public boolean isMutingExpectedDevice() {
        return this.mAudioManager.getMutingExpectedDevice() != null && this.mCachedDevice.getAddress().equals(this.mAudioManager.getMutingExpectedDevice().getAddress());
    }

    public boolean isConnected() {
        return this.mCachedDevice.getBondState() == 12 && this.mCachedDevice.isConnected();
    }
}
