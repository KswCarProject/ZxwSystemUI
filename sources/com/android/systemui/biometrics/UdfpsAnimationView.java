package com.android.systemui.biometrics;

import android.content.Context;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public abstract class UdfpsAnimationView extends FrameLayout {
    public int mAlpha;
    public float mDialogSuggestedAlpha = 1.0f;
    public float mNotificationShadeExpansion = 0.0f;
    public boolean mPauseAuth;

    public boolean dozeTimeTick() {
        return false;
    }

    public final int expansionToAlpha(float f) {
        if (f >= 0.4f) {
            return 0;
        }
        return (int) ((1.0f - (f / 0.4f)) * 255.0f);
    }

    public abstract UdfpsDrawable getDrawable();

    public UdfpsAnimationView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void onSensorRectUpdated(RectF rectF) {
        getDrawable().onSensorRectUpdated(rectF);
    }

    public void onIlluminationStarting() {
        getDrawable().setIlluminationShowing(true);
        getDrawable().invalidateSelf();
    }

    public void onIlluminationStopped() {
        getDrawable().setIlluminationShowing(false);
        getDrawable().invalidateSelf();
    }

    public boolean setPauseAuth(boolean z) {
        if (z == this.mPauseAuth) {
            return false;
        }
        this.mPauseAuth = z;
        updateAlpha();
        return true;
    }

    public int updateAlpha() {
        int calculateAlpha = calculateAlpha();
        getDrawable().setAlpha(calculateAlpha);
        if (!this.mPauseAuth || calculateAlpha != 0 || getParent() == null) {
            ((ViewGroup) getParent()).setVisibility(0);
        } else {
            ((ViewGroup) getParent()).setVisibility(4);
        }
        return calculateAlpha;
    }

    public int calculateAlpha() {
        int expansionToAlpha = (int) (((float) expansionToAlpha(this.mNotificationShadeExpansion)) * this.mDialogSuggestedAlpha);
        this.mAlpha = expansionToAlpha;
        if (this.mPauseAuth) {
            return expansionToAlpha;
        }
        return 255;
    }

    public boolean isPauseAuth() {
        return this.mPauseAuth;
    }

    public void setDialogSuggestedAlpha(float f) {
        this.mDialogSuggestedAlpha = f;
        updateAlpha();
    }

    public float getDialogSuggestedAlpha() {
        return this.mDialogSuggestedAlpha;
    }

    public void onExpansionChanged(float f) {
        this.mNotificationShadeExpansion = f;
        updateAlpha();
    }
}
