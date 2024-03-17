package com.android.wm.shell.dagger;

import android.content.Context;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.hidedisplaycutout.HideDisplayCutoutController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;

public final class WMShellBaseModule_ProvideHideDisplayCutoutControllerFactory implements Factory<Optional<HideDisplayCutoutController>> {
    public final Provider<Context> contextProvider;
    public final Provider<DisplayController> displayControllerProvider;
    public final Provider<ShellExecutor> mainExecutorProvider;

    public WMShellBaseModule_ProvideHideDisplayCutoutControllerFactory(Provider<Context> provider, Provider<DisplayController> provider2, Provider<ShellExecutor> provider3) {
        this.contextProvider = provider;
        this.displayControllerProvider = provider2;
        this.mainExecutorProvider = provider3;
    }

    public Optional<HideDisplayCutoutController> get() {
        return provideHideDisplayCutoutController(this.contextProvider.get(), this.displayControllerProvider.get(), this.mainExecutorProvider.get());
    }

    public static WMShellBaseModule_ProvideHideDisplayCutoutControllerFactory create(Provider<Context> provider, Provider<DisplayController> provider2, Provider<ShellExecutor> provider3) {
        return new WMShellBaseModule_ProvideHideDisplayCutoutControllerFactory(provider, provider2, provider3);
    }

    public static Optional<HideDisplayCutoutController> provideHideDisplayCutoutController(Context context, DisplayController displayController, ShellExecutor shellExecutor) {
        return (Optional) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideHideDisplayCutoutController(context, displayController, shellExecutor));
    }
}
