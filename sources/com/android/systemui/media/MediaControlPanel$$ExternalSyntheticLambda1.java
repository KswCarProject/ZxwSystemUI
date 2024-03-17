package com.android.systemui.media;

import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintSet;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class MediaControlPanel$$ExternalSyntheticLambda1 implements Consumer {
    public final /* synthetic */ MediaControlPanel f$0;
    public final /* synthetic */ ConstraintSet f$1;
    public final /* synthetic */ boolean f$2;

    public /* synthetic */ MediaControlPanel$$ExternalSyntheticLambda1(MediaControlPanel mediaControlPanel, ConstraintSet constraintSet, boolean z) {
        this.f$0 = mediaControlPanel;
        this.f$1 = constraintSet;
        this.f$2 = z;
    }

    public final void accept(Object obj) {
        this.f$0.lambda$bindRecommendation$17(this.f$1, this.f$2, (TextView) obj);
    }
}
