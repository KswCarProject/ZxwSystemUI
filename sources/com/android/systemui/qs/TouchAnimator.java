package com.android.systemui.qs;

import android.util.FloatProperty;
import android.util.MathUtils;
import android.util.Property;
import android.view.View;
import android.view.animation.Interpolator;
import androidx.appcompat.R$styleable;
import java.util.ArrayList;
import java.util.List;

public class TouchAnimator {
    public static final FloatProperty<TouchAnimator> POSITION = new FloatProperty<TouchAnimator>("position") {
        public void setValue(TouchAnimator touchAnimator, float f) {
            touchAnimator.setPosition(f);
        }

        public Float get(TouchAnimator touchAnimator) {
            return Float.valueOf(touchAnimator.mLastT);
        }
    };
    public final float mEndDelay;
    public final Interpolator mInterpolator;
    public final KeyframeSet[] mKeyframeSets;
    public float mLastT;
    public final Listener mListener;
    public final float mSpan;
    public final float mStartDelay;
    public final Object[] mTargets;

    public interface Listener {
        void onAnimationAtEnd();

        void onAnimationAtStart();

        void onAnimationStarted();
    }

    public static class ListenerAdapter implements Listener {
        public void onAnimationAtEnd() {
        }

        public void onAnimationAtStart() {
        }
    }

    public TouchAnimator(Object[] objArr, KeyframeSet[] keyframeSetArr, float f, float f2, Interpolator interpolator, Listener listener) {
        this.mLastT = -1.0f;
        this.mTargets = objArr;
        this.mKeyframeSets = keyframeSetArr;
        this.mStartDelay = f;
        this.mEndDelay = f2;
        this.mSpan = (1.0f - f2) - f;
        this.mInterpolator = interpolator;
        this.mListener = listener;
    }

    public void setPosition(float f) {
        if (!Float.isNaN(f)) {
            float constrain = MathUtils.constrain((f - this.mStartDelay) / this.mSpan, 0.0f, 1.0f);
            Interpolator interpolator = this.mInterpolator;
            if (interpolator != null) {
                constrain = interpolator.getInterpolation(constrain);
            }
            float f2 = this.mLastT;
            if (constrain != f2) {
                Listener listener = this.mListener;
                if (listener != null) {
                    if (constrain == 1.0f) {
                        listener.onAnimationAtEnd();
                    } else if (constrain == 0.0f) {
                        listener.onAnimationAtStart();
                    } else if (f2 <= 0.0f || f2 == 1.0f) {
                        listener.onAnimationStarted();
                    }
                    this.mLastT = constrain;
                }
                int i = 0;
                while (true) {
                    Object[] objArr = this.mTargets;
                    if (i < objArr.length) {
                        this.mKeyframeSets[i].setValue(constrain, objArr[i]);
                        i++;
                    } else {
                        return;
                    }
                }
            }
        }
    }

    public static class Builder {
        public float mEndDelay;
        public Interpolator mInterpolator;
        public Listener mListener;
        public float mStartDelay;
        public List<Object> mTargets = new ArrayList();
        public List<KeyframeSet> mValues = new ArrayList();

        public Builder addFloat(Object obj, String str, float... fArr) {
            add(obj, KeyframeSet.ofFloat(getProperty(obj, str, Float.TYPE), fArr));
            return this;
        }

        public final void add(Object obj, KeyframeSet keyframeSet) {
            this.mTargets.add(obj);
            this.mValues.add(keyframeSet);
        }

        public static Property getProperty(Object obj, String str, Class<?> cls) {
            if (obj instanceof View) {
                str.hashCode();
                char c = 65535;
                switch (str.hashCode()) {
                    case -1225497657:
                        if (str.equals("translationX")) {
                            c = 0;
                            break;
                        }
                        break;
                    case -1225497656:
                        if (str.equals("translationY")) {
                            c = 1;
                            break;
                        }
                        break;
                    case -1225497655:
                        if (str.equals("translationZ")) {
                            c = 2;
                            break;
                        }
                        break;
                    case -908189618:
                        if (str.equals("scaleX")) {
                            c = 3;
                            break;
                        }
                        break;
                    case -908189617:
                        if (str.equals("scaleY")) {
                            c = 4;
                            break;
                        }
                        break;
                    case -40300674:
                        if (str.equals("rotation")) {
                            c = 5;
                            break;
                        }
                        break;
                    case R$styleable.AppCompatTheme_windowFixedHeightMajor:
                        if (str.equals("x")) {
                            c = 6;
                            break;
                        }
                        break;
                    case R$styleable.AppCompatTheme_windowFixedHeightMinor:
                        if (str.equals("y")) {
                            c = 7;
                            break;
                        }
                        break;
                    case 92909918:
                        if (str.equals("alpha")) {
                            c = 8;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                        return View.TRANSLATION_X;
                    case 1:
                        return View.TRANSLATION_Y;
                    case 2:
                        return View.TRANSLATION_Z;
                    case 3:
                        return View.SCALE_X;
                    case 4:
                        return View.SCALE_Y;
                    case 5:
                        return View.ROTATION;
                    case 6:
                        return View.X;
                    case 7:
                        return View.Y;
                    case 8:
                        return View.ALPHA;
                }
            }
            if (!(obj instanceof TouchAnimator) || !"position".equals(str)) {
                return Property.of(obj.getClass(), cls, str);
            }
            return TouchAnimator.POSITION;
        }

        public Builder setStartDelay(float f) {
            this.mStartDelay = f;
            return this;
        }

        public Builder setEndDelay(float f) {
            this.mEndDelay = f;
            return this;
        }

        public Builder setInterpolator(Interpolator interpolator) {
            this.mInterpolator = interpolator;
            return this;
        }

        public Builder setListener(Listener listener) {
            this.mListener = listener;
            return this;
        }

        public TouchAnimator build() {
            List<Object> list = this.mTargets;
            Object[] array = list.toArray(new Object[list.size()]);
            List<KeyframeSet> list2 = this.mValues;
            return new TouchAnimator(array, (KeyframeSet[]) list2.toArray(new KeyframeSet[list2.size()]), this.mStartDelay, this.mEndDelay, this.mInterpolator, this.mListener);
        }
    }

    public static abstract class KeyframeSet {
        public final float mFrameWidth;
        public final int mSize;

        public abstract void interpolate(int i, float f, Object obj);

        public KeyframeSet(int i) {
            this.mSize = i;
            this.mFrameWidth = 1.0f / ((float) (i - 1));
        }

        public void setValue(float f, Object obj) {
            int constrain = MathUtils.constrain((int) Math.ceil((double) (f / this.mFrameWidth)), 1, this.mSize - 1);
            float f2 = this.mFrameWidth;
            interpolate(constrain, (f - (((float) (constrain - 1)) * f2)) / f2, obj);
        }

        public static KeyframeSet ofFloat(Property property, float... fArr) {
            return new FloatKeyframeSet(property, fArr);
        }
    }

    public static class FloatKeyframeSet<T> extends KeyframeSet {
        public final Property<T, Float> mProperty;
        public final float[] mValues;

        public FloatKeyframeSet(Property<T, Float> property, float[] fArr) {
            super(fArr.length);
            this.mProperty = property;
            this.mValues = fArr;
        }

        public void interpolate(int i, float f, Object obj) {
            float[] fArr = this.mValues;
            float f2 = fArr[i - 1];
            this.mProperty.set(obj, Float.valueOf(f2 + ((fArr[i] - f2) * f)));
        }
    }
}
