package com.android.systemui.usb;

import dagger.internal.Factory;
import javax.inject.Provider;

public final class UsbPermissionActivity_Factory implements Factory<UsbPermissionActivity> {
    public final Provider<UsbAudioWarningDialogMessage> usbAudioWarningDialogMessageProvider;

    public UsbPermissionActivity_Factory(Provider<UsbAudioWarningDialogMessage> provider) {
        this.usbAudioWarningDialogMessageProvider = provider;
    }

    public UsbPermissionActivity get() {
        return newInstance(this.usbAudioWarningDialogMessageProvider.get());
    }

    public static UsbPermissionActivity_Factory create(Provider<UsbAudioWarningDialogMessage> provider) {
        return new UsbPermissionActivity_Factory(provider);
    }

    public static UsbPermissionActivity newInstance(UsbAudioWarningDialogMessage usbAudioWarningDialogMessage) {
        return new UsbPermissionActivity(usbAudioWarningDialogMessage);
    }
}
