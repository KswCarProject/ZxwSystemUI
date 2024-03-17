package com.android.systemui.power;

import android.content.Context;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.animation.DialogLaunchAnimator;
import com.android.systemui.broadcast.BroadcastSender;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.statusbar.policy.BatteryController;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class PowerNotificationWarnings_Factory implements Factory<PowerNotificationWarnings> {
    public final Provider<ActivityStarter> activityStarterProvider;
    public final Provider<BatteryController> batteryControllerLazyProvider;
    public final Provider<BroadcastSender> broadcastSenderProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DialogLaunchAnimator> dialogLaunchAnimatorProvider;
    public final Provider<UiEventLogger> uiEventLoggerProvider;

    public PowerNotificationWarnings_Factory(Provider<Context> provider, Provider<ActivityStarter> provider2, Provider<BroadcastSender> provider3, Provider<BatteryController> provider4, Provider<DialogLaunchAnimator> provider5, Provider<UiEventLogger> provider6) {
        this.contextProvider = provider;
        this.activityStarterProvider = provider2;
        this.broadcastSenderProvider = provider3;
        this.batteryControllerLazyProvider = provider4;
        this.dialogLaunchAnimatorProvider = provider5;
        this.uiEventLoggerProvider = provider6;
    }

    public PowerNotificationWarnings get() {
        return newInstance(this.contextProvider.get(), this.activityStarterProvider.get(), this.broadcastSenderProvider.get(), DoubleCheck.lazy(this.batteryControllerLazyProvider), this.dialogLaunchAnimatorProvider.get(), this.uiEventLoggerProvider.get());
    }

    public static PowerNotificationWarnings_Factory create(Provider<Context> provider, Provider<ActivityStarter> provider2, Provider<BroadcastSender> provider3, Provider<BatteryController> provider4, Provider<DialogLaunchAnimator> provider5, Provider<UiEventLogger> provider6) {
        return new PowerNotificationWarnings_Factory(provider, provider2, provider3, provider4, provider5, provider6);
    }

    public static PowerNotificationWarnings newInstance(Context context, ActivityStarter activityStarter, BroadcastSender broadcastSender, Lazy<BatteryController> lazy, DialogLaunchAnimator dialogLaunchAnimator, UiEventLogger uiEventLogger) {
        return new PowerNotificationWarnings(context, activityStarter, broadcastSender, lazy, dialogLaunchAnimator, uiEventLogger);
    }
}
