package com.android.systemui.statusbar;

import android.os.Bundle;
import android.telephony.ServiceState;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.view.View;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.systemui.demomode.DemoModeCommandReceiver;
import com.android.systemui.plugins.DarkIconDispatcher;
import com.android.systemui.statusbar.connectivity.IconState;
import com.android.systemui.statusbar.connectivity.NetworkController;
import com.android.systemui.statusbar.connectivity.SignalCallback;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.CarrierConfigTracker;
import com.android.systemui.util.ViewController;
import java.util.ArrayList;

public class OperatorNameViewController extends ViewController<OperatorNameView> {
    public final CarrierConfigTracker mCarrierConfigTracker;
    public final DarkIconDispatcher mDarkIconDispatcher;
    public final DarkIconDispatcher.DarkReceiver mDarkReceiver;
    public final DemoModeCommandReceiver mDemoModeCommandReceiver;
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    public final KeyguardUpdateMonitorCallback mKeyguardUpdateMonitorCallback;
    public final NetworkController mNetworkController;
    public final SignalCallback mSignalCallback;
    public final TelephonyManager mTelephonyManager;
    public final TunerService.Tunable mTunable;
    public final TunerService mTunerService;

    public OperatorNameViewController(OperatorNameView operatorNameView, DarkIconDispatcher darkIconDispatcher, NetworkController networkController, TunerService tunerService, TelephonyManager telephonyManager, KeyguardUpdateMonitor keyguardUpdateMonitor, CarrierConfigTracker carrierConfigTracker) {
        super(operatorNameView);
        this.mDarkReceiver = new OperatorNameViewController$$ExternalSyntheticLambda0(this);
        this.mSignalCallback = new SignalCallback() {
            public void setIsAirplaneMode(IconState iconState) {
                OperatorNameViewController.this.update();
            }
        };
        this.mTunable = new OperatorNameViewController$$ExternalSyntheticLambda1(this);
        this.mKeyguardUpdateMonitorCallback = new KeyguardUpdateMonitorCallback() {
            public void onRefreshCarrierInfo() {
                ((OperatorNameView) OperatorNameViewController.this.mView).updateText(OperatorNameViewController.this.getDefaultSubInfo());
            }
        };
        this.mDemoModeCommandReceiver = new DemoModeCommandReceiver() {
            public void onDemoModeStarted() {
                ((OperatorNameView) OperatorNameViewController.this.mView).setDemoMode(true);
            }

            public void onDemoModeFinished() {
                ((OperatorNameView) OperatorNameViewController.this.mView).setDemoMode(false);
                OperatorNameViewController.this.update();
            }

            public void dispatchDemoCommand(String str, Bundle bundle) {
                ((OperatorNameView) OperatorNameViewController.this.mView).setText(bundle.getString("name"));
            }
        };
        this.mDarkIconDispatcher = darkIconDispatcher;
        this.mNetworkController = networkController;
        this.mTunerService = tunerService;
        this.mTelephonyManager = telephonyManager;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mCarrierConfigTracker = carrierConfigTracker;
    }

    public void onViewAttached() {
        this.mDarkIconDispatcher.addDarkReceiver(this.mDarkReceiver);
        this.mNetworkController.addCallback(this.mSignalCallback);
        this.mTunerService.addTunable(this.mTunable, "show_operator_name");
        this.mKeyguardUpdateMonitor.registerCallback(this.mKeyguardUpdateMonitorCallback);
    }

    public void onViewDetached() {
        this.mDarkIconDispatcher.removeDarkReceiver(this.mDarkReceiver);
        this.mNetworkController.removeCallback(this.mSignalCallback);
        this.mTunerService.removeTunable(this.mTunable);
        this.mKeyguardUpdateMonitor.removeCallback(this.mKeyguardUpdateMonitorCallback);
    }

    public final void update() {
        boolean z = true;
        if (!this.mCarrierConfigTracker.getShowOperatorNameInStatusBarConfig(getDefaultSubInfo().getSubId()) || this.mTunerService.getValue("show_operator_name", 1) == 0) {
            z = false;
        }
        ((OperatorNameView) this.mView).update(z, this.mTelephonyManager.isDataCapable(), getDefaultSubInfo());
    }

    public final SubInfo getDefaultSubInfo() {
        int defaultDataSubscriptionId = SubscriptionManager.getDefaultDataSubscriptionId();
        SubscriptionInfo subscriptionInfoForSubId = this.mKeyguardUpdateMonitor.getSubscriptionInfoForSubId(defaultDataSubscriptionId);
        return new SubInfo(subscriptionInfoForSubId.getSubscriptionId(), subscriptionInfoForSubId.getCarrierName(), this.mKeyguardUpdateMonitor.getSimState(defaultDataSubscriptionId), this.mKeyguardUpdateMonitor.getServiceState(defaultDataSubscriptionId));
    }

    public static class Factory {
        public final CarrierConfigTracker mCarrierConfigTracker;
        public final DarkIconDispatcher mDarkIconDispatcher;
        public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
        public final NetworkController mNetworkController;
        public final TelephonyManager mTelephonyManager;
        public final TunerService mTunerService;

        public Factory(DarkIconDispatcher darkIconDispatcher, NetworkController networkController, TunerService tunerService, TelephonyManager telephonyManager, KeyguardUpdateMonitor keyguardUpdateMonitor, CarrierConfigTracker carrierConfigTracker) {
            this.mDarkIconDispatcher = darkIconDispatcher;
            this.mNetworkController = networkController;
            this.mTunerService = tunerService;
            this.mTelephonyManager = telephonyManager;
            this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
            this.mCarrierConfigTracker = carrierConfigTracker;
        }

        public OperatorNameViewController create(OperatorNameView operatorNameView) {
            return new OperatorNameViewController(operatorNameView, this.mDarkIconDispatcher, this.mNetworkController, this.mTunerService, this.mTelephonyManager, this.mKeyguardUpdateMonitor, this.mCarrierConfigTracker);
        }
    }

    public View getView() {
        return this.mView;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(ArrayList arrayList, float f, int i) {
        T t = this.mView;
        ((OperatorNameView) t).setTextColor(DarkIconDispatcher.getTint(arrayList, t, i));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(String str, String str2) {
        update();
    }

    public static class SubInfo {
        public final CharSequence mCarrierName;
        public final ServiceState mServiceState;
        public final int mSimState;
        public final int mSubId;

        public SubInfo(int i, CharSequence charSequence, int i2, ServiceState serviceState) {
            this.mSubId = i;
            this.mCarrierName = charSequence;
            this.mSimState = i2;
            this.mServiceState = serviceState;
        }

        public int getSubId() {
            return this.mSubId;
        }

        public boolean simReady() {
            return this.mSimState == 5;
        }

        public CharSequence getCarrierName() {
            return this.mCarrierName;
        }

        public boolean stateInService() {
            ServiceState serviceState = this.mServiceState;
            return serviceState != null && serviceState.getState() == 0;
        }
    }
}
