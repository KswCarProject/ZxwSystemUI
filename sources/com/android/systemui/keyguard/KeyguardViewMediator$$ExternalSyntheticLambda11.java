package com.android.systemui.keyguard;

import android.animation.ValueAnimator;
import android.view.RemoteAnimationTarget;
import android.view.SyncRtSurfaceTransactionApplier;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class KeyguardViewMediator$$ExternalSyntheticLambda11 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ RemoteAnimationTarget f$0;
    public final /* synthetic */ SyncRtSurfaceTransactionApplier f$1;

    public /* synthetic */ KeyguardViewMediator$$ExternalSyntheticLambda11(RemoteAnimationTarget remoteAnimationTarget, SyncRtSurfaceTransactionApplier syncRtSurfaceTransactionApplier) {
        this.f$0 = remoteAnimationTarget;
        this.f$1 = syncRtSurfaceTransactionApplier;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.f$1.scheduleApply(new SyncRtSurfaceTransactionApplier.SurfaceParams[]{new SyncRtSurfaceTransactionApplier.SurfaceParams.Builder(this.f$0.leash).withAlpha(valueAnimator.getAnimatedFraction()).build()});
    }
}
