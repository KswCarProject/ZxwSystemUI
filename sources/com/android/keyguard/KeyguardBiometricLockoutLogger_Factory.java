package com.android.keyguard;

import android.content.Context;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.log.SessionTracker;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class KeyguardBiometricLockoutLogger_Factory implements Factory<KeyguardBiometricLockoutLogger> {
    public final Provider<Context> contextProvider;
    public final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    public final Provider<SessionTracker> sessionTrackerProvider;
    public final Provider<UiEventLogger> uiEventLoggerProvider;

    public KeyguardBiometricLockoutLogger_Factory(Provider<Context> provider, Provider<UiEventLogger> provider2, Provider<KeyguardUpdateMonitor> provider3, Provider<SessionTracker> provider4) {
        this.contextProvider = provider;
        this.uiEventLoggerProvider = provider2;
        this.keyguardUpdateMonitorProvider = provider3;
        this.sessionTrackerProvider = provider4;
    }

    public KeyguardBiometricLockoutLogger get() {
        return newInstance(this.contextProvider.get(), this.uiEventLoggerProvider.get(), this.keyguardUpdateMonitorProvider.get(), this.sessionTrackerProvider.get());
    }

    public static KeyguardBiometricLockoutLogger_Factory create(Provider<Context> provider, Provider<UiEventLogger> provider2, Provider<KeyguardUpdateMonitor> provider3, Provider<SessionTracker> provider4) {
        return new KeyguardBiometricLockoutLogger_Factory(provider, provider2, provider3, provider4);
    }

    public static KeyguardBiometricLockoutLogger newInstance(Context context, UiEventLogger uiEventLogger, KeyguardUpdateMonitor keyguardUpdateMonitor, SessionTracker sessionTracker) {
        return new KeyguardBiometricLockoutLogger(context, uiEventLogger, keyguardUpdateMonitor, sessionTracker);
    }
}
