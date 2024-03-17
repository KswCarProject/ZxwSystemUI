package com.android.systemui.statusbar.phone;

import com.android.internal.colorextraction.ColorExtractor;
import com.android.internal.util.function.TriConsumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ScrimController$$ExternalSyntheticLambda0 implements TriConsumer {
    public final /* synthetic */ LightBarController f$0;

    public /* synthetic */ ScrimController$$ExternalSyntheticLambda0(LightBarController lightBarController) {
        this.f$0 = lightBarController;
    }

    public final void accept(Object obj, Object obj2, Object obj3) {
        this.f$0.setScrimState((ScrimState) obj, ((Float) obj2).floatValue(), (ColorExtractor.GradientColors) obj3);
    }
}
