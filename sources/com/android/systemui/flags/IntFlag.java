package com.android.systemui.flags;

import android.os.Parcel;
import android.os.Parcelable;
import com.android.systemui.flags.ParcelableFlag;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: Flag.kt */
public final class IntFlag implements ParcelableFlag<Integer> {
    @NotNull
    public static final Parcelable.Creator<IntFlag> CREATOR = new IntFlag$Companion$CREATOR$1();
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);

    /* renamed from: default  reason: not valid java name */
    public final int f3default;
    public final int id;
    public final boolean teamfood;

    public /* synthetic */ IntFlag(Parcel parcel, DefaultConstructorMarker defaultConstructorMarker) {
        this(parcel);
    }

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof IntFlag)) {
            return false;
        }
        IntFlag intFlag = (IntFlag) obj;
        return getId() == intFlag.getId() && getDefault().intValue() == intFlag.getDefault().intValue() && getTeamfood() == intFlag.getTeamfood();
    }

    public int hashCode() {
        int hashCode = ((Integer.hashCode(getId()) * 31) + getDefault().hashCode()) * 31;
        boolean teamfood2 = getTeamfood();
        if (teamfood2) {
            teamfood2 = true;
        }
        return hashCode + (teamfood2 ? 1 : 0);
    }

    @NotNull
    public String toString() {
        return "IntFlag(id=" + getId() + ", default=" + getDefault().intValue() + ", teamfood=" + getTeamfood() + ')';
    }

    public IntFlag(int i, int i2, boolean z) {
        this.id = i;
        this.f3default = i2;
        this.teamfood = z;
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ IntFlag(int i, int i2, boolean z, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this(i, (i3 & 2) != 0 ? 0 : i2, (i3 & 4) != 0 ? false : z);
    }

    public int describeContents() {
        return ParcelableFlag.DefaultImpls.describeContents(this);
    }

    public int getId() {
        return this.id;
    }

    @NotNull
    public Integer getDefault() {
        return Integer.valueOf(this.f3default);
    }

    public boolean getTeamfood() {
        return this.teamfood;
    }

    /* compiled from: Flag.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }

    public IntFlag(Parcel parcel) {
        this(parcel.readInt(), parcel.readInt(), false, 4, (DefaultConstructorMarker) null);
    }

    public void writeToParcel(@NotNull Parcel parcel, int i) {
        parcel.writeInt(getId());
        parcel.writeInt(getDefault().intValue());
    }
}
