package com.android.systemui.media.taptotransfer.common;

import android.graphics.drawable.Drawable;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: MediaTttChipControllerCommon.kt */
public final class IconInfo {
    @NotNull
    public final Drawable icon;
    @NotNull
    public final String iconName;
    public final boolean isAppIcon;

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof IconInfo)) {
            return false;
        }
        IconInfo iconInfo = (IconInfo) obj;
        return Intrinsics.areEqual((Object) this.iconName, (Object) iconInfo.iconName) && Intrinsics.areEqual((Object) this.icon, (Object) iconInfo.icon) && this.isAppIcon == iconInfo.isAppIcon;
    }

    public int hashCode() {
        int hashCode = ((this.iconName.hashCode() * 31) + this.icon.hashCode()) * 31;
        boolean z = this.isAppIcon;
        if (z) {
            z = true;
        }
        return hashCode + (z ? 1 : 0);
    }

    @NotNull
    public String toString() {
        return "IconInfo(iconName=" + this.iconName + ", icon=" + this.icon + ", isAppIcon=" + this.isAppIcon + ')';
    }

    public IconInfo(@NotNull String str, @NotNull Drawable drawable, boolean z) {
        this.iconName = str;
        this.icon = drawable;
        this.isAppIcon = z;
    }

    @NotNull
    public final String getIconName() {
        return this.iconName;
    }

    @NotNull
    public final Drawable getIcon() {
        return this.icon;
    }

    public final boolean isAppIcon() {
        return this.isAppIcon;
    }
}
