package com.android.wm.shell.bubbles;

import com.android.wm.shell.bubbles.Bubbles;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class BubbleController$$ExternalSyntheticLambda2 implements Consumer {
    public final /* synthetic */ Bubbles.SysuiProxy f$0;

    public /* synthetic */ BubbleController$$ExternalSyntheticLambda2(Bubbles.SysuiProxy sysuiProxy) {
        this.f$0 = sysuiProxy;
    }

    public final void accept(Object obj) {
        this.f$0.onUnbubbleConversation((String) obj);
    }
}
