package com.android.keyguard;

import android.view.WindowInsetsAnimationController;
import com.android.keyguard.KeyguardPasswordView;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class KeyguardPasswordView$1$1$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ KeyguardPasswordView.AnonymousClass1.AnonymousClass1 f$0;
    public final /* synthetic */ WindowInsetsAnimationController f$1;
    public final /* synthetic */ Runnable f$2;

    public /* synthetic */ KeyguardPasswordView$1$1$$ExternalSyntheticLambda0(KeyguardPasswordView.AnonymousClass1.AnonymousClass1 r1, WindowInsetsAnimationController windowInsetsAnimationController, Runnable runnable) {
        this.f$0 = r1;
        this.f$1 = windowInsetsAnimationController;
        this.f$2 = runnable;
    }

    public final void run() {
        this.f$0.lambda$onAnimationEnd$0(this.f$1, this.f$2);
    }
}
