package com.android.systemui.media;

import android.view.MotionEvent;
import com.android.systemui.Gefingerpoken;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.Nullable;

/* compiled from: MediaCarouselScrollHandler.kt */
public final class MediaCarouselScrollHandler$touchListener$1 implements Gefingerpoken {
    public final /* synthetic */ MediaCarouselScrollHandler this$0;

    public MediaCarouselScrollHandler$touchListener$1(MediaCarouselScrollHandler mediaCarouselScrollHandler) {
        this.this$0 = mediaCarouselScrollHandler;
    }

    public boolean onTouchEvent(@Nullable MotionEvent motionEvent) {
        MediaCarouselScrollHandler mediaCarouselScrollHandler = this.this$0;
        Intrinsics.checkNotNull(motionEvent);
        return mediaCarouselScrollHandler.onTouch(motionEvent);
    }

    public boolean onInterceptTouchEvent(@Nullable MotionEvent motionEvent) {
        MediaCarouselScrollHandler mediaCarouselScrollHandler = this.this$0;
        Intrinsics.checkNotNull(motionEvent);
        return mediaCarouselScrollHandler.onInterceptTouch(motionEvent);
    }
}
