package com.android.systemui.util.animation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import com.android.systemui.statusbar.CrossFadeHelper;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: TransitionLayout.kt */
public final class TransitionLayout extends ConstraintLayout {
    @NotNull
    public final Rect boundsRect;
    @NotNull
    public TransitionViewState currentState;
    public int desiredMeasureHeight;
    public int desiredMeasureWidth;
    public boolean isPreDrawApplicatorRegistered;
    public boolean measureAsConstraint;
    @NotNull
    public TransitionViewState measureState;
    @NotNull
    public final Set<Integer> originalGoneChildrenSet;
    @NotNull
    public final Map<Integer, Float> originalViewAlphas;
    @NotNull
    public final TransitionLayout$preDrawApplicator$1 preDrawApplicator;
    public int transitionVisibility;
    public boolean updateScheduled;

    public TransitionLayout(@NotNull Context context) {
        this(context, (AttributeSet) null, 0, 6, (DefaultConstructorMarker) null);
    }

    public TransitionLayout(@NotNull Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0, 4, (DefaultConstructorMarker) null);
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ TransitionLayout(Context context, AttributeSet attributeSet, int i, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i2 & 2) != 0 ? null : attributeSet, (i2 & 4) != 0 ? 0 : i);
    }

    public TransitionLayout(@NotNull Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.boundsRect = new Rect();
        this.originalGoneChildrenSet = new LinkedHashSet();
        this.originalViewAlphas = new LinkedHashMap();
        this.currentState = new TransitionViewState();
        this.measureState = new TransitionViewState();
        this.preDrawApplicator = new TransitionLayout$preDrawApplicator$1(this);
    }

    public final void setMeasureState(@NotNull TransitionViewState transitionViewState) {
        int width = transitionViewState.getWidth();
        int height = transitionViewState.getHeight();
        if (width != this.desiredMeasureWidth || height != this.desiredMeasureHeight) {
            this.desiredMeasureWidth = width;
            this.desiredMeasureHeight = height;
            if (isInLayout()) {
                forceLayout();
            } else {
                requestLayout();
            }
        }
    }

    public void setTransitionVisibility(int i) {
        super.setTransitionVisibility(i);
        this.transitionVisibility = i;
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        int i = 0;
        while (i < childCount) {
            int i2 = i + 1;
            View childAt = getChildAt(i);
            if (childAt.getId() == -1) {
                childAt.setId(i);
            }
            if (childAt.getVisibility() == 8) {
                this.originalGoneChildrenSet.add(Integer.valueOf(childAt.getId()));
            }
            this.originalViewAlphas.put(Integer.valueOf(childAt.getId()), Float.valueOf(childAt.getAlpha()));
            i = i2;
        }
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.isPreDrawApplicatorRegistered) {
            getViewTreeObserver().removeOnPreDrawListener(this.preDrawApplicator);
            this.isPreDrawApplicatorRegistered = false;
        }
    }

    public final void applyCurrentState() {
        Integer num;
        int i;
        int i2;
        int childCount = getChildCount();
        int i3 = (int) this.currentState.getContentTranslation().x;
        int i4 = (int) this.currentState.getContentTranslation().y;
        int i5 = 0;
        while (i5 < childCount) {
            int i6 = i5 + 1;
            View childAt = getChildAt(i5);
            WidgetState widgetState = this.currentState.getWidgetStates().get(Integer.valueOf(childAt.getId()));
            if (widgetState != null) {
                if (!(childAt instanceof TextView) || widgetState.getWidth() >= widgetState.getMeasureWidth()) {
                    num = null;
                } else {
                    num = Integer.valueOf(((TextView) childAt).getLayout().getParagraphDirection(0) == -1 ? widgetState.getMeasureWidth() - widgetState.getWidth() : 0);
                }
                if (!(childAt.getMeasuredWidth() == widgetState.getMeasureWidth() && childAt.getMeasuredHeight() == widgetState.getMeasureHeight())) {
                    childAt.measure(View.MeasureSpec.makeMeasureSpec(widgetState.getMeasureWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(widgetState.getMeasureHeight(), 1073741824));
                    childAt.layout(0, 0, childAt.getMeasuredWidth(), childAt.getMeasuredHeight());
                }
                if (num == null) {
                    i = 0;
                } else {
                    i = num.intValue();
                }
                int x = (((int) widgetState.getX()) + i3) - i;
                int y = ((int) widgetState.getY()) + i4;
                boolean z = true;
                boolean z2 = num != null;
                childAt.setLeftTopRightBottom(x, y, (z2 ? widgetState.getMeasureWidth() : widgetState.getWidth()) + x, (z2 ? widgetState.getMeasureHeight() : widgetState.getHeight()) + y);
                childAt.setScaleX(widgetState.getScale());
                childAt.setScaleY(widgetState.getScale());
                Rect clipBounds = childAt.getClipBounds();
                if (clipBounds == null) {
                    clipBounds = new Rect();
                }
                clipBounds.set(i, 0, widgetState.getWidth() + i, widgetState.getHeight());
                childAt.setClipBounds(clipBounds);
                CrossFadeHelper.fadeIn(childAt, widgetState.getAlpha());
                if (!widgetState.getGone()) {
                    if (widgetState.getAlpha() != 0.0f) {
                        z = false;
                    }
                    if (!z) {
                        i2 = 0;
                        childAt.setVisibility(i2);
                    }
                }
                i2 = 4;
                childAt.setVisibility(i2);
            }
            i5 = i6;
        }
        updateBounds();
        setTranslationX(this.currentState.getTranslation().x);
        setTranslationY(this.currentState.getTranslation().y);
        CrossFadeHelper.fadeIn(this, this.currentState.getAlpha());
        int i7 = this.transitionVisibility;
        if (i7 != 0) {
            setTransitionVisibility(i7);
        }
    }

    public final void applyCurrentStateOnPredraw() {
        if (!this.updateScheduled) {
            this.updateScheduled = true;
            if (!this.isPreDrawApplicatorRegistered) {
                getViewTreeObserver().addOnPreDrawListener(this.preDrawApplicator);
                this.isPreDrawApplicatorRegistered = true;
            }
        }
    }

    public void onMeasure(int i, int i2) {
        if (this.measureAsConstraint) {
            super.onMeasure(i, i2);
            return;
        }
        int i3 = 0;
        int childCount = getChildCount();
        while (i3 < childCount) {
            int i4 = i3 + 1;
            View childAt = getChildAt(i3);
            WidgetState widgetState = this.currentState.getWidgetStates().get(Integer.valueOf(childAt.getId()));
            if (widgetState != null) {
                childAt.measure(View.MeasureSpec.makeMeasureSpec(widgetState.getMeasureWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(widgetState.getMeasureHeight(), 1073741824));
            }
            i3 = i4;
        }
        setMeasuredDimension(this.desiredMeasureWidth, this.desiredMeasureHeight);
    }

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        if (this.measureAsConstraint) {
            super.onLayout(z, getLeft(), getTop(), getRight(), getBottom());
            return;
        }
        int childCount = getChildCount();
        int i5 = 0;
        while (i5 < childCount) {
            int i6 = i5 + 1;
            View childAt = getChildAt(i5);
            childAt.layout(0, 0, childAt.getMeasuredWidth(), childAt.getMeasuredHeight());
            i5 = i6;
        }
        applyCurrentState();
    }

    public void dispatchDraw(@Nullable Canvas canvas) {
        if (canvas != null) {
            canvas.save();
        }
        if (canvas != null) {
            canvas.clipRect(this.boundsRect);
        }
        super.dispatchDraw(canvas);
        if (canvas != null) {
            canvas.restore();
        }
    }

    public final void updateBounds() {
        int left = getLeft();
        int top = getTop();
        setLeftTopRightBottom(left, top, this.currentState.getWidth() + left, this.currentState.getHeight() + top);
        this.boundsRect.set(0, 0, getWidth(), getHeight());
    }

    @NotNull
    public final TransitionViewState calculateViewState(@NotNull MeasurementInput measurementInput, @NotNull ConstraintSet constraintSet, @Nullable TransitionViewState transitionViewState) {
        if (transitionViewState == null) {
            transitionViewState = new TransitionViewState();
        }
        applySetToFullLayout(constraintSet);
        int measuredHeight = getMeasuredHeight();
        int measuredWidth = getMeasuredWidth();
        this.measureAsConstraint = true;
        measure(measurementInput.getWidthMeasureSpec(), measurementInput.getHeightMeasureSpec());
        int left = getLeft();
        int top = getTop();
        layout(left, top, getMeasuredWidth() + left, getMeasuredHeight() + top);
        this.measureAsConstraint = false;
        transitionViewState.initFromLayout(this);
        ensureViewsNotGone();
        setMeasuredDimension(measuredWidth, measuredHeight);
        applyCurrentStateOnPredraw();
        return transitionViewState;
    }

    public final void applySetToFullLayout(ConstraintSet constraintSet) {
        int childCount = getChildCount();
        int i = 0;
        while (i < childCount) {
            int i2 = i + 1;
            View childAt = getChildAt(i);
            if (this.originalGoneChildrenSet.contains(Integer.valueOf(childAt.getId()))) {
                childAt.setVisibility(8);
            }
            Float f = this.originalViewAlphas.get(Integer.valueOf(childAt.getId()));
            childAt.setAlpha(f == null ? 1.0f : f.floatValue());
            i = i2;
        }
        constraintSet.applyTo(this);
    }

    public final void ensureViewsNotGone() {
        boolean z;
        int childCount = getChildCount();
        int i = 0;
        while (i < childCount) {
            int i2 = i + 1;
            View childAt = getChildAt(i);
            WidgetState widgetState = this.currentState.getWidgetStates().get(Integer.valueOf(childAt.getId()));
            if (widgetState != null && !widgetState.getGone()) {
                z = true;
            } else {
                z = false;
            }
            childAt.setVisibility(!z ? 4 : 0);
            i = i2;
        }
    }

    public final void setState(@NotNull TransitionViewState transitionViewState) {
        this.currentState = transitionViewState;
        applyCurrentState();
    }
}
