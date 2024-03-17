package com.android.systemui.qs.tiles.dialog;

import android.content.Context;
import com.android.systemui.qs.tiles.dialog.InternetDialogController;
import java.util.Set;
import java.util.function.Function;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class InternetDialogController$$ExternalSyntheticLambda0 implements Function {
    public final /* synthetic */ Set f$0;
    public final /* synthetic */ Context f$1;

    public /* synthetic */ InternetDialogController$$ExternalSyntheticLambda0(Set set, Context context) {
        this.f$0 = set;
        this.f$1 = context;
    }

    public final Object apply(Object obj) {
        return InternetDialogController.lambda$getUniqueSubscriptionDisplayNames$5(this.f$0, this.f$1, (InternetDialogController.AnonymousClass1DisplayInfo) obj);
    }
}
