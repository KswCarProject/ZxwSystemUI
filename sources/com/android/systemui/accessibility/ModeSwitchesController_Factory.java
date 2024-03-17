package com.android.systemui.accessibility;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ModeSwitchesController_Factory implements Factory<ModeSwitchesController> {
    public final Provider<Context> contextProvider;

    public ModeSwitchesController_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    public ModeSwitchesController get() {
        return newInstance(this.contextProvider.get());
    }

    public static ModeSwitchesController_Factory create(Provider<Context> provider) {
        return new ModeSwitchesController_Factory(provider);
    }

    public static ModeSwitchesController newInstance(Context context) {
        return new ModeSwitchesController(context);
    }
}
