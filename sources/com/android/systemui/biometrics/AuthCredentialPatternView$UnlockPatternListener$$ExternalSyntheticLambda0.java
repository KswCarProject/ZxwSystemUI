package com.android.systemui.biometrics;

import com.android.internal.widget.LockPatternChecker;
import com.android.internal.widget.VerifyCredentialResponse;
import com.android.systemui.biometrics.AuthCredentialPatternView;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class AuthCredentialPatternView$UnlockPatternListener$$ExternalSyntheticLambda0 implements LockPatternChecker.OnVerifyCallback {
    public final /* synthetic */ AuthCredentialPatternView.UnlockPatternListener f$0;

    public /* synthetic */ AuthCredentialPatternView$UnlockPatternListener$$ExternalSyntheticLambda0(AuthCredentialPatternView.UnlockPatternListener unlockPatternListener) {
        this.f$0 = unlockPatternListener;
    }

    public final void onVerified(VerifyCredentialResponse verifyCredentialResponse, int i) {
        this.f$0.onPatternVerified(verifyCredentialResponse, i);
    }
}
