package com.android.systemui.util;

import android.media.AudioManager;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.FunctionReferenceImpl;
import org.jetbrains.annotations.NotNull;

/* compiled from: RingerModeTrackerImpl.kt */
public /* synthetic */ class RingerModeTrackerImpl$ringerMode$1 extends FunctionReferenceImpl implements Function0<Integer> {
    public RingerModeTrackerImpl$ringerMode$1(Object obj) {
        super(0, obj, AudioManager.class, "getRingerMode", "getRingerMode()I", 0);
    }

    @NotNull
    public final Integer invoke() {
        return Integer.valueOf(((AudioManager) this.receiver).getRingerMode());
    }
}
