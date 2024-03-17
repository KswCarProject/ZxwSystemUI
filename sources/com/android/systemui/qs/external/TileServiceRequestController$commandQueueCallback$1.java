package com.android.systemui.qs.external;

import android.content.ComponentName;
import android.graphics.drawable.Icon;
import com.android.internal.statusbar.IAddTileResultCallback;
import com.android.systemui.statusbar.CommandQueue;
import kotlin.jvm.functions.Function1;
import org.jetbrains.annotations.NotNull;

/* compiled from: TileServiceRequestController.kt */
public final class TileServiceRequestController$commandQueueCallback$1 implements CommandQueue.Callbacks {
    public final /* synthetic */ TileServiceRequestController this$0;

    public TileServiceRequestController$commandQueueCallback$1(TileServiceRequestController tileServiceRequestController) {
        this.this$0 = tileServiceRequestController;
    }

    public void requestAddTile(@NotNull ComponentName componentName, @NotNull CharSequence charSequence, @NotNull CharSequence charSequence2, @NotNull Icon icon, @NotNull IAddTileResultCallback iAddTileResultCallback) {
        this.this$0.requestTileAdd$frameworks__base__packages__SystemUI__android_common__SystemUI_core(componentName, charSequence, charSequence2, icon, new TileServiceRequestController$commandQueueCallback$1$requestAddTile$1(iAddTileResultCallback));
    }

    public void cancelRequestAddTile(@NotNull String str) {
        Function1 access$getDialogCanceller$p = this.this$0.dialogCanceller;
        if (access$getDialogCanceller$p != null) {
            access$getDialogCanceller$p.invoke(str);
        }
    }
}
