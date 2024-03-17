package com.android.settingslib.bluetooth;

import android.bluetooth.BleBroadcastAudioScanAssistCallback;
import android.bluetooth.BleBroadcastAudioScanAssistManager;
import android.bluetooth.BleBroadcastSourceInfo;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanResult;
import android.util.Log;
import androidx.annotation.Keep;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public class VendorCachedBluetoothDevice extends CachedBluetoothDevice {
    public static Map<CachedBluetoothDevice, VendorCachedBluetoothDevice> mVcbdEntries = new IdentityHashMap();
    public Map<Integer, BleBroadcastSourceInfo> mBleBroadcastReceiverStates = new HashMap();
    public LocalBluetoothProfileManager mProfileManager = null;
    public BleBroadcastAudioScanAssistCallback mScanAssistCallback = new BleBroadcastAudioScanAssistCallback() {
        public void onBleBroadcastAudioSourceAdded(BluetoothDevice bluetoothDevice, byte b, int i) {
        }

        public void onBleBroadcastAudioSourceRemoved(BluetoothDevice bluetoothDevice, byte b, int i) {
        }

        public void onBleBroadcastAudioSourceUpdated(BluetoothDevice bluetoothDevice, byte b, int i) {
        }

        public void onBleBroadcastPinUpdated(BluetoothDevice bluetoothDevice, byte b, int i) {
        }

        public void onBleBroadcastSourceFound(ScanResult scanResult) {
            Log.d("VendorCachedBluetoothDevice", "onBleBroadcastSourceFound" + scanResult.getDevice());
            VendorCachedBluetoothDevice.this.setScanResult(scanResult);
        }
    };
    public BleBroadcastAudioScanAssistManager mScanAssistManager;
    public ScanResult mScanRes = null;

    public static VendorCachedBluetoothDevice getVendorCachedBluetoothDevice(CachedBluetoothDevice cachedBluetoothDevice, LocalBluetoothProfileManager localBluetoothProfileManager) {
        Map<CachedBluetoothDevice, VendorCachedBluetoothDevice> map = mVcbdEntries;
        VendorCachedBluetoothDevice vendorCachedBluetoothDevice = map != null ? map.get(cachedBluetoothDevice) : null;
        if (vendorCachedBluetoothDevice != null || localBluetoothProfileManager == null) {
            return vendorCachedBluetoothDevice;
        }
        VendorCachedBluetoothDevice vendorCachedBluetoothDevice2 = new VendorCachedBluetoothDevice(cachedBluetoothDevice, localBluetoothProfileManager);
        Log.d("VendorCachedBluetoothDevice", "getVendorCachedBluetoothDevice: created new Instance");
        mVcbdEntries.put(cachedBluetoothDevice, vendorCachedBluetoothDevice2);
        return vendorCachedBluetoothDevice2;
    }

    public VendorCachedBluetoothDevice(CachedBluetoothDevice cachedBluetoothDevice, LocalBluetoothProfileManager localBluetoothProfileManager) {
        super(cachedBluetoothDevice);
        this.mProfileManager = localBluetoothProfileManager;
        this.mBleBroadcastReceiverStates = new HashMap();
        InitializeSAManager();
    }

    public void onProfileStateChanged(LocalBluetoothProfile localBluetoothProfile, int i) {
        Log.d("VendorCachedBluetoothDevice", "onProfileStateChanged: profile " + localBluetoothProfile + ", device=" + this.mDevice + ", newProfileState " + i);
        if ((localBluetoothProfile instanceof BCProfile) && i == 0) {
            cleanUpSAMananger();
            super.lambda$refresh$0();
        }
    }

    public void InitializeSAManager() {
        this.mScanAssistManager = ((BCProfile) this.mProfileManager.getBCProfile()).getBSAManager(this.mDevice, this.mScanAssistCallback);
    }

    public void cleanUpSAMananger() {
        this.mScanAssistManager = null;
        Map<Integer, BleBroadcastSourceInfo> map = this.mBleBroadcastReceiverStates;
        if (map != null) {
            map.clear();
        }
    }

    public void updateBroadcastreceiverStates(BleBroadcastSourceInfo bleBroadcastSourceInfo, int i, int i2) {
        Log.d("VendorCachedBluetoothDevice", "updateBroadcastreceiverStates index: " + i);
        if (this.mBleBroadcastReceiverStates.get(Integer.valueOf(i)) != null) {
            Log.d("VendorCachedBluetoothDevice", "updateBroadcastreceiverStates: Replacing receiver State Information");
            this.mBleBroadcastReceiverStates.replace(Integer.valueOf(i), bleBroadcastSourceInfo);
        } else {
            Log.d("VendorCachedBluetoothDevice", "updateBroadcastreceiverStates: New entry for index: " + i);
            this.mBleBroadcastReceiverStates.put(Integer.valueOf(i), bleBroadcastSourceInfo);
        }
        super.lambda$refresh$0();
    }

    public void onBroadcastReceiverStateChanged(BleBroadcastSourceInfo bleBroadcastSourceInfo, int i, int i2) {
        updateBroadcastreceiverStates(bleBroadcastSourceInfo, i, i2);
    }

    public void setScanResult(ScanResult scanResult) {
        this.mScanRes = scanResult;
    }

    @Keep
    public boolean isBroadcastAudioSynced() {
        if (this.mScanAssistManager == null) {
            InitializeSAManager();
            if (this.mScanAssistManager == null) {
                Log.e("VendorCachedBluetoothDevice", "SA Manager cant be initialized");
                return false;
            }
        }
        List allBroadcastSourceInformation = this.mScanAssistManager.getAllBroadcastSourceInformation();
        if (allBroadcastSourceInformation == null) {
            Log.e("VendorCachedBluetoothDevice", "isBroadcastAudioSynced: no src Info");
            return false;
        }
        for (int i = 0; i < allBroadcastSourceInformation.size(); i++) {
            if (((BleBroadcastSourceInfo) allBroadcastSourceInformation.get(i)).getAudioSyncState() == 1) {
                return true;
            }
        }
        Log.d("VendorCachedBluetoothDevice", "isAudioSynced: false");
        return false;
    }
}
