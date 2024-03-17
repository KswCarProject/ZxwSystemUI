package com.android.systemui.media;

import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class MediaControlPanel$$ExternalSyntheticLambda26 implements Consumer {
    public final /* synthetic */ MediaControlPanel f$0;
    public final /* synthetic */ MediaButton f$1;

    public /* synthetic */ MediaControlPanel$$ExternalSyntheticLambda26(MediaControlPanel mediaControlPanel, MediaButton mediaButton) {
        this.f$0 = mediaControlPanel;
        this.f$1 = mediaButton;
    }

    public final void accept(Object obj) {
        this.f$0.lambda$updateDisplayForScrubbingChange$14(this.f$1, (Integer) obj);
    }
}
