package com.airbnb.lottie.value;

public class LottieFrameInfo<T> {
    public float endFrame;
    public T endValue;
    public float interpolatedKeyframeProgress;
    public float linearKeyframeProgress;
    public float overallProgress;
    public float startFrame;
    public T startValue;

    public LottieFrameInfo<T> set(float f, float f2, T t, T t2, float f3, float f4, float f5) {
        this.startFrame = f;
        this.endFrame = f2;
        this.startValue = t;
        this.endValue = t2;
        this.linearKeyframeProgress = f3;
        this.interpolatedKeyframeProgress = f4;
        this.overallProgress = f5;
        return this;
    }
}
