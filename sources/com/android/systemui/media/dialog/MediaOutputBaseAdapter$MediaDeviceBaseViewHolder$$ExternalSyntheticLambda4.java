package com.android.systemui.media.dialog;

import android.graphics.drawable.Icon;
import com.android.settingslib.media.MediaDevice;
import com.android.systemui.media.dialog.MediaOutputBaseAdapter;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class MediaOutputBaseAdapter$MediaDeviceBaseViewHolder$$ExternalSyntheticLambda4 implements Runnable {
    public final /* synthetic */ MediaOutputBaseAdapter.MediaDeviceBaseViewHolder f$0;
    public final /* synthetic */ MediaDevice f$1;
    public final /* synthetic */ Icon f$2;

    public /* synthetic */ MediaOutputBaseAdapter$MediaDeviceBaseViewHolder$$ExternalSyntheticLambda4(MediaOutputBaseAdapter.MediaDeviceBaseViewHolder mediaDeviceBaseViewHolder, MediaDevice mediaDevice, Icon icon) {
        this.f$0 = mediaDeviceBaseViewHolder;
        this.f$1 = mediaDevice;
        this.f$2 = icon;
    }

    public final void run() {
        this.f$0.lambda$setUpDeviceIcon$3(this.f$1, this.f$2);
    }
}
