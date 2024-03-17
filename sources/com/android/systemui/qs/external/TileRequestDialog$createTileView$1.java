package com.android.systemui.qs.external;

import com.android.systemui.qs.tileimpl.QSTileViewImpl;

/* compiled from: TileRequestDialog.kt */
public final class TileRequestDialog$createTileView$1 implements Runnable {
    public final /* synthetic */ QSTileViewImpl $tile;

    public TileRequestDialog$createTileView$1(QSTileViewImpl qSTileViewImpl) {
        this.$tile = qSTileViewImpl;
    }

    public final void run() {
        this.$tile.setStateDescription("");
        this.$tile.setClickable(false);
        this.$tile.setSelected(true);
    }
}
