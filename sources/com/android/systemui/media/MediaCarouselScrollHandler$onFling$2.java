package com.android.systemui.media;

import android.view.View;

/* compiled from: MediaCarouselScrollHandler.kt */
public final class MediaCarouselScrollHandler$onFling$2 implements Runnable {
    public final /* synthetic */ View $view;
    public final /* synthetic */ MediaCarouselScrollHandler this$0;

    public MediaCarouselScrollHandler$onFling$2(MediaCarouselScrollHandler mediaCarouselScrollHandler, View view) {
        this.this$0 = mediaCarouselScrollHandler;
        this.$view = view;
    }

    public final void run() {
        this.this$0.scrollView.smoothScrollTo(this.$view.getLeft(), this.this$0.scrollView.getScrollY());
    }
}
