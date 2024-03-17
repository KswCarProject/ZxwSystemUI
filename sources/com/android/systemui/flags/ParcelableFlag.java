package com.android.systemui.flags;

import android.os.Parcelable;
import org.jetbrains.annotations.NotNull;

/* compiled from: Flag.kt */
public interface ParcelableFlag<T> extends Flag<T>, Parcelable {

    /* compiled from: Flag.kt */
    public static final class DefaultImpls {
        public static <T> int describeContents(@NotNull ParcelableFlag<T> parcelableFlag) {
            return 0;
        }
    }
}
