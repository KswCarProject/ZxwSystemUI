package androidx.leanback.widget;

import java.util.ArrayList;
import java.util.List;

public abstract class ParallaxEffect {
    public final List<Object> mMarkerValues = new ArrayList(2);
    public final List<ParallaxTarget> mTargets = new ArrayList(4);
    public final List<Float> mTotalWeights = new ArrayList(2);
    public final List<Float> mWeights = new ArrayList(2);

    public abstract Number calculateDirectValue(Parallax parallax);

    public abstract float calculateFraction(Parallax parallax);

    public final void performMapping(Parallax parallax) {
        if (this.mMarkerValues.size() >= 2) {
            parallax.verifyFloatProperties();
            float f = 0.0f;
            Number number = null;
            boolean z = false;
            for (int i = 0; i < this.mTargets.size(); i++) {
                ParallaxTarget parallaxTarget = this.mTargets.get(i);
                if (parallaxTarget.isDirectMapping()) {
                    if (number == null) {
                        number = calculateDirectValue(parallax);
                    }
                    parallaxTarget.directUpdate(number);
                } else {
                    if (!z) {
                        f = calculateFraction(parallax);
                        z = true;
                    }
                    parallaxTarget.update(f);
                }
            }
        }
    }
}
