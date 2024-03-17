package com.android.systemui.media;

import androidx.dynamicanimation.animation.FloatPropertyCompat;
import org.jetbrains.annotations.NotNull;

/* compiled from: MediaCarouselScrollHandler.kt */
public final class MediaCarouselScrollHandler$Companion$CONTENT_TRANSLATION$1 extends FloatPropertyCompat<MediaCarouselScrollHandler> {
    public MediaCarouselScrollHandler$Companion$CONTENT_TRANSLATION$1() {
        super("contentTranslation");
    }

    public float getValue(@NotNull MediaCarouselScrollHandler mediaCarouselScrollHandler) {
        return mediaCarouselScrollHandler.getContentTranslation();
    }

    public void setValue(@NotNull MediaCarouselScrollHandler mediaCarouselScrollHandler, float f) {
        mediaCarouselScrollHandler.setContentTranslation(f);
    }
}
