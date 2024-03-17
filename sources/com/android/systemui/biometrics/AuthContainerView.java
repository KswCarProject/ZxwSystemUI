package com.android.systemui.biometrics;

import android.content.Context;
import android.hardware.biometrics.PromptInfo;
import android.hardware.face.FaceSensorPropertiesInternal;
import android.hardware.fingerprint.FingerprintSensorPropertiesInternal;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.UserManager;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.widget.LockPatternUtils;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.biometrics.AuthBiometricView;
import com.android.systemui.biometrics.AuthCredentialView;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.util.concurrency.DelayableExecutor;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AuthContainerView extends LinearLayout implements AuthDialog, WakefulnessLifecycle.Observer {
    public final DelayableExecutor mBackgroundExecutor;
    public final ImageView mBackgroundView;
    @VisibleForTesting
    public final BiometricCallback mBiometricCallback;
    public final ScrollView mBiometricScrollView;
    public AuthBiometricView mBiometricView;
    public final Config mConfig;
    public int mContainerState = 0;
    public byte[] mCredentialAttestation;
    public final CredentialCallback mCredentialCallback;
    public AuthCredentialView mCredentialView;
    public final int mEffectiveUserId;
    public final Set<Integer> mFailedModalities = new HashSet();
    public final FrameLayout mFrameLayout;
    public final Handler mHandler;
    public final Interpolator mLinearOutSlowIn;
    public final LockPatternUtils mLockPatternUtils;
    public final AuthPanelController mPanelController;
    public final View mPanelView;
    public Integer mPendingCallbackReason;
    public final float mTranslationY;
    public final WakefulnessLifecycle mWakefulnessLifecycle;
    public final WindowManager mWindowManager;
    public final IBinder mWindowToken = new Binder();

    @VisibleForTesting
    public static class Config {
        public AuthDialogCallback mCallback;
        public Context mContext;
        public int mMultiSensorConfig = 0;
        public String mOpPackageName;
        public long mOperationId;
        public PromptInfo mPromptInfo;
        public long mRequestId = -1;
        public boolean mRequireConfirmation;
        public int[] mSensorIds;
        public boolean mSkipAnimation = false;
        public boolean mSkipIntro;
        public int mUserId;
    }

    public static class Builder {
        public Config mConfig;

        public Builder(Context context) {
            Config config = new Config();
            this.mConfig = config;
            config.mContext = context;
        }

        public Builder setCallback(AuthDialogCallback authDialogCallback) {
            this.mConfig.mCallback = authDialogCallback;
            return this;
        }

        public Builder setPromptInfo(PromptInfo promptInfo) {
            this.mConfig.mPromptInfo = promptInfo;
            return this;
        }

        public Builder setRequireConfirmation(boolean z) {
            this.mConfig.mRequireConfirmation = z;
            return this;
        }

        public Builder setUserId(int i) {
            this.mConfig.mUserId = i;
            return this;
        }

        public Builder setOpPackageName(String str) {
            this.mConfig.mOpPackageName = str;
            return this;
        }

        public Builder setSkipIntro(boolean z) {
            this.mConfig.mSkipIntro = z;
            return this;
        }

        public Builder setOperationId(long j) {
            this.mConfig.mOperationId = j;
            return this;
        }

        public Builder setRequestId(long j) {
            this.mConfig.mRequestId = j;
            return this;
        }

        @VisibleForTesting
        public Builder setSkipAnimationDuration(boolean z) {
            this.mConfig.mSkipAnimation = z;
            return this;
        }

        public Builder setMultiSensorConfig(int i) {
            this.mConfig.mMultiSensorConfig = i;
            return this;
        }

        public AuthContainerView build(DelayableExecutor delayableExecutor, int[] iArr, List<FingerprintSensorPropertiesInternal> list, List<FaceSensorPropertiesInternal> list2, WakefulnessLifecycle wakefulnessLifecycle, UserManager userManager, LockPatternUtils lockPatternUtils) {
            this.mConfig.mSensorIds = iArr;
            return new AuthContainerView(this.mConfig, list, list2, wakefulnessLifecycle, userManager, lockPatternUtils, new Handler(Looper.getMainLooper()), delayableExecutor);
        }
    }

    @VisibleForTesting
    public final class BiometricCallback implements AuthBiometricView.Callback {
        public BiometricCallback() {
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onAction$0() {
            AuthContainerView.this.addCredentialView(false, true);
        }

        public void onAction(int i) {
            switch (i) {
                case 1:
                    AuthContainerView.this.animateAway(4);
                    return;
                case 2:
                    AuthContainerView.this.sendEarlyUserCanceled();
                    AuthContainerView.this.animateAway(1);
                    return;
                case 3:
                    AuthContainerView.this.animateAway(2);
                    return;
                case 4:
                    AuthContainerView.this.mFailedModalities.clear();
                    AuthContainerView.this.mConfig.mCallback.onTryAgainPressed();
                    return;
                case 5:
                    AuthContainerView.this.animateAway(5);
                    return;
                case 6:
                    AuthContainerView.this.mConfig.mCallback.onDeviceCredentialPressed();
                    AuthContainerView.this.mHandler.postDelayed(new AuthContainerView$BiometricCallback$$ExternalSyntheticLambda0(this), AuthContainerView.this.mConfig.mSkipAnimation ? 0 : 300);
                    return;
                default:
                    Log.e("AuthContainerView", "Unhandled action: " + i);
                    return;
            }
        }
    }

    public final class CredentialCallback implements AuthCredentialView.Callback {
        public CredentialCallback() {
        }

        public void onCredentialMatched(byte[] bArr) {
            AuthContainerView.this.mCredentialAttestation = bArr;
            AuthContainerView.this.animateAway(7);
        }
    }

    @VisibleForTesting
    public AuthContainerView(Config config, List<FingerprintSensorPropertiesInternal> list, List<FaceSensorPropertiesInternal> list2, WakefulnessLifecycle wakefulnessLifecycle, UserManager userManager, LockPatternUtils lockPatternUtils, Handler handler, DelayableExecutor delayableExecutor) {
        super(config.mContext);
        this.mConfig = config;
        this.mLockPatternUtils = lockPatternUtils;
        int credentialOwnerProfile = userManager.getCredentialOwnerProfile(config.mUserId);
        this.mEffectiveUserId = credentialOwnerProfile;
        this.mHandler = handler;
        this.mWindowManager = (WindowManager) this.mContext.getSystemService(WindowManager.class);
        this.mWakefulnessLifecycle = wakefulnessLifecycle;
        this.mTranslationY = getResources().getDimension(R$dimen.biometric_dialog_animation_translation_offset);
        this.mLinearOutSlowIn = Interpolators.LINEAR_OUT_SLOW_IN;
        BiometricCallback biometricCallback = new BiometricCallback();
        this.mBiometricCallback = biometricCallback;
        this.mCredentialCallback = new CredentialCallback();
        LayoutInflater from = LayoutInflater.from(this.mContext);
        FrameLayout frameLayout = (FrameLayout) from.inflate(R$layout.auth_container_view, this, false);
        this.mFrameLayout = frameLayout;
        addView(frameLayout);
        this.mBiometricScrollView = (ScrollView) frameLayout.findViewById(R$id.biometric_scrollview);
        ImageView imageView = (ImageView) frameLayout.findViewById(R$id.background);
        this.mBackgroundView = imageView;
        View findViewById = frameLayout.findViewById(R$id.panel);
        this.mPanelView = findViewById;
        AuthPanelController authPanelController = new AuthPanelController(this.mContext, findViewById);
        this.mPanelController = authPanelController;
        this.mBackgroundExecutor = delayableExecutor;
        if (Utils.isBiometricAllowed(config.mPromptInfo)) {
            FingerprintSensorPropertiesInternal findFirstSensorProperties = Utils.findFirstSensorProperties(list, config.mSensorIds);
            FaceSensorPropertiesInternal findFirstSensorProperties2 = Utils.findFirstSensorProperties(list2, config.mSensorIds);
            if (findFirstSensorProperties != null && findFirstSensorProperties2 != null) {
                AuthBiometricFingerprintAndFaceView authBiometricFingerprintAndFaceView = (AuthBiometricFingerprintAndFaceView) from.inflate(R$layout.auth_biometric_fingerprint_and_face_view, (ViewGroup) null, false);
                authBiometricFingerprintAndFaceView.setSensorProperties(findFirstSensorProperties);
                this.mBiometricView = authBiometricFingerprintAndFaceView;
            } else if (findFirstSensorProperties != null) {
                AuthBiometricFingerprintView authBiometricFingerprintView = (AuthBiometricFingerprintView) from.inflate(R$layout.auth_biometric_fingerprint_view, (ViewGroup) null, false);
                authBiometricFingerprintView.setSensorProperties(findFirstSensorProperties);
                this.mBiometricView = authBiometricFingerprintView;
            } else if (findFirstSensorProperties2 != null) {
                this.mBiometricView = (AuthBiometricFaceView) from.inflate(R$layout.auth_biometric_face_view, (ViewGroup) null, false);
            } else {
                Log.e("AuthContainerView", "No sensors found!");
            }
        }
        AuthBiometricView authBiometricView = this.mBiometricView;
        if (authBiometricView != null) {
            authBiometricView.setRequireConfirmation(config.mRequireConfirmation);
            this.mBiometricView.setPanelController(authPanelController);
            this.mBiometricView.setPromptInfo(config.mPromptInfo);
            this.mBiometricView.setCallback(biometricCallback);
            this.mBiometricView.setBackgroundView(imageView);
            this.mBiometricView.setUserId(config.mUserId);
            this.mBiometricView.setEffectiveUserId(credentialOwnerProfile);
        }
        setOnKeyListener(new AuthContainerView$$ExternalSyntheticLambda0(this));
        setImportantForAccessibility(2);
        setFocusableInTouchMode(true);
        requestFocus();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$new$0(View view, int i, KeyEvent keyEvent) {
        if (i != 4) {
            return false;
        }
        if (keyEvent.getAction() == 1) {
            sendEarlyUserCanceled();
            animateAway(1);
        }
        return true;
    }

    public void sendEarlyUserCanceled() {
        this.mConfig.mCallback.onSystemEvent(1);
    }

    public boolean isAllowDeviceCredentials() {
        return Utils.isDeviceCredentialAllowed(this.mConfig.mPromptInfo);
    }

    public final void addCredentialView(boolean z, boolean z2) {
        LayoutInflater from = LayoutInflater.from(this.mContext);
        int credentialType = Utils.getCredentialType(this.mLockPatternUtils, this.mEffectiveUserId);
        if (credentialType != 1) {
            if (credentialType == 2) {
                this.mCredentialView = (AuthCredentialView) from.inflate(R$layout.auth_credential_pattern_view, (ViewGroup) null, false);
                this.mBackgroundView.setOnClickListener((View.OnClickListener) null);
                this.mBackgroundView.setImportantForAccessibility(2);
                this.mCredentialView.setContainerView(this);
                this.mCredentialView.setUserId(this.mConfig.mUserId);
                this.mCredentialView.setOperationId(this.mConfig.mOperationId);
                this.mCredentialView.setEffectiveUserId(this.mEffectiveUserId);
                this.mCredentialView.setCredentialType(credentialType);
                this.mCredentialView.setCallback(this.mCredentialCallback);
                this.mCredentialView.setPromptInfo(this.mConfig.mPromptInfo);
                this.mCredentialView.setPanelController(this.mPanelController, z);
                this.mCredentialView.setShouldAnimateContents(z2);
                this.mCredentialView.setBackgroundExecutor(this.mBackgroundExecutor);
                this.mFrameLayout.addView(this.mCredentialView);
            } else if (credentialType != 3) {
                throw new IllegalStateException("Unknown credential type: " + credentialType);
            }
        }
        this.mCredentialView = (AuthCredentialView) from.inflate(R$layout.auth_credential_password_view, (ViewGroup) null, false);
        this.mBackgroundView.setOnClickListener((View.OnClickListener) null);
        this.mBackgroundView.setImportantForAccessibility(2);
        this.mCredentialView.setContainerView(this);
        this.mCredentialView.setUserId(this.mConfig.mUserId);
        this.mCredentialView.setOperationId(this.mConfig.mOperationId);
        this.mCredentialView.setEffectiveUserId(this.mEffectiveUserId);
        this.mCredentialView.setCredentialType(credentialType);
        this.mCredentialView.setCallback(this.mCredentialCallback);
        this.mCredentialView.setPromptInfo(this.mConfig.mPromptInfo);
        this.mCredentialView.setPanelController(this.mPanelController, z);
        this.mCredentialView.setShouldAnimateContents(z2);
        this.mCredentialView.setBackgroundExecutor(this.mBackgroundExecutor);
        this.mFrameLayout.addView(this.mCredentialView);
    }

    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        this.mPanelController.setContainerDimensions(getMeasuredWidth(), getMeasuredHeight());
    }

    public void onOrientationChanged() {
        maybeUpdatePositionForUdfps(true);
    }

    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        if (!z) {
            Log.v("AuthContainerView", "Lost window focus, dismissing the dialog");
            animateAway(1);
        }
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mWakefulnessLifecycle.addObserver(this);
        if (Utils.isBiometricAllowed(this.mConfig.mPromptInfo)) {
            this.mBiometricScrollView.addView(this.mBiometricView);
        } else if (Utils.isDeviceCredentialAllowed(this.mConfig.mPromptInfo)) {
            addCredentialView(true, false);
        } else {
            throw new IllegalStateException("Unknown configuration: " + this.mConfig.mPromptInfo.getAuthenticators());
        }
        maybeUpdatePositionForUdfps(false);
        if (this.mConfig.mSkipIntro) {
            this.mContainerState = 3;
            return;
        }
        this.mContainerState = 1;
        this.mPanelView.setY(this.mTranslationY);
        this.mBiometricScrollView.setY(this.mTranslationY);
        setAlpha(0.0f);
        postOnAnimation(new AuthContainerView$$ExternalSyntheticLambda1(this, this.mConfig.mSkipAnimation ? 0 : 250));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onAttachedToWindow$1(long j) {
        this.mPanelView.animate().translationY(0.0f).setDuration(j).setInterpolator(this.mLinearOutSlowIn).withLayer().withEndAction(new AuthContainerView$$ExternalSyntheticLambda2(this)).start();
        this.mBiometricScrollView.animate().translationY(0.0f).setDuration(j).setInterpolator(this.mLinearOutSlowIn).withLayer().start();
        AuthCredentialView authCredentialView = this.mCredentialView;
        if (authCredentialView != null && authCredentialView.isAttachedToWindow()) {
            this.mCredentialView.setY(this.mTranslationY);
            this.mCredentialView.animate().translationY(0.0f).setDuration(j).setInterpolator(this.mLinearOutSlowIn).withLayer().start();
        }
        animate().alpha(1.0f).setDuration(j).setInterpolator(this.mLinearOutSlowIn).withLayer().start();
    }

    public static boolean shouldUpdatePositionForUdfps(View view) {
        if (view instanceof AuthBiometricFingerprintView) {
            return ((AuthBiometricFingerprintView) view).isUdfps();
        }
        return false;
    }

    public final boolean maybeUpdatePositionForUdfps(boolean z) {
        Display display = getDisplay();
        if (display == null || !shouldUpdatePositionForUdfps(this.mBiometricView)) {
            return false;
        }
        int rotation = display.getRotation();
        if (rotation == 0) {
            this.mPanelController.setPosition(1);
            setScrollViewGravity(81);
        } else if (rotation == 1) {
            this.mPanelController.setPosition(3);
            setScrollViewGravity(21);
        } else if (rotation != 3) {
            Log.e("AuthContainerView", "Unsupported display rotation: " + rotation);
            this.mPanelController.setPosition(1);
            setScrollViewGravity(81);
        } else {
            this.mPanelController.setPosition(2);
            setScrollViewGravity(19);
        }
        if (z) {
            this.mPanelView.invalidateOutline();
            this.mBiometricView.requestLayout();
        }
        return true;
    }

    public final void setScrollViewGravity(int i) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mBiometricScrollView.getLayoutParams();
        layoutParams.gravity = i;
        this.mBiometricScrollView.setLayoutParams(layoutParams);
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mWakefulnessLifecycle.removeObserver(this);
    }

    public void onStartedGoingToSleep() {
        animateAway(1);
    }

    public void show(WindowManager windowManager, Bundle bundle) {
        AuthBiometricView authBiometricView = this.mBiometricView;
        if (authBiometricView != null) {
            authBiometricView.restoreState(bundle);
        }
        windowManager.addView(this, getLayoutParams(this.mWindowToken, this.mConfig.mPromptInfo.getTitle()));
    }

    public void dismissWithoutCallback(boolean z) {
        if (z) {
            animateAway(false, 0);
        } else {
            removeWindowIfAttached();
        }
    }

    public void dismissFromSystemServer() {
        animateAway(false, 0);
    }

    public void onAuthenticationSucceeded(int i) {
        this.mBiometricView.onAuthenticationSucceeded(i);
    }

    public void onAuthenticationFailed(int i, String str) {
        this.mFailedModalities.add(Integer.valueOf(i));
        this.mBiometricView.onAuthenticationFailed(i, str);
    }

    public void onHelp(int i, String str) {
        this.mBiometricView.onHelp(i, str);
    }

    public void onError(int i, String str) {
        this.mBiometricView.onError(i, str);
    }

    public void onPointerDown() {
        if (this.mBiometricView.onPointerDown(this.mFailedModalities)) {
            Log.d("AuthContainerView", "retrying failed modalities (pointer down)");
            this.mBiometricCallback.onAction(4);
        }
    }

    public void onSaveState(Bundle bundle) {
        boolean z = true;
        bundle.putBoolean("container_going_away", this.mContainerState == 4);
        bundle.putBoolean("biometric_showing", this.mBiometricView != null && this.mCredentialView == null);
        if (this.mCredentialView == null) {
            z = false;
        }
        bundle.putBoolean("credential_showing", z);
        AuthBiometricView authBiometricView = this.mBiometricView;
        if (authBiometricView != null) {
            authBiometricView.onSaveState(bundle);
        }
    }

    public String getOpPackageName() {
        return this.mConfig.mOpPackageName;
    }

    public long getRequestId() {
        return this.mConfig.mRequestId;
    }

    public void animateToCredentialUI() {
        this.mBiometricView.startTransitionToCredentialUI();
    }

    public void animateAway(int i) {
        animateAway(true, i);
    }

    public final void animateAway(boolean z, int i) {
        int i2 = this.mContainerState;
        if (i2 == 1) {
            Log.w("AuthContainerView", "startDismiss(): waiting for onDialogAnimatedIn");
            this.mContainerState = 2;
        } else if (i2 == 4) {
            Log.w("AuthContainerView", "Already dismissing, sendReason: " + z + " reason: " + i);
        } else {
            this.mContainerState = 4;
            if (z) {
                this.mPendingCallbackReason = Integer.valueOf(i);
            } else {
                this.mPendingCallbackReason = null;
            }
            postOnAnimation(new AuthContainerView$$ExternalSyntheticLambda4(this, this.mConfig.mSkipAnimation ? 0 : 350, new AuthContainerView$$ExternalSyntheticLambda3(this)));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$animateAway$2() {
        setVisibility(4);
        removeWindowIfAttached();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$animateAway$3(long j, Runnable runnable) {
        this.mPanelView.animate().translationY(this.mTranslationY).setDuration(j).setInterpolator(this.mLinearOutSlowIn).withLayer().withEndAction(runnable).start();
        this.mBiometricScrollView.animate().translationY(this.mTranslationY).setDuration(j).setInterpolator(this.mLinearOutSlowIn).withLayer().start();
        AuthCredentialView authCredentialView = this.mCredentialView;
        if (authCredentialView != null && authCredentialView.isAttachedToWindow()) {
            this.mCredentialView.animate().translationY(this.mTranslationY).setDuration(j).setInterpolator(this.mLinearOutSlowIn).withLayer().start();
        }
        animate().alpha(0.0f).setDuration(j).setInterpolator(this.mLinearOutSlowIn).withLayer().start();
    }

    public final void sendPendingCallbackIfNotNull() {
        Log.d("AuthContainerView", "pendingCallback: " + this.mPendingCallbackReason);
        Integer num = this.mPendingCallbackReason;
        if (num != null) {
            this.mConfig.mCallback.onDismissed(num.intValue(), this.mCredentialAttestation);
            this.mPendingCallbackReason = null;
        }
    }

    public final void removeWindowIfAttached() {
        sendPendingCallbackIfNotNull();
        if (this.mContainerState != 5) {
            this.mContainerState = 5;
            if (isAttachedToWindow()) {
                this.mWindowManager.removeView(this);
            }
        }
    }

    public final void onDialogAnimatedIn() {
        int i = this.mContainerState;
        if (i == 2) {
            Log.d("AuthContainerView", "onDialogAnimatedIn(): mPendingDismissDialog=true, dismissing now");
            animateAway(1);
        } else if (i == 4 || i == 5) {
            Log.d("AuthContainerView", "onDialogAnimatedIn(): ignore, already animating out or gone - state: " + this.mContainerState);
        } else {
            this.mContainerState = 3;
            if (this.mBiometricView != null) {
                this.mConfig.mCallback.onDialogAnimatedIn();
                this.mBiometricView.onDialogAnimatedIn();
            }
        }
    }

    @VisibleForTesting
    public static WindowManager.LayoutParams getLayoutParams(IBinder iBinder, CharSequence charSequence) {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-1, -1, 2041, 16785408, -3);
        layoutParams.privateFlags |= 16;
        layoutParams.setFitInsetsTypes(layoutParams.getFitInsetsTypes() & (~WindowInsets.Type.ime()));
        layoutParams.setTitle("BiometricPrompt");
        layoutParams.accessibilityTitle = charSequence;
        layoutParams.token = iBinder;
        return layoutParams;
    }
}
