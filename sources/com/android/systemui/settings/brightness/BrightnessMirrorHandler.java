package com.android.systemui.settings.brightness;

import com.android.systemui.statusbar.policy.BrightnessMirrorController;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: BrightnessMirrorHandler.kt */
public final class BrightnessMirrorHandler {
    @NotNull
    public final MirroredBrightnessController brightnessController;
    @NotNull
    public final BrightnessMirrorController.BrightnessMirrorListener brightnessMirrorListener = new BrightnessMirrorHandler$brightnessMirrorListener$1(this);
    @Nullable
    public BrightnessMirrorController mirrorController;

    public BrightnessMirrorHandler(@NotNull MirroredBrightnessController mirroredBrightnessController) {
        this.brightnessController = mirroredBrightnessController;
    }

    public final void onQsPanelAttached() {
        BrightnessMirrorController brightnessMirrorController = this.mirrorController;
        if (brightnessMirrorController != null) {
            brightnessMirrorController.addCallback(this.brightnessMirrorListener);
        }
    }

    public final void onQsPanelDettached() {
        BrightnessMirrorController brightnessMirrorController = this.mirrorController;
        if (brightnessMirrorController != null) {
            brightnessMirrorController.removeCallback(this.brightnessMirrorListener);
        }
    }

    public final void setController(@NotNull BrightnessMirrorController brightnessMirrorController) {
        BrightnessMirrorController brightnessMirrorController2 = this.mirrorController;
        if (brightnessMirrorController2 != null) {
            brightnessMirrorController2.removeCallback(this.brightnessMirrorListener);
        }
        this.mirrorController = brightnessMirrorController;
        if (brightnessMirrorController != null) {
            brightnessMirrorController.addCallback(this.brightnessMirrorListener);
        }
        updateBrightnessMirror();
    }

    public final void updateBrightnessMirror() {
        BrightnessMirrorController brightnessMirrorController = this.mirrorController;
        if (brightnessMirrorController != null) {
            this.brightnessController.setMirror(brightnessMirrorController);
        }
    }
}
