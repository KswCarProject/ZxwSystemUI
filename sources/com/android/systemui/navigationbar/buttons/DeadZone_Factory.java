package com.android.systemui.navigationbar.buttons;

import com.android.systemui.navigationbar.NavigationBarView;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class DeadZone_Factory implements Factory<DeadZone> {
    public final Provider<NavigationBarView> viewProvider;

    public DeadZone_Factory(Provider<NavigationBarView> provider) {
        this.viewProvider = provider;
    }

    public DeadZone get() {
        return newInstance(this.viewProvider.get());
    }

    public static DeadZone_Factory create(Provider<NavigationBarView> provider) {
        return new DeadZone_Factory(provider);
    }

    public static DeadZone newInstance(NavigationBarView navigationBarView) {
        return new DeadZone(navigationBarView);
    }
}
