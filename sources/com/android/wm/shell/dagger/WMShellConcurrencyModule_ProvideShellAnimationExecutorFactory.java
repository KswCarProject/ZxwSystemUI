package com.android.wm.shell.dagger;

import com.android.wm.shell.common.ShellExecutor;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class WMShellConcurrencyModule_ProvideShellAnimationExecutorFactory implements Factory<ShellExecutor> {

    public static final class InstanceHolder {
        public static final WMShellConcurrencyModule_ProvideShellAnimationExecutorFactory INSTANCE = new WMShellConcurrencyModule_ProvideShellAnimationExecutorFactory();
    }

    public ShellExecutor get() {
        return provideShellAnimationExecutor();
    }

    public static WMShellConcurrencyModule_ProvideShellAnimationExecutorFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static ShellExecutor provideShellAnimationExecutor() {
        return (ShellExecutor) Preconditions.checkNotNullFromProvides(WMShellConcurrencyModule.provideShellAnimationExecutor());
    }
}
