package com.android.systemui.media;

/* compiled from: MediaDataManager.kt */
public final class MediaDataManager$dismissSmartspaceRecommendation$1 implements Runnable {
    public final /* synthetic */ MediaDataManager this$0;

    public MediaDataManager$dismissSmartspaceRecommendation$1(MediaDataManager mediaDataManager) {
        this.this$0 = mediaDataManager;
    }

    public final void run() {
        MediaDataManager mediaDataManager = this.this$0;
        mediaDataManager.notifySmartspaceMediaDataRemoved(mediaDataManager.getSmartspaceMediaData().getTargetId(), true);
    }
}
