package com.android.settingslib.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Paint;
import android.view.RenderNodeAnimator;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import com.android.settingslib.R$dimen;

public class AppearAnimationUtils implements AppearAnimationCreator<View> {
    public boolean mAppearing;
    public final float mDelayScale;
    public final long mDuration;
    public final Interpolator mInterpolator;
    public final AppearAnimationProperties mProperties;
    public RowTranslationScaler mRowTranslationScaler;
    public final float mStartTranslation;

    public interface RowTranslationScaler {
        float getRowTranslationScale(int i, int i2);
    }

    public AppearAnimationUtils(Context context) {
        this(context, 220, 1.0f, 1.0f, AnimationUtils.loadInterpolator(context, 17563662));
    }

    public AppearAnimationUtils(Context context, long j, float f, float f2, Interpolator interpolator) {
        this.mProperties = new AppearAnimationProperties();
        this.mInterpolator = interpolator;
        this.mStartTranslation = ((float) context.getResources().getDimensionPixelOffset(R$dimen.appear_y_translation_start)) * f;
        this.mDelayScale = f2;
        this.mDuration = j;
        this.mAppearing = true;
    }

    public void startAnimation2d(View[][] viewArr, Runnable runnable) {
        startAnimation2d(viewArr, runnable, this);
    }

    public void startAnimation(View[] viewArr, Runnable runnable) {
        startAnimation(viewArr, runnable, this);
    }

    public <T> void startAnimation2d(T[][] tArr, Runnable runnable, AppearAnimationCreator<T> appearAnimationCreator) {
        startAnimations(getDelays(tArr), tArr, runnable, appearAnimationCreator);
    }

    public <T> void startAnimation(T[] tArr, Runnable runnable, AppearAnimationCreator<T> appearAnimationCreator) {
        startAnimations(getDelays(tArr), tArr, runnable, appearAnimationCreator);
    }

    public final <T> void startAnimations(AppearAnimationProperties appearAnimationProperties, T[] tArr, Runnable runnable, AppearAnimationCreator<T> appearAnimationCreator) {
        AppearAnimationProperties appearAnimationProperties2 = appearAnimationProperties;
        if (appearAnimationProperties2.maxDelayRowIndex == -1 || appearAnimationProperties2.maxDelayColIndex == -1) {
            runnable.run();
            return;
        }
        int i = 0;
        while (true) {
            long[][] jArr = appearAnimationProperties2.delays;
            if (i < jArr.length) {
                long j = jArr[i][0];
                Runnable runnable2 = (appearAnimationProperties2.maxDelayRowIndex == i && appearAnimationProperties2.maxDelayColIndex == 0) ? runnable : null;
                RowTranslationScaler rowTranslationScaler = this.mRowTranslationScaler;
                float rowTranslationScale = (rowTranslationScaler != null ? rowTranslationScaler.getRowTranslationScale(i, jArr.length) : 1.0f) * this.mStartTranslation;
                T t = tArr[i];
                long j2 = this.mDuration;
                boolean z = this.mAppearing;
                if (!z) {
                    rowTranslationScale = -rowTranslationScale;
                }
                appearAnimationCreator.createAnimation(t, j, j2, rowTranslationScale, z, this.mInterpolator, runnable2);
                i++;
            } else {
                return;
            }
        }
    }

    public final <T> void startAnimations(AppearAnimationProperties appearAnimationProperties, T[][] tArr, Runnable runnable, AppearAnimationCreator<T> appearAnimationCreator) {
        AppearAnimationProperties appearAnimationProperties2 = appearAnimationProperties;
        if (appearAnimationProperties2.maxDelayRowIndex == -1 || appearAnimationProperties2.maxDelayColIndex == -1) {
            runnable.run();
            return;
        }
        int i = 0;
        while (true) {
            long[][] jArr = appearAnimationProperties2.delays;
            if (i < jArr.length) {
                long[] jArr2 = jArr[i];
                RowTranslationScaler rowTranslationScaler = this.mRowTranslationScaler;
                float rowTranslationScale = (rowTranslationScaler != null ? rowTranslationScaler.getRowTranslationScale(i, jArr.length) : 1.0f) * this.mStartTranslation;
                int i2 = 0;
                while (i2 < jArr2.length) {
                    long j = jArr2[i2];
                    Runnable runnable2 = (appearAnimationProperties2.maxDelayRowIndex == i && appearAnimationProperties2.maxDelayColIndex == i2) ? runnable : null;
                    T t = tArr[i][i2];
                    long j2 = this.mDuration;
                    boolean z = this.mAppearing;
                    appearAnimationCreator.createAnimation(t, j, j2, z ? rowTranslationScale : -rowTranslationScale, z, this.mInterpolator, runnable2);
                    i2++;
                }
                i++;
            } else {
                return;
            }
        }
    }

    public final <T> AppearAnimationProperties getDelays(T[] tArr) {
        AppearAnimationProperties appearAnimationProperties = this.mProperties;
        appearAnimationProperties.maxDelayColIndex = -1;
        appearAnimationProperties.maxDelayRowIndex = -1;
        appearAnimationProperties.delays = new long[tArr.length][];
        long j = -1;
        for (int i = 0; i < tArr.length; i++) {
            this.mProperties.delays[i] = new long[1];
            long calculateDelay = calculateDelay(i, 0);
            AppearAnimationProperties appearAnimationProperties2 = this.mProperties;
            appearAnimationProperties2.delays[i][0] = calculateDelay;
            if (tArr[i] != null && calculateDelay > j) {
                appearAnimationProperties2.maxDelayColIndex = 0;
                appearAnimationProperties2.maxDelayRowIndex = i;
                j = calculateDelay;
            }
        }
        return this.mProperties;
    }

    public final <T> AppearAnimationProperties getDelays(T[][] tArr) {
        AppearAnimationProperties appearAnimationProperties = this.mProperties;
        appearAnimationProperties.maxDelayColIndex = -1;
        appearAnimationProperties.maxDelayRowIndex = -1;
        appearAnimationProperties.delays = new long[tArr.length][];
        long j = -1;
        for (int i = 0; i < tArr.length; i++) {
            T[] tArr2 = tArr[i];
            this.mProperties.delays[i] = new long[tArr2.length];
            for (int i2 = 0; i2 < tArr2.length; i2++) {
                long calculateDelay = calculateDelay(i, i2);
                AppearAnimationProperties appearAnimationProperties2 = this.mProperties;
                appearAnimationProperties2.delays[i][i2] = calculateDelay;
                if (tArr[i][i2] != null && calculateDelay > j) {
                    appearAnimationProperties2.maxDelayColIndex = i2;
                    appearAnimationProperties2.maxDelayRowIndex = i;
                    j = calculateDelay;
                }
            }
        }
        return this.mProperties;
    }

    public long calculateDelay(int i, int i2) {
        return (long) ((((double) (i * 40)) + (((double) i2) * (Math.pow((double) i, 0.4d) + 0.4d) * 20.0d)) * ((double) this.mDelayScale));
    }

    public Interpolator getInterpolator() {
        return this.mInterpolator;
    }

    public float getStartTranslation() {
        return this.mStartTranslation;
    }

    public void createAnimation(View view, long j, long j2, float f, boolean z, Interpolator interpolator, Runnable runnable) {
        createAnimation(view, j, j2, f, z, interpolator, runnable, (AnimatorListenerAdapter) null);
    }

    public void createAnimation(final View view, long j, long j2, float f, boolean z, Interpolator interpolator, Runnable runnable, AnimatorListenerAdapter animatorListenerAdapter) {
        RenderNodeAnimator renderNodeAnimator;
        if (view != null) {
            final float f2 = z ? 1.0f : 0.0f;
            float f3 = z ? 0.0f : f;
            view.setAlpha(1.0f - f2);
            view.setTranslationY(f - f3);
            if (view.isHardwareAccelerated()) {
                renderNodeAnimator = new RenderNodeAnimator(11, f2);
                renderNodeAnimator.setTarget(view);
            } else {
                renderNodeAnimator = ObjectAnimator.ofFloat(view, View.ALPHA, new float[]{view.getAlpha(), f2});
            }
            renderNodeAnimator.setInterpolator(interpolator);
            renderNodeAnimator.setDuration(j2);
            long j3 = j;
            renderNodeAnimator.setStartDelay(j);
            if (view.hasOverlappingRendering()) {
                view.setLayerType(2, (Paint) null);
                renderNodeAnimator.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animator) {
                        view.setLayerType(0, (Paint) null);
                    }
                });
            }
            final Runnable runnable2 = runnable;
            renderNodeAnimator.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animator) {
                    view.setAlpha(f2);
                    Runnable runnable = runnable2;
                    if (runnable != null) {
                        runnable.run();
                    }
                }
            });
            renderNodeAnimator.start();
            startTranslationYAnimation(view, j, j2, f3, interpolator, animatorListenerAdapter);
        }
    }

    public static void startTranslationYAnimation(final View view, long j, long j2, final float f, Interpolator interpolator, AnimatorListenerAdapter animatorListenerAdapter) {
        RenderNodeAnimator renderNodeAnimator;
        if (view.isHardwareAccelerated()) {
            renderNodeAnimator = new RenderNodeAnimator(1, f);
            renderNodeAnimator.setTarget(view);
        } else {
            renderNodeAnimator = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, new float[]{view.getTranslationY(), f});
        }
        renderNodeAnimator.setInterpolator(interpolator);
        renderNodeAnimator.setDuration(j2);
        renderNodeAnimator.setStartDelay(j);
        if (animatorListenerAdapter != null) {
            renderNodeAnimator.addListener(animatorListenerAdapter);
        }
        renderNodeAnimator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                view.setTranslationY(f);
            }
        });
        renderNodeAnimator.start();
    }

    public class AppearAnimationProperties {
        public long[][] delays;
        public int maxDelayColIndex;
        public int maxDelayRowIndex;

        public AppearAnimationProperties() {
        }
    }
}
