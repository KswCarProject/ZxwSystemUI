package com.android.systemui.statusbar.events;

import android.view.View;
import com.android.systemui.statusbar.events.PrivacyDotViewController;

/* compiled from: PrivacyDotViewController.kt */
public final class PrivacyDotViewController$hideDotView$1 implements Runnable {
    public final /* synthetic */ View $dot;
    public final /* synthetic */ PrivacyDotViewController this$0;

    public PrivacyDotViewController$hideDotView$1(View view, PrivacyDotViewController privacyDotViewController) {
        this.$dot = view;
        this.this$0 = privacyDotViewController;
    }

    public final void run() {
        this.$dot.setVisibility(4);
        PrivacyDotViewController.ShowingListener access$getShowingListener$p = this.this$0.showingListener;
        if (access$getShowingListener$p != null) {
            access$getShowingListener$p.onPrivacyDotHidden(this.$dot);
        }
    }
}
