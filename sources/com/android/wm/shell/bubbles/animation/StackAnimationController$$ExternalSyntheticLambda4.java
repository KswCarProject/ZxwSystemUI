package com.android.wm.shell.bubbles.animation;

import android.view.View;
import com.android.wm.shell.R;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class StackAnimationController$$ExternalSyntheticLambda4 implements Runnable {
    public final /* synthetic */ View f$0;

    public /* synthetic */ StackAnimationController$$ExternalSyntheticLambda4(View view) {
        this.f$0 = view;
    }

    public final void run() {
        this.f$0.setTag(R.id.reorder_animator_tag, (Object) null);
    }
}
