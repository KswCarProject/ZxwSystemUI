package com.android.systemui.keyguard;

import android.view.IRemoteAnimationFinishedCallback;
import android.view.RemoteAnimationTarget;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class KeyguardViewMediator$$ExternalSyntheticLambda9 implements Runnable {
    public final /* synthetic */ KeyguardViewMediator f$0;
    public final /* synthetic */ IRemoteAnimationFinishedCallback f$1;
    public final /* synthetic */ RemoteAnimationTarget[] f$2;

    public /* synthetic */ KeyguardViewMediator$$ExternalSyntheticLambda9(KeyguardViewMediator keyguardViewMediator, IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback, RemoteAnimationTarget[] remoteAnimationTargetArr) {
        this.f$0 = keyguardViewMediator;
        this.f$1 = iRemoteAnimationFinishedCallback;
        this.f$2 = remoteAnimationTargetArr;
    }

    public final void run() {
        this.f$0.lambda$handleStartKeyguardExitAnimation$8(this.f$1, this.f$2);
    }
}
