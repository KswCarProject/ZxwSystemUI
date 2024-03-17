package com.android.wm.shell.bubbles;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PointF;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.internal.util.ContrastColorUtil;
import com.android.wm.shell.R;
import kotlin.Lazy;
import kotlin.LazyKt__LazyJVMKt;
import org.jetbrains.annotations.NotNull;

/* compiled from: StackEducationView.kt */
public final class StackEducationView extends LinearLayout {
    public final long ANIMATE_DURATION = 200;
    public final long ANIMATE_DURATION_SHORT = 40;
    @NotNull
    public final String TAG = "Bubbles";
    @NotNull
    public final BubbleController controller;
    @NotNull
    public final Lazy descTextView$delegate;
    public boolean isHiding;
    @NotNull
    public final BubblePositioner positioner;
    @NotNull
    public final Lazy titleTextView$delegate;
    @NotNull
    public final Lazy view$delegate;

    public StackEducationView(@NotNull Context context, @NotNull BubblePositioner bubblePositioner, @NotNull BubbleController bubbleController) {
        super(context);
        this.positioner = bubblePositioner;
        this.controller = bubbleController;
        this.view$delegate = LazyKt__LazyJVMKt.lazy(new StackEducationView$view$2(this));
        this.titleTextView$delegate = LazyKt__LazyJVMKt.lazy(new StackEducationView$titleTextView$2(this));
        this.descTextView$delegate = LazyKt__LazyJVMKt.lazy(new StackEducationView$descTextView$2(this));
        LayoutInflater.from(context).inflate(R.layout.bubble_stack_user_education, this);
        setVisibility(8);
        setElevation((float) getResources().getDimensionPixelSize(R.dimen.bubble_elevation));
        setLayoutDirection(3);
    }

    public final View getView() {
        return (View) this.view$delegate.getValue();
    }

    public final TextView getTitleTextView() {
        return (TextView) this.titleTextView$delegate.getValue();
    }

    public final TextView getDescTextView() {
        return (TextView) this.descTextView$delegate.getValue();
    }

    public void setLayoutDirection(int i) {
        super.setLayoutDirection(i);
        setDrawableDirection();
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        setLayoutDirection(getResources().getConfiguration().getLayoutDirection());
        setTextColor();
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        setFocusableInTouchMode(true);
        setOnKeyListener(new StackEducationView$onAttachedToWindow$1(this));
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setOnKeyListener((View.OnKeyListener) null);
        this.controller.updateWindowFlagsForBackpress(false);
    }

    public final void setTextColor() {
        TypedArray obtainStyledAttributes = this.mContext.obtainStyledAttributes(new int[]{16843829, 16842809});
        int color = obtainStyledAttributes.getColor(0, -16777216);
        int color2 = obtainStyledAttributes.getColor(1, -1);
        obtainStyledAttributes.recycle();
        int ensureTextContrast = ContrastColorUtil.ensureTextContrast(color2, color, true);
        getTitleTextView().setTextColor(ensureTextContrast);
        getDescTextView().setTextColor(ensureTextContrast);
    }

    public final void setDrawableDirection() {
        int i;
        View view = getView();
        if (getResources().getConfiguration().getLayoutDirection() == 0) {
            i = R.drawable.bubble_stack_user_education_bg;
        } else {
            i = R.drawable.bubble_stack_user_education_bg_rtl;
        }
        view.setBackgroundResource(i);
    }

    public final boolean show(@NotNull PointF pointF) {
        int i;
        this.isHiding = false;
        if (getVisibility() == 0) {
            return false;
        }
        this.controller.updateWindowFlagsForBackpress(true);
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (this.positioner.isLargeScreen() || this.positioner.isLandscape()) {
            i = getContext().getResources().getDimensionPixelSize(R.dimen.bubbles_user_education_width);
        } else {
            i = -1;
        }
        layoutParams.width = i;
        int dimensionPixelSize = getContext().getResources().getDimensionPixelSize(R.dimen.bubble_user_education_stack_padding);
        setAlpha(0.0f);
        setVisibility(0);
        post(new StackEducationView$show$1(this, dimensionPixelSize, pointF));
        setShouldShow(false);
        return true;
    }

    public final void hide(boolean z) {
        if (getVisibility() == 0 && !this.isHiding) {
            this.isHiding = true;
            this.controller.updateWindowFlagsForBackpress(false);
            animate().alpha(0.0f).setDuration(z ? this.ANIMATE_DURATION_SHORT : this.ANIMATE_DURATION).withEndAction(new StackEducationView$hide$1(this));
        }
    }

    public final void setShouldShow(boolean z) {
        getContext().getSharedPreferences(getContext().getPackageName(), 0).edit().putBoolean("HasSeenBubblesOnboarding", !z).apply();
    }
}
