package com.android.systemui.controls.ui;

import android.content.ComponentName;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: ControlsUiControllerImpl.kt */
public final class ControlKey {
    @NotNull
    public final ComponentName componentName;
    @NotNull
    public final String controlId;

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ControlKey)) {
            return false;
        }
        ControlKey controlKey = (ControlKey) obj;
        return Intrinsics.areEqual((Object) this.componentName, (Object) controlKey.componentName) && Intrinsics.areEqual((Object) this.controlId, (Object) controlKey.controlId);
    }

    public int hashCode() {
        return (this.componentName.hashCode() * 31) + this.controlId.hashCode();
    }

    @NotNull
    public String toString() {
        return "ControlKey(componentName=" + this.componentName + ", controlId=" + this.controlId + ')';
    }

    public ControlKey(@NotNull ComponentName componentName2, @NotNull String str) {
        this.componentName = componentName2;
        this.controlId = str;
    }
}
