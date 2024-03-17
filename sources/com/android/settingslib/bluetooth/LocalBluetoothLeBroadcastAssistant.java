package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothLeBroadcastAssistant;
import android.bluetooth.BluetoothLeBroadcastMetadata;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;
import java.util.List;

public class LocalBluetoothLeBroadcastAssistant implements LocalBluetoothProfile {
    public BluetoothLeBroadcastMetadata.Builder mBuilder = new BluetoothLeBroadcastMetadata.Builder();
    public final CachedBluetoothDeviceManager mDeviceManager;
    public boolean mIsProfileReady;
    public LocalBluetoothProfileManager mProfileManager;
    public BluetoothLeBroadcastAssistant mService;
    public final BluetoothProfile.ServiceListener mServiceListener;

    public boolean accessProfileEnabled() {
        return false;
    }

    public int getDrawableResource(BluetoothClass bluetoothClass) {
        return 0;
    }

    public int getProfileId() {
        return 29;
    }

    public String toString() {
        return "LE_AUDIO_BROADCAST_ASSISTANT";
    }

    public LocalBluetoothLeBroadcastAssistant(Context context, CachedBluetoothDeviceManager cachedBluetoothDeviceManager, LocalBluetoothProfileManager localBluetoothProfileManager) {
        AnonymousClass1 r0 = new BluetoothProfile.ServiceListener() {
            public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
                Log.d("LocalBluetoothLeBroadcastAssistant", "Bluetooth service connected");
                LocalBluetoothLeBroadcastAssistant.this.mService = (BluetoothLeBroadcastAssistant) bluetoothProfile;
                List connectedDevices = LocalBluetoothLeBroadcastAssistant.this.mService.getConnectedDevices();
                while (!connectedDevices.isEmpty()) {
                    BluetoothDevice bluetoothDevice = (BluetoothDevice) connectedDevices.remove(0);
                    CachedBluetoothDevice findDevice = LocalBluetoothLeBroadcastAssistant.this.mDeviceManager.findDevice(bluetoothDevice);
                    if (findDevice == null) {
                        Log.d("LocalBluetoothLeBroadcastAssistant", "LocalBluetoothLeBroadcastAssistant found new device: " + bluetoothDevice);
                        findDevice = LocalBluetoothLeBroadcastAssistant.this.mDeviceManager.addDevice(bluetoothDevice);
                    }
                    findDevice.onProfileStateChanged(LocalBluetoothLeBroadcastAssistant.this, 2);
                    findDevice.refresh();
                }
                LocalBluetoothLeBroadcastAssistant.this.mProfileManager.callServiceConnectedListeners();
                LocalBluetoothLeBroadcastAssistant.this.mIsProfileReady = true;
            }

            public void onServiceDisconnected(int i) {
                if (i != 29) {
                    Log.d("LocalBluetoothLeBroadcastAssistant", "The profile is not LE_AUDIO_BROADCAST_ASSISTANT");
                    return;
                }
                Log.d("LocalBluetoothLeBroadcastAssistant", "Bluetooth service disconnected");
                LocalBluetoothLeBroadcastAssistant.this.mProfileManager.callServiceDisconnectedListeners();
                LocalBluetoothLeBroadcastAssistant.this.mIsProfileReady = false;
            }
        };
        this.mServiceListener = r0;
        this.mProfileManager = localBluetoothProfileManager;
        this.mDeviceManager = cachedBluetoothDeviceManager;
        BluetoothAdapter.getDefaultAdapter().getProfileProxy(context, r0, 29);
    }

    public int getConnectionStatus(BluetoothDevice bluetoothDevice) {
        BluetoothLeBroadcastAssistant bluetoothLeBroadcastAssistant = this.mService;
        if (bluetoothLeBroadcastAssistant == null) {
            return 0;
        }
        return bluetoothLeBroadcastAssistant.getConnectionState(bluetoothDevice);
    }

    public boolean setEnabled(BluetoothDevice bluetoothDevice, boolean z) {
        BluetoothLeBroadcastAssistant bluetoothLeBroadcastAssistant = this.mService;
        if (bluetoothLeBroadcastAssistant == null || bluetoothDevice == null) {
            return false;
        }
        if (!z) {
            return bluetoothLeBroadcastAssistant.setConnectionPolicy(bluetoothDevice, 0);
        }
        if (bluetoothLeBroadcastAssistant.getConnectionPolicy(bluetoothDevice) < 100) {
            return this.mService.setConnectionPolicy(bluetoothDevice, 100);
        }
        return false;
    }

    public void finalize() {
        Log.d("LocalBluetoothLeBroadcastAssistant", "finalize()");
        if (this.mService != null) {
            try {
                BluetoothAdapter.getDefaultAdapter().closeProfileProxy(29, this.mService);
                this.mService = null;
            } catch (Throwable th) {
                Log.w("LocalBluetoothLeBroadcastAssistant", "Error cleaning up LeAudio proxy", th);
            }
        }
    }
}
