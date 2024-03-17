package com.android.systemui.statusbar.gesture;

import android.content.Context;
import android.view.GestureDetector;
import android.view.InputEvent;
import android.view.MotionEvent;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: TapGestureDetector.kt */
public final class TapGestureDetector extends GenericGestureDetector {
    @NotNull
    public final Context context;
    @Nullable
    public GestureDetector gestureDetector;
    @NotNull
    public final TapGestureDetector$gestureListener$1 gestureListener = new TapGestureDetector$gestureListener$1(this);

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public TapGestureDetector(@org.jetbrains.annotations.NotNull android.content.Context r2) {
        /*
            r1 = this;
            java.lang.Class<com.android.systemui.statusbar.gesture.TapGestureDetector> r0 = com.android.systemui.statusbar.gesture.TapGestureDetector.class
            kotlin.reflect.KClass r0 = kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(r0)
            java.lang.String r0 = r0.getSimpleName()
            kotlin.jvm.internal.Intrinsics.checkNotNull(r0)
            r1.<init>(r0)
            r1.context = r2
            com.android.systemui.statusbar.gesture.TapGestureDetector$gestureListener$1 r2 = new com.android.systemui.statusbar.gesture.TapGestureDetector$gestureListener$1
            r2.<init>(r1)
            r1.gestureListener = r2
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.gesture.TapGestureDetector.<init>(android.content.Context):void");
    }

    public void onInputEvent(@NotNull InputEvent inputEvent) {
        if (inputEvent instanceof MotionEvent) {
            GestureDetector gestureDetector2 = this.gestureDetector;
            Intrinsics.checkNotNull(gestureDetector2);
            gestureDetector2.onTouchEvent((MotionEvent) inputEvent);
        }
    }

    public void startGestureListening$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
        super.startGestureListening$frameworks__base__packages__SystemUI__android_common__SystemUI_core();
        this.gestureDetector = new GestureDetector(this.context, this.gestureListener);
    }

    public void stopGestureListening$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
        super.stopGestureListening$frameworks__base__packages__SystemUI__android_common__SystemUI_core();
        this.gestureDetector = null;
    }
}
