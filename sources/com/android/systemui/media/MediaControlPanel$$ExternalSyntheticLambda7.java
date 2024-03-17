package com.android.systemui.media;

import com.android.systemui.media.SeekBarViewModel;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class MediaControlPanel$$ExternalSyntheticLambda7 implements SeekBarViewModel.EnabledChangeListener {
    public final /* synthetic */ MediaControlPanel f$0;

    public /* synthetic */ MediaControlPanel$$ExternalSyntheticLambda7(MediaControlPanel mediaControlPanel) {
        this.f$0 = mediaControlPanel;
    }

    public final void onEnabledChanged(boolean z) {
        this.f$0.setIsSeekBarEnabled(z);
    }
}
