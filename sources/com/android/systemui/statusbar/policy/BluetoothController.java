package com.android.systemui.statusbar.policy;

import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.systemui.Dumpable;
import java.util.List;

public interface BluetoothController extends CallbackController<Callback>, Dumpable {

    public interface Callback {
        void onBluetoothDevicesChanged();

        void onBluetoothStateChange(boolean z);
    }

    boolean canConfigBluetooth();

    int getBluetoothState();

    String getConnectedDeviceName();

    List<CachedBluetoothDevice> getConnectedDevices();

    boolean isBluetoothAudioActive();

    boolean isBluetoothAudioProfileOnly();

    boolean isBluetoothConnected();

    boolean isBluetoothConnecting();

    boolean isBluetoothEnabled();

    boolean isBluetoothSupported();

    void setBluetoothEnabled(boolean z);
}
