package com.android.wm.shell.compatui.letterboxedu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.IntProperty;
import android.util.Log;
import android.util.Property;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.animation.Animation;
import com.android.internal.R;
import com.android.internal.policy.TransitionAnimation;
import com.android.wm.shell.transition.Transitions;

public class LetterboxEduAnimationController {
    public static final Property<Drawable, Integer> DRAWABLE_ALPHA = new IntProperty<Drawable>("alpha") {
        public void setValue(Drawable drawable, int i) {
            drawable.setAlpha(i);
        }

        public Integer get(Drawable drawable) {
            return Integer.valueOf(drawable.getAlpha());
        }
    };
    public static final int ENTER_ANIM_START_DELAY_MILLIS = (Transitions.ENABLE_SHELL_TRANSITIONS ? 300 : 500);
    public final int mAnimStyleResId;
    public Animator mBackgroundDimAnimator;
    public Animation mDialogAnimation;
    public final String mPackageName;
    public final TransitionAnimation mTransitionAnimation;

    public static /* synthetic */ void lambda$startExitAnimation$2() {
    }

    public LetterboxEduAnimationController(Context context) {
        this.mTransitionAnimation = new TransitionAnimation(context, false, "LetterboxEduAnimation");
        this.mAnimStyleResId = new ContextThemeWrapper(context, 16974550).getTheme().obtainStyledAttributes(R.styleable.Window).getResourceId(8, 0);
        this.mPackageName = context.getPackageName();
    }

    public void startEnterAnimation(LetterboxEduDialogLayout letterboxEduDialogLayout, Runnable runnable) {
        cancelAnimation();
        View dialogContainer = letterboxEduDialogLayout.getDialogContainer();
        Animation loadAnimation = loadAnimation(0);
        this.mDialogAnimation = loadAnimation;
        if (loadAnimation == null) {
            runnable.run();
            return;
        }
        loadAnimation.setAnimationListener(getAnimationListener(new LetterboxEduAnimationController$$ExternalSyntheticLambda0(dialogContainer), new LetterboxEduAnimationController$$ExternalSyntheticLambda1(this, runnable)));
        Animator alphaAnimator = getAlphaAnimator(letterboxEduDialogLayout.getBackgroundDim(), 204, this.mDialogAnimation.getDuration());
        this.mBackgroundDimAnimator = alphaAnimator;
        alphaAnimator.addListener(getDimAnimatorListener());
        Animation animation = this.mDialogAnimation;
        int i = ENTER_ANIM_START_DELAY_MILLIS;
        animation.setStartOffset((long) i);
        this.mBackgroundDimAnimator.setStartDelay((long) i);
        dialogContainer.startAnimation(this.mDialogAnimation);
        this.mBackgroundDimAnimator.start();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$startEnterAnimation$1(Runnable runnable) {
        this.mDialogAnimation = null;
        runnable.run();
    }

    public void startExitAnimation(LetterboxEduDialogLayout letterboxEduDialogLayout, Runnable runnable) {
        cancelAnimation();
        View dialogContainer = letterboxEduDialogLayout.getDialogContainer();
        Animation loadAnimation = loadAnimation(1);
        this.mDialogAnimation = loadAnimation;
        if (loadAnimation == null) {
            runnable.run();
            return;
        }
        loadAnimation.setAnimationListener(getAnimationListener(new LetterboxEduAnimationController$$ExternalSyntheticLambda2(), new LetterboxEduAnimationController$$ExternalSyntheticLambda3(this, dialogContainer, runnable)));
        Animator alphaAnimator = getAlphaAnimator(letterboxEduDialogLayout.getBackgroundDim(), 0, this.mDialogAnimation.getDuration());
        this.mBackgroundDimAnimator = alphaAnimator;
        alphaAnimator.addListener(getDimAnimatorListener());
        dialogContainer.startAnimation(this.mDialogAnimation);
        this.mBackgroundDimAnimator.start();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$startExitAnimation$3(View view, Runnable runnable) {
        view.setAlpha(0.0f);
        this.mDialogAnimation = null;
        runnable.run();
    }

    public void cancelAnimation() {
        Animation animation = this.mDialogAnimation;
        if (animation != null) {
            animation.cancel();
            this.mDialogAnimation = null;
        }
        Animator animator = this.mBackgroundDimAnimator;
        if (animator != null) {
            animator.cancel();
            this.mBackgroundDimAnimator = null;
        }
    }

    public final Animation loadAnimation(int i) {
        Animation loadAnimationAttr = this.mTransitionAnimation.loadAnimationAttr(this.mPackageName, this.mAnimStyleResId, i, false);
        if (loadAnimationAttr == null) {
            Log.e("LetterboxEduAnimation", "Failed to load animation " + i);
        }
        return loadAnimationAttr;
    }

    public final Animation.AnimationListener getAnimationListener(final Runnable runnable, final Runnable runnable2) {
        return new Animation.AnimationListener() {
            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
                runnable.run();
            }

            public void onAnimationEnd(Animation animation) {
                runnable2.run();
            }
        };
    }

    public final AnimatorListenerAdapter getDimAnimatorListener() {
        return new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                LetterboxEduAnimationController.this.mBackgroundDimAnimator = null;
            }
        };
    }

    public static Animator getAlphaAnimator(Drawable drawable, int i, long j) {
        ObjectAnimator ofInt = ObjectAnimator.ofInt(drawable, DRAWABLE_ALPHA, new int[]{i});
        ofInt.setDuration(j);
        return ofInt;
    }
}
