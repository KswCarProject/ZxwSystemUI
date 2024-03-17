package com.android.systemui.qs.external;

import com.android.systemui.qs.external.TileServiceRequestController;
import com.android.systemui.statusbar.commandline.Command;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: TileServiceRequestController.kt */
public final class TileServiceRequestController$init$1 extends Lambda implements Function0<Command> {
    public final /* synthetic */ TileServiceRequestController this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public TileServiceRequestController$init$1(TileServiceRequestController tileServiceRequestController) {
        super(0);
        this.this$0 = tileServiceRequestController;
    }

    @NotNull
    public final Command invoke() {
        return new TileServiceRequestController.TileServiceRequestCommand();
    }
}
