package com.android.systemui.qs;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.android.systemui.Dumpable;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.qs.customize.QSCustomizer;
import java.io.PrintWriter;

public class QSContainerImpl extends FrameLayout implements Dumpable {
    public boolean mClippingEnabled;
    public int mContentHorizontalPadding = -1;
    public int mFancyClippingBottom;
    public final Path mFancyClippingPath = new Path();
    public final float[] mFancyClippingRadii = {0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
    public int mFancyClippingTop;
    public QuickStatusBarHeader mHeader;
    public int mHeightOverride = -1;
    public int mHorizontalMargins;
    public QSCustomizer mQSCustomizer;
    public NonInterceptingScrollView mQSPanelContainer;
    public boolean mQsDisabled;
    public float mQsExpansion;
    public int mTilesPageMargin;

    public boolean hasOverlappingRendering() {
        return false;
    }

    public boolean performClick() {
        return true;
    }

    public QSContainerImpl(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        this.mQSPanelContainer = (NonInterceptingScrollView) findViewById(R$id.expanded_qs_scroll_view);
        this.mHeader = (QuickStatusBarHeader) findViewById(R$id.header);
        this.mQSCustomizer = (QSCustomizer) findViewById(R$id.qs_customize);
        setImportantForAccessibility(2);
    }

    public void onMeasure(int i, int i2) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.mQSPanelContainer.getLayoutParams();
        int size = View.MeasureSpec.getSize(i2);
        int paddingBottom = ((size - marginLayoutParams.topMargin) - marginLayoutParams.bottomMargin) - getPaddingBottom();
        int i3 = this.mPaddingLeft + this.mPaddingRight + marginLayoutParams.leftMargin + marginLayoutParams.rightMargin;
        this.mQSPanelContainer.measure(FrameLayout.getChildMeasureSpec(i, i3, marginLayoutParams.width), View.MeasureSpec.makeMeasureSpec(paddingBottom, Integer.MIN_VALUE));
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(this.mQSPanelContainer.getMeasuredWidth() + i3, 1073741824), View.MeasureSpec.makeMeasureSpec(size, 1073741824));
        this.mQSCustomizer.measure(i, View.MeasureSpec.makeMeasureSpec(size, 1073741824));
    }

    public void dispatchDraw(Canvas canvas) {
        if (!this.mFancyClippingPath.isEmpty()) {
            canvas.translate(0.0f, -getTranslationY());
            canvas.clipOutPath(this.mFancyClippingPath);
            canvas.translate(0.0f, getTranslationY());
        }
        super.dispatchDraw(canvas);
    }

    public void measureChildWithMargins(View view, int i, int i2, int i3, int i4) {
        if (view != this.mQSPanelContainer) {
            super.measureChildWithMargins(view, i, i2, i3, i4);
        }
    }

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        updateExpansion();
        updateClippingPath();
    }

    public void disable(int i, int i2, boolean z) {
        boolean z2 = true;
        if ((i2 & 1) == 0) {
            z2 = false;
        }
        if (z2 != this.mQsDisabled) {
            this.mQsDisabled = z2;
        }
    }

    public void updateResources(QSPanelController qSPanelController, QuickStatusBarHeaderController quickStatusBarHeaderController) {
        NonInterceptingScrollView nonInterceptingScrollView = this.mQSPanelContainer;
        nonInterceptingScrollView.setPaddingRelative(nonInterceptingScrollView.getPaddingStart(), QSUtils.getQsHeaderSystemIconsAreaHeight(this.mContext), this.mQSPanelContainer.getPaddingEnd(), this.mQSPanelContainer.getPaddingBottom());
        int dimensionPixelSize = getResources().getDimensionPixelSize(R$dimen.qs_horizontal_margin);
        int dimensionPixelSize2 = getResources().getDimensionPixelSize(R$dimen.qs_content_horizontal_padding);
        int dimensionPixelSize3 = getResources().getDimensionPixelSize(R$dimen.qs_tiles_page_horizontal_margin);
        boolean z = (dimensionPixelSize2 == this.mContentHorizontalPadding && dimensionPixelSize == this.mHorizontalMargins && dimensionPixelSize3 == this.mTilesPageMargin) ? false : true;
        this.mContentHorizontalPadding = dimensionPixelSize2;
        this.mHorizontalMargins = dimensionPixelSize;
        this.mTilesPageMargin = dimensionPixelSize3;
        if (z) {
            updatePaddingsAndMargins(qSPanelController, quickStatusBarHeaderController);
        }
    }

    public void setHeightOverride(int i) {
        this.mHeightOverride = i;
        updateExpansion();
    }

    public void updateExpansion() {
        setBottom(getTop() + calculateContainerHeight());
    }

    public int calculateContainerHeight() {
        int i = this.mHeightOverride;
        if (i == -1) {
            i = getMeasuredHeight();
        }
        if (this.mQSCustomizer.isCustomizing()) {
            return this.mQSCustomizer.getHeight();
        }
        return this.mHeader.getHeight() + Math.round(this.mQsExpansion * ((float) (i - this.mHeader.getHeight())));
    }

    public void setExpansion(float f) {
        this.mQsExpansion = f;
        this.mQSPanelContainer.setScrollingEnabled(f > 0.0f);
        updateExpansion();
    }

    public final void updatePaddingsAndMargins(QSPanelController qSPanelController, QuickStatusBarHeaderController quickStatusBarHeaderController) {
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (childAt != this.mQSCustomizer) {
                if (!(childAt instanceof FooterActionsView)) {
                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) childAt.getLayoutParams();
                    int i2 = this.mHorizontalMargins;
                    layoutParams.rightMargin = i2;
                    layoutParams.leftMargin = i2;
                }
                if (childAt == this.mQSPanelContainer) {
                    int i3 = this.mContentHorizontalPadding;
                    qSPanelController.setContentMargins(i3, i3);
                    qSPanelController.setPageMargin(this.mTilesPageMargin);
                } else if (childAt == this.mHeader) {
                    int i4 = this.mContentHorizontalPadding;
                    quickStatusBarHeaderController.setContentMargins(i4, i4);
                } else {
                    childAt.setPaddingRelative(this.mContentHorizontalPadding, childAt.getPaddingTop(), this.mContentHorizontalPadding, childAt.getPaddingBottom());
                }
            }
        }
    }

    public void setFancyClipping(int i, int i2, int i3, boolean z) {
        float[] fArr = this.mFancyClippingRadii;
        boolean z2 = false;
        float f = (float) i3;
        boolean z3 = true;
        if (fArr[0] != f) {
            fArr[0] = f;
            fArr[1] = f;
            fArr[2] = f;
            fArr[3] = f;
            z2 = true;
        }
        if (this.mFancyClippingTop != i) {
            this.mFancyClippingTop = i;
            z2 = true;
        }
        if (this.mFancyClippingBottom != i2) {
            this.mFancyClippingBottom = i2;
            z2 = true;
        }
        if (this.mClippingEnabled != z) {
            this.mClippingEnabled = z;
        } else {
            z3 = z2;
        }
        if (z3) {
            updateClippingPath();
        }
    }

    public boolean isTransformedTouchPointInView(float f, float f2, View view, PointF pointF) {
        if (!this.mClippingEnabled || getTranslationY() + f2 <= ((float) this.mFancyClippingTop)) {
            return super.isTransformedTouchPointInView(f, f2, view, pointF);
        }
        return false;
    }

    public final void updateClippingPath() {
        this.mFancyClippingPath.reset();
        if (!this.mClippingEnabled) {
            invalidate();
            return;
        }
        this.mFancyClippingPath.addRoundRect(0.0f, (float) this.mFancyClippingTop, (float) getWidth(), (float) this.mFancyClippingBottom, this.mFancyClippingRadii, Path.Direction.CW);
        invalidate();
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println(getClass().getSimpleName() + " updateClippingPath: top(" + this.mFancyClippingTop + ") bottom(" + this.mFancyClippingBottom + ") mClippingEnabled(" + this.mClippingEnabled + ")");
    }
}
