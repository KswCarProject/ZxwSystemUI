package com.android.systemui.wmshell;

import com.android.systemui.wmshell.BubblesManager;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class BubblesManager$5$$ExternalSyntheticLambda7 implements Runnable {
    public final /* synthetic */ BubblesManager.AnonymousClass5 f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ BubblesManager$5$$ExternalSyntheticLambda7(BubblesManager.AnonymousClass5 r1, String str, int i) {
        this.f$0 = r1;
        this.f$1 = str;
        this.f$2 = i;
    }

    public final void run() {
        this.f$0.lambda$notifyRemoveNotification$5(this.f$1, this.f$2);
    }
}
