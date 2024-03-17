package com.android.keyguard;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnimationUtils;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import com.android.settingslib.animation.AppearAnimationUtils;
import com.android.settingslib.animation.DisappearAnimationUtils;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.R$string;

public class KeyguardPINView extends KeyguardPinBasedInputView {
    public final AppearAnimationUtils mAppearAnimationUtils;
    public ConstraintLayout mContainer;
    public final DisappearAnimationUtils mDisappearAnimationUtils;
    public final DisappearAnimationUtils mDisappearAnimationUtilsLocked;
    public int mDisappearYTranslation;
    public int mLastDevicePosture;
    public View[][] mViews;

    public boolean hasOverlappingRendering() {
        return false;
    }

    public KeyguardPINView(Context context) {
        this(context, (AttributeSet) null);
    }

    public KeyguardPINView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mLastDevicePosture = 0;
        this.mAppearAnimationUtils = new AppearAnimationUtils(context);
        Context context2 = context;
        this.mDisappearAnimationUtils = new DisappearAnimationUtils(context2, 125, 0.6f, 0.45f, AnimationUtils.loadInterpolator(this.mContext, 17563663));
        this.mDisappearAnimationUtilsLocked = new DisappearAnimationUtils(context2, 187, 0.6f, 0.45f, AnimationUtils.loadInterpolator(this.mContext, 17563663));
        this.mDisappearYTranslation = getResources().getDimensionPixelSize(R$dimen.disappear_y_translation);
    }

    public void onConfigurationChanged(Configuration configuration) {
        updateMargins();
    }

    public void onDevicePostureChanged(int i) {
        this.mLastDevicePosture = i;
        updateMargins();
    }

    public int getPasswordTextViewId() {
        return R$id.pinEntry;
    }

    public final void updateMargins() {
        int dimensionPixelSize = this.mContext.getResources().getDimensionPixelSize(R$dimen.num_pad_entry_row_margin_bottom);
        int dimensionPixelSize2 = this.mContext.getResources().getDimensionPixelSize(R$dimen.num_pad_key_margin_end);
        String string = this.mContext.getResources().getString(R$string.num_pad_key_ratio);
        for (int i = 1; i < 5; i++) {
            for (int i2 = 0; i2 < 3; i2++) {
                View view = this.mViews[i][i2];
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                layoutParams.dimensionRatio = string;
                if (i != 4) {
                    layoutParams.bottomMargin = dimensionPixelSize;
                }
                if (i2 != 2) {
                    layoutParams.rightMargin = dimensionPixelSize2;
                }
                view.setLayoutParams(layoutParams);
            }
        }
        float f = this.mContext.getResources().getFloat(R$dimen.half_opened_bouncer_height_ratio);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this.mContainer);
        int i3 = R$id.pin_pad_top_guideline;
        if (this.mLastDevicePosture != 2) {
            f = 0.0f;
        }
        constraintSet.setGuidelinePercent(i3, f);
        constraintSet.applyTo(this.mContainer);
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        this.mContainer = (ConstraintLayout) findViewById(R$id.pin_container);
        this.mViews = new View[][]{new View[]{findViewById(R$id.row0), null, null}, new View[]{findViewById(R$id.key1), findViewById(R$id.key2), findViewById(R$id.key3)}, new View[]{findViewById(R$id.key4), findViewById(R$id.key5), findViewById(R$id.key6)}, new View[]{findViewById(R$id.key7), findViewById(R$id.key8), findViewById(R$id.key9)}, new View[]{findViewById(R$id.delete_button), findViewById(R$id.key0), findViewById(R$id.key_enter)}, new View[]{null, this.mEcaView, null}};
    }

    public int getWrongPasswordStringId() {
        return R$string.kg_wrong_pin;
    }

    public void startAppearAnimation() {
        enableClipping(false);
        setAlpha(1.0f);
        setTranslationY(this.mAppearAnimationUtils.getStartTranslation());
        AppearAnimationUtils.startTranslationYAnimation(this, 0, 500, 0.0f, this.mAppearAnimationUtils.getInterpolator(), getAnimationListener(19));
        this.mAppearAnimationUtils.startAnimation2d(this.mViews, new Runnable() {
            public void run() {
                KeyguardPINView.this.enableClipping(true);
            }
        });
    }

    public boolean startDisappearAnimation(boolean z, Runnable runnable) {
        DisappearAnimationUtils disappearAnimationUtils;
        enableClipping(false);
        setTranslationY(0.0f);
        if (z) {
            disappearAnimationUtils = this.mDisappearAnimationUtilsLocked;
        } else {
            disappearAnimationUtils = this.mDisappearAnimationUtils;
        }
        disappearAnimationUtils.createAnimation(this, 0, 200, (float) this.mDisappearYTranslation, false, this.mDisappearAnimationUtils.getInterpolator(), new KeyguardPINView$$ExternalSyntheticLambda0(this, runnable), getAnimationListener(22));
        return true;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$startDisappearAnimation$0(Runnable runnable) {
        enableClipping(true);
        if (runnable != null) {
            runnable.run();
        }
    }

    public final void enableClipping(boolean z) {
        this.mContainer.setClipToPadding(z);
        this.mContainer.setClipChildren(z);
        setClipChildren(z);
    }
}
