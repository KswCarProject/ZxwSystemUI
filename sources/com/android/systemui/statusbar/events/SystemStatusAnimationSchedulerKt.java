package com.android.systemui.statusbar.events;

import android.view.animation.PathInterpolator;
import org.jetbrains.annotations.NotNull;

/* compiled from: SystemStatusAnimationScheduler.kt */
public final class SystemStatusAnimationSchedulerKt {
    @NotNull
    public static final PathInterpolator STATUS_BAR_X_MOVE_IN = new PathInterpolator(0.0f, 0.0f, 0.0f, 1.0f);
    @NotNull
    public static final PathInterpolator STATUS_BAR_X_MOVE_OUT = new PathInterpolator(0.33f, 0.0f, 0.0f, 1.0f);
    @NotNull
    public static final PathInterpolator STATUS_CHIP_HEIGHT_TO_DOT_KEYFRAME_1 = new PathInterpolator(0.4f, 0.0f, 0.17f, 1.0f);
    @NotNull
    public static final PathInterpolator STATUS_CHIP_HEIGHT_TO_DOT_KEYFRAME_2 = new PathInterpolator(0.3f, 0.0f, 0.0f, 1.0f);
    @NotNull
    public static final PathInterpolator STATUS_CHIP_MOVE_TO_DOT = new PathInterpolator(0.0f, 0.0f, 0.05f, 1.0f);
    @NotNull
    public static final PathInterpolator STATUS_CHIP_WIDTH_TO_DOT_KEYFRAME_1 = new PathInterpolator(0.44f, 0.0f, 0.25f, 1.0f);
    @NotNull
    public static final PathInterpolator STATUS_CHIP_WIDTH_TO_DOT_KEYFRAME_2 = new PathInterpolator(0.3f, 0.0f, 0.26f, 1.0f);

    @NotNull
    public static final PathInterpolator getSTATUS_CHIP_WIDTH_TO_DOT_KEYFRAME_1() {
        return STATUS_CHIP_WIDTH_TO_DOT_KEYFRAME_1;
    }

    @NotNull
    public static final PathInterpolator getSTATUS_CHIP_WIDTH_TO_DOT_KEYFRAME_2() {
        return STATUS_CHIP_WIDTH_TO_DOT_KEYFRAME_2;
    }

    @NotNull
    public static final PathInterpolator getSTATUS_CHIP_HEIGHT_TO_DOT_KEYFRAME_1() {
        return STATUS_CHIP_HEIGHT_TO_DOT_KEYFRAME_1;
    }

    @NotNull
    public static final PathInterpolator getSTATUS_CHIP_HEIGHT_TO_DOT_KEYFRAME_2() {
        return STATUS_CHIP_HEIGHT_TO_DOT_KEYFRAME_2;
    }

    @NotNull
    public static final PathInterpolator getSTATUS_CHIP_MOVE_TO_DOT() {
        return STATUS_CHIP_MOVE_TO_DOT;
    }
}
