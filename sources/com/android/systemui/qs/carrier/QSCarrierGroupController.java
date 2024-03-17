package com.android.systemui.qs.carrier;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.android.keyguard.CarrierTextManager;
import com.android.settingslib.AccessibilityContentDescriptions;
import com.android.settingslib.mobile.TelephonyIcons;
import com.android.systemui.R$drawable;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.statusbar.connectivity.IconState;
import com.android.systemui.statusbar.connectivity.MobileDataIndicators;
import com.android.systemui.statusbar.connectivity.NetworkController;
import com.android.systemui.statusbar.connectivity.SignalCallback;
import com.android.systemui.util.CarrierConfigTracker;
import java.util.function.Consumer;

public class QSCarrierGroupController {
    public final ActivityStarter mActivityStarter;
    public final Handler mBgHandler;
    public final Callback mCallback;
    public final CarrierConfigTracker mCarrierConfigTracker;
    public View[] mCarrierDividers;
    public QSCarrier[] mCarrierGroups;
    public final CarrierTextManager mCarrierTextManager;
    public final CellSignalState[] mInfos;
    public boolean mIsSingleCarrier;
    public int[] mLastSignalLevel;
    public String[] mLastSignalLevelDescription;
    public boolean mListening;
    public H mMainHandler;
    public final NetworkController mNetworkController;
    public final TextView mNoSimTextView;
    public OnSingleCarrierChangedListener mOnSingleCarrierChangedListener;
    public final boolean mProviderModel;
    public final SignalCallback mSignalCallback;
    public final SlotIndexResolver mSlotIndexResolver;

    @FunctionalInterface
    public interface OnSingleCarrierChangedListener {
        void onSingleCarrierChanged(boolean z);
    }

    @FunctionalInterface
    public interface SlotIndexResolver {
        int getSlotIndex(int i);
    }

    public static class Callback implements CarrierTextManager.CarrierTextCallback {
        public H mHandler;

        public Callback(H h) {
            this.mHandler = h;
        }

        public void updateCarrierInfo(CarrierTextManager.CarrierTextCallbackInfo carrierTextCallbackInfo) {
            this.mHandler.obtainMessage(0, carrierTextCallbackInfo).sendToTarget();
        }
    }

    public QSCarrierGroupController(QSCarrierGroup qSCarrierGroup, ActivityStarter activityStarter, Handler handler, Looper looper, NetworkController networkController, CarrierTextManager.Builder builder, Context context, CarrierConfigTracker carrierConfigTracker, FeatureFlags featureFlags, SlotIndexResolver slotIndexResolver) {
        QSCarrierGroup qSCarrierGroup2 = qSCarrierGroup;
        Context context2 = context;
        this.mInfos = new CellSignalState[3];
        this.mCarrierDividers = new View[2];
        this.mCarrierGroups = new QSCarrier[3];
        this.mLastSignalLevel = new int[3];
        this.mLastSignalLevelDescription = new String[3];
        this.mSignalCallback = new SignalCallback() {
            public void setMobileDataIndicators(MobileDataIndicators mobileDataIndicators) {
                if (!QSCarrierGroupController.this.mProviderModel) {
                    int slotIndex = QSCarrierGroupController.this.getSlotIndex(mobileDataIndicators.subId);
                    if (slotIndex >= 3) {
                        Log.w("QSCarrierGroup", "setMobileDataIndicators - slot: " + slotIndex);
                    } else if (slotIndex == -1) {
                        Log.e("QSCarrierGroup", "Invalid SIM slot index for subscription: " + mobileDataIndicators.subId);
                    } else {
                        CellSignalState[] r1 = QSCarrierGroupController.this.mInfos;
                        IconState iconState = mobileDataIndicators.statusIcon;
                        r1[slotIndex] = new CellSignalState(iconState.visible, iconState.icon, iconState.contentDescription, mobileDataIndicators.typeContentDescription.toString(), mobileDataIndicators.roaming, QSCarrierGroupController.this.mProviderModel);
                        QSCarrierGroupController.this.mMainHandler.obtainMessage(1).sendToTarget();
                    }
                }
            }

            public void setCallIndicator(IconState iconState, int i) {
                if (QSCarrierGroupController.this.mProviderModel) {
                    int slotIndex = QSCarrierGroupController.this.getSlotIndex(i);
                    if (slotIndex >= 3) {
                        Log.w("QSCarrierGroup", "setMobileDataIndicators - slot: " + slotIndex);
                    } else if (slotIndex == -1) {
                        Log.e("QSCarrierGroup", "Invalid SIM slot index for subscription: " + i);
                    } else {
                        boolean callStrengthConfig = QSCarrierGroupController.this.mCarrierConfigTracker.getCallStrengthConfig(i);
                        int i2 = iconState.icon;
                        int i3 = R$drawable.ic_qs_no_calling_sms;
                        if (i2 == i3) {
                            if (iconState.visible) {
                                QSCarrierGroupController.this.mInfos[slotIndex] = new CellSignalState(true, iconState.icon, iconState.contentDescription, "", false, QSCarrierGroupController.this.mProviderModel);
                            } else if (callStrengthConfig) {
                                QSCarrierGroupController.this.mInfos[slotIndex] = new CellSignalState(true, QSCarrierGroupController.this.mLastSignalLevel[slotIndex], QSCarrierGroupController.this.mLastSignalLevelDescription[slotIndex], "", false, QSCarrierGroupController.this.mProviderModel);
                            } else {
                                QSCarrierGroupController.this.mInfos[slotIndex] = new CellSignalState(true, R$drawable.ic_qs_sim_card, "", "", false, QSCarrierGroupController.this.mProviderModel);
                            }
                            QSCarrierGroupController.this.mMainHandler.obtainMessage(1).sendToTarget();
                            return;
                        }
                        QSCarrierGroupController.this.mLastSignalLevel[slotIndex] = iconState.icon;
                        QSCarrierGroupController.this.mLastSignalLevelDescription[slotIndex] = iconState.contentDescription;
                        if (QSCarrierGroupController.this.mInfos[slotIndex].mobileSignalIconId != i3) {
                            if (callStrengthConfig) {
                                QSCarrierGroupController.this.mInfos[slotIndex] = new CellSignalState(true, iconState.icon, iconState.contentDescription, "", false, QSCarrierGroupController.this.mProviderModel);
                            } else {
                                QSCarrierGroupController.this.mInfos[slotIndex] = new CellSignalState(true, R$drawable.ic_qs_sim_card, "", "", false, QSCarrierGroupController.this.mProviderModel);
                            }
                            QSCarrierGroupController.this.mMainHandler.obtainMessage(1).sendToTarget();
                        }
                    }
                }
            }

            public void setNoSims(boolean z, boolean z2) {
                if (z) {
                    for (int i = 0; i < 3; i++) {
                        QSCarrierGroupController.this.mInfos[i] = QSCarrierGroupController.this.mInfos[i].changeVisibility(false);
                    }
                }
                QSCarrierGroupController.this.mMainHandler.obtainMessage(1).sendToTarget();
            }
        };
        if (featureFlags.isEnabled(Flags.COMBINED_STATUS_BAR_SIGNAL_ICONS)) {
            this.mProviderModel = true;
        } else {
            this.mProviderModel = false;
        }
        this.mActivityStarter = activityStarter;
        this.mBgHandler = handler;
        this.mNetworkController = networkController;
        this.mCarrierTextManager = builder.setShowAirplaneMode(false).setShowMissingSim(false).build();
        this.mCarrierConfigTracker = carrierConfigTracker;
        this.mSlotIndexResolver = slotIndexResolver;
        QSCarrierGroupController$$ExternalSyntheticLambda1 qSCarrierGroupController$$ExternalSyntheticLambda1 = new QSCarrierGroupController$$ExternalSyntheticLambda1(this);
        qSCarrierGroup2.setOnClickListener(qSCarrierGroupController$$ExternalSyntheticLambda1);
        TextView noSimTextView = qSCarrierGroup.getNoSimTextView();
        this.mNoSimTextView = noSimTextView;
        noSimTextView.setOnClickListener(qSCarrierGroupController$$ExternalSyntheticLambda1);
        H h = new H(looper, new QSCarrierGroupController$$ExternalSyntheticLambda2(this), new QSCarrierGroupController$$ExternalSyntheticLambda3(this));
        this.mMainHandler = h;
        this.mCallback = new Callback(h);
        this.mCarrierGroups[0] = qSCarrierGroup.getCarrier1View();
        this.mCarrierGroups[1] = qSCarrierGroup.getCarrier2View();
        this.mCarrierGroups[2] = qSCarrierGroup.getCarrier3View();
        this.mCarrierDividers[0] = qSCarrierGroup.getCarrierDivider1();
        this.mCarrierDividers[1] = qSCarrierGroup.getCarrierDivider2();
        for (int i = 0; i < 3; i++) {
            this.mInfos[i] = new CellSignalState(true, R$drawable.ic_qs_no_calling_sms, context2.getText(AccessibilityContentDescriptions.NO_CALLING).toString(), "", false, this.mProviderModel);
            this.mLastSignalLevel[i] = TelephonyIcons.MOBILE_CALL_STRENGTH_ICONS[0];
            this.mLastSignalLevelDescription[i] = context2.getText(AccessibilityContentDescriptions.PHONE_SIGNAL_STRENGTH[0]).toString();
            this.mCarrierGroups[i].setOnClickListener(qSCarrierGroupController$$ExternalSyntheticLambda1);
        }
        this.mIsSingleCarrier = computeIsSingleCarrier();
        qSCarrierGroup2.setImportantForAccessibility(1);
        qSCarrierGroup2.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            public void onViewAttachedToWindow(View view) {
            }

            public void onViewDetachedFromWindow(View view) {
                QSCarrierGroupController.this.setListening(false);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(View view) {
        if (view.isVisibleToUser()) {
            this.mActivityStarter.postStartActivityDismissingKeyguard(new Intent("android.settings.WIRELESS_SETTINGS"), 0);
        }
    }

    public int getSlotIndex(int i) {
        return this.mSlotIndexResolver.getSlotIndex(i);
    }

    public void setOnSingleCarrierChangedListener(OnSingleCarrierChangedListener onSingleCarrierChangedListener) {
        this.mOnSingleCarrierChangedListener = onSingleCarrierChangedListener;
    }

    public boolean isSingleCarrier() {
        return this.mIsSingleCarrier;
    }

    public final boolean computeIsSingleCarrier() {
        int i = 0;
        for (int i2 = 0; i2 < 3; i2++) {
            if (this.mInfos[i2].visible) {
                i++;
            }
        }
        return i == 1;
    }

    public void setListening(boolean z) {
        if (z != this.mListening) {
            this.mListening = z;
            this.mBgHandler.post(new QSCarrierGroupController$$ExternalSyntheticLambda0(this));
        }
    }

    public final void updateListeners() {
        if (this.mListening) {
            if (this.mNetworkController.hasVoiceCallingFeature()) {
                this.mNetworkController.addCallback(this.mSignalCallback);
            }
            this.mCarrierTextManager.setListening(this.mCallback);
            return;
        }
        this.mNetworkController.removeCallback(this.mSignalCallback);
        this.mCarrierTextManager.setListening((CarrierTextManager.CarrierTextCallback) null);
    }

    public final void handleUpdateState() {
        if (!this.mMainHandler.getLooper().isCurrentThread()) {
            this.mMainHandler.obtainMessage(1).sendToTarget();
            return;
        }
        boolean computeIsSingleCarrier = computeIsSingleCarrier();
        int i = 0;
        if (computeIsSingleCarrier) {
            for (int i2 = 0; i2 < 3; i2++) {
                CellSignalState[] cellSignalStateArr = this.mInfos;
                CellSignalState cellSignalState = cellSignalStateArr[i2];
                if (cellSignalState.visible && cellSignalState.mobileSignalIconId == R$drawable.ic_qs_sim_card) {
                    cellSignalStateArr[i2] = new CellSignalState(true, R$drawable.ic_blank, "", "", false, this.mProviderModel);
                }
            }
        }
        for (int i3 = 0; i3 < 3; i3++) {
            this.mCarrierGroups[i3].updateState(this.mInfos[i3], computeIsSingleCarrier);
        }
        View view = this.mCarrierDividers[0];
        CellSignalState[] cellSignalStateArr2 = this.mInfos;
        view.setVisibility((!cellSignalStateArr2[0].visible || !cellSignalStateArr2[1].visible) ? 8 : 0);
        View view2 = this.mCarrierDividers[1];
        CellSignalState[] cellSignalStateArr3 = this.mInfos;
        if ((!cellSignalStateArr3[1].visible || !cellSignalStateArr3[2].visible) && (!cellSignalStateArr3[0].visible || !cellSignalStateArr3[2].visible)) {
            i = 8;
        }
        view2.setVisibility(i);
        if (this.mIsSingleCarrier != computeIsSingleCarrier) {
            this.mIsSingleCarrier = computeIsSingleCarrier;
            OnSingleCarrierChangedListener onSingleCarrierChangedListener = this.mOnSingleCarrierChangedListener;
            if (onSingleCarrierChangedListener != null) {
                onSingleCarrierChangedListener.onSingleCarrierChanged(computeIsSingleCarrier);
            }
        }
    }

    public final void handleUpdateCarrierInfo(CarrierTextManager.CarrierTextCallbackInfo carrierTextCallbackInfo) {
        if (!this.mMainHandler.getLooper().isCurrentThread()) {
            this.mMainHandler.obtainMessage(0, carrierTextCallbackInfo).sendToTarget();
            return;
        }
        this.mNoSimTextView.setVisibility(8);
        if (carrierTextCallbackInfo.airplaneMode || !carrierTextCallbackInfo.anySimReady) {
            for (int i = 0; i < 3; i++) {
                CellSignalState[] cellSignalStateArr = this.mInfos;
                cellSignalStateArr[i] = cellSignalStateArr[i].changeVisibility(false);
                this.mCarrierGroups[i].setCarrierText("");
                this.mCarrierGroups[i].setVisibility(8);
            }
            this.mNoSimTextView.setText(carrierTextCallbackInfo.carrierText);
            if (!TextUtils.isEmpty(carrierTextCallbackInfo.carrierText)) {
                this.mNoSimTextView.setVisibility(0);
            }
        } else {
            boolean[] zArr = new boolean[3];
            if (carrierTextCallbackInfo.listOfCarriers.length == carrierTextCallbackInfo.subscriptionIds.length) {
                int i2 = 0;
                while (i2 < 3 && i2 < carrierTextCallbackInfo.listOfCarriers.length) {
                    int slotIndex = getSlotIndex(carrierTextCallbackInfo.subscriptionIds[i2]);
                    if (slotIndex >= 3) {
                        Log.w("QSCarrierGroup", "updateInfoCarrier - slot: " + slotIndex);
                    } else if (slotIndex == -1) {
                        Log.e("QSCarrierGroup", "Invalid SIM slot index for subscription: " + carrierTextCallbackInfo.subscriptionIds[i2]);
                    } else {
                        CellSignalState[] cellSignalStateArr2 = this.mInfos;
                        cellSignalStateArr2[slotIndex] = cellSignalStateArr2[slotIndex].changeVisibility(true);
                        zArr[slotIndex] = true;
                        this.mCarrierGroups[slotIndex].setCarrierText(carrierTextCallbackInfo.listOfCarriers[i2].toString().trim());
                        this.mCarrierGroups[slotIndex].setVisibility(0);
                    }
                    i2++;
                }
                for (int i3 = 0; i3 < 3; i3++) {
                    if (!zArr[i3]) {
                        CellSignalState[] cellSignalStateArr3 = this.mInfos;
                        cellSignalStateArr3[i3] = cellSignalStateArr3[i3].changeVisibility(false);
                        this.mCarrierGroups[i3].setVisibility(8);
                    }
                }
            } else {
                Log.e("QSCarrierGroup", "Carrier information arrays not of same length");
            }
        }
        handleUpdateState();
    }

    public static class H extends Handler {
        public Consumer<CarrierTextManager.CarrierTextCallbackInfo> mUpdateCarrierInfo;
        public Runnable mUpdateState;

        public H(Looper looper, Consumer<CarrierTextManager.CarrierTextCallbackInfo> consumer, Runnable runnable) {
            super(looper);
            this.mUpdateCarrierInfo = consumer;
            this.mUpdateState = runnable;
        }

        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 0) {
                this.mUpdateCarrierInfo.accept((CarrierTextManager.CarrierTextCallbackInfo) message.obj);
            } else if (i != 1) {
                super.handleMessage(message);
            } else {
                this.mUpdateState.run();
            }
        }
    }

    public static class Builder {
        public final ActivityStarter mActivityStarter;
        public final CarrierConfigTracker mCarrierConfigTracker;
        public final CarrierTextManager.Builder mCarrierTextControllerBuilder;
        public final Context mContext;
        public final FeatureFlags mFeatureFlags;
        public final Handler mHandler;
        public final Looper mLooper;
        public final NetworkController mNetworkController;
        public final SlotIndexResolver mSlotIndexResolver;
        public QSCarrierGroup mView;

        public Builder(ActivityStarter activityStarter, Handler handler, Looper looper, NetworkController networkController, CarrierTextManager.Builder builder, Context context, CarrierConfigTracker carrierConfigTracker, FeatureFlags featureFlags, SlotIndexResolver slotIndexResolver) {
            this.mActivityStarter = activityStarter;
            this.mHandler = handler;
            this.mLooper = looper;
            this.mNetworkController = networkController;
            this.mCarrierTextControllerBuilder = builder;
            this.mContext = context;
            this.mCarrierConfigTracker = carrierConfigTracker;
            this.mFeatureFlags = featureFlags;
            this.mSlotIndexResolver = slotIndexResolver;
        }

        public Builder setQSCarrierGroup(QSCarrierGroup qSCarrierGroup) {
            this.mView = qSCarrierGroup;
            return this;
        }

        public QSCarrierGroupController build() {
            return new QSCarrierGroupController(this.mView, this.mActivityStarter, this.mHandler, this.mLooper, this.mNetworkController, this.mCarrierTextControllerBuilder, this.mContext, this.mCarrierConfigTracker, this.mFeatureFlags, this.mSlotIndexResolver);
        }
    }

    public static class SubscriptionManagerSlotIndexResolver implements SlotIndexResolver {
        public int getSlotIndex(int i) {
            return SubscriptionManager.getSlotIndex(i);
        }
    }
}
