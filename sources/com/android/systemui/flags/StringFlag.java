package com.android.systemui.flags;

import android.os.Parcel;
import android.os.Parcelable;
import com.android.systemui.flags.ParcelableFlag;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: Flag.kt */
public final class StringFlag implements ParcelableFlag<String> {
    @NotNull
    public static final Parcelable.Creator<StringFlag> CREATOR = new StringFlag$Companion$CREATOR$1();
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull

    /* renamed from: default  reason: not valid java name */
    public final String f5default;
    public final int id;
    public final boolean teamfood;

    public /* synthetic */ StringFlag(Parcel parcel, DefaultConstructorMarker defaultConstructorMarker) {
        this(parcel);
    }

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof StringFlag)) {
            return false;
        }
        StringFlag stringFlag = (StringFlag) obj;
        return getId() == stringFlag.getId() && Intrinsics.areEqual((Object) getDefault(), (Object) stringFlag.getDefault()) && getTeamfood() == stringFlag.getTeamfood();
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
        return "StringFlag(id=" + getId() + ", default=" + getDefault() + ", teamfood=" + getTeamfood() + ')';
    }

    public StringFlag(int i, @NotNull String str, boolean z) {
        this.id = i;
        this.f5default = str;
        this.teamfood = z;
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ StringFlag(int i, String str, boolean z, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this(i, (i2 & 2) != 0 ? "" : str, (i2 & 4) != 0 ? false : z);
    }

    public int describeContents() {
        return ParcelableFlag.DefaultImpls.describeContents(this);
    }

    public int getId() {
        return this.id;
    }

    @NotNull
    public String getDefault() {
        return this.f5default;
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

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public StringFlag(android.os.Parcel r7) {
        /*
            r6 = this;
            int r1 = r7.readInt()
            java.lang.String r7 = r7.readString()
            if (r7 != 0) goto L_0x000c
            java.lang.String r7 = ""
        L_0x000c:
            r2 = r7
            r3 = 0
            r4 = 4
            r5 = 0
            r0 = r6
            r0.<init>(r1, r2, r3, r4, r5)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.flags.StringFlag.<init>(android.os.Parcel):void");
    }

    public void writeToParcel(@NotNull Parcel parcel, int i) {
        parcel.writeInt(getId());
        parcel.writeString(getDefault());
    }
}
