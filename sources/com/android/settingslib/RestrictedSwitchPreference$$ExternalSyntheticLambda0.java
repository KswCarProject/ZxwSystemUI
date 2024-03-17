package com.android.settingslib;

import android.content.Context;
import java.util.function.Supplier;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class RestrictedSwitchPreference$$ExternalSyntheticLambda0 implements Supplier {
    public final /* synthetic */ Context f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ RestrictedSwitchPreference$$ExternalSyntheticLambda0(Context context, int i) {
        this.f$0 = context;
        this.f$1 = i;
    }

    public final Object get() {
        return this.f$0.getString(this.f$1);
    }
}
