package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothLeAudio;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;
import com.android.settingslib.R$drawable;
import java.util.ArrayList;
import java.util.List;

public class LeAudioProfile implements LocalBluetoothProfile {
    public static boolean DEBUG = true;
    public final BluetoothAdapter mBluetoothAdapter;
    public Context mContext;
    public final CachedBluetoothDeviceManager mDeviceManager;
    public boolean mIsProfileReady;
    public final LocalBluetoothProfileManager mProfileManager;
    public BluetoothLeAudio mService;

    public boolean accessProfileEnabled() {
        return true;
    }

    public int getProfileId() {
        return 22;
    }

    public String toString() {
        return "LE_AUDIO";
    }

    public final class LeAudioServiceListener implements BluetoothProfile.ServiceListener {
        public LeAudioServiceListener() {
        }

        public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
            if (LeAudioProfile.DEBUG) {
                Log.d("LeAudioProfile", "Bluetooth service connected");
            }
            LeAudioProfile.this.mService = (BluetoothLeAudio) bluetoothProfile;
            List connectedDevices = LeAudioProfile.this.mService.getConnectedDevices();
            while (!connectedDevices.isEmpty()) {
                BluetoothDevice bluetoothDevice = (BluetoothDevice) connectedDevices.remove(0);
                CachedBluetoothDevice findDevice = LeAudioProfile.this.mDeviceManager.findDevice(bluetoothDevice);
                if (findDevice == null) {
                    if (LeAudioProfile.DEBUG) {
                        Log.d("LeAudioProfile", "LeAudioProfile found new device: " + bluetoothDevice);
                    }
                    findDevice = LeAudioProfile.this.mDeviceManager.addDevice(bluetoothDevice);
                }
                findDevice.onProfileStateChanged(LeAudioProfile.this, 2);
                findDevice.refresh();
            }
            LeAudioProfile.this.mProfileManager.callServiceConnectedListeners();
            LeAudioProfile.this.mIsProfileReady = true;
        }

        public void onServiceDisconnected(int i) {
            if (LeAudioProfile.DEBUG) {
                Log.d("LeAudioProfile", "Bluetooth service disconnected");
            }
            LeAudioProfile.this.mProfileManager.callServiceDisconnectedListeners();
            LeAudioProfile.this.mIsProfileReady = false;
        }
    }

    public LeAudioProfile(Context context, CachedBluetoothDeviceManager cachedBluetoothDeviceManager, LocalBluetoothProfileManager localBluetoothProfileManager) {
        this.mContext = context;
        this.mDeviceManager = cachedBluetoothDeviceManager;
        this.mProfileManager = localBluetoothProfileManager;
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mBluetoothAdapter = defaultAdapter;
        defaultAdapter.getProfileProxy(context, new LeAudioServiceListener(), 22);
    }

    public int getConnectionStatus(BluetoothDevice bluetoothDevice) {
        BluetoothLeAudio bluetoothLeAudio = this.mService;
        if (bluetoothLeAudio == null) {
            return 0;
        }
        return bluetoothLeAudio.getConnectionState(bluetoothDevice);
    }

    public List<BluetoothDevice> getActiveDevices() {
        BluetoothAdapter bluetoothAdapter = this.mBluetoothAdapter;
        if (bluetoothAdapter == null) {
            return new ArrayList();
        }
        return bluetoothAdapter.getActiveDevices(22);
    }

    public boolean setEnabled(BluetoothDevice bluetoothDevice, boolean z) {
        BluetoothLeAudio bluetoothLeAudio = this.mService;
        if (bluetoothLeAudio == null || bluetoothDevice == null) {
            return false;
        }
        if (!z) {
            return bluetoothLeAudio.setConnectionPolicy(bluetoothDevice, 0);
        }
        if (bluetoothLeAudio.getConnectionPolicy(bluetoothDevice) < 100) {
            return this.mService.setConnectionPolicy(bluetoothDevice, 100);
        }
        return false;
    }

    public int getDrawableResource(BluetoothClass bluetoothClass) {
        return R$drawable.ic_bt_le_audio;
    }

    public void finalize() {
        if (DEBUG) {
            Log.d("LeAudioProfile", "finalize()");
        }
        if (this.mService != null) {
            try {
                BluetoothAdapter.getDefaultAdapter().closeProfileProxy(22, this.mService);
                this.mService = null;
            } catch (Throwable th) {
                Log.w("LeAudioProfile", "Error cleaning up LeAudio proxy", th);
            }
        }
    }
}
