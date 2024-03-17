package com.android.settingslib.bluetooth;

import android.bluetooth.BleBroadcastAudioScanAssistCallback;
import android.bluetooth.BleBroadcastAudioScanAssistManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothSyncHelper;
import android.content.Context;
import android.util.Log;
import androidx.annotation.Keep;
import com.android.settingslib.R$string;
import java.util.ArrayList;
import java.util.List;

@Keep
public class BCProfile implements LocalBluetoothProfile {
    public static final String NAME = "BCProfile";
    private static final int ORDINAL = 1;
    private static final String TAG = "BCProfile";
    private static boolean V = true;
    private Context mContext;
    private final CachedBluetoothDeviceManager mDeviceManager;
    /* access modifiers changed from: private */
    public boolean mIsProfileReady;
    /* access modifiers changed from: private */
    public final LocalBluetoothProfileManager mProfileManager;
    /* access modifiers changed from: private */
    public BluetoothSyncHelper mService;

    public boolean accessProfileEnabled() {
        return true;
    }

    public int getConnectionPolicy(BluetoothDevice bluetoothDevice) {
        return 100;
    }

    public int getDrawableResource(BluetoothClass bluetoothClass) {
        return 17302337;
    }

    public int getOrdinal() {
        return 1;
    }

    public int getProfileId() {
        return 35;
    }

    public String toString() {
        return "BCProfile";
    }

    public final class BassclientServiceListener implements BluetoothProfile.ServiceListener {
        public BassclientServiceListener() {
        }

        public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
            Log.d("BCProfile", "BassclientService connected");
            BCProfile.this.mService = (BluetoothSyncHelper) bluetoothProfile;
            BCProfile.this.mIsProfileReady = true;
            BCProfile.this.mProfileManager.callServiceConnectedListeners();
        }

        public void onServiceDisconnected(int i) {
            Log.d("BCProfile", "BassclientService disconnected");
            BCProfile.this.mIsProfileReady = false;
        }
    }

    public boolean isProfileReady() {
        return this.mIsProfileReady;
    }

    public boolean setEnabled(BluetoothDevice bluetoothDevice, boolean z) {
        BluetoothSyncHelper bluetoothSyncHelper = this.mService;
        if (bluetoothSyncHelper == null) {
            return false;
        }
        if (!z) {
            return bluetoothSyncHelper.setConnectionPolicy(bluetoothDevice, 0);
        }
        Log.d("BCProfile", "BCProfile: " + bluetoothDevice + ":" + z);
        if (this.mService.getConnectionPolicy(bluetoothDevice) < 100) {
            return this.mService.setConnectionPolicy(bluetoothDevice, 100);
        }
        return false;
    }

    public boolean isEnabled(BluetoothDevice bluetoothDevice) {
        BluetoothSyncHelper bluetoothSyncHelper = this.mService;
        if (bluetoothSyncHelper != null && bluetoothSyncHelper.getConnectionPolicy(bluetoothDevice) > 0) {
            return true;
        }
        return false;
    }

    public BCProfile(Context context, CachedBluetoothDeviceManager cachedBluetoothDeviceManager, LocalBluetoothProfileManager localBluetoothProfileManager) {
        this.mContext = context;
        this.mDeviceManager = cachedBluetoothDeviceManager;
        this.mProfileManager = localBluetoothProfileManager;
        BluetoothAdapter.getDefaultAdapter().getProfileProxy(context, new BassclientServiceListener(), 35);
    }

    public boolean isAutoConnectable() {
        if (this.mService == null) {
            return false;
        }
        Log.d("BCProfile", "isAutoConnectable return false");
        return false;
    }

    public List<BluetoothDevice> getConnectedDevices() {
        return getDevicesByStates(new int[]{2, 1, 3});
    }

    public List<BluetoothDevice> getConnectableDevices() {
        return getDevicesByStates(new int[]{0, 2, 1, 3});
    }

    private List<BluetoothDevice> getDevicesByStates(int[] iArr) {
        BluetoothSyncHelper bluetoothSyncHelper = this.mService;
        if (bluetoothSyncHelper == null) {
            return new ArrayList(0);
        }
        return bluetoothSyncHelper.getDevicesMatchingConnectionStates(iArr);
    }

    public boolean connect(BluetoothDevice bluetoothDevice) {
        Log.d("BCProfile", "BCProfile Connect to  device: " + bluetoothDevice);
        BluetoothSyncHelper bluetoothSyncHelper = this.mService;
        if (bluetoothSyncHelper == null) {
            return false;
        }
        return bluetoothSyncHelper.connect(bluetoothDevice);
    }

    public boolean disconnect(BluetoothDevice bluetoothDevice) {
        Log.d("BCProfile", "BCProfile disonnect to  device: " + bluetoothDevice);
        BluetoothSyncHelper bluetoothSyncHelper = this.mService;
        if (bluetoothSyncHelper == null) {
            return false;
        }
        if (bluetoothSyncHelper.getConnectionPolicy(bluetoothDevice) > 100) {
            this.mService.setConnectionPolicy(bluetoothDevice, 100);
        }
        return this.mService.disconnect(bluetoothDevice);
    }

    public int getConnectionStatus(BluetoothDevice bluetoothDevice) {
        BluetoothSyncHelper bluetoothSyncHelper = this.mService;
        if (bluetoothSyncHelper == null) {
            return 0;
        }
        return bluetoothSyncHelper.getConnectionState(bluetoothDevice);
    }

    public int getPreferred(BluetoothDevice bluetoothDevice) {
        BluetoothSyncHelper bluetoothSyncHelper = this.mService;
        if (bluetoothSyncHelper == null) {
            return -1;
        }
        return bluetoothSyncHelper.getConnectionPolicy(bluetoothDevice);
    }

    public void setPreferred(BluetoothDevice bluetoothDevice, boolean z) {
        BluetoothSyncHelper bluetoothSyncHelper = this.mService;
        if (bluetoothSyncHelper != null) {
            if (!z) {
                bluetoothSyncHelper.setConnectionPolicy(bluetoothDevice, -1);
            } else if (bluetoothSyncHelper.getConnectionPolicy(bluetoothDevice) != 100) {
                this.mService.setConnectionPolicy(bluetoothDevice, 100);
            }
        }
    }

    public int getNameResource(BluetoothDevice bluetoothDevice) {
        return R$string.bluetooth_profile_bc;
    }

    public BleBroadcastAudioScanAssistManager getBSAManager(BluetoothDevice bluetoothDevice, BleBroadcastAudioScanAssistCallback bleBroadcastAudioScanAssistCallback) {
        BluetoothSyncHelper bluetoothSyncHelper = this.mService;
        if (bluetoothSyncHelper != null) {
            return bluetoothSyncHelper.getBleBroadcastAudioScanAssistManager(bluetoothDevice, bleBroadcastAudioScanAssistCallback);
        }
        Log.d("BCProfile", "getBroadcastAudioScanAssistManager: service is null");
        return null;
    }

    public int getSummaryResourceForDevice(BluetoothDevice bluetoothDevice) {
        int connectionStatus = getConnectionStatus(bluetoothDevice);
        if (connectionStatus == 0) {
            return R$string.bluetooth_bc_profile_summary_use_for;
        }
        if (connectionStatus != 2) {
            return BluetoothUtils.getConnectionStateSummary(connectionStatus);
        }
        return R$string.bluetooth_bc_profile_summary_connected;
    }

    public void finalize() {
        Log.d("BCProfile", "finalize()");
        if (this.mService != null) {
            try {
                BluetoothAdapter.getDefaultAdapter().closeProfileProxy(35, this.mService);
                this.mService = null;
            } catch (Throwable th) {
                Log.w("BCProfile", "Error cleaning up BAss client proxy", th);
            }
        }
    }

    public static boolean isBCSupported() {
        Log.d("BCProfile", "BassClientProfile: isBCSupported returns " + true);
        return true;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v1, resolved type: boolean} */
    /* JADX WARNING: type inference failed for: r3v0 */
    /* JADX WARNING: type inference failed for: r3v2, types: [int] */
    /* JADX WARNING: type inference failed for: r3v3 */
    /* JADX WARNING: type inference failed for: r3v5 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean isBASeeker(android.bluetooth.BluetoothDevice r6) {
        /*
            if (r6 == 0) goto L_0x0007
            android.os.ParcelUuid[] r6 = r6.getUuids()
            goto L_0x0008
        L_0x0007:
            r6 = 0
        L_0x0008:
            java.lang.String r0 = "0000184F-0000-1000-8000-00805F9B34FB"
            android.os.ParcelUuid r0 = android.os.ParcelUuid.fromString(r0)
            boolean r1 = isBCSupported()
            java.lang.String r2 = "BCProfile"
            r3 = 0
            if (r1 == 0) goto L_0x002f
            if (r6 == 0) goto L_0x002f
            int r1 = r6.length
            r4 = r3
        L_0x001b:
            if (r3 >= r1) goto L_0x002e
            r5 = r6[r3]
            boolean r5 = r5.equals(r0)
            if (r5 == 0) goto L_0x002b
            java.lang.String r4 = "SD uuid present"
            android.util.Log.d(r2, r4)
            r4 = 1
        L_0x002b:
            int r3 = r3 + 1
            goto L_0x001b
        L_0x002e:
            r3 = r4
        L_0x002f:
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r0 = "isBASeeker returns:"
            r6.append(r0)
            r6.append(r3)
            java.lang.String r6 = r6.toString()
            android.util.Log.d(r2, r6)
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settingslib.bluetooth.BCProfile.isBASeeker(android.bluetooth.BluetoothDevice):boolean");
    }
}
