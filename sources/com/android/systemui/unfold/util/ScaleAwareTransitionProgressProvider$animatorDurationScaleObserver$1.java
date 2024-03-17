package com.android.systemui.unfold.util;

import android.database.ContentObserver;
import android.os.Handler;

/* compiled from: ScaleAwareTransitionProgressProvider.kt */
public final class ScaleAwareTransitionProgressProvider$animatorDurationScaleObserver$1 extends ContentObserver {
    public final /* synthetic */ ScaleAwareTransitionProgressProvider this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public ScaleAwareTransitionProgressProvider$animatorDurationScaleObserver$1(ScaleAwareTransitionProgressProvider scaleAwareTransitionProgressProvider) {
        super((Handler) null);
        this.this$0 = scaleAwareTransitionProgressProvider;
    }

    public void onChange(boolean z) {
        this.this$0.onAnimatorScaleChanged();
    }
}
