package com.android.systemui.qs.external;

import android.service.quicksettings.Tile;
import com.android.internal.annotations.VisibleForTesting;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

/* compiled from: CustomTileStatePersister.kt */
public final class CustomTileStatePersisterKt {
    @NotNull
    @VisibleForTesting
    public static final Tile readTileFromString(@NotNull String str) {
        JSONObject jSONObject = new JSONObject(str);
        Tile tile = new Tile();
        tile.setState(jSONObject.getInt("state"));
        tile.setLabel(getStringOrNull(jSONObject, "label"));
        tile.setSubtitle(getStringOrNull(jSONObject, "subtitle"));
        tile.setContentDescription(getStringOrNull(jSONObject, "content_description"));
        tile.setStateDescription(getStringOrNull(jSONObject, "state_description"));
        return tile;
    }

    public static final String getStringOrNull(JSONObject jSONObject, String str) {
        if (jSONObject.has(str)) {
            return jSONObject.getString(str);
        }
        return null;
    }

    @NotNull
    @VisibleForTesting
    public static final String writeToString(@NotNull Tile tile) {
        return new JSONObject().put("state", tile.getState()).put("label", tile.getLabel()).put("subtitle", tile.getSubtitle()).put("content_description", tile.getContentDescription()).put("state_description", tile.getStateDescription()).toString();
    }
}
