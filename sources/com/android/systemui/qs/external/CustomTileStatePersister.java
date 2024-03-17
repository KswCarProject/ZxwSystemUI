package com.android.systemui.qs.external;

import android.content.Context;
import android.content.SharedPreferences;
import android.service.quicksettings.Tile;
import android.util.Log;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;

/* compiled from: CustomTileStatePersister.kt */
public final class CustomTileStatePersister {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    public final SharedPreferences sharedPreferences;

    /* compiled from: CustomTileStatePersister.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }

    public CustomTileStatePersister(@NotNull Context context) {
        this.sharedPreferences = context.getSharedPreferences("custom_tiles_state", 0);
    }

    @Nullable
    public final Tile readState(@NotNull TileServiceKey tileServiceKey) {
        String string = this.sharedPreferences.getString(tileServiceKey.toString(), (String) null);
        if (string == null) {
            return null;
        }
        try {
            return CustomTileStatePersisterKt.readTileFromString(string);
        } catch (JSONException e) {
            Log.e("TileServicePersistence", Intrinsics.stringPlus("Bad saved state: ", string), e);
            return null;
        }
    }

    public final void persistState(@NotNull TileServiceKey tileServiceKey, @NotNull Tile tile) {
        this.sharedPreferences.edit().putString(tileServiceKey.toString(), CustomTileStatePersisterKt.writeToString(tile)).apply();
    }

    public final void removeState(@NotNull TileServiceKey tileServiceKey) {
        this.sharedPreferences.edit().remove(tileServiceKey.toString()).apply();
    }
}
