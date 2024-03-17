package com.android.wm.shell.bubbles;

import android.view.KeyEvent;
import android.view.View;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: StackEducationView.kt */
public final class StackEducationView$onAttachedToWindow$1 implements View.OnKeyListener {
    public final /* synthetic */ StackEducationView this$0;

    public StackEducationView$onAttachedToWindow$1(StackEducationView stackEducationView) {
        this.this$0 = stackEducationView;
    }

    public boolean onKey(@Nullable View view, int i, @NotNull KeyEvent keyEvent) {
        if (keyEvent.getAction() != 1 || i != 4 || this.this$0.isHiding) {
            return false;
        }
        this.this$0.hide(false);
        return true;
    }
}
