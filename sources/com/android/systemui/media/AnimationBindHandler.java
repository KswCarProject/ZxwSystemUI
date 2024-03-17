package com.android.systemui.media;

import android.graphics.drawable.Animatable2;
import android.graphics.drawable.Drawable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: AnimationBindHandler.kt */
public final class AnimationBindHandler extends Animatable2.AnimationCallback {
    @NotNull
    public final List<Function0<Unit>> onAnimationsComplete = new ArrayList();
    @Nullable
    public Integer rebindId;
    @NotNull
    public final List<Animatable2> registrations = new ArrayList();

    public final boolean isAnimationRunning() {
        Iterable<Animatable2> iterable = this.registrations;
        if ((iterable instanceof Collection) && ((Collection) iterable).isEmpty()) {
            return false;
        }
        for (Animatable2 isRunning : iterable) {
            if (isRunning.isRunning()) {
                return true;
            }
        }
        return false;
    }

    public final boolean updateRebindId(@Nullable Integer num) {
        Integer num2 = this.rebindId;
        if (num2 != null && num != null && Intrinsics.areEqual((Object) num2, (Object) num)) {
            return false;
        }
        this.rebindId = num;
        return true;
    }

    public final void tryRegister(@Nullable Drawable drawable) {
        if (drawable instanceof Animatable2) {
            Animatable2 animatable2 = (Animatable2) drawable;
            animatable2.registerAnimationCallback(this);
            this.registrations.add(animatable2);
        }
    }

    public final void unregisterAll() {
        for (Animatable2 unregisterAnimationCallback : this.registrations) {
            unregisterAnimationCallback.unregisterAnimationCallback(this);
        }
        this.registrations.clear();
    }

    public final void tryExecute(@NotNull Function0<Unit> function0) {
        if (isAnimationRunning()) {
            this.onAnimationsComplete.add(function0);
        } else {
            function0.invoke();
        }
    }

    public void onAnimationEnd(@NotNull Drawable drawable) {
        super.onAnimationEnd(drawable);
        if (!isAnimationRunning()) {
            for (Function0 invoke : this.onAnimationsComplete) {
                invoke.invoke();
            }
            this.onAnimationsComplete.clear();
        }
    }
}
