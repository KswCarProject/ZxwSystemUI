package com.android.systemui.statusbar.policy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.icu.text.DateTimePatternGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.os.SystemClock;
import android.os.UserHandle;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.widget.TextView;
import com.android.settingslib.Utils;
import com.android.systemui.Dependency;
import com.android.systemui.FontSizeUtils;
import com.android.systemui.R$attr;
import com.android.systemui.R$dimen;
import com.android.systemui.R$style;
import com.android.systemui.R$styleable;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.demomode.DemoModeCommandReceiver;
import com.android.systemui.plugins.DarkIconDispatcher;
import com.android.systemui.settings.CurrentUserTracker;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.tuner.TunerService;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class Clock extends TextView implements DemoModeCommandReceiver, TunerService.Tunable, CommandQueue.Callbacks, DarkIconDispatcher.DarkReceiver, ConfigurationController.ConfigurationListener {
    public final int mAmPmStyle;
    public boolean mAttached;
    public final BroadcastDispatcher mBroadcastDispatcher;
    public int mCachedWidth;
    public Calendar mCalendar;
    public int mCharsAtCurrentWidth;
    public SimpleDateFormat mClockFormat;
    public boolean mClockVisibleByPolicy;
    public boolean mClockVisibleByUser;
    public final CommandQueue mCommandQueue;
    public SimpleDateFormat mContentDescriptionFormat;
    public String mContentDescriptionFormatString;
    public int mCurrentUserId;
    public final CurrentUserTracker mCurrentUserTracker;
    public DateTimePatternGenerator mDateTimePatternGenerator;
    public boolean mDemoMode;
    public final BroadcastReceiver mIntentReceiver;
    public Locale mLocale;
    public int mNonAdaptedColor;
    public final BroadcastReceiver mScreenReceiver;
    public boolean mScreenReceiverRegistered;
    public final Runnable mSecondTick;
    public Handler mSecondsHandler;
    public boolean mShowSeconds;

    public Clock(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    /* JADX INFO: finally extract failed */
    public Clock(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mClockVisibleByPolicy = true;
        this.mClockVisibleByUser = true;
        this.mCharsAtCurrentWidth = -1;
        this.mCachedWidth = -1;
        this.mIntentReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                Handler handler = Clock.this.getHandler();
                if (handler != null) {
                    String action = intent.getAction();
                    if (action.equals("android.intent.action.TIMEZONE_CHANGED")) {
                        handler.post(new Clock$2$$ExternalSyntheticLambda0(this, intent.getStringExtra("time-zone")));
                    } else if (action.equals("android.intent.action.CONFIGURATION_CHANGED")) {
                        handler.post(new Clock$2$$ExternalSyntheticLambda1(this, Clock.this.getResources().getConfiguration().locale));
                    }
                    handler.post(new Clock$2$$ExternalSyntheticLambda2(this));
                }
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$onReceive$0(String str) {
                Clock.this.mCalendar = Calendar.getInstance(TimeZone.getTimeZone(str));
                if (Clock.this.mClockFormat != null) {
                    Clock.this.mClockFormat.setTimeZone(Clock.this.mCalendar.getTimeZone());
                }
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$onReceive$1(Locale locale) {
                if (!locale.equals(Clock.this.mLocale)) {
                    Clock.this.mLocale = locale;
                    Clock.this.mContentDescriptionFormatString = "";
                    Clock.this.mDateTimePatternGenerator = null;
                }
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$onReceive$2() {
                Clock.this.updateClock();
            }
        };
        this.mScreenReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if ("android.intent.action.SCREEN_OFF".equals(action)) {
                    if (Clock.this.mSecondsHandler != null) {
                        Clock.this.mSecondsHandler.removeCallbacks(Clock.this.mSecondTick);
                    }
                } else if ("android.intent.action.SCREEN_ON".equals(action) && Clock.this.mSecondsHandler != null) {
                    Clock.this.mSecondsHandler.postAtTime(Clock.this.mSecondTick, ((SystemClock.uptimeMillis() / 1000) * 1000) + 1000);
                }
            }
        };
        this.mSecondTick = new Runnable() {
            public void run() {
                if (Clock.this.mCalendar != null) {
                    Clock.this.updateClock();
                }
                Clock.this.mSecondsHandler.postAtTime(this, ((SystemClock.uptimeMillis() / 1000) * 1000) + 1000);
            }
        };
        this.mCommandQueue = (CommandQueue) Dependency.get(CommandQueue.class);
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, R$styleable.Clock, 0, 0);
        try {
            this.mAmPmStyle = obtainStyledAttributes.getInt(R$styleable.Clock_amPmStyle, 2);
            this.mNonAdaptedColor = getCurrentTextColor();
            obtainStyledAttributes.recycle();
            BroadcastDispatcher broadcastDispatcher = (BroadcastDispatcher) Dependency.get(BroadcastDispatcher.class);
            this.mBroadcastDispatcher = broadcastDispatcher;
            this.mCurrentUserTracker = new CurrentUserTracker(broadcastDispatcher) {
                public void onUserSwitched(int i) {
                    Clock.this.mCurrentUserId = i;
                }
            };
        } catch (Throwable th) {
            obtainStyledAttributes.recycle();
            throw th;
        }
    }

    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("clock_super_parcelable", super.onSaveInstanceState());
        bundle.putInt("current_user_id", this.mCurrentUserId);
        bundle.putBoolean("visible_by_policy", this.mClockVisibleByPolicy);
        bundle.putBoolean("visible_by_user", this.mClockVisibleByUser);
        bundle.putBoolean("show_seconds", this.mShowSeconds);
        bundle.putInt("visibility", getVisibility());
        return bundle;
    }

    public void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable == null || !(parcelable instanceof Bundle)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        Bundle bundle = (Bundle) parcelable;
        super.onRestoreInstanceState(bundle.getParcelable("clock_super_parcelable"));
        if (bundle.containsKey("current_user_id")) {
            this.mCurrentUserId = bundle.getInt("current_user_id");
        }
        this.mClockVisibleByPolicy = bundle.getBoolean("visible_by_policy", true);
        this.mClockVisibleByUser = bundle.getBoolean("visible_by_user", true);
        this.mShowSeconds = bundle.getBoolean("show_seconds", false);
        if (bundle.containsKey("visibility")) {
            super.setVisibility(bundle.getInt("visibility"));
        }
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!this.mAttached) {
            this.mAttached = true;
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.TIME_TICK");
            intentFilter.addAction("android.intent.action.TIME_SET");
            intentFilter.addAction("android.intent.action.TIMEZONE_CHANGED");
            intentFilter.addAction("android.intent.action.CONFIGURATION_CHANGED");
            intentFilter.addAction("android.intent.action.USER_SWITCHED");
            this.mBroadcastDispatcher.registerReceiverWithHandler(this.mIntentReceiver, intentFilter, (Handler) Dependency.get(Dependency.TIME_TICK_HANDLER), UserHandle.ALL);
            ((TunerService) Dependency.get(TunerService.class)).addTunable(this, "clock_seconds", "icon_blacklist");
            this.mCommandQueue.addCallback((CommandQueue.Callbacks) this);
            this.mCurrentUserTracker.startTracking();
            this.mCurrentUserId = this.mCurrentUserTracker.getCurrentUserId();
        }
        this.mCalendar = Calendar.getInstance(TimeZone.getDefault());
        this.mContentDescriptionFormatString = "";
        this.mDateTimePatternGenerator = null;
        updateClock();
        updateClockVisibility();
        updateShowSeconds();
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mScreenReceiverRegistered) {
            this.mScreenReceiverRegistered = false;
            this.mBroadcastDispatcher.unregisterReceiver(this.mScreenReceiver);
            Handler handler = this.mSecondsHandler;
            if (handler != null) {
                handler.removeCallbacks(this.mSecondTick);
                this.mSecondsHandler = null;
            }
        }
        if (this.mAttached) {
            this.mBroadcastDispatcher.unregisterReceiver(this.mIntentReceiver);
            this.mAttached = false;
            ((TunerService) Dependency.get(TunerService.class)).removeTunable(this);
            this.mCommandQueue.removeCallback((CommandQueue.Callbacks) this);
            this.mCurrentUserTracker.stopTracking();
        }
    }

    public void setVisibility(int i) {
        if (i != 0 || shouldBeVisible()) {
            super.setVisibility(i);
        }
    }

    public void setClockVisibleByUser(boolean z) {
        this.mClockVisibleByUser = z;
        updateClockVisibility();
    }

    public void setClockVisibilityByPolicy(boolean z) {
        this.mClockVisibleByPolicy = z;
        updateClockVisibility();
    }

    public final boolean shouldBeVisible() {
        return this.mClockVisibleByPolicy && this.mClockVisibleByUser;
    }

    public final void updateClockVisibility() {
        super.setVisibility(shouldBeVisible() ? 0 : 8);
    }

    public final void updateClock() {
        if (!this.mDemoMode) {
            this.mCalendar.setTimeInMillis(System.currentTimeMillis());
            CharSequence smallTime = getSmallTime();
            if (!TextUtils.equals(smallTime, getText())) {
                setText(smallTime);
            }
            setContentDescription(this.mContentDescriptionFormat.format(this.mCalendar.getTime()));
        }
    }

    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        int length = getText().length();
        if (length != this.mCharsAtCurrentWidth) {
            this.mCharsAtCurrentWidth = length;
            this.mCachedWidth = getMeasuredWidth();
            return;
        }
        int measuredWidth = getMeasuredWidth();
        int i3 = this.mCachedWidth;
        if (i3 > measuredWidth) {
            setMeasuredDimension(i3, getMeasuredHeight());
        } else {
            this.mCachedWidth = measuredWidth;
        }
    }

    public void onTuningChanged(String str, String str2) {
        if ("clock_seconds".equals(str)) {
            this.mShowSeconds = TunerService.parseIntegerSwitch(str2, false);
            updateShowSeconds();
        } else if ("icon_blacklist".equals(str)) {
            setClockVisibleByUser(!StatusBarIconController.getIconHideList(getContext(), str2).contains("clock"));
            updateClockVisibility();
        }
    }

    public void disable(int i, int i2, int i3, boolean z) {
        if (i == getDisplay().getDisplayId()) {
            boolean z2 = (8388608 & i2) == 0;
            if (z2 != this.mClockVisibleByPolicy) {
                setClockVisibilityByPolicy(z2);
            }
        }
    }

    public void onDarkChanged(ArrayList<Rect> arrayList, float f, int i) {
        int tint = DarkIconDispatcher.getTint(arrayList, this, i);
        this.mNonAdaptedColor = tint;
        setTextColor(tint);
    }

    public void onColorsChanged(boolean z) {
        setTextColor(Utils.getColorAttrDefaultColor(new ContextThemeWrapper(this.mContext, z ? R$style.Theme_SystemUI_LightWallpaper : R$style.Theme_SystemUI), R$attr.wallpaperTextColor));
    }

    public void onDensityOrFontScaleChanged() {
        FontSizeUtils.updateFontSize(this, R$dimen.status_bar_clock_size);
        setPaddingRelative(this.mContext.getResources().getDimensionPixelSize(R$dimen.status_bar_clock_starting_padding), 0, this.mContext.getResources().getDimensionPixelSize(R$dimen.status_bar_clock_end_padding), 0);
    }

    public final void updateShowSeconds() {
        if (this.mShowSeconds) {
            if (this.mSecondsHandler == null && getDisplay() != null) {
                this.mSecondsHandler = new Handler();
                if (getDisplay().getState() == 2) {
                    this.mSecondsHandler.postAtTime(this.mSecondTick, ((SystemClock.uptimeMillis() / 1000) * 1000) + 1000);
                }
                this.mScreenReceiverRegistered = true;
                IntentFilter intentFilter = new IntentFilter("android.intent.action.SCREEN_OFF");
                intentFilter.addAction("android.intent.action.SCREEN_ON");
                this.mBroadcastDispatcher.registerReceiver(this.mScreenReceiver, intentFilter);
            }
        } else if (this.mSecondsHandler != null) {
            this.mScreenReceiverRegistered = false;
            this.mBroadcastDispatcher.unregisterReceiver(this.mScreenReceiver);
            this.mSecondsHandler.removeCallbacks(this.mSecondTick);
            this.mSecondsHandler = null;
            updateClock();
        }
    }

    public final CharSequence getSmallTime() {
        Context context = getContext();
        boolean is24HourFormat = DateFormat.is24HourFormat(context, this.mCurrentUserId);
        if (this.mDateTimePatternGenerator == null) {
            this.mDateTimePatternGenerator = DateTimePatternGenerator.getInstance(context.getResources().getConfiguration().locale);
        }
        String bestPattern = this.mDateTimePatternGenerator.getBestPattern(this.mShowSeconds ? is24HourFormat ? "Hms" : "hms" : is24HourFormat ? "Hm" : "hm");
        if (!bestPattern.equals(this.mContentDescriptionFormatString)) {
            this.mContentDescriptionFormatString = bestPattern;
            this.mContentDescriptionFormat = new SimpleDateFormat(bestPattern);
            if (this.mAmPmStyle != 0) {
                int i = 0;
                boolean z = false;
                while (true) {
                    if (i >= bestPattern.length()) {
                        i = -1;
                        break;
                    }
                    char charAt = bestPattern.charAt(i);
                    if (charAt == '\'') {
                        z = !z;
                    }
                    if (!z && charAt == 'a') {
                        break;
                    }
                    i++;
                }
                if (i >= 0) {
                    int i2 = i;
                    while (i2 > 0 && Character.isWhitespace(bestPattern.charAt(i2 - 1))) {
                        i2--;
                    }
                    bestPattern = bestPattern.substring(0, i2) + 61184 + bestPattern.substring(i2, i) + "a" + 61185 + bestPattern.substring(i + 1);
                }
            }
            this.mClockFormat = new SimpleDateFormat(bestPattern);
        }
        String format = this.mClockFormat.format(this.mCalendar.getTime());
        if (this.mAmPmStyle != 0) {
            int indexOf = format.indexOf(61184);
            int indexOf2 = format.indexOf(61185);
            if (indexOf >= 0 && indexOf2 > indexOf) {
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(format);
                int i3 = this.mAmPmStyle;
                if (i3 == 2) {
                    spannableStringBuilder.delete(indexOf, indexOf2 + 1);
                } else {
                    if (i3 == 1) {
                        spannableStringBuilder.setSpan(new RelativeSizeSpan(0.7f), indexOf, indexOf2, 34);
                    }
                    spannableStringBuilder.delete(indexOf2, indexOf2 + 1);
                    spannableStringBuilder.delete(indexOf, indexOf + 1);
                }
                return spannableStringBuilder;
            }
        }
        return format;
    }

    public void dispatchDemoCommand(String str, Bundle bundle) {
        String string = bundle.getString("millis");
        String string2 = bundle.getString("hhmm");
        if (string != null) {
            this.mCalendar.setTimeInMillis(Long.parseLong(string));
        } else if (string2 != null && string2.length() == 4) {
            int parseInt = Integer.parseInt(string2.substring(0, 2));
            int parseInt2 = Integer.parseInt(string2.substring(2));
            if (DateFormat.is24HourFormat(getContext(), this.mCurrentUserId)) {
                this.mCalendar.set(11, parseInt);
            } else {
                this.mCalendar.set(10, parseInt);
            }
            this.mCalendar.set(12, parseInt2);
        }
        setText(getSmallTime());
        setContentDescription(this.mContentDescriptionFormat.format(this.mCalendar.getTime()));
    }

    public void onDemoModeStarted() {
        this.mDemoMode = true;
    }

    public void onDemoModeFinished() {
        this.mDemoMode = false;
        updateClock();
    }
}
