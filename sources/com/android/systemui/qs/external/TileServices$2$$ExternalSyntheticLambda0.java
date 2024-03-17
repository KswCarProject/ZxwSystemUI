package com.android.systemui.qs.external;

import android.content.ComponentName;
import com.android.systemui.qs.external.TileServices;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class TileServices$2$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ TileServices.AnonymousClass2 f$0;
    public final /* synthetic */ ComponentName f$1;

    public /* synthetic */ TileServices$2$$ExternalSyntheticLambda0(TileServices.AnonymousClass2 r1, ComponentName componentName) {
        this.f$0 = r1;
        this.f$1 = componentName;
    }

    public final void run() {
        this.f$0.lambda$requestTileServiceListeningState$0(this.f$1);
    }
}
