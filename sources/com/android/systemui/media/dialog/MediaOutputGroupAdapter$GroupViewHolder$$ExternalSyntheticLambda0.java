package com.android.systemui.media.dialog;

import android.widget.CompoundButton;
import com.android.settingslib.media.MediaDevice;
import com.android.systemui.media.dialog.MediaOutputGroupAdapter;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class MediaOutputGroupAdapter$GroupViewHolder$$ExternalSyntheticLambda0 implements CompoundButton.OnCheckedChangeListener {
    public final /* synthetic */ MediaOutputGroupAdapter.GroupViewHolder f$0;
    public final /* synthetic */ MediaDevice f$1;

    public /* synthetic */ MediaOutputGroupAdapter$GroupViewHolder$$ExternalSyntheticLambda0(MediaOutputGroupAdapter.GroupViewHolder groupViewHolder, MediaDevice mediaDevice) {
        this.f$0 = groupViewHolder;
        this.f$1 = mediaDevice;
    }

    public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
        this.f$0.lambda$onBind$0(this.f$1, compoundButton, z);
    }
}
