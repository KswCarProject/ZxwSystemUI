package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;

public class VolumeControlProfile implements LocalBluetoothProfile {
    public boolean accessProfileEnabled() {
        return false;
    }

    public int getConnectionStatus(BluetoothDevice bluetoothDevice) {
        return 0;
    }

    public int getDrawableResource(BluetoothClass bluetoothClass) {
        return 0;
    }

    public int getProfileId() {
        return 23;
    }

    public boolean setEnabled(BluetoothDevice bluetoothDevice, boolean z) {
        return false;
    }

    public String toString() {
        return "VCP";
    }
}
