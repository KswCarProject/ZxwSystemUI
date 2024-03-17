package com.android.systemui.classifier;

import com.android.systemui.plugins.FalsingManager;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class BrightLineFalsingManager$$ExternalSyntheticLambda3 implements Consumer {
    public final /* synthetic */ FalsingManager.ProximityEvent f$0;

    public /* synthetic */ BrightLineFalsingManager$$ExternalSyntheticLambda3(FalsingManager.ProximityEvent proximityEvent) {
        this.f$0 = proximityEvent;
    }

    public final void accept(Object obj) {
        ((FalsingClassifier) obj).onProximityEvent(this.f$0);
    }
}
