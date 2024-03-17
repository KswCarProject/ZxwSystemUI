package com.android.systemui.statusbar.events;

import android.graphics.Rect;
import com.android.systemui.statusbar.events.PrivacyDotViewController;
import kotlin.Unit;

/* compiled from: PrivacyDotViewController.kt */
public final class PrivacyDotViewController$2$onLayoutDirectionChanged$1 implements Runnable {
    public final /* synthetic */ boolean $isRtl;
    public final /* synthetic */ PrivacyDotViewController this$0;
    public final /* synthetic */ PrivacyDotViewController.AnonymousClass2 this$1;

    public PrivacyDotViewController$2$onLayoutDirectionChanged$1(PrivacyDotViewController privacyDotViewController, PrivacyDotViewController.AnonymousClass2 r2, boolean z) {
        this.this$0 = privacyDotViewController;
        this.this$1 = r2;
        this.$isRtl = z;
    }

    public final void run() {
        this.this$0.setCornerVisibilities(4);
        PrivacyDotViewController.AnonymousClass2 r1 = this.this$1;
        PrivacyDotViewController privacyDotViewController = this.this$0;
        boolean z = this.$isRtl;
        synchronized (r1) {
            privacyDotViewController.setNextViewState(ViewState.copy$default(privacyDotViewController.nextViewState, false, false, false, false, (Rect) null, (Rect) null, (Rect) null, (Rect) null, z, 0, 0, 0, privacyDotViewController.selectDesignatedCorner(privacyDotViewController.nextViewState.getRotation(), z), 3839, (Object) null));
            Unit unit = Unit.INSTANCE;
        }
    }
}
