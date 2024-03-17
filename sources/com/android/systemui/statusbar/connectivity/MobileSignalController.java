package com.android.systemui.statusbar.connectivity;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.telephony.CellSignalStrength;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.ims.ImsException;
import android.telephony.ims.ImsMmTelManager;
import android.telephony.ims.ImsReasonInfo;
import android.telephony.ims.ImsRegistrationAttributes;
import android.telephony.ims.ImsStateCallback;
import android.telephony.ims.RegistrationManager;
import android.telephony.ims.feature.MmTelFeature;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import androidx.mediarouter.media.MediaRoute2Provider$$ExternalSyntheticLambda0;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settingslib.AccessibilityContentDescriptions;
import com.android.settingslib.SignalIcon$MobileIconGroup;
import com.android.settingslib.graph.SignalDrawable;
import com.android.settingslib.mobile.MobileMappings;
import com.android.settingslib.mobile.MobileStatusTracker;
import com.android.settingslib.mobile.TelephonyIcons;
import com.android.settingslib.net.SignalStrengthUtil;
import com.android.systemui.R$drawable;
import com.android.systemui.R$string;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.android.systemui.statusbar.policy.FiveGServiceClient;
import com.android.systemui.util.CarrierConfigTracker;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.BitSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MobileSignalController extends SignalController<MobileState, SignalIcon$MobileIconGroup> {
    public static final SimpleDateFormat SSDF = new SimpleDateFormat("MM-dd HH:mm:ss.SSS");
    public int mCallState = 0;
    public ImsMmTelManager.CapabilityCallback mCapabilityCallback;
    public final CarrierConfigTracker mCarrierConfigTracker;
    public FiveGServiceClient mClient;
    public MobileMappings.Config mConfig;
    public SignalIcon$MobileIconGroup mDefaultIcons;
    public final MobileStatusTracker.SubscriptionDefaults mDefaults;
    @VisibleForTesting
    public FiveGServiceClient.FiveGServiceState mFiveGState;
    @VisibleForTesting
    public FiveGStateListener mFiveGStateListener;
    public final ImsMmTelManager mImsMmTelManager;
    public final ImsStateCallback mImsStateCallback;
    public int mImsType = 1;
    @VisibleForTesting
    public boolean mInflateSignalStrengths = false;
    public int mLastLevel;
    public int mLastWlanCrossSimLevel;
    public int mLastWlanLevel;
    public int mLastWwanLevel;
    public final MobileStatusTracker.Callback mMobileCallback;
    public final String[] mMobileStatusHistory = new String[64];
    public int mMobileStatusHistoryIndex;
    @VisibleForTesting
    public MobileStatusTracker mMobileStatusTracker;
    public final String mNetworkNameDefault;
    public final String mNetworkNameSeparator;
    public Map<String, SignalIcon$MobileIconGroup> mNetworkToIconLookup;
    public final ContentObserver mObserver;
    public final TelephonyManager mPhone;
    public final boolean mProviderModelBehavior;
    public final Handler mReceiverHandler;
    public final RegistrationManager.RegistrationCallback mRegistrationCallback;
    public final SubscriptionInfo mSubscriptionInfo;
    public final Runnable mTryRegisterIms;
    public final BroadcastReceiver mVolteSwitchObserver;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public MobileSignalController(Context context, MobileMappings.Config config, boolean z, TelephonyManager telephonyManager, CallbackHandler callbackHandler, NetworkControllerImpl networkControllerImpl, SubscriptionInfo subscriptionInfo, MobileStatusTracker.SubscriptionDefaults subscriptionDefaults, Looper looper, CarrierConfigTracker carrierConfigTracker, FeatureFlags featureFlags) {
        super("MobileSignalController(" + subscriptionInfo.getSubscriptionId() + ")", context, 0, callbackHandler, networkControllerImpl);
        boolean z2 = z;
        Looper looper2 = looper;
        AnonymousClass1 r5 = new MobileStatusTracker.Callback() {
            public String mLastStatus;

            public void onMobileStatusChanged(boolean z, MobileStatusTracker.MobileStatus mobileStatus) {
                if (SignalController.DEBUG) {
                    String str = MobileSignalController.this.mTag;
                    Log.d(str, "onMobileStatusChanged= updateTelephony=" + z + " mobileStatus=" + mobileStatus.toString());
                }
                String mobileStatus2 = mobileStatus.toString();
                if (!mobileStatus2.equals(this.mLastStatus)) {
                    this.mLastStatus = mobileStatus2;
                    MobileSignalController.this.recordLastMobileStatus(MobileSignalController.SSDF.format(Long.valueOf(System.currentTimeMillis())) + "," + mobileStatus2);
                }
                MobileSignalController.this.updateMobileStatus(mobileStatus);
                if (z) {
                    MobileSignalController.this.updateTelephony();
                } else {
                    MobileSignalController.this.notifyListenersIfNecessary();
                }
            }
        };
        this.mMobileCallback = r5;
        this.mRegistrationCallback = new RegistrationManager.RegistrationCallback() {
            public void onRegistered(ImsRegistrationAttributes imsRegistrationAttributes) {
                String str = MobileSignalController.this.mTag;
                Log.d(str, "onRegistered: attributes=" + imsRegistrationAttributes);
                T t = MobileSignalController.this.mCurrentState;
                ((MobileState) t).imsRegistered = true;
                ((MobileState) t).imsRegistrationTech = imsRegistrationAttributes.getRegistrationTechnology();
                MobileSignalController.this.notifyListenersIfNecessary();
                if (MobileSignalController.this.mProviderModelBehavior) {
                    int transportType = imsRegistrationAttributes.getTransportType();
                    int attributeFlags = imsRegistrationAttributes.getAttributeFlags();
                    if (transportType == 1) {
                        MobileSignalController.this.mImsType = 1;
                        MobileSignalController mobileSignalController = MobileSignalController.this;
                        int r0 = mobileSignalController.getCallStrengthIcon(mobileSignalController.mLastWwanLevel, false);
                        MobileSignalController mobileSignalController2 = MobileSignalController.this;
                        IconState iconState = new IconState(true, r0, mobileSignalController2.getCallStrengthDescription(mobileSignalController2.mLastWwanLevel, false));
                        MobileSignalController mobileSignalController3 = MobileSignalController.this;
                        mobileSignalController3.notifyCallStateChange(iconState, mobileSignalController3.mSubscriptionInfo.getSubscriptionId());
                    } else if (transportType != 2) {
                    } else {
                        if (attributeFlags == 0) {
                            MobileSignalController.this.mImsType = 2;
                            MobileSignalController mobileSignalController4 = MobileSignalController.this;
                            int r02 = mobileSignalController4.getCallStrengthIcon(mobileSignalController4.mLastWlanLevel, true);
                            MobileSignalController mobileSignalController5 = MobileSignalController.this;
                            IconState iconState2 = new IconState(true, r02, mobileSignalController5.getCallStrengthDescription(mobileSignalController5.mLastWlanLevel, true));
                            MobileSignalController mobileSignalController6 = MobileSignalController.this;
                            mobileSignalController6.notifyCallStateChange(iconState2, mobileSignalController6.mSubscriptionInfo.getSubscriptionId());
                        } else if (attributeFlags == 1) {
                            MobileSignalController.this.mImsType = 3;
                            MobileSignalController mobileSignalController7 = MobileSignalController.this;
                            int r03 = mobileSignalController7.getCallStrengthIcon(mobileSignalController7.mLastWlanCrossSimLevel, false);
                            MobileSignalController mobileSignalController8 = MobileSignalController.this;
                            IconState iconState3 = new IconState(true, r03, mobileSignalController8.getCallStrengthDescription(mobileSignalController8.mLastWlanCrossSimLevel, false));
                            MobileSignalController mobileSignalController9 = MobileSignalController.this;
                            mobileSignalController9.notifyCallStateChange(iconState3, mobileSignalController9.mSubscriptionInfo.getSubscriptionId());
                        }
                    }
                }
            }

            public void onUnregistered(ImsReasonInfo imsReasonInfo) {
                String str = MobileSignalController.this.mTag;
                Log.d(str, "onDeregistered: info=" + imsReasonInfo);
                MobileSignalController mobileSignalController = MobileSignalController.this;
                T t = mobileSignalController.mCurrentState;
                ((MobileState) t).imsRegistered = false;
                ((MobileState) t).imsRegistrationTech = -1;
                mobileSignalController.notifyListenersIfNecessary();
                if (MobileSignalController.this.mProviderModelBehavior) {
                    MobileSignalController.this.mImsType = 1;
                    MobileSignalController mobileSignalController2 = MobileSignalController.this;
                    int r1 = mobileSignalController2.getCallStrengthIcon(mobileSignalController2.mLastWwanLevel, false);
                    MobileSignalController mobileSignalController3 = MobileSignalController.this;
                    IconState iconState = new IconState(true, r1, mobileSignalController3.getCallStrengthDescription(mobileSignalController3.mLastWwanLevel, false));
                    MobileSignalController mobileSignalController4 = MobileSignalController.this;
                    mobileSignalController4.notifyCallStateChange(iconState, mobileSignalController4.mSubscriptionInfo.getSubscriptionId());
                }
            }
        };
        this.mTryRegisterIms = new Runnable() {
            public int mRetryCount;

            public void run() {
                try {
                    this.mRetryCount++;
                    ImsMmTelManager r0 = MobileSignalController.this.mImsMmTelManager;
                    Handler r1 = MobileSignalController.this.mReceiverHandler;
                    Objects.requireNonNull(r1);
                    r0.registerImsRegistrationCallback(new MediaRoute2Provider$$ExternalSyntheticLambda0(r1), MobileSignalController.this.mRegistrationCallback);
                    Log.d(MobileSignalController.this.mTag, "registerImsRegistrationCallback succeeded");
                } catch (ImsException | RuntimeException e) {
                    if (this.mRetryCount < 12) {
                        Log.e(MobileSignalController.this.mTag, this.mRetryCount + " registerImsRegistrationCallback failed", e);
                        MobileSignalController.this.mReceiverHandler.postDelayed(MobileSignalController.this.mTryRegisterIms, 5000);
                    }
                }
            }
        };
        this.mCapabilityCallback = new ImsMmTelManager.CapabilityCallback() {
            public void onCapabilitiesStatusChanged(MmTelFeature.MmTelCapabilities mmTelCapabilities) {
                ((MobileState) MobileSignalController.this.mCurrentState).voiceCapable = mmTelCapabilities.isCapable(1);
                ((MobileState) MobileSignalController.this.mCurrentState).videoCapable = mmTelCapabilities.isCapable(2);
                String str = MobileSignalController.this.mTag;
                Log.d(str, "onCapabilitiesStatusChanged isVoiceCapable=" + ((MobileState) MobileSignalController.this.mCurrentState).voiceCapable + " isVideoCapable=" + ((MobileState) MobileSignalController.this.mCurrentState).videoCapable);
                MobileSignalController.this.notifyListenersIfNecessary();
            }
        };
        this.mVolteSwitchObserver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String str = MobileSignalController.this.mTag;
                Log.d(str, "action=" + intent.getAction());
                if (MobileSignalController.this.mConfig.showVolteIcon) {
                    MobileSignalController.this.notifyListeners();
                }
            }
        };
        this.mImsStateCallback = new ImsStateCallback() {
            public void onUnavailable(int i) {
                String str = MobileSignalController.this.mTag;
                Log.d(str, "ImsStateCallback.onUnavailable: reason=" + i);
                MobileSignalController.this.removeListeners();
            }

            public void onAvailable() {
                Log.d(MobileSignalController.this.mTag, "ImsStateCallback.onAvailable");
                MobileSignalController.this.setListeners();
            }

            public void onError() {
                Log.e(MobileSignalController.this.mTag, "ImsStateCallback.onError");
                MobileSignalController.this.removeListeners();
            }
        };
        this.mCarrierConfigTracker = carrierConfigTracker;
        this.mConfig = config;
        this.mPhone = telephonyManager;
        this.mDefaults = subscriptionDefaults;
        this.mSubscriptionInfo = subscriptionInfo;
        this.mFiveGStateListener = new FiveGStateListener();
        this.mFiveGState = new FiveGServiceClient.FiveGServiceState();
        this.mNetworkNameSeparator = getTextIfExists(R$string.status_bar_network_name_separator).toString();
        String charSequence = getTextIfExists(17040607).toString();
        this.mNetworkNameDefault = charSequence;
        this.mReceiverHandler = new Handler(looper2);
        this.mNetworkToIconLookup = MobileMappings.mapIconSets(this.mConfig);
        this.mDefaultIcons = MobileMappings.getDefaultIcons(this.mConfig);
        charSequence = subscriptionInfo.getCarrierName() != null ? subscriptionInfo.getCarrierName().toString() : charSequence;
        T t = this.mLastState;
        T t2 = this.mCurrentState;
        ((MobileState) t2).networkName = charSequence;
        ((MobileState) t).networkName = charSequence;
        ((MobileState) t2).networkNameData = charSequence;
        ((MobileState) t).networkNameData = charSequence;
        ((MobileState) t2).enabled = z2;
        ((MobileState) t).enabled = z2;
        SignalIcon$MobileIconGroup signalIcon$MobileIconGroup = this.mDefaultIcons;
        ((MobileState) t2).iconGroup = signalIcon$MobileIconGroup;
        ((MobileState) t).iconGroup = signalIcon$MobileIconGroup;
        this.mObserver = new ContentObserver(new Handler(looper2)) {
            public void onChange(boolean z) {
                MobileSignalController.this.updateTelephony();
            }
        };
        this.mImsMmTelManager = ImsMmTelManager.createForSubscriptionId(subscriptionInfo.getSubscriptionId());
        this.mMobileStatusTracker = new MobileStatusTracker(telephonyManager, looper, subscriptionInfo, subscriptionDefaults, r5);
        this.mProviderModelBehavior = featureFlags.isEnabled(Flags.COMBINED_STATUS_BAR_SIGNAL_ICONS);
    }

    public void setConfiguration(MobileMappings.Config config) {
        this.mConfig = config;
        updateInflateSignalStrength();
        this.mNetworkToIconLookup = MobileMappings.mapIconSets(this.mConfig);
        this.mDefaultIcons = MobileMappings.getDefaultIcons(this.mConfig);
        updateTelephony();
    }

    public void setAirplaneMode(boolean z) {
        ((MobileState) this.mCurrentState).airplaneMode = z;
        notifyListenersIfNecessary();
    }

    public void setUserSetupComplete(boolean z) {
        ((MobileState) this.mCurrentState).userSetup = z;
        notifyListenersIfNecessary();
    }

    public void updateConnectivity(BitSet bitSet, BitSet bitSet2) {
        boolean z = bitSet2.get(this.mTransportType);
        ((MobileState) this.mCurrentState).isDefault = bitSet.get(this.mTransportType);
        T t = this.mCurrentState;
        ((MobileState) t).inetCondition = (z || !((MobileState) t).isDefault) ? 1 : 0;
        notifyListenersIfNecessary();
    }

    public void setCarrierNetworkChangeMode(boolean z) {
        ((MobileState) this.mCurrentState).carrierNetworkChangeMode = z;
        updateTelephony();
    }

    public void registerListener() {
        this.mMobileStatusTracker.setListening(true);
        this.mContext.getContentResolver().registerContentObserver(Settings.Global.getUriFor("mobile_data"), true, this.mObserver);
        ContentResolver contentResolver = this.mContext.getContentResolver();
        contentResolver.registerContentObserver(Settings.Global.getUriFor("mobile_data" + this.mSubscriptionInfo.getSubscriptionId()), true, this.mObserver);
        this.mContext.getContentResolver().registerContentObserver(Settings.Global.getUriFor("data_roaming"), true, this.mObserver);
        ContentResolver contentResolver2 = this.mContext.getContentResolver();
        contentResolver2.registerContentObserver(Settings.Global.getUriFor("data_roaming" + this.mSubscriptionInfo.getSubscriptionId()), true, this.mObserver);
        this.mContext.registerReceiver(this.mVolteSwitchObserver, new IntentFilter("org.codeaurora.intent.action.ACTION_ENHANCE_4G_SWITCH"));
        MobileMappings.Config config = this.mConfig;
        if (config.showVolteIcon || config.showVowifiIcon) {
            try {
                this.mImsMmTelManager.registerImsStateCallback(this.mContext.getMainExecutor(), this.mImsStateCallback);
            } catch (ImsException e) {
                Log.e(this.mTag, "failed to call registerImsStateCallback ", e);
            }
        }
        if (this.mProviderModelBehavior) {
            this.mReceiverHandler.post(this.mTryRegisterIms);
        }
    }

    public void unregisterListener() {
        this.mMobileStatusTracker.setListening(false);
        this.mContext.getContentResolver().unregisterContentObserver(this.mObserver);
        try {
            this.mImsMmTelManager.unregisterImsRegistrationCallback(this.mRegistrationCallback);
        } catch (Exception e) {
            Log.e(this.mTag, "unregisterListener: fail to call unregisterImsRegistrationCallback", e);
        }
        this.mContext.unregisterReceiver(this.mVolteSwitchObserver);
        MobileMappings.Config config = this.mConfig;
        if (config.showVolteIcon || config.showVowifiIcon) {
            this.mImsMmTelManager.unregisterImsStateCallback(this.mImsStateCallback);
        }
    }

    public final void updateInflateSignalStrength() {
        this.mInflateSignalStrengths = SignalStrengthUtil.shouldInflateSignalStrength(this.mContext, this.mSubscriptionInfo.getSubscriptionId());
    }

    public final int getNumLevels() {
        if (this.mInflateSignalStrengths) {
            return CellSignalStrength.getNumSignalStrengthLevels() + 1;
        }
        return CellSignalStrength.getNumSignalStrengthLevels();
    }

    public int getCurrentIconId() {
        T t = this.mCurrentState;
        if (((MobileState) t).iconGroup == TelephonyIcons.CARRIER_NETWORK_CHANGE) {
            return SignalDrawable.getCarrierChangeState(getNumLevels());
        }
        boolean z = false;
        if (((MobileState) t).connected) {
            int i = ((MobileState) t).level;
            if (this.mInflateSignalStrengths) {
                i++;
            }
            boolean z2 = true;
            boolean z3 = ((MobileState) t).userSetup && (((MobileState) t).iconGroup == TelephonyIcons.DATA_DISABLED || (((MobileState) t).iconGroup == TelephonyIcons.NOT_DEFAULT_DATA && ((MobileState) t).defaultDataOff));
            boolean z4 = ((MobileState) t).inetCondition == 0;
            if (!z3 && !z4) {
                z2 = false;
            }
            if (!this.mConfig.hideNoInternetState) {
                z = z2;
            }
            return SignalDrawable.getState(i, getNumLevels(), z);
        } else if (((MobileState) t).enabled) {
            return SignalDrawable.getEmptyState(getNumLevels());
        } else {
            return 0;
        }
    }

    public int getQsCurrentIconId() {
        return getCurrentIconId();
    }

    public final int getVolteResId() {
        int voiceNetworkType = ((MobileState) this.mCurrentState).getVoiceNetworkType();
        T t = this.mCurrentState;
        if ((((MobileState) t).voiceCapable || ((MobileState) t).videoCapable) && ((MobileState) t).imsRegistered) {
            return R$drawable.ic_volte;
        }
        if ((((MobileState) t).telephonyDisplayInfo.getNetworkType() == 13 || ((MobileState) this.mCurrentState).telephonyDisplayInfo.getNetworkType() == 19) && voiceNetworkType == 0) {
            return R$drawable.ic_volte_no_voice;
        }
        return 0;
    }

    public final void setListeners() {
        try {
            Log.d(this.mTag, "setListeners: register CapabilitiesCallback and RegistrationCallback");
            this.mImsMmTelManager.registerMmTelCapabilityCallback(this.mContext.getMainExecutor(), this.mCapabilityCallback);
            this.mImsMmTelManager.registerImsRegistrationCallback(this.mContext.getMainExecutor(), this.mRegistrationCallback);
        } catch (ImsException e) {
            Log.e(this.mTag, "unable to register listeners.", e);
        }
        queryImsState();
    }

    public final void queryImsState() {
        TelephonyManager createForSubscriptionId = this.mPhone.createForSubscriptionId(this.mSubscriptionInfo.getSubscriptionId());
        ((MobileState) this.mCurrentState).voiceCapable = createForSubscriptionId.isVolteAvailable();
        ((MobileState) this.mCurrentState).videoCapable = createForSubscriptionId.isVideoTelephonyAvailable();
        ((MobileState) this.mCurrentState).imsRegistered = this.mPhone.isImsRegistered(this.mSubscriptionInfo.getSubscriptionId());
        if (SignalController.DEBUG) {
            String str = this.mTag;
            Log.d(str, "queryImsState tm=" + createForSubscriptionId + " phone=" + this.mPhone + " voiceCapable=" + ((MobileState) this.mCurrentState).voiceCapable + " videoCapable=" + ((MobileState) this.mCurrentState).videoCapable + " imsResitered=" + ((MobileState) this.mCurrentState).imsRegistered);
        }
        notifyListenersIfNecessary();
    }

    public final void removeListeners() {
        try {
            Log.d(this.mTag, "removeListeners: unregister CapabilitiesCallback and RegistrationCallback");
            this.mImsMmTelManager.unregisterMmTelCapabilityCallback(this.mCapabilityCallback);
            this.mImsMmTelManager.unregisterImsRegistrationCallback(this.mRegistrationCallback);
        } catch (Exception e) {
            Log.e(this.mTag, "removeListeners", e);
        }
    }

    public void notifyListeners(SignalCallback signalCallback) {
        if (!this.mNetworkController.isCarrierMergedWifi(this.mSubscriptionInfo.getSubscriptionId())) {
            SignalIcon$MobileIconGroup signalIcon$MobileIconGroup = (SignalIcon$MobileIconGroup) getIcons();
            String charSequence = getTextIfExists(getContentDescription()).toString();
            CharSequence textIfExists = getTextIfExists(signalIcon$MobileIconGroup.dataContentDescription);
            String obj = Html.fromHtml(textIfExists.toString(), 0).toString();
            if (((MobileState) this.mCurrentState).inetCondition == 0) {
                obj = this.mContext.getString(R$string.data_connection_no_internet);
            }
            String str = obj;
            QsInfo qsInfo = getQsInfo(charSequence, signalIcon$MobileIconGroup.dataType);
            SbInfo sbInfo = getSbInfo(charSequence, signalIcon$MobileIconGroup.dataType);
            int volteResId = this.mConfig.showVolteIcon ? getVolteResId() : 0;
            signalCallback.setMobileDataIndicators(new MobileDataIndicators(sbInfo.icon, qsInfo.icon, sbInfo.ratTypeIcon, qsInfo.ratTypeIcon, ((MobileState) this.mCurrentState).hasActivityIn(), ((MobileState) this.mCurrentState).hasActivityOut(), volteResId, str, textIfExists, qsInfo.description, this.mSubscriptionInfo.getSubscriptionId(), ((MobileState) this.mCurrentState).roaming, sbInfo.showTriangle));
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v0, resolved type: boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v1, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v1, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v2, resolved type: boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v3, resolved type: boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v4, resolved type: boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v2, resolved type: int} */
    /* JADX WARNING: type inference failed for: r4v1, types: [java.lang.CharSequence] */
    /* JADX WARNING: type inference failed for: r4v2 */
    /* JADX WARNING: type inference failed for: r4v4 */
    /* JADX WARNING: type inference failed for: r2v4, types: [java.lang.String] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final com.android.systemui.statusbar.connectivity.MobileSignalController.QsInfo getQsInfo(java.lang.String r5, int r6) {
        /*
            r4 = this;
            T r0 = r4.mCurrentState
            r1 = r0
            com.android.systemui.statusbar.connectivity.MobileState r1 = (com.android.systemui.statusbar.connectivity.MobileState) r1
            boolean r1 = r1.dataSim
            r2 = 0
            r3 = 0
            if (r1 == 0) goto L_0x0052
            r1 = r0
            com.android.systemui.statusbar.connectivity.MobileState r1 = (com.android.systemui.statusbar.connectivity.MobileState) r1
            boolean r1 = r1.isDefault
            if (r1 != 0) goto L_0x0018
            com.android.systemui.statusbar.connectivity.MobileSignalController$QsInfo r4 = new com.android.systemui.statusbar.connectivity.MobileSignalController$QsInfo
            r4.<init>(r3, r2, r2)
            return r4
        L_0x0018:
            com.android.systemui.statusbar.connectivity.MobileState r0 = (com.android.systemui.statusbar.connectivity.MobileState) r0
            boolean r0 = r0.showQuickSettingsRatIcon()
            if (r0 != 0) goto L_0x0028
            com.android.settingslib.mobile.MobileMappings$Config r0 = r4.mConfig
            boolean r0 = r0.alwaysShowDataRatIcon
            if (r0 == 0) goto L_0x0027
            goto L_0x0028
        L_0x0027:
            r6 = r3
        L_0x0028:
            T r0 = r4.mCurrentState
            r1 = r0
            com.android.systemui.statusbar.connectivity.MobileState r1 = (com.android.systemui.statusbar.connectivity.MobileState) r1
            boolean r1 = r1.enabled
            if (r1 == 0) goto L_0x0038
            com.android.systemui.statusbar.connectivity.MobileState r0 = (com.android.systemui.statusbar.connectivity.MobileState) r0
            boolean r0 = r0.isEmergency
            if (r0 != 0) goto L_0x0038
            r3 = 1
        L_0x0038:
            com.android.systemui.statusbar.connectivity.IconState r0 = new com.android.systemui.statusbar.connectivity.IconState
            int r1 = r4.getQsCurrentIconId()
            r0.<init>(r3, r1, r5)
            T r4 = r4.mCurrentState
            r5 = r4
            com.android.systemui.statusbar.connectivity.MobileState r5 = (com.android.systemui.statusbar.connectivity.MobileState) r5
            boolean r5 = r5.isEmergency
            if (r5 != 0) goto L_0x004e
            com.android.systemui.statusbar.connectivity.MobileState r4 = (com.android.systemui.statusbar.connectivity.MobileState) r4
            java.lang.String r2 = r4.networkName
        L_0x004e:
            r3 = r6
            r4 = r2
            r2 = r0
            goto L_0x0053
        L_0x0052:
            r4 = r2
        L_0x0053:
            com.android.systemui.statusbar.connectivity.MobileSignalController$QsInfo r5 = new com.android.systemui.statusbar.connectivity.MobileSignalController$QsInfo
            r5.<init>(r3, r2, r4)
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.connectivity.MobileSignalController.getQsInfo(java.lang.String, int):com.android.systemui.statusbar.connectivity.MobileSignalController$QsInfo");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:26:0x005a, code lost:
        if (((com.android.systemui.statusbar.connectivity.MobileState) r6.mCurrentState).airplaneMode == false) goto L_0x005c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x00aa, code lost:
        if (((com.android.systemui.statusbar.connectivity.MobileState) r7).airplaneMode == false) goto L_0x005c;
     */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x00b3  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x00b8  */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x00df  */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x00e2  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final com.android.systemui.statusbar.connectivity.MobileSignalController.SbInfo getSbInfo(java.lang.String r7, int r8) {
        /*
            r6 = this;
            T r0 = r6.mCurrentState
            com.android.systemui.statusbar.connectivity.MobileState r0 = (com.android.systemui.statusbar.connectivity.MobileState) r0
            boolean r0 = r0.isDataDisabledOrNotDefault()
            boolean r1 = r6.mProviderModelBehavior
            r2 = 1
            r3 = 0
            if (r1 == 0) goto L_0x005e
            T r1 = r6.mCurrentState
            r4 = r1
            com.android.systemui.statusbar.connectivity.MobileState r4 = (com.android.systemui.statusbar.connectivity.MobileState) r4
            boolean r4 = r4.dataConnected
            if (r4 != 0) goto L_0x0019
            if (r0 == 0) goto L_0x0029
        L_0x0019:
            r0 = r1
            com.android.systemui.statusbar.connectivity.MobileState r0 = (com.android.systemui.statusbar.connectivity.MobileState) r0
            boolean r0 = r0.dataSim
            if (r0 == 0) goto L_0x0029
            r0 = r1
            com.android.systemui.statusbar.connectivity.MobileState r0 = (com.android.systemui.statusbar.connectivity.MobileState) r0
            boolean r0 = r0.isDefault
            if (r0 == 0) goto L_0x0029
            r0 = r2
            goto L_0x002a
        L_0x0029:
            r0 = r3
        L_0x002a:
            if (r0 != 0) goto L_0x0038
            com.android.settingslib.mobile.MobileMappings$Config r4 = r6.mConfig
            boolean r5 = r4.alwaysShowDataRatIcon
            if (r5 != 0) goto L_0x0038
            boolean r4 = r4.alwaysShowNetworkTypeIcon
            if (r4 == 0) goto L_0x0037
            goto L_0x0038
        L_0x0037:
            r8 = r3
        L_0x0038:
            r4 = r1
            com.android.systemui.statusbar.connectivity.MobileState r4 = (com.android.systemui.statusbar.connectivity.MobileState) r4
            boolean r4 = r4.roaming
            r0 = r0 | r4
            com.android.systemui.statusbar.connectivity.IconState r4 = new com.android.systemui.statusbar.connectivity.IconState
            if (r0 == 0) goto L_0x004a
            com.android.systemui.statusbar.connectivity.MobileState r1 = (com.android.systemui.statusbar.connectivity.MobileState) r1
            boolean r1 = r1.airplaneMode
            if (r1 != 0) goto L_0x004a
            r1 = r2
            goto L_0x004b
        L_0x004a:
            r1 = r3
        L_0x004b:
            int r5 = r6.getCurrentIconId()
            r4.<init>(r1, r5, r7)
            if (r0 == 0) goto L_0x00ad
            T r7 = r6.mCurrentState
            com.android.systemui.statusbar.connectivity.MobileState r7 = (com.android.systemui.statusbar.connectivity.MobileState) r7
            boolean r7 = r7.airplaneMode
            if (r7 != 0) goto L_0x00ad
        L_0x005c:
            r3 = r2
            goto L_0x00ad
        L_0x005e:
            com.android.systemui.statusbar.connectivity.IconState r4 = new com.android.systemui.statusbar.connectivity.IconState
            T r1 = r6.mCurrentState
            r5 = r1
            com.android.systemui.statusbar.connectivity.MobileState r5 = (com.android.systemui.statusbar.connectivity.MobileState) r5
            boolean r5 = r5.enabled
            if (r5 == 0) goto L_0x0071
            com.android.systemui.statusbar.connectivity.MobileState r1 = (com.android.systemui.statusbar.connectivity.MobileState) r1
            boolean r1 = r1.airplaneMode
            if (r1 != 0) goto L_0x0071
            r1 = r2
            goto L_0x0072
        L_0x0071:
            r1 = r3
        L_0x0072:
            int r5 = r6.getCurrentIconId()
            r4.<init>(r1, r5, r7)
            T r7 = r6.mCurrentState
            r1 = r7
            com.android.systemui.statusbar.connectivity.MobileState r1 = (com.android.systemui.statusbar.connectivity.MobileState) r1
            boolean r1 = r1.dataConnected
            if (r1 == 0) goto L_0x0089
            r1 = r7
            com.android.systemui.statusbar.connectivity.MobileState r1 = (com.android.systemui.statusbar.connectivity.MobileState) r1
            boolean r1 = r1.isDefault
            if (r1 != 0) goto L_0x0094
        L_0x0089:
            com.android.settingslib.mobile.MobileMappings$Config r1 = r6.mConfig
            boolean r1 = r1.alwaysShowNetworkTypeIcon
            if (r1 != 0) goto L_0x0094
            if (r0 == 0) goto L_0x0092
            goto L_0x0094
        L_0x0092:
            r0 = r3
            goto L_0x0095
        L_0x0094:
            r0 = r2
        L_0x0095:
            if (r0 != 0) goto L_0x009f
            com.android.settingslib.mobile.MobileMappings$Config r0 = r6.mConfig
            boolean r0 = r0.alwaysShowDataRatIcon
            if (r0 == 0) goto L_0x009e
            goto L_0x009f
        L_0x009e:
            r8 = r3
        L_0x009f:
            r0 = r7
            com.android.systemui.statusbar.connectivity.MobileState r0 = (com.android.systemui.statusbar.connectivity.MobileState) r0
            boolean r0 = r0.enabled
            if (r0 == 0) goto L_0x00ad
            com.android.systemui.statusbar.connectivity.MobileState r7 = (com.android.systemui.statusbar.connectivity.MobileState) r7
            boolean r7 = r7.airplaneMode
            if (r7 != 0) goto L_0x00ad
            goto L_0x005c
        L_0x00ad:
            com.android.settingslib.mobile.MobileMappings$Config r7 = r6.mConfig
            boolean r0 = r7.enableRatIconEnhancement
            if (r0 == 0) goto L_0x00b8
            int r8 = r6.getEnhancementDataRatIcon()
            goto L_0x00c0
        L_0x00b8:
            boolean r7 = r7.enableDdsRatIconEnhancement
            if (r7 == 0) goto L_0x00c0
            int r8 = r6.getEnhancementDdsRatIcon()
        L_0x00c0:
            com.android.settingslib.SignalIcon$MobileIconGroup r7 = r6.getVowifiIconGroup()
            com.android.settingslib.mobile.MobileMappings$Config r0 = r6.mConfig
            boolean r0 = r0.showVowifiIcon
            if (r0 == 0) goto L_0x00e9
            if (r7 == 0) goto L_0x00e9
            int r8 = r7.dataType
            com.android.systemui.statusbar.connectivity.IconState r7 = new com.android.systemui.statusbar.connectivity.IconState
            T r6 = r6.mCurrentState
            r0 = r6
            com.android.systemui.statusbar.connectivity.MobileState r0 = (com.android.systemui.statusbar.connectivity.MobileState) r0
            boolean r0 = r0.enabled
            if (r0 == 0) goto L_0x00e2
            com.android.systemui.statusbar.connectivity.MobileState r6 = (com.android.systemui.statusbar.connectivity.MobileState) r6
            boolean r6 = r6.airplaneMode
            if (r6 != 0) goto L_0x00e2
            int r6 = r4.icon
            goto L_0x00e3
        L_0x00e2:
            r6 = -1
        L_0x00e3:
            java.lang.String r0 = r4.contentDescription
            r7.<init>(r2, r6, r0)
            r4 = r7
        L_0x00e9:
            com.android.systemui.statusbar.connectivity.MobileSignalController$SbInfo r6 = new com.android.systemui.statusbar.connectivity.MobileSignalController$SbInfo
            r6.<init>(r3, r8, r4)
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.connectivity.MobileSignalController.getSbInfo(java.lang.String, int):com.android.systemui.statusbar.connectivity.MobileSignalController$SbInfo");
    }

    public MobileState cleanState() {
        return new MobileState();
    }

    public boolean isInService() {
        return ((MobileState) this.mCurrentState).isInService();
    }

    public String getNetworkNameForCarrierWiFi() {
        return this.mPhone.getSimOperatorName();
    }

    public final boolean isRoaming() {
        if (isCarrierNetworkChangeActive()) {
            return false;
        }
        if (!((MobileState) this.mCurrentState).isCdma()) {
            return ((MobileState) this.mCurrentState).isRoaming();
        }
        if (this.mPhone.getCdmaEnhancedRoamingIndicatorDisplayNumber() != 1) {
            return true;
        }
        return false;
    }

    public final boolean isCarrierNetworkChangeActive() {
        return ((MobileState) this.mCurrentState).carrierNetworkChangeMode;
    }

    public void handleBroadcast(Intent intent) {
        String action = intent.getAction();
        if (action.equals("android.telephony.action.SERVICE_PROVIDERS_UPDATED")) {
            updateNetworkName(intent.getBooleanExtra("android.telephony.extra.SHOW_SPN", false), intent.getStringExtra("android.telephony.extra.SPN"), intent.getStringExtra("android.telephony.extra.DATA_SPN"), intent.getBooleanExtra("android.telephony.extra.SHOW_PLMN", false), intent.getStringExtra("android.telephony.extra.PLMN"));
            notifyListenersIfNecessary();
        } else if (action.equals("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED")) {
            updateDataSim();
            notifyListenersIfNecessary();
        }
    }

    public final void updateDataSim() {
        int activeDataSubId = this.mDefaults.getActiveDataSubId();
        boolean z = true;
        if (SubscriptionManager.isValidSubscriptionId(activeDataSubId)) {
            MobileState mobileState = (MobileState) this.mCurrentState;
            if (activeDataSubId != this.mSubscriptionInfo.getSubscriptionId()) {
                z = false;
            }
            mobileState.dataSim = z;
            return;
        }
        ((MobileState) this.mCurrentState).dataSim = true;
    }

    public void updateNetworkName(boolean z, String str, String str2, boolean z2, String str3) {
        if (SignalController.CHATTY) {
            Log.d("CarrierLabel", "updateNetworkName showSpn=" + z + " spn=" + str + " dataSpn=" + str2 + " showPlmn=" + z2 + " plmn=" + str3);
        }
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        if (z2 && str3 != null) {
            sb.append(str3);
            sb2.append(str3);
        }
        if (z && str != null) {
            if (sb.length() != 0) {
                sb.append(this.mNetworkNameSeparator);
            }
            sb.append(str);
        }
        if (sb.length() != 0) {
            ((MobileState) this.mCurrentState).networkName = sb.toString();
        } else {
            ((MobileState) this.mCurrentState).networkName = this.mNetworkNameDefault;
        }
        if (z && str2 != null) {
            if (sb2.length() != 0) {
                sb2.append(this.mNetworkNameSeparator);
            }
            sb2.append(str2);
        }
        if (sb2.length() != 0) {
            ((MobileState) this.mCurrentState).networkNameData = sb2.toString();
            return;
        }
        ((MobileState) this.mCurrentState).networkNameData = this.mNetworkNameDefault;
    }

    public final int getCdmaLevel(SignalStrength signalStrength) {
        List<CellSignalStrengthCdma> cellSignalStrengths = signalStrength.getCellSignalStrengths(CellSignalStrengthCdma.class);
        if (!cellSignalStrengths.isEmpty()) {
            return cellSignalStrengths.get(0).getLevel();
        }
        return 0;
    }

    public final void updateMobileStatus(MobileStatusTracker.MobileStatus mobileStatus) {
        int voiceServiceState = ((MobileState) this.mCurrentState).getVoiceServiceState();
        ((MobileState) this.mCurrentState).setFromMobileStatus(mobileStatus);
        notifyMobileLevelChangeIfNecessary(mobileStatus.signalStrength);
        if (this.mProviderModelBehavior) {
            maybeNotifyCallStateChanged(voiceServiceState);
        }
    }

    public final void maybeNotifyCallStateChanged(int i) {
        int voiceServiceState = ((MobileState) this.mCurrentState).getVoiceServiceState();
        if (i != voiceServiceState) {
            if (i == -1 || i == 0 || voiceServiceState == 0) {
                notifyCallStateChange(new IconState(((MobileState) this.mCurrentState).isNoCalling() & (!hideNoCalling()), R$drawable.ic_qs_no_calling_sms, getTextIfExists(AccessibilityContentDescriptions.NO_CALLING).toString()), this.mSubscriptionInfo.getSubscriptionId());
            }
        }
    }

    public void updateNoCallingState() {
        notifyCallStateChange(new IconState((((MobileState) this.mCurrentState).getVoiceServiceState() != 0) & (true ^ hideNoCalling()), R$drawable.ic_qs_no_calling_sms, getTextIfExists(AccessibilityContentDescriptions.NO_CALLING).toString()), this.mSubscriptionInfo.getSubscriptionId());
    }

    public final boolean hideNoCalling() {
        return this.mNetworkController.hasDefaultNetwork() && this.mCarrierConfigTracker.getNoCallingConfig(this.mSubscriptionInfo.getSubscriptionId());
    }

    public final int getCallStrengthIcon(int i, boolean z) {
        if (z) {
            return TelephonyIcons.WIFI_CALL_STRENGTH_ICONS[i];
        }
        return TelephonyIcons.MOBILE_CALL_STRENGTH_ICONS[i];
    }

    public final String getCallStrengthDescription(int i, boolean z) {
        if (z) {
            return getTextIfExists(AccessibilityContentDescriptions.WIFI_CONNECTION_STRENGTH[i]).toString();
        }
        return getTextIfExists(AccessibilityContentDescriptions.PHONE_SIGNAL_STRENGTH[i]).toString();
    }

    public void refreshCallIndicator(SignalCallback signalCallback) {
        IconState iconState = new IconState(((MobileState) this.mCurrentState).isNoCalling() & (!hideNoCalling()), R$drawable.ic_qs_no_calling_sms, getTextIfExists(AccessibilityContentDescriptions.NO_CALLING).toString());
        signalCallback.setCallIndicator(iconState, this.mSubscriptionInfo.getSubscriptionId());
        int i = this.mImsType;
        if (i == 1) {
            iconState = new IconState(true, getCallStrengthIcon(this.mLastWwanLevel, false), getCallStrengthDescription(this.mLastWwanLevel, false));
        } else if (i == 2) {
            iconState = new IconState(true, getCallStrengthIcon(this.mLastWlanLevel, true), getCallStrengthDescription(this.mLastWlanLevel, true));
        } else if (i == 3) {
            iconState = new IconState(true, getCallStrengthIcon(this.mLastWlanCrossSimLevel, false), getCallStrengthDescription(this.mLastWlanCrossSimLevel, false));
        }
        signalCallback.setCallIndicator(iconState, this.mSubscriptionInfo.getSubscriptionId());
    }

    public void notifyWifiLevelChange(int i) {
        if (this.mProviderModelBehavior) {
            this.mLastWlanLevel = i;
            if (this.mImsType == 2) {
                notifyCallStateChange(new IconState(true, getCallStrengthIcon(i, true), getCallStrengthDescription(i, true)), this.mSubscriptionInfo.getSubscriptionId());
            }
        }
    }

    public void notifyDefaultMobileLevelChange(int i) {
        if (this.mProviderModelBehavior) {
            this.mLastWlanCrossSimLevel = i;
            if (this.mImsType == 3) {
                notifyCallStateChange(new IconState(true, getCallStrengthIcon(i, false), getCallStrengthDescription(i, false)), this.mSubscriptionInfo.getSubscriptionId());
            }
        }
    }

    public void notifyMobileLevelChangeIfNecessary(SignalStrength signalStrength) {
        int signalLevel;
        if (this.mProviderModelBehavior && (signalLevel = getSignalLevel(signalStrength)) != this.mLastLevel) {
            this.mLastLevel = signalLevel;
            this.mLastWwanLevel = signalLevel;
            if (this.mImsType == 1) {
                notifyCallStateChange(new IconState(true, getCallStrengthIcon(signalLevel, false), getCallStrengthDescription(signalLevel, false)), this.mSubscriptionInfo.getSubscriptionId());
            }
            if (((MobileState) this.mCurrentState).dataSim) {
                this.mNetworkController.notifyDefaultMobileLevelChange(signalLevel);
            }
        }
    }

    public int getSignalLevel(SignalStrength signalStrength) {
        if (signalStrength == null) {
            return 0;
        }
        if (signalStrength.isGsm() || !this.mConfig.alwaysShowCdmaRssi) {
            return signalStrength.getLevel();
        }
        return getCdmaLevel(signalStrength);
    }

    public final void updateTelephony() {
        int voiceNetworkType;
        boolean z = SignalController.DEBUG;
        if (z) {
            String str = this.mTag;
            Log.d(str, "updateTelephonySignalStrength: hasService=" + ((MobileState) this.mCurrentState).isInService() + " ss=" + ((MobileState) this.mCurrentState).signalStrength + " displayInfo=" + ((MobileState) this.mCurrentState).telephonyDisplayInfo);
        }
        checkDefaultData();
        T t = this.mCurrentState;
        ((MobileState) t).connected = ((MobileState) t).isInService();
        T t2 = this.mCurrentState;
        if (((MobileState) t2).connected) {
            ((MobileState) t2).level = getSignalLevel(((MobileState) t2).signalStrength);
            if (this.mConfig.showRsrpSignalLevelforLTE) {
                if (z) {
                    String str2 = this.mTag;
                    Log.d(str2, "updateTelephony CS:" + ((MobileState) this.mCurrentState).getVoiceNetworkType() + "/" + TelephonyManager.getNetworkTypeName(((MobileState) this.mCurrentState).getVoiceNetworkType()) + ", PS:" + ((MobileState) this.mCurrentState).getDataNetworkType() + "/" + TelephonyManager.getNetworkTypeName(((MobileState) this.mCurrentState).getDataNetworkType()));
                }
                int dataNetworkType = ((MobileState) this.mCurrentState).getDataNetworkType();
                if (dataNetworkType == 13 || dataNetworkType == 19) {
                    T t3 = this.mCurrentState;
                    ((MobileState) t3).level = getAlternateLteLevel(((MobileState) t3).signalStrength);
                } else if (dataNetworkType == 0 && ((voiceNetworkType = ((MobileState) this.mCurrentState).getVoiceNetworkType()) == 13 || voiceNetworkType == 19)) {
                    T t4 = this.mCurrentState;
                    ((MobileState) t4).level = getAlternateLteLevel(((MobileState) t4).signalStrength);
                }
            }
        }
        String iconKey = MobileMappings.getIconKey(((MobileState) this.mCurrentState).telephonyDisplayInfo);
        if (this.mNetworkToIconLookup.get(iconKey) != null) {
            ((MobileState) this.mCurrentState).iconGroup = this.mNetworkToIconLookup.get(iconKey);
        } else {
            ((MobileState) this.mCurrentState).iconGroup = this.mDefaultIcons;
        }
        if (this.mFiveGState.isNrIconTypeValid()) {
            ((MobileState) this.mCurrentState).iconGroup = this.mFiveGState.getIconGroup();
        } else {
            ((MobileState) this.mCurrentState).iconGroup = getNetworkTypeIconGroup();
        }
        T t5 = this.mCurrentState;
        ((MobileState) t5).dataConnected = ((MobileState) t5).isDataConnected();
        ((MobileState) this.mCurrentState).roaming = isRoaming();
        if (isCarrierNetworkChangeActive()) {
            ((MobileState) this.mCurrentState).iconGroup = TelephonyIcons.CARRIER_NETWORK_CHANGE;
        } else if (isDataDisabled() && !this.mConfig.alwaysShowDataRatIcon) {
            if (this.mSubscriptionInfo.getSubscriptionId() != this.mDefaults.getDefaultDataSubId()) {
                ((MobileState) this.mCurrentState).iconGroup = TelephonyIcons.NOT_DEFAULT_DATA;
            } else {
                ((MobileState) this.mCurrentState).iconGroup = TelephonyIcons.DATA_DISABLED;
            }
        }
        boolean isEmergencyOnly = ((MobileState) this.mCurrentState).isEmergencyOnly();
        T t6 = this.mCurrentState;
        if (isEmergencyOnly != ((MobileState) t6).isEmergency) {
            ((MobileState) t6).isEmergency = ((MobileState) t6).isEmergencyOnly();
            this.mNetworkController.recalculateEmergency();
        }
        if (((MobileState) this.mCurrentState).networkName.equals(this.mNetworkNameDefault) && !TextUtils.isEmpty(((MobileState) this.mCurrentState).getOperatorAlphaShort())) {
            T t7 = this.mCurrentState;
            ((MobileState) t7).networkName = ((MobileState) t7).getOperatorAlphaShort();
        }
        if (((MobileState) this.mCurrentState).networkNameData.equals(this.mNetworkNameDefault)) {
            T t8 = this.mCurrentState;
            if (((MobileState) t8).dataSim && !TextUtils.isEmpty(((MobileState) t8).getOperatorAlphaShort())) {
                T t9 = this.mCurrentState;
                ((MobileState) t9).networkNameData = ((MobileState) t9).getOperatorAlphaShort();
            }
        }
        if (this.mConfig.alwaysShowNetworkTypeIcon) {
            T t10 = this.mCurrentState;
            if (!((MobileState) t10).connected) {
                ((MobileState) t10).iconGroup = TelephonyIcons.UNKNOWN;
            } else if (this.mFiveGState.isNrIconTypeValid()) {
                ((MobileState) this.mCurrentState).iconGroup = this.mFiveGState.getIconGroup();
            } else {
                ((MobileState) this.mCurrentState).iconGroup = getNetworkTypeIconGroup();
            }
        }
        ((MobileState) this.mCurrentState).mobileDataEnabled = this.mPhone.isDataEnabled();
        ((MobileState) this.mCurrentState).roamingDataEnabled = this.mPhone.isDataRoamingEnabled();
        notifyListenersIfNecessary();
    }

    public final void checkDefaultData() {
        T t = this.mCurrentState;
        if (((MobileState) t).iconGroup != TelephonyIcons.NOT_DEFAULT_DATA) {
            ((MobileState) t).defaultDataOff = false;
            return;
        }
        ((MobileState) t).defaultDataOff = this.mNetworkController.isDataControllerDisabled();
    }

    public void onMobileDataChanged() {
        checkDefaultData();
        notifyListenersIfNecessary();
    }

    public boolean isDataDisabled() {
        return !this.mPhone.isDataConnectionAllowed();
    }

    public final boolean isCallIdle() {
        return this.mCallState == 0;
    }

    public final int getAlternateLteLevel(SignalStrength signalStrength) {
        int i = 0;
        if (signalStrength == null) {
            Log.e(this.mTag, "getAlternateLteLevel signalStrength is null");
            return 0;
        }
        int lteDbm = signalStrength.getLteDbm();
        if (lteDbm == Integer.MAX_VALUE) {
            int level = signalStrength.getLevel();
            if (SignalController.DEBUG) {
                String str = this.mTag;
                Log.d(str, "getAlternateLteLevel lteRsrp:INVALID  signalStrengthLevel = " + level);
            }
            return level;
        }
        if (lteDbm <= -44) {
            if (lteDbm >= -97) {
                i = 4;
            } else if (lteDbm >= -105) {
                i = 3;
            } else if (lteDbm >= -113) {
                i = 2;
            } else if (lteDbm >= -120) {
                i = 1;
            }
        }
        if (SignalController.DEBUG) {
            String str2 = this.mTag;
            Log.d(str2, "getAlternateLteLevel lteRsrp:" + lteDbm + " rsrpLevel = " + i);
        }
        return i;
    }

    @VisibleForTesting
    public void setActivity(int i) {
        T t = this.mCurrentState;
        boolean z = false;
        ((MobileState) t).activityIn = i == 3 || i == 1;
        MobileState mobileState = (MobileState) t;
        if (i == 3 || i == 2) {
            z = true;
        }
        mobileState.activityOut = z;
        notifyListenersIfNecessary();
    }

    public final void recordLastMobileStatus(String str) {
        String[] strArr = this.mMobileStatusHistory;
        int i = this.mMobileStatusHistoryIndex;
        strArr[i] = str;
        this.mMobileStatusHistoryIndex = (i + 1) % 64;
    }

    @VisibleForTesting
    public void setImsType(int i) {
        this.mImsType = i;
    }

    public void registerFiveGStateListener(FiveGServiceClient fiveGServiceClient) {
        fiveGServiceClient.registerListener(this.mSubscriptionInfo.getSimSlotIndex(), this.mFiveGStateListener);
        this.mClient = fiveGServiceClient;
    }

    public void unregisterFiveGStateListener(FiveGServiceClient fiveGServiceClient) {
        fiveGServiceClient.unregisterListener(this.mSubscriptionInfo.getSimSlotIndex());
    }

    public final SignalIcon$MobileIconGroup getNetworkTypeIconGroup() {
        String str;
        int overrideNetworkType = ((MobileState) this.mCurrentState).telephonyDisplayInfo.getOverrideNetworkType();
        if (overrideNetworkType == 0 || overrideNetworkType == 4 || overrideNetworkType == 3) {
            int networkType = ((MobileState) this.mCurrentState).telephonyDisplayInfo.getNetworkType();
            if (networkType == 0) {
                networkType = ((MobileState) this.mCurrentState).getVoiceNetworkType();
            }
            str = MobileMappings.toIconKey(networkType);
        } else {
            str = MobileMappings.toDisplayIconKey(overrideNetworkType);
        }
        return this.mNetworkToIconLookup.getOrDefault(str, this.mDefaultIcons);
    }

    public final boolean showDataRatIcon() {
        T t = this.mCurrentState;
        return ((MobileState) t).mobileDataEnabled && (((MobileState) t).roamingDataEnabled || !((MobileState) t).roaming);
    }

    public final int getEnhancementDataRatIcon() {
        if (!showDataRatIcon() || !((MobileState) this.mCurrentState).connected) {
            return 0;
        }
        return getRatIconGroup().dataType;
    }

    public final int getEnhancementDdsRatIcon() {
        T t = this.mCurrentState;
        if (!((MobileState) t).dataSim || !((MobileState) t).connected) {
            return 0;
        }
        return getRatIconGroup().dataType;
    }

    public final SignalIcon$MobileIconGroup getRatIconGroup() {
        if (this.mFiveGState.isNrIconTypeValid()) {
            return this.mFiveGState.getIconGroup();
        }
        return getNetworkTypeIconGroup();
    }

    public final boolean isVowifiAvailable() {
        T t = this.mCurrentState;
        return ((MobileState) t).voiceCapable && ((MobileState) t).imsRegistrationTech == 1;
    }

    public final SignalIcon$MobileIconGroup getVowifiIconGroup() {
        if (isVowifiAvailable() && !isCallIdle()) {
            return TelephonyIcons.VOWIFI_CALLING;
        }
        if (isVowifiAvailable()) {
            return TelephonyIcons.VOWIFI;
        }
        return null;
    }

    public void dump(PrintWriter printWriter) {
        super.dump(printWriter);
        printWriter.println("  mSubscription=" + this.mSubscriptionInfo + ",");
        printWriter.println("  mProviderModelBehavior=" + this.mProviderModelBehavior + ",");
        printWriter.println("  mInflateSignalStrengths=" + this.mInflateSignalStrengths + ",");
        printWriter.println("  isDataDisabled=" + isDataDisabled() + ",");
        printWriter.println("  mConfig.enableRatIconEnhancement=" + this.mConfig.enableRatIconEnhancement + ",");
        printWriter.println("  mConfig.enableDdsRatIconEnhancement=" + this.mConfig.enableDdsRatIconEnhancement + ",");
        printWriter.println("  mConfig.alwaysShowNetworkTypeIcon=" + this.mConfig.alwaysShowNetworkTypeIcon + ",");
        printWriter.println("  mConfig.showVowifiIcon=" + this.mConfig.showVowifiIcon + ",");
        printWriter.println("  mConfig.showVolteIcon=" + this.mConfig.showVolteIcon + ",");
        printWriter.println("  mNetworkToIconLookup=" + this.mNetworkToIconLookup + ",");
        printWriter.println("  MobileStatusHistory");
        int i = 0;
        for (int i2 = 0; i2 < 64; i2++) {
            if (this.mMobileStatusHistory[i2] != null) {
                i++;
            }
        }
        int i3 = this.mMobileStatusHistoryIndex + 64;
        while (true) {
            i3--;
            if (i3 >= (this.mMobileStatusHistoryIndex + 64) - i) {
                printWriter.println("  Previous MobileStatus(" + ((this.mMobileStatusHistoryIndex + 64) - i3) + "): " + this.mMobileStatusHistory[i3 & 63]);
            } else {
                printWriter.println("  mFiveGState=" + this.mFiveGState + ",");
                dumpTableData(printWriter);
                return;
            }
        }
    }

    public class FiveGStateListener implements FiveGServiceClient.IFiveGStateListener {
        public FiveGStateListener() {
        }

        public void onStateChanged(FiveGServiceClient.FiveGServiceState fiveGServiceState) {
            if (SignalController.DEBUG) {
                String str = MobileSignalController.this.mTag;
                Log.d(str, "onStateChanged: state=" + fiveGServiceState);
            }
            MobileSignalController mobileSignalController = MobileSignalController.this;
            mobileSignalController.mFiveGState = fiveGServiceState;
            mobileSignalController.updateTelephony();
            MobileSignalController.this.notifyListeners();
        }
    }

    public static final class QsInfo {
        public final CharSequence description;
        public final IconState icon;
        public final int ratTypeIcon;

        public QsInfo(int i, IconState iconState, CharSequence charSequence) {
            this.ratTypeIcon = i;
            this.icon = iconState;
            this.description = charSequence;
        }
    }

    public static final class SbInfo {
        public final IconState icon;
        public final int ratTypeIcon;
        public final boolean showTriangle;

        public SbInfo(boolean z, int i, IconState iconState) {
            this.showTriangle = z;
            this.ratTypeIcon = i;
            this.icon = iconState;
        }
    }
}
