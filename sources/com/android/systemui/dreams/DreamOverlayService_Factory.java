package com.android.systemui.dreams;

import android.content.Context;
import com.android.internal.logging.UiEventLogger;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.dreams.dagger.DreamOverlayComponent;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class DreamOverlayService_Factory implements Factory<DreamOverlayService> {
    public final Provider<Context> contextProvider;
    public final Provider<DreamOverlayComponent.Factory> dreamOverlayComponentFactoryProvider;
    public final Provider<Executor> executorProvider;
    public final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    public final Provider<DreamOverlayStateController> stateControllerProvider;
    public final Provider<UiEventLogger> uiEventLoggerProvider;

    public DreamOverlayService_Factory(Provider<Context> provider, Provider<Executor> provider2, Provider<DreamOverlayComponent.Factory> provider3, Provider<DreamOverlayStateController> provider4, Provider<KeyguardUpdateMonitor> provider5, Provider<UiEventLogger> provider6) {
        this.contextProvider = provider;
        this.executorProvider = provider2;
        this.dreamOverlayComponentFactoryProvider = provider3;
        this.stateControllerProvider = provider4;
        this.keyguardUpdateMonitorProvider = provider5;
        this.uiEventLoggerProvider = provider6;
    }

    public DreamOverlayService get() {
        return newInstance(this.contextProvider.get(), this.executorProvider.get(), this.dreamOverlayComponentFactoryProvider.get(), this.stateControllerProvider.get(), this.keyguardUpdateMonitorProvider.get(), this.uiEventLoggerProvider.get());
    }

    public static DreamOverlayService_Factory create(Provider<Context> provider, Provider<Executor> provider2, Provider<DreamOverlayComponent.Factory> provider3, Provider<DreamOverlayStateController> provider4, Provider<KeyguardUpdateMonitor> provider5, Provider<UiEventLogger> provider6) {
        return new DreamOverlayService_Factory(provider, provider2, provider3, provider4, provider5, provider6);
    }

    public static DreamOverlayService newInstance(Context context, Executor executor, DreamOverlayComponent.Factory factory, DreamOverlayStateController dreamOverlayStateController, KeyguardUpdateMonitor keyguardUpdateMonitor, UiEventLogger uiEventLogger) {
        return new DreamOverlayService(context, executor, factory, dreamOverlayStateController, keyguardUpdateMonitor, uiEventLogger);
    }
}
