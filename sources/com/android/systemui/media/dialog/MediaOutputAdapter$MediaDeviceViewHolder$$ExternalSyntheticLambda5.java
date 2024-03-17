package com.android.systemui.media.dialog;

import android.widget.CompoundButton;
import com.android.settingslib.media.MediaDevice;
import com.android.systemui.media.dialog.MediaOutputAdapter;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class MediaOutputAdapter$MediaDeviceViewHolder$$ExternalSyntheticLambda5 implements CompoundButton.OnCheckedChangeListener {
    public final /* synthetic */ MediaOutputAdapter.MediaDeviceViewHolder f$0;
    public final /* synthetic */ MediaDevice f$1;

    public /* synthetic */ MediaOutputAdapter$MediaDeviceViewHolder$$ExternalSyntheticLambda5(MediaOutputAdapter.MediaDeviceViewHolder mediaDeviceViewHolder, MediaDevice mediaDevice) {
        this.f$0 = mediaDeviceViewHolder;
        this.f$1 = mediaDevice;
    }

    public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
        this.f$0.lambda$onBind$5(this.f$1, compoundButton, z);
    }
}
