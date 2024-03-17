package com.android.systemui.media;

import android.app.PendingIntent;
import android.graphics.drawable.Icon;
import android.media.MediaDescription;
import android.media.session.MediaSession;
import androidx.appcompat.R$styleable;
import com.android.internal.logging.InstanceId;
import java.util.List;
import kotlin.collections.CollectionsKt__CollectionsJVMKt;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* compiled from: MediaDataManager.kt */
public final class MediaDataManager$loadMediaDataInBgForResumption$1 implements Runnable {
    public final /* synthetic */ PendingIntent $appIntent;
    public final /* synthetic */ String $appName;
    public final /* synthetic */ int $appUid;
    public final /* synthetic */ Icon $artworkIcon;
    public final /* synthetic */ MediaDescription $desc;
    public final /* synthetic */ InstanceId $instanceId;
    public final /* synthetic */ long $lastActive;
    public final /* synthetic */ MediaAction $mediaAction;
    public final /* synthetic */ String $packageName;
    public final /* synthetic */ Runnable $resumeAction;
    public final /* synthetic */ MediaSession.Token $token;
    public final /* synthetic */ int $userId;
    public final /* synthetic */ MediaDataManager this$0;

    public MediaDataManager$loadMediaDataInBgForResumption$1(MediaDataManager mediaDataManager, String str, int i, String str2, MediaDescription mediaDescription, Icon icon, MediaAction mediaAction, MediaSession.Token token, PendingIntent pendingIntent, Runnable runnable, long j, InstanceId instanceId, int i2) {
        this.this$0 = mediaDataManager;
        this.$packageName = str;
        this.$userId = i;
        this.$appName = str2;
        this.$desc = mediaDescription;
        this.$artworkIcon = icon;
        this.$mediaAction = mediaAction;
        this.$token = token;
        this.$appIntent = pendingIntent;
        this.$resumeAction = runnable;
        this.$lastActive = j;
        this.$instanceId = instanceId;
        this.$appUid = i2;
    }

    public final void run() {
        MediaDataManager mediaDataManager = this.this$0;
        String str = this.$packageName;
        int i = this.$userId;
        String str2 = this.$appName;
        CharSequence subtitle = this.$desc.getSubtitle();
        CharSequence title = this.$desc.getTitle();
        Icon icon = this.$artworkIcon;
        List listOf = CollectionsKt__CollectionsJVMKt.listOf(this.$mediaAction);
        List listOf2 = CollectionsKt__CollectionsJVMKt.listOf(0);
        MediaButton mediaButton = r14;
        MediaButton mediaButton2 = new MediaButton(this.$mediaAction, (MediaAction) null, (MediaAction) null, (MediaAction) null, (MediaAction) null, false, false, R$styleable.AppCompatTheme_windowNoTitle, (DefaultConstructorMarker) null);
        String str3 = this.$packageName;
        MediaData mediaData = r3;
        MediaData mediaData2 = new MediaData(i, true, str2, (Icon) null, subtitle, title, icon, listOf, listOf2, mediaButton, str3, this.$token, this.$appIntent, (MediaDeviceData) null, false, this.$resumeAction, 0, true, str3, true, (Boolean) null, false, this.$lastActive, this.$instanceId, this.$appUid, 3211264, (DefaultConstructorMarker) null);
        mediaDataManager.onMediaDataLoaded(str, (String) null, mediaData);
    }
}
