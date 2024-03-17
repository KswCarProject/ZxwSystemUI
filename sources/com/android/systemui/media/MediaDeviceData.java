package com.android.systemui.media;

import android.app.PendingIntent;
import android.graphics.drawable.Drawable;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: MediaData.kt */
public final class MediaDeviceData {
    public final boolean enabled;
    @Nullable
    public final Drawable icon;
    @Nullable
    public final String id;
    @Nullable
    public final PendingIntent intent;
    @Nullable
    public final CharSequence name;

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MediaDeviceData)) {
            return false;
        }
        MediaDeviceData mediaDeviceData = (MediaDeviceData) obj;
        return this.enabled == mediaDeviceData.enabled && Intrinsics.areEqual((Object) this.icon, (Object) mediaDeviceData.icon) && Intrinsics.areEqual((Object) this.name, (Object) mediaDeviceData.name) && Intrinsics.areEqual((Object) this.intent, (Object) mediaDeviceData.intent) && Intrinsics.areEqual((Object) this.id, (Object) mediaDeviceData.id);
    }

    public int hashCode() {
        boolean z = this.enabled;
        if (z) {
            z = true;
        }
        int i = (z ? 1 : 0) * true;
        Drawable drawable = this.icon;
        int i2 = 0;
        int hashCode = (i + (drawable == null ? 0 : drawable.hashCode())) * 31;
        CharSequence charSequence = this.name;
        int hashCode2 = (hashCode + (charSequence == null ? 0 : charSequence.hashCode())) * 31;
        PendingIntent pendingIntent = this.intent;
        int hashCode3 = (hashCode2 + (pendingIntent == null ? 0 : pendingIntent.hashCode())) * 31;
        String str = this.id;
        if (str != null) {
            i2 = str.hashCode();
        }
        return hashCode3 + i2;
    }

    @NotNull
    public String toString() {
        return "MediaDeviceData(enabled=" + this.enabled + ", icon=" + this.icon + ", name=" + this.name + ", intent=" + this.intent + ", id=" + this.id + ')';
    }

    public MediaDeviceData(boolean z, @Nullable Drawable drawable, @Nullable CharSequence charSequence, @Nullable PendingIntent pendingIntent, @Nullable String str) {
        this.enabled = z;
        this.icon = drawable;
        this.name = charSequence;
        this.intent = pendingIntent;
        this.id = str;
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ MediaDeviceData(boolean z, Drawable drawable, CharSequence charSequence, PendingIntent pendingIntent, String str, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(z, drawable, charSequence, (i & 8) != 0 ? null : pendingIntent, (i & 16) != 0 ? null : str);
    }

    public final boolean getEnabled() {
        return this.enabled;
    }

    @Nullable
    public final Drawable getIcon() {
        return this.icon;
    }

    @Nullable
    public final CharSequence getName() {
        return this.name;
    }

    @Nullable
    public final PendingIntent getIntent() {
        return this.intent;
    }

    public final boolean equalsWithoutIcon(@Nullable MediaDeviceData mediaDeviceData) {
        if (mediaDeviceData != null && this.enabled == mediaDeviceData.enabled && Intrinsics.areEqual((Object) this.name, (Object) mediaDeviceData.name) && Intrinsics.areEqual((Object) this.intent, (Object) mediaDeviceData.intent) && Intrinsics.areEqual((Object) this.id, (Object) mediaDeviceData.id)) {
            return true;
        }
        return false;
    }
}
