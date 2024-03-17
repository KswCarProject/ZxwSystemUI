package com.android.systemui.controls.controller;

import com.android.systemui.controls.ControlStatus;
import com.android.systemui.controls.controller.ControlsController;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/* compiled from: ControlsController.kt */
public final class ControlsControllerKt$createLoadDataObject$1 implements ControlsController.LoadData {
    public final /* synthetic */ List<ControlStatus> $allControls;
    public final /* synthetic */ boolean $error;
    public final /* synthetic */ List<String> $favorites;
    @NotNull
    public final List<ControlStatus> allControls;
    public final boolean errorOnLoad;
    @NotNull
    public final List<String> favoritesIds;

    public ControlsControllerKt$createLoadDataObject$1(List<ControlStatus> list, List<String> list2, boolean z) {
        this.$allControls = list;
        this.$favorites = list2;
        this.$error = z;
        this.allControls = list;
        this.favoritesIds = list2;
        this.errorOnLoad = z;
    }

    @NotNull
    public List<ControlStatus> getAllControls() {
        return this.allControls;
    }

    @NotNull
    public List<String> getFavoritesIds() {
        return this.favoritesIds;
    }

    public boolean getErrorOnLoad() {
        return this.errorOnLoad;
    }
}
