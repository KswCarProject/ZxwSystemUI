package com.android.systemui.flags;

import android.os.Parcel;
import android.os.Parcelable;
import com.android.systemui.flags.ParcelableFlag;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: Flag.kt */
public final class LongFlag implements ParcelableFlag<Long> {
    @NotNull
    public static final Parcelable.Creator<LongFlag> CREATOR = new LongFlag$Companion$CREATOR$1();
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);

    /* renamed from: default  reason: not valid java name */
    public final long f4default;
    public final int id;
    public final boolean teamfood;

    public /* synthetic */ LongFlag(Parcel parcel, DefaultConstructorMarker defaultConstructorMarker) {
        this(parcel);
    }

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof LongFlag)) {
            return false;
        }
        LongFlag longFlag = (LongFlag) obj;
        return getId() == longFlag.getId() && getDefault().longValue() == longFlag.getDefault().longValue() && getTeamfood() == longFlag.getTeamfood();
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
        return "LongFlag(id=" + getId() + ", default=" + getDefault().longValue() + ", teamfood=" + getTeamfood() + ')';
    }

    public LongFlag(int i, long j, boolean z) {
        this.id = i;
        this.f4default = j;
        this.teamfood = z;
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ LongFlag(int i, long j, boolean z, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this(i, (i2 & 2) != 0 ? 0 : j, (i2 & 4) != 0 ? false : z);
    }

    public int describeContents() {
        return ParcelableFlag.DefaultImpls.describeContents(this);
    }

    public int getId() {
        return this.id;
    }

    @NotNull
    public Long getDefault() {
        return Long.valueOf(this.f4default);
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

    public LongFlag(Parcel parcel) {
        this(parcel.readInt(), parcel.readLong(), false, 4, (DefaultConstructorMarker) null);
    }

    public void writeToParcel(@NotNull Parcel parcel, int i) {
        parcel.writeInt(getId());
        parcel.writeLong(getDefault().longValue());
    }
}
