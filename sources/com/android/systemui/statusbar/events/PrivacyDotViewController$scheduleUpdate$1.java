package com.android.systemui.statusbar.events;

/* compiled from: PrivacyDotViewController.kt */
public final class PrivacyDotViewController$scheduleUpdate$1 implements Runnable {
    public final /* synthetic */ PrivacyDotViewController this$0;

    public PrivacyDotViewController$scheduleUpdate$1(PrivacyDotViewController privacyDotViewController) {
        this.this$0 = privacyDotViewController;
    }

    public final void run() {
        this.this$0.processNextViewState();
    }
}
