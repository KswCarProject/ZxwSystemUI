package com.android.systemui.media;

import android.content.res.Configuration;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.animation.TransitionLayout;
import org.jetbrains.annotations.Nullable;

/* compiled from: MediaViewController.kt */
public final class MediaViewController$configurationListener$1 implements ConfigurationController.ConfigurationListener {
    public final /* synthetic */ MediaViewController this$0;

    public MediaViewController$configurationListener$1(MediaViewController mediaViewController) {
        this.this$0 = mediaViewController;
    }

    public void onConfigChanged(@Nullable Configuration configuration) {
        if (configuration != null) {
            MediaViewController mediaViewController = this.this$0;
            TransitionLayout access$getTransitionLayout$p = mediaViewController.transitionLayout;
            boolean z = false;
            if (access$getTransitionLayout$p != null && access$getTransitionLayout$p.getRawLayoutDirection() == configuration.getLayoutDirection()) {
                z = true;
            }
            if (!z) {
                TransitionLayout access$getTransitionLayout$p2 = mediaViewController.transitionLayout;
                if (access$getTransitionLayout$p2 != null) {
                    access$getTransitionLayout$p2.setLayoutDirection(configuration.getLayoutDirection());
                }
                mediaViewController.refreshState();
            }
        }
    }
}
