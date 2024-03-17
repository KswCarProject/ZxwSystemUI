package com.android.systemui.navigationbar.buttons;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import com.android.systemui.shared.rotation.RotationButton;
import com.android.systemui.shared.rotation.RotationButtonController;

public class RotationContextButton extends ContextualButton implements RotationButton {
    public RotationButtonController mRotationButtonController;

    public /* bridge */ /* synthetic */ Drawable getImageDrawable() {
        return super.getImageDrawable();
    }

    public RotationContextButton(int i, Context context, int i2) {
        super(i, context, i2);
    }

    public void setRotationButtonController(RotationButtonController rotationButtonController) {
        this.mRotationButtonController = rotationButtonController;
    }

    public void setUpdatesCallback(RotationButton.RotationButtonUpdatesCallback rotationButtonUpdatesCallback) {
        setListener(new RotationContextButton$$ExternalSyntheticLambda0(rotationButtonUpdatesCallback));
    }

    public static /* synthetic */ void lambda$setUpdatesCallback$0(RotationButton.RotationButtonUpdatesCallback rotationButtonUpdatesCallback, ContextualButton contextualButton, boolean z) {
        if (rotationButtonUpdatesCallback != null) {
            rotationButtonUpdatesCallback.onVisibilityChanged(z);
        }
    }

    public void setVisibility(int i) {
        super.setVisibility(i);
        KeyButtonDrawable imageDrawable = getImageDrawable();
        if (i == 0 && imageDrawable != null) {
            imageDrawable.resetAnimation();
            imageDrawable.startAnimation();
        }
    }

    public KeyButtonDrawable getNewDrawable(int i, int i2) {
        return KeyButtonDrawable.create(this.mRotationButtonController.getContext(), i, i2, this.mRotationButtonController.getIconResId(), false, (Color) null);
    }

    public boolean acceptRotationProposal() {
        View currentView = getCurrentView();
        return currentView != null && currentView.isAttachedToWindow();
    }
}
