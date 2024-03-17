package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothUuid;
import android.content.Context;
import android.content.Intent;
import android.os.ParcelUuid;
import android.util.Log;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.CollectionUtils;
import com.android.settingslib.bluetooth.BluetoothEventManager;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class LocalBluetoothProfileManager {
    public A2dpProfile mA2dpProfile;
    public A2dpSinkProfile mA2dpSinkProfile;
    public LocalBluetoothProfile mBCProfile;
    public Object mBroadcastProfileObject;
    public final Context mContext;
    public CsipSetCoordinatorProfile mCsipSetCoordinatorProfile;
    public final CachedBluetoothDeviceManager mDeviceManager;
    public DunServerProfile mDunProfile;
    public final BluetoothEventManager mEventManager;
    public DeviceGroupClientProfile mGroupClientProfile;
    public HeadsetProfile mHeadsetProfile;
    public HearingAidProfile mHearingAidProfile;
    public HfpClientProfile mHfpClientProfile;
    public HidDeviceProfile mHidDeviceProfile;
    public HidProfile mHidProfile;
    public LocalBluetoothLeBroadcast mLeAudioBroadcast;
    public LocalBluetoothLeBroadcastAssistant mLeAudioBroadcastAssistant;
    public LeAudioProfile mLeAudioProfile;
    public MapClientProfile mMapClientProfile;
    public MapProfile mMapProfile;
    public OppProfile mOppProfile;
    public PanProfile mPanProfile;
    public PbapClientProfile mPbapClientProfile;
    public PbapServerProfile mPbapProfile;
    public final Map<String, LocalBluetoothProfile> mProfileNameMap = new HashMap();
    public SapProfile mSapProfile;
    public final Collection<ServiceListener> mServiceListeners = new CopyOnWriteArrayList();
    public VcpProfile mVcpProfile;
    public VolumeControlProfile mVolumeControlProfile;

    public interface ServiceListener {
        void onServiceConnected();

        void onServiceDisconnected();
    }

    public LocalBluetoothProfileManager(Context context, LocalBluetoothAdapter localBluetoothAdapter, CachedBluetoothDeviceManager cachedBluetoothDeviceManager, BluetoothEventManager bluetoothEventManager) {
        this.mContext = context;
        this.mDeviceManager = cachedBluetoothDeviceManager;
        this.mEventManager = bluetoothEventManager;
        localBluetoothAdapter.setProfileManager(this);
        Log.d("LocalBluetoothProfileManager", "LocalBluetoothProfileManager construction complete");
    }

    public void updateLocalProfiles() {
        List supportedProfiles = BluetoothAdapter.getDefaultAdapter().getSupportedProfiles();
        if (CollectionUtils.isEmpty(supportedProfiles)) {
            Log.d("LocalBluetoothProfileManager", "supportedList is null");
            return;
        }
        if (this.mA2dpProfile == null && supportedProfiles.contains(2)) {
            Log.d("LocalBluetoothProfileManager", "Adding local A2DP profile");
            A2dpProfile a2dpProfile = new A2dpProfile(this.mContext, this.mDeviceManager, this);
            this.mA2dpProfile = a2dpProfile;
            addProfile(a2dpProfile, "A2DP", "android.bluetooth.a2dp.profile.action.CONNECTION_STATE_CHANGED");
        }
        if (this.mA2dpSinkProfile == null && supportedProfiles.contains(11)) {
            Log.d("LocalBluetoothProfileManager", "Adding local A2DP SINK profile");
            A2dpSinkProfile a2dpSinkProfile = new A2dpSinkProfile(this.mContext, this.mDeviceManager, this);
            this.mA2dpSinkProfile = a2dpSinkProfile;
            addProfile(a2dpSinkProfile, "A2DPSink", "android.bluetooth.a2dp-sink.profile.action.CONNECTION_STATE_CHANGED");
        }
        if (this.mHeadsetProfile == null && supportedProfiles.contains(1)) {
            Log.d("LocalBluetoothProfileManager", "Adding local HEADSET profile");
            HeadsetProfile headsetProfile = new HeadsetProfile(this.mContext, this.mDeviceManager, this);
            this.mHeadsetProfile = headsetProfile;
            addHeadsetProfile(headsetProfile, "HEADSET", "android.bluetooth.headset.profile.action.CONNECTION_STATE_CHANGED", "android.bluetooth.headset.profile.action.AUDIO_STATE_CHANGED", 10);
        }
        if (this.mHfpClientProfile == null && supportedProfiles.contains(16)) {
            Log.d("LocalBluetoothProfileManager", "Adding local HfpClient profile");
            HfpClientProfile hfpClientProfile = new HfpClientProfile(this.mContext, this.mDeviceManager, this);
            this.mHfpClientProfile = hfpClientProfile;
            addProfile(hfpClientProfile, "HEADSET_CLIENT", "android.bluetooth.headsetclient.profile.action.CONNECTION_STATE_CHANGED");
        }
        if (this.mMapClientProfile == null && supportedProfiles.contains(18)) {
            Log.d("LocalBluetoothProfileManager", "Adding local MAP CLIENT profile");
            MapClientProfile mapClientProfile = new MapClientProfile(this.mContext, this.mDeviceManager, this);
            this.mMapClientProfile = mapClientProfile;
            addProfile(mapClientProfile, "MAP Client", "android.bluetooth.mapmce.profile.action.CONNECTION_STATE_CHANGED");
        }
        if (this.mMapProfile == null && supportedProfiles.contains(9)) {
            Log.d("LocalBluetoothProfileManager", "Adding local MAP profile");
            MapProfile mapProfile = new MapProfile(this.mContext, this.mDeviceManager, this);
            this.mMapProfile = mapProfile;
            addProfile(mapProfile, "MAP", "android.bluetooth.map.profile.action.CONNECTION_STATE_CHANGED");
        }
        if (this.mOppProfile == null && supportedProfiles.contains(20)) {
            Log.d("LocalBluetoothProfileManager", "Adding local OPP profile");
            OppProfile oppProfile = new OppProfile();
            this.mOppProfile = oppProfile;
            this.mProfileNameMap.put("OPP", oppProfile);
        }
        if (this.mHearingAidProfile == null && supportedProfiles.contains(21)) {
            Log.d("LocalBluetoothProfileManager", "Adding local Hearing Aid profile");
            HearingAidProfile hearingAidProfile = new HearingAidProfile(this.mContext, this.mDeviceManager, this);
            this.mHearingAidProfile = hearingAidProfile;
            addProfile(hearingAidProfile, "HearingAid", "android.bluetooth.hearingaid.profile.action.CONNECTION_STATE_CHANGED");
        }
        if (this.mHidProfile == null && supportedProfiles.contains(4)) {
            Log.d("LocalBluetoothProfileManager", "Adding local HID_HOST profile");
            HidProfile hidProfile = new HidProfile(this.mContext, this.mDeviceManager, this);
            this.mHidProfile = hidProfile;
            addProfile(hidProfile, "HID", "android.bluetooth.input.profile.action.CONNECTION_STATE_CHANGED");
        }
        if (this.mHidDeviceProfile == null && supportedProfiles.contains(19)) {
            Log.d("LocalBluetoothProfileManager", "Adding local HID_DEVICE profile");
            HidDeviceProfile hidDeviceProfile = new HidDeviceProfile(this.mContext, this.mDeviceManager, this);
            this.mHidDeviceProfile = hidDeviceProfile;
            addProfile(hidDeviceProfile, "HID DEVICE", "android.bluetooth.hiddevice.profile.action.CONNECTION_STATE_CHANGED");
        }
        if (this.mPanProfile == null && supportedProfiles.contains(5)) {
            Log.d("LocalBluetoothProfileManager", "Adding local PAN profile");
            PanProfile panProfile = new PanProfile(this.mContext);
            this.mPanProfile = panProfile;
            addPanProfile(panProfile, "PAN", "android.bluetooth.pan.profile.action.CONNECTION_STATE_CHANGED");
        }
        if (this.mPbapProfile == null && supportedProfiles.contains(6)) {
            Log.d("LocalBluetoothProfileManager", "Adding local PBAP profile");
            PbapServerProfile pbapServerProfile = new PbapServerProfile(this.mContext);
            this.mPbapProfile = pbapServerProfile;
            addProfile(pbapServerProfile, PbapServerProfile.NAME, "android.bluetooth.pbap.profile.action.CONNECTION_STATE_CHANGED");
        }
        if (this.mPbapClientProfile == null && supportedProfiles.contains(17)) {
            Log.d("LocalBluetoothProfileManager", "Adding local PBAP Client profile");
            PbapClientProfile pbapClientProfile = new PbapClientProfile(this.mContext, this.mDeviceManager, this);
            this.mPbapClientProfile = pbapClientProfile;
            addProfile(pbapClientProfile, "PbapClient", "android.bluetooth.pbapclient.profile.action.CONNECTION_STATE_CHANGED");
        }
        if (this.mBCProfile == null && supportedProfiles.contains(35)) {
            Log.d("LocalBluetoothProfileManager", "Adding local BC profile");
            Class<BCProfile> cls = BCProfile.class;
            try {
                String str = BCProfile.NAME;
                LocalBluetoothProfile newInstance = cls.getDeclaredConstructor(new Class[]{Context.class, CachedBluetoothDeviceManager.class, LocalBluetoothProfileManager.class}).newInstance(new Object[]{this.mContext, this.mDeviceManager, this});
                this.mBCProfile = newInstance;
                addProfile(newInstance, BCProfile.NAME, "android.bluetooth.bc.profile.action.CONNECTION_STATE_CHANGED");
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        if (this.mSapProfile == null && supportedProfiles.contains(10)) {
            Log.d("LocalBluetoothProfileManager", "Adding local SAP profile");
            SapProfile sapProfile = new SapProfile(this.mContext, this.mDeviceManager, this);
            this.mSapProfile = sapProfile;
            addProfile(sapProfile, "SAP", "android.bluetooth.sap.profile.action.CONNECTION_STATE_CHANGED");
        }
        if (this.mBroadcastProfileObject == null && supportedProfiles.contains(33)) {
            Log.d("LocalBluetoothProfileManager", "Adding local Broadcast profile");
            Class<BroadcastProfile> cls2 = BroadcastProfile.class;
            try {
                String str2 = BroadcastProfile.NAME;
                BroadcastProfile newInstance2 = cls2.getDeclaredConstructor(new Class[]{Context.class}).newInstance(new Object[]{this.mContext});
                this.mBroadcastProfileObject = newInstance2;
                this.mProfileNameMap.put(BroadcastProfile.NAME, newInstance2);
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e2) {
                e2.printStackTrace();
            }
        }
        if (this.mDunProfile == null && supportedProfiles.contains(31)) {
            Log.d("LocalBluetoothProfileManager", "Adding local DUN profile");
            DunServerProfile dunServerProfile = new DunServerProfile(this.mContext);
            this.mDunProfile = dunServerProfile;
            addProfile(dunServerProfile, "DUN Server", "codeaurora.bluetooth.dun.profile.action.CONNECTION_STATE_CHANGED");
        }
        if (this.mGroupClientProfile == null && supportedProfiles.contains(32)) {
            Log.d("LocalBluetoothProfileManager", "Adding local GROUP CLIENT profile");
            DeviceGroupClientProfile deviceGroupClientProfile = new DeviceGroupClientProfile(this.mContext, this.mDeviceManager, this);
            this.mGroupClientProfile = deviceGroupClientProfile;
            addProfile(deviceGroupClientProfile, "DeviceGroup Client", "android.bluetooth.group.profile.action.CONNECTION_STATE_CHANGED");
        }
        if (this.mVcpProfile == null && supportedProfiles.contains(34)) {
            Log.d("LocalBluetoothProfileManager", "Adding local VCP profile");
            VcpProfile vcpProfile = new VcpProfile(this.mContext, this.mDeviceManager, this);
            this.mVcpProfile = vcpProfile;
            addProfile(vcpProfile, "VCP", "android.bluetooth.vcp.profile.action.CONNECTION_STATE_CHANGED");
        }
        if (this.mVolumeControlProfile == null && supportedProfiles.contains(23)) {
            Log.d("LocalBluetoothProfileManager", "Adding local Volume Control profile");
            VolumeControlProfile volumeControlProfile = new VolumeControlProfile();
            this.mVolumeControlProfile = volumeControlProfile;
            this.mProfileNameMap.put("VCP", volumeControlProfile);
        }
        if (this.mLeAudioProfile == null && supportedProfiles.contains(22)) {
            Log.d("LocalBluetoothProfileManager", "Adding local LE_AUDIO profile");
            LeAudioProfile leAudioProfile = new LeAudioProfile(this.mContext, this.mDeviceManager, this);
            this.mLeAudioProfile = leAudioProfile;
            addProfile(leAudioProfile, "LE_AUDIO", "android.bluetooth.action.LE_AUDIO_CONNECTION_STATE_CHANGED");
        }
        if (this.mLeAudioBroadcast == null && supportedProfiles.contains(26)) {
            Log.d("LocalBluetoothProfileManager", "Adding local LE_AUDIO_BROADCAST profile");
            LocalBluetoothLeBroadcast localBluetoothLeBroadcast = new LocalBluetoothLeBroadcast(this.mContext);
            this.mLeAudioBroadcast = localBluetoothLeBroadcast;
            this.mProfileNameMap.put("LE_AUDIO_BROADCAST", localBluetoothLeBroadcast);
        }
        if (this.mLeAudioBroadcastAssistant == null && supportedProfiles.contains(29)) {
            Log.d("LocalBluetoothProfileManager", "Adding local LE_AUDIO_BROADCAST_ASSISTANT profile");
            LocalBluetoothLeBroadcastAssistant localBluetoothLeBroadcastAssistant = new LocalBluetoothLeBroadcastAssistant(this.mContext, this.mDeviceManager, this);
            this.mLeAudioBroadcastAssistant = localBluetoothLeBroadcastAssistant;
            addProfile(localBluetoothLeBroadcastAssistant, "LE_AUDIO_BROADCAST", "android.bluetooth.action.CONNECTION_STATE_CHANGED");
        }
        if (this.mCsipSetCoordinatorProfile == null && supportedProfiles.contains(25)) {
            Log.d("LocalBluetoothProfileManager", "Adding local CSIP set coordinator profile");
            CsipSetCoordinatorProfile csipSetCoordinatorProfile = new CsipSetCoordinatorProfile(this.mContext, this.mDeviceManager, this);
            this.mCsipSetCoordinatorProfile = csipSetCoordinatorProfile;
            addProfile(csipSetCoordinatorProfile, "CSIP Set Coordinator", "android.bluetooth.action.CSIS_CONNECTION_STATE_CHANGED");
        }
        this.mEventManager.registerProfileIntentReceiver();
    }

    public final void addHeadsetProfile(LocalBluetoothProfile localBluetoothProfile, String str, String str2, String str3, int i) {
        HeadsetStateChangeHandler headsetStateChangeHandler = new HeadsetStateChangeHandler(localBluetoothProfile, str3, i);
        this.mEventManager.addProfileHandler(str2, headsetStateChangeHandler);
        this.mEventManager.addProfileHandler(str3, headsetStateChangeHandler);
        this.mProfileNameMap.put(str, localBluetoothProfile);
    }

    public final void addProfile(LocalBluetoothProfile localBluetoothProfile, String str, String str2) {
        this.mEventManager.addProfileHandler(str2, new StateChangedHandler(localBluetoothProfile));
        this.mProfileNameMap.put(str, localBluetoothProfile);
    }

    public final void addPanProfile(LocalBluetoothProfile localBluetoothProfile, String str, String str2) {
        this.mEventManager.addProfileHandler(str2, new PanStateChangedHandler(localBluetoothProfile));
        this.mProfileNameMap.put(str, localBluetoothProfile);
    }

    public void setBluetoothStateOn() {
        updateLocalProfiles();
        this.mEventManager.readPairedDevices();
    }

    public class StateChangedHandler implements BluetoothEventManager.Handler {
        public final LocalBluetoothProfile mProfile;

        public StateChangedHandler(LocalBluetoothProfile localBluetoothProfile) {
            this.mProfile = localBluetoothProfile;
        }

        public void onReceive(Context context, Intent intent, BluetoothDevice bluetoothDevice) {
            if (bluetoothDevice == null) {
                Log.w("LocalBluetoothProfileManager", "StateChangedHandler receives state-change for invalid device");
                return;
            }
            CachedBluetoothDevice findDevice = LocalBluetoothProfileManager.this.mDeviceManager.findDevice(bluetoothDevice);
            if (findDevice == null) {
                Log.w("LocalBluetoothProfileManager", "StateChangedHandler found new device: " + bluetoothDevice);
                findDevice = LocalBluetoothProfileManager.this.mDeviceManager.addDevice(bluetoothDevice);
            }
            onReceiveInternal(intent, findDevice);
        }

        public void onReceiveInternal(Intent intent, CachedBluetoothDevice cachedBluetoothDevice) {
            Map<Integer, ParcelUuid> groupUuidMapByDevice;
            int intExtra = intent.getIntExtra("android.bluetooth.profile.extra.STATE", 0);
            int intExtra2 = intent.getIntExtra("android.bluetooth.profile.extra.PREVIOUS_STATE", 0);
            boolean z = true;
            if (intExtra == 0 && intExtra2 == 1) {
                Log.i("LocalBluetoothProfileManager", "Failed to connect " + this.mProfile + " device");
            }
            if (LocalBluetoothProfileManager.this.getHearingAidProfile() != null && (this.mProfile instanceof HearingAidProfile) && intExtra == 2) {
                int deviceSide = LocalBluetoothProfileManager.this.getHearingAidProfile().getDeviceSide(cachedBluetoothDevice.getDevice());
                int deviceMode = LocalBluetoothProfileManager.this.getHearingAidProfile().getDeviceMode(cachedBluetoothDevice.getDevice());
                cachedBluetoothDevice.setDeviceSide(deviceSide);
                cachedBluetoothDevice.setDeviceMode(deviceMode);
                if (cachedBluetoothDevice.getHiSyncId() == 0) {
                    long hiSyncId = LocalBluetoothProfileManager.this.getHearingAidProfile().getHiSyncId(cachedBluetoothDevice.getDevice());
                    if (hiSyncId != 0) {
                        cachedBluetoothDevice.setHiSyncId(hiSyncId);
                    }
                }
            }
            if (LocalBluetoothProfileManager.this.getCsipSetCoordinatorProfile() != null && (this.mProfile instanceof CsipSetCoordinatorProfile) && intExtra == 2 && cachedBluetoothDevice.getGroupId() == -1 && (groupUuidMapByDevice = LocalBluetoothProfileManager.this.getCsipSetCoordinatorProfile().getGroupUuidMapByDevice(cachedBluetoothDevice.getDevice())) != null) {
                Iterator<Map.Entry<Integer, ParcelUuid>> it = groupUuidMapByDevice.entrySet().iterator();
                if (it.hasNext()) {
                    cachedBluetoothDevice.setGroupId(((Integer) it.next().getKey()).intValue());
                }
            }
            cachedBluetoothDevice.onProfileStateChanged(this.mProfile, intExtra);
            if (!(cachedBluetoothDevice.getHiSyncId() == 0 && cachedBluetoothDevice.getGroupId() == -1)) {
                z = true ^ LocalBluetoothProfileManager.this.mDeviceManager.onProfileConnectionStateChangedIfProcessed(cachedBluetoothDevice, intExtra, this.mProfile.getProfileId());
            }
            if (z) {
                cachedBluetoothDevice.refresh();
                LocalBluetoothProfileManager.this.mEventManager.dispatchProfileConnectionStateChanged(cachedBluetoothDevice, intExtra, this.mProfile.getProfileId());
            }
        }
    }

    public class HeadsetStateChangeHandler extends StateChangedHandler {
        public final String mAudioChangeAction;
        public final int mAudioDisconnectedState;

        public HeadsetStateChangeHandler(LocalBluetoothProfile localBluetoothProfile, String str, int i) {
            super(localBluetoothProfile);
            this.mAudioChangeAction = str;
            this.mAudioDisconnectedState = i;
        }

        public void onReceiveInternal(Intent intent, CachedBluetoothDevice cachedBluetoothDevice) {
            if (this.mAudioChangeAction.equals(intent.getAction())) {
                if (intent.getIntExtra("android.bluetooth.profile.extra.STATE", 0) != this.mAudioDisconnectedState) {
                    cachedBluetoothDevice.onProfileStateChanged(this.mProfile, 2);
                }
                cachedBluetoothDevice.refresh();
                return;
            }
            super.onReceiveInternal(intent, cachedBluetoothDevice);
        }
    }

    public class PanStateChangedHandler extends StateChangedHandler {
        public PanStateChangedHandler(LocalBluetoothProfile localBluetoothProfile) {
            super(localBluetoothProfile);
        }

        public void onReceive(Context context, Intent intent, BluetoothDevice bluetoothDevice) {
            ((PanProfile) this.mProfile).setLocalRole(bluetoothDevice, intent.getIntExtra("android.bluetooth.pan.extra.LOCAL_ROLE", 0));
            super.onReceive(context, intent, bluetoothDevice);
        }
    }

    public void addServiceListener(ServiceListener serviceListener) {
        this.mServiceListeners.add(serviceListener);
    }

    public void callServiceConnectedListeners() {
        for (ServiceListener onServiceConnected : new ArrayList(this.mServiceListeners)) {
            onServiceConnected.onServiceConnected();
        }
    }

    public void callServiceDisconnectedListeners() {
        for (ServiceListener onServiceDisconnected : new ArrayList(this.mServiceListeners)) {
            onServiceDisconnected.onServiceDisconnected();
        }
    }

    public A2dpProfile getA2dpProfile() {
        return this.mA2dpProfile;
    }

    public HeadsetProfile getHeadsetProfile() {
        return this.mHeadsetProfile;
    }

    public HearingAidProfile getHearingAidProfile() {
        return this.mHearingAidProfile;
    }

    public LeAudioProfile getLeAudioProfile() {
        return this.mLeAudioProfile;
    }

    public LocalBluetoothLeBroadcast getLeAudioBroadcastProfile() {
        return this.mLeAudioBroadcast;
    }

    public final boolean isBASeeker(BluetoothDevice bluetoothDevice) {
        if (bluetoothDevice == null) {
            Log.e("LocalBluetoothProfileManager", "isBASeeker: device is null");
            return false;
        }
        Class<BCProfile> cls = BCProfile.class;
        try {
            String str = BCProfile.NAME;
            return ((Boolean) cls.getDeclaredMethod("isBASeeker", new Class[]{BluetoothDevice.class}).invoke((Object) null, new Object[]{bluetoothDevice})).booleanValue();
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            return false;
        }
    }

    public LocalBluetoothProfile getBCProfile() {
        Log.d("LocalBluetoothProfileManager", "getBCProfile returning: " + this.mBCProfile);
        return this.mBCProfile;
    }

    public HidProfile getHidProfile() {
        return this.mHidProfile;
    }

    public HidDeviceProfile getHidDeviceProfile() {
        return this.mHidDeviceProfile;
    }

    public CsipSetCoordinatorProfile getCsipSetCoordinatorProfile() {
        return this.mCsipSetCoordinatorProfile;
    }

    public synchronized void updateProfiles(ParcelUuid[] parcelUuidArr, ParcelUuid[] parcelUuidArr2, Collection<LocalBluetoothProfile> collection, Collection<LocalBluetoothProfile> collection2, boolean z, BluetoothDevice bluetoothDevice) {
        LeAudioProfile leAudioProfile;
        HearingAidProfile hearingAidProfile;
        HidProfile hidProfile;
        OppProfile oppProfile;
        A2dpSinkProfile a2dpSinkProfile;
        collection2.clear();
        collection2.addAll(collection);
        Log.d("LocalBluetoothProfileManager", "Current Profiles" + collection.toString());
        collection.clear();
        if (parcelUuidArr != null) {
            if (this.mHeadsetProfile != null && ((ArrayUtils.contains(parcelUuidArr2, BluetoothUuid.HSP_AG) && ArrayUtils.contains(parcelUuidArr, BluetoothUuid.HSP)) || ((ArrayUtils.contains(parcelUuidArr2, BluetoothUuid.HFP_AG) && ArrayUtils.contains(parcelUuidArr, BluetoothUuid.HFP)) || this.mHeadsetProfile.getConnectionStatus(bluetoothDevice) == 2))) {
                collection.add(this.mHeadsetProfile);
                collection2.remove(this.mHeadsetProfile);
            }
            if (this.mHfpClientProfile != null && ArrayUtils.contains(parcelUuidArr, BluetoothUuid.HFP_AG) && ArrayUtils.contains(parcelUuidArr2, BluetoothUuid.HFP)) {
                collection.add(this.mHfpClientProfile);
                collection2.remove(this.mHfpClientProfile);
            }
            if (this.mA2dpProfile != null && (BluetoothUuid.containsAnyUuid(parcelUuidArr, A2dpProfile.SINK_UUIDS) || this.mA2dpProfile.getConnectionStatus(bluetoothDevice) == 2)) {
                collection.add(this.mA2dpProfile);
                collection2.remove(this.mA2dpProfile);
            }
            if (this.mHeadsetProfile != null && (ArrayUtils.contains(parcelUuidArr, BluetoothUuid.ADVANCE_VOICE_P_UUID) || ArrayUtils.contains(parcelUuidArr, BluetoothUuid.ADVANCE_VOICE_T_UUID) || ArrayUtils.contains(parcelUuidArr, BluetoothUuid.ADVANCE_HEARINGAID_UUID) || this.mHeadsetProfile.getConnectionStatus(bluetoothDevice) == 2)) {
                if (!collection.contains(this.mHeadsetProfile)) {
                    collection.add(this.mHeadsetProfile);
                    collection2.remove(this.mHeadsetProfile);
                    Log.d("LocalBluetoothProfileManager", "Advance Audio Voice supported");
                } else {
                    Log.d("LocalBluetoothProfileManager", "HeadsetProfile already added");
                }
            }
            if (this.mA2dpProfile != null && (ArrayUtils.contains(parcelUuidArr, BluetoothUuid.ADVANCE_MEDIA_T_UUID) || ArrayUtils.contains(parcelUuidArr, BluetoothUuid.ADVANCE_HEARINGAID_UUID) || ArrayUtils.contains(parcelUuidArr, BluetoothUuid.ADVANCE_MEDIA_P_UUID) || ArrayUtils.contains(parcelUuidArr, BluetoothUuid.ADVANCE_MEDIA_G_UUID) || ArrayUtils.contains(parcelUuidArr, BluetoothUuid.ADVANCE_MEDIA_W_UUID) || this.mA2dpProfile.getConnectionStatus(bluetoothDevice) == 2)) {
                if (!collection.contains(this.mA2dpProfile)) {
                    collection.add(this.mA2dpProfile);
                    collection2.remove(this.mA2dpProfile);
                    Log.d("LocalBluetoothProfileManager", "Advance Audio Media supported");
                } else {
                    Log.d("LocalBluetoothProfileManager", "A2dpProfile already added");
                }
            }
            if (BluetoothUuid.containsAnyUuid(parcelUuidArr, A2dpSinkProfile.SRC_UUIDS) && (a2dpSinkProfile = this.mA2dpSinkProfile) != null) {
                collection.add(a2dpSinkProfile);
                collection2.remove(this.mA2dpSinkProfile);
            }
            if (ArrayUtils.contains(parcelUuidArr, BluetoothUuid.OBEX_OBJECT_PUSH) && (oppProfile = this.mOppProfile) != null) {
                collection.add(oppProfile);
                collection2.remove(this.mOppProfile);
            }
            if ((ArrayUtils.contains(parcelUuidArr, BluetoothUuid.HID) || ArrayUtils.contains(parcelUuidArr, BluetoothUuid.HOGP)) && (hidProfile = this.mHidProfile) != null) {
                collection.add(hidProfile);
                collection2.remove(this.mHidProfile);
            }
            HidDeviceProfile hidDeviceProfile = this.mHidDeviceProfile;
            if (!(hidDeviceProfile == null || hidDeviceProfile.getConnectionStatus(bluetoothDevice) == 0)) {
                collection.add(this.mHidDeviceProfile);
                collection2.remove(this.mHidDeviceProfile);
            }
            if (z) {
                Log.d("LocalBluetoothProfileManager", "Valid PAN-NAP connection exists.");
            }
            if ((ArrayUtils.contains(parcelUuidArr, BluetoothUuid.NAP) && this.mPanProfile != null) || z) {
                collection.add(this.mPanProfile);
                collection2.remove(this.mPanProfile);
            }
            MapProfile mapProfile = this.mMapProfile;
            if (mapProfile != null && mapProfile.getConnectionStatus(bluetoothDevice) == 2) {
                collection.add(this.mMapProfile);
                collection2.remove(this.mMapProfile);
                this.mMapProfile.setEnabled(bluetoothDevice, true);
            }
            if (this.mPbapProfile != null && BluetoothUuid.containsAnyUuid(parcelUuidArr, PbapServerProfile.PBAB_CLIENT_UUIDS)) {
                collection.add(this.mPbapProfile);
                collection2.remove(this.mPbapProfile);
                this.mPbapProfile.setEnabled(bluetoothDevice, true);
            }
            if (this.mMapClientProfile != null && BluetoothUuid.containsAnyUuid(parcelUuidArr, MapClientProfile.UUIDS)) {
                collection.add(this.mMapClientProfile);
                collection2.remove(this.mMapClientProfile);
            }
            if (this.mPbapClientProfile != null && BluetoothUuid.containsAnyUuid(parcelUuidArr, PbapClientProfile.SRC_UUIDS)) {
                collection.add(this.mPbapClientProfile);
                collection2.remove(this.mPbapClientProfile);
            }
            if (ArrayUtils.contains(parcelUuidArr, BluetoothUuid.HEARING_AID) && (hearingAidProfile = this.mHearingAidProfile) != null) {
                collection.add(hearingAidProfile);
                collection2.remove(this.mHearingAidProfile);
            }
            if (ArrayUtils.contains(parcelUuidArr, BluetoothUuid.LE_AUDIO) && (leAudioProfile = this.mLeAudioProfile) != null) {
                collection.add(leAudioProfile);
                collection2.remove(this.mLeAudioProfile);
            }
            if (this.mSapProfile != null && ArrayUtils.contains(parcelUuidArr, BluetoothUuid.SAP)) {
                collection.add(this.mSapProfile);
                collection2.remove(this.mSapProfile);
            }
            if (this.mBCProfile != null && isBASeeker(bluetoothDevice)) {
                collection.add(this.mBCProfile);
                collection2.remove(this.mBCProfile);
                Log.d("LocalBluetoothProfileManager", "BC profile added");
            }
            if (this.mVolumeControlProfile != null && ArrayUtils.contains(parcelUuidArr, BluetoothUuid.VOLUME_CONTROL)) {
                collection.add(this.mVolumeControlProfile);
                collection2.remove(this.mVolumeControlProfile);
            }
            if (this.mCsipSetCoordinatorProfile != null && ArrayUtils.contains(parcelUuidArr, BluetoothUuid.COORDINATED_SET)) {
                collection.add(this.mCsipSetCoordinatorProfile);
                collection2.remove(this.mCsipSetCoordinatorProfile);
            }
            Log.d("LocalBluetoothProfileManager", "New Profiles" + collection.toString());
        }
    }
}
