package com.android.systemui.recents;

import com.android.systemui.recents.OverviewProxyService;
import java.util.function.Supplier;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class OverviewProxyService$1$$ExternalSyntheticLambda24 implements Supplier {
    public final /* synthetic */ OverviewProxyService.AnonymousClass1 f$0;
    public final /* synthetic */ Runnable f$1;

    public /* synthetic */ OverviewProxyService$1$$ExternalSyntheticLambda24(OverviewProxyService.AnonymousClass1 r1, Runnable runnable) {
        this.f$0 = r1;
        this.f$1 = runnable;
    }

    public final Object get() {
        return this.f$0.lambda$verifyCallerAndClearCallingIdentityPostMain$25(this.f$1);
    }
}
