package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothVcp;
import android.content.Context;
import android.util.Log;

public class VcpProfile implements LocalBluetoothProfile {
    public final BluetoothAdapter mBluetoothAdapter;
    public Context mContext;
    public final CachedBluetoothDeviceManager mDeviceManager;
    public boolean mIsProfileReady;
    public final LocalBluetoothProfileManager mProfileManager;
    public BluetoothVcp mService;

    public boolean accessProfileEnabled() {
        return false;
    }

    public int getDrawableResource(BluetoothClass bluetoothClass) {
        return 0;
    }

    public int getProfileId() {
        return 34;
    }

    public boolean setEnabled(BluetoothDevice bluetoothDevice, boolean z) {
        return false;
    }

    public String toString() {
        return "VCP";
    }

    public final class VcpServiceListener implements BluetoothProfile.ServiceListener {
        public VcpServiceListener() {
        }

        public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
            VcpProfile.this.mService = (BluetoothVcp) bluetoothProfile;
            Log.w("VcpProfile", "Bluetooth service Connected");
            VcpProfile.this.mIsProfileReady = true;
            VcpProfile.this.mProfileManager.callServiceConnectedListeners();
        }

        public void onServiceDisconnected(int i) {
            Log.w("VcpProfile", "Bluetooth service Disconnected");
            VcpProfile.this.mIsProfileReady = false;
        }
    }

    public VcpProfile(Context context, CachedBluetoothDeviceManager cachedBluetoothDeviceManager, LocalBluetoothProfileManager localBluetoothProfileManager) {
        this.mContext = context;
        this.mDeviceManager = cachedBluetoothDeviceManager;
        this.mProfileManager = localBluetoothProfileManager;
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mBluetoothAdapter = defaultAdapter;
        defaultAdapter.getProfileProxy(context, new VcpServiceListener(), 34);
    }

    public int getConnectionStatus(BluetoothDevice bluetoothDevice) {
        BluetoothVcp bluetoothVcp = this.mService;
        if (bluetoothVcp == null) {
            return 0;
        }
        return bluetoothVcp.getConnectionState(bluetoothDevice);
    }

    public void finalize() {
        Log.d("VcpProfile", "finalize()");
        if (this.mService != null) {
            try {
                BluetoothAdapter.getDefaultAdapter().closeProfileProxy(34, this.mService);
                this.mService = null;
            } catch (Throwable th) {
                Log.w("VcpProfile", "Error cleaning up Vcp proxy", th);
            }
        }
    }
}
