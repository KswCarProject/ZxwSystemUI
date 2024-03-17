package com.android.systemui.biometrics;

import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.UserInfo;
import android.graphics.drawable.Drawable;
import android.hardware.biometrics.PromptInfo;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.os.UserManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.accessibility.AccessibilityManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.internal.widget.LockPatternUtils;
import com.android.internal.widget.VerifyCredentialResponse;
import com.android.systemui.R$dimen;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import com.android.systemui.R$string;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.util.concurrency.DelayableExecutor;

public abstract class AuthCredentialView extends LinearLayout {
    public final AccessibilityManager mAccessibilityManager = ((AccessibilityManager) this.mContext.getSystemService(AccessibilityManager.class));
    public DelayableExecutor mBackgroundExecutor;
    public Callback mCallback;
    public final Runnable mClearErrorRunnable = new Runnable() {
        public void run() {
            TextView textView = AuthCredentialView.this.mErrorView;
            if (textView != null) {
                textView.setText("");
            }
        }
    };
    public AuthContainerView mContainerView;
    public int mCredentialType;
    public TextView mDescriptionView;
    public final DevicePolicyManager mDevicePolicyManager = ((DevicePolicyManager) this.mContext.getSystemService(DevicePolicyManager.class));
    public int mEffectiveUserId;
    public ErrorTimer mErrorTimer;
    public TextView mErrorView;
    public final Handler mHandler = new Handler(Looper.getMainLooper());
    public ImageView mIconView;
    public final LockPatternUtils mLockPatternUtils = new LockPatternUtils(this.mContext);
    public long mOperationId;
    public AuthPanelController mPanelController;
    public AsyncTask<?, ?, ?> mPendingLockCheck;
    public PromptInfo mPromptInfo;
    public boolean mShouldAnimateContents;
    public boolean mShouldAnimatePanel;
    public TextView mSubtitleView;
    public TextView mTitleView;
    public int mUserId;
    public final UserManager mUserManager = ((UserManager) this.mContext.getSystemService(UserManager.class));

    public interface Callback {
        void onCredentialMatched(byte[] bArr);
    }

    public static String getLastAttemptBeforeWipeProfileUpdatableStringId(int i) {
        return i != 1 ? i != 2 ? "SystemUi.BIOMETRIC_DIALOG_WORK_PASSWORD_LAST_ATTEMPT" : "SystemUi.BIOMETRIC_DIALOG_WORK_PATTERN_LAST_ATTEMPT" : "SystemUi.BIOMETRIC_DIALOG_WORK_PIN_LAST_ATTEMPT";
    }

    public final String getNowWipingUpdatableStringId(int i) {
        return i != 2 ? "UNDEFINED" : "SystemUi.BIOMETRIC_DIALOG_WORK_LOCK_FAILED_ATTEMPTS";
    }

    public void onErrorTimeoutFinish() {
    }

    public static class ErrorTimer extends CountDownTimer {
        public final Context mContext;
        public final TextView mErrorView;

        public ErrorTimer(Context context, long j, long j2, TextView textView) {
            super(j, j2);
            this.mErrorView = textView;
            this.mContext = context;
        }

        public void onTick(long j) {
            this.mErrorView.setText(this.mContext.getString(R$string.biometric_dialog_credential_too_many_attempts, new Object[]{Integer.valueOf((int) (j / 1000))}));
        }
    }

    public AuthCredentialView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void showError(String str) {
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeCallbacks(this.mClearErrorRunnable);
            this.mHandler.postDelayed(this.mClearErrorRunnable, 3000);
        }
        TextView textView = this.mErrorView;
        if (textView != null) {
            textView.setText(str);
        }
    }

    public final void setTextOrHide(TextView textView, CharSequence charSequence) {
        if (TextUtils.isEmpty(charSequence)) {
            textView.setVisibility(8);
        } else {
            textView.setText(charSequence);
        }
        Utils.notifyAccessibilityContentChanged(this.mAccessibilityManager, this);
    }

    public final void setText(TextView textView, CharSequence charSequence) {
        textView.setText(charSequence);
    }

    public void setUserId(int i) {
        this.mUserId = i;
    }

    public void setOperationId(long j) {
        this.mOperationId = j;
    }

    public void setEffectiveUserId(int i) {
        this.mEffectiveUserId = i;
    }

    public void setCredentialType(int i) {
        this.mCredentialType = i;
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    public void setPromptInfo(PromptInfo promptInfo) {
        this.mPromptInfo = promptInfo;
    }

    public void setPanelController(AuthPanelController authPanelController, boolean z) {
        this.mPanelController = authPanelController;
        this.mShouldAnimatePanel = z;
    }

    public void setShouldAnimateContents(boolean z) {
        this.mShouldAnimateContents = z;
    }

    public void setContainerView(AuthContainerView authContainerView) {
        this.mContainerView = authContainerView;
    }

    public void setBackgroundExecutor(DelayableExecutor delayableExecutor) {
        this.mBackgroundExecutor = delayableExecutor;
    }

    public void onAttachedToWindow() {
        Drawable drawable;
        super.onAttachedToWindow();
        CharSequence title = getTitle(this.mPromptInfo);
        setText(this.mTitleView, title);
        setTextOrHide(this.mSubtitleView, getSubtitle(this.mPromptInfo));
        setTextOrHide(this.mDescriptionView, getDescription(this.mPromptInfo));
        announceForAccessibility(title);
        if (this.mIconView != null) {
            if (Utils.isManagedProfile(this.mContext, this.mEffectiveUserId)) {
                drawable = getResources().getDrawable(R$drawable.auth_dialog_enterprise, this.mContext.getTheme());
            } else {
                drawable = getResources().getDrawable(R$drawable.auth_dialog_lock, this.mContext.getTheme());
            }
            this.mIconView.setImageDrawable(drawable);
        }
        if (this.mShouldAnimateContents) {
            setTranslationY(getResources().getDimension(R$dimen.biometric_dialog_credential_translation_offset));
            setAlpha(0.0f);
            postOnAnimation(new AuthCredentialView$$ExternalSyntheticLambda0(this));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onAttachedToWindow$0() {
        animate().translationY(0.0f).setDuration(150).alpha(1.0f).setInterpolator(Interpolators.LINEAR_OUT_SLOW_IN).withLayer().start();
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ErrorTimer errorTimer = this.mErrorTimer;
        if (errorTimer != null) {
            errorTimer.cancel();
        }
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        this.mTitleView = (TextView) findViewById(R$id.title);
        this.mSubtitleView = (TextView) findViewById(R$id.subtitle);
        this.mDescriptionView = (TextView) findViewById(R$id.description);
        this.mIconView = (ImageView) findViewById(R$id.icon);
        this.mErrorView = (TextView) findViewById(R$id.error);
    }

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (this.mShouldAnimatePanel) {
            this.mPanelController.setUseFullScreen(true);
            AuthPanelController authPanelController = this.mPanelController;
            authPanelController.updateForContentDimensions(authPanelController.getContainerWidth(), this.mPanelController.getContainerHeight(), 0);
            this.mShouldAnimatePanel = false;
        }
    }

    public void onCredentialVerified(VerifyCredentialResponse verifyCredentialResponse, int i) {
        int i2;
        if (verifyCredentialResponse.isMatched()) {
            this.mClearErrorRunnable.run();
            this.mLockPatternUtils.userPresent(this.mEffectiveUserId);
            long gatekeeperPasswordHandle = verifyCredentialResponse.getGatekeeperPasswordHandle();
            this.mCallback.onCredentialMatched(this.mLockPatternUtils.verifyGatekeeperPasswordHandle(gatekeeperPasswordHandle, this.mOperationId, this.mEffectiveUserId).getGatekeeperHAT());
            this.mLockPatternUtils.removeGatekeeperPasswordHandle(gatekeeperPasswordHandle);
        } else if (i > 0) {
            this.mHandler.removeCallbacks(this.mClearErrorRunnable);
            AnonymousClass2 r0 = new ErrorTimer(this.mContext, this.mLockPatternUtils.setLockoutAttemptDeadline(this.mEffectiveUserId, i) - SystemClock.elapsedRealtime(), 1000, this.mErrorView) {
                public void onFinish() {
                    AuthCredentialView.this.onErrorTimeoutFinish();
                    AuthCredentialView.this.mClearErrorRunnable.run();
                }
            };
            this.mErrorTimer = r0;
            r0.start();
        } else if (!reportFailedAttempt()) {
            int i3 = this.mCredentialType;
            if (i3 == 1) {
                i2 = R$string.biometric_dialog_wrong_pin;
            } else if (i3 != 2) {
                i2 = R$string.biometric_dialog_wrong_password;
            } else {
                i2 = R$string.biometric_dialog_wrong_pattern;
            }
            showError(getResources().getString(i2));
        }
    }

    public final boolean reportFailedAttempt() {
        boolean updateErrorMessage = updateErrorMessage(this.mLockPatternUtils.getCurrentFailedPasswordAttempts(this.mEffectiveUserId) + 1);
        this.mLockPatternUtils.reportFailedPasswordAttempt(this.mEffectiveUserId);
        return updateErrorMessage;
    }

    public final boolean updateErrorMessage(int i) {
        int maximumFailedPasswordsForWipe = this.mLockPatternUtils.getMaximumFailedPasswordsForWipe(this.mEffectiveUserId);
        if (maximumFailedPasswordsForWipe <= 0 || i <= 0) {
            return false;
        }
        if (this.mErrorView != null) {
            showError(getResources().getString(R$string.biometric_dialog_credential_attempts_before_wipe, new Object[]{Integer.valueOf(i), Integer.valueOf(maximumFailedPasswordsForWipe)}));
        }
        int i2 = maximumFailedPasswordsForWipe - i;
        if (i2 == 1) {
            showLastAttemptBeforeWipeDialog();
        } else if (i2 <= 0) {
            showNowWipingDialog();
        }
        return true;
    }

    public final void showLastAttemptBeforeWipeDialog() {
        this.mBackgroundExecutor.execute(new AuthCredentialView$$ExternalSyntheticLambda1(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$showLastAttemptBeforeWipeDialog$1() {
        AlertDialog create = new AlertDialog.Builder(this.mContext).setTitle(R$string.biometric_dialog_last_attempt_before_wipe_dialog_title).setMessage(getLastAttemptBeforeWipeMessage(getUserTypeForWipe(), this.mCredentialType)).setPositiveButton(17039370, (DialogInterface.OnClickListener) null).create();
        create.getWindow().setType(2017);
        this.mHandler.post(new AuthCredentialView$$ExternalSyntheticLambda3(create));
    }

    public final void showNowWipingDialog() {
        this.mBackgroundExecutor.execute(new AuthCredentialView$$ExternalSyntheticLambda2(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$showNowWipingDialog$3() {
        AlertDialog create = new AlertDialog.Builder(this.mContext).setMessage(getNowWipingMessage(getUserTypeForWipe())).setPositiveButton(com.android.settingslib.R$string.failed_attempts_now_wiping_dialog_dismiss, (DialogInterface.OnClickListener) null).setOnDismissListener(new AuthCredentialView$$ExternalSyntheticLambda4(this)).create();
        create.getWindow().setType(2017);
        this.mHandler.post(new AuthCredentialView$$ExternalSyntheticLambda3(create));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$showNowWipingDialog$2(DialogInterface dialogInterface) {
        this.mContainerView.animateAway(5);
    }

    public final int getUserTypeForWipe() {
        UserInfo userInfo = this.mUserManager.getUserInfo(this.mDevicePolicyManager.getProfileWithMinimumFailedPasswordsForWipe(this.mEffectiveUserId));
        if (userInfo == null || userInfo.isPrimary()) {
            return 1;
        }
        return userInfo.isManagedProfile() ? 2 : 3;
    }

    public final String getLastAttemptBeforeWipeMessage(int i, int i2) {
        if (i == 1) {
            return getLastAttemptBeforeWipeDeviceMessage(i2);
        }
        if (i == 2) {
            return getLastAttemptBeforeWipeProfileMessage(i2);
        }
        if (i == 3) {
            return getLastAttemptBeforeWipeUserMessage(i2);
        }
        throw new IllegalArgumentException("Unrecognized user type:" + i);
    }

    public final String getLastAttemptBeforeWipeDeviceMessage(int i) {
        if (i == 1) {
            return this.mContext.getString(R$string.biometric_dialog_last_pin_attempt_before_wipe_device);
        }
        if (i != 2) {
            return this.mContext.getString(R$string.biometric_dialog_last_password_attempt_before_wipe_device);
        }
        return this.mContext.getString(R$string.biometric_dialog_last_pattern_attempt_before_wipe_device);
    }

    public final String getLastAttemptBeforeWipeProfileMessage(int i) {
        return this.mDevicePolicyManager.getResources().getString(getLastAttemptBeforeWipeProfileUpdatableStringId(i), new AuthCredentialView$$ExternalSyntheticLambda6(this, i));
    }

    /* renamed from: getLastAttemptBeforeWipeProfileDefaultMessage */
    public final String lambda$getLastAttemptBeforeWipeProfileMessage$4(int i) {
        int i2;
        if (i == 1) {
            i2 = R$string.biometric_dialog_last_pin_attempt_before_wipe_profile;
        } else if (i != 2) {
            i2 = R$string.biometric_dialog_last_password_attempt_before_wipe_profile;
        } else {
            i2 = R$string.biometric_dialog_last_pattern_attempt_before_wipe_profile;
        }
        return this.mContext.getString(i2);
    }

    public final String getLastAttemptBeforeWipeUserMessage(int i) {
        int i2;
        if (i == 1) {
            i2 = R$string.biometric_dialog_last_pin_attempt_before_wipe_user;
        } else if (i != 2) {
            i2 = R$string.biometric_dialog_last_password_attempt_before_wipe_user;
        } else {
            i2 = R$string.biometric_dialog_last_pattern_attempt_before_wipe_user;
        }
        return this.mContext.getString(i2);
    }

    public final String getNowWipingMessage(int i) {
        return this.mDevicePolicyManager.getResources().getString(getNowWipingUpdatableStringId(i), new AuthCredentialView$$ExternalSyntheticLambda5(this, i));
    }

    /* renamed from: getNowWipingDefaultMessage */
    public final String lambda$getNowWipingMessage$5(int i) {
        int i2;
        if (i == 1) {
            i2 = com.android.settingslib.R$string.failed_attempts_now_wiping_device;
        } else if (i == 2) {
            i2 = com.android.settingslib.R$string.failed_attempts_now_wiping_profile;
        } else if (i == 3) {
            i2 = com.android.settingslib.R$string.failed_attempts_now_wiping_user;
        } else {
            throw new IllegalArgumentException("Unrecognized user type:" + i);
        }
        return this.mContext.getString(i2);
    }

    public static CharSequence getTitle(PromptInfo promptInfo) {
        CharSequence deviceCredentialTitle = promptInfo.getDeviceCredentialTitle();
        return deviceCredentialTitle != null ? deviceCredentialTitle : promptInfo.getTitle();
    }

    public static CharSequence getSubtitle(PromptInfo promptInfo) {
        CharSequence deviceCredentialSubtitle = promptInfo.getDeviceCredentialSubtitle();
        return deviceCredentialSubtitle != null ? deviceCredentialSubtitle : promptInfo.getSubtitle();
    }

    public static CharSequence getDescription(PromptInfo promptInfo) {
        CharSequence deviceCredentialDescription = promptInfo.getDeviceCredentialDescription();
        return deviceCredentialDescription != null ? deviceCredentialDescription : promptInfo.getDescription();
    }
}
