package com.android.systemui.qs.external;

import com.android.systemui.statusbar.phone.SystemUIDialog;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: TileServiceRequestController.kt */
public final class TileServiceRequestController$requestTileAdd$1$1 extends Lambda implements Function1<String, Unit> {
    public final /* synthetic */ SystemUIDialog $dialog;
    public final /* synthetic */ String $packageName;
    public final /* synthetic */ TileServiceRequestController this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public TileServiceRequestController$requestTileAdd$1$1(String str, SystemUIDialog systemUIDialog, TileServiceRequestController tileServiceRequestController) {
        super(1);
        this.$packageName = str;
        this.$dialog = systemUIDialog;
        this.this$0 = tileServiceRequestController;
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        invoke((String) obj);
        return Unit.INSTANCE;
    }

    public final void invoke(@NotNull String str) {
        if (Intrinsics.areEqual((Object) this.$packageName, (Object) str)) {
            this.$dialog.cancel();
        }
        this.this$0.dialogCanceller = null;
    }
}
