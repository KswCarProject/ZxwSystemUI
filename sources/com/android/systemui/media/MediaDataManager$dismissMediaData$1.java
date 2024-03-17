package com.android.systemui.media;

import android.media.session.MediaSession;

/* compiled from: MediaDataManager.kt */
public final class MediaDataManager$dismissMediaData$1 implements Runnable {
    public final /* synthetic */ String $key;
    public final /* synthetic */ MediaDataManager this$0;

    public MediaDataManager$dismissMediaData$1(MediaDataManager mediaDataManager, String str) {
        this.this$0 = mediaDataManager;
        this.$key = str;
    }

    public final void run() {
        MediaSession.Token token;
        MediaData mediaData = (MediaData) this.this$0.mediaEntries.get(this.$key);
        if (mediaData != null) {
            MediaDataManager mediaDataManager = this.this$0;
            if (mediaData.isLocalSession() && (token = mediaData.getToken()) != null) {
                mediaDataManager.mediaControllerFactory.create(token).getTransportControls().stop();
            }
        }
    }
}
