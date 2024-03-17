package com.android.systemui.controls.management;

import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: ControlsModel.kt */
public final class ZoneNameWrapper extends ElementWrapper {
    @NotNull
    public final CharSequence zoneName;

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof ZoneNameWrapper) && Intrinsics.areEqual((Object) this.zoneName, (Object) ((ZoneNameWrapper) obj).zoneName);
    }

    public int hashCode() {
        return this.zoneName.hashCode();
    }

    @NotNull
    public String toString() {
        return "ZoneNameWrapper(zoneName=" + this.zoneName + ')';
    }

    public ZoneNameWrapper(@NotNull CharSequence charSequence) {
        super((DefaultConstructorMarker) null);
        this.zoneName = charSequence;
    }

    @NotNull
    public final CharSequence getZoneName() {
        return this.zoneName;
    }
}
