package com.android.systemui.biometrics;

import android.app.ActivityTaskManager;
import android.content.Context;
import android.hardware.display.DisplayManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.WindowManager;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.util.concurrency.DelayableExecutor;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class SidefpsController_Factory implements Factory<SidefpsController> {
    public final Provider<ActivityTaskManager> activityTaskManagerProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DisplayManager> displayManagerProvider;
    public final Provider<FingerprintManager> fingerprintManagerProvider;
    public final Provider<Handler> handlerProvider;
    public final Provider<LayoutInflater> layoutInflaterProvider;
    public final Provider<DelayableExecutor> mainExecutorProvider;
    public final Provider<OverviewProxyService> overviewProxyServiceProvider;
    public final Provider<WindowManager> windowManagerProvider;

    public SidefpsController_Factory(Provider<Context> provider, Provider<LayoutInflater> provider2, Provider<FingerprintManager> provider3, Provider<WindowManager> provider4, Provider<ActivityTaskManager> provider5, Provider<OverviewProxyService> provider6, Provider<DisplayManager> provider7, Provider<DelayableExecutor> provider8, Provider<Handler> provider9) {
        this.contextProvider = provider;
        this.layoutInflaterProvider = provider2;
        this.fingerprintManagerProvider = provider3;
        this.windowManagerProvider = provider4;
        this.activityTaskManagerProvider = provider5;
        this.overviewProxyServiceProvider = provider6;
        this.displayManagerProvider = provider7;
        this.mainExecutorProvider = provider8;
        this.handlerProvider = provider9;
    }

    public SidefpsController get() {
        return newInstance(this.contextProvider.get(), this.layoutInflaterProvider.get(), this.fingerprintManagerProvider.get(), this.windowManagerProvider.get(), this.activityTaskManagerProvider.get(), this.overviewProxyServiceProvider.get(), this.displayManagerProvider.get(), this.mainExecutorProvider.get(), this.handlerProvider.get());
    }

    public static SidefpsController_Factory create(Provider<Context> provider, Provider<LayoutInflater> provider2, Provider<FingerprintManager> provider3, Provider<WindowManager> provider4, Provider<ActivityTaskManager> provider5, Provider<OverviewProxyService> provider6, Provider<DisplayManager> provider7, Provider<DelayableExecutor> provider8, Provider<Handler> provider9) {
        return new SidefpsController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9);
    }

    public static SidefpsController newInstance(Context context, LayoutInflater layoutInflater, FingerprintManager fingerprintManager, WindowManager windowManager, ActivityTaskManager activityTaskManager, OverviewProxyService overviewProxyService, DisplayManager displayManager, DelayableExecutor delayableExecutor, Handler handler) {
        return new SidefpsController(context, layoutInflater, fingerprintManager, windowManager, activityTaskManager, overviewProxyService, displayManager, delayableExecutor, handler);
    }
}
