package com.android.systemui.dreams;

import android.app.AlarmManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.text.format.DateFormat;
import android.util.PluralsMessageFormatter;
import com.android.systemui.R$string;
import com.android.systemui.dreams.DreamOverlayNotificationCountProvider;
import com.android.systemui.statusbar.policy.IndividualSensorPrivacyController;
import com.android.systemui.statusbar.policy.NextAlarmController;
import com.android.systemui.statusbar.policy.ZenModeController;
import com.android.systemui.statusbar.window.StatusBarWindowStateController;
import com.android.systemui.touch.TouchInsetManager;
import com.android.systemui.util.ViewController;
import com.android.systemui.util.time.DateFormatUtil;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executor;

public class DreamOverlayStatusBarViewController extends ViewController<DreamOverlayStatusBarView> {
    public final AlarmManager mAlarmManager;
    public final ConnectivityManager mConnectivityManager;
    public final DateFormatUtil mDateFormatUtil;
    public final DreamOverlayNotificationCountProvider mDreamOverlayNotificationCountProvider;
    public boolean mIsAttached;
    public final Executor mMainExecutor;
    public final ConnectivityManager.NetworkCallback mNetworkCallback = new ConnectivityManager.NetworkCallback() {
        public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
            DreamOverlayStatusBarViewController.this.updateWifiUnavailableStatusIcon();
        }

        public void onAvailable(Network network) {
            DreamOverlayStatusBarViewController.this.updateWifiUnavailableStatusIcon();
        }

        public void onLost(Network network) {
            DreamOverlayStatusBarViewController.this.updateWifiUnavailableStatusIcon();
        }
    };
    public final NetworkRequest mNetworkRequest = new NetworkRequest.Builder().clearCapabilities().addTransportType(1).build();
    public final NextAlarmController.NextAlarmChangeCallback mNextAlarmCallback = new DreamOverlayStatusBarViewController$$ExternalSyntheticLambda1(this);
    public final NextAlarmController mNextAlarmController;
    public final DreamOverlayNotificationCountProvider.Callback mNotificationCountCallback = new DreamOverlayStatusBarViewController$$ExternalSyntheticLambda2(this);
    public final Resources mResources;
    public final IndividualSensorPrivacyController.Callback mSensorCallback = new DreamOverlayStatusBarViewController$$ExternalSyntheticLambda0(this);
    public final IndividualSensorPrivacyController mSensorPrivacyController;
    public final TouchInsetManager.TouchInsetSession mTouchInsetSession;
    public final ZenModeController.Callback mZenModeCallback = new ZenModeController.Callback() {
        public void onZenChanged(int i) {
            DreamOverlayStatusBarViewController.this.updatePriorityModeStatusIcon();
        }
    };
    public final ZenModeController mZenModeController;

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(int i, boolean z) {
        updateMicCameraBlockedStatusIcon();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(AlarmManager.AlarmClockInfo alarmClockInfo) {
        updateAlarmStatusIcon();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2(int i) {
        showIcon(0, i > 0, i > 0 ? buildNotificationsContentDescription(i) : null);
    }

    public DreamOverlayStatusBarViewController(DreamOverlayStatusBarView dreamOverlayStatusBarView, Resources resources, Executor executor, ConnectivityManager connectivityManager, TouchInsetManager.TouchInsetSession touchInsetSession, AlarmManager alarmManager, NextAlarmController nextAlarmController, DateFormatUtil dateFormatUtil, IndividualSensorPrivacyController individualSensorPrivacyController, DreamOverlayNotificationCountProvider dreamOverlayNotificationCountProvider, ZenModeController zenModeController, StatusBarWindowStateController statusBarWindowStateController) {
        super(dreamOverlayStatusBarView);
        this.mResources = resources;
        this.mMainExecutor = executor;
        this.mConnectivityManager = connectivityManager;
        this.mTouchInsetSession = touchInsetSession;
        this.mAlarmManager = alarmManager;
        this.mNextAlarmController = nextAlarmController;
        this.mDateFormatUtil = dateFormatUtil;
        this.mSensorPrivacyController = individualSensorPrivacyController;
        this.mDreamOverlayNotificationCountProvider = dreamOverlayNotificationCountProvider;
        this.mZenModeController = zenModeController;
        statusBarWindowStateController.addListener(new DreamOverlayStatusBarViewController$$ExternalSyntheticLambda3(this));
    }

    public void onViewAttached() {
        this.mIsAttached = true;
        this.mConnectivityManager.registerNetworkCallback(this.mNetworkRequest, this.mNetworkCallback);
        updateWifiUnavailableStatusIcon();
        this.mNextAlarmController.addCallback(this.mNextAlarmCallback);
        updateAlarmStatusIcon();
        this.mSensorPrivacyController.addCallback(this.mSensorCallback);
        updateMicCameraBlockedStatusIcon();
        this.mZenModeController.addCallback(this.mZenModeCallback);
        updatePriorityModeStatusIcon();
        this.mDreamOverlayNotificationCountProvider.addCallback(this.mNotificationCountCallback);
        this.mTouchInsetSession.addViewToTracking(this.mView);
    }

    public void onViewDetached() {
        this.mZenModeController.removeCallback(this.mZenModeCallback);
        this.mSensorPrivacyController.removeCallback(this.mSensorCallback);
        this.mNextAlarmController.removeCallback(this.mNextAlarmCallback);
        this.mConnectivityManager.unregisterNetworkCallback(this.mNetworkCallback);
        this.mDreamOverlayNotificationCountProvider.removeCallback(this.mNotificationCountCallback);
        this.mTouchInsetSession.clear();
        this.mIsAttached = false;
    }

    public final void updateWifiUnavailableStatusIcon() {
        ConnectivityManager connectivityManager = this.mConnectivityManager;
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
        showIcon(1, !(networkCapabilities != null && networkCapabilities.hasTransport(1)));
    }

    public final void updateAlarmStatusIcon() {
        AlarmManager.AlarmClockInfo nextAlarmClock = this.mAlarmManager.getNextAlarmClock(-2);
        boolean z = nextAlarmClock != null && nextAlarmClock.getTriggerTime() > 0;
        showIcon(2, z, z ? buildAlarmContentDescription(nextAlarmClock) : null);
    }

    public final String buildAlarmContentDescription(AlarmManager.AlarmClockInfo alarmClockInfo) {
        return this.mResources.getString(R$string.accessibility_quick_settings_alarm, new Object[]{DateFormat.format(DateFormat.getBestDateTimePattern(Locale.getDefault(), this.mDateFormatUtil.is24HourFormat() ? "EHm" : "Ehma"), alarmClockInfo.getTriggerTime()).toString()});
    }

    public final void updateMicCameraBlockedStatusIcon() {
        boolean z = true;
        boolean isSensorBlocked = this.mSensorPrivacyController.isSensorBlocked(1);
        boolean isSensorBlocked2 = this.mSensorPrivacyController.isSensorBlocked(2);
        if (!isSensorBlocked || !isSensorBlocked2) {
            z = false;
        }
        showIcon(3, z);
    }

    public final String buildNotificationsContentDescription(int i) {
        return PluralsMessageFormatter.format(this.mResources, Map.of("count", Integer.valueOf(i)), R$string.dream_overlay_status_bar_notification_indicator);
    }

    public final void updatePriorityModeStatusIcon() {
        showIcon(4, this.mZenModeController.getZen() != 0);
    }

    public final void showIcon(int i, boolean z) {
        showIcon(i, z, (String) null);
    }

    public final void showIcon(int i, boolean z, String str) {
        this.mMainExecutor.execute(new DreamOverlayStatusBarViewController$$ExternalSyntheticLambda4(this, i, z, str));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$showIcon$3(int i, boolean z, String str) {
        if (this.mIsAttached) {
            ((DreamOverlayStatusBarView) this.mView).showIcon(i, z, str);
        }
    }

    public final void onSystemStatusBarStateChanged(int i) {
        this.mMainExecutor.execute(new DreamOverlayStatusBarViewController$$ExternalSyntheticLambda5(this, i));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onSystemStatusBarStateChanged$4(int i) {
        if (this.mIsAttached) {
            if (i == 0) {
                ((DreamOverlayStatusBarView) this.mView).setVisibility(4);
            } else if (i == 1 || i == 2) {
                ((DreamOverlayStatusBarView) this.mView).setVisibility(0);
            }
        }
    }
}
