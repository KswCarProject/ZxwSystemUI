package com.android.systemui.statusbar.notification;

import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.animation.LaunchAnimator;

/* compiled from: LaunchAnimationParameters.kt */
public final class LaunchAnimationParameters extends LaunchAnimator.State {
    public float linearProgress;
    public int parentStartClipTopAmount;
    public int parentStartRoundedTopClipping;
    public float progress;
    public int startClipTopAmount;
    public float startNotificationTop;
    public int startRoundedTopClipping;
    public float startTranslationZ;

    public LaunchAnimationParameters(int i, int i2, int i3, int i4, float f, float f2) {
        super(i, i2, i3, i4, f, f2);
    }

    @VisibleForTesting
    public LaunchAnimationParameters() {
        this(0, 0, 0, 0, 0.0f, 0.0f);
    }

    public final float getStartTranslationZ() {
        return this.startTranslationZ;
    }

    public final void setStartTranslationZ(float f) {
        this.startTranslationZ = f;
    }

    public final float getStartNotificationTop() {
        return this.startNotificationTop;
    }

    public final void setStartNotificationTop(float f) {
        this.startNotificationTop = f;
    }

    public final int getStartClipTopAmount() {
        return this.startClipTopAmount;
    }

    public final void setStartClipTopAmount(int i) {
        this.startClipTopAmount = i;
    }

    public final int getParentStartClipTopAmount() {
        return this.parentStartClipTopAmount;
    }

    public final void setParentStartClipTopAmount(int i) {
        this.parentStartClipTopAmount = i;
    }

    public final float getProgress() {
        return this.progress;
    }

    public final void setProgress(float f) {
        this.progress = f;
    }

    public final void setLinearProgress(float f) {
        this.linearProgress = f;
    }

    public final int getStartRoundedTopClipping() {
        return this.startRoundedTopClipping;
    }

    public final void setStartRoundedTopClipping(int i) {
        this.startRoundedTopClipping = i;
    }

    public final int getParentStartRoundedTopClipping() {
        return this.parentStartRoundedTopClipping;
    }

    public final void setParentStartRoundedTopClipping(int i) {
        this.parentStartRoundedTopClipping = i;
    }

    public final float getProgress(long j, long j2) {
        return LaunchAnimator.Companion.getProgress(ActivityLaunchAnimator.TIMINGS, this.linearProgress, j, j2);
    }
}
