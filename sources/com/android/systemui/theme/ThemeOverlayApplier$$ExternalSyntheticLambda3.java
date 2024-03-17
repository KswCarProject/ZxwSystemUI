package com.android.systemui.theme;

import android.content.om.OverlayInfo;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ThemeOverlayApplier$$ExternalSyntheticLambda3 implements Predicate {
    public final /* synthetic */ ThemeOverlayApplier f$0;

    public /* synthetic */ ThemeOverlayApplier$$ExternalSyntheticLambda3(ThemeOverlayApplier themeOverlayApplier) {
        this.f$0 = themeOverlayApplier;
    }

    public final boolean test(Object obj) {
        return this.f$0.lambda$applyCurrentUserOverlays$2((OverlayInfo) obj);
    }
}
