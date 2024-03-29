package com.airbnb.lottie.model.animatable;

import android.graphics.PointF;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.PathKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.PointKeyframeAnimation;
import com.airbnb.lottie.value.Keyframe;
import java.util.List;

public class AnimatablePathValue implements AnimatableValue<PointF, PointF> {
    public final List<Keyframe<PointF>> keyframes;

    public AnimatablePathValue(List<Keyframe<PointF>> list) {
        this.keyframes = list;
    }

    public List<Keyframe<PointF>> getKeyframes() {
        return this.keyframes;
    }

    public boolean isStatic() {
        return this.keyframes.size() == 1 && this.keyframes.get(0).isStatic();
    }

    public BaseKeyframeAnimation<PointF, PointF> createAnimation() {
        if (this.keyframes.get(0).isStatic()) {
            return new PointKeyframeAnimation(this.keyframes);
        }
        return new PathKeyframeAnimation(this.keyframes);
    }
}
