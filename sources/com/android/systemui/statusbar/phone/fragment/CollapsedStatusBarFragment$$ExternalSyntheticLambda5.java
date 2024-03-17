package com.android.systemui.statusbar.phone.fragment;

import com.android.systemui.plugins.DarkIconDispatcher;
import java.util.ArrayList;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class CollapsedStatusBarFragment$$ExternalSyntheticLambda5 implements DarkIconDispatcher.DarkReceiver {
    public final /* synthetic */ CollapsedStatusBarFragment f$0;

    public /* synthetic */ CollapsedStatusBarFragment$$ExternalSyntheticLambda5(CollapsedStatusBarFragment collapsedStatusBarFragment) {
        this.f$0 = collapsedStatusBarFragment;
    }

    public final void onDarkChanged(ArrayList arrayList, float f, int i) {
        this.f$0.lambda$onViewCreated$4(arrayList, f, i);
    }
}
