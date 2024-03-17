package com.android.systemui.qs.external;

import android.content.ComponentName;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: CustomTileStatePersister.kt */
public final class TileServiceKey {
    @NotNull
    public final ComponentName componentName;
    @NotNull
    public final String string;
    public final int user;

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof TileServiceKey)) {
            return false;
        }
        TileServiceKey tileServiceKey = (TileServiceKey) obj;
        return Intrinsics.areEqual((Object) this.componentName, (Object) tileServiceKey.componentName) && this.user == tileServiceKey.user;
    }

    public int hashCode() {
        return (this.componentName.hashCode() * 31) + Integer.hashCode(this.user);
    }

    public TileServiceKey(@NotNull ComponentName componentName2, int i) {
        this.componentName = componentName2;
        this.user = i;
        this.string = componentName2.flattenToString() + ':' + i;
    }

    @NotNull
    public String toString() {
        return this.string;
    }
}
