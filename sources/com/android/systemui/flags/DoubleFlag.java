package com.android.systemui.flags;

import android.os.Parcel;
import android.os.Parcelable;
import com.android.systemui.flags.ParcelableFlag;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: Flag.kt */
public final class DoubleFlag implements ParcelableFlag<Double> {
    @NotNull
    public static final Parcelable.Creator<DoubleFlag> CREATOR = new DoubleFlag$Companion$CREATOR$1();
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);

    /* renamed from: default  reason: not valid java name */
    public final double f1default;
    public final int id;
    public final boolean teamfood;

    public /* synthetic */ DoubleFlag(Parcel parcel, DefaultConstructorMarker defaultConstructorMarker) {
        this(parcel);
    }

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DoubleFlag)) {
            return false;
        }
        DoubleFlag doubleFlag = (DoubleFlag) obj;
        return getId() == doubleFlag.getId() && Intrinsics.areEqual((Object) getDefault(), (Object) doubleFlag.getDefault()) && getTeamfood() == doubleFlag.getTeamfood();
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
        return "DoubleFlag(id=" + getId() + ", default=" + getDefault().doubleValue() + ", teamfood=" + getTeamfood() + ')';
    }

    public DoubleFlag(int i, double d, boolean z) {
        this.id = i;
        this.f1default = d;
        this.teamfood = z;
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ DoubleFlag(int i, double d, boolean z, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this(i, (i2 & 2) != 0 ? 0.0d : d, (i2 & 4) != 0 ? false : z);
    }

    public int describeContents() {
        return ParcelableFlag.DefaultImpls.describeContents(this);
    }

    public int getId() {
        return this.id;
    }

    @NotNull
    public Double getDefault() {
        return Double.valueOf(this.f1default);
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

    public DoubleFlag(Parcel parcel) {
        this(parcel.readInt(), parcel.readDouble(), false, 4, (DefaultConstructorMarker) null);
    }

    public void writeToParcel(@NotNull Parcel parcel, int i) {
        parcel.writeInt(getId());
        parcel.writeDouble(getDefault().doubleValue());
    }
}
