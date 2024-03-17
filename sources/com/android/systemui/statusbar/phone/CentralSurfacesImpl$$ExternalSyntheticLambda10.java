package com.android.systemui.statusbar.phone;

import com.android.systemui.statusbar.phone.FoldStateListener;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class CentralSurfacesImpl$$ExternalSyntheticLambda10 implements FoldStateListener.OnFoldStateChangeListener {
    public final /* synthetic */ CentralSurfacesImpl f$0;

    public /* synthetic */ CentralSurfacesImpl$$ExternalSyntheticLambda10(CentralSurfacesImpl centralSurfacesImpl) {
        this.f$0 = centralSurfacesImpl;
    }

    public final void onFoldStateChanged(boolean z, boolean z2) {
        this.f$0.onFoldedStateChanged(z, z2);
    }
}
