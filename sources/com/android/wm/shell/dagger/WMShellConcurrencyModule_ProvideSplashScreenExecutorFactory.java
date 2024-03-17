package com.android.wm.shell.dagger;

import com.android.wm.shell.common.ShellExecutor;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class WMShellConcurrencyModule_ProvideSplashScreenExecutorFactory implements Factory<ShellExecutor> {

    public static final class InstanceHolder {
        public static final WMShellConcurrencyModule_ProvideSplashScreenExecutorFactory INSTANCE = new WMShellConcurrencyModule_ProvideSplashScreenExecutorFactory();
    }

    public ShellExecutor get() {
        return provideSplashScreenExecutor();
    }

    public static WMShellConcurrencyModule_ProvideSplashScreenExecutorFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static ShellExecutor provideSplashScreenExecutor() {
        return (ShellExecutor) Preconditions.checkNotNullFromProvides(WMShellConcurrencyModule.provideSplashScreenExecutor());
    }
}
