package com.android.wm.shell.bubbles;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Insets;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowMetrics;
import com.android.launcher3.icons.IconNormalizer;
import com.android.wm.shell.R;
import com.android.wm.shell.bubbles.BubbleStackView;

public class BubblePositioner {
    public int mBubbleOffscreenAmount;
    public int mBubblePaddingTop;
    public int mBubbleSize;
    public Context mContext;
    public int mDefaultMaxBubbles;
    public int mExpandedViewLargeScreenInsetClosestEdge;
    public int mExpandedViewLargeScreenInsetFurthestEdge;
    public int mExpandedViewLargeScreenWidth;
    public int mExpandedViewMinHeight;
    public int mExpandedViewPadding;
    public int mImeHeight;
    public boolean mImeVisible;
    public Insets mInsets;
    public boolean mIsLargeScreen;
    public boolean mIsSmallTablet;
    public int mManageButtonHeight;
    public int mMaxBubbles;
    public int mMinimumFlyoutWidthLargeScreen;
    public int mOverflowHeight;
    public int mOverflowWidth;
    public int[] mPaddings = new int[4];
    public PointF mPinLocation;
    public int mPointerHeight;
    public int mPointerMargin;
    public int mPointerOverlap;
    public int mPointerWidth;
    public Rect mPositionRect;
    public PointF mRestingStackPosition;
    public int mRotation = 0;
    public Rect mScreenRect;
    public boolean mShowingInTaskbar;
    public int mSpacingBetweenBubbles;
    public int mStackOffset;
    public int mTaskbarIconSize;
    public int mTaskbarPosition = -1;
    public WindowManager mWindowManager;

    public BubblePositioner(Context context, WindowManager windowManager) {
        this.mContext = context;
        this.mWindowManager = windowManager;
        update();
    }

    public void update() {
        WindowMetrics currentWindowMetrics = this.mWindowManager.getCurrentWindowMetrics();
        if (currentWindowMetrics != null) {
            Insets insetsIgnoringVisibility = currentWindowMetrics.getWindowInsets().getInsetsIgnoringVisibility(WindowInsets.Type.navigationBars() | WindowInsets.Type.statusBars() | WindowInsets.Type.displayCutout());
            Rect bounds = currentWindowMetrics.getBounds();
            Configuration configuration = this.mContext.getResources().getConfiguration();
            boolean z = true;
            boolean z2 = configuration.smallestScreenWidthDp >= 600;
            this.mIsLargeScreen = z2;
            if (z2) {
                if (((float) Math.max(configuration.screenWidthDp, configuration.screenHeightDp)) >= 960.0f) {
                    z = false;
                }
                this.mIsSmallTablet = z;
            } else {
                this.mIsSmallTablet = false;
            }
            updateInternal(this.mRotation, insetsIgnoringVisibility, bounds);
        }
    }

    public void updateInternal(int i, Insets insets, Rect rect) {
        float f;
        float f2;
        this.mRotation = i;
        this.mInsets = insets;
        this.mScreenRect = new Rect(rect);
        Rect rect2 = new Rect(rect);
        this.mPositionRect = rect2;
        int i2 = rect2.left;
        Insets insets2 = this.mInsets;
        rect2.left = i2 + insets2.left;
        rect2.top += insets2.top;
        rect2.right -= insets2.right;
        rect2.bottom -= insets2.bottom;
        Resources resources = this.mContext.getResources();
        this.mBubbleSize = resources.getDimensionPixelSize(R.dimen.bubble_size);
        this.mSpacingBetweenBubbles = resources.getDimensionPixelSize(R.dimen.bubble_spacing);
        this.mDefaultMaxBubbles = resources.getInteger(R.integer.bubbles_max_rendered);
        this.mExpandedViewPadding = resources.getDimensionPixelSize(R.dimen.bubble_expanded_view_padding);
        this.mBubblePaddingTop = resources.getDimensionPixelSize(R.dimen.bubble_padding_top);
        this.mBubbleOffscreenAmount = resources.getDimensionPixelSize(R.dimen.bubble_stack_offscreen);
        this.mStackOffset = resources.getDimensionPixelSize(R.dimen.bubble_stack_offset);
        if (this.mIsSmallTablet) {
            this.mExpandedViewLargeScreenWidth = (int) (((float) rect.width()) * 0.72f);
        } else {
            if (isLandscape()) {
                f = (float) rect.width();
                f2 = 0.48f;
            } else {
                f = (float) rect.width();
                f2 = 0.7f;
            }
            this.mExpandedViewLargeScreenWidth = (int) (f * f2);
        }
        if (!this.mIsLargeScreen) {
            int i3 = this.mExpandedViewPadding;
            this.mExpandedViewLargeScreenInsetClosestEdge = i3;
            this.mExpandedViewLargeScreenInsetFurthestEdge = i3;
        } else if (!isLandscape() || this.mIsSmallTablet) {
            int width = (rect.width() - this.mExpandedViewLargeScreenWidth) / 2;
            this.mExpandedViewLargeScreenInsetClosestEdge = width;
            this.mExpandedViewLargeScreenInsetFurthestEdge = width;
        } else {
            this.mExpandedViewLargeScreenInsetClosestEdge = resources.getDimensionPixelSize(R.dimen.bubble_expanded_view_largescreen_landscape_padding);
            this.mExpandedViewLargeScreenInsetFurthestEdge = (rect.width() - this.mExpandedViewLargeScreenInsetClosestEdge) - this.mExpandedViewLargeScreenWidth;
        }
        this.mOverflowWidth = resources.getDimensionPixelSize(R.dimen.bubble_expanded_view_overflow_width);
        this.mPointerWidth = resources.getDimensionPixelSize(R.dimen.bubble_pointer_width);
        this.mPointerHeight = resources.getDimensionPixelSize(R.dimen.bubble_pointer_height);
        this.mPointerMargin = resources.getDimensionPixelSize(R.dimen.bubble_pointer_margin);
        this.mPointerOverlap = resources.getDimensionPixelSize(R.dimen.bubble_pointer_overlap);
        this.mManageButtonHeight = resources.getDimensionPixelSize(R.dimen.bubble_manage_button_total_height);
        this.mExpandedViewMinHeight = resources.getDimensionPixelSize(R.dimen.bubble_expanded_default_height);
        this.mOverflowHeight = resources.getDimensionPixelSize(R.dimen.bubble_overflow_height);
        this.mMinimumFlyoutWidthLargeScreen = resources.getDimensionPixelSize(R.dimen.bubbles_flyout_min_width_large_screen);
        this.mMaxBubbles = calculateMaxBubbles();
        if (this.mShowingInTaskbar) {
            adjustForTaskbar();
        }
    }

    public final int calculateMaxBubbles() {
        int min = Math.min(this.mPositionRect.width(), this.mPositionRect.height()) - (showBubblesVertically() ? 0 : this.mExpandedViewPadding * 2);
        int i = this.mBubbleSize;
        int i2 = (min - i) / (i + this.mSpacingBetweenBubbles);
        int i3 = this.mDefaultMaxBubbles;
        return i2 < i3 ? i2 : i3;
    }

    public final void adjustForTaskbar() {
        if (this.mShowingInTaskbar && this.mTaskbarPosition != 2) {
            Insets insetsIgnoringVisibility = this.mWindowManager.getCurrentWindowMetrics().getWindowInsets().getInsetsIgnoringVisibility(WindowInsets.Type.navigationBars());
            Insets insets = this.mInsets;
            int i = insets.left;
            int i2 = insets.right;
            int i3 = this.mTaskbarPosition;
            if (i3 == 1) {
                Rect rect = this.mPositionRect;
                int i4 = rect.left;
                int i5 = insetsIgnoringVisibility.left;
                rect.left = i4 - i5;
                i -= i5;
            } else if (i3 == 0) {
                Rect rect2 = this.mPositionRect;
                int i6 = rect2.right;
                int i7 = insetsIgnoringVisibility.right;
                rect2.right = i6 + i7;
                i2 -= i7;
            }
            this.mInsets = Insets.of(i, insets.top, i2, insets.bottom);
        }
    }

    public Rect getAvailableRect() {
        return this.mPositionRect;
    }

    public Rect getScreenRect() {
        return this.mScreenRect;
    }

    public Insets getInsets() {
        return this.mInsets;
    }

    public boolean isLandscape() {
        return this.mContext.getResources().getConfiguration().orientation == 2;
    }

    public boolean isLargeScreen() {
        return this.mIsLargeScreen;
    }

    public boolean showBubblesVertically() {
        return isLandscape() || this.mShowingInTaskbar || this.mIsLargeScreen;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0004, code lost:
        r0 = r1.mTaskbarIconSize;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getBubbleSize() {
        /*
            r1 = this;
            boolean r0 = r1.mShowingInTaskbar
            if (r0 == 0) goto L_0x0009
            int r0 = r1.mTaskbarIconSize
            if (r0 <= 0) goto L_0x0009
            goto L_0x000b
        L_0x0009:
            int r0 = r1.mBubbleSize
        L_0x000b:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.bubbles.BubblePositioner.getBubbleSize():int");
    }

    public int getBubblePaddingTop() {
        return this.mBubblePaddingTop;
    }

    public int getStackOffset() {
        return this.mStackOffset;
    }

    public int getPointerSize() {
        return this.mPointerHeight - this.mPointerOverlap;
    }

    public int getMaxBubbles() {
        return this.mMaxBubbles;
    }

    public int getImeHeight() {
        if (this.mImeVisible) {
            return this.mImeHeight;
        }
        return 0;
    }

    public void setImeVisible(boolean z, int i) {
        this.mImeVisible = z;
        this.mImeHeight = i;
    }

    public final int getExpandedViewLargeScreenInsetFurthestEdge(boolean z) {
        if (!z || !this.mIsLargeScreen) {
            return this.mExpandedViewLargeScreenInsetFurthestEdge;
        }
        return (this.mScreenRect.width() - this.mExpandedViewLargeScreenInsetClosestEdge) - this.mOverflowWidth;
    }

    public int[] getExpandedViewContainerPadding(boolean z, boolean z2) {
        int i;
        int pointerSize = getPointerSize();
        int expandedViewLargeScreenInsetFurthestEdge = getExpandedViewLargeScreenInsetFurthestEdge(z2);
        int i2 = 0;
        if (this.mIsLargeScreen) {
            int[] iArr = this.mPaddings;
            iArr[0] = z ? this.mExpandedViewLargeScreenInsetClosestEdge - pointerSize : expandedViewLargeScreenInsetFurthestEdge;
            iArr[1] = 0;
            if (!z) {
                expandedViewLargeScreenInsetFurthestEdge = this.mExpandedViewLargeScreenInsetClosestEdge - pointerSize;
            }
            iArr[2] = expandedViewLargeScreenInsetFurthestEdge;
            if (z2) {
                i2 = this.mExpandedViewPadding;
            }
            iArr[3] = i2;
            return iArr;
        }
        Insets insets = this.mInsets;
        int i3 = insets.left;
        int i4 = this.mExpandedViewPadding;
        int i5 = i3 + i4;
        int i6 = insets.right + i4;
        if (z2) {
            i = this.mOverflowWidth;
        } else {
            i = this.mExpandedViewLargeScreenWidth;
        }
        float f = (float) i;
        if (showBubblesVertically()) {
            float f2 = 0.0f;
            if (!z) {
                i6 += this.mBubbleSize - pointerSize;
                float f3 = (float) i5;
                if (z2) {
                    f2 = ((float) (this.mPositionRect.width() - i6)) - f;
                }
                i5 = (int) (f3 + f2);
            } else {
                i5 += this.mBubbleSize - pointerSize;
                float f4 = (float) i6;
                if (z2) {
                    f2 = ((float) (this.mPositionRect.width() - i5)) - f;
                }
                i6 = (int) (f4 + f2);
            }
        }
        int[] iArr2 = this.mPaddings;
        iArr2[0] = i5;
        iArr2[1] = showBubblesVertically() ? 0 : this.mPointerMargin;
        int[] iArr3 = this.mPaddings;
        iArr3[2] = i6;
        iArr3[3] = 0;
        return iArr3;
    }

    public float getExpandedViewYTopAligned() {
        int i;
        int i2;
        int i3 = getAvailableRect().top;
        if (showBubblesVertically()) {
            i = i3 - this.mPointerWidth;
            i2 = this.mExpandedViewPadding;
        } else {
            i = i3 + this.mBubbleSize;
            i2 = this.mPointerMargin;
        }
        return (float) (i + i2);
    }

    public int getMaxExpandedViewHeight(boolean z) {
        int i;
        int i2;
        int expandedViewYTopAligned = ((int) getExpandedViewYTopAligned()) - getInsets().top;
        if (showBubblesVertically()) {
            i = 0;
        } else {
            i = this.mPointerHeight;
        }
        if (showBubblesVertically()) {
            i2 = this.mPointerWidth;
        } else {
            i2 = this.mPointerHeight + this.mPointerMargin;
        }
        return (((getAvailableRect().height() - expandedViewYTopAligned) - i) - i2) - (z ? this.mExpandedViewPadding : this.mManageButtonHeight);
    }

    public float getExpandedViewHeight(BubbleViewProvider bubbleViewProvider) {
        float f;
        boolean z = bubbleViewProvider == null || "Overflow".equals(bubbleViewProvider.getKey());
        if (z && showBubblesVertically() && !this.mIsLargeScreen) {
            return -1.0f;
        }
        if (z) {
            f = (float) this.mOverflowHeight;
        } else {
            f = ((Bubble) bubbleViewProvider).getDesiredHeight(this.mContext);
        }
        float max = Math.max(f, (float) this.mExpandedViewMinHeight);
        if (max > ((float) getMaxExpandedViewHeight(z))) {
            return -1.0f;
        }
        return max;
    }

    public float getExpandedViewY(BubbleViewProvider bubbleViewProvider, float f) {
        boolean z = bubbleViewProvider == null || "Overflow".equals(bubbleViewProvider.getKey());
        float expandedViewHeight = getExpandedViewHeight(bubbleViewProvider);
        float expandedViewYTopAligned = getExpandedViewYTopAligned();
        if (!showBubblesVertically() || expandedViewHeight == -1.0f) {
            return expandedViewYTopAligned;
        }
        int i = z ? this.mExpandedViewPadding : this.mManageButtonHeight;
        float pointerPosition = getPointerPosition(f);
        float f2 = expandedViewHeight / 2.0f;
        float f3 = pointerPosition + f2 + ((float) i);
        float f4 = pointerPosition - f2;
        Rect rect = this.mPositionRect;
        int i2 = rect.top;
        if (f4 > ((float) i2) && ((float) rect.bottom) > f3) {
            return (pointerPosition - ((float) this.mPointerWidth)) - f2;
        }
        if (f4 <= ((float) i2)) {
            return expandedViewYTopAligned;
        }
        return (((float) (rect.bottom - i)) - expandedViewHeight) - ((float) this.mPointerWidth);
    }

    public float getPointerPosition(float f) {
        float normalizedCircleSize = (float) IconNormalizer.getNormalizedCircleSize(getBubbleSize());
        if (showBubblesVertically()) {
            return f + (((float) getBubbleSize()) / 2.0f);
        }
        return (f + (normalizedCircleSize / 2.0f)) - ((float) this.mPointerWidth);
    }

    public final int getExpandedStackSize(int i) {
        return (this.mBubbleSize * i) + ((i - 1) * this.mSpacingBetweenBubbles);
    }

    public PointF getExpandedBubbleXY(int i, BubbleStackView.StackViewState stackViewState) {
        int i2;
        float f;
        float f2;
        int i3;
        int i4;
        float f3 = (float) ((this.mBubbleSize + this.mSpacingBetweenBubbles) * i);
        float expandedStackSize = (float) getExpandedStackSize(stackViewState.numberOfBubbles);
        if (showBubblesVertically()) {
            i2 = this.mPositionRect.centerY();
        } else {
            i2 = this.mPositionRect.centerX();
        }
        float f4 = ((float) i2) - (expandedStackSize / 2.0f);
        if (showBubblesVertically()) {
            int i5 = this.mExpandedViewLargeScreenInsetClosestEdge;
            f = f4 + f3;
            boolean z = this.mIsLargeScreen;
            if (z) {
                i3 = (i5 - this.mExpandedViewPadding) - this.mBubbleSize;
            } else {
                i3 = this.mPositionRect.left;
            }
            if (z) {
                i4 = (this.mPositionRect.right - i5) + this.mExpandedViewPadding;
            } else {
                i4 = this.mPositionRect.right - this.mBubbleSize;
            }
            f2 = stackViewState.onLeft ? (float) i3 : (float) i4;
        } else {
            f2 = f3 + f4;
            f = (float) (this.mPositionRect.top + this.mExpandedViewPadding);
        }
        if (!showBubblesVertically() || !this.mImeVisible) {
            return new PointF(f2, f);
        }
        return new PointF(f2, getExpandedBubbleYForIme(i, stackViewState));
    }

    public final float getExpandedBubbleYForIme(int i, BubbleStackView.StackViewState stackViewState) {
        int i2;
        float f = (float) (getAvailableRect().top + this.mExpandedViewPadding);
        if (!showBubblesVertically()) {
            return f;
        }
        float imeHeight = ((float) this.mScreenRect.bottom) - ((float) ((getImeHeight() + this.mInsets.bottom) + (this.mSpacingBetweenBubbles * 2)));
        float centerY = (float) this.mPositionRect.centerY();
        float expandedStackSize = ((float) getExpandedStackSize(stackViewState.numberOfBubbles)) / 2.0f;
        float f2 = centerY + expandedStackSize;
        float f3 = centerY - expandedStackSize;
        if (f2 > imeHeight) {
            float f4 = f3 - (f2 - imeHeight);
            float max = Math.max(f4, f);
            if (f4 < f) {
                float expandedStackSize2 = (float) getExpandedStackSize(stackViewState.numberOfBubbles - 1);
                if (showBubblesVertically()) {
                    i2 = this.mPositionRect.centerY();
                } else {
                    i2 = this.mPositionRect.centerX();
                }
                float f5 = (float) i2;
                float f6 = expandedStackSize2 / 2.0f;
                f3 = (f5 - f6) - ((f5 + f6) - imeHeight);
            } else {
                f3 = max;
            }
        }
        int i3 = stackViewState.selectedIndex;
        int i4 = this.mBubbleSize;
        int i5 = this.mSpacingBetweenBubbles;
        if (((float) (i3 * (i4 + i5))) + f3 >= f) {
            f = f3;
        }
        return f + ((float) (i * (i4 + i5)));
    }

    public float getMaxFlyoutSize() {
        if (isLargeScreen()) {
            return Math.max(((float) this.mScreenRect.width()) * 0.3f, (float) this.mMinimumFlyoutWidthLargeScreen);
        }
        return ((float) this.mScreenRect.width()) * 0.6f;
    }

    public boolean isStackOnLeft(PointF pointF) {
        if (pointF == null) {
            pointF = getRestingPosition();
        }
        return ((int) pointF.x) + (this.mBubbleSize / 2) < this.mScreenRect.width() / 2;
    }

    public void setRestingPosition(PointF pointF) {
        PointF pointF2 = this.mRestingStackPosition;
        if (pointF2 == null) {
            this.mRestingStackPosition = new PointF(pointF);
        } else {
            pointF2.set(pointF);
        }
    }

    public PointF getRestingPosition() {
        PointF pointF = this.mPinLocation;
        if (pointF != null) {
            return pointF;
        }
        PointF pointF2 = this.mRestingStackPosition;
        return pointF2 == null ? getDefaultStartPosition() : pointF2;
    }

    public PointF getDefaultStartPosition() {
        return new BubbleStackView.RelativeStackPosition(this.mContext.getResources().getConfiguration().getLayoutDirection() != 1, ((float) this.mContext.getResources().getDimensionPixelOffset(R.dimen.bubble_stack_starting_offset_y)) / ((float) this.mPositionRect.height())).getAbsolutePositionInRegion(getAllowableStackPositionRegion(1));
    }

    public RectF getAllowableStackPositionRegion(int i) {
        int i2;
        RectF rectF = new RectF(getAvailableRect());
        int imeHeight = getImeHeight();
        if (i > 1) {
            i2 = this.mBubblePaddingTop + this.mStackOffset;
        } else {
            i2 = this.mBubblePaddingTop;
        }
        float f = rectF.left;
        int i3 = this.mBubbleOffscreenAmount;
        rectF.left = f - ((float) i3);
        rectF.top += (float) this.mBubblePaddingTop;
        float f2 = rectF.right;
        int i4 = this.mBubbleSize;
        rectF.right = f2 + ((float) (i3 - i4));
        rectF.bottom -= (((float) imeHeight) + ((float) i2)) + ((float) i4);
        return rectF;
    }

    public boolean showingInTaskbar() {
        return this.mShowingInTaskbar;
    }
}
