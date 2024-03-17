package com.android.systemui.media.dialog;

import android.view.View;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class MediaOutputAdapter$MediaDeviceViewHolder$$ExternalSyntheticLambda8 implements View.OnClickListener {
    public final /* synthetic */ MediaOutputController f$0;

    public /* synthetic */ MediaOutputAdapter$MediaDeviceViewHolder$$ExternalSyntheticLambda8(MediaOutputController mediaOutputController) {
        this.f$0 = mediaOutputController;
    }

    public final void onClick(View view) {
        this.f$0.launchBluetoothPairing(view);
    }
}
