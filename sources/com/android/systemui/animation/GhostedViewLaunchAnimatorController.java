package com.android.systemui.animation;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Insets;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;
import android.view.GhostView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.widget.FrameLayout;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.animation.LaunchAnimator;
import java.util.LinkedList;
import kotlin.Lazy;
import kotlin.LazyKt__LazyJVMKt;
import kotlin.collections.ArraysKt___ArraysJvmKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: GhostedViewLaunchAnimatorController.kt */
public class GhostedViewLaunchAnimatorController implements ActivityLaunchAnimator.Controller {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @Nullable
    public final Drawable background;
    @Nullable
    public WrappedDrawable backgroundDrawable;
    @NotNull
    public final Lazy backgroundInsets$delegate;
    @Nullable
    public FrameLayout backgroundView;
    @Nullable
    public final Integer cujType;
    @Nullable
    public GhostView ghostView;
    @NotNull
    public final Matrix ghostViewMatrix;
    @NotNull
    public final View ghostedView;
    @NotNull
    public final int[] ghostedViewLocation;
    @NotNull
    public final LaunchAnimator.State ghostedViewState;
    @NotNull
    public final float[] initialGhostViewMatrixValues;
    @Nullable
    public InteractionJankMonitor interactionJankMonitor;
    @NotNull
    public ViewGroup launchContainer;
    @NotNull
    public final int[] launchContainerLocation;
    public int startBackgroundAlpha;

    public GhostedViewLaunchAnimatorController(@NotNull View view, @Nullable Integer num, @Nullable InteractionJankMonitor interactionJankMonitor2) {
        this.ghostedView = view;
        this.cujType = num;
        this.interactionJankMonitor = interactionJankMonitor2;
        View rootView = view.getRootView();
        if (rootView != null) {
            this.launchContainer = (ViewGroup) rootView;
            this.launchContainerLocation = new int[2];
            float[] fArr = new float[9];
            for (int i = 0; i < 9; i++) {
                fArr[i] = 0.0f;
            }
            this.initialGhostViewMatrixValues = fArr;
            this.ghostViewMatrix = new Matrix();
            this.backgroundInsets$delegate = LazyKt__LazyJVMKt.lazy(new GhostedViewLaunchAnimatorController$backgroundInsets$2(this));
            this.startBackgroundAlpha = 255;
            this.ghostedViewLocation = new int[2];
            this.ghostedViewState = new LaunchAnimator.State(0, 0, 0, 0, 0.0f, 0.0f, 63, (DefaultConstructorMarker) null);
            this.background = _init_$findBackground(this.ghostedView);
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type android.view.ViewGroup");
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ GhostedViewLaunchAnimatorController(View view, Integer num, InteractionJankMonitor interactionJankMonitor2, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(view, (i & 2) != 0 ? null : num, (i & 4) != 0 ? null : interactionJankMonitor2);
    }

    public GhostedViewLaunchAnimatorController(@NotNull View view, int i) {
        this(view, Integer.valueOf(i), (InteractionJankMonitor) null);
    }

    @NotNull
    public ViewGroup getLaunchContainer() {
        return this.launchContainer;
    }

    public void setLaunchContainer(@NotNull ViewGroup viewGroup) {
        this.launchContainer = viewGroup;
    }

    public final ViewGroupOverlay getLaunchContainerOverlay() {
        return getLaunchContainer().getOverlay();
    }

    public final Insets getBackgroundInsets() {
        return (Insets) this.backgroundInsets$delegate.getValue();
    }

    public static final Drawable _init_$findBackground(View view) {
        if (view.getBackground() != null) {
            return view.getBackground();
        }
        LinkedList linkedList = new LinkedList();
        linkedList.add(view);
        while (!linkedList.isEmpty()) {
            View view2 = (View) linkedList.removeFirst();
            if (view2.getBackground() != null) {
                return view2.getBackground();
            }
            if (view2 instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view2;
                int childCount = viewGroup.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    linkedList.add(viewGroup.getChildAt(i));
                }
            }
        }
        return null;
    }

    public void setBackgroundCornerRadius(@NotNull Drawable drawable, float f, float f2) {
        WrappedDrawable wrappedDrawable = this.backgroundDrawable;
        if (wrappedDrawable != null) {
            wrappedDrawable.setBackgroundRadius(f, f2);
        }
    }

    public float getCurrentTopCornerRadius() {
        GradientDrawable findGradientDrawable;
        Drawable drawable = this.background;
        if (drawable == null || (findGradientDrawable = Companion.findGradientDrawable(drawable)) == null) {
            return 0.0f;
        }
        float[] cornerRadii = findGradientDrawable.getCornerRadii();
        Float valueOf = cornerRadii == null ? null : Float.valueOf(cornerRadii[0]);
        return valueOf == null ? findGradientDrawable.getCornerRadius() : valueOf.floatValue();
    }

    public float getCurrentBottomCornerRadius() {
        GradientDrawable findGradientDrawable;
        Drawable drawable = this.background;
        if (drawable == null || (findGradientDrawable = Companion.findGradientDrawable(drawable)) == null) {
            return 0.0f;
        }
        float[] cornerRadii = findGradientDrawable.getCornerRadii();
        Float valueOf = cornerRadii == null ? null : Float.valueOf(cornerRadii[4]);
        return valueOf == null ? findGradientDrawable.getCornerRadius() : valueOf.floatValue();
    }

    @NotNull
    public LaunchAnimator.State createAnimatorState() {
        LaunchAnimator.State state = new LaunchAnimator.State(0, 0, 0, 0, getCurrentTopCornerRadius(), getCurrentBottomCornerRadius(), 15, (DefaultConstructorMarker) null);
        fillGhostedViewState(state);
        return state;
    }

    public final void fillGhostedViewState(@NotNull LaunchAnimator.State state) {
        this.ghostedView.getLocationOnScreen(this.ghostedViewLocation);
        Insets backgroundInsets = getBackgroundInsets();
        state.setTop(this.ghostedViewLocation[1] + backgroundInsets.top);
        state.setBottom((this.ghostedViewLocation[1] + this.ghostedView.getHeight()) - backgroundInsets.bottom);
        state.setLeft(this.ghostedViewLocation[0] + backgroundInsets.left);
        state.setRight((this.ghostedViewLocation[0] + this.ghostedView.getWidth()) - backgroundInsets.right);
    }

    public void onLaunchAnimationStart(boolean z) {
        Matrix matrix;
        if (!(this.ghostedView.getParent() instanceof ViewGroup)) {
            Log.w("GhostedViewLaunchAnimatorController", "Skipping animation as ghostedView is not attached to a ViewGroup");
            return;
        }
        this.backgroundView = new FrameLayout(getLaunchContainer().getContext());
        getLaunchContainerOverlay().add(this.backgroundView);
        Drawable drawable = this.background;
        this.startBackgroundAlpha = drawable == null ? 255 : drawable.getAlpha();
        WrappedDrawable wrappedDrawable = new WrappedDrawable(this.background);
        this.backgroundDrawable = wrappedDrawable;
        FrameLayout frameLayout = this.backgroundView;
        if (frameLayout != null) {
            frameLayout.setBackground(wrappedDrawable);
        }
        GhostView addGhost = GhostView.addGhost(this.ghostedView, getLaunchContainer());
        this.ghostView = addGhost;
        if (addGhost == null) {
            matrix = null;
        } else {
            matrix = addGhost.getAnimationMatrix();
        }
        if (matrix == null) {
            matrix = Matrix.IDENTITY_MATRIX;
        }
        matrix.getValues(this.initialGhostViewMatrixValues);
        Integer num = this.cujType;
        if (num != null) {
            int intValue = num.intValue();
            InteractionJankMonitor interactionJankMonitor2 = this.interactionJankMonitor;
            if (interactionJankMonitor2 != null) {
                interactionJankMonitor2.begin(this.ghostedView, intValue);
            }
        }
    }

    public void onLaunchAnimationProgress(@NotNull LaunchAnimator.State state, float f, float f2) {
        GhostView ghostView2 = this.ghostView;
        if (ghostView2 != null) {
            FrameLayout frameLayout = this.backgroundView;
            Intrinsics.checkNotNull(frameLayout);
            if (state.getVisible()) {
                if (ghostView2.getVisibility() == 4) {
                    ghostView2.setVisibility(0);
                    frameLayout.setVisibility(0);
                }
                fillGhostedViewState(this.ghostedViewState);
                int left = state.getLeft() - this.ghostedViewState.getLeft();
                int right = state.getRight() - this.ghostedViewState.getRight();
                int top = state.getTop() - this.ghostedViewState.getTop();
                int bottom = state.getBottom() - this.ghostedViewState.getBottom();
                float min = Math.min(((float) state.getWidth()) / ((float) this.ghostedViewState.getWidth()), ((float) state.getHeight()) / ((float) this.ghostedViewState.getHeight()));
                if (this.ghostedView.getParent() instanceof ViewGroup) {
                    GhostView.calculateMatrix(this.ghostedView, getLaunchContainer(), this.ghostViewMatrix);
                }
                getLaunchContainer().getLocationOnScreen(this.launchContainerLocation);
                this.ghostViewMatrix.postScale(min, min, this.ghostedViewState.getCenterX() - ((float) this.launchContainerLocation[0]), this.ghostedViewState.getCenterY() - ((float) this.launchContainerLocation[1]));
                this.ghostViewMatrix.postTranslate(((float) (left + right)) / 2.0f, ((float) (top + bottom)) / 2.0f);
                ghostView2.setAnimationMatrix(this.ghostViewMatrix);
                Insets backgroundInsets = getBackgroundInsets();
                int top2 = state.getTop() - backgroundInsets.top;
                int left2 = state.getLeft() - backgroundInsets.left;
                int right2 = state.getRight() + backgroundInsets.right;
                int bottom2 = state.getBottom() + backgroundInsets.bottom;
                frameLayout.setTop(top2 - this.launchContainerLocation[1]);
                frameLayout.setBottom(bottom2 - this.launchContainerLocation[1]);
                frameLayout.setLeft(left2 - this.launchContainerLocation[0]);
                frameLayout.setRight(right2 - this.launchContainerLocation[0]);
                WrappedDrawable wrappedDrawable = this.backgroundDrawable;
                Intrinsics.checkNotNull(wrappedDrawable);
                Drawable wrapped = wrappedDrawable.getWrapped();
                if (wrapped != null) {
                    setBackgroundCornerRadius(wrapped, state.getTopCornerRadius(), state.getBottomCornerRadius());
                }
            } else if (ghostView2.getVisibility() == 0) {
                ghostView2.setVisibility(4);
                this.ghostedView.setTransitionVisibility(4);
                frameLayout.setVisibility(4);
            }
        }
    }

    public void onLaunchAnimationEnd(boolean z) {
        if (this.ghostView != null) {
            Integer num = this.cujType;
            if (num != null) {
                int intValue = num.intValue();
                InteractionJankMonitor interactionJankMonitor2 = this.interactionJankMonitor;
                if (interactionJankMonitor2 != null) {
                    interactionJankMonitor2.end(intValue);
                }
            }
            WrappedDrawable wrappedDrawable = this.backgroundDrawable;
            Drawable wrapped = wrappedDrawable == null ? null : wrappedDrawable.getWrapped();
            if (wrapped != null) {
                wrapped.setAlpha(this.startBackgroundAlpha);
            }
            GhostView.removeGhost(this.ghostedView);
            getLaunchContainerOverlay().remove(this.backgroundView);
            this.ghostedView.setVisibility(4);
            this.ghostedView.setVisibility(0);
            this.ghostedView.invalidate();
        }
    }

    /* compiled from: GhostedViewLaunchAnimatorController.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }

        @Nullable
        public final GradientDrawable findGradientDrawable(@NotNull Drawable drawable) {
            if (drawable instanceof GradientDrawable) {
                return (GradientDrawable) drawable;
            }
            if (drawable instanceof InsetDrawable) {
                Drawable drawable2 = ((InsetDrawable) drawable).getDrawable();
                if (drawable2 == null) {
                    return null;
                }
                return GhostedViewLaunchAnimatorController.Companion.findGradientDrawable(drawable2);
            }
            if (drawable instanceof LayerDrawable) {
                int i = 0;
                LayerDrawable layerDrawable = (LayerDrawable) drawable;
                int numberOfLayers = layerDrawable.getNumberOfLayers();
                while (i < numberOfLayers) {
                    int i2 = i + 1;
                    Drawable drawable3 = layerDrawable.getDrawable(i);
                    if (drawable3 instanceof GradientDrawable) {
                        return (GradientDrawable) drawable3;
                    }
                    i = i2;
                }
            }
            return null;
        }
    }

    /* compiled from: GhostedViewLaunchAnimatorController.kt */
    public static final class WrappedDrawable extends Drawable {
        @NotNull
        public float[] cornerRadii;
        public int currentAlpha = 255;
        @NotNull
        public Rect previousBounds = new Rect();
        @NotNull
        public float[] previousCornerRadii;
        @Nullable
        public final Drawable wrapped;

        public WrappedDrawable(@Nullable Drawable drawable) {
            this.wrapped = drawable;
            float[] fArr = new float[8];
            for (int i = 0; i < 8; i++) {
                fArr[i] = -1.0f;
            }
            this.cornerRadii = fArr;
            this.previousCornerRadii = new float[8];
        }

        @Nullable
        public final Drawable getWrapped() {
            return this.wrapped;
        }

        public void draw(@NotNull Canvas canvas) {
            Drawable drawable = this.wrapped;
            if (drawable != null) {
                drawable.copyBounds(this.previousBounds);
                drawable.setAlpha(this.currentAlpha);
                drawable.setBounds(getBounds());
                applyBackgroundRadii();
                drawable.draw(canvas);
                drawable.setAlpha(0);
                drawable.setBounds(this.previousBounds);
                restoreBackgroundRadii();
            }
        }

        public void setAlpha(int i) {
            if (i != this.currentAlpha) {
                this.currentAlpha = i;
                invalidateSelf();
            }
        }

        public int getAlpha() {
            return this.currentAlpha;
        }

        public int getOpacity() {
            Drawable drawable = this.wrapped;
            if (drawable == null) {
                return -2;
            }
            int alpha = drawable.getAlpha();
            drawable.setAlpha(this.currentAlpha);
            int opacity = drawable.getOpacity();
            drawable.setAlpha(alpha);
            return opacity;
        }

        public void setColorFilter(@Nullable ColorFilter colorFilter) {
            Drawable drawable = this.wrapped;
            if (drawable != null) {
                drawable.setColorFilter(colorFilter);
            }
        }

        public final void setBackgroundRadius(float f, float f2) {
            updateRadii(this.cornerRadii, f, f2);
            invalidateSelf();
        }

        public final void updateRadii(float[] fArr, float f, float f2) {
            fArr[0] = f;
            fArr[1] = f;
            fArr[2] = f;
            fArr[3] = f;
            fArr[4] = f2;
            fArr[5] = f2;
            fArr[6] = f2;
            fArr[7] = f2;
        }

        public final void applyBackgroundRadii() {
            Drawable drawable;
            if (this.cornerRadii[0] >= 0.0f && (drawable = this.wrapped) != null) {
                savePreviousBackgroundRadii(drawable);
                applyBackgroundRadii(this.wrapped, this.cornerRadii);
            }
        }

        public final void savePreviousBackgroundRadii(Drawable drawable) {
            GradientDrawable findGradientDrawable = GhostedViewLaunchAnimatorController.Companion.findGradientDrawable(drawable);
            if (findGradientDrawable != null) {
                float[] cornerRadii2 = findGradientDrawable.getCornerRadii();
                if (cornerRadii2 != null) {
                    ArraysKt___ArraysJvmKt.copyInto$default(cornerRadii2, this.previousCornerRadii, 0, 0, 0, 14, (Object) null);
                    return;
                }
                float cornerRadius = findGradientDrawable.getCornerRadius();
                updateRadii(this.previousCornerRadii, cornerRadius, cornerRadius);
            }
        }

        public final void applyBackgroundRadii(Drawable drawable, float[] fArr) {
            if (drawable instanceof GradientDrawable) {
                ((GradientDrawable) drawable).setCornerRadii(fArr);
            } else if (drawable instanceof InsetDrawable) {
                Drawable drawable2 = ((InsetDrawable) drawable).getDrawable();
                if (drawable2 != null) {
                    applyBackgroundRadii(drawable2, fArr);
                }
            } else if (drawable instanceof LayerDrawable) {
                int i = 0;
                LayerDrawable layerDrawable = (LayerDrawable) drawable;
                int numberOfLayers = layerDrawable.getNumberOfLayers();
                while (i < numberOfLayers) {
                    int i2 = i + 1;
                    Drawable drawable3 = layerDrawable.getDrawable(i);
                    GradientDrawable gradientDrawable = drawable3 instanceof GradientDrawable ? (GradientDrawable) drawable3 : null;
                    if (gradientDrawable != null) {
                        gradientDrawable.setCornerRadii(fArr);
                    }
                    i = i2;
                }
            }
        }

        public final void restoreBackgroundRadii() {
            Drawable drawable;
            if (this.cornerRadii[0] >= 0.0f && (drawable = this.wrapped) != null) {
                applyBackgroundRadii(drawable, this.previousCornerRadii);
            }
        }
    }
}
