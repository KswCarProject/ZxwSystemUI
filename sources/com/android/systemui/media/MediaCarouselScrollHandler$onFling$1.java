package com.android.systemui.media;

/* compiled from: MediaCarouselScrollHandler.kt */
public final class MediaCarouselScrollHandler$onFling$1 implements Runnable {
    public final /* synthetic */ MediaCarouselScrollHandler this$0;

    public MediaCarouselScrollHandler$onFling$1(MediaCarouselScrollHandler mediaCarouselScrollHandler) {
        this.this$0 = mediaCarouselScrollHandler;
    }

    public final void run() {
        this.this$0.getDismissCallback().invoke();
    }
}
