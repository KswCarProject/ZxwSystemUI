package com.android.keyguard;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.ContextThemeWrapper;
import android.view.animation.Interpolator;
import android.widget.TextView;
import com.android.settingslib.Utils;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.util.ColorUtilKt;

public class NumPadAnimator {
    public GradientDrawable mBackground;
    public ValueAnimator mContractAnimator;
    public AnimatorSet mContractAnimatorSet;
    public TextView mDigitTextView;
    public ValueAnimator mExpandAnimator;
    public AnimatorSet mExpandAnimatorSet;
    public Drawable mImageButton;
    public int mNormalBackgroundColor;
    public int mPressedBackgroundColor;
    public int mStyle;
    public int mTextColorPressed;
    public int mTextColorPrimary;

    public NumPadAnimator(Context context, Drawable drawable, int i, Drawable drawable2) {
        this(context, drawable, i, (TextView) null, drawable2);
    }

    public NumPadAnimator(Context context, Drawable drawable, int i, TextView textView, Drawable drawable2) {
        this.mStyle = i;
        this.mBackground = (GradientDrawable) drawable;
        this.mDigitTextView = textView;
        this.mImageButton = drawable2;
        reloadColors(context);
    }

    public void expand() {
        this.mExpandAnimatorSet.cancel();
        this.mContractAnimatorSet.cancel();
        this.mExpandAnimatorSet.start();
    }

    public void contract() {
        this.mExpandAnimatorSet.cancel();
        this.mContractAnimatorSet.cancel();
        this.mContractAnimatorSet.start();
    }

    public void onLayout(int i) {
        float f = (float) i;
        float f2 = f / 2.0f;
        float f3 = f / 4.0f;
        this.mBackground.setCornerRadius(f2);
        this.mExpandAnimator.setFloatValues(new float[]{f2, f3});
        this.mContractAnimator.setFloatValues(new float[]{f3, f2});
    }

    public void reloadColors(Context context) {
        int i;
        boolean z = this.mImageButton == null;
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, this.mStyle);
        TypedArray obtainStyledAttributes = contextThemeWrapper.obtainStyledAttributes(new int[]{16843817});
        this.mNormalBackgroundColor = ColorUtilKt.getPrivateAttrColorIfUnset(contextThemeWrapper, obtainStyledAttributes, 0, 0, 17956909);
        obtainStyledAttributes.recycle();
        this.mBackground.setColor(this.mNormalBackgroundColor);
        this.mPressedBackgroundColor = context.getColor(17170491);
        if (z) {
            i = Utils.getColorAttrDefaultColor(context, 16842806);
        } else {
            i = Utils.getColorAttrDefaultColor(context, 16842809);
        }
        this.mTextColorPrimary = i;
        this.mTextColorPressed = Utils.getColorAttrDefaultColor(context, 17957103);
        createAnimators();
    }

    public final void createAnimators() {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        this.mExpandAnimator = ofFloat;
        ofFloat.setDuration(100);
        ValueAnimator valueAnimator = this.mExpandAnimator;
        Interpolator interpolator = Interpolators.LINEAR;
        valueAnimator.setInterpolator(interpolator);
        this.mExpandAnimator.addUpdateListener(new NumPadAnimator$$ExternalSyntheticLambda0(this));
        ValueAnimator ofObject = ValueAnimator.ofObject(new ArgbEvaluator(), new Object[]{Integer.valueOf(this.mNormalBackgroundColor), Integer.valueOf(this.mPressedBackgroundColor)});
        ofObject.setDuration(50);
        ofObject.setInterpolator(interpolator);
        ofObject.addUpdateListener(new NumPadAnimator$$ExternalSyntheticLambda1(this));
        ValueAnimator ofObject2 = ValueAnimator.ofObject(new ArgbEvaluator(), new Object[]{Integer.valueOf(this.mTextColorPrimary), Integer.valueOf(this.mTextColorPressed)});
        ofObject2.setInterpolator(interpolator);
        ofObject2.setDuration(50);
        ofObject2.addUpdateListener(new NumPadAnimator$$ExternalSyntheticLambda2(this));
        AnimatorSet animatorSet = new AnimatorSet();
        this.mExpandAnimatorSet = animatorSet;
        animatorSet.playTogether(new Animator[]{this.mExpandAnimator, ofObject, ofObject2});
        ValueAnimator ofFloat2 = ValueAnimator.ofFloat(new float[]{1.0f, 0.0f});
        this.mContractAnimator = ofFloat2;
        ofFloat2.setStartDelay(33);
        this.mContractAnimator.setDuration(417);
        this.mContractAnimator.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
        this.mContractAnimator.addUpdateListener(new NumPadAnimator$$ExternalSyntheticLambda3(this));
        ValueAnimator ofObject3 = ValueAnimator.ofObject(new ArgbEvaluator(), new Object[]{Integer.valueOf(this.mPressedBackgroundColor), Integer.valueOf(this.mNormalBackgroundColor)});
        ofObject3.setInterpolator(interpolator);
        ofObject3.setStartDelay(33);
        ofObject3.setDuration(417);
        ofObject3.addUpdateListener(new NumPadAnimator$$ExternalSyntheticLambda4(this));
        ValueAnimator ofObject4 = ValueAnimator.ofObject(new ArgbEvaluator(), new Object[]{Integer.valueOf(this.mTextColorPressed), Integer.valueOf(this.mTextColorPrimary)});
        ofObject4.setInterpolator(interpolator);
        ofObject4.setStartDelay(33);
        ofObject4.setDuration(417);
        ofObject4.addUpdateListener(new NumPadAnimator$$ExternalSyntheticLambda5(this));
        AnimatorSet animatorSet2 = new AnimatorSet();
        this.mContractAnimatorSet = animatorSet2;
        animatorSet2.playTogether(new Animator[]{this.mContractAnimator, ofObject3, ofObject4});
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$createAnimators$0(ValueAnimator valueAnimator) {
        this.mBackground.setCornerRadius(((Float) valueAnimator.getAnimatedValue()).floatValue());
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$createAnimators$1(ValueAnimator valueAnimator) {
        this.mBackground.setColor(((Integer) valueAnimator.getAnimatedValue()).intValue());
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$createAnimators$2(ValueAnimator valueAnimator) {
        TextView textView = this.mDigitTextView;
        if (textView != null) {
            textView.setTextColor(((Integer) valueAnimator.getAnimatedValue()).intValue());
        }
        Drawable drawable = this.mImageButton;
        if (drawable != null) {
            drawable.setTint(((Integer) valueAnimator.getAnimatedValue()).intValue());
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$createAnimators$3(ValueAnimator valueAnimator) {
        this.mBackground.setCornerRadius(((Float) valueAnimator.getAnimatedValue()).floatValue());
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$createAnimators$4(ValueAnimator valueAnimator) {
        this.mBackground.setColor(((Integer) valueAnimator.getAnimatedValue()).intValue());
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$createAnimators$5(ValueAnimator valueAnimator) {
        TextView textView = this.mDigitTextView;
        if (textView != null) {
            textView.setTextColor(((Integer) valueAnimator.getAnimatedValue()).intValue());
        }
        Drawable drawable = this.mImageButton;
        if (drawable != null) {
            drawable.setTint(((Integer) valueAnimator.getAnimatedValue()).intValue());
        }
    }
}
