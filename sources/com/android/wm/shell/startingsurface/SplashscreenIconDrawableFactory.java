package com.android.wm.shell.startingsurface;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Trace;
import android.util.Log;
import android.util.PathParser;
import android.window.SplashScreenView;
import java.util.function.LongConsumer;

public class SplashscreenIconDrawableFactory {
    public static Drawable[] makeIconDrawable(int i, int i2, Drawable drawable, int i3, int i4, boolean z, Handler handler) {
        Drawable drawable2;
        boolean z2 = (i == 0 || i == i2) ? false : true;
        if (drawable instanceof Animatable) {
            drawable2 = new AnimatableIconAnimateListener(drawable);
        } else if (drawable instanceof AdaptiveIconDrawable) {
            drawable2 = new ImmobileIconDrawable(drawable, i3, i4, z, handler);
            z2 = false;
        } else {
            drawable2 = new ImmobileIconDrawable(new AdaptiveForegroundDrawable(drawable), i3, i4, z, handler);
        }
        return new Drawable[]{drawable2, z2 ? new MaskBackgroundDrawable(i) : null};
    }

    public static Drawable[] makeLegacyIconDrawable(Drawable drawable, int i, int i2, boolean z, Handler handler) {
        return new Drawable[]{new ImmobileIconDrawable(drawable, i, i2, z, handler)};
    }

    public static class ImmobileIconDrawable extends Drawable {
        public Bitmap mIconBitmap;
        public final Matrix mMatrix;
        public final Paint mPaint = new Paint(7);

        public int getOpacity() {
            return 1;
        }

        public void setAlpha(int i) {
        }

        public void setColorFilter(ColorFilter colorFilter) {
        }

        public ImmobileIconDrawable(Drawable drawable, int i, int i2, boolean z, Handler handler) {
            Matrix matrix = new Matrix();
            this.mMatrix = matrix;
            if (z) {
                handler.post(new SplashscreenIconDrawableFactory$ImmobileIconDrawable$$ExternalSyntheticLambda0(this, drawable, i2));
                return;
            }
            float f = ((float) i2) / ((float) i);
            matrix.setScale(f, f);
            handler.post(new SplashscreenIconDrawableFactory$ImmobileIconDrawable$$ExternalSyntheticLambda1(this, drawable, i));
        }

        /* renamed from: preDrawIcon */
        public final void lambda$new$1(Drawable drawable, int i) {
            synchronized (this.mPaint) {
                Trace.traceBegin(32, "preDrawIcon");
                this.mIconBitmap = Bitmap.createBitmap(i, i, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(this.mIconBitmap);
                drawable.setBounds(0, 0, i, i);
                drawable.draw(canvas);
                Trace.traceEnd(32);
            }
        }

        public void draw(Canvas canvas) {
            synchronized (this.mPaint) {
                Bitmap bitmap = this.mIconBitmap;
                if (bitmap != null) {
                    canvas.drawBitmap(bitmap, this.mMatrix, this.mPaint);
                } else {
                    invalidateSelf();
                }
            }
        }
    }

    public static class MaskBackgroundDrawable extends Drawable {
        public static Path sMask;
        public final Paint mBackgroundPaint;
        public final Matrix mMaskMatrix = new Matrix();
        public final Path mMaskScaleOnly = new Path(new Path(sMask));

        public int getOpacity() {
            return 1;
        }

        public void setColorFilter(ColorFilter colorFilter) {
        }

        public MaskBackgroundDrawable(int i) {
            sMask = PathParser.createPathFromPathData(Resources.getSystem().getString(17039989));
            if (i != 0) {
                Paint paint = new Paint(7);
                this.mBackgroundPaint = paint;
                paint.setColor(i);
                paint.setStyle(Paint.Style.FILL);
                return;
            }
            this.mBackgroundPaint = null;
        }

        public void onBoundsChange(Rect rect) {
            if (!rect.isEmpty()) {
                updateLayerBounds(rect);
            }
        }

        public void updateLayerBounds(Rect rect) {
            this.mMaskMatrix.setScale(((float) rect.width()) / 100.0f, ((float) rect.height()) / 100.0f);
            sMask.transform(this.mMaskMatrix, this.mMaskScaleOnly);
        }

        public void draw(Canvas canvas) {
            canvas.clipPath(this.mMaskScaleOnly);
            Paint paint = this.mBackgroundPaint;
            if (paint != null) {
                canvas.drawPath(this.mMaskScaleOnly, paint);
            }
        }

        public void setAlpha(int i) {
            Paint paint = this.mBackgroundPaint;
            if (paint != null) {
                paint.setAlpha(i);
            }
        }
    }

    public static class AdaptiveForegroundDrawable extends MaskBackgroundDrawable {
        public final Drawable mForegroundDrawable;
        public final Rect mTmpOutRect = new Rect();

        public AdaptiveForegroundDrawable(Drawable drawable) {
            super(0);
            this.mForegroundDrawable = drawable;
        }

        public void updateLayerBounds(Rect rect) {
            super.updateLayerBounds(rect);
            int width = rect.width() / 2;
            int height = rect.height() / 2;
            int width2 = (int) (((float) rect.width()) / 1.3333334f);
            int height2 = (int) (((float) rect.height()) / 1.3333334f);
            Rect rect2 = this.mTmpOutRect;
            rect2.set(width - width2, height - height2, width + width2, height + height2);
            Drawable drawable = this.mForegroundDrawable;
            if (drawable != null) {
                drawable.setBounds(rect2);
            }
            invalidateSelf();
        }

        public void draw(Canvas canvas) {
            super.draw(canvas);
            this.mForegroundDrawable.draw(canvas);
        }

        public void setColorFilter(ColorFilter colorFilter) {
            this.mForegroundDrawable.setColorFilter(colorFilter);
        }
    }

    public static class AnimatableIconAnimateListener extends AdaptiveForegroundDrawable implements SplashScreenView.IconAnimateListener {
        public final Animatable mAnimatableIcon = ((Animatable) this.mForegroundDrawable);
        public boolean mAnimationTriggered;
        public AnimatorListenerAdapter mJankMonitoringListener;
        public boolean mRunning;
        public LongConsumer mStartListener;

        public /* bridge */ /* synthetic */ void setColorFilter(ColorFilter colorFilter) {
            super.setColorFilter(colorFilter);
        }

        public AnimatableIconAnimateListener(Drawable drawable) {
            super(drawable);
            this.mForegroundDrawable.setCallback(new Drawable.Callback() {
                public void invalidateDrawable(Drawable drawable) {
                    AnimatableIconAnimateListener.this.invalidateSelf();
                }

                public void scheduleDrawable(Drawable drawable, Runnable runnable, long j) {
                    AnimatableIconAnimateListener.this.scheduleSelf(runnable, j);
                }

                public void unscheduleDrawable(Drawable drawable, Runnable runnable) {
                    AnimatableIconAnimateListener.this.unscheduleSelf(runnable);
                }
            });
        }

        public void setAnimationJankMonitoring(AnimatorListenerAdapter animatorListenerAdapter) {
            this.mJankMonitoringListener = animatorListenerAdapter;
        }

        public void prepareAnimate(LongConsumer longConsumer) {
            stopAnimation();
            this.mStartListener = longConsumer;
        }

        public final void startAnimation() {
            AnimatorListenerAdapter animatorListenerAdapter = this.mJankMonitoringListener;
            if (animatorListenerAdapter != null) {
                animatorListenerAdapter.onAnimationStart((Animator) null);
            }
            long j = 0;
            try {
                this.mAnimatableIcon.start();
                Animatable animatable = this.mAnimatableIcon;
                if (!(animatable instanceof AnimatedVectorDrawable) || ((AnimatedVectorDrawable) animatable).getTotalDuration() <= 0) {
                    Animatable animatable2 = this.mAnimatableIcon;
                    if ((animatable2 instanceof AnimationDrawable) && ((AnimationDrawable) animatable2).getTotalDuration() > 0) {
                        j = ((AnimationDrawable) this.mAnimatableIcon).getTotalDuration();
                    }
                } else {
                    j = ((AnimatedVectorDrawable) this.mAnimatableIcon).getTotalDuration();
                }
                this.mRunning = true;
                LongConsumer longConsumer = this.mStartListener;
                if (longConsumer != null) {
                    longConsumer.accept(j);
                }
            } catch (Exception e) {
                Log.e("ShellStartingWindow", "Error while running the splash screen animated icon", e);
                this.mRunning = false;
                AnimatorListenerAdapter animatorListenerAdapter2 = this.mJankMonitoringListener;
                if (animatorListenerAdapter2 != null) {
                    animatorListenerAdapter2.onAnimationCancel((Animator) null);
                }
                LongConsumer longConsumer2 = this.mStartListener;
                if (longConsumer2 != null) {
                    longConsumer2.accept(0);
                }
            }
        }

        public final void onAnimationEnd() {
            this.mAnimatableIcon.stop();
            AnimatorListenerAdapter animatorListenerAdapter = this.mJankMonitoringListener;
            if (animatorListenerAdapter != null) {
                animatorListenerAdapter.onAnimationEnd((Animator) null);
            }
            this.mStartListener = null;
            this.mRunning = false;
        }

        public void stopAnimation() {
            if (this.mRunning) {
                onAnimationEnd();
                this.mJankMonitoringListener = null;
            }
        }

        public final void ensureAnimationStarted() {
            if (!this.mAnimationTriggered) {
                if (!this.mRunning) {
                    startAnimation();
                }
                this.mAnimationTriggered = true;
            }
        }

        public void draw(Canvas canvas) {
            ensureAnimationStarted();
            super.draw(canvas);
        }
    }
}
