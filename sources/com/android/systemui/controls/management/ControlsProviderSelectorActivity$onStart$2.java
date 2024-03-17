package com.android.systemui.controls.management;

import android.content.ComponentName;
import com.android.systemui.controls.controller.ControlsController;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReferenceImpl;
import org.jetbrains.annotations.NotNull;

/* compiled from: ControlsProviderSelectorActivity.kt */
public /* synthetic */ class ControlsProviderSelectorActivity$onStart$2 extends FunctionReferenceImpl implements Function1<ComponentName, Integer> {
    public ControlsProviderSelectorActivity$onStart$2(Object obj) {
        super(1, obj, ControlsController.class, "countFavoritesForComponent", "countFavoritesForComponent(Landroid/content/ComponentName;)I", 0);
    }

    @NotNull
    public final Integer invoke(@NotNull ComponentName componentName) {
        return Integer.valueOf(((ControlsController) this.receiver).countFavoritesForComponent(componentName));
    }
}
