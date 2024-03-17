package com.android.systemui.statusbar.phone;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.hardware.display.AmbientDisplayConfiguration;
import android.net.Uri;
import android.os.Handler;
import android.os.PowerManager;
import android.os.SystemProperties;
import android.provider.Settings;
import android.util.Log;
import android.util.MathUtils;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.systemui.Dumpable;
import com.android.systemui.R$array;
import com.android.systemui.R$bool;
import com.android.systemui.R$integer;
import com.android.systemui.doze.AlwaysOnDisplayPolicy;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.unfold.FoldAodAnimationController;
import com.android.systemui.unfold.SysUIUnfoldComponent;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class DozeParameters implements TunerService.Tunable, com.android.systemui.plugins.statusbar.DozeParameters, Dumpable, ConfigurationController.ConfigurationListener, StatusBarStateController.StateListener, FoldAodAnimationController.FoldAodAnimationStatus {
    public static final boolean FORCE_BLANKING = SystemProperties.getBoolean("debug.force_blanking", false);
    public static final boolean FORCE_NO_BLANKING = SystemProperties.getBoolean("debug.force_no_blanking", false);
    public final AlwaysOnDisplayPolicy mAlwaysOnPolicy;
    public final AmbientDisplayConfiguration mAmbientDisplayConfiguration;
    public final BatteryController mBatteryController;
    public final Set<Callback> mCallbacks = new HashSet();
    public boolean mControlScreenOffAnimation;
    public boolean mDozeAlwaysOn;
    public final FeatureFlags mFeatureFlags;
    public final FoldAodAnimationController mFoldAodAnimationController;
    public boolean mIsQuickPickupEnabled;
    public boolean mKeyguardShowing;
    public final KeyguardUpdateMonitorCallback mKeyguardVisibilityCallback;
    public final PowerManager mPowerManager;
    public final Resources mResources;
    public final ScreenOffAnimationController mScreenOffAnimationController;
    public final UnlockedScreenOffAnimationController mUnlockedScreenOffAnimationController;

    public interface Callback {
        void onAlwaysOnChange();
    }

    public DozeParameters(Context context, Handler handler, Resources resources, AmbientDisplayConfiguration ambientDisplayConfiguration, AlwaysOnDisplayPolicy alwaysOnDisplayPolicy, PowerManager powerManager, BatteryController batteryController, TunerService tunerService, DumpManager dumpManager, FeatureFlags featureFlags, ScreenOffAnimationController screenOffAnimationController, Optional<SysUIUnfoldComponent> optional, UnlockedScreenOffAnimationController unlockedScreenOffAnimationController, KeyguardUpdateMonitor keyguardUpdateMonitor, ConfigurationController configurationController, StatusBarStateController statusBarStateController) {
        AnonymousClass1 r2 = new KeyguardUpdateMonitorCallback() {
            public void onKeyguardVisibilityChanged(boolean z) {
                DozeParameters.this.mKeyguardShowing = z;
                DozeParameters.this.updateControlScreenOff();
            }

            public void onShadeExpandedChanged(boolean z) {
                DozeParameters.this.updateControlScreenOff();
            }

            public void onUserSwitchComplete(int i) {
                DozeParameters.this.updateQuickPickupEnabled();
            }
        };
        this.mKeyguardVisibilityCallback = r2;
        this.mResources = resources;
        this.mAmbientDisplayConfiguration = ambientDisplayConfiguration;
        this.mAlwaysOnPolicy = alwaysOnDisplayPolicy;
        this.mBatteryController = batteryController;
        DumpManager dumpManager2 = dumpManager;
        dumpManager.registerDumpable("DozeParameters", this);
        boolean z = !getDisplayNeedsBlanking();
        this.mControlScreenOffAnimation = z;
        this.mPowerManager = powerManager;
        powerManager.setDozeAfterScreenOff(!z);
        this.mFeatureFlags = featureFlags;
        this.mScreenOffAnimationController = screenOffAnimationController;
        this.mUnlockedScreenOffAnimationController = unlockedScreenOffAnimationController;
        keyguardUpdateMonitor.registerCallback(r2);
        TunerService tunerService2 = tunerService;
        tunerService.addTunable(this, "doze_always_on", "accessibility_display_inversion_enabled");
        configurationController.addCallback(this);
        statusBarStateController.addCallback(this);
        FoldAodAnimationController foldAodAnimationController = (FoldAodAnimationController) optional.map(new DozeParameters$$ExternalSyntheticLambda0()).orElse((Object) null);
        this.mFoldAodAnimationController = foldAodAnimationController;
        if (foldAodAnimationController != null) {
            foldAodAnimationController.addCallback((FoldAodAnimationController.FoldAodAnimationStatus) this);
        }
        Context context2 = context;
        Handler handler2 = handler;
        new SettingsObserver(context, handler).observe();
    }

    public final void updateQuickPickupEnabled() {
        this.mIsQuickPickupEnabled = this.mAmbientDisplayConfiguration.quickPickupSensorEnabled(-2);
    }

    public boolean getDisplayStateSupported() {
        return getBoolean("doze.display.supported", R$bool.doze_display_state_supported);
    }

    public boolean getDozeSuspendDisplayStateSupported() {
        return this.mResources.getBoolean(R$bool.doze_suspend_display_state_supported);
    }

    public int getPulseDuration() {
        return getPulseInDuration() + getPulseVisibleDuration() + getPulseOutDuration();
    }

    public float getScreenBrightnessDoze() {
        return ((float) this.mResources.getInteger(17694923)) / 255.0f;
    }

    public int getPulseInDuration() {
        return getInt("doze.pulse.duration.in", R$integer.doze_pulse_duration_in);
    }

    public int getPulseVisibleDuration() {
        return getInt("doze.pulse.duration.visible", R$integer.doze_pulse_duration_visible);
    }

    public int getPulseOutDuration() {
        return getInt("doze.pulse.duration.out", R$integer.doze_pulse_duration_out);
    }

    public boolean getPulseOnSigMotion() {
        return getBoolean("doze.pulse.sigmotion", R$bool.doze_pulse_on_significant_motion);
    }

    public boolean getVibrateOnSigMotion() {
        return SystemProperties.getBoolean("doze.vibrate.sigmotion", false);
    }

    public boolean getVibrateOnPickup() {
        return SystemProperties.getBoolean("doze.vibrate.pickup", false);
    }

    public boolean getProxCheckBeforePulse() {
        return getBoolean("doze.pulse.proxcheck", R$bool.doze_proximity_check_before_pulse);
    }

    public boolean getSelectivelyRegisterSensorsUsingProx() {
        return getBoolean("doze.prox.selectively_register", R$bool.doze_selectively_register_prox);
    }

    public int getPickupVibrationThreshold() {
        return getInt("doze.pickup.vibration.threshold", R$integer.doze_pickup_vibration_threshold);
    }

    public long getWallpaperAodDuration() {
        if (shouldControlScreenOff()) {
            return 2500;
        }
        return this.mAlwaysOnPolicy.wallpaperVisibilityDuration;
    }

    public long getWallpaperFadeOutDuration() {
        return this.mAlwaysOnPolicy.wallpaperFadeOutDuration;
    }

    public boolean getAlwaysOn() {
        return this.mDozeAlwaysOn && !this.mBatteryController.isAodPowerSave();
    }

    public boolean isQuickPickupEnabled() {
        return this.mIsQuickPickupEnabled;
    }

    public boolean getDisplayNeedsBlanking() {
        return FORCE_BLANKING || (!FORCE_NO_BLANKING && this.mResources.getBoolean(17891602));
    }

    public boolean shouldControlScreenOff() {
        return this.mControlScreenOffAnimation;
    }

    public void setControlScreenOffAnimation(boolean z) {
        if (this.mControlScreenOffAnimation != z) {
            this.mControlScreenOffAnimation = z;
            this.mPowerManager.setDozeAfterScreenOff(!z);
        }
    }

    public void updateControlScreenOff() {
        if (!getDisplayNeedsBlanking()) {
            setControlScreenOffAnimation(getAlwaysOn() && (this.mKeyguardShowing || shouldControlUnlockedScreenOff()));
        }
    }

    public boolean canControlUnlockedScreenOff() {
        return getAlwaysOn() && this.mFeatureFlags.isEnabled(Flags.LOCKSCREEN_ANIMATIONS) && !getDisplayNeedsBlanking();
    }

    public boolean shouldControlUnlockedScreenOff() {
        return this.mUnlockedScreenOffAnimationController.shouldPlayUnlockedScreenOffAnimation();
    }

    public boolean shouldDelayKeyguardShow() {
        return this.mScreenOffAnimationController.shouldDelayKeyguardShow();
    }

    public boolean shouldClampToDimBrightness() {
        return this.mScreenOffAnimationController.shouldClampDozeScreenBrightness();
    }

    public boolean shouldShowLightRevealScrim() {
        return this.mScreenOffAnimationController.shouldShowLightRevealScrim();
    }

    public boolean shouldAnimateDozingChange() {
        return this.mScreenOffAnimationController.shouldAnimateDozingChange();
    }

    public boolean shouldDelayDisplayDozeTransition() {
        return willAnimateFromLockScreenToAod() || this.mScreenOffAnimationController.shouldDelayDisplayDozeTransition();
    }

    public final boolean willAnimateFromLockScreenToAod() {
        return getAlwaysOn() && this.mKeyguardShowing;
    }

    public final boolean getBoolean(String str, int i) {
        return SystemProperties.getBoolean(str, this.mResources.getBoolean(i));
    }

    public final int getInt(String str, int i) {
        return MathUtils.constrain(SystemProperties.getInt(str, this.mResources.getInteger(i)), 0, 60000);
    }

    public int getPulseVisibleDurationExtended() {
        return getPulseVisibleDuration() * 2;
    }

    public boolean doubleTapReportsTouchCoordinates() {
        return this.mResources.getBoolean(R$bool.doze_double_tap_reports_touch_coordinates);
    }

    public boolean singleTapUsesProx(int i) {
        return getPostureSpecificBool(this.mResources.getIntArray(R$array.doze_single_tap_uses_prox_posture_mapping), singleTapUsesProx(), i);
    }

    public final boolean singleTapUsesProx() {
        return this.mResources.getBoolean(R$bool.doze_single_tap_uses_prox);
    }

    public boolean longPressUsesProx() {
        return this.mResources.getBoolean(R$bool.doze_long_press_uses_prox);
    }

    public String[] brightnessNames() {
        return this.mResources.getStringArray(R$array.doze_brightness_sensor_name_posture_mapping);
    }

    public void onTuningChanged(String str, String str2) {
        this.mDozeAlwaysOn = this.mAmbientDisplayConfiguration.alwaysOnEnabled(-2);
        if (str.equals("doze_always_on")) {
            updateControlScreenOff();
        }
        for (Callback onAlwaysOnChange : this.mCallbacks) {
            onAlwaysOnChange.onAlwaysOnChange();
        }
        this.mScreenOffAnimationController.onAlwaysOnChanged(getAlwaysOn());
    }

    public void onConfigChanged(Configuration configuration) {
        updateControlScreenOff();
    }

    public void onStatePostChange() {
        updateControlScreenOff();
    }

    public void onFoldToAodAnimationChanged() {
        updateControlScreenOff();
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.print("getAlwaysOn(): ");
        printWriter.println(getAlwaysOn());
        printWriter.print("getDisplayStateSupported(): ");
        printWriter.println(getDisplayStateSupported());
        printWriter.print("getPulseDuration(): ");
        printWriter.println(getPulseDuration());
        printWriter.print("getPulseInDuration(): ");
        printWriter.println(getPulseInDuration());
        printWriter.print("getPulseInVisibleDuration(): ");
        printWriter.println(getPulseVisibleDuration());
        printWriter.print("getPulseOutDuration(): ");
        printWriter.println(getPulseOutDuration());
        printWriter.print("getPulseOnSigMotion(): ");
        printWriter.println(getPulseOnSigMotion());
        printWriter.print("getVibrateOnSigMotion(): ");
        printWriter.println(getVibrateOnSigMotion());
        printWriter.print("getVibrateOnPickup(): ");
        printWriter.println(getVibrateOnPickup());
        printWriter.print("getProxCheckBeforePulse(): ");
        printWriter.println(getProxCheckBeforePulse());
        printWriter.print("getPickupVibrationThreshold(): ");
        printWriter.println(getPickupVibrationThreshold());
        printWriter.print("getSelectivelyRegisterSensorsUsingProx(): ");
        printWriter.println(getSelectivelyRegisterSensorsUsingProx());
        printWriter.print("isQuickPickupEnabled(): ");
        printWriter.println(isQuickPickupEnabled());
    }

    public final boolean getPostureSpecificBool(int[] iArr, boolean z, int i) {
        if (i < iArr.length) {
            return iArr[i] != 0;
        }
        Log.e("DozeParameters", "Unsupported doze posture " + i);
        return z;
    }

    public final class SettingsObserver extends ContentObserver {
        public final Uri mAlwaysOnEnabled = Settings.Secure.getUriFor("doze_always_on");
        public final Context mContext;
        public final Uri mPickupGesture = Settings.Secure.getUriFor("doze_pulse_on_pick_up");
        public final Uri mQuickPickupGesture = Settings.Secure.getUriFor("doze_quick_pickup_gesture");

        public SettingsObserver(Context context, Handler handler) {
            super(handler);
            this.mContext = context;
        }

        public void observe() {
            ContentResolver contentResolver = this.mContext.getContentResolver();
            contentResolver.registerContentObserver(this.mQuickPickupGesture, false, this, -1);
            contentResolver.registerContentObserver(this.mPickupGesture, false, this, -1);
            contentResolver.registerContentObserver(this.mAlwaysOnEnabled, false, this, -1);
            update((Uri) null);
        }

        public void onChange(boolean z, Uri uri) {
            update(uri);
        }

        public void update(Uri uri) {
            if (uri == null || this.mQuickPickupGesture.equals(uri) || this.mPickupGesture.equals(uri) || this.mAlwaysOnEnabled.equals(uri)) {
                DozeParameters.this.updateQuickPickupEnabled();
            }
        }
    }
}
