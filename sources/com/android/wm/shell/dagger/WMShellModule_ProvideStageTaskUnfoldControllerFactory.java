package com.android.wm.shell.dagger;

import android.content.Context;
import com.android.wm.shell.common.DisplayInsetsController;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.TransactionPool;
import com.android.wm.shell.splitscreen.StageTaskUnfoldController;
import com.android.wm.shell.unfold.ShellUnfoldProgressProvider;
import com.android.wm.shell.unfold.UnfoldBackgroundController;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;

public final class WMShellModule_ProvideStageTaskUnfoldControllerFactory implements Factory<Optional<StageTaskUnfoldController>> {
    public final Provider<Context> contextProvider;
    public final Provider<DisplayInsetsController> displayInsetsControllerProvider;
    public final Provider<ShellExecutor> mainExecutorProvider;
    public final Provider<Optional<ShellUnfoldProgressProvider>> progressProvider;
    public final Provider<TransactionPool> transactionPoolProvider;
    public final Provider<UnfoldBackgroundController> unfoldBackgroundControllerProvider;

    public WMShellModule_ProvideStageTaskUnfoldControllerFactory(Provider<Optional<ShellUnfoldProgressProvider>> provider, Provider<Context> provider2, Provider<TransactionPool> provider3, Provider<UnfoldBackgroundController> provider4, Provider<DisplayInsetsController> provider5, Provider<ShellExecutor> provider6) {
        this.progressProvider = provider;
        this.contextProvider = provider2;
        this.transactionPoolProvider = provider3;
        this.unfoldBackgroundControllerProvider = provider4;
        this.displayInsetsControllerProvider = provider5;
        this.mainExecutorProvider = provider6;
    }

    public Optional<StageTaskUnfoldController> get() {
        return provideStageTaskUnfoldController(this.progressProvider.get(), this.contextProvider.get(), this.transactionPoolProvider.get(), DoubleCheck.lazy(this.unfoldBackgroundControllerProvider), this.displayInsetsControllerProvider.get(), this.mainExecutorProvider.get());
    }

    public static WMShellModule_ProvideStageTaskUnfoldControllerFactory create(Provider<Optional<ShellUnfoldProgressProvider>> provider, Provider<Context> provider2, Provider<TransactionPool> provider3, Provider<UnfoldBackgroundController> provider4, Provider<DisplayInsetsController> provider5, Provider<ShellExecutor> provider6) {
        return new WMShellModule_ProvideStageTaskUnfoldControllerFactory(provider, provider2, provider3, provider4, provider5, provider6);
    }

    /* JADX WARNING: type inference failed for: r0v0, types: [java.util.Optional, java.util.Optional<com.android.wm.shell.unfold.ShellUnfoldProgressProvider>] */
    /* JADX WARNING: type inference failed for: r3v0, types: [dagger.Lazy<com.android.wm.shell.unfold.UnfoldBackgroundController>, dagger.Lazy] */
    /* JADX WARNING: Unknown variable types count: 2 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.Optional<com.android.wm.shell.splitscreen.StageTaskUnfoldController> provideStageTaskUnfoldController(java.util.Optional<com.android.wm.shell.unfold.ShellUnfoldProgressProvider> r0, android.content.Context r1, com.android.wm.shell.common.TransactionPool r2, dagger.Lazy<com.android.wm.shell.unfold.UnfoldBackgroundController> r3, com.android.wm.shell.common.DisplayInsetsController r4, com.android.wm.shell.common.ShellExecutor r5) {
        /*
            java.util.Optional r0 = com.android.wm.shell.dagger.WMShellModule.provideStageTaskUnfoldController(r0, r1, r2, r3, r4, r5)
            java.lang.Object r0 = dagger.internal.Preconditions.checkNotNullFromProvides(r0)
            java.util.Optional r0 = (java.util.Optional) r0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.dagger.WMShellModule_ProvideStageTaskUnfoldControllerFactory.provideStageTaskUnfoldController(java.util.Optional, android.content.Context, com.android.wm.shell.common.TransactionPool, dagger.Lazy, com.android.wm.shell.common.DisplayInsetsController, com.android.wm.shell.common.ShellExecutor):java.util.Optional");
    }
}
