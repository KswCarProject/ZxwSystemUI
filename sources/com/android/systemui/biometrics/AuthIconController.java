package com.android.systemui.biometrics;

import android.content.Context;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: AuthBiometricIconController.kt */
public abstract class AuthIconController extends Animatable2.AnimationCallback {
    public final boolean actsAsConfirmButton;
    @NotNull
    public final Context context;
    public boolean deactivated;
    @NotNull
    public final ImageView iconView;

    public void handleAnimationEnd(@NotNull Drawable drawable) {
    }

    public abstract void updateIcon(int i, int i2);

    @NotNull
    public final Context getContext() {
        return this.context;
    }

    @NotNull
    public final ImageView getIconView() {
        return this.iconView;
    }

    public AuthIconController(@NotNull Context context2, @NotNull ImageView imageView) {
        this.context = context2;
        this.iconView = imageView;
    }

    public final void setDeactivated(boolean z) {
        this.deactivated = z;
    }

    public boolean getActsAsConfirmButton() {
        return this.actsAsConfirmButton;
    }

    public final void onAnimationStart(@NotNull Drawable drawable) {
        super.onAnimationStart(drawable);
    }

    public final void onAnimationEnd(@NotNull Drawable drawable) {
        super.onAnimationEnd(drawable);
        if (!this.deactivated) {
            handleAnimationEnd(drawable);
        }
    }

    public final void showStaticDrawable(int i) {
        this.iconView.setImageDrawable(this.context.getDrawable(i));
    }

    public final void animateIconOnce(int i) {
        animateIcon(i, false);
    }

    public final void animateIcon(int i, boolean z) {
        if (!this.deactivated) {
            Drawable drawable = this.context.getDrawable(i);
            if (drawable != null) {
                AnimatedVectorDrawable animatedVectorDrawable = (AnimatedVectorDrawable) drawable;
                this.iconView.setImageDrawable(animatedVectorDrawable);
                animatedVectorDrawable.forceAnimationOnUI();
                if (z) {
                    animatedVectorDrawable.registerAnimationCallback(this);
                }
                animatedVectorDrawable.start();
                return;
            }
            throw new NullPointerException("null cannot be cast to non-null type android.graphics.drawable.AnimatedVectorDrawable");
        }
    }

    public final void updateState(int i, int i2) {
        if (this.deactivated) {
            Log.w("AuthIconController", Intrinsics.stringPlus("Ignoring updateState when deactivated: ", Integer.valueOf(i2)));
        } else {
            updateIcon(i, i2);
        }
    }
}
