package com.android.systemui.media;

import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class MediaControlPanel$$ExternalSyntheticLambda24 implements Predicate {
    public final /* synthetic */ MediaButton f$0;

    public /* synthetic */ MediaControlPanel$$ExternalSyntheticLambda24(MediaButton mediaButton) {
        this.f$0 = mediaButton;
    }

    public final boolean test(Object obj) {
        return MediaControlPanel.lambda$scrubbingTimeViewsEnabled$15(this.f$0, (Integer) obj);
    }
}
