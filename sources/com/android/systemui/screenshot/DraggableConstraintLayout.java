package com.android.systemui.screenshot;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.MathUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.android.systemui.R$id;

public class DraggableConstraintLayout extends ConstraintLayout implements ViewTreeObserver.OnComputeInternalInsetsListener {
    public View mActionsContainer;
    public View mActionsContainerBackground;
    public SwipeDismissCallbacks mCallbacks;
    public final DisplayMetrics mDisplayMetrics;
    public final GestureDetector mSwipeDetector;
    public final SwipeDismissHandler mSwipeDismissHandler;

    public interface SwipeDismissCallbacks {
        void onDismissComplete() {
        }

        void onInteraction() {
        }

        void onSwipeDismissInitiated(Animator animator) {
        }
    }

    public DraggableConstraintLayout(Context context) {
        this(context, (AttributeSet) null);
    }

    public DraggableConstraintLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public DraggableConstraintLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.mDisplayMetrics = displayMetrics;
        this.mContext.getDisplay().getRealMetrics(displayMetrics);
        SwipeDismissHandler swipeDismissHandler = new SwipeDismissHandler(this.mContext, this);
        this.mSwipeDismissHandler = swipeDismissHandler;
        setOnTouchListener(swipeDismissHandler);
        GestureDetector gestureDetector = new GestureDetector(this.mContext, new GestureDetector.SimpleOnGestureListener() {
            public final Rect mActionsRect = new Rect();

            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                DraggableConstraintLayout.this.mActionsContainer.getBoundsOnScreen(this.mActionsRect);
                return !this.mActionsRect.contains((int) motionEvent2.getRawX(), (int) motionEvent2.getRawY()) || !DraggableConstraintLayout.this.mActionsContainer.canScrollHorizontally((int) f);
            }
        });
        this.mSwipeDetector = gestureDetector;
        gestureDetector.setIsLongpressEnabled(false);
    }

    public void setCallbacks(SwipeDismissCallbacks swipeDismissCallbacks) {
        this.mCallbacks = swipeDismissCallbacks;
    }

    public boolean onInterceptHoverEvent(MotionEvent motionEvent) {
        SwipeDismissCallbacks swipeDismissCallbacks = this.mCallbacks;
        if (swipeDismissCallbacks != null) {
            swipeDismissCallbacks.onInteraction();
        }
        return super.onInterceptHoverEvent(motionEvent);
    }

    public void onFinishInflate() {
        this.mActionsContainer = findViewById(R$id.actions_container);
        this.mActionsContainerBackground = findViewById(R$id.actions_container_background);
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() == 0) {
            this.mSwipeDismissHandler.onTouch(this, motionEvent);
        }
        return this.mSwipeDetector.onTouchEvent(motionEvent);
    }

    public void cancelDismissal() {
        this.mSwipeDismissHandler.cancel();
    }

    public boolean isDismissing() {
        return this.mSwipeDismissHandler.isDismissing();
    }

    public void dismiss() {
        this.mSwipeDismissHandler.dismiss();
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnComputeInternalInsetsListener(this);
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeOnComputeInternalInsetsListener(this);
    }

    public void onComputeInternalInsets(ViewTreeObserver.InternalInsetsInfo internalInsetsInfo) {
        Region region = new Region();
        Rect rect = new Rect();
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).getGlobalVisibleRect(rect);
            region.op(rect, Region.Op.UNION);
        }
        internalInsetsInfo.setTouchableInsets(3);
        internalInsetsInfo.touchableRegion.set(region);
    }

    public class SwipeDismissHandler implements View.OnTouchListener {
        public int mDirectionX;
        public ValueAnimator mDismissAnimation;
        public final DisplayMetrics mDisplayMetrics;
        public final GestureDetector mGestureDetector;
        public float mPreviousX;
        public float mStartX;
        public final DraggableConstraintLayout mView;

        public SwipeDismissHandler(Context context, DraggableConstraintLayout draggableConstraintLayout) {
            this.mView = draggableConstraintLayout;
            this.mGestureDetector = new GestureDetector(context, new SwipeDismissGestureListener());
            DisplayMetrics displayMetrics = new DisplayMetrics();
            this.mDisplayMetrics = displayMetrics;
            context.getDisplay().getRealMetrics(displayMetrics);
            DraggableConstraintLayout.this.mCallbacks = new SwipeDismissCallbacks(DraggableConstraintLayout.this) {
            };
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            boolean onTouchEvent = this.mGestureDetector.onTouchEvent(motionEvent);
            DraggableConstraintLayout.this.mCallbacks.onInteraction();
            if (motionEvent.getActionMasked() == 0) {
                float rawX = motionEvent.getRawX();
                this.mStartX = rawX;
                this.mPreviousX = rawX;
                return true;
            } else if (motionEvent.getActionMasked() != 1) {
                return onTouchEvent;
            } else {
                ValueAnimator valueAnimator = this.mDismissAnimation;
                if (valueAnimator != null && valueAnimator.isRunning()) {
                    return true;
                }
                if (isPastDismissThreshold()) {
                    dismiss();
                } else {
                    createSwipeReturnAnimation().start();
                }
                return true;
            }
        }

        public class SwipeDismissGestureListener extends GestureDetector.SimpleOnGestureListener {
            public SwipeDismissGestureListener() {
            }

            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                SwipeDismissHandler.this.mView.setTranslationX(motionEvent2.getRawX() - SwipeDismissHandler.this.mStartX);
                SwipeDismissHandler.this.mDirectionX = motionEvent2.getRawX() < SwipeDismissHandler.this.mPreviousX ? -1 : 1;
                SwipeDismissHandler.this.mPreviousX = motionEvent2.getRawX();
                return true;
            }

            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                if (SwipeDismissHandler.this.mView.getTranslationX() * f <= 0.0f) {
                    return false;
                }
                if (SwipeDismissHandler.this.mDismissAnimation != null && SwipeDismissHandler.this.mDismissAnimation.isRunning()) {
                    return false;
                }
                ValueAnimator r1 = SwipeDismissHandler.this.createSwipeDismissAnimation(f / 1000.0f);
                DraggableConstraintLayout.this.mCallbacks.onSwipeDismissInitiated(r1);
                SwipeDismissHandler.this.dismiss(r1);
                return true;
            }
        }

        public final boolean isPastDismissThreshold() {
            float translationX = this.mView.getTranslationX();
            if (((float) this.mDirectionX) * translationX <= 0.0f || Math.abs(translationX) < FloatingWindowUtil.dpToPx(this.mDisplayMetrics, 20.0f)) {
                return false;
            }
            return true;
        }

        public boolean isDismissing() {
            ValueAnimator valueAnimator = this.mDismissAnimation;
            return valueAnimator != null && valueAnimator.isRunning();
        }

        public void cancel() {
            if (isDismissing()) {
                this.mDismissAnimation.cancel();
            }
        }

        public void dismiss() {
            ValueAnimator createSwipeDismissAnimation = createSwipeDismissAnimation(FloatingWindowUtil.dpToPx(this.mDisplayMetrics, 1.0f));
            DraggableConstraintLayout.this.mCallbacks.onSwipeDismissInitiated(createSwipeDismissAnimation);
            dismiss(createSwipeDismissAnimation);
        }

        public final void dismiss(ValueAnimator valueAnimator) {
            this.mDismissAnimation = valueAnimator;
            valueAnimator.addListener(new AnimatorListenerAdapter() {
                public boolean mCancelled;

                public void onAnimationCancel(Animator animator) {
                    super.onAnimationCancel(animator);
                    this.mCancelled = true;
                }

                public void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                    if (!this.mCancelled) {
                        DraggableConstraintLayout.this.mCallbacks.onDismissComplete();
                    }
                }
            });
            this.mDismissAnimation.start();
        }

        public final ValueAnimator createSwipeDismissAnimation(float f) {
            int i;
            float min = Math.min(3.0f, Math.max(1.0f, f));
            ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
            float translationX = this.mView.getTranslationX();
            int layoutDirection = this.mView.getContext().getResources().getConfiguration().getLayoutDirection();
            int i2 = (translationX > 0.0f ? 1 : (translationX == 0.0f ? 0 : -1));
            if (i2 > 0 || (i2 == 0 && layoutDirection == 1)) {
                i = this.mDisplayMetrics.widthPixels;
            } else {
                i = DraggableConstraintLayout.this.mActionsContainerBackground.getRight() * -1;
            }
            float f2 = (float) i;
            float abs = Math.abs(f2 - translationX);
            ofFloat.addUpdateListener(new DraggableConstraintLayout$SwipeDismissHandler$$ExternalSyntheticLambda1(this, translationX, f2));
            ofFloat.setDuration((long) (abs / Math.abs(min)));
            return ofFloat;
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$createSwipeDismissAnimation$0(float f, float f2, ValueAnimator valueAnimator) {
            this.mView.setTranslationX(MathUtils.lerp(f, f2, valueAnimator.getAnimatedFraction()));
            this.mView.setAlpha(1.0f - valueAnimator.getAnimatedFraction());
        }

        public final ValueAnimator createSwipeReturnAnimation() {
            ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
            ofFloat.addUpdateListener(new DraggableConstraintLayout$SwipeDismissHandler$$ExternalSyntheticLambda0(this, this.mView.getTranslationX(), 0.0f));
            return ofFloat;
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$createSwipeReturnAnimation$1(float f, float f2, ValueAnimator valueAnimator) {
            this.mView.setTranslationX(MathUtils.lerp(f, f2, valueAnimator.getAnimatedFraction()));
        }
    }
}
