package com.android.systemui.controls;

import android.content.ComponentName;
import android.graphics.drawable.Icon;
import android.service.controls.Control;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: ControlStatus.kt */
public final class ControlStatus implements ControlInterface {
    @NotNull
    public final ComponentName component;
    @NotNull
    public final Control control;
    public boolean favorite;
    public final boolean removed;

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ControlStatus)) {
            return false;
        }
        ControlStatus controlStatus = (ControlStatus) obj;
        return Intrinsics.areEqual((Object) this.control, (Object) controlStatus.control) && Intrinsics.areEqual((Object) getComponent(), (Object) controlStatus.getComponent()) && getFavorite() == controlStatus.getFavorite() && getRemoved() == controlStatus.getRemoved();
    }

    public int hashCode() {
        int hashCode = ((this.control.hashCode() * 31) + getComponent().hashCode()) * 31;
        boolean favorite2 = getFavorite();
        boolean z = true;
        if (favorite2) {
            favorite2 = true;
        }
        int i = (hashCode + (favorite2 ? 1 : 0)) * 31;
        boolean removed2 = getRemoved();
        if (!removed2) {
            z = removed2;
        }
        return i + (z ? 1 : 0);
    }

    @NotNull
    public String toString() {
        return "ControlStatus(control=" + this.control + ", component=" + getComponent() + ", favorite=" + getFavorite() + ", removed=" + getRemoved() + ')';
    }

    public ControlStatus(@NotNull Control control2, @NotNull ComponentName componentName, boolean z, boolean z2) {
        this.control = control2;
        this.component = componentName;
        this.favorite = z;
        this.removed = z2;
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ ControlStatus(Control control2, ComponentName componentName, boolean z, boolean z2, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(control2, componentName, z, (i & 8) != 0 ? false : z2);
    }

    @NotNull
    public final Control getControl() {
        return this.control;
    }

    @NotNull
    public ComponentName getComponent() {
        return this.component;
    }

    public boolean getFavorite() {
        return this.favorite;
    }

    public void setFavorite(boolean z) {
        this.favorite = z;
    }

    public boolean getRemoved() {
        return this.removed;
    }

    @NotNull
    public String getControlId() {
        return this.control.getControlId();
    }

    @NotNull
    public CharSequence getTitle() {
        return this.control.getTitle();
    }

    @NotNull
    public CharSequence getSubtitle() {
        return this.control.getSubtitle();
    }

    @Nullable
    public Icon getCustomIcon() {
        return this.control.getCustomIcon();
    }

    public int getDeviceType() {
        return this.control.getDeviceType();
    }
}
