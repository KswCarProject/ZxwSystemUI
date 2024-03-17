package com.android.systemui.biometrics;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.hardware.biometrics.PromptInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.widget.LockPatternUtils;
import com.android.systemui.R$color;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.R$string;
import com.android.systemui.biometrics.AuthDialog;
import com.android.systemui.util.LargeScreenUtils;
import java.util.ArrayList;
import java.util.Set;

public class AuthBiometricView extends LinearLayout {
    public final AccessibilityManager mAccessibilityManager;
    @VisibleForTesting
    public int mAnimationDurationHideDialog;
    @VisibleForTesting
    public int mAnimationDurationLong;
    @VisibleForTesting
    public int mAnimationDurationShort;
    public final View.OnClickListener mBackgroundClickListener;
    public Callback mCallback;
    @VisibleForTesting
    public Button mCancelButton;
    @VisibleForTesting
    public Button mConfirmButton;
    public TextView mDescriptionView;
    public boolean mDialogSizeAnimating;
    public int mEffectiveUserId;
    public final Handler mHandler;
    @VisibleForTesting
    public AuthIconController mIconController;
    public View mIconHolderView;
    public float mIconOriginalY;
    public ImageView mIconView;
    public TextView mIndicatorView;
    @VisibleForTesting
    public AuthDialog.LayoutParams mLayoutParams;
    public final LockPatternUtils mLockPatternUtils;
    @VisibleForTesting
    public Button mNegativeButton;
    public AuthPanelController mPanelController;
    public PromptInfo mPromptInfo;
    public boolean mRequireConfirmation;
    public final Runnable mResetErrorRunnable;
    public final Runnable mResetHelpRunnable;
    public Bundle mSavedState;
    public int mSize;
    public int mState;
    public TextView mSubtitleView;
    public final int mTextColorError;
    public final int mTextColorHint;
    public TextView mTitleView;
    @VisibleForTesting
    public Button mTryAgainButton;
    @VisibleForTesting
    public Button mUseCredentialButton;
    public int mUserId;

    public interface Callback {
        void onAction(int i);
    }

    public boolean forceRequireConfirmation(int i) {
        return false;
    }

    public int getDelayAfterAuthenticatedDurationMs() {
        return 0;
    }

    public int getStateForAfterError() {
        return 0;
    }

    public void handleResetAfterError() {
    }

    public void handleResetAfterHelp() {
    }

    public boolean ignoreUnsuccessfulEventsFrom(int i) {
        return false;
    }

    public boolean onPointerDown(Set<Integer> set) {
        return false;
    }

    public boolean supportsManualRetry() {
        return false;
    }

    public boolean supportsRequireConfirmation() {
        return false;
    }

    public boolean supportsSmallDialog() {
        return false;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(View view) {
        if (this.mState == 6) {
            Log.w("AuthBiometricView", "Ignoring background click after authenticated");
            return;
        }
        int i = this.mSize;
        if (i == 1) {
            Log.w("AuthBiometricView", "Ignoring background click during small dialog");
        } else if (i == 3) {
            Log.w("AuthBiometricView", "Ignoring background click during large dialog");
        } else {
            this.mCallback.onAction(2);
        }
    }

    public AuthBiometricView(Context context) {
        this(context, (AttributeSet) null);
    }

    public AuthBiometricView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mSize = 0;
        this.mAnimationDurationShort = 150;
        this.mAnimationDurationLong = 450;
        this.mAnimationDurationHideDialog = 2000;
        this.mBackgroundClickListener = new AuthBiometricView$$ExternalSyntheticLambda10(this);
        this.mHandler = new Handler(Looper.getMainLooper());
        this.mTextColorError = getResources().getColor(R$color.biometric_dialog_error, context.getTheme());
        this.mTextColorHint = getResources().getColor(R$color.biometric_dialog_gray, context.getTheme());
        this.mAccessibilityManager = (AccessibilityManager) context.getSystemService(AccessibilityManager.class);
        this.mLockPatternUtils = new LockPatternUtils(context);
        this.mResetErrorRunnable = new AuthBiometricView$$ExternalSyntheticLambda11(this);
        this.mResetHelpRunnable = new AuthBiometricView$$ExternalSyntheticLambda12(this);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1() {
        updateState(getStateForAfterError());
        handleResetAfterError();
        Utils.notifyAccessibilityContentChanged(this.mAccessibilityManager, this);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2() {
        updateState(2);
        handleResetAfterHelp();
        Utils.notifyAccessibilityContentChanged(this.mAccessibilityManager, this);
    }

    public int getConfirmationPrompt() {
        return R$string.biometric_dialog_tap_confirm;
    }

    public AuthIconController createIconController() {
        return new AuthIconController(this.mContext, this.mIconView) {
            public void updateIcon(int i, int i2) {
            }
        };
    }

    public void setPanelController(AuthPanelController authPanelController) {
        this.mPanelController = authPanelController;
    }

    public void setPromptInfo(PromptInfo promptInfo) {
        this.mPromptInfo = promptInfo;
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    public void setBackgroundView(View view) {
        view.setOnClickListener(this.mBackgroundClickListener);
    }

    public void setUserId(int i) {
        this.mUserId = i;
    }

    public void setEffectiveUserId(int i) {
        this.mEffectiveUserId = i;
    }

    public void setRequireConfirmation(boolean z) {
        this.mRequireConfirmation = z && supportsRequireConfirmation();
    }

    @VisibleForTesting
    public final void updateSize(final int i) {
        Log.v("AuthBiometricView", "Current size: " + this.mSize + " New size: " + i);
        if (i == 1) {
            this.mTitleView.setVisibility(8);
            this.mSubtitleView.setVisibility(8);
            this.mDescriptionView.setVisibility(8);
            this.mIndicatorView.setVisibility(8);
            this.mNegativeButton.setVisibility(8);
            this.mUseCredentialButton.setVisibility(8);
            float dimension = getResources().getDimension(R$dimen.biometric_dialog_icon_padding);
            this.mIconHolderView.setY(((float) (getHeight() - this.mIconHolderView.getHeight())) - dimension);
            this.mPanelController.updateForContentDimensions(this.mLayoutParams.mMediumWidth, ((this.mIconHolderView.getHeight() + (((int) dimension) * 2)) - this.mIconHolderView.getPaddingTop()) - this.mIconHolderView.getPaddingBottom(), 0);
            this.mSize = i;
        } else if (this.mSize == 1 && i == 2) {
            if (!this.mDialogSizeAnimating) {
                this.mDialogSizeAnimating = true;
                ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{this.mIconHolderView.getY(), this.mIconOriginalY});
                ofFloat.addUpdateListener(new AuthBiometricView$$ExternalSyntheticLambda0(this));
                ValueAnimator ofFloat2 = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
                ofFloat2.addUpdateListener(new AuthBiometricView$$ExternalSyntheticLambda1(this));
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.setDuration((long) this.mAnimationDurationShort);
                animatorSet.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationStart(Animator animator) {
                        super.onAnimationStart(animator);
                        AuthBiometricView.this.mTitleView.setVisibility(0);
                        AuthBiometricView.this.mIndicatorView.setVisibility(0);
                        if (AuthBiometricView.this.isDeviceCredentialAllowed()) {
                            AuthBiometricView.this.mUseCredentialButton.setVisibility(0);
                        } else {
                            AuthBiometricView.this.mNegativeButton.setVisibility(0);
                        }
                        if (AuthBiometricView.this.supportsManualRetry()) {
                            AuthBiometricView.this.mTryAgainButton.setVisibility(0);
                        }
                        if (!TextUtils.isEmpty(AuthBiometricView.this.mSubtitleView.getText())) {
                            AuthBiometricView.this.mSubtitleView.setVisibility(0);
                        }
                        if (!TextUtils.isEmpty(AuthBiometricView.this.mDescriptionView.getText())) {
                            AuthBiometricView.this.mDescriptionView.setVisibility(0);
                        }
                    }

                    public void onAnimationEnd(Animator animator) {
                        super.onAnimationEnd(animator);
                        AuthBiometricView.this.mSize = i;
                        AuthBiometricView authBiometricView = AuthBiometricView.this;
                        authBiometricView.mDialogSizeAnimating = false;
                        Utils.notifyAccessibilityContentChanged(authBiometricView.mAccessibilityManager, AuthBiometricView.this);
                    }
                });
                animatorSet.play(ofFloat).with(ofFloat2);
                animatorSet.start();
                AuthPanelController authPanelController = this.mPanelController;
                AuthDialog.LayoutParams layoutParams = this.mLayoutParams;
                authPanelController.updateForContentDimensions(layoutParams.mMediumWidth, layoutParams.mMediumHeight, 150);
            } else {
                return;
            }
        } else if (i == 2) {
            AuthPanelController authPanelController2 = this.mPanelController;
            AuthDialog.LayoutParams layoutParams2 = this.mLayoutParams;
            authPanelController2.updateForContentDimensions(layoutParams2.mMediumWidth, layoutParams2.mMediumHeight, 0);
            this.mSize = i;
        } else if (i == 3) {
            ValueAnimator ofFloat3 = ValueAnimator.ofFloat(new float[]{getY(), getY() - getResources().getDimension(R$dimen.biometric_dialog_medium_to_large_translation_offset)});
            ofFloat3.setDuration((long) this.mAnimationDurationLong);
            ofFloat3.addUpdateListener(new AuthBiometricView$$ExternalSyntheticLambda2(this));
            ofFloat3.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                    if (this.getParent() instanceof ViewGroup) {
                        ((ViewGroup) this.getParent()).removeView(this);
                    }
                    AuthBiometricView.this.mSize = i;
                }
            });
            ValueAnimator ofFloat4 = ValueAnimator.ofFloat(new float[]{1.0f, 0.0f});
            ofFloat4.setDuration((long) (this.mAnimationDurationLong / 2));
            ofFloat4.addUpdateListener(new AuthBiometricView$$ExternalSyntheticLambda3(this));
            this.mPanelController.setUseFullScreen(true);
            AuthPanelController authPanelController3 = this.mPanelController;
            authPanelController3.updateForContentDimensions(authPanelController3.getContainerWidth(), this.mPanelController.getContainerHeight(), this.mAnimationDurationLong);
            AnimatorSet animatorSet2 = new AnimatorSet();
            ArrayList arrayList = new ArrayList();
            arrayList.add(ofFloat3);
            arrayList.add(ofFloat4);
            animatorSet2.playTogether(arrayList);
            animatorSet2.setDuration((long) ((this.mAnimationDurationLong * 2) / 3));
            animatorSet2.start();
        } else {
            Log.e("AuthBiometricView", "Unknown transition from: " + this.mSize + " to: " + i);
        }
        Utils.notifyAccessibilityContentChanged(this.mAccessibilityManager, this);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateSize$3(ValueAnimator valueAnimator) {
        this.mIconHolderView.setY(((Float) valueAnimator.getAnimatedValue()).floatValue());
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateSize$4(ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.mTitleView.setAlpha(floatValue);
        this.mIndicatorView.setAlpha(floatValue);
        this.mNegativeButton.setAlpha(floatValue);
        this.mCancelButton.setAlpha(floatValue);
        this.mTryAgainButton.setAlpha(floatValue);
        if (!TextUtils.isEmpty(this.mSubtitleView.getText())) {
            this.mSubtitleView.setAlpha(floatValue);
        }
        if (!TextUtils.isEmpty(this.mDescriptionView.getText())) {
            this.mDescriptionView.setAlpha(floatValue);
        }
    }

    public void updateState(int i) {
        Log.v("AuthBiometricView", "newState: " + i);
        this.mIconController.updateState(this.mState, i);
        if (i == 1 || i == 2) {
            removePendingAnimations();
            if (this.mRequireConfirmation) {
                this.mConfirmButton.setEnabled(false);
                this.mConfirmButton.setVisibility(0);
            }
        } else if (i != 4) {
            int i2 = 8;
            if (i == 5) {
                removePendingAnimations();
                this.mNegativeButton.setVisibility(8);
                this.mCancelButton.setVisibility(0);
                this.mUseCredentialButton.setVisibility(8);
                this.mConfirmButton.setEnabled(this.mRequireConfirmation);
                Button button = this.mConfirmButton;
                if (this.mRequireConfirmation) {
                    i2 = 0;
                }
                button.setVisibility(i2);
                this.mIndicatorView.setTextColor(this.mTextColorHint);
                this.mIndicatorView.setText(getConfirmationPrompt());
                this.mIndicatorView.setVisibility(0);
            } else if (i != 6) {
                Log.w("AuthBiometricView", "Unhandled state: " + i);
            } else {
                if (this.mSize != 1) {
                    this.mConfirmButton.setVisibility(8);
                    this.mNegativeButton.setVisibility(8);
                    this.mUseCredentialButton.setVisibility(8);
                    this.mCancelButton.setVisibility(8);
                    this.mIndicatorView.setVisibility(4);
                }
                announceForAccessibility(getResources().getString(R$string.biometric_dialog_authenticated));
                this.mHandler.postDelayed(new AuthBiometricView$$ExternalSyntheticLambda13(this), (long) getDelayAfterAuthenticatedDurationMs());
            }
        } else if (this.mSize == 1) {
            updateSize(2);
        }
        Utils.notifyAccessibilityContentChanged(this.mAccessibilityManager, this);
        this.mState = i;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateState$7() {
        this.mCallback.onAction(1);
    }

    public void onDialogAnimatedIn() {
        updateState(2);
    }

    public void onAuthenticationSucceeded(int i) {
        removePendingAnimations();
        if (this.mRequireConfirmation || forceRequireConfirmation(i)) {
            updateState(5);
        } else {
            updateState(6);
        }
    }

    public void onAuthenticationFailed(int i, String str) {
        if (!ignoreUnsuccessfulEventsFrom(i)) {
            showTemporaryMessage(str, this.mResetErrorRunnable);
            updateState(4);
        }
    }

    public void onError(int i, String str) {
        if (!ignoreUnsuccessfulEventsFrom(i)) {
            showTemporaryMessage(str, this.mResetErrorRunnable);
            updateState(4);
            this.mHandler.postDelayed(new AuthBiometricView$$ExternalSyntheticLambda14(this), (long) this.mAnimationDurationHideDialog);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onError$8() {
        this.mCallback.onAction(5);
    }

    public void onHelp(int i, String str) {
        if (!ignoreUnsuccessfulEventsFrom(i)) {
            if (this.mSize != 2) {
                Log.w("AuthBiometricView", "Help received in size: " + this.mSize);
            } else if (TextUtils.isEmpty(str)) {
                Log.w("AuthBiometricView", "Ignoring blank help message");
            } else {
                showTemporaryMessage(str, this.mResetHelpRunnable);
                updateState(3);
            }
        }
    }

    public void onSaveState(Bundle bundle) {
        bundle.putInt("confirm_visibility", this.mConfirmButton.getVisibility());
        bundle.putInt("try_agian_visibility", this.mTryAgainButton.getVisibility());
        bundle.putInt("state", this.mState);
        bundle.putString("indicator_string", this.mIndicatorView.getText() != null ? this.mIndicatorView.getText().toString() : "");
        bundle.putBoolean("error_is_temporary", this.mHandler.hasCallbacks(this.mResetErrorRunnable));
        bundle.putBoolean("hint_is_temporary", this.mHandler.hasCallbacks(this.mResetHelpRunnable));
        bundle.putInt("size", this.mSize);
    }

    public void restoreState(Bundle bundle) {
        this.mSavedState = bundle;
    }

    public final void setTextOrHide(TextView textView, CharSequence charSequence) {
        if (TextUtils.isEmpty(charSequence)) {
            textView.setVisibility(8);
        } else {
            textView.setText(charSequence);
        }
        Utils.notifyAccessibilityContentChanged(this.mAccessibilityManager, this);
    }

    public final void removePendingAnimations() {
        this.mHandler.removeCallbacks(this.mResetHelpRunnable);
        this.mHandler.removeCallbacks(this.mResetErrorRunnable);
    }

    public final void showTemporaryMessage(String str, Runnable runnable) {
        removePendingAnimations();
        this.mIndicatorView.setText(str);
        this.mIndicatorView.setTextColor(this.mTextColorError);
        boolean z = false;
        this.mIndicatorView.setVisibility(0);
        TextView textView = this.mIndicatorView;
        if (!this.mAccessibilityManager.isEnabled() || !this.mAccessibilityManager.isTouchExplorationEnabled()) {
            z = true;
        }
        textView.setSelected(z);
        this.mHandler.postDelayed(runnable, (long) this.mAnimationDurationHideDialog);
        Utils.notifyAccessibilityContentChanged(this.mAccessibilityManager, this);
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        this.mTitleView = (TextView) findViewById(R$id.title);
        this.mSubtitleView = (TextView) findViewById(R$id.subtitle);
        this.mDescriptionView = (TextView) findViewById(R$id.description);
        this.mIconView = (ImageView) findViewById(R$id.biometric_icon);
        this.mIconHolderView = findViewById(R$id.biometric_icon_frame);
        this.mIndicatorView = (TextView) findViewById(R$id.indicator);
        this.mNegativeButton = (Button) findViewById(R$id.button_negative);
        this.mCancelButton = (Button) findViewById(R$id.button_cancel);
        this.mUseCredentialButton = (Button) findViewById(R$id.button_use_credential);
        this.mConfirmButton = (Button) findViewById(R$id.button_confirm);
        this.mTryAgainButton = (Button) findViewById(R$id.button_try_again);
        this.mNegativeButton.setOnClickListener(new AuthBiometricView$$ExternalSyntheticLambda4(this));
        this.mCancelButton.setOnClickListener(new AuthBiometricView$$ExternalSyntheticLambda5(this));
        this.mUseCredentialButton.setOnClickListener(new AuthBiometricView$$ExternalSyntheticLambda6(this));
        this.mConfirmButton.setOnClickListener(new AuthBiometricView$$ExternalSyntheticLambda7(this));
        this.mTryAgainButton.setOnClickListener(new AuthBiometricView$$ExternalSyntheticLambda8(this));
        AuthIconController createIconController = createIconController();
        this.mIconController = createIconController;
        if (createIconController.getActsAsConfirmButton()) {
            this.mIconView.setOnClickListener(new AuthBiometricView$$ExternalSyntheticLambda9(this));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onFinishInflate$9(View view) {
        this.mCallback.onAction(3);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onFinishInflate$10(View view) {
        this.mCallback.onAction(2);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onFinishInflate$11(View view) {
        startTransitionToCredentialUI();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onFinishInflate$12(View view) {
        updateState(6);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onFinishInflate$13(View view) {
        updateState(2);
        this.mCallback.onAction(4);
        this.mTryAgainButton.setVisibility(8);
        Utils.notifyAccessibilityContentChanged(this.mAccessibilityManager, this);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onFinishInflate$14(View view) {
        if (this.mState == 5) {
            updateState(6);
        }
    }

    public void startTransitionToCredentialUI() {
        updateSize(3);
        this.mCallback.onAction(6);
    }

    public void onAttachedToWindow() {
        String str;
        super.onAttachedToWindow();
        this.mTitleView.setText(this.mPromptInfo.getTitle());
        this.mTitleView.setSelected(true);
        this.mSubtitleView.setSelected(true);
        this.mDescriptionView.setMovementMethod(new ScrollingMovementMethod());
        if (isDeviceCredentialAllowed()) {
            int credentialType = Utils.getCredentialType(this.mLockPatternUtils, this.mEffectiveUserId);
            if (credentialType != 1) {
                str = credentialType != 2 ? getResources().getString(R$string.biometric_dialog_use_password) : getResources().getString(R$string.biometric_dialog_use_pattern);
            } else {
                str = getResources().getString(R$string.biometric_dialog_use_pin);
            }
            this.mNegativeButton.setVisibility(8);
            this.mUseCredentialButton.setText(str);
            this.mUseCredentialButton.setVisibility(0);
        } else {
            this.mNegativeButton.setText(this.mPromptInfo.getNegativeButtonText());
        }
        setTextOrHide(this.mSubtitleView, this.mPromptInfo.getSubtitle());
        setTextOrHide(this.mDescriptionView, this.mPromptInfo.getDescription());
        Bundle bundle = this.mSavedState;
        if (bundle == null) {
            updateState(1);
            return;
        }
        updateState(bundle.getInt("state"));
        this.mConfirmButton.setVisibility(this.mSavedState.getInt("confirm_visibility"));
        if (this.mConfirmButton.getVisibility() == 8) {
            setRequireConfirmation(false);
        }
        this.mTryAgainButton.setVisibility(this.mSavedState.getInt("try_agian_visibility"));
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mIconController.setDeactivated(true);
        this.mHandler.removeCallbacksAndMessages((Object) null);
    }

    public AuthDialog.LayoutParams onMeasureInternal(int i, int i2) {
        int childCount = getChildCount();
        int i3 = 0;
        for (int i4 = 0; i4 < childCount; i4++) {
            View childAt = getChildAt(i4);
            if (childAt.getId() == R$id.space_above_icon || childAt.getId() == R$id.space_below_icon || childAt.getId() == R$id.button_bar) {
                childAt.measure(View.MeasureSpec.makeMeasureSpec(i, 1073741824), View.MeasureSpec.makeMeasureSpec(childAt.getLayoutParams().height, 1073741824));
            } else if (childAt.getId() == R$id.biometric_icon_frame) {
                View findViewById = findViewById(R$id.biometric_icon);
                childAt.measure(View.MeasureSpec.makeMeasureSpec(findViewById.getLayoutParams().width, 1073741824), View.MeasureSpec.makeMeasureSpec(findViewById.getLayoutParams().height, 1073741824));
            } else if (childAt.getId() == R$id.biometric_icon) {
                childAt.measure(View.MeasureSpec.makeMeasureSpec(i, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(i2, Integer.MIN_VALUE));
            } else {
                childAt.measure(View.MeasureSpec.makeMeasureSpec(i, 1073741824), View.MeasureSpec.makeMeasureSpec(i2, Integer.MIN_VALUE));
            }
            if (childAt.getVisibility() != 8) {
                i3 += childAt.getMeasuredHeight();
            }
        }
        return new AuthDialog.LayoutParams(i, i3);
    }

    public final boolean isLargeDisplay() {
        return LargeScreenUtils.shouldUseSplitNotificationShade(getResources());
    }

    public void onMeasure(int i, int i2) {
        int i3;
        int size = View.MeasureSpec.getSize(i);
        int size2 = View.MeasureSpec.getSize(i2);
        if (isLargeDisplay()) {
            i3 = (Math.min(size, size2) * 2) / 3;
        } else {
            i3 = Math.min(size, size2);
        }
        AuthDialog.LayoutParams onMeasureInternal = onMeasureInternal(i3, size2);
        this.mLayoutParams = onMeasureInternal;
        setMeasuredDimension(onMeasureInternal.mMediumWidth, onMeasureInternal.mMediumHeight);
    }

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (this.mIconOriginalY == 0.0f) {
            this.mIconOriginalY = this.mIconHolderView.getY();
            Bundle bundle = this.mSavedState;
            if (bundle == null) {
                updateSize((this.mRequireConfirmation || !supportsSmallDialog()) ? 2 : 1);
                return;
            }
            updateSize(bundle.getInt("size"));
            String string = this.mSavedState.getString("indicator_string");
            if (this.mSavedState.getBoolean("hint_is_temporary")) {
                onHelp(0, string);
            } else if (this.mSavedState.getBoolean("error_is_temporary")) {
                onAuthenticationFailed(0, string);
            }
        }
    }

    public final boolean isDeviceCredentialAllowed() {
        return Utils.isDeviceCredentialAllowed(this.mPromptInfo);
    }

    public int getSize() {
        return this.mSize;
    }
}
