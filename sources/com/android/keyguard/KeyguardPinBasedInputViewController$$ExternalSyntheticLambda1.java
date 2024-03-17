package com.android.keyguard;

import com.android.keyguard.PasswordTextView;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class KeyguardPinBasedInputViewController$$ExternalSyntheticLambda1 implements PasswordTextView.UserActivityListener {
    public final /* synthetic */ KeyguardPinBasedInputViewController f$0;

    public /* synthetic */ KeyguardPinBasedInputViewController$$ExternalSyntheticLambda1(KeyguardPinBasedInputViewController keyguardPinBasedInputViewController) {
        this.f$0 = keyguardPinBasedInputViewController;
    }

    public final void onUserActivity() {
        this.f$0.onUserInput();
    }
}
