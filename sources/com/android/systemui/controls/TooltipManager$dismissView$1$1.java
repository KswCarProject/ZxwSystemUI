package com.android.systemui.controls;

import android.view.View;

/* compiled from: TooltipManager.kt */
public final class TooltipManager$dismissView$1$1 implements View.OnClickListener {
    public final /* synthetic */ TooltipManager this$0;

    public TooltipManager$dismissView$1$1(TooltipManager tooltipManager) {
        this.this$0 = tooltipManager;
    }

    public final void onClick(View view) {
        this.this$0.hide(true);
    }
}
