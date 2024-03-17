package com.android.systemui.sensorprivacy.television;

import com.android.systemui.statusbar.policy.IndividualSensorPrivacyController;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class TvUnblockSensorActivity$$ExternalSyntheticLambda0 implements IndividualSensorPrivacyController.Callback {
    public final /* synthetic */ TvUnblockSensorActivity f$0;

    public /* synthetic */ TvUnblockSensorActivity$$ExternalSyntheticLambda0(TvUnblockSensorActivity tvUnblockSensorActivity) {
        this.f$0 = tvUnblockSensorActivity;
    }

    public final void onSensorBlockedChanged(int i, boolean z) {
        this.f$0.lambda$onCreate$0(i, z);
    }
}
