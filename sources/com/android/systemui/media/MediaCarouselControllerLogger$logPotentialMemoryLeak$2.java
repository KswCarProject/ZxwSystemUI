package com.android.systemui.media;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: MediaCarouselControllerLogger.kt */
public final class MediaCarouselControllerLogger$logPotentialMemoryLeak$2 extends Lambda implements Function1<LogMessage, String> {
    public static final MediaCarouselControllerLogger$logPotentialMemoryLeak$2 INSTANCE = new MediaCarouselControllerLogger$logPotentialMemoryLeak$2();

    public MediaCarouselControllerLogger$logPotentialMemoryLeak$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "Potential memory leak: Removing control panel for " + logMessage.getStr1() + " from map without calling #onDestroy";
    }
}
