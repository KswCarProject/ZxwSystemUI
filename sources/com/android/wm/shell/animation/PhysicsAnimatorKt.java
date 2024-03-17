package com.android.wm.shell.animation;

import android.view.View;
import com.android.wm.shell.animation.PhysicsAnimator;
import java.util.WeakHashMap;
import org.jetbrains.annotations.NotNull;

/* compiled from: PhysicsAnimator.kt */
public final class PhysicsAnimatorKt {
    public static final float UNSET = -3.4028235E38f;
    @NotNull
    public static final WeakHashMap<Object, PhysicsAnimator<?>> animators = new WeakHashMap<>();
    @NotNull
    public static final PhysicsAnimator.FlingConfig globalDefaultFling = new PhysicsAnimator.FlingConfig(1.0f, -3.4028235E38f, Float.MAX_VALUE);
    @NotNull
    public static final PhysicsAnimator.SpringConfig globalDefaultSpring = new PhysicsAnimator.SpringConfig(1500.0f, 0.5f);
    public static boolean verboseLogging;

    @NotNull
    public static final <T extends View> PhysicsAnimator<T> getPhysicsAnimator(@NotNull T t) {
        return PhysicsAnimator.Companion.getInstance(t);
    }

    @NotNull
    public static final WeakHashMap<Object, PhysicsAnimator<?>> getAnimators() {
        return animators;
    }
}
