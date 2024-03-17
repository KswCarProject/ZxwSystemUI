package com.android.wm.shell.dagger;

import com.android.wm.shell.compatui.CompatUI;
import com.android.wm.shell.compatui.CompatUIController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;

public final class WMShellBaseModule_ProvideCompatUIFactory implements Factory<Optional<CompatUI>> {
    public final Provider<CompatUIController> compatUIControllerProvider;

    public WMShellBaseModule_ProvideCompatUIFactory(Provider<CompatUIController> provider) {
        this.compatUIControllerProvider = provider;
    }

    public Optional<CompatUI> get() {
        return provideCompatUI(this.compatUIControllerProvider.get());
    }

    public static WMShellBaseModule_ProvideCompatUIFactory create(Provider<CompatUIController> provider) {
        return new WMShellBaseModule_ProvideCompatUIFactory(provider);
    }

    public static Optional<CompatUI> provideCompatUI(CompatUIController compatUIController) {
        return (Optional) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideCompatUI(compatUIController));
    }
}
