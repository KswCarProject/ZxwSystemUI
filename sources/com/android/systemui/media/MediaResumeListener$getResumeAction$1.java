package com.android.systemui.media;

import android.content.ComponentName;
import com.android.systemui.media.ResumeMediaBrowser;

/* compiled from: MediaResumeListener.kt */
public final class MediaResumeListener$getResumeAction$1 implements Runnable {
    public final /* synthetic */ ComponentName $componentName;
    public final /* synthetic */ MediaResumeListener this$0;

    public MediaResumeListener$getResumeAction$1(MediaResumeListener mediaResumeListener, ComponentName componentName) {
        this.this$0 = mediaResumeListener;
        this.$componentName = componentName;
    }

    public final void run() {
        MediaResumeListener mediaResumeListener = this.this$0;
        mediaResumeListener.setMediaBrowser(mediaResumeListener.mediaBrowserFactory.create((ResumeMediaBrowser.Callback) null, this.$componentName));
        ResumeMediaBrowser access$getMediaBrowser$p = this.this$0.mediaBrowser;
        if (access$getMediaBrowser$p != null) {
            access$getMediaBrowser$p.restart();
        }
    }
}
