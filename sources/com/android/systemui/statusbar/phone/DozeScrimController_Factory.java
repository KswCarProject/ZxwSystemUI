package com.android.systemui.statusbar.phone;

import com.android.systemui.doze.DozeLog;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class DozeScrimController_Factory implements Factory<DozeScrimController> {
    public final Provider<DozeLog> dozeLogProvider;
    public final Provider<DozeParameters> dozeParametersProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;

    public DozeScrimController_Factory(Provider<DozeParameters> provider, Provider<DozeLog> provider2, Provider<StatusBarStateController> provider3) {
        this.dozeParametersProvider = provider;
        this.dozeLogProvider = provider2;
        this.statusBarStateControllerProvider = provider3;
    }

    public DozeScrimController get() {
        return newInstance(this.dozeParametersProvider.get(), this.dozeLogProvider.get(), this.statusBarStateControllerProvider.get());
    }

    public static DozeScrimController_Factory create(Provider<DozeParameters> provider, Provider<DozeLog> provider2, Provider<StatusBarStateController> provider3) {
        return new DozeScrimController_Factory(provider, provider2, provider3);
    }

    public static DozeScrimController newInstance(DozeParameters dozeParameters, DozeLog dozeLog, StatusBarStateController statusBarStateController) {
        return new DozeScrimController(dozeParameters, dozeLog, statusBarStateController);
    }
}
