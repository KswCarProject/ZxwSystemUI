package com.android.systemui.globalactions;

import com.android.systemui.plugins.GlobalActions;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class GlobalActionsComponent$$ExternalSyntheticLambda1 implements Consumer {
    public final /* synthetic */ GlobalActionsComponent f$0;

    public /* synthetic */ GlobalActionsComponent$$ExternalSyntheticLambda1(GlobalActionsComponent globalActionsComponent) {
        this.f$0 = globalActionsComponent;
    }

    public final void accept(Object obj) {
        this.f$0.onExtensionCallback((GlobalActions) obj);
    }
}
