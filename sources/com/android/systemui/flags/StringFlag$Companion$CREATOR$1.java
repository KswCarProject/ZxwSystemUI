package com.android.systemui.flags;

import android.os.Parcel;
import android.os.Parcelable;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;

/* compiled from: Flag.kt */
public final class StringFlag$Companion$CREATOR$1 implements Parcelable.Creator<StringFlag> {
    @NotNull
    public StringFlag createFromParcel(@NotNull Parcel parcel) {
        return new StringFlag(parcel, (DefaultConstructorMarker) null);
    }

    @NotNull
    public StringFlag[] newArray(int i) {
        return new StringFlag[i];
    }
}
