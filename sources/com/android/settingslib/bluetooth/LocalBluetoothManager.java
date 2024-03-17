package com.android.settingslib.bluetooth;

import android.content.Context;
import android.os.Handler;
import android.os.UserHandle;

public class LocalBluetoothManager {
    public final CachedBluetoothDeviceManager mCachedDeviceManager;
    public final Context mContext;
    public final BluetoothEventManager mEventManager;
    public final LocalBluetoothAdapter mLocalAdapter;
    public final LocalBluetoothProfileManager mProfileManager;

    public static LocalBluetoothManager create(Context context, Handler handler, UserHandle userHandle) {
        LocalBluetoothAdapter instance = LocalBluetoothAdapter.getInstance();
        if (instance == null) {
            return null;
        }
        return new LocalBluetoothManager(instance, context, handler, userHandle);
    }

    public LocalBluetoothManager(LocalBluetoothAdapter localBluetoothAdapter, Context context, Handler handler, UserHandle userHandle) {
        Context applicationContext = context.getApplicationContext();
        this.mContext = applicationContext;
        this.mLocalAdapter = localBluetoothAdapter;
        CachedBluetoothDeviceManager cachedBluetoothDeviceManager = new CachedBluetoothDeviceManager(applicationContext, this);
        this.mCachedDeviceManager = cachedBluetoothDeviceManager;
        BluetoothEventManager bluetoothEventManager = new BluetoothEventManager(localBluetoothAdapter, cachedBluetoothDeviceManager, applicationContext, handler, userHandle);
        this.mEventManager = bluetoothEventManager;
        LocalBluetoothProfileManager localBluetoothProfileManager = new LocalBluetoothProfileManager(applicationContext, localBluetoothAdapter, cachedBluetoothDeviceManager, bluetoothEventManager);
        this.mProfileManager = localBluetoothProfileManager;
        localBluetoothProfileManager.updateLocalProfiles();
        bluetoothEventManager.readPairedDevices();
    }

    public LocalBluetoothAdapter getBluetoothAdapter() {
        return this.mLocalAdapter;
    }

    public CachedBluetoothDeviceManager getCachedDeviceManager() {
        return this.mCachedDeviceManager;
    }

    public BluetoothEventManager getEventManager() {
        return this.mEventManager;
    }

    public LocalBluetoothProfileManager getProfileManager() {
        return this.mProfileManager;
    }
}
