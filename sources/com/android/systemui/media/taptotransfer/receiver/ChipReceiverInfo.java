package com.android.systemui.media.taptotransfer.receiver;

import android.graphics.drawable.Drawable;
import android.media.MediaRoute2Info;
import com.android.systemui.media.taptotransfer.common.ChipInfoCommon;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: MediaTttChipControllerReceiver.kt */
public final class ChipReceiverInfo implements ChipInfoCommon {
    @Nullable
    public final Drawable appIconDrawableOverride;
    @Nullable
    public final CharSequence appNameOverride;
    @NotNull
    public final MediaRoute2Info routeInfo;

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ChipReceiverInfo)) {
            return false;
        }
        ChipReceiverInfo chipReceiverInfo = (ChipReceiverInfo) obj;
        return Intrinsics.areEqual((Object) this.routeInfo, (Object) chipReceiverInfo.routeInfo) && Intrinsics.areEqual((Object) this.appIconDrawableOverride, (Object) chipReceiverInfo.appIconDrawableOverride) && Intrinsics.areEqual((Object) this.appNameOverride, (Object) chipReceiverInfo.appNameOverride);
    }

    public long getTimeoutMs() {
        return 3000;
    }

    public int hashCode() {
        int hashCode = this.routeInfo.hashCode() * 31;
        Drawable drawable = this.appIconDrawableOverride;
        int i = 0;
        int hashCode2 = (hashCode + (drawable == null ? 0 : drawable.hashCode())) * 31;
        CharSequence charSequence = this.appNameOverride;
        if (charSequence != null) {
            i = charSequence.hashCode();
        }
        return hashCode2 + i;
    }

    @NotNull
    public String toString() {
        return "ChipReceiverInfo(routeInfo=" + this.routeInfo + ", appIconDrawableOverride=" + this.appIconDrawableOverride + ", appNameOverride=" + this.appNameOverride + ')';
    }

    public ChipReceiverInfo(@NotNull MediaRoute2Info mediaRoute2Info, @Nullable Drawable drawable, @Nullable CharSequence charSequence) {
        this.routeInfo = mediaRoute2Info;
        this.appIconDrawableOverride = drawable;
        this.appNameOverride = charSequence;
    }

    @NotNull
    public final MediaRoute2Info getRouteInfo() {
        return this.routeInfo;
    }

    @Nullable
    public final Drawable getAppIconDrawableOverride() {
        return this.appIconDrawableOverride;
    }

    @Nullable
    public final CharSequence getAppNameOverride() {
        return this.appNameOverride;
    }
}
