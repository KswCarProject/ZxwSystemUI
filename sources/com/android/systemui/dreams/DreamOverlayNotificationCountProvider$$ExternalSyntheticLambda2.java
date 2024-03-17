package com.android.systemui.dreams;

import com.android.systemui.dreams.DreamOverlayNotificationCountProvider;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class DreamOverlayNotificationCountProvider$$ExternalSyntheticLambda2 implements Consumer {
    public final /* synthetic */ int f$0;

    public /* synthetic */ DreamOverlayNotificationCountProvider$$ExternalSyntheticLambda2(int i) {
        this.f$0 = i;
    }

    public final void accept(Object obj) {
        ((DreamOverlayNotificationCountProvider.Callback) obj).onNotificationCountChanged(this.f$0);
    }
}
