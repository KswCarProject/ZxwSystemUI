package com.android.systemui.navigationbar;

import com.android.wm.shell.back.BackAnimation;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class NavigationBar$$ExternalSyntheticLambda10 implements Consumer {
    public final /* synthetic */ NavigationBarView f$0;

    public /* synthetic */ NavigationBar$$ExternalSyntheticLambda10(NavigationBarView navigationBarView) {
        this.f$0 = navigationBarView;
    }

    public final void accept(Object obj) {
        this.f$0.registerBackAnimation((BackAnimation) obj);
    }
}
