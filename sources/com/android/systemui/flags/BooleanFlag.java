package com.android.systemui.flags;

import android.os.Parcel;
import android.os.Parcelable;
import com.android.systemui.flags.ParcelableFlag;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: Flag.kt */
public final class BooleanFlag implements ParcelableFlag<Boolean> {
    @NotNull
    public static final Parcelable.Creator<BooleanFlag> CREATOR = new BooleanFlag$Companion$CREATOR$1();
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);

    /* renamed from: default  reason: not valid java name */
    public final boolean f0default;
    public final int id;
    public final boolean teamfood;

    public BooleanFlag(int i, boolean z) {
        this(i, z, false, 4, (DefaultConstructorMarker) null);
    }

    public /* synthetic */ BooleanFlag(Parcel parcel, DefaultConstructorMarker defaultConstructorMarker) {
        this(parcel);
    }

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof BooleanFlag)) {
            return false;
        }
        BooleanFlag booleanFlag = (BooleanFlag) obj;
        return getId() == booleanFlag.getId() && getDefault().booleanValue() == booleanFlag.getDefault().booleanValue() && getTeamfood() == booleanFlag.getTeamfood();
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
        return "BooleanFlag(id=" + getId() + ", default=" + getDefault().booleanValue() + ", teamfood=" + getTeamfood() + ')';
    }

    public BooleanFlag(int i, boolean z, boolean z2) {
        this.id = i;
        this.f0default = z;
        this.teamfood = z2;
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ BooleanFlag(int i, boolean z, boolean z2, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this(i, (i2 & 2) != 0 ? false : z, (i2 & 4) != 0 ? false : z2);
    }

    public int describeContents() {
        return ParcelableFlag.DefaultImpls.describeContents(this);
    }

    public int getId() {
        return this.id;
    }

    @NotNull
    public Boolean getDefault() {
        return Boolean.valueOf(this.f0default);
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

    public BooleanFlag(Parcel parcel) {
        this(parcel.readInt(), parcel.readBoolean(), false, 4, (DefaultConstructorMarker) null);
    }

    public void writeToParcel(@NotNull Parcel parcel, int i) {
        parcel.writeInt(getId());
        parcel.writeBoolean(getDefault().booleanValue());
    }
}
