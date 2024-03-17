package com.android.systemui.navigationbar;

import com.android.systemui.statusbar.phone.CentralSurfaces;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class NavigationBar$$ExternalSyntheticLambda27 implements Consumer {
    public final /* synthetic */ boolean f$0;

    public /* synthetic */ NavigationBar$$ExternalSyntheticLambda27(boolean z) {
        this.f$0 = z;
    }

    public final void accept(Object obj) {
        ((CentralSurfaces) obj).setQsScrimEnabled(!this.f$0);
    }
}
