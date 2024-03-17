package com.android.systemui.statusbar.notification.collection.legacy;

import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import java.util.HashMap;
import java.util.function.Function;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class NotificationGroupManagerLegacy$$ExternalSyntheticLambda0 implements Function {
    public final /* synthetic */ HashMap f$0;

    public /* synthetic */ NotificationGroupManagerLegacy$$ExternalSyntheticLambda0(HashMap hashMap) {
        this.f$0 = hashMap;
    }

    public final Object apply(Object obj) {
        return (NotificationGroupManagerLegacy.NotificationGroup) this.f$0.get((String) obj);
    }
}
