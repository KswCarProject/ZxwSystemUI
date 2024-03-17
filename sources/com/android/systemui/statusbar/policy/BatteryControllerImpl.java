package com.android.systemui.statusbar.policy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settingslib.fuelgauge.BatterySaverUtils;
import com.android.settingslib.fuelgauge.Estimate;
import com.android.settingslib.utils.PowerUtil;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.demomode.DemoMode;
import com.android.systemui.demomode.DemoModeController;
import com.android.systemui.power.EnhancedEstimates;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.util.Assert;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class BatteryControllerImpl extends BroadcastReceiver implements BatteryController {
    public static final boolean DEBUG = Log.isLoggable("BatteryController", 3);
    public boolean mAodPowerSave;
    public final Handler mBgHandler;
    public final BroadcastDispatcher mBroadcastDispatcher;
    public final ArrayList<BatteryController.BatteryStateChangeCallback> mChangeCallbacks = new ArrayList<>();
    public boolean mCharged;
    public boolean mCharging;
    public final Context mContext;
    public final DemoModeController mDemoModeController;
    public Estimate mEstimate;
    public final EnhancedEstimates mEstimates;
    public final ArrayList<BatteryController.EstimateFetchCompletion> mFetchCallbacks = new ArrayList<>();
    public boolean mFetchingEstimate = false;
    @VisibleForTesting
    public boolean mHasReceivedBattery = false;
    public int mLevel;
    public final Handler mMainHandler;
    public boolean mPluggedIn;
    public boolean mPluggedInWireless;
    public final PowerManager mPowerManager;
    public boolean mPowerSave;
    public AtomicReference<WeakReference<View>> mPowerSaverStartView = new AtomicReference<>();
    public boolean mStateUnknown = false;
    public boolean mTestMode = false;
    public boolean mWirelessCharging;

    @VisibleForTesting
    public BatteryControllerImpl(Context context, EnhancedEstimates enhancedEstimates, PowerManager powerManager, BroadcastDispatcher broadcastDispatcher, DemoModeController demoModeController, Handler handler, Handler handler2) {
        this.mContext = context;
        this.mMainHandler = handler;
        this.mBgHandler = handler2;
        this.mPowerManager = powerManager;
        this.mEstimates = enhancedEstimates;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mDemoModeController = demoModeController;
    }

    public final void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.BATTERY_CHANGED");
        intentFilter.addAction("android.os.action.POWER_SAVE_MODE_CHANGED");
        intentFilter.addAction("com.android.systemui.BATTERY_LEVEL_TEST");
        this.mBroadcastDispatcher.registerReceiver(this, intentFilter);
    }

    public void init() {
        Intent registerReceiver;
        registerReceiver();
        if (!this.mHasReceivedBattery && (registerReceiver = this.mContext.registerReceiver((BroadcastReceiver) null, new IntentFilter("android.intent.action.BATTERY_CHANGED"))) != null && !this.mHasReceivedBattery) {
            onReceive(this.mContext, registerReceiver);
        }
        this.mDemoModeController.addCallback((DemoMode) this);
        updatePowerSave();
        updateEstimateInBackground();
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("BatteryController state:");
        printWriter.print("  mLevel=");
        printWriter.println(this.mLevel);
        printWriter.print("  mPluggedIn=");
        printWriter.println(this.mPluggedIn);
        printWriter.print("  mCharging=");
        printWriter.println(this.mCharging);
        printWriter.print("  mCharged=");
        printWriter.println(this.mCharged);
        printWriter.print("  mPowerSave=");
        printWriter.println(this.mPowerSave);
        printWriter.print("  mStateUnknown=");
        printWriter.println(this.mStateUnknown);
    }

    public void setPowerSaveMode(boolean z, View view) {
        if (z) {
            this.mPowerSaverStartView.set(new WeakReference(view));
        }
        BatterySaverUtils.setPowerSaveMode(this.mContext, z, true);
    }

    public WeakReference<View> getLastPowerSaverStartView() {
        return this.mPowerSaverStartView.get();
    }

    public void clearLastPowerSaverStartView() {
        this.mPowerSaverStartView.set((Object) null);
    }

    public void addCallback(BatteryController.BatteryStateChangeCallback batteryStateChangeCallback) {
        synchronized (this.mChangeCallbacks) {
            this.mChangeCallbacks.add(batteryStateChangeCallback);
        }
        if (this.mHasReceivedBattery) {
            batteryStateChangeCallback.onBatteryLevelChanged(this.mLevel, this.mPluggedIn, this.mCharging);
            batteryStateChangeCallback.onPowerSaveChanged(this.mPowerSave);
            batteryStateChangeCallback.onBatteryUnknownStateChanged(this.mStateUnknown);
            batteryStateChangeCallback.onWirelessChargingChanged(this.mWirelessCharging);
        }
    }

    public void removeCallback(BatteryController.BatteryStateChangeCallback batteryStateChangeCallback) {
        synchronized (this.mChangeCallbacks) {
            this.mChangeCallbacks.remove(batteryStateChangeCallback);
        }
    }

    public void onReceive(final Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals("android.intent.action.BATTERY_CHANGED")) {
            boolean z = false;
            if (!this.mTestMode || intent.getBooleanExtra("testmode", false)) {
                this.mHasReceivedBattery = true;
                this.mLevel = (int) ((((float) intent.getIntExtra("level", 0)) * 100.0f) / ((float) intent.getIntExtra("scale", 100)));
                this.mPluggedIn = intent.getIntExtra("plugged", 0) != 0;
                this.mPluggedInWireless = intent.getIntExtra("plugged", 0) == 4;
                int intExtra = intent.getIntExtra("status", 1);
                boolean z2 = intExtra == 5;
                this.mCharged = z2;
                boolean z3 = z2 || intExtra == 2;
                this.mCharging = z3;
                boolean z4 = this.mWirelessCharging;
                if (z3 && intent.getIntExtra("plugged", 0) == 4) {
                    z = true;
                }
                if (z4 != z) {
                    this.mWirelessCharging = !this.mWirelessCharging;
                    fireWirelessChargingChanged();
                }
                boolean z5 = !intent.getBooleanExtra("present", true);
                if (z5 != this.mStateUnknown) {
                    this.mStateUnknown = z5;
                    fireBatteryUnknownStateChanged();
                }
                fireBatteryLevelChanged();
            }
        } else if (action.equals("android.os.action.POWER_SAVE_MODE_CHANGED")) {
            updatePowerSave();
        } else if (action.equals("com.android.systemui.BATTERY_LEVEL_TEST")) {
            this.mTestMode = true;
            this.mMainHandler.post(new Runnable() {
                public int mCurrentLevel = 0;
                public int mIncrement = 1;
                public int mSavedLevel;
                public boolean mSavedPluggedIn;
                public Intent mTestIntent;

                {
                    this.mSavedLevel = BatteryControllerImpl.this.mLevel;
                    this.mSavedPluggedIn = BatteryControllerImpl.this.mPluggedIn;
                    this.mTestIntent = new Intent("android.intent.action.BATTERY_CHANGED");
                }

                public void run() {
                    int i = this.mCurrentLevel;
                    int i2 = 0;
                    if (i < 0) {
                        BatteryControllerImpl.this.mTestMode = false;
                        this.mTestIntent.putExtra("level", this.mSavedLevel);
                        this.mTestIntent.putExtra("plugged", this.mSavedPluggedIn);
                        this.mTestIntent.putExtra("testmode", false);
                    } else {
                        this.mTestIntent.putExtra("level", i);
                        Intent intent = this.mTestIntent;
                        if (this.mIncrement > 0) {
                            i2 = 1;
                        }
                        intent.putExtra("plugged", i2);
                        this.mTestIntent.putExtra("testmode", true);
                    }
                    context.sendBroadcast(this.mTestIntent);
                    if (BatteryControllerImpl.this.mTestMode) {
                        int i3 = this.mCurrentLevel;
                        int i4 = this.mIncrement;
                        int i5 = i3 + i4;
                        this.mCurrentLevel = i5;
                        if (i5 == 100) {
                            this.mIncrement = i4 * -1;
                        }
                        BatteryControllerImpl.this.mMainHandler.postDelayed(this, 200);
                    }
                }
            });
        }
    }

    public final void fireWirelessChargingChanged() {
        synchronized (this.mChangeCallbacks) {
            this.mChangeCallbacks.forEach(new BatteryControllerImpl$$ExternalSyntheticLambda1(this));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$fireWirelessChargingChanged$0(BatteryController.BatteryStateChangeCallback batteryStateChangeCallback) {
        batteryStateChangeCallback.onWirelessChargingChanged(this.mWirelessCharging);
    }

    public boolean isPluggedIn() {
        return this.mPluggedIn;
    }

    public boolean isPowerSave() {
        return this.mPowerSave;
    }

    public boolean isAodPowerSave() {
        return this.mAodPowerSave;
    }

    public boolean isWirelessCharging() {
        return this.mWirelessCharging;
    }

    public boolean isPluggedInWireless() {
        return this.mPluggedInWireless;
    }

    public void getEstimatedTimeRemainingString(BatteryController.EstimateFetchCompletion estimateFetchCompletion) {
        synchronized (this.mFetchCallbacks) {
            this.mFetchCallbacks.add(estimateFetchCompletion);
        }
        updateEstimateInBackground();
    }

    public final String generateTimeRemainingString() {
        synchronized (this.mFetchCallbacks) {
            Estimate estimate = this.mEstimate;
            if (estimate == null) {
                return null;
            }
            String batteryRemainingShortStringFormatted = PowerUtil.getBatteryRemainingShortStringFormatted(this.mContext, estimate.getEstimateMillis());
            return batteryRemainingShortStringFormatted;
        }
    }

    public final void updateEstimateInBackground() {
        if (!this.mFetchingEstimate) {
            this.mFetchingEstimate = true;
            this.mBgHandler.post(new BatteryControllerImpl$$ExternalSyntheticLambda0(this));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateEstimateInBackground$1() {
        synchronized (this.mFetchCallbacks) {
            this.mEstimate = null;
            if (this.mEstimates.isHybridNotificationEnabled()) {
                updateEstimate();
            }
        }
        this.mFetchingEstimate = false;
        this.mMainHandler.post(new BatteryControllerImpl$$ExternalSyntheticLambda2(this));
    }

    public final void notifyEstimateFetchCallbacks() {
        synchronized (this.mFetchCallbacks) {
            String generateTimeRemainingString = generateTimeRemainingString();
            Iterator<BatteryController.EstimateFetchCompletion> it = this.mFetchCallbacks.iterator();
            while (it.hasNext()) {
                it.next().onBatteryRemainingEstimateRetrieved(generateTimeRemainingString);
            }
            this.mFetchCallbacks.clear();
        }
    }

    public final void updateEstimate() {
        Assert.isNotMainThread();
        Estimate cachedEstimateIfAvailable = Estimate.getCachedEstimateIfAvailable(this.mContext);
        this.mEstimate = cachedEstimateIfAvailable;
        if (cachedEstimateIfAvailable == null) {
            Estimate estimate = this.mEstimates.getEstimate();
            this.mEstimate = estimate;
            if (estimate != null) {
                Estimate.storeCachedEstimate(this.mContext, estimate);
            }
        }
    }

    public final void updatePowerSave() {
        setPowerSave(this.mPowerManager.isPowerSaveMode());
    }

    public final void setPowerSave(boolean z) {
        if (z != this.mPowerSave) {
            this.mPowerSave = z;
            this.mAodPowerSave = this.mPowerManager.getPowerSaveState(14).batterySaverEnabled;
            if (DEBUG) {
                StringBuilder sb = new StringBuilder();
                sb.append("Power save is ");
                sb.append(this.mPowerSave ? "on" : "off");
                Log.d("BatteryController", sb.toString());
            }
            firePowerSaveChanged();
        }
    }

    public void fireBatteryLevelChanged() {
        synchronized (this.mChangeCallbacks) {
            int size = this.mChangeCallbacks.size();
            for (int i = 0; i < size; i++) {
                this.mChangeCallbacks.get(i).onBatteryLevelChanged(this.mLevel, this.mPluggedIn, this.mCharging);
            }
        }
    }

    public final void fireBatteryUnknownStateChanged() {
        synchronized (this.mChangeCallbacks) {
            int size = this.mChangeCallbacks.size();
            for (int i = 0; i < size; i++) {
                this.mChangeCallbacks.get(i).onBatteryUnknownStateChanged(this.mStateUnknown);
            }
        }
    }

    public final void firePowerSaveChanged() {
        synchronized (this.mChangeCallbacks) {
            int size = this.mChangeCallbacks.size();
            for (int i = 0; i < size; i++) {
                this.mChangeCallbacks.get(i).onPowerSaveChanged(this.mPowerSave);
            }
        }
    }

    public void dispatchDemoCommand(String str, Bundle bundle) {
        if (this.mDemoModeController.isInDemoMode()) {
            String string = bundle.getString("level");
            String string2 = bundle.getString("plugged");
            String string3 = bundle.getString("powersave");
            String string4 = bundle.getString("present");
            if (string != null) {
                this.mLevel = Math.min(Math.max(Integer.parseInt(string), 0), 100);
            }
            if (string2 != null) {
                this.mPluggedIn = Boolean.parseBoolean(string2);
            }
            if (string3 != null) {
                this.mPowerSave = string3.equals("true");
                firePowerSaveChanged();
            }
            if (string4 != null) {
                this.mStateUnknown = !string4.equals("true");
                fireBatteryUnknownStateChanged();
            }
            fireBatteryLevelChanged();
        }
    }

    public List<String> demoCommands() {
        ArrayList arrayList = new ArrayList();
        arrayList.add("battery");
        return arrayList;
    }

    public void onDemoModeStarted() {
        this.mBroadcastDispatcher.unregisterReceiver(this);
    }

    public void onDemoModeFinished() {
        registerReceiver();
        updatePowerSave();
    }
}
