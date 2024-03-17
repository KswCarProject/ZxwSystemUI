package com.android.systemui.media;

import com.android.systemui.R$id;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: MediaData.kt */
public final class MediaButton {
    @Nullable
    public final MediaAction custom0;
    @Nullable
    public final MediaAction custom1;
    @Nullable
    public final MediaAction nextOrCustom;
    @Nullable
    public final MediaAction playOrPause;
    @Nullable
    public final MediaAction prevOrCustom;
    public final boolean reserveNext;
    public final boolean reservePrev;

    public MediaButton() {
        this((MediaAction) null, (MediaAction) null, (MediaAction) null, (MediaAction) null, (MediaAction) null, false, false, 127, (DefaultConstructorMarker) null);
    }

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MediaButton)) {
            return false;
        }
        MediaButton mediaButton = (MediaButton) obj;
        return Intrinsics.areEqual((Object) this.playOrPause, (Object) mediaButton.playOrPause) && Intrinsics.areEqual((Object) this.nextOrCustom, (Object) mediaButton.nextOrCustom) && Intrinsics.areEqual((Object) this.prevOrCustom, (Object) mediaButton.prevOrCustom) && Intrinsics.areEqual((Object) this.custom0, (Object) mediaButton.custom0) && Intrinsics.areEqual((Object) this.custom1, (Object) mediaButton.custom1) && this.reserveNext == mediaButton.reserveNext && this.reservePrev == mediaButton.reservePrev;
    }

    public int hashCode() {
        MediaAction mediaAction = this.playOrPause;
        int i = 0;
        int hashCode = (mediaAction == null ? 0 : mediaAction.hashCode()) * 31;
        MediaAction mediaAction2 = this.nextOrCustom;
        int hashCode2 = (hashCode + (mediaAction2 == null ? 0 : mediaAction2.hashCode())) * 31;
        MediaAction mediaAction3 = this.prevOrCustom;
        int hashCode3 = (hashCode2 + (mediaAction3 == null ? 0 : mediaAction3.hashCode())) * 31;
        MediaAction mediaAction4 = this.custom0;
        int hashCode4 = (hashCode3 + (mediaAction4 == null ? 0 : mediaAction4.hashCode())) * 31;
        MediaAction mediaAction5 = this.custom1;
        if (mediaAction5 != null) {
            i = mediaAction5.hashCode();
        }
        int i2 = (hashCode4 + i) * 31;
        boolean z = this.reserveNext;
        boolean z2 = true;
        if (z) {
            z = true;
        }
        int i3 = (i2 + (z ? 1 : 0)) * 31;
        boolean z3 = this.reservePrev;
        if (!z3) {
            z2 = z3;
        }
        return i3 + (z2 ? 1 : 0);
    }

    @NotNull
    public String toString() {
        return "MediaButton(playOrPause=" + this.playOrPause + ", nextOrCustom=" + this.nextOrCustom + ", prevOrCustom=" + this.prevOrCustom + ", custom0=" + this.custom0 + ", custom1=" + this.custom1 + ", reserveNext=" + this.reserveNext + ", reservePrev=" + this.reservePrev + ')';
    }

    public MediaButton(@Nullable MediaAction mediaAction, @Nullable MediaAction mediaAction2, @Nullable MediaAction mediaAction3, @Nullable MediaAction mediaAction4, @Nullable MediaAction mediaAction5, boolean z, boolean z2) {
        this.playOrPause = mediaAction;
        this.nextOrCustom = mediaAction2;
        this.prevOrCustom = mediaAction3;
        this.custom0 = mediaAction4;
        this.custom1 = mediaAction5;
        this.reserveNext = z;
        this.reservePrev = z2;
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ MediaButton(MediaAction mediaAction, MediaAction mediaAction2, MediaAction mediaAction3, MediaAction mediaAction4, MediaAction mediaAction5, boolean z, boolean z2, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? null : mediaAction, (i & 2) != 0 ? null : mediaAction2, (i & 4) != 0 ? null : mediaAction3, (i & 8) != 0 ? null : mediaAction4, (i & 16) != 0 ? null : mediaAction5, (i & 32) != 0 ? false : z, (i & 64) != 0 ? false : z2);
    }

    public final boolean getReserveNext() {
        return this.reserveNext;
    }

    public final boolean getReservePrev() {
        return this.reservePrev;
    }

    @Nullable
    public final MediaAction getActionById(int i) {
        if (i == R$id.actionPlayPause) {
            return this.playOrPause;
        }
        if (i == R$id.actionNext) {
            return this.nextOrCustom;
        }
        if (i == R$id.actionPrev) {
            return this.prevOrCustom;
        }
        if (i == R$id.action0) {
            return this.custom0;
        }
        if (i == R$id.action1) {
            return this.custom1;
        }
        return null;
    }
}
