package com.android.systemui.statusbar.policy.dagger;

import com.android.systemui.statusbar.connectivity.NetworkController;
import com.android.systemui.statusbar.policy.DataSaverController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class StatusBarPolicyModule_ProvideDataSaverControllerFactory implements Factory<DataSaverController> {
    public final Provider<NetworkController> networkControllerProvider;

    public StatusBarPolicyModule_ProvideDataSaverControllerFactory(Provider<NetworkController> provider) {
        this.networkControllerProvider = provider;
    }

    public DataSaverController get() {
        return provideDataSaverController(this.networkControllerProvider.get());
    }

    public static StatusBarPolicyModule_ProvideDataSaverControllerFactory create(Provider<NetworkController> provider) {
        return new StatusBarPolicyModule_ProvideDataSaverControllerFactory(provider);
    }

    public static DataSaverController provideDataSaverController(NetworkController networkController) {
        return (DataSaverController) Preconditions.checkNotNullFromProvides(StatusBarPolicyModule.provideDataSaverController(networkController));
    }
}
