package com.android.systemui.navigationbar.buttons;

import com.android.systemui.navigationbar.buttons.ContextualButton;
import com.android.systemui.shared.rotation.RotationButton;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class RotationContextButton$$ExternalSyntheticLambda0 implements ContextualButton.ContextButtonListener {
    public final /* synthetic */ RotationButton.RotationButtonUpdatesCallback f$0;

    public /* synthetic */ RotationContextButton$$ExternalSyntheticLambda0(RotationButton.RotationButtonUpdatesCallback rotationButtonUpdatesCallback) {
        this.f$0 = rotationButtonUpdatesCallback;
    }

    public final void onVisibilityChanged(ContextualButton contextualButton, boolean z) {
        RotationContextButton.lambda$setUpdatesCallback$0(this.f$0, contextualButton, z);
    }
}
