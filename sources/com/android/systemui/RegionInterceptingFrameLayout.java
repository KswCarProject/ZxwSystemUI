package com.android.systemui;

import android.content.Context;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

public class RegionInterceptingFrameLayout extends FrameLayout {
    public final ViewTreeObserver.OnComputeInternalInsetsListener mInsetsListener = new RegionInterceptingFrameLayout$$ExternalSyntheticLambda0(this);

    public interface RegionInterceptableView {
        Region getInterceptRegion();

        boolean shouldInterceptTouch() {
            return false;
        }
    }

    public RegionInterceptingFrameLayout(Context context) {
        super(context);
    }

    public RegionInterceptingFrameLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public RegionInterceptingFrameLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public RegionInterceptingFrameLayout(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnComputeInternalInsetsListener(this.mInsetsListener);
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeOnComputeInternalInsetsListener(this.mInsetsListener);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(ViewTreeObserver.InternalInsetsInfo internalInsetsInfo) {
        Region interceptRegion;
        internalInsetsInfo.setTouchableInsets(3);
        internalInsetsInfo.touchableRegion.setEmpty();
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (childAt instanceof RegionInterceptableView) {
                RegionInterceptableView regionInterceptableView = (RegionInterceptableView) childAt;
                if (regionInterceptableView.shouldInterceptTouch() && (interceptRegion = regionInterceptableView.getInterceptRegion()) != null) {
                    internalInsetsInfo.touchableRegion.op(interceptRegion, Region.Op.UNION);
                }
            }
        }
    }
}
