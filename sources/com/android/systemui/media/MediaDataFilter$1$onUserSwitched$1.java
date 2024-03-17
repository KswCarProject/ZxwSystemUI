package com.android.systemui.media;

/* compiled from: MediaDataFilter.kt */
public final class MediaDataFilter$1$onUserSwitched$1 implements Runnable {
    public final /* synthetic */ int $newUserId;
    public final /* synthetic */ MediaDataFilter this$0;

    public MediaDataFilter$1$onUserSwitched$1(MediaDataFilter mediaDataFilter, int i) {
        this.this$0 = mediaDataFilter;
        this.$newUserId = i;
    }

    public final void run() {
        this.this$0.handleUserSwitched$frameworks__base__packages__SystemUI__android_common__SystemUI_core(this.$newUserId);
    }
}
