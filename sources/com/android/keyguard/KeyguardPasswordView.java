package com.android.keyguard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Insets;
import android.graphics.Rect;
import android.os.CancellationSignal;
import android.os.Trace;
import android.util.AttributeSet;
import android.view.WindowInsets;
import android.view.WindowInsetsAnimationControlListener;
import android.view.WindowInsetsAnimationController;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.TextView;
import com.android.internal.widget.LockscreenCredential;
import com.android.internal.widget.TextViewInputDisabler;
import com.android.systemui.DejankUtils;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.R$string;
import com.android.systemui.animation.Interpolators;

public class KeyguardPasswordView extends KeyguardAbsKeyInputView {
    public final int mDisappearYTranslation;
    public Interpolator mFastOutLinearInInterpolator;
    public Interpolator mLinearOutSlowInInterpolator;
    public TextView mPasswordEntry;
    public TextViewInputDisabler mPasswordEntryDisabler;

    public KeyguardPasswordView(Context context) {
        this(context, (AttributeSet) null);
    }

    public KeyguardPasswordView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mDisappearYTranslation = getResources().getDimensionPixelSize(R$dimen.disappear_y_translation);
        this.mLinearOutSlowInInterpolator = AnimationUtils.loadInterpolator(context, 17563662);
        this.mFastOutLinearInInterpolator = AnimationUtils.loadInterpolator(context, 17563663);
    }

    public int getPasswordTextViewId() {
        return R$id.passwordEntry;
    }

    public int getPromptReasonStringRes(int i) {
        if (i == 0) {
            return 0;
        }
        if (i == 1) {
            return R$string.kg_prompt_reason_restart_password;
        }
        if (i == 2) {
            return R$string.kg_prompt_reason_timeout_password;
        }
        if (i == 3) {
            return R$string.kg_prompt_reason_device_admin;
        }
        if (i == 4) {
            return R$string.kg_prompt_reason_user_request;
        }
        if (i == 6) {
            return R$string.kg_prompt_reason_timeout_password;
        }
        if (i != 7) {
            return R$string.kg_prompt_reason_timeout_password;
        }
        return R$string.kg_prompt_reason_timeout_password;
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        this.mPasswordEntry = (TextView) findViewById(getPasswordTextViewId());
        this.mPasswordEntryDisabler = new TextViewInputDisabler(this.mPasswordEntry);
    }

    public boolean onRequestFocusInDescendants(int i, Rect rect) {
        return this.mPasswordEntry.requestFocus(i, rect);
    }

    public void resetPasswordText(boolean z, boolean z2) {
        this.mPasswordEntry.setText("");
    }

    public LockscreenCredential getEnteredCredential() {
        return LockscreenCredential.createPasswordOrNone(this.mPasswordEntry.getText());
    }

    public void setPasswordEntryEnabled(boolean z) {
        this.mPasswordEntry.setEnabled(z);
    }

    public void setPasswordEntryInputEnabled(boolean z) {
        this.mPasswordEntryDisabler.setInputEnabled(z);
    }

    public int getWrongPasswordStringId() {
        return R$string.kg_wrong_password;
    }

    public void startAppearAnimation() {
        setAlpha(0.0f);
        animate().alpha(1.0f).setDuration(300).start();
        setTranslationY(0.0f);
    }

    public boolean startDisappearAnimation(final Runnable runnable) {
        getWindowInsetsController().controlWindowInsetsAnimation(WindowInsets.Type.ime(), 100, Interpolators.LINEAR, (CancellationSignal) null, new WindowInsetsAnimationControlListener() {
            public void onFinished(WindowInsetsAnimationController windowInsetsAnimationController) {
            }

            public void onReady(final WindowInsetsAnimationController windowInsetsAnimationController, int i) {
                ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{1.0f, 0.0f});
                ofFloat.addUpdateListener(new KeyguardPasswordView$1$$ExternalSyntheticLambda0(windowInsetsAnimationController, ofFloat));
                ofFloat.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationStart(Animator animator) {
                    }

                    public void onAnimationEnd(Animator animator) {
                        DejankUtils.postAfterTraversal(new KeyguardPasswordView$1$1$$ExternalSyntheticLambda0(this, windowInsetsAnimationController, runnable));
                    }

                    /* access modifiers changed from: private */
                    public /* synthetic */ void lambda$onAnimationEnd$0(WindowInsetsAnimationController windowInsetsAnimationController, Runnable runnable) {
                        Trace.beginSection("KeyguardPasswordView#onAnimationEnd");
                        windowInsetsAnimationController.finish(false);
                        KeyguardPasswordView.this.runOnFinishImeAnimationRunnable();
                        runnable.run();
                        Trace.endSection();
                    }
                });
                ofFloat.setInterpolator(Interpolators.FAST_OUT_LINEAR_IN);
                ofFloat.start();
            }

            public static /* synthetic */ void lambda$onReady$0(WindowInsetsAnimationController windowInsetsAnimationController, ValueAnimator valueAnimator, ValueAnimator valueAnimator2) {
                if (!windowInsetsAnimationController.isCancelled()) {
                    Insets shownStateInsets = windowInsetsAnimationController.getShownStateInsets();
                    windowInsetsAnimationController.setInsetsAndAlpha(Insets.add(shownStateInsets, Insets.of(0, 0, 0, (int) (((float) ((-shownStateInsets.bottom) / 4)) * valueAnimator.getAnimatedFraction()))), ((Float) valueAnimator2.getAnimatedValue()).floatValue(), valueAnimator.getAnimatedFraction());
                }
            }

            public void onCancelled(WindowInsetsAnimationController windowInsetsAnimationController) {
                KeyguardPasswordView.this.runOnFinishImeAnimationRunnable();
                runnable.run();
            }
        });
        return true;
    }

    public CharSequence getTitle() {
        return getResources().getString(17040520);
    }
}