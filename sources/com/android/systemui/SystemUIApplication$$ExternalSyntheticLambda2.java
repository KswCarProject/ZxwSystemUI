package com.android.systemui;

import java.util.Map;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SystemUIApplication$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ SystemUIApplication f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ String f$2;
    public final /* synthetic */ Map.Entry f$3;

    public /* synthetic */ SystemUIApplication$$ExternalSyntheticLambda2(SystemUIApplication systemUIApplication, int i, String str, Map.Entry entry) {
        this.f$0 = systemUIApplication;
        this.f$1 = i;
        this.f$2 = str;
        this.f$3 = entry;
    }

    public final void run() {
        this.f$0.lambda$startServicesIfNeeded$0(this.f$1, this.f$2, this.f$3);
    }
}
