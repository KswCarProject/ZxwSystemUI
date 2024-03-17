package com.android.wm.shell.bubbles;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.PathParser;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.android.launcher3.icons.DotRenderer;
import com.android.launcher3.icons.IconNormalizer;
import com.android.wm.shell.R;
import com.android.wm.shell.animation.Interpolators;
import java.util.EnumSet;

public class BadgedImageView extends ConstraintLayout {
    public float mAnimatingToDotScale;
    public final ImageView mAppIcon;
    public BubbleViewProvider mBubble;
    public final ImageView mBubbleIcon;
    public int mDotColor;
    public boolean mDotIsAnimating;
    public DotRenderer mDotRenderer;
    public float mDotScale;
    public final EnumSet<SuppressionFlag> mDotSuppressionFlags;
    public DotRenderer.DrawParams mDrawParams;
    public boolean mOnLeft;
    public BubblePositioner mPositioner;
    public Rect mTempBounds;

    public enum SuppressionFlag {
        FLYOUT_VISIBLE,
        BEHIND_STACK
    }

    public BadgedImageView(Context context) {
        this(context, (AttributeSet) null);
    }

    public BadgedImageView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public BadgedImageView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public BadgedImageView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mDotSuppressionFlags = EnumSet.of(SuppressionFlag.FLYOUT_VISIBLE);
        this.mDotScale = 0.0f;
        this.mAnimatingToDotScale = 0.0f;
        this.mDotIsAnimating = false;
        this.mTempBounds = new Rect();
        setLayoutDirection(0);
        LayoutInflater.from(context).inflate(R.layout.badged_image_view, this);
        ImageView imageView = (ImageView) findViewById(R.id.icon_view);
        this.mBubbleIcon = imageView;
        this.mAppIcon = (ImageView) findViewById(R.id.app_icon_view);
        TypedArray obtainStyledAttributes = this.mContext.obtainStyledAttributes(attributeSet, new int[]{16843033}, i, i2);
        imageView.setImageResource(obtainStyledAttributes.getResourceId(0, 0));
        obtainStyledAttributes.recycle();
        this.mDrawParams = new DotRenderer.DrawParams();
        setFocusable(true);
        setClickable(true);
        setOutlineProvider(new ViewOutlineProvider() {
            public void getOutline(View view, Outline outline) {
                BadgedImageView.this.getOutline(outline);
            }
        });
    }

    public final void getOutline(Outline outline) {
        int bubbleSize = this.mPositioner.getBubbleSize();
        int normalizedCircleSize = IconNormalizer.getNormalizedCircleSize(bubbleSize);
        int i = (bubbleSize - normalizedCircleSize) / 2;
        int i2 = normalizedCircleSize + i;
        outline.setOval(i, i, i2, i2);
    }

    public void initialize(BubblePositioner bubblePositioner) {
        this.mPositioner = bubblePositioner;
        this.mDotRenderer = new DotRenderer(this.mPositioner.getBubbleSize(), PathParser.createPathFromPathData(getResources().getString(17039989)), 100);
    }

    public void showDotAndBadge(boolean z) {
        removeDotSuppressionFlag(SuppressionFlag.BEHIND_STACK);
        animateDotBadgePositions(z);
    }

    public void hideDotAndBadge(boolean z) {
        addDotSuppressionFlag(SuppressionFlag.BEHIND_STACK);
        this.mOnLeft = z;
        hideBadge();
    }

    public void setRenderedBubble(BubbleViewProvider bubbleViewProvider) {
        this.mBubble = bubbleViewProvider;
        this.mBubbleIcon.setImageBitmap(bubbleViewProvider.getBubbleIcon());
        this.mAppIcon.setImageBitmap(bubbleViewProvider.getAppBadge());
        if (this.mDotSuppressionFlags.contains(SuppressionFlag.BEHIND_STACK)) {
            hideBadge();
        } else {
            showBadge();
        }
        this.mDotColor = bubbleViewProvider.getDotColor();
        drawDot(bubbleViewProvider.getDotPath());
    }

    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (shouldDrawDot()) {
            getDrawingRect(this.mTempBounds);
            DotRenderer.DrawParams drawParams = this.mDrawParams;
            drawParams.dotColor = this.mDotColor;
            drawParams.iconBounds = this.mTempBounds;
            drawParams.leftAlign = this.mOnLeft;
            drawParams.scale = this.mDotScale;
            this.mDotRenderer.draw(canvas, drawParams);
        }
    }

    public void setIconImageResource(int i) {
        this.mBubbleIcon.setImageResource(i);
    }

    public Drawable getIconDrawable() {
        return this.mBubbleIcon.getDrawable();
    }

    public void addDotSuppressionFlag(SuppressionFlag suppressionFlag) {
        if (this.mDotSuppressionFlags.add(suppressionFlag)) {
            updateDotVisibility(suppressionFlag == SuppressionFlag.BEHIND_STACK);
        }
    }

    public void removeDotSuppressionFlag(SuppressionFlag suppressionFlag) {
        if (this.mDotSuppressionFlags.remove(suppressionFlag)) {
            updateDotVisibility(suppressionFlag == SuppressionFlag.BEHIND_STACK);
        }
    }

    public void updateDotVisibility(boolean z) {
        float f = shouldDrawDot() ? 1.0f : 0.0f;
        if (z) {
            animateDotScale(f, (Runnable) null);
            return;
        }
        this.mDotScale = f;
        this.mAnimatingToDotScale = f;
        invalidate();
    }

    public void drawDot(Path path) {
        this.mDotRenderer = new DotRenderer(this.mPositioner.getBubbleSize(), path, 100);
        invalidate();
    }

    public void setDotScale(float f) {
        this.mDotScale = f;
        invalidate();
    }

    public boolean getDotOnLeft() {
        return this.mOnLeft;
    }

    public float[] getDotCenter() {
        float[] fArr;
        if (this.mOnLeft) {
            fArr = this.mDotRenderer.getLeftDotPosition();
        } else {
            fArr = this.mDotRenderer.getRightDotPosition();
        }
        getDrawingRect(this.mTempBounds);
        return new float[]{((float) this.mTempBounds.width()) * fArr[0], ((float) this.mTempBounds.height()) * fArr[1]};
    }

    public String getKey() {
        BubbleViewProvider bubbleViewProvider = this.mBubble;
        if (bubbleViewProvider != null) {
            return bubbleViewProvider.getKey();
        }
        return null;
    }

    public int getDotColor() {
        return this.mDotColor;
    }

    public void animateDotBadgePositions(boolean z) {
        this.mOnLeft = z;
        if (z != getDotOnLeft() && shouldDrawDot()) {
            animateDotScale(0.0f, new BadgedImageView$$ExternalSyntheticLambda2(this));
        }
        showBadge();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$animateDotBadgePositions$0() {
        invalidate();
        animateDotScale(1.0f, (Runnable) null);
    }

    public void setDotBadgeOnLeft(boolean z) {
        this.mOnLeft = z;
        invalidate();
        showBadge();
    }

    public final boolean shouldDrawDot() {
        return this.mDotIsAnimating || (this.mBubble.showDot() && this.mDotSuppressionFlags.isEmpty());
    }

    public final void animateDotScale(float f, Runnable runnable) {
        boolean z = true;
        this.mDotIsAnimating = true;
        if (this.mAnimatingToDotScale == f || !shouldDrawDot()) {
            this.mDotIsAnimating = false;
            return;
        }
        this.mAnimatingToDotScale = f;
        if (f <= 0.0f) {
            z = false;
        }
        clearAnimation();
        animate().setDuration(200).setInterpolator(Interpolators.FAST_OUT_SLOW_IN).setUpdateListener(new BadgedImageView$$ExternalSyntheticLambda0(this, z)).withEndAction(new BadgedImageView$$ExternalSyntheticLambda1(this, z, runnable)).start();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$animateDotScale$1(boolean z, ValueAnimator valueAnimator) {
        float animatedFraction = valueAnimator.getAnimatedFraction();
        if (!z) {
            animatedFraction = 1.0f - animatedFraction;
        }
        setDotScale(animatedFraction);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$animateDotScale$2(boolean z, Runnable runnable) {
        setDotScale(z ? 1.0f : 0.0f);
        this.mDotIsAnimating = false;
        if (runnable != null) {
            runnable.run();
        }
    }

    public void showBadge() {
        Bitmap appBadge = this.mBubble.getAppBadge();
        if (appBadge == null) {
            this.mAppIcon.setVisibility(8);
            return;
        }
        this.mAppIcon.setTranslationX((float) (this.mOnLeft ? -(this.mBubble.getBubbleIcon().getWidth() - appBadge.getWidth()) : 0));
        this.mAppIcon.setVisibility(0);
    }

    public void hideBadge() {
        this.mAppIcon.setVisibility(8);
    }

    public String toString() {
        return "BadgedImageView{" + this.mBubble + "}";
    }
}
