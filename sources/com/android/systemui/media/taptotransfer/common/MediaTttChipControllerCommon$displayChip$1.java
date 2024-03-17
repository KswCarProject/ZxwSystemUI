package com.android.systemui.media.taptotransfer.common;

import android.view.MotionEvent;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReferenceImpl;
import org.jetbrains.annotations.NotNull;

/* compiled from: MediaTttChipControllerCommon.kt */
public /* synthetic */ class MediaTttChipControllerCommon$displayChip$1 extends FunctionReferenceImpl implements Function1<MotionEvent, Unit> {
    public MediaTttChipControllerCommon$displayChip$1(Object obj) {
        super(1, obj, MediaTttChipControllerCommon.class, "onScreenTapped", "onScreenTapped(Landroid/view/MotionEvent;)V", 0);
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        invoke((MotionEvent) obj);
        return Unit.INSTANCE;
    }

    public final void invoke(@NotNull MotionEvent motionEvent) {
        ((MediaTttChipControllerCommon) this.receiver).onScreenTapped(motionEvent);
    }
}
