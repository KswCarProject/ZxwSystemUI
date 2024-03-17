package com.android.settingslib.wifi;

import com.android.settingslib.wifi.AccessPoint;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class AccessPoint$AccessPointProvisioningCallback$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ AccessPoint.AccessPointProvisioningCallback f$0;

    public /* synthetic */ AccessPoint$AccessPointProvisioningCallback$$ExternalSyntheticLambda2(AccessPoint.AccessPointProvisioningCallback accessPointProvisioningCallback) {
        this.f$0 = accessPointProvisioningCallback;
    }

    public final void run() {
        this.f$0.lambda$onProvisioningFailure$0();
    }
}
