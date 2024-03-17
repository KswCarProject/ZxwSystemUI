package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothUuid;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelUuid;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.EventLog;
import android.util.Log;
import android.util.LruCache;
import com.android.internal.util.ArrayUtils;
import com.android.settingslib.utils.ThreadUtils;
import com.android.settingslib.wifi.WifiTracker;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

public class CachedBluetoothDevice implements Comparable<CachedBluetoothDevice> {
    public final int BREDR = 100;
    public final int GROUPID_END = 15;
    public final int GROUPID_START = 0;
    public final int UNKNOWN = -1;
    public final Collection<Callback> mCallbacks = new CopyOnWriteArrayList();
    public long mConnectAttempted;
    public final Context mContext;
    public BluetoothDevice mDevice;
    public int mDeviceMode;
    public int mDeviceSide;
    public LruCache<String, BitmapDrawable> mDrawableCache;
    public int mGroupId;
    public final Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                CachedBluetoothDevice.this.mIsHeadsetProfileConnectedFail = true;
            } else if (i == 2) {
                CachedBluetoothDevice.this.mIsA2dpProfileConnectedFail = true;
            } else if (i == 21) {
                CachedBluetoothDevice.this.mIsHearingAidProfileConnectedFail = true;
            } else if (i != 22) {
                Log.w("CachedBluetoothDevice", "handleMessage(): unknown message : " + message.what);
            } else {
                CachedBluetoothDevice.this.mIsLeAudioProfileConnectedFail = true;
            }
            Log.w("CachedBluetoothDevice", "Connect to profile : " + message.what + " timeout, show error message !");
            CachedBluetoothDevice.this.refresh();
        }
    };
    public long mHiSyncId;
    public boolean mIsA2dpProfileConnectedFail = false;
    public boolean mIsActiveDeviceA2dp = false;
    public boolean mIsActiveDeviceHeadset = false;
    public boolean mIsActiveDeviceHearingAid = false;
    public boolean mIsActiveDeviceLeAudio = false;
    public boolean mIsCoordinatedSetMember = false;
    public boolean mIsGroupDevice = false;
    public boolean mIsHeadsetProfileConnectedFail = false;
    public boolean mIsHearingAidProfileConnectedFail = false;
    public boolean mIsIgnore = false;
    public boolean mIsLeAudioProfileConnectedFail = false;
    public boolean mJustDiscovered;
    public final BluetoothAdapter mLocalAdapter;
    public boolean mLocalNapRoleConnected;
    public Set<CachedBluetoothDevice> mMemberDevices = new HashSet();
    public final Object mProfileLock = new Object();
    public final LocalBluetoothProfileManager mProfileManager;
    public final Collection<LocalBluetoothProfile> mProfiles = new CopyOnWriteArrayList();
    public int mQGroupId;
    public final Collection<LocalBluetoothProfile> mRemovedProfiles = new CopyOnWriteArrayList();
    public short mRssi;
    public CachedBluetoothDevice mSubDevice;
    public int mTwspBatteryLevel;
    public int mTwspBatteryState;
    public int mType = -1;
    public boolean mUnpairing;

    public interface Callback {
        void onDeviceAttributesChanged();
    }

    public CachedBluetoothDevice(Context context, LocalBluetoothProfileManager localBluetoothProfileManager, BluetoothDevice bluetoothDevice) {
        this.mContext = context;
        this.mLocalAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mProfileManager = localBluetoothProfileManager;
        this.mDevice = bluetoothDevice;
        fillData();
        this.mHiSyncId = 0;
        this.mGroupId = -1;
        this.mQGroupId = -1;
        initDrawableCache();
        this.mTwspBatteryState = -1;
        this.mTwspBatteryLevel = -1;
        this.mUnpairing = false;
    }

    public CachedBluetoothDevice(CachedBluetoothDevice cachedBluetoothDevice) {
        this.mContext = cachedBluetoothDevice.mContext;
        this.mLocalAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mProfileManager = cachedBluetoothDevice.mProfileManager;
        this.mDevice = cachedBluetoothDevice.mDevice;
        fillData();
        this.mHiSyncId = 0;
        initDrawableCache();
        this.mTwspBatteryState = -1;
        this.mTwspBatteryLevel = -1;
        this.mUnpairing = false;
    }

    public final void initDrawableCache() {
        this.mDrawableCache = new LruCache<String, BitmapDrawable>(((int) (Runtime.getRuntime().maxMemory() / 1024)) / 8) {
            public int sizeOf(String str, BitmapDrawable bitmapDrawable) {
                return bitmapDrawable.getBitmap().getByteCount() / 1024;
            }
        };
    }

    public final BluetoothDevice getTwsPeerDevice() {
        if (this.mDevice.isTwsPlusDevice()) {
            return BluetoothAdapter.getDefaultAdapter().getRemoteDevice(this.mDevice.getTwsPlusPeerAddress());
        }
        return null;
    }

    public final String describe(LocalBluetoothProfile localBluetoothProfile) {
        StringBuilder sb = new StringBuilder();
        sb.append("Address:");
        sb.append(this.mDevice);
        if (localBluetoothProfile != null) {
            sb.append(" Profile:");
            sb.append(localBluetoothProfile);
        }
        return sb.toString();
    }

    public void onProfileStateChanged(LocalBluetoothProfile localBluetoothProfile, int i) {
        Log.d("CachedBluetoothDevice", "onProfileStateChanged: profile " + localBluetoothProfile + ", device " + this.mDevice.getAnonymizedAddress() + ", newProfileState " + i);
        if (this.mLocalAdapter.getState() == 13) {
            Log.d("CachedBluetoothDevice", " BT Turninig Off...Profile conn state change ignored...");
            return;
        }
        synchronized (this.mProfileLock) {
            if ((localBluetoothProfile instanceof A2dpProfile) || (localBluetoothProfile instanceof HeadsetProfile) || (localBluetoothProfile instanceof HearingAidProfile)) {
                setProfileConnectedStatus(localBluetoothProfile.getProfileId(), false);
                if (i != 0) {
                    if (i == 1) {
                        this.mHandler.sendEmptyMessageDelayed(localBluetoothProfile.getProfileId(), 60000);
                    } else if (i == 2) {
                        this.mHandler.removeMessages(localBluetoothProfile.getProfileId());
                    } else if (i != 3) {
                        Log.w("CachedBluetoothDevice", "onProfileStateChanged(): unknown profile state : " + i);
                    } else if (this.mHandler.hasMessages(localBluetoothProfile.getProfileId())) {
                        this.mHandler.removeMessages(localBluetoothProfile.getProfileId());
                    }
                } else if (this.mHandler.hasMessages(localBluetoothProfile.getProfileId())) {
                    this.mHandler.removeMessages(localBluetoothProfile.getProfileId());
                    setProfileConnectedStatus(localBluetoothProfile.getProfileId(), true);
                }
            }
            if (i == 2) {
                if (localBluetoothProfile instanceof MapProfile) {
                    localBluetoothProfile.setEnabled(this.mDevice, true);
                }
                if (!this.mProfiles.contains(localBluetoothProfile)) {
                    this.mRemovedProfiles.remove(localBluetoothProfile);
                    this.mProfiles.add(localBluetoothProfile);
                    if ((localBluetoothProfile instanceof PanProfile) && ((PanProfile) localBluetoothProfile).isLocalRoleNap(this.mDevice)) {
                        this.mLocalNapRoleConnected = true;
                    }
                }
            } else if ((localBluetoothProfile instanceof MapProfile) && i == 0) {
                localBluetoothProfile.setEnabled(this.mDevice, false);
            } else if (this.mLocalNapRoleConnected && (localBluetoothProfile instanceof PanProfile) && ((PanProfile) localBluetoothProfile).isLocalRoleNap(this.mDevice) && i == 0) {
                Log.d("CachedBluetoothDevice", "Removing PanProfile from device after NAP disconnect");
                this.mProfiles.remove(localBluetoothProfile);
                this.mRemovedProfiles.add(localBluetoothProfile);
                this.mLocalNapRoleConnected = false;
            } else if ((localBluetoothProfile instanceof HeadsetProfile) && i == 0) {
                this.mTwspBatteryState = -1;
                this.mTwspBatteryLevel = -1;
            }
        }
        fetchActiveDevices();
    }

    public void setProfileConnectedStatus(int i, boolean z) {
        if (i == 1) {
            this.mIsHeadsetProfileConnectedFail = z;
        } else if (i == 2) {
            this.mIsA2dpProfileConnectedFail = z;
        } else if (i == 21) {
            this.mIsHearingAidProfileConnectedFail = z;
        } else if (i != 22) {
            Log.w("CachedBluetoothDevice", "setProfileConnectedStatus(): unknown profile id : " + i);
        } else {
            this.mIsLeAudioProfileConnectedFail = z;
        }
    }

    @Deprecated
    public void connect(boolean z) {
        connect();
    }

    public void connect() {
        if (ensurePaired()) {
            this.mConnectAttempted = SystemClock.elapsedRealtime();
            Log.d("CachedBluetoothDevice", "connect: mConnectAttempted = " + this.mConnectAttempted);
            connectDevice();
        }
    }

    public void setDeviceSide(int i) {
        this.mDeviceSide = i;
    }

    public void setDeviceMode(int i) {
        this.mDeviceMode = i;
    }

    public long getHiSyncId() {
        return this.mHiSyncId;
    }

    public void setHiSyncId(long j) {
        this.mHiSyncId = j;
    }

    public boolean isHearingAidDevice() {
        return this.mHiSyncId != 0;
    }

    public void setIsCoordinatedSetMember(boolean z) {
        this.mIsCoordinatedSetMember = z;
    }

    public int getGroupId() {
        return this.mGroupId;
    }

    public int getQGroupId() {
        return this.mQGroupId;
    }

    public void setGroupId(int i) {
        this.mGroupId = i;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:0x007e, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void connectDevice() {
        /*
            r5 = this;
            java.lang.Object r0 = r5.mProfileLock
            monitor-enter(r0)
            java.util.Collection<com.android.settingslib.bluetooth.LocalBluetoothProfile> r1 = r5.mProfiles     // Catch:{ all -> 0x007f }
            boolean r1 = r1.isEmpty()     // Catch:{ all -> 0x007f }
            if (r1 == 0) goto L_0x0025
            java.lang.String r1 = "CachedBluetoothDevice"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x007f }
            r2.<init>()     // Catch:{ all -> 0x007f }
            java.lang.String r3 = "No profiles. Maybe we will connect later for device "
            r2.append(r3)     // Catch:{ all -> 0x007f }
            android.bluetooth.BluetoothDevice r5 = r5.mDevice     // Catch:{ all -> 0x007f }
            r2.append(r5)     // Catch:{ all -> 0x007f }
            java.lang.String r5 = r2.toString()     // Catch:{ all -> 0x007f }
            android.util.Log.d(r1, r5)     // Catch:{ all -> 0x007f }
            monitor-exit(r0)     // Catch:{ all -> 0x007f }
            return
        L_0x0025:
            android.bluetooth.BluetoothDevice r1 = r5.mDevice     // Catch:{ all -> 0x007f }
            boolean r1 = r1.isBondingInitiatedLocally()     // Catch:{ all -> 0x007f }
            if (r1 == 0) goto L_0x003a
            java.lang.String r1 = "CachedBluetoothDevice"
            java.lang.String r2 = "reset BondingInitiatedLocally flag"
            android.util.Log.w(r1, r2)     // Catch:{ all -> 0x007f }
            android.bluetooth.BluetoothDevice r1 = r5.mDevice     // Catch:{ all -> 0x007f }
            r2 = 0
            r1.setBondingInitiatedLocally(r2)     // Catch:{ all -> 0x007f }
        L_0x003a:
            android.bluetooth.BluetoothDevice r1 = r5.mDevice     // Catch:{ all -> 0x007f }
            r1.connect()     // Catch:{ all -> 0x007f }
            int r1 = r5.getGroupId()     // Catch:{ all -> 0x007f }
            r2 = -1
            if (r1 == r2) goto L_0x007d
            java.util.Set r5 = r5.getMemberDevice()     // Catch:{ all -> 0x007f }
            java.util.Iterator r5 = r5.iterator()     // Catch:{ all -> 0x007f }
        L_0x004e:
            boolean r1 = r5.hasNext()     // Catch:{ all -> 0x007f }
            if (r1 == 0) goto L_0x007d
            java.lang.Object r1 = r5.next()     // Catch:{ all -> 0x007f }
            com.android.settingslib.bluetooth.CachedBluetoothDevice r1 = (com.android.settingslib.bluetooth.CachedBluetoothDevice) r1     // Catch:{ all -> 0x007f }
            java.lang.String r2 = "CachedBluetoothDevice"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x007f }
            r3.<init>()     // Catch:{ all -> 0x007f }
            java.lang.String r4 = "connect the member("
            r3.append(r4)     // Catch:{ all -> 0x007f }
            java.lang.String r4 = r1.getAddress()     // Catch:{ all -> 0x007f }
            r3.append(r4)     // Catch:{ all -> 0x007f }
            java.lang.String r4 = ")"
            r3.append(r4)     // Catch:{ all -> 0x007f }
            java.lang.String r3 = r3.toString()     // Catch:{ all -> 0x007f }
            android.util.Log.d(r2, r3)     // Catch:{ all -> 0x007f }
            r1.connect()     // Catch:{ all -> 0x007f }
            goto L_0x004e
        L_0x007d:
            monitor-exit(r0)     // Catch:{ all -> 0x007f }
            return
        L_0x007f:
            r5 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x007f }
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settingslib.bluetooth.CachedBluetoothDevice.connectDevice():void");
    }

    public final boolean ensurePaired() {
        if (getBondState() != 10) {
            return true;
        }
        startPairing();
        return false;
    }

    public boolean startPairing() {
        if (this.mLocalAdapter.isDiscovering()) {
            this.mLocalAdapter.cancelDiscovery();
        }
        return this.mDevice.createBond();
    }

    public void unpair() {
        BluetoothDevice twsPeerDevice;
        int bondState = getBondState();
        if (bondState == 11) {
            this.mDevice.cancelBondProcess();
        }
        if (bondState != 10) {
            BluetoothDevice bluetoothDevice = this.mDevice;
            if (bluetoothDevice.isTwsPlusDevice() && (twsPeerDevice = getTwsPeerDevice()) != null && twsPeerDevice.removeBond()) {
                Log.d("CachedBluetoothDevice", "Command sent successfully:REMOVE_BOND " + twsPeerDevice.getName());
            }
            if (bluetoothDevice != null) {
                this.mUnpairing = true;
                if (bluetoothDevice.removeBond()) {
                    releaseLruCache();
                    Log.d("CachedBluetoothDevice", "Command sent successfully:REMOVE_BOND " + describe((LocalBluetoothProfile) null));
                }
            }
        }
    }

    public int getProfileConnectionState(LocalBluetoothProfile localBluetoothProfile) {
        if (localBluetoothProfile != null) {
            return localBluetoothProfile.getConnectionStatus(this.mDevice);
        }
        return 0;
    }

    public final void fillData() {
        updateProfiles();
        fetchActiveDevices();
        migratePhonebookPermissionChoice();
        migrateMessagePermissionChoice();
        lambda$refresh$0();
    }

    public BluetoothDevice getDevice() {
        return this.mDevice;
    }

    public String getAddress() {
        return this.mDevice.getAddress();
    }

    public String getName() {
        String alias = this.mDevice.getAlias();
        return TextUtils.isEmpty(alias) ? getAddress() : alias;
    }

    public void setName(String str) {
        if (str != null && !TextUtils.equals(str, getName())) {
            this.mDevice.setAlias(str);
            lambda$refresh$0();
        }
    }

    public void refreshName() {
        Log.d("CachedBluetoothDevice", "Device name: " + getName());
        lambda$refresh$0();
    }

    public int getBatteryLevel() {
        return this.mDevice.getBatteryLevel();
    }

    public void refresh() {
        ThreadUtils.postOnBackgroundThread(new CachedBluetoothDevice$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$refresh$1() {
        Uri uriMetaData;
        if (BluetoothUtils.isAdvancedDetailsHeader(this.mDevice) && (uriMetaData = BluetoothUtils.getUriMetaData(getDevice(), 5)) != null && this.mDrawableCache.get(uriMetaData.toString()) == null) {
            this.mDrawableCache.put(uriMetaData.toString(), (BitmapDrawable) BluetoothUtils.getBtDrawableWithDescription(this.mContext, this).first);
        }
        ThreadUtils.postOnMainThread(new CachedBluetoothDevice$$ExternalSyntheticLambda1(this));
    }

    public void setJustDiscovered(boolean z) {
        if (this.mJustDiscovered != z) {
            this.mJustDiscovered = z;
            lambda$refresh$0();
        }
    }

    public int getBondState() {
        return this.mDevice.getBondState();
    }

    /* JADX WARNING: Removed duplicated region for block: B:27:0x0054  */
    /* JADX WARNING: Removed duplicated region for block: B:29:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onActiveDeviceChanged(boolean r4, int r5) {
        /*
            r3 = this;
            r0 = 1
            r1 = 0
            if (r5 == r0) goto L_0x0049
            r2 = 2
            if (r5 == r2) goto L_0x0040
            r2 = 21
            if (r5 == r2) goto L_0x0037
            r2 = 22
            if (r5 == r2) goto L_0x002e
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r2 = "onActiveDeviceChanged: unknown profile "
            r0.append(r2)
            r0.append(r5)
            java.lang.String r5 = " isActive "
            r0.append(r5)
            r0.append(r4)
            java.lang.String r4 = r0.toString()
            java.lang.String r5 = "CachedBluetoothDevice"
            android.util.Log.w(r5, r4)
            goto L_0x0052
        L_0x002e:
            boolean r5 = r3.mIsActiveDeviceLeAudio
            if (r5 == r4) goto L_0x0033
            goto L_0x0034
        L_0x0033:
            r0 = r1
        L_0x0034:
            r3.mIsActiveDeviceLeAudio = r4
            goto L_0x0051
        L_0x0037:
            boolean r5 = r3.mIsActiveDeviceHearingAid
            if (r5 == r4) goto L_0x003c
            goto L_0x003d
        L_0x003c:
            r0 = r1
        L_0x003d:
            r3.mIsActiveDeviceHearingAid = r4
            goto L_0x0051
        L_0x0040:
            boolean r5 = r3.mIsActiveDeviceA2dp
            if (r5 == r4) goto L_0x0045
            goto L_0x0046
        L_0x0045:
            r0 = r1
        L_0x0046:
            r3.mIsActiveDeviceA2dp = r4
            goto L_0x0051
        L_0x0049:
            boolean r5 = r3.mIsActiveDeviceHeadset
            if (r5 == r4) goto L_0x004e
            goto L_0x004f
        L_0x004e:
            r0 = r1
        L_0x004f:
            r3.mIsActiveDeviceHeadset = r4
        L_0x0051:
            r1 = r0
        L_0x0052:
            if (r1 == 0) goto L_0x0057
            r3.lambda$refresh$0()
        L_0x0057:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settingslib.bluetooth.CachedBluetoothDevice.onActiveDeviceChanged(boolean, int):void");
    }

    public void onAudioModeChanged() {
        lambda$refresh$0();
    }

    public boolean isActiveDevice(int i) {
        if (i == 1) {
            return this.mIsActiveDeviceHeadset;
        }
        if (i == 2) {
            return this.mIsActiveDeviceA2dp;
        }
        if (i == 21) {
            return this.mIsActiveDeviceHearingAid;
        }
        if (i == 22) {
            return this.mIsActiveDeviceLeAudio;
        }
        Log.w("CachedBluetoothDevice", "getActiveDevice: unknown profile " + i);
        return false;
    }

    public void setRssi(short s) {
        if (this.mRssi != s) {
            this.mRssi = s;
            lambda$refresh$0();
        }
    }

    public boolean isConnected() {
        synchronized (this.mProfileLock) {
            for (LocalBluetoothProfile profileConnectionState : this.mProfiles) {
                if (getProfileConnectionState(profileConnectionState) == 2) {
                    return true;
                }
            }
            return false;
        }
    }

    public boolean isConnectedProfile(LocalBluetoothProfile localBluetoothProfile) {
        return getProfileConnectionState(localBluetoothProfile) == 2;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0020, code lost:
        return true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x002c, code lost:
        return r3;
     */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0021 A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:6:0x0010  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isBusy() {
        /*
            r5 = this;
            java.lang.Object r0 = r5.mProfileLock
            monitor-enter(r0)
            java.util.Collection<com.android.settingslib.bluetooth.LocalBluetoothProfile> r1 = r5.mProfiles     // Catch:{ all -> 0x002d }
            java.util.Iterator r1 = r1.iterator()     // Catch:{ all -> 0x002d }
        L_0x0009:
            boolean r2 = r1.hasNext()     // Catch:{ all -> 0x002d }
            r3 = 1
            if (r2 == 0) goto L_0x0021
            java.lang.Object r2 = r1.next()     // Catch:{ all -> 0x002d }
            com.android.settingslib.bluetooth.LocalBluetoothProfile r2 = (com.android.settingslib.bluetooth.LocalBluetoothProfile) r2     // Catch:{ all -> 0x002d }
            int r2 = r5.getProfileConnectionState(r2)     // Catch:{ all -> 0x002d }
            if (r2 == r3) goto L_0x001f
            r4 = 3
            if (r2 != r4) goto L_0x0009
        L_0x001f:
            monitor-exit(r0)     // Catch:{ all -> 0x002d }
            return r3
        L_0x0021:
            int r5 = r5.getBondState()     // Catch:{ all -> 0x002d }
            r1 = 11
            if (r5 != r1) goto L_0x002a
            goto L_0x002b
        L_0x002a:
            r3 = 0
        L_0x002b:
            monitor-exit(r0)     // Catch:{ all -> 0x002d }
            return r3
        L_0x002d:
            r5 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x002d }
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settingslib.bluetooth.CachedBluetoothDevice.isBusy():boolean");
    }

    public final boolean updateProfiles() {
        ParcelUuid[] uuids = this.mDevice.getUuids();
        if (uuids == null) {
            return false;
        }
        List uuidsList = this.mLocalAdapter.getUuidsList();
        ParcelUuid[] parcelUuidArr = new ParcelUuid[uuidsList.size()];
        uuidsList.toArray(parcelUuidArr);
        processPhonebookAccess();
        synchronized (this.mProfileLock) {
            this.mProfileManager.updateProfiles(uuids, parcelUuidArr, this.mProfiles, this.mRemovedProfiles, this.mLocalNapRoleConnected, this.mDevice);
        }
        Log.d("CachedBluetoothDevice", "updating profiles for " + this.mDevice.getAnonymizedAddress());
        BluetoothClass bluetoothClass = this.mDevice.getBluetoothClass();
        if (bluetoothClass != null) {
            Log.v("CachedBluetoothDevice", "Class: " + bluetoothClass.toString());
        }
        Log.v("CachedBluetoothDevice", "UUID:");
        for (ParcelUuid parcelUuid : uuids) {
            Log.v("CachedBluetoothDevice", "  " + parcelUuid);
        }
        return true;
    }

    public final void fetchActiveDevices() {
        A2dpProfile a2dpProfile = this.mProfileManager.getA2dpProfile();
        if (a2dpProfile != null) {
            this.mIsActiveDeviceA2dp = this.mDevice.equals(a2dpProfile.getActiveDevice());
        }
        HeadsetProfile headsetProfile = this.mProfileManager.getHeadsetProfile();
        if (headsetProfile != null) {
            this.mIsActiveDeviceHeadset = this.mDevice.equals(headsetProfile.getActiveDevice());
        }
        HearingAidProfile hearingAidProfile = this.mProfileManager.getHearingAidProfile();
        if (hearingAidProfile != null) {
            this.mIsActiveDeviceHearingAid = hearingAidProfile.getActiveDevices().contains(this.mDevice);
        }
        LeAudioProfile leAudioProfile = this.mProfileManager.getLeAudioProfile();
        if (leAudioProfile != null) {
            this.mIsActiveDeviceLeAudio = leAudioProfile.getActiveDevices().contains(this.mDevice);
        }
    }

    public void onUuidChanged() {
        updateProfiles();
        ParcelUuid[] uuids = this.mDevice.getUuids();
        long j = 30000;
        if (!ArrayUtils.contains(uuids, BluetoothUuid.HOGP)) {
            if (ArrayUtils.contains(uuids, BluetoothUuid.HEARING_AID)) {
                j = WifiTracker.MAX_SCAN_RESULT_AGE_MILLIS;
            } else if (!ArrayUtils.contains(uuids, BluetoothUuid.LE_AUDIO)) {
                j = 5000;
            }
        }
        Log.d("CachedBluetoothDevice", "onUuidChanged: Time since last connect=" + (SystemClock.elapsedRealtime() - this.mConnectAttempted));
        if (this.mConnectAttempted + j > SystemClock.elapsedRealtime()) {
            Log.d("CachedBluetoothDevice", "onUuidChanged: triggering connectDevice");
            connectDevice();
        }
        lambda$refresh$0();
    }

    public void onBondingStateChanged(int i) {
        if (i == 10) {
            synchronized (this.mProfileLock) {
                this.mProfiles.clear();
            }
            this.mDevice.setPhonebookAccessPermission(0);
            this.mDevice.setMessageAccessPermission(0);
            this.mDevice.setSimAccessPermission(0);
        }
        refresh();
        if (i == 12) {
            boolean isBondingInitiatedLocally = this.mDevice.isBondingInitiatedLocally();
            Log.w("CachedBluetoothDevice", "mIsBondingInitiatedLocally" + isBondingInitiatedLocally);
            if (isBondingInitiatedLocally) {
                connect();
            }
        }
    }

    public BluetoothClass getBtClass() {
        return this.mDevice.getBluetoothClass();
    }

    public List<LocalBluetoothProfile> getProfiles() {
        return new ArrayList(this.mProfiles);
    }

    public boolean isBASeeker() {
        if (this.mDevice == null) {
            Log.e("CachedBluetoothDevice", "isBASeeker: mDevice is null");
            return false;
        }
        Class<BCProfile> cls = BCProfile.class;
        try {
            String str = BCProfile.NAME;
            return ((Boolean) cls.getDeclaredMethod("isBASeeker", new Class[]{BluetoothDevice.class}).invoke((Object) null, new Object[]{this.mDevice})).booleanValue();
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<LocalBluetoothProfile> getConnectableProfiles() {
        ArrayList arrayList = new ArrayList();
        Class<BCProfile> cls = BCProfile.class;
        try {
            String str = BCProfile.NAME;
        } catch (ClassNotFoundException unused) {
            Log.e("CachedBluetoothDevice", "no BCProfileClass: exists");
            cls = null;
        }
        synchronized (this.mProfileLock) {
            for (LocalBluetoothProfile next : this.mProfiles) {
                if (cls == null || !cls.isInstance(next)) {
                    if (next.accessProfileEnabled()) {
                        arrayList.add(next);
                    }
                } else if (isBASeeker()) {
                    arrayList.add(next);
                } else {
                    Log.d("CachedBluetoothDevice", "BC profile is not enabled for" + this.mDevice);
                }
            }
        }
        return arrayList;
    }

    public void registerCallback(Callback callback) {
        this.mCallbacks.add(callback);
    }

    public void unregisterCallback(Callback callback) {
        this.mCallbacks.remove(callback);
    }

    /* renamed from: dispatchAttributesChanged */
    public void lambda$refresh$0() {
        for (Callback onDeviceAttributesChanged : this.mCallbacks) {
            onDeviceAttributesChanged.onDeviceAttributesChanged();
        }
    }

    public String toString() {
        return this.mDevice.toString();
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof CachedBluetoothDevice)) {
            return false;
        }
        return this.mDevice.equals(((CachedBluetoothDevice) obj).mDevice);
    }

    public int hashCode() {
        return this.mDevice.getAddress().hashCode();
    }

    public int compareTo(CachedBluetoothDevice cachedBluetoothDevice) {
        int i = (cachedBluetoothDevice.isConnected() ? 1 : 0) - (isConnected() ? 1 : 0);
        if (i != 0) {
            return i;
        }
        int i2 = 1;
        int i3 = cachedBluetoothDevice.getBondState() == 12 ? 1 : 0;
        if (getBondState() != 12) {
            i2 = 0;
        }
        int i4 = i3 - i2;
        if (i4 != 0) {
            return i4;
        }
        int i5 = (cachedBluetoothDevice.mJustDiscovered ? 1 : 0) - (this.mJustDiscovered ? 1 : 0);
        if (i5 != 0) {
            return i5;
        }
        int i6 = cachedBluetoothDevice.mRssi - this.mRssi;
        if (i6 != 0) {
            return i6;
        }
        return getName().compareTo(cachedBluetoothDevice.getName());
    }

    public final void migratePhonebookPermissionChoice() {
        SharedPreferences sharedPreferences = this.mContext.getSharedPreferences("bluetooth_phonebook_permission", 0);
        if (sharedPreferences.contains(this.mDevice.getAddress())) {
            if (this.mDevice.getPhonebookAccessPermission() == 0) {
                int i = sharedPreferences.getInt(this.mDevice.getAddress(), 0);
                if (i == 1) {
                    this.mDevice.setPhonebookAccessPermission(1);
                } else if (i == 2) {
                    this.mDevice.setPhonebookAccessPermission(2);
                }
            }
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.remove(this.mDevice.getAddress());
            edit.commit();
        }
    }

    public final void migrateMessagePermissionChoice() {
        SharedPreferences sharedPreferences = this.mContext.getSharedPreferences("bluetooth_message_permission", 0);
        if (sharedPreferences.contains(this.mDevice.getAddress())) {
            if (this.mDevice.getMessageAccessPermission() == 0) {
                int i = sharedPreferences.getInt(this.mDevice.getAddress(), 0);
                if (i == 1) {
                    this.mDevice.setMessageAccessPermission(1);
                } else if (i == 2) {
                    this.mDevice.setMessageAccessPermission(2);
                }
            }
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.remove(this.mDevice.getAddress());
            edit.commit();
        }
    }

    public final void processPhonebookAccess() {
        if (this.mDevice.getBondState() == 12 && BluetoothUuid.containsAnyUuid(this.mDevice.getUuids(), PbapServerProfile.PBAB_CLIENT_UUIDS)) {
            BluetoothClass bluetoothClass = this.mDevice.getBluetoothClass();
            if (this.mDevice.getPhonebookAccessPermission() == 0 && bluetoothClass != null) {
                if (bluetoothClass.getDeviceClass() == 1032 || bluetoothClass.getDeviceClass() == 1028) {
                    EventLog.writeEvent(1397638484, new Object[]{"138529441", -1, ""});
                }
            }
        }
    }

    public int getMaxConnectionState() {
        int i;
        synchronized (this.mProfileLock) {
            i = 0;
            for (LocalBluetoothProfile profileConnectionState : getProfiles()) {
                int profileConnectionState2 = getProfileConnectionState(profileConnectionState);
                if (profileConnectionState2 > i) {
                    i = profileConnectionState2;
                }
            }
        }
        return i;
    }

    public CachedBluetoothDevice getSubDevice() {
        return this.mSubDevice;
    }

    public void setSubDevice(CachedBluetoothDevice cachedBluetoothDevice) {
        this.mSubDevice = cachedBluetoothDevice;
    }

    public void switchSubDeviceContent() {
        BluetoothDevice bluetoothDevice = this.mDevice;
        short s = this.mRssi;
        boolean z = this.mJustDiscovered;
        int i = this.mDeviceSide;
        CachedBluetoothDevice cachedBluetoothDevice = this.mSubDevice;
        this.mDevice = cachedBluetoothDevice.mDevice;
        this.mRssi = cachedBluetoothDevice.mRssi;
        this.mJustDiscovered = cachedBluetoothDevice.mJustDiscovered;
        this.mDeviceSide = cachedBluetoothDevice.mDeviceSide;
        cachedBluetoothDevice.mDevice = bluetoothDevice;
        cachedBluetoothDevice.mRssi = s;
        cachedBluetoothDevice.mJustDiscovered = z;
        cachedBluetoothDevice.mDeviceSide = i;
        fetchActiveDevices();
    }

    public Set<CachedBluetoothDevice> getMemberDevice() {
        return this.mMemberDevices;
    }

    public void addMemberDevice(CachedBluetoothDevice cachedBluetoothDevice) {
        this.mMemberDevices.add(cachedBluetoothDevice);
    }

    public void removeMemberDevice(CachedBluetoothDevice cachedBluetoothDevice) {
        this.mMemberDevices.remove(cachedBluetoothDevice);
    }

    public void switchMemberDeviceContent(CachedBluetoothDevice cachedBluetoothDevice, CachedBluetoothDevice cachedBluetoothDevice2) {
        BluetoothDevice bluetoothDevice = this.mDevice;
        short s = this.mRssi;
        boolean z = this.mJustDiscovered;
        this.mDevice = cachedBluetoothDevice2.mDevice;
        this.mRssi = cachedBluetoothDevice2.mRssi;
        this.mJustDiscovered = cachedBluetoothDevice2.mJustDiscovered;
        addMemberDevice(cachedBluetoothDevice);
        this.mMemberDevices.remove(cachedBluetoothDevice2);
        cachedBluetoothDevice2.mDevice = bluetoothDevice;
        cachedBluetoothDevice2.mRssi = s;
        cachedBluetoothDevice2.mJustDiscovered = z;
        fetchActiveDevices();
    }

    public void releaseLruCache() {
        this.mDrawableCache.evictAll();
    }

    public boolean isGroupDevice() {
        return this.mIsGroupDevice;
    }

    public void setDeviceType(int i) {
        if (i != this.mType) {
            this.mType = i;
            if (i == -1 || i == 100) {
                this.mIsGroupDevice = false;
                this.mQGroupId = -1;
                this.mIsIgnore = false;
            } else if (i == 101) {
                this.mIsGroupDevice = false;
                this.mQGroupId = -1;
                this.mIsIgnore = true;
            } else if (i < 0 || i > 15) {
                Log.e("CachedBluetoothDevice", "setDeviceType error type " + this.mType);
            } else {
                this.mQGroupId = i;
                this.mIsIgnore = false;
                this.mIsGroupDevice = true;
            }
        }
    }

    public boolean getUnpairing() {
        return this.mUnpairing;
    }
}
