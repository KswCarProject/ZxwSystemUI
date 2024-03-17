package com.android.wm.shell.animation;

import android.graphics.Rect;
import android.graphics.RectF;
import androidx.dynamicanimation.animation.FloatPropertyCompat;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;

/* compiled from: FloatProperties.kt */
public final class FloatProperties {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    public static final FloatPropertyCompat<RectF> RECTF_X = new FloatProperties$Companion$RECTF_X$1();
    @NotNull
    public static final FloatPropertyCompat<RectF> RECTF_Y = new FloatProperties$Companion$RECTF_Y$1();
    @NotNull
    public static final FloatPropertyCompat<Rect> RECT_HEIGHT = new FloatProperties$Companion$RECT_HEIGHT$1();
    @NotNull
    public static final FloatPropertyCompat<Rect> RECT_WIDTH = new FloatProperties$Companion$RECT_WIDTH$1();
    @NotNull
    public static final FloatPropertyCompat<Rect> RECT_X = new FloatProperties$Companion$RECT_X$1();
    @NotNull
    public static final FloatPropertyCompat<Rect> RECT_Y = new FloatProperties$Companion$RECT_Y$1();

    /* compiled from: FloatProperties.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }
}
