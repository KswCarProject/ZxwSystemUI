package com.android.systemui.shared.rotation;

import android.view.MotionEvent;
import android.view.View;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class RotationButtonController$$ExternalSyntheticLambda3 implements View.OnHoverListener {
    public final /* synthetic */ RotationButtonController f$0;

    public /* synthetic */ RotationButtonController$$ExternalSyntheticLambda3(RotationButtonController rotationButtonController) {
        this.f$0 = rotationButtonController;
    }

    public final boolean onHover(View view, MotionEvent motionEvent) {
        return this.f$0.onRotateSuggestionHover(view, motionEvent);
    }
}
