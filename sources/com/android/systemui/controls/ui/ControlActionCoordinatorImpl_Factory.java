package com.android.systemui.controls.ui;

import android.content.Context;
import android.os.Handler;
import com.android.systemui.broadcast.BroadcastSender;
import com.android.systemui.controls.ControlsMetricsLogger;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.settings.UserContextProvider;
import com.android.systemui.statusbar.VibratorHelper;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.settings.SecureSettings;
import com.android.wm.shell.TaskViewFactory;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;

public final class ControlActionCoordinatorImpl_Factory implements Factory<ControlActionCoordinatorImpl> {
    public final Provider<ActivityStarter> activityStarterProvider;
    public final Provider<DelayableExecutor> bgExecutorProvider;
    public final Provider<BroadcastSender> broadcastSenderProvider;
    public final Provider<Context> contextProvider;
    public final Provider<ControlsMetricsLogger> controlsMetricsLoggerProvider;
    public final Provider<KeyguardStateController> keyguardStateControllerProvider;
    public final Provider<Handler> mainHandlerProvider;
    public final Provider<SecureSettings> secureSettingsProvider;
    public final Provider<Optional<TaskViewFactory>> taskViewFactoryProvider;
    public final Provider<DelayableExecutor> uiExecutorProvider;
    public final Provider<UserContextProvider> userContextProvider;
    public final Provider<VibratorHelper> vibratorProvider;

    public ControlActionCoordinatorImpl_Factory(Provider<Context> provider, Provider<DelayableExecutor> provider2, Provider<DelayableExecutor> provider3, Provider<ActivityStarter> provider4, Provider<BroadcastSender> provider5, Provider<KeyguardStateController> provider6, Provider<Optional<TaskViewFactory>> provider7, Provider<ControlsMetricsLogger> provider8, Provider<VibratorHelper> provider9, Provider<SecureSettings> provider10, Provider<UserContextProvider> provider11, Provider<Handler> provider12) {
        this.contextProvider = provider;
        this.bgExecutorProvider = provider2;
        this.uiExecutorProvider = provider3;
        this.activityStarterProvider = provider4;
        this.broadcastSenderProvider = provider5;
        this.keyguardStateControllerProvider = provider6;
        this.taskViewFactoryProvider = provider7;
        this.controlsMetricsLoggerProvider = provider8;
        this.vibratorProvider = provider9;
        this.secureSettingsProvider = provider10;
        this.userContextProvider = provider11;
        this.mainHandlerProvider = provider12;
    }

    public ControlActionCoordinatorImpl get() {
        return newInstance(this.contextProvider.get(), this.bgExecutorProvider.get(), this.uiExecutorProvider.get(), this.activityStarterProvider.get(), this.broadcastSenderProvider.get(), this.keyguardStateControllerProvider.get(), this.taskViewFactoryProvider.get(), this.controlsMetricsLoggerProvider.get(), this.vibratorProvider.get(), this.secureSettingsProvider.get(), this.userContextProvider.get(), this.mainHandlerProvider.get());
    }

    public static ControlActionCoordinatorImpl_Factory create(Provider<Context> provider, Provider<DelayableExecutor> provider2, Provider<DelayableExecutor> provider3, Provider<ActivityStarter> provider4, Provider<BroadcastSender> provider5, Provider<KeyguardStateController> provider6, Provider<Optional<TaskViewFactory>> provider7, Provider<ControlsMetricsLogger> provider8, Provider<VibratorHelper> provider9, Provider<SecureSettings> provider10, Provider<UserContextProvider> provider11, Provider<Handler> provider12) {
        return new ControlActionCoordinatorImpl_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12);
    }

    public static ControlActionCoordinatorImpl newInstance(Context context, DelayableExecutor delayableExecutor, DelayableExecutor delayableExecutor2, ActivityStarter activityStarter, BroadcastSender broadcastSender, KeyguardStateController keyguardStateController, Optional<TaskViewFactory> optional, ControlsMetricsLogger controlsMetricsLogger, VibratorHelper vibratorHelper, SecureSettings secureSettings, UserContextProvider userContextProvider2, Handler handler) {
        return new ControlActionCoordinatorImpl(context, delayableExecutor, delayableExecutor2, activityStarter, broadcastSender, keyguardStateController, optional, controlsMetricsLogger, vibratorHelper, secureSettings, userContextProvider2, handler);
    }
}
