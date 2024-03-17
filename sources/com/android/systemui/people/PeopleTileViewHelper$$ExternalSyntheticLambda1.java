package com.android.systemui.people;

import android.app.people.ConversationStatus;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class PeopleTileViewHelper$$ExternalSyntheticLambda1 implements Predicate {
    public final /* synthetic */ PeopleTileViewHelper f$0;

    public /* synthetic */ PeopleTileViewHelper$$ExternalSyntheticLambda1(PeopleTileViewHelper peopleTileViewHelper) {
        this.f$0 = peopleTileViewHelper;
    }

    public final boolean test(Object obj) {
        return this.f$0.lambda$getViewForTile$1((ConversationStatus) obj);
    }
}
