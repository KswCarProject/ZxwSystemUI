package com.android.systemui.flags;

import android.util.Log;
import androidx.core.graphics.drawable.IconCompat;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: FlagSerializer.kt */
public abstract class FlagSerializer<T> {
    @NotNull
    public final Function2<JSONObject, String, T> getter;
    @NotNull
    public final Function3<JSONObject, String, T, Unit> setter;
    @NotNull
    public final String type;

    public FlagSerializer(@NotNull String str, @NotNull Function3<? super JSONObject, ? super String, ? super T, Unit> function3, @NotNull Function2<? super JSONObject, ? super String, ? extends T> function2) {
        this.type = str;
        this.setter = function3;
        this.getter = function2;
    }

    @Nullable
    public final String toSettingsData(T t) {
        try {
            JSONObject put = new JSONObject().put(IconCompat.EXTRA_TYPE, this.type);
            this.setter.invoke(put, "value", t);
            return put.toString();
        } catch (JSONException e) {
            Log.w("FlagSerializer", "write error", e);
            return null;
        }
    }

    @Nullable
    public final T fromSettingsData(@Nullable String str) {
        if (str != null) {
            if (!(str.length() == 0)) {
                try {
                    JSONObject jSONObject = new JSONObject(str);
                    if (Intrinsics.areEqual((Object) jSONObject.getString(IconCompat.EXTRA_TYPE), (Object) this.type)) {
                        return this.getter.invoke(jSONObject, "value");
                    }
                    return null;
                } catch (JSONException e) {
                    Log.w("FlagSerializer", "read error", e);
                    throw new InvalidFlagStorageException();
                }
            }
        }
        return null;
    }
}
