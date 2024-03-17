package com.android.systemui.flags;

import android.os.Parcel;
import android.os.Parcelable;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;

/* compiled from: Flag.kt */
public final class FloatFlag$Companion$CREATOR$1 implements Parcelable.Creator<FloatFlag> {
    @NotNull
    public FloatFlag createFromParcel(@NotNull Parcel parcel) {
        return new FloatFlag(parcel, (DefaultConstructorMarker) null);
    }

    @NotNull
    public FloatFlag[] newArray(int i) {
        return new FloatFlag[i];
    }
}
