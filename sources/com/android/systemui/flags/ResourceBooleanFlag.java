package com.android.systemui.flags;

import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: Flag.kt */
public final class ResourceBooleanFlag implements Flag {
    public final int id;
    public final int resourceId;
    public final boolean teamfood;

    public ResourceBooleanFlag(int i, int i2) {
        this(i, i2, false, 4, (DefaultConstructorMarker) null);
    }

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ResourceBooleanFlag)) {
            return false;
        }
        ResourceBooleanFlag resourceBooleanFlag = (ResourceBooleanFlag) obj;
        return getId() == resourceBooleanFlag.getId() && getResourceId() == resourceBooleanFlag.getResourceId() && getTeamfood() == resourceBooleanFlag.getTeamfood();
    }

    public int hashCode() {
        int hashCode = ((Integer.hashCode(getId()) * 31) + Integer.hashCode(getResourceId())) * 31;
        boolean teamfood2 = getTeamfood();
        if (teamfood2) {
            teamfood2 = true;
        }
        return hashCode + (teamfood2 ? 1 : 0);
    }

    @NotNull
    public String toString() {
        return "ResourceBooleanFlag(id=" + getId() + ", resourceId=" + getResourceId() + ", teamfood=" + getTeamfood() + ')';
    }

    public ResourceBooleanFlag(int i, int i2, boolean z) {
        this.id = i;
        this.resourceId = i2;
        this.teamfood = z;
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ ResourceBooleanFlag(int i, int i2, boolean z, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this(i, i2, (i3 & 4) != 0 ? false : z);
    }

    public int getId() {
        return this.id;
    }

    public int getResourceId() {
        return this.resourceId;
    }

    public boolean getTeamfood() {
        return this.teamfood;
    }
}
