package com.android.systemui.controls.ui;

import android.content.ComponentName;
import android.service.controls.Control;
import com.android.systemui.controls.controller.ControlInfo;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: ControlWithState.kt */
public final class ControlWithState {
    @NotNull
    public final ControlInfo ci;
    @NotNull
    public final ComponentName componentName;
    @Nullable
    public final Control control;

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ControlWithState)) {
            return false;
        }
        ControlWithState controlWithState = (ControlWithState) obj;
        return Intrinsics.areEqual((Object) this.componentName, (Object) controlWithState.componentName) && Intrinsics.areEqual((Object) this.ci, (Object) controlWithState.ci) && Intrinsics.areEqual((Object) this.control, (Object) controlWithState.control);
    }

    public int hashCode() {
        int hashCode = ((this.componentName.hashCode() * 31) + this.ci.hashCode()) * 31;
        Control control2 = this.control;
        return hashCode + (control2 == null ? 0 : control2.hashCode());
    }

    @NotNull
    public String toString() {
        return "ControlWithState(componentName=" + this.componentName + ", ci=" + this.ci + ", control=" + this.control + ')';
    }

    public ControlWithState(@NotNull ComponentName componentName2, @NotNull ControlInfo controlInfo, @Nullable Control control2) {
        this.componentName = componentName2;
        this.ci = controlInfo;
        this.control = control2;
    }

    @NotNull
    public final ComponentName getComponentName() {
        return this.componentName;
    }

    @NotNull
    public final ControlInfo getCi() {
        return this.ci;
    }

    @Nullable
    public final Control getControl() {
        return this.control;
    }
}
