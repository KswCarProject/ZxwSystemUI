package com.android.systemui.statusbar.notification.collection;

import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.Invalidator;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.Pluggable;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ShadeListBuilder$$ExternalSyntheticLambda5 implements Pluggable.PluggableListener {
    public final /* synthetic */ ShadeListBuilder f$0;

    public /* synthetic */ ShadeListBuilder$$ExternalSyntheticLambda5(ShadeListBuilder shadeListBuilder) {
        this.f$0 = shadeListBuilder;
    }

    public final void onPluggableInvalidated(Object obj) {
        this.f$0.onPreRenderInvalidated((Invalidator) obj);
    }
}
