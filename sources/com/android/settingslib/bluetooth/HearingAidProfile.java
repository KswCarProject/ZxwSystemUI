package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHearingAid;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class HearingAidProfile implements LocalBluetoothProfile {
    public static boolean V = true;
    public final BluetoothAdapter mBluetoothAdapter;
    public Context mContext;
    public final CachedBluetoothDeviceManager mDeviceManager;
    public boolean mIsProfileReady;
    public final LocalBluetoothProfileManager mProfileManager;
    public BluetoothHearingAid mService;

    public boolean accessProfileEnabled() {
        return false;
    }

    public int getDrawableResource(BluetoothClass bluetoothClass) {
        return 17302337;
    }

    public int getProfileId() {
        return 21;
    }

    public String toString() {
        return "HearingAid";
    }

    public final class HearingAidServiceListener implements BluetoothProfile.ServiceListener {
        public HearingAidServiceListener() {
        }

        public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
            HearingAidProfile.this.mService = (BluetoothHearingAid) bluetoothProfile;
            List<BluetoothDevice> connectedDevices = HearingAidProfile.this.mService.getConnectedDevices();
            while (!connectedDevices.isEmpty()) {
                BluetoothDevice remove = connectedDevices.remove(0);
                CachedBluetoothDevice findDevice = HearingAidProfile.this.mDeviceManager.findDevice(remove);
                if (findDevice == null) {
                    if (HearingAidProfile.V) {
                        Log.d("HearingAidProfile", "HearingAidProfile found new device: " + remove);
                    }
                    findDevice = HearingAidProfile.this.mDeviceManager.addDevice(remove);
                }
                findDevice.onProfileStateChanged(HearingAidProfile.this, 2);
                findDevice.refresh();
            }
            HearingAidProfile.this.mDeviceManager.updateHearingAidsDevices();
            HearingAidProfile.this.mIsProfileReady = true;
            HearingAidProfile.this.mProfileManager.callServiceConnectedListeners();
        }

        public void onServiceDisconnected(int i) {
            HearingAidProfile.this.mIsProfileReady = false;
        }
    }

    public HearingAidProfile(Context context, CachedBluetoothDeviceManager cachedBluetoothDeviceManager, LocalBluetoothProfileManager localBluetoothProfileManager) {
        this.mContext = context;
        this.mDeviceManager = cachedBluetoothDeviceManager;
        this.mProfileManager = localBluetoothProfileManager;
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mBluetoothAdapter = defaultAdapter;
        defaultAdapter.getProfileProxy(context, new HearingAidServiceListener(), 21);
    }

    public int getConnectionStatus(BluetoothDevice bluetoothDevice) {
        BluetoothHearingAid bluetoothHearingAid = this.mService;
        if (bluetoothHearingAid == null) {
            return 0;
        }
        return bluetoothHearingAid.getConnectionState(bluetoothDevice);
    }

    public List<BluetoothDevice> getActiveDevices() {
        BluetoothAdapter bluetoothAdapter = this.mBluetoothAdapter;
        if (bluetoothAdapter == null) {
            return new ArrayList();
        }
        return bluetoothAdapter.getActiveDevices(21);
    }

    public boolean setEnabled(BluetoothDevice bluetoothDevice, boolean z) {
        BluetoothHearingAid bluetoothHearingAid = this.mService;
        if (bluetoothHearingAid == null || bluetoothDevice == null) {
            return false;
        }
        if (!z) {
            return bluetoothHearingAid.setConnectionPolicy(bluetoothDevice, 0);
        }
        if (bluetoothHearingAid.getConnectionPolicy(bluetoothDevice) < 100) {
            return this.mService.setConnectionPolicy(bluetoothDevice, 100);
        }
        return false;
    }

    public long getHiSyncId(BluetoothDevice bluetoothDevice) {
        BluetoothHearingAid bluetoothHearingAid = this.mService;
        if (bluetoothHearingAid == null || bluetoothDevice == null) {
            return 0;
        }
        return bluetoothHearingAid.getHiSyncId(bluetoothDevice);
    }

    public int getDeviceSide(BluetoothDevice bluetoothDevice) {
        BluetoothHearingAid bluetoothHearingAid = this.mService;
        if (bluetoothHearingAid == null) {
            Log.w("HearingAidProfile", "Proxy not attached to HearingAidService");
            return -1;
        }
        try {
            Method declaredMethod = bluetoothHearingAid.getClass().getDeclaredMethod("getDeviceSideInternal", new Class[]{BluetoothDevice.class});
            declaredMethod.setAccessible(true);
            return ((Integer) declaredMethod.invoke(this.mService, new Object[]{bluetoothDevice})).intValue();
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            Log.e("HearingAidProfile", "fail to get getDeviceSideInternal\n" + e.toString() + "\n" + Log.getStackTraceString(new Throwable()));
            return -1;
        }
    }

    public int getDeviceMode(BluetoothDevice bluetoothDevice) {
        BluetoothHearingAid bluetoothHearingAid = this.mService;
        if (bluetoothHearingAid == null) {
            Log.w("HearingAidProfile", "Proxy not attached to HearingAidService");
            return -1;
        }
        try {
            Method declaredMethod = bluetoothHearingAid.getClass().getDeclaredMethod("getDeviceModeInternal", new Class[]{BluetoothDevice.class});
            declaredMethod.setAccessible(true);
            return ((Integer) declaredMethod.invoke(this.mService, new Object[]{bluetoothDevice})).intValue();
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            Log.e("HearingAidProfile", "fail to get getDeviceModeInternal\n" + e.toString() + "\n" + Log.getStackTraceString(new Throwable()));
            return -1;
        }
    }

    public void finalize() {
        Log.d("HearingAidProfile", "finalize()");
        if (this.mService != null) {
            try {
                BluetoothAdapter.getDefaultAdapter().closeProfileProxy(21, this.mService);
                this.mService = null;
            } catch (Throwable th) {
                Log.w("HearingAidProfile", "Error cleaning up Hearing Aid proxy", th);
            }
        }
    }
}
