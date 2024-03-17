package com.android.systemui.dreams;

import android.app.AlarmManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import com.android.systemui.statusbar.policy.IndividualSensorPrivacyController;
import com.android.systemui.statusbar.policy.NextAlarmController;
import com.android.systemui.statusbar.policy.ZenModeController;
import com.android.systemui.statusbar.window.StatusBarWindowStateController;
import com.android.systemui.touch.TouchInsetManager;
import com.android.systemui.util.time.DateFormatUtil;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class DreamOverlayStatusBarViewController_Factory implements Factory<DreamOverlayStatusBarViewController> {
    public final Provider<AlarmManager> alarmManagerProvider;
    public final Provider<ConnectivityManager> connectivityManagerProvider;
    public final Provider<DateFormatUtil> dateFormatUtilProvider;
    public final Provider<DreamOverlayNotificationCountProvider> dreamOverlayNotificationCountProvider;
    public final Provider<Executor> mainExecutorProvider;
    public final Provider<NextAlarmController> nextAlarmControllerProvider;
    public final Provider<Resources> resourcesProvider;
    public final Provider<IndividualSensorPrivacyController> sensorPrivacyControllerProvider;
    public final Provider<StatusBarWindowStateController> statusBarWindowStateControllerProvider;
    public final Provider<TouchInsetManager.TouchInsetSession> touchInsetSessionProvider;
    public final Provider<DreamOverlayStatusBarView> viewProvider;
    public final Provider<ZenModeController> zenModeControllerProvider;

    public DreamOverlayStatusBarViewController_Factory(Provider<DreamOverlayStatusBarView> provider, Provider<Resources> provider2, Provider<Executor> provider3, Provider<ConnectivityManager> provider4, Provider<TouchInsetManager.TouchInsetSession> provider5, Provider<AlarmManager> provider6, Provider<NextAlarmController> provider7, Provider<DateFormatUtil> provider8, Provider<IndividualSensorPrivacyController> provider9, Provider<DreamOverlayNotificationCountProvider> provider10, Provider<ZenModeController> provider11, Provider<StatusBarWindowStateController> provider12) {
        this.viewProvider = provider;
        this.resourcesProvider = provider2;
        this.mainExecutorProvider = provider3;
        this.connectivityManagerProvider = provider4;
        this.touchInsetSessionProvider = provider5;
        this.alarmManagerProvider = provider6;
        this.nextAlarmControllerProvider = provider7;
        this.dateFormatUtilProvider = provider8;
        this.sensorPrivacyControllerProvider = provider9;
        this.dreamOverlayNotificationCountProvider = provider10;
        this.zenModeControllerProvider = provider11;
        this.statusBarWindowStateControllerProvider = provider12;
    }

    public DreamOverlayStatusBarViewController get() {
        return newInstance(this.viewProvider.get(), this.resourcesProvider.get(), this.mainExecutorProvider.get(), this.connectivityManagerProvider.get(), this.touchInsetSessionProvider.get(), this.alarmManagerProvider.get(), this.nextAlarmControllerProvider.get(), this.dateFormatUtilProvider.get(), this.sensorPrivacyControllerProvider.get(), this.dreamOverlayNotificationCountProvider.get(), this.zenModeControllerProvider.get(), this.statusBarWindowStateControllerProvider.get());
    }

    public static DreamOverlayStatusBarViewController_Factory create(Provider<DreamOverlayStatusBarView> provider, Provider<Resources> provider2, Provider<Executor> provider3, Provider<ConnectivityManager> provider4, Provider<TouchInsetManager.TouchInsetSession> provider5, Provider<AlarmManager> provider6, Provider<NextAlarmController> provider7, Provider<DateFormatUtil> provider8, Provider<IndividualSensorPrivacyController> provider9, Provider<DreamOverlayNotificationCountProvider> provider10, Provider<ZenModeController> provider11, Provider<StatusBarWindowStateController> provider12) {
        return new DreamOverlayStatusBarViewController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12);
    }

    public static DreamOverlayStatusBarViewController newInstance(DreamOverlayStatusBarView dreamOverlayStatusBarView, Resources resources, Executor executor, ConnectivityManager connectivityManager, TouchInsetManager.TouchInsetSession touchInsetSession, AlarmManager alarmManager, NextAlarmController nextAlarmController, DateFormatUtil dateFormatUtil, IndividualSensorPrivacyController individualSensorPrivacyController, DreamOverlayNotificationCountProvider dreamOverlayNotificationCountProvider2, ZenModeController zenModeController, StatusBarWindowStateController statusBarWindowStateController) {
        return new DreamOverlayStatusBarViewController(dreamOverlayStatusBarView, resources, executor, connectivityManager, touchInsetSession, alarmManager, nextAlarmController, dateFormatUtil, individualSensorPrivacyController, dreamOverlayNotificationCountProvider2, zenModeController, statusBarWindowStateController);
    }
}
