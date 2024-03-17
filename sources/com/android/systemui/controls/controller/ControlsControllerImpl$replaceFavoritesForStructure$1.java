package com.android.systemui.controls.controller;

/* compiled from: ControlsControllerImpl.kt */
public final class ControlsControllerImpl$replaceFavoritesForStructure$1 implements Runnable {
    public final /* synthetic */ StructureInfo $structureInfo;
    public final /* synthetic */ ControlsControllerImpl this$0;

    public ControlsControllerImpl$replaceFavoritesForStructure$1(StructureInfo structureInfo, ControlsControllerImpl controlsControllerImpl) {
        this.$structureInfo = structureInfo;
        this.this$0 = controlsControllerImpl;
    }

    public final void run() {
        Favorites favorites = Favorites.INSTANCE;
        favorites.replaceControls(this.$structureInfo);
        this.this$0.persistenceWrapper.storeFavorites(favorites.getAllStructures());
    }
}
