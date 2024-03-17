package com.android.systemui.media.dialog;

import android.view.View;
import com.android.settingslib.media.MediaDevice;
import com.android.systemui.media.dialog.MediaOutputAdapter;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class MediaOutputAdapter$MediaDeviceViewHolder$$ExternalSyntheticLambda0 implements View.OnClickListener {
    public final /* synthetic */ MediaOutputAdapter.MediaDeviceViewHolder f$0;
    public final /* synthetic */ MediaDevice f$1;

    public /* synthetic */ MediaOutputAdapter$MediaDeviceViewHolder$$ExternalSyntheticLambda0(MediaOutputAdapter.MediaDeviceViewHolder mediaDeviceViewHolder, MediaDevice mediaDevice) {
        this.f$0 = mediaDeviceViewHolder;
        this.f$1 = mediaDevice;
    }

    public final void onClick(View view) {
        this.f$0.lambda$onBind$0(this.f$1, view);
    }
}
