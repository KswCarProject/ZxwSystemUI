package com.android.wm.shell.bubbles;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.IntProperty;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.FrameLayout;
import androidx.dynamicanimation.animation.DynamicAnimation;
import com.android.wm.shell.R;
import com.android.wm.shell.animation.PhysicsAnimator;
import com.android.wm.shell.common.DismissCircleView;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import org.jetbrains.annotations.NotNull;

/* compiled from: DismissView.kt */
public final class DismissView extends FrameLayout {
    public final long DISMISS_SCRIM_FADE_MS = 200;
    @NotNull
    public final IntProperty<GradientDrawable> GRADIENT_ALPHA;
    @NotNull
    public final PhysicsAnimator<DismissCircleView> animator;
    @NotNull
    public DismissCircleView circle;
    @NotNull
    public GradientDrawable gradientDrawable;
    public boolean isShowing;
    @NotNull
    public final PhysicsAnimator.SpringConfig spring = new PhysicsAnimator.SpringConfig(200.0f, 0.75f);
    @NotNull
    public WindowManager wm;

    public DismissView(@NotNull Context context) {
        super(context);
        DismissCircleView dismissCircleView = new DismissCircleView(context);
        this.circle = dismissCircleView;
        this.animator = PhysicsAnimator.Companion.getInstance(dismissCircleView);
        Object systemService = context.getSystemService("window");
        if (systemService != null) {
            this.wm = (WindowManager) systemService;
            this.gradientDrawable = createGradient();
            this.GRADIENT_ALPHA = new DismissView$GRADIENT_ALPHA$1();
            Resources resources = getResources();
            int i = R.dimen.floating_dismiss_gradient_height;
            setLayoutParams(new FrameLayout.LayoutParams(-1, resources.getDimensionPixelSize(i), 80));
            updatePadding();
            setClipToPadding(false);
            setClipChildren(false);
            setVisibility(4);
            setBackgroundDrawable(this.gradientDrawable);
            int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.dismiss_circle_size);
            addView(this.circle, new FrameLayout.LayoutParams(dimensionPixelSize, dimensionPixelSize, 81));
            this.circle.setTranslationY((float) getResources().getDimensionPixelSize(i));
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type android.view.WindowManager");
    }

    @NotNull
    public final DismissCircleView getCircle() {
        return this.circle;
    }

    public final boolean isShowing() {
        return this.isShowing;
    }

    public final void show() {
        if (!this.isShowing) {
            this.isShowing = true;
            setVisibility(0);
            GradientDrawable gradientDrawable2 = this.gradientDrawable;
            ObjectAnimator ofInt = ObjectAnimator.ofInt(gradientDrawable2, this.GRADIENT_ALPHA, new int[]{gradientDrawable2.getAlpha(), 255});
            ofInt.setDuration(this.DISMISS_SCRIM_FADE_MS);
            ofInt.start();
            this.animator.cancel();
            this.animator.spring(DynamicAnimation.TRANSLATION_Y, 0.0f, this.spring).start();
        }
    }

    public final void hide() {
        if (this.isShowing) {
            this.isShowing = false;
            GradientDrawable gradientDrawable2 = this.gradientDrawable;
            ObjectAnimator ofInt = ObjectAnimator.ofInt(gradientDrawable2, this.GRADIENT_ALPHA, new int[]{gradientDrawable2.getAlpha(), 0});
            ofInt.setDuration(this.DISMISS_SCRIM_FADE_MS);
            ofInt.start();
            this.animator.spring(DynamicAnimation.TRANSLATION_Y, (float) getHeight(), this.spring).withEndActions((Function0<Unit>[]) new Function0[]{new DismissView$hide$1(this)}).start();
        }
    }

    public final void cancelAnimators() {
        this.animator.cancel();
    }

    public final void updateResources() {
        updatePadding();
        getLayoutParams().height = getResources().getDimensionPixelSize(R.dimen.floating_dismiss_gradient_height);
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.dismiss_circle_size);
        this.circle.getLayoutParams().width = dimensionPixelSize;
        this.circle.getLayoutParams().height = dimensionPixelSize;
        this.circle.requestLayout();
    }

    public final GradientDrawable createGradient() {
        int color = getContext().getResources().getColor(17170472);
        int argb = Color.argb((int) 178.5f, Color.red(color), Color.green(color), Color.blue(color));
        GradientDrawable gradientDrawable2 = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{argb, 0});
        gradientDrawable2.setAlpha(0);
        return gradientDrawable2;
    }

    public final void updatePadding() {
        setPadding(0, 0, 0, this.wm.getCurrentWindowMetrics().getWindowInsets().getInsetsIgnoringVisibility(WindowInsets.Type.navigationBars()).bottom + getResources().getDimensionPixelSize(R.dimen.floating_dismiss_bottom_margin));
    }
}
