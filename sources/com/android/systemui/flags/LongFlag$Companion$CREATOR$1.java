package com.android.systemui.flags;

import android.os.Parcel;
import android.os.Parcelable;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;

/* compiled from: Flag.kt */
public final class LongFlag$Companion$CREATOR$1 implements Parcelable.Creator<LongFlag> {
    @NotNull
    public LongFlag createFromParcel(@NotNull Parcel parcel) {
        return new LongFlag(parcel, (DefaultConstructorMarker) null);
    }

    @NotNull
    public LongFlag[] newArray(int i) {
        return new LongFlag[i];
    }
}
