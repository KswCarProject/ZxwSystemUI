package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothBroadcast;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;
import androidx.annotation.Keep;
import com.android.settingslib.R$string;

@Keep
public final class BroadcastProfile implements LocalBluetoothProfile {
    public static final String NAME = "Broadcast";
    private static final int ORDINAL = 0;
    private static final String TAG = "BroadcastProfile";
    /* access modifiers changed from: private */
    public static boolean V = true;
    /* access modifiers changed from: private */
    public boolean mIsProfileReady = false;
    /* access modifiers changed from: private */
    public BluetoothBroadcast mService;

    public boolean connect(BluetoothDevice bluetoothDevice) {
        return false;
    }

    public boolean disconnect(BluetoothDevice bluetoothDevice) {
        return false;
    }

    public int getConnectionPolicy(BluetoothDevice bluetoothDevice) {
        return 0;
    }

    public int getConnectionStatus(BluetoothDevice bluetoothDevice) {
        return 0;
    }

    public int getDrawableResource(BluetoothClass bluetoothClass) {
        return 0;
    }

    public int getOrdinal() {
        return 0;
    }

    public int getPreferred(BluetoothDevice bluetoothDevice) {
        return 0;
    }

    public int getSummaryResourceForDevice(BluetoothDevice bluetoothDevice) {
        return 0;
    }

    public boolean isAutoConnectable() {
        return false;
    }

    public boolean isEnabled(BluetoothDevice bluetoothDevice) {
        return false;
    }

    public boolean isPreferred(BluetoothDevice bluetoothDevice) {
        return false;
    }

    public boolean setEnabled(BluetoothDevice bluetoothDevice, boolean z) {
        return false;
    }

    public void setPreferred(BluetoothDevice bluetoothDevice, boolean z) {
    }

    public String toString() {
        return NAME;
    }

    public final class BroadcastListener implements BluetoothProfile.ServiceListener {
        public BroadcastListener() {
        }

        public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
            if (i == 33) {
                if (BroadcastProfile.V) {
                    Log.d(BroadcastProfile.TAG, "Bluetooth Broadcast service connected");
                }
                BroadcastProfile.this.mService = (BluetoothBroadcast) bluetoothProfile;
                BroadcastProfile.this.mIsProfileReady = true;
            }
        }

        public void onServiceDisconnected(int i) {
            if (i == 33) {
                if (BroadcastProfile.V) {
                    Log.d(BroadcastProfile.TAG, "Bluetooth Broadcast service disconnected");
                }
                BroadcastProfile.this.mIsProfileReady = false;
            }
        }
    }

    public boolean isProfileReady() {
        Log.d(TAG, "isProfileReady = " + this.mIsProfileReady);
        return this.mIsProfileReady;
    }

    public int getProfileId() {
        Log.d(TAG, "getProfileId");
        return 33;
    }

    public BroadcastProfile(Context context) {
        Log.d(TAG, "BroadcastProfile constructor");
        BluetoothAdapter.getDefaultAdapter().getProfileProxy(context, new BroadcastListener(), 33);
    }

    public boolean accessProfileEnabled() {
        Log.d(TAG, "accessProfileEnabled");
        return false;
    }

    public int getNameResource(BluetoothDevice bluetoothDevice) {
        return R$string.bluetooth_profile_broadcast;
    }

    public boolean setEncryption(boolean z, int i, boolean z2) {
        Log.d(TAG, "setEncryption");
        return this.mService.SetEncryption(z, i, z2);
    }

    public byte[] getEncryptionKey() {
        Log.d(TAG, "getEncryptionKey");
        return this.mService.GetEncryptionKey();
    }

    public int getBroadcastStatus() {
        Log.d(TAG, "getBroadcastStatus");
        return this.mService.GetBroadcastStatus();
    }

    public boolean setBroadcastMode(boolean z) {
        Log.d(TAG, "setBroadcastMode");
        return this.mService.SetBroadcastMode(z);
    }

    public void finalize() {
        if (V) {
            Log.d(TAG, "finalize()");
        }
        if (this.mService != null) {
            try {
                BluetoothAdapter.getDefaultAdapter().closeProfileProxy(33, this.mService);
                this.mService = null;
            } catch (Throwable th) {
                Log.w(TAG, "Error cleaning up Broadcast proxy", th);
            }
        }
    }
}
