package com.android.systemui.statusbar.phone;

import android.view.IRemoteAnimationRunner;
import com.android.systemui.statusbar.phone.CentralSurfacesImpl;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class CentralSurfacesImpl$25$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ CentralSurfacesImpl.AnonymousClass25 f$0;
    public final /* synthetic */ IRemoteAnimationRunner f$1;

    public /* synthetic */ CentralSurfacesImpl$25$$ExternalSyntheticLambda0(CentralSurfacesImpl.AnonymousClass25 r1, IRemoteAnimationRunner iRemoteAnimationRunner) {
        this.f$0 = r1;
        this.f$1 = iRemoteAnimationRunner;
    }

    public final void run() {
        this.f$0.lambda$hideKeyguardWithAnimation$0(this.f$1);
    }
}
