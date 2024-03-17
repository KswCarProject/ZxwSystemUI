package com.android.systemui.hdmi;

import dagger.internal.Factory;
import javax.inject.Provider;

public final class HdmiCecSetMenuLanguageActivity_Factory implements Factory<HdmiCecSetMenuLanguageActivity> {
    public final Provider<HdmiCecSetMenuLanguageHelper> hdmiCecSetMenuLanguageHelperProvider;

    public HdmiCecSetMenuLanguageActivity_Factory(Provider<HdmiCecSetMenuLanguageHelper> provider) {
        this.hdmiCecSetMenuLanguageHelperProvider = provider;
    }

    public HdmiCecSetMenuLanguageActivity get() {
        return newInstance(this.hdmiCecSetMenuLanguageHelperProvider.get());
    }

    public static HdmiCecSetMenuLanguageActivity_Factory create(Provider<HdmiCecSetMenuLanguageHelper> provider) {
        return new HdmiCecSetMenuLanguageActivity_Factory(provider);
    }

    public static HdmiCecSetMenuLanguageActivity newInstance(HdmiCecSetMenuLanguageHelper hdmiCecSetMenuLanguageHelper) {
        return new HdmiCecSetMenuLanguageActivity(hdmiCecSetMenuLanguageHelper);
    }
}
