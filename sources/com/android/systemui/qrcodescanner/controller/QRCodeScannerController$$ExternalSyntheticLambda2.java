package com.android.systemui.qrcodescanner.controller;

import android.provider.DeviceConfig;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class QRCodeScannerController$$ExternalSyntheticLambda2 implements DeviceConfig.OnPropertiesChangedListener {
    public final /* synthetic */ QRCodeScannerController f$0;

    public /* synthetic */ QRCodeScannerController$$ExternalSyntheticLambda2(QRCodeScannerController qRCodeScannerController) {
        this.f$0 = qRCodeScannerController;
    }

    public final void onPropertiesChanged(DeviceConfig.Properties properties) {
        this.f$0.lambda$registerDefaultQRCodeScannerObserver$4(properties);
    }
}
