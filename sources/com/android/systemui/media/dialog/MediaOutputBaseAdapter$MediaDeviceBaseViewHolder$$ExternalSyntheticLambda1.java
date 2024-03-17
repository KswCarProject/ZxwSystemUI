package com.android.systemui.media.dialog;

import com.android.settingslib.media.MediaDevice;
import com.android.systemui.media.dialog.MediaOutputBaseAdapter;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class MediaOutputBaseAdapter$MediaDeviceBaseViewHolder$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ MediaOutputBaseAdapter.MediaDeviceBaseViewHolder f$0;
    public final /* synthetic */ MediaDevice f$1;

    public /* synthetic */ MediaOutputBaseAdapter$MediaDeviceBaseViewHolder$$ExternalSyntheticLambda1(MediaOutputBaseAdapter.MediaDeviceBaseViewHolder mediaDeviceBaseViewHolder, MediaDevice mediaDevice) {
        this.f$0 = mediaDeviceBaseViewHolder;
        this.f$1 = mediaDevice;
    }

    public final void run() {
        this.f$0.lambda$setUpDeviceIcon$4(this.f$1);
    }
}
