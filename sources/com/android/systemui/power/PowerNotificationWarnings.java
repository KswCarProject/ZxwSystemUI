package com.android.systemui.power;

import android.app.Dialog;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.os.UserHandle;
import android.provider.Settings;
import android.text.Annotation;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Log;
import android.util.Slog;
import android.view.View;
import com.android.internal.logging.UiEventLogger;
import com.android.settingslib.Utils;
import com.android.settingslib.fuelgauge.BatterySaverUtils;
import com.android.systemui.R$bool;
import com.android.systemui.R$drawable;
import com.android.systemui.R$string;
import com.android.systemui.R$style;
import com.android.systemui.SystemUIApplication;
import com.android.systemui.animation.DialogLaunchAnimator;
import com.android.systemui.broadcast.BroadcastSender;
import com.android.systemui.globalactions.GlobalActionsDialogLite;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.power.PowerUI;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.theme.ThemeOverlayApplier;
import com.android.systemui.util.NotificationChannels;
import com.android.systemui.volume.Events;
import dagger.Lazy;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;

public class PowerNotificationWarnings implements PowerUI.WarningsUI {
    public static final AudioAttributes AUDIO_ATTRIBUTES = new AudioAttributes.Builder().setContentType(4).setUsage(13).build();
    public static final boolean DEBUG = PowerUI.DEBUG;
    public static final String[] SHOWING_STRINGS = {"SHOWING_NOTHING", "SHOWING_WARNING", "SHOWING_SAVER", "SHOWING_INVALID_CHARGER", "SHOWING_AUTO_SAVER_SUGGESTION"};
    public ActivityStarter mActivityStarter;
    public final Lazy<BatteryController> mBatteryControllerLazy;
    public int mBatteryLevel;
    public final BroadcastSender mBroadcastSender;
    public int mBucket;
    public final Context mContext;
    public BatteryStateSnapshot mCurrentBatterySnapshot;
    public final DialogLaunchAnimator mDialogLaunchAnimator;
    public final Handler mHandler = new Handler(Looper.getMainLooper());
    public SystemUIDialog mHighTempDialog;
    public boolean mHighTempWarning;
    public boolean mInvalidCharger;
    public final KeyguardManager mKeyguard;
    public final NotificationManager mNoMan;
    public final Intent mOpenBatterySaverSettings;
    public final Intent mOpenBatterySettings;
    public boolean mPlaySound;
    public final PowerManager mPowerMan;
    public final Receiver mReceiver;
    public SystemUIDialog mSaverConfirmation;
    public long mScreenOffTime;
    public boolean mShowAutoSaverSuggestion;
    public int mShowing;
    public SystemUIDialog mThermalShutdownDialog;
    public final UiEventLogger mUiEventLogger;
    public SystemUIDialog mUsbHighTempDialog;
    public final boolean mUseSevereDialog;
    public boolean mWarning;
    public long mWarningTriggerTimeMs;

    public PowerNotificationWarnings(Context context, ActivityStarter activityStarter, BroadcastSender broadcastSender, Lazy<BatteryController> lazy, DialogLaunchAnimator dialogLaunchAnimator, UiEventLogger uiEventLogger) {
        Receiver receiver = new Receiver();
        this.mReceiver = receiver;
        this.mOpenBatterySettings = settings("android.intent.action.POWER_USAGE_SUMMARY");
        this.mOpenBatterySaverSettings = settings("android.settings.BATTERY_SAVER_SETTINGS");
        this.mContext = context;
        this.mNoMan = (NotificationManager) context.getSystemService(NotificationManager.class);
        this.mPowerMan = (PowerManager) context.getSystemService(GlobalActionsDialogLite.GLOBAL_ACTION_KEY_POWER);
        this.mKeyguard = (KeyguardManager) context.getSystemService(KeyguardManager.class);
        receiver.init();
        this.mActivityStarter = activityStarter;
        this.mBroadcastSender = broadcastSender;
        this.mBatteryControllerLazy = lazy;
        this.mDialogLaunchAnimator = dialogLaunchAnimator;
        this.mUseSevereDialog = context.getResources().getBoolean(R$bool.config_severe_battery_dialog);
        this.mUiEventLogger = uiEventLogger;
    }

    public void dump(PrintWriter printWriter) {
        printWriter.print("mWarning=");
        printWriter.println(this.mWarning);
        printWriter.print("mPlaySound=");
        printWriter.println(this.mPlaySound);
        printWriter.print("mInvalidCharger=");
        printWriter.println(this.mInvalidCharger);
        printWriter.print("mShowing=");
        printWriter.println(SHOWING_STRINGS[this.mShowing]);
        printWriter.print("mSaverConfirmation=");
        String str = "not null";
        printWriter.println(this.mSaverConfirmation != null ? str : null);
        printWriter.print("mSaverEnabledConfirmation=");
        printWriter.print("mHighTempWarning=");
        printWriter.println(this.mHighTempWarning);
        printWriter.print("mHighTempDialog=");
        printWriter.println(this.mHighTempDialog != null ? str : null);
        printWriter.print("mThermalShutdownDialog=");
        printWriter.println(this.mThermalShutdownDialog != null ? str : null);
        printWriter.print("mUsbHighTempDialog=");
        if (this.mUsbHighTempDialog == null) {
            str = null;
        }
        printWriter.println(str);
    }

    public void update(int i, int i2, long j) {
        this.mBatteryLevel = i;
        if (i2 >= 0) {
            this.mWarningTriggerTimeMs = 0;
        } else if (i2 < this.mBucket) {
            this.mWarningTriggerTimeMs = System.currentTimeMillis();
        }
        this.mBucket = i2;
        this.mScreenOffTime = j;
    }

    public void updateSnapshot(BatteryStateSnapshot batteryStateSnapshot) {
        this.mCurrentBatterySnapshot = batteryStateSnapshot;
    }

    public final void updateNotification() {
        if (DEBUG) {
            Slog.d("PowerUI.Notification", "updateNotification mWarning=" + this.mWarning + " mPlaySound=" + this.mPlaySound + " mInvalidCharger=" + this.mInvalidCharger);
        }
        if (this.mInvalidCharger) {
            showInvalidChargerNotification();
            this.mShowing = 3;
        } else if (this.mWarning) {
            showWarningNotification();
            this.mShowing = 1;
        } else if (this.mShowAutoSaverSuggestion) {
            if (this.mShowing != 4) {
                showAutoSaverSuggestionNotification();
            }
            this.mShowing = 4;
        } else {
            this.mNoMan.cancelAsUser("low_battery", 2, UserHandle.ALL);
            this.mNoMan.cancelAsUser("low_battery", 3, UserHandle.ALL);
            this.mNoMan.cancelAsUser("auto_saver", 49, UserHandle.ALL);
            this.mShowing = 0;
        }
    }

    public final void showInvalidChargerNotification() {
        Notification.Builder color = new Notification.Builder(this.mContext, NotificationChannels.ALERTS).setSmallIcon(R$drawable.ic_power_low).setWhen(0).setShowWhen(false).setOngoing(true).setContentTitle(this.mContext.getString(R$string.invalid_charger_title)).setContentText(this.mContext.getString(R$string.invalid_charger_text)).setColor(this.mContext.getColor(17170460));
        SystemUIApplication.overrideNotificationAppName(this.mContext, color, false);
        Notification build = color.build();
        this.mNoMan.cancelAsUser("low_battery", 3, UserHandle.ALL);
        this.mNoMan.notifyAsUser("low_battery", 2, build, UserHandle.ALL);
    }

    public void showWarningNotification() {
        if (showSevereLowBatteryDialog()) {
            this.mBroadcastSender.sendBroadcast(new Intent("PNW.enableSevereDialog").setPackage(this.mContext.getPackageName()).putExtra("extra_scheduled_by_percentage", isScheduledByPercentage()).addFlags(1342177280));
            dismissLowBatteryNotification();
            this.mPlaySound = false;
        } else if (!isScheduledByPercentage()) {
            String format = NumberFormat.getPercentInstance().format(((double) this.mCurrentBatterySnapshot.getBatteryLevel()) / 100.0d);
            String string = this.mContext.getString(R$string.battery_low_title);
            String string2 = this.mContext.getString(R$string.battery_low_description, new Object[]{format});
            Notification.Builder visibility = new Notification.Builder(this.mContext, NotificationChannels.BATTERY).setSmallIcon(R$drawable.ic_power_low).setWhen(this.mWarningTriggerTimeMs).setShowWhen(false).setContentText(string2).setContentTitle(string).setOnlyAlertOnce(true).setDeleteIntent(pendingBroadcast("PNW.dismissedWarning")).setStyle(new Notification.BigTextStyle().bigText(string2)).setVisibility(1);
            if (hasBatterySettings()) {
                visibility.setContentIntent(pendingBroadcast("PNW.batterySaverSettings"));
            }
            if (!this.mCurrentBatterySnapshot.isHybrid() || this.mBucket < -1 || this.mCurrentBatterySnapshot.getTimeRemainingMillis() < this.mCurrentBatterySnapshot.getSevereThresholdMillis()) {
                visibility.setColor(Utils.getColorAttrDefaultColor(this.mContext, 16844099));
            }
            if (!this.mPowerMan.isPowerSaveMode()) {
                visibility.addAction(0, this.mContext.getString(R$string.battery_saver_dismiss_action), pendingBroadcast("PNW.dismissedWarning"));
                visibility.addAction(0, this.mContext.getString(R$string.battery_saver_start_action), pendingBroadcast("PNW.startSaver"));
            }
            visibility.setOnlyAlertOnce(!this.mPlaySound);
            this.mPlaySound = false;
            SystemUIApplication.overrideNotificationAppName(this.mContext, visibility, false);
            Notification build = visibility.build();
            this.mNoMan.cancelAsUser("low_battery", 2, UserHandle.ALL);
            this.mNoMan.notifyAsUser("low_battery", 3, build, UserHandle.ALL);
        }
    }

    public final boolean showSevereLowBatteryDialog() {
        return this.mBucket < -1 && this.mUseSevereDialog;
    }

    public final boolean isScheduledByPercentage() {
        ContentResolver contentResolver = this.mContext.getContentResolver();
        if (Settings.Global.getInt(contentResolver, "automatic_power_save_mode", 0) != 0 || Settings.Global.getInt(contentResolver, "low_power_trigger_level", 0) <= 0) {
            return false;
        }
        return true;
    }

    public final void showAutoSaverSuggestionNotification() {
        String string = this.mContext.getString(R$string.auto_saver_text);
        Notification.Builder contentText = new Notification.Builder(this.mContext, NotificationChannels.HINTS).setSmallIcon(R$drawable.ic_power_saver).setWhen(0).setShowWhen(false).setContentTitle(this.mContext.getString(R$string.auto_saver_title)).setStyle(new Notification.BigTextStyle().bigText(string)).setContentText(string);
        contentText.setContentIntent(pendingBroadcast("PNW.enableAutoSaver"));
        contentText.setDeleteIntent(pendingBroadcast("PNW.dismissAutoSaverSuggestion"));
        contentText.addAction(0, this.mContext.getString(R$string.no_auto_saver_action), pendingBroadcast("PNW.autoSaverNoThanks"));
        SystemUIApplication.overrideNotificationAppName(this.mContext, contentText, false);
        this.mNoMan.notifyAsUser("auto_saver", 49, contentText.build(), UserHandle.ALL);
    }

    public final PendingIntent pendingBroadcast(String str) {
        return PendingIntent.getBroadcastAsUser(this.mContext, 0, new Intent(str).setPackage(this.mContext.getPackageName()).setFlags(268435456), 67108864, UserHandle.CURRENT);
    }

    public static Intent settings(String str) {
        return new Intent(str).setFlags(1551892480);
    }

    public boolean isInvalidChargerWarningShowing() {
        return this.mInvalidCharger;
    }

    public void dismissHighTemperatureWarning() {
        if (this.mHighTempWarning) {
            dismissHighTemperatureWarningInternal();
        }
    }

    public final void dismissHighTemperatureWarningInternal() {
        this.mNoMan.cancelAsUser("high_temp", 4, UserHandle.ALL);
        this.mHighTempWarning = false;
    }

    public void showHighTemperatureWarning() {
        if (!this.mHighTempWarning) {
            this.mHighTempWarning = true;
            String string = this.mContext.getString(R$string.high_temp_notif_message);
            Notification.Builder color = new Notification.Builder(this.mContext, NotificationChannels.ALERTS).setSmallIcon(R$drawable.ic_device_thermostat_24).setWhen(0).setShowWhen(false).setContentTitle(this.mContext.getString(R$string.high_temp_title)).setContentText(string).setStyle(new Notification.BigTextStyle().bigText(string)).setVisibility(1).setContentIntent(pendingBroadcast("PNW.clickedTempWarning")).setDeleteIntent(pendingBroadcast("PNW.dismissedTempWarning")).setColor(Utils.getColorAttrDefaultColor(this.mContext, 16844099));
            SystemUIApplication.overrideNotificationAppName(this.mContext, color, false);
            this.mNoMan.notifyAsUser("high_temp", 4, color.build(), UserHandle.ALL);
        }
    }

    public final void showHighTemperatureDialog() {
        if (this.mHighTempDialog == null) {
            SystemUIDialog systemUIDialog = new SystemUIDialog(this.mContext);
            systemUIDialog.setIconAttribute(16843605);
            systemUIDialog.setTitle(R$string.high_temp_title);
            systemUIDialog.setMessage(R$string.high_temp_dialog_message);
            systemUIDialog.setPositiveButton(17039370, (DialogInterface.OnClickListener) null);
            systemUIDialog.setShowForAllUsers(true);
            systemUIDialog.setOnDismissListener(new PowerNotificationWarnings$$ExternalSyntheticLambda9(this));
            final String string = this.mContext.getString(R$string.high_temp_dialog_help_url);
            if (!string.isEmpty()) {
                systemUIDialog.setNeutralButton(R$string.high_temp_dialog_help_text, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PowerNotificationWarnings.this.mActivityStarter.startActivity(new Intent("android.intent.action.VIEW").setData(Uri.parse(string)).setFlags(268435456), true, (ActivityStarter.Callback) new PowerNotificationWarnings$1$$ExternalSyntheticLambda0(this));
                    }

                    /* access modifiers changed from: private */
                    public /* synthetic */ void lambda$onClick$0(int i) {
                        PowerNotificationWarnings.this.mHighTempDialog = null;
                    }
                });
            }
            systemUIDialog.show();
            this.mHighTempDialog = systemUIDialog;
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$showHighTemperatureDialog$0(DialogInterface dialogInterface) {
        this.mHighTempDialog = null;
    }

    public void dismissThermalShutdownWarning() {
        this.mNoMan.cancelAsUser("high_temp", 39, UserHandle.ALL);
    }

    public final void showThermalShutdownDialog() {
        if (this.mThermalShutdownDialog == null) {
            SystemUIDialog systemUIDialog = new SystemUIDialog(this.mContext);
            systemUIDialog.setIconAttribute(16843605);
            systemUIDialog.setTitle(R$string.thermal_shutdown_title);
            systemUIDialog.setMessage(R$string.thermal_shutdown_dialog_message);
            systemUIDialog.setPositiveButton(17039370, (DialogInterface.OnClickListener) null);
            systemUIDialog.setShowForAllUsers(true);
            systemUIDialog.setOnDismissListener(new PowerNotificationWarnings$$ExternalSyntheticLambda4(this));
            final String string = this.mContext.getString(R$string.thermal_shutdown_dialog_help_url);
            if (!string.isEmpty()) {
                systemUIDialog.setNeutralButton(R$string.thermal_shutdown_dialog_help_text, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PowerNotificationWarnings.this.mActivityStarter.startActivity(new Intent("android.intent.action.VIEW").setData(Uri.parse(string)).setFlags(268435456), true, (ActivityStarter.Callback) new PowerNotificationWarnings$2$$ExternalSyntheticLambda0(this));
                    }

                    /* access modifiers changed from: private */
                    public /* synthetic */ void lambda$onClick$0(int i) {
                        PowerNotificationWarnings.this.mThermalShutdownDialog = null;
                    }
                });
            }
            systemUIDialog.show();
            this.mThermalShutdownDialog = systemUIDialog;
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$showThermalShutdownDialog$1(DialogInterface dialogInterface) {
        this.mThermalShutdownDialog = null;
    }

    public void showThermalShutdownWarning() {
        String string = this.mContext.getString(R$string.thermal_shutdown_message);
        Notification.Builder color = new Notification.Builder(this.mContext, NotificationChannels.ALERTS).setSmallIcon(R$drawable.ic_device_thermostat_24).setWhen(0).setShowWhen(false).setContentTitle(this.mContext.getString(R$string.thermal_shutdown_title)).setContentText(string).setStyle(new Notification.BigTextStyle().bigText(string)).setVisibility(1).setContentIntent(pendingBroadcast("PNW.clickedThermalShutdownWarning")).setDeleteIntent(pendingBroadcast("PNW.dismissedThermalShutdownWarning")).setColor(Utils.getColorAttrDefaultColor(this.mContext, 16844099));
        SystemUIApplication.overrideNotificationAppName(this.mContext, color, false);
        this.mNoMan.notifyAsUser("high_temp", 39, color.build(), UserHandle.ALL);
    }

    public void showUsbHighTemperatureAlarm() {
        this.mHandler.post(new PowerNotificationWarnings$$ExternalSyntheticLambda0(this));
    }

    /* renamed from: showUsbHighTemperatureAlarmInternal */
    public final void lambda$showUsbHighTemperatureAlarm$2() {
        if (this.mUsbHighTempDialog == null) {
            SystemUIDialog systemUIDialog = new SystemUIDialog(this.mContext, R$style.Theme_SystemUI_Dialog_Alert);
            systemUIDialog.setCancelable(false);
            systemUIDialog.setIconAttribute(16843605);
            systemUIDialog.setTitle(R$string.high_temp_alarm_title);
            systemUIDialog.setShowForAllUsers(true);
            systemUIDialog.setMessage(this.mContext.getString(R$string.high_temp_alarm_notify_message, new Object[]{""}));
            systemUIDialog.setPositiveButton(17039370, new PowerNotificationWarnings$$ExternalSyntheticLambda1(this));
            systemUIDialog.setNegativeButton(R$string.high_temp_alarm_help_care_steps, new PowerNotificationWarnings$$ExternalSyntheticLambda2(this));
            systemUIDialog.setOnDismissListener(new PowerNotificationWarnings$$ExternalSyntheticLambda3(this));
            systemUIDialog.getWindow().addFlags(2097280);
            systemUIDialog.show();
            this.mUsbHighTempDialog = systemUIDialog;
            Events.writeEvent(19, 3, Boolean.valueOf(this.mKeyguard.isKeyguardLocked()));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$showUsbHighTemperatureAlarmInternal$3(DialogInterface dialogInterface, int i) {
        this.mUsbHighTempDialog = null;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$showUsbHighTemperatureAlarmInternal$5(DialogInterface dialogInterface, int i) {
        String string = this.mContext.getString(R$string.high_temp_alarm_help_url);
        Intent intent = new Intent();
        intent.setClassName(ThemeOverlayApplier.SETTINGS_PACKAGE, "com.android.settings.HelpTrampoline");
        intent.putExtra("android.intent.extra.TEXT", string);
        this.mActivityStarter.startActivity(intent, true, (ActivityStarter.Callback) new PowerNotificationWarnings$$ExternalSyntheticLambda10(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$showUsbHighTemperatureAlarmInternal$4(int i) {
        this.mUsbHighTempDialog = null;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$showUsbHighTemperatureAlarmInternal$6(DialogInterface dialogInterface) {
        this.mUsbHighTempDialog = null;
        Events.writeEvent(20, 9, Boolean.valueOf(this.mKeyguard.isKeyguardLocked()));
    }

    public void updateLowBatteryWarning() {
        updateNotification();
    }

    public void dismissLowBatteryWarning() {
        if (DEBUG) {
            Slog.d("PowerUI.Notification", "dismissing low battery warning: level=" + this.mBatteryLevel);
        }
        dismissLowBatteryNotification();
    }

    public final void dismissLowBatteryNotification() {
        if (this.mWarning) {
            Slog.i("PowerUI.Notification", "dismissing low battery notification");
        }
        this.mWarning = false;
        updateNotification();
    }

    public final boolean hasBatterySettings() {
        return this.mOpenBatterySettings.resolveActivity(this.mContext.getPackageManager()) != null;
    }

    public void showLowBatteryWarning(boolean z) {
        Slog.i("PowerUI.Notification", "show low battery warning: level=" + this.mBatteryLevel + " [" + this.mBucket + "] playSound=" + z);
        logEvent(BatteryWarningEvents$LowBatteryWarningEvent.LOW_BATTERY_NOTIFICATION);
        this.mPlaySound = z;
        this.mWarning = true;
        updateNotification();
    }

    public void dismissInvalidChargerWarning() {
        dismissInvalidChargerNotification();
    }

    public final void dismissInvalidChargerNotification() {
        if (this.mInvalidCharger) {
            Slog.i("PowerUI.Notification", "dismissing invalid charger notification");
        }
        this.mInvalidCharger = false;
        updateNotification();
    }

    public void showInvalidChargerWarning() {
        this.mInvalidCharger = true;
        updateNotification();
    }

    public final void showAutoSaverSuggestion() {
        this.mShowAutoSaverSuggestion = true;
        updateNotification();
    }

    public final void dismissAutoSaverSuggestion() {
        this.mShowAutoSaverSuggestion = false;
        updateNotification();
    }

    public void userSwitched() {
        updateNotification();
    }

    public final void showStartSaverConfirmation(Bundle bundle) {
        if (this.mSaverConfirmation == null) {
            SystemUIDialog systemUIDialog = new SystemUIDialog(this.mContext);
            boolean z = bundle.getBoolean("extra_confirm_only");
            int i = bundle.getInt("extra_power_save_mode_trigger", 0);
            int i2 = bundle.getInt("extra_power_save_mode_trigger_level", 0);
            systemUIDialog.setMessage(getBatterySaverDescription());
            if (isEnglishLocale()) {
                systemUIDialog.setMessageHyphenationFrequency(0);
            }
            systemUIDialog.setMessageMovementMethod(LinkMovementMethod.getInstance());
            if (z) {
                systemUIDialog.setTitle(R$string.battery_saver_confirmation_title_generic);
                systemUIDialog.setPositiveButton(17040080, new PowerNotificationWarnings$$ExternalSyntheticLambda5(this, i, i2));
            } else {
                systemUIDialog.setTitle(R$string.battery_saver_confirmation_title);
                systemUIDialog.setPositiveButton(R$string.battery_saver_confirmation_ok, new PowerNotificationWarnings$$ExternalSyntheticLambda6(this));
                systemUIDialog.setNegativeButton(17039360, new PowerNotificationWarnings$$ExternalSyntheticLambda7(this));
            }
            systemUIDialog.setShowForAllUsers(true);
            systemUIDialog.setOnDismissListener(new PowerNotificationWarnings$$ExternalSyntheticLambda8(this));
            WeakReference<View> lastPowerSaverStartView = this.mBatteryControllerLazy.get().getLastPowerSaverStartView();
            if (lastPowerSaverStartView == null || lastPowerSaverStartView.get() == null || !((View) lastPowerSaverStartView.get()).isAggregatedVisible()) {
                systemUIDialog.show();
            } else {
                this.mDialogLaunchAnimator.showFromView(systemUIDialog, (View) lastPowerSaverStartView.get());
            }
            logEvent(BatteryWarningEvents$LowBatteryWarningEvent.SAVER_CONFIRM_DIALOG);
            this.mSaverConfirmation = systemUIDialog;
            this.mBatteryControllerLazy.get().clearLastPowerSaverStartView();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$showStartSaverConfirmation$7(int i, int i2, DialogInterface dialogInterface, int i3) {
        ContentResolver contentResolver = this.mContext.getContentResolver();
        Settings.Global.putInt(contentResolver, "automatic_power_save_mode", i);
        Settings.Global.putInt(contentResolver, "low_power_trigger_level", i2);
        Settings.Secure.putIntForUser(contentResolver, "low_power_warning_acknowledged", 1, -2);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$showStartSaverConfirmation$8(DialogInterface dialogInterface, int i) {
        setSaverMode(true, false);
        logEvent(BatteryWarningEvents$LowBatteryWarningEvent.SAVER_CONFIRM_OK);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$showStartSaverConfirmation$9(DialogInterface dialogInterface, int i) {
        logEvent(BatteryWarningEvents$LowBatteryWarningEvent.SAVER_CONFIRM_CANCEL);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$showStartSaverConfirmation$10(DialogInterface dialogInterface) {
        this.mSaverConfirmation = null;
        logEvent(BatteryWarningEvents$LowBatteryWarningEvent.SAVER_CONFIRM_DISMISS);
    }

    public Dialog getSaverConfirmationDialog() {
        return this.mSaverConfirmation;
    }

    public final boolean isEnglishLocale() {
        return Objects.equals(Locale.getDefault().getLanguage(), Locale.ENGLISH.getLanguage());
    }

    public final CharSequence getBatterySaverDescription() {
        String charSequence = this.mContext.getText(R$string.help_uri_battery_saver_learn_more_link_target).toString();
        if (TextUtils.isEmpty(charSequence)) {
            return this.mContext.getText(R$string.battery_low_intro);
        }
        SpannableString spannableString = new SpannableString(this.mContext.getText(17039799));
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(spannableString);
        for (Annotation annotation : (Annotation[]) spannableString.getSpans(0, spannableString.length(), Annotation.class)) {
            if ("url".equals(annotation.getValue())) {
                int spanStart = spannableString.getSpanStart(annotation);
                int spanEnd = spannableString.getSpanEnd(annotation);
                AnonymousClass3 r8 = new URLSpan(charSequence) {
                    public void updateDrawState(TextPaint textPaint) {
                        super.updateDrawState(textPaint);
                        textPaint.setUnderlineText(false);
                    }

                    public void onClick(View view) {
                        if (PowerNotificationWarnings.this.mSaverConfirmation != null) {
                            PowerNotificationWarnings.this.mSaverConfirmation.dismiss();
                        }
                        PowerNotificationWarnings.this.mBroadcastSender.sendBroadcast(new Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS").setFlags(268435456));
                        Uri parse = Uri.parse(getURL());
                        Context context = view.getContext();
                        Intent flags = new Intent("android.intent.action.VIEW", parse).setFlags(268435456);
                        try {
                            context.startActivity(flags);
                        } catch (ActivityNotFoundException unused) {
                            Log.w("PowerUI.Notification", "Activity was not found for intent, " + flags.toString());
                        }
                    }
                };
                spannableStringBuilder.setSpan(r8, spanStart, spanEnd, spannableString.getSpanFlags(r8));
            }
        }
        return spannableStringBuilder;
    }

    public final void setSaverMode(boolean z, boolean z2) {
        BatterySaverUtils.setPowerSaveMode(this.mContext, z, z2);
    }

    public final void startBatterySaverSchedulePage() {
        Intent intent = new Intent("com.android.settings.BATTERY_SAVER_SCHEDULE_SETTINGS");
        intent.setFlags(268468224);
        this.mActivityStarter.startActivity(intent, true);
    }

    public final void logEvent(BatteryWarningEvents$LowBatteryWarningEvent batteryWarningEvents$LowBatteryWarningEvent) {
        UiEventLogger uiEventLogger = this.mUiEventLogger;
        if (uiEventLogger != null) {
            uiEventLogger.log(batteryWarningEvents$LowBatteryWarningEvent);
        }
    }

    public final class Receiver extends BroadcastReceiver {
        public Receiver() {
        }

        public void init() {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("PNW.batterySaverSettings");
            intentFilter.addAction("PNW.startSaver");
            intentFilter.addAction("PNW.dismissedWarning");
            intentFilter.addAction("PNW.clickedTempWarning");
            intentFilter.addAction("PNW.dismissedTempWarning");
            intentFilter.addAction("PNW.clickedThermalShutdownWarning");
            intentFilter.addAction("PNW.dismissedThermalShutdownWarning");
            intentFilter.addAction("PNW.startSaverConfirmation");
            intentFilter.addAction("PNW.autoSaverSuggestion");
            intentFilter.addAction("PNW.enableAutoSaver");
            intentFilter.addAction("PNW.autoSaverNoThanks");
            intentFilter.addAction("PNW.dismissAutoSaverSuggestion");
            PowerNotificationWarnings.this.mContext.registerReceiverAsUser(this, UserHandle.ALL, intentFilter, "android.permission.DEVICE_POWER", PowerNotificationWarnings.this.mHandler, 2);
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Slog.i("PowerUI.Notification", "Received " + action);
            if (action.equals("PNW.batterySaverSettings")) {
                PowerNotificationWarnings.this.logEvent(BatteryWarningEvents$LowBatteryWarningEvent.LOW_BATTERY_NOTIFICATION_SETTINGS);
                PowerNotificationWarnings.this.dismissLowBatteryNotification();
                PowerNotificationWarnings.this.mContext.startActivityAsUser(PowerNotificationWarnings.this.mOpenBatterySaverSettings, UserHandle.CURRENT);
            } else if (action.equals("PNW.startSaver")) {
                PowerNotificationWarnings.this.logEvent(BatteryWarningEvents$LowBatteryWarningEvent.LOW_BATTERY_NOTIFICATION_TURN_ON);
                PowerNotificationWarnings.this.setSaverMode(true, true);
                PowerNotificationWarnings.this.dismissLowBatteryNotification();
            } else if (action.equals("PNW.startSaverConfirmation")) {
                PowerNotificationWarnings.this.dismissLowBatteryNotification();
                PowerNotificationWarnings.this.showStartSaverConfirmation(intent.getExtras());
            } else if (action.equals("PNW.dismissedWarning")) {
                PowerNotificationWarnings.this.logEvent(BatteryWarningEvents$LowBatteryWarningEvent.LOW_BATTERY_NOTIFICATION_CANCEL);
                PowerNotificationWarnings.this.dismissLowBatteryWarning();
            } else if ("PNW.clickedTempWarning".equals(action)) {
                PowerNotificationWarnings.this.dismissHighTemperatureWarningInternal();
                PowerNotificationWarnings.this.showHighTemperatureDialog();
            } else if ("PNW.dismissedTempWarning".equals(action)) {
                PowerNotificationWarnings.this.dismissHighTemperatureWarningInternal();
            } else if ("PNW.clickedThermalShutdownWarning".equals(action)) {
                PowerNotificationWarnings.this.dismissThermalShutdownWarning();
                PowerNotificationWarnings.this.showThermalShutdownDialog();
            } else if ("PNW.dismissedThermalShutdownWarning".equals(action)) {
                PowerNotificationWarnings.this.dismissThermalShutdownWarning();
            } else if ("PNW.autoSaverSuggestion".equals(action)) {
                PowerNotificationWarnings.this.showAutoSaverSuggestion();
            } else if ("PNW.dismissAutoSaverSuggestion".equals(action)) {
                PowerNotificationWarnings.this.dismissAutoSaverSuggestion();
            } else if ("PNW.enableAutoSaver".equals(action)) {
                PowerNotificationWarnings.this.dismissAutoSaverSuggestion();
                PowerNotificationWarnings.this.startBatterySaverSchedulePage();
            } else if ("PNW.autoSaverNoThanks".equals(action)) {
                PowerNotificationWarnings.this.dismissAutoSaverSuggestion();
                BatterySaverUtils.suppressAutoBatterySaver(context);
            }
        }
    }
}
