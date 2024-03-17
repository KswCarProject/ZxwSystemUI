package com.android.systemui.media;

import com.android.internal.logging.InstanceId;
import com.android.internal.logging.InstanceIdSequence;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.R$id;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: MediaUiEventLogger.kt */
public final class MediaUiEventLogger {
    @NotNull
    public final InstanceIdSequence instanceIdSequence = new InstanceIdSequence(1048576);
    @NotNull
    public final UiEventLogger logger;

    public MediaUiEventLogger(@NotNull UiEventLogger uiEventLogger) {
        this.logger = uiEventLogger;
    }

    @NotNull
    public final InstanceId getNewInstanceId() {
        return this.instanceIdSequence.newInstanceId();
    }

    public final void logActiveMediaAdded(int i, @NotNull String str, @NotNull InstanceId instanceId, int i2) {
        MediaUiEvent mediaUiEvent;
        if (i2 == 0) {
            mediaUiEvent = MediaUiEvent.LOCAL_MEDIA_ADDED;
        } else if (i2 == 1) {
            mediaUiEvent = MediaUiEvent.CAST_MEDIA_ADDED;
        } else if (i2 == 2) {
            mediaUiEvent = MediaUiEvent.REMOTE_MEDIA_ADDED;
        } else {
            throw new IllegalArgumentException("Unknown playback location");
        }
        this.logger.logWithInstanceId(mediaUiEvent, i, str, instanceId);
    }

    public final void logPlaybackLocationChange(int i, @NotNull String str, @NotNull InstanceId instanceId, int i2) {
        MediaUiEvent mediaUiEvent;
        if (i2 == 0) {
            mediaUiEvent = MediaUiEvent.TRANSFER_TO_LOCAL;
        } else if (i2 == 1) {
            mediaUiEvent = MediaUiEvent.TRANSFER_TO_CAST;
        } else if (i2 == 2) {
            mediaUiEvent = MediaUiEvent.TRANSFER_TO_REMOTE;
        } else {
            throw new IllegalArgumentException("Unknown playback location");
        }
        this.logger.logWithInstanceId(mediaUiEvent, i, str, instanceId);
    }

    public final void logResumeMediaAdded(int i, @NotNull String str, @NotNull InstanceId instanceId) {
        this.logger.logWithInstanceId(MediaUiEvent.RESUME_MEDIA_ADDED, i, str, instanceId);
    }

    public final void logActiveConvertedToResume(int i, @NotNull String str, @NotNull InstanceId instanceId) {
        this.logger.logWithInstanceId(MediaUiEvent.ACTIVE_TO_RESUME, i, str, instanceId);
    }

    public final void logMediaTimeout(int i, @NotNull String str, @NotNull InstanceId instanceId) {
        this.logger.logWithInstanceId(MediaUiEvent.MEDIA_TIMEOUT, i, str, instanceId);
    }

    public final void logMediaRemoved(int i, @NotNull String str, @NotNull InstanceId instanceId) {
        this.logger.logWithInstanceId(MediaUiEvent.MEDIA_REMOVED, i, str, instanceId);
    }

    public final void logMediaCarouselPage(int i) {
        this.logger.logWithPosition(MediaUiEvent.CAROUSEL_PAGE, 0, (String) null, i);
    }

    public final void logSwipeDismiss() {
        this.logger.log(MediaUiEvent.DISMISS_SWIPE);
    }

    public final void logLongPressOpen(int i, @NotNull String str, @NotNull InstanceId instanceId) {
        this.logger.logWithInstanceId(MediaUiEvent.OPEN_LONG_PRESS, i, str, instanceId);
    }

    public final void logLongPressDismiss(int i, @NotNull String str, @NotNull InstanceId instanceId) {
        this.logger.logWithInstanceId(MediaUiEvent.DISMISS_LONG_PRESS, i, str, instanceId);
    }

    public final void logLongPressSettings(int i, @NotNull String str, @NotNull InstanceId instanceId) {
        this.logger.logWithInstanceId(MediaUiEvent.OPEN_SETTINGS_LONG_PRESS, i, str, instanceId);
    }

    public final void logCarouselSettings() {
        this.logger.log(MediaUiEvent.OPEN_SETTINGS_CAROUSEL);
    }

    public final void logTapAction(int i, int i2, @NotNull String str, @NotNull InstanceId instanceId) {
        MediaUiEvent mediaUiEvent;
        if (i == R$id.actionPlayPause) {
            mediaUiEvent = MediaUiEvent.TAP_ACTION_PLAY_PAUSE;
        } else if (i == R$id.actionPrev) {
            mediaUiEvent = MediaUiEvent.TAP_ACTION_PREV;
        } else if (i == R$id.actionNext) {
            mediaUiEvent = MediaUiEvent.TAP_ACTION_NEXT;
        } else {
            mediaUiEvent = MediaUiEvent.TAP_ACTION_OTHER;
        }
        this.logger.logWithInstanceId(mediaUiEvent, i2, str, instanceId);
    }

    public final void logSeek(int i, @NotNull String str, @NotNull InstanceId instanceId) {
        this.logger.logWithInstanceId(MediaUiEvent.ACTION_SEEK, i, str, instanceId);
    }

    public final void logOpenOutputSwitcher(int i, @NotNull String str, @NotNull InstanceId instanceId) {
        this.logger.logWithInstanceId(MediaUiEvent.OPEN_OUTPUT_SWITCHER, i, str, instanceId);
    }

    public final void logTapContentView(int i, @NotNull String str, @NotNull InstanceId instanceId) {
        this.logger.logWithInstanceId(MediaUiEvent.MEDIA_TAP_CONTENT_VIEW, i, str, instanceId);
    }

    public final void logCarouselPosition(int i) {
        MediaUiEvent mediaUiEvent;
        if (i == 0) {
            mediaUiEvent = MediaUiEvent.MEDIA_CAROUSEL_LOCATION_QS;
        } else if (i == 1) {
            mediaUiEvent = MediaUiEvent.MEDIA_CAROUSEL_LOCATION_QQS;
        } else if (i == 2) {
            mediaUiEvent = MediaUiEvent.MEDIA_CAROUSEL_LOCATION_LOCKSCREEN;
        } else if (i == 3) {
            mediaUiEvent = MediaUiEvent.MEDIA_CAROUSEL_LOCATION_DREAM;
        } else {
            throw new IllegalArgumentException(Intrinsics.stringPlus("Unknown media carousel location ", Integer.valueOf(i)));
        }
        this.logger.log(mediaUiEvent);
    }

    public final void logRecommendationAdded(@NotNull String str, @NotNull InstanceId instanceId) {
        this.logger.logWithInstanceId(MediaUiEvent.MEDIA_RECOMMENDATION_ADDED, 0, str, instanceId);
    }

    public final void logRecommendationRemoved(@NotNull String str, @NotNull InstanceId instanceId) {
        this.logger.logWithInstanceId(MediaUiEvent.MEDIA_RECOMMENDATION_REMOVED, 0, str, instanceId);
    }

    public final void logRecommendationActivated(int i, @NotNull String str, @NotNull InstanceId instanceId) {
        this.logger.logWithInstanceId(MediaUiEvent.MEDIA_RECOMMENDATION_ACTIVATED, i, str, instanceId);
    }

    public final void logRecommendationItemTap(@NotNull String str, @NotNull InstanceId instanceId, int i) {
        this.logger.logWithInstanceIdAndPosition(MediaUiEvent.MEDIA_RECOMMENDATION_ITEM_TAP, 0, str, instanceId, i);
    }

    public final void logRecommendationCardTap(@NotNull String str, @NotNull InstanceId instanceId) {
        this.logger.logWithInstanceId(MediaUiEvent.MEDIA_RECOMMENDATION_CARD_TAP, 0, str, instanceId);
    }
}
