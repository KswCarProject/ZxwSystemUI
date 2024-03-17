package com.android.systemui.flags;

import android.os.Parcel;
import android.os.Parcelable;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;

/* compiled from: Flag.kt */
public final class BooleanFlag$Companion$CREATOR$1 implements Parcelable.Creator<BooleanFlag> {
    @NotNull
    public BooleanFlag createFromParcel(@NotNull Parcel parcel) {
        return new BooleanFlag(parcel, (DefaultConstructorMarker) null);
    }

    @NotNull
    public BooleanFlag[] newArray(int i) {
        return new BooleanFlag[i];
    }
}
