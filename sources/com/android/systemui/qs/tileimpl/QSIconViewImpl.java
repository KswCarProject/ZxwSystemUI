package com.android.systemui.qs.tileimpl;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import com.android.settingslib.Utils;
import com.android.systemui.R$color;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.plugins.qs.QSIconView;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.qs.AlphaControlledSignalTileView;
import java.util.Objects;
import java.util.function.Supplier;

public class QSIconViewImpl extends QSIconView {
    public boolean mAnimationEnabled = true;
    public ValueAnimator mColorAnimator = new ValueAnimator();
    public final View mIcon;
    public int mIconSizePx;
    public QSTile.Icon mLastIcon;
    public int mState = -1;
    public int mTint;

    public int getIconMeasureMode() {
        return 1073741824;
    }

    public QSIconViewImpl(Context context) {
        super(context);
        this.mIconSizePx = context.getResources().getDimensionPixelSize(R$dimen.qs_icon_size);
        View createIcon = createIcon();
        this.mIcon = createIcon;
        addView(createIcon);
        this.mColorAnimator.setDuration(350);
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mIconSizePx = getContext().getResources().getDimensionPixelSize(R$dimen.qs_icon_size);
    }

    public void disableAnimation() {
        this.mAnimationEnabled = false;
    }

    public View getIconView() {
        return this.mIcon;
    }

    public void onMeasure(int i, int i2) {
        int size = View.MeasureSpec.getSize(i);
        this.mIcon.measure(View.MeasureSpec.makeMeasureSpec(size, getIconMeasureMode()), exactly(this.mIconSizePx));
        setMeasuredDimension(size, this.mIcon.getMeasuredHeight());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName());
        sb.append('[');
        sb.append("state=" + this.mState);
        sb.append(", tint=" + this.mTint);
        if (this.mLastIcon != null) {
            sb.append(", lastIcon=" + this.mLastIcon.toString());
        }
        sb.append("]");
        return sb.toString();
    }

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        layout(this.mIcon, (getMeasuredWidth() - this.mIcon.getMeasuredWidth()) / 2, 0);
    }

    public void setIcon(QSTile.State state, boolean z) {
        setIcon((ImageView) this.mIcon, state, z);
    }

    /* renamed from: updateIcon */
    public void lambda$setIcon$0(ImageView imageView, QSTile.State state, boolean z) {
        Drawable drawable;
        Supplier<QSTile.Icon> supplier = state.iconSupplier;
        QSTile.Icon icon = supplier != null ? supplier.get() : state.icon;
        int i = R$id.qs_icon_tag;
        if (!Objects.equals(icon, imageView.getTag(i)) || !Objects.equals(state.slash, imageView.getTag(R$id.qs_slash_tag))) {
            boolean z2 = z && shouldAnimate(imageView);
            this.mLastIcon = icon;
            if (icon != null) {
                drawable = z2 ? icon.getDrawable(this.mContext) : icon.getInvisibleDrawable(this.mContext);
            } else {
                drawable = null;
            }
            int padding = icon != null ? icon.getPadding() : 0;
            if (drawable != null) {
                if (drawable.getConstantState() != null) {
                    drawable = drawable.getConstantState().newDrawable();
                }
                drawable.setAutoMirrored(false);
                drawable.setLayoutDirection(getLayoutDirection());
            }
            if (imageView instanceof SlashImageView) {
                SlashImageView slashImageView = (SlashImageView) imageView;
                slashImageView.setAnimationEnabled(z2);
                slashImageView.setState((QSTile.SlashState) null, drawable);
            } else {
                imageView.setImageDrawable(drawable);
            }
            imageView.setTag(i, icon);
            imageView.setTag(R$id.qs_slash_tag, state.slash);
            imageView.setPadding(0, padding, 0, padding);
            if (drawable instanceof Animatable2) {
                final Animatable2 animatable2 = (Animatable2) drawable;
                animatable2.start();
                if (state.isTransient) {
                    animatable2.registerAnimationCallback(new Animatable2.AnimationCallback() {
                        public void onAnimationEnd(Drawable drawable) {
                            animatable2.start();
                        }
                    });
                }
            }
        }
    }

    public final boolean shouldAnimate(ImageView imageView) {
        return this.mAnimationEnabled && imageView.isShown() && imageView.getDrawable() != null;
    }

    public void setIcon(ImageView imageView, QSTile.State state, boolean z) {
        if (state.disabledByPolicy) {
            imageView.setColorFilter(getContext().getColor(R$color.qs_tile_disabled_color));
        } else {
            imageView.clearColorFilter();
        }
        int i = state.state;
        if (i != this.mState) {
            int color = getColor(i);
            this.mState = state.state;
            if (this.mTint == 0 || !z || !shouldAnimate(imageView)) {
                if (imageView instanceof AlphaControlledSignalTileView.AlphaControlledSlashImageView) {
                    ((AlphaControlledSignalTileView.AlphaControlledSlashImageView) imageView).setFinalImageTintList(ColorStateList.valueOf(color));
                } else {
                    setTint(imageView, color);
                }
                lambda$setIcon$0(imageView, state, z);
                return;
            }
            animateGrayScale(this.mTint, color, imageView, new QSIconViewImpl$$ExternalSyntheticLambda0(this, imageView, state, z));
            return;
        }
        lambda$setIcon$0(imageView, state, z);
    }

    public int getColor(int i) {
        return getIconColorForState(getContext(), i);
    }

    public final void animateGrayScale(int i, int i2, ImageView imageView, Runnable runnable) {
        if (imageView instanceof AlphaControlledSignalTileView.AlphaControlledSlashImageView) {
            ((AlphaControlledSignalTileView.AlphaControlledSlashImageView) imageView).setFinalImageTintList(ColorStateList.valueOf(i2));
        }
        this.mColorAnimator.cancel();
        if (!this.mAnimationEnabled || !ValueAnimator.areAnimatorsEnabled()) {
            setTint(imageView, i2);
            runnable.run();
            return;
        }
        PropertyValuesHolder ofInt = PropertyValuesHolder.ofInt("color", new int[]{i, i2});
        ofInt.setEvaluator(ArgbEvaluator.getInstance());
        this.mColorAnimator.setValues(new PropertyValuesHolder[]{ofInt});
        this.mColorAnimator.removeAllListeners();
        this.mColorAnimator.addUpdateListener(new QSIconViewImpl$$ExternalSyntheticLambda1(this, imageView));
        this.mColorAnimator.addListener(new EndRunnableAnimatorListener(runnable));
        this.mColorAnimator.start();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$animateGrayScale$1(ImageView imageView, ValueAnimator valueAnimator) {
        setTint(imageView, ((Integer) valueAnimator.getAnimatedValue()).intValue());
    }

    public void setTint(ImageView imageView, int i) {
        imageView.setImageTintList(ColorStateList.valueOf(i));
        this.mTint = i;
    }

    public View createIcon() {
        SlashImageView slashImageView = new SlashImageView(this.mContext);
        slashImageView.setId(16908294);
        slashImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        return slashImageView;
    }

    public final int exactly(int i) {
        return View.MeasureSpec.makeMeasureSpec(i, 1073741824);
    }

    public final void layout(View view, int i, int i2) {
        view.layout(i, i2, view.getMeasuredWidth() + i, view.getMeasuredHeight() + i2);
    }

    public static int getIconColorForState(Context context, int i) {
        if (i == 0) {
            return Utils.applyAlpha(0.3f, Utils.getColorAttrDefaultColor(context, 16842806));
        }
        if (i == 1) {
            return Utils.getColorAttrDefaultColor(context, 16842806);
        }
        if (i == 2) {
            return Utils.getColorAttrDefaultColor(context, 17957103);
        }
        Log.e("QSIconView", "Invalid state " + i);
        return 0;
    }

    public static class EndRunnableAnimatorListener extends AnimatorListenerAdapter {
        public Runnable mRunnable;

        public EndRunnableAnimatorListener(Runnable runnable) {
            this.mRunnable = runnable;
        }

        public void onAnimationCancel(Animator animator) {
            super.onAnimationCancel(animator);
            this.mRunnable.run();
        }

        public void onAnimationEnd(Animator animator) {
            super.onAnimationEnd(animator);
            this.mRunnable.run();
        }
    }
}
