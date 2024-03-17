package com.android.systemui.media;

import android.media.session.MediaController;

/* compiled from: MediaDataManager.kt */
public final class MediaDataManager$getStandardAction$1 implements Runnable {
    public final /* synthetic */ MediaController $controller;

    public MediaDataManager$getStandardAction$1(MediaController mediaController) {
        this.$controller = mediaController;
    }

    public final void run() {
        this.$controller.getTransportControls().play();
    }
}
