package com.android.systemui.statusbar.connectivity;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: SignalCallback.kt */
public final class IconState {
    @NotNull
    public final String contentDescription;
    public final int icon;
    public final boolean visible;

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof IconState)) {
            return false;
        }
        IconState iconState = (IconState) obj;
        return this.visible == iconState.visible && this.icon == iconState.icon && Intrinsics.areEqual((Object) this.contentDescription, (Object) iconState.contentDescription);
    }

    public int hashCode() {
        boolean z = this.visible;
        if (z) {
            z = true;
        }
        return ((((z ? 1 : 0) * true) + Integer.hashCode(this.icon)) * 31) + this.contentDescription.hashCode();
    }

    public IconState(boolean z, int i, @NotNull String str) {
        this.visible = z;
        this.icon = i;
        this.contentDescription = str;
    }

    @NotNull
    public String toString() {
        return "[visible=" + this.visible + ',' + "icon=" + this.icon + ',' + "contentDescription=" + this.contentDescription + ']';
    }
}
