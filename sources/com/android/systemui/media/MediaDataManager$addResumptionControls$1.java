package com.android.systemui.media;

import android.app.PendingIntent;
import android.media.MediaDescription;
import android.media.session.MediaSession;

/* compiled from: MediaDataManager.kt */
public final class MediaDataManager$addResumptionControls$1 implements Runnable {
    public final /* synthetic */ Runnable $action;
    public final /* synthetic */ PendingIntent $appIntent;
    public final /* synthetic */ String $appName;
    public final /* synthetic */ MediaDescription $desc;
    public final /* synthetic */ String $packageName;
    public final /* synthetic */ MediaSession.Token $token;
    public final /* synthetic */ int $userId;
    public final /* synthetic */ MediaDataManager this$0;

    public MediaDataManager$addResumptionControls$1(MediaDataManager mediaDataManager, int i, MediaDescription mediaDescription, Runnable runnable, MediaSession.Token token, String str, PendingIntent pendingIntent, String str2) {
        this.this$0 = mediaDataManager;
        this.$userId = i;
        this.$desc = mediaDescription;
        this.$action = runnable;
        this.$token = token;
        this.$appName = str;
        this.$appIntent = pendingIntent;
        this.$packageName = str2;
    }

    public final void run() {
        this.this$0.loadMediaDataInBgForResumption(this.$userId, this.$desc, this.$action, this.$token, this.$appName, this.$appIntent, this.$packageName);
    }
}
