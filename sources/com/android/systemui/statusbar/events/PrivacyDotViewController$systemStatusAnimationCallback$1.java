package com.android.systemui.statusbar.events;

import android.animation.Animator;
import android.graphics.Rect;
import android.view.View;
import com.android.systemui.statusbar.events.SystemStatusAnimationCallback;
import kotlin.Unit;
import org.jetbrains.annotations.Nullable;

/* compiled from: PrivacyDotViewController.kt */
public final class PrivacyDotViewController$systemStatusAnimationCallback$1 implements SystemStatusAnimationCallback {
    public final /* synthetic */ PrivacyDotViewController this$0;

    public PrivacyDotViewController$systemStatusAnimationCallback$1(PrivacyDotViewController privacyDotViewController) {
        this.this$0 = privacyDotViewController;
    }

    @Nullable
    public Animator onSystemEventAnimationBegin() {
        return SystemStatusAnimationCallback.DefaultImpls.onSystemEventAnimationBegin(this);
    }

    @Nullable
    public Animator onSystemEventAnimationFinish(boolean z) {
        return SystemStatusAnimationCallback.DefaultImpls.onSystemEventAnimationFinish(this, z);
    }

    @Nullable
    public Animator onSystemStatusAnimationTransitionToPersistentDot() {
        Object access$getLock$p = this.this$0.lock;
        PrivacyDotViewController privacyDotViewController = this.this$0;
        synchronized (access$getLock$p) {
            privacyDotViewController.setNextViewState(ViewState.copy$default(privacyDotViewController.nextViewState, false, true, false, false, (Rect) null, (Rect) null, (Rect) null, (Rect) null, false, 0, 0, 0, (View) null, 8189, (Object) null));
            Unit unit = Unit.INSTANCE;
        }
        return null;
    }

    @Nullable
    public Animator onHidePersistentDot() {
        Object access$getLock$p = this.this$0.lock;
        PrivacyDotViewController privacyDotViewController = this.this$0;
        synchronized (access$getLock$p) {
            privacyDotViewController.setNextViewState(ViewState.copy$default(privacyDotViewController.nextViewState, false, false, false, false, (Rect) null, (Rect) null, (Rect) null, (Rect) null, false, 0, 0, 0, (View) null, 8189, (Object) null));
            Unit unit = Unit.INSTANCE;
        }
        return null;
    }
}
