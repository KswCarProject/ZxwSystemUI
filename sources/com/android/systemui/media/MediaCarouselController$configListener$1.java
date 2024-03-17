package com.android.systemui.media;

import android.content.res.Configuration;
import com.android.systemui.statusbar.policy.ConfigurationController;
import org.jetbrains.annotations.Nullable;

/* compiled from: MediaCarouselController.kt */
public final class MediaCarouselController$configListener$1 implements ConfigurationController.ConfigurationListener {
    public final /* synthetic */ MediaCarouselController this$0;

    public MediaCarouselController$configListener$1(MediaCarouselController mediaCarouselController) {
        this.this$0 = mediaCarouselController;
    }

    public void onDensityOrFontScaleChanged() {
        this.this$0.updatePlayers(true);
        this.this$0.inflateSettingsButton();
    }

    public void onThemeChanged() {
        this.this$0.updatePlayers(false);
        this.this$0.inflateSettingsButton();
    }

    public void onConfigChanged(@Nullable Configuration configuration) {
        if (configuration != null) {
            MediaCarouselController mediaCarouselController = this.this$0;
            boolean z = true;
            if (configuration.getLayoutDirection() != 1) {
                z = false;
            }
            mediaCarouselController.setRtl(z);
        }
    }

    public void onUiModeChanged() {
        this.this$0.updatePlayers(false);
        this.this$0.inflateSettingsButton();
    }
}
