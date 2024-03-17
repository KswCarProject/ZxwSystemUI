package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothCsipSetCoordinator;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.os.ParcelUuid;
import android.util.Log;
import java.util.List;
import java.util.Map;

public class CsipSetCoordinatorProfile implements LocalBluetoothProfile {
    public Context mContext;
    public final CachedBluetoothDeviceManager mDeviceManager;
    public boolean mIsProfileReady;
    public final LocalBluetoothProfileManager mProfileManager;
    public BluetoothCsipSetCoordinator mService;

    public boolean accessProfileEnabled() {
        return false;
    }

    public int getDrawableResource(BluetoothClass bluetoothClass) {
        return 0;
    }

    public int getProfileId() {
        return 25;
    }

    public String toString() {
        return "CSIP Set Coordinator";
    }

    public final class CoordinatedSetServiceListener implements BluetoothProfile.ServiceListener {
        public CoordinatedSetServiceListener() {
        }

        public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
            Log.d("CsipSetCoordinatorProfile", "Bluetooth service connected");
            CsipSetCoordinatorProfile.this.mService = (BluetoothCsipSetCoordinator) bluetoothProfile;
            List connectedDevices = CsipSetCoordinatorProfile.this.mService.getConnectedDevices();
            while (!connectedDevices.isEmpty()) {
                BluetoothDevice bluetoothDevice = (BluetoothDevice) connectedDevices.remove(0);
                CachedBluetoothDevice findDevice = CsipSetCoordinatorProfile.this.mDeviceManager.findDevice(bluetoothDevice);
                if (findDevice == null) {
                    Log.d("CsipSetCoordinatorProfile", "CsipSetCoordinatorProfile found new device: " + bluetoothDevice);
                    findDevice = CsipSetCoordinatorProfile.this.mDeviceManager.addDevice(bluetoothDevice);
                }
                findDevice.onProfileStateChanged(CsipSetCoordinatorProfile.this, 2);
                findDevice.refresh();
            }
            CsipSetCoordinatorProfile.this.mDeviceManager.updateCsipDevices();
            CsipSetCoordinatorProfile.this.mProfileManager.callServiceConnectedListeners();
            CsipSetCoordinatorProfile.this.mIsProfileReady = true;
        }

        public void onServiceDisconnected(int i) {
            Log.d("CsipSetCoordinatorProfile", "Bluetooth service disconnected");
            CsipSetCoordinatorProfile.this.mProfileManager.callServiceDisconnectedListeners();
            CsipSetCoordinatorProfile.this.mIsProfileReady = false;
        }
    }

    public CsipSetCoordinatorProfile(Context context, CachedBluetoothDeviceManager cachedBluetoothDeviceManager, LocalBluetoothProfileManager localBluetoothProfileManager) {
        this.mContext = context;
        this.mDeviceManager = cachedBluetoothDeviceManager;
        this.mProfileManager = localBluetoothProfileManager;
        BluetoothAdapter.getDefaultAdapter().getProfileProxy(context, new CoordinatedSetServiceListener(), 25);
    }

    public int getConnectionStatus(BluetoothDevice bluetoothDevice) {
        BluetoothCsipSetCoordinator bluetoothCsipSetCoordinator = this.mService;
        if (bluetoothCsipSetCoordinator == null) {
            return 0;
        }
        return bluetoothCsipSetCoordinator.getConnectionState(bluetoothDevice);
    }

    public boolean setEnabled(BluetoothDevice bluetoothDevice, boolean z) {
        BluetoothCsipSetCoordinator bluetoothCsipSetCoordinator = this.mService;
        if (bluetoothCsipSetCoordinator == null || bluetoothDevice == null) {
            return false;
        }
        if (!z) {
            return bluetoothCsipSetCoordinator.setConnectionPolicy(bluetoothDevice, 0);
        }
        if (bluetoothCsipSetCoordinator.getConnectionPolicy(bluetoothDevice) < 100) {
            return this.mService.setConnectionPolicy(bluetoothDevice, 100);
        }
        return false;
    }

    public Map<Integer, ParcelUuid> getGroupUuidMapByDevice(BluetoothDevice bluetoothDevice) {
        BluetoothCsipSetCoordinator bluetoothCsipSetCoordinator = this.mService;
        if (bluetoothCsipSetCoordinator == null || bluetoothDevice == null) {
            return null;
        }
        return bluetoothCsipSetCoordinator.getGroupUuidMapByDevice(bluetoothDevice);
    }

    public void finalize() {
        Log.d("CsipSetCoordinatorProfile", "finalize()");
        if (this.mService != null) {
            try {
                BluetoothAdapter.getDefaultAdapter().closeProfileProxy(25, this.mService);
                this.mService = null;
            } catch (Throwable th) {
                Log.w("CsipSetCoordinatorProfile", "Error cleaning up CSIP Set Coordinator proxy", th);
            }
        }
    }
}
