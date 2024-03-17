package com.android.systemui.power;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.IThermalEventListener;
import android.os.IThermalService;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.Temperature;
import android.provider.Settings;
import android.util.Log;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settingslib.fuelgauge.Estimate;
import com.android.settingslib.utils.ThreadUtils;
import com.android.systemui.CoreStartable;
import com.android.systemui.R$integer;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import dagger.Lazy;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.Future;

public class PowerUI extends CoreStartable implements CommandQueue.Callbacks {
    public static final boolean DEBUG = Log.isLoggable("PowerUI", 3);
    @VisibleForTesting
    public int mBatteryLevel = 100;
    @VisibleForTesting
    public int mBatteryStatus = 1;
    public final BroadcastDispatcher mBroadcastDispatcher;
    public final Lazy<Optional<CentralSurfaces>> mCentralSurfacesOptionalLazy;
    public final CommandQueue mCommandQueue;
    @VisibleForTesting
    public BatteryStateSnapshot mCurrentBatteryStateSnapshot;
    public boolean mEnableSkinTemperatureWarning;
    public boolean mEnableUsbTemperatureAlarm;
    public final EnhancedEstimates mEnhancedEstimates;
    public final Handler mHandler = new Handler();
    public int mInvalidCharger = 0;
    @VisibleForTesting
    public BatteryStateSnapshot mLastBatteryStateSnapshot;
    public final Configuration mLastConfiguration = new Configuration();
    public Future mLastShowWarningTask;
    public int mLowBatteryAlertCloseLevel;
    public final int[] mLowBatteryReminderLevels = new int[2];
    @VisibleForTesting
    public boolean mLowWarningShownThisChargeCycle;
    public InattentiveSleepWarningView mOverlayView;
    public int mPlugType = 0;
    public final PowerManager mPowerManager;
    @VisibleForTesting
    public final Receiver mReceiver = new Receiver();
    public long mScreenOffTime = -1;
    @VisibleForTesting
    public boolean mSevereWarningShownThisChargeCycle;
    public IThermalEventListener mSkinThermalEventListener;
    @VisibleForTesting
    public IThermalService mThermalService;
    public IThermalEventListener mUsbThermalEventListener;
    public final WarningsUI mWarnings;

    public interface WarningsUI {
        void dismissHighTemperatureWarning();

        void dismissInvalidChargerWarning();

        void dismissLowBatteryWarning();

        void dump(PrintWriter printWriter);

        boolean isInvalidChargerWarningShowing();

        void showHighTemperatureWarning();

        void showInvalidChargerWarning();

        void showLowBatteryWarning(boolean z);

        void showThermalShutdownWarning();

        void showUsbHighTemperatureAlarm();

        void update(int i, int i2, long j);

        void updateLowBatteryWarning();

        void updateSnapshot(BatteryStateSnapshot batteryStateSnapshot);

        void userSwitched();
    }

    public PowerUI(Context context, BroadcastDispatcher broadcastDispatcher, CommandQueue commandQueue, Lazy<Optional<CentralSurfaces>> lazy, WarningsUI warningsUI, EnhancedEstimates enhancedEstimates, PowerManager powerManager) {
        super(context);
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mCommandQueue = commandQueue;
        this.mCentralSurfacesOptionalLazy = lazy;
        this.mWarnings = warningsUI;
        this.mEnhancedEstimates = enhancedEstimates;
        this.mPowerManager = powerManager;
    }

    public void start() {
        this.mScreenOffTime = this.mPowerManager.isScreenOn() ? -1 : SystemClock.elapsedRealtime();
        this.mLastConfiguration.setTo(this.mContext.getResources().getConfiguration());
        AnonymousClass1 r0 = new ContentObserver(this.mHandler) {
            public void onChange(boolean z) {
                PowerUI.this.updateBatteryWarningLevels();
            }
        };
        ContentResolver contentResolver = this.mContext.getContentResolver();
        contentResolver.registerContentObserver(Settings.Global.getUriFor("low_power_trigger_level"), false, r0, -1);
        updateBatteryWarningLevels();
        this.mReceiver.init();
        showWarnOnThermalShutdown();
        contentResolver.registerContentObserver(Settings.Global.getUriFor("show_temperature_warning"), false, new ContentObserver(this.mHandler) {
            public void onChange(boolean z) {
                PowerUI.this.doSkinThermalEventListenerRegistration();
            }
        });
        contentResolver.registerContentObserver(Settings.Global.getUriFor("show_usb_temperature_alarm"), false, new ContentObserver(this.mHandler) {
            public void onChange(boolean z) {
                PowerUI.this.doUsbThermalEventListenerRegistration();
            }
        });
        initThermalEventListeners();
        this.mCommandQueue.addCallback((CommandQueue.Callbacks) this);
    }

    public void onConfigurationChanged(Configuration configuration) {
        if ((this.mLastConfiguration.updateFrom(configuration) & 3) != 0) {
            this.mHandler.post(new PowerUI$$ExternalSyntheticLambda0(this));
        }
    }

    public void updateBatteryWarningLevels() {
        int integer = this.mContext.getResources().getInteger(17694772);
        int integer2 = this.mContext.getResources().getInteger(17694859);
        if (integer2 < integer) {
            integer2 = integer;
        }
        int[] iArr = this.mLowBatteryReminderLevels;
        iArr[0] = integer2;
        iArr[1] = integer;
        this.mLowBatteryAlertCloseLevel = integer2 + this.mContext.getResources().getInteger(17694858);
    }

    public final int findBatteryLevelBucket(int i) {
        if (i >= this.mLowBatteryAlertCloseLevel) {
            return 1;
        }
        int[] iArr = this.mLowBatteryReminderLevels;
        if (i > iArr[0]) {
            return 0;
        }
        for (int length = iArr.length - 1; length >= 0; length--) {
            if (i <= this.mLowBatteryReminderLevels[length]) {
                return -1 - length;
            }
        }
        throw new RuntimeException("not possible!");
    }

    @VisibleForTesting
    public final class Receiver extends BroadcastReceiver {
        public boolean mHasReceivedBattery = false;

        public Receiver() {
        }

        public void init() {
            Intent registerReceiver;
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.os.action.POWER_SAVE_MODE_CHANGED");
            intentFilter.addAction("android.intent.action.BATTERY_CHANGED");
            intentFilter.addAction("android.intent.action.SCREEN_OFF");
            intentFilter.addAction("android.intent.action.SCREEN_ON");
            intentFilter.addAction("android.intent.action.USER_SWITCHED");
            PowerUI.this.mBroadcastDispatcher.registerReceiverWithHandler(this, intentFilter, PowerUI.this.mHandler);
            if (!this.mHasReceivedBattery && (registerReceiver = PowerUI.this.mContext.registerReceiver((BroadcastReceiver) null, new IntentFilter("android.intent.action.BATTERY_CHANGED"))) != null) {
                onReceive(PowerUI.this.mContext, registerReceiver);
            }
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.os.action.POWER_SAVE_MODE_CHANGED".equals(action)) {
                ThreadUtils.postOnBackgroundThread(new PowerUI$Receiver$$ExternalSyntheticLambda0(this));
            } else if ("android.intent.action.BATTERY_CHANGED".equals(action)) {
                this.mHasReceivedBattery = true;
                PowerUI powerUI = PowerUI.this;
                int i = powerUI.mBatteryLevel;
                powerUI.mBatteryLevel = intent.getIntExtra("level", 100);
                PowerUI powerUI2 = PowerUI.this;
                int i2 = powerUI2.mBatteryStatus;
                powerUI2.mBatteryStatus = intent.getIntExtra("status", 1);
                int r0 = PowerUI.this.mPlugType;
                PowerUI.this.mPlugType = intent.getIntExtra("plugged", 1);
                int r4 = PowerUI.this.mInvalidCharger;
                PowerUI.this.mInvalidCharger = intent.getIntExtra("invalid_charger", 0);
                PowerUI powerUI3 = PowerUI.this;
                powerUI3.mLastBatteryStateSnapshot = powerUI3.mCurrentBatteryStateSnapshot;
                boolean z = powerUI3.mPlugType != 0;
                boolean z2 = r0 != 0;
                int r6 = PowerUI.this.findBatteryLevelBucket(i);
                PowerUI powerUI4 = PowerUI.this;
                int r8 = powerUI4.findBatteryLevelBucket(powerUI4.mBatteryLevel);
                boolean z3 = PowerUI.DEBUG;
                if (z3) {
                    Slog.d("PowerUI", "buckets   ....." + PowerUI.this.mLowBatteryAlertCloseLevel + " .. " + PowerUI.this.mLowBatteryReminderLevels[0] + " .. " + PowerUI.this.mLowBatteryReminderLevels[1]);
                    StringBuilder sb = new StringBuilder();
                    sb.append("level          ");
                    sb.append(i);
                    sb.append(" --> ");
                    sb.append(PowerUI.this.mBatteryLevel);
                    Slog.d("PowerUI", sb.toString());
                    Slog.d("PowerUI", "status         " + i2 + " --> " + PowerUI.this.mBatteryStatus);
                    Slog.d("PowerUI", "plugType       " + r0 + " --> " + PowerUI.this.mPlugType);
                    Slog.d("PowerUI", "invalidCharger " + r4 + " --> " + PowerUI.this.mInvalidCharger);
                    Slog.d("PowerUI", "bucket         " + r6 + " --> " + r8);
                    Slog.d("PowerUI", "plugged        " + z2 + " --> " + z);
                }
                WarningsUI r02 = PowerUI.this.mWarnings;
                PowerUI powerUI5 = PowerUI.this;
                r02.update(powerUI5.mBatteryLevel, r8, powerUI5.mScreenOffTime);
                if (r4 != 0 || PowerUI.this.mInvalidCharger == 0) {
                    if (r4 != 0 && PowerUI.this.mInvalidCharger == 0) {
                        PowerUI.this.mWarnings.dismissInvalidChargerWarning();
                    } else if (PowerUI.this.mWarnings.isInvalidChargerWarningShowing()) {
                        if (z3) {
                            Slog.d("PowerUI", "Bad Charger");
                            return;
                        }
                        return;
                    }
                    if (PowerUI.this.mLastShowWarningTask != null) {
                        PowerUI.this.mLastShowWarningTask.cancel(true);
                        if (z3) {
                            Slog.d("PowerUI", "cancelled task");
                        }
                    }
                    PowerUI.this.mLastShowWarningTask = ThreadUtils.postOnBackgroundThread(new PowerUI$Receiver$$ExternalSyntheticLambda1(this, z, r8));
                    return;
                }
                Slog.d("PowerUI", "showing invalid charger warning");
                PowerUI.this.mWarnings.showInvalidChargerWarning();
            } else if ("android.intent.action.SCREEN_OFF".equals(action)) {
                PowerUI.this.mScreenOffTime = SystemClock.elapsedRealtime();
            } else if ("android.intent.action.SCREEN_ON".equals(action)) {
                PowerUI.this.mScreenOffTime = -1;
            } else if ("android.intent.action.USER_SWITCHED".equals(action)) {
                PowerUI.this.mWarnings.userSwitched();
            } else {
                Slog.w("PowerUI", "unknown intent: " + intent);
            }
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onReceive$0() {
            if (PowerUI.this.mPowerManager.isPowerSaveMode()) {
                PowerUI.this.mWarnings.dismissLowBatteryWarning();
            }
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onReceive$1(boolean z, int i) {
            PowerUI.this.maybeShowBatteryWarningV2(z, i);
        }
    }

    public void maybeShowBatteryWarningV2(boolean z, int i) {
        boolean isHybridNotificationEnabled = this.mEnhancedEstimates.isHybridNotificationEnabled();
        boolean isPowerSaveMode = this.mPowerManager.isPowerSaveMode();
        boolean z2 = DEBUG;
        if (z2) {
            Slog.d("PowerUI", "evaluating which notification to show");
        }
        if (isHybridNotificationEnabled) {
            if (z2) {
                Slog.d("PowerUI", "using hybrid");
            }
            Estimate refreshEstimateIfNeeded = refreshEstimateIfNeeded();
            int i2 = this.mBatteryLevel;
            int i3 = this.mBatteryStatus;
            int[] iArr = this.mLowBatteryReminderLevels;
            this.mCurrentBatteryStateSnapshot = new BatteryStateSnapshot(i2, isPowerSaveMode, z, i, i3, iArr[1], iArr[0], refreshEstimateIfNeeded.getEstimateMillis(), refreshEstimateIfNeeded.getAverageDischargeTime(), this.mEnhancedEstimates.getSevereWarningThreshold(), this.mEnhancedEstimates.getLowWarningThreshold(), refreshEstimateIfNeeded.isBasedOnUsage(), this.mEnhancedEstimates.getLowWarningEnabled());
        } else {
            if (z2) {
                Slog.d("PowerUI", "using standard");
            }
            int i4 = this.mBatteryLevel;
            int i5 = this.mBatteryStatus;
            int[] iArr2 = this.mLowBatteryReminderLevels;
            this.mCurrentBatteryStateSnapshot = new BatteryStateSnapshot(i4, isPowerSaveMode, z, i, i5, iArr2[1], iArr2[0]);
        }
        this.mWarnings.updateSnapshot(this.mCurrentBatteryStateSnapshot);
        maybeShowHybridWarning(this.mCurrentBatteryStateSnapshot, this.mLastBatteryStateSnapshot);
    }

    @VisibleForTesting
    public Estimate refreshEstimateIfNeeded() {
        BatteryStateSnapshot batteryStateSnapshot = this.mLastBatteryStateSnapshot;
        if (batteryStateSnapshot != null && batteryStateSnapshot.getTimeRemainingMillis() != -1 && this.mBatteryLevel == this.mLastBatteryStateSnapshot.getBatteryLevel()) {
            return new Estimate(this.mLastBatteryStateSnapshot.getTimeRemainingMillis(), this.mLastBatteryStateSnapshot.isBasedOnUsage(), this.mLastBatteryStateSnapshot.getAverageTimeToDischargeMillis());
        }
        Estimate estimate = this.mEnhancedEstimates.getEstimate();
        if (DEBUG) {
            Slog.d("PowerUI", "updated estimate: " + estimate.getEstimateMillis());
        }
        return estimate;
    }

    @VisibleForTesting
    public void maybeShowHybridWarning(BatteryStateSnapshot batteryStateSnapshot, BatteryStateSnapshot batteryStateSnapshot2) {
        boolean z = false;
        if (batteryStateSnapshot.getBatteryLevel() >= 30) {
            this.mLowWarningShownThisChargeCycle = false;
            this.mSevereWarningShownThisChargeCycle = false;
            if (DEBUG) {
                Slog.d("PowerUI", "Charge cycle reset! Can show warnings again");
            }
        }
        if (batteryStateSnapshot.getBucket() != batteryStateSnapshot2.getBucket() || batteryStateSnapshot2.getPlugged()) {
            z = true;
        }
        if (shouldShowHybridWarning(batteryStateSnapshot)) {
            this.mWarnings.showLowBatteryWarning(z);
            if (batteryStateSnapshot.getBatteryLevel() <= batteryStateSnapshot.getSevereLevelThreshold()) {
                this.mSevereWarningShownThisChargeCycle = true;
                this.mLowWarningShownThisChargeCycle = true;
                if (DEBUG) {
                    Slog.d("PowerUI", "Severe warning marked as shown this cycle");
                    return;
                }
                return;
            }
            Slog.d("PowerUI", "Low warning marked as shown this cycle");
            this.mLowWarningShownThisChargeCycle = true;
        } else if (shouldDismissHybridWarning(batteryStateSnapshot)) {
            if (DEBUG) {
                Slog.d("PowerUI", "Dismissing warning");
            }
            this.mWarnings.dismissLowBatteryWarning();
        } else {
            if (DEBUG) {
                Slog.d("PowerUI", "Updating warning");
            }
            this.mWarnings.updateLowBatteryWarning();
        }
    }

    @VisibleForTesting
    public boolean shouldShowHybridWarning(BatteryStateSnapshot batteryStateSnapshot) {
        boolean z = false;
        boolean z2 = true;
        if (batteryStateSnapshot.getPlugged() || batteryStateSnapshot.getBatteryStatus() == 1) {
            StringBuilder sb = new StringBuilder();
            sb.append("can't show warning due to - plugged: ");
            sb.append(batteryStateSnapshot.getPlugged());
            sb.append(" status unknown: ");
            if (batteryStateSnapshot.getBatteryStatus() != 1) {
                z2 = false;
            }
            sb.append(z2);
            Slog.d("PowerUI", sb.toString());
            return false;
        }
        boolean z3 = !this.mLowWarningShownThisChargeCycle && !batteryStateSnapshot.isPowerSaver() && batteryStateSnapshot.getBatteryLevel() <= batteryStateSnapshot.getLowLevelThreshold();
        boolean z4 = !this.mSevereWarningShownThisChargeCycle && batteryStateSnapshot.getBatteryLevel() <= batteryStateSnapshot.getSevereLevelThreshold();
        if (z3 || z4) {
            z = true;
        }
        if (DEBUG) {
            Slog.d("PowerUI", "Enhanced trigger is: " + z + "\nwith battery snapshot: mLowWarningShownThisChargeCycle: " + this.mLowWarningShownThisChargeCycle + " mSevereWarningShownThisChargeCycle: " + this.mSevereWarningShownThisChargeCycle + "\n" + batteryStateSnapshot.toString());
        }
        return z;
    }

    @VisibleForTesting
    public boolean shouldDismissHybridWarning(BatteryStateSnapshot batteryStateSnapshot) {
        return batteryStateSnapshot.getPlugged() || batteryStateSnapshot.getBatteryLevel() > batteryStateSnapshot.getLowLevelThreshold();
    }

    @VisibleForTesting
    public boolean shouldShowLowBatteryWarning(BatteryStateSnapshot batteryStateSnapshot, BatteryStateSnapshot batteryStateSnapshot2) {
        if (batteryStateSnapshot.getPlugged() || batteryStateSnapshot.isPowerSaver() || ((batteryStateSnapshot.getBucket() >= batteryStateSnapshot2.getBucket() && !batteryStateSnapshot2.getPlugged()) || batteryStateSnapshot.getBucket() >= 0 || batteryStateSnapshot.getBatteryStatus() == 1)) {
            return false;
        }
        return true;
    }

    @VisibleForTesting
    public boolean shouldDismissLowBatteryWarning(BatteryStateSnapshot batteryStateSnapshot, BatteryStateSnapshot batteryStateSnapshot2) {
        return batteryStateSnapshot.isPowerSaver() || batteryStateSnapshot.getPlugged() || (batteryStateSnapshot.getBucket() > batteryStateSnapshot2.getBucket() && batteryStateSnapshot.getBucket() > 0);
    }

    public final void initThermalEventListeners() {
        doSkinThermalEventListenerRegistration();
        doUsbThermalEventListenerRegistration();
    }

    @VisibleForTesting
    public synchronized void doSkinThermalEventListenerRegistration() {
        boolean z;
        boolean z2 = this.mEnableSkinTemperatureWarning;
        boolean z3 = true;
        boolean z4 = Settings.Global.getInt(this.mContext.getContentResolver(), "show_temperature_warning", this.mContext.getResources().getInteger(R$integer.config_showTemperatureWarning)) != 0;
        this.mEnableSkinTemperatureWarning = z4;
        if (z4 != z2) {
            try {
                if (this.mSkinThermalEventListener == null) {
                    this.mSkinThermalEventListener = new SkinThermalEventListener();
                }
                if (this.mThermalService == null) {
                    this.mThermalService = IThermalService.Stub.asInterface(ServiceManager.getService("thermalservice"));
                }
                if (this.mEnableSkinTemperatureWarning) {
                    z = this.mThermalService.registerThermalEventListenerWithType(this.mSkinThermalEventListener, 3);
                } else {
                    z = this.mThermalService.unregisterThermalEventListener(this.mSkinThermalEventListener);
                }
            } catch (RemoteException e) {
                Slog.e("PowerUI", "Exception while (un)registering skin thermal event listener.", e);
                z = false;
            }
            if (!z) {
                if (this.mEnableSkinTemperatureWarning) {
                    z3 = false;
                }
                this.mEnableSkinTemperatureWarning = z3;
                Slog.e("PowerUI", "Failed to register or unregister skin thermal event listener.");
            }
        }
    }

    @VisibleForTesting
    public synchronized void doUsbThermalEventListenerRegistration() {
        boolean z;
        boolean z2 = this.mEnableUsbTemperatureAlarm;
        boolean z3 = true;
        boolean z4 = Settings.Global.getInt(this.mContext.getContentResolver(), "show_usb_temperature_alarm", this.mContext.getResources().getInteger(R$integer.config_showUsbPortAlarm)) != 0;
        this.mEnableUsbTemperatureAlarm = z4;
        if (z4 != z2) {
            try {
                if (this.mUsbThermalEventListener == null) {
                    this.mUsbThermalEventListener = new UsbThermalEventListener();
                }
                if (this.mThermalService == null) {
                    this.mThermalService = IThermalService.Stub.asInterface(ServiceManager.getService("thermalservice"));
                }
                if (this.mEnableUsbTemperatureAlarm) {
                    z = this.mThermalService.registerThermalEventListenerWithType(this.mUsbThermalEventListener, 4);
                } else {
                    z = this.mThermalService.unregisterThermalEventListener(this.mUsbThermalEventListener);
                }
            } catch (RemoteException e) {
                Slog.e("PowerUI", "Exception while (un)registering usb thermal event listener.", e);
                z = false;
            }
            if (!z) {
                if (this.mEnableUsbTemperatureAlarm) {
                    z3 = false;
                }
                this.mEnableUsbTemperatureAlarm = z3;
                Slog.e("PowerUI", "Failed to register or unregister usb thermal event listener.");
            }
        }
    }

    public final void showWarnOnThermalShutdown() {
        int i = -1;
        int i2 = this.mContext.getSharedPreferences("powerui_prefs", 0).getInt("boot_count", -1);
        try {
            i = Settings.Global.getInt(this.mContext.getContentResolver(), "boot_count");
        } catch (Settings.SettingNotFoundException unused) {
            Slog.e("PowerUI", "Failed to read system boot count from Settings.Global.BOOT_COUNT");
        }
        if (i > i2) {
            this.mContext.getSharedPreferences("powerui_prefs", 0).edit().putInt("boot_count", i).apply();
            if (this.mPowerManager.getLastShutdownReason() == 4) {
                this.mWarnings.showThermalShutdownWarning();
            }
        }
    }

    public void showInattentiveSleepWarning() {
        if (this.mOverlayView == null) {
            this.mOverlayView = new InattentiveSleepWarningView(this.mContext);
        }
        this.mOverlayView.show();
    }

    public void dismissInattentiveSleepWarning(boolean z) {
        InattentiveSleepWarningView inattentiveSleepWarningView = this.mOverlayView;
        if (inattentiveSleepWarningView != null) {
            inattentiveSleepWarningView.dismiss(z);
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.print("mLowBatteryAlertCloseLevel=");
        printWriter.println(this.mLowBatteryAlertCloseLevel);
        printWriter.print("mLowBatteryReminderLevels=");
        printWriter.println(Arrays.toString(this.mLowBatteryReminderLevels));
        printWriter.print("mBatteryLevel=");
        printWriter.println(Integer.toString(this.mBatteryLevel));
        printWriter.print("mBatteryStatus=");
        printWriter.println(Integer.toString(this.mBatteryStatus));
        printWriter.print("mPlugType=");
        printWriter.println(Integer.toString(this.mPlugType));
        printWriter.print("mInvalidCharger=");
        printWriter.println(Integer.toString(this.mInvalidCharger));
        printWriter.print("mScreenOffTime=");
        printWriter.print(this.mScreenOffTime);
        if (this.mScreenOffTime >= 0) {
            printWriter.print(" (");
            printWriter.print(SystemClock.elapsedRealtime() - this.mScreenOffTime);
            printWriter.print(" ago)");
        }
        printWriter.println();
        printWriter.print("soundTimeout=");
        printWriter.println(Settings.Global.getInt(this.mContext.getContentResolver(), "low_battery_sound_timeout", 0));
        printWriter.print("bucket: ");
        printWriter.println(Integer.toString(findBatteryLevelBucket(this.mBatteryLevel)));
        printWriter.print("mEnableSkinTemperatureWarning=");
        printWriter.println(this.mEnableSkinTemperatureWarning);
        printWriter.print("mEnableUsbTemperatureAlarm=");
        printWriter.println(this.mEnableUsbTemperatureAlarm);
        this.mWarnings.dump(printWriter);
    }

    @VisibleForTesting
    public final class SkinThermalEventListener extends IThermalEventListener.Stub {
        public SkinThermalEventListener() {
        }

        public void notifyThrottling(Temperature temperature) {
            int status = temperature.getStatus();
            if (status < 5) {
                PowerUI.this.mWarnings.dismissHighTemperatureWarning();
            } else if (!((Boolean) ((Optional) PowerUI.this.mCentralSurfacesOptionalLazy.get()).map(new PowerUI$SkinThermalEventListener$$ExternalSyntheticLambda0()).orElse(Boolean.FALSE)).booleanValue()) {
                PowerUI.this.mWarnings.showHighTemperatureWarning();
                Slog.d("PowerUI", "SkinThermalEventListener: notifyThrottling was called , current skin status = " + status + ", temperature = " + temperature.getValue());
            }
        }
    }

    @VisibleForTesting
    public final class UsbThermalEventListener extends IThermalEventListener.Stub {
        public UsbThermalEventListener() {
        }

        public void notifyThrottling(Temperature temperature) {
            int status = temperature.getStatus();
            if (status >= 5) {
                PowerUI.this.mWarnings.showUsbHighTemperatureAlarm();
                Slog.d("PowerUI", "UsbThermalEventListener: notifyThrottling was called , current usb port status = " + status + ", temperature = " + temperature.getValue());
            }
        }
    }
}
