package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothMapClient;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothUuid;
import android.content.Context;
import android.os.ParcelUuid;
import android.util.Log;
import java.util.List;

public final class MapClientProfile implements LocalBluetoothProfile {
    public static final ParcelUuid[] UUIDS = {BluetoothUuid.MAS};
    public final CachedBluetoothDeviceManager mDeviceManager;
    public boolean mIsProfileReady;
    public final LocalBluetoothProfileManager mProfileManager;
    public BluetoothMapClient mService;

    public boolean accessProfileEnabled() {
        return true;
    }

    public int getDrawableResource(BluetoothClass bluetoothClass) {
        return 17302816;
    }

    public int getProfileId() {
        return 18;
    }

    public String toString() {
        return "MAP Client";
    }

    public final class MapClientServiceListener implements BluetoothProfile.ServiceListener {
        public MapClientServiceListener() {
        }

        public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
            MapClientProfile.this.mService = (BluetoothMapClient) bluetoothProfile;
            List connectedDevices = MapClientProfile.this.mService.getConnectedDevices();
            while (!connectedDevices.isEmpty()) {
                BluetoothDevice bluetoothDevice = (BluetoothDevice) connectedDevices.remove(0);
                CachedBluetoothDevice findDevice = MapClientProfile.this.mDeviceManager.findDevice(bluetoothDevice);
                if (findDevice == null) {
                    Log.w("MapClientProfile", "MapProfile found new device: " + bluetoothDevice);
                    findDevice = MapClientProfile.this.mDeviceManager.addDevice(bluetoothDevice);
                }
                findDevice.onProfileStateChanged(MapClientProfile.this, 2);
                findDevice.refresh();
            }
            MapClientProfile.this.mProfileManager.callServiceConnectedListeners();
            MapClientProfile.this.mIsProfileReady = true;
        }

        public void onServiceDisconnected(int i) {
            MapClientProfile.this.mProfileManager.callServiceDisconnectedListeners();
            MapClientProfile.this.mIsProfileReady = false;
        }
    }

    public MapClientProfile(Context context, CachedBluetoothDeviceManager cachedBluetoothDeviceManager, LocalBluetoothProfileManager localBluetoothProfileManager) {
        this.mDeviceManager = cachedBluetoothDeviceManager;
        this.mProfileManager = localBluetoothProfileManager;
        BluetoothAdapter.getDefaultAdapter().getProfileProxy(context, new MapClientServiceListener(), 18);
    }

    public int getConnectionStatus(BluetoothDevice bluetoothDevice) {
        BluetoothMapClient bluetoothMapClient = this.mService;
        if (bluetoothMapClient == null) {
            return 0;
        }
        return bluetoothMapClient.getConnectionState(bluetoothDevice);
    }

    public boolean setEnabled(BluetoothDevice bluetoothDevice, boolean z) {
        BluetoothMapClient bluetoothMapClient = this.mService;
        if (bluetoothMapClient == null) {
            return false;
        }
        if (!z) {
            return bluetoothMapClient.setConnectionPolicy(bluetoothDevice, 0);
        }
        if (bluetoothMapClient.getConnectionPolicy(bluetoothDevice) < 100) {
            return this.mService.setConnectionPolicy(bluetoothDevice, 100);
        }
        return false;
    }

    public void finalize() {
        Log.d("MapClientProfile", "finalize()");
        if (this.mService != null) {
            try {
                BluetoothAdapter.getDefaultAdapter().closeProfileProxy(18, this.mService);
                this.mService = null;
            } catch (Throwable th) {
                Log.w("MapClientProfile", "Error cleaning up MAP Client proxy", th);
            }
        }
    }
}