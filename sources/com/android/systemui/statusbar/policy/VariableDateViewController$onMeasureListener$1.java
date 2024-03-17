package com.android.systemui.statusbar.policy;

import com.android.systemui.statusbar.policy.VariableDateView;

/* compiled from: VariableDateViewController.kt */
public final class VariableDateViewController$onMeasureListener$1 implements VariableDateView.OnMeasureListener {
    public final /* synthetic */ VariableDateViewController this$0;

    public VariableDateViewController$onMeasureListener$1(VariableDateViewController variableDateViewController) {
        this.this$0 = variableDateViewController;
    }

    public void onMeasureAction(int i) {
        if (i != this.this$0.lastWidth) {
            this.this$0.maybeChangeFormat(i);
            this.this$0.lastWidth = i;
        }
    }
}
