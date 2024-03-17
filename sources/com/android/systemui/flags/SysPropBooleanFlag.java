package com.android.systemui.flags;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: Flag.kt */
public final class SysPropBooleanFlag implements SysPropFlag<Boolean> {

    /* renamed from: default  reason: not valid java name */
    public final boolean f6default;
    public final int id;
    @NotNull
    public final String name;
    public final boolean teamfood;

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SysPropBooleanFlag)) {
            return false;
        }
        SysPropBooleanFlag sysPropBooleanFlag = (SysPropBooleanFlag) obj;
        return getId() == sysPropBooleanFlag.getId() && Intrinsics.areEqual((Object) getName(), (Object) sysPropBooleanFlag.getName()) && getDefault().booleanValue() == sysPropBooleanFlag.getDefault().booleanValue();
    }

    public int hashCode() {
        return (((Integer.hashCode(getId()) * 31) + getName().hashCode()) * 31) + getDefault().hashCode();
    }

    @NotNull
    public String toString() {
        return "SysPropBooleanFlag(id=" + getId() + ", name=" + getName() + ", default=" + getDefault().booleanValue() + ')';
    }

    public SysPropBooleanFlag(int i, @NotNull String str, boolean z) {
        this.id = i;
        this.name = str;
        this.f6default = z;
    }

    public int getId() {
        return this.id;
    }

    @NotNull
    public String getName() {
        return this.name;
    }

    @NotNull
    public Boolean getDefault() {
        return Boolean.valueOf(this.f6default);
    }

    public boolean getTeamfood() {
        return this.teamfood;
    }
}
