package com.android.systemui.shared.animation;

import android.graphics.Point;
import android.view.View;
import android.view.WindowManager;
import com.android.systemui.unfold.UnfoldTransitionProgressProvider;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: UnfoldMoveFromCenterAnimator.kt */
public final class UnfoldMoveFromCenterAnimator implements UnfoldTransitionProgressProvider.TransitionProgressListener {
    @Nullable
    public final AlphaProvider alphaProvider;
    @NotNull
    public final List<AnimatedView> animatedViews;
    public boolean isVerticalFold;
    public float lastAnimationProgress;
    @NotNull
    public final Point screenSize;
    @NotNull
    public final TranslationApplier translationApplier;
    @NotNull
    public final ViewCenterProvider viewCenterProvider;
    @NotNull
    public final WindowManager windowManager;

    /* compiled from: UnfoldMoveFromCenterAnimator.kt */
    public interface AlphaProvider {
        float getAlpha(float f);
    }

    public UnfoldMoveFromCenterAnimator(@NotNull WindowManager windowManager2, @NotNull TranslationApplier translationApplier2, @NotNull ViewCenterProvider viewCenterProvider2, @Nullable AlphaProvider alphaProvider2) {
        this.windowManager = windowManager2;
        this.translationApplier = translationApplier2;
        this.viewCenterProvider = viewCenterProvider2;
        this.alphaProvider = alphaProvider2;
        this.screenSize = new Point();
        this.animatedViews = new ArrayList();
        this.lastAnimationProgress = 1.0f;
    }

    public void onTransitionFinished() {
        UnfoldTransitionProgressProvider.TransitionProgressListener.DefaultImpls.onTransitionFinished(this);
    }

    public void onTransitionStarted() {
        UnfoldTransitionProgressProvider.TransitionProgressListener.DefaultImpls.onTransitionStarted(this);
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ UnfoldMoveFromCenterAnimator(WindowManager windowManager2, TranslationApplier translationApplier2, ViewCenterProvider viewCenterProvider2, AlphaProvider alphaProvider2, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(windowManager2, (i & 2) != 0 ? new TranslationApplier() {
            public void apply(@NotNull View view, float f, float f2) {
                TranslationApplier.DefaultImpls.apply(this, view, f, f2);
            }
        } : translationApplier2, (i & 4) != 0 ? new ViewCenterProvider() {
            public void getViewCenter(@NotNull View view, @NotNull Point point) {
                ViewCenterProvider.DefaultImpls.getViewCenter(this, view, point);
            }
        } : viewCenterProvider2, (i & 8) != 0 ? null : alphaProvider2);
    }

    public final void updateDisplayProperties() {
        this.windowManager.getDefaultDisplay().getSize(this.screenSize);
        this.isVerticalFold = this.windowManager.getDefaultDisplay().getRotation() == 0 || this.windowManager.getDefaultDisplay().getRotation() == 2;
    }

    public final void updateViewPositions() {
        for (AnimatedView animatedView : this.animatedViews) {
            View view = (View) animatedView.getView().get();
            if (view != null) {
                updateAnimatedView(animatedView, view);
            }
        }
        onTransitionProgress(this.lastAnimationProgress);
    }

    public final void registerViewForAnimation(@NotNull View view) {
        this.animatedViews.add(createAnimatedView(view));
    }

    public final void clearRegisteredViews() {
        onTransitionProgress(1.0f);
        this.animatedViews.clear();
    }

    public void onTransitionProgress(float f) {
        for (AnimatedView animatedView : this.animatedViews) {
            applyTransition(animatedView, f);
            applyAlpha(animatedView, f);
        }
        this.lastAnimationProgress = f;
    }

    public final void applyTransition(AnimatedView animatedView, float f) {
        View view = (View) animatedView.getView().get();
        if (view != null) {
            float f2 = ((float) 1) - f;
            this.translationApplier.apply(view, animatedView.getStartTranslationX() * f2, animatedView.getStartTranslationY() * f2);
        }
    }

    public final void applyAlpha(AnimatedView animatedView, float f) {
        View view;
        if (this.alphaProvider != null && (view = (View) animatedView.getView().get()) != null) {
            view.setAlpha(this.alphaProvider.getAlpha(f));
        }
    }

    public final AnimatedView createAnimatedView(View view) {
        return updateAnimatedView(new AnimatedView(new WeakReference(view), 0.0f, 0.0f, 6, (DefaultConstructorMarker) null), view);
    }

    public final AnimatedView updateAnimatedView(AnimatedView animatedView, View view) {
        Point point = new Point();
        this.viewCenterProvider.getViewCenter(view, point);
        int i = point.x;
        int i2 = point.y;
        if (this.isVerticalFold) {
            animatedView.setStartTranslationX(((float) ((this.screenSize.x / 2) - i)) * 0.3f);
            animatedView.setStartTranslationY(0.0f);
        } else {
            animatedView.setStartTranslationX(0.0f);
            animatedView.setStartTranslationY(((float) ((this.screenSize.y / 2) - i2)) * 0.3f);
        }
        return animatedView;
    }

    /* compiled from: UnfoldMoveFromCenterAnimator.kt */
    public interface TranslationApplier {
        void apply(@NotNull View view, float f, float f2);

        /* compiled from: UnfoldMoveFromCenterAnimator.kt */
        public static final class DefaultImpls {
            public static void apply(@NotNull TranslationApplier translationApplier, @NotNull View view, float f, float f2) {
                view.setTranslationX(f);
                view.setTranslationY(f2);
            }
        }
    }

    /* compiled from: UnfoldMoveFromCenterAnimator.kt */
    public interface ViewCenterProvider {
        void getViewCenter(@NotNull View view, @NotNull Point point);

        /* compiled from: UnfoldMoveFromCenterAnimator.kt */
        public static final class DefaultImpls {
            public static void getViewCenter(@NotNull ViewCenterProvider viewCenterProvider, @NotNull View view, @NotNull Point point) {
                int[] iArr = new int[2];
                view.getLocationOnScreen(iArr);
                int i = iArr[0];
                int i2 = iArr[1];
                point.x = i + (view.getWidth() / 2);
                point.y = i2 + (view.getHeight() / 2);
            }
        }
    }

    /* compiled from: UnfoldMoveFromCenterAnimator.kt */
    public static final class AnimatedView {
        public float startTranslationX;
        public float startTranslationY;
        @NotNull
        public final WeakReference<View> view;

        public AnimatedView(@NotNull WeakReference<View> weakReference, float f, float f2) {
            this.view = weakReference;
            this.startTranslationX = f;
            this.startTranslationY = f2;
        }

        /* JADX INFO: this call moved to the top of the method (can break code semantics) */
        public /* synthetic */ AnimatedView(WeakReference weakReference, float f, float f2, int i, DefaultConstructorMarker defaultConstructorMarker) {
            this(weakReference, (i & 2) != 0 ? 0.0f : f, (i & 4) != 0 ? 0.0f : f2);
        }

        @NotNull
        public final WeakReference<View> getView() {
            return this.view;
        }

        public final float getStartTranslationX() {
            return this.startTranslationX;
        }

        public final void setStartTranslationX(float f) {
            this.startTranslationX = f;
        }

        public final float getStartTranslationY() {
            return this.startTranslationY;
        }

        public final void setStartTranslationY(float f) {
            this.startTranslationY = f;
        }
    }
}
