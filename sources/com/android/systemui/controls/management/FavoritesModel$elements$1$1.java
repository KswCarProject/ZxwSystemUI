package com.android.systemui.controls.management;

import android.content.ComponentName;
import android.graphics.drawable.Icon;
import com.android.systemui.controls.CustomIconCache;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.FunctionReferenceImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: FavoritesModel.kt */
public /* synthetic */ class FavoritesModel$elements$1$1 extends FunctionReferenceImpl implements Function2<ComponentName, String, Icon> {
    public FavoritesModel$elements$1$1(Object obj) {
        super(2, obj, CustomIconCache.class, "retrieve", "retrieve(Landroid/content/ComponentName;Ljava/lang/String;)Landroid/graphics/drawable/Icon;", 0);
    }

    @Nullable
    public final Icon invoke(@NotNull ComponentName componentName, @NotNull String str) {
        return ((CustomIconCache) this.receiver).retrieve(componentName, str);
    }
}
