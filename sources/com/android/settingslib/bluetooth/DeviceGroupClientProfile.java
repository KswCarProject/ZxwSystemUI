package com.android.settingslib.bluetooth;

import android.app.ActivityThread;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothDeviceGroup;
import android.bluetooth.BluetoothGroupCallback;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.android.systemui.theme.ThemeOverlayApplier;
import java.util.UUID;

public class DeviceGroupClientProfile implements LocalBluetoothProfile {
    public String mCallingPackage;
    public final CachedBluetoothDeviceManager mDeviceManager;
    public final BluetoothGroupCallback mGroupCallback = new BluetoothGroupCallback() {
        public void onNewGroupFound(int i, BluetoothDevice bluetoothDevice, UUID uuid) {
            Log.d("DeviceGroupClientProfile", "onNewGroupFound()");
            CachedBluetoothDevice findDevice = DeviceGroupClientProfile.this.mDeviceManager.findDevice(bluetoothDevice);
            if (findDevice == null) {
                findDevice = DeviceGroupClientProfile.this.mDeviceManager.addDevice(bluetoothDevice);
            }
            DeviceGroupClientProfile.this.mProfileManager.mEventManager.dispatchNewGroupFound(findDevice, i, uuid);
            Log.d("DeviceGroupClientProfile", "Start Group Discovery for Audio capable device");
            DeviceGroupClientProfile.this.mService.startGroupDiscovery(i);
        }

        public void onGroupDiscoveryStatusChanged(int i, int i2, int i3) {
            Log.d("DeviceGroupClientProfile", "onGroupDiscoveryStatusChanged()");
            DeviceGroupClientProfile.this.mProfileManager.mEventManager.dispatchGroupDiscoveryStatusChanged(i, i2, i3);
        }
    };
    public boolean mIsProfileReady;
    public final LocalBluetoothProfileManager mProfileManager;
    public BluetoothDeviceGroup mService;

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
        return 32;
    }

    public boolean setEnabled(BluetoothDevice bluetoothDevice, boolean z) {
        return false;
    }

    public String toString() {
        return "DeviceGroup Client";
    }

    public DeviceGroupClientProfile(Context context, CachedBluetoothDeviceManager cachedBluetoothDeviceManager, LocalBluetoothProfileManager localBluetoothProfileManager) {
        this.mDeviceManager = cachedBluetoothDeviceManager;
        this.mProfileManager = localBluetoothProfileManager;
        this.mCallingPackage = ActivityThread.currentOpPackageName();
        BluetoothAdapter.getDefaultAdapter().getProfileProxy(context, new GroupClientServiceListener(), 32);
    }

    public final class GroupClientServiceListener implements BluetoothProfile.ServiceListener {
        public GroupClientServiceListener() {
        }

        public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
            DeviceGroupClientProfile.this.mService = (BluetoothDeviceGroup) bluetoothProfile;
            DeviceGroupClientProfile.this.mIsProfileReady = true;
            Log.d("DeviceGroupClientProfile", "onServiceConnected: mCallingPackage = " + DeviceGroupClientProfile.this.mCallingPackage);
            if (ThemeOverlayApplier.SETTINGS_PACKAGE.equals(DeviceGroupClientProfile.this.mCallingPackage)) {
                DeviceGroupClientProfile.this.mService.registerGroupClientApp(DeviceGroupClientProfile.this.mGroupCallback, new Handler(Looper.getMainLooper()));
            }
        }

        public void onServiceDisconnected(int i) {
            DeviceGroupClientProfile.this.mIsProfileReady = false;
        }
    }

    public void finalize() {
        Log.d("DeviceGroupClientProfile", "finalize()");
        if (this.mService != null) {
            try {
                BluetoothAdapter.getDefaultAdapter().closeProfileProxy(32, this.mService);
                this.mService = null;
            } catch (Throwable th) {
                Log.w("DeviceGroupClientProfile", "Error cleaning up BluetoothDeviceGroup proxy Object", th);
            }
        }
    }
}
