package com.android.systemui.media;

import android.graphics.drawable.Drawable;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: MediaData.kt */
public final class MediaAction {
    @Nullable
    public final Runnable action;
    @Nullable
    public final Drawable background;
    @Nullable
    public final CharSequence contentDescription;
    @Nullable
    public final Drawable icon;
    @Nullable
    public final Integer rebindId;

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MediaAction)) {
            return false;
        }
        MediaAction mediaAction = (MediaAction) obj;
        return Intrinsics.areEqual((Object) this.icon, (Object) mediaAction.icon) && Intrinsics.areEqual((Object) this.action, (Object) mediaAction.action) && Intrinsics.areEqual((Object) this.contentDescription, (Object) mediaAction.contentDescription) && Intrinsics.areEqual((Object) this.background, (Object) mediaAction.background) && Intrinsics.areEqual((Object) this.rebindId, (Object) mediaAction.rebindId);
    }

    public int hashCode() {
        Drawable drawable = this.icon;
        int i = 0;
        int hashCode = (drawable == null ? 0 : drawable.hashCode()) * 31;
        Runnable runnable = this.action;
        int hashCode2 = (hashCode + (runnable == null ? 0 : runnable.hashCode())) * 31;
        CharSequence charSequence = this.contentDescription;
        int hashCode3 = (hashCode2 + (charSequence == null ? 0 : charSequence.hashCode())) * 31;
        Drawable drawable2 = this.background;
        int hashCode4 = (hashCode3 + (drawable2 == null ? 0 : drawable2.hashCode())) * 31;
        Integer num = this.rebindId;
        if (num != null) {
            i = num.hashCode();
        }
        return hashCode4 + i;
    }

    @NotNull
    public String toString() {
        return "MediaAction(icon=" + this.icon + ", action=" + this.action + ", contentDescription=" + this.contentDescription + ", background=" + this.background + ", rebindId=" + this.rebindId + ')';
    }

    public MediaAction(@Nullable Drawable drawable, @Nullable Runnable runnable, @Nullable CharSequence charSequence, @Nullable Drawable drawable2, @Nullable Integer num) {
        this.icon = drawable;
        this.action = runnable;
        this.contentDescription = charSequence;
        this.background = drawable2;
        this.rebindId = num;
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ MediaAction(Drawable drawable, Runnable runnable, CharSequence charSequence, Drawable drawable2, Integer num, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(drawable, runnable, charSequence, drawable2, (i & 16) != 0 ? null : num);
    }

    @Nullable
    public final Drawable getIcon() {
        return this.icon;
    }

    @Nullable
    public final Runnable getAction() {
        return this.action;
    }

    @Nullable
    public final CharSequence getContentDescription() {
        return this.contentDescription;
    }

    @Nullable
    public final Drawable getBackground() {
        return this.background;
    }

    @Nullable
    public final Integer getRebindId() {
        return this.rebindId;
    }
}
