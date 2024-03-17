package com.android.keyguard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.icu.text.NumberFormat;
import com.android.settingslib.Utils;
import com.android.systemui.R$attr;
import com.android.systemui.R$dimen;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.util.ViewController;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class AnimatableClockController extends ViewController<AnimatableClockView> {
    public final BatteryController.BatteryStateChangeCallback mBatteryCallback;
    public final BatteryController mBatteryController;
    public final BroadcastDispatcher mBroadcastDispatcher;
    public final float mBurmeseLineSpacing;
    public final NumberFormat mBurmeseNf;
    public final String mBurmeseNumerals;
    public final float mDefaultLineSpacing;
    public float mDozeAmount;
    public final int mDozingColor = -1;
    public boolean mIsCharging;
    public boolean mIsDozing;
    public boolean mKeyguardShowing;
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    public final KeyguardUpdateMonitorCallback mKeyguardUpdateMonitorCallback;
    public Locale mLocale;
    public final BroadcastReceiver mLocaleBroadcastReceiver;
    public int mLockScreenColor;
    public final StatusBarStateController mStatusBarStateController;
    public final StatusBarStateController.StateListener mStatusBarStateListener;

    public AnimatableClockController(AnimatableClockView animatableClockView, StatusBarStateController statusBarStateController, BroadcastDispatcher broadcastDispatcher, BatteryController batteryController, KeyguardUpdateMonitor keyguardUpdateMonitor, Resources resources) {
        super(animatableClockView);
        NumberFormat instance = NumberFormat.getInstance(Locale.forLanguageTag("my"));
        this.mBurmeseNf = instance;
        this.mBatteryCallback = new BatteryController.BatteryStateChangeCallback() {
            public void onBatteryLevelChanged(int i, boolean z, boolean z2) {
                AnimatableClockController animatableClockController = AnimatableClockController.this;
                if (animatableClockController.mKeyguardShowing && !animatableClockController.mIsCharging && z2) {
                    StatusBarStateController r3 = AnimatableClockController.this.mStatusBarStateController;
                    Objects.requireNonNull(r3);
                    ((AnimatableClockView) AnimatableClockController.this.mView).animateCharge(new AnimatableClockController$1$$ExternalSyntheticLambda0(r3));
                }
                AnimatableClockController.this.mIsCharging = z2;
            }
        };
        this.mLocaleBroadcastReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                AnimatableClockController.this.updateLocale();
            }
        };
        this.mStatusBarStateListener = new StatusBarStateController.StateListener() {
            public void onDozeAmountChanged(float f, float f2) {
                boolean z = false;
                boolean z2 = (AnimatableClockController.this.mDozeAmount == 0.0f && f == 1.0f) || (AnimatableClockController.this.mDozeAmount == 1.0f && f == 0.0f);
                if (f > AnimatableClockController.this.mDozeAmount) {
                    z = true;
                }
                AnimatableClockController.this.mDozeAmount = f;
                if (AnimatableClockController.this.mIsDozing != z) {
                    AnimatableClockController.this.mIsDozing = z;
                    ((AnimatableClockView) AnimatableClockController.this.mView).animateDoze(AnimatableClockController.this.mIsDozing, !z2);
                }
            }
        };
        this.mKeyguardUpdateMonitorCallback = new KeyguardUpdateMonitorCallback() {
            public void onKeyguardVisibilityChanged(boolean z) {
                AnimatableClockController animatableClockController = AnimatableClockController.this;
                animatableClockController.mKeyguardShowing = z;
                if (!z) {
                    animatableClockController.reset();
                }
            }
        };
        this.mStatusBarStateController = statusBarStateController;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mBatteryController = batteryController;
        this.mBurmeseNumerals = instance.format(1234567890);
        this.mBurmeseLineSpacing = resources.getFloat(R$dimen.keyguard_clock_line_spacing_scale_burmese);
        this.mDefaultLineSpacing = resources.getFloat(R$dimen.keyguard_clock_line_spacing_scale);
    }

    public final void reset() {
        ((AnimatableClockView) this.mView).animateDoze(this.mIsDozing, false);
    }

    public void onInit() {
        this.mIsDozing = this.mStatusBarStateController.isDozing();
    }

    public void onViewAttached() {
        updateLocale();
        this.mBroadcastDispatcher.registerReceiver(this.mLocaleBroadcastReceiver, new IntentFilter("android.intent.action.LOCALE_CHANGED"));
        this.mDozeAmount = this.mStatusBarStateController.getDozeAmount();
        this.mIsDozing = this.mStatusBarStateController.isDozing() || this.mDozeAmount != 0.0f;
        this.mBatteryController.addCallback(this.mBatteryCallback);
        this.mKeyguardUpdateMonitor.registerCallback(this.mKeyguardUpdateMonitorCallback);
        this.mStatusBarStateController.addCallback(this.mStatusBarStateListener);
        refreshTime();
        initColors();
        ((AnimatableClockView) this.mView).animateDoze(this.mIsDozing, false);
    }

    public void onViewDetached() {
        this.mBroadcastDispatcher.unregisterReceiver(this.mLocaleBroadcastReceiver);
        this.mKeyguardUpdateMonitor.removeCallback(this.mKeyguardUpdateMonitorCallback);
        this.mBatteryController.removeCallback(this.mBatteryCallback);
        this.mStatusBarStateController.removeCallback(this.mStatusBarStateListener);
    }

    public float getBottom() {
        if (((AnimatableClockView) this.mView).getPaint() == null || ((AnimatableClockView) this.mView).getPaint().getFontMetrics() == null) {
            return 0.0f;
        }
        return ((AnimatableClockView) this.mView).getPaint().getFontMetrics().bottom;
    }

    public void animateAppear() {
        if (!this.mIsDozing) {
            ((AnimatableClockView) this.mView).animateAppearOnLockscreen();
        }
    }

    public void animateFoldAppear() {
        ((AnimatableClockView) this.mView).animateFoldAppear();
    }

    public void refreshTime() {
        ((AnimatableClockView) this.mView).refreshTime();
    }

    public void onTimeZoneChanged(TimeZone timeZone) {
        ((AnimatableClockView) this.mView).onTimeZoneChanged(timeZone);
    }

    public void refreshFormat() {
        ((AnimatableClockView) this.mView).refreshFormat();
    }

    public boolean isDozing() {
        return this.mIsDozing;
    }

    public final void updateLocale() {
        Locale locale = Locale.getDefault();
        if (!Objects.equals(locale, this.mLocale)) {
            this.mLocale = locale;
            if (NumberFormat.getInstance(locale).format(1234567890).equals(this.mBurmeseNumerals)) {
                ((AnimatableClockView) this.mView).setLineSpacingScale(this.mBurmeseLineSpacing);
            } else {
                ((AnimatableClockView) this.mView).setLineSpacingScale(this.mDefaultLineSpacing);
            }
            ((AnimatableClockView) this.mView).refreshFormat();
        }
    }

    public final void initColors() {
        int colorAttrDefaultColor = Utils.getColorAttrDefaultColor(getContext(), R$attr.wallpaperTextColorAccent);
        this.mLockScreenColor = colorAttrDefaultColor;
        ((AnimatableClockView) this.mView).setColors(-1, colorAttrDefaultColor);
        ((AnimatableClockView) this.mView).animateDoze(this.mIsDozing, false);
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println(this);
        ((AnimatableClockView) this.mView).dump(printWriter);
    }
}
