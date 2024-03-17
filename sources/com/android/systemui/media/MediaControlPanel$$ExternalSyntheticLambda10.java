package com.android.systemui.media;

import android.media.session.MediaController;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class MediaControlPanel$$ExternalSyntheticLambda10 implements Runnable {
    public final /* synthetic */ MediaControlPanel f$0;
    public final /* synthetic */ MediaController f$1;

    public /* synthetic */ MediaControlPanel$$ExternalSyntheticLambda10(MediaControlPanel mediaControlPanel, MediaController mediaController) {
        this.f$0 = mediaControlPanel;
        this.f$1 = mediaController;
    }

    public final void run() {
        this.f$0.lambda$bindPlayer$5(this.f$1);
    }
}
