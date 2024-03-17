package com.android.systemui.statusbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import org.jetbrains.annotations.NotNull;

/* compiled from: PulseExpansionHandler.kt */
public final class PulseExpansionHandler$reset$1 extends AnimatorListenerAdapter {
    public final /* synthetic */ ExpandableView $child;
    public final /* synthetic */ PulseExpansionHandler this$0;

    public PulseExpansionHandler$reset$1(PulseExpansionHandler pulseExpansionHandler, ExpandableView expandableView) {
        this.this$0 = pulseExpansionHandler;
        this.$child = expandableView;
    }

    public void onAnimationEnd(@NotNull Animator animator) {
        this.this$0.setUserLocked(this.$child, false);
    }
}
