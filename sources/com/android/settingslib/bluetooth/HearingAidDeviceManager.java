package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import java.util.HashSet;
import java.util.List;

public class HearingAidDeviceManager {
    public final LocalBluetoothManager mBtManager;
    public final List<CachedBluetoothDevice> mCachedDevices;

    public final boolean isValidHiSyncId(long j) {
        return j != 0;
    }

    public HearingAidDeviceManager(LocalBluetoothManager localBluetoothManager, List<CachedBluetoothDevice> list) {
        this.mBtManager = localBluetoothManager;
        this.mCachedDevices = list;
    }

    public void initHearingAidDeviceIfNeeded(CachedBluetoothDevice cachedBluetoothDevice) {
        long hiSyncId = getHiSyncId(cachedBluetoothDevice.getDevice());
        if (isValidHiSyncId(hiSyncId)) {
            cachedBluetoothDevice.setHiSyncId(hiSyncId);
        }
    }

    public final long getHiSyncId(BluetoothDevice bluetoothDevice) {
        HearingAidProfile hearingAidProfile = this.mBtManager.getProfileManager().getHearingAidProfile();
        if (hearingAidProfile != null) {
            return hearingAidProfile.getHiSyncId(bluetoothDevice);
        }
        return 0;
    }

    public boolean setSubDeviceIfNeeded(CachedBluetoothDevice cachedBluetoothDevice) {
        CachedBluetoothDevice cachedDevice;
        long hiSyncId = cachedBluetoothDevice.getHiSyncId();
        if (!isValidHiSyncId(hiSyncId) || (cachedDevice = getCachedDevice(hiSyncId)) == null) {
            return false;
        }
        cachedDevice.setSubDevice(cachedBluetoothDevice);
        return true;
    }

    public final CachedBluetoothDevice getCachedDevice(long j) {
        for (int size = this.mCachedDevices.size() - 1; size >= 0; size--) {
            CachedBluetoothDevice cachedBluetoothDevice = this.mCachedDevices.get(size);
            if (cachedBluetoothDevice.getHiSyncId() == j) {
                return cachedBluetoothDevice;
            }
        }
        return null;
    }

    public void updateHearingAidsDevices() {
        HashSet<Long> hashSet = new HashSet<>();
        for (CachedBluetoothDevice next : this.mCachedDevices) {
            if (!isValidHiSyncId(next.getHiSyncId())) {
                long hiSyncId = getHiSyncId(next.getDevice());
                if (isValidHiSyncId(hiSyncId)) {
                    next.setHiSyncId(hiSyncId);
                    hashSet.add(Long.valueOf(hiSyncId));
                }
            }
        }
        for (Long longValue : hashSet) {
            onHiSyncIdChanged(longValue.longValue());
        }
    }

    @VisibleForTesting
    public void onHiSyncIdChanged(long j) {
        CachedBluetoothDevice cachedBluetoothDevice;
        int size = this.mCachedDevices.size() - 1;
        int i = -1;
        while (size >= 0) {
            CachedBluetoothDevice cachedBluetoothDevice2 = this.mCachedDevices.get(size);
            if (cachedBluetoothDevice2.getHiSyncId() == j) {
                if (i == -1) {
                    i = size;
                } else {
                    if (cachedBluetoothDevice2.isConnected()) {
                        cachedBluetoothDevice = this.mCachedDevices.get(i);
                        size = i;
                    } else {
                        CachedBluetoothDevice cachedBluetoothDevice3 = cachedBluetoothDevice2;
                        cachedBluetoothDevice2 = this.mCachedDevices.get(i);
                        cachedBluetoothDevice = cachedBluetoothDevice3;
                    }
                    cachedBluetoothDevice2.setSubDevice(cachedBluetoothDevice);
                    this.mCachedDevices.remove(size);
                    log("onHiSyncIdChanged: removed from UI device =" + cachedBluetoothDevice + ", with hiSyncId=" + j);
                    this.mBtManager.getEventManager().dispatchDeviceRemoved(cachedBluetoothDevice);
                    return;
                }
            }
            size--;
        }
    }

    public boolean onProfileConnectionStateChangedIfProcessed(CachedBluetoothDevice cachedBluetoothDevice, int i) {
        if (i == 0) {
            CachedBluetoothDevice findMainDevice = findMainDevice(cachedBluetoothDevice);
            if (cachedBluetoothDevice.getUnpairing()) {
                return true;
            }
            if (findMainDevice != null) {
                findMainDevice.refresh();
                return true;
            }
            CachedBluetoothDevice subDevice = cachedBluetoothDevice.getSubDevice();
            if (subDevice == null || !subDevice.isConnected()) {
                return false;
            }
            this.mBtManager.getEventManager().dispatchDeviceRemoved(cachedBluetoothDevice);
            cachedBluetoothDevice.switchSubDeviceContent();
            cachedBluetoothDevice.refresh();
            this.mBtManager.getEventManager().dispatchDeviceAdded(cachedBluetoothDevice);
            return true;
        } else if (i != 2) {
            return false;
        } else {
            onHiSyncIdChanged(cachedBluetoothDevice.getHiSyncId());
            CachedBluetoothDevice findMainDevice2 = findMainDevice(cachedBluetoothDevice);
            if (findMainDevice2 == null) {
                return false;
            }
            if (findMainDevice2.isConnected()) {
                findMainDevice2.refresh();
                return true;
            }
            this.mBtManager.getEventManager().dispatchDeviceRemoved(findMainDevice2);
            findMainDevice2.switchSubDeviceContent();
            findMainDevice2.refresh();
            this.mBtManager.getEventManager().dispatchDeviceAdded(findMainDevice2);
            return true;
        }
    }

    public CachedBluetoothDevice findMainDevice(CachedBluetoothDevice cachedBluetoothDevice) {
        CachedBluetoothDevice subDevice;
        for (CachedBluetoothDevice next : this.mCachedDevices) {
            if (isValidHiSyncId(next.getHiSyncId()) && (subDevice = next.getSubDevice()) != null && subDevice.equals(cachedBluetoothDevice)) {
                return next;
            }
        }
        return null;
    }

    public final void log(String str) {
        Log.d("HearingAidDeviceManager", str);
    }
}
