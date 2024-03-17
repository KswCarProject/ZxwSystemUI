package com.android.systemui.media;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: MetadataAnimationHandler.kt */
public class MetadataAnimationHandler extends AnimatorListenerAdapter {
    @NotNull
    public final Animator enterAnimator;
    @NotNull
    public final Animator exitAnimator;
    @Nullable
    public Function0<Unit> postEnterUpdate;
    @Nullable
    public Function0<Unit> postExitUpdate;
    @Nullable
    public Object targetData;

    public MetadataAnimationHandler(@NotNull Animator animator, @NotNull Animator animator2) {
        this.exitAnimator = animator;
        this.enterAnimator = animator2;
        animator.addListener(this);
        animator2.addListener(this);
    }

    public final boolean isRunning() {
        return this.enterAnimator.isRunning() || this.exitAnimator.isRunning();
    }

    public final boolean setNext(@NotNull Object obj, @NotNull Function0<Unit> function0, @NotNull Function0<Unit> function02) {
        if (Intrinsics.areEqual(obj, this.targetData)) {
            return false;
        }
        this.targetData = obj;
        this.postExitUpdate = function0;
        this.postEnterUpdate = function02;
        if (isRunning()) {
            return true;
        }
        this.exitAnimator.start();
        return true;
    }

    public void onAnimationEnd(@NotNull Animator animator) {
        if (animator == this.exitAnimator) {
            Function0<Unit> function0 = this.postExitUpdate;
            if (function0 != null) {
                function0.invoke();
            }
            this.postExitUpdate = null;
            this.enterAnimator.start();
        }
        if (animator != this.enterAnimator) {
            return;
        }
        if (this.postExitUpdate != null) {
            this.exitAnimator.start();
            return;
        }
        Function0<Unit> function02 = this.postEnterUpdate;
        if (function02 != null) {
            function02.invoke();
        }
        this.postEnterUpdate = null;
    }
}
