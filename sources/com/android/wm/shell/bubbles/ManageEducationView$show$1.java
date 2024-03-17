package com.android.wm.shell.bubbles;

import android.graphics.Rect;
import android.view.View;
import android.widget.Button;
import com.android.wm.shell.R;
import com.android.wm.shell.animation.Interpolators;

/* compiled from: ManageEducationView.kt */
public final class ManageEducationView$show$1 implements Runnable {
    public final /* synthetic */ BubbleExpandedView $expandedView;
    public final /* synthetic */ boolean $isRTL;
    public final /* synthetic */ ManageEducationView this$0;

    public ManageEducationView$show$1(ManageEducationView manageEducationView, boolean z, BubbleExpandedView bubbleExpandedView) {
        this.this$0 = manageEducationView;
        this.$isRTL = z;
        this.$expandedView = bubbleExpandedView;
    }

    public final void run() {
        Button access$getManageButton = this.this$0.getManageButton();
        final ManageEducationView manageEducationView = this.this$0;
        final BubbleExpandedView bubbleExpandedView = this.$expandedView;
        access$getManageButton.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                manageEducationView.hide();
                bubbleExpandedView.findViewById(R.id.manage_button).performClick();
            }
        });
        Button access$getGotItButton = this.this$0.getGotItButton();
        final ManageEducationView manageEducationView2 = this.this$0;
        access$getGotItButton.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                manageEducationView2.hide();
            }
        });
        final ManageEducationView manageEducationView3 = this.this$0;
        manageEducationView3.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                manageEducationView3.hide();
            }
        });
        Rect rect = new Rect();
        this.this$0.getManageButton().getDrawingRect(rect);
        this.this$0.getManageView().offsetDescendantRectToMyCoords(this.this$0.getManageButton(), rect);
        if (!this.$isRTL || (!this.this$0.positioner.isLargeScreen() && !this.this$0.positioner.isLandscape())) {
            this.this$0.setTranslationX(0.0f);
        } else {
            ManageEducationView manageEducationView4 = this.this$0;
            manageEducationView4.setTranslationX((float) (manageEducationView4.positioner.getScreenRect().right - this.this$0.getWidth()));
        }
        ManageEducationView manageEducationView5 = this.this$0;
        manageEducationView5.setTranslationY((float) (manageEducationView5.realManageButtonRect.top - rect.top));
        this.this$0.bringToFront();
        this.this$0.animate().setDuration(this.this$0.ANIMATE_DURATION).setInterpolator(Interpolators.FAST_OUT_SLOW_IN).alpha(1.0f);
    }
}
