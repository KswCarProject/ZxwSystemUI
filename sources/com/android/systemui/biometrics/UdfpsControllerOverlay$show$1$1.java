package com.android.systemui.biometrics;

import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityManager;

/* compiled from: UdfpsControllerOverlay.kt */
public final class UdfpsControllerOverlay$show$1$1 implements AccessibilityManager.TouchExplorationStateChangeListener {
    public final /* synthetic */ UdfpsView $this_apply;
    public final /* synthetic */ UdfpsControllerOverlay this$0;

    public UdfpsControllerOverlay$show$1$1(UdfpsControllerOverlay udfpsControllerOverlay, UdfpsView udfpsView) {
        this.this$0 = udfpsControllerOverlay;
        this.$this_apply = udfpsView;
    }

    public final void onTouchExplorationStateChanged(boolean z) {
        if (this.this$0.accessibilityManager.isTouchExplorationEnabled()) {
            UdfpsView udfpsView = this.$this_apply;
            final UdfpsControllerOverlay udfpsControllerOverlay = this.this$0;
            udfpsView.setOnHoverListener(new View.OnHoverListener() {
                public final boolean onHover(View view, MotionEvent motionEvent) {
                    return ((Boolean) udfpsControllerOverlay.onTouch.invoke(view, motionEvent, Boolean.TRUE)).booleanValue();
                }
            });
            this.$this_apply.setOnTouchListener((View.OnTouchListener) null);
            this.this$0.touchExplorationEnabled = true;
            return;
        }
        this.$this_apply.setOnHoverListener((View.OnHoverListener) null);
        UdfpsView udfpsView2 = this.$this_apply;
        final UdfpsControllerOverlay udfpsControllerOverlay2 = this.this$0;
        udfpsView2.setOnTouchListener(new View.OnTouchListener() {
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                return ((Boolean) udfpsControllerOverlay2.onTouch.invoke(view, motionEvent, Boolean.TRUE)).booleanValue();
            }
        });
        this.this$0.touchExplorationEnabled = false;
    }
}
