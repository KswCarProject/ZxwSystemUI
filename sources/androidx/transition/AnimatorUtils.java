package androidx.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;

public class AnimatorUtils {
    public static void addPauseListener(Animator animator, AnimatorListenerAdapter animatorListenerAdapter) {
        animator.addPauseListener(animatorListenerAdapter);
    }

    public static void pause(Animator animator) {
        animator.pause();
    }

    public static void resume(Animator animator) {
        animator.resume();
    }
}
