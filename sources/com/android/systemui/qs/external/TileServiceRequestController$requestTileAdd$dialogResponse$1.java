package com.android.systemui.qs.external;

import android.content.ComponentName;
import com.android.internal.logging.InstanceId;
import java.util.function.Consumer;

/* compiled from: TileServiceRequestController.kt */
public final class TileServiceRequestController$requestTileAdd$dialogResponse$1<T> implements Consumer {
    public final /* synthetic */ Consumer<Integer> $callback;
    public final /* synthetic */ ComponentName $componentName;
    public final /* synthetic */ InstanceId $instanceId;
    public final /* synthetic */ String $packageName;
    public final /* synthetic */ TileServiceRequestController this$0;

    public TileServiceRequestController$requestTileAdd$dialogResponse$1(TileServiceRequestController tileServiceRequestController, ComponentName componentName, String str, InstanceId instanceId, Consumer<Integer> consumer) {
        this.this$0 = tileServiceRequestController;
        this.$componentName = componentName;
        this.$packageName = str;
        this.$instanceId = instanceId;
        this.$callback = consumer;
    }

    public /* bridge */ /* synthetic */ void accept(Object obj) {
        accept(((Number) obj).intValue());
    }

    public final void accept(int i) {
        if (i == 2) {
            this.this$0.addTile(this.$componentName);
        }
        this.this$0.dialogCanceller = null;
        this.this$0.eventLogger.logUserResponse(i, this.$packageName, this.$instanceId);
        this.$callback.accept(Integer.valueOf(i));
    }
}
