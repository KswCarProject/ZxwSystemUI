package com.android.systemui.util.animation;

import android.view.ViewTreeObserver;

/* compiled from: TransitionLayout.kt */
public final class TransitionLayout$preDrawApplicator$1 implements ViewTreeObserver.OnPreDrawListener {
    public final /* synthetic */ TransitionLayout this$0;

    public TransitionLayout$preDrawApplicator$1(TransitionLayout transitionLayout) {
        this.this$0 = transitionLayout;
    }

    public boolean onPreDraw() {
        this.this$0.updateScheduled = false;
        this.this$0.getViewTreeObserver().removeOnPreDrawListener(this);
        this.this$0.isPreDrawApplicatorRegistered = false;
        this.this$0.applyCurrentState();
        return true;
    }
}
