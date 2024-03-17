package com.android.systemui.qs.external;

import android.content.DialogInterface;
import com.android.systemui.qs.external.TileServiceRequestController;

/* compiled from: TileServiceRequestController.kt */
public final class TileServiceRequestController$createDialog$dialogClickListener$1 implements DialogInterface.OnClickListener {
    public final /* synthetic */ TileServiceRequestController.SingleShotConsumer<Integer> $responseHandler;

    public TileServiceRequestController$createDialog$dialogClickListener$1(TileServiceRequestController.SingleShotConsumer<Integer> singleShotConsumer) {
        this.$responseHandler = singleShotConsumer;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        if (i == -1) {
            this.$responseHandler.accept(2);
        } else {
            this.$responseHandler.accept(0);
        }
    }
}
