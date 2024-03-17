package com.android.systemui.navigationbar;

import com.android.wm.shell.pip.Pip;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class NavigationBar$$ExternalSyntheticLambda9 implements Consumer {
    public final /* synthetic */ NavigationBarView f$0;

    public /* synthetic */ NavigationBar$$ExternalSyntheticLambda9(NavigationBarView navigationBarView) {
        this.f$0 = navigationBarView;
    }

    public final void accept(Object obj) {
        this.f$0.addPipExclusionBoundsChangeListener((Pip) obj);
    }
}
