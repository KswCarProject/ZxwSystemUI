package com.android.wm.shell.bubbles;

import android.widget.TextView;
import com.android.wm.shell.R;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

/* compiled from: StackEducationView.kt */
public final class StackEducationView$titleTextView$2 extends Lambda implements Function0<TextView> {
    public final /* synthetic */ StackEducationView this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public StackEducationView$titleTextView$2(StackEducationView stackEducationView) {
        super(0);
        this.this$0 = stackEducationView;
    }

    public final TextView invoke() {
        return (TextView) this.this$0.findViewById(R.id.stack_education_title);
    }
}
