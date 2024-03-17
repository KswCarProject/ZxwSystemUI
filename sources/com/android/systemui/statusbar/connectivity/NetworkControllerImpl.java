package com.android.systemui.statusbar.connectivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemProperties;
import android.provider.Settings;
import android.telephony.CarrierConfigManager;
import android.telephony.ServiceState;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import android.telephony.UiccAccessRule;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import androidx.mediarouter.media.MediaRoute2Provider$$ExternalSyntheticLambda0;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settingslib.Utils;
import com.android.settingslib.mobile.MobileMappings;
import com.android.settingslib.mobile.MobileStatusTracker;
import com.android.settingslib.mobile.TelephonyIcons;
import com.android.settingslib.net.DataUsageController;
import com.android.systemui.Dumpable;
import com.android.systemui.R$string;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.demomode.DemoMode;
import com.android.systemui.demomode.DemoModeController;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.android.systemui.qs.tiles.dialog.InternetDialogFactory;
import com.android.systemui.settings.CurrentUserTracker;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.DataSaverController;
import com.android.systemui.statusbar.policy.DataSaverControllerImpl;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.statusbar.policy.EncryptionHelper;
import com.android.systemui.statusbar.policy.FiveGServiceClient;
import com.android.systemui.telephony.TelephonyListenerManager;
import com.android.systemui.util.CarrierConfigTracker;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.Executor;

public class NetworkControllerImpl extends BroadcastReceiver implements NetworkController, DemoMode, DataUsageController.NetworkNameProvider, Dumpable {
    public static final boolean CHATTY = Log.isLoggable("NetworkControllerChat", 3);
    public static final boolean DEBUG = Log.isLoggable("NetworkController", 3);
    public static final SimpleDateFormat SSDF = new SimpleDateFormat("MM-dd HH:mm:ss.SSS");
    public final AccessPointControllerImpl mAccessPoints;
    public int mActiveMobileDataSubscription;
    public boolean mAirplaneMode;
    public final Executor mBgExecutor;
    public final Looper mBgLooper;
    public final BroadcastDispatcher mBroadcastDispatcher;
    public final CallbackHandler mCallbackHandler;
    public final CarrierConfigTracker mCarrierConfigTracker;
    public final Runnable mClearForceValidated;
    public MobileMappings.Config mConfig;
    public ConfigurationController.ConfigurationListener mConfigurationListener;
    public final BitSet mConnectedTransports;
    public final ConnectivityManager mConnectivityManager;
    public final Context mContext;
    public List<SubscriptionInfo> mCurrentSubscriptions;
    public int mCurrentUserId;
    public final DataSaverController mDataSaverController;
    public final DataUsageController mDataUsageController;
    public MobileSignalController mDefaultSignalController;
    public boolean mDemoInetCondition;
    public final DemoModeController mDemoModeController;
    public WifiState mDemoWifiState;
    public final DumpManager mDumpManager;
    public int mEmergencySource;
    @VisibleForTesting
    public final EthernetSignalController mEthernetSignalController;
    public final FeatureFlags mFeatureFlags;
    @VisibleForTesting
    public FiveGServiceClient mFiveGServiceClient;
    public boolean mForceCellularValidated;
    public final boolean mHasMobileDataFeature;
    public boolean mHasNoSubs;
    public final String[] mHistory;
    public int mHistoryIndex;
    public boolean mInetCondition;
    public InternetDialogFactory mInternetDialogFactory;
    public boolean mIsEmergency;
    public NetworkCapabilities mLastDefaultNetworkCapabilities;
    @VisibleForTesting
    public ServiceState mLastServiceState;
    @VisibleForTesting
    public boolean mListening;
    public Locale mLocale;
    public final Object mLock;
    public Handler mMainHandler;
    @VisibleForTesting
    public final SparseArray<MobileSignalController> mMobileSignalControllers;
    public boolean mNoDefaultNetwork;
    public boolean mNoNetworksAvailable;
    public final TelephonyManager mPhone;
    public TelephonyCallback.ActiveDataSubscriptionIdListener mPhoneStateListener;
    public final boolean mProviderModelBehavior;
    public final Handler mReceiverHandler;
    public final Runnable mRegisterListeners;
    public boolean mSimDetected;
    public final MobileStatusTracker.SubscriptionDefaults mSubDefaults;
    public SubscriptionManager.OnSubscriptionsChangedListener mSubscriptionListener;
    public final SubscriptionManager mSubscriptionManager;
    public final TelephonyListenerManager mTelephonyListenerManager;
    public boolean mUserSetup;
    public final CurrentUserTracker mUserTracker;
    public final BitSet mValidatedTransports;
    public final WifiManager mWifiManager;
    @VisibleForTesting
    public final WifiSignalController mWifiSignalController;

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public NetworkControllerImpl(android.content.Context r24, android.os.Looper r25, java.util.concurrent.Executor r26, android.telephony.SubscriptionManager r27, com.android.systemui.statusbar.connectivity.CallbackHandler r28, com.android.systemui.statusbar.policy.DeviceProvisionedController r29, com.android.systemui.broadcast.BroadcastDispatcher r30, android.net.ConnectivityManager r31, android.telephony.TelephonyManager r32, com.android.systemui.telephony.TelephonyListenerManager r33, android.net.wifi.WifiManager r34, com.android.systemui.statusbar.connectivity.AccessPointControllerImpl r35, com.android.systemui.demomode.DemoModeController r36, com.android.systemui.util.CarrierConfigTracker r37, com.android.systemui.statusbar.connectivity.WifiStatusTrackerFactory r38, android.os.Handler r39, com.android.systemui.qs.tiles.dialog.InternetDialogFactory r40, com.android.systemui.flags.FeatureFlags r41, com.android.systemui.dump.DumpManager r42) {
        /*
            r23 = this;
            r13 = r23
            r0 = r23
            r1 = r24
            r8 = r25
            r9 = r26
            r6 = r27
            r10 = r28
            r14 = r29
            r15 = r30
            r2 = r31
            r3 = r32
            r4 = r33
            r5 = r34
            r11 = r35
            r16 = r36
            r17 = r37
            r18 = r38
            r19 = r39
            r20 = r41
            r21 = r42
            com.android.settingslib.mobile.MobileMappings$Config r7 = com.android.settingslib.mobile.MobileMappings.Config.readConfig(r24)
            com.android.settingslib.net.DataUsageController r12 = new com.android.settingslib.net.DataUsageController
            r25 = r12
            r13 = r24
            r22 = r0
            r0 = r25
            r0.<init>(r13)
            com.android.settingslib.mobile.MobileStatusTracker$SubscriptionDefaults r0 = new com.android.settingslib.mobile.MobileStatusTracker$SubscriptionDefaults
            r13 = r0
            r0.<init>()
            r0 = r22
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19, r20, r21)
            r0 = r23
            android.os.Handler r1 = r0.mReceiverHandler
            java.lang.Runnable r2 = r0.mRegisterListeners
            r1.post(r2)
            r1 = r40
            r0.mInternetDialogFactory = r1
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.connectivity.NetworkControllerImpl.<init>(android.content.Context, android.os.Looper, java.util.concurrent.Executor, android.telephony.SubscriptionManager, com.android.systemui.statusbar.connectivity.CallbackHandler, com.android.systemui.statusbar.policy.DeviceProvisionedController, com.android.systemui.broadcast.BroadcastDispatcher, android.net.ConnectivityManager, android.telephony.TelephonyManager, com.android.systemui.telephony.TelephonyListenerManager, android.net.wifi.WifiManager, com.android.systemui.statusbar.connectivity.AccessPointControllerImpl, com.android.systemui.demomode.DemoModeController, com.android.systemui.util.CarrierConfigTracker, com.android.systemui.statusbar.connectivity.WifiStatusTrackerFactory, android.os.Handler, com.android.systemui.qs.tiles.dialog.InternetDialogFactory, com.android.systemui.flags.FeatureFlags, com.android.systemui.dump.DumpManager):void");
    }

    @VisibleForTesting
    public NetworkControllerImpl(Context context, ConnectivityManager connectivityManager, TelephonyManager telephonyManager, TelephonyListenerManager telephonyListenerManager, WifiManager wifiManager, SubscriptionManager subscriptionManager, MobileMappings.Config config, Looper looper, Executor executor, CallbackHandler callbackHandler, AccessPointControllerImpl accessPointControllerImpl, DataUsageController dataUsageController, MobileStatusTracker.SubscriptionDefaults subscriptionDefaults, DeviceProvisionedController deviceProvisionedController, BroadcastDispatcher broadcastDispatcher, DemoModeController demoModeController, CarrierConfigTracker carrierConfigTracker, WifiStatusTrackerFactory wifiStatusTrackerFactory, Handler handler, FeatureFlags featureFlags, DumpManager dumpManager) {
        Handler handler2;
        Context context2 = context;
        WifiManager wifiManager2 = wifiManager;
        Looper looper2 = looper;
        CallbackHandler callbackHandler2 = callbackHandler;
        DataUsageController dataUsageController2 = dataUsageController;
        final DeviceProvisionedController deviceProvisionedController2 = deviceProvisionedController;
        BroadcastDispatcher broadcastDispatcher2 = broadcastDispatcher;
        this.mLock = new Object();
        this.mActiveMobileDataSubscription = -1;
        this.mMobileSignalControllers = new SparseArray<>();
        this.mConnectedTransports = new BitSet();
        this.mValidatedTransports = new BitSet();
        this.mAirplaneMode = false;
        this.mNoDefaultNetwork = false;
        this.mNoNetworksAvailable = true;
        this.mLocale = null;
        this.mCurrentSubscriptions = new ArrayList();
        this.mHistory = new String[16];
        this.mConfigurationListener = new ConfigurationController.ConfigurationListener() {
            public void onConfigChanged(Configuration configuration) {
                NetworkControllerImpl networkControllerImpl = NetworkControllerImpl.this;
                networkControllerImpl.mConfig = MobileMappings.Config.readConfig(networkControllerImpl.mContext);
                NetworkControllerImpl.this.mReceiverHandler.post(new NetworkControllerImpl$1$$ExternalSyntheticLambda0(this));
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$onConfigChanged$0() {
                NetworkControllerImpl.this.handleConfigurationChanged();
            }
        };
        this.mClearForceValidated = new NetworkControllerImpl$$ExternalSyntheticLambda6(this);
        this.mRegisterListeners = new NetworkControllerImpl$$ExternalSyntheticLambda7(this);
        this.mContext = context2;
        this.mTelephonyListenerManager = telephonyListenerManager;
        this.mConfig = config;
        this.mMainHandler = handler;
        Handler handler3 = new Handler(looper2);
        this.mReceiverHandler = handler3;
        this.mBgLooper = looper2;
        this.mBgExecutor = executor;
        this.mCallbackHandler = callbackHandler2;
        this.mDataSaverController = new DataSaverControllerImpl(context2);
        this.mBroadcastDispatcher = broadcastDispatcher2;
        this.mSubscriptionManager = subscriptionManager;
        this.mSubDefaults = subscriptionDefaults;
        this.mConnectivityManager = connectivityManager;
        boolean isDataCapable = telephonyManager.isDataCapable();
        this.mHasMobileDataFeature = isDataCapable;
        this.mDemoModeController = demoModeController;
        this.mCarrierConfigTracker = carrierConfigTracker;
        this.mFeatureFlags = featureFlags;
        this.mDumpManager = dumpManager;
        this.mPhone = telephonyManager;
        this.mWifiManager = wifiManager2;
        this.mLocale = context.getResources().getConfiguration().locale;
        this.mAccessPoints = accessPointControllerImpl;
        this.mDataUsageController = dataUsageController2;
        dataUsageController2.setNetworkController(this);
        dataUsageController2.setCallback(new DataUsageController.Callback() {
            public void onMobileDataEnabled(boolean z) {
                NetworkControllerImpl.this.mCallbackHandler.setMobileDataEnabled(z);
                NetworkControllerImpl.this.notifyControllersMobileDataChanged();
            }
        });
        WifiSignalController wifiSignalController = r0;
        Handler handler4 = handler3;
        WifiSignalController wifiSignalController2 = new WifiSignalController(context, isDataCapable, callbackHandler, this, wifiManager, wifiStatusTrackerFactory, handler4);
        this.mWifiSignalController = wifiSignalController;
        this.mEthernetSignalController = new EthernetSignalController(context2, callbackHandler2, this);
        updateAirplaneMode(true);
        AnonymousClass3 r0 = new CurrentUserTracker(broadcastDispatcher2) {
            public void onUserSwitched(int i) {
                NetworkControllerImpl.this.onUserSwitched(i);
            }
        };
        this.mUserTracker = r0;
        r0.startTracking();
        deviceProvisionedController2.addCallback(new DeviceProvisionedController.DeviceProvisionedListener() {
            public void onUserSetupChanged() {
                NetworkControllerImpl.this.setUserSetupComplete(deviceProvisionedController2.isCurrentUserSetup());
            }
        });
        setUserSetupComplete(deviceProvisionedController.isCurrentUserSetup());
        AnonymousClass5 r02 = new WifiManager.ScanResultsCallback() {
            public void onScanResultsAvailable() {
                NetworkControllerImpl.this.mNoNetworksAvailable = true;
                Iterator<ScanResult> it = NetworkControllerImpl.this.mWifiManager.getScanResults().iterator();
                while (true) {
                    if (it.hasNext()) {
                        if (!it.next().SSID.equals(((WifiState) NetworkControllerImpl.this.mWifiSignalController.getState()).ssid)) {
                            NetworkControllerImpl.this.mNoNetworksAvailable = false;
                            break;
                        }
                    } else {
                        break;
                    }
                }
                if (NetworkControllerImpl.this.mNoDefaultNetwork) {
                    NetworkControllerImpl.this.mCallbackHandler.setConnectivityStatus(NetworkControllerImpl.this.mNoDefaultNetwork, true ^ NetworkControllerImpl.this.mInetCondition, NetworkControllerImpl.this.mNoNetworksAvailable);
                }
            }
        };
        if (wifiManager2 != null) {
            Objects.requireNonNull(handler4);
            handler2 = handler4;
            wifiManager2.registerScanResultsCallback(new MediaRoute2Provider$$ExternalSyntheticLambda0(handler2), r02);
        } else {
            handler2 = handler4;
        }
        this.mFiveGServiceClient = FiveGServiceClient.getInstance(context);
        connectivityManager.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback(1) {
            public Network mLastNetwork;
            public NetworkCapabilities mLastNetworkCapabilities;

            public void onLost(Network network) {
                this.mLastNetwork = null;
                this.mLastNetworkCapabilities = null;
                NetworkControllerImpl.this.mLastDefaultNetworkCapabilities = null;
                NetworkControllerImpl.this.recordLastNetworkCallback(NetworkControllerImpl.SSDF.format(Long.valueOf(System.currentTimeMillis())) + "," + "onLost: " + "network=" + network);
                NetworkControllerImpl.this.updateConnectivity();
            }

            public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
                NetworkCapabilities networkCapabilities2 = this.mLastNetworkCapabilities;
                boolean z = networkCapabilities2 != null && networkCapabilities2.hasCapability(16);
                boolean hasCapability = networkCapabilities.hasCapability(16);
                if (network.equals(this.mLastNetwork) && hasCapability == z) {
                    int[] r0 = NetworkControllerImpl.this.getProcessedTransportTypes(networkCapabilities);
                    Arrays.sort(r0);
                    NetworkCapabilities networkCapabilities3 = this.mLastNetworkCapabilities;
                    int[] r1 = networkCapabilities3 != null ? NetworkControllerImpl.this.getProcessedTransportTypes(networkCapabilities3) : null;
                    if (r1 != null) {
                        Arrays.sort(r1);
                    }
                    if (Arrays.equals(r0, r1)) {
                        return;
                    }
                }
                this.mLastNetwork = network;
                this.mLastNetworkCapabilities = networkCapabilities;
                NetworkControllerImpl.this.mLastDefaultNetworkCapabilities = networkCapabilities;
                NetworkControllerImpl.this.recordLastNetworkCallback(NetworkControllerImpl.SSDF.format(Long.valueOf(System.currentTimeMillis())) + "," + "onCapabilitiesChanged: " + "network=" + network + "," + "networkCapabilities=" + networkCapabilities);
                NetworkControllerImpl.this.updateConnectivity();
            }
        }, handler2);
        this.mPhoneStateListener = new NetworkControllerImpl$$ExternalSyntheticLambda8(this);
        demoModeController.addCallback((DemoMode) this);
        this.mProviderModelBehavior = featureFlags.isEnabled(Flags.COMBINED_STATUS_BAR_SIGNAL_ICONS);
        dumpManager.registerDumpable("NetworkController", this);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(int i) {
        this.mBgExecutor.execute(new NetworkControllerImpl$$ExternalSyntheticLambda9(this, i));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(int i) {
        if (keepCellularValidationBitInSwitch(this.mActiveMobileDataSubscription, i)) {
            if (DEBUG) {
                Log.d("NetworkController", ": mForceCellularValidated to true.");
            }
            this.mForceCellularValidated = true;
            this.mReceiverHandler.removeCallbacks(this.mClearForceValidated);
            this.mReceiverHandler.postDelayed(this.mClearForceValidated, 2000);
        }
        this.mActiveMobileDataSubscription = i;
        doUpdateMobileControllers();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2() {
        if (DEBUG) {
            Log.d("NetworkController", ": mClearForceValidated");
        }
        this.mForceCellularValidated = false;
        updateConnectivity();
    }

    public boolean isInGroupDataSwitch(int i, int i2) {
        SubscriptionInfo activeSubscriptionInfo = this.mSubscriptionManager.getActiveSubscriptionInfo(i);
        SubscriptionInfo activeSubscriptionInfo2 = this.mSubscriptionManager.getActiveSubscriptionInfo(i2);
        return (activeSubscriptionInfo == null || activeSubscriptionInfo2 == null || activeSubscriptionInfo.getGroupUuid() == null || !activeSubscriptionInfo.getGroupUuid().equals(activeSubscriptionInfo2.getGroupUuid())) ? false : true;
    }

    public boolean keepCellularValidationBitInSwitch(int i, int i2) {
        if (!this.mValidatedTransports.get(0) || !isInGroupDataSwitch(i, i2)) {
            return false;
        }
        return true;
    }

    public DataSaverController getDataSaverController() {
        return this.mDataSaverController;
    }

    @VisibleForTesting
    /* renamed from: registerListeners */
    public void lambda$new$6() {
        for (int i = 0; i < this.mMobileSignalControllers.size(); i++) {
            MobileSignalController valueAt = this.mMobileSignalControllers.valueAt(i);
            valueAt.registerListener();
            valueAt.registerFiveGStateListener(this.mFiveGServiceClient);
        }
        if (this.mSubscriptionListener == null) {
            this.mSubscriptionListener = new SubListener(this.mBgLooper);
        }
        this.mSubscriptionManager.addOnSubscriptionsChangedListener(this.mSubscriptionListener);
        this.mTelephonyListenerManager.addActiveDataSubscriptionIdListener(this.mPhoneStateListener);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
        intentFilter.addAction("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED");
        intentFilter.addAction("android.intent.action.ACTION_DEFAULT_VOICE_SUBSCRIPTION_CHANGED");
        intentFilter.addAction("android.intent.action.SERVICE_STATE");
        intentFilter.addAction("android.telephony.action.SERVICE_PROVIDERS_UPDATED");
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction("android.intent.action.AIRPLANE_MODE");
        intentFilter.addAction("android.telephony.action.CARRIER_CONFIG_CHANGED");
        intentFilter.addAction("android.settings.panel.action.INTERNET_CONNECTIVITY");
        this.mBroadcastDispatcher.registerReceiverWithHandler(this, intentFilter, this.mReceiverHandler);
        this.mListening = true;
        this.mReceiverHandler.post(new NetworkControllerImpl$$ExternalSyntheticLambda0(this));
        Handler handler = this.mReceiverHandler;
        WifiSignalController wifiSignalController = this.mWifiSignalController;
        Objects.requireNonNull(wifiSignalController);
        handler.post(new NetworkControllerImpl$$ExternalSyntheticLambda1(wifiSignalController));
        this.mReceiverHandler.post(new NetworkControllerImpl$$ExternalSyntheticLambda2(this));
        updateMobileControllers();
        this.mReceiverHandler.post(new NetworkControllerImpl$$ExternalSyntheticLambda3(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$registerListeners$3() {
        if (this.mLastServiceState == null) {
            this.mLastServiceState = this.mPhone.getServiceState();
            if (this.mMobileSignalControllers.size() == 0) {
                recalculateEmergency();
            }
        }
    }

    public final void unregisterListeners() {
        this.mListening = false;
        for (int i = 0; i < this.mMobileSignalControllers.size(); i++) {
            MobileSignalController valueAt = this.mMobileSignalControllers.valueAt(i);
            valueAt.unregisterListener();
            valueAt.unregisterFiveGStateListener(this.mFiveGServiceClient);
        }
        this.mSubscriptionManager.removeOnSubscriptionsChangedListener(this.mSubscriptionListener);
        this.mBroadcastDispatcher.unregisterReceiver(this);
    }

    @VisibleForTesting
    public FiveGServiceClient getFiveGServiceClient() {
        return this.mFiveGServiceClient;
    }

    public DataUsageController getMobileDataController() {
        return this.mDataUsageController;
    }

    public boolean hasMobileDataFeature() {
        return this.mHasMobileDataFeature;
    }

    public boolean hasVoiceCallingFeature() {
        return this.mPhone.getPhoneType() != 0;
    }

    public final int[] getProcessedTransportTypes(NetworkCapabilities networkCapabilities) {
        int[] transportTypes = networkCapabilities.getTransportTypes();
        int i = 0;
        while (true) {
            if (i < transportTypes.length) {
                if (transportTypes[i] == 0 && Utils.tryGetWifiInfoForVcn(networkCapabilities) != null) {
                    transportTypes[i] = 1;
                    break;
                }
                i++;
            } else {
                break;
            }
        }
        return transportTypes;
    }

    public final MobileSignalController getDataController() {
        return getControllerWithSubId(this.mSubDefaults.getActiveDataSubId());
    }

    public final MobileSignalController getControllerWithSubId(int i) {
        if (!SubscriptionManager.isValidSubscriptionId(i)) {
            if (DEBUG) {
                Log.e("NetworkController", "No data sim selected");
            }
            return this.mDefaultSignalController;
        } else if (this.mMobileSignalControllers.indexOfKey(i) >= 0) {
            return this.mMobileSignalControllers.get(i);
        } else {
            if (DEBUG) {
                Log.e("NetworkController", "Cannot find controller for data sub: " + i);
            }
            return this.mDefaultSignalController;
        }
    }

    public String getMobileDataNetworkName() {
        MobileSignalController dataController = getDataController();
        return dataController != null ? ((MobileState) dataController.getState()).networkNameData : "";
    }

    public boolean isMobileDataNetworkInService() {
        MobileSignalController dataController = getDataController();
        return dataController != null && dataController.isInService();
    }

    public int getNumberSubscriptions() {
        return this.mMobileSignalControllers.size();
    }

    public boolean isDataControllerDisabled() {
        MobileSignalController dataController = getDataController();
        if (dataController == null) {
            return false;
        }
        return dataController.isDataDisabled();
    }

    public boolean isCarrierMergedWifi(int i) {
        return this.mWifiSignalController.isCarrierMergedWifi(i);
    }

    public boolean hasDefaultNetwork() {
        return !this.mNoDefaultNetwork;
    }

    public boolean isEthernetDefault() {
        return this.mConnectedTransports.get(3);
    }

    public String getNetworkNameForCarrierWiFi(int i) {
        MobileSignalController controllerWithSubId = getControllerWithSubId(i);
        return controllerWithSubId != null ? controllerWithSubId.getNetworkNameForCarrierWiFi() : "";
    }

    public void notifyWifiLevelChange(int i) {
        for (int i2 = 0; i2 < this.mMobileSignalControllers.size(); i2++) {
            this.mMobileSignalControllers.valueAt(i2).notifyWifiLevelChange(i);
        }
    }

    public void notifyDefaultMobileLevelChange(int i) {
        for (int i2 = 0; i2 < this.mMobileSignalControllers.size(); i2++) {
            this.mMobileSignalControllers.valueAt(i2).notifyDefaultMobileLevelChange(i);
        }
    }

    public final void notifyControllersMobileDataChanged() {
        for (int i = 0; i < this.mMobileSignalControllers.size(); i++) {
            this.mMobileSignalControllers.valueAt(i).onMobileDataChanged();
        }
    }

    public boolean isEmergencyOnly() {
        if (this.mMobileSignalControllers.size() == 0) {
            this.mEmergencySource = 0;
            ServiceState serviceState = this.mLastServiceState;
            if (serviceState == null || !serviceState.isEmergencyOnly()) {
                return false;
            }
            return true;
        }
        int defaultVoiceSubId = this.mSubDefaults.getDefaultVoiceSubId();
        if (!SubscriptionManager.isValidSubscriptionId(defaultVoiceSubId)) {
            for (int i = 0; i < this.mMobileSignalControllers.size(); i++) {
                MobileSignalController valueAt = this.mMobileSignalControllers.valueAt(i);
                if (!((MobileState) valueAt.getState()).isEmergency) {
                    this.mEmergencySource = valueAt.mSubscriptionInfo.getSubscriptionId() + 100;
                    if (DEBUG) {
                        Log.d("NetworkController", "Found emergency " + valueAt.mTag);
                    }
                    return false;
                }
            }
        }
        if (this.mMobileSignalControllers.indexOfKey(defaultVoiceSubId) >= 0) {
            this.mEmergencySource = defaultVoiceSubId + 200;
            if (DEBUG) {
                Log.d("NetworkController", "Getting emergency from " + defaultVoiceSubId);
            }
            return ((MobileState) this.mMobileSignalControllers.get(defaultVoiceSubId).getState()).isEmergency;
        } else if (this.mMobileSignalControllers.size() == 1) {
            this.mEmergencySource = this.mMobileSignalControllers.keyAt(0) + 400;
            if (DEBUG) {
                Log.d("NetworkController", "Getting assumed emergency from " + this.mMobileSignalControllers.keyAt(0));
            }
            return ((MobileState) this.mMobileSignalControllers.valueAt(0).getState()).isEmergency;
        } else {
            if (DEBUG) {
                Log.e("NetworkController", "Cannot find controller for voice sub: " + defaultVoiceSubId);
            }
            this.mEmergencySource = defaultVoiceSubId + 300;
            return true;
        }
    }

    public void recalculateEmergency() {
        boolean isEmergencyOnly = isEmergencyOnly();
        this.mIsEmergency = isEmergencyOnly;
        this.mCallbackHandler.setEmergencyCallsOnly(isEmergencyOnly);
    }

    public void addCallback(SignalCallback signalCallback) {
        signalCallback.setSubs(this.mCurrentSubscriptions);
        signalCallback.setIsAirplaneMode(new IconState(this.mAirplaneMode, TelephonyIcons.FLIGHT_MODE_ICON, this.mContext.getString(R$string.accessibility_airplane_mode)));
        signalCallback.setNoSims(this.mHasNoSubs, this.mSimDetected);
        signalCallback.setConnectivityStatus(this.mNoDefaultNetwork, !this.mInetCondition, this.mNoNetworksAvailable);
        this.mWifiSignalController.notifyListeners(signalCallback);
        this.mEthernetSignalController.notifyListeners(signalCallback);
        for (int i = 0; i < this.mMobileSignalControllers.size(); i++) {
            MobileSignalController valueAt = this.mMobileSignalControllers.valueAt(i);
            valueAt.notifyListeners(signalCallback);
            if (this.mProviderModelBehavior) {
                valueAt.refreshCallIndicator(signalCallback);
            }
        }
        this.mCallbackHandler.setListening(signalCallback, true);
    }

    public void removeCallback(SignalCallback signalCallback) {
        this.mCallbackHandler.setListening(signalCallback, false);
    }

    public void setWifiEnabled(final boolean z) {
        new AsyncTask<Void, Void, Void>() {
            public Void doInBackground(Void... voidArr) {
                NetworkControllerImpl.this.mWifiManager.setWifiEnabled(z);
                return null;
            }
        }.execute(new Void[0]);
    }

    public final void onUserSwitched(int i) {
        this.mCurrentUserId = i;
        this.mAccessPoints.onUserSwitched(i);
        updateConnectivity();
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onReceive(android.content.Context r4, android.content.Intent r5) {
        /*
            r3 = this;
            boolean r4 = CHATTY
            if (r4 == 0) goto L_0x001a
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r0 = "onReceive: intent="
            r4.append(r0)
            r4.append(r5)
            java.lang.String r4 = r4.toString()
            java.lang.String r0 = "NetworkController"
            android.util.Log.d(r0, r4)
        L_0x001a:
            java.lang.String r4 = r5.getAction()
            r4.hashCode()
            int r0 = r4.hashCode()
            r1 = -1
            r2 = 0
            switch(r0) {
                case -2104353374: goto L_0x007a;
                case -1465084191: goto L_0x006f;
                case -1172645946: goto L_0x0064;
                case -1138588223: goto L_0x0059;
                case -1076576821: goto L_0x004e;
                case -229777127: goto L_0x0043;
                case -25388475: goto L_0x0038;
                case 464243859: goto L_0x002d;
                default: goto L_0x002a;
            }
        L_0x002a:
            r4 = r1
            goto L_0x0084
        L_0x002d:
            java.lang.String r0 = "android.settings.panel.action.INTERNET_CONNECTIVITY"
            boolean r4 = r4.equals(r0)
            if (r4 != 0) goto L_0x0036
            goto L_0x002a
        L_0x0036:
            r4 = 7
            goto L_0x0084
        L_0x0038:
            java.lang.String r0 = "android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED"
            boolean r4 = r4.equals(r0)
            if (r4 != 0) goto L_0x0041
            goto L_0x002a
        L_0x0041:
            r4 = 6
            goto L_0x0084
        L_0x0043:
            java.lang.String r0 = "android.intent.action.SIM_STATE_CHANGED"
            boolean r4 = r4.equals(r0)
            if (r4 != 0) goto L_0x004c
            goto L_0x002a
        L_0x004c:
            r4 = 5
            goto L_0x0084
        L_0x004e:
            java.lang.String r0 = "android.intent.action.AIRPLANE_MODE"
            boolean r4 = r4.equals(r0)
            if (r4 != 0) goto L_0x0057
            goto L_0x002a
        L_0x0057:
            r4 = 4
            goto L_0x0084
        L_0x0059:
            java.lang.String r0 = "android.telephony.action.CARRIER_CONFIG_CHANGED"
            boolean r4 = r4.equals(r0)
            if (r4 != 0) goto L_0x0062
            goto L_0x002a
        L_0x0062:
            r4 = 3
            goto L_0x0084
        L_0x0064:
            java.lang.String r0 = "android.net.conn.CONNECTIVITY_CHANGE"
            boolean r4 = r4.equals(r0)
            if (r4 != 0) goto L_0x006d
            goto L_0x002a
        L_0x006d:
            r4 = 2
            goto L_0x0084
        L_0x006f:
            java.lang.String r0 = "android.intent.action.ACTION_DEFAULT_VOICE_SUBSCRIPTION_CHANGED"
            boolean r4 = r4.equals(r0)
            if (r4 != 0) goto L_0x0078
            goto L_0x002a
        L_0x0078:
            r4 = 1
            goto L_0x0084
        L_0x007a:
            java.lang.String r0 = "android.intent.action.SERVICE_STATE"
            boolean r4 = r4.equals(r0)
            if (r4 != 0) goto L_0x0083
            goto L_0x002a
        L_0x0083:
            r4 = r2
        L_0x0084:
            switch(r4) {
                case 0: goto L_0x0118;
                case 1: goto L_0x0114;
                case 2: goto L_0x0110;
                case 3: goto L_0x00fd;
                case 4: goto L_0x00f6;
                case 5: goto L_0x00e9;
                case 6: goto L_0x00c0;
                case 7: goto L_0x00b4;
                default: goto L_0x0087;
            }
        L_0x0087:
            java.lang.String r4 = "android.telephony.extra.SUBSCRIPTION_INDEX"
            int r4 = r5.getIntExtra(r4, r1)
            boolean r0 = android.telephony.SubscriptionManager.isValidSubscriptionId(r4)
            if (r0 == 0) goto L_0x00ad
            android.util.SparseArray<com.android.systemui.statusbar.connectivity.MobileSignalController> r0 = r3.mMobileSignalControllers
            int r0 = r0.indexOfKey(r4)
            if (r0 < 0) goto L_0x00a8
            android.util.SparseArray<com.android.systemui.statusbar.connectivity.MobileSignalController> r3 = r3.mMobileSignalControllers
            java.lang.Object r3 = r3.get(r4)
            com.android.systemui.statusbar.connectivity.MobileSignalController r3 = (com.android.systemui.statusbar.connectivity.MobileSignalController) r3
            r3.handleBroadcast(r5)
            goto L_0x012d
        L_0x00a8:
            r3.updateMobileControllers()
            goto L_0x012d
        L_0x00ad:
            com.android.systemui.statusbar.connectivity.WifiSignalController r3 = r3.mWifiSignalController
            r3.handleBroadcast(r5)
            goto L_0x012d
        L_0x00b4:
            android.os.Handler r4 = r3.mMainHandler
            com.android.systemui.statusbar.connectivity.NetworkControllerImpl$$ExternalSyntheticLambda5 r5 = new com.android.systemui.statusbar.connectivity.NetworkControllerImpl$$ExternalSyntheticLambda5
            r5.<init>(r3)
            r4.post(r5)
            goto L_0x012d
        L_0x00c0:
            android.util.SparseArray<com.android.systemui.statusbar.connectivity.MobileSignalController> r4 = r3.mMobileSignalControllers
            int r4 = r4.size()
            if (r2 >= r4) goto L_0x00d6
            android.util.SparseArray<com.android.systemui.statusbar.connectivity.MobileSignalController> r4 = r3.mMobileSignalControllers
            java.lang.Object r4 = r4.valueAt(r2)
            com.android.systemui.statusbar.connectivity.MobileSignalController r4 = (com.android.systemui.statusbar.connectivity.MobileSignalController) r4
            r4.handleBroadcast(r5)
            int r2 = r2 + 1
            goto L_0x00c0
        L_0x00d6:
            android.content.Context r4 = r3.mContext
            com.android.settingslib.mobile.MobileMappings$Config r4 = com.android.settingslib.mobile.MobileMappings.Config.readConfig(r4)
            r3.mConfig = r4
            android.os.Handler r4 = r3.mReceiverHandler
            com.android.systemui.statusbar.connectivity.NetworkControllerImpl$$ExternalSyntheticLambda4 r5 = new com.android.systemui.statusbar.connectivity.NetworkControllerImpl$$ExternalSyntheticLambda4
            r5.<init>(r3)
            r4.post(r5)
            goto L_0x012d
        L_0x00e9:
            java.lang.String r4 = "rebroadcastOnUnlock"
            boolean r4 = r5.getBooleanExtra(r4, r2)
            if (r4 == 0) goto L_0x00f2
            goto L_0x012d
        L_0x00f2:
            r3.updateMobileControllers()
            goto L_0x012d
        L_0x00f6:
            r3.refreshLocale()
            r3.updateAirplaneMode(r2)
            goto L_0x012d
        L_0x00fd:
            android.content.Context r4 = r3.mContext
            com.android.settingslib.mobile.MobileMappings$Config r4 = com.android.settingslib.mobile.MobileMappings.Config.readConfig(r4)
            r3.mConfig = r4
            android.os.Handler r4 = r3.mReceiverHandler
            com.android.systemui.statusbar.connectivity.NetworkControllerImpl$$ExternalSyntheticLambda4 r5 = new com.android.systemui.statusbar.connectivity.NetworkControllerImpl$$ExternalSyntheticLambda4
            r5.<init>(r3)
            r4.post(r5)
            goto L_0x012d
        L_0x0110:
            r3.updateConnectivity()
            goto L_0x012d
        L_0x0114:
            r3.recalculateEmergency()
            goto L_0x012d
        L_0x0118:
            android.os.Bundle r4 = r5.getExtras()
            android.telephony.ServiceState r4 = android.telephony.ServiceState.newFromBundle(r4)
            r3.mLastServiceState = r4
            android.util.SparseArray<com.android.systemui.statusbar.connectivity.MobileSignalController> r4 = r3.mMobileSignalControllers
            int r4 = r4.size()
            if (r4 != 0) goto L_0x012d
            r3.recalculateEmergency()
        L_0x012d:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.connectivity.NetworkControllerImpl.onReceive(android.content.Context, android.content.Intent):void");
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onReceive$4() {
        this.mInternetDialogFactory.create(true, this.mAccessPoints.canConfigMobileData(), this.mAccessPoints.canConfigWifi(), (View) null);
    }

    @VisibleForTesting
    public void handleConfigurationChanged() {
        updateMobileControllers();
        for (int i = 0; i < this.mMobileSignalControllers.size(); i++) {
            MobileSignalController valueAt = this.mMobileSignalControllers.valueAt(i);
            valueAt.setConfiguration(this.mConfig);
            if (this.mProviderModelBehavior) {
                valueAt.refreshCallIndicator(this.mCallbackHandler);
            }
        }
        refreshLocale();
    }

    public final void updateMobileControllers() {
        if (this.mListening) {
            doUpdateMobileControllers();
        }
    }

    public final void filterMobileSubscriptionInSameGroup(List<SubscriptionInfo> list) {
        if (list.size() == 2) {
            SubscriptionInfo subscriptionInfo = list.get(0);
            SubscriptionInfo subscriptionInfo2 = list.get(1);
            if (subscriptionInfo.getGroupUuid() != null && subscriptionInfo.getGroupUuid().equals(subscriptionInfo2.getGroupUuid())) {
                if (!subscriptionInfo.isOpportunistic() && !subscriptionInfo2.isOpportunistic()) {
                    return;
                }
                if (CarrierConfigManager.getDefaultConfig().getBoolean("always_show_primary_signal_bar_in_opportunistic_network_boolean")) {
                    if (!subscriptionInfo.isOpportunistic()) {
                        subscriptionInfo = subscriptionInfo2;
                    }
                    list.remove(subscriptionInfo);
                    return;
                }
                if (subscriptionInfo.getSubscriptionId() == this.mActiveMobileDataSubscription) {
                    subscriptionInfo = subscriptionInfo2;
                }
                list.remove(subscriptionInfo);
            }
        }
    }

    @VisibleForTesting
    public void doUpdateMobileControllers() {
        List completeActiveSubscriptionInfoList = this.mSubscriptionManager.getCompleteActiveSubscriptionInfoList();
        if (completeActiveSubscriptionInfoList == null) {
            completeActiveSubscriptionInfoList = Collections.emptyList();
        }
        filterMobileSubscriptionInSameGroup(completeActiveSubscriptionInfoList);
        if (hasCorrectMobileControllers(completeActiveSubscriptionInfoList)) {
            updateNoSims();
            return;
        }
        synchronized (this.mLock) {
            setCurrentSubscriptionsLocked(completeActiveSubscriptionInfoList);
        }
        updateNoSims();
        recalculateEmergency();
    }

    @VisibleForTesting
    public void updateNoSims() {
        boolean z = this.mHasMobileDataFeature && this.mMobileSignalControllers.size() == 0;
        boolean hasAnySim = hasAnySim();
        if (z != this.mHasNoSubs || hasAnySim != this.mSimDetected) {
            this.mHasNoSubs = z;
            this.mSimDetected = hasAnySim;
            this.mCallbackHandler.setNoSims(z, hasAnySim);
        }
    }

    public final boolean hasAnySim() {
        int activeModemCount = this.mPhone.getActiveModemCount();
        for (int i = 0; i < activeModemCount; i++) {
            int simState = this.mPhone.getSimState(i);
            if (simState != 1 && simState != 0) {
                return true;
            }
        }
        return false;
    }

    @GuardedBy({"mLock"})
    @VisibleForTesting
    public void setCurrentSubscriptionsLocked(List<SubscriptionInfo> list) {
        SparseArray sparseArray;
        int i;
        int i2;
        List<SubscriptionInfo> list2;
        List<SubscriptionInfo> list3 = list;
        Collections.sort(list3, new Comparator<SubscriptionInfo>() {
            public int compare(SubscriptionInfo subscriptionInfo, SubscriptionInfo subscriptionInfo2) {
                int i;
                int i2;
                if (subscriptionInfo.getSimSlotIndex() == subscriptionInfo2.getSimSlotIndex()) {
                    i2 = subscriptionInfo.getSubscriptionId();
                    i = subscriptionInfo2.getSubscriptionId();
                } else {
                    i2 = subscriptionInfo.getSimSlotIndex();
                    i = subscriptionInfo2.getSimSlotIndex();
                }
                return i2 - i;
            }
        });
        this.mCurrentSubscriptions = list3;
        SparseArray sparseArray2 = new SparseArray();
        for (int i3 = 0; i3 < this.mMobileSignalControllers.size(); i3++) {
            sparseArray2.put(this.mMobileSignalControllers.keyAt(i3), this.mMobileSignalControllers.valueAt(i3));
        }
        this.mMobileSignalControllers.clear();
        int size = list.size();
        int i4 = 0;
        while (i4 < size) {
            int subscriptionId = list3.get(i4).getSubscriptionId();
            if (sparseArray2.indexOfKey(subscriptionId) >= 0) {
                this.mMobileSignalControllers.put(subscriptionId, (MobileSignalController) sparseArray2.get(subscriptionId));
                sparseArray2.remove(subscriptionId);
                i2 = i4;
                i = size;
                list2 = list3;
                sparseArray = sparseArray2;
            } else {
                MobileStatusTracker.SubscriptionDefaults subscriptionDefaults = this.mSubDefaults;
                MobileStatusTracker.SubscriptionDefaults subscriptionDefaults2 = subscriptionDefaults;
                sparseArray = sparseArray2;
                MobileSignalController mobileSignalController = r0;
                i = size;
                MobileSignalController mobileSignalController2 = new MobileSignalController(this.mContext, this.mConfig, this.mHasMobileDataFeature, this.mPhone.createForSubscriptionId(subscriptionId), this.mCallbackHandler, this, list3.get(i4), subscriptionDefaults2, this.mReceiverHandler.getLooper(), this.mCarrierConfigTracker, this.mFeatureFlags);
                mobileSignalController.setUserSetupComplete(this.mUserSetup);
                this.mMobileSignalControllers.put(subscriptionId, mobileSignalController);
                list2 = list;
                i2 = i4;
                if (list2.get(i2).getSimSlotIndex() == 0) {
                    this.mDefaultSignalController = mobileSignalController;
                }
                if (this.mListening) {
                    mobileSignalController.registerListener();
                    mobileSignalController.registerFiveGStateListener(this.mFiveGServiceClient);
                }
            }
            i4 = i2 + 1;
            list3 = list2;
            size = i;
            sparseArray2 = sparseArray;
        }
        List<SubscriptionInfo> list4 = list3;
        SparseArray sparseArray3 = sparseArray2;
        if (this.mListening) {
            int i5 = 0;
            while (i5 < sparseArray3.size()) {
                SparseArray sparseArray4 = sparseArray3;
                int keyAt = sparseArray4.keyAt(i5);
                if (sparseArray4.get(keyAt) == this.mDefaultSignalController) {
                    this.mDefaultSignalController = null;
                }
                ((MobileSignalController) sparseArray4.get(keyAt)).unregisterListener();
                ((MobileSignalController) sparseArray4.get(keyAt)).unregisterFiveGStateListener(this.mFiveGServiceClient);
                i5++;
                sparseArray3 = sparseArray4;
            }
        }
        this.mCallbackHandler.setSubs(list4);
        notifyAllListeners();
        pushConnectivityToSignals();
        updateAirplaneMode(true);
    }

    public final void setUserSetupComplete(boolean z) {
        this.mReceiverHandler.post(new NetworkControllerImpl$$ExternalSyntheticLambda10(this, z));
    }

    /* renamed from: handleSetUserSetupComplete */
    public final void lambda$setUserSetupComplete$5(boolean z) {
        this.mUserSetup = z;
        for (int i = 0; i < this.mMobileSignalControllers.size(); i++) {
            this.mMobileSignalControllers.valueAt(i).setUserSetupComplete(this.mUserSetup);
        }
    }

    @VisibleForTesting
    public boolean isUserSetup() {
        return this.mUserSetup;
    }

    @VisibleForTesting
    public boolean hasCorrectMobileControllers(List<SubscriptionInfo> list) {
        if (list.size() != this.mMobileSignalControllers.size()) {
            return false;
        }
        for (SubscriptionInfo subscriptionId : list) {
            if (this.mMobileSignalControllers.indexOfKey(subscriptionId.getSubscriptionId()) < 0) {
                return false;
            }
        }
        return true;
    }

    @VisibleForTesting
    public void setNoNetworksAvailable(boolean z) {
        this.mNoNetworksAvailable = z;
    }

    public final boolean isFTModeAndSleepTest() {
        return SystemProperties.get("ro.boot.ftmode", "false").equals("true") && SystemProperties.get("sys.sleep.test.ready", "false").equals("true");
    }

    public final void updateAirplaneMode(boolean z) {
        boolean z2 = true;
        if (Settings.Global.getInt(this.mContext.getContentResolver(), "airplane_mode_on", 0) != 1 && !isFTModeAndSleepTest()) {
            z2 = false;
        }
        if (z2 != this.mAirplaneMode || z) {
            this.mAirplaneMode = z2;
            for (int i = 0; i < this.mMobileSignalControllers.size(); i++) {
                this.mMobileSignalControllers.valueAt(i).setAirplaneMode(this.mAirplaneMode);
            }
            notifyListeners();
        }
    }

    public final void refreshLocale() {
        Locale locale = this.mContext.getResources().getConfiguration().locale;
        if (!locale.equals(this.mLocale)) {
            this.mLocale = locale;
            this.mWifiSignalController.refreshLocale();
            notifyAllListeners();
        }
    }

    public final void notifyAllListeners() {
        notifyListeners();
        for (int i = 0; i < this.mMobileSignalControllers.size(); i++) {
            this.mMobileSignalControllers.valueAt(i).notifyListeners();
        }
        this.mWifiSignalController.notifyListeners();
        this.mEthernetSignalController.notifyListeners();
    }

    public final void notifyListeners() {
        this.mCallbackHandler.setIsAirplaneMode(new IconState(this.mAirplaneMode, TelephonyIcons.FLIGHT_MODE_ICON, this.mContext.getString(R$string.accessibility_airplane_mode)));
        this.mCallbackHandler.setNoSims(this.mHasNoSubs, this.mSimDetected);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v1, resolved type: boolean} */
    /* JADX WARNING: type inference failed for: r2v0 */
    /* JADX WARNING: type inference failed for: r2v2 */
    /* JADX WARNING: type inference failed for: r2v3, types: [int] */
    /* JADX WARNING: type inference failed for: r2v5 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void updateConnectivity() {
        /*
            r9 = this;
            java.util.BitSet r0 = r9.mConnectedTransports
            r0.clear()
            java.util.BitSet r0 = r9.mValidatedTransports
            r0.clear()
            android.net.NetworkCapabilities r0 = r9.mLastDefaultNetworkCapabilities
            r1 = 3
            r2 = 0
            r3 = 1
            if (r0 == 0) goto L_0x0056
            int[] r0 = r0.getTransportTypes()
            int r4 = r0.length
            r5 = r2
        L_0x0017:
            if (r5 >= r4) goto L_0x0056
            r6 = r0[r5]
            if (r6 == 0) goto L_0x0022
            if (r6 == r3) goto L_0x0022
            if (r6 == r1) goto L_0x0022
            goto L_0x0053
        L_0x0022:
            r7 = 16
            if (r6 != 0) goto L_0x0041
            android.net.NetworkCapabilities r8 = r9.mLastDefaultNetworkCapabilities
            android.net.wifi.WifiInfo r8 = com.android.settingslib.Utils.tryGetWifiInfoForVcn(r8)
            if (r8 == 0) goto L_0x0041
            java.util.BitSet r6 = r9.mConnectedTransports
            r6.set(r3)
            android.net.NetworkCapabilities r6 = r9.mLastDefaultNetworkCapabilities
            boolean r6 = r6.hasCapability(r7)
            if (r6 == 0) goto L_0x0053
            java.util.BitSet r6 = r9.mValidatedTransports
            r6.set(r3)
            goto L_0x0053
        L_0x0041:
            java.util.BitSet r8 = r9.mConnectedTransports
            r8.set(r6)
            android.net.NetworkCapabilities r8 = r9.mLastDefaultNetworkCapabilities
            boolean r7 = r8.hasCapability(r7)
            if (r7 == 0) goto L_0x0053
            java.util.BitSet r7 = r9.mValidatedTransports
            r7.set(r6)
        L_0x0053:
            int r5 = r5 + 1
            goto L_0x0017
        L_0x0056:
            boolean r0 = r9.mForceCellularValidated
            if (r0 == 0) goto L_0x005f
            java.util.BitSet r0 = r9.mValidatedTransports
            r0.set(r2)
        L_0x005f:
            boolean r0 = CHATTY
            if (r0 == 0) goto L_0x0093
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r4 = "updateConnectivity: mConnectedTransports="
            r0.append(r4)
            java.util.BitSet r4 = r9.mConnectedTransports
            r0.append(r4)
            java.lang.String r0 = r0.toString()
            java.lang.String r4 = "NetworkController"
            android.util.Log.d(r4, r0)
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r5 = "updateConnectivity: mValidatedTransports="
            r0.append(r5)
            java.util.BitSet r5 = r9.mValidatedTransports
            r0.append(r5)
            java.lang.String r0 = r0.toString()
            android.util.Log.d(r4, r0)
        L_0x0093:
            java.util.BitSet r0 = r9.mValidatedTransports
            boolean r0 = r0.get(r2)
            if (r0 != 0) goto L_0x00ae
            java.util.BitSet r0 = r9.mValidatedTransports
            boolean r0 = r0.get(r3)
            if (r0 != 0) goto L_0x00ae
            java.util.BitSet r0 = r9.mValidatedTransports
            boolean r0 = r0.get(r1)
            if (r0 == 0) goto L_0x00ac
            goto L_0x00ae
        L_0x00ac:
            r0 = r2
            goto L_0x00af
        L_0x00ae:
            r0 = r3
        L_0x00af:
            r9.mInetCondition = r0
            r9.pushConnectivityToSignals()
            boolean r0 = r9.mProviderModelBehavior
            if (r0 == 0) goto L_0x00f9
            java.util.BitSet r0 = r9.mConnectedTransports
            boolean r0 = r0.get(r2)
            if (r0 != 0) goto L_0x00d2
            java.util.BitSet r0 = r9.mConnectedTransports
            boolean r0 = r0.get(r3)
            if (r0 != 0) goto L_0x00d2
            java.util.BitSet r0 = r9.mConnectedTransports
            boolean r0 = r0.get(r1)
            if (r0 != 0) goto L_0x00d2
            r0 = r3
            goto L_0x00d3
        L_0x00d2:
            r0 = r2
        L_0x00d3:
            r9.mNoDefaultNetwork = r0
            com.android.systemui.statusbar.connectivity.CallbackHandler r1 = r9.mCallbackHandler
            boolean r4 = r9.mInetCondition
            r3 = r3 ^ r4
            boolean r4 = r9.mNoNetworksAvailable
            r1.setConnectivityStatus(r0, r3, r4)
        L_0x00df:
            android.util.SparseArray<com.android.systemui.statusbar.connectivity.MobileSignalController> r0 = r9.mMobileSignalControllers
            int r0 = r0.size()
            if (r2 >= r0) goto L_0x00f5
            android.util.SparseArray<com.android.systemui.statusbar.connectivity.MobileSignalController> r0 = r9.mMobileSignalControllers
            java.lang.Object r0 = r0.valueAt(r2)
            com.android.systemui.statusbar.connectivity.MobileSignalController r0 = (com.android.systemui.statusbar.connectivity.MobileSignalController) r0
            r0.updateNoCallingState()
            int r2 = r2 + 1
            goto L_0x00df
        L_0x00f5:
            r9.notifyAllListeners()
            goto L_0x011e
        L_0x00f9:
            java.util.BitSet r0 = r9.mConnectedTransports
            boolean r0 = r0.get(r2)
            if (r0 != 0) goto L_0x0112
            java.util.BitSet r0 = r9.mConnectedTransports
            boolean r0 = r0.get(r3)
            if (r0 != 0) goto L_0x0112
            java.util.BitSet r0 = r9.mConnectedTransports
            boolean r0 = r0.get(r1)
            if (r0 != 0) goto L_0x0112
            r2 = r3
        L_0x0112:
            r9.mNoDefaultNetwork = r2
            com.android.systemui.statusbar.connectivity.CallbackHandler r0 = r9.mCallbackHandler
            boolean r1 = r9.mInetCondition
            r1 = r1 ^ r3
            boolean r9 = r9.mNoNetworksAvailable
            r0.setConnectivityStatus(r2, r1, r9)
        L_0x011e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.connectivity.NetworkControllerImpl.updateConnectivity():void");
    }

    public final void pushConnectivityToSignals() {
        for (int i = 0; i < this.mMobileSignalControllers.size(); i++) {
            this.mMobileSignalControllers.valueAt(i).updateConnectivity(this.mConnectedTransports, this.mValidatedTransports);
        }
        this.mWifiSignalController.updateConnectivity(this.mConnectedTransports, this.mValidatedTransports);
        this.mEthernetSignalController.updateConnectivity(this.mConnectedTransports, this.mValidatedTransports);
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("NetworkController state:");
        printWriter.println("  mUserSetup=" + this.mUserSetup);
        printWriter.println("  - telephony ------");
        printWriter.print("  hasVoiceCallingFeature()=");
        printWriter.println(hasVoiceCallingFeature());
        printWriter.println("  mListening=" + this.mListening);
        printWriter.println("  - connectivity ------");
        printWriter.print("  mConnectedTransports=");
        printWriter.println(this.mConnectedTransports);
        printWriter.print("  mValidatedTransports=");
        printWriter.println(this.mValidatedTransports);
        printWriter.print("  mInetCondition=");
        printWriter.println(this.mInetCondition);
        printWriter.print("  mAirplaneMode=");
        printWriter.println(this.mAirplaneMode);
        printWriter.print("  mLocale=");
        printWriter.println(this.mLocale);
        printWriter.print("  mLastServiceState=");
        printWriter.println(this.mLastServiceState);
        printWriter.print("  mIsEmergency=");
        printWriter.println(this.mIsEmergency);
        printWriter.print("  mEmergencySource=");
        printWriter.println(emergencyToString(this.mEmergencySource));
        printWriter.println("  - DefaultNetworkCallback -----");
        int i = 0;
        for (int i2 = 0; i2 < 16; i2++) {
            if (this.mHistory[i2] != null) {
                i++;
            }
        }
        int i3 = this.mHistoryIndex + 16;
        while (true) {
            i3--;
            if (i3 < (this.mHistoryIndex + 16) - i) {
                break;
            }
            printWriter.println("  Previous NetworkCallback(" + ((this.mHistoryIndex + 16) - i3) + "): " + this.mHistory[i3 & 15]);
        }
        printWriter.println("  - config ------");
        for (int i4 = 0; i4 < this.mMobileSignalControllers.size(); i4++) {
            this.mMobileSignalControllers.valueAt(i4).dump(printWriter);
        }
        this.mWifiSignalController.dump(printWriter);
        this.mEthernetSignalController.dump(printWriter);
        this.mAccessPoints.dump(printWriter);
        this.mCallbackHandler.dump(printWriter);
    }

    public static String emergencyToString(int i) {
        if (i > 300) {
            return "ASSUMED_VOICE_CONTROLLER(" + (i - 200) + ")";
        } else if (i > 300) {
            return "NO_SUB(" + (i - 300) + ")";
        } else if (i > 200) {
            return "VOICE_CONTROLLER(" + (i - 200) + ")";
        } else if (i <= 100) {
            return i == 0 ? "NO_CONTROLLERS" : "UNKNOWN_SOURCE";
        } else {
            return "FIRST_CONTROLLER(" + (i - 100) + ")";
        }
    }

    public void onDemoModeStarted() {
        if (DEBUG) {
            Log.d("NetworkController", "Entering demo mode");
        }
        unregisterListeners();
        this.mDemoInetCondition = this.mInetCondition;
        WifiState wifiState = (WifiState) this.mWifiSignalController.getState();
        this.mDemoWifiState = wifiState;
        wifiState.ssid = "DemoMode";
    }

    public void onDemoModeFinished() {
        if (DEBUG) {
            Log.d("NetworkController", "Exiting demo mode");
        }
        updateMobileControllers();
        for (int i = 0; i < this.mMobileSignalControllers.size(); i++) {
            this.mMobileSignalControllers.valueAt(i).resetLastState();
        }
        this.mWifiSignalController.resetLastState();
        this.mReceiverHandler.post(this.mRegisterListeners);
        notifyAllListeners();
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void dispatchDemoCommand(java.lang.String r18, android.os.Bundle r19) {
        /*
            r17 = this;
            r0 = r17
            r1 = r19
            com.android.systemui.demomode.DemoModeController r2 = r0.mDemoModeController
            boolean r2 = r2.isInDemoMode()
            if (r2 != 0) goto L_0x000d
            return
        L_0x000d:
            java.lang.String r2 = "airplane"
            java.lang.String r2 = r1.getString(r2)
            java.lang.String r3 = "show"
            if (r2 == 0) goto L_0x0030
            boolean r2 = r2.equals(r3)
            com.android.systemui.statusbar.connectivity.CallbackHandler r4 = r0.mCallbackHandler
            com.android.systemui.statusbar.connectivity.IconState r5 = new com.android.systemui.statusbar.connectivity.IconState
            int r6 = com.android.settingslib.mobile.TelephonyIcons.FLIGHT_MODE_ICON
            android.content.Context r7 = r0.mContext
            int r8 = com.android.systemui.R$string.accessibility_airplane_mode
            java.lang.String r7 = r7.getString(r8)
            r5.<init>(r2, r6, r7)
            r4.setIsAirplaneMode(r5)
        L_0x0030:
            java.lang.String r2 = "fully"
            java.lang.String r2 = r1.getString(r2)
            r4 = 0
            if (r2 == 0) goto L_0x0074
            boolean r2 = java.lang.Boolean.parseBoolean(r2)
            r0.mDemoInetCondition = r2
            java.util.BitSet r2 = new java.util.BitSet
            r2.<init>()
            boolean r5 = r0.mDemoInetCondition
            if (r5 == 0) goto L_0x004f
            com.android.systemui.statusbar.connectivity.WifiSignalController r5 = r0.mWifiSignalController
            int r5 = r5.mTransportType
            r2.set(r5)
        L_0x004f:
            com.android.systemui.statusbar.connectivity.WifiSignalController r5 = r0.mWifiSignalController
            r5.updateConnectivity(r2, r2)
            r5 = r4
        L_0x0055:
            android.util.SparseArray<com.android.systemui.statusbar.connectivity.MobileSignalController> r6 = r0.mMobileSignalControllers
            int r6 = r6.size()
            if (r5 >= r6) goto L_0x0074
            android.util.SparseArray<com.android.systemui.statusbar.connectivity.MobileSignalController> r6 = r0.mMobileSignalControllers
            java.lang.Object r6 = r6.valueAt(r5)
            com.android.systemui.statusbar.connectivity.MobileSignalController r6 = (com.android.systemui.statusbar.connectivity.MobileSignalController) r6
            boolean r7 = r0.mDemoInetCondition
            if (r7 == 0) goto L_0x006e
            int r7 = r6.mTransportType
            r2.set(r7)
        L_0x006e:
            r6.updateConnectivity(r2, r2)
            int r5 = r5 + 1
            goto L_0x0055
        L_0x0074:
            java.lang.String r2 = "wifi"
            java.lang.String r2 = r1.getString(r2)
            r5 = 3
            java.lang.String r6 = "inout"
            java.lang.String r7 = "out"
            java.lang.String r8 = "in"
            java.lang.String r9 = "null"
            java.lang.String r10 = "activity"
            java.lang.String r11 = "level"
            r14 = 1
            if (r2 == 0) goto L_0x0118
            boolean r2 = r2.equals(r3)
            java.lang.String r15 = r1.getString(r11)
            if (r15 == 0) goto L_0x00b8
            com.android.systemui.statusbar.connectivity.WifiState r13 = r0.mDemoWifiState
            boolean r16 = r15.equals(r9)
            if (r16 == 0) goto L_0x009f
            r12 = -1
            goto L_0x00ab
        L_0x009f:
            int r15 = java.lang.Integer.parseInt(r15)
            int r16 = com.android.systemui.statusbar.connectivity.WifiIcons.WIFI_LEVEL_COUNT
            int r12 = r16 + -1
            int r12 = java.lang.Math.min(r15, r12)
        L_0x00ab:
            r13.level = r12
            com.android.systemui.statusbar.connectivity.WifiState r12 = r0.mDemoWifiState
            int r13 = r12.level
            if (r13 < 0) goto L_0x00b5
            r13 = r14
            goto L_0x00b6
        L_0x00b5:
            r13 = r4
        L_0x00b6:
            r12.connected = r13
        L_0x00b8:
            java.lang.String r12 = r1.getString(r10)
            if (r12 == 0) goto L_0x00fd
            int r13 = r12.hashCode()
            switch(r13) {
                case 3365: goto L_0x00d9;
                case 110414: goto L_0x00d0;
                case 100357129: goto L_0x00c7;
                default: goto L_0x00c5;
            }
        L_0x00c5:
            r12 = -1
            goto L_0x00e1
        L_0x00c7:
            boolean r12 = r12.equals(r6)
            if (r12 != 0) goto L_0x00ce
            goto L_0x00c5
        L_0x00ce:
            r12 = 2
            goto L_0x00e1
        L_0x00d0:
            boolean r12 = r12.equals(r7)
            if (r12 != 0) goto L_0x00d7
            goto L_0x00c5
        L_0x00d7:
            r12 = r14
            goto L_0x00e1
        L_0x00d9:
            boolean r12 = r12.equals(r8)
            if (r12 != 0) goto L_0x00e0
            goto L_0x00c5
        L_0x00e0:
            r12 = r4
        L_0x00e1:
            switch(r12) {
                case 0: goto L_0x00f7;
                case 1: goto L_0x00f0;
                case 2: goto L_0x00ea;
                default: goto L_0x00e4;
            }
        L_0x00e4:
            com.android.systemui.statusbar.connectivity.WifiSignalController r12 = r0.mWifiSignalController
            r12.setActivity(r4)
            goto L_0x0102
        L_0x00ea:
            com.android.systemui.statusbar.connectivity.WifiSignalController r12 = r0.mWifiSignalController
            r12.setActivity(r5)
            goto L_0x0102
        L_0x00f0:
            com.android.systemui.statusbar.connectivity.WifiSignalController r12 = r0.mWifiSignalController
            r13 = 2
            r12.setActivity(r13)
            goto L_0x0102
        L_0x00f7:
            com.android.systemui.statusbar.connectivity.WifiSignalController r12 = r0.mWifiSignalController
            r12.setActivity(r14)
            goto L_0x0102
        L_0x00fd:
            com.android.systemui.statusbar.connectivity.WifiSignalController r12 = r0.mWifiSignalController
            r12.setActivity(r4)
        L_0x0102:
            java.lang.String r12 = "ssid"
            java.lang.String r12 = r1.getString(r12)
            if (r12 == 0) goto L_0x010f
            com.android.systemui.statusbar.connectivity.WifiState r13 = r0.mDemoWifiState
            r13.ssid = r12
        L_0x010f:
            com.android.systemui.statusbar.connectivity.WifiState r12 = r0.mDemoWifiState
            r12.enabled = r2
            com.android.systemui.statusbar.connectivity.WifiSignalController r2 = r0.mWifiSignalController
            r2.notifyListeners()
        L_0x0118:
            java.lang.String r2 = "sims"
            java.lang.String r2 = r1.getString(r2)
            r12 = 8
            if (r2 == 0) goto L_0x0174
            int r2 = java.lang.Integer.parseInt(r2)
            int r2 = android.util.MathUtils.constrain(r2, r14, r12)
            java.util.ArrayList r13 = new java.util.ArrayList
            r13.<init>()
            android.util.SparseArray<com.android.systemui.statusbar.connectivity.MobileSignalController> r15 = r0.mMobileSignalControllers
            int r15 = r15.size()
            if (r2 == r15) goto L_0x0174
            android.util.SparseArray<com.android.systemui.statusbar.connectivity.MobileSignalController> r15 = r0.mMobileSignalControllers
            r15.clear()
            android.telephony.SubscriptionManager r15 = r0.mSubscriptionManager
            int r15 = r15.getActiveSubscriptionInfoCountMax()
            r5 = r15
        L_0x0144:
            int r14 = r15 + r2
            if (r5 >= r14) goto L_0x0152
            android.telephony.SubscriptionInfo r14 = r0.addSignalController(r5, r5)
            r13.add(r14)
            int r5 = r5 + 1
            goto L_0x0144
        L_0x0152:
            com.android.systemui.statusbar.connectivity.CallbackHandler r2 = r0.mCallbackHandler
            r2.setSubs(r13)
            r2 = r4
        L_0x0158:
            android.util.SparseArray<com.android.systemui.statusbar.connectivity.MobileSignalController> r5 = r0.mMobileSignalControllers
            int r5 = r5.size()
            if (r2 >= r5) goto L_0x0174
            android.util.SparseArray<com.android.systemui.statusbar.connectivity.MobileSignalController> r5 = r0.mMobileSignalControllers
            int r5 = r5.keyAt(r2)
            android.util.SparseArray<com.android.systemui.statusbar.connectivity.MobileSignalController> r13 = r0.mMobileSignalControllers
            java.lang.Object r5 = r13.get(r5)
            com.android.systemui.statusbar.connectivity.MobileSignalController r5 = (com.android.systemui.statusbar.connectivity.MobileSignalController) r5
            r5.notifyListeners()
            int r2 = r2 + 1
            goto L_0x0158
        L_0x0174:
            java.lang.String r2 = "nosim"
            java.lang.String r2 = r1.getString(r2)
            if (r2 == 0) goto L_0x0189
            boolean r2 = r2.equals(r3)
            r0.mHasNoSubs = r2
            com.android.systemui.statusbar.connectivity.CallbackHandler r5 = r0.mCallbackHandler
            boolean r13 = r0.mSimDetected
            r5.setNoSims(r2, r13)
        L_0x0189:
            java.lang.String r2 = "mobile"
            java.lang.String r2 = r1.getString(r2)
            if (r2 == 0) goto L_0x038c
            boolean r2 = r2.equals(r3)
            java.lang.String r5 = "datatype"
            java.lang.String r5 = r1.getString(r5)
            java.lang.String r13 = "slot"
            java.lang.String r13 = r1.getString(r13)
            boolean r14 = android.text.TextUtils.isEmpty(r13)
            if (r14 == 0) goto L_0x01aa
            r13 = r4
            goto L_0x01ae
        L_0x01aa:
            int r13 = java.lang.Integer.parseInt(r13)
        L_0x01ae:
            int r12 = android.util.MathUtils.constrain(r13, r4, r12)
            java.util.ArrayList r13 = new java.util.ArrayList
            r13.<init>()
        L_0x01b7:
            android.util.SparseArray<com.android.systemui.statusbar.connectivity.MobileSignalController> r14 = r0.mMobileSignalControllers
            int r14 = r14.size()
            if (r14 > r12) goto L_0x01cd
            android.util.SparseArray<com.android.systemui.statusbar.connectivity.MobileSignalController> r14 = r0.mMobileSignalControllers
            int r14 = r14.size()
            android.telephony.SubscriptionInfo r14 = r0.addSignalController(r14, r14)
            r13.add(r14)
            goto L_0x01b7
        L_0x01cd:
            boolean r14 = r13.isEmpty()
            if (r14 != 0) goto L_0x01d8
            com.android.systemui.statusbar.connectivity.CallbackHandler r14 = r0.mCallbackHandler
            r14.setSubs(r13)
        L_0x01d8:
            android.util.SparseArray<com.android.systemui.statusbar.connectivity.MobileSignalController> r13 = r0.mMobileSignalControllers
            java.lang.Object r12 = r13.valueAt(r12)
            com.android.systemui.statusbar.connectivity.MobileSignalController r12 = (com.android.systemui.statusbar.connectivity.MobileSignalController) r12
            com.android.systemui.statusbar.connectivity.ConnectivityState r13 = r12.getState()
            com.android.systemui.statusbar.connectivity.MobileState r13 = (com.android.systemui.statusbar.connectivity.MobileState) r13
            if (r5 == 0) goto L_0x01ea
            r14 = 1
            goto L_0x01eb
        L_0x01ea:
            r14 = r4
        L_0x01eb:
            r13.dataSim = r14
            com.android.systemui.statusbar.connectivity.ConnectivityState r13 = r12.getState()
            com.android.systemui.statusbar.connectivity.MobileState r13 = (com.android.systemui.statusbar.connectivity.MobileState) r13
            if (r5 == 0) goto L_0x01f7
            r14 = 1
            goto L_0x01f8
        L_0x01f7:
            r14 = r4
        L_0x01f8:
            r13.isDefault = r14
            com.android.systemui.statusbar.connectivity.ConnectivityState r13 = r12.getState()
            com.android.systemui.statusbar.connectivity.MobileState r13 = (com.android.systemui.statusbar.connectivity.MobileState) r13
            if (r5 == 0) goto L_0x0204
            r14 = 1
            goto L_0x0205
        L_0x0204:
            r14 = r4
        L_0x0205:
            r13.dataConnected = r14
            if (r5 == 0) goto L_0x02be
            com.android.systemui.statusbar.connectivity.ConnectivityState r13 = r12.getState()
            com.android.systemui.statusbar.connectivity.MobileState r13 = (com.android.systemui.statusbar.connectivity.MobileState) r13
            java.lang.String r14 = "1x"
            boolean r14 = r5.equals(r14)
            if (r14 == 0) goto L_0x021b
            com.android.settingslib.SignalIcon$MobileIconGroup r5 = com.android.settingslib.mobile.TelephonyIcons.ONE_X
            goto L_0x02bc
        L_0x021b:
            java.lang.String r14 = "3g"
            boolean r14 = r5.equals(r14)
            if (r14 == 0) goto L_0x0227
            com.android.settingslib.SignalIcon$MobileIconGroup r5 = com.android.settingslib.mobile.TelephonyIcons.THREE_G
            goto L_0x02bc
        L_0x0227:
            java.lang.String r14 = "4g"
            boolean r14 = r5.equals(r14)
            if (r14 == 0) goto L_0x0233
            com.android.settingslib.SignalIcon$MobileIconGroup r5 = com.android.settingslib.mobile.TelephonyIcons.FOUR_G
            goto L_0x02bc
        L_0x0233:
            java.lang.String r14 = "4g+"
            boolean r14 = r5.equals(r14)
            if (r14 == 0) goto L_0x023f
            com.android.settingslib.SignalIcon$MobileIconGroup r5 = com.android.settingslib.mobile.TelephonyIcons.FOUR_G_PLUS
            goto L_0x02bc
        L_0x023f:
            java.lang.String r14 = "5g"
            boolean r14 = r5.equals(r14)
            if (r14 == 0) goto L_0x024b
            com.android.settingslib.SignalIcon$MobileIconGroup r5 = com.android.settingslib.mobile.TelephonyIcons.NR_5G
            goto L_0x02bc
        L_0x024b:
            java.lang.String r14 = "5ge"
            boolean r14 = r5.equals(r14)
            if (r14 == 0) goto L_0x0257
            com.android.settingslib.SignalIcon$MobileIconGroup r5 = com.android.settingslib.mobile.TelephonyIcons.LTE_CA_5G_E
            goto L_0x02bc
        L_0x0257:
            java.lang.String r14 = "5g+"
            boolean r14 = r5.equals(r14)
            if (r14 == 0) goto L_0x0262
            com.android.settingslib.SignalIcon$MobileIconGroup r5 = com.android.settingslib.mobile.TelephonyIcons.NR_5G_PLUS
            goto L_0x02bc
        L_0x0262:
            java.lang.String r14 = "e"
            boolean r14 = r5.equals(r14)
            if (r14 == 0) goto L_0x026d
            com.android.settingslib.SignalIcon$MobileIconGroup r5 = com.android.settingslib.mobile.TelephonyIcons.E
            goto L_0x02bc
        L_0x026d:
            java.lang.String r14 = "g"
            boolean r14 = r5.equals(r14)
            if (r14 == 0) goto L_0x0278
            com.android.settingslib.SignalIcon$MobileIconGroup r5 = com.android.settingslib.mobile.TelephonyIcons.G
            goto L_0x02bc
        L_0x0278:
            java.lang.String r14 = "h"
            boolean r14 = r5.equals(r14)
            if (r14 == 0) goto L_0x0283
            com.android.settingslib.SignalIcon$MobileIconGroup r5 = com.android.settingslib.mobile.TelephonyIcons.H
            goto L_0x02bc
        L_0x0283:
            java.lang.String r14 = "h+"
            boolean r14 = r5.equals(r14)
            if (r14 == 0) goto L_0x028e
            com.android.settingslib.SignalIcon$MobileIconGroup r5 = com.android.settingslib.mobile.TelephonyIcons.H_PLUS
            goto L_0x02bc
        L_0x028e:
            java.lang.String r14 = "lte"
            boolean r14 = r5.equals(r14)
            if (r14 == 0) goto L_0x0299
            com.android.settingslib.SignalIcon$MobileIconGroup r5 = com.android.settingslib.mobile.TelephonyIcons.LTE
            goto L_0x02bc
        L_0x0299:
            java.lang.String r14 = "lte+"
            boolean r14 = r5.equals(r14)
            if (r14 == 0) goto L_0x02a4
            com.android.settingslib.SignalIcon$MobileIconGroup r5 = com.android.settingslib.mobile.TelephonyIcons.LTE_PLUS
            goto L_0x02bc
        L_0x02a4:
            java.lang.String r14 = "dis"
            boolean r14 = r5.equals(r14)
            if (r14 == 0) goto L_0x02af
            com.android.settingslib.SignalIcon$MobileIconGroup r5 = com.android.settingslib.mobile.TelephonyIcons.DATA_DISABLED
            goto L_0x02bc
        L_0x02af:
            java.lang.String r14 = "not"
            boolean r5 = r5.equals(r14)
            if (r5 == 0) goto L_0x02ba
            com.android.settingslib.SignalIcon$MobileIconGroup r5 = com.android.settingslib.mobile.TelephonyIcons.NOT_DEFAULT_DATA
            goto L_0x02bc
        L_0x02ba:
            com.android.settingslib.SignalIcon$MobileIconGroup r5 = com.android.settingslib.mobile.TelephonyIcons.UNKNOWN
        L_0x02bc:
            r13.iconGroup = r5
        L_0x02be:
            java.lang.String r5 = "roam"
            boolean r13 = r1.containsKey(r5)
            if (r13 == 0) goto L_0x02d6
            com.android.systemui.statusbar.connectivity.ConnectivityState r13 = r12.getState()
            com.android.systemui.statusbar.connectivity.MobileState r13 = (com.android.systemui.statusbar.connectivity.MobileState) r13
            java.lang.String r5 = r1.getString(r5)
            boolean r5 = r3.equals(r5)
            r13.roaming = r5
        L_0x02d6:
            java.lang.String r5 = r1.getString(r11)
            if (r5 == 0) goto L_0x030d
            com.android.systemui.statusbar.connectivity.ConnectivityState r11 = r12.getState()
            com.android.systemui.statusbar.connectivity.MobileState r11 = (com.android.systemui.statusbar.connectivity.MobileState) r11
            boolean r9 = r5.equals(r9)
            if (r9 == 0) goto L_0x02ea
            r5 = -1
            goto L_0x02f6
        L_0x02ea:
            int r5 = java.lang.Integer.parseInt(r5)
            int r9 = android.telephony.CellSignalStrength.getNumSignalStrengthLevels()
            int r5 = java.lang.Math.min(r5, r9)
        L_0x02f6:
            r11.level = r5
            com.android.systemui.statusbar.connectivity.ConnectivityState r5 = r12.getState()
            com.android.systemui.statusbar.connectivity.MobileState r5 = (com.android.systemui.statusbar.connectivity.MobileState) r5
            com.android.systemui.statusbar.connectivity.ConnectivityState r9 = r12.getState()
            com.android.systemui.statusbar.connectivity.MobileState r9 = (com.android.systemui.statusbar.connectivity.MobileState) r9
            int r9 = r9.level
            if (r9 < 0) goto L_0x030a
            r9 = 1
            goto L_0x030b
        L_0x030a:
            r9 = r4
        L_0x030b:
            r5.connected = r9
        L_0x030d:
            java.lang.String r5 = "inflate"
            boolean r9 = r1.containsKey(r5)
            if (r9 == 0) goto L_0x0336
            r9 = r4
        L_0x0316:
            android.util.SparseArray<com.android.systemui.statusbar.connectivity.MobileSignalController> r11 = r0.mMobileSignalControllers
            int r11 = r11.size()
            if (r9 >= r11) goto L_0x0336
            android.util.SparseArray<com.android.systemui.statusbar.connectivity.MobileSignalController> r11 = r0.mMobileSignalControllers
            java.lang.Object r11 = r11.valueAt(r9)
            com.android.systemui.statusbar.connectivity.MobileSignalController r11 = (com.android.systemui.statusbar.connectivity.MobileSignalController) r11
            java.lang.String r13 = r1.getString(r5)
            java.lang.String r14 = "true"
            boolean r13 = r14.equals(r13)
            r11.mInflateSignalStrengths = r13
            int r9 = r9 + 1
            goto L_0x0316
        L_0x0336:
            java.lang.String r5 = r1.getString(r10)
            if (r5 == 0) goto L_0x037e
            com.android.systemui.statusbar.connectivity.ConnectivityState r9 = r12.getState()
            com.android.systemui.statusbar.connectivity.MobileState r9 = (com.android.systemui.statusbar.connectivity.MobileState) r9
            r10 = 1
            r9.dataConnected = r10
            int r9 = r5.hashCode()
            switch(r9) {
                case 3365: goto L_0x0360;
                case 110414: goto L_0x0357;
                case 100357129: goto L_0x034e;
                default: goto L_0x034c;
            }
        L_0x034c:
            r13 = -1
            goto L_0x0368
        L_0x034e:
            boolean r5 = r5.equals(r6)
            if (r5 != 0) goto L_0x0355
            goto L_0x034c
        L_0x0355:
            r13 = 2
            goto L_0x0368
        L_0x0357:
            boolean r5 = r5.equals(r7)
            if (r5 != 0) goto L_0x035e
            goto L_0x034c
        L_0x035e:
            r13 = 1
            goto L_0x0368
        L_0x0360:
            boolean r5 = r5.equals(r8)
            if (r5 != 0) goto L_0x0367
            goto L_0x034c
        L_0x0367:
            r13 = r4
        L_0x0368:
            switch(r13) {
                case 0: goto L_0x0379;
                case 1: goto L_0x0374;
                case 2: goto L_0x036f;
                default: goto L_0x036b;
            }
        L_0x036b:
            r12.setActivity(r4)
            goto L_0x0381
        L_0x036f:
            r5 = 3
            r12.setActivity(r5)
            goto L_0x0381
        L_0x0374:
            r5 = 2
            r12.setActivity(r5)
            goto L_0x0381
        L_0x0379:
            r5 = 1
            r12.setActivity(r5)
            goto L_0x0381
        L_0x037e:
            r12.setActivity(r4)
        L_0x0381:
            com.android.systemui.statusbar.connectivity.ConnectivityState r5 = r12.getState()
            com.android.systemui.statusbar.connectivity.MobileState r5 = (com.android.systemui.statusbar.connectivity.MobileState) r5
            r5.enabled = r2
            r12.notifyListeners()
        L_0x038c:
            java.lang.String r2 = "carriernetworkchange"
            java.lang.String r1 = r1.getString(r2)
            if (r1 == 0) goto L_0x03ae
            boolean r1 = r1.equals(r3)
        L_0x0398:
            android.util.SparseArray<com.android.systemui.statusbar.connectivity.MobileSignalController> r2 = r0.mMobileSignalControllers
            int r2 = r2.size()
            if (r4 >= r2) goto L_0x03ae
            android.util.SparseArray<com.android.systemui.statusbar.connectivity.MobileSignalController> r2 = r0.mMobileSignalControllers
            java.lang.Object r2 = r2.valueAt(r4)
            com.android.systemui.statusbar.connectivity.MobileSignalController r2 = (com.android.systemui.statusbar.connectivity.MobileSignalController) r2
            r2.setCarrierNetworkChangeMode(r1)
            int r4 = r4 + 1
            goto L_0x0398
        L_0x03ae:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.connectivity.NetworkControllerImpl.dispatchDemoCommand(java.lang.String, android.os.Bundle):void");
    }

    public List<String> demoCommands() {
        ArrayList arrayList = new ArrayList();
        arrayList.add("network");
        return arrayList;
    }

    public final void recordLastNetworkCallback(String str) {
        String[] strArr = this.mHistory;
        int i = this.mHistoryIndex;
        strArr[i] = str;
        this.mHistoryIndex = (i + 1) % 16;
    }

    public final SubscriptionInfo addSignalController(int i, int i2) {
        SubscriptionInfo subscriptionInfo = new SubscriptionInfo(i, "", i2, "", "", 0, 0, "", 0, (Bitmap) null, (String) null, (String) null, "", false, (UiccAccessRule[]) null, (String) null);
        MobileSignalController mobileSignalController = new MobileSignalController(this.mContext, this.mConfig, this.mHasMobileDataFeature, this.mPhone.createForSubscriptionId(subscriptionInfo.getSubscriptionId()), this.mCallbackHandler, this, subscriptionInfo, this.mSubDefaults, this.mReceiverHandler.getLooper(), this.mCarrierConfigTracker, this.mFeatureFlags);
        this.mMobileSignalControllers.put(i, mobileSignalController);
        ((MobileState) mobileSignalController.getState()).userSetup = true;
        return subscriptionInfo;
    }

    public boolean hasEmergencyCryptKeeperText() {
        return EncryptionHelper.IS_DATA_ENCRYPTED;
    }

    public boolean isRadioOn() {
        return !this.mAirplaneMode;
    }

    public class SubListener extends SubscriptionManager.OnSubscriptionsChangedListener {
        public SubListener(Looper looper) {
            super(looper);
        }

        public void onSubscriptionsChanged() {
            NetworkControllerImpl.this.updateMobileControllers();
        }
    }
}
