package com.android.systemui.recents;

import android.graphics.Rect;
import java.util.function.BiConsumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class OverviewProxyService$$ExternalSyntheticLambda3 implements BiConsumer {
    public final /* synthetic */ OverviewProxyService f$0;

    public /* synthetic */ OverviewProxyService$$ExternalSyntheticLambda3(OverviewProxyService overviewProxyService) {
        this.f$0 = overviewProxyService;
    }

    public final void accept(Object obj, Object obj2) {
        this.f$0.notifySplitScreenBoundsChanged((Rect) obj, (Rect) obj2);
    }
}
