package com.android.systemui.statusbar.tv;

import android.content.Context;
import com.android.systemui.statusbar.policy.SecurityController;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class VpnStatusObserver_Factory implements Factory<VpnStatusObserver> {
    public final Provider<Context> contextProvider;
    public final Provider<SecurityController> securityControllerProvider;

    public VpnStatusObserver_Factory(Provider<Context> provider, Provider<SecurityController> provider2) {
        this.contextProvider = provider;
        this.securityControllerProvider = provider2;
    }

    public VpnStatusObserver get() {
        return newInstance(this.contextProvider.get(), this.securityControllerProvider.get());
    }

    public static VpnStatusObserver_Factory create(Provider<Context> provider, Provider<SecurityController> provider2) {
        return new VpnStatusObserver_Factory(provider, provider2);
    }

    public static VpnStatusObserver newInstance(Context context, SecurityController securityController) {
        return new VpnStatusObserver(context, securityController);
    }
}
