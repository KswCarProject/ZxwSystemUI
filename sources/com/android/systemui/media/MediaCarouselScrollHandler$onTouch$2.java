package com.android.systemui.media;

/* compiled from: MediaCarouselScrollHandler.kt */
public final class MediaCarouselScrollHandler$onTouch$2 implements Runnable {
    public final /* synthetic */ MediaCarouselScrollHandler this$0;

    public MediaCarouselScrollHandler$onTouch$2(MediaCarouselScrollHandler mediaCarouselScrollHandler) {
        this.this$0 = mediaCarouselScrollHandler;
    }

    public final void run() {
        this.this$0.getDismissCallback().invoke();
    }
}
