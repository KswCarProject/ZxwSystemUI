package com.android.systemui.qs.external;

import android.service.quicksettings.Tile;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class CustomTile$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ CustomTile f$0;
    public final /* synthetic */ Tile f$1;

    public /* synthetic */ CustomTile$$ExternalSyntheticLambda0(CustomTile customTile, Tile tile) {
        this.f$0 = customTile;
        this.f$1 = tile;
    }

    public final void run() {
        this.f$0.lambda$updateTileState$0(this.f$1);
    }
}
