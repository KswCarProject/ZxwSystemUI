package androidx.leanback.widget;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.core.view.ViewCompat;
import androidx.leanback.R$attr;
import androidx.leanback.R$color;
import androidx.leanback.R$dimen;
import androidx.leanback.R$drawable;
import androidx.leanback.R$fraction;
import androidx.leanback.R$id;
import androidx.leanback.R$integer;
import androidx.leanback.R$layout;
import androidx.leanback.R$styleable;

public class SearchOrbView extends FrameLayout implements View.OnClickListener {
    public boolean mAttachedToWindow;
    public boolean mColorAnimationEnabled;
    public ValueAnimator mColorAnimator;
    public final ArgbEvaluator mColorEvaluator;
    public Colors mColors;
    public final ValueAnimator.AnimatorUpdateListener mFocusUpdateListener;
    public final float mFocusedZ;
    public final float mFocusedZoom;
    public final ImageView mIcon;
    public Drawable mIconDrawable;
    public View.OnClickListener mListener;
    public final int mPulseDurationMs;
    public final View mRootView;
    public final int mScaleDurationMs;
    public final View mSearchOrbView;
    public ValueAnimator mShadowFocusAnimator;
    public final float mUnfocusedZ;
    public final ValueAnimator.AnimatorUpdateListener mUpdateListener;

    public static class Colors {
        public int brightColor;
        public int color;
        public int iconColor;

        public Colors(int i, int i2, int i3) {
            this.color = i;
            this.brightColor = i2 == i ? getBrightColor(i) : i2;
            this.iconColor = i3;
        }

        public static int getBrightColor(int i) {
            return Color.argb((int) ((((float) Color.alpha(i)) * 0.85f) + 38.25f), (int) ((((float) Color.red(i)) * 0.85f) + 38.25f), (int) ((((float) Color.green(i)) * 0.85f) + 38.25f), (int) ((((float) Color.blue(i)) * 0.85f) + 38.25f));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(ValueAnimator valueAnimator) {
        setOrbViewColor(((Integer) valueAnimator.getAnimatedValue()).intValue());
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(ValueAnimator valueAnimator) {
        setSearchOrbZ(valueAnimator.getAnimatedFraction());
    }

    public void setSearchOrbZ(float f) {
        View view = this.mSearchOrbView;
        float f2 = this.mUnfocusedZ;
        ViewCompat.setZ(view, f2 + (f * (this.mFocusedZ - f2)));
    }

    public SearchOrbView(Context context) {
        this(context, (AttributeSet) null);
    }

    public SearchOrbView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.searchOrbViewStyle);
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    @SuppressLint({"CustomViewStyleable"})
    public SearchOrbView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mColorEvaluator = new ArgbEvaluator();
        this.mUpdateListener = new SearchOrbView$$ExternalSyntheticLambda0(this);
        this.mFocusUpdateListener = new SearchOrbView$$ExternalSyntheticLambda1(this);
        Resources resources = context.getResources();
        View inflate = ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(getLayoutResourceId(), this, true);
        this.mRootView = inflate;
        this.mSearchOrbView = inflate.findViewById(R$id.search_orb);
        ImageView imageView = (ImageView) inflate.findViewById(R$id.icon);
        this.mIcon = imageView;
        this.mFocusedZoom = context.getResources().getFraction(R$fraction.lb_search_orb_focused_zoom, 1, 1);
        this.mPulseDurationMs = context.getResources().getInteger(R$integer.lb_search_orb_pulse_duration_ms);
        this.mScaleDurationMs = context.getResources().getInteger(R$integer.lb_search_orb_scale_duration_ms);
        float dimensionPixelSize = (float) context.getResources().getDimensionPixelSize(R$dimen.lb_search_orb_focused_z);
        this.mFocusedZ = dimensionPixelSize;
        this.mUnfocusedZ = (float) context.getResources().getDimensionPixelSize(R$dimen.lb_search_orb_unfocused_z);
        int[] iArr = R$styleable.lbSearchOrbView;
        AttributeSet attributeSet2 = attributeSet;
        int i2 = i;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet2, iArr, i2, 0);
        ViewCompat.saveAttributeDataForStyleable(this, context, iArr, attributeSet2, obtainStyledAttributes, i2, 0);
        Drawable drawable = obtainStyledAttributes.getDrawable(R$styleable.lbSearchOrbView_searchOrbIcon);
        setOrbIcon(drawable == null ? resources.getDrawable(R$drawable.lb_ic_in_app_search) : drawable);
        int color = obtainStyledAttributes.getColor(R$styleable.lbSearchOrbView_searchOrbColor, resources.getColor(R$color.lb_default_search_color));
        setOrbColors(new Colors(color, obtainStyledAttributes.getColor(R$styleable.lbSearchOrbView_searchOrbBrightColor, color), obtainStyledAttributes.getColor(R$styleable.lbSearchOrbView_searchOrbIconColor, 0)));
        obtainStyledAttributes.recycle();
        setFocusable(true);
        setClipChildren(false);
        setOnClickListener(this);
        setSoundEffectsEnabled(false);
        setSearchOrbZ(0.0f);
        ViewCompat.setZ(imageView, dimensionPixelSize);
    }

    public int getLayoutResourceId() {
        return R$layout.lb_search_orb;
    }

    public void scaleOrbViewOnly(float f) {
        this.mSearchOrbView.setScaleX(f);
        this.mSearchOrbView.setScaleY(f);
    }

    public float getFocusedZoom() {
        return this.mFocusedZoom;
    }

    public void onClick(View view) {
        View.OnClickListener onClickListener = this.mListener;
        if (onClickListener != null) {
            onClickListener.onClick(view);
        }
    }

    public final void startShadowFocusAnimation(boolean z, int i) {
        if (this.mShadowFocusAnimator == null) {
            ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
            this.mShadowFocusAnimator = ofFloat;
            ofFloat.addUpdateListener(this.mFocusUpdateListener);
        }
        if (z) {
            this.mShadowFocusAnimator.start();
        } else {
            this.mShadowFocusAnimator.reverse();
        }
        this.mShadowFocusAnimator.setDuration((long) i);
    }

    public void onFocusChanged(boolean z, int i, Rect rect) {
        super.onFocusChanged(z, i, rect);
        animateOnFocus(z);
    }

    public void animateOnFocus(boolean z) {
        float f = z ? this.mFocusedZoom : 1.0f;
        this.mRootView.animate().scaleX(f).scaleY(f).setDuration((long) this.mScaleDurationMs).start();
        startShadowFocusAnimation(z, this.mScaleDurationMs);
        enableOrbColorAnimation(z);
    }

    public void setOrbIcon(Drawable drawable) {
        this.mIconDrawable = drawable;
        this.mIcon.setImageDrawable(drawable);
    }

    public void setOnOrbClickedListener(View.OnClickListener onClickListener) {
        this.mListener = onClickListener;
    }

    public void setOrbColors(Colors colors) {
        this.mColors = colors;
        this.mIcon.setColorFilter(colors.iconColor);
        if (this.mColorAnimator == null) {
            setOrbViewColor(this.mColors.color);
        } else {
            enableOrbColorAnimation(true);
        }
    }

    public void enableOrbColorAnimation(boolean z) {
        this.mColorAnimationEnabled = z;
        updateColorAnimator();
    }

    public final void updateColorAnimator() {
        ValueAnimator valueAnimator = this.mColorAnimator;
        if (valueAnimator != null) {
            valueAnimator.end();
            this.mColorAnimator = null;
        }
        if (this.mColorAnimationEnabled && this.mAttachedToWindow) {
            ValueAnimator ofObject = ValueAnimator.ofObject(this.mColorEvaluator, new Object[]{Integer.valueOf(this.mColors.color), Integer.valueOf(this.mColors.brightColor), Integer.valueOf(this.mColors.color)});
            this.mColorAnimator = ofObject;
            ofObject.setRepeatCount(-1);
            this.mColorAnimator.setDuration((long) (this.mPulseDurationMs * 2));
            this.mColorAnimator.addUpdateListener(this.mUpdateListener);
            this.mColorAnimator.start();
        }
    }

    public void setOrbViewColor(int i) {
        if (this.mSearchOrbView.getBackground() instanceof GradientDrawable) {
            ((GradientDrawable) this.mSearchOrbView.getBackground()).setColor(i);
        }
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mAttachedToWindow = true;
        updateColorAnimator();
    }

    public void onDetachedFromWindow() {
        this.mAttachedToWindow = false;
        updateColorAnimator();
        super.onDetachedFromWindow();
    }
}
