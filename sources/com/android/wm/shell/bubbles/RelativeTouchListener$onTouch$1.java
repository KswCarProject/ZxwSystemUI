package com.android.wm.shell.bubbles;

import android.view.View;

/* compiled from: RelativeTouchListener.kt */
public final class RelativeTouchListener$onTouch$1 implements Runnable {
    public final /* synthetic */ View $v;
    public final /* synthetic */ RelativeTouchListener this$0;

    public RelativeTouchListener$onTouch$1(View view, RelativeTouchListener relativeTouchListener) {
        this.$v = view;
        this.this$0 = relativeTouchListener;
    }

    public final void run() {
        if (this.$v.isLongClickable()) {
            this.this$0.performedLongClick = this.$v.performLongClick();
        }
    }
}
