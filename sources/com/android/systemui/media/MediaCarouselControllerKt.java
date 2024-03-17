package com.android.systemui.media;

import android.content.Intent;
import android.util.Log;

/* compiled from: MediaCarouselController.kt */
public final class MediaCarouselControllerKt {
    public static final boolean DEBUG = Log.isLoggable("MediaCarouselController", 3);
    public static final Intent settingsIntent = new Intent().setAction("android.settings.ACTION_MEDIA_CONTROLS_SETTINGS");
}
