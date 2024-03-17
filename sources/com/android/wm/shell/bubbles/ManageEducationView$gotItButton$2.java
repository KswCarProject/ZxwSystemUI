package com.android.wm.shell.bubbles;

import android.widget.Button;
import com.android.wm.shell.R;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

/* compiled from: ManageEducationView.kt */
public final class ManageEducationView$gotItButton$2 extends Lambda implements Function0<Button> {
    public final /* synthetic */ ManageEducationView this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public ManageEducationView$gotItButton$2(ManageEducationView manageEducationView) {
        super(0);
        this.this$0 = manageEducationView;
    }

    public final Button invoke() {
        return (Button) this.this$0.findViewById(R.id.got_it);
    }
}
