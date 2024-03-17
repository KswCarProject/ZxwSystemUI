package com.android.systemui.usb;

import dagger.internal.Factory;
import javax.inject.Provider;

public final class UsbConfirmActivity_Factory implements Factory<UsbConfirmActivity> {
    public final Provider<UsbAudioWarningDialogMessage> usbAudioWarningDialogMessageProvider;

    public UsbConfirmActivity_Factory(Provider<UsbAudioWarningDialogMessage> provider) {
        this.usbAudioWarningDialogMessageProvider = provider;
    }

    public UsbConfirmActivity get() {
        return newInstance(this.usbAudioWarningDialogMessageProvider.get());
    }

    public static UsbConfirmActivity_Factory create(Provider<UsbAudioWarningDialogMessage> provider) {
        return new UsbConfirmActivity_Factory(provider);
    }

    public static UsbConfirmActivity newInstance(UsbAudioWarningDialogMessage usbAudioWarningDialogMessage) {
        return new UsbConfirmActivity(usbAudioWarningDialogMessage);
    }
}
