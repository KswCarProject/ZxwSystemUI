package com.android.systemui.shared.rotation;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import androidx.core.view.OneShotPreDrawListener;
import com.android.systemui.shared.rotation.FloatingRotationButtonPositionCalculator;
import com.android.systemui.shared.rotation.RotationButton;

public class FloatingRotationButton implements RotationButton {
    public AnimatedVectorDrawable mAnimatedDrawable;
    public final int mButtonDiameterResource;
    public boolean mCanShow = true;
    public int mContainerSize;
    public final int mContentDescriptionResource;
    public final Context mContext;
    public int mDisplayRotation;
    public boolean mIsShowing;
    public boolean mIsTaskbarStashed = false;
    public boolean mIsTaskbarVisible = false;
    public final ViewGroup mKeyButtonContainer;
    public final FloatingRotationButtonView mKeyButtonView;
    public final int mMinMarginResource;
    public FloatingRotationButtonPositionCalculator.Position mPosition;
    public FloatingRotationButtonPositionCalculator mPositionCalculator;
    public RotationButtonController mRotationButtonController;
    public final int mRoundedContentPaddingResource;
    public final int mTaskbarBottomMarginResource;
    public final int mTaskbarLeftMarginResource;
    public RotationButton.RotationButtonUpdatesCallback mUpdatesCallback;
    public final WindowManager mWindowManager;

    public FloatingRotationButton(Context context, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9) {
        this.mWindowManager = (WindowManager) context.getSystemService(WindowManager.class);
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(context).inflate(i2, (ViewGroup) null);
        this.mKeyButtonContainer = viewGroup;
        FloatingRotationButtonView floatingRotationButtonView = (FloatingRotationButtonView) viewGroup.findViewById(i3);
        this.mKeyButtonView = floatingRotationButtonView;
        floatingRotationButtonView.setVisibility(0);
        floatingRotationButtonView.setContentDescription(context.getString(i));
        floatingRotationButtonView.setRipple(i9);
        this.mContext = context;
        this.mContentDescriptionResource = i;
        this.mMinMarginResource = i4;
        this.mRoundedContentPaddingResource = i5;
        this.mTaskbarLeftMarginResource = i6;
        this.mTaskbarBottomMarginResource = i7;
        this.mButtonDiameterResource = i8;
        updateDimensionResources();
    }

    public final void updateDimensionResources() {
        Resources resources = this.mContext.getResources();
        int max = Math.max(resources.getDimensionPixelSize(this.mMinMarginResource), resources.getDimensionPixelSize(this.mRoundedContentPaddingResource));
        int dimensionPixelSize = resources.getDimensionPixelSize(this.mTaskbarLeftMarginResource);
        int dimensionPixelSize2 = resources.getDimensionPixelSize(this.mTaskbarBottomMarginResource);
        this.mPositionCalculator = new FloatingRotationButtonPositionCalculator(max, dimensionPixelSize, dimensionPixelSize2);
        this.mContainerSize = resources.getDimensionPixelSize(this.mButtonDiameterResource) + Math.max(max, Math.max(dimensionPixelSize, dimensionPixelSize2));
    }

    public void setRotationButtonController(RotationButtonController rotationButtonController) {
        this.mRotationButtonController = rotationButtonController;
        updateIcon(rotationButtonController.getLightIconColor(), this.mRotationButtonController.getDarkIconColor());
    }

    public void setUpdatesCallback(RotationButton.RotationButtonUpdatesCallback rotationButtonUpdatesCallback) {
        this.mUpdatesCallback = rotationButtonUpdatesCallback;
    }

    public View getCurrentView() {
        return this.mKeyButtonView;
    }

    public boolean show() {
        if (!this.mCanShow || this.mIsShowing) {
            return false;
        }
        this.mIsShowing = true;
        this.mWindowManager.addView(this.mKeyButtonContainer, adjustViewPositionAndCreateLayoutParams());
        AnimatedVectorDrawable animatedVectorDrawable = this.mAnimatedDrawable;
        if (animatedVectorDrawable != null) {
            animatedVectorDrawable.reset();
            this.mAnimatedDrawable.start();
        }
        OneShotPreDrawListener.add(this.mKeyButtonView, new FloatingRotationButton$$ExternalSyntheticLambda1(this));
        return true;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$show$0() {
        RotationButton.RotationButtonUpdatesCallback rotationButtonUpdatesCallback;
        if (this.mIsShowing && (rotationButtonUpdatesCallback = this.mUpdatesCallback) != null) {
            rotationButtonUpdatesCallback.onVisibilityChanged(true);
        }
    }

    public boolean hide() {
        if (!this.mIsShowing) {
            return false;
        }
        this.mWindowManager.removeViewImmediate(this.mKeyButtonContainer);
        this.mIsShowing = false;
        RotationButton.RotationButtonUpdatesCallback rotationButtonUpdatesCallback = this.mUpdatesCallback;
        if (rotationButtonUpdatesCallback == null) {
            return true;
        }
        rotationButtonUpdatesCallback.onVisibilityChanged(false);
        return true;
    }

    public boolean isVisible() {
        return this.mIsShowing;
    }

    public void updateIcon(int i, int i2) {
        AnimatedVectorDrawable animatedVectorDrawable = (AnimatedVectorDrawable) this.mKeyButtonView.getContext().getDrawable(this.mRotationButtonController.getIconResId());
        this.mAnimatedDrawable = animatedVectorDrawable;
        this.mKeyButtonView.setImageDrawable(animatedVectorDrawable);
        this.mKeyButtonView.setColors(i, i2);
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.mKeyButtonView.setOnClickListener(onClickListener);
    }

    public void setOnHoverListener(View.OnHoverListener onHoverListener) {
        this.mKeyButtonView.setOnHoverListener(onHoverListener);
    }

    public Drawable getImageDrawable() {
        return this.mAnimatedDrawable;
    }

    public void setDarkIntensity(float f) {
        this.mKeyButtonView.setDarkIntensity(f);
    }

    public void setCanShowRotationButton(boolean z) {
        this.mCanShow = z;
        if (!z) {
            hide();
        }
    }

    public void onTaskbarStateChanged(boolean z, boolean z2) {
        this.mIsTaskbarVisible = z;
        this.mIsTaskbarStashed = z2;
        if (this.mIsShowing) {
            FloatingRotationButtonPositionCalculator.Position calculatePosition = this.mPositionCalculator.calculatePosition(this.mDisplayRotation, z, z2);
            if (calculatePosition.getTranslationX() != this.mPosition.getTranslationX() || calculatePosition.getTranslationY() != this.mPosition.getTranslationY()) {
                updateTranslation(calculatePosition, true);
                this.mPosition = calculatePosition;
            }
        }
    }

    public void onConfigurationChanged(int i) {
        if (!((i & 4096) == 0 && (i & 1024) == 0)) {
            updateDimensionResources();
            if (this.mIsShowing) {
                this.mWindowManager.updateViewLayout(this.mKeyButtonContainer, adjustViewPositionAndCreateLayoutParams());
            }
        }
        if ((i & 4) != 0) {
            this.mKeyButtonView.setContentDescription(this.mContext.getString(this.mContentDescriptionResource));
        }
    }

    public final WindowManager.LayoutParams adjustViewPositionAndCreateLayoutParams() {
        int i = this.mContainerSize;
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(i, i, 0, 0, 2024, 8, -3);
        layoutParams.privateFlags |= 16;
        layoutParams.setTitle("FloatingRotationButton");
        layoutParams.setFitInsetsTypes(0);
        int rotation = this.mWindowManager.getDefaultDisplay().getRotation();
        this.mDisplayRotation = rotation;
        FloatingRotationButtonPositionCalculator.Position calculatePosition = this.mPositionCalculator.calculatePosition(rotation, this.mIsTaskbarVisible, this.mIsTaskbarStashed);
        this.mPosition = calculatePosition;
        layoutParams.gravity = calculatePosition.getGravity();
        ((FrameLayout.LayoutParams) this.mKeyButtonView.getLayoutParams()).gravity = this.mPosition.getGravity();
        updateTranslation(this.mPosition, false);
        return layoutParams;
    }

    public final void updateTranslation(FloatingRotationButtonPositionCalculator.Position position, boolean z) {
        int translationX = position.getTranslationX();
        int translationY = position.getTranslationY();
        if (z) {
            this.mKeyButtonView.animate().translationX((float) translationX).translationY((float) translationY).setDuration(300).setInterpolator(new AccelerateDecelerateInterpolator()).withEndAction(new FloatingRotationButton$$ExternalSyntheticLambda0(this)).start();
            return;
        }
        this.mKeyButtonView.setTranslationX((float) translationX);
        this.mKeyButtonView.setTranslationY((float) translationY);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateTranslation$1() {
        RotationButton.RotationButtonUpdatesCallback rotationButtonUpdatesCallback = this.mUpdatesCallback;
        if (rotationButtonUpdatesCallback != null && this.mIsShowing) {
            rotationButtonUpdatesCallback.onPositionChanged();
        }
    }
}
