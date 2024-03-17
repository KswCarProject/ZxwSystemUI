package com.android.systemui.settings.brightness;

import android.view.MotionEvent;
import com.android.systemui.settings.brightness.BrightnessSliderView;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class BrightnessSliderController$$ExternalSyntheticLambda0 implements BrightnessSliderView.DispatchTouchEventListener {
    public final /* synthetic */ BrightnessSliderController f$0;

    public /* synthetic */ BrightnessSliderController$$ExternalSyntheticLambda0(BrightnessSliderController brightnessSliderController) {
        this.f$0 = brightnessSliderController;
    }

    public final boolean onDispatchTouchEvent(MotionEvent motionEvent) {
        return this.f$0.mirrorTouchEvent(motionEvent);
    }
}
