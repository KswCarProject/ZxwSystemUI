package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothDun;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;

public final class DunServerProfile implements LocalBluetoothProfile {
    public static boolean V = true;
    public boolean mIsProfileReady;
    public BluetoothDun mService;

    public boolean accessProfileEnabled() {
        return true;
    }

    public int getDrawableResource(BluetoothClass bluetoothClass) {
        return 17302340;
    }

    public int getProfileId() {
        return 31;
    }

    public boolean setEnabled(BluetoothDevice bluetoothDevice, boolean z) {
        return true;
    }

    public String toString() {
        return "DUN Server";
    }

    public final class DunServiceListener implements BluetoothProfile.ServiceListener {
        public DunServiceListener() {
        }

        public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
            if (DunServerProfile.V) {
                Log.d("DunServerProfile", "Bluetooth service connected");
            }
            DunServerProfile.this.mService = (BluetoothDun) bluetoothProfile;
            DunServerProfile.this.mIsProfileReady = true;
        }

        public void onServiceDisconnected(int i) {
            if (DunServerProfile.V) {
                Log.d("DunServerProfile", "Bluetooth service disconnected");
            }
            DunServerProfile.this.mIsProfileReady = false;
        }
    }

    public DunServerProfile(Context context) {
        BluetoothAdapter.getDefaultAdapter().getProfileProxy(context, new DunServiceListener(), 31);
    }

    public int getConnectionStatus(BluetoothDevice bluetoothDevice) {
        BluetoothDun bluetoothDun = this.mService;
        if (bluetoothDun == null) {
            return 0;
        }
        return bluetoothDun.getConnectionState(bluetoothDevice);
    }

    public void finalize() {
        if (V) {
            Log.d("DunServerProfile", "finalize()");
        }
        if (this.mService != null) {
            try {
                BluetoothAdapter.getDefaultAdapter().closeProfileProxy(31, this.mService);
                this.mService = null;
            } catch (Throwable th) {
                Log.w("DunServerProfile", "Error cleaning up DUN proxy", th);
            }
        }
    }
}
