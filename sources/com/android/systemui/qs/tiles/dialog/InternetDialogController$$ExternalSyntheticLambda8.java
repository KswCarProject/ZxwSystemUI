package com.android.systemui.qs.tiles.dialog;

import com.android.systemui.qs.tiles.dialog.InternetDialogController;
import java.util.Set;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class InternetDialogController$$ExternalSyntheticLambda8 implements Predicate {
    public final /* synthetic */ Set f$0;

    public /* synthetic */ InternetDialogController$$ExternalSyntheticLambda8(Set set) {
        this.f$0 = set;
    }

    public final boolean test(Object obj) {
        return InternetDialogController.lambda$getUniqueSubscriptionDisplayNames$7(this.f$0, (InternetDialogController.AnonymousClass1DisplayInfo) obj);
    }
}