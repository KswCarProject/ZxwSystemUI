package com.android.systemui.people;

import android.app.people.PeopleSpaceTile;
import android.content.pm.LauncherApps;
import android.content.pm.ShortcutInfo;
import java.util.function.Function;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class PeopleSpaceUtils$$ExternalSyntheticLambda5 implements Function {
    public final /* synthetic */ LauncherApps f$0;

    public /* synthetic */ PeopleSpaceUtils$$ExternalSyntheticLambda5(LauncherApps launcherApps) {
        this.f$0 = launcherApps;
    }

    public final Object apply(Object obj) {
        return new PeopleSpaceTile.Builder((ShortcutInfo) obj, this.f$0).build();
    }
}
