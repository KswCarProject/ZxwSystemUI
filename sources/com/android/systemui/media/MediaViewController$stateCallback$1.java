package com.android.systemui.media;

import com.android.systemui.media.MediaHostStatesManager;
import org.jetbrains.annotations.NotNull;

/* compiled from: MediaViewController.kt */
public final class MediaViewController$stateCallback$1 implements MediaHostStatesManager.Callback {
    public final /* synthetic */ MediaViewController this$0;

    public MediaViewController$stateCallback$1(MediaViewController mediaViewController) {
        this.this$0 = mediaViewController;
    }

    public void onHostStateChanged(int i, @NotNull MediaHostState mediaHostState) {
        if (i == this.this$0.getCurrentEndLocation() || i == this.this$0.currentStartLocation) {
            MediaViewController mediaViewController = this.this$0;
            mediaViewController.setCurrentState(mediaViewController.currentStartLocation, this.this$0.getCurrentEndLocation(), this.this$0.currentTransitionProgress, false);
        }
    }
}
