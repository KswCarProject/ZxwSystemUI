package com.android.systemui.controls.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.ArrayMap;
import android.util.SparseArray;
import com.android.systemui.R$drawable;
import kotlin.Pair;
import kotlin.collections.MapsKt__MapsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: RenderInfo.kt */
public final class RenderInfo {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    public static final ArrayMap<ComponentName, Drawable> appIconMap = new ArrayMap<>();
    @NotNull
    public static final SparseArray<Drawable> iconMap = new SparseArray<>();
    public final int enabledBackground;
    public final int foreground;
    @NotNull
    public final Drawable icon;

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof RenderInfo)) {
            return false;
        }
        RenderInfo renderInfo = (RenderInfo) obj;
        return Intrinsics.areEqual((Object) this.icon, (Object) renderInfo.icon) && this.foreground == renderInfo.foreground && this.enabledBackground == renderInfo.enabledBackground;
    }

    public int hashCode() {
        return (((this.icon.hashCode() * 31) + Integer.hashCode(this.foreground)) * 31) + Integer.hashCode(this.enabledBackground);
    }

    @NotNull
    public String toString() {
        return "RenderInfo(icon=" + this.icon + ", foreground=" + this.foreground + ", enabledBackground=" + this.enabledBackground + ')';
    }

    public RenderInfo(@NotNull Drawable drawable, int i, int i2) {
        this.icon = drawable;
        this.foreground = i;
        this.enabledBackground = i2;
    }

    @NotNull
    public final Drawable getIcon() {
        return this.icon;
    }

    public final int getForeground() {
        return this.foreground;
    }

    public final int getEnabledBackground() {
        return this.enabledBackground;
    }

    /* compiled from: RenderInfo.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }

        public static /* synthetic */ RenderInfo lookup$default(Companion companion, Context context, ComponentName componentName, int i, int i2, int i3, Object obj) {
            if ((i3 & 8) != 0) {
                i2 = 0;
            }
            return companion.lookup(context, componentName, i, i2);
        }

        @NotNull
        public final RenderInfo lookup(@NotNull Context context, @NotNull ComponentName componentName, int i, int i2) {
            Drawable drawable;
            if (i2 > 0) {
                i = (i * 1000) + i2;
            }
            Pair pair = (Pair) MapsKt__MapsKt.getValue(RenderInfoKt.deviceColorMap, Integer.valueOf(i));
            int intValue = ((Number) pair.component1()).intValue();
            int intValue2 = ((Number) pair.component2()).intValue();
            int intValue3 = ((Number) MapsKt__MapsKt.getValue(RenderInfoKt.deviceIconMap, Integer.valueOf(i))).intValue();
            if (intValue3 == -1) {
                drawable = (Drawable) RenderInfo.appIconMap.get(componentName);
                if (drawable == null) {
                    drawable = context.getResources().getDrawable(R$drawable.ic_device_unknown_on, (Resources.Theme) null);
                    RenderInfo.appIconMap.put(componentName, drawable);
                }
            } else {
                Drawable drawable2 = (Drawable) RenderInfo.iconMap.get(intValue3);
                if (drawable2 == null) {
                    drawable2 = context.getResources().getDrawable(intValue3, (Resources.Theme) null);
                    RenderInfo.iconMap.put(intValue3, drawable2);
                }
                drawable = drawable2;
            }
            Intrinsics.checkNotNull(drawable);
            return new RenderInfo(drawable.getConstantState().newDrawable(context.getResources()), intValue, intValue2);
        }

        public final void registerComponentIcon(@NotNull ComponentName componentName, @NotNull Drawable drawable) {
            RenderInfo.appIconMap.put(componentName, drawable);
        }

        public final void clearCache() {
            RenderInfo.iconMap.clear();
            RenderInfo.appIconMap.clear();
        }
    }
}
