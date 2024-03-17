package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothUuid;
import android.content.Context;
import android.os.ParcelUuid;
import android.util.Log;
import java.util.List;

public class A2dpProfile implements LocalBluetoothProfile {
    public static final ParcelUuid[] SINK_UUIDS = {BluetoothUuid.A2DP_SINK, BluetoothUuid.ADV_AUDIO_DIST};
    public final BluetoothAdapter mBluetoothAdapter;
    public Context mContext;
    public final CachedBluetoothDeviceManager mDeviceManager;
    public boolean mIsProfileReady;
    public final LocalBluetoothProfileManager mProfileManager;
    public BluetoothA2dp mService;

    public boolean accessProfileEnabled() {
        return true;
    }

    public int getDrawableResource(BluetoothClass bluetoothClass) {
        return 17302335;
    }

    public int getProfileId() {
        return 2;
    }

    public String toString() {
        return "A2DP";
    }

    public final class A2dpServiceListener implements BluetoothProfile.ServiceListener {
        public A2dpServiceListener() {
        }

        public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
            A2dpProfile.this.mService = (BluetoothA2dp) bluetoothProfile;
            List<BluetoothDevice> connectedDevices = A2dpProfile.this.mService.getConnectedDevices();
            while (!connectedDevices.isEmpty()) {
                BluetoothDevice remove = connectedDevices.remove(0);
                CachedBluetoothDevice findDevice = A2dpProfile.this.mDeviceManager.findDevice(remove);
                if (findDevice == null) {
                    Log.w("A2dpProfile", "A2dpProfile found new device: " + remove);
                    findDevice = A2dpProfile.this.mDeviceManager.addDevice(remove);
                }
                findDevice.onProfileStateChanged(A2dpProfile.this, 2);
                findDevice.refresh();
            }
            A2dpProfile.this.mIsProfileReady = true;
            A2dpProfile.this.mProfileManager.callServiceConnectedListeners();
        }

        public void onServiceDisconnected(int i) {
            A2dpProfile.this.mIsProfileReady = false;
        }
    }

    public A2dpProfile(Context context, CachedBluetoothDeviceManager cachedBluetoothDeviceManager, LocalBluetoothProfileManager localBluetoothProfileManager) {
        this.mContext = context;
        this.mDeviceManager = cachedBluetoothDeviceManager;
        this.mProfileManager = localBluetoothProfileManager;
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mBluetoothAdapter = defaultAdapter;
        defaultAdapter.getProfileProxy(context, new A2dpServiceListener(), 2);
    }

    public int getConnectionStatus(BluetoothDevice bluetoothDevice) {
        BluetoothA2dp bluetoothA2dp = this.mService;
        if (bluetoothA2dp == null) {
            return 0;
        }
        return bluetoothA2dp.getConnectionState(bluetoothDevice);
    }

    public BluetoothDevice getActiveDevice() {
        BluetoothAdapter bluetoothAdapter = this.mBluetoothAdapter;
        if (bluetoothAdapter == null) {
            return null;
        }
        List activeDevices = bluetoothAdapter.getActiveDevices(2);
        if (activeDevices.size() > 0) {
            return (BluetoothDevice) activeDevices.get(0);
        }
        return null;
    }

    public boolean setEnabled(BluetoothDevice bluetoothDevice, boolean z) {
        BluetoothA2dp bluetoothA2dp = this.mService;
        if (bluetoothA2dp == null) {
            return false;
        }
        if (!z) {
            return bluetoothA2dp.setConnectionPolicy(bluetoothDevice, 0);
        }
        if (bluetoothA2dp.getConnectionPolicy(bluetoothDevice) < 100) {
            return this.mService.setConnectionPolicy(bluetoothDevice, 100);
        }
        return false;
    }

    public void finalize() {
        Log.d("A2dpProfile", "finalize()");
        if (this.mService != null) {
            try {
                BluetoothAdapter.getDefaultAdapter().closeProfileProxy(2, this.mService);
                this.mService = null;
            } catch (Throwable th) {
                Log.w("A2dpProfile", "Error cleaning up A2DP proxy", th);
            }
        }
    }
}
