package com.android.wm.shell.dagger;

import android.content.Context;
import com.android.wm.shell.common.DisplayInsetsController;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.fullscreen.FullscreenUnfoldController;
import com.android.wm.shell.unfold.ShellUnfoldProgressProvider;
import com.android.wm.shell.unfold.UnfoldBackgroundController;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;

public final class WMShellModule_ProvideFullscreenUnfoldControllerFactory implements Factory<FullscreenUnfoldController> {
    public final Provider<Context> contextProvider;
    public final Provider<DisplayInsetsController> displayInsetsControllerProvider;
    public final Provider<ShellExecutor> mainExecutorProvider;
    public final Provider<Optional<ShellUnfoldProgressProvider>> progressProvider;
    public final Provider<UnfoldBackgroundController> unfoldBackgroundControllerProvider;

    public WMShellModule_ProvideFullscreenUnfoldControllerFactory(Provider<Context> provider, Provider<Optional<ShellUnfoldProgressProvider>> provider2, Provider<UnfoldBackgroundController> provider3, Provider<DisplayInsetsController> provider4, Provider<ShellExecutor> provider5) {
        this.contextProvider = provider;
        this.progressProvider = provider2;
        this.unfoldBackgroundControllerProvider = provider3;
        this.displayInsetsControllerProvider = provider4;
        this.mainExecutorProvider = provider5;
    }

    public FullscreenUnfoldController get() {
        return provideFullscreenUnfoldController(this.contextProvider.get(), this.progressProvider.get(), DoubleCheck.lazy(this.unfoldBackgroundControllerProvider), this.displayInsetsControllerProvider.get(), this.mainExecutorProvider.get());
    }

    public static WMShellModule_ProvideFullscreenUnfoldControllerFactory create(Provider<Context> provider, Provider<Optional<ShellUnfoldProgressProvider>> provider2, Provider<UnfoldBackgroundController> provider3, Provider<DisplayInsetsController> provider4, Provider<ShellExecutor> provider5) {
        return new WMShellModule_ProvideFullscreenUnfoldControllerFactory(provider, provider2, provider3, provider4, provider5);
    }

    public static FullscreenUnfoldController provideFullscreenUnfoldController(Context context, Optional<ShellUnfoldProgressProvider> optional, Lazy<UnfoldBackgroundController> lazy, DisplayInsetsController displayInsetsController, ShellExecutor shellExecutor) {
        return (FullscreenUnfoldController) Preconditions.checkNotNullFromProvides(WMShellModule.provideFullscreenUnfoldController(context, optional, lazy, displayInsetsController, shellExecutor));
    }
}
