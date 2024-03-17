package com.android.systemui.settings.brightness;

import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.settings.brightness.BrightnessSliderController;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class BrightnessSliderController_Factory_Factory implements Factory<BrightnessSliderController.Factory> {
    public final Provider<FalsingManager> falsingManagerProvider;

    public BrightnessSliderController_Factory_Factory(Provider<FalsingManager> provider) {
        this.falsingManagerProvider = provider;
    }

    public BrightnessSliderController.Factory get() {
        return newInstance(this.falsingManagerProvider.get());
    }

    public static BrightnessSliderController_Factory_Factory create(Provider<FalsingManager> provider) {
        return new BrightnessSliderController_Factory_Factory(provider);
    }

    public static BrightnessSliderController.Factory newInstance(FalsingManager falsingManager) {
        return new BrightnessSliderController.Factory(falsingManager);
    }
}
