package com.android.systemui.media;

import com.android.systemui.media.SeekBarViewModel;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class MediaControlPanel$$ExternalSyntheticLambda6 implements SeekBarViewModel.ScrubbingChangeListener {
    public final /* synthetic */ MediaControlPanel f$0;

    public /* synthetic */ MediaControlPanel$$ExternalSyntheticLambda6(MediaControlPanel mediaControlPanel) {
        this.f$0 = mediaControlPanel;
    }

    public final void onScrubbingChanged(boolean z) {
        this.f$0.setIsScrubbing(z);
    }
}
