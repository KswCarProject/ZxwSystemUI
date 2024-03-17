package com.android.wm.shell.bubbles;

import android.service.notification.NotificationListenerService;
import com.android.wm.shell.bubbles.BubbleController;
import java.util.HashMap;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class BubbleController$BubblesImpl$$ExternalSyntheticLambda12 implements Runnable {
    public final /* synthetic */ BubbleController.BubblesImpl f$0;
    public final /* synthetic */ NotificationListenerService.RankingMap f$1;
    public final /* synthetic */ HashMap f$2;

    public /* synthetic */ BubbleController$BubblesImpl$$ExternalSyntheticLambda12(BubbleController.BubblesImpl bubblesImpl, NotificationListenerService.RankingMap rankingMap, HashMap hashMap) {
        this.f$0 = bubblesImpl;
        this.f$1 = rankingMap;
        this.f$2 = hashMap;
    }

    public final void run() {
        this.f$0.lambda$onRankingUpdated$17(this.f$1, this.f$2);
    }
}
