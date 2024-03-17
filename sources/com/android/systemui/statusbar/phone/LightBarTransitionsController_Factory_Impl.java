package com.android.systemui.statusbar.phone;

import com.android.systemui.statusbar.phone.LightBarTransitionsController;
import dagger.internal.InstanceFactory;
import javax.inject.Provider;

public final class LightBarTransitionsController_Factory_Impl implements LightBarTransitionsController.Factory {
    public final C0006LightBarTransitionsController_Factory delegateFactory;

    public LightBarTransitionsController_Factory_Impl(C0006LightBarTransitionsController_Factory lightBarTransitionsController_Factory) {
        this.delegateFactory = lightBarTransitionsController_Factory;
    }

    public LightBarTransitionsController create(LightBarTransitionsController.DarkIntensityApplier darkIntensityApplier) {
        return this.delegateFactory.get(darkIntensityApplier);
    }

    public static Provider<LightBarTransitionsController.Factory> create(C0006LightBarTransitionsController_Factory lightBarTransitionsController_Factory) {
        return InstanceFactory.create(new LightBarTransitionsController_Factory_Impl(lightBarTransitionsController_Factory));
    }
}
