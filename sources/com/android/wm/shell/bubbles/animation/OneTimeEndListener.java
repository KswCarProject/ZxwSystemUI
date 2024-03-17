package com.android.wm.shell.bubbles.animation;

import androidx.dynamicanimation.animation.DynamicAnimation;

public class OneTimeEndListener implements DynamicAnimation.OnAnimationEndListener {
    public void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z, float f, float f2) {
        dynamicAnimation.removeEndListener(this);
    }
}
