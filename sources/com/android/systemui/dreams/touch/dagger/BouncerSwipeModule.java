package com.android.systemui.dreams.touch.dagger;

import android.content.res.Resources;
import android.util.TypedValue;
import com.android.systemui.R$dimen;
import com.android.systemui.dreams.touch.BouncerSwipeTouchHandler;
import com.android.systemui.dreams.touch.DreamTouchHandler;
import com.android.wm.shell.animation.FlingAnimationUtils;
import javax.inject.Provider;

public class BouncerSwipeModule {
    public static DreamTouchHandler providesBouncerSwipeTouchHandler(BouncerSwipeTouchHandler bouncerSwipeTouchHandler) {
        return bouncerSwipeTouchHandler;
    }

    public static FlingAnimationUtils providesSwipeToBouncerFlingAnimationUtilsClosing(Provider<FlingAnimationUtils.Builder> provider) {
        return provider.get().reset().setMaxLengthSeconds(0.6f).setSpeedUpFactor(0.6f).build();
    }

    public static FlingAnimationUtils providesSwipeToBouncerFlingAnimationUtilsOpening(Provider<FlingAnimationUtils.Builder> provider) {
        return provider.get().reset().setMaxLengthSeconds(0.6f).setSpeedUpFactor(0.6f).build();
    }

    public static float providesSwipeToBouncerStartRegion(Resources resources) {
        TypedValue typedValue = new TypedValue();
        resources.getValue(R$dimen.dream_overlay_bouncer_start_region_screen_percentage, typedValue, true);
        return typedValue.getFloat();
    }

    public static BouncerSwipeTouchHandler.ValueAnimatorCreator providesValueAnimatorCreator() {
        return new BouncerSwipeModule$$ExternalSyntheticLambda1();
    }

    public static BouncerSwipeTouchHandler.VelocityTrackerFactory providesVelocityTrackerFactory() {
        return new BouncerSwipeModule$$ExternalSyntheticLambda0();
    }
}
