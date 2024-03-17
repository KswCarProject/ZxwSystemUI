package com.android.systemui.flags;

import android.os.Parcel;
import android.os.Parcelable;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;

/* compiled from: Flag.kt */
public final class DoubleFlag$Companion$CREATOR$1 implements Parcelable.Creator<DoubleFlag> {
    @NotNull
    public DoubleFlag createFromParcel(@NotNull Parcel parcel) {
        return new DoubleFlag(parcel, (DefaultConstructorMarker) null);
    }

    @NotNull
    public DoubleFlag[] newArray(int i) {
        return new DoubleFlag[i];
    }
}
