package com.android.systemui.statusbar.notification.row;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import com.android.systemui.R$bool;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.statusbar.notification.AnimatableProperty;
import com.android.systemui.statusbar.notification.PropertyAnimator;
import com.android.systemui.statusbar.notification.stack.AnimationProperties;

public abstract class ExpandableOutlineView extends ExpandableView {
    public static final AnimatableProperty BOTTOM_ROUNDNESS = AnimatableProperty.from("bottomRoundness", new ExpandableOutlineView$$ExternalSyntheticLambda2(), new ExpandableOutlineView$$ExternalSyntheticLambda3(), R$id.bottom_roundess_animator_tag, R$id.bottom_roundess_animator_end_tag, R$id.bottom_roundess_animator_start_tag);
    public static final Path EMPTY_PATH = new Path();
    public static final AnimationProperties ROUNDNESS_PROPERTIES = new AnimationProperties().setDuration(200);
    public static final AnimatableProperty TOP_ROUNDNESS = AnimatableProperty.from("topRoundness", new ExpandableOutlineView$$ExternalSyntheticLambda0(), new ExpandableOutlineView$$ExternalSyntheticLambda1(), R$id.top_roundess_animator_tag, R$id.top_roundess_animator_end_tag, R$id.top_roundess_animator_start_tag);
    public boolean mAlwaysRoundBothCorners;
    public int mBackgroundTop;
    public float mBottomRoundness;
    public final Path mClipPath = new Path();
    public float mCurrentBottomRoundness;
    public float mCurrentTopRoundness;
    public boolean mCustomOutline;
    public boolean mDismissUsingRowTranslationX = true;
    public float mOutlineAlpha = -1.0f;
    public float mOutlineRadius;
    public final Rect mOutlineRect = new Rect();
    public final ViewOutlineProvider mProvider;
    public float[] mTmpCornerRadii = new float[8];
    public Path mTmpPath = new Path();
    public float mTopRoundness;

    public boolean childNeedsClipping(View view) {
        return false;
    }

    public Path getCustomClipPath(View view) {
        return null;
    }

    public Path getClipPath(boolean z) {
        int i;
        int i2;
        int i3;
        int i4;
        float currentBackgroundRadiusTop = this.mAlwaysRoundBothCorners ? this.mOutlineRadius : getCurrentBackgroundRadiusTop();
        if (!this.mCustomOutline) {
            int translation = (this.mDismissUsingRowTranslationX || z) ? 0 : (int) getTranslation();
            int i5 = (int) (this.mExtraWidthForClipping / 2.0f);
            i4 = Math.max(translation, 0) - i5;
            i3 = this.mClipTopAmount + this.mBackgroundTop;
            i2 = getWidth() + i5 + Math.min(translation, 0);
            i = Math.max(this.mMinimumHeightForClipping, Math.max(getActualHeight() - this.mClipBottomAmount, (int) (((float) i3) + currentBackgroundRadiusTop)));
        } else {
            Rect rect = this.mOutlineRect;
            i4 = rect.left;
            i3 = rect.top;
            i2 = rect.right;
            i = rect.bottom;
        }
        int i6 = i;
        int i7 = i4;
        int i8 = i3;
        int i9 = i2;
        int i10 = i6 - i8;
        if (i10 == 0) {
            return EMPTY_PATH;
        }
        float currentBackgroundRadiusBottom = this.mAlwaysRoundBothCorners ? this.mOutlineRadius : getCurrentBackgroundRadiusBottom();
        float f = currentBackgroundRadiusTop + currentBackgroundRadiusBottom;
        float f2 = (float) i10;
        if (f > f2) {
            float f3 = f - f2;
            float currentTopRoundness = getCurrentTopRoundness();
            float currentBottomRoundness = getCurrentBottomRoundness();
            float f4 = f3 * currentTopRoundness;
            float f5 = currentTopRoundness + currentBottomRoundness;
            currentBackgroundRadiusTop -= f4 / f5;
            currentBackgroundRadiusBottom -= (f3 * currentBottomRoundness) / f5;
        }
        getRoundedRectPath(i7, i8, i9, i6, currentBackgroundRadiusTop, currentBackgroundRadiusBottom, this.mTmpPath);
        return this.mTmpPath;
    }

    public void getRoundedRectPath(int i, int i2, int i3, int i4, float f, float f2, Path path) {
        path.reset();
        float[] fArr = this.mTmpCornerRadii;
        fArr[0] = f;
        fArr[1] = f;
        fArr[2] = f;
        fArr[3] = f;
        fArr[4] = f2;
        fArr[5] = f2;
        fArr[6] = f2;
        fArr[7] = f2;
        path.addRoundRect((float) i, (float) i2, (float) i3, (float) i4, fArr, Path.Direction.CW);
    }

    public ExpandableOutlineView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        AnonymousClass1 r1 = new ViewOutlineProvider() {
            public void getOutline(View view, Outline outline) {
                if (ExpandableOutlineView.this.mCustomOutline || ExpandableOutlineView.this.getCurrentTopRoundness() != 0.0f || ExpandableOutlineView.this.getCurrentBottomRoundness() != 0.0f || ExpandableOutlineView.this.mAlwaysRoundBothCorners) {
                    Path clipPath = ExpandableOutlineView.this.getClipPath(false);
                    if (clipPath != null) {
                        outline.setPath(clipPath);
                    }
                } else {
                    ExpandableOutlineView expandableOutlineView = ExpandableOutlineView.this;
                    int translation = !expandableOutlineView.mDismissUsingRowTranslationX ? (int) expandableOutlineView.getTranslation() : 0;
                    int max = Math.max(translation, 0);
                    ExpandableOutlineView expandableOutlineView2 = ExpandableOutlineView.this;
                    int r3 = expandableOutlineView2.mClipTopAmount + expandableOutlineView2.mBackgroundTop;
                    outline.setRect(max, r3, ExpandableOutlineView.this.getWidth() + Math.min(translation, 0), Math.max(ExpandableOutlineView.this.getActualHeight() - ExpandableOutlineView.this.mClipBottomAmount, r3));
                }
                outline.setAlpha(ExpandableOutlineView.this.mOutlineAlpha);
            }
        };
        this.mProvider = r1;
        setOutlineProvider(r1);
        initDimens();
    }

    public boolean drawChild(Canvas canvas, View view, long j) {
        canvas.save();
        if (childNeedsClipping(view)) {
            Path customClipPath = getCustomClipPath(view);
            if (customClipPath == null) {
                customClipPath = getClipPath(false);
            }
            if (customClipPath != null) {
                canvas.clipPath(customClipPath);
            }
        }
        boolean drawChild = super.drawChild(canvas, view, j);
        canvas.restore();
        return drawChild;
    }

    public void setExtraWidthForClipping(float f) {
        super.setExtraWidthForClipping(f);
        invalidate();
    }

    public void setMinimumHeightForClipping(int i) {
        super.setMinimumHeightForClipping(i);
        invalidate();
    }

    public boolean isClippingNeeded() {
        boolean z = getTranslation() != 0.0f && !this.mDismissUsingRowTranslationX;
        if (this.mAlwaysRoundBothCorners || this.mCustomOutline || z) {
            return true;
        }
        return false;
    }

    private void initDimens() {
        Resources resources = getResources();
        this.mOutlineRadius = resources.getDimension(R$dimen.notification_shadow_radius);
        boolean z = resources.getBoolean(R$bool.config_clipNotificationsToOutline);
        this.mAlwaysRoundBothCorners = z;
        if (!z) {
            this.mOutlineRadius = (float) resources.getDimensionPixelSize(R$dimen.notification_corner_radius);
        }
        setClipToOutline(this.mAlwaysRoundBothCorners);
    }

    public boolean setTopRoundness(float f, boolean z) {
        float f2 = this.mTopRoundness;
        if (f2 == f) {
            return false;
        }
        float abs = Math.abs(f - f2);
        this.mTopRoundness = f;
        AnimatableProperty animatableProperty = TOP_ROUNDNESS;
        if (PropertyAnimator.isAnimating(this, animatableProperty) && abs > 0.5f) {
            z = true;
        }
        PropertyAnimator.setProperty(this, animatableProperty, f, ROUNDNESS_PROPERTIES, z);
        return true;
    }

    public void applyRoundness() {
        invalidateOutline();
        invalidate();
    }

    public float getCurrentBackgroundRadiusTop() {
        return getCurrentTopRoundness() * this.mOutlineRadius;
    }

    public float getCurrentTopRoundness() {
        return this.mCurrentTopRoundness;
    }

    public float getCurrentBottomRoundness() {
        return this.mCurrentBottomRoundness;
    }

    public float getCurrentBackgroundRadiusBottom() {
        return getCurrentBottomRoundness() * this.mOutlineRadius;
    }

    public boolean setBottomRoundness(float f, boolean z) {
        float f2 = this.mBottomRoundness;
        if (f2 == f) {
            return false;
        }
        float abs = Math.abs(f - f2);
        this.mBottomRoundness = f;
        AnimatableProperty animatableProperty = BOTTOM_ROUNDNESS;
        if (PropertyAnimator.isAnimating(this, animatableProperty) && abs > 0.5f) {
            z = true;
        }
        PropertyAnimator.setProperty(this, animatableProperty, f, ROUNDNESS_PROPERTIES, z);
        return true;
    }

    public final void setTopRoundnessInternal(float f) {
        this.mCurrentTopRoundness = f;
        applyRoundness();
    }

    public final void setBottomRoundnessInternal(float f) {
        this.mCurrentBottomRoundness = f;
        applyRoundness();
    }

    public void onDensityOrFontScaleChanged() {
        initDimens();
        applyRoundness();
    }

    public void setActualHeight(int i, boolean z) {
        int actualHeight = getActualHeight();
        super.setActualHeight(i, z);
        if (actualHeight != i) {
            applyRoundness();
        }
    }

    public void setClipTopAmount(int i) {
        int clipTopAmount = getClipTopAmount();
        super.setClipTopAmount(i);
        if (clipTopAmount != i) {
            applyRoundness();
        }
    }

    public void setClipBottomAmount(int i) {
        int clipBottomAmount = getClipBottomAmount();
        super.setClipBottomAmount(i);
        if (clipBottomAmount != i) {
            applyRoundness();
        }
    }

    public void setOutlineAlpha(float f) {
        if (f != this.mOutlineAlpha) {
            this.mOutlineAlpha = f;
            applyRoundness();
        }
    }

    public float getOutlineAlpha() {
        return this.mOutlineAlpha;
    }

    public void setOutlineRect(RectF rectF) {
        if (rectF != null) {
            setOutlineRect(rectF.left, rectF.top, rectF.right, rectF.bottom);
            return;
        }
        this.mCustomOutline = false;
        applyRoundness();
    }

    public void setDismissUsingRowTranslationX(boolean z) {
        this.mDismissUsingRowTranslationX = z;
    }

    public int getOutlineTranslation() {
        if (this.mCustomOutline) {
            return this.mOutlineRect.left;
        }
        if (this.mDismissUsingRowTranslationX) {
            return 0;
        }
        return (int) getTranslation();
    }

    public void updateOutline() {
        if (!this.mCustomOutline) {
            setOutlineProvider(needsOutline() ? this.mProvider : null);
        }
    }

    public boolean needsOutline() {
        if (isChildInGroup()) {
            if (!isGroupExpanded() || isGroupExpansionChanging()) {
                return false;
            }
            return true;
        } else if (!isSummaryWithChildren()) {
            return true;
        } else {
            if (!isGroupExpanded() || isGroupExpansionChanging()) {
                return true;
            }
            return false;
        }
    }

    public void setOutlineRect(float f, float f2, float f3, float f4) {
        this.mCustomOutline = true;
        this.mOutlineRect.set((int) f, (int) f2, (int) f3, (int) f4);
        Rect rect = this.mOutlineRect;
        rect.bottom = (int) Math.max(f2, (float) rect.bottom);
        Rect rect2 = this.mOutlineRect;
        rect2.right = (int) Math.max(f, (float) rect2.right);
        applyRoundness();
    }
}
