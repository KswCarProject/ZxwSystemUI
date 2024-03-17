package com.android.systemui.controls.management;

import android.content.ComponentName;
import android.content.res.Resources;
import com.android.systemui.R$plurals;
import kotlin.jvm.functions.Function1;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: AppAdapter.kt */
public final class FavoritesRenderer {
    @NotNull
    public final Function1<ComponentName, Integer> favoriteFunction;
    @NotNull
    public final Resources resources;

    public FavoritesRenderer(@NotNull Resources resources2, @NotNull Function1<? super ComponentName, Integer> function1) {
        this.resources = resources2;
        this.favoriteFunction = function1;
    }

    @Nullable
    public final String renderFavoritesForComponent(@NotNull ComponentName componentName) {
        int intValue = this.favoriteFunction.invoke(componentName).intValue();
        if (intValue == 0) {
            return null;
        }
        return this.resources.getQuantityString(R$plurals.controls_number_of_favorites, intValue, new Object[]{Integer.valueOf(intValue)});
    }
}
