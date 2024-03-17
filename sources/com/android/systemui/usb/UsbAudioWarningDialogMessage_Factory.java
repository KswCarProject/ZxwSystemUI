package com.android.systemui.usb;

import dagger.internal.Factory;

public final class UsbAudioWarningDialogMessage_Factory implements Factory<UsbAudioWarningDialogMessage> {

    public static final class InstanceHolder {
        public static final UsbAudioWarningDialogMessage_Factory INSTANCE = new UsbAudioWarningDialogMessage_Factory();
    }

    public UsbAudioWarningDialogMessage get() {
        return newInstance();
    }

    public static UsbAudioWarningDialogMessage_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static UsbAudioWarningDialogMessage newInstance() {
        return new UsbAudioWarningDialogMessage();
    }
}
