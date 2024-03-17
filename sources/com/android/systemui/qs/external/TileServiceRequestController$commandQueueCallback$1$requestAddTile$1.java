package com.android.systemui.qs.external;

import android.os.RemoteException;
import android.util.Log;
import com.android.internal.statusbar.IAddTileResultCallback;
import java.util.function.Consumer;

/* compiled from: TileServiceRequestController.kt */
public final class TileServiceRequestController$commandQueueCallback$1$requestAddTile$1<T> implements Consumer {
    public final /* synthetic */ IAddTileResultCallback $callback;

    public TileServiceRequestController$commandQueueCallback$1$requestAddTile$1(IAddTileResultCallback iAddTileResultCallback) {
        this.$callback = iAddTileResultCallback;
    }

    public /* bridge */ /* synthetic */ void accept(Object obj) {
        accept(((Number) obj).intValue());
    }

    public final void accept(int i) {
        try {
            this.$callback.onTileRequest(i);
        } catch (RemoteException e) {
            Log.e("TileServiceRequestController", "Couldn't respond to request", e);
        }
    }
}
