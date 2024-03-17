package com.android.systemui.util;

import android.media.AudioManager;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.FunctionReferenceImpl;
import org.jetbrains.annotations.NotNull;

/* compiled from: RingerModeTrackerImpl.kt */
public /* synthetic */ class RingerModeTrackerImpl$ringerModeInternal$1 extends FunctionReferenceImpl implements Function0<Integer> {
    public RingerModeTrackerImpl$ringerModeInternal$1(Object obj) {
        super(0, obj, AudioManager.class, "getRingerModeInternal", "getRingerModeInternal()I", 0);
    }

    @NotNull
    public final Integer invoke() {
        return Integer.valueOf(((AudioManager) this.receiver).getRingerModeInternal());
    }
}
