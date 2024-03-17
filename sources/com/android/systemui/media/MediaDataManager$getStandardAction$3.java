package com.android.systemui.media;

import android.media.session.MediaController;

/* compiled from: MediaDataManager.kt */
public final class MediaDataManager$getStandardAction$3 implements Runnable {
    public final /* synthetic */ MediaController $controller;

    public MediaDataManager$getStandardAction$3(MediaController mediaController) {
        this.$controller = mediaController;
    }

    public final void run() {
        this.$controller.getTransportControls().skipToPrevious();
    }
}
