package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.view.View;
import com.android.settingslib.Utils;
import com.android.systemui.R$color;
import com.android.systemui.animation.Interpolators;

public class BarTransitions {
    public boolean mAlwaysOpaque = false;
    public final BarBackgroundDrawable mBarBackground;
    public int mMode;
    public final String mTag;
    public final View mView;

    public boolean isLightsOut(int i) {
        return i == 3 || i == 6;
    }

    public BarTransitions(View view, int i) {
        this.mTag = "BarTransitions." + view.getClass().getSimpleName();
        this.mView = view;
        BarBackgroundDrawable barBackgroundDrawable = new BarBackgroundDrawable(view.getContext(), i);
        this.mBarBackground = barBackgroundDrawable;
        view.setBackground(barBackgroundDrawable);
    }

    public int getMode() {
        return this.mMode;
    }

    public boolean isAlwaysOpaque() {
        return this.mAlwaysOpaque;
    }

    public void transitionTo(int i, boolean z) {
        if (isAlwaysOpaque() && (i == 1 || i == 2 || i == 0)) {
            i = 4;
        }
        if (isAlwaysOpaque() && i == 6) {
            i = 3;
        }
        int i2 = this.mMode;
        if (i2 != i) {
            this.mMode = i;
            onTransition(i2, i, z);
        }
    }

    public void onTransition(int i, int i2, boolean z) {
        applyModeBackground(i, i2, z);
    }

    public void applyModeBackground(int i, int i2, boolean z) {
        this.mBarBackground.applyModeBackground(i, i2, z);
    }

    public static String modeToString(int i) {
        if (i == 4) {
            return "MODE_OPAQUE";
        }
        if (i == 1) {
            return "MODE_SEMI_TRANSPARENT";
        }
        if (i == 2) {
            return "MODE_TRANSLUCENT";
        }
        if (i == 3) {
            return "MODE_LIGHTS_OUT";
        }
        if (i == 0) {
            return "MODE_TRANSPARENT";
        }
        if (i == 5) {
            return "MODE_WARNING";
        }
        if (i == 6) {
            return "MODE_LIGHTS_OUT_TRANSPARENT";
        }
        throw new IllegalArgumentException("Unknown mode " + i);
    }

    public void finishAnimations() {
        this.mBarBackground.finishAnimation();
    }

    public static class BarBackgroundDrawable extends Drawable {
        public boolean mAnimating;
        public int mColor;
        public int mColorStart;
        public long mEndTime;
        public Rect mFrame;
        public final Drawable mGradient;
        public int mGradientAlpha;
        public int mGradientAlphaStart;
        public int mMode = -1;
        public final int mOpaque;
        public float mOverrideAlpha = 1.0f;
        public Paint mPaint = new Paint();
        public final int mSemiTransparent;
        public long mStartTime;
        public PorterDuffColorFilter mTintFilter;
        public final int mTransparent;
        public final int mWarning;

        public int getOpacity() {
            return -3;
        }

        public void setAlpha(int i) {
        }

        public void setColorFilter(ColorFilter colorFilter) {
        }

        public BarBackgroundDrawable(Context context, int i) {
            context.getResources();
            this.mOpaque = context.getColor(R$color.system_bar_background_opaque);
            this.mSemiTransparent = context.getColor(17171094);
            this.mTransparent = context.getColor(R$color.system_bar_background_transparent);
            this.mWarning = Utils.getColorAttrDefaultColor(context, 16844099);
            this.mGradient = context.getDrawable(i);
        }

        public void setFrame(Rect rect) {
            this.mFrame = rect;
        }

        public void setOverrideAlpha(float f) {
            this.mOverrideAlpha = f;
            invalidateSelf();
        }

        public float getOverrideAlpha() {
            return this.mOverrideAlpha;
        }

        public int getColor() {
            return this.mColor;
        }

        public Rect getFrame() {
            return this.mFrame;
        }

        public void setTint(int i) {
            PorterDuff.Mode mode;
            PorterDuffColorFilter porterDuffColorFilter = this.mTintFilter;
            if (porterDuffColorFilter == null) {
                mode = PorterDuff.Mode.SRC_IN;
            } else {
                mode = porterDuffColorFilter.getMode();
            }
            PorterDuffColorFilter porterDuffColorFilter2 = this.mTintFilter;
            if (porterDuffColorFilter2 == null || porterDuffColorFilter2.getColor() != i) {
                this.mTintFilter = new PorterDuffColorFilter(i, mode);
            }
            invalidateSelf();
        }

        public void setTintMode(PorterDuff.Mode mode) {
            PorterDuffColorFilter porterDuffColorFilter = this.mTintFilter;
            int color = porterDuffColorFilter == null ? 0 : porterDuffColorFilter.getColor();
            PorterDuffColorFilter porterDuffColorFilter2 = this.mTintFilter;
            if (porterDuffColorFilter2 == null || porterDuffColorFilter2.getMode() != mode) {
                this.mTintFilter = new PorterDuffColorFilter(color, mode);
            }
            invalidateSelf();
        }

        public void onBoundsChange(Rect rect) {
            super.onBoundsChange(rect);
            this.mGradient.setBounds(rect);
        }

        public void applyModeBackground(int i, int i2, boolean z) {
            if (this.mMode != i2) {
                this.mMode = i2;
                this.mAnimating = z;
                if (z) {
                    long elapsedRealtime = SystemClock.elapsedRealtime();
                    this.mStartTime = elapsedRealtime;
                    this.mEndTime = elapsedRealtime + 200;
                    this.mGradientAlphaStart = this.mGradientAlpha;
                    this.mColorStart = this.mColor;
                }
                invalidateSelf();
            }
        }

        public void finishAnimation() {
            if (this.mAnimating) {
                this.mAnimating = false;
                invalidateSelf();
            }
        }

        public void draw(Canvas canvas) {
            int i;
            int i2 = this.mMode;
            if (i2 == 5) {
                i = this.mWarning;
            } else if (i2 == 2) {
                i = this.mSemiTransparent;
            } else if (i2 == 1) {
                i = this.mSemiTransparent;
            } else if (i2 == 0 || i2 == 6) {
                i = this.mTransparent;
            } else {
                i = this.mOpaque;
            }
            if (!this.mAnimating) {
                this.mColor = i;
                this.mGradientAlpha = 0;
            } else {
                long elapsedRealtime = SystemClock.elapsedRealtime();
                long j = this.mEndTime;
                if (elapsedRealtime >= j) {
                    this.mAnimating = false;
                    this.mColor = i;
                    this.mGradientAlpha = 0;
                } else {
                    long j2 = this.mStartTime;
                    float max = Math.max(0.0f, Math.min(Interpolators.LINEAR.getInterpolation(((float) (elapsedRealtime - j2)) / ((float) (j - j2))), 1.0f));
                    float f = 1.0f - max;
                    this.mGradientAlpha = (int) ((((float) 0) * max) + (((float) this.mGradientAlphaStart) * f));
                    this.mColor = Color.argb((int) ((((float) Color.alpha(i)) * max) + (((float) Color.alpha(this.mColorStart)) * f)), (int) ((((float) Color.red(i)) * max) + (((float) Color.red(this.mColorStart)) * f)), (int) ((((float) Color.green(i)) * max) + (((float) Color.green(this.mColorStart)) * f)), (int) ((max * ((float) Color.blue(i))) + (((float) Color.blue(this.mColorStart)) * f)));
                }
            }
            int i3 = this.mGradientAlpha;
            if (i3 > 0) {
                this.mGradient.setAlpha(i3);
                this.mGradient.draw(canvas);
            }
            if (Color.alpha(this.mColor) > 0) {
                this.mPaint.setColor(this.mColor);
                PorterDuffColorFilter porterDuffColorFilter = this.mTintFilter;
                if (porterDuffColorFilter != null) {
                    this.mPaint.setColorFilter(porterDuffColorFilter);
                }
                this.mPaint.setAlpha((int) (((float) Color.alpha(this.mColor)) * this.mOverrideAlpha));
                Rect rect = this.mFrame;
                if (rect != null) {
                    canvas.drawRect(rect, this.mPaint);
                } else {
                    canvas.drawPaint(this.mPaint);
                }
            }
            if (this.mAnimating) {
                invalidateSelf();
            }
        }
    }
}
