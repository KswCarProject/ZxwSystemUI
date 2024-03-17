package com.android.systemui.people;

import android.app.people.IPeopleManager;
import android.app.people.PeopleSpaceTile;
import java.util.function.Function;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class PeopleSpaceUtils$$ExternalSyntheticLambda7 implements Function {
    public final /* synthetic */ IPeopleManager f$0;

    public /* synthetic */ PeopleSpaceUtils$$ExternalSyntheticLambda7(IPeopleManager iPeopleManager) {
        this.f$0 = iPeopleManager;
    }

    public final Object apply(Object obj) {
        return ((PeopleSpaceTile) obj).toBuilder().setLastInteractionTimestamp(PeopleSpaceUtils.getLastInteraction(this.f$0, (PeopleSpaceTile) obj).longValue()).build();
    }
}
