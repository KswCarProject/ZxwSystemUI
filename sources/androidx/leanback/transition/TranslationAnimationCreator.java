package androidx.leanback.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.graphics.Path;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.view.View;
import androidx.leanback.R$id;

public class TranslationAnimationCreator {
    public static Animator createAnimation(View view, TransitionValues transitionValues, int i, int i2, float f, float f2, float f3, float f4, TimeInterpolator timeInterpolator, Transition transition) {
        float translationX = view.getTranslationX();
        float translationY = view.getTranslationY();
        int[] iArr = (int[]) transitionValues.view.getTag(R$id.transitionPosition);
        if (iArr != null) {
            f = ((float) (iArr[0] - i)) + translationX;
            f2 = ((float) (iArr[1] - i2)) + translationY;
        }
        int round = Math.round(f - translationX) + i;
        int round2 = i2 + Math.round(f2 - translationY);
        view.setTranslationX(f);
        view.setTranslationY(f2);
        if (f == f3 && f2 == f4) {
            return null;
        }
        Path path = new Path();
        path.moveTo(f, f2);
        path.lineTo(f3, f4);
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, View.TRANSLATION_Y, path);
        TransitionPositionListener transitionPositionListener = new TransitionPositionListener(view, transitionValues.view, round, round2, translationX, translationY);
        transition.addListener(transitionPositionListener);
        ofFloat.addListener(transitionPositionListener);
        ofFloat.addPauseListener(transitionPositionListener);
        ofFloat.setInterpolator(timeInterpolator);
        return ofFloat;
    }

    public static class TransitionPositionListener extends AnimatorListenerAdapter implements Transition.TransitionListener {
        public final View mMovingView;
        public float mPausedX;
        public float mPausedY;
        public final int mStartX;
        public final int mStartY;
        public final float mTerminalX;
        public final float mTerminalY;
        public int[] mTransitionPosition;
        public final View mViewInHierarchy;

        public void onAnimationEnd(Animator animator) {
        }

        public void onTransitionCancel(Transition transition) {
        }

        public void onTransitionPause(Transition transition) {
        }

        public void onTransitionResume(Transition transition) {
        }

        public void onTransitionStart(Transition transition) {
        }

        public TransitionPositionListener(View view, View view2, int i, int i2, float f, float f2) {
            this.mMovingView = view;
            this.mViewInHierarchy = view2;
            this.mStartX = i - Math.round(view.getTranslationX());
            this.mStartY = i2 - Math.round(view.getTranslationY());
            this.mTerminalX = f;
            this.mTerminalY = f2;
            int i3 = R$id.transitionPosition;
            int[] iArr = (int[]) view2.getTag(i3);
            this.mTransitionPosition = iArr;
            if (iArr != null) {
                view2.setTag(i3, (Object) null);
            }
        }

        public void onAnimationCancel(Animator animator) {
            if (this.mTransitionPosition == null) {
                this.mTransitionPosition = new int[2];
            }
            this.mTransitionPosition[0] = Math.round(((float) this.mStartX) + this.mMovingView.getTranslationX());
            this.mTransitionPosition[1] = Math.round(((float) this.mStartY) + this.mMovingView.getTranslationY());
            this.mViewInHierarchy.setTag(R$id.transitionPosition, this.mTransitionPosition);
        }

        public void onAnimationPause(Animator animator) {
            this.mPausedX = this.mMovingView.getTranslationX();
            this.mPausedY = this.mMovingView.getTranslationY();
            this.mMovingView.setTranslationX(this.mTerminalX);
            this.mMovingView.setTranslationY(this.mTerminalY);
        }

        public void onAnimationResume(Animator animator) {
            this.mMovingView.setTranslationX(this.mPausedX);
            this.mMovingView.setTranslationY(this.mPausedY);
        }

        public void onTransitionEnd(Transition transition) {
            this.mMovingView.setTranslationX(this.mTerminalX);
            this.mMovingView.setTranslationY(this.mTerminalY);
        }
    }
}
