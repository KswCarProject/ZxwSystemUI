package com.android.systemui.flags;

import android.os.Parcel;
import android.os.Parcelable;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;

/* compiled from: Flag.kt */
public final class IntFlag$Companion$CREATOR$1 implements Parcelable.Creator<IntFlag> {
    @NotNull
    public IntFlag createFromParcel(@NotNull Parcel parcel) {
        return new IntFlag(parcel, (DefaultConstructorMarker) null);
    }

    @NotNull
    public IntFlag[] newArray(int i) {
        return new IntFlag[i];
    }
}
