package com.android.systemui.flags;

import android.os.Parcel;
import android.os.Parcelable;
import com.android.systemui.flags.ParcelableFlag;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: Flag.kt */
public final class FloatFlag implements ParcelableFlag<Float> {
    @NotNull
    public static final Parcelable.Creator<FloatFlag> CREATOR = new FloatFlag$Companion$CREATOR$1();
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);

    /* renamed from: default  reason: not valid java name */
    public final float f2default;
    public final int id;
    public final boolean teamfood;

    public /* synthetic */ FloatFlag(Parcel parcel, DefaultConstructorMarker defaultConstructorMarker) {
        this(parcel);
    }

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof FloatFlag)) {
            return false;
        }
        FloatFlag floatFlag = (FloatFlag) obj;
        return getId() == floatFlag.getId() && Intrinsics.areEqual((Object) getDefault(), (Object) floatFlag.getDefault()) && getTeamfood() == floatFlag.getTeamfood();
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
        return "FloatFlag(id=" + getId() + ", default=" + getDefault().floatValue() + ", teamfood=" + getTeamfood() + ')';
    }

    public FloatFlag(int i, float f, boolean z) {
        this.id = i;
        this.f2default = f;
        this.teamfood = z;
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ FloatFlag(int i, float f, boolean z, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this(i, (i2 & 2) != 0 ? 0.0f : f, (i2 & 4) != 0 ? false : z);
    }

    public int describeContents() {
        return ParcelableFlag.DefaultImpls.describeContents(this);
    }

    public int getId() {
        return this.id;
    }

    @NotNull
    public Float getDefault() {
        return Float.valueOf(this.f2default);
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

    public FloatFlag(Parcel parcel) {
        this(parcel.readInt(), parcel.readFloat(), false, 4, (DefaultConstructorMarker) null);
    }

    public void writeToParcel(@NotNull Parcel parcel, int i) {
        parcel.writeInt(getId());
        parcel.writeFloat(getDefault().floatValue());
    }
}
