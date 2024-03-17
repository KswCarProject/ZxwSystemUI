package com.android.systemui.biometrics;

import android.content.Context;
import android.util.AttributeSet;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: AuthBiometricFaceView.kt */
public final class AuthBiometricFaceView extends AuthBiometricView {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);

    public int getDelayAfterAuthenticatedDurationMs() {
        return 500;
    }

    public int getStateForAfterError() {
        return 0;
    }

    public boolean supportsManualRetry() {
        return true;
    }

    public boolean supportsRequireConfirmation() {
        return true;
    }

    public boolean supportsSmallDialog() {
        return true;
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ AuthBiometricFaceView(Context context, AttributeSet attributeSet, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i & 2) != 0 ? null : attributeSet);
    }

    public AuthBiometricFaceView(@NotNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void handleResetAfterError() {
        resetErrorView();
    }

    public void handleResetAfterHelp() {
        resetErrorView();
    }

    @NotNull
    public AuthIconController createIconController() {
        return new AuthBiometricFaceIconController(this.mContext, this.mIconView);
    }

    public void updateState(int i) {
        if (i == 1 || (i == 2 && getSize() == 2)) {
            resetErrorView();
        }
        super.updateState(i);
    }

    public void onAuthenticationFailed(int i, @Nullable String str) {
        if (getSize() == 2 && supportsManualRetry()) {
            this.mTryAgainButton.setVisibility(0);
            this.mConfirmButton.setVisibility(8);
        }
        super.onAuthenticationFailed(i, str);
    }

    public final void resetErrorView() {
        this.mIndicatorView.setTextColor(this.mTextColorHint);
        this.mIndicatorView.setVisibility(4);
    }

    /* compiled from: AuthBiometricFaceView.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }
}
