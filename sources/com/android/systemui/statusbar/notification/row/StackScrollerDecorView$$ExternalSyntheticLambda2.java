package com.android.systemui.statusbar.notification.row;

import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class StackScrollerDecorView$$ExternalSyntheticLambda2 implements Consumer {
    public final /* synthetic */ StackScrollerDecorView f$0;
    public final /* synthetic */ Consumer f$1;

    public /* synthetic */ StackScrollerDecorView$$ExternalSyntheticLambda2(StackScrollerDecorView stackScrollerDecorView, Consumer consumer) {
        this.f$0 = stackScrollerDecorView;
        this.f$1 = consumer;
    }

    public final void accept(Object obj) {
        this.f$0.lambda$setContentVisible$2(this.f$1, (Boolean) obj);
    }
}
