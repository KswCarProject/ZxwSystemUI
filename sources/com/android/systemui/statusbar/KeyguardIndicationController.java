package com.android.systemui.statusbar;

import android.app.IActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.UserInfo;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.hardware.biometrics.BiometricSourceType;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.UserHandle;
import android.os.UserManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.app.IBatteryStats;
import com.android.internal.widget.LockPatternUtils;
import com.android.internal.widget.ViewClippingUtil;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.settingslib.Utils;
import com.android.settingslib.fuelgauge.BatteryStatus;
import com.android.systemui.DejankUtils;
import com.android.systemui.R$color;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import com.android.systemui.R$string;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dock.DockManager;
import com.android.systemui.keyguard.KeyguardIndication;
import com.android.systemui.keyguard.KeyguardIndicationRotateTextViewController;
import com.android.systemui.keyguard.ScreenLifecycle;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.KeyguardIndicationTextView;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.wakelock.SettableWakeLock;
import com.android.systemui.util.wakelock.WakeLock;
import java.io.PrintWriter;
import java.text.NumberFormat;

public class KeyguardIndicationController {
    public String mAlignmentIndication;
    public final DelayableExecutor mBackgroundExecutor;
    public final IBatteryStats mBatteryInfo;
    public int mBatteryLevel;
    public boolean mBatteryOverheated;
    public boolean mBatteryPresent = true;
    public CharSequence mBiometricMessage;
    public final BroadcastDispatcher mBroadcastDispatcher;
    public BroadcastReceiver mBroadcastReceiver;
    public int mChargingSpeed;
    public long mChargingTimeRemaining;
    public int mChargingWattage;
    public final ViewClippingUtil.ClippingParameters mClippingParams = new ViewClippingUtil.ClippingParameters() {
        public boolean shouldFinish(View view) {
            return view == KeyguardIndicationController.this.mIndicationArea;
        }
    };
    public final Context mContext;
    public final DevicePolicyManager mDevicePolicyManager;
    public final DockManager mDockManager;
    public boolean mDozing;
    public boolean mEnableBatteryDefender;
    public final DelayableExecutor mExecutor;
    public final FalsingManager mFalsingManager;
    public final Handler mHandler;
    public final IActivityManager mIActivityManager;
    public ViewGroup mIndicationArea;
    public boolean mInited;
    public ColorStateList mInitialTextColorState;
    public final KeyguardBypassController mKeyguardBypassController;
    public KeyguardStateController.Callback mKeyguardStateCallback;
    public final KeyguardStateController mKeyguardStateController;
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    public final LockPatternUtils mLockPatternUtils;
    public KeyguardIndicationTextView mLockScreenIndicationView;
    public String mMessageToShowOnScreenOn;
    public boolean mOrganizationOwnedDevice;
    public boolean mPowerCharged;
    public boolean mPowerPluggedIn;
    public boolean mPowerPluggedInDock;
    public boolean mPowerPluggedInWired;
    public boolean mPowerPluggedInWireless;
    public String mRestingIndication;
    public KeyguardIndicationRotateTextViewController mRotateTextViewController;
    public ScreenLifecycle mScreenLifecycle;
    public final ScreenLifecycle.Observer mScreenObserver;
    public StatusBarKeyguardViewManager mStatusBarKeyguardViewManager;
    public final StatusBarStateController mStatusBarStateController;
    public StatusBarStateController.StateListener mStatusBarStateListener;
    public KeyguardIndicationTextView mTopIndicationView;
    public CharSequence mTransientIndication;
    public CharSequence mTrustGrantedIndication;
    public KeyguardUpdateMonitorCallback mUpdateMonitorCallback;
    public final UserManager mUserManager;
    public boolean mVisible;
    public final SettableWakeLock mWakeLock;

    public final String getTrustManagedIndication() {
        return null;
    }

    public KeyguardIndicationController(Context context, Looper looper, WakeLock.Builder builder, KeyguardStateController keyguardStateController, StatusBarStateController statusBarStateController, KeyguardUpdateMonitor keyguardUpdateMonitor, DockManager dockManager, BroadcastDispatcher broadcastDispatcher, DevicePolicyManager devicePolicyManager, IBatteryStats iBatteryStats, UserManager userManager, DelayableExecutor delayableExecutor, DelayableExecutor delayableExecutor2, FalsingManager falsingManager, LockPatternUtils lockPatternUtils, ScreenLifecycle screenLifecycle, IActivityManager iActivityManager, KeyguardBypassController keyguardBypassController) {
        ScreenLifecycle screenLifecycle2 = screenLifecycle;
        AnonymousClass2 r2 = new ScreenLifecycle.Observer() {
            public void onScreenTurnedOn() {
                if (KeyguardIndicationController.this.mMessageToShowOnScreenOn != null) {
                    KeyguardIndicationController keyguardIndicationController = KeyguardIndicationController.this;
                    keyguardIndicationController.showBiometricMessage((CharSequence) keyguardIndicationController.mMessageToShowOnScreenOn);
                    KeyguardIndicationController.this.hideBiometricMessageDelayed(5000);
                    KeyguardIndicationController.this.mMessageToShowOnScreenOn = null;
                }
            }
        };
        this.mScreenObserver = r2;
        this.mStatusBarStateListener = new StatusBarStateController.StateListener() {
            public void onStateChanged(int i) {
                KeyguardIndicationController keyguardIndicationController = KeyguardIndicationController.this;
                boolean z = true;
                if (i != 1) {
                    z = false;
                }
                keyguardIndicationController.setVisible(z);
            }

            public void onDozingChanged(boolean z) {
                if (KeyguardIndicationController.this.mDozing != z) {
                    KeyguardIndicationController.this.mDozing = z;
                    if (KeyguardIndicationController.this.mDozing) {
                        KeyguardIndicationController.this.hideBiometricMessage();
                    }
                    KeyguardIndicationController.this.updateDeviceEntryIndication(false);
                }
            }
        };
        this.mKeyguardStateCallback = new KeyguardStateController.Callback() {
            public void onUnlockedChanged() {
                KeyguardIndicationController.this.updateDeviceEntryIndication(false);
            }

            public void onKeyguardShowingChanged() {
                if (!KeyguardIndicationController.this.mKeyguardStateController.isShowing()) {
                    KeyguardIndicationController.this.mTopIndicationView.clearMessages();
                    KeyguardIndicationController.this.mRotateTextViewController.clearMessages();
                    return;
                }
                KeyguardIndicationController.this.updateDeviceEntryIndication(false);
            }
        };
        this.mContext = context;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mDevicePolicyManager = devicePolicyManager;
        this.mKeyguardStateController = keyguardStateController;
        this.mStatusBarStateController = statusBarStateController;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mDockManager = dockManager;
        WakeLock.Builder builder2 = builder;
        this.mWakeLock = new SettableWakeLock(builder.setTag("Doze:KeyguardIndication").build(), "KeyguardIndication");
        this.mBatteryInfo = iBatteryStats;
        this.mUserManager = userManager;
        this.mExecutor = delayableExecutor;
        this.mBackgroundExecutor = delayableExecutor2;
        this.mLockPatternUtils = lockPatternUtils;
        this.mIActivityManager = iActivityManager;
        this.mFalsingManager = falsingManager;
        this.mKeyguardBypassController = keyguardBypassController;
        this.mScreenLifecycle = screenLifecycle2;
        screenLifecycle2.addObserver(r2);
        Looper looper2 = looper;
        this.mHandler = new Handler(looper) {
            public void handleMessage(Message message) {
                int i = message.what;
                if (i == 1) {
                    KeyguardIndicationController.this.hideTransientIndication();
                } else if (i == 2) {
                    KeyguardIndicationController.this.showActionToUnlock();
                } else if (i == 3) {
                    KeyguardIndicationController.this.hideBiometricMessage();
                }
            }
        };
    }

    public void init() {
        if (!this.mInited) {
            this.mInited = true;
            this.mDockManager.addAlignmentStateListener(new KeyguardIndicationController$$ExternalSyntheticLambda5(this));
            this.mKeyguardUpdateMonitor.registerCallback(getKeyguardCallback());
            this.mStatusBarStateController.addCallback(this.mStatusBarStateListener);
            this.mKeyguardStateController.addCallback(this.mKeyguardStateCallback);
            this.mStatusBarStateListener.onDozingChanged(this.mStatusBarStateController.isDozing());
        }
    }

    public void setIndicationArea(ViewGroup viewGroup) {
        this.mIndicationArea = viewGroup;
        this.mTopIndicationView = (KeyguardIndicationTextView) viewGroup.findViewById(R$id.keyguard_indication_text);
        this.mLockScreenIndicationView = (KeyguardIndicationTextView) viewGroup.findViewById(R$id.keyguard_indication_text_bottom);
        KeyguardIndicationTextView keyguardIndicationTextView = this.mTopIndicationView;
        this.mInitialTextColorState = keyguardIndicationTextView != null ? keyguardIndicationTextView.getTextColors() : ColorStateList.valueOf(-1);
        this.mRotateTextViewController = new KeyguardIndicationRotateTextViewController(this.mLockScreenIndicationView, this.mExecutor, this.mStatusBarStateController);
        updateDeviceEntryIndication(false);
        updateOrganizedOwnedDevice();
        if (this.mBroadcastReceiver == null) {
            this.mBroadcastReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    KeyguardIndicationController.this.updateOrganizedOwnedDevice();
                }
            };
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.app.action.DEVICE_POLICY_MANAGER_STATE_CHANGED");
            intentFilter.addAction("android.intent.action.USER_REMOVED");
            this.mBroadcastDispatcher.registerReceiver(this.mBroadcastReceiver, intentFilter);
        }
    }

    public KeyguardUpdateMonitorCallback getKeyguardCallback() {
        if (this.mUpdateMonitorCallback == null) {
            this.mUpdateMonitorCallback = new BaseKeyguardCallback();
        }
        return this.mUpdateMonitorCallback;
    }

    public final void updateLockScreenIndications(boolean z, int i) {
        updateBiometricMessage();
        updateTransient();
        updateLockScreenDisclosureMsg();
        updateLockScreenOwnerInfo();
        updateLockScreenBatteryMsg(z);
        updateLockScreenUserLockedMsg(i);
        updateLockScreenTrustMsg(i, getTrustGrantedIndication(), getTrustManagedIndication());
        updateLockScreenAlignmentMsg();
        updateLockScreenLogoutView();
        updateLockScreenRestingMsg();
    }

    public final void updateOrganizedOwnedDevice() {
        this.mOrganizationOwnedDevice = ((Boolean) DejankUtils.whitelistIpcs(new KeyguardIndicationController$$ExternalSyntheticLambda1(this))).booleanValue();
        updateDeviceEntryIndication(false);
    }

    public final void updateLockScreenDisclosureMsg() {
        if (this.mOrganizationOwnedDevice) {
            this.mBackgroundExecutor.execute(new KeyguardIndicationController$$ExternalSyntheticLambda4(this));
        } else {
            this.mRotateTextViewController.hideIndication(1);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateLockScreenDisclosureMsg$3() {
        this.mExecutor.execute(new KeyguardIndicationController$$ExternalSyntheticLambda6(this, getDisclosureText(getOrganizationOwnedDeviceOrganizationName())));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateLockScreenDisclosureMsg$2(CharSequence charSequence) {
        if (this.mKeyguardStateController.isShowing()) {
            this.mRotateTextViewController.updateIndication(1, new KeyguardIndication.Builder().setMessage(charSequence).setTextColor(this.mInitialTextColorState).build(), false);
        }
    }

    public final CharSequence getDisclosureText(CharSequence charSequence) {
        Resources resources = this.mContext.getResources();
        if (charSequence == null) {
            return this.mDevicePolicyManager.getResources().getString("SystemUi.KEYGUARD_MANAGEMENT_DISCLOSURE", new KeyguardIndicationController$$ExternalSyntheticLambda8(resources));
        }
        if (this.mDevicePolicyManager.isDeviceManaged()) {
            DevicePolicyManager devicePolicyManager = this.mDevicePolicyManager;
            if (devicePolicyManager.getDeviceOwnerType(devicePolicyManager.getDeviceOwnerComponentOnAnyUser()) == 1) {
                return resources.getString(R$string.do_financed_disclosure_with_name, new Object[]{charSequence});
            }
        }
        return this.mDevicePolicyManager.getResources().getString("SystemUi.KEYGUARD_NAMED_MANAGEMENT_DISCLOSURE", new KeyguardIndicationController$$ExternalSyntheticLambda9(resources, charSequence), new Object[]{charSequence});
    }

    public final void updateLockScreenOwnerInfo() {
        this.mBackgroundExecutor.execute(new KeyguardIndicationController$$ExternalSyntheticLambda2(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateLockScreenOwnerInfo$7() {
        String deviceOwnerInfo = this.mLockPatternUtils.getDeviceOwnerInfo();
        if (deviceOwnerInfo == null && this.mLockPatternUtils.isOwnerInfoEnabled(KeyguardUpdateMonitor.getCurrentUser())) {
            deviceOwnerInfo = this.mLockPatternUtils.getOwnerInfo(KeyguardUpdateMonitor.getCurrentUser());
        }
        this.mExecutor.execute(new KeyguardIndicationController$$ExternalSyntheticLambda7(this, deviceOwnerInfo));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateLockScreenOwnerInfo$6(String str) {
        if (TextUtils.isEmpty(str) || !this.mKeyguardStateController.isShowing()) {
            this.mRotateTextViewController.hideIndication(0);
        } else {
            this.mRotateTextViewController.updateIndication(0, new KeyguardIndication.Builder().setMessage(str).setTextColor(this.mInitialTextColorState).build(), false);
        }
    }

    public final void updateLockScreenBatteryMsg(boolean z) {
        if (this.mPowerPluggedIn || this.mEnableBatteryDefender) {
            this.mRotateTextViewController.updateIndication(3, new KeyguardIndication.Builder().setMessage(computePowerIndication()).setTextColor(this.mInitialTextColorState).build(), z);
            return;
        }
        this.mRotateTextViewController.hideIndication(3);
    }

    public final void updateLockScreenUserLockedMsg(int i) {
        if (!this.mKeyguardUpdateMonitor.isUserUnlocked(i)) {
            this.mRotateTextViewController.updateIndication(8, new KeyguardIndication.Builder().setMessage(this.mContext.getResources().getText(17040643)).setTextColor(this.mInitialTextColorState).build(), false);
        } else {
            this.mRotateTextViewController.hideIndication(8);
        }
    }

    public final void updateBiometricMessage() {
        if (this.mDozing) {
            updateDeviceEntryIndication(false);
        } else if (!TextUtils.isEmpty(this.mBiometricMessage)) {
            this.mRotateTextViewController.updateIndication(11, new KeyguardIndication.Builder().setMessage(this.mBiometricMessage).setMinVisibilityMillis(2600L).setTextColor(this.mInitialTextColorState).build(), true);
        } else {
            this.mRotateTextViewController.hideIndication(11);
        }
    }

    public final void updateTransient() {
        if (this.mDozing) {
            updateDeviceEntryIndication(false);
        } else if (!TextUtils.isEmpty(this.mTransientIndication)) {
            this.mRotateTextViewController.showTransient(this.mTransientIndication);
        } else {
            this.mRotateTextViewController.hideTransient();
        }
    }

    public final void updateLockScreenTrustMsg(int i, CharSequence charSequence, CharSequence charSequence2) {
        boolean userHasTrust = this.mKeyguardUpdateMonitor.getUserHasTrust(i);
        if (!TextUtils.isEmpty(charSequence) && userHasTrust) {
            this.mRotateTextViewController.updateIndication(6, new KeyguardIndication.Builder().setMessage(charSequence).setTextColor(this.mInitialTextColorState).build(), true);
            hideBiometricMessage();
        } else if (TextUtils.isEmpty(charSequence2) || !this.mKeyguardUpdateMonitor.getUserTrustIsManaged(i) || userHasTrust) {
            this.mRotateTextViewController.hideIndication(6);
        } else {
            this.mRotateTextViewController.updateIndication(6, new KeyguardIndication.Builder().setMessage(charSequence2).setTextColor(this.mInitialTextColorState).build(), false);
        }
    }

    public final void updateLockScreenAlignmentMsg() {
        if (!TextUtils.isEmpty(this.mAlignmentIndication)) {
            this.mRotateTextViewController.updateIndication(4, new KeyguardIndication.Builder().setMessage(this.mAlignmentIndication).setTextColor(ColorStateList.valueOf(this.mContext.getColor(R$color.misalignment_text_color))).build(), true);
        } else {
            this.mRotateTextViewController.hideIndication(4);
        }
    }

    public final void updateLockScreenRestingMsg() {
        if (TextUtils.isEmpty(this.mRestingIndication) || this.mRotateTextViewController.hasIndications()) {
            this.mRotateTextViewController.hideIndication(7);
        } else {
            this.mRotateTextViewController.updateIndication(7, new KeyguardIndication.Builder().setMessage(this.mRestingIndication).setTextColor(this.mInitialTextColorState).build(), false);
        }
    }

    public final void updateLockScreenLogoutView() {
        if (this.mKeyguardUpdateMonitor.isLogoutEnabled() && KeyguardUpdateMonitor.getCurrentUser() != 0) {
            this.mRotateTextViewController.updateIndication(2, new KeyguardIndication.Builder().setMessage(this.mContext.getResources().getString(17040404)).setTextColor(Utils.getColorAttr(this.mContext, 17957103)).setBackground(this.mContext.getDrawable(R$drawable.logout_button_background)).setClickListener(new KeyguardIndicationController$$ExternalSyntheticLambda3(this)).build(), false);
        } else {
            this.mRotateTextViewController.hideIndication(2);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateLockScreenLogoutView$8(View view) {
        if (!this.mFalsingManager.isFalseTap(1)) {
            KeyguardUpdateMonitor.getCurrentUser();
            this.mDevicePolicyManager.logoutUser();
        }
    }

    public final boolean isOrganizationOwnedDevice() {
        return this.mDevicePolicyManager.isDeviceManaged() || this.mDevicePolicyManager.isOrganizationOwnedDeviceWithManagedProfile();
    }

    public final CharSequence getOrganizationOwnedDeviceOrganizationName() {
        if (this.mDevicePolicyManager.isDeviceManaged()) {
            return this.mDevicePolicyManager.getDeviceOwnerOrganizationName();
        }
        if (this.mDevicePolicyManager.isOrganizationOwnedDeviceWithManagedProfile()) {
            return getWorkProfileOrganizationName();
        }
        return null;
    }

    public final CharSequence getWorkProfileOrganizationName() {
        int workProfileUserId = getWorkProfileUserId(UserHandle.myUserId());
        if (workProfileUserId == -10000) {
            return null;
        }
        return this.mDevicePolicyManager.getOrganizationNameForUser(workProfileUserId);
    }

    public final int getWorkProfileUserId(int i) {
        for (UserInfo userInfo : this.mUserManager.getProfiles(i)) {
            if (userInfo.isManagedProfile()) {
                return userInfo.id;
            }
        }
        return -10000;
    }

    public void setVisible(boolean z) {
        this.mVisible = z;
        this.mIndicationArea.setVisibility(z ? 0 : 8);
        if (z) {
            if (!this.mHandler.hasMessages(1)) {
                hideTransientIndication();
            }
            updateDeviceEntryIndication(false);
        } else if (!z) {
            hideTransientIndication();
        }
    }

    @VisibleForTesting
    public String getTrustGrantedIndication() {
        if (TextUtils.isEmpty(this.mTrustGrantedIndication)) {
            return this.mContext.getString(R$string.keyguard_indication_trust_unlocked);
        }
        return this.mTrustGrantedIndication.toString();
    }

    @VisibleForTesting
    public void setPowerPluggedIn(boolean z) {
        this.mPowerPluggedIn = z;
    }

    public void hideTransientIndicationDelayed(long j) {
        Handler handler = this.mHandler;
        handler.sendMessageDelayed(handler.obtainMessage(1), j);
    }

    public void hideBiometricMessageDelayed(long j) {
        Handler handler = this.mHandler;
        handler.sendMessageDelayed(handler.obtainMessage(3), j);
    }

    public void showTransientIndication(int i) {
        showTransientIndication((CharSequence) this.mContext.getResources().getString(i));
    }

    public final void showTransientIndication(CharSequence charSequence) {
        this.mTransientIndication = charSequence;
        this.mHandler.removeMessages(1);
        hideTransientIndicationDelayed(5000);
        updateTransient();
    }

    public void showBiometricMessage(int i) {
        showBiometricMessage((CharSequence) this.mContext.getResources().getString(i));
    }

    public final void showBiometricMessage(CharSequence charSequence) {
        if (!TextUtils.equals(charSequence, this.mBiometricMessage)) {
            this.mBiometricMessage = charSequence;
            this.mHandler.removeMessages(2);
            this.mHandler.removeMessages(3);
            hideBiometricMessageDelayed(5000);
            updateBiometricMessage();
        }
    }

    public final void hideBiometricMessage() {
        if (this.mBiometricMessage != null) {
            this.mBiometricMessage = null;
            this.mHandler.removeMessages(3);
            updateBiometricMessage();
        }
    }

    public void hideTransientIndication() {
        if (this.mTransientIndication != null) {
            this.mTransientIndication = null;
            this.mHandler.removeMessages(1);
            updateTransient();
        }
    }

    public final void updateDeviceEntryIndication(boolean z) {
        CharSequence charSequence;
        if (this.mVisible) {
            this.mIndicationArea.setVisibility(0);
            if (this.mDozing) {
                this.mLockScreenIndicationView.setVisibility(8);
                this.mTopIndicationView.setVisibility(0);
                this.mTopIndicationView.setTextColor(-1);
                if (!TextUtils.isEmpty(this.mBiometricMessage)) {
                    charSequence = this.mBiometricMessage;
                } else if (!TextUtils.isEmpty(this.mTransientIndication)) {
                    charSequence = this.mTransientIndication;
                } else if (!this.mBatteryPresent) {
                    this.mIndicationArea.setVisibility(8);
                    return;
                } else if (!TextUtils.isEmpty(this.mAlignmentIndication)) {
                    charSequence = this.mAlignmentIndication;
                    this.mTopIndicationView.setTextColor(this.mContext.getColor(R$color.misalignment_text_color));
                } else if (this.mPowerPluggedIn || this.mEnableBatteryDefender) {
                    charSequence = computePowerIndication();
                } else {
                    charSequence = NumberFormat.getPercentInstance().format((double) (((float) this.mBatteryLevel) / 100.0f));
                }
                if (!TextUtils.equals(this.mTopIndicationView.getText(), charSequence)) {
                    this.mWakeLock.setAcquired(true);
                    this.mTopIndicationView.switchIndication(charSequence, (KeyguardIndication) null, true, new KeyguardIndicationController$$ExternalSyntheticLambda0(this));
                    return;
                }
                return;
            }
            this.mTopIndicationView.setVisibility(8);
            this.mTopIndicationView.setText((CharSequence) null);
            this.mLockScreenIndicationView.setVisibility(0);
            updateLockScreenIndications(z, KeyguardUpdateMonitor.getCurrentUser());
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateDeviceEntryIndication$9() {
        this.mWakeLock.setAcquired(false);
    }

    public String computePowerIndication() {
        int i;
        if (this.mBatteryOverheated) {
            return this.mContext.getResources().getString(R$string.keyguard_plugged_in_charging_limited, new Object[]{NumberFormat.getPercentInstance().format((double) (((float) this.mBatteryLevel) / 100.0f))});
        } else if (this.mPowerCharged) {
            return this.mContext.getResources().getString(R$string.keyguard_charged);
        } else {
            boolean z = this.mChargingTimeRemaining > 0;
            if (this.mPowerPluggedInWired) {
                int i2 = this.mChargingSpeed;
                if (i2 != 0) {
                    if (i2 != 2) {
                        if (z) {
                            i = R$string.keyguard_indication_charging_time;
                        } else {
                            i = R$string.keyguard_plugged_in;
                        }
                    } else if (z) {
                        i = R$string.keyguard_indication_charging_time_fast;
                    } else {
                        i = R$string.keyguard_plugged_in_charging_fast;
                    }
                } else if (z) {
                    i = R$string.keyguard_indication_charging_time_slowly;
                } else {
                    i = R$string.keyguard_plugged_in_charging_slowly;
                }
            } else if (this.mPowerPluggedInWireless) {
                if (z) {
                    i = R$string.keyguard_indication_charging_time_wireless;
                } else {
                    i = R$string.keyguard_plugged_in_wireless;
                }
            } else if (this.mPowerPluggedInDock) {
                if (z) {
                    i = R$string.keyguard_indication_charging_time_dock;
                } else {
                    i = R$string.keyguard_plugged_in_dock;
                }
            } else if (z) {
                i = R$string.keyguard_indication_charging_time;
            } else {
                i = R$string.keyguard_plugged_in;
            }
            String format = NumberFormat.getPercentInstance().format((double) (((float) this.mBatteryLevel) / 100.0f));
            if (z) {
                return this.mContext.getResources().getString(i, new Object[]{Formatter.formatShortElapsedTimeRoundingUpToMinutes(this.mContext, this.mChargingTimeRemaining), format});
            }
            return this.mContext.getResources().getString(i, new Object[]{format});
        }
    }

    public void setStatusBarKeyguardViewManager(StatusBarKeyguardViewManager statusBarKeyguardViewManager) {
        this.mStatusBarKeyguardViewManager = statusBarKeyguardViewManager;
    }

    public void showActionToUnlock() {
        int i;
        if (this.mDozing && !this.mKeyguardUpdateMonitor.getUserCanSkipBouncer(KeyguardUpdateMonitor.getCurrentUser())) {
            return;
        }
        if (this.mStatusBarKeyguardViewManager.isBouncerShowing()) {
            if (!this.mStatusBarKeyguardViewManager.isShowingAlternateAuth() && this.mKeyguardUpdateMonitor.isFaceEnrolled()) {
                this.mStatusBarKeyguardViewManager.showBouncerMessage(this.mContext.getString(R$string.keyguard_retry), this.mInitialTextColorState);
            }
        } else if (!this.mKeyguardUpdateMonitor.isUdfpsSupported() || !this.mKeyguardUpdateMonitor.getUserCanSkipBouncer(KeyguardUpdateMonitor.getCurrentUser())) {
            showBiometricMessage((CharSequence) this.mContext.getString(R$string.keyguard_unlock));
        } else {
            if (this.mKeyguardUpdateMonitor.getIsFaceAuthenticated()) {
                i = R$string.keyguard_face_successful_unlock_press;
            } else {
                i = R$string.keyguard_unlock_press;
            }
            showBiometricMessage((CharSequence) this.mContext.getString(i));
        }
    }

    public final void showFaceFailedTryFingerprintMsg(int i, String str) {
        showBiometricMessage(R$string.keyguard_face_failed_use_fp);
        if (!TextUtils.isEmpty(str)) {
            this.mLockScreenIndicationView.announceForAccessibility(str);
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("KeyguardIndicationController:");
        printWriter.println("  mInitialTextColorState: " + this.mInitialTextColorState);
        printWriter.println("  mPowerPluggedInWired: " + this.mPowerPluggedInWired);
        printWriter.println("  mPowerPluggedIn: " + this.mPowerPluggedIn);
        printWriter.println("  mPowerCharged: " + this.mPowerCharged);
        printWriter.println("  mChargingSpeed: " + this.mChargingSpeed);
        printWriter.println("  mChargingWattage: " + this.mChargingWattage);
        printWriter.println("  mMessageToShowOnScreenOn: " + this.mMessageToShowOnScreenOn);
        printWriter.println("  mDozing: " + this.mDozing);
        printWriter.println("  mTransientIndication: " + this.mTransientIndication);
        printWriter.println("  mBiometricMessage: " + this.mBiometricMessage);
        printWriter.println("  mBatteryLevel: " + this.mBatteryLevel);
        printWriter.println("  mBatteryPresent: " + this.mBatteryPresent);
        StringBuilder sb = new StringBuilder();
        sb.append("  AOD text: ");
        KeyguardIndicationTextView keyguardIndicationTextView = this.mTopIndicationView;
        sb.append(keyguardIndicationTextView == null ? null : keyguardIndicationTextView.getText());
        printWriter.println(sb.toString());
        printWriter.println("  computePowerIndication(): " + computePowerIndication());
        printWriter.println("  trustGrantedIndication: " + getTrustGrantedIndication());
        this.mRotateTextViewController.dump(printWriter, strArr);
    }

    public class BaseKeyguardCallback extends KeyguardUpdateMonitorCallback {
        public BaseKeyguardCallback() {
        }

        public void onTimeChanged() {
            if (KeyguardIndicationController.this.mVisible) {
                KeyguardIndicationController.this.updateDeviceEntryIndication(false);
            }
        }

        public void onRefreshBatteryInfo(BatteryStatus batteryStatus) {
            boolean z = false;
            boolean z2 = batteryStatus.status == 2 || batteryStatus.isCharged();
            boolean r3 = KeyguardIndicationController.this.mPowerPluggedIn;
            KeyguardIndicationController.this.mPowerPluggedInWired = batteryStatus.isPluggedInWired() && z2;
            KeyguardIndicationController.this.mPowerPluggedInWireless = batteryStatus.isPluggedInWireless() && z2;
            KeyguardIndicationController.this.mPowerPluggedInDock = batteryStatus.isPluggedInDock() && z2;
            KeyguardIndicationController.this.mPowerPluggedIn = batteryStatus.isPluggedIn() && z2;
            KeyguardIndicationController.this.mPowerCharged = batteryStatus.isCharged();
            KeyguardIndicationController.this.mChargingWattage = batteryStatus.maxChargingWattage;
            KeyguardIndicationController keyguardIndicationController = KeyguardIndicationController.this;
            keyguardIndicationController.mChargingSpeed = batteryStatus.getChargingSpeed(keyguardIndicationController.mContext);
            KeyguardIndicationController.this.mBatteryLevel = batteryStatus.level;
            KeyguardIndicationController.this.mBatteryPresent = batteryStatus.present;
            KeyguardIndicationController.this.mBatteryOverheated = batteryStatus.isOverheated();
            KeyguardIndicationController keyguardIndicationController2 = KeyguardIndicationController.this;
            keyguardIndicationController2.mEnableBatteryDefender = keyguardIndicationController2.mBatteryOverheated && batteryStatus.isPluggedIn();
            try {
                KeyguardIndicationController keyguardIndicationController3 = KeyguardIndicationController.this;
                keyguardIndicationController3.mChargingTimeRemaining = keyguardIndicationController3.mPowerPluggedIn ? KeyguardIndicationController.this.mBatteryInfo.computeChargeTimeRemaining() : -1;
            } catch (RemoteException e) {
                Log.e("KeyguardIndication", "Error calling IBatteryStats: ", e);
                KeyguardIndicationController.this.mChargingTimeRemaining = -1;
            }
            KeyguardIndicationController keyguardIndicationController4 = KeyguardIndicationController.this;
            if (!r3 && keyguardIndicationController4.mPowerPluggedInWired) {
                z = true;
            }
            keyguardIndicationController4.updateDeviceEntryIndication(z);
            if (!KeyguardIndicationController.this.mDozing) {
                return;
            }
            if (!r3 && KeyguardIndicationController.this.mPowerPluggedIn) {
                KeyguardIndicationController keyguardIndicationController5 = KeyguardIndicationController.this;
                keyguardIndicationController5.showTransientIndication((CharSequence) keyguardIndicationController5.computePowerIndication());
            } else if (r3 && !KeyguardIndicationController.this.mPowerPluggedIn) {
                KeyguardIndicationController.this.hideTransientIndication();
            }
        }

        public void onBiometricHelp(int i, String str, BiometricSourceType biometricSourceType) {
            boolean z = true;
            if (KeyguardIndicationController.this.mKeyguardUpdateMonitor.isUnlockingWithBiometricAllowed(true)) {
                if (i != -2) {
                    z = false;
                }
                if (KeyguardIndicationController.this.mStatusBarKeyguardViewManager.isBouncerShowing()) {
                    KeyguardIndicationController.this.mStatusBarKeyguardViewManager.showBouncerMessage(str, KeyguardIndicationController.this.mInitialTextColorState);
                } else if (KeyguardIndicationController.this.mScreenLifecycle.getScreenState() == 2) {
                    KeyguardIndicationController.this.showBiometricMessage((CharSequence) str);
                } else if (z) {
                    KeyguardIndicationController.this.mHandler.sendMessageDelayed(KeyguardIndicationController.this.mHandler.obtainMessage(2), 1300);
                }
            }
        }

        public void onBiometricError(int i, String str, BiometricSourceType biometricSourceType) {
            if (!shouldSuppressBiometricError(i, biometricSourceType, KeyguardIndicationController.this.mKeyguardUpdateMonitor)) {
                if (i == 3) {
                    if (!KeyguardIndicationController.this.mStatusBarKeyguardViewManager.isBouncerShowing() && KeyguardIndicationController.this.mKeyguardUpdateMonitor.isUdfpsEnrolled() && KeyguardIndicationController.this.mKeyguardUpdateMonitor.isFingerprintDetectionRunning()) {
                        KeyguardIndicationController.this.showFaceFailedTryFingerprintMsg(i, str);
                    } else if (KeyguardIndicationController.this.mStatusBarKeyguardViewManager.isShowingAlternateAuth()) {
                        KeyguardIndicationController.this.mStatusBarKeyguardViewManager.showBouncerMessage(KeyguardIndicationController.this.mContext.getResources().getString(R$string.keyguard_try_fingerprint), KeyguardIndicationController.this.mInitialTextColorState);
                    } else {
                        KeyguardIndicationController.this.showActionToUnlock();
                    }
                } else if (KeyguardIndicationController.this.mStatusBarKeyguardViewManager.isBouncerShowing()) {
                    KeyguardIndicationController.this.mStatusBarKeyguardViewManager.showBouncerMessage(str, KeyguardIndicationController.this.mInitialTextColorState);
                } else if (KeyguardIndicationController.this.mScreenLifecycle.getScreenState() == 2) {
                    KeyguardIndicationController.this.showBiometricMessage((CharSequence) str);
                } else {
                    KeyguardIndicationController.this.mMessageToShowOnScreenOn = str;
                }
            }
        }

        public final boolean shouldSuppressBiometricError(int i, BiometricSourceType biometricSourceType, KeyguardUpdateMonitor keyguardUpdateMonitor) {
            if (biometricSourceType == BiometricSourceType.FINGERPRINT) {
                return shouldSuppressFingerprintError(i, keyguardUpdateMonitor);
            }
            if (biometricSourceType == BiometricSourceType.FACE) {
                return shouldSuppressFaceError(i, keyguardUpdateMonitor);
            }
            return false;
        }

        public final boolean shouldSuppressFingerprintError(int i, KeyguardUpdateMonitor keyguardUpdateMonitor) {
            return (!keyguardUpdateMonitor.isUnlockingWithBiometricAllowed(true) && i != 9) || i == 5 || i == 10;
        }

        public final boolean shouldSuppressFaceError(int i, KeyguardUpdateMonitor keyguardUpdateMonitor) {
            return (!keyguardUpdateMonitor.isUnlockingWithBiometricAllowed(true) && i != 9) || i == 5;
        }

        public void onTrustChanged(int i) {
            if (KeyguardUpdateMonitor.getCurrentUser() == i) {
                KeyguardIndicationController.this.updateDeviceEntryIndication(false);
            }
        }

        public void showTrustGrantedMessage(CharSequence charSequence) {
            KeyguardIndicationController.this.mTrustGrantedIndication = charSequence;
            KeyguardIndicationController.this.updateDeviceEntryIndication(false);
        }

        public void onTrustAgentErrorMessage(CharSequence charSequence) {
            KeyguardIndicationController.this.showBiometricMessage(charSequence);
        }

        public void onBiometricRunningStateChanged(boolean z, BiometricSourceType biometricSourceType) {
            if (z && biometricSourceType == BiometricSourceType.FACE) {
                KeyguardIndicationController.this.hideBiometricMessage();
                KeyguardIndicationController.this.mMessageToShowOnScreenOn = null;
            }
        }

        public void onBiometricAuthenticated(int i, BiometricSourceType biometricSourceType, boolean z) {
            super.onBiometricAuthenticated(i, biometricSourceType, z);
            KeyguardIndicationController.this.hideBiometricMessage();
            if (biometricSourceType == BiometricSourceType.FACE && !KeyguardIndicationController.this.mKeyguardBypassController.canBypass()) {
                KeyguardIndicationController.this.showActionToUnlock();
            }
        }

        public void onUserSwitchComplete(int i) {
            if (KeyguardIndicationController.this.mVisible) {
                KeyguardIndicationController.this.updateDeviceEntryIndication(false);
            }
        }

        public void onUserUnlocked() {
            if (KeyguardIndicationController.this.mVisible) {
                KeyguardIndicationController.this.updateDeviceEntryIndication(false);
            }
        }

        public void onLogoutEnabledChanged() {
            if (KeyguardIndicationController.this.mVisible) {
                KeyguardIndicationController.this.updateDeviceEntryIndication(false);
            }
        }

        public void onRequireUnlockForNfc() {
            KeyguardIndicationController keyguardIndicationController = KeyguardIndicationController.this;
            keyguardIndicationController.showTransientIndication((CharSequence) keyguardIndicationController.mContext.getString(R$string.require_unlock_for_nfc));
            KeyguardIndicationController.this.hideTransientIndicationDelayed(5000);
        }
    }
}
