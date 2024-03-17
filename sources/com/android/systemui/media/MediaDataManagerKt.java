package com.android.systemui.media;

import android.app.PendingIntent;
import android.app.smartspace.SmartspaceAction;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.media.session.MediaSession;
import android.provider.Settings;
import android.service.notification.StatusBarNotification;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.InstanceId;
import com.android.systemui.util.Utils;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;

/* compiled from: MediaDataManager.kt */
public final class MediaDataManagerKt {
    @NotNull
    public static final String[] ART_URIS = {"android.media.metadata.ALBUM_ART_URI", "android.media.metadata.ART_URI", "android.media.metadata.DISPLAY_ICON_URI"};
    @NotNull
    public static final SmartspaceMediaData EMPTY_SMARTSPACE_MEDIA_DATA = new SmartspaceMediaData("INVALID", false, "INVALID", (SmartspaceAction) null, CollectionsKt__CollectionsKt.emptyList(), (Intent) null, 0, InstanceId.fakeInstanceId(-1));
    @NotNull
    public static final MediaData LOADING = new MediaData(-1, false, (String) null, (Icon) null, (CharSequence) null, (CharSequence) null, (Icon) null, CollectionsKt__CollectionsKt.emptyList(), CollectionsKt__CollectionsKt.emptyList(), (MediaButton) null, "INVALID", (MediaSession.Token) null, (PendingIntent) null, (MediaDeviceData) null, true, (Runnable) null, 0, false, (String) null, false, (Boolean) null, false, 0, InstanceId.fakeInstanceId(-1), -1, 8323584, (DefaultConstructorMarker) null);

    @VisibleForTesting
    public static /* synthetic */ void getEMPTY_SMARTSPACE_MEDIA_DATA$annotations() {
    }

    @NotNull
    public static final SmartspaceMediaData getEMPTY_SMARTSPACE_MEDIA_DATA() {
        return EMPTY_SMARTSPACE_MEDIA_DATA;
    }

    public static final boolean isMediaNotification(@NotNull StatusBarNotification statusBarNotification) {
        return statusBarNotification.getNotification().isMediaNotification();
    }

    public static final boolean allowMediaRecommendations(Context context) {
        int i = Settings.Secure.getInt(context.getContentResolver(), "qs_media_recommend", 1);
        if (!Utils.useQsMediaPlayer(context) || i <= 0) {
            return false;
        }
        return true;
    }
}
