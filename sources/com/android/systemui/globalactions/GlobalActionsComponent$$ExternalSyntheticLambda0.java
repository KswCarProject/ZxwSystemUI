package com.android.systemui.globalactions;

import com.android.systemui.plugins.GlobalActions;
import java.util.function.Supplier;
import javax.inject.Provider;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class GlobalActionsComponent$$ExternalSyntheticLambda0 implements Supplier {
    public final /* synthetic */ Provider f$0;

    public /* synthetic */ GlobalActionsComponent$$ExternalSyntheticLambda0(Provider provider) {
        this.f$0 = provider;
    }

    public final Object get() {
        return (GlobalActions) this.f$0.get();
    }
}
