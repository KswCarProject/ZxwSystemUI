package com.android.keyguard;

import android.content.Context;
import android.os.Handler;
import com.android.keyguard.AdminSecondaryLockScreenController;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class AdminSecondaryLockScreenController_Factory_Factory implements Factory<AdminSecondaryLockScreenController.Factory> {
    public final Provider<Context> contextProvider;
    public final Provider<Handler> handlerProvider;
    public final Provider<KeyguardSecurityContainer> parentProvider;
    public final Provider<KeyguardUpdateMonitor> updateMonitorProvider;

    public AdminSecondaryLockScreenController_Factory_Factory(Provider<Context> provider, Provider<KeyguardSecurityContainer> provider2, Provider<KeyguardUpdateMonitor> provider3, Provider<Handler> provider4) {
        this.contextProvider = provider;
        this.parentProvider = provider2;
        this.updateMonitorProvider = provider3;
        this.handlerProvider = provider4;
    }

    public AdminSecondaryLockScreenController.Factory get() {
        return newInstance(this.contextProvider.get(), this.parentProvider.get(), this.updateMonitorProvider.get(), this.handlerProvider.get());
    }

    public static AdminSecondaryLockScreenController_Factory_Factory create(Provider<Context> provider, Provider<KeyguardSecurityContainer> provider2, Provider<KeyguardUpdateMonitor> provider3, Provider<Handler> provider4) {
        return new AdminSecondaryLockScreenController_Factory_Factory(provider, provider2, provider3, provider4);
    }

    public static AdminSecondaryLockScreenController.Factory newInstance(Context context, KeyguardSecurityContainer keyguardSecurityContainer, KeyguardUpdateMonitor keyguardUpdateMonitor, Handler handler) {
        return new AdminSecondaryLockScreenController.Factory(context, keyguardSecurityContainer, keyguardUpdateMonitor, handler);
    }
}
