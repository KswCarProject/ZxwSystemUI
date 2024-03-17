package com.android.systemui.controls.controller;

import android.content.ComponentName;

/* compiled from: ControlsControllerImpl.kt */
public final class ControlsControllerImpl$addFavorite$1 implements Runnable {
    public final /* synthetic */ ComponentName $componentName;
    public final /* synthetic */ ControlInfo $controlInfo;
    public final /* synthetic */ CharSequence $structureName;
    public final /* synthetic */ ControlsControllerImpl this$0;

    public ControlsControllerImpl$addFavorite$1(ComponentName componentName, CharSequence charSequence, ControlInfo controlInfo, ControlsControllerImpl controlsControllerImpl) {
        this.$componentName = componentName;
        this.$structureName = charSequence;
        this.$controlInfo = controlInfo;
        this.this$0 = controlsControllerImpl;
    }

    public final void run() {
        Favorites favorites = Favorites.INSTANCE;
        if (favorites.addFavorite(this.$componentName, this.$structureName, this.$controlInfo)) {
            this.this$0.persistenceWrapper.storeFavorites(favorites.getAllStructures());
        }
    }
}
