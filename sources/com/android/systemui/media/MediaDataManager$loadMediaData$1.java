package com.android.systemui.media;

import android.service.notification.StatusBarNotification;

/* compiled from: MediaDataManager.kt */
public final class MediaDataManager$loadMediaData$1 implements Runnable {
    public final /* synthetic */ String $key;
    public final /* synthetic */ boolean $logEvent;
    public final /* synthetic */ String $oldKey;
    public final /* synthetic */ StatusBarNotification $sbn;
    public final /* synthetic */ MediaDataManager this$0;

    public MediaDataManager$loadMediaData$1(MediaDataManager mediaDataManager, String str, StatusBarNotification statusBarNotification, String str2, boolean z) {
        this.this$0 = mediaDataManager;
        this.$key = str;
        this.$sbn = statusBarNotification;
        this.$oldKey = str2;
        this.$logEvent = z;
    }

    public final void run() {
        this.this$0.loadMediaDataInBg(this.$key, this.$sbn, this.$oldKey, this.$logEvent);
    }
}
